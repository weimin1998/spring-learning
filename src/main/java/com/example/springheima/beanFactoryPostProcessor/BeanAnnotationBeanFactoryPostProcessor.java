package com.example.springheima.beanFactoryPostProcessor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.type.MethodMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;

import java.io.IOException;
import java.util.Set;

public class BeanAnnotationBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        try {
            CachingMetadataReaderFactory cachingMetadataReaderFactory = new CachingMetadataReaderFactory();
            // 这种方式不走类加载，效率比反射高
            MetadataReader metadataReader = cachingMetadataReaderFactory.getMetadataReader(new ClassPathResource("com/example/springheima/beanFactoryPostProcessor/Config.class"));
            Set<MethodMetadata> annotatedMethods = metadataReader.getAnnotationMetadata().getAnnotatedMethods(Bean.class.getName());

            for (MethodMetadata annotatedMethod : annotatedMethods) {
                System.out.println(annotatedMethod);

                String initMethod = annotatedMethod.getAnnotationAttributes(Bean.class.getName()).get("initMethod").toString();

                BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition();
                beanDefinitionBuilder.setFactoryMethodOnBean(annotatedMethod.getMethodName(),"config");
                beanDefinitionBuilder.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_CONSTRUCTOR);
                if(initMethod.length()>0){
                    beanDefinitionBuilder.setInitMethodName(initMethod);
                }

                AbstractBeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();

                DefaultListableBeanFactory beanFactory1 = (DefaultListableBeanFactory) beanFactory;
                beanFactory1.registerBeanDefinition(annotatedMethod.getMethodName(), beanDefinition);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
