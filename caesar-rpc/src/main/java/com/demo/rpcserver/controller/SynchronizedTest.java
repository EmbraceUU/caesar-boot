package com.demo.rpcserver.controller;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SynchronizedTest {

    private void synchronzedTest(){

        ExecutorService executorService = Executors.newFixedThreadPool(3);

        List<String> list = Arrays.asList(
                "123","234","345","456","567","678",
                "123","234","345","456","567","678",
                "123","234","345","456","567","678",
                "123", "234","345","456","567","678",
                "123", "234","345","456","567","678");

        Queue<String> queue = new LinkedList<>();

        int count = 10;

        while (count > 0){
            for (String str: list){
                queue.add(str);
            }
            for (String str : list){
                executorService.submit(() -> {
                    synchronized(str){
                        System.out.println("begin: "+ Thread.currentThread() + " " + str);
                        try {
                            Thread.sleep(5000);

                            // 执行update操作
                            //
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("end: "+ Thread.currentThread() + " " + str);
                    }
                });
            }
            count --;
        }


        executorService.shutdown();
    }


    /**
     * 1. 当list size >= 线程池 size 时, 因为list中的order_id不会相同,
     * 所以synchronized代码块不会发生阻塞, 主线程会因为线程池资源不够发生阻塞
     * 2. 当list size < 线程池 size 时, 主线程不会因为线程池发生阻塞,
     * 但是synchronized会对相同的order_id发生阻塞, 在
     */

    public static void main(String[] args) {
        SynchronizedTest synchronizedTest = new SynchronizedTest();
        synchronizedTest.synchronzedTest();
    }
}
