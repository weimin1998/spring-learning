package org.springframework.aop.framework.autoproxy;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.support.GenericApplicationContext;

import java.util.List;

public class Main3 {
    public static void main(String[] args) {
        GenericApplicationContext applicationContext = new GenericApplicationContext();

        applicationContext.registerBean("AspectClass", AspectClass.class);
        applicationContext.registerBean("config", Config.class);

        applicationContext.registerBean(ConfigurationClassPostProcessor.class);

        // AnnotationAwareAspectJAutoProxyCreator
        //
        applicationContext.registerBean(AnnotationAwareAspectJAutoProxyCreator.class);

        applicationContext.refresh();

        AnnotationAwareAspectJAutoProxyCreator creator = applicationContext.getBean(AnnotationAwareAspectJAutoProxyCreator.class);

        // 为目标类找切面
        List<Advisor> advisors = creator.findEligibleAdvisors(Target1.class, "");
        System.out.println(advisors.size());
        for (Advisor advisor : advisors) {
            System.out.println(advisor);
        }

        // 创建代理
        Object o1 = creator.wrapIfNecessary(new Target1(), "target1", "target1");

        Object o2 = creator.wrapIfNecessary(new Target2(), "target2", "target2");

        System.out.println(o1.getClass());
        Target1 target1 = (Target1)o1;
        target1.foo();
        System.out.println(o2.getClass());
    }

    static class Target1 {
        public void foo() {
            System.out.println("target1 foo");
        }
    }

    static class Target2 {
        public void bar() {
            System.out.println("target2 bar");
        }
    }

    @Aspect // 高级切面类
    static class AspectClass {

        @Before("execution(* foo())")

        public void before() {
            System.out.println("aspect before");
        }

        @After("execution(* foo())")
        public void after() {
            System.out.println("aspect after");
        }
    }

    @Configuration
    static class Config {
        @Bean
        public Advisor advisor() {// 低级切面
            AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
            pointcut.setExpression("execution(* foo())");

            return new DefaultPointcutAdvisor(pointcut, advice());
        }

        @Bean
        public MethodInterceptor advice() {
            return new MethodInterceptor() {
                @Override
                public Object invoke(MethodInvocation invocation) throws Throwable {
                    System.out.println("advice before");

                    Object proceed = invocation.proceed();

                    System.out.println("advice after");
                    return proceed;
                }
            };
        }
    }
}
