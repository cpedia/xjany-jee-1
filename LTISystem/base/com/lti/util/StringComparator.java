package com.lti.util;

import java.util.Comparator;

class StringComparator implements Comparator {
	public int compare(Object a, Object b) {
		String str1 = (String) a;
		String str2 = (String) b;
		return str1.compareTo(str2);
	}
}