package com.example.springheima.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class TestController {

    @RequestMapping("/test")
    @ResponseBody
    public String test(@Token String token) {
        System.out.println("token:" + token);
        return "test...";
    }

    @GetMapping("/test1")
    @ResponseBody
    public String test1(String token) {
        return "test1";
    }

    @PostMapping("/test2")
    @ResponseBody
    public String test2(String token) {
        return "test2";
    }

    @PostMapping("/test3")
    @Yml
    public User test3() {
        return new User("weimin", 24);
    }
}
