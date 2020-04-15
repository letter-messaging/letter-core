package com.github.ivanjermakov.lettercore.user.mapper;

import com.github.ivanjermakov.lettercore.common.mapper.Mapper;
import com.github.ivanjermakov.lettercore.file.dto.AvatarDto;
import com.github.ivanjermakov.lettercore.file.service.AvatarService;
import com.github.ivanjermakov.lettercore.user.dto.UserInfoDto;
import com.github.ivanjermakov.lettercore.user.entity.UserInfo;
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
