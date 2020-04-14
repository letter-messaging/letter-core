package com.gmail.ivanjermakov1.messenger.validator;

import com.github.ivanjermakov.jtrue.predicate.NotBlank;
import com.github.ivanjermakov.jtrue.predicate.NotNull;
import com.github.ivanjermakov.jtrue.validator.Validatable;
import com.github.ivanjermakov.jtrue.validator.Validator;
import com.gmail.ivanjermakov1.messenger.dto.RegisterUserDto;
import com.gmail.ivanjermakov1.messenger.exception.InvalidEntityException;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class RegisterUserDtoValidator implements Validatable<RegisterUserDto> {

	private Validator<String> stringValidator = new Validator<String>()
			.rule(new NotNull<>(), "cannot be null")
			.rule(new NotBlank(), "cannot be blank");

	private Validator<RegisterUserDto> validator = new Validator<RegisterUserDto>()
			.field(ru -> ru.firstName, v -> stringValidator)
			.field(ru -> ru.lastName, v -> stringValidator)
			.field(ru -> ru.login, v -> stringValidator)
			.field(ru -> ru.password, v -> stringValidator)
			.field(ru -> ru.password, v -> v
					.rule(p -> p.length() >= 8, "password must be between 8 and 32 characters long")
					.rule(p -> p.length() <= 32, "password must be between 8 and 32 characters long")
			);

	@Override
	public boolean validate(RegisterUserDto registerUserDto) {
		return validator.validate(registerUserDto);
	}

	public void throwInvalid(RegisterUserDto target) {
		validator.throwInvalid(target, (Function<String, InvalidEntityException>) InvalidEntityException::new);
	}

}
