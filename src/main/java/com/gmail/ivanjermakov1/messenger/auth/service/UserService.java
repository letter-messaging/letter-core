package com.gmail.ivanjermakov1.messenger.auth.service;

import com.gmail.ivanjermakov1.messenger.auth.dto.RegisterUserDTO;
import com.gmail.ivanjermakov1.messenger.auth.dto.UserDTO;
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
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class UserService {
	
	private final static Logger LOG = LoggerFactory.getLogger(UserService.class);
	
	private final UserRepository userRepository;
	private final TokenRepository tokenRepository;
	private final UserInfoService userInfoService;
	private final UserOnlineRepository userOnlineRepository;
	
	@Autowired
	public UserService(UserRepository userRepository, TokenRepository tokenRepository, UserInfoService userInfoService, UserOnlineRepository userOnlineRepository) {
		this.userRepository = userRepository;
		this.tokenRepository = tokenRepository;
		this.userInfoService = userInfoService;
		this.userOnlineRepository = userOnlineRepository;
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
	
	public void register(RegisterUserDTO registerUserDTO) throws RegistrationException {
		registerUserDTO.validate();
		
		LOG.debug("register user: @" + registerUserDTO.getLogin());
		
		if (userRepository.findByLogin(registerUserDTO.getLogin()) != null)
			throw new RegistrationException("user already exists.");
		
		User user = new User(null, registerUserDTO.getLogin(), Hasher.getHash(registerUserDTO.getPassword()));
		user = userRepository.save(user);
		userInfoService.save(new UserInfo(user, registerUserDTO.getFirstName(), registerUserDTO.getLastName()));
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
	
	public UserDTO full(User user) {
		UserInfo userInfo = userInfoService.getById(user.getId());
		UserOnline userOnline = userOnlineRepository.findFirstByUserIdOrderBySeenDesc(user.getId());
		return new UserDTO(
				user.getId(),
				user.getLogin(),
				userInfo.getFirstName(),
				userInfo.getLastName(),
				Optional.ofNullable(userOnline).orElse(new UserOnline()).getSeen()
		);
	}
	
	public void appearOnline(User user) {
		LOG.debug("user @" + user.getLogin() + " is now online");
		userOnlineRepository.save(new UserOnline(user, LocalDateTime.now()));
	}
	
}
