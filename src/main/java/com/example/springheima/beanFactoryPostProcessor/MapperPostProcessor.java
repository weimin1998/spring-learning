package com.example.springheima.beanFactoryPostProcessor;

import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;

import java.io.IOException;

public class MapperPostProcessor implements BeanDefinitionRegistryPostProcessor {
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {

        try {
            PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();
            AnnotationBeanNameGenerator annotationBeanNameGenerator = new AnnotationBeanNameGenerator();

            Resource[] resources = pathMatchingResourcePatternResolver.
                    getResources("classpath:com/example/springheima/beanFactoryPostProcessor/mapper/**/*.class");
            CachingMetadataReaderFactory cachingMetadataReaderFactory = new CachingMetadataReaderFactory();
            for (Resource resource : resources) {
                MetadataReader metadataReader = cachingMetadataReaderFactory.getMetadataReader(resource);
                ClassMetadata classMetadata = metadataReader.getClassMetadata();
                if (classMetadata.isInterface()) {
                    AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(MapperFactoryBean.class)
                            .addConstructorArgValue(classMetadata.getClassName())
                            .setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE)
                            .getBeanDefinition();

                    AbstractBeanDefinition beanDefinition1 = BeanDefinitionBuilder.genericBeanDefinition(classMetadata.getClassName()).getBeanDefinition();
                    String beanName = annotationBeanNameGenerator.generateBeanName(beanDefinition1, registry);
                    registry.registerBeanDefinition(beanName, beanDefinition);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }
}
