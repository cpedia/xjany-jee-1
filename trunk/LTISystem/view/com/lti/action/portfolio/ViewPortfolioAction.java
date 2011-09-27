package com.lti.action.portfolio;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.lti.Exception.PortfolioException;
import com.lti.Exception.Security.NoPriceException;
import com.lti.action.Action;
import com.lti.permission.PermissionChecker;
import com.lti.permission.PortfolioPermissionChecker;
import com.lti.permission.PublicMaker;
import com.lti.service.AssetClassManager;
import com.lti.service.GroupManager;
import com.lti.service.PortfolioHoldingManager;
import com.lti.service.PortfolioManager;
import com.lti.service.SecurityManager;
import com.lti.service.StrategyManager;
import com.lti.service.UserManager;
import com.lti.service.bo.EmailNotification;
import com.lti.service.bo.GroupRole;
import com.lti.service.bo.HoldingItem;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.PortfolioInf;
import com.lti.service.bo.Security;
import com.lti.service.bo.Strategy;
import com.lti.service.bo.User;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.type.finance.HoldingInf;
import com.lti.util.StringUtil;
import com.opensymphony.xwork2.ActionSupport;

public class ViewPortfolioAction extends ActionSupport implements Action {
	private static final long serialVersionUID = 1L;
	private Long ID;
	private String validName;
	private Boolean emailNotification;
	private Date date;
	private Boolean operation;
	private Boolean isOwner;
	private Boolean isAdmin;
	private Boolean isAdvancedUser;
	private boolean isPublic=false;
	private Boolean isPlanPortfolio = false;
	private Boolean isPersonal = false;
	private Boolean isFullyPublic = false;
	private Date lastLegalDate=null;
	
	public Date getLastLegalDate() {
		return lastLegalDate;
	}

	public void setLastLegalDate(Date lastLegalDate) {
		this.lastLegalDate = lastLegalDate;
	}

	public boolean isPublic() {
		return isPublic;
	}

	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

	private long time=System.currentTimeMillis();
	

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public Boolean getIsPersonal() {
		return isPersonal;
	}

	public void setIsPersonal(Boolean isPersonal) {
		this.isPersonal = isPersonal;
	}

	public Boolean getIsFullyPublic() {
		return isFullyPublic;
	}

	public void setIsFullyPublic(Boolean isFullyPublic) {
		this.isFullyPublic = isFullyPublic;
	}

	private Boolean read;
	private String roleDelayed = "";
	private String roleRealtime = "";
	private String roleOperation = "";

	PortfolioManager portfolioManager;
	UserManager userManager;
	StrategyManager strategyManager;
	SecurityManager securityManager;
	GroupManager groupManager;

	public Long getID() {
		return ID;
	}

	public void setID(Long id) {
		ID = id;
	}

	public Boolean getEmailNotification() {
		return emailNotification;
	}

	public void setEmailNotification(Boolean emailNotification) {
		this.emailNotification = emailNotification;
	}

	private Boolean realtime = false;

	private Portfolio portfolio;
	private HoldingInf holding;
	private List<Date> holdingDates;
	private List<List<String>> holdingInfs;
	private boolean aggregateFlag;

	public boolean isAggregateFlag() {
		return aggregateFlag;
	}

	public void setAggregateFlag(boolean aggregateFlag) {
		this.aggregateFlag = aggregateFlag;
	}

	private void initEmailNotifiction(long userid, long portfolioid) {
		EmailNotification en = userManager.getEmailNotification(userid, portfolioid);

		if (en != null)
			setEmailNotification(true);
		else
			setEmailNotification(false);
	}

	private PermissionChecker pc;
	private Strategy plan;
	
	public PermissionChecker getPc() {
		return pc;
	}

	public void setPc(PermissionChecker pc) {
		this.pc = pc;
	}

	public Strategy getPlan() {
		return plan;
	}

	public void setPlan(Strategy plan) {
		this.plan = plan;
	}
	private Date endtime;
	
