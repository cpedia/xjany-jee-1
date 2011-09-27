package com.lti.widgets;

import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date; 
import java.util.List;

import org.apache.struts2.ServletActionContext;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONBuilder;

import com.lti.action.Action;
import com.lti.permission.MYPLANIQPermissionManager;
import com.lti.permission.PortfolioPermissionChecker;
import com.lti.service.PortfolioHoldingManager;
import com.lti.service.PortfolioManager;
import com.lti.service.SecurityManager;
import com.lti.service.StrategyManager;
import com.lti.service.UserManager;
import com.lti.service.bo.CachePortfolioItem;
import com.lti.service.bo.EmailNotification;
import com.lti.service.bo.HoldingItem;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.PortfolioInf;
import com.lti.service.bo.PortfolioMPT;
import com.lti.service.bo.Security;
import com.lti.service.bo.SecurityMPT;
import com.lti.service.bo.Strategy;
import com.lti.service.bo.User;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.util.FormatUtil;
import com.lti.util.LTIDate;
import com.lti.util.SecurityUtil;

public class portfoliowidget extends framework {
	private String message;
	private Portfolio portfolio;
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	private Long portfolioID;

	public Long getPortfolioID() {
		return portfolioID;
	}

	public void setPortfolioID(Long portfolioID) {
		this.portfolioID = portfolioID;
	}
	private List<List<String>> performance;
	public String view() {
		String hasRealtime;
		String isAdmin;
		String isOwner;
		String planIdStr;
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();
		PortfolioHoldingManager holdingManager = ContextHolder.getPortfolioHoldingManager();
		portfolio = portfolioManager.get(portfolioID);
		StringWriter w = new StringWriter();
		StrategyManager strm=ContextHolder.getStrategyManager();
		JSONBuilder jb = new JSONBuilder(w);
		PortfolioPermissionChecker pc = new PortfolioPermissionChecker(portfolio, ServletActionContext.getRequest());
		planIdStr = portfolio.getStrategies().getAssetAllocationStrategy().getParameter().get("PlanID");
		Long planID=Long.parseLong(planIdStr);
		//Overview
		List<Date> holdingDates;
		try {
			String sql = "select distinct h.date from " + Configuration.TABLE_PORTFOLIO_HOLDINGITEM + " h where h.PortfolioID=" + portfolioID;
			if (!pc.hasRealtimeRole()) {
				sql += " and h.date<='" + df.format(pc.getLastLegalDate()) + "'";
				holdingDates = holdingManager.findBySQL(sql);// --------------
				portfolio.setEndDate(holdingDates.get(holdingDates.size() - 1));
			}
		} catch (Exception e) {
			portfolio.setEndDate(pc.getLastLegalDate());
		}
		jb.object().key("id").value(portfolioID);
		jb.key("name").value(portfolio.getName());
		jb.key("enddate").value(df.format(portfolio.getEndDate()));
		jb.key("description").value(portfolio.getDescription());
		
		//follow
		hasRealtime = String.valueOf(pc.hasRealtimeRole());
		isAdmin = String.valueOf(pc.isAdmin());
		isOwner = String.valueOf(pc.isOwner());
		jb.key("hasRealtime").value(hasRealtime).key("isAdmin").value(isAdmin).key("isOwner").value(isOwner).key("planID").value(planIdStr);
		
		
		//Description
		String str;
		String RiskProfile=null;
		String RebalanceFrequency="Monthly";
		String NumberOfMainRiskyClass=null;
		String NumberOfMainStableClass = null;
		String portfolioType = "SAA";
		String MainAssetClass = "";
		String MainAssetClassWeight = "";
		String SpecifyAssetFund="false";
		String createDate = "null";
		String MajorAssetCount="0";
		String UserName="null";
		SimpleDateFormat sdf=new SimpleDateFormat("MM/dd/yyyy");
		try{
			createDate=sdf.format(portfolio.getCreatedDate());
		}catch(Exception e){
			//e.printStackTrace();
		}
		
		
		String PlanName = strm.get(planID).getName();
		
		int availableFunds = strm.get(planID).getVariablesFor401k().size();
		List<String> listMajorAsset=null;
		try {
			listMajorAsset = strm.getMajorAssetByPlanID(planID);
			MajorAssetCount=String.valueOf(listMajorAsset.size());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try{
			SAXReader reader = new SAXReader();
			Document doc = DocumentHelper.parseText(portfolio.getStrategies().toXML());
			
			Element root = doc.getRootElement();
			Element foo;
			List<Element> list = doc.selectNodes("//strategy-inf//asset-allocation-strategy//parameter");
			List<Element> list2 = doc.selectNodes("//strategy-inf//asset-allocation-strategy");
			for (Element CdcEntryItem : list) {
                String key = CdcEntryItem.elementTextTrim("key");
                if("RiskProfile".equals(key)) {
                    System.out.println(CdcEntryItem.elementTextTrim("value"));
                    RiskProfile = CdcEntryItem.elementTextTrim("value");
                }
                if("RebalanceFrequency".equals(key)){
                	RebalanceFrequency=CdcEntryItem.elementTextTrim("value");
                }
                if("SpecifyAssetFund".equals(key)){
                	SpecifyAssetFund = CdcEntryItem.elementTextTrim("value");
                }
                if("NumberOfMainRiskyClass".equals(key)){
                	NumberOfMainRiskyClass = CdcEntryItem.elementTextTrim("value");
                }
                if("NumberOfMainStableClass".equals(key)){
                	NumberOfMainStableClass = CdcEntryItem.elementTextTrim("value");
                }
                if("MainAssetClass".equals(key)){
                	MainAssetClass = CdcEntryItem.elementTextTrim("value");
                }
                if("MainAssetClassWeight".equals(key)){
                	MainAssetClassWeight = CdcEntryItem.elementTextTrim("value");
                }
            }
			for (Element CdcEntryItem2 : list2) {
				portfolioType = CdcEntryItem2.elementTextTrim("name");
			}
			
			}catch(Exception e){
				e.printStackTrace();
			}
		//
		jb.key("descriptions");
		jb.object().key("MainAssetClassWeight").value(MainAssetClassWeight).key("MainAssetClass").value(MainAssetClass).key("MajorAssetCount").value(MajorAssetCount).key("createDate").value(createDate).key("portfolioType").value(portfolioType).key("NumberOfMainRiskyClass").value(NumberOfMainRiskyClass).key("NumberOfMainStableClass").value(NumberOfMainStableClass).key("listMajorAsset").value(listMajorAsset.toString()).key("availableFunds").value(availableFunds).key("planName").value(PlanName).key("RiskProfile").value(RiskProfile).key("RebalanceFrequency").value(RebalanceFrequency).key("SpecifyAssetFund").value(SpecifyAssetFund);
		jb.endObject(); 
		
		jb.key("holdings");
		PortfolioHoldingManager portfolioHoldingManager = ContextHolder.getPortfolioHoldingManager();
		PortfolioInf pinf=portfolioManager.getPortfolioInf(portfolioID, Configuration.PORTFOLIO_HOLDING_CURRENT);
		List<HoldingItem> hi = pinf.getHolding().getHoldingItems();
		if(!pc.hasRealtimeRole()){
			
			hi = portfolioHoldingManager.getHoldingItems(portfolioID,portfolio.getEndDate());
			
		}
		SecurityUtil.usedescription(portfolio, hi);
		if(hi!=null&&hi!=null&&hi.size()>0){
			jb.object().key("size").value(hi.size());
			jb.key("headers");
			jb.value(JSONArray.fromObject(new String[]{"Asset","ticker","funddescription","percentage"}));
			jb.key("items");
			JSONArray arr = new JSONArray();
			for (int i = 0; i < hi.size(); i++) {
				HoldingItem item = hi.get(i);
				//System.out.println(item.getDescription());
				JSONObject jo = new JSONObject();
				jo.accumulate("Asset", item.getAssetName());
				jo.accumulate("ticker", item.getSymbol());
				jo.accumulate("funddescription", item.getDescription());
				jo.accumulate("percentage", FormatUtil.formatPercentage(item.getPercentage())+"%");
				arr.add(jo);
			}
			jb.value(arr);
			jb.endObject();
		}else{
			jb.object().key("size").value(0);
			jb.key("headers");
			jb.value(JSONArray.fromObject(new String[]{"ticker","funddescription","percentage"}));
			jb.endObject();
		}
		
		//
		List<CachePortfolioItem> pitems = null;
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
			} else {
				mpts.add("");
			}
		}
		performance.add(mpts);
	}
		
		jb.key("perinf");
		
		jb.object().key("size").value(performance.size());
		jb.key("headers");
		jb.value(JSONArray.fromObject(new String[]{"Name","Last 1 Year","Last 3 Years","Last 5 Years"}));
		jb.key("items");
		JSONArray arr = new JSONArray();
		for (int i = 0; i < performance.size(); i++) {
			JSONObject jo = new JSONObject();
			jo.accumulate("Name", performance.get(i).get(0));
			jo.accumulate("Last 1 Year", performance.get(i).get(1));
			jo.accumulate("Last 3 Years", performance.get(i).get(2));
			jo.accumulate("Last 5 Years", performance.get(i).get(3));
			arr.add(jo);
		}
		jb.value(arr);
		jb.endObject();
		//

		jb.endObject();

		json=w.toString();
		
		return Action.JSON;
	}

	/**
	 * 这个action执行unfollow的功能或删除功能
	 * 如果是用户创建的portfolio，先删除，再对资源和计数进行调整
	 * 如果不是用户创建，则只是对资源和计数进行调整
	 * @return
	 * @throws Exception
	 */
	public String unfollow() throws Exception {
		MYPLANIQPermissionManager mpm = new MYPLANIQPermissionManager();
		portfolio=ContextHolder.getPortfolioManager().get(portfolioID);
		PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();
		StringWriter w = new StringWriter();
		JSONBuilder jb = new JSONBuilder(w);
		User loginUser = ContextHolder.getUserManager().getLoginUser();
		Long userID = loginUser.getID();
		portfolio=portfolioManager.get(portfolioID);
		boolean isAdmin = userID == Configuration.SUPER_USER_ID;
		if(isAdmin && portfolio.getUserID().equals(userID)){
			portfolioManager.remove(portfolioID);
			message = "delete successful";
			jb.object().key("message").value(message).key("success").value("false");
			jb.endObject();
			json=w.toString();
			return Action.JSON;
		}
		//先判断这个portfolio是否是用户follow的
		if(!mpm.hasUserPortfolioFollow(userID, portfolio)) {
			message = "You didn't follow this portfolio before";
			ServletActionContext.getResponse().setStatus(500);
			jb.object().key("message").value(message).key("success").value("false");
			jb.endObject();
			json=w.toString();
			return Action.JSON;
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
		jb.object().key("message").value(message).key("success").value("true");
		jb.endObject();
		json=w.toString();
		return Action.JSON;
	}
	
	public String followportfolio() throws Exception{
		MYPLANIQPermissionManager mpm = new MYPLANIQPermissionManager();
		StringWriter w = new StringWriter();
		JSONBuilder jb = new JSONBuilder(w);
		//followportfolio
		User loginUser = ContextHolder.getUserManager().getLoginUser();
		Long userID = loginUser.getID();
		portfolio=ContextHolder.getPortfolioManager().get(portfolioID);
		System.out.println(portfolio.getStrategies().getAssetAllocationStrategy().getParameter().get("PlanID"));
		Long planID=Long.parseLong(portfolio.getStrategies().getAssetAllocationStrategy().getParameter().get("PlanID"));
		//检查plan是否存在
		if (!isPlanExist(planID)) {
			message = "The plan doesn't exist.";
			jb.object().key("message").value(message).key("success").value("false");
			jb.endObject();
			json=w.toString();
			return Action.JSON;
		}
		
		//权限检查
		if(!checkFollowPermission(userID, portfolio, planID)){
			jb.object().key("message").value(message).key("success").value("false");
			jb.endObject();
			json=w.toString();
			return Action.JSON;
		}
		//为用户设置email alert
		setEmailNotification(userID, portfolioID);
		//follow portfolio后的资源和计数操作
		mpm.afterPortfolioFollow(userID, portfolio, planID);
		//返回还可以customize/follow的portfolio数目
		message = "This portfolio has been added to \" My Followed Public Portfolios\" table. You will receive rebalance email alerts of this portfolio. You can customize/follow " + mpm.getAllowPortfolioFollowNum(userID) + " more portfolio(s)";
		jb.object().key("message").value(message).key("success").value("true");
		jb.endObject();
		json=w.toString();
		return Action.JSON;
	}
	
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
	
	/**
	 * 检查是否可以follow
	 * @param userID
	 * @param portfolioID
	 * @param planID
	 * @return
	 */
	private boolean checkFollowPermission(Long userID, Portfolio portfolio, Long planID) {
		MYPLANIQPermissionManager mpm = new MYPLANIQPermissionManager();
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
	
	private boolean isPlanExist(Long planID){
		StrategyManager strategyManager = ContextHolder.getStrategyManager();
		Strategy plan = strategyManager.get(planID);
		if (plan != null)
			return true;
		return false;
	}
	
	public String customize(){
		User user = ContextHolder.getUserManager().getLoginUser();
		StringWriter w = new StringWriter();
		JSONBuilder jb = new JSONBuilder(w);
		jb.object();
		if(user ==null||user.getID()==0){
			jb.key("result").value(false);
		}else{
			jb.key("result").value(true);
		}
		jb.endObject();
		json = w.toString();
		return Action.JSON;
	}
	
	private String getplink(String name, long id){
		return name;
	}
	private String getlink(String name, String symbol){
		return name;
	}
	public static void main(String[] args){
		
		portfoliowidget pw = new portfoliowidget();;
		pw.portfolioID = Long.valueOf(33160);
		pw.view();
		System.out.println(pw.json);
	}
}

