package com.gmail.ivanjermakov1.messenger.service;

import com.gmail.ivanjermakov1.messenger.dto.NewImageDto;
import com.gmail.ivanjermakov1.messenger.dto.enums.FileType;
import com.gmail.ivanjermakov1.messenger.entity.Image;
import com.gmail.ivanjermakov1.messenger.entity.Message;
import com.gmail.ivanjermakov1.messenger.entity.User;
import com.gmail.ivanjermakov1.messenger.exception.AuthorizationException;
import com.gmail.ivanjermakov1.messenger.exception.InvalidFileException;
import com.gmail.ivanjermakov1.messenger.exception.NoSuchEntityException;
import com.gmail.ivanjermakov1.messenger.repository.ImageRepository;
import com.gmail.ivanjermakov1.messenger.repository.MessageRepository;
import com.gmail.ivanjermakov1.messenger.util.Uploads;
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
		if (!Uploads.isSupportedImage(imageFile)) throw new InvalidFileException("provided file is not an image");

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
