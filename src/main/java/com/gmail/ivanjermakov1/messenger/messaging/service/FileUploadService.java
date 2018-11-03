package com.gmail.ivanjermakov1.messenger.messaging.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class FileUploadService {
	
	@Value("${fileupload.path}")
	private String uploadPlaceholder;
	
	@Value("${web.static.resources.path}")
	private String webResources;
	
	public String getUploadPlaceholder() {
		return uploadPlaceholder;
	}
	
	public String getWebResources() {
		return webResources;
	}
	
	public String upload(MultipartFile multipartFile) throws IOException {
		
		String realPath = uploadPlaceholder;
		new File(realPath).mkdirs();
		
		File file = new File(realPath + "/" + multipartFile.getOriginalFilename());
		multipartFile.transferTo(file);
		
		return webResources + multipartFile.getOriginalFilename();
	}
	
}
