package com.lti.action.strategy.modelportfolio;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.action.Action;
import com.lti.bean.PortfolioItem;
import com.lti.service.GroupManager;
import com.lti.service.PortfolioManager;
import com.lti.service.StrategyManager;
import com.lti.service.UserManager;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.PortfolioMPT;
import com.lti.service.bo.Role;
import com.lti.service.bo.Strategy;
import com.lti.service.bo.User;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.util.FormatUtil;
import com.lti.util.LTIDate;
import com.opensymphony.xwork2.ActionSupport;

public class MainAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(MainAction.class);

	private List<Portfolio> portfolios;

	private Long strategyID;

	private StrategyManager strategyManager;

	private PortfolioManager portfolioManager;

	private UserManager userManager;

	private String ActionMessage;

	private List<PortfolioItem> portfolioList;

	private Boolean isHolding;

	private Boolean P_operation;

	private String holdingDate;

	private Strategy strategy;

	private Integer size = 0;

	private Boolean includeHeader;

	private Boolean includeJS;

	public Boolean getIncludeHeader() {
		return includeHeader;
	}

	public void setIncludeHeader(Boolean includeHeader) {
		this.includeHeader = includeHeader;
	}

	public Boolean getIncludeJS() {
		return includeJS;
	}

	public void setIncludeJS(Boolean includeJS) {
		this.includeJS = includeJS;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	private int getYear(int year, boolean realtime) {
		if (!realtime) {
			switch (year) {
			case PortfolioMPT.LAST_ONE_YEAR:
				return year = PortfolioMPT.DELAY_LAST_ONE_YEAR;

			case PortfolioMPT.LAST_THREE_YEAR:
				return year = PortfolioMPT.DELAY_LAST_THREE_YEAR;
			case PortfolioMPT.LAST_FIVE_YEAR:
				return year = PortfolioMPT.DELAY_LAST_FIVE_YEAR;
			default:
				return year;
			}
		}
		return year;
	}
	
	public void validate() {
		if (this.strategyID == null) {
			addFieldError("strategyID", "Strategy ID is not validate!");
		} else {
			strategy = strategyManager.get(strategyID);

			if (strategy == null)
				addFieldError("strategyID", "Strategy ID is not validate!");
		}
	}

	public static Double round(Double v, int scale) {
		if (v == null) {
			return Double.NaN;
		}
		BigDecimal b = new BigDecimal(Double.toString(v * 100));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	@Override
	public String execute() throws Exception {

		boolean realtime = false;
		User user = userManager.getLoginUser();
		Long userID;
		if (user == null) {
			userID = Configuration.USER_ANONYMOUS;
		} else
			userID = user.getID();

		// create modelPortfolio operation role
		P_operation = userManager.HasRole(Configuration.ROLE_PORTFOLIO_OPERATION, userID);

		boolean isAdmin = false;
		if (userID.equals(Configuration.SUPER_USER_ID)) {
			isAdmin = true;
		}

		boolean isAnonymous = true;
		if (!userID.equals(Configuration.USER_ANONYMOUS)) {
			isAnonymous = false;
		}

		boolean isOwner = false;
		Strategy strategy = strategyManager.get(strategyID);
		if (strategy == null)
			return Action.ERROR;
		if (userID.equals(strategy.getUserID())) {
			isOwner = true;
		}
		
		
		if(!isOwner&&!isAdmin&&!userManager.HaveRole(Configuration.ROLE_STRATEGY_READ, userID, strategy.getID(),Configuration.RESOURCE_TYPE_STRATEGY)){
			return Action.ERROR;
		}
		
		portfolioList = new ArrayList<PortfolioItem>();
		portfolios = strategyManager.getModelPortfolios(strategyID);
		SimpleDateFormat sdf=new SimpleDateFormat("MM/dd/yyyy");
		if (portfolios != null) {
			for (int i = 0; i < portfolios.size(); i++) {
				Portfolio p = portfolios.get(i);
				if (isAdmin || userID.equals(p.getUserID())) {
					realtime = true;
				} else if (isAnonymous) {
					realtime = false;
				} else {
					realtime = userManager.HaveRole(Configuration.ROLE_PORTFOLIO_REALTIME, userID, p.getID(),Configuration.RESOURCE_TYPE_PORTFOLIO);
				}
				PortfolioItem pi = new PortfolioItem();
				pi.setName(p.getName());
				pi.setShowName();
				pi.setID(p.getID());

				Date lastValidDate = p.getEndDate();
				// permission filter2 === start

				pi.setDelayed(!realtime);
				if (!realtime) {
					boolean delay = userManager.HaveRole(Configuration.ROLE_PORTFOLIO_DELAYED, userID, p.getID(),Configuration.RESOURCE_TYPE_PORTFOLIO);
					if (!delay)
						continue;
				}
				// permission filter2 === end
				if (lastValidDate != null) {
					Date lastLegalDate = LTIDate.getHoldingDateMonthEnd(lastValidDate);
					if (!realtime && lastLegalDate.before(lastValidDate)) {
						pi.setLastValidDate(sdf.format(lastLegalDate));
					} else {
						pi.setLastValidDate(sdf.format(lastValidDate));
					}

					Date lastTransactionDate = portfolioManager.getTransactionLatestDate(p.getID());
					if (lastTransactionDate != null) {
						if (!realtime && lastLegalDate.before(lastTransactionDate)) {
							Date d = portfolioManager.getTransactionLatestDate(p.getID(), lastLegalDate);
							if (d != null)
								pi.setLastTransactionDate(sdf.format(d));
						} else {
							pi.setLastTransactionDate(sdf.format(lastTransactionDate));
						}
					}

				}// last Valid Date

				int[] years = new int[] { PortfolioMPT.LAST_ONE_YEAR, PortfolioMPT.LAST_THREE_YEAR, PortfolioMPT.LAST_FIVE_YEAR };
				for (int j = 0; j < years.length; j++) {
					PortfolioMPT m = portfolioManager.getPortfolioMPT(p.getID(), getYear(years[j], realtime));
					if (m != null) {
						if (years[j] == PortfolioMPT.LAST_ONE_YEAR) {
							pi.setAR1(FormatUtil.formatPercentage(m.getAR()));
							pi.setSharpeRatio1(FormatUtil.formatPercentage(m.getSharpeRatio()));
							pi.setBeta1(FormatUtil.formatPercentage(m.getBeta()));
						} else if (years[j] == PortfolioMPT.LAST_THREE_YEAR) {
							pi.setAR3(FormatUtil.formatPercentage(m.getAR()));
							pi.setSharpeRatio3(FormatUtil.formatPercentage(m.getSharpeRatio()));
							pi.setBeta3(FormatUtil.formatPercentage(m.getBeta()));
						} else {
							pi.setAR5(FormatUtil.formatPercentage(m.getAR()));
							pi.setSharpeRatio5(FormatUtil.formatPercentage(m.getSharpeRatio()));
							pi.setBeta5(FormatUtil.formatPercentage(m.getBeta()));
						}
					}
				}
				
				portfolioList.add(pi);
				if (size != 0 && portfolioList.size() >= size){
					break;
				}

			}
		}

		return Action.SUCCESS;

	}

	public List<Portfolio> getPortfolios() {
		return portfolios;
	}

	public void setPortfolios(List<Portfolio> portfolios) {
		this.portfolios = portfolios;
	}

	public Long getStrategyID() {
		return strategyID;
	}

	public void setStrategyID(Long strategyID) {
		this.strategyID = strategyID;
	}

	public void setStrategyManager(StrategyManager strategyManager) {
		this.strategyManager = strategyManager;
	}

	public void setPortfolioManager(PortfolioManager portfolioManager) {
		this.portfolioManager = portfolioManager;
	}

	public String getActionMessage() {
		return ActionMessage;
	}

	public void setActionMessage(String actionMessage) {
		ActionMessage = actionMessage;
	}

	public List<PortfolioItem> getPortfolioList() {
		return portfolioList;
	}

	public void setPortfolioList(List<PortfolioItem> portfolioList) {
		this.portfolioList = portfolioList;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
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

	public Boolean getP_operation() {
		return P_operation;
	}

	public void setP_operation(Boolean p_operation) {
		P_operation = p_operation;
	}

	public Strategy getStrategy() {
		return strategy;
	}

	public void setStrategy(Strategy strategy) {
		this.strategy = strategy;
	}
}
