package com.example.springheima.eventListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Configuration
public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Main.class);

        applicationContext.getBean(MyService.class).doBusiness();

        //applicationContext.close();
    }

    @Component
    static class MyService{
        @Autowired
        private ApplicationContext applicationContext;
        public void doBusiness(){
            System.out.println("主线业务");
            // 发布事件
            applicationContext.publishEvent(new BusinessLogicFinishedEvent("from MyService doBusiness"));
        }
    }

    static class BusinessLogicFinishedEvent extends ApplicationEvent {
        public BusinessLogicFinishedEvent(Object source) {
            super(source);
        }
    }

    //@Component
    static class SmsApplicationListener implements ApplicationListener<BusinessLogicFinishedEvent>{

        @Override
        public void onApplicationEvent(BusinessLogicFinishedEvent event) {
            System.out.println(event.getSource());
            System.out.println("发送短信");
        }
    }

    //@Component
    static class EmailApplicationListener implements ApplicationListener<BusinessLogicFinishedEvent>{

        @Override
        public void onApplicationEvent(BusinessLogicFinishedEvent event) {
            System.out.println(event.getSource());
            System.out.println("发送邮件");
        }
    }

    @Component
    static class SmsService{
        private static final Logger log = LoggerFactory.getLogger(SmsService.class);
        @EventListener
        public void listener(BusinessLogicFinishedEvent event){
            log.debug(event.getSource().toString());
            log.debug("发送短信");
        }
    }

    @Component
    static class EmailService{
        private static final Logger log = LoggerFactory.getLogger(EmailService.class);
        @EventListener
        public void listener(BusinessLogicFinishedEvent event){
            log.debug(event.getSource().toString());
            log.debug("发送邮件");
        }
    }

    // 异步发送事件
//    @Bean
//    public ThreadPoolTaskExecutor executor(){
//        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
//        threadPoolTaskExecutor.setCorePoolSize(3);
//        threadPoolTaskExecutor.setMaxPoolSize(10);
//        threadPoolTaskExecutor.setQueueCapacity(100);
//        return threadPoolTaskExecutor;
//    }
//
//    @Bean
//    public SimpleApplicationEventMulticaster applicationEventMulticaster(ThreadPoolTaskExecutor executor){
//        SimpleApplicationEventMulticaster simpleApplicationEventMulticaster = new SimpleApplicationEventMulticaster();
//        simpleApplicationEventMulticaster.setTaskExecutor(executor);
//        return simpleApplicationEventMulticaster;
//    }


}
