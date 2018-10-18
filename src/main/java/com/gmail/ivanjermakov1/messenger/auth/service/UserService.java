package com.gmail.ivanjermakov1.messenger.auth.service;

import com.gmail.ivanjermakov1.messenger.auth.entity.Token;
import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.auth.repository.TokenRepository;
import com.gmail.ivanjermakov1.messenger.auth.repository.UserRepository;
import com.gmail.ivanjermakov1.messenger.auth.security.Hasher;
import com.gmail.ivanjermakov1.messenger.auth.security.TokenGenerator;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.exception.NoSuchEntityException;
import com.gmail.ivanjermakov1.messenger.exception.RegistrationException;
import com.gmail.ivanjermakov1.messenger.messaging.dto.UserDTO;
import com.gmail.ivanjermakov1.messenger.messaging.entity.UserMainInfo;
import com.gmail.ivanjermakov1.messenger.messaging.repository.UserMainInfoRepository;
import com.gmail.ivanjermakov1.messenger.messaging.service.UserMainInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class UserService {
	
	private final UserRepository userRepository;
	private final TokenRepository tokenRepository;
	private final UserMainInfoService userMainInfoService;
	
	
	@Autowired
	public UserService(UserRepository userRepository, TokenRepository tokenRepository, UserMainInfoService userMainInfoService) {
		this.userRepository = userRepository;
		this.tokenRepository = tokenRepository;
		this.userMainInfoService = userMainInfoService;
	}
	
	public String authenticate(String login, String password) throws AuthenticationException {
		User user = userRepository.findByLogin(login);
		
		if (user == null || !Hasher.check(password, user.getHash()))
			throw new AuthenticationException("wrong credentials");
		
		Token token = tokenRepository.findById(user.getId())
				.orElse(null);
		if (token == null) {
			token = new Token(user.getId(), TokenGenerator.generate());
			tokenRepository.save(token);
		}
		
		return token.getToken();
	}
	
	public void register(String firstName, String lastName, String login, String password) throws RegistrationException {
		if (userRepository.findByLogin(login) != null) throw new RegistrationException("user already exits.");
		
		User user = new User(null, login, Hasher.getHash(password));
		userRepository.save(user);
		userMainInfoService.save(new UserMainInfo(user.getId(), firstName, lastName));
	}
	
	public User getUser(Long id) throws NoSuchEntityException {
		return userRepository.findById(id).orElseThrow(() -> new NoSuchEntityException("no such user"));
	}
	
	public User getUser(String login) throws NoSuchEntityException {
		return Optional.ofNullable(userRepository.findByLogin(login))
				.orElseThrow(() -> new NoSuchEntityException("no such user"));
	}
	
	public Long getUserId(String token) throws NoSuchEntityException {
		return Optional.ofNullable(userRepository.getId(token)).orElseThrow(() -> new NoSuchEntityException("no such user"));
	}
	
	public UserDTO full(User user) {
		return new UserDTO(user, userMainInfoService.getById(user.getId()));
	}
	
	public User auth(String token) throws AuthenticationException {
		try {
			return getUser(getUserId(token));
		} catch (NoSuchEntityException e) {
			throw new AuthenticationException("invalid token");
		}
	}
	
}
