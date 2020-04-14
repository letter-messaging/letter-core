package com.github.ivanjermakov.lettercore.controller;

import com.github.ivanjermakov.lettercore.dto.PreviewDto;
import com.github.ivanjermakov.lettercore.dto.UserDto;
import com.github.ivanjermakov.lettercore.entity.User;
import com.github.ivanjermakov.lettercore.exception.InvalidSearchFormatException;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SearchController {

	/**
	 * Find preview by companion login or first or last name, presented partially or fully in search query parameter
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
