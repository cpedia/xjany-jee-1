package com.xjany.common;

import javax.servlet.http.HttpServletRequest;

public interface MD5Util {
	public String encryption(String password, String username);
	public String decryption(String password);
	public String randFour();
	public String getIp(HttpServletRequest request);
}
