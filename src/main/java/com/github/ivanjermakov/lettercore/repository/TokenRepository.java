package com.github.ivanjermakov.lettercore.repository;

import com.github.ivanjermakov.lettercore.entity.Token;
import com.github.ivanjermakov.lettercore.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface TokenRepository extends CrudRepository<Token, Long> {

	Optional<Token> findById(Long userId);

	Optional<Token> findByToken(String token);

	@Modifying
	@Transactional
	void deleteAllByUser(User user);

}
