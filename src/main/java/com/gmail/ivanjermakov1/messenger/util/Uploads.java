package com.gmail.ivanjermakov1.messenger.util;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

public class Uploads {

	public static boolean isSupportedImage(MultipartFile file) {
		if (file == null || file.getOriginalFilename() == null) return false;

		return Arrays.stream(ImageFileType.values())
				.anyMatch(e -> e.toString().toLowerCase().equals(FilenameUtils.getExtension(file.getOriginalFilename()).toLowerCase()));
	}

}
