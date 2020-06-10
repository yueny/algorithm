package com.yueny.study.jdk.longaccumulator;

import lombok.AllArgsConstructor;

import java.util.concurrent.Phaser;
import java.util.concurrent.atomic.LongAccumulator;

@AllArgsConstructor
public class AccTask implements Runnable {
    private LongAccumulator num;
    private Phaser phaser;
    private int incs;

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        num.accumulate(1);
        phaser.arriveAndDeregister();
    }
}
