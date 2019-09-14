package com.gmail.ivanjermakov1.messenger.service;

import com.gmail.ivanjermakov1.messenger.dto.PreviewDto;
import com.gmail.ivanjermakov1.messenger.dto.UserDto;
import com.gmail.ivanjermakov1.messenger.entity.User;
import com.gmail.ivanjermakov1.messenger.exception.InvalidSearchFormatException;
import com.gmail.ivanjermakov1.messenger.mapper.UserMapper;
import com.gmail.ivanjermakov1.messenger.repository.UserRepository;
import com.gmail.ivanjermakov1.messenger.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

//	TODO: use non strict search engine
@Service
public class SearchService {

	private final PreviewService previewService;
	private final UserRepository userRepository;

	private final UserMapper userMapper;

	@Value("${search.result.limit}")
	private Integer searchResultLimit;

	@Autowired
	public SearchService(PreviewService previewService, UserRepository userRepository, UserMapper userMapper) {
		this.previewService = previewService;
		this.userRepository = userRepository;
		this.userMapper = userMapper;
	}

	//	TODO: optimize
	public List<PreviewDto> searchConversations(User user, String search, Pageable pageable) {
		return previewService.all(user, PageRequest.of(0, Integer.MAX_VALUE))
				.stream()
				.filter(p -> Strings.startsWith(search, p.with.firstName) ||
						Strings.startsWith(search, p.with.lastName) ||
						Strings.startsWith(search, p.with.firstName + " " +
								p.with.lastName) ||
						Strings.startsWith(search, p.with.lastName + " " +
								p.with.firstName))
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
