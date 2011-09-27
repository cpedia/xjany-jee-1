package com.lti.action.ajax;

import com.lti.action.Action;
import com.lti.service.AssetClassManager;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author Michael Cai
 * 1. consider to remove this file
 *
 */
public class GetClassTxtAction extends ActionSupport implements Action  {

	private static final long serialVersionUID = 1L;
	
	AssetClassManager assetClassManager;
	
	private Long id;
	
	private String type;
	
	private String resultString;
	
	public String getResultString() {
		return resultString;
	}

	public void setResultString(String resultString) {
		this.resultString = resultString;
	}

	public void validate(){
		
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	
	
	@Override
	public String execute() throws Exception {
		
 		resultString="";
		//String name=servletRequest.getParameter("name");
		if(type==null||!(type.equals("asset")||type.equals("strateyg")))resultString="Error Request!";
		else {
			@SuppressWarnings("unused")
			long classid;
			try {
				classid=id;
			} catch (RuntimeException e) {
				classid=0;
			}


			if(type.equals("asset")){
				//Stack s=new Stack();
				//List<AssetClass> treeList=assetClassManager.getClasses();
				
			}
			else {
				
			}
			
		}
		
		return Action.SUCCESS;

	}	
	

	public void setAssetClassManager(AssetClassManager assetClassManager) {
		this.assetClassManager = assetClassManager;
	}

}
