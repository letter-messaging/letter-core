package com.gmail.ivanjermakov1.messenger.messaging.repository;

import com.gmail.ivanjermakov1.messenger.messaging.entity.UserMainInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
public interface UserMainInfoRepository extends CrudRepository<UserMainInfo, Long> {
	
	@Query("select i from UserMainInfo i where i.id = :userId")
	Optional<UserMainInfo> findById(@Param("userId") Long userId);
	
}
