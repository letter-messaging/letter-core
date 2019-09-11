package com.gmail.ivanjermakov1.messenger.core.util;

import java.util.Collection;

public class Objects {

	public static Boolean isNullOrEmpty(Collection c) {
		return c == null || c.size() == 0;
	}

}
