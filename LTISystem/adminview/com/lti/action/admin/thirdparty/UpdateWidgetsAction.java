package com.lti.action.admin.thirdparty;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.lti.action.Action;
import com.lti.service.StrategyManager;
import com.lti.service.bo.ThirdParty;
import com.lti.service.bo.ThirdPartyResource;
import com.lti.system.ContextHolder;
import com.lti.util.CSVReader;

public class UpdateWidgetsAction {
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
		List<ThirdParty> trs = new ArrayList<ThirdParty>();
		List<String> line = r.readLineAsList();
		while (line != null) {
			if(line.size()==0||line.get(0).trim().equals("")){
				line=r.readLineAsList();
				continue;
			}
			ThirdParty tr = new ThirdParty();
			tr.setThirdPartyID(Long.parseLong(line.get(0)));
			tr.setThirdParty(line.get(1));
			tr.setParameters(line.get(2));
			trs.add(tr);
			line=r.readLineAsList();
		}
		sm.updateThirdParties(trs);
		message = "Successfully";

		return Action.MESSAGE;
	}
}
