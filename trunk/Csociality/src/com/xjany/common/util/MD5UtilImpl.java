package com.xjany.common.util;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import org.acegisecurity.providers.encoding.Md5PasswordEncoder;

import com.xjany.common.MD5Util;

//加密
public class MD5UtilImpl implements MD5Util{
	
	private final Md5PasswordEncoder md5 = new Md5PasswordEncoder();

	public String encryption(String password, String username) {
		byte[] pswByteTemp = new byte[5];
		byte[] usnByteTemp = new byte[5];
		String sTemp = "";
//		String randTemp = "";
//		String passwordTemp = "";
//		for(int i = 0; i < 6; i++)
//		{
//			if(i/2 != 0)
//				randTemp += String.valueOf((char)(Math.random() * 25 + 97));
//			else
//				randTemp += String.valueOf(Math.round(Math.random() * 9));
//		}

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
		String psw = md5.encodePassword(sTemp, username);
		return psw;
//		passwordTemp = psw.substring(0, 11) + randTemp + psw.substring(17, psw.length());
//		return passwordTemp;
	}

	public String decryption(String password) {
		return password.substring(0, 11) + password.substring(17, password.length());
	}
	public String randFour()
	{
		String randTemp = "";
		for(int i = 0; i < 4; i++)
		{
			if(i/2 != 0)
				randTemp += String.valueOf((char)(Math.random() * 25 + 97));
			else
				randTemp += String.valueOf(Math.round(Math.random() * 9));
		}
		return randTemp;
	}
	public String getIp(HttpServletRequest request) {  
        String ip = request.getHeader("x-forwarded-for");  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("WL-Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getRemoteAddr();  
        }  
        return ip;  
    }  
//	public static void main(String[] args)
//	{
//		MyMD5UtilImpl md5 = new MyMD5UtilImpl();
//		System.out.println(md5.encryption("abcdebd","ddddb"));
//	}
}
// Md5PasswordEncoder md5 = new Md5PasswordEncoder();
// String dbpassword = user.getPassword();
// curPassword = md5.encodePassword(curPassword, null);