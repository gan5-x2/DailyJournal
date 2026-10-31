package org.example.dailyjournal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling; // 1. 导入这个

@SpringBootApplication
@EnableScheduling // 2. 加上这个注解
public class DailyJournalApplication {

	public static void main(String[] args) {
		SpringApplication.run(DailyJournalApplication.class, args);
	}
}
