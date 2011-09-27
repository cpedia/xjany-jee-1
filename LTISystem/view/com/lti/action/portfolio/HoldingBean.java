package com.lti.action.portfolio;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lti.action.selectaction;
import com.lti.service.bo.AssetClass;
import com.lti.service.bo.HoldingItem;
import com.lti.service.bo.Transaction;
import com.lti.type.executor.StrategyBasicInf;
import com.lti.type.executor.StrategyInf;
import com.lti.type.finance.Asset;
import com.lti.type.finance.HoldingInf;
import com.lti.util.StringUtil;

public class HoldingBean {
	protected double Cash = 0.0;
	protected String BenchmarkSymbol;
	protected Date StartingDate;
	protected String AssetAllocationStrategy;
	protected String CashFlowStrategy;
	protected String RebalancingStrategy;
	protected Map<String, String> AssetAllocationStrategyParameter=new HashMap<String, String>();
	protected Map<String, String> CashFlowStrategyParameter;
	protected Map<String, String> RebalancingStrategyParameter;
	protected String AssetAllocationStrategyParameterJSON;
	
	protected List<Transaction> PersonalTransactions;
	

	public List<Transaction> getPersonalTransactions() {
		return PersonalTransactions;
	}

	public void setPersonalTransactions(List<Transaction> personalTransactions) {
		PersonalTransactions = personalTransactions;
	}

	public Date getStartingDate() {
		return StartingDate;
	}

	public void setStartingDate(Date startingDate) {
		StartingDate = startingDate;
	}


	protected String CashFlowStrategyParameterJSON;
	protected String RebalancingStrategyParameterJSON;
	protected List<AssetBean> Assets;

	public String toJSON() {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("\"root\":[");
		Map<String, String> settings = new HashMap<String, String>();
		settings.put("name", "name");
		settings.put("targetpercentage", "targetPercentage");
		settings.put("assetclassname", "assetClassName");
		settings.put("holdingitems", "leaf");
		settings.put("symbol", "symbol");
		settings.put("share", "share");
		settings.put("reinvest", "reInvest");
		settings.put("price", "price");
		settings.put("amount", "amount");
		settings.put("assetstrategy", "assetStrategy");
		//settings.put("assetstrategyparameter", "assetStrategyParameter");
		for (int i = 0; i < Assets.size(); i++) {
			sb.append(StringUtil.toJSON(Assets.get(i), settings));
			if (i != (Assets.size() - 1)) {
				sb.append(",");
			}
		}
		sb.append("]");
		sb.append("}");
		return sb.toString();
	}

	public void init(HoldingInf hi, StrategyInf si) {
		this.setCash(hi.getCash());
		this.setBenchmarkSymbol(hi.getBenchmarkSymbol());

		this.setAssetAllocationStrategy(si.getAssetAllocationStrategy().getName());
		this.setAssetAllocationStrategyParameter(si.getAssetAllocationStrategy().getParameter());
		this.setAssetAllocationStrategyParameterJSON(StringUtil.toJSON(si.getAssetAllocationStrategy().getParameter()));

		this.setCashFlowStrategy(si.getCashFlowStrategy().getName());
		this.setCashFlowStrategyParameter(si.getCashFlowStrategy().getParameter());
		this.setCashFlowStrategyParameterJSON(StringUtil.toJSON(si.getCashFlowStrategy().getParameter()));

		this.setRebalancingStrategy(si.getRebalancingStrategy().getName());
		this.setRebalancingStrategyParameter(si.getRebalancingStrategy().getParameter());
		this.setRebalancingStrategyParameterJSON(StringUtil.toJSON(si.getRebalancingStrategy().getParameter()));

		this.Assets = new ArrayList<AssetBean>();
		selectaction.buildAssetClasses();
		for (Asset asset : hi.getAssets()) {
			AssetBean ab = new AssetBean();
			// TODO:
			AssetClass ac=selectaction.AssetClasses.get(asset.getAssetClassID());
			if(ac==null){
				ab.setAssetClassName("0. Root");
			}else{
				ab.setAssetClassName(ac.getID()+". "+ac.getFullName());
			}
			ab.setName(asset.getName());
			ab.setTargetPercentage(asset.getTargetPercentage());

			StrategyBasicInf sbi = si.getAssetStrategy(asset.getName());
			if (sbi != null) {
				ab.setAssetStrategy(sbi.getName());
				ab.setAssetStrategyParameterJSON(StringUtil.toJSON(sbi.getParameter()));
				ab.setAssetStrategyParameter(sbi.getParameter());
			} else {
				ab.setAssetStrategy("STATIC");
				ab.setAssetStrategyParameterJSON("");
			}

			ab.setHoldingItems(new ArrayList<HoldingItemBean>());
			for (HoldingItem holdingItem : asset.getHoldingItems()) {
				HoldingItemBean hib = new HoldingItemBean();
				hib.setReInvest(holdingItem.getReInvest());
				if(holdingItem.getShare()==null){
					holdingItem.setShare(0.0);
				} 
				if(holdingItem.getPrice()==null){
					holdingItem.setPrice(0.0);
				}
				hib.setShare(holdingItem.getShare());
			
				hib.setSymbol(holdingItem.getSymbol());
				hib.setPrice(holdingItem.getPrice());
			
				double amount=holdingItem.getPrice()*holdingItem.getShare();
				int _a=(int)amount;
				if(amount-_a>0.9999){
					amount=_a+1;
				}
				
				hib.setAmount(amount);
				ab.getHoldingItems().add(hib);
			}
			this.Assets.add(ab);
		}
		this.PersonalTransactions=hi.getPersonalTransactions();
	}

