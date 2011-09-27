package com.lti.service.impl;

import java.io.Serializable;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.lti.Exception.SecurityException;
import com.lti.Exception.Executor.SimulateException;
import com.lti.Exception.Security.NoPriceException;
import com.lti.listener.impl.SimulatorTransactionProcessor;
import com.lti.permission.MYPLANIQPermissionManager;
import com.lti.service.AssetClassManager;
import com.lti.service.PortfolioHoldingManager;
import com.lti.service.PortfolioManager;
import com.lti.service.ProfileManager;
import com.lti.service.SecurityManager;
import com.lti.service.StrategyManager;
import com.lti.service.UserManager;
import com.lti.service.bo.CachePortfolioItem;
import com.lti.service.bo.CacheStrategyItem;
import com.lti.service.bo.ConfidenceCheck;
import com.lti.service.bo.EmailNotification;
import com.lti.service.bo.GlobalObject;
import com.lti.service.bo.HoldingItem;
import com.lti.service.bo.HoldingRecord;
import com.lti.service.bo.Log;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.PortfolioDailyData;
import com.lti.service.bo.PortfolioFollowDate;
import com.lti.service.bo.PortfolioInf;
import com.lti.service.bo.PortfolioMPT;
import com.lti.service.bo.PortfolioPerformance;
import com.lti.service.bo.PortfolioState;
import com.lti.service.bo.Profile;
import com.lti.service.bo.Security;
import com.lti.service.bo.SecurityDailyData;
import com.lti.service.bo.SecurityRanking;
import com.lti.service.bo.Strategy;
import com.lti.service.bo.Transaction;
import com.lti.service.bo.UserPermission;
import com.lti.service.bo.UserResource;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.type.Interval;
import com.lti.type.PaginationSupport;
import com.lti.type.finance.Asset;
import com.lti.type.finance.HoldingInf;
import com.lti.type.finance.PortfolioInfo;
import com.lti.util.LTIDate;
import com.lti.util.StringUtil;

@SuppressWarnings("unchecked")
public class PortfolioManagerImpl extends DAOManagerImpl implements PortfolioManager, Serializable {

	private static final long serialVersionUID = -7076431704357506909L;

	@Override
	public Portfolio get(Long id) {
		Session s = null;
		try {
			if (id == null)
				return null;
			s = getHibernateTemplate().getSessionFactory().openSession();
			Portfolio p = (Portfolio) s.get(Portfolio.class, id);
			return p;
		} finally {
			if (s != null && s.isOpen())
				s.close();
		}

	}

	@Override
	public PortfolioState getPortfolioState(Long id) {
		Session s = null;
		try {
			if (id == null)
				return null;
			s = getHibernateTemplate().getSessionFactory().openSession();
			PortfolioState p = (PortfolioState) s.get(PortfolioState.class, id);
			return p;
		} finally {
			if (s != null && s.isOpen())
				s.close();
		}
	}

	/*
	 * (non-Javadoc) the transaction should be added to this API.
	 * 
	 * @see com.lti.service.PortfolioManager#save(com.lti.service.bo.Portfolio)
	 */
	@Override
	public long save(Portfolio p) {
		String name = p.getName();
		if (name == null || name.equals("")) {
			throw new RuntimeException("The name of the portfolio is invalid.");
		}

		name = StringUtil.getValidName2(name).trim();
		p.setName(name);
		Portfolio op = this.get(p.getName());
		if (op != null) {
			throw new RuntimeException("The portfolio name has been used before.");
		}
		p.setCreatedDate(new Date());
		getHibernateTemplate().save(p);
		updateSymbol(p);
		return p.getID();
	}

	@Override
	public void updateSymbol(Portfolio portf) {
		if (portf.getID() != null) {
			String symbol = "P_" + portf.getID();
			SecurityManager sm = ContextHolder.getSecurityManager();
			Security s = sm.get(symbol);
			if (s == null) {
				s = new Security();
			}
			s.setName(portf.getName());
			s.setSymbol(symbol);
			s.setSecurityType(Configuration.SECURITY_TYPE_PORTFOLIO);
			if (s.getID() == null) {
				try {
					sm.add(s);
				} catch (SecurityException e) {
					e.printStackTrace();
				}
			} else
				sm.update(s);
		}
	}

	@Override
	public void update(Portfolio p) {
		String name = p.getName();
		if (name == null || name.equals("")) {
			throw new RuntimeException("The name of the portfolio is invalid.");
		}

		name = StringUtil.getValidName2(name).trim();
		p.setName(name);
		getHibernateTemplate().update(p);
		updateSymbol(p);
	}

	@Override
	public void remove(Long id) throws Exception {
		this.remove(id, false);
	}

	public void remove(Long id, boolean expired) throws Exception {
		List<String> sqls = new ArrayList<String>();

		Portfolio portfolio = this.get(id);

		// 先清除其它用户follow此portfolio的记录
		Long userID = portfolio.getUserID();
		Long planID = portfolio.getPlanID();
		boolean admin = userID == Configuration.SUPER_USER_ID;
		this.changeUserResourceBeforeDeletePortfolio(portfolio);

		sqls.add("delete from " + Configuration.TABLE_EMAILNOTIFICATION + " where portfolioid=" + id);
		sqls.add("delete from " + Configuration.TABLE_PROFILE + " where portfolioid=" + id);
		sqls.add("delete from " + Configuration.TABLE_SECURITY + " where symbol='P_" + id + "'");
		sqls.add("delete from " + Configuration.TABLE_LOG + " where portfolioid=" + id);
		sqls.add("delete from " + Configuration.TABLE_PORTFOLIO_DAILY_DATA + " where portfolioid=" + id);
		sqls.add("delete from " + Configuration.TABLE_PORTFOLIO_MPT + " where portfolioid=" + id);
		sqls.add("delete from " + Configuration.TABLE_TRANACTION + " where portfolioid=" + id);
		sqls.add("delete from " + Configuration.TABLE_PORTFOLIO_HOLDINGITEM + " where portfolioid=" + id);
		sqls.add("delete from " + Configuration.TABLE_PORTFOLIO_FOLLOWDATE + " where portfolioid=" + id);
		sqls.add("delete from " + Configuration.TABLE_PERSONALPORTFOLIO_TRANSACTION + " where PersonalPortfolioID=" + id);
		sqls.add("delete from " + Configuration.TABLE_USERRESOURCE + " where ResourceID=" + id + " and ResourceType=" + Configuration.USER_RESOURCE_PORTFOLIO);
		sqls.add("delete from " + Configuration.TABLE_PORTFOLIO + " where id=" + id);
		sqls.add("delete from " + Configuration.TABLE_PORTFOLIOINF + " where portfolioid=" + id);
		executeSQL(sqls);

		if (!expired && !admin) {
			MYPLANIQPermissionManager mpm = new MYPLANIQPermissionManager();
			// 最后删除portfolio owner与这个portfolio的相关记录
			mpm.afterPortfolioDelete(userID, id, planID);
		}

	}

