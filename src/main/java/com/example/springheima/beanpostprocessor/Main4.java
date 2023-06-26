package com.example.springheima.beanpostprocessor;

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Configuration
public class Main4 {
    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Main4.class);

        DefaultListableBeanFactory beanFactory = applicationContext.getDefaultListableBeanFactory();

        testPrimary(beanFactory);
    }


    private static void testPrimary(DefaultListableBeanFactory beanFactory) throws NoSuchFieldException{
        //ContextAnnotationAutowireCandidateResolver resolver = new ContextAnnotationAutowireCandidateResolver();
        DependencyDescriptor dependencyDescriptor = new DependencyDescriptor(Target1.class.getDeclaredField("service"), false);

        Class<?> dependencyType = dependencyDescriptor.getDependencyType();
        String[] names = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(beanFactory, dependencyType);
        for (String name : names) {
            if (beanFactory.getMergedBeanDefinition(name).isPrimary()) {
                System.out.println(name);
            }
        }
    }

    private static void testFieldName(DefaultListableBeanFactory beanFactory) throws NoSuchFieldException{
        //ContextAnnotationAutowireCandidateResolver resolver = new ContextAnnotationAutowireCandidateResolver();
        DependencyDescriptor dependencyDescriptor = new DependencyDescriptor(Target2.class.getDeclaredField("service1"), false);

        Class<?> dependencyType = dependencyDescriptor.getDependencyType();
        String[] names = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(beanFactory, dependencyType);
        for (String name : names) {
            if(name.equals(dependencyDescriptor.getDependencyName())){
                System.out.println(name);
            }
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
    @Primary
    static class Service3 implements Service {
    }

    static class Target1{
        @Autowired
        private Service service;
    }

    static class Target2{
        @Autowired
        private Service service1;
    }
}
