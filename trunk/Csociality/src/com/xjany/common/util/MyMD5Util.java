package com.xjany.common.util;

import java.io.UnsupportedEncodingException;

import org.acegisecurity.providers.encoding.Md5PasswordEncoder;

//加密
public class MyMD5Util {

	public static String MD5(String password, String username) {
		Md5PasswordEncoder md5 = new Md5PasswordEncoder();
		byte[] pswByteTemp = new byte[5];
		byte[] usnByteTemp = new byte[5];
		String sTemp = "";

		password += "jany";
		username += username.substring(1, 3) + "x";
		try {
			pswByteTemp = password.getBytes("gbk");
			usnByteTemp = username.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < pswByteTemp.length; i++) {
			sTemp += pswByteTemp[i] + username;
		}
		for (int i = 0; i < usnByteTemp.length; i++) {
			sTemp += usnByteTemp[i] + password;
		}
		return md5.encodePassword(sTemp, password);
	}
	public static void main(String[] args)
	{
		System.out.println(MD5("aaa","abc"));
	}
}
// Md5PasswordEncoder md5 = new Md5PasswordEncoder();
// String dbpassword = user.getPassword();
// curPassword = md5.encodePassword(curPassword, null);