	public double getCash() {
		return Cash;
	}

	public void setCash(double cash) {
		Cash = cash;
	}

	public String getBenchmarkSymbol() {
		return BenchmarkSymbol;
	}

	public void setBenchmarkSymbol(String benchmarkSymbol) {
		BenchmarkSymbol = benchmarkSymbol;
	}

	public String getAssetAllocationStrategy() {
		return AssetAllocationStrategy;
	}

	public void setAssetAllocationStrategy(String assetAllocationStrategy) {
		AssetAllocationStrategy = assetAllocationStrategy;
	}

	public String getCashFlowStrategy() {
		return CashFlowStrategy;
	}

	public void setCashFlowStrategy(String cashFlowStrategy) {
		CashFlowStrategy = cashFlowStrategy;
	}

	public String getRebalancingStrategy() {
		return RebalancingStrategy;
	}

	public void setRebalancingStrategy(String rebalancingStrategy) {
		RebalancingStrategy = rebalancingStrategy;
	}

	public List<AssetBean> getAssets() {
		return Assets;
	}

	public void setAssets(List<AssetBean> assets) {
		Assets = assets;
	}

	public Map<String, String> getAssetAllocationStrategyParameter() {
		return AssetAllocationStrategyParameter;
	}

	public void setAssetAllocationStrategyParameter(Map<String, String> assetAllocationStrategyParameter) {
		AssetAllocationStrategyParameter = assetAllocationStrategyParameter;
	}

	public Map<String, String> getCashFlowStrategyParameter() {
		return CashFlowStrategyParameter;
	}

	public void setCashFlowStrategyParameter(Map<String, String> cashFlowStrategyParameter) {
		CashFlowStrategyParameter = cashFlowStrategyParameter;
	}

	public Map<String, String> getRebalancingStrategyParameter() {
		return RebalancingStrategyParameter;
	}

	public void setRebalancingStrategyParameter(Map<String, String> rebalancingStrategyParameter) {
		RebalancingStrategyParameter = rebalancingStrategyParameter;
	}

	public String getAssetAllocationStrategyParameterJSON() {
		return AssetAllocationStrategyParameterJSON;
	}

	public void setAssetAllocationStrategyParameterJSON(String assetAllocationStrategyParameterJSON) {
		AssetAllocationStrategyParameterJSON = assetAllocationStrategyParameterJSON;
	}

	public String getCashFlowStrategyParameterJSON() {
		return CashFlowStrategyParameterJSON;
	}

	public void setCashFlowStrategyParameterJSON(String cashFlowStrategyParameterJSON) {
		CashFlowStrategyParameterJSON = cashFlowStrategyParameterJSON;
	}

	public String getRebalancingStrategyParameterJSON() {
		return RebalancingStrategyParameterJSON;
	}

	public void setRebalancingStrategyParameterJSON(String rebalancingStrategyParameterJSON) {
		RebalancingStrategyParameterJSON = rebalancingStrategyParameterJSON;
	}

}