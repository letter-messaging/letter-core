package com.github.ivanjermakov.lettercore.util;

import org.modelmapper.ModelMapper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Mappers {

	private static final ModelMapper modelMapper;

	static {
		modelMapper = new ModelMapper();
		modelMapper
				.getConfiguration()
				.setFieldMatchingEnabled(true)
				.setSkipNullEnabled(true);
	}

	public static <D, T> D map(T entity, Class<D> outClass) {
		return modelMapper.map(entity, outClass);
	}

	public static <D, T> List<D> mapAll(Collection<T> entities, Class<D> outCLass) {
		return entities
				.stream()
				.map(e -> map(e, outCLass))
				.collect(Collectors.toList());
	}

}