package com.gmail.ivanjermakov1.messenger.auth;

import com.gmail.ivanjermakov1.messenger.auth.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class UserTest {

	@Autowired
	private UserService userService;

	@Test
	public void shouldDeleteOnlineRecords() {
		userService.deleteOnline();
	}

}
