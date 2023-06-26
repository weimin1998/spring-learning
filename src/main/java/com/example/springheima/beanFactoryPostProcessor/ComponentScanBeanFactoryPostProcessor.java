package com.example.springheima.beanFactoryPostProcessor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.stereotype.Component;

import java.io.IOException;

public class ComponentScanBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

        try {
            CachingMetadataReaderFactory cachingMetadataReaderFactory = new CachingMetadataReaderFactory();
            AnnotationBeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();

            ComponentScan componentScan = AnnotationUtils.findAnnotation(Config.class, ComponentScan.class);
            if (componentScan != null) {
                for (String basePackage : componentScan.basePackages()) {
                    // 包名 ： com.example.springheima.beanFactoryPostProcessor.component
                    System.out.println(basePackage);
                    // 需要替换成路径 classpath*:com/example/springheima/beanFactoryPostProcessor/component/**/*.class
                    String path = "classpath*:" + basePackage.replace(".", "/") + "/**/*.class";
                    System.out.println(path);

                    // 获取资源
                    Resource[] resources = new PathMatchingResourcePatternResolver().getResources(path);
                    for (Resource resource : resources) {
                        System.out.println(resource);

                        MetadataReader metadataReader = cachingMetadataReaderFactory.getMetadataReader(resource);
                        ClassMetadata classMetadata = metadataReader.getClassMetadata();
                        AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
                        System.out.println("class name: " + classMetadata.getClassName());
                        System.out.println("has @Component: " + annotationMetadata.hasAnnotation(Component.class.getName()));
                        System.out.println("has @Component 派生: " + annotationMetadata.hasMetaAnnotation(Component.class.getName()));


                        if (annotationMetadata.hasAnnotation(Component.class.getName()) || annotationMetadata.hasMetaAnnotation(Component.class.getName())) {
                            AbstractBeanDefinition beanDefinition =
                                    BeanDefinitionBuilder.
                                            genericBeanDefinition(metadataReader.getClassMetadata().getClassName()).getBeanDefinition();


                            if (beanFactory instanceof DefaultListableBeanFactory) {

                                String beanName = beanNameGenerator.generateBeanName(beanDefinition, (DefaultListableBeanFactory) beanFactory);
                                ((DefaultListableBeanFactory) beanFactory).registerBeanDefinition(beanName, beanDefinition);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
