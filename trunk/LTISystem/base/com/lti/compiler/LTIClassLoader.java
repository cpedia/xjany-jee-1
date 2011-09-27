package com.lti.compiler;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;

import com.lti.executor.Compiler;
import com.lti.util.StringUtil;

public class LTIClassLoader extends ClassLoader {

	private byte[] getBytes(String filename) throws IOException {

		File file = new File(filename);

		long len = file.length();

		byte raw[] = new byte[(int) len];

		FileInputStream fin = new FileInputStream(file);

		int r = fin.read(raw);

		if (r != len)

			throw new IOException("Can't read all, " + r + " != " + len);

		fin.close();

		return raw;

	}

	@SuppressWarnings("unchecked")
	public Class loadUserClassFromJAR(String fileName, String jarURL) {
		Class clas = null;
		try {
			URL jfile = new URL("jar", "", jarURL +"!/");
			URLClassLoader cl = URLClassLoader.newInstance(new URL[] { jfile });
			clas = cl.loadClass(fileName);
		} catch (Exception ie) {
			System.out.println("load" + "fileName" + "failed");
		}
		return clas;
	}
	
	@SuppressWarnings("unused")
	private boolean compile(String javaFile) throws IOException {

		System.out.println("CCL: Compiling " + javaFile + "...");

		Process p = Runtime.getRuntime().exec("javac " + javaFile);

		try {

			p.waitFor();

		} catch (InterruptedException ie) {
			System.out.println(ie);
		}

		int ret = p.exitValue();

		return ret == 0;

	}

	@SuppressWarnings("unchecked")
	public Class loadUserClass(String classFileAbsoluteNamePath, String classAbsoluteName, boolean resolve) throws ClassNotFoundException {

		Class clas = null;

		try {
			clas = this.findLoadedClass(classAbsoluteName);
			if (clas == null) {
				byte raw[] = getBytes(classFileAbsoluteNamePath);
				clas = defineClass(classAbsoluteName, raw, 0, raw.length);
			}
		} catch (Exception ie) {
			//System.out.println("class: " + classAbsoluteName + "\n" + StringUtil.getStackTraceString(ie));
		}

		if (clas == null) {

			clas = Class.forName(classAbsoluteName);
		}

		if (clas == null) {

			clas = findSystemClass(classAbsoluteName);

		}
		if (resolve && clas != null) {
			resolveClass(clas);
		}

		if (clas == null){
			throw new ClassNotFoundException(classAbsoluteName);
		}

		return clas;

	}

	@SuppressWarnings("unchecked")
	public Class loadClass(String name, boolean resolve) throws ClassNotFoundException {

		Class clas = null;

		clas = findLoadedClass(name);
		
		if(name.contains("com.lti.compiledstrategy")&&!name.contains("BaseStrategy")&&clas==null){
			String[] strs=name.split("\\.");
			try {
				clas=this.loadUserClass(StringUtil.getPath(new String[]{Compiler.absoluteclasspath,strs[strs.length-1]+".class"}), name, false);
			} catch (Throwable e) {
				System.out.println("class: " + name + "\n" + StringUtil.getStackTraceString(e));
			}
		}

		if (clas == null) {

			clas = Class.forName(name);
		}

		if (clas == null) {

			clas = findSystemClass(name);

		}
		
		if (resolve && clas != null) {
			resolveClass(clas);
		}

		if (clas == null){
			throw new ClassNotFoundException(name);
		}

		return clas;

	}

}
