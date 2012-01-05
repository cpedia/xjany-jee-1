package com.xjany.common.springmvc;

import org.acegisecurity.providers.encoding.Md5PasswordEncoder;

//加密
public class MyMD5Util {
	
	public static String MD5(String password)
	{
		Md5PasswordEncoder md5 = new Md5PasswordEncoder();
		return md5.encodePassword(password, null);
	}
}
//Md5PasswordEncoder md5 = new Md5PasswordEncoder();
//String dbpassword = user.getPassword();
//curPassword = md5.encodePassword(curPassword, null);