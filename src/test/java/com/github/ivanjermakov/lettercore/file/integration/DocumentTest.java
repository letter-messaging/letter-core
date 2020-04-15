package com.github.ivanjermakov.lettercore.file.integration;

import com.github.ivanjermakov.lettercore.auth.exception.AuthenticationException;
import com.github.ivanjermakov.lettercore.auth.exception.AuthorizationException;
import com.github.ivanjermakov.lettercore.auth.exception.RegistrationException;
import com.github.ivanjermakov.lettercore.common.dto.TestingUser;
import com.github.ivanjermakov.lettercore.common.exception.NoSuchEntityException;
import com.github.ivanjermakov.lettercore.common.service.TestingService;
import com.github.ivanjermakov.lettercore.conversation.controller.ConversationController;
import com.github.ivanjermakov.lettercore.conversation.dto.ConversationDto;
import com.github.ivanjermakov.lettercore.file.controller.DocumentController;
import com.github.ivanjermakov.lettercore.file.dto.NewDocumentDto;
import com.github.ivanjermakov.lettercore.messaging.controller.MessagingController;
import com.github.ivanjermakov.lettercore.messaging.dto.MessageDto;
import com.github.ivanjermakov.lettercore.messaging.dto.NewMessageDto;
import com.github.ivanjermakov.lettercore.messaging.entity.Message;
import com.github.ivanjermakov.lettercore.messaging.service.MessageService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class DocumentTest {

	@Autowired
	private DocumentController documentController;

	@Autowired
	private MessagingController messagingController;

	@Autowired
	private ConversationController conversationController;

	@Autowired
	private MessageService messageService;

	@Autowired
	private TestingService testingService;

	@Test
	public void shouldSendMessageWithDocumentAndDeleteDocument() throws RegistrationException, AuthenticationException, NoSuchEntityException, IOException, AuthorizationException {
		TestingUser user1 = testingService.registerUser("Jack");
		TestingUser user2 = testingService.registerUser("Ron");

		ConversationDto conversationDto = conversationController.create(
				user1.user,
				user2.user.login
		);

		NewDocumentDto document = documentController.upload(
				user1.user,
				testingService.mockTestImage()
		);

		NewMessageDto newMessage = new NewMessageDto(
				user1.user.id,
				conversationDto.id,
				"Hello!",
				new ArrayList<>(),
				new ArrayList<>(),
				new ArrayList<>(Collections.singletonList(document))
		);

		MessageDto messageDto = messagingController.sendMessage(user1.user, newMessage);

		Assert.assertNotNull(messageDto);
		Assert.assertEquals(1, messageDto.documents.size());

		documentController.delete(
				user1.user,
				messageDto.documents
						.stream()
						.findFirst()
						.orElseThrow(NoSuchEntityException::new)
						.id
		);

		Message messageWithoutImage = messageService.get(messageDto.id);

		Assert.assertNotNull(messageWithoutImage);
		Assert.assertTrue(messageWithoutImage.images.isEmpty());
	}

}
