package com.lti.permission;

import com.lti.bean.ProfileItem;
import com.lti.bean.StrategyItem;
import com.lti.service.GroupManager;
import com.lti.service.bo.GroupRole;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.Strategy;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;

public class PublicMaker {

	private boolean isPortfolio=true;
	
	private Portfolio portfolio;
	
	private Strategy strategy;
	
	public PublicMaker(Portfolio port){
		this.portfolio=port;
		this.isPortfolio=true;
	}
	
	public PublicMaker(Strategy str){
		this.strategy=str;
		this.isPortfolio=false;
	}
	
	public PublicMaker(ProfileItem pi) {
		Portfolio port=new Portfolio();
		port.setID(pi.getPortfolioID());
		this.portfolio=port;
		this.isPortfolio=true;
	}

	public PublicMaker(StrategyItem item) {
		Strategy str=new Strategy();
		str.setID(item.getID());
		this.strategy=str;
		this.isPortfolio=false;
	}

	public void makePublic()throws Exception{
		GroupManager gm = ContextHolder.getGroupManager();
		if(!isPortfolio){
			gm.addGroupRole(Configuration.GROUP_MPIQ_B_ID, gm.getRoleByName(Configuration.ROLE_STRATEGY_READ).getID(), strategy.getID(), Configuration.RESOURCE_TYPE_STRATEGY);
			gm.addGroupRole(Configuration.GROUP_ANONYMOUS_ID, gm.getRoleByName(Configuration.ROLE_STRATEGY_READ).getID(), strategy.getID(), Configuration.RESOURCE_TYPE_STRATEGY);
			gm.addGroupRole(Configuration.GROUP_MPIQ_ID, gm.getRoleByName(Configuration.ROLE_STRATEGY_READ).getID(),strategy.getID(), Configuration.RESOURCE_TYPE_STRATEGY);
		}else{
			gm.addGroupRole(Configuration.GROUP_MPIQ_B_ID, Configuration.ROLE_PORTFOLIO_REALTIME_ID, portfolio.getID(), Configuration.RESOURCE_TYPE_PORTFOLIO);
			gm.addGroupRole(Configuration.GROUP_ANONYMOUS_ID, Configuration.ROLE_PORTFOLIO_DELAYED_ID, portfolio.getID(), Configuration.RESOURCE_TYPE_PORTFOLIO);
			
			boolean issaa=false;
			try {
				if(portfolio.getStrategies().getAssetAllocationStrategy().getID().longValue()==Configuration.STRATEGY_SAA_ID)issaa=true;
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(issaa){
				gm.addGroupRole(Configuration.GROUP_MPIQ_ID, Configuration.ROLE_PORTFOLIO_REALTIME_ID, portfolio.getID(), Configuration.RESOURCE_TYPE_PORTFOLIO);
				
			}else{
				gm.addGroupRole(Configuration.GROUP_MPIQ_ID, Configuration.ROLE_PORTFOLIO_DELAYED_ID, portfolio.getID(), Configuration.RESOURCE_TYPE_PORTFOLIO);
				
			}
		}
	}
	
	public void makePrivate()throws Exception{
		GroupManager gm = ContextHolder.getGroupManager();
		if(!isPortfolio){
			gm.deleteGroupRole(Configuration.GROUP_MPIQ_B_ID, Configuration.ROLE_STRATEGY_READ_ID, strategy.getID(), Configuration.RESOURCE_TYPE_STRATEGY);
			gm.deleteGroupRole(Configuration.GROUP_ANONYMOUS_ID, Configuration.ROLE_STRATEGY_READ_ID, strategy.getID(), Configuration.RESOURCE_TYPE_STRATEGY);
			gm.deleteGroupRole(Configuration.GROUP_MPIQ_ID, Configuration.ROLE_STRATEGY_READ_ID,strategy.getID(), Configuration.RESOURCE_TYPE_STRATEGY);
		}else{
			gm.deleteGroupRole(Configuration.GROUP_MPIQ_B_ID, Configuration.ROLE_PORTFOLIO_REALTIME_ID, portfolio.getID(), Configuration.RESOURCE_TYPE_PORTFOLIO);
			gm.deleteGroupRole(Configuration.GROUP_ANONYMOUS_ID, Configuration.ROLE_PORTFOLIO_DELAYED_ID, portfolio.getID(), Configuration.RESOURCE_TYPE_PORTFOLIO);
			gm.deleteGroupRole(Configuration.GROUP_MPIQ_ID, Configuration.ROLE_PORTFOLIO_DELAYED_ID, portfolio.getID(), Configuration.RESOURCE_TYPE_PORTFOLIO);
			gm.deleteGroupRole(Configuration.GROUP_MPIQ_ID, Configuration.ROLE_PORTFOLIO_REALTIME_ID, portfolio.getID(), Configuration.RESOURCE_TYPE_PORTFOLIO);
			
		}
	}
	
	public boolean isPublic(){
		GroupManager gm = ContextHolder.getGroupManager();
		if(!isPortfolio){
			GroupRole gr1=gm.getGroupRole(Configuration.GROUP_MPIQ_B_ID, Configuration.ROLE_STRATEGY_READ_ID, strategy.getID(), Configuration.RESOURCE_TYPE_STRATEGY);
			GroupRole gr2=gm.getGroupRole(Configuration.GROUP_ANONYMOUS_ID, Configuration.ROLE_STRATEGY_READ_ID, strategy.getID(), Configuration.RESOURCE_TYPE_STRATEGY);
			GroupRole gr3=gm.getGroupRole(Configuration.GROUP_MPIQ_ID, Configuration.ROLE_STRATEGY_READ_ID, strategy.getID(), Configuration.RESOURCE_TYPE_STRATEGY);
			if(gr1!=null&&gr2!=null&&gr3!=null){
				return true;
			}
		}else{
			GroupRole gr1=gm.getGroupRole(Configuration.GROUP_MPIQ_B_ID, Configuration.ROLE_PORTFOLIO_REALTIME_ID, portfolio.getID(), Configuration.RESOURCE_TYPE_PORTFOLIO);
			GroupRole gr2=gm.getGroupRole(Configuration.GROUP_ANONYMOUS_ID, Configuration.ROLE_PORTFOLIO_DELAYED_ID, portfolio.getID(), Configuration.RESOURCE_TYPE_PORTFOLIO);
			//GroupRole gr3=gm.getGroupRole(Configuration.GROUP_MPIQ_ID, Configuration.ROLE_PORTFOLIO_DELAYED_ID, portfolio.getID(), Configuration.RESOURCE_TYPE_PORTFOLIO);
			if(gr1!=null&&gr2!=null){
				return true;
			}
		}
		return false;
	}
}
