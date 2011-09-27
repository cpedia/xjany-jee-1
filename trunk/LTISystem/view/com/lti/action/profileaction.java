package com.lti.action;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.lti.Exception.SecurityException;
import com.lti.Exception.Security.NoPriceException;
import com.lti.bean.ProfileItem;
import com.lti.jobscheduler.DailyExecutionJob;
import com.lti.permission.MPIQChecker;
import com.lti.permission.MYPLANIQPermissionManager;
import com.lti.permission.PermissionChecker;
import com.lti.permission.PortfolioPermissionChecker;
import com.lti.permission.PublicMaker;
import com.lti.permission.SubscrPlanChecker;
import com.lti.service.GroupManager;
import com.lti.service.PortfolioManager;
import com.lti.service.ProfileManager;
import com.lti.service.SecurityManager;
import com.lti.service.StrategyManager;
import com.lti.service.UserManager;
import com.lti.service.bo.CachePortfolioItem;
import com.lti.service.bo.EmailNotification;
import com.lti.service.bo.HoldingItem;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.PortfolioFollowDate;
import com.lti.service.bo.PortfolioMPT;
import com.lti.service.bo.PortfolioState;
import com.lti.service.bo.Profile;
import com.lti.service.bo.Security;
import com.lti.service.bo.SecurityMPT;
import com.lti.service.bo.Strategy;
import com.lti.service.bo.User;
import com.lti.service.bo.UserResource;
import com.lti.service.bo.VariableFor401k;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.type.finance.HoldingInf;
import com.lti.util.BuildingUtil;
import com.lti.util.CopyUtil;
import com.lti.util.FormatUtil;
import com.lti.util.LTIDate;
import com.lti.util.StringUtil;
import com.lti.util.PortfolioAdjustAmountUtil;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.inject.Context;

public class profileaction  extends ActionSupport{

	private static final long serialVersionUID = 1L;

	private ProfileManager profileManager = null;
	
	private MYPLANIQPermissionManager mpm = new MYPLANIQPermissionManager();

	public profileaction() {
		super();
		profileManager = (ProfileManager) ContextHolder.getInstance().getApplicationContext().getBean("profileManager");
		User u=ContextHolder.getUserManager().getLoginUser();
		userID = u.getID();

	}

	public boolean checkPermission(Long userid, Long planid, Long portfolioid) {
		try {
			if (userID.equals(Configuration.SUPER_USER_ID))
				return true;
			Profile obj = profileManager.get(portfolioid, userid, planid);
			if (obj.getUserID().equals(userID))
				return true;
		} catch (Exception e) {
		}
		return false;
	}

	private String title;
	private String description;
	private Boolean create = true;
	private Integer size = 0;
	private Long searchByUserID = -1l;
	private String followDateStr;
	private String imitationDateStr;
	/**
	 * 0: follow
	 * 1: customize
	 */
	private int portfolioSource = 0;
	/**
	 * 0: saa or taa
	 * 1: adv
	 */
	private int portfolioType = 0;
	
	
	public String changefollowdate() {
		UserManager userManager = ContextHolder.getUserManager();
		UserResource userResource = userManager.getUserResourceByUserIDAndResourceIDAndResourceType(userID, portfolioID, Configuration.USER_RESOURCE_PORTFOLIO_FOLLOW);
		if(followDateStr != null) {
			Date followDate = LTIDate.parseStringToDate(followDateStr);
			userResource.setUpdateTime(followDate);
			userManager.updateUserResourse(userResource);
		}
		return Action.MESSAGE;
	}
	/**
	 * start to imitate
	 * @return
	 */
	public String starttoimitate() {
		PortfolioManager pm = (PortfolioManager) ContextHolder.getInstance().getApplicationContext().getBean("portfolioManager");
		Portfolio portfolio = pm.get(portfolioID);
		PortfolioFollowDate pfd = pm.getPortfolioFollowDate(portfolioID);
		Date imitationDate = portfolio.getEndDate();
		imitationDateStr = LTIDate.parseDateToString(imitationDate);
		//add a new follow date to the portfolio
		if(pfd!=null){
			String dateString = pfd.getDateString();
			String[] dates = dateString.split("#");
			int length = dates.length;
			if(!imitationDateStr.equalsIgnoreCase(dates[length-1])){
				//now change the transaction record for the portfolio
				dateString += "#" + imitationDateStr;
				pm.changeTransaction(portfolioID, imitationDate);
				pfd.setDateString(dateString);
			}
		}else{
			pfd = new PortfolioFollowDate();
			pfd.setPortfolioID(portfolioID);
			pfd.setDateString(imitationDateStr);
			pm.changeTransaction(portfolioID, imitationDate);
		}
		pm.saveOrUpdatePortfolioFollowDate(pfd);
		message="ok";
		return Action.MESSAGE;
	}
	/**
	 * user plan portfolio list
	 * @return
	 */
	public String list() {
		UserManager userManager = ContextHolder.getUserManager();
		StrategyManager strategyManager = ContextHolder.getStrategyManager();
		PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();
		
		profiles = new ArrayList<ProfileItem>();
		if(portfolioSource == 0){//follow
			List<Long> followPortfolioIDList = new ArrayList<Long>();
			List<Long> followIDList = userManager.getResourceIDListByUserIDAndResourceType(userID, Configuration.USER_RESOURCE_PORTFOLIO_FOLLOW);
			List<Long> customizeIDList = userManager.getResourceIDListByUserIDAndResourceType(userID, Configuration.USER_RESOURCE_PORTFOLIO_CUSTOMIZE);
			if(followIDList != null){
				for(Long pID : followIDList){
					if(customizeIDList != null && customizeIDList.contains(pID))
						continue;
					followPortfolioIDList.add(pID);
				}
			}
			for(Long pID : followPortfolioIDList) {
				Portfolio portfolio = portfolioManager.get(pID);
				UserResource userResource = userManager.getUserResourceByUserIDAndResourceIDAndResourceType(userID, pID, Configuration.USER_RESOURCE_PORTFOLIO_FOLLOW);
				String planIDStr = portfolio.getStrategies().getAssetAllocationStrategy().getParameter().get("PlanID");
				if(planIDStr != null) {//是taa/saa
					ProfileItem pi = new ProfileItem();
					pi.setPortfolioID(pID);
					if(userResource != null && userResource.getUpdateTime() != null)
						pi.setStartToFollowDate(LTIDate.parseDateToString(userResource.getUpdateTime()));
					try{
						Long planID = Long.parseLong(planIDStr);
						Strategy plan = strategyManager.get(planID);
						pi.setPlanID(planID);
						pi.setStrategyID(portfolio.getStrategies().getAssetAllocationStrategy().getID());
						pi.setStrategyName(portfolio.getStrategies().getAssetAllocationStrategy().getName());
						//pi.setStartToFollowDate(portfolioManager.getPortfolioLastFollowDate(portfolioID));
						pi.setImitationDate(portfolioManager.getPortfolioLastFollowDate(portfolioID));
						pi.setPlanName(plan.getName());
						List<CachePortfolioItem> pitems = null;
						pitems = portfolioManager.findByHQL("from CachePortfolioItem cp where cp.PortfolioID=" + portfolio.getID() + " and cp.GroupID=0 and cp.RoleID=" + Configuration.ROLE_PORTFOLIO_REALTIME_ID);
						if (pitems != null && pitems.size() > 0) {
							CopyUtil.Translate(pitems.get(0), pi);
						}
						Map<String,String> map = portfolio.getStrategies().getAssetAllocationStrategy().getParameter();
						String fre=map.get("Frequency");
						if(fre==null) fre=map.get("CheckFrequency");
						if(fre==null) fre=map.get("RebalanceFrequency");
						if(fre==null) fre="";
						pi.setFrequency(fre);
						String riskNumber = map.get("RiskProfile");
						if(riskNumber != null)
							pi.setRiskNumber(Double.parseDouble(riskNumber));
					}catch(Exception e){
						
					}
					PublicMaker publicMaker=new PublicMaker(pi);
					pi.setPublic(publicMaker.isPublic());
					profiles.add(pi);
				}
			}
		}else{//customize可以直接从profile那里拿
			List<Long> customizeIDList = userManager.getResourceIDListByUserIDAndResourceType(userID, Configuration.USER_RESOURCE_PORTFOLIO_CUSTOMIZE);
			DetachedCriteria dc = DetachedCriteria.forClass(Profile.class);
			dc.add(Restrictions.eq("UserID", userID));
			dc.addOrder(Order.desc("UpdateTime"));
			List<Profile> pros = profileManager.getProfiles(dc);
			for (int i = 0; i < pros.size(); i++) {
				Profile p = pros.get(i);
				if(customizeIDList == null || !customizeIDList.contains(p.getPortfolioID()))
					continue;
				p.setIsEMailAlert(true);
				ProfileItem pi = new ProfileItem();
				CopyUtil.Translate(p, pi);
				List<CachePortfolioItem> pitems = null;
				pitems = portfolioManager.findByHQL("from CachePortfolioItem cp where cp.PortfolioID=" + p.getPortfolioID() + " and cp.GroupID=0 and cp.RoleID=" + Configuration.ROLE_PORTFOLIO_REALTIME_ID);
				if (pitems != null && pitems.size() > 0) {
					CopyUtil.Translate(pitems.get(0), pi);
				}
				Long portfolioID = pi.getPortfolioID();
				UserResource userResource = userManager.getUserResourceByUserIDAndResourceIDAndResourceType(userID, portfolioID, Configuration.USER_RESOURCE_PORTFOLIO_FOLLOW);
				try {
					pi.setImitationDate(portfolioManager.getPortfolioLastFollowDate(portfolioID));
					if(userResource != null && userResource.getUpdateTime() != null)
						pi.setStartToFollowDate(LTIDate.parseDateToString(userResource.getUpdateTime()));
					Portfolio port = portfolioManager.get(p.getPortfolioID());
					pi.setStrategyID(port.getStrategies().getAssetAllocationStrategy().getID());
					pi.setStrategyName(port.getStrategies().getAssetAllocationStrategy().getName());
					Map<String,String> map=port.getStrategies().getAssetAllocationStrategy().getParameter();
					
					String fre=map.get("Frequency");
					if(fre==null){
						fre=map.get("CheckFrequency");
					}
					if(fre==null){
						fre=map.get("RebalanceFrequency");
					}
					if(fre==null){
						fre="";
					}
					pi.setFrequency(fre);
					String riskNumber = map.get("RiskProfile");
					if(riskNumber != null)
						pi.setRiskNumber(Double.parseDouble(riskNumber));
				} catch (Exception e) {
				}
				PublicMaker publicMaker=new PublicMaker(pi);
				pi.setPublic(publicMaker.isPublic());
				profiles.add(pi);
			}
		}
		
		return Action.SUCCESS;
	}
	
