package com.gmail.ivanjermakov1.messenger.auth.repository;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import org.springframework.data.domain.PageRequest;
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
	List<User> searchUsers(@Param("search") String search, Pageable pageable);
	
	/**
	 * Search for users with login started on specified string
	 *
	 * @param search string started with "@", otherwise return is empty list.
	 * @param amount amount of results
	 * @return list of users
	 */
	default List<User> searchUsersAmount(String search, Integer amount) {
		return searchUsers(search.substring(1) + "%", PageRequest.of(0, amount));
	}
	
}
