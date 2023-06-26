package com.example.springheima.dataconvert;

import org.springframework.beans.BeanWrapperImpl;

import java.util.Date;

public class TestBeanWrapperImpl {
    public static void main(String[] args) {

        MyBean myBean = new MyBean();

        BeanWrapperImpl beanWrapper = new BeanWrapperImpl(myBean);

        beanWrapper.setPropertyValue("a","10");
        beanWrapper.setPropertyValue("b","hello");
        beanWrapper.setPropertyValue("c","2023/01/01");

        System.out.println(myBean);

    }

    static class MyBean{
        private int a;
        private String b;
        private Date c;

        public int getA() {
            return a;
        }

        public void setA(int a) {
            this.a = a;
        }

        public String getB() {
            return b;
        }

        public void setB(String b) {
            this.b = b;
        }

        public Date getC() {
            return c;
        }

        public void setC(Date c) {
            this.c = c;
        }

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
