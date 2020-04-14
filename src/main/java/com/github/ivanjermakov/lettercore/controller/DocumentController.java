package com.github.ivanjermakov.lettercore.controller;

import com.github.ivanjermakov.lettercore.dto.NewDocumentDto;
import com.github.ivanjermakov.lettercore.entity.User;
import com.github.ivanjermakov.lettercore.exception.AuthorizationException;
import com.github.ivanjermakov.lettercore.exception.NoSuchEntityException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface DocumentController {

	/**
	 * Upload document.
	 *
	 * @param user     authenticated user. automatically maps, when {@literal Auth-Token} parameter present
	 * @param document multipart document file
	 * @return uploaded document
	 * @throws IOException on server file system error
	 */
	NewDocumentDto upload(User user, MultipartFile document) throws IOException;

	/**
	 * Delete document from certain message
	 *
	 * @param user       authenticated user. automatically maps, when {@literal Auth-Token} parameter present
	 * @param documentId document id to delete
	 * @throws NoSuchEntityException  on invalid document id
	 * @throws AuthorizationException if user is not an a sender of a message document attached to
	 */
	void delete(User user, Long documentId);

}
