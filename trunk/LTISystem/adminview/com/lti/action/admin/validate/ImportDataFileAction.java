package com.lti.action.admin.validate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import com.lti.action.Action;
import com.lti.system.Configuration;
import com.opensymphony.xwork2.ActionSupport;



public class ImportDataFileAction extends ActionSupport  implements Action {
	
		private static final long serialVersionUID = 1L;

		private File uploadFile;
		
		private String uploadFileFileName;
		public void validate(){
			
			
			if(this.uploadFile==null){
				addFieldError("uploadFile","Upload file is not validate!");
				return;
			}
			
		}
		
		@Override
		public String execute() throws Exception {
			String filePath = Configuration.getTempDir();
			
			InputStream stream = new FileInputStream(uploadFile);

			OutputStream bos = new FileOutputStream(filePath + uploadFileFileName);

			int bytesRead = 0;
			
			byte[] buffer = new byte[8192];
			while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
				bos.write(buffer, 0, bytesRead);
			}
			bos.close();
			stream.close();
            System.out.println("upload success!");
            
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

	

}
