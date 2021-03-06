package com.github.ivanjermakov.lettercore.search.service;

import com.github.ivanjermakov.jtrue.predicate.NotBlank;
import com.github.ivanjermakov.jtrue.predicate.NotNull;
import com.github.ivanjermakov.jtrue.validator.Validator;
import com.github.ivanjermakov.lettercore.common.exception.InvalidEntityException;
import com.github.ivanjermakov.lettercore.conversation.dto.PreviewDto;
import com.github.ivanjermakov.lettercore.conversation.mapper.PreviewMapper;
import com.github.ivanjermakov.lettercore.conversation.repository.ConversationRepository;
import com.github.ivanjermakov.lettercore.search.exception.InvalidSearchFormatException;
import com.github.ivanjermakov.lettercore.user.entity.User;
import com.github.ivanjermakov.lettercore.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchService {


	private final UserRepository userRepository;
	private final ConversationRepository conversationRepository;

	private final PreviewMapper previewMapper;

	@Value("${search.result.limit}")
	private Integer searchResultLimit;

	@Autowired
	public SearchService(UserRepository userRepository, ConversationRepository conversationRepository, PreviewMapper previewMapper) {
		this.userRepository = userRepository;
		this.conversationRepository = conversationRepository;
		this.previewMapper = previewMapper;
	}

	public List<PreviewDto> searchConversations(User user, String search, Pageable pageable) throws InvalidEntityException {
		new Validator<String>()
				.rule(new NotNull<>(), "search query cannot be null")
				.rule(new NotBlank(), "search query cannot be blank")
				.throwInvalid(search, m -> new InvalidEntityException(m));

		return conversationRepository.findConversationsBySearchQuery(user.id, search)
				.stream()
				.skip(pageable.getOffset())
				.limit(pageable.getPageSize())
				.map(c -> previewMapper.with(user).map(c))
				.collect(Collectors.toList());
	}

	public List<User> searchUsers(String search, Pageable pageable) throws InvalidSearchFormatException {
		if (search.charAt(0) != '@') throw new InvalidSearchFormatException("user search must starts with '@'");

		return userRepository.searchUsers(search, PageRequest.of(
				pageable.getPageNumber(),
				Math.min(searchResultLimit, pageable.getPageSize()),
				pageable.getSort()
		));
	}

}
