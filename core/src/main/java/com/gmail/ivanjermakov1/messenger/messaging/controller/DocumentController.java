package com.gmail.ivanjermakov1.messenger.messaging.controller;

import com.gmail.ivanjermakov1.messenger.auth.service.UserService;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.messaging.dto.NewDocumentDto;
import com.gmail.ivanjermakov1.messenger.messaging.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("document")
@Transactional
public class DocumentController {
	
	private final UserService userService;
	private final DocumentService documentService;
	
	@Autowired
	public DocumentController(UserService userService, DocumentService documentService) {
		this.userService = userService;
		this.documentService = documentService;
	}
	
	/**
	 * Upload document.
	 *
	 * @param token    user token
	 * @param document multipart document file
	 * @return uploaded document
	 * @throws AuthenticationException on invalid @param token
	 * @throws IOException             on server file system error
	 */
	@PostMapping("upload")
	public NewDocumentDto upload(@RequestHeader("Auth-Token") String token,
	                             @RequestParam("document") MultipartFile document) throws AuthenticationException, IOException {
		userService.authenticate(token);
		
		return documentService.upload(document);
	}
	
}
