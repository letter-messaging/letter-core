package com.github.ivanjermakov.lettercore.file.service;

import com.github.ivanjermakov.lettercore.auth.exception.AuthorizationException;
import com.github.ivanjermakov.lettercore.common.exception.NoSuchEntityException;
import com.github.ivanjermakov.lettercore.file.dto.NewDocumentDto;
import com.github.ivanjermakov.lettercore.file.entity.Document;
import com.github.ivanjermakov.lettercore.file.enums.FileType;
import com.github.ivanjermakov.lettercore.file.repository.DocumentRepository;
import com.github.ivanjermakov.lettercore.user.entity.User;
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