	public Date getEndtime() {
		return endtime;
	}

	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}
	private List<HoldingItem> holdingItems;
	public List<HoldingItem> getHoldingItems() {
		return holdingItems;
	}

	public void setHoldingItems(List<HoldingItem> holdingItems) {
		this.holdingItems = holdingItems;
	}
	
	
	private String RiskProfile=null;
	public String getRiskProfile() {
		return RiskProfile;
	}

	public void setRiskProfile(String riskProfile) {
		RiskProfile = riskProfile;
	}

	public String getRebalanceFrequency() {
		return RebalanceFrequency;
	}

	public void setRebalanceFrequency(String rebalanceFrequency) {
		RebalanceFrequency = rebalanceFrequency;
	}

	public String getNumberOfMainRiskyClass() {
		return NumberOfMainRiskyClass;
	}

	public void setNumberOfMainRiskyClass(String numberOfMainRiskyClass) {
		NumberOfMainRiskyClass = numberOfMainRiskyClass;
	}

	public String getNumberOfMainStableClass() {
		return NumberOfMainStableClass;
	}

	public void setNumberOfMainStableClass(String numberOfMainStableClass) {
		NumberOfMainStableClass = numberOfMainStableClass;
	}

	public String getPortfolioType() {
		return portfolioType;
	}

	public void setPortfolioType(String portfolioType) {
		this.portfolioType = portfolioType;
	}

	public String getSpecifyAssetFund() {
		return SpecifyAssetFund;
	}

	public void setSpecifyAssetFund(String specifyAssetFund) {
		SpecifyAssetFund = specifyAssetFund;
	}

	public String getPlanName() {
		return PlanName;
	}

	public void setPlanName(String planName) {
		PlanName = planName;
	}

	private String RebalanceFrequency="Monthly";
	private String NumberOfMainRiskyClass="";
	private String NumberOfMainStableClass = "";
	private String portfolioType = "SAA";
	private String SpecifyAssetFund="false";
	private String PlanName="";
	private int availableFunds=0;
	private String listMajorAsset="";
	private String listPercent="";
	private String MajorAssetCount="0";
	private List<String> listMajorAssets=null;
	private String listMainAssetClassWeight;
	private Date endDate;
	
	public String getListPercent()
	{
		return listPercent;
	}

	public void setListPercent(String listPercent)
	{
		this.listPercent = listPercent;
	}

	public String getListMainAssetClassWeight() {
		return listMainAssetClassWeight;
	}

	public void setListMainAssetClassWeight(String listMainAssetClassWeight) {
		this.listMainAssetClassWeight = listMainAssetClassWeight;
	}

	private String createDate = "null";

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getMajorAssetCount() {
		return MajorAssetCount;
	}

	public void setMajorAssetCount(String majorAssetCount) {
		MajorAssetCount = majorAssetCount;
	}

	public int getAvailableFunds() {
		return availableFunds;
	}

	public void setAvailableFunds(int availableFunds) {
		this.availableFunds = availableFunds;
	}

	public String getListMajorAsset() {
		return listMajorAsset;
	}

	public void setListMajorAsset(String listMajorAsset) {
		this.listMajorAsset = listMajorAsset;
	}

	public String execute() throws NoPriceException, PortfolioException {
		PortfolioHoldingManager holdingManager = ContextHolder.getPortfolioHoldingManager();
		UserManager userManager = ContextHolder.getUserManager();
		PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();
		StrategyManager strm=ContextHolder.getStrategyManager();
		User user = userManager.getLoginUser();
		Long userID = user.getID();

		portfolio = portfolioManager.get(ID);
		if (portfolio == null) {
			portfolio = portfolioManager.getOriginalPortfolio(ID);
			if(portfolio==null){
				addActionError("The portfolio doesn't exist.");
				return Action.ERROR;
			}else{
				ID=portfolio.getID();
			}
			
		}
		
		endDate = portfolio.getEndDate();
		String planIDStr = portfolio.getStrategies().getAssetAllocationStrategy().getParameter().get("PlanID");
		if(planIDStr != null)
			isPlanPortfolio = true;
		
		try {
			plan = strategyManager.get(Long.parseLong(portfolio.getStrategies().getAssetAllocationStrategy().getParameter().get("PlanID")));
		} catch (Exception e1) {
			//e1.printStackTrace();
		}

		
		isPersonal = portfolio.isPersonal();
		isFullyPublic = portfolio.isFullyPublic();
		
		pc = new PortfolioPermissionChecker(portfolio, ServletActionContext.getRequest());		
		isAdmin = pc.isAdmin();
		isOwner = pc.isOwner();		
		isAdvancedUser=pc.isAdvancedUser();
		
		if(isFullyPublic){
			realtime = true;
			operation = true;
			read = true;
			lastLegalDate = portfolio.getEndDate();
		}else{
			realtime = pc.hasRealtimeRole();
			lastLegalDate = pc.getLastLegalDate();
			operation = pc.hasOperationRole();
			read = pc.hasViewRole();
		}

		
		if(isPersonal && !isFullyPublic){
			if(!isAdmin && !isOwner){
				addActionError("You have no permission to view this portfolio.");
				return Action.ERROR;
			}
		}
		
		PublicMaker publicMaker = new PublicMaker(portfolio);
		isPublic=publicMaker.isPublic();

		initEmailNotifiction(userID, ID);

		validName = StringUtil.getValidName(portfolio.getName());
		
		initGroupInformation();

//		if (read == false && isOwner == false) {
//			addActionError("You are trying to access a private portfolio. Please retrun to the previous page or the home page. Further questions, please contact support@myplaniq.com . Thank you for your suggestions. ");
//			return Action.ERROR;
//		}

		if (!realtime) {
			PortfolioInf _pif=portfolioManager.getPortfolioInf(ID, Configuration.PORTFOLIO_HOLDING_DELAY);
			if(_pif!=null)holding = _pif.getHolding();
		} else {
			PortfolioInf _pif=portfolioManager.getPortfolioInf(ID, Configuration.PORTFOLIO_HOLDING_CURRENT);
			if(_pif!=null)holding = _pif.getHolding();
		}
		
//		if(holding==null){
//			holding=new HoldingInf(ID, 10000.0, pc.getLastLegalDate());
//		}

		// chartUrl=plotHodingChart(portfolioManager,portfolio);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (portfolio != null) {
			try {
				String sql = "select distinct h.date from " + Configuration.TABLE_PORTFOLIO_HOLDINGITEM + " h where h.PortfolioID=" + ID;
				if (!realtime) {
					sql += " and h.date<='" + sdf.format(pc.getLastLegalDate()) + "'";
				}
				holdingDates = holdingManager.findBySQL(sql);// --------------
				if (realtime) {
					try {
						if (!holding.getCurrentDate().equals(holdingDates.get(holdingDates.size() - 1)))
							holdingDates.add(holding.getCurrentDate());
					} catch (Exception e) {
					}
				}else{
					portfolio.setEndDate(holdingDates.get(holdingDates.size() - 1));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//获取Symbols
		PortfolioInf pinf=portfolioManager.getPortfolioInf(ID, Configuration.PORTFOLIO_HOLDING_CURRENT);
		holdingInfs = new ArrayList<List<String>>();
		if(pinf!=null&&pinf.getHolding()!=null){
			holdingItems = pinf.getHolding().getHoldingItems();
			double amount = pinf.getHolding().getAmount();
			DecimalFormat format = new DecimalFormat("########");
			for(HoldingItem hi:holdingItems){
				List<String> list = new ArrayList<String>();
				list.add(hi.getAssetName());
				list.add(hi.getSymbol());
				String amountStr = format.format(hi.getPercentage()*amount);
				list.add(amountStr);
				list.add(amountStr);
				holdingInfs.add(list);
			}
		}
		
		//判断是否为aggregate portfolio类型
		aggregateFlag = portfolioManager.get(ID).isType(Configuration.PORTFOLIO_TYPE_COMPOUND);
		try{
			PortfolioInf pinfs=portfolioManager.getPortfolioInf(ID, Configuration.PORTFOLIO_HOLDING_CURRENT);
			holdingItems = pinfs.getHolding().getHoldingItems();
		}catch(Exception e){
			holdingItems = null;
		}
		
		//获取description
		SimpleDateFormat sdfd=new SimpleDateFormat("MM/dd/yyyy");
		try{
			createDate=sdfd.format(portfolio.getCreatedDate());
		}catch(Exception e){
			
		}
		try{
			long planID=Long.parseLong(portfolio.getStrategies().getAssetAllocationStrategy().getParameter().get("PlanID"));
			PlanName = strm.get(planID).getName();
			availableFunds = strm.get(planID).getVariablesFor401k().size();
			listMajorAssets = strm.getMajorAssetByPlanID(planID);
			MajorAssetCount = String.valueOf(listMajorAssets.size());
			listMajorAsset = listMajorAssets.toString().replaceAll("\\[", "").replaceAll("\\]", "");
			listPercent = listMajorAssets.toString().replaceAll("\\[", "").replaceAll("\\]", "");
		} catch (Exception e1) {
			
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
                if("MainAssetClassWeight".equals(key)){
                	listMainAssetClassWeight = CdcEntryItem.elementTextTrim("value");
                }
                if("MainAssetClass".equals(key)){
                	listPercent = CdcEntryItem.elementTextTrim("value");
                }
            }
			for (Element CdcEntryItem2 : list2) {
				portfolioType = CdcEntryItem2.elementTextTrim("name");
			}
			
			}catch(Exception e){
				//e.printStackTrace();
			}
			//
		
		return Action.SUCCESS;
	}

	private String totalAmount = "";
	private String chartUrl = "nochart.jpg";

	/**
	 * * Add for the Groups Permission Su Ping 2009-12-10
	 */
	public void initGroupInformation() {
		if (groupManager.getGroupRolesByResource(ID, Configuration.RESOURCE_TYPE_PORTFOLIO) != null) {
			List<GroupRole> grs = groupManager.getGroupRolesByResource(ID, Configuration.RESOURCE_TYPE_PORTFOLIO);
			for (int i = 0; i < grs.size(); i++) {
				if (grs.get(i).getGroupID() != 0L) {
					String groupname = groupManager.get(grs.get(i).getGroupID()).getName();
					if (groupname.equalsIgnoreCase(Configuration.GROUP_ANONYMOUS))
						groupname = Configuration.ANONYMOUS;
					if (groupname.equalsIgnoreCase(Configuration.GROUP_MEMBER))
						groupname = Configuration.MEMBER;
					if (groupname.equalsIgnoreCase(Configuration.GROUP_SUPER))
						groupname = Configuration.SUPER;
					String rolename = groupManager.getRole(grs.get(i).getRoleID()).getName();
					if (rolename.equalsIgnoreCase(Configuration.ROLE_PORTFOLIO_DELAYED.toLowerCase())) {
						roleDelayed += groupname + ",";
					} else if (rolename.equalsIgnoreCase(Configuration.ROLE_PORTFOLIO_REALTIME.toLowerCase())) {
						roleRealtime += groupname + ",";
					} else if (rolename.equalsIgnoreCase(Configuration.ROLE_PORTFOLIO_OPERATION.toLowerCase())) {
						roleOperation += groupname + ",";
					}
				}
			}
		}
	}
	
	
	private String assetNames;
	private String securityNames;
	private String amount;
    private String minSell;
    private String minBuy;
    private String avaliableToSell;
    private String message;
    
	public String transation(){
		PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();	
		
		Map<String, Double> AssetAvailableTradingPercentage = new HashMap<String, Double>();
		
		Map<String, Double> SecurityAvailableTradingPercentage = new HashMap<String, Double>();
		
		Map<String,List<String>> assetSecurityMap = new HashMap<String, List<String>>();
		
		List<String> HoldingAssetList = new ArrayList<String>();
		
		Map<String,List<String>> AssetRepresentFundMap = new HashMap<String, List<String>>();
		
		Map<String,Double> SelectedAssetPercentagesMap = new HashMap<String, Double>();
		
		List<String> HoldingSecurityList = null;
		
		
		Map<String,HashMap<String,Double>>AssetSecurityTradingPercentageMap =null;
		
		Map<String,Double> AssetActualPercentagesMap = new HashMap<String,Double>();
		
		Map<String,Double> FundPercentageMap = new HashMap<String, Double>();
		
		Map<String,Double> securityActualAmountMap = new HashMap<String,Double>();
		double MinimumBuyingPercentage;
		double MinimumSellingPercentage;
		double aa;
		message ="";

		
		
	/********************** Initialize Parameters***************************************************/
		
		PortfolioInf pif = portfolioManager.getPortfolioInf(ID, Configuration.PORTFOLIO_HOLDING_CURRENT);
		HoldingInf hif = pif.getHolding();
		
		
		
		List<HoldingItem> holdingList = hif.getHoldingItems();
		for(HoldingItem hi: holdingList){
			String assetName = hi.getAssetName();
			String symbol = hi.getSymbol();
			double percentage = hi.getPercentage();
			FundPercentageMap.put(symbol, percentage);
			
			if(AssetRepresentFundMap.get(assetName)!= null){
				AssetRepresentFundMap.get(assetName).add(symbol);
			}else{
				List<String> lists = new ArrayList<String>();
				lists.add(symbol);
				AssetRepresentFundMap.put(assetName, lists);
			}
			
			if(SelectedAssetPercentagesMap.get(assetName)!=null){
				double dd = SelectedAssetPercentagesMap.get(assetName)+percentage;
				SelectedAssetPercentagesMap.put(assetName, dd);
			}else{
				SelectedAssetPercentagesMap.put(assetName, percentage);
			}
		}
		
		
		String[] assetItems = assetNames.split(",");
		String[] securityItems = securityNames.split(",");
		String[] amountItems = amount.split(",");
		String[] avaSellItems = avaliableToSell.split(",");
		
		double [] percentItems =new double[amountItems.length];
		double [] avaSellPercentItems = new double[amountItems.length];
		double totleAmounts=0.0;
		
		for(int i=0;i<securityItems.length;i++){
			int end = securityItems[i].indexOf("(");
			if(end!=-1){
				securityItems[i] = securityItems[i].substring(0,end);
			}
		}
		
		for(int i =0;i<amountItems.length;i++){
			totleAmounts += Double.parseDouble(amountItems[i]);
			percentItems[i] = Double.parseDouble(amountItems[i]);
		}
		for(int k=0;k<percentItems.length;k++){
			percentItems[k]=percentItems[k]/totleAmounts;
		}
		
		for(int j = 0;j<avaSellItems.length;j++){
			double sellAmount = Double.parseDouble(avaSellItems[j]);
			avaSellPercentItems[j] = sellAmount/totleAmounts;
		}
		
		for(int i=0;i<assetItems.length;i++){
			if(AssetActualPercentagesMap.get(assetItems[i])!=null){
				double d1 = AssetActualPercentagesMap.get(assetItems[i])+percentItems[i];
				AssetActualPercentagesMap.put(assetItems[i], d1);
			}else{
				AssetActualPercentagesMap.put(assetItems[i], percentItems[i]);
			}
		}
		
		for(int i = 0;i<assetItems.length;i++){
			if(assetSecurityMap.get(assetItems[i].trim())==null){
				List<String> lists = new ArrayList<String>();
				lists.add(securityItems[i]);
				assetSecurityMap.put(assetItems[i].trim(), lists);
			}else{
				assetSecurityMap.get(assetItems[i].trim()).add(securityItems[i]);
			}
		}
		
		
		for(int j=0;j<assetItems.length;j++){
			
			if(AssetAvailableTradingPercentage.get(assetItems[j].trim())==null){				
				AssetAvailableTradingPercentage.put(assetItems[j], avaSellPercentItems[j]);
			}else{
				double dd = AssetAvailableTradingPercentage.get(assetItems[j]) +avaSellPercentItems[j];
				AssetAvailableTradingPercentage.put(assetItems[j], dd);
			}
		}
		
		for(int k = 0;k<securityItems.length;k++){
			String fund = securityItems[k].trim();
			
			SecurityAvailableTradingPercentage.put(fund, avaSellPercentItems[k]);
		}
		
		for(String s :assetItems){
			if(!HoldingAssetList.contains(s.trim()))
				HoldingAssetList.add(s.trim());
		}
		
		
		for(int i =0;i<securityItems.length;i++){
			securityActualAmountMap.put(securityItems[i].trim(), Double.valueOf(amountItems[i]));
		}
		
		double minSellAmount = Double.parseDouble(minSell);
		MinimumSellingPercentage = minSellAmount/totleAmounts;
		
		double minBuyAmount = Double.parseDouble(minBuy);
		MinimumBuyingPercentage = minBuyAmount/totleAmounts;
		
		/**********************End Initialize Parameters***************************************************/
		
		
		boolean New;
		{
			Iterator<String> iter = SelectedAssetPercentagesMap.keySet().iterator();
			while (iter.hasNext()) {
				New = false;
				String AssetName = iter.next();
				if (!HoldingAssetList.contains(AssetName)) {
					New = true;
				}
				if (New) {
					HoldingAssetList.add(AssetName);
				} 

			}
		}
		
		
	for(HoldingItem hi :holdingList){	
		String assetName = hi.getAssetName();
		String securityName = hi.getSymbol();
		if(!securityNames.contains(securityName)){
			SecurityAvailableTradingPercentage.put(securityName, 0.0);
			securityActualAmountMap.put(securityName, 0.0);
			if(assetSecurityMap.keySet().contains(assetName)){
				assetSecurityMap.get(assetName).add(securityName);
			}
			else{
				List<String> list = new ArrayList<String>();
				list.add(securityName);
				assetSecurityMap.put(assetName, list);
			}
		}
	}
		
		
		for (int i = 0; i < HoldingAssetList.size(); i++) {
			New = true;
			if (SelectedAssetPercentagesMap.get(HoldingAssetList.get(i).trim()) != null) {
				New = false;
			}

			if (New) {
				SelectedAssetPercentagesMap.put(HoldingAssetList.get(i).trim(), 0.0);
			}
		}

		/* calculate the asset trading percentage */

		HashMap<String, Double> HoldingAssetActualPercentage = new HashMap<String, Double>();

		for (int i = 0; i < HoldingAssetList.size(); i++) {
			if(AssetActualPercentagesMap.get(HoldingAssetList.get(i).trim())!=null){
				aa = AssetActualPercentagesMap.get(HoldingAssetList.get(i));
				HoldingAssetActualPercentage.put(HoldingAssetList.get(i).trim(), aa);
			}else{
				HoldingAssetActualPercentage.put(HoldingAssetList.get(i).trim(), 0.00);
			}						
		}

		HashMap<String, Double> AssetActualTradingPercentage = new HashMap<String, Double>();
		String AssetName;
		double UnableToSellPercentage = 0;
		double AvailableTradingPercentage;
		double TargetPercentage;
		double ActualPercentage;
		double TotalTargetSellPercentage = 0;
		double TotalAvailableCapital = 0;
		double TotalSellPercentageOfStableAsset = 0;

		for (int i = 0; i < HoldingAssetList.size(); i++) {
			AssetName = HoldingAssetList.get(i).trim();
			ActualPercentage = HoldingAssetActualPercentage.get(AssetName).doubleValue();
			if (AssetAvailableTradingPercentage.get(AssetName) == null) {
				AvailableTradingPercentage = 0.0;
				//printToLog(AssetName + ": 0.0");
			} else {
				AvailableTradingPercentage = AssetAvailableTradingPercentage.get(AssetName).doubleValue();

			}
			if (SelectedAssetPercentagesMap.get(AssetName.trim()) != null)
				TargetPercentage = SelectedAssetPercentagesMap.get(AssetName.trim()).doubleValue();
			else
				TargetPercentage = 0;
			if (ActualPercentage > TargetPercentage) {
				TotalTargetSellPercentage += (ActualPercentage - TargetPercentage);
				if (AvailableTradingPercentage >= ActualPercentage - TargetPercentage){
					AssetActualTradingPercentage.put(AssetName, new Double(TargetPercentage - ActualPercentage));
					if(AssetName.equals("CASH") || AssetName.equals("INTERNATIONAL BONDS") || AssetName.equals("FIXED INCOME"))
                        TotalSellPercentageOfStableAsset += (ActualPercentage - TargetPercentage);
				}					
				else {
					AssetActualTradingPercentage.put(AssetName, new Double(-AvailableTradingPercentage));
					UnableToSellPercentage += (ActualPercentage - TargetPercentage - AvailableTradingPercentage);
					if(AssetName.equals("CASH") || AssetName.equals("INTERNATIONAL BONDS") || AssetName.equals("FIXED INCOME"))
                        TotalSellPercentageOfStableAsset += AvailableTradingPercentage;
				}
			}
		}
		TotalAvailableCapital = TotalTargetSellPercentage - UnableToSellPercentage;
        //System.out.println("Test0501: TotalAvailableCapital = " + TotalAvailableCapital + "  TotalSellPercentageOfStableAsset = " + TotalSellPercentageOfStableAsset);
		
		// Sactisfy the stable asset in priority
        // how much stable asset we have
        double totalCurrentStableAssetPerc = 0;
        for (int i = 0; i < HoldingAssetList.size(); i++) {
                String assetName = HoldingAssetList.get(i).trim();
                if(assetName.equals("CASH") || assetName.equals("INTERNATIONAL BONDS") || assetName.equals("FIXED INCOME"))
                	totalCurrentStableAssetPerc += HoldingAssetActualPercentage.get(assetName.trim());    
        } 

        //how much stable asset we should hold
        double StablePercentageShouldHold = 0;
        Iterator<String> iter9 = SelectedAssetPercentagesMap.keySet().iterator();            
        while(iter9.hasNext()){
        	AssetName = iter9.next();
            if(AssetName.equals("CASH") || AssetName.equals("INTERNATIONAL BONDS") || AssetName.equals("FIXED INCOME"))                           
                StablePercentageShouldHold += SelectedAssetPercentagesMap.get(AssetName);
         	}

         // The least Stable Asset We should buy
         double LeastStableToBuy = StablePercentageShouldHold - totalCurrentStableAssetPerc + TotalSellPercentageOfStableAsset;
         if(LeastStableToBuy > TotalAvailableCapital)
             LeastStableToBuy = TotalAvailableCapital;
      // try to sell CASH to satisfy the risk profile
         if(LeastStableToBuy < 0 && AssetAvailableTradingPercentage.containsKey("CASH"))     // It means Current Stable Percentage is greater than what (100-risky profile)
         {
             double alreadySoldCASH;
             if(AssetActualTradingPercentage.containsKey("CASH"))
                 alreadySoldCASH = AssetActualTradingPercentage.get("CASH");
             else
                 alreadySoldCASH = 0;
             double leftAvailableCASH = AssetAvailableTradingPercentage.get("CASH") + alreadySoldCASH;
             double sellMoreCASH = leftAvailableCASH < (-LeastStableToBuy)?leftAvailableCASH:(-LeastStableToBuy);
             AssetActualTradingPercentage.put("CASH", alreadySoldCASH - sellMoreCASH);
             LeastStableToBuy += sellMoreCASH;
             TotalAvailableCapital += sellMoreCASH;
             //printToLog("test0501: Adjust : LeastStableToBuy = " + LeastStableToBuy + "   CASH Actual Trade = " + (alreadySoldCASH - sellMoreCASH) + "  TotalAvailableCapital = " + TotalAvailableCapital);
          }

        // System.out.println("test0501: totalCurrentStableAssetPerc = " + totalCurrentStableAssetPerc + "StablePercentageShouldHold = " + StablePercentageShouldHold+ "  LeastStableToBuy = "  + LeastStableToBuy );
         
         //  how much stable asset we are going to buy
                boolean meetStablePercentage = false;
                if(LeastStableToBuy < MinimumBuyingPercentage)
                    meetStablePercentage = true;
                    
                Iterator<String> iter8 = SelectedAssetPercentagesMap.keySet().iterator();
                while (iter8.hasNext()) {
                		AssetName = iter8.next();
                        if(AssetName.equals("CASH") || AssetName.equals("INTERNATIONAL BONDS") || AssetName.equals("FIXED INCOME"))
                        {
                        	TargetPercentage = SelectedAssetPercentagesMap.get(AssetName.trim()).doubleValue();
                        	if (HoldingAssetActualPercentage.get(AssetName) != null)
                        		ActualPercentage = HoldingAssetActualPercentage.get(AssetName).doubleValue();
                        	else
                        		ActualPercentage = 0;
                        	if (ActualPercentage <= TargetPercentage)
                            {
                                 if(meetStablePercentage)
                                     AssetActualTradingPercentage.put(AssetName, new Double(0.0));
                                 else{
                                     double toBuy = 0;
                                     if(LeastStableToBuy < (TargetPercentage - ActualPercentage))
                                         toBuy = LeastStableToBuy;
                                     else
                                         toBuy = TargetPercentage - ActualPercentage;
                                     if(toBuy < MinimumBuyingPercentage && TotalAvailableCapital >= MinimumBuyingPercentage  && toBuy > 0)
                                         toBuy = MinimumBuyingPercentage;        
                                     AssetActualTradingPercentage.put(AssetName, new Double(toBuy));
                                  // System.out.println("test0501 : Asset = " + AssetName  +  "  to buy = " + toBuy);
                                     TotalAvailableCapital -= toBuy;
                                      LeastStableToBuy -= toBuy;
                                      if(LeastStableToBuy <= 0)
                                          meetStablePercentage = true;
                                 }
                            }
                         }
                 }  // end of "iter8"

               // System.out.println("test0501 : Asset Actual Trading Percentage (after stable allocation) : " + AssetActualTradingPercentage);
      

                      //  if we have available capital, then decide how much to buy Risky Asset

                // we want to buy how much risky asset
                double TotalTargetBuyOfRiskyAsset = 0;
               Iterator<String> iter7 = SelectedAssetPercentagesMap.keySet().iterator();
              while (iter7.hasNext()) {
	                AssetName = iter7.next();
                        if(!AssetName.equals("CASH") && !AssetName.equals("INTERNATIONAL BONDS") && !AssetName.equals("FIXED INCOME"))
                        {
                            TargetPercentage = SelectedAssetPercentagesMap.get(AssetName.trim()).doubleValue();
                            if (HoldingAssetActualPercentage.get(AssetName) != null)
                            	ActualPercentage = HoldingAssetActualPercentage.get(AssetName).doubleValue();
                            else
                            	ActualPercentage = 0;
                            if (ActualPercentage <= TargetPercentage)
                                TotalTargetBuyOfRiskyAsset += (TargetPercentage - ActualPercentage);
                         }
                 }  // end of "iter7"

		double BuyWeight = TotalAvailableCapital / TotalTargetBuyOfRiskyAsset;
		//System.out.println("test0501 : (after stable allocation) TotalAvailableCapital = " + TotalAvailableCapital +  " TotalTargetBuyOfRiskyAsset = " + TotalTargetBuyOfRiskyAsset);
		double UnableToBuyPercentage = 0;
		boolean BelowMinimum = false;
		boolean AbleToBuyBoolean = false;
		Map<String, Integer> AbleToBuy = new HashMap<String, Integer>();
		Map<String, Double> PlanToBuyPercentage = new HashMap<String, Double>();
		{
			Iterator<String> iter = SelectedAssetPercentagesMap.keySet().iterator();
			while (iter.hasNext()) {
				AssetName = iter.next();
				if(!AssetName.equals("CASH") && !AssetName.equals("INTERNATIONAL BONDS") && !AssetName.equals("FIXED INCOME")){
					
				AbleToBuy.put(AssetName, 0);
				TargetPercentage = SelectedAssetPercentagesMap.get(AssetName.trim()).doubleValue();
				if (HoldingAssetActualPercentage.get(AssetName) != null)
					ActualPercentage = HoldingAssetActualPercentage.get(AssetName).doubleValue();
				else
					ActualPercentage = 0;
				if (ActualPercentage <= TargetPercentage + 0.00001) {
					if ((TargetPercentage - ActualPercentage) * BuyWeight > MinimumBuyingPercentage)
						AssetActualTradingPercentage.put(AssetName, new Double((TargetPercentage - ActualPercentage) * BuyWeight));
                                            else if((TargetPercentage - ActualPercentage)*BuyWeight + UnableToBuyPercentage  > MinimumBuyingPercentage)
                                            {
                                                 AssetActualTradingPercentage.put(AssetName, new Double((TargetPercentage - ActualPercentage)*BuyWeight) +  UnableToBuyPercentage);
                                                 UnableToBuyPercentage = 0;
                                            }
					else {
						AssetActualTradingPercentage.put(AssetName, new Double(0));
						UnableToBuyPercentage += ((TargetPercentage - ActualPercentage) * BuyWeight);
						BelowMinimum = true;
						if ((TargetPercentage - ActualPercentage) >= MinimumBuyingPercentage) {
							AbleToBuy.put(AssetName, 1);
							PlanToBuyPercentage.put(AssetName, TargetPercentage - ActualPercentage);
							AbleToBuyBoolean = true;
						} else
							AbleToBuy.put(AssetName, 0);
					}
				}
			}
		}// end of "iter"
	}
		Iterator<String> iter10 = SelectedAssetPercentagesMap.keySet().iterator();
		  while (iter10.hasNext()) {
			  AssetName = iter10.next();
                    if(!AssetActualTradingPercentage.containsKey(AssetName))
                         AssetActualTradingPercentage.put(AssetName, new Double(0.0));
                    }
                   // printToLog("Asset Actual Trading Percentage (before adjusted 1) : " + AssetActualTradingPercentage);

		if (BelowMinimum && AbleToBuyBoolean) {
			{
				Iterator<String> iter = SelectedAssetPercentagesMap.keySet().iterator();
				int i = 0;
				while (iter.hasNext()) {
					AssetName = iter.next();
					i++;
				if(!AssetName.equals("CASH") && !AssetName.equals("INTERNATIONAL BONDS") && !AssetName.equals("FIXED INCOME")){
					
					if (UnableToBuyPercentage > 0 && i <= SelectedAssetPercentagesMap.size()) {
						if (AbleToBuy.get(AssetName) == 1) {
							if (PlanToBuyPercentage.get(AssetName) < UnableToBuyPercentage) {
								AssetActualTradingPercentage.put(AssetName, new Double(PlanToBuyPercentage.get(AssetName)));
								UnableToBuyPercentage -= PlanToBuyPercentage.get(AssetName);
							} else {
								AssetActualTradingPercentage.put(AssetName, new Double(UnableToBuyPercentage));
								UnableToBuyPercentage = 0;
							}
						}
					}
				}
			}// end of "iter"
		}
	}

                    //printToLog("Asset Actual Trading Percentage (before adjusted 2) : " + AssetActualTradingPercentage);
		/*
		 * Calculate: use the left percentage to buy the asset with the max
		 * Target Percentage
		 */
                   // printToLog("UnableToBuyPercentage = " + UnableToBuyPercentage);
                    //printToLog("SelectedAssetPercentagesMap = " + SelectedAssetPercentagesMap);
		if (UnableToBuyPercentage > 0) {
			String MaxTargetPercentageAsset = null;
			double MaxTargetPercentage = Double.MIN_VALUE;
			double CompareTargetPercentage;
			{
				Iterator<String> iter = SelectedAssetPercentagesMap.keySet().iterator();
				while (iter.hasNext()) {
					AssetName = iter.next();
                                           // printToLog("Asset Name = " + AssetName);
					CompareTargetPercentage = SelectedAssetPercentagesMap.get(AssetName).doubleValue();
					if (CompareTargetPercentage > MaxTargetPercentage) {
                                            {
                                                double tmpTrading =  AssetActualTradingPercentage.get(AssetName);
                                                tmpTrading  +=  UnableToBuyPercentage;
                                                //printToLog("Adjusted percentage = " + tmpTrading);
                                                if(tmpTrading < -MinimumSellingPercentage || tmpTrading > MinimumBuyingPercentage)
                                                {
						MaxTargetPercentage = CompareTargetPercentage;
						MaxTargetPercentageAsset = AssetName;
					    }
				      	}
				}
			}
                            if(MaxTargetPercentageAsset != null)
                            {
			    double TempTrading = AssetActualTradingPercentage.get(MaxTargetPercentageAsset).doubleValue();
			    double NewActualTradingPercentage = TempTrading + UnableToBuyPercentage;
			    if (NewActualTradingPercentage > -0.001 & NewActualTradingPercentage < 0.001)
				NewActualTradingPercentage = 0;
			    AssetActualTradingPercentage.put(MaxTargetPercentageAsset, new Double(NewActualTradingPercentage));
   			         }
                   }
               }

		//printToLog("Asset Actual Trading Percentage  = " + AssetActualTradingPercentage);

		/* Calculate trading percentage of each security, not rebalance */

		AssetSecurityTradingPercentageMap = new HashMap<String, HashMap<String, Double>>();
		HashMap<String, Double> SecurityActualTradingPercentage = new HashMap<String, Double>();
		HashMap<String, Double> SecurityTargetPercentageMap = new HashMap<String, Double>();
		HashMap<String, Double> HoldingSecurityActualPercentage = new HashMap<String, Double>();
		HoldingSecurityList = null;
		List<String> PresentativeSecurityList = null;
		double TotalBuyingPercentage;
		double SellingPercentage;
		double BuyingPercentage;
		double SecurityActualPercentage;
		double SecurityTargetPercentage;
		String SecurityName;
		Map<String, Integer> NewAdd = new HashMap<String, Integer>();
		double UnTradePercentage = 0;
		{
			Iterator<String> iter = SelectedAssetPercentagesMap.keySet().iterator();
			while (iter.hasNext()) {
				AssetName = iter.next();
				HoldingSecurityList = new ArrayList<String>();
				PresentativeSecurityList = new ArrayList<String>();
				PresentativeSecurityList = AssetRepresentFundMap.get(AssetName);
				TargetPercentage = SelectedAssetPercentagesMap.get(AssetName).doubleValue();
                                    double AssetActualTrading =  AssetActualTradingPercentage.get(AssetName);
                                    double LastUntrade = 0;

				if (HoldingAssetActualPercentage.get(AssetName) != null) {
					HoldingSecurityList = assetSecurityMap.get(AssetName);
					ActualPercentage = HoldingAssetActualPercentage.get(AssetName).doubleValue();
					if (AssetAvailableTradingPercentage.get(AssetName) == null) {
						AvailableTradingPercentage = 0.0;
						//printToLog(AssetName + ": 0.0");
					} else {
						AvailableTradingPercentage = AssetAvailableTradingPercentage.get(AssetName).doubleValue();

					}
					NewAdd.put(AssetName, 0);
				} else {
					HoldingSecurityList = null;
					ActualPercentage = 0;
					AvailableTradingPercentage = 0;
					NewAdd.put(AssetName, 1);
				}

                                  // added by WYJ on 2010.08.10, let the calculation take care of  the un-traded trading fraction of the former asset class 
                                    if(UnTradePercentage != 0){
                                        LastUntrade = UnTradePercentage;
                                        UnTradePercentage = 0.0;
                                       // printToLog("adjust the asset target percentage and reset the UnTradePercentage : " + AssetName + "  ");
                                    }

				// printToLog("i = " + i + "  ActualPercentage = " +
				// ActualPercentage + "  TargetPercentage = " +
				// TargetPercentage
				// + "  AvailableTradingPercentage = " +
				// AvailableTradingPercentage +
				// "  AssetActualTradingPercentage = " +
				// AssetActualTradingPercentage.get(AssetName).doubleValue()
				// );

				if ((AvailableTradingPercentage <= ActualPercentage - TargetPercentage && AvailableTradingPercentage != 0) || PresentativeSecurityList == null || PresentativeSecurityList.size() == 0 && HoldingSecurityList != null) {
					SecurityActualTradingPercentage = new HashMap<String, Double>();
					for (int j = 0; j < HoldingSecurityList.size(); j++) {
						SellingPercentage = SecurityAvailableTradingPercentage.get(HoldingSecurityList.get(j).trim());
						SecurityActualTradingPercentage.put(HoldingSecurityList.get(j).trim(), new Double(-SellingPercentage));
					}
					AssetSecurityTradingPercentageMap.put(AssetName.trim(), SecurityActualTradingPercentage);
                                            UnTradePercentage += LastUntrade;
				} else if (NewAdd.get(AssetName) == 1) {
					SecurityActualTradingPercentage = new HashMap<String, Double>();
					BuyingPercentage = (AssetActualTradingPercentage.get(AssetName) + LastUntrade) / PresentativeSecurityList.size();
					for (int j = 0; j < PresentativeSecurityList.size(); j++)
						SecurityActualTradingPercentage.put(PresentativeSecurityList.get(j), new Double(BuyingPercentage));
					AssetSecurityTradingPercentageMap.put(AssetName.trim(), SecurityActualTradingPercentage);
				} else if (AvailableTradingPercentage > ActualPercentage - TargetPercentage) {
					SecurityActualTradingPercentage = new HashMap<String, Double>();
					for (int j = 0; j < HoldingSecurityList.size(); j++) {
						SellingPercentage = SecurityAvailableTradingPercentage.get(HoldingSecurityList.get(j).trim());
						SecurityActualTradingPercentage.put(HoldingSecurityList.get(j).trim(), new Double(-SellingPercentage));
						//printToLog("Only For Debug  -----  Selling all holdings Step :   " + HoldingSecurityList.get(j).trim() + "  Amount = " + TotalAmount * SellingPercentage);
					}
					TotalBuyingPercentage = AssetActualTradingPercentage.get(AssetName) + LastUntrade + AvailableTradingPercentage;
					// printToLog(" Actual Target Percentage = " +
					// (TotalBuyingPercentage + ActualPercentage -
					// AvailableTradingPercentage));
					int j = 0;
					while (TotalBuyingPercentage > 0.001) {
						SecurityName = PresentativeSecurityList.get(j).trim();
						SecurityTargetPercentage = FundPercentageMap.get(SecurityName) + (LastUntrade / PresentativeSecurityList.size());
						if (SecurityAvailableTradingPercentage.get(SecurityName) == null) {
							SecurityActualPercentage = 0;
							SellingPercentage = 0;
						} else {
							SecurityActualPercentage = SecurityAvailableTradingPercentage.get(SecurityName);
							SellingPercentage = SecurityAvailableTradingPercentage.get(SecurityName);
						}
						// printToLog("j = " + j +
						// "  SecurityActualPercentage = " +
						// SecurityActualPercentage +
						// "  SellingPercentage = " +
						// SellingPercentage +
						// "  SecurityTargetPercentage = " +
						// SecurityTargetPercentage +
						// "  TotalBuyingPercentage = " +
						// TotalBuyingPercentage);
						if (SecurityActualPercentage - SellingPercentage >= SecurityTargetPercentage) {
							// printToLog("condition 1");
			
							if (SellingPercentage == 0 && j != PresentativeSecurityList.size() - 1) /*
																									 * modify
																									 * on
																									 * 2010.04
																									 * .15
																									 */
								SecurityActualTradingPercentage.put(SecurityName, new Double(0.0));
							else if (j == PresentativeSecurityList.size() - 1) {
								if (TotalBuyingPercentage - SellingPercentage < MinimumBuyingPercentage && TotalBuyingPercentage - SellingPercentage > -MinimumSellingPercentage) {
									UnTradePercentage += TotalBuyingPercentage - SellingPercentage;
									SecurityActualTradingPercentage.put(SecurityName, new Double(0.0));
								} else
									SecurityActualTradingPercentage.put(SecurityName, new Double(TotalBuyingPercentage - SellingPercentage));
								TotalBuyingPercentage = 0;
								// printToLog("condition 1-1");
							} else {
								if ((-SellingPercentage) < MinimumBuyingPercentage && (-SellingPercentage) > -MinimumSellingPercentage) {
									UnTradePercentage += (-SellingPercentage);
									SecurityActualTradingPercentage.put(SecurityName, new Double(0.0));
								} else
									SecurityActualTradingPercentage.put(SecurityName, new Double(-SellingPercentage));
								TotalBuyingPercentage -= SellingPercentage;
								// printToLog("condition 1-2");
							}
						} else if (SecurityActualPercentage - SellingPercentage + TotalBuyingPercentage > SecurityTargetPercentage + 0.001 && j < (PresentativeSecurityList.size() - 1)) {
							// printToLog("condition 2");
							if (SecurityTargetPercentage - SecurityActualPercentage < MinimumBuyingPercentage && SecurityTargetPercentage - SecurityActualPercentage > -MinimumSellingPercentage) {
								UnTradePercentage += (SecurityTargetPercentage - SecurityActualPercentage);
								SecurityActualTradingPercentage.put(SecurityName, new Double(0.0));
							} else
								SecurityActualTradingPercentage.put(SecurityName, new Double(SecurityTargetPercentage - SecurityActualPercentage));
							TotalBuyingPercentage -= (SecurityTargetPercentage - SecurityActualPercentage + SellingPercentage);
						} else {
							// printToLog("condition 3");
							if (TotalBuyingPercentage - SellingPercentage < MinimumBuyingPercentage && TotalBuyingPercentage - SellingPercentage > -MinimumSellingPercentage && SecurityActualPercentage != -(TotalBuyingPercentage - SellingPercentage)) {
								UnTradePercentage += (TotalBuyingPercentage - SellingPercentage);
								SecurityActualTradingPercentage.put(SecurityName, new Double(0.0));
							} else
								SecurityActualTradingPercentage.put(SecurityName, new Double(TotalBuyingPercentage - SellingPercentage));
							TotalBuyingPercentage = 0;
						}
						// printToLog("SecurityActualTradingPercentage = " +
						// SecurityActualTradingPercentage.get(SecurityName));
						j++;
					}
					AssetSecurityTradingPercentageMap.put(AssetName.trim(), SecurityActualTradingPercentage);
				}
                                    else
                                    {
                                        UnTradePercentage += LastUntrade; // printToLog("No Change Allocation to the asset : " + AssetName);
                                    }
			}
		}
                   // printToLog("un-trade percentage after security allocation = " + UnTradePercentage);
		/* For Debug *///printToLog("Actual trading Percentage Map (before adjusted) =  " + AssetSecurityTradingPercentageMap);

                    /*Adjust one buying transaction for the UnTradePercentage  Added by WYJ on 2010.05.10 */

		double TempPercentage;
		boolean Done = false;
		if (UnTradePercentage != 0)
		{
			Iterator<String> iter1 = AssetSecurityTradingPercentageMap.keySet().iterator();
			Iterator<String> iter2 = null;
			while (iter1.hasNext() && !Done) {
				String Asset = (String) iter1.next();
				HashMap<String, Double> TempTradingPercentageMap = AssetSecurityTradingPercentageMap.get(Asset.trim());
				iter2 = TempTradingPercentageMap.keySet().iterator();
				while (iter2.hasNext() && !Done) {
					String Security = (String) iter2.next();
					TempPercentage = TempTradingPercentageMap.get(Security.trim());
					if (TempPercentage * UnTradePercentage > 0 || TempPercentage == - UnTradePercentage) // Modified on 2010.08.10, combine the fraction trading into the other trading with the same sign
					{
						TempPercentage += UnTradePercentage;
						TempTradingPercentageMap.put(Security.trim(), TempPercentage);
						AssetSecurityTradingPercentageMap.put(Asset.trim(), TempTradingPercentageMap);
						Done = true;
					}
				}
			}
		}
		
		Iterator<String> iter = AssetSecurityTradingPercentageMap.keySet().iterator();
		java.text.DecimalFormat Dformat = new DecimalFormat("###,###,###");
		java.text.DecimalFormat format1 = new DecimalFormat("#########");
		java.text.DecimalFormat format2 = new DecimalFormat("##.##");
		String holdingmessage = "";
		String sellMessage="";
		String buyMessage="";
		String amount = "";
		boolean sell=false;
		boolean needHolding=false;
		double trading = 0.0;
		while(iter.hasNext()){
			String asset = iter.next().trim();
			HashMap<String,Double> securityTradingPercnetage = AssetSecurityTradingPercentageMap.get(asset);
			Iterator<String> iter1 = securityTradingPercnetage.keySet().iterator();
			while(iter1.hasNext()){
				String fund = iter1.next().trim();
				trading = securityTradingPercnetage.get(fund);
				double dd = trading*totleAmounts;
				if(dd<0)dd=0-dd;
				amount = Dformat.format(dd);
				String s= format1.format(dd);
				String tranPercnetage = format2.format(Double.parseDouble(s)/totleAmounts*100)+"%";
				if(trading <0 && !amount.equals("0")){
					sellMessage+="SELL";
					sellMessage+="#";
					sellMessage+=fund;
					sellMessage+="#";
					sellMessage+=amount;
					sellMessage+="#";
					sellMessage+=tranPercnetage;
					sell=true;
					
				}else if(trading > 0 && !amount.equals("0")){
					buyMessage+="BUY";
					buyMessage+="#";
					buyMessage+=fund;
					buyMessage+="#";
					buyMessage+=amount;
					buyMessage+="#";
					buyMessage+=tranPercnetage;
					sell=false;
				}
				String holdingAmount = Dformat.format(securityActualAmountMap.get(fund)+trading*totleAmounts);
				String str = format1.format(securityActualAmountMap.get(fund)+trading*totleAmounts);
				if(!holdingAmount.equals("0")){
					holdingmessage+=asset;
					holdingmessage+="#";
					holdingmessage+=fund;
					holdingmessage+="#";
					
					String percent =format2.format(Double.parseDouble(str)/totleAmounts*100) ;
					percent+="%";
					holdingmessage += holdingAmount;
					holdingmessage+="#";
					holdingmessage+=percent;
					needHolding = true;
				}else needHolding = false;
				
				
				if(iter1.hasNext()){
					if(sell && !amount.equals("0")){
						sellMessage+="$";
					}else if(!sell && !amount.equals("0")){
						buyMessage+="$";
					}
					if(needHolding)
					holdingmessage+="$";
				}
			}
			if(iter.hasNext()){
				if(sell && !amount.equals("0")){
					sellMessage+="$";
				}else if(!sell && !amount.equals("0")){
					buyMessage+="$";
				}
				if(needHolding)
				holdingmessage+="$";
			}
			
		}
		if(sellMessage.equals("")&&buyMessage.equals("")){
			message+="NULL"+"@"+holdingmessage;
			return Action.MESSAGE;
		}
		
		if(sellMessage.endsWith("$")){
			message+=sellMessage+buyMessage+"@"+holdingmessage;
		}else if(!sellMessage.endsWith("$")){
			message+=sellMessage+"$"+buyMessage+"@"+holdingmessage;
		}
						
		return Action.MESSAGE;
	}
	
	
	private String fund;
	public String getFundAssetClass(){
		message="";
		int end = fund.indexOf("(");
		if(end!=-1){
			fund = fund.substring(0, end);
		}
		if(fund.equalsIgnoreCase("CASH")){
			message="CASH";
			return Action.MESSAGE;
		}
		AssetClassManager manager = ContextHolder.getAssetClassManager();
		SecurityManager securityManager = ContextHolder.getSecurityManager();
		Security security = securityManager.get(fund);
		if(security == null) return Action.MESSAGE;
		String assetClassName = manager.get(security.getClassID()).getName();
		if(assetClassName == null)return Action.MESSAGE;
		
		String lowAssetClassName = assetClassName.toLowerCase();
		
		if(lowAssetClassName.contains("high yield")){
			message="High Yield Bond";
			return Action.MESSAGE;
		}
		
		if(assetClassName.equals("EQUITY")){
			message="US EQUITY";
			return Action.MESSAGE;
		}
		
		while(true){
			if(manager.isUpperOrSameClass("CASH", assetClassName)){
				message="CASH";
				break;
			}else if(manager.isUpperOrSameClass("SECTOR EQUITY", assetClassName)){
				message="US EQUITY";
				break;
			}else if(manager.isUpperOrSameClass("US EQUITY", assetClassName)){
				message="US EQUITY";
				break;
			}else if(manager.isUpperOrSameClass("INTERNATIONAL EQUITY", assetClassName)){
				message="INTERNATIONAL EQUITY";
				break;
			}else if(manager.isUpperOrSameClass("FIXED INCOME", assetClassName)){
				message="FIXED INCOME";
				break;
			}else if(manager.isUpperOrSameClass("REAL ESTATE", assetClassName)){
				message="REAL ESTATE";
				break;
			}else if(manager.isUpperOrSameClass("COMMODITIES", assetClassName)){
				message="COMMODITIES";
				break;
			}else if(manager.isUpperOrSameClass("Emerging Market", assetClassName)){
				message="Emerging Market";
				break;
			}else if(manager.isUpperOrSameClass("INTERNATIONAL BONDS", assetClassName)){
				message="INTERNATIONAL BONDS";
				break;
			}else if(manager.isUpperOrSameClass("High Yield Bond", assetClassName)){
				message="High Yield Bond";
				break;
			}else if(manager.isUpperOrSameClass("BALANCE FUND", assetClassName)){
				message="BALANCE FUND";
				break;
			}else{
				break;
			}
		}
		return Action.MESSAGE;
	}

	public Portfolio getPortfolio() {
		return portfolio;
	}

	public void setPortfolio(Portfolio portfolio) {
		this.portfolio = portfolio;
	}

	public List<Date> getHoldingDates() {
		return holdingDates;
	}

	public void setHoldingDates(List<Date> holdingDates) {
		this.holdingDates = holdingDates;
	}

	public Boolean getRealtime() {
		return realtime;
	}

	public void setRealtime(Boolean realtime) {
		this.realtime = realtime;
	}

	public String getChartUrl() {
		return chartUrl;
	}

	public void setChartUrl(String chartUrl) {
		this.chartUrl = chartUrl;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Boolean getOperation() {
		return operation;
	}

	public void setOperation(Boolean operation) {
		this.operation = operation;
	}

	public PortfolioManager getPortfolioManager() {
		return portfolioManager;
	}

	public void setPortfolioManager(PortfolioManager portfolioManager) {
		this.portfolioManager = portfolioManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public void setStrategyManager(StrategyManager strategyManager) {
		this.strategyManager = strategyManager;
	}

	public void setSecurityManager(SecurityManager securityManager) {
		this.securityManager = securityManager;
	}

	public void setGroupManager(GroupManager groupManager) {
		this.groupManager = groupManager;
	}

	public Boolean getIsOwner() {
		return isOwner;
	}

	public void setIsOwner(Boolean isOwner) {
		this.isOwner = isOwner;
	}

	public Boolean getRead() {
		return read;
	}

	public void setRead(Boolean read) {
		this.read = read;
	}

	public String getRoleDelayed() {
		return roleDelayed;
	}

	public void setRoleDelayed(String roleDelayed) {
		this.roleDelayed = roleDelayed;
	}

	public String getRoleRealtime() {
		return roleRealtime;
	}

	public void setRoleRealtime(String roleRealtime) {
		this.roleRealtime = roleRealtime;
	}

	public String getRoleOperation() {
		return roleOperation;
	}

	public void setRoleOperation(String roleOperation) {
		this.roleOperation = roleOperation;
	}

	public String getValidName() {
		return validName;
	}

	public void setValidName(String validName) {
		this.validName = validName;
	}

	public Boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public HoldingInf getHolding() {
		return holding;
	}

	public void setHolding(HoldingInf holding) {
		this.holding = holding;
	}

	public Boolean getIsAdvancedUser() {
		return isAdvancedUser;
	}

	public void setIsAdvancedUser(Boolean isAdvancedUser) {
		this.isAdvancedUser = isAdvancedUser;
	}

	public Boolean getIsPlanPortfolio() {
		return isPlanPortfolio;
	}

	public void setIsPlanPortfolio(Boolean isPlanPortfolio) {
		this.isPlanPortfolio = isPlanPortfolio;
	}

	public String getAssetNames() {
		return assetNames;
	}

	public void setAssetNames(String assetNames) {
		this.assetNames = assetNames;
	}

	public String getSecurityNames() {
		return securityNames;
	}

	public void setSecurityNames(String securityNames) {
		this.securityNames = securityNames;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getMinSell() {
		return minSell;
	}

	public void setMinSell(String minSell) {
		this.minSell = minSell;
	}

	public String getMinBuy() {
		return minBuy;
	}

	public void setMinBuy(String minBuy) {
		this.minBuy = minBuy;
	}

	public String getAvaliableToSell() {
		return avaliableToSell;
	}

	public void setAvaliableToSell(String avaliableToSell) {
		this.avaliableToSell = avaliableToSell;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getFund() {
		return fund;
	}

	public void setFund(String fund) {
		this.fund = fund;
	}

	public List<List<String>> getHoldingInfs() {
		return holdingInfs;
	}

	public void setHoldingInfs(List<List<String>> holdingInfs) {
		this.holdingInfs = holdingInfs;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}



}
