package com.example.springheima.beanFactoryPostProcessor;

import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.*;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.MethodMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws IOException {
        GenericApplicationContext context = new GenericApplicationContext();
        context.registerBean("config", Config.class);

        // 解析 @ComponentScan @Bean @Import @ImportResource
        context.registerBean(ConfigurationClassPostProcessor.class);

        // @MapperScanner
         context.registerBean(MapperScannerConfigurer.class, bd -> {bd.getPropertyValues().add("basePackage","com.example.springheima.beanFactoryPostProcessor.mapper");});


        // 模拟 ConfigurationClassPostProcessor 包扫描，解析@Component
        //context.registerBean(ComponentScanBeanFactoryPostProcessor.class);

        // 模拟 ConfigurationClassPostProcessor解析@Bean
        // context.registerBean(BeanAnnotationBeanFactoryPostProcessor.class);

         // 模拟MapperScannerConfigurer
        // context.registerBean(MapperPostProcessor.class);
        context.refresh();

        System.out.println("======================================================================================================================");
        for (String beanDefinitionName : context.getBeanDefinitionNames()) {
            System.out.println(beanDefinitionName);
        }

        context.close();
    }
}
