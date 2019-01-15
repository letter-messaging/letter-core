package com.gmail.ivanjermakov1.messenger.messaging.service;

import com.gmail.ivanjermakov1.messenger.auth.dto.UserDTO;
import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.auth.repository.UserRepository;
import com.gmail.ivanjermakov1.messenger.auth.service.UserService;
import com.gmail.ivanjermakov1.messenger.exception.InvalidSearchFormatException;
import com.gmail.ivanjermakov1.messenger.messaging.dto.PreviewDTO;
import com.gmail.ivanjermakov1.messenger.messaging.repository.UserInfoRepository;
import com.gmail.ivanjermakov1.messenger.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchService {
	
	private final PreviewService previewService;
	private final UserRepository userRepository;
	private final UserService userService;
	
	@Autowired
	public SearchService(PreviewService previewService, UserInfoRepository userInfoRepository, UserRepository userRepository, UserInfoService userInfoService, UserService userService) {
		this.previewService = previewService;
		this.userRepository = userRepository;
		this.userService = userService;
	}
	
	//	TODO: use non strict search engine
	public List<PreviewDTO> searchConversations(User user, String search) {
		return previewService.all(user)
				.stream()
				.filter(p -> Strings.startsWith(search, p.getWith().getFirstName()) ||
						Strings.startsWith(search, p.getWith().getLastName()) ||
						Strings.startsWith(search, p.getWith().getFirstName() + " " +
								p.getWith().getLastName()) ||
						Strings.startsWith(search, p.getWith().getLastName() + " " +
								p.getWith().getFirstName()))
				.collect(Collectors.toList());
	}
	
	// TODO: make amount of responses configurable
	public List<UserDTO> searchUsers(String search) throws InvalidSearchFormatException {
		if (search.charAt(0) != '@') throw new InvalidSearchFormatException("user search must starts with \'@\'");
		
		return userRepository.searchUsersAmount(search, 20)
				.stream()
				.map(userService::full)
				.collect(Collectors.toList());
	}
	
}
