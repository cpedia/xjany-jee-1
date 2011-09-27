package com.lti.action.customizepage;

import com.lti.service.bo.*;
import java.util.*;
import com.lti.type.*;
import com.lti.util.*;

import java.io.*;
import com.lti.bean.*;
import com.lti.system.*;
import com.lti.service.*;
import com.lti.action.customizepage.CustomizePageAction;
import com.lti.customizepage.BasePage;
import com.lti.action.Action;
import freemarker.template.Template;

public class OutputXMLExample extends CustomizePageAction{
	public OutputXMLExample(){super();}

	private static final long serialVersionUID = 2496394976004782444L;
public String getXML(Long[] pids, Date startDate, Date endDate) {
		PortfolioManager pm = ContextHolder.getPortfolioManager();
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<Beta><Data>");

		int startPointers[] = new int[pids.length];
		int size = -1;
		int maxPointer = -1;
		Date date = null;

		List[] dailyDatas = new List[pids.length];
		for (int i = 0; i < pids.length; i++) {
			startPointers[i] = -1;
			dailyDatas[i] = pm.getDailydatasByPeriod(pids[i], startDate, endDate);
			if (dailyDatas[i].size() > size) {
				size = dailyDatas[i].size();
				maxPointer = i;
				date = ((com.lti.service.bo.PortfolioDailyData) dailyDatas[i].get(0)).getDate();
			}
		}
		startPointers[maxPointer] = 0;

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < pids.length; j++) {
				if (startPointers[j] == -1) {
					if (LTIDate.equals(((com.lti.service.bo.PortfolioDailyData) dailyDatas[j].get(0)).getDate(), date)) {
						startPointers[j] = i;
					}
				}
			}

			String betas = "";
			for (int j = 0; j < pids.length; j++) {
				if (startPointers[j] != -1 && i - startPointers[j] < dailyDatas[j].size()) {
					com.lti.service.bo.PortfolioDailyData dailydata = (com.lti.service.bo.PortfolioDailyData) dailyDatas[j].get(i - startPointers[j]);
					betas += FormatUtil.formatQuantity(dailydata.getAmount());

				} else {
					betas += "0.0";
				}
				if (j != pids.length - 1)
					betas += ",";
			}

			sb.append("<E d='" + date + "' v='" + betas + "'/>");

			if (i + 1 < size)
				date = ((com.lti.service.bo.PortfolioDailyData) dailyDatas[maxPointer].get(i + 1)).getDate();

		}

		sb.append("</Data></Beta>");
		return sb.toString();
	}	@SuppressWarnings("unchecked")
	public String process() throws Exception {

Template t = BasePage.CustomizePageConf.getTemplate("OutputXMLExample.ftl");ByteArrayOutputStream buf = new ByteArrayOutputStream();Writer out = new OutputStreamWriter(buf, "utf8");		executeUserCode();
		t.process(root, out);		return (buf.toString());
	}
	@SuppressWarnings("unchecked")
	public void executeUserCode() throws Exception {

			
		String[] names=((String[])parameters.get("portfolios"))[0].split(",");
		Long[] pids = new Long[names.length];
		for(int i=0;i<names.length;i++){
			Portfolio p=portfolioManager.get(names[i]);
			pids[i]=p.getID();
		}
		java.text.DateFormat df=new java.text.SimpleDateFormat("yyyy-MM-dd");
		Date startDate = df.parse(((String[])parameters.get("startDate"))[0]);
		Date endDate = df.parse(((String[])parameters.get("endDate"))[0]);
		root.put("xml", getXML(pids, startDate, endDate));

	}
	@SuppressWarnings("unchecked")
	public String execute() throws Exception {

		init();
			
		String[] names=((String[])parameters.get("portfolios"))[0].split(",");
		Long[] pids = new Long[names.length];
		for(int i=0;i<names.length;i++){
			Portfolio p=portfolioManager.get(names[i]);
			pids[i]=p.getID();
		}
		java.text.DateFormat df=new java.text.SimpleDateFormat("yyyy-MM-dd");
		Date startDate = df.parse(((String[])parameters.get("startDate"))[0]);
		Date endDate = df.parse(((String[])parameters.get("endDate"))[0]);
		root.put("xml", getXML(pids, startDate, endDate));
		return Action.SUCCESS;
	}

}
