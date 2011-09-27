package com.lti.action.admin.group.portfolio;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.action.Action;
import com.lti.service.GroupManager;
import com.lti.service.PortfolioManager;
import com.lti.service.bo.Group;
import com.lti.service.bo.GroupRole;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.Role;
import com.lti.system.Configuration;
import com.opensymphony.xwork2.ActionSupport;

public class SaveAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(SaveAction.class);

	private String title;

	private String action;

	private Long ID;

	private Long groupID;
	
	private Long roleID;

	private Long portfolioID;
	
	private String resourceType;
	
	private Integer resourceName;
	
	private String resourceString;

	private Integer permission;

	private GroupManager groupManager;

	private PortfolioManager portfolioManager;

	public Long getGroupID() {
		return groupID;
	}

	public void setGroupID(Long groupID) {
		this.groupID = groupID;
	}

	public GroupManager getGroupManager() {
		return groupManager;
	}

	public PortfolioManager getPortfolioManager() {
		return portfolioManager;
	}

	public void setPortfolioManager(PortfolioManager portfolioManager) {
		this.portfolioManager = portfolioManager;
	}

	public Long getPortfolioID() {
		return portfolioID;
	}

	public void setPortfolioID(Long portfolioID) {
		this.portfolioID = portfolioID;
	}

	public Integer getPermission() {
		return permission;
	}

	public void setPermission(Integer permission) {
		this.permission = permission;
	}

	public void setGroupManager(GroupManager groupManager) {
		this.groupManager = groupManager;
	}

	public void validate() {

		if (this.action == null || this.action.equals(""))
			this.action = ACTION_CREATE;
		if (this.action.equalsIgnoreCase(ACTION_SAVE)) {
			Group g;
			Role r;
			Portfolio p;
			if (groupID == null) {
				addFieldError("groupID", "Group ID is not validate!");
				return;
			} else {
				g = groupManager.get(groupID);
				if (g == null) {
					addFieldError("groupID", "Group ID is not validate!");
					return;
				}
			}
			if (roleID == null) {
				addFieldError("roleID", "Role ID is not validate!");
				return;
			} else {
				r = groupManager.getRole(roleID);
				if (r == null) {
					addFieldError("roleID", "Role ID is not validate!");
					return;
				}
			}
			if (portfolioID == null) {
				addFieldError("portfolioID", "Portfolio ID is not validate!");
				return;
			} else {
				p = portfolioManager.get(portfolioID);
				if (p == null) {
					addFieldError("portfolioID", "Portfolio ID is not validate!");
					return;
				}
			}

			title = "Group :" + g.getName() + "==> Portfolio : " + p.getName();
		}
		else if (this.action.equalsIgnoreCase(ACTION_DELETE)) {

			if (groupID == null ||roleID==null|| portfolioID == null) {
				addFieldError("ID", "ID is not validate!");
				return;
			}
		} else if (this.action.equalsIgnoreCase(ACTION_CREATE)) {
			if (groupID == null) {
				addFieldError("groupID", "groupID is not validate!");
				return;
			} else {
				Group g = groupManager.get(groupID);
				if (g == null) {
					addFieldError("groupID", "groupID is not validate!");
					return;
				}
				title = "Group :" + g.getName();
			}
		}
		else {
			addFieldError("action", "Request is not validate!");
		}
	}

	@Override
	public String execute() throws Exception {

		if (this.action.equalsIgnoreCase(ACTION_SAVE)) {
			
			GroupRole gr = new GroupRole();
			gr.setGroupID(groupID);
			gr.setRoleID(roleID);
			gr.setResourceID(portfolioID);
			gr.setResourceType(Configuration.RESOURCE_TYPE_PORTFOLIO);
			groupManager.addGroupRole(gr);
			addActionMessage("Add Successfully!");
			return Action.SUCCESS;
		} else if (this.action.equalsIgnoreCase(ACTION_DELETE)) {
			//groupManager.deleteResource(groupID,portfolioID);
			groupManager.deleteResource(groupID,roleID, portfolioID);
			action = ACTION_CREATE;
			addActionMessage("Remove Successfully!");
			return Action.SUCCESS;
		} else if (this.action.equalsIgnoreCase(ACTION_CREATE)) {
			action = ACTION_SAVE;
			return Action.INPUT;
		}
		return Action.ERROR;
	}

	public String getAction() {

		return action;
	}

	public void setAction(String action) {

		this.action = action;
	}

	public Long getID() {

		return ID;
	}

	public void setID(Long id) {

		ID = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Long getRoleID() {
		return roleID;
	}

	public void setRoleID(Long roleID) {
		this.roleID = roleID;
	}

	public String getResourceString() {
		return resourceString;
	}

	public void setResourceString(String resourceString) {
		this.resourceString = resourceString;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public void setResourceName(Integer resourceName) {
		this.resourceName = resourceName;
	}

}
