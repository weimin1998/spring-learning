package com.example.springheima.eventListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Configuration
public class Main1 {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Main1.class);


//        for (String beanDefinitionName : applicationContext.getBeanDefinitionNames()) {
//            Object bean = applicationContext.getBean(beanDefinitionName);
//            for (Method method : bean.getClass().getMethods()) {
//                if (method.isAnnotationPresent(MyListener.class)) {
//                    ApplicationListener applicationListener = new ApplicationListener() {
//                        @Override
//                        public void onApplicationEvent(ApplicationEvent event) {
//                            Class<?> eventType = method.getParameterTypes()[0];
//
//                            // 筛选事件类型
//                            if (eventType.isAssignableFrom(event.getClass())) {
//                                try {
//                                    method.invoke(bean, event);
//                                } catch (IllegalAccessException | InvocationTargetException e) {
//                                    throw new RuntimeException(e);
//                                }
//                            }
//                        }
//                    };
//                    applicationContext.addApplicationListener(applicationListener);
//                }
//            }
//        }


        applicationContext.getBean(MyService.class).doBusiness();

        applicationContext.close();
    }

    @Component
    static class MyService {
        @Autowired
        private ApplicationContext applicationContext;

        public void doBusiness() {
            System.out.println("主线业务");
            // 下面是支线业务
//            System.out.println("发送短信");
//            System.out.println("发送邮件");

            applicationContext.publishEvent(new BusinessLogicFinishedEvent("from MyService doBusiness"));
        }
    }

    static class BusinessLogicFinishedEvent extends ApplicationEvent {
        public BusinessLogicFinishedEvent(Object source) {
            super(source);
        }
    }

    @Component
    static class SmsService {
        private static final Logger log = LoggerFactory.getLogger(SmsService.class);

        @MyListener
        public void listener(BusinessLogicFinishedEvent event) {
            log.debug(event.getSource().toString());
            log.debug("发送短信");
        }
    }

    @Component
    static class EmailService {
        private static final Logger log = LoggerFactory.getLogger(EmailService.class);

        @MyListener
        public void listener(BusinessLogicFinishedEvent event) {
            log.debug(event.getSource().toString());
            log.debug("发送邮件");
        }
    }


    @Bean
    public SmartInitializingSingleton smartInitializingSingleton(ConfigurableApplicationContext applicationContext) {
        return new SmartInitializingSingleton() {
            @Override
            public void afterSingletonsInstantiated() {
                for (String beanDefinitionName : applicationContext.getBeanDefinitionNames()) {
                    Object bean = applicationContext.getBean(beanDefinitionName);
                    for (Method method : bean.getClass().getMethods()) {
                        if (method.isAnnotationPresent(MyListener.class)) {
                            ApplicationListener applicationListener = new ApplicationListener() {
                                @Override
                                public void onApplicationEvent(ApplicationEvent event) {
                                    Class<?> eventType = method.getParameterTypes()[0];

                                    // 筛选事件类型
                                    if (eventType.isAssignableFrom(event.getClass())) {
                                        try {
                                            method.invoke(bean, event);
                                        } catch (IllegalAccessException | InvocationTargetException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                }
                            };
                            applicationContext.addApplicationListener(applicationListener);
                        }
                    }
                }
            }
        };
    }
}
