package com.lti.action.admin.assetclass;

import com.lti.action.Action;
import com.lti.service.AssetClassManager;
import com.lti.system.ContextHolder;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author CCD
 * last modified by CCD on 2010-01-19
 */
public class ModifyAssetClassAction extends ActionSupport implements Action {
	
	private static final long serialVersionUID = 1L;
	private Long originalID;
	private Long targetID;
	private String resultString;
	private String action;
	
	@Override
	public String execute() throws Exception{
		if(action==null)
			return Action.INPUT;
		if(action.equalsIgnoreCase("Merge"))
			return merge();
		else if(action.equalsIgnoreCase("Catalogue"))
			return catalogue();
		resultString = "fail";
		return Action.ERROR;			
	}
	public String merge() throws Exception {
		AssetClassManager assetClassManager = (AssetClassManager) ContextHolder.getInstance().getApplicationContext().getBean("assetClassManager");
		try{
			if(originalID==null || targetID==null){
				return Action.INPUT;
			}
			assetClassManager.mergeAssetClass(originalID, targetID);
			resultString = "Merge successful";
		}catch(Exception e){
			resultString ="Fail,Please check the IDs of Original Asset Class and the Target Asset Class";
			e.printStackTrace();
		}
		return Action.SUCCESS;
	}
	
	public String catalogue(){
		AssetClassManager assetClassManager = (AssetClassManager) ContextHolder.getInstance().getApplicationContext().getBean("assetClassManager");
		try{
			if(originalID==null || targetID==null){
				return Action.INPUT;
			}
			assetClassManager.catalogueAssetClass(originalID, targetID);
			resultString = "Catalogue successful";
		}catch(Exception e){
			resultString ="Fail,Please check the IDs of Original Asset Class and the Target Asset Class";
			e.printStackTrace();
		}
		return Action.SUCCESS;
	}
	
	public Long getOriginalID() {
		return originalID;
	}
	public void setOriginalID(Long originalID) {
		this.originalID = originalID;
	}
	public Long getTargetID() {
		return targetID;
	}
	public void setTargetID(Long targetID) {
		this.targetID = targetID;
	}
	public String getResultString() {
		return resultString;
	}
	public void setResultString(String resultString) {
		this.resultString = resultString;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
}
