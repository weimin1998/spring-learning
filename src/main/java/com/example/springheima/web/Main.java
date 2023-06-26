package com.example.springheima.web;

import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception {
        AnnotationConfigServletWebServerApplicationContext applicationContext = new AnnotationConfigServletWebServerApplicationContext(WebConfig.class);


        RequestMappingHandlerMapping handlerMapping = applicationContext.getBean(RequestMappingHandlerMapping.class);

        Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();

        handlerMethods.forEach((k, v) -> {
            System.out.println(k + "-->" + v);
        });

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/test");
        request.addHeader("token", "1247asihofdpi12ywqdhq9wey19rhpqw4y");
        MockHttpServletResponse response = new MockHttpServletResponse();

        MockHttpServletRequest request1 = new MockHttpServletRequest("POST", "/test3");
        MockHttpServletResponse response1 = new MockHttpServletResponse();

        HandlerExecutionChain handlerExecutionChain = handlerMapping.getHandler(request);
        System.out.println(handlerExecutionChain);

        HandlerExecutionChain handlerExecutionChain1 = handlerMapping.getHandler(request1);


        MyAdapter handlerAdapter = applicationContext.getBean(MyAdapter.class);

        handlerAdapter.invokeHandlerMethod(request, response, (HandlerMethod) handlerExecutionChain.getHandler());
        handlerAdapter.invokeHandlerMethod(request1, response1, (HandlerMethod) handlerExecutionChain1.getHandler());
        byte[] array = response1.getContentAsByteArray();
        System.out.println(new String(array, Charset.defaultCharset()));


        List<HandlerMethodArgumentResolver> argumentResolvers = handlerAdapter.getArgumentResolvers();
        List<HandlerMethodReturnValueHandler> returnValueHandlers = handlerAdapter.getReturnValueHandlers();

        System.out.println("=========all argumentResolvers===========");
        argumentResolvers.forEach((argumentResolver) -> {
            System.out.println(argumentResolver);
        });
        System.out.println("=========all returnValueHandlers===========");
        returnValueHandlers.forEach((returnValueHandler) -> {
            System.out.println(returnValueHandler);
        });


    }
}