	private List<Strategy> planList;
	
	public String listexpiredplan() {
		UserManager userManager = ContextHolder.getUserManager();
		StrategyManager strategyManager = ContextHolder.getStrategyManager();
		List<UserResource> expiredPlanList = userManager.getUserResourceByUserIDAndResourceType(userID, Configuration.USER_RESOURCE_PLAN_CREATE_EXPIRED);
		planList = new ArrayList<Strategy>();
		if(expiredPlanList != null && expiredPlanList.size() > 0) {
			for(UserResource userResource : expiredPlanList) {
				Strategy plan = strategyManager.get(userResource.getResourceId());
				if(plan != null)
					planList.add(plan);
			}
		}
		return Action.SUCCESS;
	}
	
	
	public String canactivateexpiredplannum() {
		MYPLANIQPermissionManager mpm = new MYPLANIQPermissionManager();
		int count = mpm.getAllowPlanCreateNum(userID);
		message = count + "";
		return Action.MESSAGE;
	}
	
	public String canactivateexpiredportfolionum() {
		MYPLANIQPermissionManager mpm = new MYPLANIQPermissionManager();
		int count = mpm.getAllowPlanCreateNum(userID);
		message = count + "";
		return Action.MESSAGE;
	}
	
	public String expiredportfolios() {
		UserManager userManager = ContextHolder.getUserManager();
		PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();
		StrategyManager strategyManager = ContextHolder.getStrategyManager();
		profiles = new ArrayList<ProfileItem>();
		List<UserResource> expiredPortfolioList = new ArrayList<UserResource>();
		List<UserResource> expiredCustomizePortfolioList = userManager.getUserResourceByUserIDAndResourceType(userID, Configuration.USER_RESOURCE_PORTFOLIO_CUSTOMIZE_EXPIRED);
		List<UserResource> expiredFollowPortfolioList = userManager.getUserResourceByUserIDAndResourceType(userID, Configuration.USER_RESOURCE_PORTFOLIO_FOLLOW_EXPIRED);
		if(expiredCustomizePortfolioList != null)
			expiredPortfolioList.addAll(expiredCustomizePortfolioList);
		if(expiredFollowPortfolioList != null)
			expiredCustomizePortfolioList.addAll(expiredFollowPortfolioList);
		if(expiredPortfolioList != null && expiredPortfolioList.size() > 0) {
			for(UserResource ur : expiredPortfolioList) {
				ProfileItem pi = new ProfileItem();
				Portfolio p = portfolioManager.get(ur.getResourceId());
				if(p != null){
					List<CachePortfolioItem> pitems = null;
					pitems = portfolioManager.findByHQL("from CachePortfolioItem cp where cp.PortfolioID=" + p.getID() + " and cp.GroupID=0 and cp.RoleID=" + Configuration.ROLE_PORTFOLIO_REALTIME_ID);
					if (pitems != null && pitems.size() > 0) {
						CopyUtil.Translate(pitems.get(0), pi);
					}
					Long planID = p.getPlanID();
					if(planID != null){
						pi.setPlanID(p.getPlanID());
						Strategy plan = strategyManager.get(planID);
						pi.setPlanName(plan.getName());
					}
					pi.setPortfolioID(p.getID());
					pi.setPortfolioName(p.getName());
					pi.setStrategyID(p.getStrategies().getAssetAllocationStrategy().getID());
					pi.setStrategyName(p.getStrategies().getAssetAllocationStrategy().getName());
					profiles.add(pi);
				}
			}
		}
		return Action.SUCCESS;
	}
	
	
	/**
	 * 激活某一个过期的portfolio，如果这个portfolio以前是用户创建的，则将其加入到customized portfolio表中，如果不是用户创建的，则加入到follow 表中
	 */
	public String activateexpiredportfolio() throws Exception {
		PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();
		Portfolio portfolio = portfolioManager.get(portfolioID);
		planID = portfolio.getPlanID();
		if(portfolio.isPlanPortfolio()){
			//检查plan是否存在
			if (!isPlanExist(planID)) {
				message = "The plan doesn't exist.";
				ServletActionContext.getResponse().setStatus(500);
				return Action.MESSAGE;
			}
		}
		portfolio=ContextHolder.getPortfolioManager().get(portfolioID);
		//权限检查
		if(!checkFollowPermission(userID, portfolio, planID)){
			ServletActionContext.getResponse().setStatus(500);
			return Action.MESSAGE;
		}
		MYPLANIQPermissionManager mpm = new MYPLANIQPermissionManager();
		UserManager userManager = ContextHolder.getUserManager();
		UserResource customizeResource = userManager.getUserResourceByUserIDAndResourceIDAndResourceType(userID, portfolioID, Configuration.USER_RESOURCE_PORTFOLIO_CUSTOMIZE_EXPIRED);
		if(customizeResource != null) {
			//设置email
			setEmailNotification(userID, portfolioID);
			//添加资源，调整资源数
			mpm.afterPortfolioCustomize(userID, portfolioID, portfolio.getPlanID());
			UserResource ur = userManager.getUserResourceByUserIDAndResourceIDAndResourceType(userID, portfolioID, Configuration.USER_RESOURCE_PORTFOLIO_FOLLOW);
			ur.setUpdateTime(customizeResource.getUpdateTime());
			userManager.updateUserResourse(ur);
			userManager.deleteUserResource(customizeResource);
			message = "This portfolio has been added to \" My Customized Private Portfolios\" table. You will receive rebalance email alerts of this portfolio. You can customize/follow " + mpm.getAllowPortfolioFollowNum(userID) + " more portfolio(s)";
		}else{
			UserResource followResource = userManager.getUserResourceByUserIDAndResourceIDAndResourceType(userID, portfolioID, Configuration.USER_RESOURCE_PORTFOLIO_FOLLOW_EXPIRED);
			if(followResource != null) {
				//设置email
				setEmailNotification(userID, portfolioID);
				//添加资源，调整资源数
				mpm.afterPortfolioFollow(userID, portfolio, portfolio.getPlanID());
				UserResource ur = userManager.getUserResourceByUserIDAndResourceIDAndResourceType(userID, portfolioID, Configuration.USER_RESOURCE_PORTFOLIO_FOLLOW);
				ur.setUpdateTime(followResource.getUpdateTime());
				userManager.updateUserResourse(ur);
				userManager.deleteUserResource(followResource);
				message = "This portfolio has been added to \" My Followed Public Portfolios\" table. You will receive rebalance email alerts of this portfolio. You can customize/follow " + mpm.getAllowPortfolioFollowNum(userID) + " more portfolio(s)";
			}
		}
		return Action.MESSAGE;
	}
	/**
	 * 激活某一个过期的plan
	 * @return
	 * @throws Exception
	 */
	public String activateexpiredplan() throws Exception {
		if(!checkcreateplan(userID)) {
			ServletActionContext.getResponse().setStatus(500);
			return Action.MESSAGE;
		}
		UserManager userManager = ContextHolder.getUserManager();
		UserResource planResource = userManager.getUserResourceByUserIDAndResourceIDAndResourceType(userID, planID, Configuration.USER_RESOURCE_PLAN_CREATE_EXPIRED);
		if(planResource != null) {
			mpm.afterPlanCreate(userID, planID);
			userManager.deleteUserResource(planResource);
			message = "This plan has been added to \" My Plans \" table. You can create " + mpm.getAllowPlanCreateNum(userID) + " more plan(s)";
		}
		return Action.MESSAGE;
	}
	
	
	/**
	 * 检查是否有权限创建
	 * 检查数目限制
	 * @return
	 */
	public boolean checkcreateplan(Long userID){
		MYPLANIQPermissionManager mpm = new MYPLANIQPermissionManager();
		int operationCode = mpm.canPlanCreate(userID);
		if(operationCode == mpm.SUCCESS)
			return true;
		if(operationCode == mpm.PERMISSION_LIMIT){
			message = "You need to subscribe as a Expert User or a Pro User to get the permission of creating plans.";
		}else if(operationCode == mpm.COUNT_LIMIT){
			message = "You have reached the maximum number of your own plans. To create a new plan, please delete one of your plans in the \"My Portfolio\" page, or subscribe as a higher level user to obtain the permission for more own plans.";
		}
		return false;
	}
	/**
	 * 检查是否可以follow
	 * @param userID
	 * @param portfolioID
	 * @param planID
	 * @return
	 */
	private boolean checkFollowPermission(Long userID, Portfolio portfolio, Long planID) {
		int operationCode = mpm.canPortfolioFollow(userID, portfolio, planID);
		if(operationCode == mpm.SUCCESS)
			return true;
		if(operationCode == mpm.PERMISSION_SAA_LIMIT)
			message = "You need to register for free to follow SAA portfolios. Please login or register first.";
		else if(operationCode == mpm.PERMISSION_TAA_LIMIT)
			message = "You need to subscribe as Basic User to follow the TAA portfolios. Please subscribe for one month free trial.";
		else if(operationCode == mpm.COUNT_LIMIT)
			message = "You have reached the maximum number of Customized/followed portfolios. To follow/customize another portfolio, please remove one portfolio in the “My Portfolio” page, or subscribe as a higher level user to obtain the permission for more customized/followed portfolios.";
			//message = "You have already customized/followed " + mpm.getMaxPortfolioFollowNum(userID) + " portfolio(s)";
		else if(operationCode == mpm.PLAN_REF_COUNT_LIMIT){
			message = "You have already referenced " + mpm.getMaxPlanRefNum(userID) + " plan(s)";
		}
		return false;
	}
	
