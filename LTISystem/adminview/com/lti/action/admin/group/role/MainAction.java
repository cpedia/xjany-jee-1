package com.lti.action.admin.group.role;


import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.action.Action;
import com.lti.service.GroupManager;
import com.lti.service.bo.Role;
import com.opensymphony.xwork2.ActionSupport;

public class MainAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(MainAction.class);
	
	private List<Role> roles;

	
	private GroupManager groupManager;
	
	
	public void setGroupManager(GroupManager groupManager) {
		this.groupManager = groupManager;
	}

	public void validate(){
		
		
	}

	@Override
	public String execute() throws Exception {
		
		roles = groupManager.getAllRoles();
		
		return Action.SUCCESS;

	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}	

}
