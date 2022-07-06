package com.iiplabs.nns.sms.utils;

public final class StringUtil {

	public static String getLastField(String path) {
		String[] a = path.split("\\.");
		return a[a.length - 1];
	}

	private StringUtil() {
		throw new AssertionError();
	}

}
