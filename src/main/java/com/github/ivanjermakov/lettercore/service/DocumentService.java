package com.github.ivanjermakov.lettercore.service;

import com.github.ivanjermakov.lettercore.dto.NewDocumentDto;
import com.github.ivanjermakov.lettercore.dto.enums.FileType;
import com.github.ivanjermakov.lettercore.entity.Document;
import com.github.ivanjermakov.lettercore.entity.User;
import com.github.ivanjermakov.lettercore.exception.AuthorizationException;
import com.github.ivanjermakov.lettercore.exception.NoSuchEntityException;
import com.github.ivanjermakov.lettercore.repository.DocumentRepository;
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

	//	TODO: store original document name
	public NewDocumentDto upload(MultipartFile documentFile) throws IOException {
		return new NewDocumentDto(fileUploadService.upload(documentFile, FileType.DOCUMENT));
	}

	public void delete(User user, Long documentId) throws AuthorizationException {
		Document document = documentRepository.findById(documentId)
				.orElseThrow(() -> new NoSuchEntityException("such document does not exist"));

		if (!document.user.id.equals(user.id))
			throw new AuthorizationException("user can delete only own documents");

		documentRepository.delete(document);
	}

}
