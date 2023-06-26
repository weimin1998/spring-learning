package com.example.springheima.beanpostprocessor;

import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.boot.context.properties.ConfigurationPropertiesBindingPostProcessor;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;
import org.springframework.context.support.GenericApplicationContext;

public class Main {
    public static void main(String[] args) {
        // GenericApplicationContext是一个【干净】的容器
        GenericApplicationContext context = new GenericApplicationContext();

        context.registerBean("bean1",Bean1.class);
        context.registerBean("bean2",Bean2.class);
        context.registerBean("bean3",Bean3.class);
        context.registerBean("bean4",Bean4.class);


        // ContextAnnotationAutowireCandidateResolver获取@Value中的值
        // No qualifying bean of type 'java.lang.String' available: expected at least 1 bean which qualifies as autowire candidate. Dependency annotations: {@org.springframework.beans.factory.annotation.Value(value=${JAVA_HOME})}
        context.getDefaultListableBeanFactory().setAutowireCandidateResolver(new ContextAnnotationAutowireCandidateResolver());

        // 解析@Autowired, @Value
        context.registerBean(AutowiredAnnotationBeanPostProcessor.class);

        // 解析@Resource, @PostConstruct, @PreDestroy
        context.registerBean(CommonAnnotationBeanPostProcessor.class);

        // 解析@ConfigurationProperties
        ConfigurationPropertiesBindingPostProcessor.register(context.getDefaultListableBeanFactory());


        context.refresh();

        System.out.println(context.getBean("bean4"));
        context.close();

    }
}
