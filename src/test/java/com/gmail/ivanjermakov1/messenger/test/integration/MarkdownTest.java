package com.gmail.ivanjermakov1.messenger.test.integration;

import com.gmail.ivanjermakov1.messenger.service.MarkdownService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class MarkdownTest {

	@Autowired
	private MarkdownService markdownService;

	@Test
	public void shouldParseBold() {
		String html = markdownService.format("This is *Sparta*");

		Assert.assertEquals("<p>This is <em>Sparta</em></p>", html);
	}

}
