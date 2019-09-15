package com.gmail.ivanjermakov1.messenger.controller;

import com.gmail.ivanjermakov1.messenger.dto.PreviewDto;
import com.gmail.ivanjermakov1.messenger.dto.UserDto;
import com.gmail.ivanjermakov1.messenger.entity.User;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.exception.InvalidSearchFormatException;
import com.gmail.ivanjermakov1.messenger.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
	 * TODO: describe search algorithm more detailed
	 * Find preview by companion first or last name
	 *
	 * @param user   authenticated user. automatically maps, when {@literal Auth-Token} parameter present
	 * @param search search query
	 * @return list of found previews
	 * @throws AuthenticationException on invalid @param token
	 */
	@GetMapping("conversations")
	public List<PreviewDto> searchConversations(@ModelAttribute User user,
	                                            @RequestParam("search") String search,
	                                            Pageable pageable) throws AuthenticationException {
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
	 * @throws AuthenticationException      on invalid @param token
	 * @throws InvalidSearchFormatException on invalid @param query
	 */
	@GetMapping("users")
	public List<UserDto> searchUsers(@ModelAttribute User user,
	                                 @RequestParam("search") String search,
	                                 Pageable pageable) throws AuthenticationException, InvalidSearchFormatException {
		return searchService.searchUsers(search, pageable);
	}

}
