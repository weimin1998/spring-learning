package com.example.springheima.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;

public class Main {
    public static void main(String[] args) {
        // 准备切点
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* foo())");

        // 通知
        MethodInterceptor advice =  new MethodInterceptor() {
            @Override
            public Object invoke(MethodInvocation invocation) throws Throwable {
                System.out.println("before");
                Object proceed = invocation.proceed();
                System.out.println("after");
                return proceed;
            }
        };

        // 切面
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(pointcut, advice);

        target1(advisor);
        //target2(advisor);


    }

    private static void target2(DefaultPointcutAdvisor advisor) {
        // 创建代理
        Target2 target = new Target2();

        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setTarget(target);
        proxyFactory.addAdvisor(advisor);
        // 他不能自己判断是否实现了接口
        proxyFactory.setInterfaces(target.getClass().getInterfaces());

        proxyFactory.setProxyTargetClass(true);

        Target2 proxy = (Target2) proxyFactory.getProxy();

        //
        System.out.println(proxy.getClass());

        proxy.foo();
        proxy.bar();
    }

    private static void target1(DefaultPointcutAdvisor advisor) {
        // 创建代理
        Target1 target = new Target1();

        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setTarget(target);
        proxyFactory.addAdvisor(advisor);
        // 他不能自己判断是否实现了接口
        proxyFactory.setInterfaces(target.getClass().getInterfaces());

        proxyFactory.setProxyTargetClass(true);

        I1 proxy = (I1) proxyFactory.getProxy();

        //
        System.out.println(proxy.getClass());

        proxy.foo();
        proxy.bar();
    }

    interface I1{
        void foo();
        void bar();
    }

    static class Target1 implements I1{

        @Override
        public void foo() {
            System.out.println("Target1 foo");
        }

        @Override
        public void bar() {
            System.out.println("Target1 bar");
        }
    }

    static class Target2{

        public void foo() {
            System.out.println("Target2 foo");
        }

        public void bar() {
            System.out.println("Target2 bar");
        }
    }
}
