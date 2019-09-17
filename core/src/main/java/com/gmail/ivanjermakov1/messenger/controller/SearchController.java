package com.gmail.ivanjermakov1.messenger.controller;

import com.gmail.ivanjermakov1.messenger.dto.PreviewDto;
import com.gmail.ivanjermakov1.messenger.dto.UserDto;
import com.gmail.ivanjermakov1.messenger.entity.User;
import com.gmail.ivanjermakov1.messenger.exception.InvalidSearchFormatException;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SearchController {

	/**
	 * TODO: describe search algorithm more detailed
	 * Find preview by companion first or last name
	 *
	 * @param user   authenticated user. automatically maps, when {@literal Auth-Token} parameter present
	 * @param search search query
	 * @return list of found previews
	 */
	List<PreviewDto> searchConversations(User user, String search, Pageable pageable);

	/**
	 * Find users by their login.
	 * Search @param search should start with '@' otherwise {@code InvalidSearchFormatException} will be thrown.
	 * Amount of results is limited by @value {@code search.result.limit}
	 *
	 * @param user   authenticated user. automatically maps, when {@literal Auth-Token} parameter present
	 * @param search search query
	 * @return list of found users
	 * @throws InvalidSearchFormatException on invalid @param query
	 */
	List<UserDto> searchUsers(User user, String search, Pageable pageable);

}
