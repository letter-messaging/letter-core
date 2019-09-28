package com.gmail.ivanjermakov1.messenger.validator;

import com.github.ivanjermakov.jtrue.core.Validatable;
import com.github.ivanjermakov.jtrue.core.Validator;
import com.github.ivanjermakov.jtrue.predicate.NotEmptyCollection;
import com.github.ivanjermakov.jtrue.predicate.NotNull;
import com.gmail.ivanjermakov1.messenger.entity.Document;
import com.gmail.ivanjermakov1.messenger.entity.Image;
import com.gmail.ivanjermakov1.messenger.entity.Message;
import com.gmail.ivanjermakov1.messenger.exception.InvalidEntityException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MessageValidator implements Validatable<Message> {

	private Validator<Message> attachmentValidator = new Validator<Message>()
			.field(m -> m.forwarded, v -> v
					.rule(new NotNull<List<Message>>()
							.and(new NotEmptyCollection<>())
							.negate()))
			.field(m -> m.images, v -> v
					.rule(new NotNull<List<Image>>()
							.and(new NotEmptyCollection<>())
							.negate()))
			.field(m -> m.documents, v -> v
					.rule(new NotNull<List<Document>>()
							.and(new NotEmptyCollection<>())
							.negate()));

	private Validator<Message> validator = new Validator<Message>()
			.field(m -> m.sender, senderValidator -> senderValidator
					.rule(new NotNull<>(), "user cannot be null")
					.field(u -> u.id, senderIdValidator -> senderIdValidator
							.rule(new NotNull<>(), "user id cannot be null")
					)
			)
			.field(m -> m.conversation, conversationValidator -> conversationValidator
					.rule(new NotNull<>(), "conversation cannot be null")
					.field(c -> c.id, conversationIdValidator -> conversationIdValidator
							.rule(new NotNull<>(), "conversation id cannot be null")
					)
			)
			.rule(m -> !m.text.isEmpty() || !attachmentValidator.validate(m), "message without text must contain attachments");

	@Override
	public boolean validate(Message target) {
		return validator.validate(target);
	}

	public void throwInvalid(Message target) throws InvalidEntityException {
		validator.throwInvalid(target, m -> new InvalidEntityException(m));
	}

}
