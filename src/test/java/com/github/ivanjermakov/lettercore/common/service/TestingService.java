package com.github.ivanjermakov.lettercore.common.service;

import com.github.ivanjermakov.lettercore.auth.exception.AuthenticationException;
import com.github.ivanjermakov.lettercore.auth.exception.RegistrationException;
import com.github.ivanjermakov.lettercore.common.dto.TestingUser;
import com.github.ivanjermakov.lettercore.user.dto.RegisterUserDto;
import com.github.ivanjermakov.lettercore.user.dto.UserDto;
import com.github.ivanjermakov.lettercore.user.entity.User;
import com.github.ivanjermakov.lettercore.user.mapper.UserMapper;
import com.github.ivanjermakov.lettercore.user.service.UserService;
import com.github.ivanjermakov.lettercore.util.Files;
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
