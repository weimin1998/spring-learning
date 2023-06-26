package com.example.springheima.ExceptionHandle;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class Config {
    @ControllerAdvice
    static class MyControllerAdvice {
        @ExceptionHandler
        @ResponseBody
        public Map<String, String> handle(Exception e) {
            Map<String, String> map = new HashMap<>();
            map.put("error handle by advice", e.getMessage());
            return map;
        }
    }
    @Bean
    public ExceptionHandlerExceptionResolver exceptionHandlerExceptionResolver() {
        ExceptionHandlerExceptionResolver exceptionHandlerExceptionResolver = new ExceptionHandlerExceptionResolver();

        exceptionHandlerExceptionResolver.setMessageConverters(Arrays.asList(new MappingJackson2HttpMessageConverter()));//消息转换
        return exceptionHandlerExceptionResolver;
    }
}
