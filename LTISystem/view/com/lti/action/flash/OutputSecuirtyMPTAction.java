package com.lti.action.flash;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.lti.action.Action;
import com.lti.bean.SortTypeBean;
import com.lti.service.PortfolioManager;
import com.lti.service.SecurityManager;
import com.lti.service.bo.Security;
import com.lti.system.ContextHolder;
import com.lti.util.FormatUtil;
import com.lti.util.LTIDate;
import com.lti.service.bo.SecurityDailyData;
import com.opensymphony.xwork2.ActionSupport;

public class OutputSecuirtyMPTAction extends ActionSupport implements Action {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private SecurityManager securityManager;

	// private PortfolioManager portfolioManager;

	private String symbols;

	private String xml;

	private String chosenMPT;

	private String startDate;

	private String endDate;

	private int chosen;

	@Override
	public void validate() {
		// TODO Auto-generated method stub
		super.validate();
		if (symbols == null || symbols.equals("")) {
			addActionError("No Security Is Selected!");
			return;
		}

		if (chosenMPT == null || chosenMPT.equals("")) {
			chosenMPT = "totalReturn";
		}
		chosen = SortTypeBean.getMPT(chosenMPT);
	}

	@Override
	public String execute() throws Exception {
		String[] symbolArray = symbols.split(",");

		java.text.DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
		Date startDate1 = null;
		if (startDate != null && !startDate.equals("")) {
			startDate1 = df.parse(startDate);
			startDate1 = LTIDate.getNewNYSETradingDay(startDate1);
		}

		Date endDate1 = null;
		if (endDate != null) {
			endDate1 = df.parse(endDate);
		}

		xml = getXML(symbolArray, startDate1, endDate1, 10000.0);
		//System.out.println(xml);
		return Action.SUCCESS;
	}

	private int[] pointers;
	
	public String getXML(String[] symbolArray, Date startDate, Date endDate, Double amount) {

		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<Beta><Data>");

		Security[] securities = new Security[symbolArray.length];
		for (int i = 0; i < securities.length; i++) {
			securities[i] = securityManager.get(symbolArray[i]);
		}

		if (startDate == null) {
			for (int i = 0; i < securities.length; i++) {
				if (securities[i] == null || securities[i].getStartDate() == null)
					continue;
				if (startDate == null || startDate.before(securities[i].getStartDate())) {
					startDate = securities[i].getStartDate();
				}
			}
		}

		if (endDate == null) {
			for (int i = 0; i < securities.length; i++) {
				if (securities[i] == null || securities[i].getEndDate() == null)
					continue;
				if (endDate == null || endDate.before(securities[i].getEndDate())) {
					endDate = securities[i].getEndDate();
				}
			}
		}

		if (startDate == null || endDate == null) {
			sb.append("</Data></Beta>");
			return sb.toString();
		}

		List<Date> dateList = LTIDate.getTradingDates(startDate, endDate);

		if (dateList == null || dateList.size() < 2) {
			sb.append("</Data></Beta>");
			return sb.toString();
		}

		List<SecurityDailyData>[] dailyDatas = new List[securities.length];
		for (int i = 0; i < securities.length; i++) {
			if (securities[i] == null)
				continue;
			dailyDatas[i] = securityManager.getDailyDatas(securities[i].getID(), startDate, endDate);
		}

		Double[] bases = new Double[securities.length];
		for (int i = 0; i < securities.length; i++) {
			if (dailyDatas[i] != null && dailyDatas[i].size() != 0) {
				bases[i] = ((SecurityDailyData) dailyDatas[i].get(i)).getAdjClose();
			}
		}
		
		pointers=new int[securities.length];
		for(int i=0;i<pointers.length;i++)pointers[i]=1;

		for (int tt = 1; tt < dateList.size(); tt++) {
			Date date = dateList.get(tt);
			String betas = "";
			for (int i = 0; i < securities.length; i++) {
				Double adjClose = getAmount(i,dailyDatas[i],date);
				if (adjClose != null && bases[i] != null) {
					Double totalReturn = amount * ((adjClose - bases[i]) / bases[i] + 1);
					betas += FormatUtil.formatQuantity(totalReturn);
				} else {
					betas += "null";
				}
				if (i != securities.length - 1)
					betas += ",";

			}
			sb.append("<E d='" + date + "' v='" + betas + "'/>\n");
		}

		sb.append("</Data></Beta>");
		return sb.toString();
	}

	private Double getAmount(int index, List<SecurityDailyData> list, Date date) {
		int pointer=pointers[index];
		if(list==null||list.size()<pointer+2){
			return null;
		}
		SecurityDailyData cur=list.get(pointer);
		SecurityDailyData next=list.get(pointer+1);
		if(LTIDate.equals(next.getDate(),date)){
			pointers[index]=pointer+1;
			return next.getAdjClose();
		}else if(cur.getDate().after(date)){
			return null;
		}if(next.getDate().after(date)){
			return cur.getAdjClose();
		}
		
		return null;
	}

	public String getSymbols() {
		return symbols;
	}

	public void setSymbols(String symbols) {
		this.symbols = symbols;
	}

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	public String getChosenMPT() {
		return chosenMPT;
	}

	public void setChosenMPT(String chosenMPT) {
		this.chosenMPT = chosenMPT;
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

	public void setSecurityManager(SecurityManager securityManager) {
		this.securityManager = securityManager;
	}

}
