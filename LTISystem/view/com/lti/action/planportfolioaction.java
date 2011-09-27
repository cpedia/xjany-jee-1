package com.lti.action;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.ServletActionContext;

import com.lti.bean.ProfileItem;
import com.lti.permission.PublicMaker;
import com.lti.service.PortfolioManager;
import com.lti.service.UserManager;
import com.lti.service.bo.CachePortfolioItem;
import com.lti.service.bo.EmailNotification;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.Profile;
import com.lti.service.bo.Strategy;
import com.lti.service.bo.User;
import com.lti.service.bo.UserResource;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.util.CopyUtil;

public class planportfolioaction {
	private Long planID;
	private Long portfolioID;
	private String portfolioName;
	private String message;
	public Long getPlanID() {
		return planID;
	}
	public void setPlanID(Long planID) {
		this.planID = planID;
	}
	public Long getPortfolioID() {
		return portfolioID;
	}
	public void setPortfolioID(Long portfolioID) {
		this.portfolioID = portfolioID;
	}
	public String getPortfolioName() {
		return portfolioName;
	}
	public void setPortfolioName(String portfolioName) {
		this.portfolioName = portfolioName;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String add(){
		UserManager userManager = ContextHolder.getUserManager();
		User loginUser = userManager.getLoginUser();
		
		PortfolioManager pm=ContextHolder.getPortfolioManager();
		Portfolio p=pm.get(portfolioName);
		if(p==null||planID==null){
			ServletActionContext.getResponse().setStatus(500);
			message="no ok.";
			return Action.MESSAGE;
		}
		
		UserResource ur=null;
		ur=userManager.getUserResourceByUserIDAndResourceIDAndResourceTypeAndRelationID(loginUser.getID(), p.getID(), Configuration.USER_RESOURCE_PORTFOLIO_ON_PLAN_PAGE,planID);
		
		if(ur==null){
			ur=new UserResource();
		}else{
			message="OK.";
			return Action.MESSAGE;
		}
		
		ur.setRelationID(planID);
		ur.setUserID(loginUser.getID());
		ur.setResourceId(p.getID());
		ur.setResourceType(Configuration.USER_RESOURCE_PORTFOLIO_ON_PLAN_PAGE);
		
		userManager.saveUserResource(ur);
		message="OK.";
		return Action.MESSAGE;
	}
	
	public String remove(){
		UserManager userManager = ContextHolder.getUserManager();
		User loginUser = userManager.getLoginUser();
		
		UserResource ur=null;
		ur=userManager.getUserResourceByUserIDAndResourceIDAndResourceTypeAndRelationID(loginUser.getID(), portfolioID, Configuration.USER_RESOURCE_PORTFOLIO_ON_PLAN_PAGE,planID);
		
		if(ur==null){
			message="OK.";
			return Action.MESSAGE;
		}else{
			userManager.deleteUserResource(ur);
		}
		
		return Action.MESSAGE;
	}
	
	private List<Profile> profiles;
	
	public List<Profile> getProfiles() {
		return profiles;
	}
	public void setProfiles(List<Profile> profiles) {
		this.profiles = profiles;
	}

	private boolean owner;
	private boolean admin;
	
	public boolean isOwner() {
		return owner;
	}
	public void setOwner(boolean owner) {
		this.owner = owner;
	}
	public boolean isAdmin() {
		return admin;
	}
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
	public String list(){
		UserManager userManager = ContextHolder.getUserManager();
		PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();
		User loginUser=userManager.getLoginUser();
		
		profiles=new ArrayList<Profile>();
		
		Strategy plan=ContextHolder.getStrategyManager().get(planID);
		if(plan.getUserID().equals(loginUser.getID())){
			owner=true;
		}
		if(loginUser.getID().equals(Configuration.SUPER_USER_ID)){
			admin=true;
		}
		
		List<Long> portfolioList = userManager.getResourceIDListByUserIDAndResourceTypeAndRelationID(loginUser.getID(), Configuration.USER_RESOURCE_PORTFOLIO_ON_PLAN_PAGE,planID);
		
		if(portfolioList != null && portfolioList.size()>0){
			for(Long portID : portfolioList){
				Portfolio portfolio = portfolioManager.get(portID);
				if(portfolio != null ){
					ProfileItem pi = new ProfileItem();
					
					EmailNotification en = userManager.getEmailNotification(loginUser.getID(), portfolio.getID());
					List<CachePortfolioItem> pitems = null;
					pitems = portfolioManager.findByHQL("from CachePortfolioItem cp where cp.PortfolioID=" + portfolio.getID() + " and cp.GroupID=0 and cp.RoleID=" + Configuration.ROLE_PORTFOLIO_REALTIME_ID);
					
					if (pitems != null && pitems.size() > 0) {
						CopyUtil.Translate(pitems.get(0), pi);
					}
					
					pi.setIsEMailAlert(en!=null);
					pi.setPlanID(planID);
					pi.setPlanName("N/A");
					
					PublicMaker publicMaker=new PublicMaker(pi);
					pi.setPublic(publicMaker.isPublic());
					pi.setStrategyID(portfolio.getStrategies().getAssetAllocationStrategy().getID());
					pi.setStrategyName(portfolio.getStrategies().getAssetAllocationStrategy().getName());
					
					profiles.add(pi);
				}//end if
			}//end for
		}//end if
		return Action.SUCCESS;
	}
	
	
	
}
