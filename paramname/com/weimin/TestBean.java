package com.weimin;

public class TestBean {
    public void foo(String name,int age){
        // javac -g .\TestBean.java
        // asm可以获取
/*
        LocalVariableTable:
        Start  Length  Slot  Name   Signature
        0       1     0  this   Lcom/weimin/TestBean;
        0       1     1  name   Ljava/lang/String;
        0       1     2   age   I
*/

        // javac -parameters .\TestBean.java
        // 反射api可以获取
/*        MethodParameters:
        Name                           Flags
        name
        age
*/

    }
}
