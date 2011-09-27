package com.lti.service.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.lti.service.bo.base.BasePortfolio;
import com.lti.system.Configuration;
import com.lti.type.PSHoldingBean;
import com.lti.type.executor.StrategyInf;

@SuppressWarnings("unchecked")
public class Portfolio extends BasePortfolio implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;
	@Deprecated
	public boolean holdSecurity(Long securityID) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public Portfolio(){
		super();
	}
	public Portfolio(Long id,String name,Long userID,Long type,Integer state,Date enddate,Date cr){
		this(id, name, type, state, enddate);
		this.CreatedDate=cr;
		this.UserID=userID;
	}
	public Portfolio(Long id,String name,Long type,Integer state,Date enddate){
		this.ID=id;
		this.Name=name;
		this.Type=type;
		this.State=state;
		this.EndDate=enddate;
	}
	
	public Portfolio(Long id,String name,Long type,Integer state,Date enddate ,StrategyInf Strategies){
		this.ID=id;
		this.Name=name;
		this.Type=type;
		this.State=state;
		this.EndDate=enddate;
		this.Strategies = Strategies;
	}
	
	public String getSymbol() {
		return "P_"+this.ID;
	}
	@Override
	public Portfolio clone() throws CloneNotSupportedException {
		Portfolio p = (Portfolio) super.clone();
		p.setOriginalPortfolioID(null);
		p.setUserID(null);
		p.setUserName(null);
		p.setEndDate(null);
		p.setLastTransactionDate(null);
		p.setDelayLastTransactionDate(null);
		p.setID(null);
		return p;
	}
	
	private List<PSHoldingBean> psHoldings;
	private double costBasis;
	private double mktValue;
	private double avgCostBasis;
	private double cash;

	private String mainStrategyName;
	
	public String getMainStrategyName() {
		return mainStrategyName;
	}

	public void setMainStrategyName(String mainStrategyName) {
		this.mainStrategyName = mainStrategyName;
	}

	public List<PSHoldingBean> getPsHoldings() {
		return psHoldings;
	}

	public void setPsHoldings(List<PSHoldingBean> psHoldings) {
		this.psHoldings = psHoldings;
	}
	
	public double getCostBasis() {
		return this.costBasis;
	}

	public void setCostBasis(double costBasis) {
		this.costBasis = costBasis;
	}

	public double getMktValue() {
		return this.mktValue;
	}

	public void setMktValue(double mktValue) {
		this.mktValue = mktValue;
	}

	public double getCash() {
		return this.cash;
	}

	public void setCash(double cash) {
		this.cash = cash;
	}

	public double getAvgCostBasis() {
		return avgCostBasis;
	}

	public void setAvgCostBasis(double avgCostBasis) {
		this.avgCostBasis = avgCostBasis;
	}
	
	public boolean isPlanPortfolio(){
		try{
			Long.parseLong(this.getStrategies().getAssetAllocationStrategy().getParameter().get("PlanID"));
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	public Long getPlanID(){
		try{
			Long planID = Long.parseLong(this.getStrategies().getAssetAllocationStrategy().getParameter().get("PlanID"));
			return planID;
		}catch(Exception e){
			return null;
		}
	}
	
	public boolean isTAAPortfolio(){
		try {
			if(this.getStrategies().getAssetAllocationStrategy().getID().longValue() == Configuration.STRATEGY_TAA_ID)
				return true;
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return false;
	}
	
	public boolean isSAAPortfolio(){
		try {
			if(this.getStrategies().getAssetAllocationStrategy().getID().longValue() == Configuration.STRATEGY_SAA_ID)
				return true;
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return false;
	}
}