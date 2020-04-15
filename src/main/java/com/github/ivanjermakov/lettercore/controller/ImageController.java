package com.github.ivanjermakov.lettercore.controller;

import com.github.ivanjermakov.lettercore.dto.NewImageDto;
import com.github.ivanjermakov.lettercore.entity.User;
import com.github.ivanjermakov.lettercore.exception.AuthorizationException;
import com.github.ivanjermakov.lettercore.exception.InvalidEntityException;
import com.github.ivanjermakov.lettercore.exception.NoSuchEntityException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageController {

	/**
	 * Upload image.
	 *
	 * @param user  authenticated user. automatically maps, when {@literal Auth-Token} parameter present
	 * @param image multipart image file
	 * @return uploaded image
	 * @throws IOException            on server file system error
	 * @throws InvalidEntityException on upload of invalid file (mostly caused by invalid file extension or file size
	 *                                specified in @value {@code spring.servlet.multipart.max-file-size})
	 */
	NewImageDto upload(User user, MultipartFile image) throws IOException;

	/**
	 * Delete image from certain message
	 *
	 * @param user    authenticated user. automatically maps, when {@literal Auth-Token} parameter present
	 * @param imageId image id to delete
	 * @throws NoSuchEntityException  on invalid image id
	 * @throws AuthorizationException if user is not an a sender of a message image attached to
	 */
	void delete(User user, Long imageId);

}
