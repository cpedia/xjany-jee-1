package com.lti.action.admin.loaddata;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.action.Action;
import com.lti.jobscheduler.DailyUpdateControler;
import com.lti.util.LTIDate;
import com.lti.util.LTIDownLoader;
import com.lti.util.StringUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class UpdateAdjustCloseAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(UpdateAdjustCloseAction.class);
	
	private File uploadFile;
	
	private String uploadFileFileName;
	
	private String dateStr;
	
	public void validate(){
		if(this.uploadFile==null){
			addFieldError("uploadFile","Upload file is not validate!");
			return;
		}
	}

	public class UpdateAdjustCloseThread extends Thread{
		
		private Map session;
		
		private File uploadFile;
		
		private String fileName;
		
		private LTIDownLoader downLoader;
		
		private Date startDate;
		
		public UpdateAdjustCloseThread(Map session, LTIDownLoader downLoader, String fileName, File uploadFile, Date startDate) {
			super();
			this.session = session;
			this.downLoader = downLoader;
			this.fileName = fileName;
			this.uploadFile=uploadFile;
			this.startDate = startDate;
		}
		
		public void run(){
			try {
				LTIDownLoader.SESSION.set(session);
				downLoader.batchUpdateAdjustClose(fileName, startDate);
			} catch (Exception e) {
				addMessage(StringUtil.getStackTraceString(e),session);
				e.printStackTrace();
			}finally{
				uploadFile.delete();
			}
		}
	}
	
	public static void addMessage(String s,Map session){
		if(session==null)return;
		synchronized(session){
			List<String> list=(List<String>)session.get("UpdateAdjustCloseAction");
			if(list==null)list=new ArrayList<String>();
			list.add(s);
			session.put("UpdateAdjustCloseAction",list);
		}
	}
	
	public static String getMessage(Map session){
		if(session==null)return "No Message!";
		synchronized(session){
			List<String> list=(List<String>)session.get("UpdateAdjustCloseAction");
			if(list==null||list.size()==0)return "No Message!";
			StringBuffer sb=new StringBuffer();
			for(int i=0;i<list.size();i++){
				sb.append(list.get(i));
				sb.append("\n"); 
			}
			session.remove("UpdateAdjustCloseAction");
			return sb.toString();
		}
	}
	
	@Override
	public String execute() throws Exception {
		Date startDate = LTIDate.parseStringToDate(dateStr);
		if(startDate == null){
			return Action.SUCCESS;
		}
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
		UpdateAdjustCloseThread ut = new UpdateAdjustCloseThread(ActionContext.getContext().getSession(), dl, fileName, uploadFile, startDate);
		ut.start();
		return Action.SUCCESS;
	}

	public File getUploadFile() {
		return uploadFile;
	}

	public void setUploadFile(File uploadFile) {
		this.uploadFile = uploadFile;
	}

	public String getUploadFileFileName() {
		return uploadFileFileName;
	}

	public void setUploadFileFileName(String uploadFileFileName) {
		this.uploadFileFileName = uploadFileFileName;
	}

	public String getDateStr() {
		return dateStr;
	}

	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}

	


	

}
