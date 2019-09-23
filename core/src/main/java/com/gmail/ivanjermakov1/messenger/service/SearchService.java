package com.gmail.ivanjermakov1.messenger.service;

import com.github.ivanjermakov.jtrue.core.Validator;
import com.github.ivanjermakov.jtrue.exception.InvalidObjectException;
import com.github.ivanjermakov.jtrue.predicate.NotBlank;
import com.github.ivanjermakov.jtrue.predicate.NotNull;
import com.gmail.ivanjermakov1.messenger.dto.PreviewDto;
import com.gmail.ivanjermakov1.messenger.dto.UserDto;
import com.gmail.ivanjermakov1.messenger.entity.User;
import com.gmail.ivanjermakov1.messenger.exception.InvalidEntityException;
import com.gmail.ivanjermakov1.messenger.exception.InvalidSearchFormatException;
import com.gmail.ivanjermakov1.messenger.mapper.PreviewMapper;
import com.gmail.ivanjermakov1.messenger.mapper.UserMapper;
import com.gmail.ivanjermakov1.messenger.repository.ConversationRepository;
import com.gmail.ivanjermakov1.messenger.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchService {


	private final UserRepository userRepository;
	private final ConversationRepository conversationRepository;

	private final UserMapper userMapper;
	private final PreviewMapper previewMapper;

	@Value("${search.result.limit}")
	private Integer searchResultLimit;

	@Autowired
	public SearchService(UserRepository userRepository, UserMapper userMapper, ConversationRepository conversationRepository, PreviewMapper previewMapper) {
		this.userRepository = userRepository;
		this.userMapper = userMapper;
		this.conversationRepository = conversationRepository;
		this.previewMapper = previewMapper;
	}

	public List<PreviewDto> searchConversations(User user, String search, Pageable pageable) throws InvalidEntityException {
		try {
			new Validator<String>()
					.rule(new NotNull<>(), "search query cannot be null")
					.rule(new NotBlank(), "search query cannot be blank")
					.throwInvalid(search);
		} catch (InvalidObjectException e) {
			throw new InvalidEntityException(e.getMessage());
		} catch (Throwable ignored) {
		}

		return conversationRepository.findConversationsBySearchQuery(user.id, search)
				.stream()
				.skip(pageable.getOffset())
				.limit(pageable.getPageSize())
				.map(c -> previewMapper.with(user).map(c))
				.collect(Collectors.toList());
	}

	public List<UserDto> searchUsers(String search, Pageable pageable) throws InvalidSearchFormatException {
		if (search.charAt(0) != '@') throw new InvalidSearchFormatException("user search must starts with \'@\'");

		return userRepository.searchUsers(search, pageable)
				.stream()
				.map(userMapper::map)
				.collect(Collectors.toList());
	}

}
