package com.gmail.ivanjermakov1.messenger.messaging.repository;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Conversation;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Message;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Repository
public interface MessageRepository extends CrudRepository<Message, Long> {
	
	Message getById(Long messageId);
	
	Set<Message> getAllBySenderAndConversation(User sender, Conversation conversation);
	
	Message getTop1ByConversationIdOrderBySentDesc(Long conversationId);
	
	@Query(value = "select * from Message m where m.conversation_id = :conversationId " +
			"order by m.sent desc offset :offset limit :limit", nativeQuery = true)
	Set<Message> get(@Param("conversationId") Long conversationId,
	                 @Param("offset") Integer offset,
	                 @Param("limit") Integer limit);
	
	@Modifying
	@Query("update Message m " +
			"set m.read = true " +
			"where m.sender.id <> :exceptUserId and m.conversation.id = :conversationId")
	void readAllExcept(@Param("exceptUserId") Long exceptUserId, @Param("conversationId") Long conversationId);
	
	@Query("select count(m) " +
			"from Message m " +
			"where m.conversation.id = :conversationId and m.sender.id <> :exceptUserId and m.read = false")
	Integer getUnreadCount(@Param("exceptUserId") Long exceptUserId, @Param("conversationId") Long conversationId);
	
	@Modifying
	@Transactional
	@Query(value = "delete from forwarded_message where forwarded_message_id = :id", nativeQuery = true)
	void deleteFromForwarded(@Param("id") Long id);
	
	@Modifying
	@Query(value = "delete from forwarded_message where parent_message_id = :id", nativeQuery = true)
	void deleteForwarded(@Param("id") Long id);
	
}
