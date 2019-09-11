package com.gmail.ivanjermakov1.messenger.messaging.service;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.core.mapper.UserMapper;
import com.gmail.ivanjermakov1.messenger.core.util.Mappers;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
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

	public UserInfoDto edit(User user, UserInfoDto userInfoDto) throws AuthenticationException {
		if (!userInfoDto.getUser().getId().equals(user.getId()))
			throw new AuthenticationException("allow only to edit personal info");

		UserInfo userInfo = getByUser(user);

		userInfo.setUser(user);
		userInfo.setFirstName(userInfoDto.getFirstName());
		userInfo.setLastName(userInfoDto.getLastName());
		userInfo.setGender(userInfoDto.getGender());
		userInfo.setBirthDate(userInfoDto.getBirthDate());
		userInfo.setMaritalStatus(userInfoDto.getMaritalStatus());
		userInfo.setCountry(userInfoDto.getCountry());
		userInfo.setCity(userInfoDto.getCity());
		userInfo.setLocation(userInfoDto.getLocation());
		userInfo.setPhoneNumber(userInfoDto.getPhoneNumber());
		userInfo.setMail(userInfoDto.getMail());
		userInfo.setPlaceOfEducation(userInfoDto.getPlaceOfEducation());
		userInfo.setPlaceOfWork(userInfoDto.getPlaceOfWork());
		userInfo.setAbout(userInfoDto.getAbout());

		UserInfoDto edited = Mappers.map(save(userInfo), UserInfoDto.class);
		userInfoDto.setAvatars(Mappers.mapAll(avatarService.getAll(userInfo.getUser()), AvatarDto.class));
		userInfoDto.setUser(userMapper.map(userInfo.getUser()));
		return edited;
	}

}
