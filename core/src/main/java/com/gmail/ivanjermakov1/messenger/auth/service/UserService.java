package com.gmail.ivanjermakov1.messenger.auth.service;

import com.gmail.ivanjermakov1.messenger.auth.dto.RegisterUserDto;
import com.gmail.ivanjermakov1.messenger.auth.dto.UserDto;
import com.gmail.ivanjermakov1.messenger.auth.entity.Token;
import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.auth.repository.TokenRepository;
import com.gmail.ivanjermakov1.messenger.auth.repository.UserRepository;
import com.gmail.ivanjermakov1.messenger.auth.security.Hasher;
import com.gmail.ivanjermakov1.messenger.auth.security.TokenGenerator;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.exception.NoSuchEntityException;
import com.gmail.ivanjermakov1.messenger.exception.RegistrationException;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Avatar;
import com.gmail.ivanjermakov1.messenger.messaging.entity.UserInfo;
import com.gmail.ivanjermakov1.messenger.messaging.entity.UserOnline;
import com.gmail.ivanjermakov1.messenger.messaging.repository.UserOnlineRepository;
import com.gmail.ivanjermakov1.messenger.messaging.service.AvatarService;
import com.gmail.ivanjermakov1.messenger.messaging.service.UserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class UserService {
	
	private final static Logger LOG = LoggerFactory.getLogger(UserService.class);
	
	private final UserRepository userRepository;
	private final TokenRepository tokenRepository;
	private final UserOnlineRepository userOnlineRepository;
	private final AvatarService avatarService;
	private UserInfoService userInfoService;
	
	@Value("${default.avatar.path}")
	private String defaultAvatarPath;
	
	@Autowired
	public void setUserInfoService(UserInfoService userInfoService) {
		this.userInfoService = userInfoService;
	}
	
	@Autowired
	public UserService(UserRepository userRepository, TokenRepository tokenRepository, UserOnlineRepository userOnlineRepository, AvatarService avatarService) {
		this.userRepository = userRepository;
		this.tokenRepository = tokenRepository;
		this.userOnlineRepository = userOnlineRepository;
		this.avatarService = avatarService;
	}
	
	public String authenticate(String login, String password) throws AuthenticationException {
		LOG.debug("authenticate user: @" + login);
		User user = userRepository.findByLogin(login);
		
		if (user == null || !Hasher.check(password, user.getHash()))
			throw new AuthenticationException("wrong credentials");
		
		Token token = tokenRepository.findById(user.getId())
				.orElse(null);
		if (token == null) {
			token = tokenRepository.save(new Token(user, TokenGenerator.generate()));
		}
		
		return token.getToken();
	}
	
	public User authenticate(String token) throws AuthenticationException {
		try {
			return getUserByToken(token);
		} catch (NoSuchEntityException e) {
			throw new AuthenticationException("invalid token");
		}
	}
	
	public void register(RegisterUserDto registerUserDto) throws RegistrationException {
		registerUserDto.validate();
		
		LOG.debug("register user: @" + registerUserDto.getLogin());
		
		if (userRepository.findByLogin(registerUserDto.getLogin()) != null)
			throw new RegistrationException("user already exists.");
		
		User user = new User(null, registerUserDto.getLogin(), Hasher.getHash(registerUserDto.getPassword()));
		user = userRepository.save(user);
		userInfoService.save(new UserInfo(user, registerUserDto.getFirstName(), registerUserDto.getLastName()));
	}
	
	public User getUser(Long id) throws NoSuchEntityException {
		return userRepository.findById(id).orElseThrow(() -> new NoSuchEntityException("no such user"));
	}
	
	public User getUser(String login) throws NoSuchEntityException {
		return Optional.ofNullable(userRepository.findByLogin(login))
				.orElseThrow(() -> new NoSuchEntityException("no such user"));
	}
	
	public User getUserByToken(String token) throws NoSuchEntityException {
		return tokenRepository.findByToken(token).orElseThrow(() -> new NoSuchEntityException("no such user")).getUser();
	}
	
	public UserDto full(User user) {
		UserInfo userInfo = userInfoService.getById(user.getId());
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
	
	public void appearOnline(User user) {
		LOG.debug("user @" + user.getLogin() + " is now online");
		userOnlineRepository.save(new UserOnline(user, LocalDateTime.now()));
	}
	
	public void logout(User user) {
		LOG.debug("user @" + user.getLogin() + " is logout from everywhere");
		
		tokenRepository.deleteAllByUser(user);
	}
	
}
