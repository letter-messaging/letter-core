package com.gmail.ivanjermakov1.messenger.messaging.controller;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.auth.service.UserService;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.exception.AuthorizationException;
import com.gmail.ivanjermakov1.messenger.exception.InvalidFileException;
import com.gmail.ivanjermakov1.messenger.exception.NoSuchEntityException;
import com.gmail.ivanjermakov1.messenger.messaging.dto.NewImageDto;
import com.gmail.ivanjermakov1.messenger.messaging.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("image")
@Transactional
public class ImageController {
	
	private final UserService userService;
	private final ImageService imageService;
	
	@Autowired
	public ImageController(UserService userService, ImageService imageService) {
		this.userService = userService;
		this.imageService = imageService;
	}
	
	/**
	 * Upload image.
	 *
	 * @param token user token
	 * @param image multipart image file
	 * @return uploaded image
	 * @throws AuthenticationException on invalid @param token
	 * @throws IOException             on server file system error
	 * @throws InvalidFileException    on upload of invalid file (mostly caused by invalid file extension or file size
	 *                                 specified in @value {@code spring.servlet.multipart.max-file-size})
	 */
	@PostMapping("upload")
	public NewImageDto upload(@RequestHeader("Auth-Token") String token,
	                          @RequestParam("image") MultipartFile image) throws AuthenticationException, IOException, InvalidFileException {
		userService.authenticate(token);
		
		return imageService.upload(image);
	}
	
	/**
	 * Delete image from certain message
	 *
	 * @param token   user token
	 * @param imageId image id to delete
	 * @throws AuthenticationException on invalid token
	 * @throws NoSuchEntityException   on invalid avatar id
	 */
	@GetMapping("delete")
	public void delete(@RequestHeader("Auth-Token") String token,
	                   @RequestParam("imageId") Long imageId) throws AuthenticationException, AuthorizationException {
		User user = userService.authenticate(token);
		
		imageService.delete(user, imageId);
	}
	
}
