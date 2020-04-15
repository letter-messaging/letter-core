package com.github.ivanjermakov.lettercore.test.integration;

import com.github.ivanjermakov.lettercore.controller.AvatarController;
import com.github.ivanjermakov.lettercore.dto.AvatarDto;
import com.github.ivanjermakov.lettercore.dto.TestingUser;
import com.github.ivanjermakov.lettercore.exception.AuthenticationException;
import com.github.ivanjermakov.lettercore.exception.RegistrationException;
import com.github.ivanjermakov.lettercore.service.TestingService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class AvatarTest {

	@Autowired
	private TestingService testingService;

	@Autowired
	private AvatarController avatarController;

	@Test
	public void shouldUploadAvatar() throws RegistrationException, AuthenticationException, IOException {
		TestingUser user = testingService.registerUser("Jack");

		MultipartFile file = testingService.mockTestImage();

		AvatarDto avatar = avatarController.upload(
				user.user,
				file
		);

		Assert.assertNotNull(avatar);
		Assert.assertNotNull(avatar.id);

		TestingUser userWithAvatar = testingService.getUser("Jack");

		Assert.assertNotNull(userWithAvatar);
		Assert.assertNotNull(userWithAvatar.userDto.avatar);
		Assert.assertEquals(avatar.id, userWithAvatar.userDto.avatar.id);
	}

	@Test
	public void shouldDeleteAvatar() throws RegistrationException, AuthenticationException, IOException {
		TestingUser user = testingService.registerUser("Jack");

		AvatarDto avatar = avatarController.upload(
				user.user,
				testingService.mockTestImage()
		);

		avatarController.delete(user.user, avatar.id);

		TestingUser userWithoutAvatar = testingService.getUser("Jack");

		Assert.assertNotNull(userWithoutAvatar);
		Assert.assertNotNull(userWithoutAvatar.userDto.avatar);
		Assert.assertNull(userWithoutAvatar.userDto.avatar.id);
	}

}
