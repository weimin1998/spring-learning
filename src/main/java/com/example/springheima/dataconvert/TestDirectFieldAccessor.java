package com.example.springheima.dataconvert;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.DirectFieldAccessor;

import java.util.Date;

public class TestDirectFieldAccessor {
    public static void main(String[] args) {

        MyBean myBean = new MyBean();

        DirectFieldAccessor directFieldAccessor = new DirectFieldAccessor(myBean);

        directFieldAccessor.setPropertyValue("a","10");
        directFieldAccessor.setPropertyValue("b","hello");
        directFieldAccessor.setPropertyValue("c","2023/01/01");

        System.out.println(myBean);

    }

    static class MyBean{
        private int a;
        private String b;
        private Date c;

        @Override
        public String toString() {
            return "MyBean{" +
                    "a=" + a +
                    ", b='" + b + '\'' +
                    ", c=" + c +
                    '}';
        }
    }
}
