package com.gmail.ivanjermakov1.messenger.messaging;

import com.gmail.ivanjermakov1.messenger.controller.UserInfoController;
import com.gmail.ivanjermakov1.messenger.dto.UserInfoDto;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.exception.AuthorizationException;
import com.gmail.ivanjermakov1.messenger.exception.RegistrationException;
import com.gmail.ivanjermakov1.messenger.messaging.dto.TestingUser;
import com.gmail.ivanjermakov1.messenger.messaging.service.TestingService;
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
				user.token,
				user.user.id
		);

		Assert.assertNotNull(userInfo);
		Assert.assertEquals(userInfo.firstName, user.user.firstName);
	}

	@Test
	public void shouldGetAnotherUserInfo() throws AuthenticationException, RegistrationException {
		TestingUser user1 = testingService.registerUser("Jack");
		TestingUser user2 = testingService.registerUser("Ron");

		UserInfoDto user2Info = userInfoController.get(
				user1.token,
				user2.user.id
		);

		Assert.assertNotNull(user2Info);
		Assert.assertEquals(user2Info.firstName, user2.user.firstName);
	}

	@Test
	public void shouldEditUserInfo() throws RegistrationException, AuthenticationException {
		TestingUser user = testingService.registerUser("Jack");

		UserInfoDto userInfo = userInfoController.get(
				user.token,
				user.user.id
		);

		userInfo.firstName = "John";

		UserInfoDto editedUserInfo = userInfoController.edit(
				user.token,
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
				user1.token,
				user2.user.id
		);

		user2Info.firstName = "John";

		userInfoController.edit(
				user1.token,
				user2Info
		);
	}

}
