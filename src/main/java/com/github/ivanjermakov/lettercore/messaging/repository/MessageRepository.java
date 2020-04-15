package com.github.ivanjermakov.lettercore.messaging.repository;

import com.github.ivanjermakov.lettercore.conversation.entity.Conversation;
import com.github.ivanjermakov.lettercore.messaging.entity.Message;
import com.github.ivanjermakov.lettercore.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MessageRepository extends CrudRepository<Message, Long> {

	Optional<Message> getById(Long messageId);

	Optional<Message> getTop1ByConversationOrderBySentDesc(Conversation conversation);

	List<Message> getAllBySenderAndConversation(User sender, Conversation conversation);

	List<Message> findAllByConversationOrderBySentDesc(Conversation conversation, Pageable pageable);

	@Query("select count(m) from Message m where m.conversation = :conversation and m.sender <> :user and m.sent > :lastRead")
	Integer countUnread(@Param("user") User user, @Param("conversation") Conversation conversation, @Param("lastRead") LocalDateTime lastRead);

	@Modifying
	@Transactional
	@Query(value = "delete from forwarded_message where forwarded_message_id = :id", nativeQuery = true)
	void deleteFromForwarded(@Param("id") Long id);

	@Modifying
	@Transactional
	@Query(value = "delete from forwarded_message where parent_message_id = :id", nativeQuery = true)
	void deleteForwarded(@Param("id") Long id);

}
