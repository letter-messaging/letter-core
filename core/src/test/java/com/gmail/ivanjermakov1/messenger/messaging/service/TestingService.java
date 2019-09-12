package com.gmail.ivanjermakov1.messenger.messaging.service;

import com.gmail.ivanjermakov1.messenger.auth.dto.RegisterUserDto;
import com.gmail.ivanjermakov1.messenger.auth.dto.UserDto;
import com.gmail.ivanjermakov1.messenger.auth.service.UserService;
import com.gmail.ivanjermakov1.messenger.core.mapper.UserMapper;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.exception.RegistrationException;
import com.gmail.ivanjermakov1.messenger.messaging.dto.TestingUser;
import com.gmail.ivanjermakov1.messenger.messaging.util.Images;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class TestingService {

	@Autowired
	private UserService userService;

	@Autowired
	private UserMapper userMapper;

	public TestingUser registerUser(String name) throws RegistrationException, AuthenticationException {
		String password = Strings.repeat(name, 4);

		RegisterUserDto registerUserDto = new RegisterUserDto(name, name, name, password);

		userService.register(registerUserDto);
		String userToken = userService.authenticate(name, password);
		UserDto user = userMapper.map(userService.authenticate(userToken));

		return new TestingUser(
				user,
				userToken
		);
	}

	public TestingUser getUser(String name) throws AuthenticationException {
		String password = Strings.repeat(name, 4);

		String userToken = userService.authenticate(name, password);
		UserDto user = userMapper.map(userService.authenticate(userToken));

		return new TestingUser(
				user,
				userToken
		);
	}

	public MultipartFile mockTestImage() throws IOException {
		return Images.multipartFileFromFile(
				new File(System.getProperty("user.dir") + "/src/test/resources/test.jpg")
		);
	}

}
