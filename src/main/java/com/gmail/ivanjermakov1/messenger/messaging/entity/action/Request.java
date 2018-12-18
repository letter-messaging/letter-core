package com.gmail.ivanjermakov1.messenger.messaging.entity.action;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import org.springframework.web.context.request.async.DeferredResult;

public class Request<T extends Action> {
	
	private User user;
	private DeferredResult<T> deferredResult;
	
	public Request(User user, DeferredResult<T> deferredResult) {
		this.user = user;
		this.deferredResult = deferredResult;
	}
	
	public boolean isSetOrExpired() {
		return deferredResult.isSetOrExpired();
	}
	
	public User getUser() {
		return user;
	}
	
	public DeferredResult<T> getDeferredResult() {
		return deferredResult;
	}
	
	public boolean isTimeout() {
		return deferredResult.isSetOrExpired();
	}
	
	public void set(T t) {
		deferredResult.setResult(t);
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Request<?> request = (Request<?>) o;
		return user.equals(request.user) &&
				deferredResult.equals(request.deferredResult);
	}
	
}
