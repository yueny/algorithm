package com.yueny.study.leetcode.daoyou;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 导游早上十点带着3位游客来到一景区，
 * 一共需要游览三个景点，分别为A、B、C,D为出口（终点）。
 *
 * 现在所有人从A出发自有行，但是必须所有人上午11点在B景点集合完成后，
 * 再出发到C，最后13点在D出口处集合统一大巴去其他景区。
 * 请使用java多线程实现以上场景。
 *
 *
 * 思路
 * 1. 栅栏
 * 2. completable future
 * 3. CyclicBarrier
 */
public class DaoyouMain {
    @AllArgsConstructor
    @Getter
    @Setter
    public static class People {
        private String userName;

        public void dosomething(){
            System.out.println("我" + userName + "在步行中");

            try{
                TimeUnit.SECONDS.sleep(3L);
            } catch (Exception e){
                e.printStackTrace();
            }

            System.out.println("我" + userName + " 到了终点啦！");
        }
    }

    @AllArgsConstructor
    public static class PeopleTask implements Runnable {
        private People people;
        private CountDownLatch ready;
        private CountDownLatch endGate;

        public void run() {
            try{
                System.out.println("我" + people.getUserName() + " 在起点，等待出发。");

                // 挂起等人齐
                ready.await();

                try {
                    people.dosomething();
                }finally {
                    // 到达后减1
                    endGate.countDown();
                }
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    public static void main(final String[] args) {
        //人数
        int peopleCount = 3;
        String[] peopleArr = {"A", "B", "C"};

        // 调用await()方法的线程会被挂起，它会等待直到count值为0才继续执行



        /*
         * 三人自行从 A 出发
         */
        // 现在是 10点
        long start = System.nanoTime();
        CountDownLatch ready = new CountDownLatch(1);
        // 每个人是否已到达
        // endGate.getCount() 表示未到齐人数
        CountDownLatch endGate = new CountDownLatch(peopleCount);
        for (int i = 0; i < peopleCount; i++) {
            final People people = new People(peopleArr[i]);

            Thread t = new Thread(new PeopleTask(people, ready, endGate));
            t.start();
        }
        // 准备好了，大家同时出发
        ready.countDown();

        try {
            //11点在B景区全部人到齐
            endGate.await(1, TimeUnit.HOURS);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        if(endGate.getCount() != 0){
            System.out.println("A-->B 人未到齐，应到：" +  peopleCount + ", 实到: " + (peopleCount-endGate.getCount()));
            return;
        }

        long end = System.nanoTime();
        System.out.println("A-->B 人齐了，开始再次各自出发去C点，耗时:" + (end-start));

        System.out.println();
        System.out.println("##################");
        System.out.println();


        /*
         * 三人自行从 B 出发
         */
        start = System.nanoTime();  // 现在11点
        endGate = new CountDownLatch(peopleCount);
        ready = new CountDownLatch(1);
        for (int i = 0; i < peopleCount; i++) {
            People people = new People(peopleArr[i]);

            Thread t = new Thread(new PeopleTask(people, ready, endGate));
            t.start();
        }
        // 准备好了，大家同时出发
        ready.countDown();

        try {
            //最优实现是取目的时间减现在时间的剩余秒数。 此处不优化
            endGate.await(1, TimeUnit.HOURS);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        if(endGate.getCount() != 0){
            System.out.println("B-->C 人未到齐，应到：" +  peopleCount + ", 实到: " + (peopleCount-endGate.getCount()));
            return;
        }

        end = System.nanoTime();
        System.out.println("B-->C 人齐了，开始再次各自出发去D点，耗时:" + (end-start));



        System.out.println();
        System.out.println("##################");
        System.out.println();


        /*
         * 三人自行从 C 出发
         */
        start = System.nanoTime();
        endGate = new CountDownLatch(peopleCount);
        ready = new CountDownLatch(1);
        for (int i = 0; i < peopleCount; i++) {
            People people = new People(peopleArr[i]);

            Thread t = new Thread(new PeopleTask(people, ready, endGate));
            t.start();
        }
        // 准备好了，大家同时出发
        ready.countDown();

        try {
            //最优实现是取目的时间减现在时间的剩余秒数。 此处不优化
            endGate.await(1, TimeUnit.SECONDS);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        if(endGate.getCount() != 0){
            System.out.println("C-->D 人未到齐，应到：" +  peopleCount + ", 实到: " + (peopleCount-endGate.getCount()));
            return;
        }

        end = System.nanoTime();
        System.out.println("C-->D 人齐了，回家，耗时:" + (end-start));


        System.out.println();
        System.out.println("##################");
        System.out.println();

    }


}
