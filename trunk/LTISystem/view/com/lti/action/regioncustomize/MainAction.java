package com.lti.action.regioncustomize;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.service.RegionCustomizeManager;
import com.lti.service.UserManager;
import com.lti.service.GroupManager;
import com.lti.service.bo.RegionCustomize;
import com.lti.action.*;
import com.opensymphony.xwork2.ActionSupport;

public class MainAction extends ActionSupport implements Action{
	
	private static final long serialVersionUID = 1L;
	
	static Log log = LogFactory.getLog(MainAction.class);
	
	private RegionCustomizeManager regionCustomizeManager;
	private UserManager userManager;
	private GroupManager groupManager;
	private String content;
	private String pageName;
	private String regionName;
	
	public void validate(){
		
	}

	@Override
	public String execute() throws Exception {
		// TODO Auto-generated method stub
		RegionCustomize regionCustomize;
		content=null;
		Object[] GroupIDs;
		GroupIDs=groupManager.getGroupIDs(userManager.getLoginUser().getID());
		for(int i=0;i<GroupIDs.length;i++)
		{
			regionCustomize=regionCustomizeManager.get(pageName, regionName,(Long)GroupIDs[i]);
			if(regionCustomize!=null){
				content=regionCustomize.getRegionContent();
				break;
			}
		}
		return Action.SUCCESS;
	}

	public RegionCustomizeManager getRegionCustomizeManager() {
		return regionCustomizeManager;
	}

	public void setRegionCustomizeManager(
			RegionCustomizeManager regionCustomizeManager) {
		this.regionCustomizeManager = regionCustomizeManager;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public UserManager getUserManager() {
		return userManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public GroupManager getGroupManager() {
		return groupManager;
	}

	public void setGroupManager(GroupManager groupManager) {
		this.groupManager = groupManager;
	}


	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

}