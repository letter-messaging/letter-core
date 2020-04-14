package com.gmail.ivanjermakov1.messenger.controller;

import com.gmail.ivanjermakov1.messenger.dto.UserInfoDto;
import com.gmail.ivanjermakov1.messenger.entity.User;
import com.gmail.ivanjermakov1.messenger.exception.AuthorizationException;

public interface UserInfoController {

	/**
	 * Get user info.
	 *
	 * @param user   authenticated user. automatically maps, when {@literal Auth-Token} parameter present
	 * @param userId user id to get user info of
	 * @return user info
	 */
	UserInfoDto get(User user, Long userId);

	/**
	 * Edit user info.
	 * It is allowed to edit fields with null to remove their value.
	 *
	 * @param user        authenticated user. automatically maps, when {@literal Auth-Token} parameter present
	 * @param userInfoDto user info with edits
	 * @return edited user info
	 * @throws AuthorizationException on attempting to edit other user info
	 */
	UserInfoDto edit(User user, UserInfoDto userInfoDto);

}
