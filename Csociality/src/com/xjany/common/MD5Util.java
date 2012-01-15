package com.xjany.common;

public interface MD5Util {
	public String encryption(String password, String username);
	public String decryption(String password);
	public String randFour();
}
