package com.github.ivanjermakov.lettercore.test.integration;

import com.github.ivanjermakov.lettercore.controller.UserInfoController;
import com.github.ivanjermakov.lettercore.dto.TestingUser;
import com.github.ivanjermakov.lettercore.dto.UserInfoDto;
import com.github.ivanjermakov.lettercore.exception.AuthenticationException;
import com.github.ivanjermakov.lettercore.exception.AuthorizationException;
import com.github.ivanjermakov.lettercore.exception.RegistrationException;
import com.github.ivanjermakov.lettercore.service.TestingService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class UserInfoTest {

	@Autowired
	private UserInfoController userInfoController;

	@Autowired
	private TestingService testingService;

	@Test
	public void shouldGetSelfUserInfo() throws AuthenticationException, RegistrationException {
		TestingUser user = testingService.registerUser("Jack");

		UserInfoDto userInfo = userInfoController.get(
				user.user,
				user.user.id
		);

		Assert.assertNotNull(userInfo);
		Assert.assertEquals(userInfo.firstName, user.userDto.firstName);
	}

	@Test
	public void shouldGetAnotherUserInfo() throws AuthenticationException, RegistrationException {
		TestingUser user1 = testingService.registerUser("Jack");
		TestingUser user2 = testingService.registerUser("Ron");

		UserInfoDto user2Info = userInfoController.get(
				user1.user,
				user2.user.id
		);

		Assert.assertNotNull(user2Info);
		Assert.assertEquals(user2Info.firstName, user2.userDto.firstName);
	}

	@Test
	public void shouldEditUserInfo() throws RegistrationException, AuthenticationException {
		TestingUser user = testingService.registerUser("Jack");

		UserInfoDto userInfo = userInfoController.get(
				user.user,
				user.user.id
		);

		userInfo.firstName = "John";

		UserInfoDto editedUserInfo = userInfoController.edit(
				user.user,
				userInfo
		);

		Assert.assertNotNull(editedUserInfo);
		Assert.assertEquals("John", editedUserInfo.firstName);
	}

	@Test(expected = AuthorizationException.class)
	public void shouldThrowException_WhenEditAnotherUserInfo() throws RegistrationException, AuthenticationException {
		TestingUser user1 = testingService.registerUser("Jack");
		TestingUser user2 = testingService.registerUser("Ron");

		UserInfoDto user2Info = userInfoController.get(
				user1.user,
				user2.user.id
		);

		user2Info.firstName = "John";

		userInfoController.edit(
				user1.user,
				user2Info
		);
	}

}
