package com.gmail.ivanjermakov1.messenger.controller;

import com.gmail.ivanjermakov1.messenger.dto.PreviewDto;
import com.gmail.ivanjermakov1.messenger.entity.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PreviewController {

	/**
	 * List previews.
	 *
	 * @param user authenticated user. automatically maps, when {@literal Auth-Token} parameter present
	 * @return list of previews
	 */
	List<PreviewDto> all(User user, Pageable pageable);

	/**
	 * Get specified conversation.
	 *
	 * @param user           authenticated user. automatically maps, when {@literal Auth-Token} parameter present
	 * @param conversationId conversation id
	 * @return conversation
	 */
	PreviewDto get(User user, Long conversationId);

}
