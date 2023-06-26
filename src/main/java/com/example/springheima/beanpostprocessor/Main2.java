package com.example.springheima.beanpostprocessor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Configuration
public class Main2 {
    @SuppressWarnings("all")
    public static void main(String[] args) throws NoSuchFieldException, NoSuchMethodException {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Main2.class);
        DefaultListableBeanFactory beanFactory = applicationContext.getDefaultListableBeanFactory();

//        DependencyDescriptor dd1 = new DependencyDescriptor(Bean1.class.getDeclaredField("bean2"),false);
//        Object o = beanFactory.doResolveDependency(dd1, "bean1", null, null);
//        System.out.println(o);

//        DependencyDescriptor dd2 = new DependencyDescriptor(new MethodParameter(Bean1.class.getDeclaredMethod("setBean2", Bean2.class), 0), false);
//        Object o1 = beanFactory.doResolveDependency(dd2, "bean1", null, null);
//        System.out.println(o1);

//        DependencyDescriptor dd3 = new DependencyDescriptor(Bean1.class.getDeclaredField("bean3"),false);
////        System.out.println(dd3.getDependencyType()); // class java.util.Optional
////        dd3.increaseNestingLevel();
////        System.out.println(dd3.getDependencyType()); // class com.example.springheima.beanpostprocessor.Main2$Bean2
//
//        if(dd3.getDependencyType()==Optional.class){
//            dd3.increaseNestingLevel();
//        }
//        Object o2 = beanFactory.doResolveDependency(dd3, "bean1", null, null);
//        System.out.println(o2);
//        System.out.println(Optional.ofNullable(o2));


//        DependencyDescriptor dd4 = new DependencyDescriptor(Bean1.class.getDeclaredField("bean4"),false);
//        if(dd4.getDependencyType()==ObjectFactory.class){
//            dd4.increaseNestingLevel();
//        }
//
//        ObjectFactory bean2ObjectFactory = new ObjectFactory() {
//            @Override
//            public Object getObject() throws BeansException {
//                Object o3 = beanFactory.doResolveDependency(dd4, "bean1", null, null);
//                return o3;
//            }
//        };
//
//        System.out.println(bean2ObjectFactory.getObject());
//        System.out.println(bean2ObjectFactory.getObject());
//        System.out.println(bean2ObjectFactory.getObject());

        // @Lazy
        DependencyDescriptor dd5 = new DependencyDescriptor(Bean1.class.getDeclaredField("bean2"),false);

        ContextAnnotationAutowireCandidateResolver resolver = new ContextAnnotationAutowireCandidateResolver();
        resolver.setBeanFactory(beanFactory);

        Object proxy = resolver.getLazyResolutionProxyIfNecessary(dd5, "bean1");
        System.out.println(proxy);
        System.out.println(proxy.getClass());

    }

    @SuppressWarnings("all")
    static class Bean1{
        @Autowired @Lazy
        private Bean2 bean2;

        @Autowired
        public void setBean2( Bean2 bean2) {
            this.bean2 = bean2;
        }

        @Autowired
        private Optional<Bean2> bean3;

        @Autowired
        private ObjectFactory<Bean2> bean4;
    }

    @Component
    static class Bean2{

    }
}
