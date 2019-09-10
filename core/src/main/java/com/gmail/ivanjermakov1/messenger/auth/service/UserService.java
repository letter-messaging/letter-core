package com.gmail.ivanjermakov1.messenger.auth.service;

import com.gmail.ivanjermakov1.messenger.auth.dto.RegisterUserDto;
import com.gmail.ivanjermakov1.messenger.auth.entity.Token;
import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.auth.repository.TokenRepository;
import com.gmail.ivanjermakov1.messenger.auth.repository.UserRepository;
import com.gmail.ivanjermakov1.messenger.auth.security.Hasher;
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
	private UserInfoService userInfoService;
	
	@Value("${online.lifetime-days}")
	public Integer onlineLifetimeDays;
	
	@Autowired
	public void setUserInfoService(UserInfoService userInfoService) {
		this.userInfoService = userInfoService;
	}
	
	@Autowired
	public UserService(UserRepository userRepository, TokenRepository tokenRepository, UserOnlineRepository userOnlineRepository) {
		this.userRepository = userRepository;
		this.tokenRepository = tokenRepository;
		this.userOnlineRepository = userOnlineRepository;
	}
	
	public String authenticate(String login, String password) throws AuthenticationException {
		LOG.debug("authenticate user: @" + login);
		Optional<User> user = userRepository.findByLogin(login);
		
		if (!user.isPresent() || !Hasher.check(password, user.get().getHash()))
			throw new AuthenticationException("wrong credentials");
		
		Token token = tokenRepository.findById(user.get().getId())
				.orElse(tokenRepository.save(new Token(user.get(), TokenGenerator.generate())));
		
		return token.getToken();
	}
	
	public User authenticate(String token) throws AuthenticationException {
		return tokenRepository.findByToken(token).orElseThrow(() -> new AuthenticationException("invalid token")).getUser();
	}
	
	public void register(RegisterUserDto registerUserDto) throws RegistrationException {
		registerUserDto.validate();
		
		LOG.debug("register user: @" + registerUserDto.getLogin());
		
		if (userRepository.findByLogin(registerUserDto.getLogin()).isPresent())
			throw new RegistrationException("user already exists.");
		
		User user = new User(registerUserDto.getLogin(), Hasher.getHash(registerUserDto.getPassword()));
		user = userRepository.save(user);
		userInfoService.save(new UserInfo(user, registerUserDto.getFirstName(), registerUserDto.getLastName()));
	}
	
	public User getUser(Long id) {
		return userRepository.findById(id).orElseThrow(() -> new NoSuchEntityException("no such user"));
	}
	
	public User getUser(String login) {
		return userRepository.findByLogin(login)
				.orElseThrow(() -> new NoSuchEntityException("no such user"));
	}
	
	public void appearOnline(User user) {
		LOG.debug("user @" + user.getLogin() + " is now online");
		userOnlineRepository.save(new UserOnline(user, LocalDateTime.now()));
	}
	
	public void logout(User user) {
		LOG.debug("user @" + user.getLogin() + " is logout from everywhere");
		
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
