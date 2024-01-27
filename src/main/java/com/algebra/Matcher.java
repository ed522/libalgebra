package com.algebra;

public class Matcher {
	
	public static boolean matches(char input, String criteria) {

		for (char c : criteria.toCharArray()) if (input == c) return true;
		return false;

	}

}
