package com.lti.action.admin.user;


import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.action.Action;
import com.lti.service.UserManager;
import com.lti.service.bo.Group;
import com.lti.service.bo.User;
import com.lti.system.Configuration;
import com.opensymphony.xwork2.ActionSupport;

public class SaveAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(SaveAction.class);
	
	
	private String action;
	
	private Long ID;
	
	private String userName;
	private java.lang.String password;
	private java.lang.Boolean enabled;
	private java.lang.String authority;
	private java.lang.String address;
	private java.lang.String QQ;
	private java.lang.String telephone;
	private java.lang.String EMail;
	private java.lang.String company;
	private java.lang.String license;
	public java.lang.String getCompany() {
		return company;
	}

	public void setCompany(java.lang.String company) {
		this.company = company;
	}

	public java.lang.String getLicense() {
		return license;
	}

	public void setLicense(java.lang.String license) {
		this.license = license;
	}

	private List<Group> groups;
	
	private java.lang.String title;
	
	private UserManager userManager;
	
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public void validate(){
		
		if(this.action==null||this.action.equals(""))this.action=ACTION_CREATE;
		
		if(this.action.equalsIgnoreCase(ACTION_SAVE)){
			if(userName==null||userName.equals(""))addFieldError("userName","Name is not validate!");
		}
		
		else if(this.action.equalsIgnoreCase(ACTION_UPDATE)){
			
			if(ID==null){
				addFieldError("ID","ID is not validate!");
			}else{
				User g=userManager.get(ID);
				
				if(g==null)addFieldError("ID","ID is not validate!");
			}
			
			if(userName==null||userName.equals(""))addFieldError("userName","Name is not validate!");
			
			
			
		}
		
		else if(this.action.equalsIgnoreCase(ACTION_DELETE)){
			
			if(ID==null){
				addFieldError("ID","ID is not validate!");
			}else{
				User g=userManager.get(ID);
				
				if(g==null)addFieldError("ID","ID is not validate!");
			}
			
			
			
		}
		
		else if(this.action.equalsIgnoreCase(ACTION_VIEW)){
			
			if(ID==null){
				addFieldError("ID","ID is not validate!");
			}else{
				User g=userManager.get(ID);
				
				if(g==null)addFieldError("ID","ID is not validate!");
			}
			
			
			
		}else if(this.action.equalsIgnoreCase(ACTION_CREATE)){
			
		}
		
		else{
			
			addFieldError("action","Request is not validate!");
			
		}
		
	}
	
	private String description;

	private Date createdDate;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String execute() throws Exception {
		
		
		
		
		if(this.action.equalsIgnoreCase(ACTION_SAVE)){
			
			User user=new User();
			
			user.setID(ID);
			
			user.setUserName(userName);
			
			user.setAddress(address);
			
			user.setAuthority(authority);
			
			user.setEMail(EMail);
			
			user.setEnabled(enabled);
			
			user.setPassword(password);
			
			user.setTelephone(telephone);
			
			user.setCompany(company);
			
			user.setLicense(license);
			
			user.setDescription(description);
			
			user.setID(null);
			
			userManager.add(user);
			
			ID=user.getID();
			
			action=ACTION_UPDATE;
			
			title="User : "+user.getUserName();
			
			addActionMessage("Add successfully!");
			
			return Action.INPUT;
			
		}
		
		else if(this.action.equalsIgnoreCase(ACTION_UPDATE)){
			
			User user=userManager.get(ID);
			
			user.setID(ID);
			
			user.setUserName(userName);
			
			user.setAddress(address);
			
			user.setAuthority(authority);
			
			user.setEMail(EMail);
			
			user.setEnabled(enabled);
			
			user.setPassword(password);
			
			user.setTelephone(telephone);
			
			user.setCompany(company);
			
			user.setLicense(license);
			
			user.setDescription(description);
			
			createdDate=user.getCreatedDate();
			
			userManager.update(user);
			
			action=ACTION_UPDATE;
			
			title="User : "+user.getUserName();
			
			addActionMessage("Update successfully!");
			
			return Action.INPUT;
			
		}
		
		else if(this.action.equalsIgnoreCase(ACTION_DELETE)){
			
			userManager.remove(ID);
			
			action=ACTION_CREATE;
			
			
			addActionMessage("Delete successfully!");			
			
			return Action.SUCCESS;
			
		}
		
		else if(this.action.equalsIgnoreCase(ACTION_VIEW)){
			
			User user=userManager.get(ID);
			
			ID=user.getID();
			
			userName=user.getUserName();
			
			address=user.getAddress();
			
			authority=user.getAuthority();
			
			EMail=user.getEMail();
			
			enabled=user.getEnabled();
			
			password=user.getPassword();
			
			telephone=user.getTelephone();
			
			company=user.getCompany();
			
			license=user.getLicense();
			
			description=user.getDescription();
			
			action=ACTION_UPDATE;
			
			title="User : "+user.getUserName();
			
			groups = userManager.getGroupsByUser(ID);
			
			createdDate=user.getCreatedDate();
			
			return Action.INPUT;
			
		}else if(this.action.equalsIgnoreCase(ACTION_CREATE)){
			
			action=ACTION_SAVE;
			 
			title="New User";
			
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

	

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public java.lang.String getPassword() {
		return password;
	}

	public void setPassword(java.lang.String password) {
		this.password = password;
	}

	public java.lang.Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(java.lang.Boolean enabled) {
		this.enabled = enabled;
	}

	public java.lang.String getAuthority() {
		return authority;
	}

	public void setAuthority(java.lang.String authority) {
		this.authority = authority;
	}

	public java.lang.String getAddress() {
		return address;
	}

	public void setAddress(java.lang.String address) {
		this.address = address;
	}

	public java.lang.String getQQ() {
		return QQ;
	}

	public void setQQ(java.lang.String qq) {
		QQ = qq;
	}

	public java.lang.String getTelephone() {
		return telephone;
	}

	public void setTelephone(java.lang.String telephone) {
		this.telephone = telephone;
	}

	public java.lang.String getEMail() {
		return EMail;
	}

	public void setEMail(java.lang.String mail) {
		EMail = mail;
	}

	public java.lang.String getTitle() {
		return title;
	}

	public void setTitle(java.lang.String title) {
		this.title = title;
	}

	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}


}