	@Override
	public Portfolio get(String name) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Portfolio.class);

		Long id = null;
		try {
			id = Long.parseLong(name.substring(2));
		} catch (Exception e) {

		}
		if (id != null) {
			detachedCriteria.add(Restrictions.or(Restrictions.eq("Name", name), Restrictions.eq("ID", id)));
		} else {
			detachedCriteria.add(Restrictions.eq("Name", name));
		}

		List<Portfolio> bolist = findByCriteria(detachedCriteria, 1, 0);

		if (bolist.size() >= 1)
			return bolist.get(0);
		else
			return null;
	}

	public boolean isExisted(String name) {
		if (this.get(name) != null)
			return true;
		else
			return false;

	}

	public List<Portfolio> getPortfolios(DetachedCriteria detachedCriteria) {
		return findByCriteria(detachedCriteria);
	}

	@Override
	public PaginationSupport getPortfolios(DetachedCriteria detachedCriteria, int pageSize, int startIndex) {
		return findPageByCriteria(detachedCriteria, pageSize, startIndex);
	}

	public Portfolio getOriginalPortfolio(long id) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Portfolio.class);
		detachedCriteria.add(Restrictions.eq("OriginalPortfolioID", id));
		List<com.lti.service.bo.Portfolio> bolist = findByCriteria(detachedCriteria, 1, 0);
		if (bolist.size() >= 1)
			return bolist.get(0);
		else
			return null;
	}

	public void updatePortfolioState(PortfolioState ps) {
		getHibernateTemplate().saveOrUpdate(ps);

	}

	/** *********************************************************** */
	/* ==original portfolio==End */
	/** *********************************************************** */

	/** *********************************************************** */
	/* ==List Method==Start */
	/** *********************************************************** */
	public List<Portfolio> getPortfolios() {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Portfolio.class);

		return this.getPortfolios(detachedCriteria);
	}

	@Override
	public List<Portfolio> getSimplePortfolios(long mainstrategyid, long type) {
		String hql = "select new Portfolio(p.ID,p.Name,p.Type,p.State,p.EndDate) from Portfolio p";
		if (mainstrategyid != -1) {
			hql += " where p.MainStrategyID=" + mainstrategyid;
		}
		if (type != -1) {
			if (hql.endsWith("p")) {
				hql += " where ";
			} else {
				hql += " and ";
			}
			hql += "bit_and(p.Type," + type + ")>0";
		}
		return this.findByHQL(hql);
	}

	@Override
	public List<Portfolio> getPortfolios(long mainstrategyid, long type) {
		String hql = "from Portfolio p";
		if (mainstrategyid != -1) {
			hql += " where p.MainStrategyID=" + mainstrategyid;
		}
		if (type != -1) {
			if (hql.endsWith("p")) {
				hql += " where ";
			} else {
				hql += " and ";
			}
			hql += "bit_and(p.Type," + type + ")>0";
		}
		return this.findByHQL(hql);
	}

	@Override
	public List<Portfolio> getPortfoliosByUser(long userid, long type) {
		String hql = "from Portfolio p";
		if (userid != -1) {
			hql += " where p.UserID=" + userid;
		}
		if (type != -1) {
			if (hql.endsWith("p")) {
				hql += " where ";
			} else {
				hql += " and ";
			}
			hql += "bit_and(p.Type," + type + ")>0";
		}
		return this.findByHQL(hql);
	}

	@Override
	public List<Portfolio> getPortfoliosByGroup(long groupid) {

		String hql = "select p from Portfolio p" + " where p.ID in ( select gr.ResourceID from GroupRole gr  where gr.GroupID=" + groupid + " and gr.ResourceType=2 )";

		List<Portfolio> bolist = findByHQL(hql);

		return bolist;
	}

	@Override
	public List<Portfolio> getPortfoliosByGroup(long groupid, long roleid) {

		String hql = "select p from Portfolio p" + " where p.ID in ( select gr.ResourceID from GroupRole gr  where gr.GroupID=" + groupid + " and gr.RoleID=" + roleid + " and gr.ResourceType=2)";

		List<Portfolio> bolist = findByHQL(hql);

		return bolist;
	}

	@Override
	public List<Portfolio> getPortfoliosByGroupRoles(Long[] groupids, Long[] roleids) {

		String hql = "select p from Portfolio p" + " where  p.ID in( select gr.ResourceID from GroupRole gr where gr.ResourceType=2 ";

		if (groupids.length == 1) {
			hql += " and gr.GroupID=" + groupids[0];
		} else if (groupids.length > 1) {
			for (int i = 0; i < groupids.length; i++) {
				if (i == 0) {
					hql += " and (gr.GroupID=" + groupids[i];
				} else if (i == groupids.length - 1) {
					hql += " or gr.GroupID=" + groupids[i] + ")";
				} else {
					hql += " or gr.GroupID=" + groupids[i];
				}

			}
		}
		if (roleids.length == 1) {
			hql += " and gr.RoleID=" + roleids[0];
		} else if (roleids.length > 1) {
			for (int i = 0; i < roleids.length; i++) {
				if (i == 0) {
					hql += " and (gr.RoleID=" + roleids[i];
				} else if (i == roleids.length - 1) {
					hql += " or gr.RoleID=" + roleids[i] + ")";
				} else {
					hql += " or gr.RoleID=" + roleids[i];
				}

			}
		}
		hql += ")";
		// System.out.println(hql);
		List<Portfolio> bolist = findByHQL(hql);
		return bolist;
	}

	public List<Portfolio> getPortfoliosByRecord(Date date, Long securityID) {
		String sDate = LTIDate.parseDateToString(date);
		String hql = "select * from ltisystem.ltisystem_portfolio where id in (select distinct portfolioid from ltisystem.ltisystem_transaction where securityID = " + securityID + " and date < \'" + sDate + "\' )";
		List<Portfolio> pos = super.findByHQL(hql);
		if (pos != null && pos.size() > 0)
			return pos;
		return null;
	}

	public List<Portfolio> getPortfoliosByName(String portfName) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Portfolio.class);
		detachedCriteria.add(Restrictions.like("Name", "%" + portfName + "%"));
		return this.getPortfolios(detachedCriteria);

	}

	public PaginationSupport getPortfoliosByName(String portfName, int pageSize, int startIndex) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Portfolio.class);

		detachedCriteria.add(Restrictions.like("Name", "%" + portfName + "%"));

		return super.findPageByCriteria(detachedCriteria, pageSize, startIndex);

	}

	private List<Long> getPortfolioIDs(List<Portfolio> portfolios) {
		if (portfolios == null || portfolios.size() == 0)
			return null;
		List<Long> IDs = new ArrayList<Long>();
		for (int i = 0; i < portfolios.size(); i++) {
			Long ID = portfolios.get(i).getID();
			IDs.add(ID);
		}
		return IDs;
	}

	/** *********************************************************** */
	/* ==List Method==End */
	/** *********************************************************** */

	/** *********************************************************** */
	/* ==Transaction==Start */
	/** *********************************************************** */
	public Transaction getTransaction(long id) {

		return (Transaction) getHibernateTemplate().get(Transaction.class, id);

	}

	public long addTransaction(Transaction transaction) {

		getHibernateTemplate().save(transaction);

		return transaction.getID();
	}

	public void clearTransaction(long portfolioID, long strategyID, Date date) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Transaction.class);

		detachedCriteria.add(Restrictions.eq("PortfolioID", portfolioID));

		detachedCriteria.add(Restrictions.eq("StrategyID", strategyID));

		detachedCriteria.add(Restrictions.eq("Date", date));

		List<Transaction> bolist = findByCriteria(detachedCriteria);

		getHibernateTemplate().delete(bolist);
	}

	public void clearTransaction(long portfID, int transactionType, Date curDate) throws Exception {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = dateFormat.format(curDate);
		// System.out.println("delete from " + Configuration.TABLE_TRANACTION +
		// " where portfolioid=" + portfID + " and transactiontype= " +
		// transactionType +" and date< "+ date);
		executeSQL("delete from " + Configuration.TABLE_TRANACTION + " where portfolioid=" + portfID + " and transactiontype= " + transactionType + " and date< \'" + date + "\'");
	}

	public void clearTransaction(long portfID) throws Exception {
		executeSQL("delete from " + Configuration.TABLE_TRANACTION + " where portfolioid=" + portfID);
	}

	public void deleteTransaction(long transactionID) throws Exception {
		executeSQL("delete from " + Configuration.TABLE_TRANACTION + " where ID= " + transactionID + " and isComplete= " + "false");
	}

	public void updateTransaction(Transaction transaction) {

		getHibernateTemplate().update(transaction);

	}

	public PaginationSupport findTransactions(DetachedCriteria detachedCriteria, int pageSize, int startIndex) {

		return findPageByCriteria(detachedCriteria, pageSize, startIndex);

	}

	public List<Transaction> getTransactions(DetachedCriteria detachedCriteria) {

		return findByCriteria(detachedCriteria);

	}

	public List<Transaction> getAllTransactions(long portfolioID) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Transaction.class);
		detachedCriteria.add(Restrictions.eq("PortfolioID", portfolioID));
		detachedCriteria.addOrder(Order.asc("Date")).addOrder(Order.desc("Operation"));
		return getTransactions(detachedCriteria);
	}

	public List<Transaction> getTransactions(long portfolioID) {

		return getTransactions(portfolioID, Configuration.TRANSACTION_TYPE_REAL);
	}

	public List<Transaction> getTransactionsInPeriod(long portfolioID, Date endDate) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Transaction.class);

		detachedCriteria.add(Restrictions.eq("PortfolioID", portfolioID));

		detachedCriteria.add(Restrictions.le("Date", endDate));
		// last added line
		detachedCriteria.add(Restrictions.eq("TransactionType", Configuration.TRANSACTION_TYPE_REAL));

		return this.getTransactions(detachedCriteria);
	}

	public PaginationSupport getTransactions(long id, int pageSize, int startIndex) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Transaction.class);

		detachedCriteria.add(Restrictions.eq("PortfolioID", id));
		// last added line
		detachedCriteria.add(Restrictions.eq("TransactionType", Configuration.TRANSACTION_TYPE_REAL));

		return this.findTransactions(detachedCriteria, pageSize, startIndex);
	}

	public List<com.lti.service.bo.Transaction> getTransactions(long id, Interval interval) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Transaction.class);

		detachedCriteria.add(Restrictions.eq("PortfolioID", id));

		if (interval.getStartDate() != null)
			detachedCriteria.add(Restrictions.ge("Date", interval.getStartDate()));

		if (interval.getEndDate() != null)
			detachedCriteria.add(Restrictions.le("Date", interval.getEndDate()));

		detachedCriteria.add(Restrictions.eq("TransactionType", Configuration.TRANSACTION_TYPE_REAL));

		return this.getTransactions(detachedCriteria);
	}

	public List<Transaction> getTransactions(long id, long securityid) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Transaction.class);

		detachedCriteria.add(Restrictions.eq("PortfolioID", id));

		detachedCriteria.add(Restrictions.eq("SecurityID", securityid));
		// last added line
		detachedCriteria.add(Restrictions.eq("TransactionType", Configuration.TRANSACTION_TYPE_REAL));

		detachedCriteria.addOrder(Order.asc("Date"));

		return this.getTransactions(detachedCriteria);
	}

	@Override
	public List<Transaction> getTransactionsAfter(long portfolioid, Date date) {
		return this.getTransactionsAfter(portfolioid, Configuration.TRANSACTION_TYPE_REAL, date);
	}

	@Override
	public List<Transaction> getTransactionsAfter(long portfolioid, int transactiontype, Date date) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Transaction.class);
		detachedCriteria.add(Restrictions.eq("PortfolioID", portfolioid));
		detachedCriteria.add(Restrictions.gt("Date", date));
		// last added line
		detachedCriteria.add(Restrictions.eq("TransactionType", transactiontype));
		detachedCriteria.addOrder(Order.asc("Date"));
		return this.getTransactions(detachedCriteria);
	}

	@Override
	public List<Transaction> getTransactionsByPortfolioIDAndTypeAndDate(long portfolioID, int transactionType, Date date) {
		// TODO Auto-generated method stub
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Transaction.class);
		detachedCriteria.add(Restrictions.eq("PortfolioID", portfolioID));
		detachedCriteria.add(Restrictions.eq("TransactionType", transactionType));
		detachedCriteria.add(Restrictions.eq("Date", date));
		return this.getTransactions(detachedCriteria);
	}

	public PaginationSupport getTransactions(long id, long securityid, int pageSize, int startIndex) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Transaction.class);

		detachedCriteria.add(Restrictions.eq("PortfolioID", id));

		detachedCriteria.add(Restrictions.lt("SecurityID", securityid));
		// last added line
		detachedCriteria.add(Restrictions.eq("TransactionType", Configuration.TRANSACTION_TYPE_REAL));

		return this.findTransactions(detachedCriteria, pageSize, startIndex);
	}

	public List<Transaction> getTransactions(long id, long securityid, Interval interval) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Transaction.class);

		detachedCriteria.add(Restrictions.eq("PortfolioID", id));

		detachedCriteria.add(Restrictions.lt("SecurityID", securityid));

		if (interval.getStartDate() != null)
			detachedCriteria.add(Restrictions.lt("Date", interval.getStartDate()));

		if (interval.getEndDate() != null)
			detachedCriteria.add(Restrictions.le("Date", interval.getEndDate()));
		// last added line
		detachedCriteria.add(Restrictions.eq("TransactionType", Configuration.TRANSACTION_TYPE_REAL));

		return this.getTransactions(detachedCriteria);
	}

	public PaginationSupport getTransactions(long id, Interval interval, long securityid, int pageSize, int startIndex) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Transaction.class);

		detachedCriteria.add(Restrictions.eq("PortfolioID", id));

		detachedCriteria.add(Restrictions.eq("SecurityID", securityid));

		detachedCriteria.add(Restrictions.gt("Date", interval.getStartDate()));

		detachedCriteria.add(Restrictions.le("Date", interval.getEndDate()));
		// last added line
		detachedCriteria.add(Restrictions.eq("TransactionType", Configuration.TRANSACTION_TYPE_REAL));

		return this.findTransactions(detachedCriteria, pageSize, startIndex);
	}

	public List<Transaction> getTransactions(long portfolioid, Date date) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Transaction.class);

		detachedCriteria.add(Restrictions.eq("PortfolioID", portfolioid));

		detachedCriteria.add(Restrictions.eq("Date", date));
		// last added line
		detachedCriteria.add(Restrictions.eq("TransactionType", Configuration.TRANSACTION_TYPE_REAL));

		return this.getTransactions(detachedCriteria);
	}

	public List<Transaction> getTransactions(long portfolioID, Integer[] transactionTypes) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Transaction.class);

		detachedCriteria.add(Restrictions.eq("PortfolioID", portfolioID));

		detachedCriteria.add(Restrictions.in("TransactionType", transactionTypes));

		detachedCriteria.addOrder(Order.asc("Date"));

		return this.getTransactions(detachedCriteria);
	}

	public List<Transaction> getTransactions(long portfolioID, long securityID, Integer[] transactionTypes) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Transaction.class);

		detachedCriteria.add(Restrictions.eq("PortfolioID", portfolioID));

		detachedCriteria.add(Restrictions.eq("SecurityID", securityID));

		detachedCriteria.add(Restrictions.in("TransactionType", transactionTypes));

		detachedCriteria.addOrder(Order.asc("Date"));

		return this.getTransactions(detachedCriteria);
	}

	public List<Transaction> getNonScheduleTransactions(long portfolioID, long securityID, Date lastDate) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Transaction.class);
		detachedCriteria.add(Restrictions.eq("PortfolioID", portfolioID));
		detachedCriteria.add(Restrictions.eq("SecurityID", securityID));
		detachedCriteria.add(Restrictions.eq("IsIgnore", false));
		detachedCriteria.add(Restrictions.lt("Date", lastDate));
		detachedCriteria.add(Restrictions.ne("TransactionType", Configuration.TRANSACTION_TYPE_SCHEDULE));
		detachedCriteria.addOrder(Order.asc("Date"));
		return this.getTransactions(detachedCriteria);
	}

	@Override
	public List<Transaction> getRealAndReinvestTransactions(Long portfolioID) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Transaction.class);
		detachedCriteria.add(Restrictions.eq("PortfolioID", portfolioID));
		detachedCriteria.add(Restrictions.or(Restrictions.eq("TransactionType", Configuration.TRANSACTION_TYPE_REAL), Restrictions.eq("TransactionType", Configuration.TRANSACTION_TYPE_REINVEST)));
		detachedCriteria.addOrder(Order.asc("Date"));
		return this.getTransactions(detachedCriteria);
	}

	@Override
	public List<Transaction> getRealTransactions(long portfolioID, long securityID, Date startDate) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Transaction.class);
		detachedCriteria.add(Restrictions.eq("PortfolioID", portfolioID));
		detachedCriteria.add(Restrictions.eq("SecurityID", securityID));
		detachedCriteria.add(Restrictions.eq("IsIgnore", false));
		detachedCriteria.add(Restrictions.ge("Date", startDate));
		detachedCriteria.add(Restrictions.ne("TransactionType", Configuration.TRANSACTION_TYPE_SCHEDULE));
		detachedCriteria.addOrder(Order.asc("Date"));
		return this.getTransactions(detachedCriteria);
	}

	public List<Transaction> getCashFlowTransaction(long portfolioID) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Transaction.class);
		detachedCriteria.add(Restrictions.or(Restrictions.eq("Operation", Configuration.WITHDRAW), Restrictions.eq("Operation", Configuration.DEPOSIT)));
		detachedCriteria.add(Restrictions.eq("PortfolioID", portfolioID));
		detachedCriteria.addOrder(Order.asc("Date"));
		return this.getTransactions(detachedCriteria);
	}

	public List<Transaction> getTransactions(long portfolioID, int transactionType) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Transaction.class);

		detachedCriteria.add(Restrictions.eq("PortfolioID", portfolioID));

		detachedCriteria.add(Restrictions.eq("TransactionType", transactionType));

		detachedCriteria.addOrder(Order.asc("Date"));

		return this.getTransactions(detachedCriteria);
	}

	public void deleteTransactions(long portfolioID, int transactionType) throws Exception {
		List<String> sqls = new ArrayList<String>();
		sqls.add("delete from " + Configuration.TABLE_TRANACTION + " where portfolioid=" + portfolioID + " and transactiontype= " + transactionType);
		executeSQL(sqls);
	}

	@Override
	public List<Transaction> getLatestTransactions(long portfolioid, Date date) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Transaction.class);

		detachedCriteria.add(Restrictions.eq("PortfolioID", portfolioid));

		Date d = this.getTransactionLatestDate(portfolioid, date);

		detachedCriteria.add(Restrictions.eq("Date", d));

		// last added line
		detachedCriteria.add(Restrictions.eq("TransactionType", Configuration.TRANSACTION_TYPE_REAL));

		return this.getTransactions(detachedCriteria);
	}

	public PaginationSupport getTransactions(long portfolioid, Interval interval, int pageSize, int startIndex) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Transaction.class);

		detachedCriteria.add(Restrictions.eq("PortfolioID", portfolioid));

		detachedCriteria.add(Restrictions.gt("Date", interval.getStartDate()));

		detachedCriteria.add(Restrictions.le("Date", interval.getEndDate()));
		// last added line
		detachedCriteria.add(Restrictions.eq("TransactionType", Configuration.TRANSACTION_TYPE_REAL));

		return this.findTransactions(detachedCriteria, pageSize, startIndex);
	}

	/** *********************************************************** */
	/* ==Transaction==End */
	/** *********************************************************** */

	/** *********************************************************** */
	/* ==DailyData==Start */
	/** *********************************************************** */

	public long saveDailyData(PortfolioDailyData portfolioDailyData) {

		getHibernateTemplate().save(portfolioDailyData);

		return portfolioDailyData.getID();

	}

	public PortfolioDailyData getDailydata(long portfolioid, Date date) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PortfolioDailyData.class);

		detachedCriteria.add(Restrictions.eq("PortfolioID", portfolioid));

		detachedCriteria.add(Restrictions.eq("Date", date));

		List<PortfolioDailyData> bolist = findByCriteria(detachedCriteria, 1, 0);

		if (bolist.size() >= 1)
			return bolist.get(0);
		else
			return null;
	}

	public PortfolioDailyData getDailyDataIngoreError(long portfolioid, Date date) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PortfolioDailyData.class);

		detachedCriteria.add(Restrictions.eq("PortfolioID", portfolioid));

		detachedCriteria.add(Restrictions.le("Date", date));

		detachedCriteria.addOrder(Order.desc("Date"));

		List<PortfolioDailyData> bolist = findByCriteria(detachedCriteria);

		if (bolist.size() >= 1)
			return bolist.get(0);

		else {

			detachedCriteria = DetachedCriteria.forClass(PortfolioDailyData.class);

			detachedCriteria.add(Restrictions.eq("PortfolioID", portfolioid));

			detachedCriteria.add(Restrictions.ge("Date", date));

			detachedCriteria.addOrder(Order.asc("Date"));

			bolist = findByCriteria(detachedCriteria);

			if (bolist.size() >= 1)
				return bolist.get(0);

		}

		return null;
	}

	public PortfolioDailyData getLatestDailyData(long portfolioid) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PortfolioDailyData.class);

		detachedCriteria.add(Restrictions.eq("PortfolioID", portfolioid));

		detachedCriteria.addOrder(Order.desc("Date"));

		List<com.lti.service.bo.PortfolioDailyData> bolist = findByCriteria(detachedCriteria);

		if (bolist.size() >= 1)
			return bolist.get(0);
		else
			return null;
	}

	public void removeDailyData(long portfolioid, Date date) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PortfolioDailyData.class);

		detachedCriteria.add(Restrictions.eq("PortfolioID", portfolioid));

		detachedCriteria.add(Restrictions.eq("Date", date));

		List<PortfolioDailyData> bolist = findByCriteria(detachedCriteria, 1, 0);

		getHibernateTemplate().delete(bolist);
	}

	public void clearDailyData(long portfID) throws Exception {

		executeSQL("delete from " + Configuration.TABLE_PORTFOLIO_DAILY_DATA + " where portfolioid=" + portfID);

	}

	public List<PortfolioDailyData> getDailydatas(DetachedCriteria detachedCriteria) {

		return findByCriteria(detachedCriteria);

	}

	public PaginationSupport findDailyDatas(DetachedCriteria detachedCriteria, int pageSize, int startIndex) {

		return findPageByCriteria(detachedCriteria, pageSize, startIndex);

	}

	public List<PortfolioDailyData> getDailydatas(long portfolioid) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PortfolioDailyData.class);

		detachedCriteria.add(Restrictions.eq("PortfolioID", portfolioid));

		detachedCriteria.addOrder(Order.asc("Date"));

		return this.getDailydatas(detachedCriteria);
	}

	public List<PortfolioDailyData> getDailydatasByPeriod(long portfolioid, Date startDate, Date endDate) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PortfolioDailyData.class);

		detachedCriteria.add(Restrictions.eq("PortfolioID", portfolioid));

		detachedCriteria.add(Restrictions.le("Date", endDate));

		detachedCriteria.add(Restrictions.ge("Date", startDate));

		detachedCriteria.addOrder(Order.asc("Date"));

		return this.getDailydatas(detachedCriteria);
	}

	public PortfolioDailyData getSnapShotDailyData(long portfolioid, Date date) {
		List<PortfolioDailyData> dailyDatas = getDailydataInPeirod(portfolioid, date);
		if (dailyDatas != null && dailyDatas.size() > 0)
			return dailyDatas.get(dailyDatas.size() - 1);
		else
			return null;
	}

	public List<PortfolioDailyData> getDailydataInPeirod(long portfolioid, Date endDate) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PortfolioDailyData.class);

		detachedCriteria.add(Restrictions.eq("PortfolioID", portfolioid));

		detachedCriteria.add(Restrictions.le("Date", endDate));

		detachedCriteria.addOrder(Order.asc("Date"));

		return this.getDailydatas(detachedCriteria);
	}

	public PaginationSupport getDailydatas(long id, int pageSize, int startIndex) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PortfolioDailyData.class);

		detachedCriteria.add(Restrictions.eq("PortfolioID", id));

		return this.findDailyDatas(detachedCriteria, pageSize, startIndex);
	}

	public List<PortfolioDailyData> getDailydatas(long id, Interval interval) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PortfolioDailyData.class);

		detachedCriteria.add(Restrictions.eq("PortfolioID", id));

		detachedCriteria.add(Restrictions.ge("Date", interval.getStartDate()));

		detachedCriteria.add(Restrictions.le("Date", interval.getEndDate()));

		detachedCriteria.addOrder(Order.asc("Date"));

		return this.getDailydatas(detachedCriteria);
	}

	public PaginationSupport getDailydatas(long id, Interval interval, int pageSize, int startIndex) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PortfolioDailyData.class);

		detachedCriteria.add(Restrictions.eq("PortfolioID", id));

		detachedCriteria.add(Restrictions.ge("Date", interval.getStartDate()));

		detachedCriteria.add(Restrictions.le("Date", interval.getEndDate()));

		return this.findDailyDatas(detachedCriteria, pageSize, startIndex);
	}

	/** *********************************************************** */
	/* ==DailyData==End */
	/** *********************************************************** */

	/** *********************************************************** */
	/* ==Log==Start */
	/** *********************************************************** */

	public long addUserLog(Log log) {

		return this.addLog(log, com.lti.system.Configuration.LOG_TYPE_USER);

	}

	public long addSystemLog(Log log) {

		return this.addLog(log, com.lti.system.Configuration.LOG_TYPE_SYSTEM);

	}

	public long addLog(Log log, int type) {

		log.setType(type);

		getHibernateTemplate().save(log);

		return log.getID();
	}

	public void removeLog(long id) {

		deleteByHQL("from Log where ID=" + id);

	}

	public void clearLog(long portfolioid) throws Exception {

		executeSQL("delete from " + Configuration.TABLE_LOG + " where portfolioid=" + portfolioid);

	}

	public void clearLog(long portfolioid, long strategyid) {

		deleteByHQL("from Log where PortfolioID=" + portfolioid + " and StrategyID=" + strategyid);

	}

	public void clearLog(long portfolioid, Date logDate) {

		List<Log> logList = this.getLogs(portfolioid, logDate);

		if (logList == null)
			return;

		getHibernateTemplate().delete(logList);

	}

	public void clearLog(long portfolioid, long strategyid, Date logDate) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Log.class);

		detachedCriteria.add(Restrictions.eq("PortfolioID", portfolioid));

		detachedCriteria.add(Restrictions.eq("StrategyID", strategyid));

		detachedCriteria.add(Restrictions.eq("LogDate", logDate));

		List<Log> logList = this.getLogs(detachedCriteria);

		if (logList == null)
			return;

		getHibernateTemplate().delete(logList);

	}

	public PaginationSupport findLogs(DetachedCriteria detachedCriteria, int pageSize, int startIndex) {

		return findPageByCriteria(detachedCriteria, pageSize, startIndex);

	}

	public List<Log> getLogs(DetachedCriteria detachedCriteria) {

		detachedCriteria.addOrder(Order.asc("ID"));
		return findByCriteria(detachedCriteria);

	}

	public List<Log> getLogs(long portfolioID) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Log.class);

		detachedCriteria.add(Restrictions.eq("PortfolioID", portfolioID));

		return this.getLogs(detachedCriteria);
	}

	public List<Log> getLogsInPeriod(long portfolioID, Date endDate) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Log.class);

		detachedCriteria.add(Restrictions.eq("PortfolioID", portfolioID));

		detachedCriteria.add(Restrictions.le("LogDate", endDate));

		return this.getLogs(detachedCriteria);
	}

	public List<Log> getLogsInPeriod(long portfolioID, long strategyID, Date endDate) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Log.class);

		detachedCriteria.add(Restrictions.eq("PortfolioID", portfolioID));

		detachedCriteria.add(Restrictions.eq("StrategyID", strategyID));

		detachedCriteria.add(Restrictions.le("LogDate", endDate));

		return this.getLogs(detachedCriteria);
	}

	@Override
	public PaginationSupport getStrategyLogs(long portfolioID, int pageSize, int startIndex) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Log.class);

		detachedCriteria.add(Restrictions.eq("PortfolioID", portfolioID));

		detachedCriteria.add(Restrictions.eq("Type", 1));

		return this.findLogs(detachedCriteria, pageSize, startIndex);
	}

	@Override
	public PaginationSupport getSystemLogs(long portfolioID, int pageSize, int startIndex) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Log.class);

		detachedCriteria.add(Restrictions.eq("PortfolioID", portfolioID));

		detachedCriteria.add(Restrictions.eq("Type", 0));

		return this.findLogs(detachedCriteria, pageSize, startIndex);
	}

	public PaginationSupport getLogs(long portfolioID, int pageSize, int startIndex) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Log.class);

		detachedCriteria.add(Restrictions.eq("PortfolioID", portfolioID));

		return this.findLogs(detachedCriteria, pageSize, startIndex);
	}

	public List<Log> getLogs(long portfolioID, long strategyID) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Log.class);

		detachedCriteria.add(Restrictions.eq("PortfolioID", portfolioID));

		detachedCriteria.add(Restrictions.eq("StrategyID", strategyID));

		return this.getLogs(detachedCriteria);
	}

	public PaginationSupport getLogs(long portfolioID, long strategyID, int pageSize, int startIndex) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Log.class);

		detachedCriteria.add(Restrictions.eq("PortfolioID", portfolioID));

		detachedCriteria.add(Restrictions.eq("StrategyID", strategyID));

		detachedCriteria.add(Restrictions.eq("Type", 1));

		return this.findLogs(detachedCriteria, pageSize, startIndex);
	}

	public List<Log> getLogs(long portfolioID, Date logDate) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Log.class);

		detachedCriteria.add(Restrictions.eq("PortfolioID", portfolioID));

		detachedCriteria.add(Restrictions.eq("Date", logDate));

		return this.getLogs(detachedCriteria);
	}

	public PaginationSupport getLogs(long portfolioID, Date logDate, int pageSize, int startIndex) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Log.class);

		detachedCriteria.add(Restrictions.eq("PortfolioID", portfolioID));

		detachedCriteria.add(Restrictions.eq("Date", logDate));

		return this.findLogs(detachedCriteria, pageSize, startIndex);
	}

	public Log getLog(long portfolioID, long strategyID, Date logDate, int type) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Log.class);

		detachedCriteria.add(Restrictions.eq("PortfolioID", portfolioID));

		detachedCriteria.add(Restrictions.eq("StrategyID", strategyID));

		detachedCriteria.add(Restrictions.eq("LogDate", logDate));

		detachedCriteria.add(Restrictions.eq("Type", type));

		List<Log> list = findByCriteria(detachedCriteria, 1, 0);

		if (list != null && list.size() >= 1)
			return list.get(0);
		else
			return null;
	}

	public Log getUserLog(long portfolioID, long strategyID, Date logDate) {

		return this.getLog(portfolioID, strategyID, logDate, com.lti.system.Configuration.LOG_TYPE_SYSTEM);

	}

	public Log getSystemLog(long portfolioID, long strategyID, Date logDate) {

		return this.getLog(portfolioID, strategyID, logDate, com.lti.system.Configuration.LOG_TYPE_SYSTEM);

	}

	/** *********************************************************** */
	/* ==Log==End */
	/** *********************************************************** */

	// public List<PortfolioDailyData>
	// getNewPortfolioDailyData(List<PortfolioDailyData>
	// pdds,List<SecurityDailyData> sdds){
	@Override
	public void clearLogBeforeDate(Long portfolioid, Date pdate) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Log.class);

		detachedCriteria.add(Restrictions.eq("PortfolioID", portfolioid));

		detachedCriteria.add(Restrictions.lt("LogDate", pdate));

		List lists = findByCriteria(detachedCriteria);

		this.getHibernateTemplate().deleteAll(lists);

	}

	@Override
	public void clearTransactionBeforeDate(Long portfolioid, Date pdate) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Transaction.class);

		detachedCriteria.add(Restrictions.eq("PortfolioID", portfolioid));

		detachedCriteria.add(Restrictions.lt("Date", pdate));

		List lists = findByCriteria(detachedCriteria);

		this.getHibernateTemplate().deleteAll(lists);

	}

	public Date getEarliestDate(long portfolioid) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PortfolioDailyData.class);

		detachedCriteria.add(Restrictions.eq("PortfolioID", portfolioid));

		detachedCriteria.addOrder(Order.asc("Date"));

		List<PortfolioDailyData> bolist = findByCriteria(detachedCriteria, 1, 0);

		if (bolist.size() >= 1)
			return bolist.get(0).getDate();
		else
			return null;
	}

	public Date getLatestDate(long portfolioid) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PortfolioDailyData.class);

		detachedCriteria.add(Restrictions.eq("PortfolioID", portfolioid));

		detachedCriteria.addOrder(Order.desc("Date"));

		List<PortfolioDailyData> bolist = findByCriteria(detachedCriteria, 1, 0);

		if (bolist.size() >= 1)
			return bolist.get(0).getDate();
		else
			return null;
	}

	public Date getTransactionEarliestDate(long portfolioid) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Transaction.class);

		detachedCriteria.add(Restrictions.eq("PortfolioID", portfolioid));

		detachedCriteria.addOrder(Order.asc("Date"));

		List<Transaction> bolist = findByCriteria(detachedCriteria, 1, 0);

		if (bolist.size() >= 1)
			return bolist.get(0).getDate();
		else
			return null;
	}

	public Date getTransactionLatestDate(long portfolioid) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Transaction.class);

		detachedCriteria.add(Restrictions.eq("PortfolioID", portfolioid));

		detachedCriteria.addOrder(Order.desc("Date"));

		List<Transaction> bolist = findByCriteria(detachedCriteria, 1, 0);

		if (bolist.size() >= 1)
			return bolist.get(0).getDate();
		else
			return null;
	}

	public Transaction getLatestTransaction(long portfolioid) {
		return this.getLatestTransaction(portfolioid, null);
	}

	public Transaction getLatestTransaction(long portfolioid, Date CurrentDate) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Transaction.class);
		detachedCriteria.add(Restrictions.eq("PortfolioID", portfolioid));
		if (CurrentDate != null)
			detachedCriteria.add(Restrictions.le("Date", CurrentDate));
		// detachedCriteria.addOrder(Order.desc("Date"));
		detachedCriteria.addOrder(Order.desc("ID"));
		List<Transaction> bolist = findByCriteria(detachedCriteria, 1, 0);

		if (bolist.size() >= 1)
			return bolist.get(0);
		else
			return null;
	}

	public Date getTransactionLatestDateByDate(long portfolioid, Date currentDate) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Transaction.class);

		detachedCriteria.add(Restrictions.eq("PortfolioID", portfolioid));

		detachedCriteria.add(Restrictions.le("Date", currentDate));

		detachedCriteria.addOrder(Order.desc("Date"));

		List<Transaction> bolist = findByCriteria(detachedCriteria, 1, 0);

		if (bolist.size() >= 1)
			return bolist.get(0).getDate();
		else
			return null;
	}

	@Override
	public Date getTransactionLatestDate(long portfolioID, Date date) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Transaction.class);

		detachedCriteria.add(Restrictions.eq("PortfolioID", portfolioID));

		detachedCriteria.add(Restrictions.le("Date", date));

		detachedCriteria.add(Restrictions.eq("TransactionType", Configuration.TRANSACTION_TYPE_REAL));

		detachedCriteria.addOrder(Order.desc("Date"));

		List<Transaction> bolist = findByCriteria(detachedCriteria, 1, 0);

		if (bolist.size() >= 1)
			return bolist.get(0).getDate();
		else
			return null;
	}

	@Override
	public Date getRealTransactionLatestDate(long portfolioID, Date date) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Transaction.class);

		detachedCriteria.add(Restrictions.eq("PortfolioID", portfolioID));

		detachedCriteria.add(Restrictions.le("Date", date));

		detachedCriteria.add(Restrictions.or(Restrictions.eq("TransactionType", Configuration.TRANSACTION_TYPE_REAL), Restrictions.eq("TransactionType", Configuration.TRANSACTION_TYPE_MAIL)));

		detachedCriteria.addOrder(Order.desc("Date"));

		List<Transaction> bolist = findByCriteria(detachedCriteria, 1, 0);

		if (bolist.size() >= 1)
			return bolist.get(0).getDate();
		else
			return null;
	}

	@Override
	public void saveDailyDatas(List<PortfolioDailyData> pdds) {
		if (pdds == null)
			return;

		Session session = null;
		org.hibernate.Transaction tx = null;
		try {
			session = getHibernateTemplate().getSessionFactory().openSession();
			tx = session.beginTransaction();

			for (int i = 0; i < pdds.size(); i++) {
				session.save(pdds.get(i));
				if (i % 2000 == 1999) {
					session.flush();
					session.clear();
					tx.commit();
					tx = session.beginTransaction();
				}
			}
			tx.commit();
			session.close();
		} catch (HibernateException e) {
			throw new RuntimeException("Save Daily datas error", e);
		} finally {
			if (tx != null && tx.isActive())
				tx.commit();
			if (session != null && session.isOpen())
				session.close();
		}

	}

	@Override
	public List<Portfolio> getTopModelPortfolio(Object[] classid, int year, int sort, int size) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PortfolioMPT.class);

		detachedCriteria.add(Restrictions.in("classID", classid));

		detachedCriteria.add(Restrictions.eq("year", year));

		switch (sort) {
		case PortfolioMPT.SORT_BY_ALPHA:
			detachedCriteria.addOrder(Order.desc("alpha"));
			break;
		case PortfolioMPT.SORT_BY_AR:
			detachedCriteria.addOrder(Order.desc("AR"));
			break;
		case PortfolioMPT.SORT_BY_BETA:
			detachedCriteria.addOrder(Order.desc("beta"));
			break;
		case PortfolioMPT.SORT_BY_DRAWDOWN:
			detachedCriteria.addOrder(Order.desc("drawDown"));
			break;
		case PortfolioMPT.SORT_BY_RSQUARED:
			detachedCriteria.addOrder(Order.desc("RSquared"));
			break;
		case PortfolioMPT.SORT_BY_SHARPERATIO:
			detachedCriteria.addOrder(Order.desc("sharpeRatio"));
			break;
		case PortfolioMPT.SORT_BY_STANDARDDEVIATION:
			detachedCriteria.addOrder(Order.desc("standardDeviation"));
			break;
		case PortfolioMPT.SORT_BY_TREYNORRATIO:
			detachedCriteria.addOrder(Order.desc("treynorRatio"));
			break;
		default:
			break;
		}

		List<PortfolioMPT> mpts = findByCriteria(detachedCriteria, size, 0);

		if (mpts != null && mpts.size() >= 1) {
			List<Portfolio> portfolios = new ArrayList<Portfolio>();
			for (int i = 0; i < mpts.size(); i++) {
				Portfolio p = get(mpts.get(i).getPortfolioID());
				if (p != null)
					portfolios.add(p);
			}
			return portfolios;
		}
		return null;
	}

	@Override
	public List<Portfolio> getTopModelPortfolio(long strategyid, int year, int sort, int size) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PortfolioMPT.class);

		detachedCriteria.add(Restrictions.eq("strategyID", strategyid));

		detachedCriteria.add(Restrictions.eq("year", year));

		switch (sort) {
		case PortfolioMPT.SORT_BY_ALPHA:
			detachedCriteria.addOrder(Order.desc("alpha"));
			break;
		case PortfolioMPT.SORT_BY_AR:
			detachedCriteria.addOrder(Order.desc("AR"));
			break;
		case PortfolioMPT.SORT_BY_BETA:
			detachedCriteria.addOrder(Order.desc("beta"));
			break;
		case PortfolioMPT.SORT_BY_DRAWDOWN:
			detachedCriteria.addOrder(Order.desc("drawDown"));
			break;
		case PortfolioMPT.SORT_BY_RSQUARED:
			detachedCriteria.addOrder(Order.desc("RSquared"));
			break;
		case PortfolioMPT.SORT_BY_SHARPERATIO:
			detachedCriteria.addOrder(Order.desc("sharpeRatio"));
			break;
		case PortfolioMPT.SORT_BY_STANDARDDEVIATION:
			detachedCriteria.addOrder(Order.desc("standardDeviation"));
			break;
		case PortfolioMPT.SORT_BY_TREYNORRATIO:
			detachedCriteria.addOrder(Order.desc("treynorRatio"));
			break;
		default:
			break;
		}
		List<PortfolioMPT> mpts;
		if (size != 0) {
			mpts = findByCriteria(detachedCriteria, size, 0);
		} else {
			mpts = findByCriteria(detachedCriteria);
		}

		if (mpts != null && mpts.size() >= 1) {
			List<Portfolio> portfolios = new ArrayList<Portfolio>();
			for (int i = 0; i < mpts.size(); i++) {
				Portfolio p = get(mpts.get(i).getPortfolioID());
				if (p != null)
					portfolios.add(p);
			}
			return portfolios;
		}
		return null;
	}

	@Override
	public List<PortfolioMPT> getEveryYearsMPT(long portfolioID) {
		List<PortfolioMPT> pmpt = new ArrayList<PortfolioMPT>();

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PortfolioMPT.class);

		detachedCriteria.add(Restrictions.eq("portfolioID", portfolioID));

		detachedCriteria.add(Restrictions.isNotNull("year"));

		detachedCriteria.addOrder(Order.asc("year"));

		pmpt = findByCriteria(detachedCriteria);

		return pmpt;
	}

	@Override
	public PortfolioMPT getPortfolioMPT(long portfolioID, int year) {
		List<PortfolioMPT> pmpt = new ArrayList<PortfolioMPT>();

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PortfolioMPT.class);

		detachedCriteria.add(Restrictions.eq("portfolioID", portfolioID));

		detachedCriteria.add(Restrictions.eq("year", year));

		pmpt = findByCriteria(detachedCriteria, 1, 0);

		if (pmpt.size() == 0)
			return null;

		return pmpt.get(0);
	}

	public List<PortfolioMPT> getPortfolioMPTsByYearArray(long portfolioID, List<Integer> years) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PortfolioMPT.class);
		detachedCriteria.add(Restrictions.eq("portfolioID", portfolioID));
		detachedCriteria.add(Restrictions.in("year", years));
		detachedCriteria.add(Restrictions.isNotNull("AR"));
		detachedCriteria.addOrder(Order.asc("year"));
		List<PortfolioMPT> pmpts = findByCriteria(detachedCriteria);
		return pmpts;
	}

	public List<PortfolioMPT> getPortfolioMPTsByYear(List<Long> portIDs, int year) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PortfolioMPT.class);
		detachedCriteria.add(Restrictions.in("portfolioID", portIDs));
		detachedCriteria.add(Restrictions.eq("year", year));
		detachedCriteria.add(Restrictions.isNotNull("AR"));
		detachedCriteria.add(Restrictions.isNotNull("sortinoRatio"));
		detachedCriteria.add(Restrictions.isNotNull("sharpeRatio"));
		detachedCriteria.add(Restrictions.isNotNull("treynorRatio"));
		detachedCriteria.add(Restrictions.isNotNull("drawDown"));
		detachedCriteria.addOrder(Order.asc("portfolioID"));
		List<PortfolioMPT> pmpts = findByCriteria(detachedCriteria);
		return pmpts;
	}

	@Override
	public List<PortfolioMPT> getPortfolioMPTs(int year, int sort) {
		List<PortfolioMPT> pmpt = new ArrayList<PortfolioMPT>();

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PortfolioMPT.class);

		detachedCriteria.add(Restrictions.eq("year", year));

		switch (sort) {
		case PortfolioMPT.SORT_BY_ALPHA:
			detachedCriteria.addOrder(Order.desc("alpha"));
			break;
		case PortfolioMPT.SORT_BY_AR:
			detachedCriteria.addOrder(Order.desc("AR"));
			break;
		case PortfolioMPT.SORT_BY_BETA:
			detachedCriteria.addOrder(Order.desc("beta"));
			break;
		case PortfolioMPT.SORT_BY_DRAWDOWN:
			detachedCriteria.addOrder(Order.desc("drawDown"));
			break;
		case PortfolioMPT.SORT_BY_RSQUARED:
			detachedCriteria.addOrder(Order.desc("RSquared"));
			break;
		case PortfolioMPT.SORT_BY_SHARPERATIO:
			detachedCriteria.addOrder(Order.desc("sharpeRatio"));
			break;
		case PortfolioMPT.SORT_BY_STANDARDDEVIATION:
			detachedCriteria.addOrder(Order.desc("standardDeviation"));
			break;
		case PortfolioMPT.SORT_BY_TREYNORRATIO:
			detachedCriteria.addOrder(Order.desc("treynorRatio"));
			break;
		case PortfolioMPT.SORT_BY_YEAR:
			detachedCriteria.addOrder(Order.desc("year"));
			break;
		default:
			break;
		}

		pmpt = findByCriteria(detachedCriteria);

		return pmpt;
	}

	@Override
	public void clearPortfolioMPT(long generatedPortfolioID) throws Exception {
		// TODO Auto-generated method stub

		executeSQL("delete from " + Configuration.TABLE_PORTFOLIO_MPT + " where portfolioid=" + generatedPortfolioID);

	}

	@Override
	public void saveLogs(List<Log> logs) {
		// TODO Auto-generated method stub
		if (logs == null)
			return;
		Session session = null;
		org.hibernate.Transaction tx = null;
		try {
			session = getHibernateTemplate().getSessionFactory().openSession();
			tx = session.beginTransaction();

			for (int i = 0; i < logs.size(); i++) {
				session.save(logs.get(i));
				if (i % 2000 == 1999) {
					session.flush();
					session.clear();
					tx.commit();
					tx = session.beginTransaction();
				}
			}
			tx.commit();
			session.close();
		} catch (Exception e) {
			throw new RuntimeException("saveLogs sql error.", e);
		} finally {
			if (tx != null && tx.isActive()) {
				try {
					tx.commit();
				} catch (Exception e) {
				}
			}
			if (session != null && session.isOpen())
				session.close();
		}

	}

	@Override
	public void saveTransactions(List<Transaction> transactions) {
		// TODO Auto-generated method stub
		if (transactions == null)
			return;
		Session session = null;
		org.hibernate.Transaction tx = null;
		try {
			session = getHibernateTemplate().getSessionFactory().openSession();
			tx = session.beginTransaction();

			for (int i = 0; i < transactions.size(); i++) {
				session.save(transactions.get(i));
				if (i % 2000 == 0) {
					session.flush();
					session.clear();
					tx.commit();
					tx = session.beginTransaction();
				}
			}
			tx.commit();
			session.close();
		} catch (Exception e) {
			throw new RuntimeException("saveTransactions sql error.", e);
		} finally {
			if (session != null && session.isOpen())
				session.close();
		}

	}

	public List<Transaction> getCashFlowTransaction(Long id, Date startDate, Date endDate) {

		List<Transaction> tranList = new ArrayList<Transaction>();

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Transaction.class);

		detachedCriteria.add(Restrictions.eq("PortfolioID", id));

		detachedCriteria.add(Restrictions.or(Restrictions.eq("Operation", Configuration.DEPOSIT), Restrictions.eq("Operation", Configuration.WITHDRAW)));

		detachedCriteria.add(Restrictions.ge("Date", startDate));

		detachedCriteria.add(Restrictions.le("Date", endDate));

		detachedCriteria.addOrder(Order.asc("Date"));

		tranList = findByCriteria(detachedCriteria);

		return tranList;

	}

	@Override
	public void saveMPTs(List<PortfolioMPT> pmpts) {
		// TODO Auto-generated method stub
		super.saveAll(pmpts);
	}

	@Override
	public long updatePortfolioMPT(PortfolioMPT mpt) {
		getHibernateTemplate().update(mpt);
		return mpt.getID();
	}

	public List<ConfidenceCheck> checkConfidence(Long portfolioID, Long strategyID, boolean strategy) {
		List<ConfidenceCheck> cc = new ArrayList<ConfidenceCheck>();
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ConfidenceCheck.class);
		if (strategy) {
			detachedCriteria.add(Restrictions.eq("StrategyID", strategyID));
			detachedCriteria.add(Restrictions.eq("PortfolioID", portfolioID));
		} else if (strategyID == -1L) {
			detachedCriteria.add(Restrictions.eq("PortfolioID", portfolioID));
		} else {
			detachedCriteria.add(Restrictions.eq("PortfolioID", portfolioID));
			detachedCriteria.add(Restrictions.isNull("StrategyID"));
		}
		cc = findByCriteria(detachedCriteria);
		return cc;
	}

	@Override
	public void saveConfidenceCheck(ConfidenceCheck cc) {
		List<ConfidenceCheck> ccList = new ArrayList<ConfidenceCheck>();
		ccList.add(cc);
		super.saveOrUpdateAll(ccList);
	}

	@Override
	public List<Portfolio> getCurrentPortfolios() {
		// TODO Auto-generated method stub
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Portfolio.class);
		List<Portfolio> bolist = findByCriteria(detachedCriteria);
		return bolist;
	}

	@Override
	public List<PortfolioState> getPortfolioStates() {
		// TODO Auto-generated method stub
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PortfolioState.class);

		List<PortfolioState> pses = findByCriteria(detachedCriteria);

		return pses;
	}

	@Override
	public List<Log> getTailLogs(long portfolioID, int count) {
		// TODO Auto-generated method stub
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Log.class);

		detachedCriteria.add(Restrictions.eq("PortfolioID", portfolioID));

		detachedCriteria.addOrder(Order.desc("LogDate"));

		return findByCriteria(detachedCriteria, count, 0);
	}

	@Override
	public Portfolio getBasicPortfolio(Long id) {
		// TODO Auto-generated method stub
		String hql = "select new Portfolio(p.ID,p.Name,p.Type,p.State,p.EndDate) from Portfolio p where p.ID=" + id;
		List<Portfolio> pos = super.findByHQL(hql);
		if (pos != null && pos.size() > 0)
			return pos.get(0);
		else
			return null;
	}

	public List<Portfolio> getAllBasciPortfolio() {
		String hql = "select new Portfolio(p.ID,p.Name,p.Type,p.State,p.EndDate,p.Strategies) from Portfolio p";
		List<Portfolio> pos = super.findByHQL(hql);
		if (pos != null && pos.size() > 0)
			return pos;
		else
			return null;
	}

	/**
	 * @author CCD
	 */
	public List<Double> getPortfolioDailyAmount(long portfolioID) {
		String sql = "select amount from ltisystem_portfoliodailydata where portfolioid = " + portfolioID + " order by date asc";
		try {
			List<Double> amountList = super.findBySQL(sql);
			return amountList;
		} catch (Exception e) {
		}
		return null;
	}

	public Double getPortfolioAmountByDate(long portfolioID, String date) {
		String sql = "select amount from ltisystem_portfoliodailydata where portfolioid = " + portfolioID + " and date like '%" + date + "%'";
		try {
			List<Double> amountList = super.findBySQL(sql);
			Double amount = amountList.get(0);
			return amount;
		} catch (Exception e) {
		}
		return null;
	}

	@Override
	public List<Transaction> getTransactions(String portfolioname, long securityid, long userid, Date startDate, Date endDate, int size, String orderBy) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		UserManager userManager = ContextHolder.getUserManager();
		String sql = "SELECT p.name,t.* FROM ltisystem_transaction t left join ltisystem_portfolio p on t.portfolioid=p.id where t.securityid=" + securityid;
		if (portfolioname != null && !portfolioname.equals(""))
			sql += " and p.name like '%" + portfolioname + "%'";
		// if(userid>0){
		// sql += " and (p.userid="+userid+" or p.ispublic=1)";
		// sql += " and p.userid="+userid;
		// }
		if (startDate != null)
			sql += " and t.Date >= '" + sdf.format(startDate) + "'";
		if (endDate != null)
			sql += " and t.Date <= '" + sdf.format(endDate) + "'";
		if (orderBy != null && orderBy.equalsIgnoreCase("date")) {
			sql += " order by t.Date desc";
		}
		// if(size>0)sql +=" limit 0,"+size;

		System.out.println(sql);
		Session session = null;
		try {
			session = getHibernateTemplate().getSessionFactory().openSession();
			SQLQuery query = session.createSQLQuery(sql);
			query.addScalar("Name", Hibernate.STRING);
			query.addScalar("ID", Hibernate.LONG);
			query.addScalar("Operation", Hibernate.STRING);
			query.addScalar("Date", Hibernate.DATE);
			query.addScalar("Amount", Hibernate.DOUBLE);
			query.addScalar("PortfolioID", Hibernate.LONG);
			query.addScalar("SecurityID", Hibernate.LONG);
			query.addScalar("AssetName", Hibernate.STRING);
			query.addScalar("StrategyID", Hibernate.LONG);
			query.addScalar("Percentage", Hibernate.DOUBLE);

			List<Object[]> results = query.list();
			int count = 1;
			if (results != null && results.size() > 0) {
				List<Transaction> trs = new ArrayList<Transaction>(results.size());
				for (int i = 0; i < results.size(); i++) {
					String name = (String) results.get(i)[0];
					if (size > 0 && existsInTransactions(name, trs) == true)
						continue;
					Transaction t = new Transaction();
					t.setPortfolioName(name);
					t.setID((Long) results.get(i)[1]);
					t.setOperation((String) results.get(i)[2]);
					t.setDate((Date) results.get(i)[3]);
					t.setAmount((Double) results.get(i)[4]);
					t.setPortfolioID((Long) results.get(i)[5]);
					t.setSecurityID((Long) results.get(i)[6]);
					t.setAssetName((String) results.get(i)[7]);
					t.setStrategyID((Long) results.get(i)[8]);
					t.setPercentage((Double) results.get(i)[9]);
					// boolean delayed =
					// userManager.HaveRole(Configuration.ROLE_PORTFOLIO_DELAYED,
					// userid, t.getPortfolioID(),
					// Configuration.RESOURCE_TYPE_PORTFOLIO);
					// boolean isOwner=tuserManager.IsOwner(userid,
					// t.getPortfolioID(),
					// Configuration.RESOURCE_TYPE_PORTFOLIO);
					// if (delayed==true||isOwner==true){
					trs.add(t);
					count++;
					// }
					if (size > 0 && count >= size)
						break;
				}
				return trs;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen())
				session.close();
		}

		return null;

	}

	private boolean existsInTransactions(String name, List<Transaction> trs) {
		if (trs == null)
			return false;
		for (int i = 0; i < trs.size(); i++) {
			Transaction t = trs.get(i);
			if (t.getPortfolioName().equals(name))
				return true;
		}
		return false;
	}

	/***
	 * @author CCD change every year according to ranking
	 * @param assetclassname
	 * @param ranking
	 * @param interval
	 * @param enddate
	 * @return return the security id list whose ranking is ranking.
	 */
	public List<Long> createStarPortfolio(String assetclassname, int ranking, int interval, Date enddate) {
		AssetClassManager assetManager = (AssetClassManager) ContextHolder.getInstance().getApplicationContext().getBean("assetClassManager");
		Long assetClassID = assetManager.get(assetclassname).getID();
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(SecurityRanking.class);
		detachedCriteria.add(Restrictions.eq("AssetClassID", assetClassID));
		detachedCriteria.add(Restrictions.eq("Ranking", ranking));
		detachedCriteria.add(Restrictions.eq("Interval", interval));
		detachedCriteria.add(Restrictions.eq("EndDate", enddate));
		Projection projection = Property.forName("SecurityID");
		detachedCriteria.setProjection(projection);
		List<Long> list = findByCriteria(detachedCriteria);
		return list;
	}

	/**
	 * @author CCD
	 * @param portfolioID
	 *            current portfolio's ID
	 * @param updateMode
	 *            update the portfolio Mode of both the current and original
	 */
	public void updatePortfolioMode(Long portfolioID, int updateMode) {
	}

	/**
	 * 此函数需要重写，请注意todo部分
	 * 
	 * @author CCD
	 * @param securityID
	 * @param dividend
	 * @param dDate
	 *            modified by CCD on 2010-03-12
	 */
	@Override
	@Deprecated
	public boolean doOneDividendAdjustment(List<Portfolio> portfolioList, Long securityID, List<SecurityDailyData> priceList, double dividend, double price, Date dDate) {

		PortfolioHoldingManager phm = (PortfolioHoldingManager) ContextHolder.getInstance().getApplicationContext().getBean("portfolioHoldingManager");
		boolean result = true;
		double successCount = 0, failCount = 0;
		boolean doReinvest = false;
		for (int i = 0; i < portfolioList.size(); ++i) {
			try {
				double amountDelta = 0;
				Date lastDate = null;
				Portfolio portfolio = portfolioList.get(i);
				Long portfolioID = portfolio.getID();
				HoldingItem holdItem = phm.getLatestHoldingItemsBeforeBySecurityID(portfolioID, securityID, dDate);
				if (holdItem != null) {
					String assetName = holdItem.getAssetName();
					lastDate = holdItem.getDate();
					double oShare = holdItem.getShare();
					amountDelta = oShare * dividend;
					double shareDelta = amountDelta / price;
					boolean reInvest = holdItem.getReInvest();
					if (holdItem.getLastDividendDate() == null || LTIDate.after(dDate, holdItem.getLastDividendDate())) {
						if (oShare > 0 && reInvest) {
							if (portfolio.holdSecurity(securityID)) {// now we
																		// hold
																		// the
																		// security
								List<HoldingItem> holdingItemList = phm.getAllHoldItemAfter(portfolioID, securityID, lastDate);
								if (holdingItemList != null && holdingItemList.size() > 0) {
									for (int j = 0; j < holdingItemList.size(); ++j) {
										HoldingItem hi = holdingItemList.get(j);
										hi.setShare(hi.getShare() + shareDelta);
									}
									this.saveOrUpdateAll(holdingItemList);
								}
								// TODO:hereeeeeeeeeeeeeeeeeeeeeee
								Transaction transaction = null;// portfolio.createTransaction(portfolio.getCurrentStrategyID(),
																// portfolioID,
																// securityID,
																// dDate,
																// holdItem.getAssetName(),
																// Configuration.TRANSACTION_REINVEST,
																// amountDelta,
																// amountDelta,
																// shareDelta,
																// Configuration.TRANSACTION_TYPE_REINVEST);
								this.addTransaction(transaction);
								// TODO:hereeeeeeeeeeeeeeeeeeeeeee
								// portfolio.updateHolding(assetName,
								// securityID, shareDelta);
								doReinvest = true;
							}
						} else {
							// TODO:hereeeeeeeeeeeeeeeeeeeeeee
							// portfolio.setCash(portfolio.getCash() +
							// amountDelta);
						}
					} else
						continue;

					// update current holding for the portfolio
					this.update(portfolio);

					/************************ Update Holding Item ****************************/
					holdItem.setLastDividendDate(dDate);
					phm.updateHoldingItem(holdItem);
					/*********** Update portfolio Daily Data, add amountDelta to amount ************/
					DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PortfolioDailyData.class);
					detachedCriteria.add(Restrictions.eq("PortfolioID", portfolioID));
					detachedCriteria.add(Restrictions.gt("Date", dDate));
					List<PortfolioDailyData> pdds = this.getDailydatas(detachedCriteria);
					int size = pdds.size();
					if (pdds.size() > priceList.size())
						size = priceList.size();
					if (pdds != null) {
						for (int j = 0; j < size; ++j) {
							PortfolioDailyData pdd = pdds.get(j);
							if (doReinvest) {
								pdd.setAmount(pdd.getAmount() + shareDelta * priceList.get(j).getClose());
							} else
								pdd.setAmount(pdd.getAmount() + amountDelta);
						}
						this.saveOrUpdateAll(pdds);
					}
					++successCount;
					// log.info("Dividend Adjustment: securityID: " + securityID
					// + "  portfolioID: " + portfolioID);
				}
			} catch (Exception e) {
				++failCount;
				result = false;
				continue;
			}
		}
		// log.info("Dividend Adjustment for securityID: " + securityID +
		// "\n  Success: " + successCount + "\n  Fail: " + failCount);
		return result;
	}

	@Override
	/**
	 * @author CCD
	 * added by CCD on 2010-03-12
	 */
	public void checkDividendAdjustment() {
		SecurityManager sm = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
		// DetachedCriteria detachedCriteria =
		// DetachedCriteria.forClass(Security.class);
		// detachedCriteria.add(Restrictions.isNotNull("NewDividendDate"));
		// List<Security> securityList = findByCriteria(detachedCriteria);
		List<Security> securityList = new ArrayList<Security>();
		securityList.add(sm.get(9652L));
		Date today = new Date();
		// List<Portfolio> portfolioList = this.getMonitorPortfolios();
		List<Portfolio> portfolioList = new ArrayList<Portfolio>();
		Portfolio p = this.get(10067L);
		portfolioList.add(p);
		long start = System.currentTimeMillis();
		if (securityList != null && securityList.size() > 0) {
			Security se = null;
			Date dDate = null;
			Long securityID = null;
			for (int i = 0; i < securityList.size(); ++i) {
				try {
					se = securityList.get(i);
					dDate = se.getNewDividendDate();
					securityID = se.getID();
					List<SecurityDailyData> sdds = sm.getDividendList(securityID, dDate);
					DetachedCriteria detachedCriteria1 = DetachedCriteria.forClass(SecurityDailyData.class);
					detachedCriteria1.add(Restrictions.eq("SecurityID", securityID));
					detachedCriteria1.add(Restrictions.gt("Date", dDate));
					List<SecurityDailyData> priceList = sm.getDailydatas(detachedCriteria1);
					boolean result = true;
					int curSize = 0;
					for (int j = 0; j < sdds.size(); ++j) {

						SecurityDailyData sdd = sdds.get(j);
						double dividend = sdd.getDividend();
						double price = sdd.getClose();
						dDate = sdd.getDate();
						System.out.println("Dividend Adjustment: " + se.getSymbol() + "  date: " + sdd.getDate() + "  dividend: " + dividend);
						// log.info("Dividend Adjustment: " + se.getSymbol() +
						// "  date: " + sdd.getDate() + "  dividend: " +
						// dividend);
						result = doOneDividendAdjustment(portfolioList, securityID, priceList, dividend, price, dDate);
						if (j == curSize && result) {
							if (j + 1 < sdds.size())
								se.setNewDividendDate(sdds.get(j + 1).getDate());
							else
								se.setNewDividendDate(null);
							sm.updateNewDividendDate(se);
							++curSize;
						}
					}
				} catch (Exception e) {
					System.out.println("Dividend Adjustment for SecurityID: " + securityID + "Fail");
					// log.info("Dividend Adjustment for SecurityID: " +
					// securityID + "Fail");
					e.printStackTrace();
					continue;
				}
			}
		}
		long end = System.currentTimeMillis();
		System.out.println("Cost Time: " + (end - start));
	}

	public PortfolioFollowDate getPortfolioFollowDate(Long portfolioID) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PortfolioFollowDate.class);
		detachedCriteria.add(Restrictions.eq("PortfolioID", portfolioID));
		List<PortfolioFollowDate> pfdList = findByCriteria(detachedCriteria);
		if (pfdList != null && pfdList.size() > 0)
			return pfdList.get(0);
		return null;
	}

	public List<PortfolioFollowDate> getPortfolioFollowDates() {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PortfolioFollowDate.class);
		return findByCriteria(detachedCriteria);
	}

	public void saveOrUpdatePortfolioFollowDate(PortfolioFollowDate pfd) {
		getHibernateTemplate().saveOrUpdate(pfd);
	}

	public String getPortfolioLastFollowDate(Long portfolioID) {
		PortfolioFollowDate pfd = this.getPortfolioFollowDate(portfolioID);
		if (pfd != null) {
			String[] fds = pfd.getDateString().split("#");
			return fds[fds.length - 1];
		}
		return null;
	}

	public List<Date> getPortfolioFollowDatesByID(Long portfolioID) {
		PortfolioFollowDate pfd = this.getPortfolioFollowDate(portfolioID);
		List<Date> dateList = new ArrayList<Date>();
		if (pfd != null) {
			String dateStr = pfd.getDateString();
			String[] dateStrs = dateStr.split("#");
			for (int i = 0; i < dateStrs.length; ++i) {
				Date date = LTIDate.parseStringToDate(dateStrs[i]);
				dateList.add(date);
			}
			return dateList;
		}
		return null;
	}

	// TODO:需要检查一下symbol相关
	public void changeTransaction(Long portfolioID, Date curDate) {
		SecurityManager securityManager = ContextHolder.getSecurityManager();
		PortfolioHoldingManager phm = (PortfolioHoldingManager) ContextHolder.getInstance().getApplicationContext().getBean("portfolioHoldingManager");
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Transaction.class);
		detachedCriteria.add(Restrictions.eq("PortfolioID", portfolioID));
		detachedCriteria.add(Restrictions.eq("IsIgnore", false));
		List<Transaction> transactionList = this.getTransactions(detachedCriteria);
		List<Transaction> updateList = new ArrayList<Transaction>();
		if (transactionList != null && transactionList.size() > 0) {
			for (int i = 0; i < transactionList.size(); ++i) {
				Transaction t = transactionList.get(i);
				t.setIsIgnore(true);
				updateList.add(t);
			}
		}
		curDate = LTIDate.clearHMSM(curDate);
		PortfolioInf pif = this.getPortfolioInf(portfolioID, Configuration.PORTFOLIO_HOLDING_CURRENT);
		if (pif != null) {
			HoldingInf hinf = pif.getHolding();
			curDate = hinf.getCurrentDate();
			List<HoldingItem> holdingItemList = hinf.getHoldingItems();
			if (holdingItemList != null && holdingItemList.size() > 0) {
				for (HoldingItem hi : holdingItemList) {
					Transaction t = new Transaction();
					t.setPortfolioID(portfolioID);
					t.setAssetName(hi.getAssetName());
					t.setDate(curDate);
					t.setSecurityID(hi.getSecurityID());
					t.setSymbol(hi.getSymbol());
					t.setShare(hi.getShare());
					double price = 0;
					try {
						price = securityManager.getPrice(hi.getSecurityID(), curDate);
					} catch (NoPriceException e) {
					}
					t.setAmount(price * hi.getShare());
					t.setOperation(Configuration.TRANSACTION_VIRTUAL);
					t.setTransactionType(Configuration.TRANSACTION_TYPE_VIRTUAL);
					t.setIsIgnore(false);
					updateList.add(t);
				}
			}
			this.saveOrUpdateAll(updateList);
		}
	}

	@Override
	public HoldingInf getHolding(long portfolioID, long portfolioHoldingOriginal) {
		PortfolioInf pi = this.getPortfolioInf(portfolioID, portfolioHoldingOriginal);
		if (pi == null)
			return null;
		else
			return pi.getHolding();
	}

	@Override
	public PortfolioInf getPortfolioInf(long portfolioID, long portfolioHoldingOriginal) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PortfolioInf.class);
		detachedCriteria.add(Restrictions.eq("PortfolioID", portfolioID));
		detachedCriteria.add(Restrictions.eq("Type", portfolioHoldingOriginal));
		List<com.lti.service.bo.PortfolioInf> bolist = findByCriteria(detachedCriteria, 1, 0);
		if (bolist.size() >= 1)
			return bolist.get(0);
		else
			return null;
	}

	@Override
	public List<PortfolioInf> getPortfolioInfs(long portfolioHoldingType) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PortfolioInf.class);
		detachedCriteria.add(Restrictions.eq("Type", portfolioHoldingType));
		return findByCriteria(detachedCriteria);
	}

	@Override
	public void savePortfolioInf(PortfolioInf portfolioInf) {
		PortfolioInf db = this.getPortfolioInf(portfolioInf.getPortfolioID(), portfolioInf.getType());
		if (db == null) {
			portfolioInf.setID(null);
			getHibernateTemplate().save(portfolioInf);
		} else {
			portfolioInf.setID(db.getID());
			getHibernateTemplate().update(portfolioInf);
		}
	}

	@Override
	public List<Portfolio> getModelPortfolios(long strategyid) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Portfolio.class);

		detachedCriteria.add(Restrictions.eq("MainStrategyID", strategyid));

		return findByCriteria(detachedCriteria);
	}

	@Override
	public CachePortfolioItem getCachePortfolioItem(long groupid, long roleid, long portfolioid) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(CachePortfolioItem.class);
		detachedCriteria.add(Restrictions.eq("PortfolioID", portfolioid));
		detachedCriteria.add(Restrictions.eq("GroupID", groupid));
		detachedCriteria.add(Restrictions.eq("RoleID", roleid));
		List<CachePortfolioItem> bolist = findByCriteria(detachedCriteria, 1, 0);
		if (bolist.size() >= 1)
			return bolist.get(0);
		else
			return null;
	}

	@Override
	public List<CachePortfolioItem> getCachePortfolioItems(long portfolioid) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(CachePortfolioItem.class);
		detachedCriteria.add(Restrictions.eq("PortfolioID", portfolioid));
		// detachedCriteria.add(Restrictions.ne("GroupID", 0l));
		List<CachePortfolioItem> pfdList = findByCriteria(detachedCriteria);
		return pfdList;
	}

	@Override
	public void updateGlobalObject(GlobalObject go) {
		getHibernateTemplate().saveOrUpdate(go);
	}

	@Override
	public GlobalObject getGlobalObject(String key) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(GlobalObject.class);
		detachedCriteria.add(Restrictions.eq("Key", key));
		List<GlobalObject> bolist = findByCriteria(detachedCriteria, 1, 0);
		if (bolist.size() >= 1)
			return bolist.get(0);
		else
			return null;
	}

	@Override
	public long getBenchmarkID(long id) {
		PortfolioInf pif = this.getPortfolioInf(id, Configuration.PORTFOLIO_HOLDING_ORIGINAL);
		return pif.getHolding().getBenchmarkID();
	}

	@Override
	public HoldingInf getOriginalHolding(long id) {
		return this.getPortfolioInf(id, Configuration.PORTFOLIO_HOLDING_ORIGINAL).getHolding();
	}

	@Override
	public List<HoldingRecord> getHoldingRecords(Long portfolioID) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(HoldingRecord.class);
		detachedCriteria.add(Restrictions.eq("PortfolioID", portfolioID));
		return this.findByCriteria(detachedCriteria);
	}

	@Override
	public void setHoldingRecord(Long securityID, Date dividendDate) {
		String dividendDateStr = LTIDate.parseDateToString(dividendDate);
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(HoldingRecord.class);
		detachedCriteria.add(Restrictions.eq("SecurityID", securityID));
		detachedCriteria.add(Restrictions.lt("StartDate", dividendDate));
		detachedCriteria.add(Restrictions.or(Restrictions.isNull("EndDate"), Restrictions.ge("EndDate", dividendDate)));
		List<HoldingRecord> hrList = this.findByCriteria(detachedCriteria);
		List<HoldingRecord> newHrList = new ArrayList<HoldingRecord>();
		if (hrList != null && hrList.size() > 0) {
			for (HoldingRecord hr : hrList) {
				String dDateStr = hr.getDividendDateStr();
				if (dDateStr == null || dDateStr.equals("")) {
					hr.setDividendDateStr(dividendDateStr);
					newHrList.add(hr);
				} else {
					String[] dDateArray = dDateStr.split(",");
					if (!dividendDateStr.equals(dDateArray[dDateArray.length - 1])) {
						dDateStr += "," + dividendDateStr;
						hr.setDividendDateStr(dDateStr);
						newHrList.add(hr);
					}
				}
			}
		}
		if (newHrList.size() > 0)
			this.saveOrUpdateAll(newHrList);
	}

	@Override
	public boolean hasHoldingRecord(Long portfolioID) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(HoldingRecord.class);
		detachedCriteria.add(Restrictions.eq("PortfolioID", portfolioID));
		List<HoldingRecord> hrList = this.findByCriteria(detachedCriteria, 1, 0);
		if (hrList != null && hrList.size() > 0)
			return true;
		return false;
	}

	@Override
	public void deletePortfolioPerformance() throws Exception {
		executeSQL("delete from " + Configuration.TABLE_PORTFOLIOPERFORMANCE);
	}

	@Override
	public void deleteDailyData(Long ID) throws Exception {
		executeSQL("delete from " + Configuration.TABLE_PORTFOLIO_DAILY_DATA + " where id = " + ID);
	}

	@Override
	public void deleteDuplicateDailyDataAfterIDBeforeDate(Long portfolioID, Long ID, Date date) throws Exception {
		executeSQL("delete from " + Configuration.TABLE_PORTFOLIO_DAILY_DATA + " where portfolioID = " + portfolioID + " and id > " + ID + " and date<= \'" + date + "\'");
	}

	@Override
	public void deleteDuplicateDailyDataByPortfolioIDAndID(Long portfolioID, Long ID) throws Exception {
		executeSQL("delete from " + Configuration.TABLE_PORTFOLIO_DAILY_DATA + " portfolioID = " + portfolioID + " and id < " + ID);
	}

	@Override
	public void deletePortfolioPerformanceByPlanID(long planID) throws Exception {
		executeSQL("delete from " + Configuration.TABLE_PORTFOLIOPERFORMANCE + " where planid = " + planID);
	}

	@Override
	public void saveAllPortfolioPerformance(List<PortfolioPerformance> ppList) {
		this.saveAll(ppList);
	}

	@Override
	public void removeDailyDatas(List<PortfolioDailyData> pdds) {
		this.removeAll(pdds);
	}

	public void saveOrUpdate(Session session, List objs) {
		if (objs == null || objs.size() == 0)
			return;
		for (int i = 0; i < objs.size(); i++) {
			session.saveOrUpdate(((List) objs).get(i));
		}
	}

	public void update(Session session, List objs) {
		if (objs == null || objs.size() == 0)
			return;
		for (int i = 0; i < objs.size(); i++) {
			session.save(((List) objs).get(i));
		}
	}

	public void executeUpdate(Session session, List<String> sqls) {
		for (int i = 0; i < sqls.size(); i++) {
			Query query = session.createSQLQuery(sqls.get(i));
			query.executeUpdate();
		}
	}

	private List<String> createClearStatements(Long portfolioID) {
		List<String> sqls = new ArrayList<String>();
		sqls.add("delete from " + Configuration.TABLE_LOG + " where portfolioid=" + portfolioID);
		sqls.add("delete from " + Configuration.TABLE_PORTFOLIO_DAILY_DATA + " where portfolioid=" + portfolioID);
		sqls.add("delete from " + Configuration.TABLE_PORTFOLIO_MPT + " where portfolioid=" + portfolioID);
		sqls.add("delete from " + Configuration.TABLE_TRANACTION + " where portfolioid=" + portfolioID);
		sqls.add("delete from " + Configuration.TABLE_PORTFOLIO_HOLDINGS + " where portfolioid=" + portfolioID);
		sqls.add("delete from " + Configuration.TABLE_PORTFOLIO_HOLDINGITEM + " where portfolioid=" + portfolioID);
		sqls.add("delete from " + Configuration.TABLE_HOLDINGRECORD + " where portfolioid=" + portfolioID);
		sqls.add("delete from " + Configuration.TABLE_PORTFOLIOFOLLOWDATE + " where portfolioid=" + portfolioID);
		return sqls;
	}

	@Override
	public void savePortfolioInformation(PortfolioInfo portfolioInfo) {
		HibernateDaoSupport hds = (HibernateDaoSupport) this;
		Session session = null;
		org.hibernate.Transaction tx = null;
		try {
			session = hds.getSessionFactory().openSession();
			tx = session.beginTransaction();
			if (portfolioInfo.getMode() == 0 /* Simulator.MONITOR */) {
				executeUpdate(session, createClearStatements(portfolioInfo.getPortfolio().getID()));
			}

			session.update(portfolioInfo.getPortfolio());
			if (portfolioInfo.getMode() == 0)// monitor{
				update(session, portfolioInfo.getSimulateDailyDatas());
			else if (portfolioInfo.getFirstIndex() != -1) {// 做了DIVIDENDADJUSTMENT
				saveOrUpdate(session, portfolioInfo.getSimulateDailyDatas().subList(portfolioInfo.getFirstIndex(), portfolioInfo.getSimulateDailyDatas().size()));
			} else
				saveOrUpdate(session, portfolioInfo.getSimulateDailyDatas());

			if (portfolioInfo.getMode() == 0 /* Simulator.MONITOR */) {
				update(session, portfolioInfo.getSimulateLogs());
				update(session, portfolioInfo.getSimulatePortfolioMPTs());
				update(session, portfolioInfo.getSimulateTransactions());
				update(session, portfolioInfo.getSimulateHoldingItems());
			} else {
				saveOrUpdate(session, portfolioInfo.getSimulateLogs());
				saveOrUpdate(session, portfolioInfo.getSimulatePortfolioMPTs());
				saveOrUpdate(session, portfolioInfo.getSimulateTransactions());
				saveOrUpdate(session, portfolioInfo.getSimulateHoldingItems());
			}
			// 处理holding records,只保存最近三个月内持有的fund
			Date towMonthsAgoDate = LTIDate.getNDaysAgo(new Date(), 90);
			if (portfolioInfo.getSimulateHoldingRecords() != null) {
				for (HoldingRecord hr : portfolioInfo.getSimulateHoldingRecords()) {
					if (hr.getEndDate() == null || (!LTIDate.after(hr.getStartDate(), hr.getEndDate()) && LTIDate.before(towMonthsAgoDate, hr.getEndDate()))) {
						hr.setDividendDateStr(null);
						session.saveOrUpdate(hr);
					} else if (hr.getID() != null)
						session.delete(hr);
				}
			}

			session.saveOrUpdate(portfolioInfo.getCurrentPI());
			if (portfolioInfo.getDelayPI() != null)
				session.saveOrUpdate(portfolioInfo.getDelayPI());
			if (portfolioInfo.isDeleteExpectedHolding())
				session.delete(portfolioInfo.getExpectedPI());
			else if (portfolioInfo.getExpectedPI() != null)
				session.saveOrUpdate(portfolioInfo.getExpectedPI());

			if (portfolioInfo.getDelayCPI() != null)
				session.saveOrUpdate(portfolioInfo.getDelayCPI());
			if (portfolioInfo.getRealCPI() != null)
				session.saveOrUpdate(portfolioInfo.getRealCPI());
			if (portfolioInfo.getCPIs() != null && portfolioInfo.getCPIs().size() > 0) {
				for (CachePortfolioItem cpi : portfolioInfo.getCPIs()) {
					if (cpi.getGroupID() != 0l)
						session.update(cpi);
				}
			}
			if (portfolioInfo.getCSIs() != null && portfolioInfo.getCSIs().size() > 0) {
				for (CacheStrategyItem csi : portfolioInfo.getCSIs()) {
					session.update(csi);
				}
			}
			session.saveOrUpdate(portfolioInfo.getSecurity());

			for (GlobalObject go : portfolioInfo.getGlobalObjects()) {
				session.saveOrUpdate(go);
			}
			session.saveOrUpdate(portfolioInfo.getPortfolioState());

			if (portfolioInfo.isCustomized()) {
				UserManager userManager = ContextHolder.getUserManager();
				List<UserResource> userResourceList = userManager.getFollowPortfolioResourcesByPortfolioID(portfolioInfo.getPortfolio().getID());
				if (userResourceList != null) {
					for (UserResource userResource : userResourceList) {
						userResource.setUpdateTime(portfolioInfo.getPortfolio().getEndDate());
					}
					saveOrUpdate(session, userResourceList);
				}
			}
			session.flush();
			tx.commit();
			session.clear();
			session.close();

		} catch (Throwable e) {
			if (tx.isActive())
				tx.rollback();
			if (session.isOpen())
				session.close();
			throw new RuntimeException("Save data error where update portfolio [" + portfolioInfo.getPortfolio().getID() + "].", e);
		}
	}

	@Override
	public int getDailyDataCount(Long portfolioID) {
		String sql = "select count(*) from " + Configuration.TABLE_PORTFOLIO_DAILY_DATA + " p where p.portfolioid=" + portfolioID;
		BigInteger size = BigInteger.ZERO;
		try {
			List objects = super.findBySQL(sql);
			Object obj = (Object) objects.get(0);
			size = (BigInteger) obj;
			return size.intValue();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public List<PortfolioDailyData> getDuplicateDailyDatas(Long portfolioID, Date date) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PortfolioDailyData.class);

		detachedCriteria.add(Restrictions.eq("PortfolioID", portfolioID));

		detachedCriteria.add(Restrictions.eq("Date", date));

		detachedCriteria.addOrder(Order.asc("ID"));

		return this.getDailydatas(detachedCriteria);
	}

	@Override
	public PortfolioDailyData getSecondeDuplicateStartDailyData(Portfolio p) {
		Date startDate = LTIDate.getNewNYSETradingDay(p.getStartingDate());
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PortfolioDailyData.class);
		detachedCriteria.add(Restrictions.eq("PortfolioID", p.getID()));
		detachedCriteria.add(Restrictions.le("Date", startDate));
		detachedCriteria.addOrder(Order.asc("Date"));
		detachedCriteria.addOrder(Order.asc("ID"));
		List<PortfolioDailyData> pdds = getDailydatas(detachedCriteria);
		if (pdds != null && pdds.size() > 0) {
			PortfolioDailyData pdd = pdds.get(0);
			Date realStartDate = pdd.getDate();
			if (pdds.size() > 1) {
				PortfolioDailyData pdd2 = pdds.get(1);
				if (LTIDate.equals(realStartDate, pdd2.getDate())) {
					return pdd2;
				}
			}
		}
		return null;
	}

	@Override
	public PortfolioDailyData getFirstVersionLatestDailyDataBySecondStartDailyData(PortfolioDailyData pdd) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PortfolioDailyData.class);
		detachedCriteria.add(Restrictions.eq("PortfolioID", pdd.getPortfolioID()));
		detachedCriteria.add(Restrictions.lt("ID", pdd.getID()));
		detachedCriteria.addOrder(Order.desc("ID"));
		List<PortfolioDailyData> pdds = this.findByCriteria(detachedCriteria, 1, 0);
		if (pdds != null && pdds.size() > 0)
			return pdds.get(0);
		return null;

	}

	@Override
	public PortfolioDailyData getLatestNewerThanFirstVersionDailyData(PortfolioDailyData pdd) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PortfolioDailyData.class);
		detachedCriteria.add(Restrictions.eq("PortfolioID", pdd.getPortfolioID()));
		detachedCriteria.add(Restrictions.lt("Date", pdd.getDate()));
		detachedCriteria.addOrder(Order.asc("Date"));
		detachedCriteria.addOrder(Order.desc("ID"));
		List<PortfolioDailyData> pdds = getDailydatas(detachedCriteria);
		if (pdds != null && pdds.size() > 0) {
			return pdds.get(0);
		}
		return null;
	}

	private HoldingInf calculateExpectedHoldingInf(HoldingInf simulateHolding, List<Transaction> scheTransactions, Date date) throws SimulateException {
		boolean flag = false;
		HoldingInf simulateExpectedHolding = null;
		if (scheTransactions != null && scheTransactions.size() > 0) {
			simulateExpectedHolding = simulateHolding.clone();
			simulateExpectedHolding.switchPrice(Configuration.PRICE_TYPE_CLOSE);
			for (int i = 0; i < scheTransactions.size(); ++i) {
				Transaction t = scheTransactions.get(i);
				if (LTIDate.equals(t.getDate(), date)) {
					flag = true;
					String operation = t.getOperation();
					if (operation.equals(Configuration.TRANSACTION_BUY_AT_OPEN)) {
						// we set open price to 0 because we don't use it to
						// calculate totalamount today or do other things.
						simulateExpectedHolding.baseBuy(t.getAssetName(), t.getSecurityID(), t.getSymbol(), t.getAmount(), 0, t.getAmount() / t.getShare(), t.getReInvest(), Configuration.TRANSACTION_TYPE_REAL, false);
					} else if (operation.equals(Configuration.TRANSACTION_SELL_AT_OPEN)) {
						simulateExpectedHolding.baseSell(t.getAssetName(), t.getSecurityID(), t.getSymbol(), t.getAmount(), Configuration.TRANSACTION_TYPE_REAL, false);
					}
				} else
					continue;
			}
		}
		if (!flag)
			simulateExpectedHolding = null;
		else {
			simulateExpectedHolding.clearEmptyHolding();
			simulateExpectedHolding.refreshAmounts();
		}
		return simulateExpectedHolding;
	}

	@Override
	public void recoverPortfolio(long portfolioID, Date endDate) throws Exception {
		if (!LTIDate.isNYSETradingDay(endDate))
			endDate = LTIDate.getNewNYSETradingDay(endDate, -1);
		SecurityManager sm = ContextHolder.getSecurityManager();
		// 删除多余的数据，同时将current portfolioInf 设置好
		List<String> sqls = new ArrayList<String>();
		Portfolio p = this.get(portfolioID);
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = dateFormat.format(endDate);
		Date curEndDate = p.getEndDate();
		if (p != null && curEndDate != null && LTIDate.after(curEndDate, endDate)) {// 要执行
																					// 删除当天之后的dailydata
			sqls.add("delete from " + Configuration.TABLE_PORTFOLIO_DAILY_DATA + " where portfolioid=" + portfolioID + " and date> \'" + date + "\'");
			// 删除当天之后的transaction
			sqls.add("delete from " + Configuration.TABLE_TRANACTION + " where portfolioid=" + portfolioID + " and date> \'" + date + "\'");
			// 删除当天之后的holding Items,并取出最后的holding Items 做为当前holding Items
			sqls.add("delete from " + Configuration.TABLE_PORTFOLIO_HOLDINGITEM + " where portfolioid=" + portfolioID + " and date> \'" + date + "\'");
			PortfolioHoldingManager phm = ContextHolder.getPortfolioHoldingManager();
			List<HoldingItem> holdingItemList = phm.getLatestHoldingItemsBefore(portfolioID, LTIDate.getNewNYSETradingDay(endDate, 1));
			List<Transaction> scheduleTransactionList = this.getTransactionsByPortfolioIDAndTypeAndDate(portfolioID, Configuration.TRANSACTION_TYPE_SCHEDULE, endDate);
			PortfolioInf curPortfolioInf = this.getPortfolioInf(portfolioID, Configuration.PORTFOLIO_HOLDING_CURRENT);
			PortfolioInf expectedPortfolioInf = this.getPortfolioInf(portfolioID, Configuration.PORTFOLIO_HOLDING_EXPECTED);
			HoldingInf curHoldingInf = curPortfolioInf.getHolding();
			// 设置当前holdingInf内容,主要包括日期，holdingItems, holdingRecords,
			// scheduleTransactions
			curHoldingInf.setCurrentDate(endDate);
			// 先清除当前的所有holdingItem
			if (curHoldingInf.getAssets() != null) {
				for (Asset asset : curHoldingInf.getAssets()) {
					asset.getHoldingItems().clear();
				}
			}
			if (holdingItemList != null && holdingItemList.size() > 0) {
				for (HoldingItem hi : holdingItemList) {
					Asset asset = curHoldingInf.getAsset(hi.getAssetName());
					double close = sm.getPrice(hi.getSecurityID(), endDate);
					double open = sm.getOpenPrice(hi.getSecurityID(), endDate);
					if (asset == null) {
						asset.setName(hi.getAssetName());
						asset.setTargetPercentage(0.0);
						asset.setAmount(0);
						curHoldingInf.addAsset(asset);
					}
					hi.setOpen(open);
					hi.setClose(close);
					hi.setPrice(close);
					asset.getHoldingItems().add(hi);
				}
			}
			curHoldingInf.setCash(0);
			curHoldingInf.refreshAmounts();
			// 如果当前是有schedule transaction的话,则将schedule
			// transaction设入holdingInf中,并删除已记录的scheduletransaction
			SimulatorTransactionProcessor stp = (SimulatorTransactionProcessor) curHoldingInf.getTransactionProcessor();
			stp.clearAll();
			if (scheduleTransactionList != null && scheduleTransactionList.size() > 0) {
				for (Transaction t : scheduleTransactionList) {
					t.setID(null);
					stp.getScheduleTransactions().add(t);
				}
				// 删除当天已记录的schedule transaction
				sqls.add("delete from " + Configuration.TABLE_TRANACTION + " where transactiontype = 2 and portfolioid = " + portfolioID + " and date= \'" + date + "\'");
			}
			List<HoldingRecord> holdingRecordList = this.getHoldingRecords(portfolioID);
			List<HoldingRecord> updateList = new ArrayList<HoldingRecord>();
			// 删除在endDate之后生成的holding records，同时将enddate 在endDate之后的holding
			// record 进行了更新
			if (holdingRecordList != null && holdingRecordList.size() > 0) {
				for (HoldingRecord hr : holdingRecordList) {
					if (LTIDate.after(hr.getStartDate(), endDate)) {
						sqls.add("delete from " + Configuration.TABLE_HOLDINGRECORD + " where ID = " + hr.getID());
					} else {
						if (hr.getEndDate() != null && LTIDate.after(hr.getEndDate(), endDate)) {
							hr.setEndDate(null);
							updateList.add(hr);
						}
					}
				}
			}
			// 设置expectedholding
			HoldingInf expectedHolding = this.calculateExpectedHoldingInf(curHoldingInf, scheduleTransactionList, endDate);
			if (expectedHolding != null) {// 更新holding record
				if (expectedPortfolioInf == null) {
					expectedPortfolioInf = new PortfolioInf();
					expectedPortfolioInf.setPortfolioID(portfolioID);
					expectedPortfolioInf.setType(Configuration.PORTFOLIO_HOLDING_EXPECTED);
					expectedPortfolioInf.setHolding(expectedHolding);
					savePortfolioInf(expectedPortfolioInf);
				} else {
					expectedPortfolioInf.setHolding(expectedHolding);
					updatePortfolioInf(expectedPortfolioInf);
				}
			} else {
				if (expectedPortfolioInf != null)// 删除holding record
					sqls.add("delete from " + Configuration.TABLE_PORTFOLIOINF + " where ID = " + expectedPortfolioInf.getID());
			}
			executeSQL(sqls);
			this.saveOrUpdateAll(updateList);
			this.updatePortfolioInf(curPortfolioInf);
			p.setEndDate(endDate);
			this.update(p);
		}
	}

	@Override
	public Double getDividendAmountByPortfolioIDAndYear(Long portfolioID, int year) {
		String sql = "select sum(t.amount) from " + Configuration.TABLE_TRANACTION + " t where t.portfolioid = " + portfolioID + " and year(t.date) = " + year + " and transactiontype = 3";
		try {
			List objects = super.findBySQL(sql);
			Object obj = (Object) objects.get(0);
			if (obj != null)
				return (Double) obj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0.0;
	}

	@Override
	public PortfolioDailyData getLastPortfolioDailyDataByPortfolioIDAndYear(Long portfolioID, int year) {
		Date startDate = LTIDate.getDate(year, 1, 1);// year-01-01
		Date endDate = LTIDate.getLastNYSETradingDayOfYear(startDate);
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PortfolioDailyData.class);
		detachedCriteria.add(Restrictions.eq("PortfolioID", portfolioID));
		detachedCriteria.add(Restrictions.ge("Date", startDate));
		detachedCriteria.add(Restrictions.le("Date", endDate));
		detachedCriteria.addOrder(Order.desc("Date"));
		List<PortfolioDailyData> pdds = this.findByCriteria(detachedCriteria, 1, 0);
		if (pdds != null && pdds.size() == 1)
			return pdds.get(0);
		return null;
	}

	@Override
	public void updatePortfolioInf(PortfolioInf portfolioInf) {
		this.getHibernateTemplate().update(portfolioInf);
	}

	/*********************************** 20110120 ************************************/
	@Override
	public List<Portfolio> getPortfolioListByUserID(Long userID) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Portfolio.class);
		detachedCriteria.add(Restrictions.eq("UserID", userID));
		return this.findByCriteria(detachedCriteria);
	}

	/**
	 * 将follow的portfolio记录删除，同时添加follow expired的记录 改变类型，同时调整当前计数
	 * 
	 * @param userID
	 */
	@Override
	public void changeUserPortfolioToExpired(Long userID, Date expiredTime) {
		MYPLANIQPermissionManager mpm = new MYPLANIQPermissionManager();
		UserManager userManager = ContextHolder.getUserManager();
		List<UserResource> expiredResourceList = new ArrayList<UserResource>();
		List<UserResource> userPortfolioCustomizeList = userManager.getUserResourceByUserIDAndResourceType(userID, Configuration.USER_RESOURCE_PORTFOLIO_CUSTOMIZE);
		// 先把用户customize的portfolio设置为expired，同时去除follow和realtiem
		if (userPortfolioCustomizeList != null && userPortfolioCustomizeList.size() > 0) {
			for (UserResource ur : userPortfolioCustomizeList) {
				try {
					UserResource newUR = new UserResource();
					Long portfolioID = ur.getResourceId();
					Portfolio portfolio = this.get(portfolioID);
					Long planID = portfolio.getPlanID();
					newUR.setUserID(userID);
					newUR.setResourceId(portfolioID);
					newUR.setResourceType(Configuration.USER_RESOURCE_PORTFOLIO_CUSTOMIZE_EXPIRED);
					newUR.setRelationID(planID);
					newUR.setExpiredTime(expiredTime);
					newUR.setUpdateTime(ur.getUpdateTime());
					// 取消email alert
					userManager.deleteEmailNotification(userID, portfolioID);
					// 取消关联
					mpm.afterPortfolioUnfollowAndDelete(userID, portfolioID, planID);
					expiredResourceList.add(newUR);
				} catch (Exception e) {

				}
			}
		}
		// 再处理非用户customize并且follow的portfolio，去除follow和realtime
		List<UserResource> userPortfolioFollowList = userManager.getUserResourceByUserIDAndResourceType(userID, Configuration.USER_RESOURCE_PORTFOLIO_FOLLOW);
		for (UserResource ur : userPortfolioFollowList) {
			try {
				UserResource newUR = new UserResource();
				Long portfolioID = ur.getResourceId();
				Portfolio portfolio = this.get(portfolioID);
				Long planID = portfolio.getPlanID();
				newUR.setUserID(userID);
				newUR.setResourceId(portfolioID);
				newUR.setRelationID(planID);
				newUR.setResourceType(Configuration.USER_RESOURCE_PORTFOLIO_FOLLOW_EXPIRED);
				newUR.setUpdateTime(ur.getUpdateTime());
				newUR.setResourceCount(ur.getResourceCount());
				newUR.setExpiredTime(expiredTime);
				expiredResourceList.add(newUR);
				// 取消email alert
				userManager.deleteEmailNotification(userID, portfolioID);
				// 取消关联
				mpm.afterPortfolioUnfollow(userID, portfolio, planID);
			} catch (Exception e) {
			}
		}
		this.saveAll(expiredResourceList);
	}

	/**
	 * 将create plan的记录删除，同时添加create expired的记录 改变类型，同时调整当前计数
	 * 
	 * @param userID
	 */
	@Override
	public void changeUserPlanToExpired(Long userID, Date expiredTime) {
		MYPLANIQPermissionManager mpm = new MYPLANIQPermissionManager();
		UserManager userManager = ContextHolder.getUserManager();
		List<UserResource> userResourceList = userManager.getUserResourceByUserIDAndResourceType(userID, Configuration.USER_RESOURCE_PLAN_CREATE);
		if (userResourceList != null && userResourceList.size() > 0) {
			for (UserResource ur : userResourceList) {
				try {
					ur.setResourceType(Configuration.USER_RESOURCE_PLAN_CREATE_EXPIRED);
					ur.setExpiredTime(expiredTime);
					mpm.adjustPlanCreateNum(userID, -1);
				} catch (Exception e) {
				}
			}
			this.saveOrUpdateAll(userResourceList);
		}
	}

	/**
	 * 恢复选中的plan，之前已经做好数目检查
	 * 
	 * @param userID
	 * @param planIDList
	 */
	@Override
	public void activateUserPlan(Long userID, List<Long> planIDList) {
		MYPLANIQPermissionManager mpm = new MYPLANIQPermissionManager();
		UserManager userManager = ContextHolder.getUserManager();
		List<UserResource> userPlanExpiredList = userManager.getUserResourceByUserIDAndResourceType(userID, Configuration.USER_RESOURCE_PLAN_CREATE_EXPIRED);
		if (userPlanExpiredList != null) {
			for (UserResource userResource : userPlanExpiredList) {
				Long planID = userResource.getResourceId();
				if (planIDList != null && planIDList.contains(planID)) {// 对要恢复的plan，将其resourcetype
																		// 从USER_RESOURCE_PLAN_CREATE_EXPIRED
																		// 改变为
																		// USER_RESOURCE_PLAN_CREATE
					try {
						userResource.setResourceType(Configuration.USER_RESOURCE_PLAN_CREATE);
						userManager.updateUserResourse(userResource);
						mpm.adjustPlanCreateNum(userID, 1);
					} catch (Exception e) {
					}
				}
			}
		}
	}

	/**
	 * 只改变fund table user reousrce 类型，不调整当前计数
	 * 
	 * @param userID
	 */
	@Override
	public void changeUserFundTableToExpired(Long userID, Date expiredTime) {
		UserManager userManager = ContextHolder.getUserManager();
		List<UserResource> userResourceList = userManager.getUserResourceByUserIDAndResourceType(userID, Configuration.USER_RESOURCE_PLAN_FUNDTABLE);
		if (userResourceList != null && userResourceList.size() > 0) {
			for (UserResource ur : userResourceList) {
				try {
					userManager.deleteUserResource(ur);
					// ur.setResourceType(Configuration.USER_RESOURCE_PLAN_FUNDTABLE_EXPIRED);
					// ur.setExpiredTime(expiredTime);
				} catch (Exception e) {
				}
			}
		}
	}

	/**
	 * 将用户所有当前资源数清除为0
	 * 
	 * @param userID
	 */
	@Override
	public void clearUserPermission(Long userID) {
		UserManager userManager = ContextHolder.getUserManager();
		UserPermission userPermission = userManager.getUserPermissionByUserID(userID);
		userPermission.setCurPlanCreateNum(0);
		userPermission.setCurPlanFundTableNum(0);
		userPermission.setCurPlanFundTableNum(0);
		userPermission.setCurPortfolioRealTimeNum(0);
		userPermission.setCurPortfolioFollowNum(0);
		userPermission.setCurConsumerPlanNum(0);
		userManager.updateUserPermission(userPermission);
	}

	/**
	 * 当用户取消paypal时
	 * 
	 * @param userID
	 */
	@Override
	public void changeUserResourceWhenCanclePaypal(Long userID, Date expiredTime) {
		changeUserPlanToExpired(userID, expiredTime);
		changeUserPortfolioToExpired(userID, expiredTime);
		changeUserFundTableToExpired(userID, expiredTime);
		clearUserPermission(userID);
	}

	/**
	 * 将选中ID的portfolio设置为follow，同时去掉
	 * 
	 * @param userID
	 * @param portfolioList
	 */
	@Override
	public void activateFollowPortfolio(Long userID, List<Portfolio> portfolioList) {
		MYPLANIQPermissionManager mpm = new MYPLANIQPermissionManager();
		UserManager userManager = ContextHolder.getUserManager();
		List<UserResource> userResource = new ArrayList<UserResource>();
		if (portfolioList != null && portfolioList.size() > 0) {
			for (Portfolio portfolio : portfolioList) {
				try {
					Long planID = portfolio.getPlanID();
					mpm.afterPortfolioFollow(userID, portfolio, planID);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 删除一个portfolio时，必须调整follow此portfolio的用户记录和计数
	 * 
	 * @param portfolioID
	 * @param planID
	 */
	public void changeUserResourceBeforeDeletePortfolio(Portfolio portfolio) {
		MYPLANIQPermissionManager mpm = new MYPLANIQPermissionManager();
		UserManager userManager = ContextHolder.getUserManager();
		Long portfolioID = portfolio.getID();
		Long ownerID = portfolio.getUserID();
		Long planID = portfolio.getPlanID();
		List<UserResource> userResourceList = userManager.getUserResourceByResourceIDAndResourceType(portfolioID, Configuration.USER_RESOURCE_PORTFOLIO_FOLLOW);
		// 调整follow此portfolio并且不是owner的用户
		if (userResourceList != null) {
			for (UserResource ur : userResourceList) {
				try {
					if (ur.getUserID().equals(ownerID))
						continue;// 先不处理owner自己的记录，等删除portfolio时再执行
					userManager.deleteEmailNotification(ur.getUserID(), portfolioID);
					mpm.afterPortfolioUnfollow(ur.getUserID(), portfolio, planID);
				} catch (Exception e) {

				}
			}
		}
		// 同时删除过期记录里面的customize和follow
		userResourceList = userManager.getUserResourceByResourceIDAndResourceType(portfolioID, Configuration.USER_RESOURCE_PORTFOLIO_CUSTOMIZE_EXPIRED);
		if (userResourceList != null) {
			for (UserResource ur : userResourceList)
				userManager.deleteUserResource(ur);
		}
		userResourceList = userManager.getUserResourceByResourceIDAndResourceType(portfolioID, Configuration.USER_RESOURCE_PORTFOLIO_FOLLOW_EXPIRED);
		if (userResourceList != null) {
			for (UserResource ur : userResourceList)
				userManager.deleteUserResource(ur);
		}
	}

	/**
	 * 删除一个plan时，必须调整 1.follow此plan下面的portfolio的用户记录和计数 2.查看过此fundtable的用户记录和计数
	 * 3.过期的plan create记录
	 */
	@Override
	public void changeUserResourceBeforeDeletePlan(Strategy plan) {
		MYPLANIQPermissionManager mpm = new MYPLANIQPermissionManager();
		UserManager userManager = ContextHolder.getUserManager();
		StrategyManager strategyManager = ContextHolder.getStrategyManager();
		ProfileManager profileManager = ContextHolder.getProfileManager();
		Long planID = plan.getID();
		List<Portfolio> portfolioList = strategyManager.getModelPortfolios(planID);
		// 删除model portfolio
		if (portfolioList != null) {
			for (Portfolio portfolio : portfolioList) {
				try {
					this.remove(portfolio.getID());
				} catch (Exception e) {

				}
			}
		}
		// 删除用户属于这个plan的portfolio
		List<Profile> userProfileList = profileManager.getProfilesByPlanID(planID);
		if (userProfileList != null && userProfileList.size() > 0) {
			for (Profile p : userProfileList) {
				try {
					this.remove(p.getPortfolioID());
				} catch (Exception e) {
				}
			}
		}
		List<UserResource> userResourceList = userManager.getUserResourceByResourceIDAndResourceType(planID, Configuration.USER_RESOURCE_PLAN_FUNDTABLE);
		if (userResourceList != null) {
			for (UserResource ur : userResourceList) {
				try {
					mpm.afterPlanFundTableCancel(ur.getUserID(), planID);
				} catch (Exception e) {
				}
			}
		}
		userResourceList = userManager.getUserResourceByResourceIDAndResourceType(planID, Configuration.USER_RESOURCE_PLAN_CREATE_EXPIRED);
		if (userResourceList != null) {
			for (UserResource ur : userResourceList) {
				try {
					userManager.deleteUserResource(ur);
				} catch (Exception e) {
				}
			}
		}
	}

	public void clearExpiredPortfolios(int month) {
		UserManager userManager = ContextHolder.getUserManager();
		StrategyManager strategyManager = ContextHolder.getStrategyManager();
		Date today = new Date();
		Date expiredTime = LTIDate.getNDaysAgo(today, 30 * month);
		List<UserResource> expiredPlanList = userManager.getUserResourceByResourceTypeAndExpiredTime(Configuration.USER_RESOURCE_PLAN_CREATE_EXPIRED, expiredTime);
		if (expiredPlanList != null) {
			for (UserResource userResoruce : expiredPlanList) {
				Strategy plan = strategyManager.get(userResoruce.getResourceId());
				this.changeUserResourceBeforeDeletePlan(plan);
			}
		}
	}

	public List searchPortfolioAndPlan(String portfolioID) {
		Session session = null;
		session = getHibernateTemplate().getSessionFactory().openSession();
		final String sql = "from Portfolio as a, Strategy as b where a.MainStrategyID = b.ID and a.ID in(:portfolioid) order by a.EndDate desc";
		String[] strAry = portfolioID.trim().split(",");
		Long[] longAry = new Long[strAry.length];
		for (int i = 0; i < strAry.length; i++) {
			longAry[i] = java.lang.Long.parseLong(strAry[i]);
		}
		Query query = session.createQuery(sql);
		query.setParameterList("portfolioid", longAry);
		List list = query.list();
		session.flush();
		session.clear();
		session.close();
		return list;
	}

	public String checkPlanName(String portfolioIDs) {
		List list = this.searchPortfolioAndPlan(portfolioIDs);
		String str = "";
		String sub = "";
		String[] strName = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			Object[] obj = (Object[]) list.get(i);
			for (int j = 0; j < obj.length; j++) {
				if (obj[j] instanceof Portfolio) {
					Portfolio p = (Portfolio) obj[j];
					String subString = p.getEndDate() + "";
					subString = subString.substring(0, 10);
					sub += subString + ",";
					str += subString + " ";
				} else if (obj[j] instanceof Strategy) {
					Strategy s = (Strategy) obj[j];
					strName[j] = s.getName();
					str += strName[j] + "name=";
				} else
					System.out.println(obj[j].toString());
			}
		}
		String prefix = display(sub.split(","));
		return prefix + str;
	}

	public List<Portfolio> searchPortfolio(String portfolioID) {
		Session session = null;
		session = getHibernateTemplate().getSessionFactory().openSession();
		final String sql = "from Portfolio as a where a.ID in(:portfolioid) order by a.EndDate desc";
		String[] strAry = portfolioID.trim().split(",");
		Long[] longAry = new Long[strAry.length];
		for (int i = 0; i < strAry.length; i++) {
			longAry[i] = java.lang.Long.parseLong(strAry[i]);
		}
		// Query query =
		// session.createQuery("from Portfolio p where p.ID in (:portfolioID) order by p.EndDate desc");
		Query query = session.createQuery(sql);
		query.setParameterList("portfolioid", longAry);
		// query.executeUpdate();
		List<Portfolio> list = query.list();
		session.flush();
		session.clear();
		session.close();
		return list;
	}

	public String returnPortfolio(String portfolioID) {
		String msg = "";
		String sub = "";
		List<Portfolio> list = searchPortfolio(portfolioID);
		String[] longAry = new String[list.size()];
		Date[] dates = new Date[list.size()];
		for (int i = 0; i < list.size(); i++) {
			longAry[i] = list.get(i).getName();
			dates[i] = list.get(i).getEndDate();
			String subString = dates[i] + "";
			subString = subString.substring(0, 10);
			msg += subString + " " + longAry[i] + "name=";
			sub += subString + ",";
		}
		String prefix = display(sub.split(","));
		return prefix + msg;
	}

	public List<EmailNotification> searchEmailNotification(String portfolioID) {
		Session session = null;
		session = getHibernateTemplate().getSessionFactory().openSession();
		final String sql = "from EmailNotification as a where portfolioid in (:portfolioid) group by a.LastSentDate order by a.LastSentDate desc";
		String[] strAry = portfolioID.trim().split(",");
		Long[] longAry = new Long[strAry.length];
		for (int i = 0; i < strAry.length; i++) {
			longAry[i] = java.lang.Long.parseLong(strAry[i]);
		}
		Query query = session.createQuery(sql);
		query.setParameterList("portfolioid", longAry);
		List<EmailNotification> list = query.list();
		session.flush();
		session.clear();
		session.close();
		return list;
	}

	public String returnPortfolioStatistics(String portfolioID) {
		String msg = "";
		String sub = "";
		List<EmailNotification> list = searchEmailNotification(portfolioID);
		String[] longAry = new String[list.size()];
		Date[] dates = new Date[list.size()];
		for (int i = 0; i < list.size(); i++) {
			dates[i] = list.get(i).getLastSentDate();
			longAry[i] = String.valueOf(list.get(i).getPortfolioID());
			String subString = dates[i] + "";
			subString = subString.substring(0, 10);
			msg += subString + " " + longAry[i] + "name=";
			sub += subString + ",";
		}
		String prefix = display(sub.split(","));
		return prefix + msg;
	}

}
