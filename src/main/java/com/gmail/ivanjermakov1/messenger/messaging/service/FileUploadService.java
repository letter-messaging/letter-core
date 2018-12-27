package com.gmail.ivanjermakov1.messenger.messaging.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;

@Service
@Transactional
public class FileUploadService {
	
	private final static Logger LOG = LoggerFactory.getLogger(FileUploadService.class);
	
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
		LOG.info("uploading file \'" + multipartFile.getName() + "\'; size: " + multipartFile.getSize() / 1_000_000 + "MB");
		
		String realPath = uploadPlaceholder;
		new File(realPath).mkdirs();
		
		File file = new File(realPath + "/" + multipartFile.getOriginalFilename());
		multipartFile.transferTo(file);
		
		return webResources + multipartFile.getOriginalFilename();
	}
	
}
