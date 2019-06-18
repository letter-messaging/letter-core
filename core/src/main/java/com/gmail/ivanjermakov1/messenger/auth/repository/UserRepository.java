package com.gmail.ivanjermakov1.messenger.auth.repository;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
	
	User findByLogin(String login);
	
	Optional<User> findById(Long id);
	
	@Query("select u from User u where lower(u.login) like lower(:search)")
	List<User> searchUsersQuery(@Param("search") String search, Pageable pageable);
	
	default List<User> searchUsers(String search, Pageable pageable) {
		return this.searchUsersQuery(search.substring(1) + "%", pageable);
	}
	
}
