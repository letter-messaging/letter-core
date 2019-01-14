package com.gmail.ivanjermakov1.messender.markdown;

import com.gmail.ivanjermakov1.messender.markdown.exception.FormatException;

/**
 * General interface for markdown formatters. Formatters take plain text as input and generate rich text following some
 * rules of formatting.
 */
public interface Markdown {
	
	/**
	 * Apply formatting for plain text using some rules.
	 *
	 * @param text plain text for formatting
	 * @return rich text, result of formatting
	 * @throws FormatException when text doesn't follow syntax rules
	 */
	String format(String text) throws FormatException;
	
}
