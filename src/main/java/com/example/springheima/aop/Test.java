package com.example.springheima.aop;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Test {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();

        applicationContext.register(ConfigAop.class);

        applicationContext.refresh();

        MyService bean = applicationContext.getBean(MyService.class);

        // MyService bean = new MyService();
        System.out.println(bean.getClass());
        bean.foo();
    }
}
