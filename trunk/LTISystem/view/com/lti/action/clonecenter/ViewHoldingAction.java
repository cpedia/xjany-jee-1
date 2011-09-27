package com.lti.action.clonecenter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.struts2.ServletActionContext;

import com.lti.Exception.Security.NoPriceException;
import com.lti.action.Action;
import com.lti.permission.PermissionChecker;
import com.lti.permission.PortfolioPermissionChecker;
import com.lti.service.PortfolioHoldingManager;
import com.lti.service.PortfolioManager;
import com.lti.service.SecurityManager;
import com.lti.service.StrategyManager;
import com.lti.service.bo.HoldingItem;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.PortfolioInf;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.util.LTIDate;
import com.lti.util.SecurityUtil;

public class ViewHoldingAction {

	private Long ID;

	private String holdingDate;

	private Portfolio portfolio;

	private boolean aggregateFlag;

	private List<HoldingItem> holdingItems;

	private boolean stateType;

	// PortfolioHoldingManager portfolioHoldingManager;

	// PortfolioManager portfolioManager;

	// SecurityManager securityManager;
	public Long getID() {
		return ID;
	}

	public void setID(Long id) {
		ID = id;
	}

	public String getHoldingDate() {
		return holdingDate;
	}

	public void setHoldingDate(String holdingDate) {
		this.holdingDate = holdingDate;
	}

	public static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yyyy");

	private Double total;

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Date getDate(Date date) {
		PermissionChecker pc = new PortfolioPermissionChecker(portfolio,
				ServletActionContext.getRequest());
		if(portfolio.isFullyPublic()){
			return portfolio.getEndDate();
		}
		if (date == null)
			return pc.getLastLegalDate();
		if (LTIDate.after(date, pc.getLastLegalDate()))
			return pc.getLastLegalDate();
		return date;
	}

	private boolean schedule = false;

	private boolean usePrice = true;

	public boolean isSchedule() {
		return schedule;
	}

