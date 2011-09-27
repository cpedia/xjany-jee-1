package com.lti.action.admin.fundlist;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.action.Action;
import com.lti.system.Configuration;
import com.opensymphony.xwork2.ActionSupport;

public class MainAction extends ActionSupport implements Action{

	/**
	 * @author fang
	 */
	private static final long serialVersionUID = 1L;
	
	static Log log = LogFactory.getLog(MainAction.class);
	
	private List<String> fileList = new ArrayList<String>();
	
	public final static String rootPath = Configuration.get11KDir();
	
	public String execute()throws Exception{
		
		File root=new File(rootPath,"DataFile");
		
		if(!root.exists()){
			root.mkdirs();
		}
		
		String[] fileName = root.list();
		
		if(fileName == null){
			String message = "There is no file";
			return Action.MESSAGE;
		}
		
		fileList = Arrays.asList(fileName);
		
		return Action.SUCCESS;
	}

	public List<String> getFileList() {
		return fileList;
	}

	public void setFileList(List<String> fileList) {
		this.fileList = fileList;
	}
}
