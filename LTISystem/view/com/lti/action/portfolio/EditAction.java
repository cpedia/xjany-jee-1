package com.lti.action.portfolio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;

import com.lti.action.Action;
import com.lti.permission.MYPLANIQPermissionManager;
import com.lti.permission.PortfolioPermissionChecker;
import com.lti.service.AssetClassManager;
import com.lti.service.CustomizeRegionManager;
import com.lti.service.GroupManager;
import com.lti.service.PortfolioHoldingManager;
import com.lti.service.PortfolioManager;
import com.lti.service.SecurityManager;
import com.lti.service.StrategyClassManager;
import com.lti.service.StrategyManager;
import com.lti.service.UserManager;
import com.lti.service.bo.CustomizeRegion;
import com.lti.service.bo.HoldingItem;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.PortfolioInf;
import com.lti.service.bo.Security;
import com.lti.service.bo.Strategy;
import com.lti.service.bo.Transaction;
import com.lti.service.bo.User;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.type.Pair;
import com.lti.type.executor.StrategyBasicInf;
import com.lti.type.executor.StrategyInf;
import com.lti.type.finance.Asset;
import com.lti.type.finance.HoldingInf;
import com.lti.util.LTIDate;
import com.lti.util.PortfolioUtil;
import com.opensymphony.xwork2.ActionSupport;

public class EditAction extends ActionSupport implements Action {

	private static final long serialVersionUID = -5131318645311364117L;

	PortfolioManager portfolioManager;

	PortfolioHoldingManager portfolioHoldingManager;

	StrategyManager strategyManager;

	SecurityManager securityManager;

	StrategyClassManager strategyClassManager;

	AssetClassManager assetClassManager;

	UserManager userManager;

	GroupManager groupManager;

	CustomizeRegionManager customizeRegionManager;

	CustomizeRegion customizeRegion;

	private Long ID;
	private Portfolio portfolio;
	private String message;

	private boolean isProduction = false;
	private List<Pair> attributes = null;

	private boolean isOwner = false;
	private boolean isAdmin = false;
	private boolean isAnonymous = true;
	private boolean isRealtime = false;
	private boolean isPlanPortfolio = false;
	private boolean hasSimulateRole = false;	
	public boolean isRealtime() {
		return isRealtime;
	}

	public void setRealtime(boolean isRealtime) {
		this.isRealtime = isRealtime;
	}
	

	private long userid;
	private String username;

	public EditAction() {
		User user = ContextHolder.getUserManager().getLoginUser();
		userid = user.getID();
		username = user.getUserName();
		if (userid != Configuration.USER_ANONYMOUS)
			isAnonymous = false;
		if (userid == Configuration.SUPER_USER_ID)
			isAdmin = true;
	}

