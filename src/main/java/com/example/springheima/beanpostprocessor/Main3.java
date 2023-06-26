package com.example.springheima.beanpostprocessor;

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Configuration
public class Main3 {

    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Main3.class);
        DefaultListableBeanFactory beanFactory = applicationContext.getDefaultListableBeanFactory();
        //testArray(beanFactory);
        //testList(beanFactory);
        //testApplicationContext(beanFactory);
        //testGeneric(beanFactory);
        testQualifier(beanFactory);
    }

    private static void testQualifier(DefaultListableBeanFactory beanFactory) throws NoSuchFieldException{
        ContextAnnotationAutowireCandidateResolver resolver = new ContextAnnotationAutowireCandidateResolver();
        DependencyDescriptor dependencyDescriptor = new DependencyDescriptor(Target.class.getDeclaredField("service"), false);

        Class<?> dependencyType = dependencyDescriptor.getDependencyType();
        String[] names = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(beanFactory, dependencyType);

        for (String name : names) {
            BeanDefinition beanDefinition = beanFactory.getMergedBeanDefinition(name);

            // @Qualifier 中的name对比
            if (resolver.isAutowireCandidate(new BeanDefinitionHolder(beanDefinition, name), dependencyDescriptor)) {
                System.out.println(name);
                Object o = dependencyDescriptor.resolveCandidate(name, dependencyType, beanFactory);
                System.out.println(o);
            }
        }

    }

    private static void testGeneric(DefaultListableBeanFactory beanFactory) throws NoSuchFieldException {
        ContextAnnotationAutowireCandidateResolver resolver = new ContextAnnotationAutowireCandidateResolver();
        DependencyDescriptor dependencyDescriptor = new DependencyDescriptor(Target.class.getDeclaredField("dao"), false);

        Class<?> dependencyType = dependencyDescriptor.getDependencyType();

        String[] names = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(beanFactory, dependencyType);
        for (String name : names) {
            BeanDefinition beanDefinition = beanFactory.getMergedBeanDefinition(name);

            // 泛型对比
            if (resolver.isAutowireCandidate(new BeanDefinitionHolder(beanDefinition, name), dependencyDescriptor)) {
                System.out.println(name);
                Object o = dependencyDescriptor.resolveCandidate(name, dependencyType, beanFactory);
                System.out.println(o);
            }
        }
    }

    private static void testApplicationContext(DefaultListableBeanFactory beanFactory) throws Exception {
        DependencyDescriptor dependencyDescriptor = new DependencyDescriptor(Target.class.getDeclaredField("applicationContext"), false);

        Field resolvableDependencies = DefaultListableBeanFactory.class.getDeclaredField("resolvableDependencies");
        resolvableDependencies.setAccessible(true);

        Map<Class<?>, Object> dependencies = (Map<Class<?>, Object>) resolvableDependencies.get(beanFactory);

        for (Map.Entry<Class<?>, Object> entry : dependencies.entrySet()) {
            if (entry.getKey().isAssignableFrom(dependencyDescriptor.getDependencyType())) {
                System.out.println(entry.getValue());
                break;
            }
        }
    }

    private static void testList(DefaultListableBeanFactory beanFactory) throws NoSuchFieldException {
        DependencyDescriptor dependencyDescriptor = new DependencyDescriptor(Target.class.getDeclaredField("serviceList"), true);
        if (dependencyDescriptor.getDependencyType() == List.class) {
            Class<?> clazz = dependencyDescriptor.getResolvableType().getGeneric().resolve();
            System.out.println(clazz);//interface com.example.springheima.beanpostprocessor.Main3$Service

            List<Object> beans = new ArrayList<>();
            String[] names = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(beanFactory, clazz);
            for (String name : names) {
                System.out.println(name);

                Object o = dependencyDescriptor.resolveCandidate(name, clazz, beanFactory);
                beans.add(o);
            }
            System.out.println(beans);
        }
    }

    private static void testArray(DefaultListableBeanFactory beanFactory) throws NoSuchFieldException {
        DependencyDescriptor dependencyDescriptor = new DependencyDescriptor(Target.class.getDeclaredField("serviceArray"), true);
        if (dependencyDescriptor.getDependencyType().isArray()) {
            // 数组中元素的类型
            Class<?> componentType = dependencyDescriptor.getDependencyType().getComponentType();
            // 查找当前容器以及所有祖先容器中所有该类型的beanName
            String[] names = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(beanFactory, componentType);
            List<Object> beans = new ArrayList<>();
            for (String name : names) {
                System.out.println(name);
                Object o = dependencyDescriptor.resolveCandidate(name, componentType, beanFactory);

                beans.add(o);
            }
            Object array = beanFactory.getTypeConverter().convertIfNecessary(beans, dependencyDescriptor.getDependencyType());
            System.out.println(array);
        }
    }


    interface Service {
    }

    @Component("service1")
    static class Service1 implements Service {
    }

    @Component("service2")
    static class Service2 implements Service {
    }

    @Component("service3")
    static class Service3 implements Service {
    }

    interface Dao<T> {
    }

    static class Student {
    }

    static class Teacher {
    }

    @Component
    static class Dao1 implements Dao<Student> {
    }

    @Component
    static class Dao2 implements Dao<Teacher> {
    }

    static class Target {
        @Autowired
        private Service[] serviceArray;
        @Autowired
        private List<Service> serviceList;
        @Autowired
        private ConfigurableApplicationContext applicationContext;
        @Autowired
        private Dao<Teacher> dao;
        @Autowired
        @Qualifier("service2")
        private Service service;
    }
}
