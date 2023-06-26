package com.example.springheima.dataconvert;

import org.springframework.beans.SimpleTypeConverter;

import java.util.Date;

public class TestSimpleConverter {
    public static void main(String[] args) {
        SimpleTypeConverter simpleTypeConverter = new SimpleTypeConverter();
        Integer integer = simpleTypeConverter.convertIfNecessary("13", int.class);

        Date date = simpleTypeConverter.convertIfNecessary("1998/08/21", Date.class);
        System.out.println(integer);
        System.out.println(date);
    }
}
