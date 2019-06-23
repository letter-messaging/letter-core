package com.gmail.ivanjermakov1.messenger.auth.repository;

import com.gmail.ivanjermakov1.messenger.auth.entity.Token;
import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface TokenRepository extends CrudRepository<Token, Long> {
	
	Optional<Token> findById(Long userId);
	
	Optional<Token> findByToken(String token);
	
	@Modifying
	@Transactional
	void deleteAllByUser(User user);
	
}
