package com.gmail.ivanjermakov1.messenger.auth.repository;

import com.gmail.ivanjermakov1.messenger.auth.entity.Token;
import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TokenRepository extends CrudRepository<Token, Long> {
	
	Optional<Token> findById(Long userId);
	
}
