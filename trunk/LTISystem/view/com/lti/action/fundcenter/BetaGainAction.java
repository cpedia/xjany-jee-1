package com.lti.action.fundcenter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.lti.action.Action;
import com.lti.util.LTIDate;
import com.lti.util.LTIFactorManager;
import com.lti.util.LTIFactorManager.Factor;
import com.opensymphony.xwork2.ActionSupport;

public class BetaGainAction extends ActionSupport implements Action {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Boolean IsBetaGain;
	
	private Boolean IsResult;
	
	private String symbol;
	
	private List<String> index;
	
	private List<Integer> types;
	
	private String startDate;
	
	private String endDate;
	
	private Integer interval;

	@Override
	public void validate() {
		// TODO Auto-generated method stub
		super.validate();
		if(symbol == null){
			addActionError("No symbol was set! Use the default setting");
			IsResult = false; //not set the IsRAA to use the default setting;
			return;
		}
	}

	

	@Override
	public String execute() throws Exception {
		// TODO Auto-generated method stub
		IsBetaGain = true;
		IsResult = false;
		Date today = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		endDate = sdf.format(today);
		Date lastYear = LTIDate.getLastYear(today);
		startDate = sdf.format(lastYear);
		/*set indexes and their default value*/
		LTIFactorManager factorManager = LTIFactorManager.getInstance("RAA");
		List<Factor> factors = factorManager.getFactorsForSecurity(symbol);
		interval = 64;
		if(factors != null && factors.size() > 0){
			index = new ArrayList<String>();
			types = new ArrayList<Integer>();
			for(int i = 0; i < factors.size(); i++){
				index.add(factors.get(i).getName());
				types.add(1);
			}
		}
		return Action.SUCCESS;
	}

	public Boolean getIsBetaGain() {
		return IsBetaGain;
	}

	public void setIsBetaGain(Boolean isBetaGain) {
		IsBetaGain = isBetaGain;
	}

	public Boolean getIsResult() {
		return IsResult;
	}

	public void setIsResult(Boolean isResult) {
		IsResult = isResult;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	

	public List<String> getIndex() {
		return index;
	}

	public void setIndex(List<String> index) {
		this.index = index;
	}

	public List<Integer> getTypes() {
		return types;
	}

	public void setTypes(List<Integer> types) {
		this.types = types;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public Integer getInterval() {
		return interval;
	}

	public void setInterval(Integer interval) {
		this.interval = interval;
	}
	
	

}
