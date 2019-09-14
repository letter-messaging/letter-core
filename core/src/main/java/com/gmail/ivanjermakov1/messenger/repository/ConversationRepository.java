package com.gmail.ivanjermakov1.messenger.repository;

import com.gmail.ivanjermakov1.messenger.entity.Conversation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConversationRepository extends CrudRepository<Conversation, Long> {

	@Query("select c from Conversation c join c.userConversations uc join uc.user u on u.id = :id")
	List<Conversation> getConversations(@Param("id") Long userId, Pageable pageable);

}
