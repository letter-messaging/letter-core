package com.github.ivanjermakov.lettercore.user.unit;

import com.github.ivanjermakov.lettercore.auth.exception.AuthenticationException;
import com.github.ivanjermakov.lettercore.auth.exception.RegistrationException;
import com.github.ivanjermakov.lettercore.common.dto.TestingUser;
import com.github.ivanjermakov.lettercore.common.exception.NoSuchEntityException;
import com.github.ivanjermakov.lettercore.common.service.TestingService;
import com.github.ivanjermakov.lettercore.user.dto.UserInfoDto;
import com.github.ivanjermakov.lettercore.user.repository.UserInfoRepository;
import com.github.ivanjermakov.lettercore.user.service.UserInfoService;
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
