package com.lti.action.portfolio.transaction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.lti.action.Action;
import com.lti.permission.PermissionChecker;
import com.lti.permission.PortfolioPermissionChecker;
import com.lti.service.CustomizeRegionManager;
import com.lti.service.GroupManager;
import com.lti.service.PortfolioManager;
import com.lti.service.StrategyManager;
import com.lti.service.SecurityManager;
import com.lti.service.UserManager;
import com.lti.service.bo.CustomizeRegion;
import com.lti.service.bo.Group;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.PortfolioDailyData;
import com.lti.service.bo.Transaction;
import com.lti.service.bo.User;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.type.LongString;
import com.lti.type.PaginationSupport;
import com.lti.util.CustomizeUtil;
import com.lti.util.FormatUtil;
import com.lti.util.LTIDate;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class MainAction extends ActionSupport implements Action {

	static Log log = LogFactory.getLog(MainAction.class);

	// private PaginationSupport transactions;

	private List transactions;
	private Integer startIndex;

	private Integer pageSize;

	private Long portfolioID;

	private PortfolioManager portfolioManager;

	private SecurityManager securityManager;

	private StrategyManager strategyManager;

	private UserManager userManager;

	private GroupManager groupManager;

	private CustomizeRegionManager customizeRegionManager;

	private CustomizeRegion customizeRegion;

	private String portfolioName;

	private Boolean isHolding;

	private String holdingDate;

	private Long userID;

	public String getPortfolioName() {
		return portfolioName;
	}

	public void setPortfolioName(String portfolioName) {
		this.portfolioName = portfolioName;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public void setGroupManager(GroupManager groupManager) {
		this.groupManager = groupManager;
	}

	public void setPortfolioManager(PortfolioManager portfolioManager) {
		this.portfolioManager = portfolioManager;
	}

	public void validate() {

		User user = userManager.getLoginUser();
		if (user != null)
			userID = user.getID();
		else
			userID = Configuration.USER_ANONYMOUS;
		CustomizeUtil.setRegion(customizeRegion, userID);

	}

	private String message;

	private void setTransactions() {
		if (transactions != null && transactions.size() != 0) {
			for (int i = 0; i < transactions.size(); i++) {
				Transaction t = (Transaction) transactions.get(i);
				if (t.getDate() != null) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
					t.setDateStr(sdf.format(t.getDate()));
				}
				try {
					//t.setStrategyName(strategyManager.get(t.getStrategyID()).getName());
				} catch (Exception e) {
				}
				try {
					//t.setSecurityName(securityManager.get(t.getSecurityID()).getSymbol());
				} catch (Exception e) {
				}
			}
		}
	}
	Portfolio portfolio;
	
	public Portfolio getPortfolio() {
		return portfolio;
	}

	public void setPortfolio(Portfolio portfolio) {
		this.portfolio = portfolio;
	}

	@Override
	public String execute() throws Exception {

		if (portfolioID == null) {
			message = "The ID is null.";
			addActionError(message);
			return Action.ERROR;
		}

		portfolio = portfolioManager.get(portfolioID);

		if (portfolio == null) {
			message = "The portfolio doesn't exist.";
			addActionError(message);
			return Action.ERROR;
		}

		portfolioName = portfolio.getName();

		PermissionChecker pc = new PortfolioPermissionChecker(portfolio, ServletActionContext.getRequest());
		boolean read = pc.hasViewRole();

		if (read == false) {
			message = "You have no right to read the data";
			addActionError(message);
			return Action.ERROR;
		}

		Date legalDate = pc.getLastLegalDate();

		transactions = portfolioManager.getTransactionsInPeriod(portfolioID, legalDate);
		setTransactions();

		return Action.SUCCESS;

	}

	public Integer getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(Integer startIndex) {
		this.startIndex = startIndex;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Long getPortfolioID() {
		return portfolioID;
	}

	public void setPortfolioID(Long portfolioID) {
		this.portfolioID = portfolioID;
	}

	public void setSecurityManager(SecurityManager securityManager) {
		this.securityManager = securityManager;
	}

	public void setStrategyManager(StrategyManager strategyManager) {
		this.strategyManager = strategyManager;
	}

	public void setTransactions(List transactions) {
		this.transactions = transactions;
	}

	public List getTransactions() {
		return transactions;
	}

	public CustomizeRegion getCustomizeRegion() {
		return customizeRegion;
	}

	public void setCustomizeRegion(CustomizeRegion customizeRegion) {
		this.customizeRegion = customizeRegion;
	}

	public void setCustomizeRegionManager(CustomizeRegionManager customizeRegionManager) {
		this.customizeRegionManager = customizeRegionManager;
	}

	public Boolean getIsHolding() {
		return isHolding;
	}

	public void setIsHolding(Boolean isHolding) {
		this.isHolding = isHolding;
	}

	public String getHoldingDate() {
		return holdingDate;
	}

	public void setHoldingDate(String holdingDate) {
		this.holdingDate = holdingDate;
	}

	public Long getUserID() {
		return userID;
	}

	public void setUserID(Long userID) {
		this.userID = userID;
	}

}
