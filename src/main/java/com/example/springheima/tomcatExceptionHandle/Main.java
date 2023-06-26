package com.example.springheima.tomcatExceptionHandle;


import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Map;

public class Main {
    public static void main(String[] args) {
        AnnotationConfigServletWebServerApplicationContext applicationContext = new AnnotationConfigServletWebServerApplicationContext(WebConfig.class);


        RequestMappingHandlerMapping handlerMapping = applicationContext.getBean(RequestMappingHandlerMapping.class);

        Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();

        handlerMethods.forEach((k, v) -> {
            System.out.println(k + "-->" + v);
        });
    }
}
