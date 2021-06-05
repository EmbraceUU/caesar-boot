package com.demo.commonserver.utils;

import java.util.Objects;
import java.util.concurrent.*;

public class ThreadsUtils {

    private static ExecutorService executorService = Executors.newFixedThreadPool(10);
    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 6000, TimeUnit.MICROSECONDS, new ArrayBlockingQueue<>(5));


    public static void main(String[] args) {

        // 实现runnable 的方式
        Thread thread1 = new Thread(() -> System.out.println("this is runnable !"));

        // 实现callable的方式  callable有返回值, 需要通过FutureTask封装, 用get获取
        Thread thread2 = new Thread(new FutureTask<>(
                () -> "this is callable !"
        ));

        // 继承Thread
        String key = "K2";
        String value = "V2";
        System.out.println(Objects.hashCode(key) ^ Objects.hashCode(value));

        // 申请线程数量超过了队列长度 + 线程池最大数量, 触发丢弃并爆出异常策略
        for (int i = 0; i < 16; i ++){
            threadPoolExecutor.submit(new Thread(() -> {
                while (true){
                    System.out.println("this is thread :" + Thread.currentThread().getName());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }));
            System.out.println(i);
        }

    }
}
