package com.example.java_base.jdksource;

import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class ThreadTest {
    @Test
    public void testState() throws InterruptedException {
        Thread thread = new Thread(() -> {
            System.out.println("StartTime: " + LocalDateTime.now());
            int i = 0;
            try {
                Thread.sleep(10 * 1000);
                while (true) {
                    i++;
                    if (i > Integer.MAX_VALUE >> 1) {
                        break;
                    }
                }
            } catch (InterruptedException e) {
                System.out.println("testState: is interrupted at "
                        + LocalDateTime.now());
                e.printStackTrace();
            }
            System.out.println("  EndTime: " + LocalDateTime.now());
        });
        // NEW
        System.out.println(thread.getState());
        thread.start();
        // RUNNABLE
        System.out.println(thread.getState());
        Thread.sleep(1000);
        // TIMED_WAITING
        System.out.println(thread.getState());
        Thread.sleep(9200);
        // RUNNABLE ??
        System.out.println(thread.getState());
        Thread.sleep(10 * 1000);
        // TERMINATED
        System.out.println(thread.getState());
    }

    @Test
    public void testBlockState() throws InterruptedException {
        Object lock = new Object();
        Thread t1 = new Thread(() -> {
            synchronized (lock) {
                try {
                    Thread.sleep(Integer.MAX_VALUE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread t2 = new Thread(() -> {
            synchronized (lock) {
                System.out.println("thread2 got monitor lock...");
            }
        });
        t1.start();
        Thread.sleep(50);
        t2.start();
        Thread.sleep(50);
        System.out.println(t2.getState());
    }

    @Test
    public void testInterrupt() throws InterruptedException {
        class InterruptTask implements Runnable {
            @Override
            public void run() {
                System.out.println(Thread.interrupted());
                Thread thread = Thread.currentThread();
                while (true) {
                    if (thread.isInterrupted()) {
                        System.out.println("aa");
                    }
//                    try {
//                        Thread.sleep(5 * 1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                }
            }
        }

        Thread thread = new Thread(new InterruptTask());
        thread.start();
        Thread.sleep(2 * 1000);
        thread.interrupt();
    }

    // TODO
    @Test
    public void testInterruptStatus1() throws InterruptedException {
        class InterruptTask implements Runnable {
            @Override
            public void run() {
                long i = 0;
                while (true) {
                    i++;
                }
            }
        }
        Thread thread = new Thread(new InterruptTask());
        thread.start();
        Thread.sleep(1000);
        thread.interrupt();
        System.out.println("thread.isInterrupted() = " + thread.isInterrupted());
        System.out.println("thread.isInterrupted() = " + thread.isInterrupted());
        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(2));
    }

    // TODO
    @Test
    public void testInterruptStatus2() throws InterruptedException {
        class IntDelay implements Delayed {

            private int num;
            private long deadline;

            public IntDelay(int num) {
                this.num = num;
                deadline = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(num);
            }

            @Override
            public long getDelay(TimeUnit unit) {
                return deadline - System.currentTimeMillis();
            }

            @Override
            public int compareTo(Delayed o) {
                IntDelay param = (IntDelay) o;
                return Integer.compare(this.num, param.num);
            }
        }

        class InterruptTask implements Runnable {
            @Override
            public void run() {
                Thread current = Thread.currentThread();
                DelayQueue<IntDelay> queue = new DelayQueue<>();
                queue.add(new IntDelay(1));
                try {
                    System.out.println("Wait  " + LocalDateTime.now());
                    queue.take();
                    System.out.println("Taken " + LocalDateTime.now());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("current.isInterrupted() = " + current.isInterrupted());
                System.out.println("current.isInterrupted() = " + current.isInterrupted());
            }
        }

        Thread thread = new Thread(new InterruptTask());
        thread.start();
        Thread.sleep(500);
        thread.interrupt();
        System.out.println("thread.isInterrupted() = " + thread.isInterrupted());
        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(2));
    }

    @Test
    public void testQueue(){
        PriorityBlockingQueue pbq = new PriorityBlockingQueue(16);
        pbq.offer(1);
        pbq.add(2);
        System.out.println(pbq.peek());
        System.out.println(pbq.size());
    }

    @Test
    public void testWaitLock() throws InterruptedException {
        // ?????? wait ???????????????
        // ???????????????????????????thread1 ??? thread2 ?????????????????????
        // ??????????????? wait ???????????????????????????
        // ????????????????????????????????????????????????????????????
        //
        // ?????????????????????????????????wait ????????????????????????????????????????????????
        Object lock = new Object();
        Thread t1 = new Thread(() -> {
            synchronized (lock) {
                try {
                    System.out.println("thread1 start to wait...");
                    lock.wait(1000);
                    System.out.println("thread1 weak up...");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread t2 = new Thread(() -> {
            synchronized (lock) {
                System.out.println("thread2 got monitor lock...");
            }
        });
        t1.start();
        Thread.sleep(50);
        t2.start();
        Thread.sleep(2000);
    }

    @Test
    public void testSleepLock() throws InterruptedException {
        // ?????? sleep ???????????????
        // ?????????????????????thread1 ??????????????????????????? thread2
        // ??????????????? sleep ?????????????????????
        Object lock = new Object();
        Thread t1 = new Thread(() -> {
            synchronized (lock) {
                try {
                    System.out.println("thread1 start to wait...");
                    Thread.sleep(2000);
                    System.out.println("thread1 weak up...");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread t2 = new Thread(() -> {
            synchronized (lock) {
                System.out.println("thread2 got monitor lock...");
            }
        });
        t1.start();
        Thread.sleep(50);
        t2.start();
        Thread.sleep(3000);
    }


    @Test
    public void testJoin() throws InterruptedException {
        JoinMain.AddThread thread = new JoinMain.AddThread();
        thread.start();
        // ?????????????????????????????????????????? thread ??????????????????i?????????100000???
        // ??????????????????????????????????????????i????????????
        thread.join();
        System.out.println(JoinMain.i);
    }

    static class JoinMain {
        public volatile static int i = 0;

        static class AddThread extends Thread {
            @Override
            public void run() {
                for (i = 0; i < 100000; i++) {
                }
            }
        }
    }

    @Test
    public void testYield() throws InterruptedException {
        Map<Integer, Integer> map = new HashMap<>();
        Integer key = 1;
        Integer key2 = 2;
        Thread thread = new Thread(() -> {
            while (true) {
               Thread.yield();
                Integer num = map.getOrDefault(key, 1);
                map.put(key, ++num);
            }
        });
        Thread thread2 = new Thread(() -> {
            while (true) {
                Integer num = map.getOrDefault(key2, 1);
                map.put(key2, ++num);
            }
        });
        thread.start();
        thread2.start();
        Thread.sleep(1000);
        // ?????? Thread.yield() ???????????? CPU???????????????????????????????????????????????????
        System.out.println(map.toString().replace(",", "\n"));
        System.out.println(thread.getState());
    }

    @Test
    public void testChildThread() {
        // TODO ?????????????????????????????????????????????????????????????????????
        List<Thread> threads = new ArrayList<>();
        Thread thread1 = new Thread(() -> {
            Thread thread = Thread.currentThread();
            ThreadLocal<Integer> threadLocal = ThreadLocal.withInitial(() -> 119);

            Thread child = new Thread(() -> {
            });

            System.out.printf("id=%d, parentId=%d %n", thread.getId(), 123);
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        threads.add(thread1);
        thread1.start();
    }

    @Test
    public void testInterruptNoAction() {
        // ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        Thread thread = new Thread(() -> {
            while (true) {
                Thread.yield();
            }
        });
        thread.start();
        thread.interrupt();
        LockSupport.park();
    }

    @Test
    public void testInterruptAction() {
        Thread thread = new Thread(() -> {
            while (true) {
                Thread.yield();
                // ????????????
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Java??????????????????????????????????????????");
                    return;
                }
            }
        });
        thread.start();
        thread.interrupt();
        LockSupport.parkNanos(TimeUnit.MINUTES.toNanos(1));
    }

    @Test
    public void testInterruptFailure() throws InterruptedException {
        Thread thread = new Thread(() -> {
            while (true) {
                // ????????????
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Java??????????????????????????????????????????");
                    return;
                }

                try {
                    // sleep() ???????????????????????????????????????????????????????????????????????????
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println("Java????????????????????????????????????????????????");
                }
                System.out.println(Thread.currentThread().getState() + " ?????????????????????????????????");
            }
        });
        thread.start();
        Thread.sleep(100); // ???????????????????????????????????????????????????????????????
        thread.interrupt();
        LockSupport.parkNanos(TimeUnit.MINUTES.toNanos(1));
        System.out.println(thread.getState());
    }

    @Test
    public void testInterruptSleep() throws InterruptedException {
        Thread thread = new Thread(() -> {
            while (true) {
                // ????????????
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Java??????????????????????????????????????????");
                    return;
                }

                try {
                    // sleep() ???????????????????????????????????????????????????????????????????????????
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println("Java????????? ?????? ?????????????????????????????????");
                    Thread.currentThread().interrupt();
                }
                System.out.println(Thread.currentThread().getState() + " ?????????????????????????????????");
            }
        });
        thread.start();
        Thread.sleep(100); // ???????????????????????????????????????????????????????????????
        thread.interrupt();
        LockSupport.parkNanos(TimeUnit.MINUTES.toNanos(1));
        System.out.println(thread.getState());
    }

    @Test
    public void testSynchronized() throws InterruptedException {
        class Account {
            int money = 100;

            synchronized void increase() {
                System.out.println("start to increase");
                money -= 10;
                double var = 0;
                for (int i = 0; i < 10000000; i++) {
                    var = Math.PI * Math.E * i;
                    if (i % 2000000 == 0) {
                        throw new RuntimeException("fire");
                    }
                }
                System.out.println("finish increasing." + var);
            }

            synchronized void decrease() {
                System.out.println("start to decrease");
                money += 20;
                System.out.println("finish decreasing.");
            }
        }
        Account account = new Account();
        new Thread(account::increase).start();
        Thread.sleep(1);
        new Thread(account::decrease).start();
        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(30));
        System.out.println(account.money);
    }
}
