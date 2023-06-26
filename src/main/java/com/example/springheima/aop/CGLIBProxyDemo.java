package com.example.springheima.aop;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class CGLIBProxyDemo {
    static class Target {
        public void foo() {
            System.out.println("target foo");
        }
    }

    public static void main(String[] args) {
        Target target = new Target();

        // cglib是通过父子继承关系来创建代理
        // 第一个参数，代理类应该继承的类的类型，代理类应该是目标类的子类，所以，目标类不能是final
        // 第二个参数，代理类在重写父类（也就是）方法时，应该做的事情，所以，目标方法也不能是final
        Target proxy = (Target) Enhancer.create(Target.class, new MethodInterceptor() {

            // 第一个参数，代理对象本身
            // 第二个参数，正在执行的方法
            // 第三个参数，正在执行方法的参数
            // 第四个参数，？
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                System.out.println("before");
                // 三种方式调用目标方法
                // Object invoke = method.invoke(target, objects);// 反射调用方法
                // Object invoke = methodProxy.invoke(target, objects);// 不是反射，spring用的就是这个，需要目标对象
                Object invoke = methodProxy.invokeSuper(o, objects); // 不是反射，不需要目标对象
                System.out.println("after");
                return invoke;
            }
        });

        proxy.foo();
    }
}
