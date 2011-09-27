package com.lti.executor.web.action;

import com.lti.executor.web.BasePage;
import com.lti.util.EmailUtil;
import com.lti.util.StringUtil;

public class Sendemail extends BasePage {

	private String info;

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}


	@Override
	public String execute() throws Exception {
		try {
			EmailUtil.sendEmails(false);
			info="Done.";
		} catch (Exception e) {
			info="<pre>"+StringUtil.getStackTraceString(e)+"</pre>";
		}
		
		return "info.ftl";

	}
	
	public  static void  main(String[] args) throws Exception{
		new Sendemail().execute();
	}

}
