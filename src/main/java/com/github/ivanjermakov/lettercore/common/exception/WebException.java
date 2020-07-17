package com.github.ivanjermakov.lettercore.common.exception;

import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;

public class WebException {

	public Integer status;
	public String error;
	public String message;
	public String path;

	public WebException(Integer status, String error, String message, String path) {
		this.status = status;
		this.error = error;
		this.message = message;
		this.path = path;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {

		public HttpStatus status;
		public String message;
		public String path;

		public HttpServletRequest request;
		public Exception exception;

		public Builder status(HttpStatus status) {
			this.status = status;
			return this;
		}

		public Builder message(String message) {
			this.message = message;
			return this;
		}

		public Builder path(String path) {
			this.path = path;
			return this;
		}

		public Builder request(HttpServletRequest request) {
			this.request = request;
			return this;
		}

		public Builder exception(Exception exception) {
			this.exception = exception;
			return this;
		}

		public WebException build() {
			WebException webException = new WebException(
					status.value(),
					status.getReasonPhrase(),
					message,
					path
			);

			if (request != null) {
				webException.path = request.getRequestURI();
			}

			if (exception != null) {
				webException.message = exception.getMessage();
			}

			return webException;
		}

	}


}
