package com.gmail.ivanjermakov1.messenger.core.mapper;

import com.gmail.ivanjermakov1.messenger.core.util.Mappers;
import com.gmail.ivanjermakov1.messenger.messaging.dto.AvatarDto;
import com.gmail.ivanjermakov1.messenger.messaging.dto.UserInfoDto;
import com.gmail.ivanjermakov1.messenger.messaging.entity.UserInfo;
import com.gmail.ivanjermakov1.messenger.messaging.service.AvatarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserInfoMapper implements Mapper<UserInfo, UserInfoDto> {

	private AvatarService avatarService;
	private UserMapper userMapper;

	@Autowired
	public void setAvatarService(AvatarService avatarService) {
		this.avatarService = avatarService;
	}

	@Autowired
	public void setUserMapper(UserMapper userMapper) {
		this.userMapper = userMapper;
	}

	@Override
	public UserInfoDto map(UserInfo userInfo) {
		UserInfoDto userInfoDto = Mappers.map(userInfo, UserInfoDto.class);
		userInfoDto.setAvatars(Mappers.mapAll(avatarService.getAll(userInfo.getUser()), AvatarDto.class));
		userInfoDto.setUser(userMapper.map(userInfo.getUser()));
		return userInfoDto;
	}

}
