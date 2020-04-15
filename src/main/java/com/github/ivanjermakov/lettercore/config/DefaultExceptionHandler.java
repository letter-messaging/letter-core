package com.github.ivanjermakov.lettercore.config;

import com.github.ivanjermakov.lettercore.exception.AuthenticationException;
import com.github.ivanjermakov.lettercore.exception.AuthorizationException;
import com.github.ivanjermakov.lettercore.exception.NoSuchEntityException;
import com.github.ivanjermakov.lettercore.exception.WebException;
import com.github.ivanjermakov.lettercore.service.ConversationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class DefaultExceptionHandler {

	private final static Logger LOG = LoggerFactory.getLogger(ConversationService.class);

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public WebException handleAnyException(HttpServletRequest req, Exception e) {
		LOG.debug(
				"handle " + e.getClass().getName() + (
						e.getMessage() != null
								? " with message: " + e.getMessage()
								: ""
				)
		);

		return WebException.builder()
				.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.exception(e)
				.request(req)
				.build();
	}

	@ExceptionHandler({AuthenticationException.class, AuthorizationException.class})
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ResponseBody
	public WebException handleAuthenticationException(HttpServletRequest req, Exception e) {
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