package com.yueny.study.jdk.longaccumulator;

import com.google.common.base.Preconditions;
import org.springframework.util.Assert;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;
import java.util.concurrent.atomic.LongAccumulator;

/**
 * LongAccumulator是LongAdder的功能增强版。
 *
 * LongAdder的API只有对数值的加减，而LongAccumulator提供了自定义的函数操作。
 *
 */
public class LongAccumulatorMain {
    public static void main(String[] args) throws Exception{
        learn();
    }

    private static void testMin() throws Exception{
        // 并发环境下 取最小值
        LongAccumulator accumulator = new LongAccumulator(Long::min, Long.MIN_VALUE);

        Thread[] ts=new Thread[100];
        for(int i=0;i<100;i++){
            ts[i] = new Thread(()->{
                Random random=new Random();
                long value=random.nextLong();
                accumulator.accumulate(value);
            });
            ts[i].start();
        }


        for(int i=0;i<100;i++){
            ts[i].join();
        }

        System.out.println(accumulator.longValue());
    }

    private static  void accumulateMinAndGet(){
        // 取最大值
        LongAccumulator acc = new LongAccumulator(Long::max, 0L);
        acc.accumulate(2);
        Preconditions.checkArgument(acc.get() == 2);
        acc.accumulate(-4);
        Preconditions.checkArgument(acc.get() == 2);
        acc.accumulate(4);
        Preconditions.checkArgument(acc.get() == 4);

        System.out.println(acc.get());
    }

    private static  void learn(){
        // 多线程下累加计算
        // LongAccumulator acc = new LongAccumulator((a,b)->a+b, 0);
        LongAccumulator acc = new LongAccumulator(Long::sum, 0L);

        //下面就是新建两个线程,分别调用一次sum方法
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10000; i++) {
                    //LongAccumulator类的自增操作,这里的参数可以指定每次增加的数字
                    acc.accumulate(2);

                    System.out.println("当前num的值为num= "+ acc);
                }
            }
        }).start();

        new Thread(new Runnable() {

            @Override
            public void run() {
                for (int i = 0; i < 10000; i++) {
                    //LongAccumulator类的自增操作,这里的参数可以指定每次增加的数字
                    acc.accumulate(2);

                    System.out.println("当前num的值为num= "+ acc);
                }
            }
        }).start();


        Preconditions.checkArgument(acc.get() == 40000);

        System.out.println(acc.get());
    }


//    private static  void testSum(){
//        final int incs = 1000000;
//        final int nthreads = 4;
//        final ExecutorService pool = Executors.newCachedThreadPool();
//
//        LongAccumulator num = new LongAccumulator(Long::sum, 0L);
//
//        // 0 -- nthreads 个工作线程，即为 nthreads
//        Phaser phaser = new Phaser(nthreads + 1);
//        for (int i = 0; i < nthreads; ++i){
//            pool.execute(new AccTask(num, phaser, incs));
//        }
//
//        //等待其他任务执行完成, 此时 主线程一直等待
//        phaser.arriveAndAwaitAdvance();
//
//        long expected = incs - 1;
//        long result = num.get();
//
//        System.out.println(result);
//        Preconditions.checkArgument(expected == result);
//
//        pool.shutdown();
//    }

}
