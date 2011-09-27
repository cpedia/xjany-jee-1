package com.lti.action.admin.allocationtemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.lti.action.Action;
import com.lti.service.StrategyManager;
import com.lti.service.bo.AllocationTemplate;
import com.lti.system.ContextHolder;
import com.opensymphony.xwork2.ActionSupport;

public class allocationtemplateAction extends ActionSupport implements Action{
	private static final long serialVersionUID = 1L;
	
	private List<AllocationTemplate> templateList;
	private List<List<String>> assetClassList;
	private List<List<String>> percentageList;
	private String message;
	
	public String execute() throws Exception{
		StrategyManager strategyManager = ContextHolder.getStrategyManager();
		templateList = strategyManager.getAllallocationTemplate();
		if(templateList != null){
			assetClassList = new ArrayList<List<String>>();
			percentageList = new ArrayList<List<String>>();
			for(AllocationTemplate at: templateList){
				String assets = at.getAssetClassName();
				String percentages = at.getAssetClassWeight();
				String[] assetItems = assets.split(",");
				String[] percentageItems = percentages.split(",");
				List<String>assetList =  Arrays.asList(assetItems);
				List<String> percentList = Arrays.asList(percentageItems);
				assetClassList.add(assetList);
				percentageList.add(percentList);
			}
		}
		
		return Action.SUCCESS;
	}

	private String templateName;
	public String deleteTemplate(){
		StrategyManager strategyManager = ContextHolder.getStrategyManager();
		if(templateName != null){
			strategyManager.removeAllocationTemplate(templateName);
		}
		
		return Action.MESSAGE;
	}
	
	public String checkName(){
		StrategyManager strategyManager = ContextHolder.getStrategyManager();
		templateList = strategyManager.getAllallocationTemplate();
		message="";
		if(templateList !=null){
			for(AllocationTemplate at :templateList){
				String name = at.getTemplateName();
				if(name.equalsIgnoreCase(templateName)){
					message = "the template is exist";
					break;
				}
			}
		}
		return Action.MESSAGE;
	}
	
	private String description;
	private String assetClassName;
	private String percentages;
	public String updateTemplate(){
		StrategyManager strategyManager = ContextHolder.getStrategyManager();
		templateList = strategyManager.getAllallocationTemplate();
		boolean exist = false;
		AllocationTemplate curAt = null;
		for(AllocationTemplate at:templateList){
			String name = at.getTemplateName();
			if(name.equalsIgnoreCase(templateName)){
				exist = true;
				curAt = at;
				break;
			}			
		}		
		if(exist){
			AllocationTemplate newAt = new AllocationTemplate();
			long id = curAt.getID();
			newAt.setID(id);
			newAt.setTemplateName(templateName);
			newAt.setAssetClassName(assetClassName);
			newAt.setAssetClassWeight(percentages);
			newAt.setDescription(description);
			strategyManager.updateAllocationTemplate(newAt);
			
		}else{
			AllocationTemplate newAt = new AllocationTemplate();
			newAt.setTemplateName(templateName);
			newAt.setAssetClassName(assetClassName);
			newAt.setAssetClassWeight(percentages);
			newAt.setDescription(description);
			strategyManager.addAllocationTemplate(newAt);
		}
						
		return Action.MESSAGE;
	}
	
	public List<AllocationTemplate> getTemplateList() {
		return templateList;
	}

	public void setTemplateList(List<AllocationTemplate> templateList) {
		this.templateList = templateList;
	}

	public List<List<String>> getAssetClassList() {
		return assetClassList;
	}

	public void setAssetClassList(List<List<String>> assetClassList) {
		this.assetClassList = assetClassList;
	}

	public List<List<String>> getPercentageList() {
		return percentageList;
	}

	public void setPercentageList(List<List<String>> percentageList) {
		this.percentageList = percentageList;
	}
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAssetClassName() {
		return assetClassName;
	}

	public void setAssetClassName(String assetClassName) {
		this.assetClassName = assetClassName;
	}

	public String getPercentages() {
		return percentages;
	}

	public void setPercentages(String percentages) {
		this.percentages = percentages;
	}

	
}
