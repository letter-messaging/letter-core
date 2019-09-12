package com.gmail.ivanjermakov1.messenger.messaging.service;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.core.mapper.UserMapper;
import com.gmail.ivanjermakov1.messenger.core.util.Mappers;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.exception.AuthorizationException;
import com.gmail.ivanjermakov1.messenger.messaging.dto.AvatarDto;
import com.gmail.ivanjermakov1.messenger.messaging.dto.UserInfoDto;
import com.gmail.ivanjermakov1.messenger.messaging.entity.UserInfo;
import com.gmail.ivanjermakov1.messenger.messaging.repository.UserInfoRepository;
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
		if (!userInfoDto.user.id.equals(user.getId()))
			throw new AuthorizationException("allow only to edit personal info");

		UserInfo userInfo = getByUser(user);

		userInfo.setUser(user);
		userInfo.setFirstName(userInfoDto.firstName);
		userInfo.setLastName(userInfoDto.lastName);
		userInfo.setGender(userInfoDto.gender);
		userInfo.setBirthDate(userInfoDto.birthDate);
		userInfo.setMaritalStatus(userInfoDto.maritalStatus);
		userInfo.setCountry(userInfoDto.country);
		userInfo.setCity(userInfoDto.city);
		userInfo.setLocation(userInfoDto.location);
		userInfo.setPhoneNumber(userInfoDto.phoneNumber);
		userInfo.setMail(userInfoDto.mail);
		userInfo.setPlaceOfEducation(userInfoDto.placeOfEducation);
		userInfo.setPlaceOfWork(userInfoDto.placeOfWork);
		userInfo.setAbout(userInfoDto.about);

		UserInfoDto edited = Mappers.map(save(userInfo), UserInfoDto.class);
		userInfoDto.avatars = Mappers.mapAll(avatarService.getAll(userInfo.getUser()), AvatarDto.class);
		userInfoDto.user = userMapper.map(userInfo.getUser());
		return edited;
	}

}
