package com.gmail.ivanjermakov1.messenger.messaging.service;

import com.gmail.ivanjermakov1.messenger.messaging.entity.UserMainInfo;
import com.gmail.ivanjermakov1.messenger.messaging.repository.UserMainInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserMainInfoService {
	
	private final UserMainInfoRepository userMainInfoRepository;
	
	@Autowired
	public UserMainInfoService(UserMainInfoRepository userMainInfoRepository) {
		this.userMainInfoRepository = userMainInfoRepository;
	}
	
	public UserMainInfo getById(Long userId) {
		return userMainInfoRepository.findById(userId).get();
	}
	
}
