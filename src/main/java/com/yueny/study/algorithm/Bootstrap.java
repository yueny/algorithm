package com.yueny.study.algorithm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;

/**
 * Quick start<br>
 * 用于快速启动和关闭job服务
 *n7li
 * @author yueny09 <deep_blue_yang@163.com>
 * @DATE 2017年3月9日 下午5:53:29
 */
@SpringBootApplication(scanBasePackages={"com.yueny.study"},
		exclude = {DataSourceAutoConfiguration.class,
		DataSourceTransactionManagerAutoConfiguration.class})
public class Bootstrap {
	/**
	 * 日志记录器
	 */
	private static final Logger logger = LoggerFactory.getLogger(Bootstrap.class);

	public static void main(final String[] args) {
		try {
			// 将Runable转换成Callable
			// Executors.callable(null);

			SpringApplication.run(Bootstrap.class, args);
		} catch (final Exception e) {
			logger.error("服务启动异常:", e);
			e.printStackTrace();
		}

	}


}