package com.github.ivanjermakov.lettercore.controller;

import com.github.ivanjermakov.lettercore.dto.MessageDto;
import com.github.ivanjermakov.lettercore.entity.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MessageController {

	/**
	 * Get list of messages.
	 *
	 * @param user           authenticated user. automatically maps, when {@literal Auth-Token} parameter present
	 * @param conversationId conversation id to get messages within
	 * @param pageable       pageable
	 * @return list of messages. Return empty list on empty conversation
	 */
	List<MessageDto> get(User user, Long conversationId, Pageable pageable);

	/**
	 * Delete list of messages.
	 * It is possible to delete only user-self messages. If in list are messages from another user they will be ignored.
	 *
	 * @param user           authenticated user. automatically maps, when {@literal Auth-Token} parameter present
	 * @param deleteMessages list of messages to delete
	 */
	void delete(User user, List<MessageDto> deleteMessages);

}
