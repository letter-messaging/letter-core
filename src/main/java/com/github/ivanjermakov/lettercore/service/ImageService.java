package com.github.ivanjermakov.lettercore.service;

import com.github.ivanjermakov.lettercore.dto.NewImageDto;
import com.github.ivanjermakov.lettercore.dto.enums.FileType;
import com.github.ivanjermakov.lettercore.entity.Image;
import com.github.ivanjermakov.lettercore.entity.Message;
import com.github.ivanjermakov.lettercore.entity.User;
import com.github.ivanjermakov.lettercore.exception.AuthorizationException;
import com.github.ivanjermakov.lettercore.exception.InvalidEntityException;
import com.github.ivanjermakov.lettercore.exception.NoSuchEntityException;
import com.github.ivanjermakov.lettercore.repository.ImageRepository;
import com.github.ivanjermakov.lettercore.repository.MessageRepository;
import com.github.ivanjermakov.lettercore.util.Uploads;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ImageService {

	private final ImageRepository imageRepository;
	private final MessageRepository messageRepository;
	private final FileUploadService fileUploadService;

	@Autowired
	public ImageService(ImageRepository imageRepository, FileUploadService fileUploadService, MessageRepository messageRepository) {
		this.imageRepository = imageRepository;
		this.fileUploadService = fileUploadService;
		this.messageRepository = messageRepository;
	}

	public NewImageDto upload(MultipartFile imageFile) throws IOException {
		if (!Uploads.isSupportedImage(imageFile)) throw new InvalidEntityException("provided file is not an image");

		return new NewImageDto(fileUploadService.upload(imageFile, FileType.IMAGE));
	}

	public void delete(User user, Long imageId) throws AuthorizationException {
		Image image = imageRepository.findById(imageId)
				.orElseThrow(() -> new NoSuchEntityException("such image does not exist"));

		if (!image.user.id.equals(user.id))
			throw new AuthorizationException("user can delete only own images");

//		also remove image from messages
		Message message = image.message;
		if (message != null) {
			message.images.removeIf(i -> i.id.equals(imageId));
		}
		messageRepository.save(message);

		imageRepository.delete(image);
	}

}
