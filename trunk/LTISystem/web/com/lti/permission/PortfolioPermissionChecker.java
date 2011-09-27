package com.lti.permission;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.lti.service.GroupManager;
import com.lti.service.UserManager;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.User;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.util.LTIDate;

public class PortfolioPermissionChecker implements PermissionChecker {
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
	private boolean hasSimulateRole=false;
	private int readMode=PermissionChecker.NO_VIEW;
	private User loginUser;
	
	
	public User getLoginUser() {
		return loginUser;
	}

	public boolean isHasOperationRole() {
		return hasOperationRole;
	}

	public boolean isHasViewCodeRole() {
		return hasViewCodeRole;
	}

	public boolean isHasDelaytimeRole() {
		return hasDelaytimeRole;
	}

	public boolean isHasRealtimeRole() {
		return hasRealtimeRole;
	}

	public boolean isHasViewRole() {
		return hasViewRole;
	}

	public boolean isHasSubscred() {
		return hasSubscred;
	}

	public PortfolioPermissionChecker(Portfolio portfolio, HttpServletRequest request) {
		//check whether the user has subscred
		MYPLANIQPermissionManager mpm = new MYPLANIQPermissionManager();
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
		
		loginUser=user;
		
		if(user.getID().longValue()!=Configuration.USER_ANONYMOUS.longValue()){
			isAnonymous=false;
		}
		
		//check other permissions
		UserManager userManager=ContextHolder.getUserManager();
		
		if(user.getID().longValue()==portfolio.getUserID()){
			isOwner=true;
		}
		if(user.getID().longValue()==Configuration.SUPER_USER_ID){
			isAdmin=true;
		}
		
		//check whether the user is the advanceduser
		GroupManager gm = ContextHolder.getGroupManager();
		Object[] groups = gm.getGroupIDs(user.getID());
		if (groups != null && groups.length > 0) {
			for (int i = 0; i < groups.length; i++) {
				if (groups[i].equals(Configuration.GROUP_MPIQ_E_ID)
						|| groups[i].equals(Configuration.GROUP_MPIQ_P_ID)) {
					isAdvancedUser = true;
					break;
				}
			}
		}
		
		//check whether the portfolio's ass is raa 
		boolean isRAA=false;
		if(portfolio.getStrategies().getAssetAllocationStrategy().getID()!=null){
			if(portfolio.getStrategies().getAssetAllocationStrategy().getID()==Configuration.STRATEGY_SAA_ID){
				isRAA=true;
			}
		}
		//check whether the user has been subscribed
		
		
		if(isAdmin){
			lastLegalDate=portfolio.getEndDate();
			hasOperationRole=true;
			hasViewCodeRole=true;
			isOwner=true;
			hasRealtimeRole=true;
			hasViewRole=true;
			hasSimulateRole =true;
			readMode=PermissionChecker.REAL_TIME;
			return;
		}
		
		if(isOwner && (isRAA || hasSubscred)){
			hasViewCodeRole=true;
		}
		
		hasOperationRole = userManager.HaveRole(Configuration.ROLE_PORTFOLIO_OPERATION, user.getID(), portfolio.getID() ,Configuration.RESOURCE_TYPE_PORTFOLIO);
		//替换原来real time 的定义
		hasRealtimeRole = mpm.hasUserPortfolioRealTime(user.getID(), portfolio);
		//hasRealtimeRole = userManager.HaveRole(Configuration.ROLE_PORTFOLIO_REALTIME, user.getID(), portfolio.getID(),Configuration.RESOURCE_TYPE_PORTFOLIO);
		if(hasRealtimeRole){
			hasViewRole=true;
			readMode=REAL_TIME;
			//有权限follow
			hasSimulateRole = true;
		}else{
			hasDelaytimeRole = userManager.HaveRole(Configuration.ROLE_PORTFOLIO_DELAYED, user.getID(), portfolio.getID(),Configuration.RESOURCE_TYPE_PORTFOLIO);
			if(hasDelaytimeRole && mpm.hasFollowPortfolioPermission(user.getID(), portfolio) == mpm.SUCCESS)
				hasSimulateRole = true;
			if(hasDelaytimeRole||isOwner){
				readMode=DELAY_TIME;
				hasViewRole=true;
			}
		}
		if(mpm.hasFollowPortfolioPermission(user.getID(), portfolio) == mpm.SUCCESS)
			hasSimulateRole = true;
		
		if(hasRealtimeRole){
			lastLegalDate=portfolio.getEndDate();
		}else if(hasDelaytimeRole&&portfolio.getEndDate()!=null){
			lastLegalDate=LTIDate.getHoldingDateMonthEnd(portfolio.getEndDate());
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

	public boolean isHasSimulateRole() {
		return hasSimulateRole;
	}



}
