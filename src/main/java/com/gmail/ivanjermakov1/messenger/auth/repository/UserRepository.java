package com.gmail.ivanjermakov1.messenger.auth.repository;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends CrudRepository<User, Long> {
	
	User findByLogin(String login);
	
	@Query("select u.id\n" +
			"from User u, Token t\n" +
			"where u.id = t.id and t.token = :token")
	Long getId(@Param("token") String token);
	
}
