package com.lti.executor.web.action;

import com.lti.executor.web.BasePage;
import com.lti.util.StringUtil;

public class Dailystop extends BasePage {

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

			int mode=0;
			try{
				mode=Integer.parseInt(request.getParameter("mode"));
			}catch(Exception e){}
			if(mode==0){
				if(Action.dailyUpdate!=null&&Action.dailyUpdate.isAlive()){
					Action.dailyUpdate.stop();
					Action.dailyUpdate.setStop(true);
					info = "OK";
				}else{
					info = "The daily update is not running.";
				}
			}else if(mode==1){
				if(Action.batchUpdate!=null&&Action.batchUpdate.isAlive()){
					Action.batchUpdate.stop();
					Action.batchUpdate.setStop(true);
					info = "OK";
				}else{
					info = "The batch update is not running.";
				}
			}else{
				info="Nothing to do.";
			}
			
			
		} catch (Exception e) {
			info = "<pre>" + StringUtil.getStackTraceString(e) + "</pre>";

		}
		return "info.ftl";
	}

}
