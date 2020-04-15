package com.github.ivanjermakov.lettercore.conversation.repository;

import com.github.ivanjermakov.lettercore.conversation.entity.Conversation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConversationRepository extends CrudRepository<Conversation, Long> {

	@Query("select c from Conversation c join c.userConversations uc join uc.user u on u.id = :id")
	List<Conversation> getConversations(@Param("id") Long userId, Pageable pageable);

	/**
	 * Find conversations by search query.
	 *
	 * @param userId user id
	 * @param search search query
	 * @return found conversations
	 */
	@Query(value = "select c.id, c.chat_name, c.creator_id\n" +
			"from conversation c\n" +
			"         join user_conversation uc on uc.conversation_id = c.id\n" +
			"         join \"user\" u on u.id = uc.user_id\n" +
			"         join user_info ui on ui.user_id = u.id\n" +
			"where u.id <> :id\n" +
			"  and (\n" +
			"        lower(u.login) like '%' || lower(:s) || '%' or\n" +
			"        lower(ui.first_name) like '%' || lower(:s) || '%' or\n" +
			"        lower(ui.last_name) like '%' || lower(:s) || '%'\n" +
			"    )", nativeQuery = true)
	List<Conversation> findConversationsBySearchQuery(@Param("id") Long userId, @Param("s") String search);

}
