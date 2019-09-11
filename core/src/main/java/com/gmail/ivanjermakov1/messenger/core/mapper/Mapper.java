package com.gmail.ivanjermakov1.messenger.core.mapper;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public interface Mapper<T, D> {

	D map(T t);

	default List<D> mapAll(List<T> list) {
		return list
				.stream()
				.map(this::map)
				.collect(Collectors.toList());
	}

}
