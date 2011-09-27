package com.lti.action.clonecenter;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.lti.service.CloningCenterManager;
import com.lti.service.PortfolioManager;
import com.lti.service.UserManager;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.PortfolioMPT;
import com.lti.service.bo.User;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.util.FormatUtil;
import com.lti.util.StringUtil;

public class PerformancetableAction {
	private List<String[]> items;
	private String categories;
	private String keyword;

	private String mpt;
	private String year;

	private UserManager userManager;
	private PortfolioManager portfolioManager;

	private List<PortfolioMPT> cachePortfolioMPTs;

	public void getCachePortfolioMPTs(final Long strategyid, final Integer years[]) {
		if (cachePortfolioMPTs == null) {
			DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PortfolioMPT.class);
			detachedCriteria.add(Restrictions.in("year", years));
			detachedCriteria.add(Restrictions.eq("strategyID", strategyid));
			detachedCriteria.addOrder(Order.desc("portfolioID"));
			cachePortfolioMPTs = ContextHolder.getPortfolioManager().findByCriteria(detachedCriteria);

		}

	}

	public PortfolioMPT getPortfolioMPT(long pid, int year) {
		if (cachePortfolioMPTs == null)
			return null;
		for (int i = 0; i < cachePortfolioMPTs.size(); i++) {
			PortfolioMPT m = cachePortfolioMPTs.get(i);
			if (m.getPortfolioID().equals(pid) && m.getYear().equals(year))
				return m;
		}
		return null;
	}

	public String[] getMPT(long portfolioid, Integer[] years, String[] mpts) {

		String[] item = new String[mpts.length * years.length + 2+5];
		for (int p = 0; p < years.length; p++) {
			PortfolioMPT mpt = getPortfolioMPT(portfolioid, years[p]);
			if (mpt == null) {
				for (int i = 0; i < mpts.length; i++) {
					item[mpts.length * p + i + 2] = "NA";
				}
				continue;
			}

			for (int i = 0; i < mpts.length; i++) {
				if (mpts[i].equals("alpha")) {
					item[mpts.length * p + i + 2] = FormatUtil.formatPercentage(mpt.getAlpha());
				} else if (mpts[i].equals("beta")) {
					item[mpts.length * p + i + 2] = FormatUtil.formatPercentage(mpt.getBeta());
				} else if (mpts[i].equals("ar")) {
					item[mpts.length * p + i + 2] = FormatUtil.formatPercentage(mpt.getAR());
				} else if (mpts[i].equals("rsquared")) {
					item[mpts.length * p + i + 2] = FormatUtil.formatPercentage(mpt.getRSquared());
				} else if (mpts[i].equals("sharperatio")) {
					item[mpts.length * p + i + 2] = FormatUtil.formatPercentage(mpt.getSharpeRatio());
				} else if (mpts[i].equals("standarddeviation")) {
					item[mpts.length * p + i + 2] = FormatUtil.formatPercentage(mpt.getStandardDeviation());
				} else if (mpts[i].equals("treynorratio")) {
					item[mpts.length * p + i + 2] = FormatUtil.formatPercentage(mpt.getTreynorRatio());
				} else if (mpts[i].equals("drawdown")) {
					item[mpts.length * p + i + 2] = FormatUtil.formatPercentage(mpt.getDrawDown());
				}
			}
		}
		return item;
	}

	@Deprecated
	public String execute() {
//		userManager = ContextHolder.getUserManager();
//		portfolioManager = ContextHolder.getPortfolioManager();
//		List<Portfolio> portfolios =null;
//		portfolios=CloningCenterManager.get13FPortfolios(categories,keyword);
//		
//		if(portfolios==null)return "success";
//
//		String[] mpts = mpt.split(",");
//		String years_[] = year.split(",");
//		Integer years[] = new Integer[years_.length];
//		User user = userManager.getLoginUser();
//		long userID;
//		if (user == null) {
//			userID = -1l;
//		} else
//			userID = user.getID();
//		boolean realtime = userManager.HasRole(Configuration.ROLE_PORTFOLIO_REALTIME, userID);
//		for (int i = 0; i < years.length; i++) {
//			int y = Integer.parseInt(years_[i].trim());
//			if (!realtime) {
//				if (y == PortfolioMPT.LAST_ONE_YEAR) {
//					y = PortfolioMPT.DELAY_LAST_ONE_YEAR;
//				} else if (y == PortfolioMPT.LAST_THREE_YEAR) {
//					y = PortfolioMPT.DELAY_LAST_THREE_YEAR;
//				} else if (y == PortfolioMPT.LAST_FIVE_YEAR) {
//					y = PortfolioMPT.DELAY_LAST_FIVE_YEAR;
//				}
//			}
//			years[i] = y;
//		}
//
//		items = new ArrayList<String[]>();
//		String[] item = new String[mpts.length * years.length + 2+5];
//		item[0] = "ID";
//		item[1] = "Name";
//		
//		
//		int len=item.length;
//		item[len-5]=Configuration.CLONE_CENTER_TOP_10;
//		item[len-4]=Configuration.CLONE_CENTER_TOTAL_POSITION;
//		item[len-3]=Configuration.TRANSACTION_BUY;
//		item[len-2]=Configuration.TRANSACTION_SELL;
//		item[len-1]=Configuration.SECTOR_TOP_SECTOR;
//		for (int i = 0; i < years.length; i++) {
//			int y = years[i];
//			String yn = y + "'s ";
//			if (y == PortfolioMPT.LAST_ONE_YEAR || y == PortfolioMPT.DELAY_LAST_ONE_YEAR) {
//				yn = "Last one year's ";
//			} else if (y == PortfolioMPT.LAST_THREE_YEAR || y == PortfolioMPT.DELAY_LAST_THREE_YEAR) {
//				yn = "Last three years' ";
//			} else if (y == PortfolioMPT.LAST_FIVE_YEAR || y == PortfolioMPT.DELAY_LAST_FIVE_YEAR) {
//				yn = "Last five years' ";
//			}
//			for (int j = 0; j < mpts.length; j++) {
//				if (mpts[j].equals("alpha")) {
//					item[i * mpts.length + j + 2] = yn + "Alpha";
//				} else if (mpts[j].equals("beta")) {
//					item[i * mpts.length + j + 2] = yn + "Beta";
//				} else if (mpts[j].equals("ar")) {
//					item[i * mpts.length + j + 2] = yn + "AR";
//				} else if (mpts[j].equals("rsquared")) {
//					item[i * mpts.length + j + 2] = yn + "RSquared";
//				} else if (mpts[j].equals("sharperatio")) {
//					item[i * mpts.length + j + 2] = yn + "Sharpe ratio";
//				} else if (mpts[j].equals("standarddeviation")) {
//					item[i * mpts.length + j + 2] = yn + "Standard deviation";
//				} else if (mpts[j].equals("treynorratio")) {
//					item[i * mpts.length + j + 2] = yn + "Treynor Ratio";
//				} else if (mpts[j].equals("drawdown")) {
//					item[i * mpts.length + j + 2] = yn + "Draw down";
//				}
//			}
//		}
//		items.add(item);
//
//		getCachePortfolioMPTs(CloningCenterManager.get13FStrategy().getID(), years);
//
//		for (int i = 0; i < portfolios.size(); i++) {
//			item = getMPT(portfolios.get(i).getID(), years, mpts);
//			item[0] = portfolios.get(i).getID() + "";
//			item[1] = portfolios.get(i).getName();
//			items.add(item);
//			String output=portfolios.get(i).getOutput();
//			item[len-5]=StringUtil.parseOutput(Configuration.CLONE_CENTER_TOP_10, output);
//			item[len-4]=StringUtil.parseOutput(Configuration.CLONE_CENTER_TOTAL_POSITION, output);
//			item[len-3]=StringUtil.parseOutput(Configuration.TRANSACTION_BUY, output);
//			item[len-2]=StringUtil.parseOutput(Configuration.TRANSACTION_SELL, output);
//			item[len-1]=StringUtil.parseOutput(Configuration.SECTOR_TOP_SECTOR, output);
//		}

		return "success";
	}

	public List<String[]> getItems() {
		return items;
	}

	public void setItems(List<String[]> items) {
		this.items = items;
	}

	public String getCategories() {
		return categories;
	}

	public void setCategories(String categories) {
		this.categories = categories;
	}

	public String getMpt() {
		return mpt;
	}

	public void setMpt(String mpt) {
		this.mpt = mpt;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

}
