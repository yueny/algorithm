package com.yueny.study.console.controller;

import com.google.common.primitives.Ints;
import com.yueny.study.jdk.threadlocal.DateFormatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

/**
 *
 */
@RestController
@Slf4j
public class DateFormatController {
    @Autowired
    private DateFormatService dateFormatService;
    private ThreadLocalRandom rand = ThreadLocalRandom.current();

    // 执行长期任务性能好，创建一个线程池，一池有N个固定的线程，有固定线程数的线程
    private ExecutorService executorService = new ThreadPoolExecutor(5, 5,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>());
    private CompletionService<String> completionService = new ExecutorCompletionService<>(executorService);

    /**
     *
     */
    @RequestMapping(value = "/getTime", method = RequestMethod.GET)
    public List<String> getTime(HttpServletRequest request) {
        Long start = System.currentTimeMillis();

        List<Date> dateRandomList = getRandomDateBetweenMaxAndMin("2000-01-01", "2019-06-31", 6);

        List<String> dateResultList = new ArrayList<>(dateRandomList.size());
        try {
            /**
             * 多线程计算多个时间汇总后返回
             */
            // 全流式处理转换成CompletableFuture[]+组装成一个无返回值CompletableFuture，join等待执行完毕。返回结果whenComplete获取
            CompletableFuture[] futures = dateRandomList.stream()
                    .map(val ->
                        // supplyAsync: 使用ForkJoinPool.commonPool()作为它的线程池执行异步代码，使用指定的thread pool执行异步代码，异步操作有返回值
                        CompletableFuture.supplyAsync(()->calcCallable(val), executorService)
//                            .acceptEither()
                                // 计算结果完成时的处理。 如需获取任务完成先后顺序，此处
                                .whenComplete((dateVal, e) -> {
                                    log.info("任务 {} 完成! result={}，异常 e={}, 完成时间:{}, 耗时：{} ms.", dateVal, dateVal, e, new Date(), (System.currentTimeMillis()-start));

                                    dateResultList.add(dateVal);
                                })
                                // 当计算结算完成之后,后面可以接继续一系列的thenApply, thenApply方法只是用来处理正常值，因此一旦有异常就会抛出。
                                .thenApply(
                                        // 数据格式的转换， 此处不转换
                                        dateVal -> dateVal + "  over"
                                )
                    )
                    .toArray(CompletableFuture[]::new);

            log.info("任务分配完成，分配耗时：{} ms.", (System.currentTimeMillis()-start));

            //等待总任务完成，但是封装后无返回值，必须自己whenComplete()获取
            CompletableFuture.allOf(futures).join();

            log.info("任务完成先后顺序，结果list:{}, 总耗时：{} ms.", dateResultList, (System.currentTimeMillis()-start));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dateResultList;
    }

    private String calcCallable(Date date){
        try {
            int step = rand.nextInt(1000);
            //任务1耗时3秒
            Thread.sleep(3000 + step);

            log.info("task线程 :{} 任务处理数据 {} 完成.", Thread.currentThread().getName(), date);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String val = dateFormatService.getTime(date);
        return val;
    }

    /**
     * 获得时间区间内的所有时间
     *
     * @return
     */
    private List<Date> getDateBetweenMaxAndMin() {
        List<Date> list = new ArrayList<Date>();

        Calendar calendar = Calendar.getInstance();
        calendar.set(2010, 10, 10);
        Date minDate = calendar.getTime();  //最小时间
        calendar.set(2013, 11, 1);
        Date maxDate = calendar.getTime();//最大时间

        //计算两个时间点相隔多少天
        int totalDays = Ints.checkedCast((maxDate.getTime() - minDate.getTime()) / (1000 * 60 * 60 * 24));

        calendar.setTime(minDate);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        for (int i = 0; i <= totalDays; i++) {
            if (i != 0) {
                //天数加1
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }
            list.add(calendar.getTime());
        }

        return list;
    }

    /**
     * 随机获得时间区间内的时间
     *
     * @return
     */
    private List<Date> getRandomDateBetweenMaxAndMin(String beginDate,String endDate, int size) {
        List<Date> list = new ArrayList<Date>();

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date start = format.parse(beginDate);
            Date end = format.parse(endDate);

            if(start.getTime() >= end.getTime()){
                return null;
            }

            for (int i=0; i<size; i++){
                long date = random(start.getTime(),end.getTime());
                list.add(new Date(date));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    private long random(long begin,long end) {
        long rtn = begin + (long) (Math.random() * (end - begin));
        if (rtn == begin || rtn == end) {
            return random(begin, end);
        }
        return rtn;
    }
}
