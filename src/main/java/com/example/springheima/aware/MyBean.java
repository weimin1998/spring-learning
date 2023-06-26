package com.example.springheima.aware;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class MyBean implements BeanNameAware , ApplicationContextAware, InitializingBean {
    @Override
    public void setBeanName(String name) {
        System.out.println("BeanNameAware setName: "+name);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("ApplicationContextAware setApplicationContext: "+ applicationContext);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("InitializingBean afterPropertiesSet()");
    }
}
