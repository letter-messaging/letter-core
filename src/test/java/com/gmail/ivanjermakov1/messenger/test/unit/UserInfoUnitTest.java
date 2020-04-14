package com.gmail.ivanjermakov1.messenger.test.unit;

import com.gmail.ivanjermakov1.messenger.dto.TestingUser;
import com.gmail.ivanjermakov1.messenger.dto.UserInfoDto;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.exception.NoSuchEntityException;
import com.gmail.ivanjermakov1.messenger.exception.RegistrationException;
import com.gmail.ivanjermakov1.messenger.repository.UserInfoRepository;
import com.gmail.ivanjermakov1.messenger.service.TestingService;
import com.gmail.ivanjermakov1.messenger.service.UserInfoService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class UserInfoUnitTest {

	@InjectMocks
	private UserInfoService userInfoService;

	@Autowired
	private TestingService testingService;

	@Mock
	private UserInfoRepository userInfoRepository;

	private TestingUser user;

	@Before
	public void before() throws RegistrationException, AuthenticationException {
		user = testingService.registerUser("Jack");
	}

	@Test(expected = NoSuchEntityException.class)
	public void shouldThrowException_WhenNoSuchUserInfo() {
		when(userInfoRepository.findByUser(any()))
				.thenReturn(Optional.empty());

		userInfoService.edit(
				user.user,
				new UserInfoDto(
						user.userDto,
						"Jack",
						"Jack"
				)
		);
	}

}
