/**
 * 
 */
package com.lti.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.lti.system.Configuration;
import com.lti.system.ContextHolder;

/**
 * @author CCD
 *
 */


public class ExecutorVersionUtil {
	
	private static char hexChar[] = {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
        'a', 'b', 'c', 'd', 'e', 'f'
    };
	
	public static HashMap<String, String> versionMap;
	
	private static String getFileMD5(String filename)
    {
        String str = "";
        try{
            str = getHash(filename);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return str;
    }
	
	private static String getHash(String fileName) throws Exception {
		InputStream fis = new FileInputStream(fileName);
		byte buffer[] = new byte[1024];
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		for(int numRead = 0; (numRead = fis.read(buffer)) > 0;)
			md5.update(buffer, 0, numRead);
		fis.close();
		return toHexString(md5.digest());
	}
	
	private static String toHexString(byte b[])
    {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for(int i = 0; i < b.length; i++)
        {
            sb.append(hexChar[(b[i] & 0xf0) >>> 4]);
            sb.append(hexChar[ b[i] & 0xf]);
        }
        return sb.toString();
    }
	
	private static void calculateVersionMap_Recursive(File rootFile){
		if(rootFile.isDirectory()){
			File[] fileList = rootFile.listFiles();
			for(File file: fileList)
				calculateVersionMap_Recursive(file);
		}else if(rootFile.isFile()){
			System.out.println(rootFile.getName());
			String hashCode = getFileMD5(rootFile.getAbsolutePath());
			System.out.println(hashCode);
			int index = rootFile.getAbsolutePath().indexOf("classes");
			versionMap.put(rootFile.getAbsolutePath().substring(index + 8), hashCode);
		}
	}
	
	public static void calculateVersionMap() throws IOException{
		String dir = Configuration.getVersionDir();
		System.out.println(dir);
		System.out.println(ContextHolder.getServletPath());
		System.out.println(Configuration.get("CLASS_ROOT_PATH"));
		versionMap = new HashMap<String, String>();
		File rootFile = new File(ContextHolder.getServletPath() + "/" + Configuration.get("CLASS_ROOT_PATH"));
		calculateVersionMap_Recursive(rootFile);
		writeVersionMap(versionMap);
	}
	
	public static void writeVersionMap(Map<String, String> versionMap) throws IOException{
		
		BufferedWriter bw = new BufferedWriter( new OutputStreamWriter( new FileOutputStream(ContextHolder.getServletPath() + "/" + Configuration.get("CLASS_ROOT_PATH")+ "/" + Configuration.VERSION_FILENAME)));
		if(versionMap != null && versionMap.size() > 0){
			Set<String> set = versionMap.keySet();
			for(String key: set){
				String value = versionMap.get(key);
				bw.write(key+","+value+"\n");
			}
			bw.close();
		}
	}
	
	public static HashMap<String, String> getVersionMap() throws IOException {
		BufferedReader br = new BufferedReader( new InputStreamReader( new FileInputStream(ContextHolder.getServletPath() + "/" + Configuration.get("CLASS_ROOT_PATH") + "/" + Configuration.VERSION_FILENAME)));
		String line = null;
		versionMap = new HashMap<String, String>();
		while((line = br.readLine()) != null){
			String[] versionPair = line.split(",");
			versionMap.put(versionPair[0], versionPair[1]);
		}
		return versionMap;
	}
	
	public static void main(String[] args) throws IOException{
		ExecutorVersionUtil.calculateVersionMap();
		ExecutorVersionUtil.getVersionMap();
	}
	
}
