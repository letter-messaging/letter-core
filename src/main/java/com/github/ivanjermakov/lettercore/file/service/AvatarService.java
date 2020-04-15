package com.github.ivanjermakov.lettercore.file.service;

import com.github.ivanjermakov.lettercore.common.exception.InvalidEntityException;
import com.github.ivanjermakov.lettercore.common.exception.NoSuchEntityException;
import com.github.ivanjermakov.lettercore.file.dto.AvatarDto;
import com.github.ivanjermakov.lettercore.file.entity.Avatar;
import com.github.ivanjermakov.lettercore.file.enums.FileType;
import com.github.ivanjermakov.lettercore.file.mapper.AvatarMapper;
import com.github.ivanjermakov.lettercore.file.repository.AvatarRepository;
import com.github.ivanjermakov.lettercore.user.entity.User;
import com.github.ivanjermakov.lettercore.util.Uploads;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class AvatarService {

	private final AvatarRepository avatarRepository;
	private final FileUploadService fileUploadService;

	private AvatarMapper avatarMapper;

	@Autowired
	public AvatarService(AvatarRepository avatarRepository, FileUploadService fileUploadService) {
		this.avatarRepository = avatarRepository;
		this.fileUploadService = fileUploadService;
	}

	@Autowired
	public void setAvatarMapper(AvatarMapper avatarMapper) {
		this.avatarMapper = avatarMapper;
	}

	public Optional<Avatar> getCurrent(User user) {
		return avatarRepository.getByUserId(user.id)
				.stream()
				.max(Comparator.comparing(a -> a.uploaded));
	}

	//	TODO: delete physical file from file system
	public void delete(User user, Long avatarId) {
		if (avatarRepository.getByUserId(user.id).stream().noneMatch(a -> a.id.equals(avatarId)))
			throw new NoSuchEntityException("invalid avatar id");

		avatarRepository.deleteAvatarById(avatarId);
	}

	public List<Avatar> getAll(User user) {
		return new ArrayList<>(avatarRepository.getByUserId(user.id));
	}

	public AvatarDto upload(User user, MultipartFile avatarFile) throws IOException {
		if (!Uploads.isSupportedImage(avatarFile)) throw new InvalidEntityException("provided file is not an image");

		String avatarPath = fileUploadService.upload(avatarFile, FileType.AVATAR);
		Avatar avatar = avatarRepository.save(new Avatar(user, avatarPath, LocalDate.now()));
		return avatarMapper.map(avatar);
	}

}
