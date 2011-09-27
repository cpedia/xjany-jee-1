package com.lti.action.portfolio;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.lti.action.Action;
import com.lti.service.GroupManager;
import com.lti.service.PortfolioManager;
import com.lti.service.SecurityManager;
import com.lti.service.StrategyManager;
import com.lti.service.bo.CachePortfolioItem;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.PortfolioMPT;
import com.lti.service.bo.Security;
import com.lti.service.bo.SecurityMPT;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.util.LTIDate;
import com.opensymphony.xwork2.ActionSupport;

public class ComparePortfolioAction extends ActionSupport implements Action{

	private PortfolioManager portfolioManager;
	private StrategyManager strategyManager;
	private String portfolioString;
	private Long ID;
	private String action;
	private String endDate;
	private Map<String, CachePortfolioItem> portMap;
	private List<Long> portfolioIDs;
	private String term;
	private int size = 10;
	private String message;
	private List<String> ignoreNames;
	private String pName;
	private String portfolioName;
	private String portfolioSymbols;
	private int includeSecurity = 0;
	private String portfolioids;
	private String securityids;

	public String getPortfolioSymbols() {
		return portfolioSymbols;
	}

	public void setPortfolioSymbols(String portfolioSymbols) {
		this.portfolioSymbols = portfolioSymbols;
	}

	public String getPortfolioName() {
		return portfolioName;
	}

	public void setPortfolioName(String portfolioName) {
		this.portfolioName = portfolioName;
	}

	public String getPName() {
		return pName;
	}

	public void setPName(String name) {
		pName = name;
	}

	public List<String> getIgnoreNames() {
		return ignoreNames;
	}

