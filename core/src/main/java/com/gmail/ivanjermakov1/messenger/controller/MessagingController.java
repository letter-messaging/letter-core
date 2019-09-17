package com.gmail.ivanjermakov1.messenger.controller;

import com.gmail.ivanjermakov1.messenger.dto.EditMessageDto;
import com.gmail.ivanjermakov1.messenger.dto.MessageDto;
import com.gmail.ivanjermakov1.messenger.dto.NewMessageDto;
import com.gmail.ivanjermakov1.messenger.dto.action.Action;
import com.gmail.ivanjermakov1.messenger.entity.User;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.exception.InvalidEntityException;
import reactor.core.publisher.Flux;

public interface MessagingController {

	/**
	 * Subscribe on server-sent events.
	 * Calling user will receive events invoked by his own actions.
	 *
	 * @param token user token
	 * @return event stream of {@link Action}s
	 * @throws AuthenticationException on invalid @param token
	 */
	Flux<Action> getEvents(String token);

	/**
	 * Send message and invoke {@code com.gmail.ivanjermakov1.messenger.dto.action.MessageEditAction}.
	 *
	 * @param user          authenticated user. automatically maps, when {@literal Auth-Token} parameter present
	 * @param newMessageDto new message
	 * @return sent message
	 * @throws InvalidEntityException on invalid @param newMessageDto
	 */
	MessageDto sendMessage(User user, NewMessageDto newMessageDto);

	/**
	 * Edit message.
	 *
	 * @param user           authenticated user. automatically maps, when {@literal Auth-Token} parameter present
	 * @param editMessageDto editing message
	 * @return edited message
	 * @throws AuthenticationException on invalid @param token
	 */
	MessageDto editMessage(User user, EditMessageDto editMessageDto);

}
