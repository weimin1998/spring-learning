package com.example.springheima.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;

public class Main {
    public static void main(String[] args) {
        GenericApplicationContext context = new GenericApplicationContext();

        AnnotationConfigUtils.registerAnnotationConfigProcessors(context.getDefaultListableBeanFactory());

        context.registerBean("myConfig", Config.class);
        context.refresh();

        System.out.println(context.getBean(Config.class).getClass());

        context.close();
    }


    @Configuration
    static class Config {

        @Bean
        public Bean1 bean1() {
            return new Bean1();
        }

        @Bean
        public Bean2 bean2() {
            System.out.println(bean1());
            System.out.println(bean1());
            System.out.println(bean1());
            return new Bean2();
        }
//        @Bean
//        public Bean1 bean1(@Value("${JAVA_HOME}") String home, @Value("${java.class.version}") String version) {
//            System.out.println(home + "=======" + version);
//            return new Bean1();

//        }

//        @Bean
//        public Bean1 bean1(@Value("${JAVA_HOME}") String home) {
//            System.out.println(home);
//            return new Bean1();
//        }
    }

    static class Bean1 {
    }

    static class Bean2 {
    }
}
