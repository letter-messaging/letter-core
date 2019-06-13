package com.gmail.ivanjermakov1.messenger.markdown.service;

import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.stereotype.Service;

@Service
public class MarkdownService {
	
	private Parser parser = Parser.builder().build();
	private HtmlRenderer renderer = HtmlRenderer.builder().build();
	
	public String format(String plain) {
		return renderer.render(parser.parse(plain)).trim();
	}
	
}
