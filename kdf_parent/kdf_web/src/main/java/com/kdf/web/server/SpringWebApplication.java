package com.kdf.web.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * sss
 * 
 * @author 李文龙
 *
 */
@SpringBootApplication
@EntityScan(basePackages = { "com.kdf" })
@ComponentScan(basePackages = { "com.kdf" })
@EnableJpaRepositories(basePackages = { "com.kdf" })
public class SpringWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringWebApplication.class, args);
	}

}
