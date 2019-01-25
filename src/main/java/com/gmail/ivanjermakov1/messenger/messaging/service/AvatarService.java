package com.gmail.ivanjermakov1.messenger.messaging.service;

import com.gmail.ivanjermakov1.messenger.auth.entity.User;
import com.gmail.ivanjermakov1.messenger.exception.AuthenticationException;
import com.gmail.ivanjermakov1.messenger.messaging.dto.AvatarDTO;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Avatar;
import com.gmail.ivanjermakov1.messenger.messaging.repository.AvatarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AvatarService {
	
	private final AvatarRepository avatarRepository;
	
	@Autowired
	public AvatarService(AvatarRepository avatarRepository) {
		this.avatarRepository = avatarRepository;
	}
	
	public Optional<Avatar> getCurrent(User user) {
		return avatarRepository.getByUserId(user.getId())
				.stream()
				.max(Comparator.comparing(Avatar::getUploaded));
	}
	
	public void delete(User user, Long avatarId) throws AuthenticationException {
		if (avatarRepository.getByUserId(user.getId()).stream().noneMatch(a -> a.getId().equals(avatarId)))
			throw new AuthenticationException("invalid avatar id");
		
		avatarRepository.deleteAvatarById(avatarId);
	}
	
	public List<Avatar> getAll(User user) {
		return new ArrayList<>(avatarRepository.getByUserId(user.getId()));
	}
	
}
