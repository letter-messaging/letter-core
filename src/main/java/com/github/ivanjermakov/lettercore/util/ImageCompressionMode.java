package com.github.ivanjermakov.lettercore.util;

public enum ImageCompressionMode {

	FULL("f"),
	MEDIUM("m"),
	SMALL("s");

	private final String filePathMark;

	ImageCompressionMode(String filePathMark) {
		this.filePathMark = filePathMark;
	}

	public String getFilePathMark() {
		return filePathMark;
	}

}
