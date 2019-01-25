package com.gmail.ivanjermakov1.messenger.messaging.controller;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.auth.service.UserService;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.messaging.service.AvatarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("avatar")
public class AvatarController {
	
	private final UserService userService;
	private final AvatarService avatarService;
	
	@Autowired
	public AvatarController(UserService userService, AvatarService avatarService) {
		this.userService = userService;
		this.avatarService = avatarService;
	}
	
	@GetMapping("delete")
	public void delete(@RequestHeader("Auth-Token") String token, @RequestParam("avatarId") Long avatarId) throws AuthenticationException {
		User user = userService.authenticate(token);
		
		avatarService.delete(user, avatarId);
	}
	
}