	/**
	 * 列出某用户的所有profiles，Your Current Portfolio列表
	 * @param profiles
	 * @param userID
	 */
	public String list_bk() {
		PortfolioManager pm = (PortfolioManager) ContextHolder.getInstance().getApplicationContext().getBean("portfolioManager");
		DetachedCriteria dc = DetachedCriteria.forClass(Profile.class);
		UserManager userManager = ContextHolder.getUserManager();
		dc.add(Restrictions.eq("UserID", userID));
		dc.addOrder(Order.desc("UpdateTime"));
		List<Profile> pros = profileManager.getProfiles(dc);
		profiles = new ArrayList<ProfileItem>();
		MPIQChecker fc=new MPIQChecker(userID);
		boolean hasSubscred=fc.hasSubscred();
		for (int i = 0; i < pros.size(); i++) {
			Profile p = pros.get(i);
			
			EmailNotification en = userManager.getEmailNotification(userID, p.getPortfolioID());
			p.setIsEMailAlert(en != null);
			ProfileItem pi = new ProfileItem();
			CopyUtil.Translate(p, pi);
			List<CachePortfolioItem> pitems = null;
			if(hasSubscred)pitems = ContextHolder.getPortfolioManager().findByHQL("from CachePortfolioItem cp where cp.PortfolioID=" + p.getPortfolioID() + " and cp.GroupID=0 and cp.RoleID=" + Configuration.ROLE_PORTFOLIO_REALTIME_ID);
			else pitems = ContextHolder.getPortfolioManager().findByHQL("from CachePortfolioItem cp where cp.PortfolioID=" + p.getPortfolioID() + " and cp.GroupID=0 and cp.RoleID=" + Configuration.ROLE_PORTFOLIO_DELAYED_ID);
			if (pitems != null && pitems.size() > 0) {
				CopyUtil.Translate(pitems.get(0), pi);
			}
			Long portfolioID = pi.getPortfolioID();
			pi.setStartToFollowDate(pm.getPortfolioLastFollowDate(portfolioID));
			
			try {
				Portfolio port = pm.get(p.getPortfolioID());
				pi.setStrategyID(port.getStrategies().getAssetAllocationStrategy().getID());
				pi.setStrategyName(port.getStrategies().getAssetAllocationStrategy().getName());
				Map<String,String> map=port.getStrategies().getAssetAllocationStrategy().getParameter();
				
				String fre=map.get("Frequency");
				if(fre==null){
					fre=map.get("CheckFrequency");
				}
				if(fre==null){
					fre=map.get("RebalanceFrequency");
				}
				if(fre==null){
					fre="";
				}
				pi.setFrequency(fre);
				String riskNumber = map.get("RiskProfile");
				if(riskNumber != null)
					pi.setRiskNumber(Double.parseDouble(riskNumber));
			} catch (Exception e) {
			}
			PublicMaker publicMaker=new PublicMaker(pi);
			pi.setPublic(publicMaker.isPublic());
			profiles.add(pi);
		}

		return Action.SUCCESS;
	}

	private boolean inprofiles(List<Profile> pros,long id){
		if(pros==null)return false;
		for(Profile p:pros){
			if(p.getPortfolioID().equals(id))return true;
		}
		return false;
	}
	
	private void fit(Portfolio portfolio,PortfolioManager portfolioManager,UserManager userManager){
		ProfileItem pi = new ProfileItem();
		if(portfolioSource ==0 ||portfolioSource==1){
			UserResource userResource = userManager.getUserResourceByUserIDAndResourceIDAndResourceType(userID, portfolio.getID(), Configuration.USER_RESOURCE_PORTFOLIO_FOLLOW);
			if(userResource.getUpdateTime() != null)
				pi.setStartToFollowDate(LTIDate.parseDateToString(userResource.getUpdateTime()));
			pi.setImitationDate(portfolioManager.getPortfolioLastFollowDate(portfolio.getID()));
		}
		
		EmailNotification en = userManager.getEmailNotification(userID, portfolio.getID());
		List<CachePortfolioItem> pitems = null;
		pitems = portfolioManager.findByHQL("from CachePortfolioItem cp where cp.PortfolioID=" + portfolio.getID() + " and cp.GroupID=0 and cp.RoleID=" + Configuration.ROLE_PORTFOLIO_REALTIME_ID);
		if (pitems != null && pitems.size() > 0) {
			CopyUtil.Translate(pitems.get(0), pi);
		}
		pi.setIsEMailAlert(en!=null);
		pi.setPlanID(0l);
		pi.setPlanName("N/A");
		PublicMaker publicMaker=new PublicMaker(pi);
		pi.setPublic(publicMaker.isPublic());
		pi.setStrategyID(portfolio.getStrategies().getAssetAllocationStrategy().getID());
		pi.setStrategyName(portfolio.getStrategies().getAssetAllocationStrategy().getName());
		
		profiles.add(pi);
	}
	
