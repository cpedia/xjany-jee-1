package com.lti.action.flash;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import com.lti.system.Configuration;

public class OutputFileAction {

	private String filename;
	
	private String returnString;
	
	public String execute()throws Exception{
		File file=new File(Configuration.getTempDir()+filename);
		if(!file.exists())throw new Exception("Request file does not exist.");
		FileReader fr=new FileReader(file);
		BufferedReader br=new BufferedReader(fr);
		StringBuffer sb=new StringBuffer();
		String line=br.readLine();
		while(line!=null){
			sb.append(line);
			sb.append("\n");
			line=br.readLine();
		}
		br.close();
		fr.close();
		file.delete();
		
		returnString=sb.toString();
		
		return "success";
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getReturnString() {
		return returnString;
	}

	public void setReturnString(String returnString) {
		this.returnString = returnString;
	}
	
}
