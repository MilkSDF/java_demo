package com.example.java_base.rpc.simple;


import com.example.java_base.rpc.simple.api.HelloService;
import com.example.java_base.rpc.simple.impl.HelloServiceImpl;

public class RpcProvider {
    public static void main(String[] args) throws Exception {
        HelloService service = (HelloService) new HelloServiceImpl();
        RpcFramework.export(service, 1234);
    }
}
