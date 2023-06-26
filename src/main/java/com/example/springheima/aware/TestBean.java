package com.example.springheima.aware;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

public class TestBean implements BeanNameAware, ApplicationContextAware, InitializingBean {

    @Autowired
    A a;
    @Resource
    B b;

    @Override
    public void setBeanName(String name) {
        System.out.println("BeanNameAware");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("InitializingBean");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("ApplicationContextAware");
    }

    @PostConstruct
    public void init(){
        System.out.println("@PostConstruct");
    }

    @PreDestroy
    public void destroy(){
        System.out.println("@PreDestroy");
    }

    static class A{}
    static class B{}
}
