package com.gmail.ivanjermakov1.messenger.messaging.repository;

import com.gmail.ivanjermakov1.messenger.messaging.entity.UserMainInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserMainInfoRepository extends CrudRepository<UserMainInfo, Long> {
	
	@Query("select i from UserMainInfo i where i.user.id = :userId")
	Optional<UserMainInfo> findById(@Param("userId") Long userId);
	
}
