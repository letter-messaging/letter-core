package com.gmail.ivanjermakov1.messenger.auth.service;

import com.gmail.ivanjermakov1.messenger.auth.entity.Token;
import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.auth.repository.TokenRepository;
import com.gmail.ivanjermakov1.messenger.auth.repository.UserRepository;
import com.gmail.ivanjermakov1.messenger.auth.security.Hasher;
import com.gmail.ivanjermakov1.messenger.auth.security.TokenGenerator;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.exception.RegistrationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserService {
	
	private final UserRepository userRepository;
	private final TokenRepository tokenRepository;
	
	@Autowired
	public UserService(UserRepository userRepository, TokenRepository tokenRepository) {
		this.userRepository = userRepository;
		this.tokenRepository = tokenRepository;
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
	
	public void register(String login, String password) throws RegistrationException {
		if (userRepository.findByLogin(login) != null) throw new RegistrationException("user already exits.");
		userRepository.save(new User(null, login, Hasher.getHash(password)));
	}
	
}
