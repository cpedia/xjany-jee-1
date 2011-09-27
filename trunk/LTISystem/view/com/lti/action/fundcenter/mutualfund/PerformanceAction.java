package com.lti.action.fundcenter.mutualfund;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.lti.action.Action;
import com.lti.service.SecurityManager;
import com.lti.service.UserManager;
import com.lti.service.bo.Security;
import com.lti.service.bo.SecurityMPT;
import com.lti.util.StringUtil;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author cherry
 *
 */
public class PerformanceAction extends ActionSupport implements Action {
	
	private java.util.Map<String, Boolean> MPTMap;
	
	private java.lang.String chosenMPT;	//separated by ","
	
	//if you want to choose last 1, 3, 5 years, you can use -1, -3, -5 on behalf of them.
	private java.lang.String chosenYear;	//separated by ","
	
	private java.util.List<SecurityMPT> securityMPTList;
	
	private java.lang.String symbol;
	
	private SecurityManager securityManager;
	
	private UserManager userManager;
	
	private java.lang.Boolean alpha = false;
	
	private java.lang.Boolean beta = false;
	
	private java.lang.Boolean AR = false;
	
	private java.lang.Boolean RSquared = false;
	
	private java.lang.Boolean sharpeRatio = false;
	
	private java.lang.Boolean standardDeviation = false;
	
	private java.lang.Boolean treynorRatio = false;
	
	private java.lang.Boolean drawDown = false;
	
	private java.lang.Boolean totalReturn = false; 
	
	private java.util.List<Long> years;
	
	@Override
	public void validate() {
		// TODO Auto-generated method stub
		super.validate();
		if(symbol == null || symbol.equals("")){
			addActionError("No Security Symbol!");
			return;
		}
		//set choosen MPTs 
		if(chosenMPT != null && !chosenMPT.equals("")){
			List<String> choosenMPTs = StringUtil.splitKeywords(chosenMPT);
			if(choosenMPTs != null)
			{
				for(int i = 0; i < choosenMPTs.size(); i++){
					if(choosenMPTs.get(i).equalsIgnoreCase("alpha")){
						alpha = true;
						continue;
					}
					if(choosenMPTs.get(i).equalsIgnoreCase("beta")){
						beta = true;
						continue;
					}
					if(choosenMPTs.get(i).equalsIgnoreCase("AR")){
						AR = true;
						continue;
					}
					if(choosenMPTs.get(i).equalsIgnoreCase("RSquared")){
						RSquared = true;
						continue;
					}
					if(choosenMPTs.get(i).equalsIgnoreCase("sharpeRatio")){
						sharpeRatio = true;
						continue;
					}
					if(choosenMPTs.get(i).equalsIgnoreCase("standardDeviation")){
						standardDeviation = true;
						continue;
					}
					if(choosenMPTs.get(i).equalsIgnoreCase("treynorRatio")){
						treynorRatio = true;
						continue;
					}
					if(choosenMPTs.get(i).equalsIgnoreCase("drawDown")){
						drawDown = true;
						continue;
					}
					if(choosenMPTs.get(i).equalsIgnoreCase("totalReturn")){
						totalReturn = true;
					}
				}
			}
		}
		else{
			alpha = true; beta = true; AR = true; RSquared = true;
			sharpeRatio = true; standardDeviation = true; treynorRatio = true;
			drawDown = true; totalReturn = true;
		}
		if (chosenYear != null && !	chosenYear.equals("")) {
			List<String> chosenYears = StringUtil.splitKeywords(chosenYear);
			years = new ArrayList<Long>();
			if(chosenYears != null){
				for(int i = 0; i < chosenYears.size(); i++){
					Long year = Long.parseLong(chosenYears.get(i));
					years.add(year);
				}
			}
		}
	}
	
	public String performance() throws Exception{
		Security security = securityManager.getBySymbol(symbol);
		if(security == null){
			addActionError("No such security!");
			return Action.ERROR;
		}
		securityMPTList = securityManager.getSecurityMPTS(security.getID());
		
		return Action.SUCCESS;
	}
	
