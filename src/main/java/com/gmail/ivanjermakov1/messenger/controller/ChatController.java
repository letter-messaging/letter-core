package com.gmail.ivanjermakov1.messenger.controller;

import com.gmail.ivanjermakov1.messenger.dto.ConversationDto;
import com.gmail.ivanjermakov1.messenger.dto.NewChatDto;
import com.gmail.ivanjermakov1.messenger.entity.User;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.exception.AuthorizationException;
import com.gmail.ivanjermakov1.messenger.exception.NoSuchEntityException;

import java.util.List;

public interface ChatController {

	/**
	 * Create chat.
	 *
	 * @param user authenticated user. automatically maps, when {@literal Auth-Token} parameter present
	 * @param chat chat instance to create. Creator id is not required.
	 * @return created chat
	 */
	ConversationDto create(User user, NewChatDto chat);

	/**
	 * Add new member to chat.
	 * Every member can add new member. If you need to add multiple new members, use {@code .addMembers()}
	 *
	 * @param user     authenticated user. automatically maps, when {@literal Auth-Token} parameter present
	 * @param chatId   chat memberId
	 * @param memberId new member id
	 * @throws NoSuchEntityException if caller is not chat member or no such chat
	 */
	void addMember(User user, Long chatId, Long memberId);

	/**
	 * Add new members to chat.
	 * Every member can add new members.
	 *
	 * @param user      authenticated user. automatically maps, when {@literal Auth-Token} parameter present
	 * @param chatId    chat id
	 * @param memberIds list of new members ids
	 * @throws NoSuchEntityException if caller is not chat member
	 */
	void addMembers(User user, Long chatId, List<Long> memberIds);

	/**
	 * Kick member from chat.
	 * Only chat creator can kick members. Creator cannot kick himself, {@code .hide()} method should be used to hide
	 * conversation.
	 *
	 * @param user     authenticated user. automatically maps, when {@literal Auth-Token} parameter present
	 * @param chatId   chat memberId
	 * @param memberId kick member id
	 * @throws AuthorizationException if caller is not chat creator
	 * @throws IllegalStateException  if chat creator try to kick himself
	 */
	void kickMember(User user, Long chatId, Long memberId);

	/**
	 * Hide chat for calling user and delete all messages sent by him.
	 *
	 * @param user           authenticated user. automatically maps, when {@literal Auth-Token} parameter present
	 * @param conversationId id of conversation to delete
	 * @throws AuthenticationException on invalid @param token
	 */
	void delete(User user, Long conversationId) throws AuthenticationException;

	/**
	 * Hide chat from calling user
	 *
	 * @param user authenticated user. automatically maps, when {@literal Auth-Token} parameter present
	 * @param id   id of conversation to hide
	 */
	void hide(User user, Long id);

}
