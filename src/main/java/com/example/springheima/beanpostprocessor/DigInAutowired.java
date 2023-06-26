package com.example.springheima.beanpostprocessor;

import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.annotation.InjectionMetadata;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;
import org.springframework.core.MethodParameter;
import org.springframework.core.env.StandardEnvironment;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class DigInAutowired {
    public static void main(String[] args) throws Throwable {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        beanFactory.registerSingleton("bean2", new Bean2());// 这里直接是成品的bean了
        beanFactory.registerSingleton("bean3", new Bean3());

        beanFactory.setAutowireCandidateResolver(new ContextAnnotationAutowireCandidateResolver());// @Value
        // 解析${}
        beanFactory.addEmbeddedValueResolver(new StandardEnvironment()::resolvePlaceholders);
//
//        AutowiredAnnotationBeanPostProcessor postProcessor = new AutowiredAnnotationBeanPostProcessor();
//        postProcessor.setBeanFactory(beanFactory);
//
//        Bean1 bean1 = new Bean1();
//        System.out.println("@Autowired生效之前： " + bean1);

        // 解析@Autowired, @Value
        // 1.找到bean1中，哪些属性，哪些方法上加了@Autowired，将这些信息封装成InjectionMetadata；
        // 2.利用反射给这些属性赋值；
        // postProcessor.postProcessProperties(null, bean1, "bean1");

        //System.out.println("@Autowired生效之后： " + bean1);

//        Method findAutowiringMetadata = AutowiredAnnotationBeanPostProcessor.class.getDeclaredMethod("findAutowiringMetadata", String.class, Class.class, PropertyValues.class);
//        findAutowiringMetadata.setAccessible(true);
//        InjectionMetadata injectionMetadata = (InjectionMetadata) findAutowiringMetadata.invoke(postProcessor, "bean", Bean1.class, null);
//
//        System.out.println(injectionMetadata);
//
//        injectionMetadata.inject(bean1, "bean1", null);
//
//        System.out.println("@Autowired生效之后： " + bean1);


        // 如何根据类型找到
        Field bean3 = Bean1.class.getDeclaredField("bean3");
        DependencyDescriptor dependencyDescriptor = new DependencyDescriptor(bean3, false);
        Object o = beanFactory.doResolveDependency(dependencyDescriptor, null, null, null);
        System.out.println(o);

        Method setBean2 = Bean1.class.getDeclaredMethod("setBean2", Bean2.class);
        DependencyDescriptor dependencyDescriptor1 = new DependencyDescriptor(new MethodParameter(setBean2, 0), false);
        Object o1 = beanFactory.doResolveDependency(dependencyDescriptor1, null, null, null);
        System.out.println(o1);

        Method setHome = Bean1.class.getDeclaredMethod("setHome", String.class);
        DependencyDescriptor dependencyDescriptor2 = new DependencyDescriptor(new MethodParameter(setHome, 0), false);
        Object o2 = beanFactory.doResolveDependency(dependencyDescriptor2, null, null, null);
        System.out.println(o2);

    }
}
