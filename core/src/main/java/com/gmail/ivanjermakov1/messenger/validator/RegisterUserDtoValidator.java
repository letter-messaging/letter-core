package com.gmail.ivanjermakov1.messenger.validator;

import com.github.ivanjermakov.jtrue.core.Validatable;
import com.github.ivanjermakov.jtrue.core.Validator;
import com.github.ivanjermakov.jtrue.predicate.NotBlank;
import com.github.ivanjermakov.jtrue.predicate.NotNull;
import com.gmail.ivanjermakov1.messenger.dto.RegisterUserDto;
import com.gmail.ivanjermakov1.messenger.exception.InvalidEntityException;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class RegisterUserDtoValidator implements Validatable<RegisterUserDto> {

	private Function<String, Validator<String>> function = m -> new Validator<String>()
			.rule(new NotNull<>(), "cannot be null")
			.rule(new NotBlank(), "cannot be blank")
			.throwing(__ -> new InvalidEntityException(m + ": "));

	private Validator<RegisterUserDto> validator = new Validator<RegisterUserDto>()
			.map(ru -> ru.firstName).use(function.apply("first name"))
			.map(ru -> ru.lastName).use(function.apply("last name"))
			.map(ru -> ru.login).use(function.apply("login"))
			.map(ru -> ru.password).use(function.apply("password"))
			.map(ru -> ru.password).use(new Validator<String>()
					.rule(p -> p.length() >= 8, "password must be between 8 and 32 characters long")
					.rule(p -> p.length() <= 32, "password must be between 8 and 32 characters long")
			)
			.throwing((Function<String, Throwable>) InvalidEntityException::new);

	@Override
	public boolean validate(RegisterUserDto registerUserDto) {
		return validator.validate(registerUserDto);
	}

	@Override
	public void throwInvalid(RegisterUserDto target) {
		try {
			validator.throwInvalid(target);
		} catch (InvalidEntityException e) {
			throw e;
		} catch (Throwable ignored) {
		}
	}

}
