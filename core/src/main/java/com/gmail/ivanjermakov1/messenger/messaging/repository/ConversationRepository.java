package com.gmail.ivanjermakov1.messenger.messaging.repository;

import com.gmail.ivanjermakov1.messenger.messaging.entity.Conversation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ConversationRepository extends CrudRepository<Conversation, Long> {
	
	@Query("select c from Conversation c join c.users u on u.id = :id")
	List<Conversation> getConversations(@Param("id") Long userId, Pageable pageable);
	
	@Modifying
	@Query(value = "update user_conversation " +
			"set hidden = :hidden " +
			"where user_conversation.user_id = :userId and user_conversation.conversation_id = :conversationId",
			nativeQuery = true)
	void setHidden(@Param("userId") Long userId, @Param("conversationId") Long conversationId, @Param("hidden") Boolean hidden);
	
	@Query(value = "select hidden from user_conversation " +
			"where user_conversation.user_id = :userId and user_conversation.conversation_id = :conversationId",
			nativeQuery = true)
	Boolean isHidden(@Param("userId") Long userId, @Param("conversationId") Long conversationId);
	
}
