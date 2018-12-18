package com.gmail.ivanjermakov1.messenger.auth;

import com.gmail.ivanjermakov1.messenger.auth.controller.RegistrationController;
import com.gmail.ivanjermakov1.messenger.exception.InvalidPasswordException;
import com.gmail.ivanjermakov1.messenger.exception.RegistrationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class PasswordTest {
	
	@Autowired
	private RegistrationController registrationController;
	
	@Test(expected = InvalidPasswordException.class)
	public void shouldThrowInvalidPasswordException_WithEmptyPassword() throws RegistrationException {
		registrationController.register("Jack", "Johnson", "jackj", "");
	}
	
	@Test(expected = InvalidPasswordException.class)
	public void shouldThrowInvalidPasswordException_WithBlankPassword() throws RegistrationException {
		registrationController.register("Jack", "Johnson", "jackj", "   ");
	}
	
	@Test(expected = InvalidPasswordException.class)
	public void shouldThrowInvalidPasswordException_WithLessThen8Characters() throws RegistrationException {
		registrationController.register("Jack", "Johnson", "jackj", "1234567");
	}
	
}