	public String allReturn() throws Exception{
		Security security = securityManager.getBySymbol(symbol);
		if(security == null){
			addActionError("No such security!");
			return Action.ERROR;
		}
		alpha = false; beta = false; AR = false; RSquared = false;
		sharpeRatio = false; standardDeviation = false; treynorRatio = false;
		drawDown = false; totalReturn = true;
		return Action.SUCCESS;
	}
	
	public String partialPerformance(){
		Security security = securityManager.getBySymbol(symbol);
		if(security == null){
			addActionError("No such security!");
			return Action.ERROR;
		}
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(SecurityMPT.class);
		detachedCriteria.add(Restrictions.eq("SecurityID", security.getID()));
		if(years != null)
			detachedCriteria.add(Restrictions.in("Year", years));
		securityMPTList = securityManager.getSecurityByMPT(detachedCriteria);
		return Action.SUCCESS;
	}

	public java.util.Map<String, Boolean> getMPTMap() {
		return MPTMap;
	}

	public void setMPTMap(java.util.Map<String, Boolean> map) {
		MPTMap = map;
	}

	public java.util.List<SecurityMPT> getSecurityMPTList() {
		return securityMPTList;
	}

	public void setSecurityMPTList(java.util.List<SecurityMPT> securityMPTList) {
		this.securityMPTList = securityMPTList;
	}

	public java.lang.String getSymbol() {
		return symbol;
	}

	public void setSymbol(java.lang.String symbol) {
		this.symbol = symbol;
	}

	public void setSecurityManager(SecurityManager securityManager) {
		this.securityManager = securityManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}


	public java.lang.String getChosenMPT() {
		return chosenMPT;
	}

	public void setChosenMPT(java.lang.String chosenMPT) {
		this.chosenMPT = chosenMPT;
	}

	public java.lang.String getChosenYear() {
		return chosenYear;
	}

	public void setChosenYear(java.lang.String chosenYear) {
		this.chosenYear = chosenYear;
	}

	public java.lang.Boolean getAlpha() {
		return alpha;
	}

	public void setAlpha(java.lang.Boolean alpha) {
		this.alpha = alpha;
	}

	public java.lang.Boolean getBeta() {
		return beta;
	}

	public void setBeta(java.lang.Boolean beta) {
		this.beta = beta;
	}

	public java.lang.Boolean getAR() {
		return AR;
	}

	public void setAR(java.lang.Boolean ar) {
		AR = ar;
	}

	public java.lang.Boolean getRSquared() {
		return RSquared;
	}

	public void setRSquared(java.lang.Boolean squared) {
		RSquared = squared;
	}

	public java.lang.Boolean getSharpeRatio() {
		return sharpeRatio;
	}

	public void setSharpeRatio(java.lang.Boolean sharpeRatio) {
		this.sharpeRatio = sharpeRatio;
	}

	public java.lang.Boolean getStandardDeviation() {
		return standardDeviation;
	}

	public void setStandardDeviation(java.lang.Boolean standardDeviation) {
		this.standardDeviation = standardDeviation;
	}

	public java.lang.Boolean getTreynorRatio() {
		return treynorRatio;
	}

	public void setTreynorRatio(java.lang.Boolean treynorRatio) {
		this.treynorRatio = treynorRatio;
	}

	public java.lang.Boolean getDrawDown() {
		return drawDown;
	}

	public void setDrawDown(java.lang.Boolean drawDown) {
		this.drawDown = drawDown;
	}

	public java.lang.Boolean getTotalReturn() {
		return totalReturn;
	}

	public void setTotalReturn(java.lang.Boolean totalReturn) {
		this.totalReturn = totalReturn;
	}

	public java.util.List<Long> getYears() {
		return years;
	}

	public void setYears(java.util.List<Long> years) {
		this.years = years;
	}
	
	
}
