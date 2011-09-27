package com.lti.action.strategyClass;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.lti.action.Action;
import com.lti.bean.StrategyClassBean;
import com.lti.bean.StrategyItem;
import com.lti.service.PortfolioManager;
import com.lti.service.StrategyClassManager;
import com.lti.service.StrategyManager;
import com.lti.service.UserManager;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.PortfolioMPT;
import com.lti.service.bo.Strategy;
import com.lti.service.bo.StrategyClass;
import com.lti.service.bo.User;
import com.lti.util.FormatUtil;
import com.lti.system.Configuration;
import com.opensymphony.xwork2.ActionSupport;

public class SaveAction extends ActionSupport implements Action {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long ID;

	private StrategyClass sc;

	private StrategyClassManager strategyClassManager;

	private UserManager userManager;

	private StrategyManager strategyManager;

	private PortfolioManager portfolioManager;

	private List<StrategyClassBean> sis;

	private String action;

	public void validate() {
		if (action == null || ID == null || action.equals("") || (!action.equals("view") && !action.equals("updateOverView") && !action.equals("updateStrategyTable") && !action.equals("updateLatestCommons")))
			this.addActionError("Invalidate action.");
		else if (strategyClassManager.get(ID) == null)
			this.addActionError("Invalidate strategy class ID");
	}

	public String execute() {
		if (action.equals("view")) {

			List<StrategyClass> scs = strategyClassManager.getChildClass(ID);
			sis = new ArrayList<StrategyClassBean>();
			// set strategies belong to ID
			if (scs != null) {
				for (int i = 0; i < scs.size(); i++) {
					List<StrategyItem> items = new ArrayList<StrategyItem>();
					StrategyClass sc = scs.get(i);
					Object[] IDs = strategyClassManager.getClassIDs(sc.getID());
					setTopStrategies(items, IDs);
					if (items.size() > 0) {
						StrategyClassBean scb = new StrategyClassBean();
						scb.setClassID(sc.getID());
						scb.setName(sc.getName());
						scb.setItems(items);
						sis.add(scb);
					}
				}
			}
			List<StrategyItem> portfolioStrategyItems = new ArrayList<StrategyItem>();
			Object[] portfolioIDs = { ID };
			setTopStrategies(portfolioStrategyItems, portfolioIDs);
			if (portfolioStrategyItems.size() > 0) {
				StrategyClassBean scb = new StrategyClassBean();
				scb.setClassID(ID);
				scb.setName(strategyClassManager.get(ID).getName());
				scb.setItems(portfolioStrategyItems);
				sis.add(scb);
			}
			sc = strategyClassManager.get(ID);
			return Action.SUCCESS;
		} else if (action.equals("updateOverView")) {
			StrategyClass tempsc = strategyClassManager.get(ID);
			tempsc.setOverView(sc.getOverView());
			strategyClassManager.update(tempsc);
			return "view";
		} else if (action.equals("updateStrategyTable")) {
			StrategyClass tempsc = strategyClassManager.get(ID);
			tempsc.setStrategyTableDown(sc.getStrategyTableDown());
			tempsc.setStrategyTableUp(sc.getStrategyTableUp());
			strategyClassManager.update(tempsc);
			return "view";
		} else if (action.equals("updateLatestCommons")) {
			StrategyClass tempsc = strategyClassManager.get(ID);
			tempsc.setLatestCommons(sc.getLatestCommons());
			strategyClassManager.update(tempsc);
			return "view";
		} else
			this.addActionError("The request is no avalidate.");
		return Action.SUCCESS;
	}

