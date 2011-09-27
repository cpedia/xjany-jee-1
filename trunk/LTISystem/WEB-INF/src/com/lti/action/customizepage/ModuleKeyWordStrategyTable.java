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

public class ModuleKeyWordStrategyTable extends CustomizePageAction{
	public ModuleKeyWordStrategyTable(){super();}

	private static final long serialVersionUID = 2496394976004782444L;
Long userID;
	public List<StrategyItem> getTopStrategies(List<Strategy> strategies) {
		
		boolean realtime = userManager.HasRole(Configuration.ROLE_PORTFOLIO_REALTIME, userID);
		
		int year1 = PortfolioMPT.LAST_ONE_YEAR;
		int year3 = PortfolioMPT.LAST_THREE_YEAR;
		int year5 = PortfolioMPT.LAST_FIVE_YEAR;
		if (!realtime) {
			year1 = PortfolioMPT.DELAY_LAST_ONE_YEAR;
			year3 = PortfolioMPT.DELAY_LAST_THREE_YEAR;
			year5 = PortfolioMPT.DELAY_LAST_FIVE_YEAR;
		}
		
		List<StrategyItem> items = new ArrayList<StrategyItem>();

		for (int i = 0; i < strategies.size(); i++) {

			StrategyItem si = new StrategyItem();
			PortfolioMPT mpt = strategyManager.getTopStrategyByMPT(strategies.get(i).getID(), year1, com.lti.service.bo.PortfolioMPT.SORT_BY_SHARPERATIO);

			if (mpt == null)
				continue;
			

			si.setID(strategies.get(i).getID());
			si.setName(strategies.get(i).getName());
			
			si.setUserName("ValidFi");
			User u = null;
			if (mpt.getStrategyUserID() != null) {
				u = userManager.get(mpt.getStrategyUserID());
				if (u != null) {
					si.setUserName(u.getUserName());
				}
			}
			
			si.setShowName();

			Portfolio p = portfolioManager.get(mpt.getPortfolioID());
			if (p != null && p.getState() != Configuration.PORTFOLIO_STATE_TEMP) {
				si.setPortfolioID(p.getID());
				si.setPortfolioName(p.getName());
				
				if(p.getState()!=null&&p.getState()==Configuration.PORTFOLIO_STATE_TEMP)continue;
				
				if(!userID.equals(p.getUserID())){
					continue;
				}

				PortfolioMPT mpt1 = mpt;
				PortfolioMPT mpt2 = portfolioManager.getPortfolioMPT(p.getID(), year3);
				PortfolioMPT mpt3 = portfolioManager.getPortfolioMPT(p.getID(), year5);

				
				si.setPortfolioUserName("ValidFi");
				if (u != null) {
					si.setPortfolioUserName(u.getUserName());
				}
				
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
				si.setPortfolioShortName(mpt1.getState());
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
			items.add(si);
		}
		return items;
	}	@SuppressWarnings("unchecked")
	public String process() throws Exception {

Template t = BasePage.CustomizePageConf.getTemplate("ModuleKeyWordStrategyTable.ftl");ByteArrayOutputStream buf = new ByteArrayOutputStream();Writer out = new OutputStreamWriter(buf, "utf8");		executeUserCode();
		t.process(root, out);		return (buf.toString());
	}
	@SuppressWarnings("unchecked")
	public void executeUserCode() throws Exception {

		//usage:
//in your customize template, invoke this module with ${module("ModuleKeyWordStrategyTable","endowment")}
//"endowment" represent keyword

User user = userManager.getLoginUser();
userID = user.getID();
List<StrategyItem> items = new ArrayList<StrategyItem>();
String keyword=getModuleParameter(1);


String hql="from Strategy s where (s.Name like '%"+keyword+"%' or s.Categories like '%"+keyword+"%' or s.Description like '%"+keyword+"%') and (s.UserID="+userID+" or s.IsPublic=1)";
List<Strategy> strategies=strategyManager.findByHQL(hql);
		
if(strategies!=null)items=getTopStrategies(strategies);

root.put("items",items );
root.put("keyword",keyword);

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
//in your customize template, invoke this module with ${module("ModuleKeyWordStrategyTable","endowment")}
//"endowment" represent keyword

User user = userManager.getLoginUser();
userID = user.getID();
List<StrategyItem> items = new ArrayList<StrategyItem>();
String keyword=getModuleParameter(1);


String hql="from Strategy s where (s.Name like '%"+keyword+"%' or s.Categories like '%"+keyword+"%' or s.Description like '%"+keyword+"%') and (s.UserID="+userID+" or s.IsPublic=1)";
List<Strategy> strategies=strategyManager.findByHQL(hql);
		
if(strategies!=null)items=getTopStrategies(strategies);

root.put("items",items );
root.put("keyword",keyword);

String includeHeader=getParameter("includeHeader");
if(includeHeader!=null&&includeHeader.equalsIgnoreCase("true")){
  root.put("javascript","");
}else{
  root.put("javascript","<script type='text/javascript' src='../images/jquery-1.2.6.pack.js'></script>");
}

		return Action.SUCCESS;
	}

}
