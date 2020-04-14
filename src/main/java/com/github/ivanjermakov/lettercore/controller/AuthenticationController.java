package com.github.ivanjermakov.lettercore.controller;

import com.github.ivanjermakov.lettercore.dto.UserDto;
import com.github.ivanjermakov.lettercore.entity.User;
import com.github.ivanjermakov.lettercore.exception.AuthenticationException;

public interface AuthenticationController {

	/**
	 * Authenticate user by its credentials.
	 *
	 * @param login    user login
	 * @param password user password
	 * @return user authentication token
	 * @throws AuthenticationException on invalid credentials
	 */
//	TODO: use AuthUserDto
	String authenticate(String login, String password) throws AuthenticationException;

	/**
	 * Validate user token. During validation user appears online.
	 *
	 * @param user  authenticated user. automatically maps, when {@literal Auth-Token} parameter present
	 * @param token user token
	 * @return user
	 * @throws AuthenticationException on invalid token
	 */
	UserDto validate(User user, String token) throws AuthenticationException;

	/**
	 * Logout current user from everywhere by removing all his tokens. To login user is forced to authenticate again.
	 *
	 * @param user authenticated user. automatically maps, when {@literal Auth-Token} parameter present
	 */
	void logout(User user);

}
