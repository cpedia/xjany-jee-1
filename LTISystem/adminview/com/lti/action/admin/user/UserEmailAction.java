package com.lti.action.admin.user;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lti.action.Action;
import com.lti.service.PortfolioManager;
import com.lti.service.UserManager;
import com.lti.service.bo.EmailNotification;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.User;
import com.lti.service.bo.Transaction;
import com.lti.system.Configuration;
import com.lti.type.Interval;
import com.lti.util.LTIDate;
import com.opensymphony.xwork2.ActionSupport;

public class UserEmailAction extends ActionSupport implements Action{
	
	private String action;
	private Long portfolioID;
	private String dateStr;
	private String portfolioName;
	private Portfolio portfolio;
	private UserManager userManager;
	private PortfolioManager portfolioManager;
	private List<Transaction> transactions;
	private List<Portfolio> portfolios;
	private List<Portfolio> errorPortfolios;
	private List<Portfolio> dePortfolios;
	private List<Portfolio> sHoldingPortfolios;
	private Map<String,List<Portfolio>> userPortfolios;
	private int transNums;
	private int portNums;
	private int sportNums;
	private int eportNums;
	private int dportNums;
	private int userNums;
	
	public List<Portfolio> getSHoldingPortfolios() {
		return sHoldingPortfolios;
	}

	public void setSHoldingPortfolios(List<Portfolio> holdingPortfolios) {
		sHoldingPortfolios = holdingPortfolios;
	}

	public List<Portfolio> getErrorPortfolios() {
		return errorPortfolios;
	}

