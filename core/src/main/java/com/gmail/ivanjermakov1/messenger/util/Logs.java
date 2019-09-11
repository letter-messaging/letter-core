package com.gmail.ivanjermakov1.messenger.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Logs {

	public static String collectionSizeList(Collection... collections) {
		List<Collection> c = Arrays.asList(collections);
		return c
				.stream()
				.map(collection -> String.valueOf(collection.size()))
				.collect(Collectors.toList()).toString();
	}

}
