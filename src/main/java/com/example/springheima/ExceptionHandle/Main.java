package com.example.springheima.ExceptionHandle;

import org.springframework.beans.factory.support.ManagedList;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception {
        ExceptionHandlerExceptionResolver exceptionHandlerExceptionResolver = new ExceptionHandlerExceptionResolver();

        exceptionHandlerExceptionResolver.setMessageConverters(Arrays.asList(new MappingJackson2HttpMessageConverter()));//消息转换
        exceptionHandlerExceptionResolver.afterPropertiesSet();// 参数解析器，返回值处理器，重用了目标方法的参数解析器

        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();

        //HandlerMethod handlerMethod = new HandlerMethod(new Controller01(), Controller01.class.getMethod("foo"));
//        HandlerMethod handlerMethod = new HandlerMethod(new Controller02(), Controller02.class.getMethod("foo"));
//        ArithmeticException exception = new ArithmeticException("/ by zero");

        //HandlerMethod handlerMethod = new HandlerMethod(new Controller03(), Controller03.class.getMethod("foo"));
        HandlerMethod handlerMethod = new HandlerMethod(new Controller04(), Controller04.class.getMethod("foo"));

        // 可以处理嵌套的异常
        // from version ???
        // Exception exception = new Exception("e1",new RuntimeException("e2",new IOException("e3")));
        Exception exception = new Exception("e1");

        ModelAndView modelAndView = exceptionHandlerExceptionResolver.resolveException(mockHttpServletRequest, mockHttpServletResponse, handlerMethod, exception);

        System.out.println("==========================");
        System.out.println(new String(mockHttpServletResponse.getContentAsByteArray(), StandardCharsets.UTF_8));
        System.out.println("==========================");

        System.out.println(modelAndView.getModel());
        System.out.println(modelAndView.getViewName());
    }

    static class Controller01 {
        public void foo() {
        }

        @ExceptionHandler
        @ResponseBody
        public Map<String, String> handle(ArithmeticException e) {
            Map<String, String> map = new HashMap<>();
            map.put("error", e.getMessage());
            return map;
        }
    }

    static class Controller02 {
        public void foo() {

        }

        @ExceptionHandler
        public ModelAndView handle(ArithmeticException e) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("test1");
            modelAndView.addObject("error", "aaa");
            return modelAndView;
        }
    }

    static class Controller03 {
        public void foo() {
        }

        @ExceptionHandler
        @ResponseBody
        public Map<String, String> handle(IOException e) {
            Map<String, String> map = new HashMap<>();
            map.put("error", e.getMessage());
            return map;
        }
    }

    static class Controller04 {
        public void foo() {
        }

        @ExceptionHandler
        @ResponseBody
        public Map<String, String> handle(Exception e, HttpServletRequest request) {
            Map<String, String> map = new HashMap<>();
            map.put("error", e.getMessage());
            System.out.println(request);
            return map;
        }
    }
}
