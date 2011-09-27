package com.lti.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;


public class ZipUtil {
	public static List<File> unZip(File zip,String root) throws Exception{
		if(!zip.exists())return null;
		ZipFile zipFile = new ZipFile(zip);
		Enumeration emu = zipFile.entries();
		List<File> files=new ArrayList<File>();
		while (emu.hasMoreElements()) {
			ZipEntry entry = (ZipEntry) emu.nextElement();
			File file = new File(root, entry.getName());
			if(file.exists())continue;
			else file.createNewFile();
			BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry));
			FileOutputStream fos = new FileOutputStream(file);
			BufferedOutputStream bos = new BufferedOutputStream(fos, 2048);
			
			int count;
			byte data[] = new byte[2048];
			while ((count = bis.read(data, 0, 2048)) != -1) {
				bos.write(data, 0, count);
			}
			bos.flush();
			bos.close();
			bis.close();
			files.add(file);
		}
		zipFile.close();
		return files;
	}
	public static void zip(List<File> files,String target)throws Exception{
		BufferedInputStream origin = null;
		FileOutputStream dest = new FileOutputStream(target);
		ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
		byte data[] = new byte[2048];
		for (int i = 0; i < files.size(); i++) {
			File item=files.get(i);
			if(!item.exists())continue;
			FileInputStream fi = new FileInputStream(files.get(i));
			origin = new BufferedInputStream(fi, 2048);
			ZipEntry entry = new ZipEntry(item.getName());
			out.putNextEntry(entry);
			int count;
			while ((count = origin.read(data, 0, 2048)) != -1) {
				out.write(data, 0, count);
			}
			origin.close();
		}
		out.close();
	}
	
	public static boolean createDir(File file){
		String parent=file.getAbsolutePath().substring(0,file.getAbsolutePath().replace("\\", "/").lastIndexOf("/"));
		try{
			File p=new File(parent);
			return p.mkdirs();
			
		}catch(Exception e){
		}
		return false;
	}
	
	
}
