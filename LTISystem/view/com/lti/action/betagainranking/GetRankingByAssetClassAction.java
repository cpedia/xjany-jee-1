/**
 * 
 */
package com.lti.action.betagainranking;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.lti.action.Action;
import com.lti.service.MutualFundManager;
import com.lti.service.bo.SecurityRanking;
import com.lti.system.ContextHolder;
import com.lti.util.LTIDate;

/**
 * @author CCD
 *
 */
public class GetRankingByAssetClassAction {
	
	private List<SecurityRanking> securityRankingList;
	
	private String name="";
	
	private Long assetClassID=0L;
	
	public String execute() throws Exception {
		Date date ;
		Calendar tmpCa = Calendar.getInstance();
		tmpCa.set(2009,2,1);
		date = LTIDate.clearHMSM(tmpCa.getTime());
		MutualFundManager mutualFundManager = (MutualFundManager)ContextHolder.getInstance().getApplicationContext().getBean("mutualFundManager");
		if(name!=null&& name.length()>0)
			securityRankingList= mutualFundManager.getFundRankingByAssetClassName(name, date,0);
		else
			securityRankingList=mutualFundManager.getFundRankingByAssetClassID(assetClassID, date,0);
		return Action.SUCCESS;
	}
	public List<SecurityRanking> getSecurityRankingList() {
		return securityRankingList;
	}

	public void setSecurityRankingList(List<SecurityRanking> securityRankingList) {
		this.securityRankingList = securityRankingList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public Long getAssetClassID() {
		return assetClassID;
	}

	public void setAssetClassID(Long assetClassID) {
		this.assetClassID = assetClassID;
	}
}
