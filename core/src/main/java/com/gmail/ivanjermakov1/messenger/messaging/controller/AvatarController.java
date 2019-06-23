package com.gmail.ivanjermakov1.messenger.messaging.controller;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.auth.service.UserService;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.exception.InvalidFileException;
import com.gmail.ivanjermakov1.messenger.messaging.dto.AvatarDto;
import com.gmail.ivanjermakov1.messenger.messaging.service.AvatarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
	
	/**
	 * Upload avatar.
	 *
	 * @param token  user token
	 * @param avatar multipart file to be uploaded
	 * @return uploaded avatar
	 * @throws AuthenticationException on invalid token
	 * @throws IOException             on server file system error
	 * @throws InvalidFileException    on upload of invalid file (mostly caused by invalid file extension or file size
	 *                                 specified in @value {@code spring.servlet.multipart.max-file-size})
	 */
	@PostMapping("upload")
	public AvatarDto upload(@RequestHeader("Auth-Token") String token,
	                        @RequestParam("avatar") MultipartFile avatar) throws AuthenticationException, IOException, InvalidFileException {
		User user = userService.authenticate(token);
		
		return avatarService.upload(user, avatar);
	}
	
	/**
	 * Delete avatar from current avatar and avatar list
	 *
	 * @param token    user token
	 * @param avatarId avatar id to delete
	 * @throws AuthenticationException on invalid token
	 */
	@GetMapping("delete")
	public void delete(@RequestHeader("Auth-Token") String token,
	                   @RequestParam("avatarId") Long avatarId) throws AuthenticationException {
		User user = userService.authenticate(token);
		
		avatarService.delete(user, avatarId);
	}
	
}
