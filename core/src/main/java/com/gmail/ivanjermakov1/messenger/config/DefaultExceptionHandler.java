package com.gmail.ivanjermakov1.messenger.config;

import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.exception.AuthorizationException;
import com.gmail.ivanjermakov1.messenger.exception.NoSuchEntityException;
import com.gmail.ivanjermakov1.messenger.exception.WebException;
import com.gmail.ivanjermakov1.messenger.service.ConversationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
class DefaultExceptionHandler {

	private final static Logger LOG = LoggerFactory.getLogger(ConversationService.class);

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public WebException handleAnyException(HttpServletRequest req, Exception e) {
		LOG.debug("handle " + e.getClass().getName() + " with message: " + e.getMessage());

		return WebException.builder()
				.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.exception(e)
				.request(req)
				.build();
	}

	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(AuthenticationException.class)
	@ResponseBody
	public WebException handleAuthenticationException(HttpServletRequest req, Exception e) {
		return WebException.builder()
				.status(HttpStatus.UNAUTHORIZED)
				.message(e.getMessage())
				.path(req.getRequestURI())
				.build();
	}

	@ExceptionHandler(AuthorizationException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ResponseBody
	public WebException handleAuthorizationException(HttpServletRequest req, Exception e) {
		return WebException.builder()
				.status(HttpStatus.UNAUTHORIZED)
				.exception(e)
				.request(req)
				.build();
	}

	@ExceptionHandler(NoSuchEntityException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public WebException handleNoSuchEntityException(HttpServletRequest req, Exception e) {
		return WebException.builder()
				.status(HttpStatus.BAD_REQUEST)
				.exception(e)
				.request(req)
				.build();
	}

}