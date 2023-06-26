package com.example.springheima.beanpostprocessor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanExpressionContext;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Configuration
public class Main1 {
    public static void main(String[] args) throws Exception{
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Main1.class);
        DefaultListableBeanFactory beanFactory = applicationContext.getDefaultListableBeanFactory();

        ContextAnnotationAutowireCandidateResolver resolver = new ContextAnnotationAutowireCandidateResolver();
        resolver.setBeanFactory(beanFactory);

        // 获取@Value的内容

        Field home = Bean1.class.getDeclaredField("home");
        DependencyDescriptor dependencyDescriptor = new DependencyDescriptor(home,false);
        System.out.println(resolver.getSuggestedValue(dependencyDescriptor));// ${JAVA_HOME}
        System.out.println(applicationContext.getEnvironment().resolvePlaceholders(resolver.getSuggestedValue(dependencyDescriptor).toString()));// C:\Program Files\Java\jdk1.8.0_271

        Field age = Bean1.class.getDeclaredField("age");
        DependencyDescriptor dependencyDescriptor1 = new DependencyDescriptor(age,false);
        System.out.println(resolver.getSuggestedValue(dependencyDescriptor1));// 18
        System.out.println(resolver.getSuggestedValue(dependencyDescriptor1).getClass());//String
        System.out.println(beanFactory.getTypeConverter().convertIfNecessary(
                resolver.getSuggestedValue(dependencyDescriptor1),
                dependencyDescriptor1.getDependencyType())
        );
        System.out.println(beanFactory.getTypeConverter().convertIfNecessary(
                resolver.getSuggestedValue(dependencyDescriptor1),
                dependencyDescriptor1.getDependencyType()
        ).getClass());// Integer


        Field bean3 = Bean2.class.getDeclaredField("bean3");
        DependencyDescriptor dependencyDescriptor2 = new DependencyDescriptor(bean3, false);
        System.out.println(resolver.getSuggestedValue(dependencyDescriptor2));// #{@bean3}
        // 解析SpEL
        Object o = beanFactory.getBeanExpressionResolver().evaluate(resolver.getSuggestedValue(dependencyDescriptor2).toString(), new BeanExpressionContext(beanFactory, null));
        System.out.println(o);// com.example.springheima.beanpostprocessor.Main1$Bean3@647e447


        Field value = Bean4.class.getDeclaredField("value");
        DependencyDescriptor dependencyDescriptor3 = new DependencyDescriptor(value, false);
        System.out.println(resolver.getSuggestedValue(dependencyDescriptor3));//#{'hello'+ '${JAVA_HOME}'}
        System.out.println(applicationContext.getEnvironment().resolvePlaceholders(resolver.getSuggestedValue(dependencyDescriptor3).toString())); // #{'hello'+ 'C:\Program Files\Java\jdk1.8.0_271'}
        System.out.println(beanFactory.getBeanExpressionResolver().evaluate(applicationContext.getEnvironment().resolvePlaceholders(resolver.getSuggestedValue(dependencyDescriptor3).toString()), new BeanExpressionContext(beanFactory, null)));// helloC:\Program Files\Java\jdk1.8.0_271
    }

    public class Bean1{
        @Value("${JAVA_HOME}")
        private String home;
        @Value("18")
        private int age;
    }

    public class Bean2{
        // SpEL
        @Value("#{@bean3}")
        private Bean3 bean3;
    }

    @Component("bean3")
    public class Bean3{}

    public class Bean4{
        @Value("#{'hello'+ '${JAVA_HOME}'}")
        private String value;
    }
}
