package com.gmail.ivanjermakov1.messender.markdown.html;

import com.gmail.ivanjermakov1.messender.markdown.Markdown;

/**
 * More specific markdown formatter for generating HTML rich text documents
 */
public interface MarkdownHtml extends Markdown {
	
	/**
	 * HTML formatter besides rich text output must return styles, required for correct rich text displaying.
	 *
	 * @return text representation of CSS styles for current formatter implementation
	 */
	String style();
	
}