	/**
	 * user advance portfolio list
	 * @return
	 */
	public String advancedlist() {
		PortfolioManager pm = (PortfolioManager) ContextHolder.getInstance().getApplicationContext().getBean("portfolioManager");
		UserManager userManager = ContextHolder.getUserManager();
		MYPLANIQPermissionManager mpm = new MYPLANIQPermissionManager();
		profiles = new ArrayList<ProfileItem>();
		//portfolioSource==1或者portfolioSource==2
		if(portfolioSource ==0 ||portfolioSource==1){
			List<Long> portfolioList = userManager.getResourceIDListByUserIDAndResourceType(userID, Configuration.USER_RESOURCE_PORTFOLIO_FOLLOW);
			if(portfolioList != null && portfolioList.size()>0){
				for(Long portID : portfolioList){
					Portfolio portfolio = pm.get(portID);
					if(portfolio != null && !portfolio.isPlanPortfolio()){
						
						boolean iscustomize = mpm.hasUserPortfolioCustomize(userID, portfolio);
						
						boolean type = portfolio.isType(Configuration.PORTFOLIO_TYPE_COMPOUND);
						if((portfolioSource == 1 && iscustomize && !type) || (portfolioSource == 0 && !iscustomize && !type)){
							fit(portfolio, pm, userManager);
						}
					}
				}
			}
		}
		//portfolioSource==1或者portfolioSource==2 结束
		
		//personal portfolio
		if(portfolioSource==3){
			List<Portfolio> pps=pm.getPortfoliosByUser(userID, Configuration.PORTFOLIO_TYPE_PERSONAL);
			for(Portfolio portfolio:pps){
				if(portfolio != null)
					fit(portfolio, pm, userManager);
			}
		}
		//personal portfolio 结束
		
		//plan 页面的portfolio 己停用
		if(portfolioSource==4){
			List<Long> portfolioList = userManager.getResourceIDListByUserIDAndResourceTypeAndRelationID(userID, Configuration.USER_RESOURCE_PORTFOLIO_ON_PLAN_PAGE,planID);
			if(portfolioList != null && portfolioList.size()>0){
				for(Long portID : portfolioList){
					Portfolio portfolio = pm.get(portID);
					if(portfolio != null ){
						fit(portfolio, pm, userManager);
					}
				}
			}
		}
		//plan 页面的portfolio 结束
		return Action.SUCCESS;
	}
	/*
	 * user aggregate list
	 */
	public String aggregatelist(){
		PortfolioManager pm = (PortfolioManager) ContextHolder.getInstance().getApplicationContext().getBean("portfolioManager");
		UserManager userManager = ContextHolder.getUserManager();
		profiles=new ArrayList<ProfileItem>();
		List<Long> portfolioList = userManager.getResourceIDListByUserIDAndResourceType(userID, Configuration.USER_RESOURCE_PORTFOLIO_FOLLOW);
		if(portfolioList != null && portfolioList.size()>0){
			for(Long portID : portfolioList){
				Portfolio portfolio = pm.get(portID);
				if(portfolio != null && !portfolio.isPlanPortfolio()){//判断非TAA/SAA
					boolean iscustomize = mpm.hasUserPortfolioCustomize(userID, portfolio);
					boolean type = portfolio.isType(Configuration.PORTFOLIO_TYPE_COMPOUND);
					if(iscustomize && type){
						fit(portfolio, pm, userManager);
					}
				}
			}
		}
		return Action.SUCCESS;
	}
	/**
	 * 如果编辑时不存在相应的default profile自动生成一个
	 */
	public String editforselecting(){
		if (portfolioID == 0 && planID == 0) {
			Profile _default = profileManager.get(portfolioID, userID, planID);
			if (_default == null) {
				_default=profileManager.get(portfolioID, Configuration.SUPER_USER_ID, planID);
				_default.setPlanID(0l);
				_default.setPortfolioID(0l);
				_default.setUserID(userID);
				_default.setPlanName("STATIC");
				_default.setPortfolioName("New Portfolio");
				_default.setUserName(ContextHolder.getUserManager().getLoginUser().getUserName());
				profileManager.save(_default);
			} 
		}
		return edit();
	}
	
	/**
	 * CRUD　中转D
	 */
	public String edit() {
		if (userID.equals(Configuration.USER_ANONYMOUS))
			return Action.LOGIN;
		if (operation.equals(Action.ACTION_VIEW)) {
			return view();
		} else if (operation.equals(Action.ACTION_UPDATE)) {
			return update();
		} else if (operation.equals(Action.ACTION_CREATE)) {
			return create();
		} else if (operation.equals(Action.ACTION_SAVE)) {
			return save();
		}
		return Action.ERROR;

	}

	/**
	 * 查看profile，如果为default profile且不存在会跳到创建页面
	 */
	public String view() {
		if (portfolioID == 0 && planID == 0) {
			Profile _default = profileManager.get(portfolioID, userID, planID);
			if (_default == null) {
				params = "operation=create";
				return Action.REDIRECT_EDIT;
			} else {
				profile = _default;
				return Action.SUCCESS;
			}
		}
		profile = profileManager.get(portfolioID, userID, planID);
		return Action.SUCCESS;
	}

	/**
	 * 更新profile
	 */
	public String update() {
		if (!checkPermission(userID, planID, portfolioID))
			return Action.ERROR;
		profile.setPlanID(planID);
		profile.setUserID(userID);
		profile.setPortfolioID(portfolioID);
		profileManager.update(profile);
		params = "portfolioID=" + portfolioID + "&planID=" + planID;
		return Action.REDIRECT_EDIT;
	}

	/**
	 * 新增一个profile
	 */
	public String save() {
		try {
			planID = profile.getPlanID();
			portfolioID = profile.getPortfolioID();
			profile.setUserID(userID);
			profile.setUserName(ContextHolder.getUserManager().getLoginUser().getUserName());
			if (portfolioID == 0 && planID == 0) {
				Profile _default = profileManager.get(portfolioID, userID, planID);
				if (_default != null) {
					profileManager.update(profile);
				} else {
					profile.setPlanName("STATIC");
					profile.setPortfolioName("New Portfolio");
					profileManager.save(profile);
				}
			} else {
				Portfolio p=ContextHolder.getPortfolioManager().get(portfolioID);
				if(p!=null){
					profile.setPortfolioName(p.getName());
				}
				Strategy plan=ContextHolder.getStrategyManager().get(planID);
				if(plan!=null){
					profile.setPlanName(plan.getName());
				}
				profileManager.save(profile);
			}
			params = "portfolioID=" + portfolioID + "&planID=" + planID;
			return Action.REDIRECT_EDIT;
		} catch (Exception e) {
			e.printStackTrace();
			message = "Failed to create profile.error:-1";
		}
		return Action.MESSAGE;
	}

	/**
	 * 准备创建一个profile
	 */
	public String create() {
		profile = new Profile();
		return Action.SUCCESS;
	}

