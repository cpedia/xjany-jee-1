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

public class ModuleStrategyClassTable extends CustomizePageAction{
	public ModuleStrategyClassTable(){super();}

	private static final long serialVersionUID = 2496394976004782444L;
	public  void setTopStrategies(List<StrategyItem> strategies, Long strategyClassID, int size) {
		UserManager userManager = ContextHolder.getUserManager();
		StrategyManager strategyManager = ContextHolder.getStrategyManager();
		StrategyClassManager strategyClassManager = ContextHolder.getStrategyClassManager();
		PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();
		Long userID;
		User user = userManager.getLoginUser();
		if (user == null) {
			userID = -1l;
		} else
			userID = user.getID();
		boolean realtime = userManager.HasRole(Configuration.ROLE_PORTFOLIO_REALTIME, userID);
		

		Object[] IDs = strategyClassManager.getClassIDs(strategyClassID);

		int year1 = PortfolioMPT.LAST_ONE_YEAR;
		int year3 = PortfolioMPT.LAST_THREE_YEAR;
		int year5 = PortfolioMPT.LAST_FIVE_YEAR;
		if (!realtime) {
			year1 = PortfolioMPT.DELAY_LAST_ONE_YEAR;
			year3 = PortfolioMPT.DELAY_LAST_THREE_YEAR;
			year5 = PortfolioMPT.DELAY_LAST_FIVE_YEAR;
		}

		List<PortfolioMPT> mpts = strategyManager.getTopStrategyByMPT(IDs, year1, com.lti.service.bo.PortfolioMPT.SORT_BY_SHARPERATIO, userID, size);

		if (mpts == null) {
			return;
		}
		for (int i = 0; i < mpts.size(); i++) {

			StrategyItem si = new StrategyItem();
			si.setID(mpts.get(i).getStrategyID());
			si.setName(mpts.get(i).getStrategyName());
			si.setUserName("ValidFi");
			User u = null;
			if (mpts.get(i).getStrategyUserID() != null) {
				u = userManager.get(mpts.get(i).getStrategyUserID());
				if (u != null) {
					si.setUserName(u.getUserName());
				}
			}

			si.setShowName();

			PortfolioMPT mpt1 = mpts.get(i);

			if (mpt1.getPortfolioID() != null) {
				si.setPortfolioID(mpt1.getPortfolioID());
				si.setPortfolioName(mpt1.getName());
				Date lastValidDate = mpt1.getEndDate();
				if (lastValidDate != null) {
					Date lastLegalDate=userManager.getLegalDate(userID, lastValidDate);
					
					java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy/MM/dd");
					if(!realtime&&lastLegalDate.before(lastValidDate)){
						si.setLastValidDate(sdf.format(lastLegalDate));
					}else{
						si.setLastValidDate(sdf.format(lastValidDate));
					}
					
					
					Date lastTransactionDate = mpt1.getLastTransactionDate();
					if (lastTransactionDate != null) {
						if(!realtime&&lastLegalDate.before(lastTransactionDate)){
							Date d=portfolioManager.getTransactionLatestDate(mpt1.getPortfolioID(), lastLegalDate);
							if(d!=null)si.setLastTransactionDate(sdf.format(d));
						}else{
							si.setLastTransactionDate(sdf.format(lastTransactionDate));
						}
						
					}
				}
				
				si.setPortfolioUserName("ValidFi");
				if (u != null) {
					si.setPortfolioUserName(u.getUserName());
				}
				si.setPortfolioShortName(mpt1.getState());
				PortfolioMPT mpt2 = portfolioManager.getPortfolioMPT(mpt1.getPortfolioID(), year3);
				PortfolioMPT mpt3 = portfolioManager.getPortfolioMPT(mpt1.getPortfolioID(), year5);
				if (mpt1 != null) {
					si.setAR1(FormatUtil.formatPercentage(mpt1.getAR()));
					si.setBeta1(FormatUtil.formatQuantity(mpt1.getBeta()));
					si.setSharpeRatio1(FormatUtil.formatPercentage(mpt1.getSharpeRatio()));
				}
				if (mpt2 != null) {
					si.setAR3(FormatUtil.formatPercentage(mpt2.getAR()));
					si.setBeta3(FormatUtil.formatQuantity(mpt2.getBeta()));
					si.setSharpeRatio3(FormatUtil.formatPercentage(mpt2.getSharpeRatio()));
				}
				if (mpt3 != null) {
					si.setAR5(FormatUtil.formatPercentage(mpt3.getAR()));
					si.setBeta5(FormatUtil.formatQuantity(mpt3.getBeta()));
					si.setSharpeRatio5(FormatUtil.formatPercentage(mpt3.getSharpeRatio()));
				}
			}
			strategies.add(si);
		}
	}	@SuppressWarnings("unchecked")
	public String process() throws Exception {

Template t = BasePage.CustomizePageConf.getTemplate("ModuleStrategyClassTable.ftl");ByteArrayOutputStream buf = new ByteArrayOutputStream();Writer out = new OutputStreamWriter(buf, "utf8");		executeUserCode();
		t.process(root, out);		return (buf.toString());
	}
	@SuppressWarnings("unchecked")
	public void executeUserCode() throws Exception {

		//usage:
//in your customize template, invoke this module with ${module("ModuleStrategyClassTable","3","0")}
//"3" represent class ID
//"0" represent size

List<StrategyItem> items = new ArrayList<StrategyItem>();
Long classid=Long.parseLong(getModuleParameter(1));
Integer size=Integer.parseInt(getModuleParameter(2));
setTopStrategies(items, classid,size);
root.put("items",items );
root.put("classid",classid);
String includeHeader=getParameter("includeHeader");
if(includeHeader!=null&&includeHeader.equalsIgnoreCase("true")){
  root.put("javascript","");
}else{
  root.put("javascript","<script type='text/javascript' src='../images/jquery-1.2.6.pack.js'></script>");
}


	}
	@SuppressWarnings("unchecked")
	public String execute() throws Exception {

		init();
		//usage:
//in your customize template, invoke this module with ${module("ModuleStrategyClassTable","3","0")}
//"3" represent class ID
//"0" represent size

List<StrategyItem> items = new ArrayList<StrategyItem>();
Long classid=Long.parseLong(getModuleParameter(1));
Integer size=Integer.parseInt(getModuleParameter(2));
setTopStrategies(items, classid,size);
root.put("items",items );
root.put("classid",classid);
String includeHeader=getParameter("includeHeader");
if(includeHeader!=null&&includeHeader.equalsIgnoreCase("true")){
  root.put("javascript","");
}else{
  root.put("javascript","<script type='text/javascript' src='../images/jquery-1.2.6.pack.js'></script>");
}

		return Action.SUCCESS;
	}

}
