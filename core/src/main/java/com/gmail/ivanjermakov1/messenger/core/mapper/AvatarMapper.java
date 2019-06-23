package com.gmail.ivanjermakov1.messenger.core.mapper;

import com.gmail.ivanjermakov1.messenger.core.util.Mappers;
import com.gmail.ivanjermakov1.messenger.messaging.dto.AvatarDto;
import com.gmail.ivanjermakov1.messenger.messaging.entity.Avatar;
import org.springframework.stereotype.Component;

@Component
public class AvatarMapper implements Mapper<Avatar, AvatarDto> {
	
	@Override
	public AvatarDto map(Avatar avatar) {
		return Mappers.map(avatar, AvatarDto.class);
	}
	
}