	public String edit() {
		try {
			ServletActionContext.getResponse().sendRedirect("/LTISystem/jsp/portfolio/ViewPortfolio.action?ID=" + ID);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Action.MESSAGE;
	}

	private boolean planPortfolio;

	public boolean isPlanPortfolio() {
		return planPortfolio;
	}

	public void setPlanPortfolio(boolean planPortfolio) {
		this.planPortfolio = planPortfolio;
	}

	private Long strategyID;

	public Long getStrategyID() {
		return strategyID;
	}

	public void setStrategyID(Long strategyID) {
		this.strategyID = strategyID;
	}

	public boolean isOwner() {
		return isOwner;
	}

	public void setOwner(boolean isOwner) {
		this.isOwner = isOwner;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public boolean isAnonymous() {
		return isAnonymous;
	}

	public void setAnonymous(boolean isAnonymous) {
		this.isAnonymous = isAnonymous;
	}

	private String operation;

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	private String delayGroup;
	private String realtimeGroup;

	private void fetchgroups() {

		String[] grs = groupManager.getGroupRoleNameArray(ID, Configuration.ROLE_PORTFOLIO_DELAYED_ID);
		delayGroup = "";
		if (grs != null && grs.length != 0) {
			boolean first = true;
			for (String gr : grs) {
				if (!first) {
					delayGroup += ",";
				}
				first = false;
				delayGroup += gr;
			}
		}

		String[] rgrs = groupManager.getGroupRoleNameArray(ID, Configuration.ROLE_PORTFOLIO_REALTIME_ID);
		realtimeGroup = "";
		if (rgrs != null && rgrs.length != 0) {
			boolean first = true;
			for (String gr : rgrs) {
				if (!first) {
					realtimeGroup += ",";
				}
				first = false;
				realtimeGroup += gr;
			}
		}
	}

	public String ajaxsavegroups() {
		if (isAnonymous) {
			message = "Please login before changing group information.";
			return Action.MESSAGE;
		}
		if (!isAdmin) {
			message = "Only adminstrator has the permission to change the group information of portfolios.";
			return Action.MESSAGE;
		}

		Portfolio dbportfolio = portfolioManager.get(ID);
		if (dbportfolio == null) {
			message = "No such portfolio.";
			return Action.MESSAGE;
		}

		if (delayGroup != null) {
			String[] dgps = delayGroup.split(",");
			groupManager.changeGroupRoles(ID, dgps, Configuration.ROLE_PORTFOLIO_DELAYED_ID);
		}

		if (realtimeGroup != null) {
			String[] rgps = realtimeGroup.split(",");
			groupManager.changeGroupRoles(ID, rgps, Configuration.ROLE_PORTFOLIO_REALTIME_ID);
		}
		fetchgroups();
		message = "Delay Groups: " + delayGroup;
		message += "\nRealtime Groups: " + realtimeGroup;
		return Action.MESSAGE;
	}

	public String delete() {
		return Action.SUCCESS;
	}

	private HoldingBean holding;
	private String holdingJSON;

	private String getSymbol(String symbol) {
		if (symbol.indexOf("(") != -1) {
			return symbol.substring(0, symbol.indexOf("("));
		}
		return symbol;
	}

	private Long getParsedID(String symbol) {
		if (symbol.indexOf(".") != -1) {
			return Long.parseLong(symbol.substring(0, symbol.indexOf(".")));
		}
		return 0l;
	}

	private StrategyBasicInf getStrategyBasicInf(String name, Map<String, String> parameters, String assetname) {
		Strategy strategy = null;
		if (name != null&&!name.trim().equals("")) {
			strategy = strategyManager.get(name);
		}
		StrategyBasicInf sbi = new StrategyBasicInf();
		if (strategy != null) {
			sbi.setID(strategy.getID());
			sbi.setName(strategy.getName());
			sbi.setParameter(parameters);
			sbi.setAssetName(assetname);
		}else{
			sbi.setID(0l);
			sbi.setName("STATIC");
			sbi.setParameter(null);
			sbi.setAssetName(assetname);
		}
		return sbi;
	}
	
	private String personalTransactions;
	

	public String getPersonalTransactions() {
		return personalTransactions;
	}

	public void setPersonalTransactions(String personalTransactions) {
		this.personalTransactions = personalTransactions;
	}
	
	private String output;
	
	

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public String saveholding() throws Exception {
		if (isAnonymous) {
			message = "Please login before saving a portfolio.";
			return Action.MESSAGE;
		}

		if (ID == 0l) {
			portfolio.setID(null);
			portfolio.setUserID(userid);
			portfolio.setUserName(username);
			portfolio.assignType(Configuration.PORTFOLIO_TYPE_PERSONAL, personal);
			if (strategyID != null)
				portfolio.setMainStrategyID(strategyID);
			portfolioManager.save(portfolio);
			ID = portfolio.getID();
		} else {
			Portfolio dbportfolio = portfolioManager.get(ID);
			if (dbportfolio == null) {
				message = "No such portfolio.";
				return Action.MESSAGE;
			}

			if (dbportfolio.getEndDate() == null) {
				dbportfolio.setEndDate(new Date());
			}
			MYPLANIQPermissionManager mpm = new MYPLANIQPermissionManager();
			PortfolioPermissionChecker pc = new PortfolioPermissionChecker(dbportfolio, ServletActionContext.getRequest());
			isOwner = pc.isOwner();
			isAdmin = pc.isAdmin();

			if (operation != null && operation.equals(Action.ACTION_SAVEAS)) {
				try {
					//检查权限和检查数量
					int operationCode = mpm.canPortfolioCustomize(userid, dbportfolio, null);
					if(operationCode == mpm.PERMISSION_ADV_LIMIT) {
						if(pc.isAnonymous())
							message = "You need to subscribe to follow the Advanced portfolios. Please login or register first.";
						else
							message = "You need to subscribe as Expert User or Professional User to follow the advanced portfolios. Please subscribe for two weeks trial.";
						return Action.MESSAGE;
					}else if(operationCode == mpm.COUNT_LIMIT){
						message = "You have already customized/followed " + mpm.getMaxPortfolioFollowNum(userid) + " portfolio(s)";
						return Action.MESSAGE;
					}

					Portfolio newportfolio = dbportfolio.clone();
					newportfolio.setName(portfolio.getName());
					newportfolio.setUserID(userid);
					newportfolio.setUserName(username);
					if(newportfolio.getMainStrategyID()!=null){
						Strategy mainStrategy=strategyManager.get(newportfolio.getMainStrategyID());
						if(mainStrategy!=null&&mainStrategy.getUserID()!=null&&mainStrategy.getUserID().equals(userid)){
							
						}else{
							newportfolio.setMainStrategyID(null);
						}
					}
					PortfolioInf pif = portfolioManager.getPortfolioInf(ID, Configuration.PORTFOLIO_HOLDING_ORIGINAL);
					portfolioManager.save(newportfolio);
					ID = newportfolio.getID();
					PortfolioInf npif = new PortfolioInf();
					HoldingInf nhi = null;
					if (pif != null && pif.getHolding() != null) {
						nhi = pif.getHolding().clone();
					} else {
						nhi = new HoldingInf(ID, 10000.0, newportfolio.getStartingDate());
					}
					nhi.setPortfolioID(ID);
					nhi.setPortfolioName(newportfolio.getName());
					nhi.setCurrentDate(newportfolio.getStartingDate());
					
					npif.setHolding(nhi);
					npif.setPortfolioID(ID);
					npif.setType(Configuration.PORTFOLIO_HOLDING_ORIGINAL);
					portfolioManager.savePortfolioInf(npif);
					
					mpm.afterPortfolioCustomize(userid, ID, null);

				} catch (Exception e) {
					message = "Error while saving as portfolio: " + e.getMessage();
					return Action.MESSAGE;
				}
			} else if (operation != null && operation.equals(Action.ACTION_DELETE)) {
				if (!isOwner && !isAdmin) {
					message = "You are not the owner of this portfolio, please go back to the home page.";
					return Action.MESSAGE;
				}
				try {
					portfolioManager.remove(ID);
				} catch (Exception e) {
					message = "Erorr when deleting the portfolio of ID[" + ID + "]: " + e.getMessage();
					e.printStackTrace();
					return Action.MESSAGE;
				}
			} else {

				try {
					if (!isOwner && !isAdmin) {
						message = "You are not the owner of this portfolio, please go back to the home page.";
						return Action.MESSAGE;
					}
					if(personal){
						StrategyInf strategies = new StrategyInf();
						StrategyBasicInf sbi=new StrategyBasicInf();
						Strategy strategy=strategyManager.get(Configuration.STRATEGY_PERSONAL_PORTFOLIO_SIMULATION);
						sbi.setID(strategy.getID());
						sbi.setName(strategy.getName());
						strategies.setAssetAllocationStrategy(sbi);

						dbportfolio.assignType(Configuration.PORTFOLIO_TYPE_PERSONAL, true);
						dbportfolio.setStrategies(strategies);

						dbportfolio.setStartingDate(portfolio.getStartingDate());

						if (isProduction) {
							dbportfolio.setProduction(true);
						}
						if (attributes == null) {
							portfolio.setAttributes(null);
						} else {
							Map<String, String> map = new HashMap<String, String>();
							for (Pair p : attributes) {
								if (p.getPre() != null && !p.getPre().equals("")) {
									map.put(p.getPre(), p.getPost());
								}
							}
							dbportfolio.setAttributes(map);
						}
						dbportfolio.setName(portfolio.getName());
						dbportfolio.setCategories(portfolio.getCategories());
						dbportfolio.setDescription(portfolio.getDescription());
						
						Object[] objs=PortfolioUtil.parseTransactions(personalTransactions);
						List<Transaction> trans=(List<Transaction>) objs[0];
						output=(String)objs[1];
						if(output.length()>0){
							message=output;
							ServletActionContext.getResponse().setStatus(500);
							return Action.MESSAGE;
						}
						if(trans!=null&&trans.size()>0){
							Date _startdate=dbportfolio.getStartingDate();
							for(Transaction tran:trans){
								Security s=securityManager.get(tran.getSymbol());
								if(_startdate.before(s.getStartDate())){
									_startdate=s.getStartDate();
								}
								if(!LTIDate.isNYSETradingDay(tran.getDate())){
									tran.setDate(LTIDate.getNewNYSETradingDay(tran.getDate(), 1));
								}
							}
							dbportfolio.setStartingDate(_startdate);
						}
						
						portfolioManager.update(dbportfolio);
						
						HoldingInf holdingInf=new HoldingInf();
						
						holdingInf.setCurrentDate(portfolio.getStartingDate());
				 		holdingInf.setCash(holding==null?10000:(holding.Cash!=0.0?holding.Cash:10000));
						holdingInf.setPortfolioID(ID);
						holdingInf.setPortfolioName(portfolio.getName());
						
						holdingInf.setPersonalTransactions(trans);
						
						PortfolioInf pinf = new PortfolioInf();
						pinf.setPortfolioID(ID);
						pinf.setType(Configuration.PORTFOLIO_HOLDING_ORIGINAL);
						pinf.setHolding(holdingInf);
						portfolioManager.savePortfolioInf(pinf);
						
					}else{
						StrategyInf strategies = new StrategyInf();
						

						strategies.setAssetAllocationStrategy(getStrategyBasicInf(holding.getAssetAllocationStrategy(), holding.getAssetAllocationStrategyParameter(), null));
						strategies.setRebalancingStrategy(getStrategyBasicInf(holding.getRebalancingStrategy(), holding.getRebalancingStrategyParameter(), null));
						strategies.setCashFlowStrategy(getStrategyBasicInf(holding.getCashFlowStrategy(), holding.getCashFlowStrategyParameter(), null));

						if (holding.getAssets() != null && holding.getAssets().size() != 0) {
							for (AssetBean ab : holding.getAssets()) {
								StrategyBasicInf sbi = getStrategyBasicInf(ab.getAssetStrategy(), ab.getAssetStrategyParameter(), ab.getName());
								strategies.getAssetStrategies().add(sbi);
							}
						}

						dbportfolio.setStrategies(strategies);

						dbportfolio.setStartingDate(portfolio.getStartingDate());

						if (isProduction) {
							dbportfolio.setProduction(true);
						}
						if (attributes == null) {
							portfolio.setAttributes(null);
						} else {
							Map<String, String> map = new HashMap<String, String>();
							for (Pair p : attributes) {
								if (p.getPre() != null && !p.getPre().equals("")) {
									map.put(p.getPre(), p.getPost());
								}
							}
							dbportfolio.setAttributes(map);
						}
						dbportfolio.setName(portfolio.getName());
						dbportfolio.setCategories(portfolio.getCategories());
						dbportfolio.setDescription(portfolio.getDescription());
						dbportfolio.assignType(Configuration.PORTFOLIO_TYPE_PERSONAL, false);
						portfolioManager.update(dbportfolio);

						HoldingInf holdingInf = new HoldingInf(ID, 10000.0, null);
						holdingInf.setCurrentDate(portfolio.getStartingDate());
						holdingInf.setCash(holding.getCash());
						holdingInf.setPortfolioID(ID);
						holdingInf.setPortfolioName(portfolio.getName());
						try {
							long benchmarkID = securityManager.get(getSymbol(holding.getBenchmarkSymbol())).getID();
							holdingInf.setBenchmarkID(benchmarkID);
							holdingInf.setBenchmarkSymbol(getSymbol(holding.getBenchmarkSymbol()));
						} catch (Exception e) {
							holdingInf.setBenchmarkID(14l);
							holdingInf.setBenchmarkSymbol("^GSPC");
						}

						if (holding.getAssets() != null && holding.getAssets().size() != 0) {
							for (AssetBean ab : holding.getAssets()) {
								Asset asset = new Asset();
								asset.setName(ab.getName());
								asset.setTargetPercentage(ab.getTargetPercentage());
								asset.setAssetClassID(getParsedID(ab.getAssetClassName()));
								asset.setAssetClassName(ab.getAssetClassName());
								if (ab.getHoldingItems() != null && ab.getHoldingItems().size() != 0) {
									for (HoldingItemBean hib : ab.getHoldingItems()) {
										HoldingItem hi = new HoldingItem();
										hi.setPortfolioID(portfolio.getID());
										hi.setAssetName(asset.getName());

										Security s = securityManager.get(getSymbol(hib.getSymbol()));
										if(s==null){
											throw new Exception("There is no security match the symbol: "+hib.getSymbol());
										}
										hi.setSecurityID(s.getID());
										hi.setSymbol(s.getSymbol());
										hi.setDate(portfolio.getStartingDate());
										
										hi.setReInvest(hib.getReInvest());
										double price = securityManager.getPrice(s.getID(), portfolio.getStartingDate());
										hi.setPrice(price);
										
										hi.setShare(hib.getAmount()/price);
										
										hi.setSymbol(s.getSymbol());
										asset.getHoldingItems().add(hi);
									}
								}
								holdingInf.addAsset(asset);
							}
						}
						
						holdingInf.refreshAmounts();

						PortfolioInf pinf = new PortfolioInf();
						pinf.setPortfolioID(ID);
						pinf.setType(Configuration.PORTFOLIO_HOLDING_ORIGINAL);
						pinf.setHolding(holdingInf);
						portfolioManager.savePortfolioInf(pinf);
					}
					
					
				} catch (Exception e) {
					e.printStackTrace();
					message=e.getMessage();
					ServletActionContext.getResponse().setStatus(500);
					return Action.MESSAGE;
				}
			}
		}
		return Action.SUCCESS;

	}

	private boolean personal=false;
	private String compound;

	
	public String getCompound() {
		return compound;
	}

	public void setCompound(String compound) {
		this.compound = compound;
	}

	public boolean isPersonal() {
		return personal;
	}

	public void setPersonal(boolean personal) {
		this.personal = personal;
	}

	public String editholding() {
		if (isAnonymous) {
			message = "Please login before editing a portfolio.";
			return Action.MESSAGE;
		}
		HoldingInf holdingInf = null;
		StrategyInf strategies = null;
		
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

		if (ID == 0) {
			return Action.SUCCESS;
		}
		String planIDStr = portfolio.getStrategies().getAssetAllocationStrategy().getParameter().get("PlanID");
		if(planIDStr != null)
			isPlanPortfolio = true;
		PortfolioPermissionChecker pc = new PortfolioPermissionChecker(portfolio, ServletActionContext.getRequest());
		isOwner = pc.isOwner();
		isAdmin = pc.isAdmin();

		isRealtime = pc.hasRealtimeRole();
		hasSimulateRole = pc.isHasSimulateRole();

		if (!pc.hasViewRole()) {
			message = "This portfolio is set to be a private one, please go back to the home page.";
			return Action.MESSAGE;
		}

		strategies = portfolio.getStrategies();
		if (strategies == null) {
			strategies = new StrategyInf();
		}
		PortfolioInf portfolioInf = portfolioManager.getPortfolioInf(ID, Configuration.PORTFOLIO_HOLDING_ORIGINAL);
		if (portfolioInf != null) {
			holdingInf = portfolioInf.getHolding();
		}
		if (holdingInf == null) {
			holdingInf = new HoldingInf(ID, 10000.0, portfolio.getStartingDate());
		}
		
		if(personalTransactions!=null&&output!=null&&output.length()>0){
			
		}
		else
			personalTransactions=PortfolioUtil.formatTransactions(holdingInf.getPersonalTransactions());
		
		holding = new HoldingBean();
		holding.init(holdingInf, strategies);

		holding.setStartingDate(portfolio.getStartingDate());

		holdingJSON = holding.toJSON();

		if (isAdmin) {
			fetchgroups();
		}

		if ((portfolio.getType() & Configuration.PORTFOLIO_TYPE_PRODUCTION) > 0) {
			isProduction = true;
		}
		
		if ((portfolio.getType() & Configuration.PORTFOLIO_TYPE_PERSONAL) > 0) {
			personal = true;
		}

		if (portfolio.getAttributes() != null) {
			java.util.Iterator<String> iter = portfolio.getAttributes().keySet().iterator();
			attributes = new ArrayList<Pair>();
			while (iter.hasNext()) {
				String key = iter.next();
				attributes.add(new Pair(key, portfolio.getAttributes().get(key)));
			}
		}
//		//生成
//		portfolio.setType(Configuration.PORTFOLIO_TYPE_COMPOUND);
//		//判读
//		if(portfolio.isType(Configuration.PORTFOLIO_TYPE_COMPOUND)){
//			//
//		}
		if (portfolio.getMainStrategyID() != null) {
			Strategy str = strategyManager.getBasicStrategy(portfolio.getMainStrategyID());
			if (str != null) {
				portfolio.setMainStrategyName(str.getName());
			}

		}

		return Action.SUCCESS;
	}
	
	private String type;
	public String makepublic(){
		PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();
		message = "";
		portfolio = portfolioManager.get(ID);
		if(portfolio==null){
			message = "fail to operate";
			return Action.MESSAGE;
		}
		if(type.equalsIgnoreCase("public")){
			portfolio.setFullyPublic(true);			
		}else{
			portfolio.setFullyPublic(false);
		}
		portfolioManager.update(portfolio);
		message = "success to operate";
		return Action.MESSAGE;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Portfolio getPortfolio() {
		return portfolio;
	}

	public void setPortfolio(Portfolio portfolio) {
		this.portfolio = portfolio;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Long getID() {
		return ID;
	}

	public void setID(Long iD) {
		ID = iD;
	}

	public void setPortfolioManager(PortfolioManager portfolioManager) {
		this.portfolioManager = portfolioManager;
	}

	public void setPortfolioHoldingManager(PortfolioHoldingManager portfolioHoldingManager) {
		this.portfolioHoldingManager = portfolioHoldingManager;
	}

	public void setStrategyManager(StrategyManager strategyManager) {
		this.strategyManager = strategyManager;
	}

	public void setSecurityManager(SecurityManager securityManager) {
		this.securityManager = securityManager;
	}

	public void setStrategyClassManager(StrategyClassManager strategyClassManager) {
		this.strategyClassManager = strategyClassManager;
	}

	public void setAssetClassManager(AssetClassManager assetClassManager) {
		this.assetClassManager = assetClassManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public void setGroupManager(GroupManager groupManager) {
		this.groupManager = groupManager;
	}

	public void setCustomizeRegionManager(CustomizeRegionManager customizeRegionManager) {
		this.customizeRegionManager = customizeRegionManager;
	}

	public void setCustomizeRegion(CustomizeRegion customizeRegion) {
		this.customizeRegion = customizeRegion;
	}

	public HoldingBean getHolding() {
		return holding;
	}

	public void setHolding(HoldingBean holding) {
		this.holding = holding;
	}

	public String getHoldingJSON() {
		return holdingJSON;
	}

	public void setHoldingJSON(String holdingJSON) {
		this.holdingJSON = holdingJSON;
	}

	public boolean getIsProduction() {
		return isProduction;
	}

	public void setIsProduction(boolean isProduction) {
		this.isProduction = isProduction;
	}

	public List<Pair> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<Pair> attributes) {
		this.attributes = attributes;
	}

	public String getDelayGroup() {
		return delayGroup;
	}

	public void setDelayGroup(String delayGroup) {
		this.delayGroup = delayGroup;
	}

	public String getRealtimeGroup() {
		return realtimeGroup;
	}

	public void setRealtimeGroup(String realtimeGroup) {
		this.realtimeGroup = realtimeGroup;
	}

	public Boolean getIsPlanPortfolio() {
		return isPlanPortfolio;
	}

	public void setIsPlanPortfolio(Boolean isPlanPortfolio) {
		this.isPlanPortfolio = isPlanPortfolio;
	}

	public boolean isHasSimulateRole() {
		return hasSimulateRole;
	}

	public void setHasSimulateRole(boolean hasSimulateRole) {
		this.hasSimulateRole = hasSimulateRole;
	}


}
