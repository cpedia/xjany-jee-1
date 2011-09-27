package com.lti.action.portfolio.log;

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
import com.lti.service.CustomizePageManager;
import com.lti.service.CustomizeRegionManager;
import com.lti.service.GroupManager;
import com.lti.service.PortfolioManager;
import com.lti.service.StrategyManager;
import com.lti.service.UserManager;
import com.lti.service.bo.CustomizeRegion;
import com.lti.service.bo.Group;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.User;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.type.LongString;
import com.lti.type.PaginationSupport;
import com.lti.util.CustomizeUtil;
import com.lti.util.LTIDate;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class MainAction extends ActionSupport implements Action {

	static Log log = LogFactory.getLog(MainAction.class);

	// private PaginationSupport logs;

	private List<com.lti.service.bo.Log> logs;
	private Integer startIndex;

	private Integer pageSize;

	private Long portfolioID;

	private Long strategyID;

	private List<LongString> strategyIDs;

	private PortfolioManager portfolioManager;

	private StrategyManager strategyManager;

	private UserManager userManager;

	private GroupManager groupManager;

	private CustomizeRegionManager customizeRegionManager;

	private CustomizeRegion customizeRegion;

	private Integer type;

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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public void setPortfolioManager(PortfolioManager portfolioManager) {
		this.portfolioManager = portfolioManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public void setGroupManager(GroupManager groupManager) {
		this.groupManager = groupManager;
	}

	public void validate() {

		User user = userManager.getLoginUser();
		if (user != null)
			userID = user.getID();
		else
			userID = Configuration.USER_ANONYMOUS;
		CustomizeUtil.setRegion(customizeRegion, userID);

		if (type == null)
			type = -1;

		if (strategyID != null)
			type = 1;

	}

	private String message;

	@Override
	public String execute() throws Exception {
		if (portfolioID == null) {
			message = "The ID is null.";
			addActionError(message);
			return Action.ERROR;
		}

		Portfolio portfolio = portfolioManager.get(portfolioID);

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
		if (type == 0) {
			logs = portfolioManager.getLogsInPeriod(portfolioID, legalDate);
		} else if (type == 1) {
			if (strategyID == null)
				logs = portfolioManager.getLogsInPeriod(portfolioID, legalDate);
			else
				logs = portfolioManager.getLogsInPeriod(portfolioID, strategyID, legalDate);
		} else {
			logs = portfolioManager.getLogsInPeriod(portfolioID, legalDate);
		}
		setLogDates(logs);
		return Action.SUCCESS;

	}

	private void setLogDates(List<com.lti.service.bo.Log> logs) {
		if (logs == null)
			return;
		for (int i = 0; i < logs.size(); i++) {
			com.lti.service.bo.Log log = logs.get(i);
			Date logDate = log.getLogDate();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			log.setLogDateStr(sdf.format(logDate));
		}
	}

	// public PaginationSupport getLogs() {
	// return logs;
	// }
	//
	//
	// public void setLogs(PaginationSupport logs) {
	// this.logs = logs;
	// }

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

	public Long getStrategyID() {
		return strategyID;
	}

	public void setStrategyID(Long strategyID) {
		this.strategyID = strategyID;
	}

	public List<LongString> getStrategyIDs() {
		return strategyIDs;
	}

	public void setStrategyIDs(List<LongString> strategyIDs) {
		this.strategyIDs = strategyIDs;
	}

	public PortfolioManager getPortfolioManager() {
		return portfolioManager;
	}

	public void setStrategyManager(StrategyManager strategyManager) {
		this.strategyManager = strategyManager;
	}

	public void setLogs(List logs) {
		this.logs = logs;
	}

	public List getLogs() {
		return logs;
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
