package com.gmail.ivanjermakov1.messenger.messaging.controller;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.auth.service.UserService;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.exception.NoSuchEntityException;
import com.gmail.ivanjermakov1.messenger.messaging.dto.PreviewDTO;
import com.gmail.ivanjermakov1.messenger.messaging.dto.UserDTO;
import com.gmail.ivanjermakov1.messenger.messaging.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("search")
public class SearchController {
	
	private final SearchService searchService;
	private final UserService userService;
	
	@Autowired
	public SearchController(SearchService searchService, UserService userService) {
		this.searchService = searchService;
		this.userService = userService;
	}
	
	@GetMapping("conversations")
	public List<PreviewDTO> searchConversations(@RequestParam("token") String token,
	                                            @RequestParam("search") String search) throws AuthenticationException {
		try {
			User user = userService.getUser(userService.getUserId(token));
			return searchService.searchConversations(user, search);
		} catch (NoSuchEntityException e) {
			throw new AuthenticationException("invalid token");
		}
	}
	
	@GetMapping("users")
	public List<UserDTO> searchUsers(@RequestParam("token") String token,
	                                 @RequestParam("search") String search) throws AuthenticationException {
		try {
			userService.getUser(userService.getUserId(token));
			return searchService.searchUsers(search);
		} catch (NoSuchEntityException e) {
			throw new AuthenticationException("invalid token");
		}
	}
	
}
