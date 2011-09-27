package com.lti.action.admin.user;

import java.util.ArrayList;
import java.util.List;

import com.lti.action.Action;
import com.lti.service.UserManager;
import com.lti.service.bo.User;
import com.lti.service.bo.UserPermission;
import com.lti.system.ContextHolder;
import com.opensymphony.xwork2.ActionSupport;

public class UserQuotaAction extends ActionSupport implements Action{
	private static final long serialVersionUID = 1L;
	private String name;
	private List<List<String>> userQuotaList;
	
	public String getName() {
		return name;
	}
	public List<List<String>> getUserQuotaList() {
		return userQuotaList;
	}
	public void setUserQuotaList(List<List<String>> userQuotaList) {
		this.userQuotaList = userQuotaList;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String execute(){
		if(name == null  || name.equals("")){
			return Action.INPUT;
		}
		UserManager userManager = ContextHolder.getUserManager();
		List<User> userList = userManager.getBySubOfName(name);
		if(userList ==null){
			return Action.INPUT;
		}
		userQuotaList = new ArrayList<List<String>>();
		for(User user :userList){
			List<String> lists = new ArrayList<String>();
			lists.add(user.getUserName());
		    UserPermission ups = userManager.getUserPermissionByUserID(user.getID());
		    int maxPlanNum = ups.getMaxPlanCreateNum();
		    int maxPortfolioNum = ups.getMaxPortfolioFollowNum();
		    int maxPlanViewNum = ups.getMaxPlanFundTableNum();
		    int curPlanNum = ups.getCurPlanCreateNum();
		    int curPortfolioNum = ups.getCurPortfolioFollowNum();
		    int curPlanViewNum = ups.getCurPlanFundTableNum();
		    lists.add(String.valueOf(maxPlanNum));
		    lists.add(String.valueOf(maxPortfolioNum));
		    lists.add(String.valueOf(maxPlanViewNum));
		    lists.add(String.valueOf(curPlanNum));
		    lists.add(String.valueOf(curPortfolioNum));
		    lists.add(String.valueOf(curPlanViewNum));
		    userQuotaList.add(lists);
		}
		
		return Action.SUCCESS;
	}
	
	private String userNames;
	private String maxPlanNum;
	private String maxPortfolioNum;
	private String maxPlanViewNum;
	private String message;
	public String modify(){
		message="";
		if(userNames ==null|| maxPlanNum ==null ||maxPortfolioNum==null || maxPlanViewNum ==null)
			return Action.MESSAGE;
		UserManager userManager = ContextHolder.getUserManager();
		String[] nameItems = userNames.split(",");
		String[] planItems = maxPlanNum.split(",");
		String[] portfolioItems = maxPortfolioNum.split(",");
		String[] viewItems = maxPlanViewNum.split(",");
		for(int i=0;i<nameItems.length;i++){
			User user = userManager.get(nameItems[i]);
			UserPermission ups = userManager.getUserPermissionByUserID(user.getID());
			ups.setMaxPlanCreateNum(Integer.parseInt(planItems[i]));
			ups.setMaxPortfolioFollowNum(Integer.parseInt(portfolioItems[i]));
			ups.setMaxPlanFundTableNum(Integer.parseInt(viewItems[i]));
			ups.setMaxPlanRefNum(Integer.parseInt(portfolioItems[i]));
			ups.setMaxPortfolioRealTimeNum(Integer.parseInt(portfolioItems[i]));
		    userManager.updateUserPermission(ups);
		}
		
		return Action.MESSAGE;
	}
	public String getUserNames() {
		return userNames;
	}
	public void setUserNames(String userNames) {
		this.userNames = userNames;
	}
	public String getMaxPlanNum() {
		return maxPlanNum;
	}
	public void setMaxPlanNum(String maxPlanNum) {
		this.maxPlanNum = maxPlanNum;
	}
	public String getMaxPortfolioNum() {
		return maxPortfolioNum;
	}
	public void setMaxPortfolioNum(String maxPortfolioNum) {
		this.maxPortfolioNum = maxPortfolioNum;
	}
	public String getMaxPlanViewNum() {
		return maxPlanViewNum;
	}
	public void setMaxPlanViewNum(String maxPlanViewNum) {
		this.maxPlanViewNum = maxPlanViewNum;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

}
