package com.gmail.ivanjermakov1.messenger.auth.service;

import com.gmail.ivanjermakov1.messenger.auth.dto.RegisterUserDto;
import com.gmail.ivanjermakov1.messenger.auth.entity.Token;
import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.auth.repository.TokenRepository;
import com.gmail.ivanjermakov1.messenger.auth.repository.UserRepository;
import com.gmail.ivanjermakov1.messenger.auth.security.TokenGenerator;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.exception.NoSuchEntityException;
import com.gmail.ivanjermakov1.messenger.exception.RegistrationException;
import com.gmail.ivanjermakov1.messenger.messaging.entity.UserInfo;
import com.gmail.ivanjermakov1.messenger.messaging.entity.UserOnline;
import com.gmail.ivanjermakov1.messenger.messaging.repository.UserOnlineRepository;
import com.gmail.ivanjermakov1.messenger.messaging.service.UserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

	private final static Logger LOG = LoggerFactory.getLogger(UserService.class);

	private final UserRepository userRepository;
	private final TokenRepository tokenRepository;
	private final UserOnlineRepository userOnlineRepository;
	private final HashService hashService;
	private UserInfoService userInfoService;

	@Value("${online.lifetime-days}")
	public Integer onlineLifetimeDays;

	@Autowired
	public void setUserInfoService(UserInfoService userInfoService) {
		this.userInfoService = userInfoService;
	}

	@Autowired
	public UserService(UserRepository userRepository, TokenRepository tokenRepository, UserOnlineRepository userOnlineRepository, HashService hashService) {
		this.userRepository = userRepository;
		this.tokenRepository = tokenRepository;
		this.userOnlineRepository = userOnlineRepository;
		this.hashService = hashService;
	}

	public String authenticate(String login, String password) throws AuthenticationException {
		LOG.debug("authenticate user: @" + login);
		Optional<User> user = userRepository.findByLogin(login);

		if (!user.isPresent() || !hashService.check(password, user.get().hash))
			throw new AuthenticationException("wrong credentials");

//		even if the password matches, client won't receive @system's user token
		if (user.get().id.equals(0L))
			throw new AuthenticationException("unable to authenticate @system user");

		Token token = tokenRepository.findById(user.get().id)
				.orElse(tokenRepository.save(new Token(user.get(), TokenGenerator.generate())));

		return token.token;
	}

	public User authenticate(String token) throws AuthenticationException {
		return tokenRepository.findByToken(token).orElseThrow(() -> new AuthenticationException("invalid token")).user;
	}

	public void register(RegisterUserDto registerUserDto) throws RegistrationException {
		registerUserDto.validate();

		LOG.debug("register user: @" + registerUserDto.login);

		if (userRepository.findByLogin(registerUserDto.login).isPresent())
			throw new RegistrationException("user already exists.");

		User user = new User(registerUserDto.login, hashService.getHash(registerUserDto.password));
		user = userRepository.save(user);
		userInfoService.save(new UserInfo(user, registerUserDto.firstName, registerUserDto.lastName));
	}

	public User getUser(Long id) {
		return userRepository.findById(id).orElseThrow(() -> new NoSuchEntityException("no such user"));
	}

	public User getUser(String login) {
		return userRepository.findByLogin(login)
				.orElseThrow(() -> new NoSuchEntityException("no such user"));
	}

	public void appearOnline(User user) {
		LOG.debug("user @" + user.login + " is now online");
		userOnlineRepository.save(new UserOnline(user, LocalDateTime.now()));
	}

	public void logout(User user) {
		LOG.debug("user @" + user.login + " is logout from everywhere");

		tokenRepository.deleteAllByUser(user);
	}

	/**
	 * Scheduled method that executes each hour. Delete all user_online records that older then
	 * {@code online.lifetime-days} days.
	 */
	@Scheduled(cron = "0 0 * * * *")
	public void deleteOnline() {
		long countBefore = userOnlineRepository.count();
		LOG.info("delete online records older then " + onlineLifetimeDays + " day(s)");
		userOnlineRepository.deleteOlderThanDays(onlineLifetimeDays);
		long countAfter = userOnlineRepository.count();
		LOG.info("deleted " + (countBefore - countAfter) + " records");
	}

}
