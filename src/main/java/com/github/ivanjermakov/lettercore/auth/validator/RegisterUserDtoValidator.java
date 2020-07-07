package com.github.ivanjermakov.lettercore.auth.validator;

import com.github.ivanjermakov.jtrue.predicate.NotBlank;
import com.github.ivanjermakov.jtrue.predicate.NotNull;
import com.github.ivanjermakov.jtrue.validator.Validatable;
import com.github.ivanjermakov.jtrue.validator.Validator;
import com.github.ivanjermakov.lettercore.common.exception.InvalidEntityException;
import com.github.ivanjermakov.lettercore.user.dto.RegisterUserDto;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class RegisterUserDtoValidator implements Validatable<RegisterUserDto> {

	private final Function<Validator<String>, Validator<String>> stringValidatorFunction = v -> v
			.rule(new NotNull<>(), "cannot be null")
			.rule(new NotBlank(), "cannot be blank");

	private final Validator<RegisterUserDto> validator = new Validator<RegisterUserDto>()
			.field(ru -> ru.firstName, stringValidatorFunction)
			.field(ru -> ru.lastName, stringValidatorFunction)
			.field(ru -> ru.login, stringValidatorFunction)
			.field(ru -> ru.password, stringValidatorFunction)
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
