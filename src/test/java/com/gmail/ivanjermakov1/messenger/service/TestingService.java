package com.gmail.ivanjermakov1.messenger.service;

import com.gmail.ivanjermakov1.messenger.dto.RegisterUserDto;
import com.gmail.ivanjermakov1.messenger.dto.TestingUser;
import com.gmail.ivanjermakov1.messenger.dto.UserDto;
import com.gmail.ivanjermakov1.messenger.entity.User;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.exception.RegistrationException;
import com.gmail.ivanjermakov1.messenger.mapper.UserMapper;
import com.gmail.ivanjermakov1.messenger.util.Files;
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
		User user = userService.authenticate(userToken);
		UserDto userDto = userMapper.map(user);

		return new TestingUser(
				user,
				userDto,
				userToken
		);
	}

	public TestingUser getUser(String name) throws AuthenticationException {
		String password = Strings.repeat(name, 4);

		String userToken = userService.authenticate(name, password);
		User user = userService.authenticate(userToken);
		UserDto userDto = userMapper.map(user);

		return new TestingUser(
				user,
				userDto,
				userToken
		);
	}

	public MultipartFile mockTestImage() throws IOException {
		return Files.multipartFileFromFile(
				new File(System.getProperty("user.dir") + "/src/test/resources/test.jpg")
		);
	}

	public MultipartFile mockTestFile() throws IOException {
		return Files.multipartFileFromFile(
				new File(System.getProperty("user.dir") + "/src/test/resources/test.doc")
		);
	}

}