	/**
	 * 删除一个profile
	 */
	public String delete() {
		if (!checkPermission(userID, planID, portfolioID))
			return Action.ERROR;
		Profile pro = profileManager.get(portfolioID, userID, planID);
		if (pro != null) {
			profileManager.delete(portfolioID, userID, planID);
			if (pro.getIsGenerated()) {
				PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();
				try {
					portfolioManager.remove(pro.getPortfolioID());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}

		return Action.SUCCESS;
	}

	private List<List<String>> performance;

	/**
	 * 获取portfolio的性能参数
	 */
	public String getperformance() {
		PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();
		portfolio = portfolioManager.get(portfolioID);
		UserManager userManager = ContextHolder.getUserManager();
		User user = userManager.getLoginUser();
		PortfolioPermissionChecker pc = new PortfolioPermissionChecker(portfolio, ServletActionContext.getRequest());

		List<CachePortfolioItem> pitems = null;
		if (pc.hasRealtimeRole()) {
			pitems = ContextHolder.getPortfolioManager().findByHQL("from CachePortfolioItem cp where cp.PortfolioID=" + portfolioID + " and cp.GroupID=0 and cp.RoleID=" + Configuration.ROLE_PORTFOLIO_REALTIME_ID);
		} else {
			pitems = ContextHolder.getPortfolioManager().findByHQL("from CachePortfolioItem cp where cp.PortfolioID=" + portfolioID + " and cp.GroupID=" + Configuration.GROUP_MPIQ_ID + " and cp.RoleID=" + Configuration.ROLE_PORTFOLIO_DELAYED_ID);
			if (pitems == null || pitems.size() == 0) {
				CachePortfolioItem cpi = new CachePortfolioItem();
				PortfolioMPT p1 = portfolioManager.getPortfolioMPT(portfolioID, PortfolioMPT.DELAY_LAST_ONE_YEAR);
				PortfolioMPT p3 = portfolioManager.getPortfolioMPT(portfolioID, PortfolioMPT.DELAY_LAST_THREE_YEAR);
				PortfolioMPT p5 = portfolioManager.getPortfolioMPT(portfolioID, PortfolioMPT.DELAY_LAST_FIVE_YEAR);
				cpi.setPortfolioID(portfolioID);
				cpi.setPortfolioName(portfolio.getName());
				if (p1 != null) {
					cpi.setAR1(p1.getAR());
					cpi.setSharpeRatio1(p1.getSharpeRatio());
				}
				if (p3 != null) {
					cpi.setAR3(p3.getAR());
					cpi.setSharpeRatio3(p3.getSharpeRatio());
				}
				if (p5 != null) {
					cpi.setAR5(p5.getAR());
					cpi.setSharpeRatio5(p5.getSharpeRatio());
				}

				pitems.add(cpi);
			}
		}

		performance = new ArrayList<List<String>>();
		List<String> mpts = new ArrayList<String>();

		if (pitems != null && pitems.size() > 0) {
			mpts.add(getplink(pitems.get(0).getPortfolioName(), pitems.get(0).getPortfolioID()));
			try {
				mpts.add((int) (pitems.get(0).getAR1() * 100 + 0.5) + "%");
			} catch (Exception e) {
				mpts.add("");
			}
			// mpts.add((int)(pitems.get(0).getSharpeRatio1()*100+0.5) + "%");
			try {
				mpts.add((int) (pitems.get(0).getAR3() * 100 + 0.5) + "%");
			} catch (Exception e) {
				mpts.add("");
			}
			// mpts.add((int)(pitems.get(0).getSharpeRatio3()*100+0.5) + "%");
			try {
				mpts.add((int) (pitems.get(0).getAR5() * 100 + 0.5) + "%");
			} catch (Exception e) {
				mpts.add("");
			}
			// mpts.add((int)(pitems.get(0).getSharpeRatio5()*100+0.5) + "%");
		} else {
			mpts.add("P_" + portfolioID);
			mpts.add("");
			mpts.add("");
			mpts.add("");
			// mpts.add("");
			// mpts.add("");
			// mpts.add("");
		}
		performance.add(mpts);

		SecurityManager securityManager = ContextHolder.getSecurityManager();

		Security vfinx = securityManager.get("vfinx");
		Security vbinx = securityManager.get("vbinx");
		long[] ids = new long[] { vfinx.getID(), vbinx.getID() };
		long[] years = new long[] { -1, -3, -5 };

		for (int i = 0; i < ids.length; i++) {
			mpts = new ArrayList<String>();
			if (ids[i] == vfinx.getID()) {
				mpts.add(getlink("VFINX (Vanguard (S&P 500) Index)", "vfinx"));
			} else {
				mpts.add(getlink("VBINX (Vanguard Balance (60% stocks/40% bonds)", "vbinx"));
			}
			for (int j = 0; j < years.length; j++) {
				SecurityMPT mpt1 = securityManager.getSecurityMPT(ids[i], years[j]);
				if (mpt1 != null) {
					mpts.add((int) (mpt1.getAR() * 100 + 0.5) + "%");
					// mpts.add((int)(mpt1.getSharpeRatio()*100+0.5) + "%");
				} else {
					mpts.add("");
					// mpts.add("");
				}
			}
			performance.add(mpts);
		}

		return Action.SUCCESS;
	}
	
	private String getlink(String name, String symbol){
		return "<a href='/LTISystem/jsp/fundcenter/View.action?symbol="+symbol+"&type=4' target='_blank'>"+name+"</a>";
	}
	private String getplink(String name, long id){
		return "<a href='/LTISystem/jsp/portfolio/ViewPortfolio.action?ID="+id+"&type=4' target='_blank'>"+name+"</a>";
	}

	/**
	 * 获取portfolio pie chart url
	 * 应该封装为一个API
	 */
	public String getpiecharturl() {
		PortfolioManager portfolioManager=ContextHolder.getPortfolioManager();
		piechart = "nochart.jpg";
		portfolio=portfolioManager.get(portfolioID);
		PermissionChecker pc=new PortfolioPermissionChecker(portfolio,ServletActionContext.getRequest());
		message=pc.hasRealtimeRole()==true?"2":"3";
		return Action.MESSAGE;
	}

	/**
	 * 获取当前portfolio的模拟状态
	 */
	public String getgeneratingstate() {
		PortfolioState ps = ContextHolder.getPortfolioManager().getPortfolioState(portfolioID);
		if (ps != null && ps.getState() != null) {
			message = ps.getState() + "";
		} else {
			message = Configuration.PORTFOLIO_RUNNING_STATE_PREPARING + "";
		}
		return Action.MESSAGE;
	}

	/**
	 * 开始customize portfolio
	 */
	public String customizeportfolio() {
		if (portfolioID == null)
			portfolioID = 0l;
		if (planID == null)
			planID = 0l;
		profile = profileManager.get(portfolioID, userID, planID);
		if (profile == null) {
			if (portfolioID == 0l && planID == 0l) {
				params = "operation=create";
				return Action.REDIRECT_EDIT;
			} else {
				return Action.ERROR;
			}

		}

		return Action.SUCCESS;
	}

	private Portfolio portfolio;

	public Portfolio getPortfolio() {
		return portfolio;
	}

	public void setPortfolio(Portfolio portfolio) {
		this.portfolio = portfolio;
	}

	private Strategy plan;
	public Strategy getPlan() {
		return plan;
	}

	public void setPlan(Strategy plan) {
		this.plan = plan;
	}

	/**
	 * 模拟portfolio中的页面
	 */
	public String customizing() {
		if (portfolioID == null)
			portfolioID = 0l;
		if (planID == null)
			planID = 0l;
		profile = profileManager.get(portfolioID, userID, planID);
		if (profile == null) {
			if (portfolioID == 0l && planID == 0l) {
				params = "operation=create";
				return Action.REDIRECT_EDIT;
			} else {
				return Action.ERROR;
			}

		}
		portfolio = ContextHolder.getPortfolioManager().get(profile.getPortfolioID());
		if (portfolio == null) {
			message = "The portfolio does not exist.";
			return Action.ERROR;
		}
		plan = ContextHolder.getStrategyManager().get(planID);

		return Action.SUCCESS;
	}
	
	private String piechart;
	public String getPiechart() {
		return piechart;
	}

	public void setPiechart(String piechart) {
		this.piechart = piechart;
	}

	private HoldingInf holding;
	/**
	 * 显示customize后结果的页面
	 * 此页面需要修改
	 */
	public String customizedresult() {
		if (portfolioID == null)
			portfolioID = 0l;
		if (planID == null)
			planID = 0l;
		profile = profileManager.get(portfolioID, userID, planID);
		if (profile == null) {
			if (portfolioID == 0l && planID == 0l) {
				params = "operation=create";
				return Action.REDIRECT_EDIT;
			} else {
				message=("The profile doesn't exist or the portfolio is not created by you.");
				return Action.MESSAGE;
				
			}
		}
		EmailNotification en = ContextHolder.getUserManager().getEmailNotification(userID, profile.getPortfolioID());
		profile.setIsEMailAlert(en != null);
		portfolio = ContextHolder.getPortfolioManager().get(profile.getPortfolioID());
		PermissionChecker pc=new PortfolioPermissionChecker(portfolio, ServletActionContext.getRequest());
		if(!pc.hasRealtimeRole()){
			holding=ContextHolder.getPortfolioManager().getHolding(profile.getPortfolioID(), Configuration.PORTFOLIO_HOLDING_DELAY);
			portfolio.setEndDate(holding.getCurrentDate());	
		}else{
			holding=ContextHolder.getPortfolioManager().getHolding(profile.getPortfolioID(), Configuration.PORTFOLIO_HOLDING_CURRENT);
		}
		if (holding == null || portfolio == null) {
			message = "The portfolio does not exist or you have not subscribed yet.";
			return Action.MESSAGE;
		}
		plan = ContextHolder.getStrategyManager().get(planID);
		
		//getpiecharturl();

		return Action.SUCCESS;
	}
	/**
	 * 显示customize后，查看portfolio的信息的页面
	 * 此页面需要修改
	 */
	public String manageyourportfolio() {
		if (portfolioID == null)
			portfolioID = 0l;
		if (planID == null)
			planID = 0l;
		profile = profileManager.get(portfolioID, userID, planID);
		if (profile == null) {
			if (portfolioID == 0l && planID == 0l) {
				params = "operation=create";
				return Action.REDIRECT_EDIT;
			}
			profile=new Profile();
			profile.setPortfolioID(portfolioID);
			profile.setPlanID(planID);
		}
		EmailNotification en = ContextHolder.getUserManager().getEmailNotification(userID, profile.getPortfolioID());
		profile.setIsEMailAlert(en != null);
		portfolio = ContextHolder.getPortfolioManager().get(profile.getPortfolioID());
		PermissionChecker pc=new PortfolioPermissionChecker(portfolio, ServletActionContext.getRequest());
		if(!pc.hasRealtimeRole()){
			holding=ContextHolder.getPortfolioManager().getHolding(profile.getPortfolioID(), Configuration.PORTFOLIO_HOLDING_DELAY);
		}else{
			holding=ContextHolder.getPortfolioManager().getHolding(profile.getPortfolioID(), Configuration.PORTFOLIO_HOLDING_CURRENT);
		}
		if (holding == null) {
			message = "The portfolio does not exist or you have not subscribed yet.";
			return Action.MESSAGE;
		}
		if (holding == null || portfolio == null) {
			message = "The portfolio does not exist or you have not subscribed yet.";
			return Action.MESSAGE;
		}
		plan = ContextHolder.getStrategyManager().get(planID);
		
		//getpiecharturl();

		return Action.SUCCESS;
	}

	public String checkname() {
		message = "false";
		if (profile != null && profile.getPortfolioName() != null) {
			if (ContextHolder.getPortfolioManager().get(StringUtil.getValidName2(profile.getPortfolioName()).trim()) == null) {
				message = "true";
			}
		}
		return Action.MESSAGE;
	}

	public HoldingInf getHolding() {
		return holding;
	}

	public void setHolding(HoldingInf holding) {
		this.holding = holding;
	}

	private String strategyName;
	private Long strategyID;
	private String frequency;
	private Integer maxOfRiskyAsset = 2;
	private Integer numberOfMainRiskyClass = 2;

	/**
	 * 检查用户是否已经generate过15以上的portfolio
	 */
	private boolean check(){
		if(userID==Configuration.SUPER_USER_ID)return true;
		
		int count=20;
		ProfileManager pm=(ProfileManager) ContextHolder.getInstance().getApplicationContext().getBean("profileManager");
		int size=count+1;
		try {
			size=pm.getProfilesByUserID(userID).size();
		} catch (Exception e) {	}
		if(size<=count)return true;
		return false;
	}
	
	
	private String useSMAFilter;
	private String useWMAFilter;
	private String useEMAFilter;
	private String emaValue;
	private String smaValue;
	private String wmaValue;
	private String wmaFreq;
	private String emaFreq;
	private String smaFreq;
	private String MainAssetClass;
	private String MainAssetClassWeight;
	private String AssetFundString;
	private String BuyThreshold;
	private String SellThreshold;
	private String SpecifyAssetFund;
	private String UseDataObject;
	
	private String changeFreqToDays(String valueList, String freqList){
		String result = "";
		if(valueList != null){
			String[] values = valueList.split(",");
			String[] freqs = freqList.split(",");
			for(int i=0;i<values.length;++i){
				int num = Integer.parseInt(values[i]);
				if(freqs[i].equals("1")){
					//daily
				}else if(freqs[i].equals("2")){
					//weekly
					num *= 5;
				}else if(freqs[i].equals("3")){
					//monthly
					num *= 22;
				}
				if(i > 0)
					result += ",";
				result += num;
			}
		}
		return result;
	}
	
	/***
	 * 检查plan是否存在
	 * @param planID
	 * @return
	 */
	private boolean isPlanExist(Long planID){
		StrategyManager strategyManager = ContextHolder.getStrategyManager();
		plan = strategyManager.get(planID);
		if (plan != null)
			return true;
		return false;
	}
	/**
	 * 检查是否可以follow
	 * @param userID
	 * @param planID
	 * @param strategyID
	 * @return
	 */
	private boolean checkCustomizePermission(Long userID, Long planID, Long strategyID) {
		int operationCode = mpm.canPortfolioCustomize(userID, planID, strategyID);
		if(operationCode == mpm.SUCCESS)
			return true;
		if(operationCode == mpm.PERMISSION_SAA_LIMIT)
			message = "You need to register for free to customize SAA portfolios. Please login or register first.";
		else if(operationCode == mpm.PERMISSION_TAA_LIMIT)
			message = "You need to subscribe as Basic User to customize the TAA portfolios. Please subscribe for one month free trial.";
		else if(operationCode == mpm.COUNT_LIMIT)
			message = "You have already customized/followed " + mpm.getMaxPortfolioFollowNum(userID) + " portfolio(s)";
		else if(operationCode == mpm.PLAN_REF_COUNT_LIMIT){
			message = "You have already referenced " + mpm.getMaxPlanRefNum(userID) + " plan(s)";
		}
		return false;
	}
	/**
	 * 添加email alert
	 * @param userID
	 * @param portfolioID
	 */
	private void setEmailNotification(Long userID, Long portfolioID) {
		UserManager userManager = ContextHolder.getUserManager();
		PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();
		if(userManager.getEmailNotification(userID, portfolioID)!=null)
			return;
		EmailNotification en = new EmailNotification();
		en.setUserID(userID);
		en.setPortfolioID(portfolioID);
		en.setSpan(0);
		Date today = new Date();
		Date lastSentDate = portfolioManager.getRealTransactionLatestDate(portfolioID, today);
		if (lastSentDate == null) lastSentDate = today;
		en.setLastSentDate(LTIDate.clearHMSM(lastSentDate));
		userManager.addEmailNotification(en);
	}
	/**
	 * 见getstarted的generate及buildingutil
	 * 产生portfolio
	 */
	@Deprecated
	public String generateportfolio() {
		try {
			//检查plan是否存在
			if(!isPlanExist(profile.getPlanID())){
				message = "The plan doesn't exist.";
				ServletActionContext.getResponse().setStatus(500);
				return Action.MESSAGE;
			}
			//权限检查
			if(!checkCustomizePermission(userID, profile.getPlanID(), strategyID)){
				ServletActionContext.getResponse().setStatus(500);
				return Action.MESSAGE;
			}
			
			PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();
			StrategyManager strategyManager = ContextHolder.getStrategyManager();
			Strategy center = strategyManager.get(Configuration.STRATEGY_401K);
			List<Portfolio> templateportfolios = portfolioManager.getModelPortfolios(center.getID());
			Portfolio p = null;
			for (int i = 0; i < templateportfolios.size(); i++) {
				Portfolio tp = templateportfolios.get(i);
				if (tp.getStrategies()!=null&&tp.getStrategies().getAssetAllocationStrategy().getID().equals(strategyID)) {
					p = tp.clone();
					break;
				}
			}
			if (p == null) {
				p = portfolioManager.get(0l).clone();
			}
			p.getStrategies().getAssetAllocationStrategy().setID(strategyID);
			p.getStrategies().getAssetAllocationStrategy().setName(strategyName);
			Map<String, String> ht = p.getStrategies().getAssetAllocationStrategy().getParameter();
			ht.put("UseRiskProfile", "true");
			ht.put("RiskProfile", profile.getRiskNumber() + "");
			if (frequency == null) {
				frequency = "monthly";
			}
			ht.put("MaxOfRiskyAsset", maxOfRiskyAsset + "");
			if(maxOfRiskyAsset != 2){
				ht.put("UseDataObject", "false");
			}
			ht.put("NumberOfMainRiskyClass", numberOfMainRiskyClass + "");
			if(numberOfMainRiskyClass != 2){
				ht.put("UseDataObject", "false");
			}
			if(SpecifyAssetFund.equals("true") && strategyID==771l  ){
				ht.put("MainAssetClass", MainAssetClass);
				ht.put("MainAssetClassWeight", MainAssetClassWeight);
				ht.put("SpecifyAssetFund", "true");
				ht.put("AssetFundString", AssetFundString);
				ht.put("BuyThreshold", BuyThreshold);
				ht.put("SellThreshold", SellThreshold);
				if(UseDataObject.equals("true")){
					ht.put("UseDataObject", "true");
				}else{
					ht.put("UseDataObject", "false");
				}				
			}
			ht.put("EMADays", changeFreqToDays(emaValue, emaFreq));
			ht.put("SMADays", changeFreqToDays(smaValue, smaFreq));
			ht.put("WMADays", changeFreqToDays(wmaValue, wmaFreq));
			ht.put("UseEMAFilter", useEMAFilter);
			ht.put("UseSMAFilter", useSMAFilter);
			ht.put("UseWMAFilter", useWMAFilter);
			ht.put("Frequency", frequency);
			ht.put("CheckFrequency", frequency);
			ht.put("RebalanceFrequency", frequency);
			ht.put("PlanID", profile.getPlanID()+"");
			p.setID(null);
			if (profile.getPortfolioName() != null && !profile.getPortfolioName().equals("")) {
				p.setName(profile.getPortfolioName());
			} else {
				p.setName(profile.getPlanName() + " " + System.currentTimeMillis());
			}
			p.setUserID(userID);

			Strategy plan = strategyManager.get(profile.getPlanID());
			List<VariableFor401k> variables = plan.getVariablesFor401k();
			StringBuffer AssetClass = new StringBuffer();
			StringBuffer CandidateFunds = new StringBuffer();
			StringBuffer RedemptionLimit = new StringBuffer();
			StringBuffer WaitingPeriod = new StringBuffer();
			StringBuffer RoundtripLimit = new StringBuffer();

			if (variables != null) {
				for (int i = 0; i < variables.size(); i++) {
					if (variables.get(i).getMemo() != null && variables.get(i).getMemo().toLowerCase().startsWith("n"))
						continue;
					AssetClass.append(variables.get(i).getAssetClassName());
					CandidateFunds.append(variables.get(i).getSymbol());
					RedemptionLimit.append(variables.get(i).getRedemption());
					WaitingPeriod.append(variables.get(i).getWaitingPeriod());
					RoundtripLimit.append(variables.get(i).getRoundtripLimit());
					if (i != variables.size() - 1) {
						AssetClass.append(",");
						CandidateFunds.append(",");
						RedemptionLimit.append(",");
						WaitingPeriod.append(",");
						RoundtripLimit.append(",");
					}
				}
			}
			ht.put("AssetClass", AssetClass.toString());
			ht.put("CandidateFund", CandidateFunds.toString());
			ht.put("RedemptionLimit", RedemptionLimit.toString());
			ht.put("WaitingPeriod", WaitingPeriod.toString());
			ht.put("RoundtripLimit", RoundtripLimit.toString());

			p.setProduction(true);
			p.setOriginalPortfolioID(null);
			p.setMainStrategyID(null);
			portfolioManager.save(p);
			profile.setPortfolioID(p.getID());
			profile.setIsGenerated(true);
			planID = profile.getPlanID();
			portfolioID = p.getID();
			
			save();
			
			//spc.savePlanAndPortUse(userID, planID, portfolioID);//save the permission data
			//为新产生的portfolio设置email
			setEmailNotification(userID, portfolioID);
			//customize portfolio后的资源和计数操作
			mpm.afterPortfolioCustomize(userID, portfolioID, planID);
			message = mpm.getAllowPortfolioFollowNum(userID) + "_" + portfolioID;
			//message = "This portfolio has been added to \" My Customized Private Portfolios\" table. You will receive rebalance email alerts of this portfolio. You can customize/follow " + mpm.getAllowPortfolioFollowNum(userID) + " more portfolio(s)";
			BuildingUtil.monitor(p.getID());
			//message = p.getID() + "";
		} catch (Exception e) {
			e.printStackTrace();
			message = "-1";
		}
		return Action.MESSAGE;
	}

	public String saveparameter(){
		PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();
		Portfolio p = portfolioManager.get(portfolioID);
		Map<String, String> ht = p.getStrategies().getAssetAllocationStrategy().getParameter();
		ht.put("RiskProfile", profile.getRiskNumber() + "");
		if (frequency == null) {
			frequency = "monthly";
		}
		ht.put("EMADays", changeFreqToDays(emaValue, emaFreq));
		ht.put("SMADays", changeFreqToDays(smaValue, smaFreq));
		ht.put("WMADays", changeFreqToDays(wmaValue, wmaFreq));
		ht.put("UseEMAFilter", useEMAFilter);
		ht.put("UseSMAFilter", useSMAFilter);
		ht.put("UseWMAFilter", useWMAFilter);
		ht.put("Frequency", frequency);
		ht.put("CheckFrequency", frequency);
		ht.put("RebalanceFrequency", frequency);
		ht.put("MaxOfRiskyAsset", maxOfRiskyAsset+"");
		portfolioManager.update(p);
		message = "save successful";
		return Action.MESSAGE;
	}
	/**
	 * 复制portfolio
	 */
	public String copyportfolio() {
		try {
			//检查plan是否存在
			if(!isPlanExist(profile.getPlanID())){
				message = "The plan doesn't exist.";
				ServletActionContext.getResponse().setStatus(500);
				return Action.MESSAGE;
			}
			//权限检查
			if(!checkCustomizePermission(userID, profile.getPlanID(), strategyID)){
				ServletActionContext.getResponse().setStatus(500);
				return Action.MESSAGE;
			}
			PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();
			Portfolio p = null;
			Portfolio op = portfolioManager.get(profile.getPortfolioID());
			if (op != null) {
				p = op.clone();
			}
			if (p == null) {
				p = portfolioManager.get(0l).clone();
			}

			if (profile.getPortfolioName() != null && !profile.getPortfolioName().equals("")) {
				p.setName(profile.getPortfolioName());
			} else {
				p.setName(profile.getPlanName() + " " + System.currentTimeMillis());
			}
			p.setUserID(userID);

			p.setProduction(true);
			p.setMainStrategyID(null);
			portfolioManager.save(p);
			profile.setPortfolioID(p.getID());
			profile.setIsGenerated(true);
			planID = profile.getPlanID();
			portfolioID = p.getID();
			Map<String, String> ht=p.getStrategies().getAssetAllocationStrategy().getParameter();
			if(ht!=null&&ht.get("RiskProfile")!=null){
				try {
					profile.setRiskNumber(Double.parseDouble(ht.get("RiskProfile")));
				} catch (Exception e) {
				}
			}
			save();
			//为新产生的portfolio设置email
			setEmailNotification(userID, portfolioID);
			//customize portfolio后的资源和计数操作
			mpm.afterPortfolioCustomize(userID, portfolioID, planID);
			// http://'+ip+':8081/execute?portfolioID=8507'+'&timestamp='+timestamp;
			BuildingUtil.monitor(portfolioID);
			message = p.getID() + "";
		} catch (Exception e) {
			e.printStackTrace();
			message = "-1";
		}
		return Action.MESSAGE;
	}

	/**
	 * 获取portfolio的end date
	 */
	public String getenddate() {
		Portfolio p = ContextHolder.getPortfolioManager().get(portfolioID);
		
		message = "";
		if (p != null && p.getEndDate() != null) {
			PermissionChecker pc = new PortfolioPermissionChecker(p, ServletActionContext.getRequest());
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			message = df.format(pc.getLastLegalDate());
		}

		return Action.MESSAGE;
	}

	private Boolean emailNotification;

	/**
	 * 设置email
	 * @param userID
	 * @param portfolioID
	 */
	public String settingemailalert() {
		EmailNotification en = ContextHolder.getUserManager().getEmailNotification(userID, portfolioID);

		if (en != null)
			setEmailNotification(true);
		else
			setEmailNotification(false);
		return Action.SUCCESS;
	}

	private String message = "Error!";
	private Date lastUpdated = new Date();
	private long currentTimeMillis = System.currentTimeMillis();

	private String operation = Action.ACTION_VIEW;
	private String params = "";

	private Long userID;
	private Long planID;
	private Long portfolioID;
	private Profile profile;

	private List<ProfileItem> profiles;

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

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	public List<ProfileItem> getProfiles() {
		return profiles;
	}

	public void setProfiles(List<ProfileItem> profiles) {
		this.profiles = profiles;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public long getCurrentTimeMillis() {
		return currentTimeMillis;
	}

	public void setCurrentTimeMillis(long currentTimeMillis) {
		this.currentTimeMillis = currentTimeMillis;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getCreate() {
		return create;
	}

	public void setCreate(Boolean create) {
		this.create = create;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Long getSearchByUserID() {
		return searchByUserID;
	}

	public void setSearchByUserID(Long searchByUserID) {
		this.searchByUserID = searchByUserID;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Boolean getEmailNotification() {
		return emailNotification;
	}

	public void setEmailNotification(Boolean emailNotification) {
		this.emailNotification = emailNotification;
	}

	public String getStrategyName() {
		return strategyName;
	}

	public void setStrategyName(String strategyName) {
		this.strategyName = strategyName;
	}

	public Long getStrategyID() {
		return strategyID;
	}

	public void setStrategyID(Long strategyID) {
		this.strategyID = strategyID;
	}

	public List<List<String>> getPerformance() {
		return performance;
	}

	public void setPerformance(List<List<String>> performance) {
		this.performance = performance;
	}

	private java.lang.String symbols;

	private java.util.List<String> symbolList;
	private java.lang.String symbol;

	/**
	 * 获取flash chart的symbol list
	 */
	public String getflashchart() {
		if (symbol == null || symbol.equals("")) {
			message = ("No Security Symbol!");
			return Action.MESSAGE;
		}
		if (symbolList == null || symbolList.size() == 0) {
			symbols = symbol;
		} else {
			symbols = "";
			for (int i = 0; i < symbolList.size(); i++) {
				String tmp = symbolList.get(i);
				if (!tmp.equals("")) {
					if (i != 0)
						symbols += ",";
					symbols += tmp;

				}
			}
		}
		return Action.SUCCESS;
	}

	public java.lang.String getSymbols() {
		return symbols;
	}

	public void setSymbols(java.lang.String symbols) {
		this.symbols = symbols;
	}

	public java.util.List<String> getSymbolList() {
		return symbolList;
	}

	public void setSymbolList(java.util.List<String> symbolList) {
		this.symbolList = symbolList;
	}

	public java.lang.String getSymbol() {
		return symbol;
	}

	public void setSymbol(java.lang.String symbol) {
		this.symbol = symbol;
	}

	/**
	 * 获取portfolio的名字
	 */
	public String getportfolioname() {
		try {
			message = ContextHolder.getPortfolioManager().get(portfolioID).getName();
		} catch (Exception e) {
			message = null;
		}
		return Action.MESSAGE;
	}

	public String getEmaValue() {
		return emaValue;
	}

	public void setEmaValue(String emaValue) {
		this.emaValue = emaValue;
	}

	public String getSmaValue() {
		return smaValue;
	}

	public void setSmaValue(String smaValue) {
		this.smaValue = smaValue;
	}

	public String getEmaFreq() {
		return emaFreq;
	}

	public void setEmaFreq(String emaFreq) {
		this.emaFreq = emaFreq;
	}

	public String getSmaFreq() {
		return smaFreq;
	}

	public void setSmaFreq(String smaFreq) {
		this.smaFreq = smaFreq;
	}

	public String getWmaValue() {
		return wmaValue;
	}

	public void setWmaValue(String wmaValue) {
		this.wmaValue = wmaValue;
	}

	public String getWmaFreq() {
		return wmaFreq;
	}

	public void setWmaFreq(String wmaFreq) {
		this.wmaFreq = wmaFreq;
	}

	public String getUseSMAFilter() {
		return useSMAFilter;
	}

	public void setUseSMAFilter(String useSMAFilter) {
		this.useSMAFilter = useSMAFilter;
	}

	public String getUseWMAFilter() {
		return useWMAFilter;
	}

	public void setUseWMAFilter(String useWMAFilter) {
		this.useWMAFilter = useWMAFilter;
	}

	public String getUseEMAFilter() {
		return useEMAFilter;
	}

	public void setUseEMAFilter(String useEMAFilter) {
		this.useEMAFilter = useEMAFilter;
	}

	public int getPortfolioSource() {
		return portfolioSource;
	}

	public void setPortfolioSource(int portfolioSource) {
		this.portfolioSource = portfolioSource;
	}
	
	
	public String getFollowDateStr() {
		return followDateStr;
	}

	public void setFollowDateStr(String followDateStr) {
		this.followDateStr = followDateStr;
	}
	
	public String getImitationDateStr() {
		return imitationDateStr;
	}

	public void setImitationDateStr(String imitationDateStr) {
		this.imitationDateStr = imitationDateStr;
	}

	public Integer getMaxOfRiskyAsset() {
		return maxOfRiskyAsset;
	}

	public void setMaxOfRiskyAsset(Integer maxOfRiskyAsset) {
		this.maxOfRiskyAsset = maxOfRiskyAsset;
	}

	public Integer getNumberOfMainRiskyClass() {
		return numberOfMainRiskyClass;
	}

	public void setNumberOfMainRiskyClass(Integer numberOfMainRiskyClass) {
		this.numberOfMainRiskyClass = numberOfMainRiskyClass;
	}

	public List<Strategy> getPlanList() {
		return planList;
	}

	public void setPlanList(List<Strategy> planList) {
		this.planList = planList;
	}

	public String getMainAssetClass() {
		return MainAssetClass;
	}

	public void setMainAssetClass(String mainAssetClass) {
		MainAssetClass = mainAssetClass;
	}

	public String getMainAssetClassWeight() {
		return MainAssetClassWeight;
	}

	public void setMainAssetClassWeight(String mainAssetClassWeight) {
		MainAssetClassWeight = mainAssetClassWeight;
	}

	public String getAssetFundString() {
		return AssetFundString;
	}

	public void setAssetFundString(String assetFundString) {
		AssetFundString = assetFundString;
	}

	public String getBuyThreshold() {
		return BuyThreshold;
	}

	public void setBuyThreshold(String buyThreshold) {
		BuyThreshold = buyThreshold;
	}

	public String getSellThreshold() {
		return SellThreshold;
	}

	public void setSellThreshold(String sellThreshold) {
		SellThreshold = sellThreshold;
	}

	public String getSpecifyAssetFund() {
		return SpecifyAssetFund;
	}

	public void setSpecifyAssetFund(String specifyAssetFund) {
		SpecifyAssetFund = specifyAssetFund;
	}

	public String getUseDataObject() {
		return UseDataObject;
	}

	public void setUseDataObject(String useDataObject) {
		UseDataObject = useDataObject;
	}

}
