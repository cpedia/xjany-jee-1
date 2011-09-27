package com.lti.action.admin.customizeregion;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.action.Action;
import com.lti.service.CustomizePageManager;
import com.lti.service.CustomizeRegionManager;
import com.lti.service.bo.CustomizeRegion;
import com.lti.util.CustomizeUtil;
import com.opensymphony.xwork2.ActionSupport;

import java.util.List;
import java.util.ArrayList;

public class MainAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(MainAction.class);
	
	private List<CustomizeRegion> crList;
	
	private CustomizeRegionManager customizeRegionManager;
	
	private CustomizePageManager customizePageManager;


	public List<CustomizeRegion> getCrList() {
		return crList;
	}

	public void setCrList(List<CustomizeRegion> crList) {
		this.crList = crList;
	}
	
	public void setCustomizePageManager(CustomizePageManager customizePageManager) {
		this.customizePageManager = customizePageManager;
	}
	
	public String execute() throws Exception{
		crList = customizeRegionManager.getRegionCustomizes();
		if(crList == null)
			return Action.SUCCESS;
		for(int i = 0; i < crList.size(); i++){
			CustomizeRegion cr =  crList.get(i);
			if(cr.getID()==0){
				crList.remove(i);
				i--;
				continue;
			}
			//set every region's content name
			CustomizeUtil.setRegionContentName(cr);
		}
		return Action.SUCCESS;
	}

	public void setCustomizeRegionManager(
			CustomizeRegionManager customizeRegionManager) {
		this.customizeRegionManager = customizeRegionManager;
	}
	
	
}
