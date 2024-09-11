package com.example.shen.accessibility;

import com.example.shen.accessibility.cache.PaymentCache;
import com.example.shen.accessibility.constant.PaymentEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

/**
 * @author Administrator
 */
@Slf4j
@EnableScheduling
@SpringBootApplication
public class DemoApplication {
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	/**
	 * 每分钟执行一次刷新任务
	 */
	@Scheduled(cron = "0 * * * * *")
	public void queryPaymentStatus() {
		log.info("begin refreshing available cache");
		PaymentCache.updatePaymentAvailabilityCache(List.of(PaymentEnum.values()));
		log.info("end refreshing available cache");
	}
}