package com.gmail.ivanjermakov1.messenger.messaging.repository;

import com.gmail.ivanjermakov1.messenger.messaging.entity.UserInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserInfoRepository extends CrudRepository<UserInfo, Long> {
	
	@Query("select i from UserInfo i where i.user.id = :userId")
	Optional<UserInfo> findByUserId(@Param("userId") Long userId);
	
}
