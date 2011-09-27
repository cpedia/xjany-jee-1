package com.lti.action.admin.thirdparty;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.lti.action.Action;
import com.lti.service.StrategyManager;
import com.lti.service.bo.ThirdPartyResource;
import com.lti.system.ContextHolder;
import com.lti.util.CSVReader;

public class UpdateAction {
	private File uploadFile;
	private String uploadFileFileName;

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

	private String thirdParty;

	public String getThirdParty() {
		return thirdParty;
	}

	public void setThirdParty(String thirdParty) {
		this.thirdParty = thirdParty;
	}

	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String execute() throws Exception {
		CSVReader r = new CSVReader(new FileReader(uploadFile));
		StrategyManager sm = ContextHolder.getStrategyManager();
		List<ThirdPartyResource> trs = new ArrayList<ThirdPartyResource>();
		List<String> line = r.readLineAsList();
		if(thirdParty==null){
			thirdParty=line.get(0).trim();
		}
		while (line != null) {
			if(line.size()==0||line.get(0).trim().equals("")){
				line=r.readLineAsList();
				continue;
			}
			ThirdPartyResource tr = new ThirdPartyResource();
			tr.setThirdParty(thirdParty);
			tr.setResourceType(Integer.parseInt(line.get(1)));
			tr.setResourceID(Long.parseLong(line.get(2)));
			trs.add(tr);
			line=r.readLineAsList();
		}
		sm.updateThirdPartyResources(thirdParty, trs);
		message = "Successfully";

		return Action.MESSAGE;
	}
}
