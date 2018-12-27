package com.gmail.ivanjermakov1.messenger.messaging.service;

import com.gmail.ivanjermakov1.messenger.auth.dto.UserDTO;
import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.auth.repository.UserRepository;
import com.gmail.ivanjermakov1.messenger.messaging.dto.PreviewDTO;
import com.gmail.ivanjermakov1.messenger.messaging.repository.UserMainInfoRepository;
import com.gmail.ivanjermakov1.messenger.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SearchService {
	
	private final PreviewService previewService;
	private final UserRepository userRepository;
	private final UserMainInfoService userMainInfoService;
	
	@Autowired
	public SearchService(PreviewService previewService, UserMainInfoRepository userMainInfoRepository, UserRepository userRepository, UserMainInfoService userMainInfoService) {
		this.previewService = previewService;
		this.userRepository = userRepository;
		this.userMainInfoService = userMainInfoService;
	}
	
	//	TODO: use non strict search engine
	public List<PreviewDTO> searchConversations(User user, String search) {
		return previewService.all(user)
				.stream()
				.filter(p -> Strings.startsWith(search, p.getWith().getUserMainInfo().getFirstName()) ||
						Strings.startsWith(search, p.getWith().getUserMainInfo().getLastName()) ||
						Strings.startsWith(search, p.getWith().getUserMainInfo().getFirstName() + " " +
								p.getWith().getUserMainInfo().getLastName()) ||
						Strings.startsWith(search, p.getWith().getUserMainInfo().getLastName() + " " +
								p.getWith().getUserMainInfo().getFirstName()))
				.collect(Collectors.toList());
	}
	
	public List<UserDTO> searchUsers(String search) {
		return userRepository.searchUsersAmount(search, 20)
				.stream()
				.map(u -> new UserDTO(u, userMainInfoService.getById(u.getId())))
				.collect(Collectors.toList());
	}
	
}
