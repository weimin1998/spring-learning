package com.example.springheima.aop;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JDKProxyDemo {
    interface Foo {
        void foo();
    }

    static final class Target implements Foo {

        @Override
        public void foo() {
            System.out.println("target foo");
        }
    }

    public static void main(String[] args) {
        Target target = new Target();
        // 第一个参数，是类加载器. 代理类和普通类的区别：普通类是先写java源文件，然后编译成class字节码文件，然后经过类加载，就可以使用；
        // 而代理类没有java源代码，它是在运行期间直接生成字节码，而代理类的字节码也需要经过类加载才能使用，这个类加载器，就是用来加载代理类的字节码文件。

        // 第二个参数，是代理类应该实现的接口。代理类应该实现目标类的所有接口。

        // 第三个参数，是代理类在实现接口方法时，应该满足的行为，也就是应该干什么。将来代理类的方法被调用时，就会调用InvocationHandler的invoke方法

        // 可以强制转换，是因为代理类实现了这个接口
        Foo proxyInstance = (Foo) Proxy.newProxyInstance(
                JDKProxyDemo.class.getClassLoader(),
                new Class[]{Foo.class},
                new InvocationHandler() {

                    // 第一个参数，代理对象本身
                    // 第二个参数，正在执行的方法
                    // 第三个参数，方法的参数
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        System.out.println("before");
                        Object invoke = method.invoke(target, args); // 反射调用方法
                        System.out.println("after");
                        // 返回目标方法的返回结果
                        return invoke;
                    }
                }
        );

        proxyInstance.foo();

        System.out.println(proxyInstance.getClass());

        try {
            System.in.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
