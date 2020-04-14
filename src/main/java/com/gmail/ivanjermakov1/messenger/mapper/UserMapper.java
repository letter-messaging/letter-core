package com.gmail.ivanjermakov1.messenger.mapper;

import com.gmail.ivanjermakov1.messenger.dto.AvatarDto;
import com.gmail.ivanjermakov1.messenger.dto.UserDto;
import com.gmail.ivanjermakov1.messenger.entity.User;
import com.gmail.ivanjermakov1.messenger.entity.UserOnline;
import com.gmail.ivanjermakov1.messenger.repository.UserOnlineRepository;
import com.gmail.ivanjermakov1.messenger.service.AvatarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserMapper implements Mapper<User, UserDto> {

	@Value("${default.avatar.conversation.path}")
	private String defaultAvatarConversationPath;

	private UserOnlineRepository userOnlineRepository;
	private AvatarService avatarService;
	private AvatarMapper avatarMapper;

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
		UserOnline userOnline = userOnlineRepository.findFirstByUserIdOrderBySeenDesc(user.id);
		return new UserDto(
				user.id,
				user.login,
				user.userInfo.firstName,
				user.userInfo.lastName,
				avatarService.getCurrent(user).map(a -> avatarMapper.map(a))
						.orElse(new AvatarDto(null, defaultAvatarConversationPath, null)),
				Optional.ofNullable(userOnline).map(uo -> uo.seen).orElse(null)
		);
	}

}
