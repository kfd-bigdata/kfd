package com.kdf.etl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
@EnableAutoConfiguration
@SpringBootApplication()
@ComponentScan
@EnableScheduling
public class SpringKdfEtlBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringKdfEtlBootApplication.class, args);
	}

}
