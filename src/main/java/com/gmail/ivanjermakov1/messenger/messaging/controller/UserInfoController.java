package com.gmail.ivanjermakov1.messenger.messaging.controller;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.auth.service.UserService;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.exception.NoSuchEntityException;
import com.gmail.ivanjermakov1.messenger.messaging.dto.UserInfoDTO;
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
	
	@GetMapping
	public UserInfoDTO get(@RequestHeader("Auth-Token") String token, @RequestParam("userId") Long userId) throws AuthenticationException {
		User user = userService.authenticate(token);
		
		return userInfoService.get(userId);
	}
	
	@PostMapping
	public UserInfoDTO edit(@RequestHeader("Auth-Token") String token, @RequestBody UserInfoDTO userInfoDTO) throws AuthenticationException, NoSuchEntityException {
		User user = userService.authenticate(token);
		
		return userInfoService.edit(user, userInfoDTO);
	}
	
}
