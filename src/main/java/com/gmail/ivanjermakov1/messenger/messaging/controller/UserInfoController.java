package com.gmail.ivanjermakov1.messenger.messaging.controller;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.auth.service.UserService;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.messaging.dto.UserInfoDto;
import com.gmail.ivanjermakov1.messenger.messaging.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("info")
public class UserInfoController {
	
	private final UserService userService;
	private final UserInfoService userInfoService;
	
	@Autowired
	public UserInfoController(UserService userService, UserInfoService userInfoService) {
		this.userService = userService;
		this.userInfoService = userInfoService;
	}
	
	/**
	 * Get user info.
	 *
	 * @param token  user token
	 * @param userId user id to get user info of
	 * @return user info
	 * @throws AuthenticationException on invalid @param token
	 */
	@GetMapping
	public UserInfoDto get(@RequestHeader("Auth-Token") String token, @RequestParam("userId") Long userId) throws AuthenticationException {
		User user = userService.authenticate(token);
		
		return userInfoService.get(userId);
	}
	
	/**
	 * Edit user info.
	 * It is allowed to edit fields with null.
	 *
	 * @param token       user token
	 * @param userInfoDto user info with edits
	 * @return edited user info
	 * @throws AuthenticationException on invalid @param token and attempting to edit other user info
	 */
	@PostMapping
	public UserInfoDto edit(@RequestHeader("Auth-Token") String token, @RequestBody UserInfoDto userInfoDto) throws AuthenticationException {
		User user = userService.authenticate(token);
		
		return userInfoService.edit(user, userInfoDto);
	}
	
}
