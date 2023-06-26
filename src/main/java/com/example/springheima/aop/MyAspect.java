package com.example.springheima.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class MyAspect {
    @Before("execution(* com.weimin.aop.MyService.*())")
    public void before(){
        System.out.println("before");
    }
}
