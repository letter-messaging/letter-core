package com.gmail.ivanjermakov1.messenger.messaging.service;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.core.util.Mapper;
import com.gmail.ivanjermakov1.messenger.exception.AuthorizationException;
import com.gmail.ivanjermakov1.messenger.exception.InvalidFileException;
import com.gmail.ivanjermakov1.messenger.exception.NoSuchEntityException;
import com.gmail.ivanjermakov1.messenger.messaging.dto.ImageDto;
import com.gmail.ivanjermakov1.messenger.messaging.dto.NewImageDto;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Image;
import com.gmail.ivanjermakov1.messenger.messaging.enums.FileType;
import com.gmail.ivanjermakov1.messenger.messaging.repository.ImageRepository;
import com.gmail.ivanjermakov1.messenger.util.Uploads;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Transactional
public class ImageService {
	
	private final ImageRepository imageRepository;
	private final FileUploadService fileUploadService;
	
	@Autowired
	public ImageService(ImageRepository imageRepository, FileUploadService fileUploadService) {
		this.imageRepository = imageRepository;
		this.fileUploadService = fileUploadService;
	}
	
	public void delete(Image image) {
		imageRepository.delete(image);
	}
	
	public NewImageDto upload(MultipartFile imageFile) throws IOException {
		if (!Uploads.isSupportedImage(imageFile)) throw new InvalidFileException("provided file is not an image");
		
		return new NewImageDto(fileUploadService.upload(imageFile, FileType.IMAGE));
	}
	
	public void delete(User user, Long imageId) throws NoSuchEntityException, AuthorizationException {
		Image image = imageRepository.findById(imageId)
				.orElseThrow(() -> new NoSuchEntityException("such image does not exist"));
		
		if (!image.getUser().getId().equals(user.getId()))
			throw new AuthorizationException("user can delete only own images");
		
		delete(image);
	}
	
}
