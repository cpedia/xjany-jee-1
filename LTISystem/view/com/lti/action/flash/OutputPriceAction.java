package com.lti.action.flash;

import java.util.Date;
import java.util.List;

import com.lti.action.Action;
import com.lti.service.SecurityManager;
import com.lti.service.bo.Security;
import com.lti.system.ContextHolder;
import com.lti.util.FormatUtil;
import com.lti.util.LTIDate;
import com.opensymphony.xwork2.ActionSupport;

public class OutputPriceAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;
	
	private SecurityManager securityManager;
	public String getXML(Long[] pids, Date startDate, Date endDate) {
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<Beta><Data>");

		int startPointers[] = new int[pids.length];
		int size = 0;
		int maxPointer = -1;
		Date date = null;

		List[] dailyDatas = new List[pids.length];
		for (int i = 0; i < pids.length; i++) {
			startPointers[i] = -1;
			dailyDatas[i] = securityManager.getDailyDatas(pids[i], startDate, endDate);
			if (dailyDatas[i].size() > size) {
				size = dailyDatas[i].size();
				maxPointer = i;
				date = ((com.lti.service.bo.SecurityDailyData) dailyDatas[i].get(0)).getDate();
			}
		}
		startPointers[maxPointer] = 0;

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < pids.length; j++) {
				if (startPointers[j] == -1) {
					if (LTIDate.equals(((com.lti.service.bo.SecurityDailyData) dailyDatas[j].get(0)).getDate(), date)) {
						startPointers[j] = i;
					}
				}
			}

			String betas = "";
			for (int j = 0; j < pids.length; j++) {
				if (startPointers[j] != -1 && i - startPointers[j] < dailyDatas[j].size()) {
					com.lti.service.bo.SecurityDailyData dailydata = (com.lti.service.bo.SecurityDailyData) dailyDatas[j].get(i - startPointers[j]);
					betas += FormatUtil.formatQuantity(dailydata.getClose());

				} else {
					betas += "0.0";
				}
				if (j != pids.length - 1)
					betas += ",";
			}

			sb.append("<E d='" + date + "' v='" + betas + "'/>");

			if (i + 1 < size)
				date = ((com.lti.service.bo.SecurityDailyData) dailyDatas[maxPointer].get(i + 1)).getDate();

		}

		sb.append("</Data></Beta>");
		return sb.toString();
	}

	private String indexArray;
	
	private String startDate;
	
	private String endDate;
	
	private String xml;

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	public String getIndexArray() {
		return indexArray;
	}

	public void setIndexArray(String indexArray) {
		this.indexArray = indexArray;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	@Override
	public String execute() throws Exception {
		securityManager = ContextHolder.getSecurityManager();
		
		String[] names=indexArray.split(",");
		Long[] pids = new Long[names.length];
		for(int i=0;i<names.length;i++){
			Security p=securityManager.get(names[i]);
			pids[i]=p.getID();
		}
		java.text.DateFormat df=new java.text.SimpleDateFormat("yyyy-MM-dd");
		Date startDate1 = df.parse(startDate);
		Date endDate1 = df.parse(endDate);
		xml=getXML(pids, startDate1, endDate1);
		
		return "success";
		
	}// end excute

}
