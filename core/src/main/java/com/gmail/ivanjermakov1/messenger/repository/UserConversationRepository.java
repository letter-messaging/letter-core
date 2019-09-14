package com.gmail.ivanjermakov1.messenger.repository;

import com.gmail.ivanjermakov1.messenger.entity.Conversation;
import com.gmail.ivanjermakov1.messenger.entity.User;
import com.gmail.ivanjermakov1.messenger.entity.UserConversation;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserConversationRepository extends CrudRepository<UserConversation, Long> {

	Optional<UserConversation> findByUserAndConversation(User user, Conversation conversation);

}
