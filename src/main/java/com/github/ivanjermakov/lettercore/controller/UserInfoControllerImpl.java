package com.github.ivanjermakov.lettercore.controller;

import com.github.ivanjermakov.lettercore.dto.UserInfoDto;
import com.github.ivanjermakov.lettercore.entity.User;
import com.github.ivanjermakov.lettercore.exception.AuthorizationException;
import com.github.ivanjermakov.lettercore.mapper.UserInfoMapper;
import com.github.ivanjermakov.lettercore.service.UserInfoService;
import com.github.ivanjermakov.lettercore.service.UserService;
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
public class UserInfoControllerImpl implements UserInfoController {

	private final UserService userService;
	private final UserInfoService userInfoService;

	private UserInfoMapper userInfoMapper;

	@Autowired
	public UserInfoControllerImpl(UserService userService, UserInfoService userInfoService) {
		this.userService = userService;
		this.userInfoService = userInfoService;
	}

	@Autowired
	public void setUserInfoMapper(UserInfoMapper userInfoMapper) {
		this.userInfoMapper = userInfoMapper;
	}

	@Override
	@GetMapping
	public UserInfoDto get(@ModelAttribute User user,
	                       @RequestParam("userId") Long userId) {
		return userInfoMapper.map(userService.getUser(userId).userInfo);
	}

	@Override
	@PostMapping
	public UserInfoDto edit(@ModelAttribute User user,
	                        @RequestBody UserInfoDto userInfoDto) throws AuthorizationException {
		return userInfoService.edit(user, userInfoDto);
	}

}
