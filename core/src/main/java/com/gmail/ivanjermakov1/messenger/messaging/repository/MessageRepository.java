package com.gmail.ivanjermakov1.messenger.messaging.repository;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Conversation;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface MessageRepository extends CrudRepository<Message, Long> {
	
	Message getById(Long messageId);
	
	List<Message> getAllBySenderAndConversation(User sender, Conversation conversation);
	
	Message getTop1ByConversationIdOrderBySentDesc(Long conversationId);
	
	List<Message> findAllByConversation(Conversation conversation, Pageable pageable);
	
	@Query("select count(m) from Message m where m.conversation = :conversation and m.sender <> :user and m.sent > :lastRead")
	Integer countUnread(@Param("user") User user, @Param("conversation") Conversation conversation, @Param("lastRead") LocalDateTime lastRead);
	
	@Modifying
	@Transactional
	@Query(value = "delete from forwarded_message where forwarded_message_id = :id", nativeQuery = true)
	void deleteFromForwarded(@Param("id") Long id);
	
	@Modifying
	@Query(value = "delete from forwarded_message where parent_message_id = :id", nativeQuery = true)
	void deleteForwarded(@Param("id") Long id);
	
}
