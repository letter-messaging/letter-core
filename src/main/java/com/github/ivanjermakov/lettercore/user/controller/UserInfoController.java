package com.github.ivanjermakov.lettercore.user.controller;

import com.github.ivanjermakov.lettercore.auth.exception.AuthorizationException;
import com.github.ivanjermakov.lettercore.user.dto.UserInfoDto;
import com.github.ivanjermakov.lettercore.user.entity.User;
import com.github.ivanjermakov.lettercore.user.mapper.UserInfoMapper;
import com.github.ivanjermakov.lettercore.user.service.UserInfoService;
import com.github.ivanjermakov.lettercore.user.service.UserService;
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
	 */
	@GetMapping
	public UserInfoDto get(@ModelAttribute User user,
	                       @RequestParam("userId") Long userId) {
		return userInfoMapper.map(userService.getUser(userId).userInfo);
	}

	/**
	 * Edit user info.
	 * It is allowed to edit fields with null to remove their value.
	 *
	 * @param user        authenticated user. automatically maps, when {@literal Auth-Token} parameter present
	 * @param userInfoDto user info with edits
	 * @return edited user info
	 * @throws AuthorizationException on attempting to edit other user info
	 */
	@PostMapping
	public UserInfoDto edit(@ModelAttribute User user,
	                        @RequestBody UserInfoDto userInfoDto) throws AuthorizationException {
		return userInfoService.edit(user, userInfoDto);
	}

}
