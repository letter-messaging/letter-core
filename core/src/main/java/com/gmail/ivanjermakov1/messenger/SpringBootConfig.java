package com.gmail.ivanjermakov1.messenger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

//TODO: better dto validation
@SpringBootApplication
@EnableScheduling
public class SpringBootConfig {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootConfig.class, args);
	}

}
