package com.lti.action.portfolio;

import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.struts2.ServletActionContext;

import com.lti.Exception.PortfolioException;
import com.lti.Exception.Security.NoPriceException;
import com.lti.action.Action;
import com.lti.permission.PermissionChecker;
import com.lti.permission.PortfolioPermissionChecker;
import com.lti.service.PortfolioHoldingManager;
import com.lti.service.PortfolioManager;
import com.lti.service.bo.HoldingItem;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.Security;
import com.lti.service.bo.HoldingItem;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.type.finance.HoldingInf;
import com.lti.util.LTIDate;

/**
 * 
 * @author SuPing 2009-07-26
 */
public class ViewHoldingCSVAction {

	private Long ID;
	private String holdingDate;
	private String fileName;
	private boolean folio = true;
	private Portfolio portfolio;
	PortfolioHoldingManager portfolioHoldingManager;
	com.lti.service.SecurityManager securityManager;
	PortfolioManager portfolioManager;
	public InputStream inputStream;

	public boolean getFolio() {
		return folio;
	}

	public void setFolio(boolean folio) {
		this.folio = folio;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public String getHoldingDate() {
		return holdingDate;
	}

	public void setHoldingDate(String holdingDate) {
		this.holdingDate = holdingDate;
	}

	public Portfolio getPortfolio() {
		return portfolio;
	}

	public void setPortfolio(Portfolio portfolio) {
		this.portfolio = portfolio;
	}

	public PortfolioHoldingManager getPortfolioHoldingManager() {
		return portfolioHoldingManager;
	}

	public void setPortfolioHoldingManager(PortfolioHoldingManager portfolioHoldingManager) {
		this.portfolioHoldingManager = portfolioHoldingManager;
	}

	public PortfolioManager getPortfolioManager() {
		return portfolioManager;
	}

	public void setPortfolioManager(PortfolioManager portfolioManager) {
		this.portfolioManager = portfolioManager;
	}

	public Long getID() {
		return ID;
	}

	public void setID(Long id) {
		ID = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public com.lti.service.SecurityManager getSecurityManager() {
		return securityManager;
	}

	public void setSecurityManager(com.lti.service.SecurityManager securityManager) {
		this.securityManager = securityManager;
	}

	public static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yyyy");
	public static DecimalFormat def = new DecimalFormat("0.00");

	public Date getDate() {
		Date hDate = null;
		try {
			hDate = sdf1.parse(holdingDate);
		} catch (Exception e) {
			try {
				hDate = sdf2.parse(holdingDate);
			} catch (Exception e1) {
			}
		}
		PermissionChecker pc = new PortfolioPermissionChecker(portfolio, ServletActionContext.getRequest());
		if (hDate == null)
			return pc.getLastLegalDate();
		if (LTIDate.after(hDate, pc.getLastLegalDate()))
			return pc.getLastLegalDate();
		return hDate;
	}

	/**
	 * For view portfolio page to download the scheduled holdings by a CSV file
	 */
	public String outputActHolding() {
		securityManager = ContextHolder.getSecurityManager();
		portfolioHoldingManager = ContextHolder.getPortfolioHoldingManager();
		portfolioManager = ContextHolder.getPortfolioManager();
		portfolio = ContextHolder.getPortfolioManager().get(ID);
		Date hDate = getDate();

		List<HoldingItem> holdingItemList = null;

		if (LTIDate.equals(hDate, portfolio.getEndDate())) {
			HoldingInf hif = portfolioManager.getHolding(ID, Configuration.PORTFOLIO_HOLDING_CURRENT);
			if (hif != null)
				holdingItemList = hif.getHoldingItems();

		} else {
			holdingItemList = portfolioHoldingManager.getLatestHoldingItems(ID, hDate);
		}

		StringBuffer sb = new StringBuffer();
		boolean blank = false;
		if (!folio)
			sb.append(portfolio.getName() + "'s Holdings\r\n");
		if (holdingItemList != null && holdingItemList.size() > 0) {
			if (!folio) {
				//sb.append("Holding Date:" + holdingDate + "\r\n\r\n");
				//sb.append("Ticker,Weight\r\n");
			}
			for (HoldingItem si : holdingItemList) {
				if(si.getSymbol().trim().toUpperCase().equals("CASH"))
					sb.append("FDIC.CASH," + def.format(si.getPercentage() * 100) + "%\r\n");
				else
					sb.append(si.getSymbol() + "," + def.format(si.getPercentage() * 100) + "%\r\n");
			}
			if (!folio)
				sb.append("" + "," + "100%");
		} else
			blank = true;
		if (blank)
			sb.append("There is no holdings for " + portfolio.getName() + ".\r\n");
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		fileName = portfolio.getName() + "_" + df.format(hDate) + ".csv";
		inputStream = new StringBufferInputStream(sb.toString());
		return Action.DOWNLOAD;
	}

	/**
	 * For view portfolio page to download the scheduled holdings by a CSV file
	 */
	public String outputSchHolding() {
		securityManager = ContextHolder.getSecurityManager();
		portfolioHoldingManager = ContextHolder.getPortfolioHoldingManager();
		portfolioManager = ContextHolder.getPortfolioManager();
		portfolio = ContextHolder.getPortfolioManager().get(ID);
		Date date = com.lti.util.LTIDate.clearHMSM(portfolio.getEndDate());
		Date futureDate = LTIDate.getNewNYSETradingDay(date, 1);
		StringBuffer sb = new StringBuffer();
		if (!folio)
			sb.append(portfolio.getName() + "'s ScheduleHolding\r\n");
		boolean blank = false;

		List<HoldingItem> holdingItemList = null;
		HoldingInf hif = portfolioManager.getHolding(ID, Configuration.PORTFOLIO_HOLDING_EXPECTED);
		if (hif != null)
			holdingItemList = hif.getHoldingItems();

		if (holdingItemList != null && holdingItemList.size() > 0) {
			if (folio) {
				//sb.append("Holding Date:" + futureDate + "\r\n\r\n");
				//sb.append("Ticker,Weight\r\n");
			}
			for (HoldingItem hi:holdingItemList) {
				if(hi.getSymbol().trim().toUpperCase().equals("CASH"))
					sb.append("FDIC.CASH," + def.format(hi.getPercentage() * 100) + "%\r\n");
				else
					sb.append(hi.getSymbol() + "," + def.format(hi.getPercentage() * 100) + "%\r\n");
			}
			if (!folio)
				sb.append("  " + "," + "100%");
		} else
			blank = true;
		if (blank)
			sb.append("There is no scheduled holdings for " + portfolio.getName() + ".\r\n");
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		fileName = portfolio.getName() + "_" + "ScheduledHolding" + ".csv";
		inputStream = new StringBufferInputStream(sb.toString());
		return Action.DOWNLOAD;
	}

}
