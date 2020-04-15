package com.github.ivanjermakov.lettercore.user.service;

import com.github.ivanjermakov.lettercore.auth.exception.AuthorizationException;
import com.github.ivanjermakov.lettercore.common.exception.NoSuchEntityException;
import com.github.ivanjermakov.lettercore.file.dto.AvatarDto;
import com.github.ivanjermakov.lettercore.file.service.AvatarService;
import com.github.ivanjermakov.lettercore.user.dto.UserInfoDto;
import com.github.ivanjermakov.lettercore.user.entity.User;
import com.github.ivanjermakov.lettercore.user.entity.UserInfo;
import com.github.ivanjermakov.lettercore.user.mapper.UserMapper;
import com.github.ivanjermakov.lettercore.user.repository.UserInfoRepository;
import com.github.ivanjermakov.lettercore.util.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserInfoService {

	private final UserInfoRepository userInfoRepository;
	private final AvatarService avatarService;

	private UserMapper userMapper;

	@Autowired
	public UserInfoService(UserInfoRepository userInfoRepository, AvatarService avatarService) {
		this.userInfoRepository = userInfoRepository;
		this.avatarService = avatarService;
	}

	@Autowired
	public void setUserMapper(UserMapper userMapper) {
		this.userMapper = userMapper;
	}

	public UserInfoDto edit(User user, UserInfoDto userInfoDto) throws AuthorizationException {
		if (!userInfoDto.user.id.equals(user.id))
			throw new AuthorizationException("allow only to edit personal info");

		UserInfo userInfo = userInfoRepository.findByUser(user)
				.orElseThrow(() -> new NoSuchEntityException("no such user info"));

		userInfo.user = user;
		userInfo.firstName = userInfoDto.firstName;
		userInfo.lastName = userInfoDto.lastName;
		userInfo.gender = userInfoDto.gender;
		userInfo.birthDate = userInfoDto.birthDate;
		userInfo.maritalStatus = userInfoDto.maritalStatus;
		userInfo.country = userInfoDto.country;
		userInfo.city = userInfoDto.city;
		userInfo.location = userInfoDto.location;
		userInfo.phoneNumber = userInfoDto.phoneNumber;
		userInfo.mail = userInfoDto.mail;
		userInfo.placeOfEducation = userInfoDto.placeOfEducation;
		userInfo.placeOfWork = userInfoDto.placeOfWork;
		userInfo.about = userInfoDto.about;

		UserInfoDto edited = Mappers.map(userInfoRepository.save(userInfo), UserInfoDto.class);
		userInfoDto.avatars = Mappers.mapAll(avatarService.getAll(userInfo.user), AvatarDto.class);
		userInfoDto.user = userMapper.map(userInfo.user);
		return edited;
	}

}
