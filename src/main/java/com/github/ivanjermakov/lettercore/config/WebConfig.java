package com.github.ivanjermakov.lettercore.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web configuration of SpringMVC
 */
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

	/**
	 * Configure correct CORS policy for same host and different port REST API access
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins("*");
	}

}
