package com.github.ivanjermakov.lettercore.repository;

import com.github.ivanjermakov.lettercore.entity.Conversation;
import com.github.ivanjermakov.lettercore.entity.User;
import com.github.ivanjermakov.lettercore.entity.UserConversation;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserConversationRepository extends CrudRepository<UserConversation, Long> {

	Optional<UserConversation> findByUserAndConversation(User user, Conversation conversation);

}
