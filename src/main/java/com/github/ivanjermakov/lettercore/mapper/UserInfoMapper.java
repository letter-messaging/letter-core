package com.github.ivanjermakov.lettercore.mapper;

import com.github.ivanjermakov.lettercore.dto.AvatarDto;
import com.github.ivanjermakov.lettercore.dto.UserInfoDto;
import com.github.ivanjermakov.lettercore.entity.UserInfo;
import com.github.ivanjermakov.lettercore.service.AvatarService;
import com.github.ivanjermakov.lettercore.util.Mappers;
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
