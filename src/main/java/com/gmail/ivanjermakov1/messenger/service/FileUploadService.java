package com.gmail.ivanjermakov1.messenger.service;

import com.gmail.ivanjermakov1.messenger.dto.enums.FileType;
import com.gmail.ivanjermakov1.messenger.exception.InvalidEntityException;
import com.gmail.ivanjermakov1.messenger.security.RandomStringGenerator;
import com.gmail.ivanjermakov1.messenger.util.ImageCompressionMode;
import com.gmail.ivanjermakov1.messenger.util.ImageCompressor;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@Service
public class FileUploadService {

	private final static Logger LOG = LoggerFactory.getLogger(FileUploadService.class);

	@Value("${fileupload.path}")
	private String uploadPlaceholder;

	public String upload(MultipartFile multipartFile, FileType fileType) throws IOException {
		String generatedFilename =
				RandomStringGenerator.generate(10) + "." + FilenameUtils.getExtension(multipartFile.getOriginalFilename());

		LOG.info("upload [" + fileType + "] \'" + multipartFile.getOriginalFilename() + "\'; size: " + multipartFile.getSize() / 1000 + "KB with name: " + generatedFilename);

		new File(uploadPlaceholder + "/" + fileType.toString().toLowerCase()).mkdirs();

		String fullFilePath = uploadPlaceholder + "/" + fileType.toString().toLowerCase() + "/" + generatedFilename;
		File file = new File(fullFilePath);
		if (file.exists()) throw new InvalidEntityException("such file already exists");
		multipartFile.transferTo(Paths.get(fullFilePath));

		uploadCompressedVersions(multipartFile, fullFilePath);

		return fileType.toString().toLowerCase() + "/" + generatedFilename;
	}

	public void uploadCompressedVersions(MultipartFile multipartFile, String filePath) throws IOException {
		String extension = "." + FilenameUtils.getExtension(filePath);
		String fileName = FilenameUtils.getBaseName(filePath);
		String path = FilenameUtils.getFullPath(filePath);
//		so {path}{fileName}.{extension} is {filePath}

		ImageCompressor.compress(filePath, path + fileName + "_" + ImageCompressionMode.MEDIUM.getFilePathMark() + extension, .4f);
		ImageCompressor.compress(filePath, path + fileName + "_" + ImageCompressionMode.SMALL.getFilePathMark() + extension, .1f);
	}

}
