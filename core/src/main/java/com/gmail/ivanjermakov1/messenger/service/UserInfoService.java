package com.gmail.ivanjermakov1.messenger.service;

import com.gmail.ivanjermakov1.messenger.dto.AvatarDto;
import com.gmail.ivanjermakov1.messenger.dto.UserInfoDto;
import com.gmail.ivanjermakov1.messenger.entity.User;
import com.gmail.ivanjermakov1.messenger.entity.UserInfo;
import com.gmail.ivanjermakov1.messenger.exception.AuthorizationException;
import com.gmail.ivanjermakov1.messenger.mapper.UserMapper;
import com.gmail.ivanjermakov1.messenger.repository.UserInfoRepository;
import com.gmail.ivanjermakov1.messenger.util.Mappers;
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

	public UserInfo getByUser(User user) {
		return userInfoRepository.findByUser(user).orElse(null);
	}

	public UserInfo save(UserInfo userInfo) {
		return userInfoRepository.save(userInfo);
	}

	public UserInfoDto edit(User user, UserInfoDto userInfoDto) throws AuthorizationException {
		if (!userInfoDto.user.id.equals(user.id))
			throw new AuthorizationException("allow only to edit personal info");

		UserInfo userInfo = getByUser(user);

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

		UserInfoDto edited = Mappers.map(save(userInfo), UserInfoDto.class);
		userInfoDto.avatars = Mappers.mapAll(avatarService.getAll(userInfo.user), AvatarDto.class);
		userInfoDto.user = userMapper.map(userInfo.user);
		return edited;
	}

}
