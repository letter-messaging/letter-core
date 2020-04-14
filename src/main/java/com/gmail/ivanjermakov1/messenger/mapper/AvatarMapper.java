package com.gmail.ivanjermakov1.messenger.mapper;

import com.gmail.ivanjermakov1.messenger.dto.AvatarDto;
import com.gmail.ivanjermakov1.messenger.entity.Avatar;
import com.gmail.ivanjermakov1.messenger.util.Mappers;
import org.springframework.stereotype.Component;

@Component
public class AvatarMapper implements Mapper<Avatar, AvatarDto> {

	@Override
	public AvatarDto map(Avatar avatar) {
		return Mappers.map(avatar, AvatarDto.class);
	}

}
