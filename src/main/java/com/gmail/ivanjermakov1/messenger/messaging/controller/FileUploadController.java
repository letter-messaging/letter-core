package com.gmail.ivanjermakov1.messenger.messaging.controller;

import com.gmail.ivanjermakov1.messenger.messaging.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("upload")
public class FileUploadController {
	
	private final FileUploadService fileUploadService;
	
	@Autowired
	public FileUploadController(FileUploadService fileUploadService) {
		this.fileUploadService = fileUploadService;
	}
	
	@PostMapping
	public String upload(@RequestParam("file") MultipartFile multipartFile) throws IOException {
		return fileUploadService.upload(multipartFile);
	}
	
}
