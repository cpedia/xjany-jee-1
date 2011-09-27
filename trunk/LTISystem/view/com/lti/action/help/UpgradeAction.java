package com.lti.action.help;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.lti.action.Action;
import com.lti.service.PortfolioManager;
import com.lti.service.SecurityManager;
import com.lti.service.bo.Security;
import com.lti.system.ContextHolder;
import com.lti.util.LTIDate;
import com.opensymphony.xwork2.ActionSupport;

public class UpgradeAction extends ActionSupport implements Action {
	private static final long serialVersionUID = 1L;
	
	
	
	private String resultString;

	public String getResultString() {
		return resultString;
	}


	public void setResultString(String resultString) {
		this.resultString = resultString;
	}


	public String execute(){
		resultString="";
		try {
			File root=new File(ContextHolder.getServletPath()+"/jsp/help");
			File[] files=root.listFiles();
			List<String> vers=new ArrayList<String>();
			for(int i=0;i<files.length;i++){
				String name=files[i].getName();
				if(name.toLowerCase().endsWith(".zip")){
					vers.add(files[i].getName().substring(0, name.length()-4));
				}
			}
			Collections.sort(vers);
			resultString=vers.get(vers.size()-1)+","+"/jsp/help/"+vers.get(vers.size()-1)+".zip";
		} catch (Exception e) {
			// TODO: handle exception
			resultString = "";
		}
		return Action.SUCCESS;
	}
	public static void main(String[] args){
		UpgradeAction ua=new UpgradeAction();
		ua.execute();
		System.out.println(ua.resultString);
	}

}
