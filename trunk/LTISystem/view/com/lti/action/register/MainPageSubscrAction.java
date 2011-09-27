package com.lti.action.register;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.lti.action.Action;
import com.lti.permission.MPIQChecker;
import com.lti.permission.ValidFiChecker;
import com.lti.service.UserManager;
import com.lti.service.bo.User;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class MainPageSubscrAction extends ActionSupport implements Action{
	
	private UserManager userManager;
	
	public UserManager getUserManager() {
		return userManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}
	
	//check for diffrent people
	public String userChecker(){
		User user = userManager.getLoginUser();
		String userName = user.getUserName();
		if(userName.equals("ANONYMOUS"))
			return "ANONYMOUS";
		else{
			HttpServletRequest request=	(HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST );
			ValidFiChecker vc = (ValidFiChecker)request.getSession().getAttribute("validfiChecker");
			MPIQChecker fc = (MPIQChecker)request.getSession().getAttribute("mpiqChecker");
			if(fc.hasSubscred()||vc.hasSubscred()){
				return "SUBSCRED_USER";
			}
			return "UNSUBSCRED_USER";
		}
	}
	
	public String execute()throws Exception {
		String userType = userChecker();
		if(userType.equals("ANONYMOUS"))
			return Action.LOGIN;
		else 
			return Action.ERROR;
	}
}
