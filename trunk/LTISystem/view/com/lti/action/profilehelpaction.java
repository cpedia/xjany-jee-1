package com.lti.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.lti.bean.ModelPortfoliosBean;
import com.lti.permission.MPIQChecker;
import com.lti.service.GroupManager;
import com.lti.service.PortfolioManager;
import com.lti.service.StrategyManager;
import com.lti.service.bo.CachePortfolioItem;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.PortfolioFollowDate;
import com.lti.service.bo.Strategy;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.util.LTIDate;

/**
 * LTISystem/profilehelp_XXXX
 * @category 页面
 *
 */
public class profilehelpaction {
	private String Message;
	public String getMessage() {
		return Message;
	}
	public void setMessage(String message) {
		Message = message;
	}
	
	private Date lastUpdated = new Date();
	
	
	public Date getLastUpdated() {
		return lastUpdated;
	}
	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	private String curDate;
	private Long portfolioID;
	private Date followDate;
	
	public String getCurDate() {
		return curDate;
	}
	public void setCurDate(String curDate) {
		this.curDate = curDate;
	}
	public Date getFollowDate() {
		return followDate;
	}
	public void setFollowDate(Date followDate) {
		this.followDate = followDate;
	}
	public Long getPortfolioID() {
		return portfolioID;
	}
	public void setPortfolioID(Long portfolioID) {
		this.portfolioID = portfolioID;
	}
	private Integer size=null;
	public Integer getSize() {
		return size;
	}
	public void setSize(Integer size) {
		this.size = size;
	}
	/**
	 * 用户在页面按了start to follow
	 * @param followDate
	 * @param curDate
	 * @param portfolioID
	 * @param Message
	 */
	public String starttofollow(){
		PortfolioManager pm = (PortfolioManager) ContextHolder.getInstance().getApplicationContext().getBean("portfolioManager");
		PortfolioFollowDate pfd = pm.getPortfolioFollowDate(portfolioID);
		followDate = LTIDate.parseStringToDate(curDate);
		//add a new follow date to the portfolio
		if(pfd!=null){
			String dateString = pfd.getDateString();
			String[] dates = dateString.split("#");
			int length = dates.length;
			if(!curDate.equalsIgnoreCase(dates[length-1])){
				//now change the transaction record for the portfolio
				dateString += "#";
				dateString += curDate;
				pm.changeTransaction(portfolioID, followDate);
				pfd.setDateString(dateString);
			}
		}else{
			pfd = new PortfolioFollowDate();
			pfd.setPortfolioID(portfolioID);
			pfd.setDateString(curDate);
			pm.changeTransaction(portfolioID, followDate);
		}
		pm.saveOrUpdatePortfolioFollowDate(pfd);
		Message="ok";
		return Action.MESSAGE;
	}
	/*pickplan start-----------------------------------------*/
	private String planName;
	private List plans;
	//select distinct(strategyname),strategyid from Cache_group_Strategy gpi where gpi.Type=2 and (gpi.GroupID=10 or gpi.GroupID=8 or  gpi.UserID=516)

	
	/**
	 * 可以让用户选择plan的列表页面
	 * @param planName 如果不为空，则为关键字
	 * @param size 如果大于０，则限制记录数
	 * @param plans 
	 */
	public String pickplan() {
		String sql="select distinct(strategyname),strategyid from cache_group_strategy gpi where gpi.Type & "+Configuration.STRATEGY_TYPE_401K+" > 0";
		if(planName!=null&&!planName.equals("")){
			sql+=" and gpi.StrategyName like '%"+planName+"%'";
		}
		long userID=ContextHolder.getUserManager().getLoginUser().getID();
		if(userID==Configuration.SUPER_USER_ID){
			
		}else{
			GroupManager gm=ContextHolder.getGroupManager();
			Object[] gids=gm.getGroupIDs(userID);
			if(gids.length==1){
				sql+=" and (gpi.GroupID="+gids[0]+" or gpi.UserID="+userID+")";
			}else{
				sql+=" and (";
				for(Object gid:gids){
					sql+="gpi.GroupID="+gid;
					sql+=" or ";
				}
				sql+="gpi.UserID="+userID+")";
			}
		}
		
		if(size!=null&&size>0){
			sql+=" limit 0,"+size;
		}
		System.out.println(sql);
		StrategyManager strategyManager = ContextHolder.getStrategyManager();
		try {
			plans=strategyManager.findBySQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Action.SUCCESS;
	}
	public String getPlanName() {
		return planName;
	}
	public void setPlanName(String planName) {
		this.planName = planName;
	}
	public List getPlans() {
		return plans;
	}
	public void setPlans(List plans) {
		this.plans = plans;
	}
	/*pickplan end-----------------------------------------*/
	
	
	/*pickstrategy start-----------------------------------------*/
	private List<ModelPortfoliosBean> modelPortfoliosBeans;
	private Long planID;
	/**
	 * 可以让用户选择strategy的列表页面
	 * @param modelPortfoliosBeans
	 * @param planID
	 */
	public String pickstrategy() {
		StrategyManager strategyManager = ContextHolder.getStrategyManager();
		Strategy f401k = strategyManager.get(Configuration.STRATEGY_401K);
		List<Strategy> strategies = strategyManager.getStrategiesByMainStrategyID(f401k.getID());
		modelPortfoliosBeans = new ArrayList<ModelPortfoliosBean>(strategies.size());
		List<Portfolio> portfolios = strategyManager.getModelPortfolios(planID);
		MPIQChecker fc=new MPIQChecker(ContextHolder.getUserManager().getLoginUser().getID());
		boolean hasSubscred=fc.hasSubscred();
		for (int i = 0; i < strategies.size(); i++) {
			Strategy s = strategies.get(i);
			ModelPortfoliosBean scb = new ModelPortfoliosBean();
			scb.setStrategyID(s.getID());
			scb.setStrategyName(s.getName());
			modelPortfoliosBeans.add(scb);
			if (portfolios == null || portfolios.size() == 0)
				continue;
			List<CachePortfolioItem> items = new ArrayList<CachePortfolioItem>();
			for (int j = 0; j < portfolios.size(); j++) {
				Portfolio p = portfolios.get(j);
				if (p.getStrategies().getAssetAllocationStrategy().getID() != null && p.getStrategies().getAssetAllocationStrategy().getID().longValue() == s.getID().longValue()) {
					CachePortfolioItem cpi = null;
					List<CachePortfolioItem> pitems = null;
					if(hasSubscred){
						pitems=strategyManager.findByHQL("from CachePortfolioItem cp where cp.PortfolioID=" + p.getID() + " and cp.GroupID=0 and cp.RoleID="	+ Configuration.ROLE_PORTFOLIO_REALTIME_ID);
					}else{
						pitems=strategyManager.findByHQL("from CachePortfolioItem cp where cp.PortfolioID=" + p.getID() + " and cp.GroupID="+Configuration.GROUP_MPIQ_ID+" and cp.RoleID="	+ Configuration.ROLE_PORTFOLIO_DELAYED_ID);
					}
					if (pitems != null && pitems.size() > 0) {
						cpi = pitems.get(0);
					} else {
						cpi = new CachePortfolioItem();
						cpi.setPortfolioID(p.getID());
						cpi.setPortfolioName(p.getName());
					}
					items.add(cpi);

				}
			}
			if (items.size() > 0)
				scb.setModelPortfolios(items);

		}

		return Action.SUCCESS;
	}
	public List<ModelPortfoliosBean> getModelPortfoliosBeans() {
		return modelPortfoliosBeans;
	}
	public void setModelPortfoliosBeans(List<ModelPortfoliosBean> modelPortfoliosBeans) {
		this.modelPortfoliosBeans = modelPortfoliosBeans;
	}
	public Long getPlanID() {
		return planID;
	}
	public void setPlanID(Long planID) {
		this.planID = planID;
	}
	/*pickstrategy start-----------------------------------------*/
}
