package com.example.springheima.returnValue;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
public class Config {
    static class ReturnValController {
        public ModelAndView test1() {
            System.out.println("test1");
            ModelAndView modelAndView = new ModelAndView("view1");
            modelAndView.addObject("name", "zhangsan");
            return modelAndView;
        }

        public String test2() {
            System.out.println("test2");
            return "view2";
        }

        @ModelAttribute// 方法的返回值会加到model中，mavContainer
        public User test3() {
            System.out.println("test3");
            User user = new User();
            user.setName("lisi");
            return user;
        }

        public User test4() {
            System.out.println("test4");
            User user = new User();
            user.setName("wangwu");
            return user;
        }

        public HttpEntity<User> test5() {
            System.out.println("test5");
            User user = new User();
            user.setName("zhaoliu");
            return new HttpEntity<>(user);
        }

        public HttpHeaders test6() {
            System.out.println("test6");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Content-Type", "text/html");
            return httpHeaders;
        }

        @ResponseBody
        public User test7(){
            System.out.println("test7");
            User user = new User();
            user.setName("7777");
            return user;
        }
    }

    static class User {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }


    @Bean
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/templates/");
        viewResolver.setSuffix(".jsp");
        viewResolver.setContentType("text/html;charset=utf-8");
        return viewResolver;
    }


//    @Bean
//    public FreeMarkerViewResolver viewResolver(){
//        FreeMarkerViewResolver freeMarkerViewResolver = new FreeMarkerViewResolver();
//        freeMarkerViewResolver.setContentType("text/html;charset=utf-8");
//        freeMarkerViewResolver.setPrefix("");
//        freeMarkerViewResolver.setSuffix("");
//        freeMarkerViewResolver.setExposeSpringMacroHelpers(false);
//        return freeMarkerViewResolver;
//    }
}