	public void setSchedule(boolean schedule) {
		this.schedule = schedule;
	}

	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String execute() {
		PortfolioHoldingManager portfolioHoldingManager = ContextHolder
				.getPortfolioHoldingManager();
		PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();
		portfolio = portfolioManager.get(ID);

		if (schedule) {
			PortfolioInf pinf = portfolioManager.getPortfolioInf(ID,
					Configuration.PORTFOLIO_HOLDING_EXPECTED);
			if (pinf == null || pinf.getHolding() == null) {
				message = "No such schedule holding.";
				ServletActionContext.getResponse().setStatus(500);
				return Action.MESSAGE;
			}
			holdingItems = pinf.getHolding().getHoldingItems();
		} else {
			Date date = null;
			try {
				date = sdf1.parse(holdingDate);
			} catch (Exception e) {
				try {
					date = sdf2.parse(holdingDate);
				} catch (Exception e1) {
				}
			}
			date = getDate(date);

			if (date.equals(portfolio.getEndDate())) {
				PortfolioInf pinf = portfolioManager.getPortfolioInf(ID,
						Configuration.PORTFOLIO_HOLDING_CURRENT);
				holdingItems = pinf.getHolding().getHoldingItems();
			} else {
				holdingItems = portfolioHoldingManager
						.getHoldingItems(ID, date);
			}
		}

		if (holdingItems != null && holdingItems.size() > 0
				&& holdingItems.get(0).getPrice() == null) {
			SecurityManager sm = ContextHolder.getSecurityManager();
			double total = 0.0;
			for (HoldingItem hi : holdingItems) {
				try {
					double price = sm
							.getPrice(hi.getSecurityID(), hi.getDate());
					hi.setPrice(price);
					total += price * hi.getShare();
				} catch (NoPriceException e) {
					break;
				}
			}
			for (HoldingItem hi : holdingItems) {
				hi.setPercentage(hi.getPrice() * hi.getShare() / total);
				try {
					ContextHolder.getPortfolioHoldingManager()
							.updateHoldingItem(hi);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			// System.out.println("processed.");
		}
		try {
			stateType = SecurityUtil.usedescription(portfolio, holdingItems);
			// stateType = false;
		} catch (Exception e) {
			stateType = false;
		}
		aggregateFlag = false;
		return "success";
	}

	// 查看子portfolio详细情况
	public String detailHoding() {
		PortfolioHoldingManager portfolioHoldingManager = ContextHolder
				.getPortfolioHoldingManager();
		PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();
		StrategyManager strategyManager = ContextHolder.getStrategyManager();
		SecurityManager securityManager = ContextHolder.getSecurityManager();
		portfolio = portfolioManager.get(ID);
		holdingItems = new ArrayList<HoldingItem>();

		List<HoldingItem> holdingItemsP = new ArrayList<HoldingItem>();
		if (schedule) {
			PortfolioInf pinf = portfolioManager.getPortfolioInf(ID,
					Configuration.PORTFOLIO_HOLDING_EXPECTED);
			if (pinf == null || pinf.getHolding() == null) {
				message = "No such schedule holding.";
				ServletActionContext.getResponse().setStatus(500);
				return Action.MESSAGE;
			}
			holdingItems = pinf.getHolding().getHoldingItems();
		} else {
			Date date = null;
			try {
				date = sdf1.parse(holdingDate);
			} catch (Exception e) {
				try {
					date = sdf2.parse(holdingDate);
				} catch (Exception e1) {
				}
			}
			date = getDate(date);

			PortfolioInf pinf = portfolioManager.getPortfolioInf(ID,
					Configuration.PORTFOLIO_HOLDING_CURRENT);
			holdingItemsP = pinf.getHolding().getHoldingItems();
		}

		if (holdingItemsP != null && holdingItemsP.size() > 0) {
			SecurityManager sm = ContextHolder.getSecurityManager();
			Date date = null;
			try {
				date = sdf1.parse(holdingDate);
			} catch (Exception e) {
				try {
					date = sdf2.parse(holdingDate);
				} catch (Exception e1) {
				}
			}
			date = getDate(date);
			double total = 0.0;
			List<Double> amountList = new ArrayList<Double>();
			for (HoldingItem hi : holdingItemsP) {
				try {
					if (hi.getSymbol().contains("P_")) {
						// 获得子portfolio的security，放入holdingitems
						List<HoldingItem> lHoldingItem = new ArrayList<HoldingItem>();
						// 获得最近日期的holdingItem
						if (date.equals(portfolio.getEndDate())) {
							PortfolioInf pinf = portfolioManager
									.getPortfolioInf(
											Long.parseLong(hi.getSymbol()
													.substring(2)),
											Configuration.PORTFOLIO_HOLDING_CURRENT);
							lHoldingItem = pinf.getHolding().getHoldingItems();
						} else {
							lHoldingItem = portfolioHoldingManager
									.getHoldingItems(Long.parseLong(hi
											.getSymbol().substring(2)), date);
						}
						if (lHoldingItem != null && lHoldingItem.size() != 0) {
							for (HoldingItem lhi : lHoldingItem) {
								double price = sm.getPrice(lhi.getSecurityID(),
										lhi.getDate());
								lhi.setPrice(price);
								double amount = price * lhi.getShare()
										* hi.getShare();
								amountList.add(amount);
								total = total + amount;
								holdingItems.add(lhi);

							}
						}
					} else {
						double price = sm.getPrice(hi.getSecurityID(), hi
								.getDate());
						hi.setPrice(price);
						total += price * hi.getShare();
						holdingItems.add(hi);
					}
				} catch (NoPriceException e) {
					break;
				}
			}
			for (int i = 0; i < holdingItems.size(); i++) {
				HoldingItem hi = new HoldingItem();
				hi = holdingItems.get(i);
				hi.setPercentage(amountList.get(i) / total);
				try {
					ContextHolder.getPortfolioHoldingManager()
							.updateHoldingItem(hi);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			// 根据assetclass对其进行分类
			List<HoldingItem> riskyHoldingItem = new ArrayList<HoldingItem>();
			List<HoldingItem> stableHoldingItem = new ArrayList<HoldingItem>();
			for (HoldingItem hi : holdingItems) {
				if (strategyManager.getRiskyOrStableAsset(
						securityManager.get(hi.getSymbol()).getAssetClass()
								.getName()).equals("Risky Asset")) {
					riskyHoldingItem.add(hi);
				} else if (strategyManager.getRiskyOrStableAsset(
						securityManager.get(hi.getSymbol()).getAssetClass()
								.getName()).equals("Stable Asset")) {
					stableHoldingItem.add(hi);
				}
			}
			// 对riskyHoldingItem进行排序
			List<HoldingItem> sortRisky = new ArrayList<HoldingItem>();
			List<HoldingItem> sortStable = new ArrayList<HoldingItem>();
			if (riskyHoldingItem != null && riskyHoldingItem.size() > 0) {
				sortRisky = this.sortHoldingItems(riskyHoldingItem);
			}
			// 对stableHoldingItem进行排序
			if (stableHoldingItem != null && stableHoldingItem.size() > 0) {
				sortStable = this.sortHoldingItems(stableHoldingItem);
			}
			// 将值置入copyHoldingItem中
			List<HoldingItem> copyHoldingItem = new ArrayList<HoldingItem>();
			double riskyPercentage = 0.0;
			double stablePercentage = 0.0;
			if (sortRisky.size() > 0) {
				for (HoldingItem hi : sortRisky) {
					riskyPercentage += hi.getPercentage();
					copyHoldingItem.add(hi);
				}
			}
			if (sortStable.size() > 0) {
				for (HoldingItem hi : sortStable) {
					stablePercentage += hi.getPercentage();
					copyHoldingItem.add(hi);
				}
			}
			// 获得每个security的portfolio在security表中的Symbol
			for (HoldingItem hi : copyHoldingItem) {
				String strSymbol = new String();
				strSymbol = "P_" + hi.getPortfolioID();
				hi.setPortflolioSymbol(strSymbol);
				hi.setPortfolioName(portfolioManager.get(hi.getPortfolioID())
						.getName());
			}
			stateType = SecurityUtil.usedescription(portfolio, holdingItems);
			// 重写holdingItem
			holdingItems = new ArrayList<HoldingItem>();
			double totalPercentage = 0.0;
			for (int i = 0; i < copyHoldingItem.size() - 1; i++) {
				if (sortRisky.size() > 0) {
					if (i == 0) {
						HoldingItem hi = new HoldingItem();
						riskyPercentage = Math.round(riskyPercentage*10000);
						riskyPercentage = riskyPercentage/100;
						hi.setAssetName("RISKY ASSET (" + riskyPercentage + "%) :");
						hi.setDescription("");
						hi.setPortflolioSymbol("");
						hi.setPortfolioName("");
						hi.setSymbol("");
						hi.setPercentage(0.0);
						holdingItems.add(hi);
					}
				}
				if (sortStable.size() > 0) {
					if (i == sortRisky.size()) {
						HoldingItem hi = new HoldingItem();
						stablePercentage = Math.round(stablePercentage*10000);
						stablePercentage = stablePercentage/100;
						hi.setAssetName("STABLE ASSET (" + stablePercentage + "%) :");
						hi.setDescription("");
						hi.setPortflolioSymbol("");
						hi.setPortfolioName("");
						hi.setSymbol("");
						hi.setPercentage(0.0);
						holdingItems.add(hi);
					}
				}
				if (copyHoldingItem.get(i).getAssetName().equals(
						copyHoldingItem.get(i + 1).getAssetName())) {
					totalPercentage = totalPercentage
							+ copyHoldingItem.get(i).getPercentage();
					holdingItems.add(copyHoldingItem.get(i));
				}
				if (!copyHoldingItem.get(i).getAssetName().equals(
						copyHoldingItem.get(i + 1).getAssetName())) {
					totalPercentage = totalPercentage
							+ copyHoldingItem.get(i).getPercentage();
					HoldingItem hi = new HoldingItem();
					hi.setAssetName("");
					hi.setDescription("Total Percentage of "
							+ copyHoldingItem.get(i).getAssetName());
					hi.setPortflolioSymbol("");
					hi.setPortfolioName("");
					hi.setSymbol("");
					hi.setPercentage(totalPercentage);
					holdingItems.add(copyHoldingItem.get(i));
					holdingItems.add(hi);
					totalPercentage = 0.0;
				}
			}
			holdingItems.add(copyHoldingItem.get(copyHoldingItem.size() - 1));
			totalPercentage = totalPercentage
					+ copyHoldingItem.get(copyHoldingItem.size() - 1)
							.getPercentage();
			HoldingItem hi = new HoldingItem();
			hi.setAssetName("");
			hi.setPercentage(totalPercentage);
			hi.setDescription("Total Percentage of "
					+ copyHoldingItem.get(copyHoldingItem.size() - 1)
							.getAssetName());
			hi.setPortflolioSymbol("");
			hi.setPortfolioName("");
			hi.setSymbol("");
			holdingItems.add(hi);
		}

		aggregateFlag = true;
		// stateType = false;

		return Action.SUCCESS;
	}

	// 根据assetclass已分类好的security进行排序
	List<HoldingItem> sortHoldingItems(List<HoldingItem> his) {
		HoldingItem ecHoldingItems = new HoldingItem();
		for (int i = 0; i < his.size() - 1; i++) {
			for (int j = 0; j < his.size() - 1; j++) {
				int compareAsset = his.get(j).getAssetName().compareTo(
						his.get(j + 1).getAssetName());
				int compareSymbol = his.get(j).getSymbol().compareTo(
						his.get(j + 1).getSymbol());
				if (compareAsset > 0) {
					ecHoldingItems = his.get(j);
					his.remove(j);
					his.add(j, his.get(j));
					his.remove(j + 1);
					his.add(j + 1, ecHoldingItems);
				} else if (compareAsset == 0 && compareSymbol > 0) {
					ecHoldingItems = his.get(j);
					his.remove(j);
					his.add(j, his.get(j));
					his.remove(j + 1);
					his.add(j + 1, ecHoldingItems);
				} else if (compareSymbol == 0
						&& his.get(j).getPortfolioID() > his.get(j)
								.getPortfolioID()) {
					ecHoldingItems = his.get(j);
					his.remove(j);
					his.add(j, his.get(j));
					his.remove(j + 1);
					his.add(j + 1, ecHoldingItems);
				}
			}
		}
		return his;
	}

	public Portfolio getPortfolio() {
		return portfolio;
	}

	public List<HoldingItem> getHoldingItems() {
		return holdingItems;
	}

	public void setHoldingItems(List<HoldingItem> holdingItems) {
		this.holdingItems = holdingItems;
	}

	public void setPortfolio(Portfolio portfolio) {
		this.portfolio = portfolio;
	}

	public boolean isStateType() {
		return stateType;
	}

	public void setStateType(boolean stateType) {
		this.stateType = stateType;
	}

	public boolean getUsePrice() {
		return usePrice;
	}

	public void setUsePrice(boolean usePrice) {
		this.usePrice = usePrice;
	}

	public boolean isAggregateFlag() {
		return aggregateFlag;
	}

	public void setAggregateFlag(boolean aggregateFlag) {
		this.aggregateFlag = aggregateFlag;
	}

}
