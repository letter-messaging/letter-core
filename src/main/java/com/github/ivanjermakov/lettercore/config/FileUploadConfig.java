package com.github.ivanjermakov.lettercore.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class FileUploadConfig implements WebMvcConfigurer {

	@Value("${fileupload.path}")
	private String fileuploadPath;

	@Value("${web.static.resources.path}")
	private String webStaticResourcesPath;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry
				.addResourceHandler("/" + webStaticResourcesPath + "**")
				.addResourceLocations("file://" + fileuploadPath);
	}

}
