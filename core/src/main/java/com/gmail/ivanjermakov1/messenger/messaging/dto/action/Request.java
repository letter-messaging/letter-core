package com.gmail.ivanjermakov1.messenger.messaging.dto.action;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public class Request<T> {

	private User user;
	private SseEmitter emitter;

	public Request() {
	}

	public Request(User user, SseEmitter emitter) {
		this.user = user;
		this.emitter = emitter;
	}

	public User getUser() {
		return user;
	}

	public SseEmitter getEmitter() {
		return emitter;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Request<?> request = (Request<?>) o;
		return user.equals(request.user) &&
				emitter.equals(request.emitter);
	}

}
