package com.gmail.ivanjermakov1.messenger.controller;

import com.gmail.ivanjermakov1.messenger.dto.AvatarDto;
import com.gmail.ivanjermakov1.messenger.entity.User;
import com.gmail.ivanjermakov1.messenger.exception.InvalidEntityException;
import com.gmail.ivanjermakov1.messenger.service.AvatarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("avatar")
@Transactional
public class AvatarController {

	private final AvatarService avatarService;

	@Autowired
	public AvatarController(AvatarService avatarService) {
		this.avatarService = avatarService;
	}

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
	@PostMapping("upload")
	public AvatarDto upload(@ModelAttribute User user,
	                        @RequestParam("avatar") MultipartFile avatar) throws IOException, InvalidEntityException {
		return avatarService.upload(user, avatar);
	}

	/**
	 * Delete avatar from current avatar and avatar list
	 *
	 * @param user     authenticated user. automatically maps, when {@literal Auth-Token} parameter present
	 * @param avatarId avatar id to delete
	 */
	@GetMapping("delete")
	public void delete(@ModelAttribute User user,
	                   @RequestParam("avatarId") Long avatarId) {
		avatarService.delete(user, avatarId);
	}

}
