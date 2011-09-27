package com.lti.action.admin.group.role;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.action.Action;
import com.lti.service.GroupManager;
import com.lti.service.bo.Role;
import com.opensymphony.xwork2.ActionSupport;

public class SaveAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(SaveAction.class);
	
	
	private String action;
	
	private Long ID;
	
	private String name;
	
	private String title;
	
	private GroupManager groupManager;
	
	public void setGroupManager(GroupManager groupManager) {
		this.groupManager = groupManager;
	}

	public void validate(){
		
		if(this.action==null||this.action.equals(""))this.action=ACTION_CREATE;
		
		if(this.action.equalsIgnoreCase(ACTION_SAVE)){
			if(name==null||name.equals("")){
				addFieldError("name","Name is not validate!");
				return;
			}
		}
		
		else if(this.action.equalsIgnoreCase(ACTION_UPDATE)){
			
			if(ID==null){
				addFieldError("ID","ID is not validate!");
			}else{
				Role r = groupManager.getRole(ID);
				
				if(r == null){
					addFieldError("ID","ID is not validate!");
					return;
				}
			}
			
			if(name==null||name.equals("")){
				addFieldError("name","Name is not validate!");
				return;
			}
			
		}
		
		else if(this.action.equalsIgnoreCase(ACTION_DELETE)){
			
			if(ID==null){
				addFieldError("ID","ID is not validate!");
			}else{
				Role r = groupManager.getRole(ID);
				
				if(r == null){
					addFieldError("ID","ID is not validate!");
					return;
				}
			}
			
			
			
		}
		
		else if(this.action.equalsIgnoreCase(ACTION_VIEW)){
			
			if(ID==null){
				addFieldError("ID","ID is not validate!");
			}else{
				Role r = groupManager.getRole(ID);
				
				if(r == null){
					addFieldError("ID","ID is not validate!");
					return;
				}
			}
			
			
			
		}else if(this.action.equalsIgnoreCase(ACTION_CREATE)){
			
		}
		
		else{
			
			addFieldError("action","Request is not validate!");
			
		}
		
	}

	@Override
	public String execute() throws Exception {
		
		Role r = new Role();
		
		r.setID(ID);
		
		r.setName(name);
		
		if(this.action.equalsIgnoreCase(ACTION_SAVE)){
			
			r.setID(null);
			
			groupManager.addRole(r);
			
			ID = groupManager.getRoleByName(name).getID();
			
			title="Role: "+name;
			
			action = ACTION_UPDATE;
			
			addActionMessage("Create Successfully!"); 
			
			return Action.INPUT;
			
		}
		
		else if(this.action.equalsIgnoreCase(ACTION_UPDATE)){
			
			groupManager.updateRole(r);
			
			action=ACTION_UPDATE;
			
			title="Role: "+name;
			
			addActionMessage("Update Successfully!"); 
			
			return Action.INPUT;
			
		}
		
		else if(this.action.equalsIgnoreCase(ACTION_DELETE)){
			
			groupManager.deleteRole(ID);
			
			groupManager.deleteGroupRoleByRole(ID);
			
			action = ACTION_CREATE;
			
			addActionMessage("Delete Successfully!"); 
			
			return Action.SUCCESS;
			
		}
		
		else if(this.action.equalsIgnoreCase(ACTION_VIEW)){
			
			r = groupManager.getRole(ID);
			
			ID = r.getID();
			
			name = r.getName();
					
			title="Role: "+name;
			
			action=ACTION_UPDATE;
			
			return Action.INPUT;
			
		}else if(this.action.equalsIgnoreCase(ACTION_CREATE)){
			
			action=ACTION_SAVE;
			
			title="Action Group";
			
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

	
	public String getName() {
	
		return name;
	}

	
	public void setName(String name) {
	
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
