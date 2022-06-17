package com.example.java_base.concurrent;

import java.util.concurrent.*;

public class ProducerConsumerDemo {
    class Producer implements Runnable{

        @Override
        public void run() {

        }
    }

    class Consumer implements Runnable{

        @Override
        public void run() {

        }
    }

    public static void main(String[] args) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 2, 30, TimeUnit.SECONDS, new LinkedBlockingDeque<>());

    }

    
}
