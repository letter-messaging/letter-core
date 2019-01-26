package com.gmail.ivanjermakov1.messenger.messaging.controller;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.auth.service.UserService;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.exception.InvalidFileException;
import com.gmail.ivanjermakov1.messenger.exception.NoSuchEntityException;
import com.gmail.ivanjermakov1.messenger.messaging.dto.NewImageDto;
import com.gmail.ivanjermakov1.messenger.messaging.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("image")
public class ImageController {
	
	private final UserService userService;
	private final ImageService imageService;
	
	@Autowired
	public ImageController(UserService userService, ImageService imageService) {
		this.userService = userService;
		this.imageService = imageService;
	}
	
	@PostMapping("upload")
	public NewImageDto upload(@RequestHeader("Auth-Token") String token, @RequestParam("avatar") MultipartFile avatar) throws AuthenticationException, IOException, InvalidFileException {
		User user = userService.authenticate(token);
		
		return imageService.upload(avatar);
	}
	
	@GetMapping("delete")
	public void delete(@RequestHeader("Auth-Token") String token, @RequestParam("imageId") Long imageId) throws AuthenticationException, NoSuchEntityException {
		User user = userService.authenticate(token);
		
		imageService.delete(user, imageId);
	}
	
}
