package com.github.ivanjermakov.lettercore.file.mapper;

import com.github.ivanjermakov.lettercore.common.mapper.Mapper;
import com.github.ivanjermakov.lettercore.file.dto.AvatarDto;
import com.github.ivanjermakov.lettercore.file.entity.Avatar;
import com.github.ivanjermakov.lettercore.util.Mappers;
import org.springframework.stereotype.Component;

@Component
public class AvatarMapper implements Mapper<Avatar, AvatarDto> {

	@Override
	public AvatarDto map(Avatar avatar) {
		return Mappers.map(avatar, AvatarDto.class);
	}

}
