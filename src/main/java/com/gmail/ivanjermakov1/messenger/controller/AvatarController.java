package com.gmail.ivanjermakov1.messenger.controller;

import com.gmail.ivanjermakov1.messenger.dto.AvatarDto;
import com.gmail.ivanjermakov1.messenger.entity.User;
import com.gmail.ivanjermakov1.messenger.exception.InvalidEntityException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AvatarController {

	/**
	 * Upload avatar.
	 *
	 * @param user   authenticated user. automatically maps, when {@literal Auth-Token} parameter present
	 * @param avatar multipart file to be uploaded
	 * @return uploaded avatar
	 * @throws IOException            on server file system error
	 * @throws InvalidEntityException on upload of invalid file (mostly caused by invalid file extension or file size
	 *                                specified in @value {@code spring.servlet.multipart.max-file-size})
	 */
	AvatarDto upload(User user, MultipartFile avatar) throws IOException;

	/**
	 * Delete avatar from current avatar and avatar list
	 *
	 * @param user     authenticated user. automatically maps, when {@literal Auth-Token} parameter present
	 * @param avatarId avatar id to delete
	 */
	void delete(User user, Long avatarId);

}
