package com.lti.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;

public class SystemEnvironment {
	public static Properties properties = null;

	public static Properties getSysEnv() {
		Properties prop = new Properties();
		try {
			Process p = null;
			String OS = System.getProperty("os.name").toLowerCase();
			if (OS.indexOf("windows") > -1) {
				p = Runtime.getRuntime().exec("cmd /c set"); // 其它的操作系统可以自行处理，
			} else if (OS.indexOf("linux") > -1) {
				p = Runtime.getRuntime().exec("/bin/sh -c set");
			} else if (OS.indexOf("sunos") > -1) {
				p = Runtime.getRuntime().exec("/bin/sh -c set");
			} else {
				System.out.println("### Unknown Operating System : Unknown Operating System! os.name=" + OS);
				return prop;
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while ((line = br.readLine()) != null) {
				int i = line.indexOf("=");
				if (i > -1) {
					String key = line.substring(0, i);
					String value = line.substring(i + 1);
					prop.setProperty(key, value);
				}
			}
		} catch (Exception ex) {
			System.out.println("Exception occurred! Message " + ex.getMessage());
		}
		return prop;
	}

	public static String getSysEnv(String key) {
		if (properties == null)
			properties = getSysEnv();
		return properties.getProperty(key);
	}
}
