package com.gmail.ivanjermakov1.messenger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.gmail.ivanjermakov1.messenger")
public class SpringBootConfig {
	
	public static void main(String[] args) {
		SpringApplication.run(SpringBootConfig.class, args);
	}
	
}
