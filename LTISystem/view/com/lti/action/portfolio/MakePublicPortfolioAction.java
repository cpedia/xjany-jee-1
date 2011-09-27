package com.lti.action.portfolio;

import org.apache.struts2.ServletActionContext;

import com.lti.action.Action;
import com.lti.permission.PermissionChecker;
import com.lti.permission.PortfolioPermissionChecker;
import com.lti.service.GroupManager;
import com.lti.service.PortfolioManager;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.PortfolioMPT;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.util.StringUtil;
import com.opensymphony.xwork2.ActionSupport;

public class MakePublicPortfolioAction extends ActionSupport implements Action{
	
	private String action;
	private Long ID;
	private String message;
	
	
	public String execute() throws Exception {
		
		GroupManager gm = ContextHolder.getGroupManager();
		PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();
		StringBuffer sb=new StringBuffer();
		Portfolio portfolio=ContextHolder.getPortfolioManager().get(ID);
		PermissionChecker pc = new PortfolioPermissionChecker(portfolio, ServletActionContext.getRequest());
		boolean isOwner = pc.isOwner();
		boolean isAdmin = pc.isAdmin();
		if(!isOwner && !isAdmin){
			message= "access denied.";
			return Action.MESSAGE;
		}
		if(action!=null && action.equals("public")){
			try {
				gm.addGroupRole(Configuration.GROUP_MPIQ_B_ID, Configuration.ROLE_PORTFOLIO_REALTIME_ID, portfolio.getID(), Configuration.RESOURCE_TYPE_PORTFOLIO);
				sb.append("Add "+portfolio.getName()+"["+portfolio.getID()+"][realtime] to group F401K.\r\n");
				gm.addGroupRole(Configuration.GROUP_ANONYMOUS_ID, Configuration.ROLE_PORTFOLIO_DELAYED_ID, portfolio.getID(), Configuration.RESOURCE_TYPE_PORTFOLIO);
				sb.append("Add "+portfolio.getName()+"["+portfolio.getID()+"][delaytime] to group ANONYMOUS.\r\n");
				//gm.addGroupRole(Configuration.GROUP_MPIQ_ID, Configuration.ROLE_PORTFOLIO_DELAYED_ID, portfolio.getID(), Configuration.RESOURCE_TYPE_PORTFOLIO);
				//sb.append("Add "+portfolio.getName()+"["+portfolio.getID()+"][delaytime] to group F401K_UN_SUBSCR.\r\n");
				PortfolioMPT mpt=portfolioManager.getPortfolioMPT(portfolio.getID(), PortfolioMPT.DELAY_LAST_FIVE_YEAR);
				if(mpt!=null)portfolioManager.updatePortfolioMPT(mpt);
				mpt=portfolioManager.getPortfolioMPT(portfolio.getID(), PortfolioMPT.DELAY_LAST_THREE_YEAR);
				if(mpt!=null)portfolioManager.updatePortfolioMPT(mpt);
				mpt=portfolioManager.getPortfolioMPT(portfolio.getID(), PortfolioMPT.DELAY_LAST_ONE_YEAR);
				if(mpt!=null)portfolioManager.updatePortfolioMPT(mpt);
				
				if (portfolio.getOriginalPortfolioID() != null) {
					gm.addGroupRole(Configuration.GROUP_MPIQ_B_ID, Configuration.ROLE_PORTFOLIO_REALTIME_ID, portfolio.getOriginalPortfolioID(), Configuration.RESOURCE_TYPE_PORTFOLIO);
					sb.append("Add "+portfolio.getName()+"["+portfolio.getOriginalPortfolioID()+"][realtime] to group F401K.<br>\r\n");
					gm.addGroupRole(Configuration.GROUP_ANONYMOUS_ID, Configuration.ROLE_PORTFOLIO_DELAYED_ID, portfolio.getOriginalPortfolioID(), Configuration.RESOURCE_TYPE_PORTFOLIO);
					sb.append("Add "+portfolio.getName()+"["+portfolio.getOriginalPortfolioID()+"][delaytime] to group ANONYMOUS.<br>\r\n");
					//gm.addGroupRole(Configuration.GROUP_MPIQ_ID, Configuration.ROLE_PORTFOLIO_DELAYED_ID, portfolio.getOriginalPortfolioID(), Configuration.RESOURCE_TYPE_PORTFOLIO);
					//sb.append("Add "+portfolio.getName()+"["+portfolio.getOriginalPortfolioID()+"][delaytime] to group F401K_UN_SUBSCR.<br>\r\n");
				}
			} catch (Exception e) {
				sb.append("fail\r\n");
				sb.append(StringUtil.getStackTraceString(e));
			}
		}
		else if(action!=null && action.equals("private")){
			try {
				gm.deleteGroupRole(Configuration.GROUP_MPIQ_B_ID, Configuration.ROLE_PORTFOLIO_REALTIME_ID, portfolio.getID(), Configuration.RESOURCE_TYPE_PORTFOLIO);
				sb.append("Remove "+portfolio.getName()+"["+portfolio.getID()+"][realtime] to group F401K.\r\n");
				gm.deleteGroupRole(Configuration.GROUP_ANONYMOUS_ID, Configuration.ROLE_PORTFOLIO_DELAYED_ID, portfolio.getID(), Configuration.RESOURCE_TYPE_PORTFOLIO);
				sb.append("Remove "+portfolio.getName()+"["+portfolio.getID()+"][delaytime] to group ANONYMOUS.\r\n");
				//gm.deleteGroupRole(Configuration.GROUP_MPIQ_ID, Configuration.ROLE_PORTFOLIO_DELAYED_ID, portfolio.getID(), Configuration.RESOURCE_TYPE_PORTFOLIO);
				//sb.append("Remove "+portfolio.getName()+"["+portfolio.getID()+"][delaytime] to group F401K_UN_SUBSCR.\r\n");
				if (portfolio.getOriginalPortfolioID() != null) {
					gm.deleteGroupRole(Configuration.GROUP_MPIQ_B_ID, Configuration.ROLE_PORTFOLIO_REALTIME_ID, portfolio.getOriginalPortfolioID(), Configuration.RESOURCE_TYPE_PORTFOLIO);
					sb.append("Remove "+portfolio.getName()+"["+portfolio.getOriginalPortfolioID()+"][realtime] to group F401K.\r\n");
					gm.deleteGroupRole(Configuration.GROUP_ANONYMOUS_ID, Configuration.ROLE_PORTFOLIO_DELAYED_ID, portfolio.getOriginalPortfolioID(), Configuration.RESOURCE_TYPE_PORTFOLIO);
					sb.append("Remove "+portfolio.getName()+"["+portfolio.getOriginalPortfolioID()+"][delaytime] to group ANONYMOUS.\r\n");
					//gm.deleteGroupRole(Configuration.GROUP_MPIQ_ID, Configuration.ROLE_PORTFOLIO_DELAYED_ID, portfolio.getOriginalPortfolioID(), Configuration.RESOURCE_TYPE_PORTFOLIO);
					//sb.append("Remove "+portfolio.getName()+"["+portfolio.getOriginalPortfolioID()+"][delaytime] to group F401K_UN_SUBSCR.\r\n");
				}
			} catch (Exception e) {
				sb.append("fail\r\n");
				sb.append(StringUtil.getStackTraceString(e));
			}
		}
		message=sb.toString();
		return Action.MESSAGE;
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


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}
}
