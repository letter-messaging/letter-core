package com.gmail.ivanjermakov1.messenger.messaging.repository;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Conversation;
import com.gmail.ivanjermakov1.messenger.messaging.entity.UserConversation;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserConversationRepository extends CrudRepository<UserConversation, Long> {
	
	Optional<UserConversation> findByUserAndConversation(User user, Conversation conversation);
	
}
