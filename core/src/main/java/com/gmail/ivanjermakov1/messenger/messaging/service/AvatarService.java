package com.gmail.ivanjermakov1.messenger.messaging.service;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.core.util.Mapper;
import com.gmail.ivanjermakov1.messenger.exception.InvalidFileException;
import com.gmail.ivanjermakov1.messenger.exception.NoSuchEntityException;
import com.gmail.ivanjermakov1.messenger.messaging.dto.AvatarDto;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Avatar;
import com.gmail.ivanjermakov1.messenger.messaging.enums.FileType;
import com.gmail.ivanjermakov1.messenger.messaging.repository.AvatarRepository;
import com.gmail.ivanjermakov1.messenger.util.Uploads;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AvatarService {
	
	private final AvatarRepository avatarRepository;
	private final FileUploadService fileUploadService;
	
	@Autowired
	public AvatarService(AvatarRepository avatarRepository, FileUploadService fileUploadService) {
		this.avatarRepository = avatarRepository;
		this.fileUploadService = fileUploadService;
	}
	
	public Optional<Avatar> getCurrent(User user) {
		return avatarRepository.getByUserId(user.getId())
				.stream()
				.max(Comparator.comparing(Avatar::getUploaded));
	}
	
	//	TODO: delete physical file from file system
	public void delete(User user, Long avatarId) throws NoSuchEntityException {
		if (avatarRepository.getByUserId(user.getId()).stream().noneMatch(a -> a.getId().equals(avatarId)))
			throw new NoSuchEntityException("invalid avatar id");
		
		avatarRepository.deleteAvatarById(avatarId);
	}
	
	public List<Avatar> getAll(User user) {
		return new ArrayList<>(avatarRepository.getByUserId(user.getId()));
	}
	
	public AvatarDto upload(User user, MultipartFile avatarFile) throws IOException, InvalidFileException {
		if (!Uploads.isSupportedImage(avatarFile)) throw new InvalidFileException("provided file is not an image");
		
		String avatarPath = fileUploadService.upload(avatarFile, FileType.AVATAR);
		Avatar avatar = avatarRepository.save(new Avatar(user, avatarPath, LocalDate.now()));
		return Mapper.map(avatar, AvatarDto.class);
	}
	
}
