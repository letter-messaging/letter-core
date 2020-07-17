package com.github.ivanjermakov.lettercore.auth.controller;

import com.github.ivanjermakov.lettercore.auth.exception.AuthenticationException;
import com.github.ivanjermakov.lettercore.common.exception.InvalidEntityException;
import com.github.ivanjermakov.lettercore.common.exception.NoSuchEntityException;

public interface PasswordRecoveryController {

	/**
	 * Request password recovery using specified in user info mail
	 *
	 * @param login user login
	 * @throws AuthenticationException on invalid @param login
	 * @throws NoSuchEntityException   if mail is not specified
	 */
	//	TODO: eager mail verification
	void requestMail(String login) throws AuthenticationException, NoSuchEntityException, InvalidEntityException;

	/**
	 * Sets new password using email token received using `requestMail()` method
	 *
	 * @param token       received mail token
	 * @param newPassword new password
	 * @throws AuthenticationException on invalid @param token
	 * @throws InvalidEntityException  if password is invalid
	 */
	void changePasswordMail(String token, String newPassword) throws AuthenticationException, InvalidEntityException;

}
