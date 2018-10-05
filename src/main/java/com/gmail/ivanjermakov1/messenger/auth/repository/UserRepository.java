package com.gmail.ivanjermakov1.messenger.auth.repository;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
	
	User findByLoginAndHash(String login, String hash);
	
	User findByLogin(String login);
	
}
