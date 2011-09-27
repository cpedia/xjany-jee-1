/**
 * 
 */
package com.lti.action.betagainranking;

import com.lti.action.Action;
import com.lti.service.MutualFundManager;
import com.lti.service.bo.SecurityRanking;
import com.lti.system.ContextHolder;
import com.lti.util.LTIDate;
import com.opensymphony.xwork2.ActionSupport;
import java.util.*;

import org.apache.struts2.ServletActionContext;

/**
 * @author CCD
 *
 */
public class SecurityRankingTableAction extends ActionSupport implements Action{
	
	
	private String name;
	
	Integer size;
	
	private List<SecurityRanking> securityRankingList;
	
	public String execute()throws Exception {
		MutualFundManager mutualFundManager = (MutualFundManager)ContextHolder.getInstance().getApplicationContext().getBean("mutualFundManager");
		Date date = new Date();
		Calendar tmpCa = Calendar.getInstance();
		tmpCa.set(2009,2,1);
		date = LTIDate.clearHMSM(tmpCa.getTime());
		if(size==null)
			size=0;
		securityRankingList = mutualFundManager.getFundRankingByAssetClassName(name,date,size);
		return Action.SUCCESS;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<SecurityRanking> getSecurityRankingList() {
		return securityRankingList;
	}

	public void setSecurityRankingList(List<SecurityRanking> securityRankingList) {
		this.securityRankingList = securityRankingList;
	}


	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}
}
