package com.lti.action.portfolio.dailydata;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.lti.action.Action;
import com.lti.permission.PermissionChecker;
import com.lti.permission.PortfolioPermissionChecker;
import com.lti.service.CustomizeRegionManager;
import com.lti.service.GroupManager;
import com.lti.service.PortfolioManager;
import com.lti.service.UserManager;
import com.lti.service.bo.CustomizeRegion;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.PortfolioDailyData;
import com.lti.service.bo.User;
import com.lti.system.Configuration;
import com.lti.util.CustomizeUtil;
import com.lti.util.FormatUtil;
import com.opensymphony.xwork2.ActionSupport;

public class MainAction extends ActionSupport implements Action {

	static Log log = LogFactory.getLog(MainAction.class);

	// private PaginationSupport dailydatas;

	private Integer startIndex;

	private Integer pageSize;

	private List<com.lti.bean.PortfolioDailyData> dailydatas;
	private Long portfolioID;

	private PortfolioManager portfolioManager;

	private UserManager userManager;

	private GroupManager groupManager;

	private CustomizeRegionManager customizeRegionManager;

	private CustomizeRegion customizeRegion;

	private String portfolioName;

	private List<com.lti.service.bo.PortfolioDailyData> dailydatas_bo;

	private Boolean isHolding;

	private String holdingDate;

	private Long userID;

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

	private void setDailyDatas() {
		if (dailydatas_bo != null && dailydatas_bo.size() != 0) {
			// List<com.lti.bean.PortfolioDailyData> items=new
			// ArrayList<com.lti.bean.PortfolioDailyData>();
			dailydatas = new ArrayList<com.lti.bean.PortfolioDailyData>();
			for (int i = 0; i < dailydatas_bo.size(); i++) {
				PortfolioDailyData pdd = (PortfolioDailyData) dailydatas_bo.get(i);
				com.lti.bean.PortfolioDailyData bean = new com.lti.bean.PortfolioDailyData();
				bean.setAlpha(FormatUtil.formatPercentage(pdd.getAlpha()));
				bean.setAlpha1(FormatUtil.formatPercentage(pdd.getAlpha1()));
				bean.setAlpha3(FormatUtil.formatPercentage(pdd.getAlpha3()));
				bean.setAlpha5(FormatUtil.formatPercentage(pdd.getAlpha5()));

				bean.setBeta(FormatUtil.formatQuantity(pdd.getBeta()));
				bean.setBeta1(FormatUtil.formatQuantity(pdd.getBeta1()));
				bean.setBeta5(FormatUtil.formatQuantity(pdd.getBeta5()));
				bean.setBeta3(FormatUtil.formatQuantity(pdd.getBeta3()));

				bean.setAR(FormatUtil.formatPercentage(pdd.getAR()));
				bean.setAR1(FormatUtil.formatPercentage(pdd.getAR1()));
				bean.setAR3(FormatUtil.formatPercentage(pdd.getAR3()));
				bean.setAR5(FormatUtil.formatPercentage(pdd.getAR5()));

				bean.setRSquared(FormatUtil.formatQuantity(pdd.getRSquared()));
				bean.setRSquared1(FormatUtil.formatQuantity(pdd.getRSquared1()));
				bean.setRSquared3(FormatUtil.formatQuantity(pdd.getRSquared3()));
				bean.setRSquared5(FormatUtil.formatQuantity(pdd.getRSquared5()));

				bean.setSharpeRatio(FormatUtil.formatPercentage(pdd.getSharpeRatio()));
				bean.setSharpeRatio1(FormatUtil.formatPercentage(pdd.getSharpeRatio1()));
				bean.setSharpeRatio3(FormatUtil.formatPercentage(pdd.getSharpeRatio3()));
				bean.setSharpeRatio5(FormatUtil.formatPercentage(pdd.getSharpeRatio5()));

				bean.setStandardDeviation(FormatUtil.formatQuantity(pdd.getStandardDeviation()));
				bean.setStandardDeviation1(FormatUtil.formatQuantity(pdd.getStandardDeviation1()));
				bean.setStandardDeviation3(FormatUtil.formatQuantity(pdd.getStandardDeviation3()));
				bean.setStandardDeviation5(FormatUtil.formatQuantity(pdd.getStandardDeviation5()));

				bean.setTreynorRatio(FormatUtil.formatQuantity(pdd.getTreynorRatio()));
				bean.setTreynorRatio1(FormatUtil.formatQuantity(pdd.getTreynorRatio1()));
				bean.setTreynorRatio3(FormatUtil.formatQuantity(pdd.getTreynorRatio3()));
				bean.setTreynorRatio5(FormatUtil.formatQuantity(pdd.getTreynorRatio5()));

				bean.setDrawDown(FormatUtil.formatQuantity(pdd.getDrawDown()));
				bean.setDrawDown1(FormatUtil.formatQuantity(pdd.getDrawDown1()));
				bean.setDrawDown3(FormatUtil.formatQuantity(pdd.getDrawDown3()));
				bean.setDrawDown5(FormatUtil.formatQuantity(pdd.getDrawDown5()));

				bean.setDate(pdd.getDate());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
				bean.setDateStr(sdf.format(pdd.getDate()));
				bean.setAmount(FormatUtil.formatQuantity(pdd.getAmount()));
				dailydatas.add(bean);
			}
		}
	}

	private String message;
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String execute() throws Exception {

		if (portfolioID == null){
			message="The ID is null." ;
			addActionError(message);
			return Action.ERROR;
		}
			

		Portfolio portfolio = portfolioManager.get(portfolioID);

		if (portfolio == null){
			message="The portfolio doesn't exist.";
			addActionError(message);
			return Action.ERROR;
		}

		portfolioName = portfolio.getName();

		PermissionChecker pc=new PortfolioPermissionChecker(portfolio, ServletActionContext.getRequest());
		boolean read = pc.hasViewRole();

		if (read == false) {
			message="You have no right to read the data";
			addActionError(message);
			return Action.ERROR;
		}

		Date legalDate = pc.getLastLegalDate();
		dailydatas_bo = portfolioManager.getDailydataInPeirod(portfolioID, legalDate);
		setDailyDatas();

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

	public List getDailydatas() {
		return dailydatas;
	}

	public void setDailydatas(List dailydatas) {
		this.dailydatas = dailydatas;
	}

	public String getPortfolioName() {
		return portfolioName;
	}

	public void setPortfolioName(String portfolioName) {
		this.portfolioName = portfolioName;
	}

	public void setCustomizeRegionManager(CustomizeRegionManager customizeRegionManager) {
		this.customizeRegionManager = customizeRegionManager;
	}

	public CustomizeRegion getCustomizeRegion() {
		return customizeRegion;
	}

	public void setCustomizeRegion(CustomizeRegion customizeRegion) {
		this.customizeRegion = customizeRegion;
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

	// public PaginationSupport getDailydatas() {
	// return dailydatas;
	// }
	//
	//
	//
	//
	// public void setDailydatas(PaginationSupport dailydatas) {
	// this.dailydatas = dailydatas;
	// }

}
