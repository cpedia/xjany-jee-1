package com.lti.action.admin.group.user;


import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.action.Action;
import com.lti.service.GroupManager;
import com.lti.service.UserManager;
import com.lti.service.bo.Group;
import com.lti.service.bo.User;
import com.opensymphony.xwork2.ActionSupport;

public class SaveAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(SaveAction.class);
	
	private String title;
	
	private String action;
	
	private Long ID;
	
	private Long groupID;
	
	private Long userID;
	
	private Integer permission;
	
	private GroupManager groupManager;
	
	private UserManager userManager;
	
	
	public Long getGroupID() {
		return groupID;
	}

	public void setGroupID(Long groupID) {
		this.groupID = groupID;
	}

	public Long getUserID() {
		return userID;
	}

	public void setUserID(Long userID) {
		this.userID = userID;
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

	public void validate(){
		
		if(this.action==null||this.action.equals(""))this.action=ACTION_CREATE;
		
		if(this.action.equalsIgnoreCase(ACTION_SAVE)){
			Group g;
			User p;
			
			if(groupID==null){
				addFieldError("groupID","Group ID is not validate!");
				return;
			}
			else{
				g=groupManager.get(groupID);
				
				if(g==null){
					addFieldError("groupID","Group ID is not validate!");
					return;
				}
			}
			
			if(userID==null){
				addFieldError("userID","User ID is not validate!");
				return;
			}
			else{
				p=userManager.get(userID);
				
				if(p==null){
					addFieldError("userID","User ID is not validate!");
					return;
				}
			}
			
			List<Group> groups=groupManager.getUserGroups(userID);
			
			if(groups!=null){
				for(int i=0;i<groups.size();i++){
						if(groups.get(i).getID().equals(groupID)){
						addFieldError("groupID","User has already in the group!");
						return;
					}
				}

			}
			
			title="Group :"+g.getName()+"==> User : "+p.getUserName();
		}
		
		else if(this.action.equalsIgnoreCase(ACTION_DELETE)){
			
			
			if(groupID==null||userID==null){
				addFieldError("ID","ID is not validate!");
				return;
			}
			
			
			
		}else if(this.action.equalsIgnoreCase(ACTION_CREATE)){
			if(groupID==null){
				addFieldError("groupID","groupID is not validate!");
				return;
			}else{
				Group g=groupManager.get(groupID);
				if(g==null){
					addFieldError("groupID","groupID is not validate!");
					return;
				}
				title="Group :"+g.getName();
			}			
		}
		
		else{
			
			addFieldError("action","Request is not validate!");
			
		}
		
	}

	@Override
	public String execute() throws Exception {
		
		
		if(this.action.equalsIgnoreCase(ACTION_SAVE)){
			
			groupManager.addGroupUser(groupID, userID);
			
			addActionMessage("Add Successfully!"); 
			
			return Action.SUCCESS;
			
		}
			
		else if(this.action.equalsIgnoreCase(ACTION_DELETE)){
			
			groupManager.deleteByHQL("from GroupUser gp where gp.GroupID="+groupID+" and gp.UserID="+userID);
			
			action=ACTION_CREATE;
			
			addActionMessage("Remove Successfully!"); 
			
			return Action.SUCCESS;
			
		}
		
		else if(this.action.equalsIgnoreCase(ACTION_CREATE)){
			
			action=ACTION_SAVE;
			
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

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}


	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}


}
