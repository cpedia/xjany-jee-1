/**
 * 
 */
package com.lti.action.betagainranking;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

import com.lti.action.Action;
import com.lti.service.MutualFundManager;
import com.lti.service.bo.SecurityRanking;
import com.lti.system.ContextHolder;
import com.lti.util.LTIDate;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author CCD
 * 
 */
public class GetRankingFundsMapAction extends ActionSupport implements Action{
	
	private HashMap<String,List<SecurityRanking>> securityRankingMap;
	
	public String execute() throws Exception {
//		Date date ;
//		Calendar tmpCa = Calendar.getInstance();
//		tmpCa.set(2009,2,1);
//		date = LTIDate.clearHMSM(tmpCa.getTime());
//		MutualFundManager mutualFundManager = (MutualFundManager)ContextHolder.getInstance().getApplicationContext().getBean("mutualFundManager");
//		securityRankingMap= (HashMap<String, List<SecurityRanking>>) mutualFundManager.getRankingFundMap(date);
		//detachedCriteria.add(criterion)
		//ActionContext.getContext().getSession().put("securityRankingMap", securityRankingMap);

		return Action.SUCCESS;
	}
	
	public HashMap<String, List<SecurityRanking>> getSecurityRankingMap() {
		return securityRankingMap;
	}

	public void setSecurityRankingMap(
			HashMap<String, List<SecurityRanking>> securityRankingMap) {
		this.securityRankingMap = securityRankingMap;
	}
}
