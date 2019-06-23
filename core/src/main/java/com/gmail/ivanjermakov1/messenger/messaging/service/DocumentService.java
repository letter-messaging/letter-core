package com.gmail.ivanjermakov1.messenger.messaging.service;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.exception.AuthorizationException;
import com.gmail.ivanjermakov1.messenger.exception.NoSuchEntityException;
import com.gmail.ivanjermakov1.messenger.messaging.dto.NewDocumentDto;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Document;
import com.gmail.ivanjermakov1.messenger.messaging.enums.FileType;
import com.gmail.ivanjermakov1.messenger.messaging.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class DocumentService {
	
	private final FileUploadService fileUploadService;
	private final DocumentRepository documentRepository;
	
	@Autowired
	public DocumentService(FileUploadService fileUploadService, DocumentRepository documentRepository) {
		this.fileUploadService = fileUploadService;
		this.documentRepository = documentRepository;
	}
	
	public void delete(Document document) {
		documentRepository.delete(document);
	}
	
	//	TODO: store original document name
	public NewDocumentDto upload(MultipartFile documentFile) throws IOException {
		return new NewDocumentDto(fileUploadService.upload(documentFile, FileType.DOCUMENT));
	}
	
	public void delete(User user, Long documentId) throws AuthorizationException {
		Document document = documentRepository.findById(documentId)
				.orElseThrow(() -> new NoSuchEntityException("such document does not exist"));
		
		if (!document.getUser().getId().equals(user.getId()))
			throw new AuthorizationException("user can delete only own documents");
		
		documentRepository.delete(document);
	}
	
}
