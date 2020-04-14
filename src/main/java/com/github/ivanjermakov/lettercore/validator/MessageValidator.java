package com.github.ivanjermakov.lettercore.validator;

import com.github.ivanjermakov.jtrue.predicate.NotEmptyCollection;
import com.github.ivanjermakov.jtrue.predicate.NotNull;
import com.github.ivanjermakov.jtrue.predicate.Null;
import com.github.ivanjermakov.jtrue.validator.Validatable;
import com.github.ivanjermakov.jtrue.validator.Validator;
import com.github.ivanjermakov.lettercore.entity.Message;
import com.github.ivanjermakov.lettercore.exception.InvalidEntityException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class MessageValidator implements Validatable<Message> {

	private final Predicate<List<?>> nullOrEmpty = new Null<List<?>>().or(new NotEmptyCollection<>().negate());

	private final Validator<Message> attachmentValidator = new Validator<Message>()
			.field(m -> m.forwarded, v -> v
					.rule(nullOrEmpty::test))
			.field(m -> m.images, v -> v
					.rule(nullOrEmpty::test))
			.field(m -> m.documents, v -> v
					.rule(nullOrEmpty::test));

	private final Validator<Message> validator = new Validator<Message>()
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
			.rule(m -> !attachmentValidator.validate(m) || !(m == null || m.text.isEmpty()), "message without text must contain attachments");

	@Override
	public boolean validate(Message target) {
		return validator.validate(target);
	}

	public void throwInvalid(Message target) throws InvalidEntityException {
		validator.throwInvalid(target, m -> new InvalidEntityException(m));
	}

}
