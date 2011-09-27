package com.lti.action;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import org.apache.struts2.ServletActionContext;

import com.lti.permission.MYPLANIQPermissionManager;
import com.lti.permission.PortfolioPermissionChecker;
import com.lti.permission.StrategyPermissionChecker;
import com.lti.permission.SubscrPlanChecker;
import com.lti.service.PortfolioManager;
import com.lti.service.ProfileManager;
import com.lti.service.StrategyManager;
import com.lti.service.UserManager;
import com.lti.service.bo.CachePortfolioItem;
import com.lti.service.bo.EmailNotification;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.Profile;
import com.lti.service.bo.Strategy;
import com.lti.service.bo.User;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.system.EmailLoginServiceImpl;
import com.lti.util.BuildingUtil;
import com.lti.util.FileOperator;
import com.lti.util.LTIDate;

public class getstartedaction {
	private Integer planType = -1;
	MYPLANIQPermissionManager mpm = new MYPLANIQPermissionManager();
	public String pickaplan() {

		return Action.SUCCESS;
	}
 
	public String entry() {
		return Action.SUCCESS;
	}

	private String planName;
	private String message;
	private Strategy plan;
	private Long planID;

	public String getdesc() {
		StrategyManager strategyManager = ContextHolder.getStrategyManager();
		plan = strategyManager.get(planID);
		return Action.SUCCESS;
	}

	public Long getPlanID() {
		return planID;
	}

	public void setPlanID(Long planID) {
		this.planID = planID;
	}
	
	private User user;
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	StrategyPermissionChecker spc;
	

	public StrategyPermissionChecker getSpc() {
		return spc;
	}

	public void setSpc(StrategyPermissionChecker spc) {
		this.spc = spc;
	}
	
	private int ranksize=0;

	public int getRanksize() {
		return ranksize;
	}

	public void setRanksize(int ranksize) {
		this.ranksize = ranksize;
	}

