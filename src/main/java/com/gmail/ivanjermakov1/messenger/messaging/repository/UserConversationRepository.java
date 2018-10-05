package com.gmail.ivanjermakov1.messenger.messaging.repository;

import com.gmail.ivanjermakov1.messenger.messaging.entity.UserConversation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface UserConversationRepository extends CrudRepository<UserConversation, Long> {
	
	@Query("select uc.conversationId\n" +
			"from UserConversation uc\n" +
			"where uc.userId = :id1 and uc.userId = :id2")
	Long findConversationIdsByUsersIds(@Param("id1") Long id1, @Param("id2") Long id2);
	
	@Query("select uc.userId " +
			"from UserConversation uc " +
			"where uc.conversationId = :conversationId")
	Set<Long> getUsersIds(@Param("conversationId") Long conversationId);
	
}
