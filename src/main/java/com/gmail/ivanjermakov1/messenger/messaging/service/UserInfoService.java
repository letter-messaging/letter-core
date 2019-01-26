package com.gmail.ivanjermakov1.messenger.messaging.service;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.auth.service.UserService;
import com.gmail.ivanjermakov1.messenger.core.util.Mapper;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.messaging.dto.AvatarDto;
import com.gmail.ivanjermakov1.messenger.messaging.dto.UserInfoDto;
import com.gmail.ivanjermakov1.messenger.messaging.entity.UserInfo;
import com.gmail.ivanjermakov1.messenger.messaging.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserInfoService {
	
	private final UserInfoRepository userInfoRepository;
	private final AvatarService avatarService;
	private UserService userService;
	
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	@Autowired
	public UserInfoService(UserInfoRepository userInfoRepository, AvatarService avatarService) {
		this.userInfoRepository = userInfoRepository;
		this.avatarService = avatarService;
	}
	
	public UserInfo getById(Long userId) {
		return userInfoRepository.findByUserId(userId).orElse(null);
	}
	
	public UserInfo save(UserInfo userInfo) {
		return userInfoRepository.save(userInfo);
	}
	
	public UserInfoDto get(Long userId) {
		UserInfo userInfo = getById(userId);
		UserInfoDto userInfoDto = Mapper.map(userInfo, UserInfoDto.class);
		userInfoDto.setAvatars(Mapper.mapAll(avatarService.getAll(userInfo.getUser()), AvatarDto.class));
		userInfoDto.setUser(userService.full(userInfo.getUser()));
		return userInfoDto;
	}
	
	public UserInfoDto edit(User user, UserInfoDto userInfoDto) throws AuthenticationException {
		if (!userInfoDto.getUser().getId().equals(user.getId()))
			throw new AuthenticationException("allow only to edit personal info");
		
		UserInfo userInfo = getById(user.getId());
		
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
		
		UserInfoDto edited = Mapper.map(save(userInfo), UserInfoDto.class);
		userInfoDto.setAvatars(Mapper.mapAll(avatarService.getAll(userInfo.getUser()), AvatarDto.class));
		userInfoDto.setUser(userService.full(userInfo.getUser()));
		return edited;
	}
	
}
