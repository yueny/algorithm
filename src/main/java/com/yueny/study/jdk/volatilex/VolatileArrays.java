package com.yueny.study.jdk.volatilex;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author fengyang <deep_blue_yang@126.com>
 * @date 2020/9/28  2:58 下午
 * @title: VolatileArrays
 * @projectName algorithm
 * @description:
 */
public class VolatileArrays {
    // 不保证原子性
    static volatile int[] arrays = {1,1,1,1,1};
    // 不保证原子性
    static int x = 0;
    // 不保证原子性
    static volatile int y = 0;
    // 保证原子性
    static volatile AtomicInteger z = new AtomicInteger(0);

    private static ExecutorService executorService = Executors.newFixedThreadPool(5);

    /**
     * 模拟并发线程
     *
     * @param args
     */
    public static void main(String []args) {
        System.out.println("模拟操作前：");
        System.out.println(Arrays.toString(arrays));
        System.out.println("X:" + x);
        System.out.println("Y:" + y);
        System.out.println("Z:" + z);

        // 模拟 1000 个线程进行 arrays、x、y、z 的累加
        for (int i=0;i<10000;i++){
            executorService.submit(new ChangeNum());
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("模拟操作结束， 执行1000次，期望均为 1000。");
        System.out.println(Arrays.toString(arrays));
        System.out.println("X:" + x);
        System.out.println("Y:" + y);
        System.out.println("Z:" + z);
    }

    static class ChangeNum implements Runnable{
        @Override
        public void run() {
            /*
             * 数据全部各自加1
             */
            arrays[0]=arrays[0]+1;
            arrays[1]=arrays[1]+1;
            arrays[2]=arrays[2]+1;
            arrays[3]=arrays[3]+1;
            arrays[4]=arrays[4]+1;

            x=x+1;
            y=y+1;
            z.incrementAndGet();
        }
    }

}
