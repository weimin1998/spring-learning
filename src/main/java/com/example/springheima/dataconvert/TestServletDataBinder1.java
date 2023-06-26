package com.example.springheima.dataconvert;

import org.springframework.format.Formatter;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestParameterPropertyValues;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.InvocableHandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.ServletRequestDataBinderFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class TestServletDataBinder1 {
    public static void main(String[] args) throws Exception {

        User user = new User();

        MockHttpServletRequest request = new MockHttpServletRequest();

        // @InitBinder
//        InvocableHandlerMethod invocableHandlerMethod = new InvocableHandlerMethod(new Mycontroller(), Mycontroller.class.getMethod("aaa", WebDataBinder.class));
//        ServletRequestDataBinderFactory servletRequestDataBinderFactory = new ServletRequestDataBinderFactory(Arrays.asList(invocableHandlerMethod), null);

        // FormattingConversionService
//        FormattingConversionService formattingConversionService = new FormattingConversionService();
//        formattingConversionService.addFormatter(new MyFormatter());
//        ConfigurableWebBindingInitializer configurableWebBindingInitializer = new ConfigurableWebBindingInitializer();
//        configurableWebBindingInitializer.setConversionService(formattingConversionService);
//        ServletRequestDataBinderFactory servletRequestDataBinderFactory = new ServletRequestDataBinderFactory(null,configurableWebBindingInitializer);


        // 同时@InitBinder  FormattingConversionService
        InvocableHandlerMethod invocableHandlerMethod = new InvocableHandlerMethod(new Mycontroller(), Mycontroller.class.getMethod("aaa", WebDataBinder.class));

        FormattingConversionService formattingConversionService = new FormattingConversionService();
        formattingConversionService.addFormatter(new MyFormatter());
        ConfigurableWebBindingInitializer configurableWebBindingInitializer = new ConfigurableWebBindingInitializer();
        configurableWebBindingInitializer.setConversionService(formattingConversionService);
        ServletRequestDataBinderFactory servletRequestDataBinderFactory = new ServletRequestDataBinderFactory(Arrays.asList(invocableHandlerMethod),configurableWebBindingInitializer);

        WebDataBinder binder = servletRequestDataBinderFactory.createBinder(new ServletWebRequest(request), user, "user");
        request.setParameter("birthday", "1998|06|22");
        request.setParameter("address.name", "shanghai");

        binder.bind(new ServletRequestParameterPropertyValues(request));

        System.out.println(user);
    }

    static class Mycontroller {

        @InitBinder
        public void aaa(WebDataBinder webDataBinder) {
            webDataBinder.addCustomFormatter(new MyFormatter());
        }
    }

    static class MyFormatter implements Formatter<Date> {

        @Override
        public Date parse(String text, Locale locale) throws ParseException {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy|MM|dd");
            return simpleDateFormat.parse(text);
        }

        @Override
        public String print(Date object, Locale locale) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy|MM|dd");
            return simpleDateFormat.format(object);
        }
    }

    static class User {
        private Date birthday;
        private Address address;

        public Date getBirthday() {
            return birthday;
        }

        public void setBirthday(Date birthday) {
            this.birthday = birthday;
        }

        public Address getAddress() {
            return address;
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        @Override
        public String toString() {
            return "User{" +
                    "birthday=" + birthday +
                    ", address=" + address +
                    '}';
        }
    }

    static class Address {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Address{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }
}
