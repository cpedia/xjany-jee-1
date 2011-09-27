package com.lti.permission;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.lti.service.GroupManager;
import com.lti.service.UserManager;
import com.lti.service.bo.Strategy;
import com.lti.service.bo.User;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;

public class StrategyPermissionChecker implements PermissionChecker {
	private Date lastLegalDate=null;
	private boolean hasOperationRole=false;
	private boolean hasViewCodeRole=false;
	private boolean isAdmin=false;
	private boolean hasDelaytimeRole=false;
	private boolean isOwner=false;
	private boolean hasRealtimeRole=false;
	private boolean hasViewRole=false;
	private boolean isAnonymous=true;
	private boolean hasSubscred=false;
	private boolean isAdvancedUser=false;
	private boolean hasSimulateTAARole=false;
	private boolean hasSimulateSAARole=false;
	private int readMode=PermissionChecker.NO_VIEW;
	
	
	
	public StrategyPermissionChecker(Strategy strategy, HttpServletRequest request) {
		//check whether the user has subscred
		String servername=request.getServerName();
		boolean isf401k=Configuration.isF401KDomain(servername);
		if(isf401k){
			MPIQChecker mc=(MPIQChecker) request.getSession().getAttribute("mpiqChecker");
			if(mc!=null)hasSubscred=mc.hasSubscred();
		}else{
			ValidFiChecker vc=(ValidFiChecker) request.getSession().getAttribute("validfiChecker");
			if(vc!=null)hasSubscred=vc.hasSubscred();
		}
		
		User user=(User) request.getSession().getAttribute("user");
		if(user==null){
			user=ContextHolder.getUserManager().getLoginUser();
		}
		
		//check other permissions
		UserManager userManager=ContextHolder.getUserManager();
		
		if(user.getID().longValue()==strategy.getUserID()){
			isOwner=true;
		}
		if(user.getID().longValue()==Configuration.SUPER_USER_ID){
			isAdmin=true;
		}
		//check whether the user has been subscribed
		
		
		if(isAdmin){
			lastLegalDate=null;
			hasOperationRole=true;
			hasViewCodeRole=true;
			isOwner=true;
			isAdvancedUser = true;
			hasViewCodeRole=true;
			hasViewRole=true;
			readMode=PermissionChecker.READ_CODE;
			hasSimulateTAARole = true;
			hasSimulateSAARole = true;
			return;
		}
		MYPLANIQPermissionManager mpm = new MYPLANIQPermissionManager();
		isAdvancedUser = mpm.isAdvancedUser(user.getID());
		if(mpm.hasCustomizePortfolioPermission(user.getID(), Configuration.STRATEGY_TAA_ID) == mpm.SUCCESS)
			hasSimulateTAARole = true;
		if(mpm.hasCustomizePortfolioPermission(user.getID(), Configuration.STRATEGY_SAA_ID) == mpm.SUCCESS)
			hasSimulateSAARole = true;
		if(user.getID().longValue()!=Configuration.USER_ANONYMOUS){
			isAnonymous=false;
		}
		
		hasOperationRole = userManager.HaveRole(Configuration.ROLE_STRATEGY_OPERATION, user.getID(), strategy.getID() ,Configuration.RESOURCE_TYPE_STRATEGY);
		hasViewCodeRole = userManager.HaveRole(Configuration.ROLE_STRATEGY_CODE, user.getID(), strategy.getID(),Configuration.RESOURCE_TYPE_STRATEGY);
		if(hasViewCodeRole){
			hasViewRole=true;
			readMode=READ_CODE;
		}else{
			hasViewRole=userManager.HaveRole(Configuration.ROLE_STRATEGY_READ, user.getID(), strategy.getID(),Configuration.RESOURCE_TYPE_STRATEGY);
			if(hasViewRole||isOwner){
				readMode=BASIC_READ;
				hasViewRole=true;
			}
		}
		
	}

	@Override
	public Date getLastLegalDate() {
		return lastLegalDate;
	}

	@Override
	public boolean hasOperationRole() {
		return hasOperationRole;
	}

	@Override
	public boolean hasViewCodeRole() {
		return hasViewCodeRole;
	}

	@Override
	public boolean isAdmin() {
		return isAdmin;
	}

	@Override
	public boolean hasDelaytimeRole() {
		return hasDelaytimeRole;
	}

	@Override
	public boolean isOwner() {
		return isOwner;
	}

	@Override
	public boolean hasRealtimeRole() {
		return hasRealtimeRole;
	}

	@Override
	public boolean hasViewRole() {
		return hasViewRole;
	}

	@Override
	public boolean isAnonymous() {
		return isAnonymous;
	}

	@Override
	public int getReadMode() {
		return readMode;
	}

	@Override
	public boolean hasSubscred() {
		return hasSubscred;
	}
	
	@Override
	public boolean isAdvancedUser(){
		return isAdvancedUser;
	}

	public boolean isHasSimulateTAARole() {
		return hasSimulateTAARole;
	}

	public void setHasSimulateTAARole(boolean hasSimulateTAARole) {
		this.hasSimulateTAARole = hasSimulateTAARole;
	}

	public boolean isHasSimulateSAARole() {
		return hasSimulateSAARole;
	}

	public void setHasSimulateSAARole(boolean hasSimulateSAARole) {
		this.hasSimulateSAARole = hasSimulateSAARole;
	}

}
