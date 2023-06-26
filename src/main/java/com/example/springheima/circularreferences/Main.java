package com.example.springheima.circularreferences;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.support.GenericApplicationContext;

import javax.annotation.PostConstruct;

public class Main {

    static class A{
        public A(){
            System.out.println("A()");
        }
        private B b;

        @Autowired
        public void setB(B b) {
            System.out.println("setB");
            this.b = b;
        }

        @PostConstruct
        public void init(){
            System.out.println("init() A");
        }
    }
    static class B{
        public B(){
            System.out.println("B()");
        }
        private A a;

        @Autowired
        public void setA(A a) {
            System.out.println("setA");
            this.a = a;
        }

        @PostConstruct
        public void init(){
            System.out.println("init() B");
        }
    }

    public static void main(String[] args) {
        GenericApplicationContext context = new GenericApplicationContext();
        AnnotationConfigUtils.registerAnnotationConfigProcessors(context.getDefaultListableBeanFactory());

        context.registerBean("a",A.class);
        context.registerBean("b",B.class);

        context.refresh();
    }
}
