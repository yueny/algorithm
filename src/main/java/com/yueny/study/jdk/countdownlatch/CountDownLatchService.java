package com.yueny.study.jdk.countdownlatch;

/**
 * 使用 CountDownLatch 进行异步转同步操作，每个线程退出前必须调用 countDown 方法，
 * 线程执行代码注意 catch 异常，确保 countDown 方法被执行到，避免主线程无法执行 至 await 方法，直到超时才返回结果。
 *
 * 说明:注意，子线程抛出异常堆栈，不能在主线程 try-catch 到。
 *
 */
public class CountDownLatchService {
}
