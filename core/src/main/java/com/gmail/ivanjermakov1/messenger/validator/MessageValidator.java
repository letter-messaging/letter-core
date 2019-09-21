package com.gmail.ivanjermakov1.messenger.validator;

import com.github.ivanjermakov.jtrue.core.Validatable;
import com.github.ivanjermakov.jtrue.core.Validator;
import com.github.ivanjermakov.jtrue.predicate.NotEmptyCollection;
import com.github.ivanjermakov.jtrue.predicate.NotNull;
import com.gmail.ivanjermakov1.messenger.entity.Conversation;
import com.gmail.ivanjermakov1.messenger.entity.Document;
import com.gmail.ivanjermakov1.messenger.entity.Image;
import com.gmail.ivanjermakov1.messenger.entity.Message;
import com.gmail.ivanjermakov1.messenger.entity.User;
import com.gmail.ivanjermakov1.messenger.exception.InvalidEntityException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Component
public class MessageValidator implements Validatable<Message> {

	private Validator<Message> attachmentValidator = new Validator<Message>()
			.map(m -> m.forwarded).rule(new NotNull<List<Message>>().and(new NotEmptyCollection<>()).negate())
			.map(m -> m.images).rule(new NotNull<List<Image>>().and(new NotEmptyCollection<>()).negate())
			.map(m -> m.documents).rule(new NotNull<List<Document>>().and(new NotEmptyCollection<>()).negate());

	private Validator<Message> validator = new Validator<Message>()
			.map(m -> m.sender).use(new Validator<User>()
					.rule(new NotNull<>(), "user cannot be null")
					.map(u -> u.id).rule(new NotNull<>(), "user id cannot be null")
			)
			.map(m -> m.conversation).use(new Validator<Conversation>()
					.rule(new NotNull<>(), "conversation cannot be null")
					.map(c -> c.id).rule(new NotNull<>(), "conversation id cannot be null")
			)
			.rule(m -> !m.text.isEmpty() || !attachmentValidator.validate(m), "message without text must contain attachments")
			.throwing((Function<String, Throwable>) InvalidEntityException::new);

	@Override
	public boolean validate(Message target) {
		return validator.validate(target);
	}

	@Override
	public void throwInvalid(Message target) throws InvalidEntityException {
		try {
			validator.throwInvalid(target);
		} catch (InvalidEntityException e) {
			throw e;
		} catch (Throwable ignored) {
		}
	}
}
