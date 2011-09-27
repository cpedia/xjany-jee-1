/**
 * 
 */
package com.lti.action.strategy;

import java.util.ArrayList;
import java.util.List;

import com.lti.action.Action;
import com.lti.service.StrategyManager;
import com.lti.service.bo.PlanScore;
import com.lti.system.ContextHolder;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author ccd
 *
 */
public class GetPlanScoreTableAction extends ActionSupport implements Action {
	
	private static final long serialVersionUID = 1L;
	
	private StrategyManager strategyManager;
	
	private String urlPrefix;
	
	private String ids;
	
	private Boolean includeSAAReturn = false;
	
	private Boolean includeTAAReturn = true;
	
	private Boolean includeCoverageScore = false;
	
	private Boolean includeFundQualityScore = false;
	
	private Boolean includeCapabilityScore = false;
	
	private Boolean includeInvestmentScore = true;
	
	private List<PlanScore> planScoreList;
	
	private Integer width;
	
	private String title=null;
	
	@Override
	public String execute(){
		List<Long> planIDList = new ArrayList<Long>();
		String[] planIDs = null;
		if(ids!=null && ! ids.trim().equals("")){
			planIDs = ids.split("\\|");
			for(int i=0;i<planIDs.length;++i)
				planIDList.add(Long.parseLong(planIDs[i]));
			planScoreList = strategyManager.getPlanScoreWithIDs(planIDList);
		}else
			planScoreList=strategyManager.getPlanScore();
		
		return Action.SUCCESS;
	}

	public StrategyManager getStrategyManager() {
		return strategyManager;
	}

	public void setStrategyManager(StrategyManager strategyManager) {
		this.strategyManager = strategyManager;
	}

	public String getUrlPrefix() {
		return urlPrefix;
	}

	public void setUrlPrefix(String urlPrefix) {
		this.urlPrefix = urlPrefix;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public Boolean getIncludeSAAReturn() {
		return includeSAAReturn;
	}

	public void setIncludeSAAReturn(Boolean includeSAAReturn) {
		this.includeSAAReturn = includeSAAReturn;
	}

	public Boolean getIncludeTAAReturn() {
		return includeTAAReturn;
	}

	public void setIncludeTAAReturn(Boolean includeTAAReturn) {
		this.includeTAAReturn = includeTAAReturn;
	}

	public Boolean getIncludeCoverageScore() {
		return includeCoverageScore;
	}

	public void setIncludeCoverageScore(Boolean includeCoverageScore) {
		this.includeCoverageScore = includeCoverageScore;
	}

	public Boolean getIncludeFundQualityScore() {
		return includeFundQualityScore;
	}

	public void setIncludeFundQualityScore(Boolean includeFundQualityScore) {
		this.includeFundQualityScore = includeFundQualityScore;
	}

	public Boolean getIncludeCapabilityScore() {
		return includeCapabilityScore;
	}

	public void setIncludeCapabilityScore(Boolean includeCapabilityScore) {
		this.includeCapabilityScore = includeCapabilityScore;
	}

	public Boolean getIncludeInvestmentScore() {
		return includeInvestmentScore;
	}

	public void setIncludeInvestmentScore(Boolean includeInvestmentScore) {
		this.includeInvestmentScore = includeInvestmentScore;
	}

	public List<PlanScore> getPlanScoreList() {
		return planScoreList;
	}

	public void setPlanScoreList(List<PlanScore> planScoreList) {
		this.planScoreList = planScoreList;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
