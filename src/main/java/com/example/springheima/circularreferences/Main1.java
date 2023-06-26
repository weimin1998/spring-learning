package com.example.springheima.circularreferences;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.support.GenericApplicationContext;

import javax.annotation.PostConstruct;

public class Main1 {

    static class A{

        @Autowired
        public A(@Lazy B b){
            System.out.println("A()");
            System.out.println(b.getClass());
        }
        private B b;

        @PostConstruct
        public void init(){
            System.out.println("init() A");
        }
    }
    static class B{
        @Autowired
        public B(A a){
            System.out.println("B()");
        }
        private A a;

        @PostConstruct
        public void init(){
            System.out.println("init() B");
        }
    }

    public static void main(String[] args) {
        GenericApplicationContext context = new GenericApplicationContext();
        AnnotationConfigUtils.registerAnnotationConfigProcessors(context.getDefaultListableBeanFactory());

        context.registerBean("a", A.class);
        context.registerBean("b", B.class);

        context.refresh();
    }
}
