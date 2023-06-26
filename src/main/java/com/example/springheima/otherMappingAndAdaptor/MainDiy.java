package com.example.springheima.otherMappingAndAdaptor;

import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;

public class MainDiy {
    public static void main(String[] args) {
        AnnotationConfigServletWebServerApplicationContext applicationContext = new AnnotationConfigServletWebServerApplicationContext(WebConfigDIY.class);

    }
}
