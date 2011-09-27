package com.lti.executor.web.action;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import com.lti.executor.web.BasePage;
import com.lti.util.StringUtil;

public class History extends BasePage {

	private String info;

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
	
	private DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public String execute() throws Exception {

		String ip = request.getParameter("ip");
		if (ip == null)
			ip = "127.0.0.1";

		try {

			StringBuffer sb = new StringBuffer();
			for(int count=0;count<Action.timestamphistory.size();count++){
				Long ts = Action.timestamphistory.get(count);
				
				sb.append(++count);
				sb.append(",");
				sb.append(Action.history.get(ts+".title"));
				sb.append(",");
				sb.append(Action.history.get(ts+".mode"));
				sb.append(",");
				Date startingdate=(Date) Action.history.get(ts+".startingdate");
				if(startingdate!=null){
					sb.append(df.format(startingdate));
				}else{
					sb.append("N/A");
				}
				
				sb.append(",");
				Date enddate = (Date) Action.history.get(ts+".enddate");
				if(enddate!=null){
					sb.append(df.format(enddate));
				}else{
					sb.append("N/A");
				}
				sb.append(",");
				String es = null;
				if(startingdate!=null&&enddate!=null){
					es=(enddate.getTime()-startingdate.getTime())*1.0/1000/60+"mins";
				}
				if (es == null)
					es = "N/A";
				
				sb.append(es);
				sb.append(",");
				sb.append(Action.history.get(ts+".size"));
				sb.append(",");
				sb.append(Action.history.get(ts+".forceMonitor"));
				sb.append("#");
			}
			info = sb.toString();
			System.out.println(Action.timestamphistory.size());
		} catch (Exception e) {
			info = "<pre>" + StringUtil.getStackTraceString(e) + "</pre>";

		}
		return "info.ftl";
	}

}
