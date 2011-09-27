package com.lti.action.admin.group.emails;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.lti.action.Action;
import com.lti.service.GroupManager;
import com.lti.service.bo.Group;
import com.lti.service.bo.GroupUser;
import com.lti.service.bo.User;
import com.lti.system.Configuration;
import com.lti.type.PaginationSupport;
import com.lti.util.StringUtil;
import com.opensymphony.xwork2.ActionSupport;


public class MainAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(MainAction.class);
	
//	private List<PaginationSupport> users;
	private List<User> users;

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	private List<String> emails;
	

	
	public List<String> getEmails() {
		return emails;
	}

	public void setEmails(List<String> emails) {
		this.emails = emails;
	}

	private Long groupID;
	
	private String title;
	
	
		
	private GroupManager groupManager;
	
	public void setGroupManager(GroupManager groupManager) {
		this.groupManager = groupManager;
	}

	public void validate(){
		
		if(groupID==null){
			
			addFieldError("groupID","Group ID is not validate!");
			
		}else{
			
			Group g=groupManager.get(groupID);
			
			if(g==null){
				addFieldError("ID","ID is not validate!");
				return;
			}
			title="Group: "+g.getName();
		}
		
	}
	@Override
	public String execute() throws Exception {
			

		users=groupManager.getUsers(groupID);
		emails=new ArrayList<String>();
		for(int i=0;i<users.size();i++){
			if(users.get(i).getEMail()==null||users.get(i).getEMail().equalsIgnoreCase(""))
				continue;
			else if (StringUtil.checkEmail(users.get(i).getEMail()) == false){
				continue;
			}
			else {
				//System.out.println(users.size()+"//"+users.get(i).getEMail());
				emails.add(users.get(i).getEMail());
				//emails.add(e)
			}
		}
		/*for(int i=0;i<emails.size();i++){
			System.out.println(emails.get(i));
		}*/
		return Action.SUCCESS;

	}


	
	public Long getGroupID() {
		return groupID;
	}


	public void setGroupID(Long groupID) {
		this.groupID = groupID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
