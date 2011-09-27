package com.lti.action.admin.loaddata;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.action.Action;
import com.lti.jobscheduler.DailyUpdateControler;
import com.lti.util.LTIDownLoader;
import com.lti.util.StringUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class ImportSecurityDataAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(ImportSecurityDataAction.class);
	
	private File uploadFile;
	private String uploadFileFileName;
	private String action;
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	private Boolean flash;
	private Boolean overWrite;

	public Boolean getOverWrite() {
		return overWrite;
	}

	public void setOverWrite(Boolean overWrite) {
		this.overWrite = overWrite;
	}

	public void validate(){
		
		if(this.flash==null){
			this.flash=false;
		}
		
		if(this.overWrite==null)
			this.overWrite=false;
		
		if(this.uploadFile==null){
			addFieldError("uploadFile","Upload file is not validate!");
			return;
		}
		
	}

	public class DownloadThread extends Thread{
		private Map session;
		private File uploadFile;
		
		public DownloadThread(Map session, LTIDownLoader downLoader, String fileName, boolean flash,File uploadFile) {
			super();
			this.session = session;
			this.downLoader = downLoader;
			this.fileName = fileName;
			this.flash = flash;
			this.uploadFile=uploadFile;
		}
		
		private LTIDownLoader downLoader;

		private String fileName;
		private boolean flash;

		public void run(){
//			if(DailyUpdateControler.isUpdating){
//				addMessage("it's udpating daily data now, please try later",session);
//				return;
//			}
			try {
				LTIDownLoader.SESSION.set(session);
				if(action.equalsIgnoreCase("OLD_VERSION"))
					downLoader.batchLoadAttribute(fileName, flash,overWrite);
				else if(action.equalsIgnoreCase("NEW_VERSION"))
					downLoader.batchImportSecurity(fileName, flash, overWrite);
			} catch (Exception e) {
				addMessage(StringUtil.getStackTraceString(e),session);
				e.printStackTrace();
			}finally{
				uploadFile.delete();
				//DailyUpdateControler.isUpdating=false;//change update state, and make daily update state page false
			}
		}
	}
	
	public static void addMessage(String s,Map session){
		if(session==null)return;
		synchronized(session){
			List<String> list=(List<String>)session.get("ImportSecurityDataAction");
			if(list==null)list=new ArrayList<String>();
			list.add(s);
			session.put("ImportSecurityDataAction",list);
		}
	}
	
	public static String getMessage(Map session){
		if(session==null)return "No Message!";
		synchronized(session){
			List<String> list=(List<String>)session.get("ImportSecurityDataAction");
			if(list==null||list.size()==0)return "No Message!";
			StringBuffer sb=new StringBuffer();
			for(int i=0;i<list.size();i++){
				sb.append(list.get(i));
				sb.append("\n"); 
			}
			session.remove("ImportSecurityDataAction");
			return sb.toString();
		}
	}
	
	@Override
	public String execute() throws Exception {
		
		LTIDownLoader dl = new LTIDownLoader();
		String filePath = dl.systemPath;
		InputStream stream = new FileInputStream(uploadFile);
		OutputStream bos = new FileOutputStream(filePath + uploadFile.getName());
		int bytesRead = 0;
		byte[] buffer = new byte[8192];
		while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
			bos.write(buffer, 0, bytesRead);
		}
		bos.close();
		stream.close();
		String fileName = filePath + uploadFile.getName();
		DownloadThread dr=new DownloadThread(ActionContext.getContext().getSession(), dl, fileName, flash, uploadFile);
		dr.start();
		return Action.SUCCESS;
	}

	public File getUploadFile() {
		return uploadFile;
	}

	public void setUploadFile(File uploadFile) {
		this.uploadFile = uploadFile;
	}

	public Boolean getFlash() {
		return flash;
	}

	public void setFlash(Boolean flash) {
		this.flash = flash;
	}

	public String getUploadFileFileName() {
		return uploadFileFileName;
	}

	public void setUploadFileFileName(String uploadFileFileName) {
		this.uploadFileFileName = uploadFileFileName;
	}

	


	

}
