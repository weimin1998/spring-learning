package com.example.springheima.otherMappingAndAdaptor;

import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;

public class Main {
    public static void main(String[] args) {
        AnnotationConfigServletWebServerApplicationContext applicationContext = new AnnotationConfigServletWebServerApplicationContext(WebConfig.class);

    }
}
