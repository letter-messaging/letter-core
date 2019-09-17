package com.gmail.ivanjermakov1.messenger.controller;

import com.gmail.ivanjermakov1.messenger.dto.NewDocumentDto;
import com.gmail.ivanjermakov1.messenger.entity.User;
import com.gmail.ivanjermakov1.messenger.exception.AuthorizationException;
import com.gmail.ivanjermakov1.messenger.exception.NoSuchEntityException;
import com.gmail.ivanjermakov1.messenger.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("document")
@Transactional
public class DocumentControllerImpl implements DocumentController {

	private final DocumentService documentService;

	@Autowired
	public DocumentControllerImpl(DocumentService documentService) {
		this.documentService = documentService;
	}

	@Override
	@PostMapping("upload")
	public NewDocumentDto upload(@ModelAttribute User user,
	                             @RequestParam("document") MultipartFile document) throws IOException {
		return documentService.upload(document);
	}

	@Override
	@GetMapping("delete")
	public void delete(@ModelAttribute User user,
	                   @RequestParam("documentId") Long documentId) throws AuthorizationException, NoSuchEntityException {
		documentService.delete(user, documentId);
	}

}
