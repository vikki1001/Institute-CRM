package com.ksv.ktrccrm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAutoConfiguration
@EnableScheduling
@EnableCaching
public class KsvSoftTechApplication {
	public static void main(String[] args) {
		SpringApplication.run(KsvSoftTechApplication.class, args);
	}

}
