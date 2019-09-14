package com.gmail.ivanjermakov1.messenger.repository;

import com.gmail.ivanjermakov1.messenger.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

	Optional<User> findByLogin(String login);

	Optional<User> findById(Long id);

	@Query("select u from User u where u.id <> 0 and lower(u.login) like lower(:search)")
	List<User> searchUsersQuery(@Param("search") String search, Pageable pageable);

	default List<User> searchUsers(String search, Pageable pageable) {
		return this.searchUsersQuery(search.substring(1) + "%", pageable);
	}

}
