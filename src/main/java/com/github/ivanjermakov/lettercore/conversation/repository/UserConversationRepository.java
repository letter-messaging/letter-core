package com.github.ivanjermakov.lettercore.conversation.repository;

import com.github.ivanjermakov.lettercore.conversation.entity.Conversation;
import com.github.ivanjermakov.lettercore.conversation.entity.UserConversation;
import com.github.ivanjermakov.lettercore.user.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserConversationRepository extends CrudRepository<UserConversation, Long> {

	Optional<UserConversation> findByUserAndConversation(User user, Conversation conversation);

}
