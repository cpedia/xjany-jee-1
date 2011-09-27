/**
 * 
 */
package com.lti.executor.web.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.lti.executor.web.BasePage;
import com.lti.service.DataManager;
import com.lti.service.SecurityManager;
import com.lti.service.bo.Security;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.util.LTIDate;
import com.lti.util.StringUtil;

/**
 * @author CCD
 *
 */
public class UpdateAdjNAVForCEF extends BasePage{

	private String info;

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}


	private String updateOneCEF(Security se, Date startDate){
		DataManager dataManager = ContextHolder.getDataManager();
		String result = "";
		try{
			boolean success = dataManager.updateAdjustNAVFromStartDate(se.getSymbol(), startDate);
			if(!success)
				result = se.getSymbol() + ",fail";
			else
				result = se.getSymbol() + ",success";
		}catch(Exception e){
			result = se.getSymbol() + ",fail";
			result +="\r\n<pre>"+StringUtil.getStackTraceString(e)+"</pre>";
		}
		return result;
	}
	
	@Override
	public String execute() throws Exception {
		StringBuffer sb = new StringBuffer();
		SecurityManager securityManager = ContextHolder.getSecurityManager();
		Date startDate = LTIDate.getDate(2009, 1, 1);
		List<Security> cefList = securityManager.getSecuritiesByType(Configuration.SECURITY_TYPE_CLOSED_END_FUND);
//		List<Security> cefList = new ArrayList<Security>();
//		cefList.add(securityManager.get(732l));
//		cefList.add(securityManager.get(679l));
//		cefList.add(securityManager.get(680l));
		if(cefList != null) {
			for(Security cef : cefList){
				sb.append(this.updateOneCEF(cef, startDate));
				sb.append("<br>\r\n");
				System.out.println(cef.getSymbol());
			}
		}
		info = sb.toString();
		return "info.ftl";
	}
	
}
