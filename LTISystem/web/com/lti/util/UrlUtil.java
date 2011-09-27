package com.lti.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

public class UrlUtil {
	public static String utfEncode(String myString) {
		String ret = null;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(bos);
			dos.writeUTF(myString);
			dos.close();
			byte[] bb = bos.toByteArray();
			StringBuffer buf = new StringBuffer();
			for (int i = 2; i < bb.length; i++) {
				buf.append("%" + Integer.toHexString(0xff & bb[i]).toUpperCase());
			}
			ret = buf.toString();
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return ret;
	}

	public static String utfDecode(String s) {
		String ret = null;
		try {
			int strLen = s.length();
			int byteLen = strLen / 3;
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			bos.write((byteLen >> 8) & 0xff);
			bos.write(byteLen & 0xff);
			for (int i = 1; i < strLen; i += 3) {
				bos.write(Integer.parseInt(s.substring(i, i + 2), 16));
			}
			bos.close();
			DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
			ret = dis.readUTF();
			dis.close();
		} catch (Exception ex) {
			// ex.printStackTrace();
			return s;
		}
		return ret;
	}

	public static String getCompleteURL(HttpServletRequest servletRequest) {
		String url = servletRequest.getRequestURL().toString();
		Iterator<String> iter = servletRequest.getParameterMap().keySet().iterator();
		boolean first = true;
		while (iter.hasNext()) {
			if (first) {
				url += "?";
				first = false;
			} else {
				url += "&";
			}
			String name = iter.next();
			String value = servletRequest.getParameter(name);
			url += name;
			url += "=";
			url += value;
		}
		return url;
	}

	public static String getHtml(String urlString) {
		try {
			StringBuffer html = new StringBuffer();
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			InputStreamReader isr = new InputStreamReader(conn.getInputStream());
			BufferedReader br = new BufferedReader(isr);
			String temp;
			while ((temp = br.readLine()) != null) {
				html.append(temp).append("\n");
			}
			br.close();
			isr.close();
			return html.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void main(String[] args) {
		System.out.println(UrlUtil.getHtml("http://202.116.76.163:8080/LTISystem/ajax_search_google.html"));
	}

}
