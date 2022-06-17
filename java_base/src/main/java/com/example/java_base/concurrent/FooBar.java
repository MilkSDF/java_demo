package com.example.java_base.concurrent;

class FooBar {
//    private int n;
//    private AtomicInteger flag = new AtomicInteger(0);
//    private ReentrantLock lock = new ReentrantLock();
//    private Condition condition1 = lock.newCondition();
//    private Condition condition2 = lock.newCondition();
//
//    public FooBar(int n) {
//        this.n = n;
//    }
//
//    public void foo(Runnable printFoo) throws InterruptedException {
//        for (int i = 0; i < n; i++) {
//            lock.lock();
//            try {
//                while (flag.get() != 0) {
//                    condition1.await();
//                }
//
//                printFoo.run();
//                flag.set(1);
//                condition2.signal();
//            } finally {
//                lock.unlock();
//            }
//        }
//    }
//
//    public void bar(Runnable printBar) throws InterruptedException {
//        for (int i = 0; i < n; i++) {
//            lock.lock();
//            try {
//                while (flag.get() != 1) {
//                    condition2.await();
//                }
//
//                printBar.run();
//                flag.set(0);
//                condition1.signal();
//            } finally {
//                lock.unlock();
//            }
//        }
//    }

    private int n;
    // 标志位，控制执行顺序，true执行printFoo，false执行printBar
    private volatile boolean type;
    // 锁标志
    private final Object object;

    public FooBar(int n) {
        this.n = n;
        object = new Object();
        type = true;
    }

    public void foo(Runnable printFoo) throws InterruptedException {

        for (int i = 0; i < n; i++) {
            // printFoo.run() outputs "foo". Do not change or remove this line.
            synchronized (object) {
                // 初始化type为true,所以肯定会先执行printFoo
                while (!type) {
                    object.wait();
                }
                printFoo.run();
                // 设置false
                type = false;
                // 唤醒printBar执行
                object.notifyAll();
            }
        }
    }

    public void bar(Runnable printBar) throws InterruptedException {

        for (int i = 0; i < n; i++) {
            // printBar.run() outputs "bar". Do not change or remove this line.
            synchronized (object) {
                // 初始化为true，这里会阻塞
                while (type) {
                    object.wait();
                }
                printBar.run();
                type = true;
                object.notifyAll();
            }
        }
    }
}
