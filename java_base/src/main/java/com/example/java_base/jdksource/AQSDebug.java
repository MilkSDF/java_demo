package com.example.java_base.jdksource;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AQSDebug {
    private Lock lock = new ReentrantLock();

    private void sayHello(){
//        try {
//            Thread.sleep(100);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        lock.lock();
        System.out.println("hello");
        lock.unlock();
    }

    public static void main(String[] args) {
        AQSDebug aqsDebug = new AQSDebug();
        new Thread(aqsDebug::sayHello, "first").start();
        new Thread(aqsDebug::sayHello, "second").start();
        new Thread(aqsDebug::sayHello, "third").start();
    }

    private static class Sync extends AbstractQueuedSynchronizer {

    }
}
