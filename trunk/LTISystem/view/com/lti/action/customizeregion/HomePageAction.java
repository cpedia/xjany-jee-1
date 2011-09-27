package com.lti.action.customizeregion;

import com.lti.action.Action;
import com.lti.service.CustomizePageManager;
import com.lti.service.CustomizeRegionManager;
import com.lti.service.GroupManager;
import com.lti.service.UserManager;
import com.lti.service.bo.CustomizePage;
import com.lti.service.bo.CustomizeRegion;
import com.lti.service.bo.Group;
import com.opensymphony.xwork2.ActionSupport;

public class HomePageAction extends ActionSupport implements Action {
	private static final long serialVersionUID = 1L;
	
	private String eastSideContent;
	
	private String westSideContent;
	
	private String southSideContent;
	
	private String centerTitle;
	
	private String eastTitle;
	
	private String southTitle;
	
	private String westTitle;
	
	private UserManager userManager;
	
	private GroupManager groupManager;
	
	private CustomizeRegionManager customizeRegionManager;
	
	private CustomizePageManager customizePageManager;
	
	public String getEastSideContent() {
		return eastSideContent;
	}

	public void setEastSideContent(String eastSideContent) {
		this.eastSideContent = eastSideContent;
	}

	public String getWestSideContent() {
		return westSideContent;
	}

	public void setWestSideContent(String westSideContent) {
		this.westSideContent = westSideContent;
	}

	public String getSouthSideContent() {
		return southSideContent;
	}

	public void setSouthSideContent(String southSideContent) {
		this.southSideContent = southSideContent;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public void setGroupManager(GroupManager groupManager) {
		this.groupManager = groupManager;
	}

	public void setCustomizeRegionManager(
			CustomizeRegionManager customizeRegionManager) {
		this.customizeRegionManager = customizeRegionManager;
	}

	public void setCustomizePageManager(CustomizePageManager customizePageManager) {
		this.customizePageManager = customizePageManager;
	}
	
	private Boolean isAuthorized(Long authorizeGroupID, Object[] groupIDs){
		if(authorizeGroupID == null)
			return true;
		for(int i = 0; i < groupIDs.length; i++){
			Long groupID =(Long) groupIDs[i];
			if(authorizeGroupID.equals(groupID))
				return true;
		}
		return false;
	}
	
	private void setRegionContents(CustomizeRegion cr, Object[] groupIDs){
		if(cr.getEast()!=null && isAuthorized(cr.getEastGroupID(), groupIDs) == true){
			eastSideContent = customizePageManager.get(cr.getEast()).getName();
		}
		else
			eastSideContent = null;
		if(cr.getWest()!=null && isAuthorized(cr.getWestGroupID(), groupIDs) == true){
			westSideContent = customizePageManager.get(cr.getWest()).getName();
		}
		else
			westSideContent = null;
		if(cr.getSouth()!=null && isAuthorized(cr.getSouthGroupID(), groupIDs) == true){
			southSideContent = customizePageManager.get(cr.getSouth()).getName();
		}
		else
			southSideContent = null;
	}
	
	private void setRegionTitles(CustomizeRegion cr){
		if(cr.getCenterTitle() != null){
			centerTitle = cr.getCenterTitle();
		}
		else
			centerTitle = "center";
		if(cr.getEastTitle() != null){
			eastTitle = cr.getEastTitle();
		}
		else
			eastTitle = "east";
		if(cr.getSouthTitle() != null){
			southTitle = cr.getSouthTitle();
		}
		else
			southTitle = "south";
		if(cr.getWestTitle() != null){
			westTitle = cr.getWestTitle();
		}
		else
			westTitle = "west";
	}
	
	public String execute() throws Exception{
		Object[] GroupIDs;
		GroupIDs = groupManager.getGroupIDs(userManager.getLoginUser().getID());
		CustomizeRegion cr = customizeRegionManager.get("Home Page");
		if(cr != null){
			setRegionContents(cr, GroupIDs);
			setRegionTitles(cr);
		}
		return Action.SUCCESS;
	}

	public String getCenterTitle() {
		return centerTitle;
	}

	public void setCenterTitle(String centerTitle) {
		this.centerTitle = centerTitle;
	}

	public String getEastTitle() {
		return eastTitle;
	}

	public void setEastTitle(String eastTitle) {
		this.eastTitle = eastTitle;
	}

	public String getSouthTitle() {
		return southTitle;
	}

	public void setSouthTitle(String southTitle) {
		this.southTitle = southTitle;
	}

	public String getWestTitle() {
		return westTitle;
	}

	public void setWestTitle(String westTitle) {
		this.westTitle = westTitle;
	}
}
