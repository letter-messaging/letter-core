package com.gmail.ivanjermakov1.messenger.util;

public enum ImageCompressionMode {

	FULL("f"),
	MEDIUM("m"),
	SMALL("s");

	private String filePathMark;

	ImageCompressionMode(String filePathMark) {
		this.filePathMark = filePathMark;
	}

	public String getFilePathMark() {
		return filePathMark;
	}

}
