package com.example.java_base.proxy;

public class BookApiImpl implements BookApi{
    @Override
    public void sell() {
        System.out.println("sell one !");
    }
}
