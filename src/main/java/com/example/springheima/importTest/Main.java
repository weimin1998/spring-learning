package com.example.springheima.importTest;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.*;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.type.AnnotationMetadata;

public class Main {

    public static void main(String[] args) {
        GenericApplicationContext applicationContext = new GenericApplicationContext();
        DefaultListableBeanFactory beanFactory = applicationContext.getDefaultListableBeanFactory();
        beanFactory.setAllowBeanDefinitionOverriding(false);

        AnnotationConfigUtils.registerAnnotationConfigProcessors(beanFactory);
        applicationContext.registerBean(MyConfig.class);
        applicationContext.refresh();
        System.out.println(applicationContext.getBean(MYBean.class));
    }

    @Configuration
    @Import(MyImport.class)
    static class MyConfig {
        @Bean
        public MYBean bean() {
            return new Bean2();
        }
    }

    static class MyImport implements DeferredImportSelector {

        @Override
        public String[] selectImports(AnnotationMetadata importingClassMetadata) {
            return new String[]{OtherConfig.class.getName()};
        }
    }

    @Configuration
    static class OtherConfig {
        @Bean
        @ConditionalOnMissingBean
        public MYBean bean() {
            return new Bean1();
        }
    }

    interface MYBean {
    }

    static class Bean1 implements MYBean {
    }

    static class Bean2 implements MYBean {
    }
}
