package com.gmail.ivanjermakov1.messenger.mapper;

import com.gmail.ivanjermakov1.messenger.dto.AvatarDto;
import com.gmail.ivanjermakov1.messenger.dto.UserInfoDto;
import com.gmail.ivanjermakov1.messenger.entity.UserInfo;
import com.gmail.ivanjermakov1.messenger.service.AvatarService;
import com.gmail.ivanjermakov1.messenger.util.Mappers;
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
		userInfoDto.avatars = Mappers.mapAll(avatarService.getAll(userInfo.user), AvatarDto.class);
		userInfoDto.user = userMapper.map(userInfo.user);
		return userInfoDto;
	}

}
