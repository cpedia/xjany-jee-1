package com.lti.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;


public class ZipObject {
	
	public static void main(String[] args)throws Exception{
		String content=FileOperator.getContent("C:/Program Files/Apache Software Foundation/Tomcat 6.0/test.csv");
		System.out.println(content.getBytes().length);
		System.out.println(ObjectToZipBytes(content).length);
	}
	
	public static byte[] ZipString(String str) throws Exception {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(bos));
		ZipEntry entry = new ZipEntry("ZipStringByte");
		zos.putNextEntry(entry);
		zos.write(str.getBytes());
		zos.close();
		bos.close();
		return bos.toByteArray();
	}
	
	public static byte[] ObjectToZipBytes(Object object) throws Exception {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(bos));
		ZipEntry entry = new ZipEntry("ZipObjectByte");
		zos.putNextEntry(entry);
		ObjectOutputStream oos = new ObjectOutputStream(zos);
		oos.writeObject(object);
		oos.close();
		zos.close();
		bos.close();
		return bos.toByteArray();
	}
	public static byte[] ObjectToBytes(Object object) throws Exception {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(object);
		oos.close();
		bos.close();
		return bos.toByteArray();
	}
	public static Object ZipBytesToObject(byte[] bytes) throws Exception {
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		ZipInputStream in = new ZipInputStream(bis);
		in.getNextEntry();
		ObjectInputStream ois = new ObjectInputStream(in);
		Object object = ois.readObject();
		ois.close();
		in.close();
		bis.close();
		return object;
	}
}
