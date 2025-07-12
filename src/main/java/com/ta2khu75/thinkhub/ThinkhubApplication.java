package com.ta2khu75.thinkhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableScheduling
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@EnableTransactionManagement
@SpringBootApplication
public class ThinkhubApplication {

	public static void main(String[] args) {
		SpringApplication.run(ThinkhubApplication.class, args);
	}

}
