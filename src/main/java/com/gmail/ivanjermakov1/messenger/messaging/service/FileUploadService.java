package com.gmail.ivanjermakov1.messenger.messaging.service;

import com.gmail.ivanjermakov1.messenger.messaging.enums.FileType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class FileUploadService {
	
	private final static Logger LOG = LoggerFactory.getLogger(FileUploadService.class);
	
	@Value("${fileupload.path}")
	private String uploadPlaceholder;
	
	@Value("${web.static.resources.path}")
	private String webResources;
	
	public String upload(MultipartFile multipartFile, FileType fileType) throws IOException {
		LOG.info("uploading file \'" + multipartFile.getOriginalFilename() + "\'; size: " + multipartFile.getSize() / 1_000_000 + "MB");
		
		String realPath = uploadPlaceholder;
		new File(realPath + "/" + fileType.toString().toLowerCase()).mkdirs();
		
		File file = new File(realPath + "/" + fileType.toString().toLowerCase() + "/" + multipartFile.getOriginalFilename());
		multipartFile.transferTo(file);
		
		return fileType.toString().toLowerCase() + "/" + multipartFile.getOriginalFilename();
	}
	
}
