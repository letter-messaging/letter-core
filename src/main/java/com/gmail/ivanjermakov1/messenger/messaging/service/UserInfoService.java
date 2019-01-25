package com.gmail.ivanjermakov1.messenger.messaging.service;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.messaging.dto.UserInfoDTO;
import com.gmail.ivanjermakov1.messenger.messaging.entity.UserInfo;
import com.gmail.ivanjermakov1.messenger.messaging.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserInfoService {
	
	private final UserInfoRepository userInfoRepository;
	
	@Autowired
	public UserInfoService(UserInfoRepository userInfoRepository) {
		this.userInfoRepository = userInfoRepository;
	}
	
	public UserInfo getById(Long userId) {
		return userInfoRepository.findByUserId(userId).orElse(null);
	}
	
	public UserInfo save(UserInfo userInfo) {
		return userInfoRepository.save(userInfo);
	}
	
	public UserInfoDTO get(Long userId) {
		UserInfo userInfo = getById(userId);
		return UserInfoDTO.map(userInfo);
	}
	
	public UserInfoDTO edit(User user, UserInfoDTO userInfoDTO) throws AuthenticationException {
		if (!userInfoDTO.getUser().getId().equals(user.getId()))
			throw new AuthenticationException("allow only to edit personal info");
		
		UserInfo userInfo = getById(user.getId());
		
		userInfo.setUser(user);
		userInfo.setFirstName(userInfoDTO.getFirstName());
		userInfo.setLastName(userInfoDTO.getLastName());
		userInfo.setGender(userInfoDTO.getGender());
		userInfo.setBirthDate(userInfoDTO.getBirthDate());
		userInfo.setMaritalStatus(userInfoDTO.getMaritalStatus());
		userInfo.setCountry(userInfoDTO.getCountry());
		userInfo.setCity(userInfoDTO.getCity());
		userInfo.setLocation(userInfoDTO.getLocation());
		userInfo.setPhoneNumber(userInfoDTO.getPhoneNumber());
		userInfo.setMail(userInfoDTO.getMail());
		userInfo.setPlaceOfEducation(userInfoDTO.getPlaceOfEducation());
		userInfo.setPlaceOfWork(userInfoDTO.getPlaceOfWork());
		userInfo.setAbout(userInfoDTO.getAbout());
		
		return UserInfoDTO.map(save(userInfo));
	}
	
}
