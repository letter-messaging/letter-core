package com.gmail.ivanjermakov1.messenger.messaging.repository;

import com.gmail.ivanjermakov1.messenger.messaging.entity.Message;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface MessageRepository extends CrudRepository<Message, Long> {
	
	Message getTop1ByConversationIdOrderBySentDesc(Long conversationId);
	
	@Query(value = "select * from Message m where m.conversation_id = :conversationId " +
			"order by m.sent desc offset :offset limit :limit", nativeQuery = true)
	Set<Message> get(@Param("conversationId") Long conversationId,
	                 @Param("offset") Integer offset,
	                 @Param("limit") Integer limit);
	
	@Modifying
	@Query("update Message m " +
			"set m.read = true " +
			"where m.senderId <> :exceptUserId and m.conversationId = :conversationId")
	void readAllExcept(@Param("exceptUserId") Long exceptUserId, @Param("conversationId") Long conversationId);
	
}
