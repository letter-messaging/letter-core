package com.gmail.ivanjermakov1.messenger.messaging.repository;

import com.gmail.ivanjermakov1.messenger.messaging.entity.Avatar;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface AvatarRepository extends CrudRepository<Avatar, Long> {
	
	@Query("select a from Avatar a where a.user.id = :userId order by a.uploaded desc")
	Set<Avatar> getByUserId(@Param("userId") Long userId);
	
}
