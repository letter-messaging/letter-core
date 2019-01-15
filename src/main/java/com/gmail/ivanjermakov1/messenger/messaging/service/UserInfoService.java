package com.gmail.ivanjermakov1.messenger.messaging.service;

import com.gmail.ivanjermakov1.messenger.auth.dto.UserInfoDTO;
import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.messaging.entity.UserInfo;
import com.gmail.ivanjermakov1.messenger.messaging.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserInfoService {
	
	private final UserInfoRepository userInfoRepository;
	
	@Autowired
	public UserInfoService(UserInfoRepository userInfoRepository) {
		this.userInfoRepository = userInfoRepository;
	}
	
	public UserInfo getById(Long userId) {
		return userInfoRepository.findById(userId).orElse(null);
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
		
		return UserInfoDTO.map(save(userInfo));
	}
	
}
