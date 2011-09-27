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
import com.lti.service.bo.SecurityDailyData;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.util.LTIDate;
import com.lti.util.StringUtil;

/**
 * @author YZW
 *
 */
public class UpdateAdjNAVByNull extends BasePage{

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
//		Date startDate = new Date();
		List<Security> cefList = securityManager.getSecuritiesByType(Configuration.SECURITY_TYPE_CLOSED_END_FUND);
//		List<Security> cefList = new ArrayList<Security>();
//		cefList.add(securityManager.get(329l));
		if(cefList != null&&cefList.size()>0) {
			
			for(Security cef : cefList){
//				startDate = cef.getStartDate();
				List<SecurityDailyData> sdds = securityManager.getNAVList(cef.getID());
				if((sdds!=null)&&(sdds.size()>0)&&(sdds.get(0).getNAV()!=null&&sdds.get(0).getNAV()>0.0)&&(sdds.get(0).getAdjNAV()==null||sdds.get(0).getAdjNAV()==0.0)){
					sdds.get(0).setAdjNAV(sdds.get(0).getNAV());
					securityManager.updateDailyData(sdds.get(0));
					sb.append(this.updateOneCEF(cef, sdds.get(0).getDate()));
					sb.append("<br>\r\n");
					System.out.println(cef.getSymbol());
				}else{
//					sb.append(this.updateOneCEF(cef, sdds.get(0).getDate()));
					sb.append(cef.getSymbol()+" without updating!");
					sb.append("<br>\r\n");
					System.out.println(cef.getSymbol());
				}	
			}
		}
		info = sb.toString();
		return "info.ftl";
	}
	public static void main(String[] args){
		StringBuffer sb = new StringBuffer();
		SecurityManager securityManager = ContextHolder.getSecurityManager();
//		Date startDate = new Date();
//		List<Security> cefList = securityManager.getSecuritiesByType(Configuration.SECURITY_TYPE_CLOSED_END_FUND);
		List<Security> cefList = new ArrayList<Security>();
		cefList.add(securityManager.get(493l));
		if(cefList != null) {
			for(Security cef : cefList){
//				startDate = cef.getStartDate();
				List<SecurityDailyData> sdds = securityManager.getNAVList(cef.getID());
				if((sdds.get(0).getNAV()!=null&&sdds.get(0).getNAV()>0.0)&&(sdds.get(0).getAdjNAV()==null||sdds.get(0).getAdjNAV()==0.0)){
					sdds.get(0).setAdjNAV(sdds.get(0).getNAV());
					securityManager.updateDailyData(sdds.get(0));
//					sb.append(this.updateOneCEF(cef, sdds.get(0).getDate()));
					sb.append("<br>\r\n");
					System.out.println(cef.getSymbol()+" update success!");
				}else{
					sb.append(cef.getSymbol()+" do not update!");
					sb.append("<br>\r\n");
					System.out.println(cef.getSymbol()+" do not update!");
				}
			}
		}
	}
}
