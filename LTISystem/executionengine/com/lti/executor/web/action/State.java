package com.lti.executor.web.action;

import com.lti.executor.web.BasePage;
import com.lti.util.StringUtil;

public class State extends BasePage {

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
			Long portfolioID = Long.parseLong(request.getParameter("portfolioID"));

			Integer state = (Integer) Action.session.get(portfolioID+".state");
			Long timestamp = (Long) Action.session.get(portfolioID+".timestamp");
			
			if(state==null){
				info="{alive:false,state:-1}";
			}else{
				if(timestamp==null||(timestamp-System.currentTimeMillis())/1000/60>1){
					info="{alive:true,state:"+state+"}";
					Action.session.remove(portfolioID+".state");
					Action.session.remove(portfolioID+".timestamp");
				}
				info="{alive:true,state:"+state+"}";
			}
			String func=request.getParameter("function");
			if(func!=null){
				info=func+"("+info+")";
			}
		} catch (Exception e) {
			info = "<pre>" + StringUtil.getStackTraceString(e) + "</pre>";
		}
		//System.out.println(info);
		return "info.ftl";
	}
	
}
