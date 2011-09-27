/**
 * 
 */
package com.lti.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.lti.system.Configuration;
import com.lti.system.ContextHolder;

/**
 * @author CCD
 *
 */
public class GeneratePatch {

	public static String host = "222.200.180.114";
	
	public static void generate(List<String> fileNameList) throws IOException{
		if(fileNameList != null && fileNameList.size() > 0){
			for(String fileName : fileNameList)
				generate(fileName);
		}
	}
	
	public static void deleteFileList(List<String> fileNameList) {
		for(String fileName: fileNameList){
			deleteFile(fileName);
		}
	}
	
	private static void deleteFile(String fileName) {
		File file = new File(ContextHolder.getServletPath() + "/" + Configuration.get("CLASS_ROOT_PATH") + "/" + fileName);
		if(file != null)
			file.delete();
	}
	
	public static List<String> getDifferentFile() throws IOException{
		List<String> fileNameList = new ArrayList<String>();
		HashMap<String, String> serverMap = getServerVersionMap();
		HashMap<String, String> localMap = ExecutorVersionUtil.getVersionMap();
		Set<String> serverSet = serverMap.keySet();
		for(String key: serverSet){
			String serverHashCode = serverMap.get(key);
			String localHashCode = localMap.get(key);
			if(localHashCode == null || !serverHashCode.equals(localHashCode))
				fileNameList.add(key);
			localMap.remove(key);
		}
		List<String> deleteFileNameList = new ArrayList<String>();
		deleteFileNameList.addAll(localMap.keySet());
		deleteFileList(deleteFileNameList);
		return fileNameList;
	}
	
	public static HashMap<String, String> getServerVersionMap() throws IOException{
		URL url = new URL("http://" + host + "/LTISystem/jsp/ajax/DownloadForUpdate.action?fileName="+Configuration.VERSION_FILENAME);
		URLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setAllowUserInteraction(false);
		BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
		String inputLine;
		HashMap<String, String> serverMap = new HashMap<String, String>();
		while ((inputLine = in.readLine()) != null) {
			String[] pairs = inputLine.split(",");
			serverMap.put(pairs[0], pairs[1]);
		}
		return serverMap;
	}
	
	public static void generate(String fileName) throws IOException{
		URL url = new URL("http://" + host + "/LTISystem/jsp/ajax/DownloadForUpdate.action?fileName="+fileName);
		URLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setAllowUserInteraction(false);
		BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
		String inputLine;
		StringBuffer tempHtml = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			tempHtml.append(inputLine + "\n");
		}
		
		BufferedWriter buff = new BufferedWriter(new FileWriter(ContextHolder.getServletPath() + "/" + Configuration.get("CLASS_ROOT_PATH")+ "/" + fileName));
		for (int i = 0; i < tempHtml.length(); i++)
			buff.write(tempHtml.charAt(i));
		buff.flush();
		buff.close();
	}
	
	public static void update() throws IOException{
		String fileName = Configuration.VERSION_FILENAME;
		//List<String> fileNameList = getDifferentFile();
		//generate(fileNameList);
		//generate(fileName);
		deleteFile(fileName);
	}
	
	public static void main(String[] args) throws IOException {
		GeneratePatch.update();
	}

}
