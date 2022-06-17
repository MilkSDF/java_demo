package com.example.java_base.rpc.simple.impl;


import com.example.java_base.rpc.simple.api.HelloService;

public class HelloServiceImpl implements HelloService {

    public String hello(String name) {
        return "Hello" + name;
    }

}