	public void setErrorPortfolios(List<Portfolio> errorPortfolios) {
		this.errorPortfolios = errorPortfolios;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getDateStr() {
		return dateStr;
	}

	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}
	
	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}

	public Portfolio getPortfolio() {
		return portfolio;
	}

	public void setPortfolio(Portfolio portfolio) {
		this.portfolio = portfolio;
	}

	public UserManager getUserManager() {
		return userManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public List<Portfolio> getPortfolios() {
		return portfolios;
	}

	public void setPortfolios(List<Portfolio> portfolios) {
		this.portfolios = portfolios;
	}
	
	public PortfolioManager getPortfolioManager() {
		return portfolioManager;
	}

	public void setPortfolioManager(PortfolioManager portfolioManager) {
		this.portfolioManager = portfolioManager;
	}
	
	public Map<String, List<Portfolio>> getUserPortfolios() {
		return userPortfolios;
	}

	public void setUserPortfolios(Map<String, List<Portfolio>> userPortfolios) {
		this.userPortfolios = userPortfolios;
	}
	
	public int getTransNums() {
		return transNums;
	}

	public void setTransNums(int transNums) {
		this.transNums = transNums;
	}

	public int getPortNums() {
		return portNums;
	}

	public void setPortNums(int portNums) {
		this.portNums = portNums;
	}

	public int getSportNums() {
		return sportNums;
	}

	public void setSportNums(int sportNums) {
		this.sportNums = sportNums;
	}

	public int getEportNums() {
		return eportNums;
	}

	public void setEportNums(int eportNums) {
		this.eportNums = eportNums;
	}

	public int getUserNums() {
		return userNums;
	}

	public void setUserNums(int userNums) {
		this.userNums = userNums;
	}
	
	public List<Portfolio> getDePortfolios() {
		return dePortfolios;
	}

	public void setDePortfolios(List<Portfolio> dePortfolios) {
		this.dePortfolios = dePortfolios;
	}

	public int getDportNums() {
		return dportNums;
	}

	public void setDportNums(int dportNums) {
		this.dportNums = dportNums;
	}
	
	public Long getPortfolioID() {
		return portfolioID;
	}

	public void setPortfolioID(Long portfolioID) {
		this.portfolioID = portfolioID;
	}
	
	public String getPortfolioName() {
		return portfolioName;
	}

	public void setPortfolioName(String portfolioName) {
		this.portfolioName = portfolioName;
	}
	
	public String reportStatistics(){
		Date today = new Date();
		List<Transaction> trans = new ArrayList<Transaction>();
		//Date testDate = com.lti.util.LTIDate.getDate(2010, 1, 21);
		trans = userManager.showEmailTransaction(today);
		transNums = trans.size();
		transactions = trans;
		userNums = userManager.getUsersFromEN().size();
		int[] nums = portfolioStatistics();
		portNums = nums[0];
		eportNums = nums[1];
		sportNums = nums[2];
		dportNums = nums[3];
		return Action.SUCCESS;
	}
	
	public String viewTransactions(){
		Date today = new Date();
		if(dateStr=="Today")
			transactions = userManager.showEmailTransaction(today);
		if(dateStr=="Last Five Days"){
			Date fiveDate = com.lti.util.LTIDate.getNDaysAgo(today, 5);
			transactions = userManager.showEmailTransaction(fiveDate);
		}
		return Action.SUCCESS;
	}
	@Deprecated
	public int[] portfolioStatistics(){
//		int nums[] = new int[4];
//		List<Object[]> portfolioIDs = userManager.showEmailPortfolio();
//		nums[0] = portfolioIDs.size();
//		List<Transaction> trans = new ArrayList<Transaction>();
//		List<Portfolio> ps = new ArrayList<Portfolio>();
//		List<Portfolio> eps = new ArrayList<Portfolio>();
//		List<Portfolio> hps = new ArrayList<Portfolio>();
//		List<Portfolio> dps = new ArrayList<Portfolio>();
//		for(int i=0;i<nums[0];i++){
//			Long id = ((BigInteger) portfolioIDs.get(i)[0]).longValue();
//			Date lastSentDate = (Date)portfolioIDs.get(i)[1];
//			Portfolio portfolio = portfolioManager.get(id);
//			if(portfolio==null) continue;
//			Date lastTransactionDate = portfolio.getLastTransactionDate();
//			if(lastSentDate==null || lastTransactionDate==null){
//				eps.add(portfolio);
//				continue;
//			}
//			portfolio.setTransactions(trans);
//			portfolio.setLastSentDate(lastSentDate);
//			if(lastSentDate.after(lastTransactionDate)){
//				Date nextDate = LTIDate.getTomorrow(lastSentDate);
//				trans = portfolioManager.getTransactions(id, new Interval(nextDate,lastTransactionDate));
//				if(trans==null){
//					portfolio.setState(7);
//					ps.add(portfolio);
//					continue;
//				}
//				else{
//					eps.add(portfolio);
//					continue;
//				}
//			}
//			if(lastSentDate.before(lastTransactionDate)){
//				dps.add(portfolio);
//				continue;
//			}
//			ps.add(portfolio);
//			Portfolio sp = portfolio.calculateScheduleHolding();
//			if(sp!=null){
//				List<Transaction> srans = portfolioManager.getTransactions(id, Configuration.TRANSACTION_TYPE_SCHEDULE);
//				sp.setTransactions(srans);
//				hps.add(sp);
//			}
//		}
//		portfolios = ps;
//		errorPortfolios = eps;
//		sHoldingPortfolios = hps;
//		dePortfolios = dps;
//		nums[1] = errorPortfolios!=null?errorPortfolios.size():0;
//		nums[2] = sHoldingPortfolios!=null?sHoldingPortfolios.size():0;
//		nums[3] = dePortfolios!=null?dePortfolios.size():0;
//		return nums;
		return null;
	}

	public String viewEmailUsers(){
		Map<String,List<Portfolio>> uPorts = new HashMap<String,List<Portfolio>>();
		List<Long> userIDs = userManager.getUsersFromEN();
		List<Object[]> portfolioIDs = userManager.showEmailPortfolio();
		List<Long> pENIds = new ArrayList<Long>();
		int pSize = portfolioIDs.size();
		for(int j=0;j<pSize;j++){
			Long id = ((BigInteger) portfolioIDs.get(j)[0]).longValue();
			Date lastSentDate = (Date)portfolioIDs.get(j)[1];
			Portfolio portfolio = portfolioManager.get(id);
			Date lastTransactionDate = portfolio.getLastTransactionDate();
			if(lastSentDate.equals(lastTransactionDate))
				pENIds.add(id);
		}
		int userSize=userIDs.size();
		for(int i=0;i<userSize;i++){
			Long userID = userIDs.get(i);
			List<Portfolio> up = new ArrayList<Portfolio>();
			User euser = userManager.get(userID);
			List<EmailNotification> userENs = userManager.getEmailNotificationsByUser(userID);
			List<Long> uPNEIds = new ArrayList<Long>();
			for(int j=0;j<userENs.size();j++){
				Long id = userENs.get(j).getPortfolioID();
				up.add(portfolioManager.get(id));
				uPNEIds.add(id);
			}
			
			uPorts.put(euser.getUserName(),up);
		}
		userPortfolios = uPorts;
		return Action.SUCCESS;
	}
	
	public String viewPortTrans(){
		Date lastSentDate = userManager.getLastSentDate(portfolioID);
		Portfolio portfolio = portfolioManager.get(portfolioID);
		portfolioName = portfolio.getName();
		Date lastTransactionDate = portfolio.getLastTransactionDate();
		List<Transaction> trans = new ArrayList<Transaction>();
		if(lastTransactionDate!=null){
			if(lastSentDate.before(lastTransactionDate)){
				trans = portfolioManager.getTransactions(portfolioID, new Interval(lastSentDate,lastTransactionDate));
			}
			else{
				trans = portfolioManager.getTransactions(portfolioID,lastTransactionDate);
			}
		}else{
			trans = null;
		}
		transactions = trans;
		return Action.SUCCESS;
	}
}
