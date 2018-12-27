package com.gmail.ivanjermakov1.messenger.auth.repository;

import com.gmail.ivanjermakov1.messenger.auth.entity.Token;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
public interface TokenRepository extends CrudRepository<Token, Long> {
	
	Optional<Token> findById(Long userId);
	
}
