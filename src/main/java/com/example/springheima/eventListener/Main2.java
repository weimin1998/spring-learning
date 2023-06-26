package com.example.springheima.eventListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.GenericApplicationListener;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Configuration
public class Main2 {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Main2.class);
        applicationContext.getBean(MyService.class).doBusiness();

        applicationContext.close();
    }

    @Component
    static class MyService {
        @Autowired
        private ApplicationContext applicationContext;

        public void doBusiness() {
            System.out.println("主线业务");
            applicationContext.publishEvent(new BusinessLogicFinishedEvent("from MyService doBusiness"));
        }
    }

    static class BusinessLogicFinishedEvent extends ApplicationEvent {
        public BusinessLogicFinishedEvent(Object source) {
            super(source);
        }
    }

    @Component
    static class SmsApplicationListener implements ApplicationListener<BusinessLogicFinishedEvent>{

        @Override
        public void onApplicationEvent(BusinessLogicFinishedEvent event) {
            System.out.println(event.getSource());
            System.out.println("发送短信");
        }
    }

    @Component
    static class EmailApplicationListener implements ApplicationListener<BusinessLogicFinishedEvent>{

        @Override
        public void onApplicationEvent(BusinessLogicFinishedEvent event) {
            System.out.println(event.getSource());
            System.out.println("发送邮件");
        }
    }



    static  abstract class AbstractApplicationEventMulticaster implements ApplicationEventMulticaster {

        @Override
        public void addApplicationListener(ApplicationListener<?> listener) {

        }

        @Override
        public void addApplicationListenerBean(String listenerBeanName) {

        }

        @Override
        public void removeApplicationListener(ApplicationListener<?> listener) {

        }

        @Override
        public void removeApplicationListenerBean(String listenerBeanName) {

        }

        @Override
        public void removeApplicationListeners(Predicate<ApplicationListener<?>> predicate) {

        }

        @Override
        public void removeApplicationListenerBeans(Predicate<String> predicate) {

        }

        @Override
        public void removeAllListeners() {

        }

        @Override
        public void multicastEvent(ApplicationEvent event) {

        }

        @Override
        public void multicastEvent(ApplicationEvent event, ResolvableType eventType) {

        }
    }


    @Bean
    public ApplicationEventMulticaster applicationEventMulticaster(ConfigurableApplicationContext applicationContext){
        return new AbstractApplicationEventMulticaster() {
            private List<GenericApplicationListener> listeners = new ArrayList<>();
            // 收集监听器
            @Override
            public void addApplicationListenerBean(String listenerBeanName) {
                System.out.println(listenerBeanName);
                ApplicationListener listener = applicationContext.getBean(listenerBeanName, ApplicationListener.class);

                // 假设只实现了一个接口，接口只有一个参数
                ResolvableType generic = ResolvableType.forClass(listener.getClass()).getInterfaces()[0].getGeneric(0);

                GenericApplicationListener genericApplicationListener = new GenericApplicationListener() {
                    @Override
                    public void onApplicationEvent(ApplicationEvent event) {
                        listener.onApplicationEvent(event);
                    }

                    @Override
                    public boolean supportsEventType(ResolvableType eventType) {

                        return generic.isAssignableFrom(eventType);
                    }
                };

                listeners.add(genericApplicationListener);
            }

            // 广播事件
            @Override
            public void multicastEvent(ApplicationEvent event, ResolvableType eventType) {

                for (GenericApplicationListener listener : listeners) {
                    if(listener.supportsEventType(ResolvableType.forClass(event.getClass()))){
                        listener.onApplicationEvent(event);
                    }
                }
            }
        };
    }
}
