package com.example.springheima.aop;

import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.transaction.annotation.Transactional;

public class Main1 {
    public static void main(String[] args) throws NoSuchMethodException {
        // 准备切点
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();

        pointcut.setExpression("execution(* bar())");

        System.out.println(pointcut.matches(Target1.class.getDeclaredMethod("bar"), Target1.class));
        System.out.println(pointcut.matches(Target1.class.getDeclaredMethod("foo"), Target1.class));



        AspectJExpressionPointcut pointcut1 = new AspectJExpressionPointcut();

        // 根据注解匹配
        pointcut1.setExpression("@annotation(org.springframework.transaction.annotation.Transactional)");
        System.out.println(pointcut1.matches(Target1.class.getDeclaredMethod("bar"), Target1.class));
        System.out.println(pointcut1.matches(Target1.class.getDeclaredMethod("foo"), Target1.class));
    }


    static class Target1 {

        @Transactional
        public void foo() {
            System.out.println("Target1 foo");
        }

        public void bar() {
            System.out.println("Target1 bar");
        }
    }
}
