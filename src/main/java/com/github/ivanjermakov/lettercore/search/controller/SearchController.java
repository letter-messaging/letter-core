package com.github.ivanjermakov.lettercore.search.controller;

import com.github.ivanjermakov.lettercore.common.exception.InvalidEntityException;
import com.github.ivanjermakov.lettercore.conversation.dto.PreviewDto;
import com.github.ivanjermakov.lettercore.search.exception.InvalidSearchFormatException;
import com.github.ivanjermakov.lettercore.search.service.SearchService;
import com.github.ivanjermakov.lettercore.user.dto.UserDto;
import com.github.ivanjermakov.lettercore.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//TODO: refactor search methods by domain, so ConversationController, UserController etc contain those methods
@RestController
@RequestMapping("search")
@Transactional
public class SearchController {

	private final SearchService searchService;

	@Autowired
	public SearchController(SearchService searchService) {
		this.searchService = searchService;
	}

	/**
	 * Find preview by companion login or first or last name, presented partially or fully in search query parameter
	 *
	 * @param user   authenticated user. automatically maps, when {@literal Auth-Token} parameter present
	 * @param search search query
	 * @return list of found previews
	 */
	@GetMapping("conversations")
	public List<PreviewDto> searchConversations(@ModelAttribute User user,
	                                            @RequestParam("search") String search,
	                                            Pageable pageable) throws InvalidEntityException {
		return searchService.searchConversations(user, search, pageable);
	}

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
	@GetMapping("users")
	public List<UserDto> searchUsers(@ModelAttribute User user,
	                                 @RequestParam("search") String search,
	                                 Pageable pageable) throws InvalidSearchFormatException {
		return searchService.searchUsers(search, pageable);
	}

}
