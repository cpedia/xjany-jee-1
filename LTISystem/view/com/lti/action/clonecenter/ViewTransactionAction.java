package com.lti.action.clonecenter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.struts2.ServletActionContext;

import com.lti.Exception.Security.NoPriceException;
import com.lti.action.Action;
import com.lti.action.TemplateAction;
import com.lti.listener.impl.SimulatorTransactionProcessor;
import com.lti.permission.PermissionChecker;
import com.lti.permission.PortfolioPermissionChecker;
import com.lti.service.PortfolioManager;
import com.lti.service.bo.HoldingItem;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.PortfolioInf;
import com.lti.service.bo.Transaction;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.util.LTIDate;
import com.lti.util.SecurityUtil;

public class ViewTransactionAction extends TemplateAction {
	private static final long serialVersionUID = 4822212019568588466L;
	private Long ID;
	private String holdingDate;
	private String symbol;
	private Integer size = 0;
	private String orderBy;
	private boolean stateType;

	private PortfolioManager portfolioManager;
	private boolean aggregateFlag;

	private List<Transaction> transactions = new ArrayList<Transaction>();
	public static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yyyy");

	private Date startDate;
	private Date endDate;

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	private Portfolio portfolio;

	private Boolean schedule = false;

	public Boolean isSchedule() {
		return schedule;
	}

	public void setSchedule(Boolean schedule) {
		this.schedule = schedule;
	}

	public String execute() throws Exception {

		portfolioManager = ContextHolder.getPortfolioManager();

		Date date = null;
		try {
			date = sdf1.parse(holdingDate);
		} catch (Exception e) {
			try {
				date = sdf2.parse(holdingDate);
			} catch (Exception e1) {
			}
		}

		portfolio = portfolioManager.get(ID);
		if (portfolio == null) {
			message = "The portfolio doesn't exist.";
			return Action.MESSAGE;
		}
		PermissionChecker pc = new PortfolioPermissionChecker(portfolio,ServletActionContext.getRequest());
		Date lastLegalDate = null;
		if(portfolio.isFullyPublic()){
			lastLegalDate = portfolio.getEndDate();
		}else{
			lastLegalDate = pc.getLastLegalDate();
		}
		
		if (schedule) {
			PortfolioInf pinf = portfolioManager.getPortfolioInf(ID,
					Configuration.PORTFOLIO_HOLDING_EXPECTED);
			if (pinf == null || pinf.getHolding() == null
					|| !pc.hasRealtimeRole()) {
				message = "No such schedule holding.";
				ServletActionContext.getResponse().setStatus(500);
				return Action.MESSAGE;
			}
			SimulatorTransactionProcessor stp = (SimulatorTransactionProcessor) pinf
					.getHolding().getTransactionProcessor();
			transactions = stp.getScheduleTransactions();

		} else {
			if (date.after(lastLegalDate)) {
				date = lastLegalDate;
			}
			transactions = portfolioManager.getLatestTransactions(ID, date);
		}

		stateType = SecurityUtil.usedescription(portfolio, transactions,schedule);

		aggregateFlag = false;
		return "success";
	}

	public String detailTransaction() {

		portfolioManager = ContextHolder.getPortfolioManager();
		
		Date date = null;
		try {
			date = sdf1.parse(holdingDate);
		} catch (Exception e) {
			try {
				date = sdf2.parse(holdingDate);
			} catch (Exception e1) {
			}
		}

		portfolio = portfolioManager.get(ID);
		if (portfolio == null) {
			message = "The portfolio doesn't exist.";
			return Action.MESSAGE;
		}
		PermissionChecker pc = new PortfolioPermissionChecker(portfolio,
				ServletActionContext.getRequest());
		if (schedule) {
			PortfolioInf pinf = portfolioManager.getPortfolioInf(ID,
					Configuration.PORTFOLIO_HOLDING_EXPECTED);
			if (pinf == null || pinf.getHolding() == null
					|| !pc.hasRealtimeRole()) {
				message = "No such schedule holding.";
				ServletActionContext.getResponse().setStatus(500);
				return Action.MESSAGE;
			}
			SimulatorTransactionProcessor stp = (SimulatorTransactionProcessor) pinf
					.getHolding().getTransactionProcessor();
			transactions = stp.getScheduleTransactions();

		} else {
			if (date.after(pc.getLastLegalDate())) {
				date = pc.getLastLegalDate();
			}
			PortfolioInf pinf = portfolioManager.getPortfolioInf(ID,
					Configuration.PORTFOLIO_HOLDING_CURRENT);
			List<HoldingItem> holdingItemsP = new ArrayList<HoldingItem>();
			holdingItemsP = pinf.getHolding().getHoldingItems();
			double total = 0.0;
			//统计总amount
			if(holdingItemsP!=null&&holdingItemsP.size()>0){
				for(HoldingItem hi : holdingItemsP){
					total+=hi.getShare()*hi.getPrice();
				}
			}
			Date cDate = null;
			for(int i=0;i<holdingItemsP.size();i++){
				Long portfolioID1 = Long.parseLong(holdingItemsP.get(i).getSymbol().substring(2));
//				Long portfolioID2 = Long.parseLong(holdingItemsP.get(i+1).getSymbol().substring(2));
				Date date1 = portfolioManager.getTransactionLatestDateByDate(portfolioID1, date);
//				Date date2 = portfolioManager.getTransactionLatestDateByDate(portfolioID2, date);
				if(cDate == null){
					cDate = date1;
				}
				if(date1!=null&&date1.after(cDate)){
					holdingItemsP.remove(i);
					i--;
				}else if(date1!=null&&!date1.after(cDate)&&!cDate.equals(date1)){
					cDate = date1;
					holdingItemsP.remove(i-1);
					i--;
				}
			}
			List<Double> amountList = new ArrayList<Double>();
			for (HoldingItem hi : holdingItemsP) {
				try {
					if (hi.getSymbol().contains("P_")) {
						List<Transaction> lTransaction = new ArrayList<Transaction>();
						lTransaction = portfolioManager.getLatestTransactions(
								Long.parseLong(hi.getSymbol().substring(2)),
								date);
						if (lTransaction != null&&lTransaction.size()>0) {
							for (Transaction ts : lTransaction) {
								String pSymbol = "P_"+Long.toString(ts.getPortfolioID());
								double amount = ts.getAmount()*hi.getShare();
								amountList.add(amount);
								ts.setPortfolioSymbol(pSymbol);
								ts.setPortfolioName(portfolioManager.get(ts.getPortfolioID()).getName());
								transactions.add(ts);
							}
						}
					}
				} catch (Exception e) {
					break;
				}
			}
			//设置transactions的percentage
			for(int i = 0;i<transactions.size();i++){
				if(total!=0.0){
					transactions.get(i).setPercentage(amountList.get(i)/total);
				}
			}
		}

		stateType = SecurityUtil.usedescription(portfolio, transactions,
				schedule);
		aggregateFlag = true;
		return Action.SUCCESS;
	}

	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

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

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public boolean isStateType() {
		return stateType;
	}

	public void setStateType(boolean stateType) {
		this.stateType = stateType;
	}

	public boolean isAggregateFlag() {
		return aggregateFlag;
	}

	public void setAggregateFlag(boolean aggregateFlag) {
		this.aggregateFlag = aggregateFlag;
	}

}