	public void setIgnoreNames(List<String> ignoreNames) {
		this.ignoreNames = ignoreNames;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public List<Long> getPortfolioIDs() {
		return portfolioIDs;
	}

	public void setPortfolioIDs(List<Long> portfolioIDs) {
		this.portfolioIDs = portfolioIDs;
	}

	public String getPortfolioids() {
		return portfolioids;
	}

	public void setPortfolioids(String portfolioids) {
		this.portfolioids = portfolioids;
	}

	public String getSecurityids() {
		return securityids;
	}

	public void setSecurityids(String securityids) {
		this.securityids = securityids;
	}

	public Map<String, CachePortfolioItem> getPortMap() {
		return portMap;
	}

	public void setPortMap(Map<String, CachePortfolioItem> portMap) {
		this.portMap = portMap;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public PortfolioManager getPortfolioManager() {
		return portfolioManager;
	}

	public void setPortfolioMangaer(PortfolioManager portfolioManager) {
		this.portfolioManager = portfolioManager;
	}

	public String getPortfolioString() {
		return portfolioString;
	}

	public void setPortfolioString(String portfolioString) {
		this.portfolioString = portfolioString;
	}

	public Long getID() {
		return ID;
	}

	public void setID(Long id) {
		ID = id;
	}

	public String compare() throws Exception{
		portfolioManager = ContextHolder.getPortfolioManager();
		SecurityManager securityManager = ContextHolder.getSecurityManager();
		strategyManager = ContextHolder.getStrategyManager();
		String[] portfolioNames = null;
		portfolioids="";
		securityids="";
		ignoreNames = new ArrayList<String>();
		List<Long> pids = new ArrayList<Long>();
		List<Security> securityList = new ArrayList<Security>();
		List<String> names = new ArrayList<String>();
		List<String> symbols = new ArrayList<String>();
		if(!StringUtils.isBlank(portfolioString)){
			portfolioNames = portfolioString.split(",");
		}
		portfolioString = new String();
		portfolioSymbols = new String();
		boolean firstPortfolio=true;
		boolean firstSecurity = true;
		for(int i=0;i<portfolioNames.length;i++){
			Portfolio portfolio=null;
			portfolioNames[i]=portfolioNames[i].trim();
			if(!portfolioNames[i].trim().equals("")){
				portfolio = portfolioManager.get(portfolioNames[i]);
				if(portfolio==null){
					Security security = securityManager.get(portfolioNames[i]);
					if(security == null){
						ignoreNames.add(portfolioNames[i]);
						continue;
					}
					securityList.add(security);
					if(firstSecurity){
						firstSecurity = false;
					}else{
						securityids +=",";
					}
					securityids+= security.getID().toString();
					names.add(security.getSymbol());
					symbols.add(security.getSymbol());
				}else{
					pids.add(portfolio.getID());
					if(firstPortfolio){
						firstPortfolio = false;
					}else{
						portfolioids+=",";
					}
					portfolioids +=portfolio.getID().toString();
					names.add(portfolio.getName());
					symbols.add(portfolio.getSymbol());
				}
				
			}
		}
		for(int i=0;i<names.size();i++){
			if(i!=names.size()-1){
				portfolioString+=names.get(i)+",";
				portfolioSymbols+=symbols.get(i)+",";
			}
			else{
				portfolioString+=names.get(i);
				portfolioSymbols+=symbols.get(i)+",";
			}
		}
		Date today = new Date();
		endDate = LTIDate.parseDateToString(today);
		portfolioIDs = pids;
		portMap = new HashMap<String, CachePortfolioItem>();
		for(int i=0;i<pids.size();i++){
			List<CachePortfolioItem> pitems = null;
			Portfolio portfolio = portfolioManager.get(pids.get(i));
			pitems=strategyManager.findByHQL("from CachePortfolioItem cp where cp.PortfolioID=" + pids.get(i) + " and cp.GroupID=0 and cp.RoleID="	+ Configuration.ROLE_PORTFOLIO_REALTIME_ID);
			String url="<a href=\"/LTISystem/jsp/portfolio/ViewPortfolio.action?ID="+pids.get(i)+"\">"+portfolio.getName()+"</a>";
			if (pitems != null && pitems.size() > 0){
				portMap.put(url, pitems.get(0));
			}
			else{
				CachePortfolioItem cpi = new CachePortfolioItem();
				cpi.setPortfolioID(pids.get(i));
				cpi.setPortfolioName(portfolio.getSymbol());
				portMap.put(url, cpi);
			}
		}
		for(int i=0;i<securityList.size();++i){
			Security se = securityList.get(i);
			CachePortfolioItem cpi = new CachePortfolioItem();
			cpi.setPortfolioID(se.getID());
			cpi.setPortfolioName(se.getSymbol());
			//http://www.myplaniq.com/LTISystem/jsp/fundcenter/View.action?symbol=AGG&type=1
			String url="<a href=\"/LTISystem/jsp/fundcenter/View.action?type=1&symbol=" + se.getSymbol() + "\">" + se.getSymbol() + "</a>";
			SecurityMPT mpt1 = securityManager.getSecurityMPT(se.getID(), PortfolioMPT.LAST_ONE_YEAR);
			if(mpt1 != null){
				cpi.setAR1(mpt1.getAR());
				cpi.setSharpeRatio1(mpt1.getSharpeRatio());
			}
			SecurityMPT mpt3 = securityManager.getSecurityMPT(se.getID(), PortfolioMPT.LAST_THREE_YEAR);
			if(mpt3 != null){
				cpi.setAR3(mpt3.getAR());
				cpi.setSharpeRatio3(mpt3.getSharpeRatio());
			}
			SecurityMPT mpt5 = securityManager.getSecurityMPT(se.getID(), PortfolioMPT.LAST_FIVE_YEAR);
			if(mpt5 != null){
				cpi.setAR5(mpt5.getAR());
				cpi.setSharpeRatio5(mpt5.getSharpeRatio());
			}
			portMap.put(url, cpi);
		}
		return Action.SUCCESS;
	}
	
	public String search() throws Exception{
		portfolioManager = ContextHolder.getPortfolioManager();
		SecurityManager securityManager = ContextHolder.getSecurityManager();
		String sql = "select distinct(Portfolioname),portfolioid from cache_group_portfolio gpi";
		if (term != null && !term.equals(""))
			sql += " where gpi.PortfolioName like '%" + term + "%'";
		else
			sql += " where gpi.PortfolioName like '%%'";
		long userID = ContextHolder.getUserManager().getLoginUser().getID();
		if (userID == Configuration.SUPER_USER_ID) {} 
		else {
			GroupManager gm = ContextHolder.getGroupManager();
			Object[] gids = gm.getGroupIDs(userID);
			if (gids.length == 1) {
				sql += " and (gpi.GroupID=" + gids[0] + " or gpi.UserID=" + userID + ")";
			} else {
				sql += " and (";
				for (Object gid : gids) {
					sql += "gpi.GroupID=" + gid;
					sql += " or ";
				}
				sql += "gpi.UserID=" + userID + ")";
			}
		}
		if (size > 0) {
			sql += " limit 0," + size;
		}
		//System.out.println(sql);
		List<Object[]> portfolios = null;
		try {
			portfolios = portfolioManager.findBySQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		StringBuffer sb = new StringBuffer();
		sb.append("[\r\n");
		int count = 0;
		if (portfolios != null && portfolios.size() != 0) {
			for (Object[] arr : portfolios) {
				if (count != 0)
					sb.append(",");
				sb.append("{\"name\":\"");
				sb.append(((String) arr[0]).replace("\"", "\\\""));
				sb.append("\",\"id\":\"");
				sb.append(((BigInteger) arr[1]).longValue());
				sb.append("\",\"data\":true}\r\n");
				++count;
			}
		}
		if(includeSecurity !=0 ){
			if(count < size){
				List<Security> resultList = securityManager.getSecuritiesByNameConsiderLength(term, 0);
				if(resultList != null){
					for(int i=count;i<size && i< resultList.size();++i){
						if (count != 0) 
							sb.append(",");
						sb.append("{\"name\":\"");
						sb.append(resultList.get(i).getSymbol());
						sb.append("\",\"id\":\"");
						sb.append((resultList.get(i).getID()));
						sb.append("\",\"data\":true}\r\n");
						++count;
					}
				}
			}
		}
		
		if (count > 0)
			sb.append(",");
		sb.append("{\"id\":\"" + term + "\",\"name\":\"Show all result for\",\"data\":false}\r\n");
		sb.append("]");
		message = sb.toString();
		return Action.MESSAGE;
	}
	
	public String main() throws Exception{
		if(action!=null && action.trim().equals("viewportfolio")){
			portfolioName=pName;
		}
		return Action.SUCCESS;
	}
	
	private String pSymbols;
	private String pString;
	private String endate;
	public String fullCompare() throws Exception{
		pSymbols=portfolioSymbols;
		endate=endDate;
		pString=portfolioString;
		return Action.SUCCESS;
	}

	public String getPSymbols() {
		return pSymbols;
	}

	public void setPSymbols(String symbols) {
		pSymbols = symbols;
	}

	public String getPString() {
		return pString;
	}

	public void setPString(String string) {
		pString = string;
	}

	public String getEndate() {
		return endate;
	}

	public void setEndate(String endate) {
		this.endate = endate;
	}

	public int getIncludeSecurity() {
		return includeSecurity;
	}

	public void setIncludeSecurity(int includeSecurity) {
		this.includeSecurity = includeSecurity;
	}
}
