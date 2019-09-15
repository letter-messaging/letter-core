package com.gmail.ivanjermakov1.messenger.controller;

import com.gmail.ivanjermakov1.messenger.dto.UserInfoDto;
import com.gmail.ivanjermakov1.messenger.entity.User;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.exception.AuthorizationException;
import com.gmail.ivanjermakov1.messenger.mapper.UserInfoMapper;
import com.gmail.ivanjermakov1.messenger.service.UserInfoService;
import com.gmail.ivanjermakov1.messenger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("info")
@Transactional
public class UserInfoController {

	private final UserService userService;
	private final UserInfoService userInfoService;

	private UserInfoMapper userInfoMapper;

	@Autowired
	public UserInfoController(UserService userService, UserInfoService userInfoService) {
		this.userService = userService;
		this.userInfoService = userInfoService;
	}

	@Autowired
	public void setUserInfoMapper(UserInfoMapper userInfoMapper) {
		this.userInfoMapper = userInfoMapper;
	}

	/**
	 * Get user info.
	 *
	 * @param user   authenticated user. automatically maps, when {@literal Auth-Token} parameter present
	 * @param userId user id to get user info of
	 * @return user info
	 * @throws AuthenticationException on invalid @param token
	 */
	@GetMapping
	public UserInfoDto get(@ModelAttribute User user,
	                       @RequestParam("userId") Long userId) throws AuthenticationException {
		return userInfoMapper.map(userService.getUser(userId).userInfo);
	}

	/**
	 * Edit user info.
	 * It is allowed to edit fields with null to remove their value.
	 *
	 * @param user        authenticated user. automatically maps, when {@literal Auth-Token} parameter present
	 * @param userInfoDto user info with edits
	 * @return edited user info
	 * @throws AuthenticationException on invalid @param token
	 * @throws AuthorizationException  on attempting to edit other user info
	 */
	@PostMapping
	public UserInfoDto edit(@ModelAttribute User user,
	                        @RequestBody UserInfoDto userInfoDto) throws AuthenticationException, AuthorizationException {
		return userInfoService.edit(user, userInfoDto);
	}

}
