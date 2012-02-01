package com.xjany.common.util;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * 工程下文件获得绝对路径：FilePathUtil.getAbsolutePath("./11.txt");
 * WebRoot下文件获得绝对路径：FilePathUtil.getAbsolutePath("/11.txt");
 * ClassPath下文件获得绝对路径：FilePathUtil.getAbsolutePath("11.txt");
 * 
 * 由相对路径获得 绝对路径、File、文件列表，此工具根据 ClassPath寻找路径 注意：本类不要加到jar包中，不然会失效
 * 在Servlet中获得文件真实路径 string
 * file_real_path=getServletContext().getRealPath("/1.swf");
 * 
 * @author xjany_lx
 * @version 1.0
 */
public class FilePathUtil {
	private FilePathUtil() {
	}

	/**
	 * 根据相对路径获得 File
	 * 
	 * @param relativePath
	 *            工程下的以"./"开头，web下的以"/"开头，ClassPath下的直接相对路径
	 * @return File
	 */
	public static File getFileByRelativePath(String relativePath) {
		return new File(getAbsolutePath(relativePath));
	}

	/**
	 * 根据相对路径获得绝对路径
	 * 
	 * @param relativePath
	 *            工程下的以"./"开头，web下的以"/"开头，ClassPath下的直接相对路径
	 * @return 绝对路径字符串
	 */
	public static String getAbsolutePath(String relativePath) {
		String result = null;
		if (null != relativePath) {
			if (relativePath.indexOf("./") == 0) {
				String workspacePath = new File("").getAbsolutePath();
				relativePath = relativePath.substring(2);
				if (relativePath.length() > 0) {
					relativePath = relativePath.replace('/', File.separatorChar);
					result = workspacePath + String.valueOf(File.separatorChar) + relativePath;
				} else {
					result = workspacePath;
				}
			} else if (relativePath.indexOf("/") == 0) {
				String webRootPath = getAbsolutePathOfWebRoot();
				if (relativePath.length() > 0) {
					relativePath = relativePath.replace('/', File.separatorChar);
					result = webRootPath + relativePath;
				} else {
					result = webRootPath;
				}
			} else {
				String classPath = getAbsolutePathOfClassPath();
				if (relativePath.length() > 0) {
					relativePath = relativePath.replace('/', File.separatorChar);
					result = classPath + File.separatorChar + relativePath;
				} else {
					result = classPath;
				}
			}
		}
		return result;
	}

	/**
	 * 得到WebRoot目录的绝对地址
	 * @return
	 */
	private static String getAbsolutePathOfWebRoot() {
		String result = null;
		result = getAbsolutePathOfClassPath();
		result = result.replace(File.separatorChar + "WEB-INF" + File.separatorChar + "classes", "");
		return result;
	}

	/**
	 * 
	 *得到ClassPath的绝对路径
	 * @return
	 */
	private static String getAbsolutePathOfClassPath() {
		String result = null;
		try {
			File file = new File(getURLOfClassPath().toURI());
			result = file.getAbsolutePath();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 得到ClassPath的URL
	 * @return
	 */
	private static URL getURLOfClassPath() {
		return getClassLoader().getResource("");
	}

	/**
	 * 得到类加载器
	 * @return
	 */
	private static ClassLoader getClassLoader() {
		return FilePathUtil.class.getClassLoader();
	}
}