	private void setTopStrategies(List<StrategyItem> strategies, Object[] IDs) {
		Long userID = userManager.getLoginUser().getID();
		List<PortfolioMPT> mpts = strategyManager.getTopStrategyByMPT(IDs, com.lti.service.bo.PortfolioMPT.LAST_ONE_YEAR, com.lti.service.bo.PortfolioMPT.SORT_BY_SHARPERATIO, userID, 0);

		for (int i = 0; i < mpts.size(); i++) {
			Strategy str = strategyManager.get(mpts.get(i).getStrategyID());
			// Boolean isPublic=str.getIsPublic();
			// if(isPublic==null)isPublic=false;
			// if(isPublic||str.getUserID().equals(userID))
			Boolean read = userManager.HaveRole(Configuration.ROLE_STRATEGY_READ, userID, str.getID(),Configuration.RESOURCE_TYPE_STRATEGY);
			if (read || str.getUserID().equals(userID)) {
				StrategyItem si = new StrategyItem();
				// Strategy s=strategyManager.get(mpts.get(i).getStrategyID());
				si.setID(mpts.get(i).getStrategyID());
				si.setName(mpts.get(i).getStrategyName());

				si.setUserName("PUBLIC");
				if (str.getUserID() != null) {
					User u = userManager.get(str.getUserID());
					if (u != null) {
						si.setUserName(u.getUserName());
					}
				}

				si.setShowName();

				Portfolio p = portfolioManager.get(mpts.get(i).getPortfolioID());
				if (p != null) {
					si.setPortfolioID(p.getID());
					si.setPortfolioName(p.getName());

					Date lastValidDate = p.getEndDate();

					si.setPortfolioUserName("PUBLIC");
					if (p.getUserID() != null) {
						User u = userManager.get(p.getUserID());
						if (u != null) {
							si.setPortfolioUserName(u.getUserName());
						}
					}

					//si.setPortfolioShortName(p.getState());

					if (lastValidDate != null) {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
						si.setLastValidDate(sdf.format(lastValidDate));
					}
					Date lastTransactionDate = portfolioManager.getTransactionLatestDate(p.getID());
					if (lastTransactionDate != null) {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
						si.setLastTransactionDate(sdf.format(lastTransactionDate));
					}
					PortfolioMPT mpt1 = mpts.get(i);
					PortfolioMPT mpt2 = portfolioManager.getPortfolioMPT(p.getID(), PortfolioMPT.LAST_THREE_YEAR);
					PortfolioMPT mpt3 = portfolioManager.getPortfolioMPT(p.getID(), PortfolioMPT.LAST_FIVE_YEAR);

					if (mpt1 != null) {
						si.setAR1(FormatUtil.formatPercentage(mpt1.getAR()));
						si.setBeta1(FormatUtil.formatQuantity(mpt1.getBeta()));
						si.setSharpeRatio1(FormatUtil.formatPercentage(mpt1.getSharpeRatio()));
					}
					if (mpt2 != null) {
						si.setAR3(FormatUtil.formatPercentage(mpt2.getAR()));
						si.setBeta3(FormatUtil.formatQuantity(mpt2.getBeta()));
						si.setSharpeRatio3(FormatUtil.formatPercentage(mpt2.getSharpeRatio()));
					}
					if (mpt3 != null) {
						si.setAR5(FormatUtil.formatPercentage(mpt3.getAR()));
						si.setBeta5(FormatUtil.formatQuantity(mpt3.getBeta()));
						si.setSharpeRatio5(FormatUtil.formatPercentage(mpt3.getSharpeRatio()));
					}
				}
				strategies.add(si);

			}
		}

	}

	public StrategyClass getSc() {
		return sc;
	}

	public void setSc(StrategyClass sc) {
		this.sc = sc;
	}

	public List<StrategyClassBean> getSis() {
		return sis;
	}

	public void setSis(List<StrategyClassBean> sis) {
		this.sis = sis;
	}

	public Long getID() {
		return ID;
	}

	public void setID(Long id) {
		ID = id;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public StrategyClassManager getStrategyClassManager() {
		return strategyClassManager;
	}

	public void setStrategyClassManager(StrategyClassManager strategyClassManager) {
		this.strategyClassManager = strategyClassManager;
	}

	public UserManager getUserManager() {
		return userManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public StrategyManager getStrategyManager() {
		return strategyManager;
	}

	public void setStrategyManager(StrategyManager strategyManager) {
		this.strategyManager = strategyManager;
	}

	public PortfolioManager getPortfolioManager() {
		return portfolioManager;
	}

	public void setPortfolioManager(PortfolioManager portfolioManager) {
		this.portfolioManager = portfolioManager;
	}

}
