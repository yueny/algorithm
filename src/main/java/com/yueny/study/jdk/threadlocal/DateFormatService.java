package com.yueny.study.jdk.threadlocal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * SimpleDateFormat 是线程不安全的类，一般不要定义为static变量，
 * 如果定义为 static，必须加锁，或者使用 DateUtils 工具类。
 *
 *
 * 正例:
 * 注意线程安全，使用 DateUtils。亦推荐如下处理:
 *
 * <pre>
 *     private static final ThreadLocal<DateFormat> df = new ThreadLocal<DateFormat>() { @Override
 *          protected DateFormat initialValue() {
 *          return new SimpleDateFormat("yyyy-MM-dd");
 *          }
 *     };
 * </pre>
 *
 * 说明:如果是 JDK8 的应用，可以使用 Instant 代替 Date，
 * LocalDateTime 代替 Calendar，
 * DateTimeFormatter 代替 SimpleDateFormat，
 *
 * 官方给出的解释:simple beautiful strong immutable thread-safe。
 *
 *
 * 必须回收自定义的ThreadLocal变量，尤其在线程池场景下，线程经常会被复用，
 * 如果不清理自定义的 ThreadLocal 变量，可能会影响后续业务逻辑和造成内存泄露等问题。
 * 尽量在代理中使用 try-finally 块进行回收。
 * <pre>
 *     objectThreadLocal.set(userInfo);
 *     try {
 *          // ...
 *      } finally {
 *          objectThreadLocal.remove();
 *      }
 * </pre>
 */
@Component
@Slf4j
public class DateFormatService {
    private static final ThreadLocal<DateFormat> df = new ThreadLocal<DateFormat>() { @Override
        protected DateFormat initialValue() {
            log.info("ThreadLocal innner 线程池  :{}.", Thread.currentThread().getName());

            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    public String getTime(Date date){
        return df.get().format(date);
    }

}
