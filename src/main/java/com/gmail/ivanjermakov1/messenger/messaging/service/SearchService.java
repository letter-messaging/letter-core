package com.gmail.ivanjermakov1.messenger.messaging.service;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Preview;
import com.gmail.ivanjermakov1.messenger.messaging.repository.UserConversationRepository;
import com.gmail.ivanjermakov1.messenger.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchService {
	
	private final PreviewService previewService;
	
	@Autowired
	public SearchService(UserConversationRepository userConversationRepository, PreviewService previewService) {
		this.previewService = previewService;
	}
	
	public List<Preview> searchConversations(User user, String search) {
		return previewService.all(user).stream()
				.filter(p -> Strings.startsWith(search, p.getFirstName()) ||
						Strings.startsWith(search, p.getLastName()) ||
						Strings.startsWith(search, p.getFirstName() + " " + p.getLastName()) ||
						Strings.startsWith(search, p.getLastName() + " " + p.getFirstName()))
				.collect(Collectors.toList());
	}
	
}
