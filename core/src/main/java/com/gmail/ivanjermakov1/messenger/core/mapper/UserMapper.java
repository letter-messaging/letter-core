package com.gmail.ivanjermakov1.messenger.core.mapper;

import com.gmail.ivanjermakov1.messenger.auth.dto.UserDto;
import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Avatar;
import com.gmail.ivanjermakov1.messenger.messaging.entity.UserInfo;
import com.gmail.ivanjermakov1.messenger.messaging.entity.UserOnline;
import com.gmail.ivanjermakov1.messenger.messaging.repository.UserOnlineRepository;
import com.gmail.ivanjermakov1.messenger.messaging.service.AvatarService;
import com.gmail.ivanjermakov1.messenger.messaging.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserMapper implements Mapper<User, UserDto> {
	
	private UserInfoService userInfoService;
	private UserOnlineRepository userOnlineRepository;
	private AvatarService avatarService;
	
	@Value("${default.avatar.path}")
	private String defaultAvatarPath;
	
	@Autowired
	public void setUserInfoService(UserInfoService userInfoService) {
		this.userInfoService = userInfoService;
	}
	
	@Autowired
	public void setUserOnlineRepository(UserOnlineRepository userOnlineRepository) {
		this.userOnlineRepository = userOnlineRepository;
	}
	
	@Autowired
	public void setAvatarService(AvatarService avatarService) {
		this.avatarService = avatarService;
	}
	
	@Override
	public UserDto map(User user) {
		UserInfo userInfo = userInfoService.getByUser(user);
		UserOnline userOnline = userOnlineRepository.findFirstByUserIdOrderBySeenDesc(user.getId());
		return new UserDto(
				user.getId(),
				user.getLogin(),
				userInfo.getFirstName(),
				userInfo.getLastName(),
				avatarService.getCurrent(user).map(Avatar::getPath).orElse(defaultAvatarPath),
				Optional.ofNullable(userOnline).map(UserOnline::getSeen).orElse(null)
		);
	}
	
}
