package com.lti.action.portfolio;

import java.util.List;

import com.lti.action.Action;
import com.lti.service.GroupManager;
import com.lti.service.PortfolioManager;
import com.lti.service.bo.GroupRole;
import com.lti.service.bo.Portfolio;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.opensymphony.xwork2.ActionSupport;

public class PermissionStateAction extends ActionSupport implements Action{

	private String action;
	private Long ID;
	private String message;
	
	public String execute() throws Exception{
		
		GroupManager gm = ContextHolder.getGroupManager();
		List<GroupRole> grs = gm.getGroupRolesByResource(ID, Configuration.RESOURCE_TYPE_PORTFOLIO);
		if(grs!=null && grs.size()>0){
			int num=0;
			for(int i=0;i<grs.size();i++){
				GroupRole gr = grs.get(i);
				if(gr.getGroupID().equals(Configuration.GROUP_ANONYMOUS_ID) && gr.getRoleID().equals(Configuration.ROLE_PORTFOLIO_DELAYED_ID))
					num++;
				else if(gr.getGroupID().equals(Configuration.GROUP_MPIQ_B_ID) && gr.getRoleID().equals(Configuration.ROLE_PORTFOLIO_REALTIME_ID))
					num++;
				else if(gr.getGroupID().equals(Configuration.GROUP_MPIQ_ID) && gr.getRoleID().equals(Configuration.ROLE_PORTFOLIO_DELAYED_ID))
					num++;
			}
			if(num==2||num==3)
				message="public";
			else
				message="private";
		}
		else
			message="private";
		return Action.MESSAGE;
	}
	
	public String checkPublicState(){
		PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();
		Portfolio portfolio = portfolioManager.get(ID);
		message = "";
		if(portfolio.isFullyPublic()){
			message = "public";
		}else{
			message = "private";
		}
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
