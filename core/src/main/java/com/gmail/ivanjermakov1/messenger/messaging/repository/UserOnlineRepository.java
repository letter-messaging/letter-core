package com.gmail.ivanjermakov1.messenger.messaging.repository;

import com.gmail.ivanjermakov1.messenger.messaging.entity.UserOnline;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserOnlineRepository extends CrudRepository<UserOnline, Long> {
	
	@Query(value = "select * from user_online " +
			"where user_id = :userId " +
			"order by seen desc " +
			"limit 1", nativeQuery = true)
	UserOnline lastSeen(@Param("userId") Long userId);
	
	UserOnline findFirstByUserIdOrderBySeenDesc(Long user_id);
	
}
