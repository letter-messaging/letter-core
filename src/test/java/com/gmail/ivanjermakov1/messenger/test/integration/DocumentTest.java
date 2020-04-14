package com.gmail.ivanjermakov1.messenger.test.integration;

import com.gmail.ivanjermakov1.messenger.controller.ConversationController;
import com.gmail.ivanjermakov1.messenger.controller.DocumentController;
import com.gmail.ivanjermakov1.messenger.controller.MessagingController;
import com.gmail.ivanjermakov1.messenger.dto.ConversationDto;
import com.gmail.ivanjermakov1.messenger.dto.MessageDto;
import com.gmail.ivanjermakov1.messenger.dto.NewDocumentDto;
import com.gmail.ivanjermakov1.messenger.dto.NewMessageDto;
import com.gmail.ivanjermakov1.messenger.dto.TestingUser;
import com.gmail.ivanjermakov1.messenger.entity.Message;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.exception.AuthorizationException;
import com.gmail.ivanjermakov1.messenger.exception.NoSuchEntityException;
import com.gmail.ivanjermakov1.messenger.exception.RegistrationException;
import com.gmail.ivanjermakov1.messenger.service.MessageService;
import com.gmail.ivanjermakov1.messenger.service.TestingService;
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