	public String build() {
		StrategyManager strategyManager=ContextHolder.getStrategyManager();
		plan=strategyManager.get(planID);
		spc=new StrategyPermissionChecker(plan, ServletActionContext.getRequest());
		if(!spc.hasViewRole()){
			message="This plan has been set to a private one.";
			return Action.MESSAGE;
		}
		user=ContextHolder.getUserManager().getLoginUser();
		planName=plan.getName();
		double score=0.0;
		try{
			score=strategyManager.getPlanScoreByPlanID(planID).getInvestmentScore()*100;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		
		try {
			int count=0;
			if (score == 0)
			    count=0;
		    else if( score<10)
			    count=1;
			else if (score<35)
				count=2;
			else if (score< 65)
				count=3;
			else if (score< 85)
				count=4;
			else
				count=5;
			ranksize=count;
		}catch(Exception e){
			ranksize=0;
		}
		return Action.SUCCESS;
	}

	List<CachePortfolioItem> TAAPortfolios;
	List<CachePortfolioItem> SAAPortfolios;

	private CachePortfolioItem _getportfolio(List<Portfolio> ports, String name, StrategyManager strategyManager) {
		for (Portfolio p : ports) {
			if (p.getName().toLowerCase().contains(name.toLowerCase())) {

				CachePortfolioItem cpi = null;
				List<CachePortfolioItem> pitems = null;
				pitems = strategyManager.findByHQL("from CachePortfolioItem cp where cp.PortfolioID=" + p.getID() + " and cp.GroupID=0 and cp.RoleID=" + Configuration.ROLE_PORTFOLIO_REALTIME_ID);
				// strategyManager.findByHQL("from CachePortfolioItem cp where cp.PortfolioID="
				// + p.getID() + " and cp.GroupID=0 and cp.RoleID=" +
				// Configuration.ROLE_PORTFOLIO_REALTIME_ID);
				if (pitems != null && pitems.size() > 0) {
					cpi = pitems.get(0);
				} else {
					cpi = new CachePortfolioItem();
					cpi.setPortfolioID(p.getID());
					cpi.setPortfolioName(p.getName());
				}
				cpi.setPortfolioName(name.split(" ")[3]);
				return cpi;
			}
		}
		return null;
	}
    private boolean useFollow=true;
    private boolean planpage=false;
	public boolean isPlanpage() {
		return planpage;
	}

	public void setPlanpage(boolean planpage) {
		this.planpage = planpage;
	}

	public String getmodelportfolios() {
		StrategyManager strategyManager = ContextHolder.getStrategyManager();
		List<Portfolio> ports = strategyManager.getModelPortfolios(planID);

		//CachePortfolioItem s1 = _getportfolio(ports, "Strategic Asset Allocation Growth", strategyManager);
		CachePortfolioItem s2 = _getportfolio(ports, "Strategic Asset Allocation Moderate", strategyManager);
		//CachePortfolioItem s3 = _getportfolio(ports, "Strategic Asset Allocation Conservative", strategyManager);
		SAAPortfolios = new ArrayList<CachePortfolioItem>();
		//if (s1 != null)
		//	SAAPortfolios.add(s1);
		if (s2 != null)
			SAAPortfolios.add(s2);
		//if (s3 != null)
		//	SAAPortfolios.add(s3);

		//CachePortfolioItem t1 = _getportfolio(ports, "Tactical Asset Allocation Growth", strategyManager);
		CachePortfolioItem t2 = _getportfolio(ports, "Tactical Asset Allocation Moderate", strategyManager);
		//CachePortfolioItem t3 = _getportfolio(ports, "Tactical Asset Allocation Conservative", strategyManager);
		TAAPortfolios = new ArrayList<CachePortfolioItem>();
		//if (t1 != null)
		//	TAAPortfolios.add(t1);
		if (t2 != null)
			TAAPortfolios.add(t2);
		//if (t3 != null)
		//	TAAPortfolios.add(t3);

		return Action.SUCCESS;
	}

	private String ids;
	private List<Strategy> plans;
	private Boolean rankByScore = true;

	public Boolean getRankByScore() {
		return rankByScore;
	}

	public void setRankByScore(Boolean rankByScore) {
		this.rankByScore = rankByScore;
	}

	public static DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static DateFormat df1=new SimpleDateFormat("yyyy-MM-dd");
	public synchronized static void writeTo(String keyword,String username,boolean found){
		String cur=df1.format(new Date());
		try {
			String filename=null;
			if(found){
				filename="search-plans-ok"+cur+".csv";
			}else{
				filename="search-plans-nook"+cur+".csv";
			}
			FileOperator.appendMethodA(ContextHolder.getServletPath()+"/"+filename, username+","+df.format(new Date())+","+keyword+"\r\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String search() {
		curtime = System.currentTimeMillis();

		StrategyManager strategyManager = ContextHolder.getStrategyManager();

		if (ids != null) {
			plans = new ArrayList<Strategy>();
			long[] pids = getIDs(ids);
			for (long id : pids) {
				Strategy p = strategyManager.get(id);
				if (p != null)
					plans.add(p);
			}

		} else {
			plans = strategyManager.getPlansByType(planName, planType, 50);
		}

		if (plans != null && plans.size() > 0) {
			if(planName!=null){
				writeTo(planName, ContextHolder.getUserManager().getLoginUser().getUserName(), true);
			}
			Iterator<Strategy> iter = plans.iterator();
			while (iter.hasNext()) {
				Strategy p=iter.next();
				StrategyPermissionChecker spc = new StrategyPermissionChecker(p, ServletActionContext.getRequest());
				if (!spc.hasViewRole()) {
					iter.remove();
					continue;
				}
				try {
					double score = strategyManager.getPlanScoreByPlanID(p.getID()).getInvestmentScore() * 100;
					int count = 0;
					if (score == 0)
						count = 0;
					else if (score < 10)
						count = 1;
					else if (score < 35)
						count = 2;
					else if (score < 65)
						count = 3;
					else if (score < 85)
						count = 4;
					else
						count = 5;
					p.setPlanType(count);

					
				} catch (Exception e) {
					p.setPlanType(0);
				}
			}
			if (rankByScore) {

				Collections.sort(plans, new Comparator<Strategy>() {

					@Override
					public int compare(Strategy o1, Strategy o2) {
						return o2.getPlanType() - o1.getPlanType();
					}
				});
			}
		}else{
			if(planName!=null){
				writeTo(planName, ContextHolder.getUserManager().getLoginUser().getUserName(), false);
			}
		}

		return Action.SUCCESS;
	}

	private long curtime = System.currentTimeMillis();

	public long getCurtime() {
		return curtime;
	}

	public void setCurtime(long curtime) {
		this.curtime = curtime;
	}

	public long[] getIDs(String ids) {
		String[] strs = ids.split(",");
		long[] pids = new long[strs.length];
		for (int i = 0; i < pids.length; i++) {
			pids[i] = Long.parseLong(strs[i].trim());
		}
		return pids;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public List<Strategy> getPlans() {
		return plans;
	}

	public void setPlans(List<Strategy> plans) {
		this.plans = plans;
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
	 * 检查是否可以follow
	 * @param userID
	 * @param portfolio
	 * @return
	 */
	private boolean checkFollowADVPermission(Long userID, Portfolio portfolio) {
		int operationCode = mpm.canPortfolioFollow(userID, portfolio, null);
		if(operationCode == mpm.SUCCESS)
			return true;
		if(operationCode == mpm.PERMISSION_ADV_LIMIT)
			message = "You need to subscribe as Expert User or Professional User to follow the advanced portfolios. Please subscribe for two weeks trial";
		else if(operationCode == mpm.COUNT_LIMIT) 
			message = "You have already customized/followed " + mpm.getMaxPortfolioFollowNum(userID) + " portfolio(s)";
		return false;
	}
	/**
	 * 检查是否可以customize
	 * @param userID
	 * @param portfolioID
	 * @param planID
	 * @return
	 */
	private boolean checkCustomizePermission(Long userID, Portfolio portfolio, Long planID) {
		int operationCode = mpm.canPortfolioCustomize(userID, portfolio, planID);
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
	 * 删除email alert
	 * @param userID
	 * @param portfolioID
	 */
	private void cancelEmailNotification(Long userID, Long portfolioID) {
		ContextHolder.getUserManager().deleteEmailNotification(userID, portfolioID);
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
	public String getallowcount() {
		User loginUser = ContextHolder.getUserManager().getLoginUser();
		Long userID = loginUser.getID();
		message = "This portfolio has been added to \"My Customized Private Portfolios\" table. You will receive rebalance email alerts of this portfolio. You can customize/follow " + mpm.getAllowPortfolioFollowNum(userID) + " more portfolio(s)";
		return Action.MESSAGE;
	}
	/***
	 * 这个action执行follow的功能
	 * 先进行功能权限检查，再进行数量控制检查，不创建新portfolio，添加email alert,执行afterPortfolioFollow();
	 * 执行成功时返回的message为  还允许customize/follow 的数目 allowCount
	 * @return
	 * @throws Exception
	 */
	public String followportfolio() throws Exception {
		User loginUser = ContextHolder.getUserManager().getLoginUser();
		Long userID = loginUser.getID();
		//检查plan是否存在
		if (!isPlanExist(planID)) {
			message = "The plan doesn't exist.";
			ServletActionContext.getResponse().setStatus(500);
			return Action.MESSAGE;
		}
		portfolio=ContextHolder.getPortfolioManager().get(portfolioID);
		//权限检查
		if(!checkFollowPermission(userID, portfolio, planID)){
			ServletActionContext.getResponse().setStatus(500);
			return Action.MESSAGE;
		}
		//为用户设置email alert
		setEmailNotification(userID, portfolioID);
		//follow portfolio后的资源和计数操作
		mpm.afterPortfolioFollow(userID, portfolio, planID);
		//返回还可以customize/follow的portfolio数目
		message = "This portfolio has been added to \" My Followed Public Portfolios\" table. You will receive rebalance email alerts of this portfolio. You can customize/follow " + mpm.getAllowPortfolioFollowNum(userID) + " more portfolio(s)";
		return Action.MESSAGE;
	}
	/**
	 * 这个action执行follow advance portfolio的功能
	 * 先进行功能权限检查，再进行数量控制检查，不创建新portfolio，添加email alert,执行afterPortfolioFollow();
	 * 执行成功时返回的message为  还允许customize/follow 的数目 allowCount
	 * @return
	 * @throws Exception
	 */
	public String followadvanceportfolio() throws Exception {
		User loginUser = ContextHolder.getUserManager().getLoginUser();
		Long userID = loginUser.getID();
		portfolio=ContextHolder.getPortfolioManager().get(portfolioID);
		//权限检查
		if(!checkFollowADVPermission(userID, portfolio)){
			ServletActionContext.getResponse().setStatus(500);
			return Action.MESSAGE;
		}
		//为用户设置email alert
		setEmailNotification(userID, portfolioID);
		//follow portfolio后的资源和计数操作
		mpm.afterPortfolioFollow(userID, portfolio, null);
		//返回还可以customize/follow的portfolio数目
		message = "This portfolio has been added to \" My Followed Public Portfolios(Advanced)\" table. You will receive rebalance email alerts of this portfolio. You can customize/follow " + mpm.getAllowPortfolioFollowNum(userID) + " more portfolio(s)";
		return Action.MESSAGE;
	}
	/**
	 * 这个action执行unfollow的功能或删除功能
	 * 如果是用户创建的portfolio，先删除，再对资源和计数进行调整
	 * 如果不是用户创建，则只是对资源和计数进行调整
	 * @return
	 * @throws Exception
	 */
	public String unfollow() throws Exception {
		PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();
		User loginUser = ContextHolder.getUserManager().getLoginUser();
		Long userID = loginUser.getID();
		portfolio=portfolioManager.get(portfolioID);
		boolean isAdmin = userID == Configuration.SUPER_USER_ID;
		if(isAdmin && portfolio.getUserID().equals(userID)){
			portfolioManager.remove(portfolioID);
			message = "delete successful";
			return Action.MESSAGE;
		}
		//先判断这个portfolio是否是用户follow的
		if(!mpm.hasUserPortfolioFollow(userID, portfolio)) {
			message = "You didn't follow this portfolio before";
			ServletActionContext.getResponse().setStatus(500);
			return Action.MESSAGE;
		}
		String planIDStr = portfolio.getStrategies().getAssetAllocationStrategy().getParameter().get("PlanID");
		Long planID = null;
		if(planIDStr != null)
			planID = Long.parseLong(planIDStr);
		//如果是用户创建的portfolio,则先删除portfolio(调整已经在remove API里面完成了)
		if(mpm.isPortfolioOwner(userID, portfolio)){
			portfolioManager.remove(portfolioID);
		}else{
			cancelEmailNotification(userID, portfolioID);
			mpm.afterPortfolioUnfollow(userID, portfolio, planID);
		}
		message="This portfolio has been removed from the \"My Followed Public Portfolio\" table. You won’t receive rebalance email alert of this portfolio. You can customize/follow " +  mpm.getAllowPortfolioFollowNum(userID) + " more portfolio(s). ";
		return Action.MESSAGE;
	}
	/**
	 * 这个action执行customize的功能
	 * 先进行功能权限检查，再进行数量控制检查，创建新的portfolio，添加email alert，执行afterPortfolioCustomize
	 * 执行成功时返回的message为新创建的portfolio ID
	 * @return
	 * @throws Exception
	 */
	public String generate() throws Exception {
		User loginUser = ContextHolder.getUserManager().getLoginUser();
		Long userID = loginUser.getID();
		//检查plan是否存在
		if(!isPlanExist(planID)){
			message = "The plan doesn't exist.";
			ServletActionContext.getResponse().setStatus(500);
			return Action.MESSAGE;
		}
		//权限检查
		if(!checkCustomizePermission(userID, planID, strategyID)){
			ServletActionContext.getResponse().setStatus(500);
			return Action.MESSAGE;
		}
		
		//创建portfolio
		Portfolio templateportfolio = null;
		PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();
		List<Portfolio> templateportfolios = portfolioManager.getPortfolios(plan.getID(), -1);
		for (Portfolio p : templateportfolios) {
			if (p.getName().toLowerCase().contains("moderate") && p.getStrategies().getAssetAllocationStrategy().getID().equals(strategyID)) {
				templateportfolio = p;
			}
		}
		ProfileManager profileManager = (ProfileManager) ContextHolder.getInstance().getApplicationContext().getBean("profileManager");
		Map<String, String> opts = new HashMap<String, String>();
		opts.put("UseRiskProfile", "true");
		opts.put("RiskProfile", riskNumber + "");
		if (frequency == null) {
			frequency = "monthly";
		}
		opts.put("Frequency", frequency);
		opts.put("CheckFrequency", frequency);
		opts.put("RebalanceFrequency", frequency);
		opts.put("PlanID", plan.getID() + "");
		long newPortfolioID = BuildingUtil.build(portfolioName, userID, plan, templateportfolio, opts);
		BuildingUtil.monitor(newPortfolioID);
		Profile profile = new Profile();
		profile.setUserID(userID);
		profile.setPortfolioID(newPortfolioID);
		profile.setPlanID(plan.getID());
		profile.setUserName(loginUser.getUserName());
		profile.setPortfolioName(templateportfolio.getName());
		profile.setPlanName(plan.getName());
		profile.setIsGenerated(true);
		profileManager.save(profile);
		//为新产生的portfolio设置email
		setEmailNotification(userID, newPortfolioID);
		//customize portfolio后的资源和计数操作
		mpm.afterPortfolioCustomize(userID, newPortfolioID, planID);
		message = "" + newPortfolioID;
		return Action.MESSAGE;
	}
	
	private Long portfolioID;
	private Date endDate;

	PortfolioPermissionChecker pc;
	
	private boolean aggregateFlag;
	public boolean isAggregateFlag() {
		return aggregateFlag;
	}

	public void setAggregateFlag(boolean aggregateFlag) {
		this.aggregateFlag = aggregateFlag;
	}

	public String follow() {
		PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();
		StrategyManager strategyManager = ContextHolder.getStrategyManager();

		portfolio = portfolioManager.get(portfolioID);
		endDate = portfolio.getEndDate();

		pc = new PortfolioPermissionChecker(portfolio, ServletActionContext.getRequest());

		plan = strategyManager.get(Long.parseLong(portfolio.getStrategies().getAssetAllocationStrategy().getParameter().get("PlanID")));
		aggregateFlag = portfolioManager.get(portfolioID).isType(Configuration.PORTFOLIO_TYPE_COMPOUND);
		return Action.SUCCESS;
	}
	
	public String email() {
		UserManager userManager = ContextHolder.getUserManager();
		PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();
		User user = userManager.getLoginUser();
		Long userID;
		
		if (user == null || user.getID() == Configuration.USER_ANONYMOUS || portfolioID == null || portfolioID.longValue() == 0l) {
			message="error";
			ServletActionContext.getResponse().setStatus(500);
			return Action.MESSAGE;
		} else {
			userID = user.getID();
		}
		if(userManager.getEmailNotification(userID, portfolioID)!=null){
			message="success";
			return Action.MESSAGE;
		}
		EmailNotification en = new EmailNotification();
		en.setPortfolioID(portfolioID);
		en.setUserID(userID);
		en.setSpan(0);
		Date today = new Date();
		Date LastSentDate = portfolioManager.getTransactionLatestDate(portfolioID, today);
		if (LastSentDate == null) {
			LastSentDate = today;
		}
		LastSentDate = LTIDate.clearHMSM(LastSentDate);
		en.setLastSentDate(LastSentDate);
		try {
			userManager.addEmailNotification(en);
			
		} catch (RuntimeException e) {
			message=e.getMessage();
			ServletActionContext.getResponse().setStatus(500);
			return Action.MESSAGE;
			
		}
		message="success";
		return Action.MESSAGE;
	}
		
	private String fbEmail;
	public String facebook() throws NoSuchAlgorithmException{
		UserManager userManager = ContextHolder.getUserManager();
		User user = userManager.getUserByEmail(fbEmail);		
		message = "";
		if(user ==null){
			String [] ss = fbEmail.split("@");
			String name = ss[0];
			User us = userManager.get(name);
			int n=1;
			String username=name;
			while(us!=null){
				username=name+n;
				n++;
				us = userManager.get(username);
			}
			
			String password="validfijohnc403";
			User nUser = new User();		
			nUser.setUserName(username);
			nUser.setEMail(fbEmail);
			nUser.setPassword(password);
			nUser.setEnabled(true);
			userManager.add(nUser);
			EmailLoginServiceImpl userDetail = (EmailLoginServiceImpl) com.lti.system.ContextHolder.getInstance().getApplicationContext().getBean("emailLoginServiceImpl");
			List<String> authNames = userDetail.AuthoritiesByUsernameQuery(nUser);
			int authN = authNames.size();
	        GrantedAuthority[] authorities = new GrantedAuthority[authN];             
	        for(int i=0; i<authN; i++) {   
	            authorities[i] = new GrantedAuthorityImpl(authNames.get(i).toString());   
	        }
			UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(username,password,authorities);
			result.setDetails(userDetail);
			SecurityContext sc = SecurityContextHolder.getContext();
			sc.setAuthentication(result);
			message="Thanks for logging in with Facebook and creating an MyPlanIQ account! Please set your password at \" My Acount\" page.";
			
		}else{
			String username = user.getUserName();
			String password = user.getPassword();
			EmailLoginServiceImpl userDetail = (EmailLoginServiceImpl) com.lti.system.ContextHolder.getInstance().getApplicationContext().getBean("emailLoginServiceImpl");
			List<String> authNames = userDetail.AuthoritiesByUsernameQuery(user);
			int authN = authNames.size();
	        GrantedAuthority[] authorities = new GrantedAuthority[authN];             
	        for(int i=0; i<authN; i++) {   
	            authorities[i] = new GrantedAuthorityImpl(authNames.get(i).toString());   
	        }   
			UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(username,password,authorities);
			result.setDetails(userDetail);
			SecurityContext sc = SecurityContextHolder.getContext();
			sc.setAuthentication(result);
			
		}
		return Action.MESSAGE;
	}

	public PortfolioPermissionChecker getPc() {
		return pc;
	}

	public void setPc(PortfolioPermissionChecker pc) {
		this.pc = pc;
	}

	private Portfolio portfolio;

	public Portfolio getPortfolio() {
		return portfolio;
	}

	public void setPortfolio(Portfolio portfolio) {
		this.portfolio = portfolio;
	}

	public Long getPortfolioID() {
		return portfolioID;
	}

	public void setPortfolioID(Long portfolioID) {
		this.portfolioID = portfolioID;
	}

	private Long strategyID;

	private String portfolioName;

	private Integer riskNumber;
	
	private Integer maxOfRiskyAsset = 2;

	public Integer getRiskNumber() {
		return riskNumber;
	}

	public void setRiskNumber(Integer riskNumber) {
		this.riskNumber = riskNumber;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	private String frequency;

	public String getPortfolioName() {
		return portfolioName;
	}

	public void setPortfolioName(String portfolioName) {
		this.portfolioName = portfolioName;
	}

	public Long getStrategyID() {
		return strategyID;
	}

	public void setStrategyID(Long strategyID) {
		this.strategyID = strategyID;
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Strategy getPlan() {
		return plan;
	}

	public void setPlan(Strategy plan) {
		this.plan = plan;
	}

	public Integer getPlanType() {
		return planType;
	}

	public void setPlanType(Integer planType) {
		this.planType = planType;
	}

	public List<CachePortfolioItem> getTAAPortfolios() {
		return TAAPortfolios;
	}

	public void setTAAPortfolios(List<CachePortfolioItem> tAAPortfolios) {
		TAAPortfolios = tAAPortfolios;
	}

	public List<CachePortfolioItem> getSAAPortfolios() {
		return SAAPortfolios;
	}

	public void setSAAPortfolios(List<CachePortfolioItem> sAAPortfolios) {
		SAAPortfolios = sAAPortfolios;
	}

	public boolean isUseFollow() {
		return useFollow;
	}

	public void setUseFollow(boolean useFollow) {
		this.useFollow = useFollow;
	}

	public String getFbEmail() {
		return fbEmail;
	}

	public void setFbEmail(String fbEmail) {
		this.fbEmail = fbEmail;
	}

	public Integer getMaxOfRiskyAsset() {
		return maxOfRiskyAsset;
	}

	public void setMaxOfRiskyAsset(Integer maxOfRiskyAsset) {
		this.maxOfRiskyAsset = maxOfRiskyAsset;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

}
