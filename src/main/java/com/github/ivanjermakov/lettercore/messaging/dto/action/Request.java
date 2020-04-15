package com.github.ivanjermakov.lettercore.messaging.dto.action;

import com.github.ivanjermakov.lettercore.user.entity.User;
import reactor.core.publisher.FluxSink;

public class Request<T> {

	public User user;
	public FluxSink<T> listener;

	public Request(User user, FluxSink<T> listener) {
		this.user = user;
		this.listener = listener;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Request<?> request = (Request<?>) o;
		return user.equals(request.user) &&
				listener.equals(request.listener);
	}

}
