package com.lti.action.clonecenter;

import java.util.Date;
import java.util.List;

import com.lti.service.PortfolioHoldingManager;
import com.lti.service.PortfolioManager;
import com.lti.service.bo.Portfolio;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;

public class EditAction {
	private Long ID;
	
	public Long getID() {
		return ID;
	}

	public void setID(Long id) {
		ID = id;
	}
	
	private Portfolio portfolio;
	private List<Date> holdingDates;
	




	public String execute(){
		
		PortfolioHoldingManager holdingManager=ContextHolder.getPortfolioHoldingManager();
		PortfolioManager portfolioManger=ContextHolder.getPortfolioManager();
		if(ID!=null)portfolio=portfolioManger.get(ID);
		if(portfolio!=null){
			try {
				holdingDates = holdingManager.findBySQL("select distinct h.date from "+Configuration.TABLE_PORTFOLIO_HOLDINGITEM+" h where h.PortfolioID="+ID);
				//holdingDates=holdingManager.findBySQL("select h.date from "+Configuration.TABLE_PORTFOLIO_HOLDINGS+" h where h.PortfolioID="+ID);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "success";
	}

	public Portfolio getPortfolio() {
		return portfolio;
	}

	public void setPortfolio(Portfolio portfolio) {
		this.portfolio = portfolio;
	}

	public List<Date> getHoldingDates() {
		return holdingDates;
	}

	public void setHoldingDates(List<Date> holdingDates) {
		this.holdingDates = holdingDates;
	}
}
