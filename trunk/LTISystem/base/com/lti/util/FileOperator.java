package com.lti.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;

public class FileOperator {

	public static synchronized void appendMethodA(String fileName, String content) {
		try {
			File file = new File(fileName);
			if (!file.exists())
				file.createNewFile();
			
			RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
			long fileLength = randomFile.length();
			randomFile.seek(fileLength);
			randomFile.writeBytes(content);
			randomFile.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public static String getContent(String path)throws Exception{
		File f=new File(path);
		if(!f.exists()){
			return "";
		}
		BufferedReader br = null;
		StringBuffer sb=new StringBuffer();
		try{
			br = new BufferedReader(new FileReader(f));
			String line=br.readLine();
			while(line!=null){
				sb.append(line);
				sb.append("\r\n");
				line=br.readLine();
			}
			br.close();
		}catch(Exception e){
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		finally{
			if(br!=null)
				br.close();
		}
		return sb.toString();
	}
	
	public static synchronized void appendMethodB(String fileName, String content) {
		appendMethodB(fileName, content, true);
	}
	public static synchronized void appendMethodB(String fileName, String content,boolean flag) {
		try {
			File file = new File(fileName);
			if (!file.exists())
				file.createNewFile();

			FileWriter writer = new FileWriter(fileName, flag);
			writer.write(content);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void writeToFile(Exception ex, String strLogFileName) {

		try {
			File file = new File(strLogFileName);
			if (!file.exists())
				file.createNewFile();

			FileWriter theFile = new FileWriter(strLogFileName, true);
			PrintWriter out = new PrintWriter(theFile);
			try {
				if (!(ex == null)) {
					ex.printStackTrace(out);
					out.println();
				}
				out.close();
				theFile.close();
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(e.getMessage());
			} finally {
				if (out != null) {
					out.close();
				}
				if (theFile != null) {
					theFile.close();
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean copy(String from, String to) {
		try {
			to = replace(to, "\\", "/");
			String toPath = to.substring(0, to.lastIndexOf("/"));
			File f = new File(toPath);
			if (!f.exists())
				f.mkdirs();

			BufferedInputStream bin = new BufferedInputStream(new FileInputStream(from));
			BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(to));
			int c;
			//this is low efficiency
			while ((c = bin.read()) != -1) {
				bout.write(c);
			}
			bin.close();
			bout.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean exists(String fileName) {
		try {
			new FileReader(new File(fileName));
			return true;
		} catch (FileNotFoundException e) {
			return false;
		}
	}

	public static String replace(String srcStr, String oldStr, String newStr) {
		int i = srcStr.indexOf(oldStr);
		StringBuffer sb = new StringBuffer();
		if (i == -1)
			return srcStr;
		sb.append(srcStr.substring(0, i) + newStr);
		if (i + oldStr.length() < srcStr.length()) 
			sb.append(replace(srcStr.substring(i + oldStr.length(), srcStr.length()), oldStr, newStr));
		return sb.toString();
	}

	public static boolean copys(String from, String to) {
		from = replace(from, "\\", "/");
		to = replace(to, "\\", "/");

		if (!from.endsWith("/"))
			from = from + "/";
		if (!to.endsWith("/"))
			to = to + "/";

		File tt = new File(to);
		if (!tt.exists())
			tt.mkdirs();

		File ff = new File(from);
		if (ff.isDirectory()) {
			File f[] = ff.listFiles();
			for (int i = 0; i < f.length; i++) {
				String temp = f[i].getName();
				if (f[i].isDirectory())
				{
					File g = new File(to + temp);
					if (!g.exists())
						g.mkdirs();
				} else
					copy(from + temp, to + temp);
				copys(from + temp, to + temp); 
			}
		}

		return true;
	}
	
	public static String saveToDir(File file, String fileName, String dir){
		String filePath = dir + fileName;
		try {
			java.io.InputStream is = new java.io.FileInputStream(file);
            java.io.OutputStream os = new java.io.FileOutputStream(filePath);
            byte buffer[] = new byte[8192];
            int count = 0;
            while ((count = is.read(buffer)) > 0)
            {
                os.write(buffer, 0, count);
            }
            os.close();
            is.close();
			return fileName;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Fail: " + fileName);
		}
		return null;
	}
}
