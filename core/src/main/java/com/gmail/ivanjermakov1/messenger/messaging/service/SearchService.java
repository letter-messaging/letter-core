package com.gmail.ivanjermakov1.messenger.messaging.service;

import com.gmail.ivanjermakov1.messenger.auth.dto.UserDto;
import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.auth.repository.UserRepository;
import com.gmail.ivanjermakov1.messenger.auth.service.UserService;
import com.gmail.ivanjermakov1.messenger.exception.InvalidSearchFormatException;
import com.gmail.ivanjermakov1.messenger.messaging.dto.PreviewDto;
import com.gmail.ivanjermakov1.messenger.messaging.repository.UserInfoRepository;
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
	private final UserService userService;
	
	@Value("${search.result.limit}")
	private Integer searchResultLimit;
	
	@Autowired
	public SearchService(PreviewService previewService, UserInfoRepository userInfoRepository, UserRepository userRepository, UserInfoService userInfoService, UserService userService) {
		this.previewService = previewService;
		this.userRepository = userRepository;
		this.userService = userService;
	}
	
	public List<PreviewDto> searchConversations(User user, String search, Pageable pageable) {
		return previewService.all(user, PageRequest.of(0, Integer.MAX_VALUE))
				.stream()
				.filter(p -> Strings.startsWith(search, p.getWith().getFirstName()) ||
						Strings.startsWith(search, p.getWith().getLastName()) ||
						Strings.startsWith(search, p.getWith().getFirstName() + " " +
								p.getWith().getLastName()) ||
						Strings.startsWith(search, p.getWith().getLastName() + " " +
								p.getWith().getFirstName()))
				.collect(Collectors.toList());
	}
	
	public List<UserDto> searchUsers(String search, Pageable pageable) throws InvalidSearchFormatException {
		if (search.charAt(0) != '@') throw new InvalidSearchFormatException("user search must starts with \'@\'");
		
		return userRepository.searchUsers(search, pageable)
				.stream()
				.map(userService::full)
				.collect(Collectors.toList());
	}
	
}
