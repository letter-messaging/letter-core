package com.github.ivanjermakov.lettercore.file.controller;

import com.github.ivanjermakov.lettercore.auth.exception.AuthorizationException;
import com.github.ivanjermakov.lettercore.common.exception.InvalidEntityException;
import com.github.ivanjermakov.lettercore.common.exception.NoSuchEntityException;
import com.github.ivanjermakov.lettercore.file.dto.NewImageDto;
import com.github.ivanjermakov.lettercore.file.service.ImageService;
import com.github.ivanjermakov.lettercore.user.entity.User;
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
@RequestMapping("image")
@Transactional
public class ImageController {

	private final ImageService imageService;

	@Autowired
	public ImageController(ImageService imageService) {
		this.imageService = imageService;
	}

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
	@PostMapping("upload")
	public NewImageDto upload(@ModelAttribute User user,
	                          @RequestParam("image") MultipartFile image) throws IOException, InvalidEntityException {
		return imageService.upload(image);
	}

	/**
	 * Delete image from certain message
	 *
	 * @param user    authenticated user. automatically maps, when {@literal Auth-Token} parameter present
	 * @param imageId image id to delete
	 * @throws NoSuchEntityException  on invalid image id
	 * @throws AuthorizationException if user is not an a sender of a message image attached to
	 */
	@GetMapping("delete")
	public void delete(@ModelAttribute User user,
	                   @RequestParam("imageId") Long imageId) throws AuthorizationException, NoSuchEntityException {
		imageService.delete(user, imageId);
	}

}
