package com.gmail.ivanjermakov1.messenger.controller;

import com.gmail.ivanjermakov1.messenger.dto.ConversationDto;
import com.gmail.ivanjermakov1.messenger.entity.User;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;

public interface ConversationController {

	/**
	 * Create conversation with specified user.
	 * If specified @param withLogin is user himself then "self-conversation" is created.
	 *
	 * @param user      authenticated user. automatically maps, when {@literal Auth-Token} parameter present
	 * @param withLogin login of user to create conversation with
	 * @return created conversation
	 */
//	TODO: use withId instead of withLogin to be more consistent
	ConversationDto create(User user, String withLogin);

	/**
	 * Hide conversation for calling user and delete all messages sent by him.
	 *
	 * @param user authenticated user. automatically maps, when {@literal Auth-Token} parameter present
	 * @param id   id of conversation to delete
	 * @throws AuthenticationException on invalid @param token
	 */
	void delete(User user, Long id) throws AuthenticationException;

	/**
	 * Hide conversation from calling user
	 *
	 * @param user authenticated user. automatically maps, when {@literal Auth-Token} parameter present
	 * @param id   id of conversation to hide
	 */
	void hide(User user, Long id);

}
