package com.lti.action.admin.header;

import com.lti.action.Action;
import com.lti.system.ContextHolder;
import com.lti.system.SearchConfiguration;
import com.opensymphony.xwork2.ActionSupport;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class SaveAction extends ActionSupport implements Action {

private static final long serialVersionUID = 1L;
	
	String fileString;

	public String getFileString() {
		return fileString;
	}

	public void setFileString(String fileString) {
		this.fileString = fileString;
	}

	public String view() throws Exception {
		fileString="";
		String path = ContextHolder.getServletPath();
		File file = new File(path + "/jsp/header.jsp");
		BufferedReader reader = null;
		if(file.exists()){
			reader = new BufferedReader(new FileReader(file));
			String tempString;
			while ((tempString = reader.readLine()) != null){
				if(tempString!=null){
					fileString += (tempString + "\n");			
				}
			}
			reader.close();
		}
		//fileString = path;
		return Action.SUCCESS;
	}
	
	public String save() throws Exception {
		String path = ContextHolder.getServletPath();
		File file = new File(path + "/jsp/header.jsp");
		BufferedWriter writer = null;
		if(file.exists()){
			writer = new BufferedWriter(new FileWriter(file));
			writer.write(fileString);
			writer.close();
			SearchConfiguration.load();
		}
		return Action.SUCCESS;
	}
}
