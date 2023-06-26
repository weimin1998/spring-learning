package com.example.springheima.dataconvert;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestParameterPropertyValues;

import java.util.Date;

public class TestServletDataBinder {
    public static void main(String[] args) {
        MyBean myBean = new MyBean();

        // 将请求参数封装到javabean对象中
        ServletRequestDataBinder servletRequestDataBinder = new ServletRequestDataBinder(myBean);
        MockHttpServletRequest request = new MockHttpServletRequest();

        request.setParameter("a","10");
        request.setParameter("b","hello");
        request.setParameter("c","1998/06/21");

        servletRequestDataBinder.bind(new ServletRequestParameterPropertyValues(request));
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
