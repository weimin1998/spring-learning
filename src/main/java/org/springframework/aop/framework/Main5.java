package org.springframework.aop.framework;

import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.*;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.framework.ReflectiveMethodInvocation;
import org.springframework.aop.interceptor.ExposeInvocationInterceptor;
import org.springframework.aop.support.DefaultPointcutAdvisor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Main5 {

    static class Aspect {
        @Before("execution(* foo())")
        public void before1() {
            System.out.println("before1");
        }

        @Before("execution(* foo())")
        public void before2() {
            System.out.println("before2");
        }

        @After("execution(* foo())")
        public void after() {
            System.out.println("after");
        }

        @AfterReturning("execution(* foo())")
        public void afterReturning() {
            System.out.println("afterReturning");
        }

        @AfterThrowing("execution(* foo())")
        public void afterThrowing() {
            System.out.println("afterThrowing");
        }

        @Around("execution(* foo())")
        public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
            System.out.println("around before");
            Object proceed = proceedingJoinPoint.proceed();
            System.out.println("around after");
            return proceed;
        }
    }

    static class Target {
        public void foo() {
            System.out.println("Target foo()");
        }
    }

    public static void main(String[] args) throws Throwable {
        List<Advisor> advisors = new ArrayList<>();

        AspectInstanceFactory factory = new SingletonAspectInstanceFactory(new Aspect());

        for (Method method : Aspect.class.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Before.class)) {
                String expression = method.getAnnotation(Before.class).value();
                AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
                pointcut.setExpression(expression);

                // 前置通知
                AspectJMethodBeforeAdvice advice = new AspectJMethodBeforeAdvice(method, pointcut, factory);
                // 切面
                Advisor advisor = new DefaultPointcutAdvisor(pointcut, advice);
                advisors.add(advisor);
            } else if (method.isAnnotationPresent(AfterReturning.class)) {
                String expression = method.getAnnotation(AfterReturning.class).value();
                AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
                pointcut.setExpression(expression);

                AspectJAfterReturningAdvice advice = new AspectJAfterReturningAdvice(method, pointcut, factory);

                // 切面
                Advisor advisor = new DefaultPointcutAdvisor(pointcut, advice);
                advisors.add(advisor);
            } else if (method.isAnnotationPresent(Around.class)) {
                String expression = method.getAnnotation(Around.class).value();
                AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
                pointcut.setExpression(expression);

                AspectJAroundAdvice advice = new AspectJAroundAdvice(method, pointcut, factory);

                Advisor advisor = new DefaultPointcutAdvisor(pointcut, advice);
                advisors.add(advisor);
            } else if (method.isAnnotationPresent(After.class)) {
                String expression = method.getAnnotation(After.class).value();
                AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
                pointcut.setExpression(expression);

                AspectJAfterAdvice advice = new AspectJAfterAdvice(method, pointcut, factory);

                Advisor advisor = new DefaultPointcutAdvisor(pointcut, advice);
                advisors.add(advisor);
            }
        }

        for (Advisor advisor : advisors) {
            System.out.println(advisor);
        }

        // 统一转换为环绕通知
        Target target = new Target();
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setTarget(target);

        //
        proxyFactory.addAdvice(ExposeInvocationInterceptor.INSTANCE);

        proxyFactory.addAdvisors(advisors);

        System.out.println("==================================================================");
        List<Object> list = proxyFactory.getInterceptorsAndDynamicInterceptionAdvice(Target.class.getDeclaredMethod("foo"), Target.class);
        for (Object o : list) {
            System.out.println(o);
        }

        System.out.println("==================================================================");
        // 创建并执行调用链(执行环绕通知+目标)
        MethodInvocation methodInvocation = new ReflectiveMethodInvocation(
                null, target, Target.class.getMethod("foo"), new Object[0], Target.class, list
        );

        methodInvocation.proceed();

    }
}
