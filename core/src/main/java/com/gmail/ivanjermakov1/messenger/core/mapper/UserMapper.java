package com.gmail.ivanjermakov1.messenger.core.mapper;

import com.gmail.ivanjermakov1.messenger.auth.dto.UserDto;
import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.messaging.dto.AvatarDto;
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

	@Value("${default.avatar.conversation.path}")
	private String defaultAvatarConversationPath;

	private UserInfoService userInfoService;
	private UserOnlineRepository userOnlineRepository;
	private AvatarService avatarService;
	private AvatarMapper avatarMapper;

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

	@Autowired
	public void setAvatarMapper(AvatarMapper avatarMapper) {
		this.avatarMapper = avatarMapper;
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
				avatarService.getCurrent(user).map(a -> avatarMapper.map(a))
						.orElse(new AvatarDto(null, defaultAvatarConversationPath, null)),
				Optional.ofNullable(userOnline).map(UserOnline::getSeen).orElse(null)
		);
	}

}
