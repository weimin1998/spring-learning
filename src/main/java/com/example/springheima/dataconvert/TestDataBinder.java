package com.example.springheima.dataconvert;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.validation.DataBinder;

import java.util.Date;

public class TestDataBinder {
    public static void main(String[] args) {
        MyBean myBean = new MyBean();
        DataBinder dataBinder = new DataBinder(myBean);
        dataBinder.initDirectFieldAccess();// 成员变量赋值

        MutablePropertyValues pvs = new MutablePropertyValues();
        pvs.add("a","10");
        pvs.add("b","hello");
        pvs.add("c","1999/10/10");
        dataBinder.bind(pvs);
        System.out.println(myBean);

    }
    static class MyBean{
        private int a;
        private String b;
        private Date c;

//        public int getA() {
//            return a;
//        }
//
//        public void setA(int a) {
//            this.a = a;
//        }
//
//        public String getB() {
//            return b;
//        }
//
//        public void setB(String b) {
//            this.b = b;
//        }
//
//        public Date getC() {
//            return c;
//        }
//
//        public void setC(Date c) {
//            this.c = c;
//        }

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
