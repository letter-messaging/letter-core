package com.gmail.ivanjermakov1.messenger.messaging.controller;

import com.gmail.ivanjermakov1.messenger.auth.dto.UserDto;
import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.auth.service.UserService;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.exception.InvalidSearchFormatException;
import com.gmail.ivanjermakov1.messenger.messaging.dto.PreviewDto;
import com.gmail.ivanjermakov1.messenger.messaging.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
	public List<PreviewDto> searchConversations(@RequestHeader("Auth-Token") String token,
	                                            @RequestParam("search") String search) throws AuthenticationException {
		User user = userService.authenticate(token);
		return searchService.searchConversations(user, search);
	}
	
	@GetMapping("users")
	public List<UserDto> searchUsers(@RequestHeader("Auth-Token") String token,
	                                 @RequestParam("search") String search) throws AuthenticationException, InvalidSearchFormatException {
		userService.authenticate(token);
		return searchService.searchUsers(search);
	}
	
}
