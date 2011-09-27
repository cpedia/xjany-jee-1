package com.lti.action.admin.user;

import java.util.List;

import com.lti.action.Action;
import com.lti.service.UserManager;
import com.lti.service.bo.UserOperation;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;

public class UserOperationAction {
	List<UserOperation> userOperations;
	public String list(){
		UserManager userManager=ContextHolder.getUserManager();
		userOperations=userManager.getUserOperations(Configuration.SUPER_USER_ID);
		return Action.SUCCESS;
	}
	private UserOperation userOperation;
	private String message;
	public String change(){
		UserManager userManager=ContextHolder.getUserManager();
		if(userOperation.getId()==null){
			message="null id.";
			return Action.MESSAGE;
		}
		
		UserOperation uo=userManager.getUserOperation(userOperation.getId());
		
		if(uo==null){
			message="null record.";
			return Action.MESSAGE;
		}
		
		uo.setOptCondition(userOperation.getOptCondition());
		userManager.updateUserOperation(uo);
		
		message="ok";
		return Action.MESSAGE;
	}
	public UserOperation getUserOperation() {
		return userOperation;
	}
	public void setUserOperation(UserOperation userOperation) {
		this.userOperation = userOperation;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public List<UserOperation> getUserOperations() {
		return userOperations;
	}
	public void setUserOperations(List<UserOperation> userOperations) {
		this.userOperations = userOperations;
	}
}
