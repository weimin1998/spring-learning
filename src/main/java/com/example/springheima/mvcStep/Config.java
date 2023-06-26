package com.example.springheima.mvcStep;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@Configuration
public class Config {

    @Controller
    static class Controller1{

        @ResponseStatus(HttpStatus.OK)
        public ModelAndView foo(User user){
            System.out.println("foo");
            return null;
        }
    }

    static class User{
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
