package com.gmail.ivanjermakov1.messenger.config;

import com.gmail.ivanjermakov1.messenger.entity.User;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.service.UserService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;

/**
 * Authentication controller advice configuration
 */
@ControllerAdvice
public class AuthenticationControllerAdvice {

	private final UserService userService;

	public AuthenticationControllerAdvice(UserService userService) {
		this.userService = userService;
	}

	/**
	 * Mapping of {@literal token} header from request to {@link User} entity automatically
	 *
	 * @param request http request
	 * @return mapped user. {@literal null} if request has no {@literal token} header
	 * @throws AuthenticationException when {@literal token} header is present but not valid
	 */
	@ModelAttribute("user")
	public User authenticate(HttpServletRequest request) throws AuthenticationException {
		String token = request.getHeader("Auth-Token");
		if (token != null) {
			return userService.authenticate(token);
		}
		return null;
	}

}