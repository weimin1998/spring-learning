package com.example.springheima.aop;

import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;

// Transactional
public class Main2 {
    public static void main(String[] args) throws NoSuchMethodException {
        StaticMethodMatcherPointcut pointcut = new StaticMethodMatcherPointcut() {
            @Override
            public boolean matches(Method method, Class<?> targetClass) {
                // MergedAnnotations工具类
                MergedAnnotations annotations = MergedAnnotations.from(method);

                if(annotations.isPresent(Transactional.class)){
                    // 方法上是否有注解
                    return true;
                }


                // from(class)只会找本类
                // annotations = MergedAnnotations.from(targetClass);
                annotations = MergedAnnotations.from(targetClass, MergedAnnotations.SearchStrategy.TYPE_HIERARCHY);

                if(annotations.isPresent(Transactional.class)){
                    // 类上是否有注解
                    return true;
                }
                return false;
            }
        };

        System.out.println(pointcut.matches(Target1.class.getDeclaredMethod("foo"), Target1.class));
        System.out.println(pointcut.matches(Target1.class.getDeclaredMethod("bar"), Target1.class));

        System.out.println(pointcut.matches(Target2.class.getDeclaredMethod("foo"), Target2.class));
        System.out.println(pointcut.matches(Target2.class.getDeclaredMethod("bar"), Target2.class));

        System.out.println(pointcut.matches(Target3.class.getDeclaredMethod("foo"), Target3.class));
        System.out.println(pointcut.matches(Target3.class.getDeclaredMethod("bar"), Target3.class));


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

    @Transactional
    static class Target2 {

        public void foo() {
            System.out.println("Target2 foo");
        }

        public void bar() {
            System.out.println("Target2 bar");
        }
    }

    @Transactional
    interface I3{
        void foo();
    }
    static class Target3 implements I3{

        @Override
        public void foo() {
            System.out.println("Target3 foo");
        }

        public void bar() {
            System.out.println("Target3 bar");
        }
    }
}
