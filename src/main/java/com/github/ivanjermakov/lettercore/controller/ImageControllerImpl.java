package com.github.ivanjermakov.lettercore.controller;

import com.github.ivanjermakov.lettercore.dto.NewImageDto;
import com.github.ivanjermakov.lettercore.entity.User;
import com.github.ivanjermakov.lettercore.exception.AuthorizationException;
import com.github.ivanjermakov.lettercore.exception.InvalidEntityException;
import com.github.ivanjermakov.lettercore.exception.NoSuchEntityException;
import com.github.ivanjermakov.lettercore.service.ImageService;
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
public class ImageControllerImpl implements ImageController {

	private final ImageService imageService;

	@Autowired
	public ImageControllerImpl(ImageService imageService) {
		this.imageService = imageService;
	}

	@Override
	@PostMapping("upload")
	public NewImageDto upload(@ModelAttribute User user,
	                          @RequestParam("image") MultipartFile image) throws IOException, InvalidEntityException {
		return imageService.upload(image);
	}

	@Override
	@GetMapping("delete")
	public void delete(@ModelAttribute User user,
	                   @RequestParam("imageId") Long imageId) throws AuthorizationException, NoSuchEntityException {
		imageService.delete(user, imageId);
	}

}
