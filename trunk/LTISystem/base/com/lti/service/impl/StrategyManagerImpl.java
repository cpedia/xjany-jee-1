package com.lti.service.impl;

import java.io.File;
import java.io.FileReader;
import java.io.Serializable;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.supercsv.io.CsvListReader;
import org.supercsv.prefs.CsvPreference;

import com.lti.permission.MYPLANIQPermissionManager;
import com.lti.service.AssetClassManager;
import com.lti.service.GroupManager;
import com.lti.service.PortfolioManager;
import com.lti.service.SecurityManager;
import com.lti.service.StrategyManager;
import com.lti.service.UserManager;
import com.lti.service.bo.AllocationTemplate;
import com.lti.service.bo.CacheStrategyItem;
import com.lti.service.bo.Data5500;
import com.lti.service.bo.PlanArticle;
import com.lti.service.bo.PlanScore;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.PortfolioMPT;
import com.lti.service.bo.Security;
import com.lti.service.bo.Strategy;
import com.lti.service.bo.StrategyClass;
import com.lti.service.bo.StrategyCode;
import com.lti.service.bo.ThirdParty;
import com.lti.service.bo.ThirdPartyResource;
import com.lti.service.bo.UserResource;
import com.lti.service.bo.VariableFor401k;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.type.PaginationSupport;
import com.lti.util.ObjectXMLDecoder;
import com.lti.util.Sort;
import com.lti.util.StringUtil;
import com.lti.util.ZipObject;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class StrategyManagerImpl extends DAOManagerImpl implements StrategyManager, Serializable {

	/** *********************************************************** */
	/* fields start */
	/** *********************************************************** */
	private static final long serialVersionUID = 1L;

	/** *********************************************************** */
	/* ==fields== end */
	/** *********************************************************** */
	private UserManager userManager;

	/** *********************************************************** */
	/* ==set method for spring==Start */
	/** *********************************************************** */

	/** *********************************************************** */
	/* ==set method for spring==End */
	/** *********************************************************** */

	/** *********************************************************** */
	/* ==basic method==Start */
	/** *********************************************************** */
	public long add(Strategy strgy) {
		String name = strgy.getName();
		if (name == null || name.equals("")) {
			throw new RuntimeException("The name of the startegy/plan is invalid.");
		}

		name = StringUtil.getValidName2(name).trim();
		strgy.setName(name);

		Strategy temp = this.get(strgy.getName());
		/*
		 * If there is a strategy with the same name already, return -1.
		 */
		if (temp != null) {
			throw new RuntimeException("The name of the startegy/plan has been used before.");
		}
		// strgy.setUpdateTime(new Date());
		strgy.setCreatedDate(new Date());
		getHibernateTemplate().save(strgy);
		List<VariableFor401k> vars = strgy.getVariablesFor401k();
		if (vars != null) {
			this.updateVariableFor401k(vars, strgy.getID());
		}

		return strgy.getID();
	}

	// 添加data5500
	public Long add(Data5500 data5500) {
		try {
			if (data5500 != null) {
				getHibernateTemplate().save(data5500);
				return data5500.getID();
			} else {
				return 0l;
			}
		} catch (Exception e) {
		}
		return 0l;
	}

	// 根据ACK查找data500
	public Data5500 getByACK(String ack_id) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Data5500.class);
		detachedCriteria.add(Restrictions.eq("ACK_ID", ack_id));
		List<Data5500> listD = findByCriteria(detachedCriteria, 1, 0);
		if (listD != null && listD.size() > 0) {
			return listD.get(0);
		} else {
			return null;
		}
	}

	// 根据planID查找DATA5500
	public Data5500 getData5500ByPlanID(Long planID) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Data5500.class);
		detachedCriteria.add(Restrictions.eq("PLAN_ID", planID));
		detachedCriteria.addOrder(Order.desc("ACK_ID"));
		List<Data5500> listD = findByCriteria(detachedCriteria, 1, 0);
		if (listD != null && listD.size() > 0) {
			return listD.get(0);
		} else {
			return null;
		}
	}

	@Override
	public Strategy get(Long id) {

		if (id == null)
			return null;
		Strategy strategy = (Strategy) getHibernateTemplate().get(Strategy.class, id);
		if (strategy != null) {
			if (strategy.is401K()) {
				List<VariableFor401k> var = this.getVariable401KByStrategyID(id);
				strategy.setVariablesFor401k(var);
			}
			if (strategy.getName() != null && strategy.getName().equals(Configuration.STRATEGY_401K)) {
				List<VariableFor401k> var = this.getVariable401KByStrategyID(strategy.getID());
				strategy.setVariablesFor401k(var);
			}
		}
		return strategy;
	}

	@Override
	public Strategy getBasicStrategy(Long id) {
		String hql = "select new Strategy(ID,Name,UserID) from Strategy where ID=" + id;
		List<Strategy> pos = super.findByHQL(hql);
		if (pos != null && pos.size() > 0)
			return pos.get(0);
		else
			return null;
	}

	@Override
	public Portfolio getFirtLiveBasicModelPortfolio(Long id) {
		String hql = "select new Portfolio(ID,Name,EndDate,UserID,State) from Portfolio where MainStrategyID=" + id + " and State=" + Configuration.PORTFOLIO_STATE_ALIVE;
		List<Portfolio> pos = super.findByHQL(hql, 1);
		if (pos != null && pos.size() > 0)
			return pos.get(0);
		else
			return null;
	}

	public Strategy get(String strategyname) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Strategy.class);

		detachedCriteria.add(Restrictions.or(Restrictions.eq("Name", strategyname), Restrictions.eq("Ticker", strategyname)));

		List<Strategy> bolist = findByCriteria(detachedCriteria, 1, 0);

		if (bolist.size() >= 1) {
			// return bolist.get(0);
			Strategy strategy = bolist.get(0);
			if (strategy != null) {
				if (strategy.is401K()) {
					List<VariableFor401k> var = this.getVariable401KByStrategyID(strategy.getID());
					strategy.setVariablesFor401k(var);
				}
				if (strategy.getName().equals(Configuration.STRATEGY_401K)) {
					List<VariableFor401k> var = this.getVariable401KByStrategyID(strategy.getID());
					strategy.setVariablesFor401k(var);
				}
			}
			return strategy;
		} else
			return null;
	}

	public void remove(long strID) {

		this.remove(strID, false);

	}

	public void remove(long strID, boolean expired) {

		PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();

		Strategy obj = (Strategy) getHibernateTemplate().get(Strategy.class, strID);

		super.deleteByHQL("from VariableFor401k vk where vk.StrategyID=" + strID);

		super.deleteByHQL("from PlanScore ps where ps.PlanID=" + strID);

		super.deleteByHQL("from PortfolioMPT p where p.strategyID=" + strID);

		super.deleteByHQL("from UserFundTable uf where uf.PlanID=" + strID);

		super.deleteByHQL("from StrategyCode sc where sc.StrategyID=" + strID);
		Long userID = obj.getUserID();

		Long planID = obj.getID();

		boolean admin = userID == Configuration.SUPER_USER_ID;

		if (obj.is401K()) {

			portfolioManager.changeUserResourceBeforeDeletePlan(obj);
			if (!expired && !admin) {
				MYPLANIQPermissionManager mpm = new MYPLANIQPermissionManager();
				try {
					mpm.afterPlanDelete(userID, planID, obj.isConsumerPlan());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		getHibernateTemplate().delete(obj);
	}

	public Boolean update(Data5500 data5500) {
		String ack_Id = data5500.getACK_ID();
		if (ack_Id != null) {
			Session session = null;
			org.hibernate.Transaction tx = null;
			try {
				session = getHibernateTemplate().getSessionFactory().openSession();
				tx = session.beginTransaction();
				session.saveOrUpdate(data5500);
				session.flush();
				tx.commit();
				session.clear();
				session.close();
			} catch (Exception e) {
				if (tx.isActive())
					tx.rollback();
				if (session.isOpen())
					session.close();
				throw new RuntimeException("update error.", e);
			}
		}
		return true;
	}

	public Boolean update(Strategy strgy) {
		String name = strgy.getName();
		if (name == null || name.equals("")) {
			name = "MyPlanIQ" + System.currentTimeMillis();
			strgy.setName(name);
		}

		// name=StringUtil.getValidName(name).trim();
		//

		getHibernateTemplate().update(strgy);

		if (strgy.is401K()) {
			List<VariableFor401k> vk = strgy.getVariablesFor401k();
			if (vk != null) {
				getHibernateTemplate().deleteAll(this.getVariable401KByStrategyID(strgy.getID()));
				this.updateVariableFor401k(vk, strgy.getID());
			}
		}
		if (strgy.getName().equals(Configuration.STRATEGY_401K)) {
			List<VariableFor401k> vk = strgy.getVariablesFor401k();
			if (vk != null) {
				getHibernateTemplate().deleteAll(this.getVariable401KByStrategyID(strgy.getID()));
				this.updateVariableFor401k(vk, strgy.getID());
			}
		}

		try {
			super.executeSQL("update " + Configuration.TABLE_PORTFOLIO_MPT + " set classid=" + strgy.getStrategyClassID() + " where strategyid=" + strgy.getID());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;

	}

	public void updatePortfolioName(long id, String name) throws Exception {
		this.executeSQL("update ltisystem_portfolio set Name= \"" + name + "\" where ID=" + id);
	}

	public void updateStrategyName(long id, String name) throws Exception {
		this.executeSQL("update ltisystem_strategy set Name= \"" + name + "\" where ID=" + id);
	}

	@Override
	public void updateStrategyType(List<Strategy> strategies) {
		this.saveOrUpdateAll(strategies);
	}

	@Override
	public List<Strategy> getStrategyByIDs(List<Long> strategyIDList) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Strategy.class);
		detachedCriteria.add(Restrictions.in("ID", strategyIDList));
		return this.getStrategies(detachedCriteria);
	}

	public PaginationSupport findStrategies(DetachedCriteria detachedCriteria, int pageSize, int startIndex) {

		return findPageByCriteria(detachedCriteria, pageSize, startIndex);

	}

	public List<Strategy> getStrategies(DetachedCriteria detachedCriteria) {

		return findByCriteria(detachedCriteria);

	}

	public List<VariableFor401k> getVariable401Ks(DetachedCriteria detachedCriteria) {

		return findByCriteria(detachedCriteria);

	}

	/** *********************************************************** */
	/* ==basic method== End */
	/** *********************************************************** */
	/** *********************************************************** */
	/* ==list method==Start */
	/** *********************************************************** */

	public List<Strategy> getStrategies() {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Strategy.class);

		return this.getStrategies(detachedCriteria);
	}

	public PaginationSupport getStrategies(int pageSize, int startIndex) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Strategy.class);

		return this.findStrategies(detachedCriteria, pageSize, startIndex);
	}

	@Override
	public List<Strategy> getStrategiesByClass(Object[] classids) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Strategy.class);

		detachedCriteria.add(Restrictions.in("StrategyClassID", classids));

		return this.getStrategies(detachedCriteria);
	}

	@Override
	public PaginationSupport getStrategiesByClass(Object[] classids, int pageSize, int startIndex) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Strategy.class);

		detachedCriteria.add(Restrictions.in("StrategyClassID", classids));

		return this.findStrategies(detachedCriteria, pageSize, startIndex);

	}

	@Override
	public List<String> getPlanNames(String term, int size) {
		try {
			String sql = "select name from " + Configuration.TABLE_STRATEGY + " s where s.type=" + Configuration.STRATEGY_401K_TYPE;
			if (term != null && term.length() != 0) {
				sql += " and name like '%" + term + "%' ";
			}
			if (size > 0) {
				sql += "limit 0," + size;
			}
			return super.findBySQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<Strategy> searchStrategies(Long classid, String q) {
		if (classid == null)
			return null;

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Strategy.class);

		detachedCriteria.add(Restrictions.like("Name", "%" + q + "%"));

		if (classid == 0)
			return this.getStrategies(detachedCriteria);

		else if (classid > 0) {

			StrategyClass cla = (StrategyClass) getHibernateTemplate().get(StrategyClass.class, classid);

			if (cla == null)
				return null;

			List<Long> IDList = new ArrayList<Long>();

			IDList.add(classid);

			Queue<Long> IDQueue = new LinkedList<Long>();

			IDQueue.add(classid);

			while (!IDQueue.isEmpty()) {
				Long ID = IDQueue.remove();

				IDList.add(ID);

				List<StrategyClass> childClasses = findByHQL("from StrategyClass ac where ac.ParentID=" + ID);

				if (childClasses != null && childClasses.size() > 0) {

					for (int i = 0; i < childClasses.size(); i++) {
						if (!IDList.contains(childClasses.get(i).getID()))
							IDQueue.add(childClasses.get(i).getID());
					}

				}
			}

			Object[] IDs = IDList.toArray();

			detachedCriteria.add(Restrictions.in("StrategyClassID", IDs));

			return this.getStrategies(detachedCriteria);
		}

		return null;
	}

	public List<Strategy> getStrategiesByName(String strategyname) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Strategy.class);

		detachedCriteria.add(Restrictions.like("Name", strategyname));

		return this.getStrategies(detachedCriteria);
	}

	public PaginationSupport getStrategiesByName(String strategyname, int pageSize, int startIndex) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Strategy.class);

		detachedCriteria.add(Restrictions.like("Name", "%" + strategyname + "%"));

		return this.findStrategies(detachedCriteria, pageSize, startIndex);

	}

	public PaginationSupport getStrategiesByUser(long userID, int pageSize, int startIndex) {

		return null;

	}

	public List<Strategy> getStrategiesByOtherGroup(long groupid) {

		String hql = "select s from Strategy s,GroupStrategy gr " + "where gr.GroupID!=" + groupid + " and gr.StrategyID=s.ID ";

		List<Strategy> bolist = findByHQL(hql);

		return bolist;
	}

	public PaginationSupport getStrategiesByOtherGroup(long groupid, int pageSize, int startIndex) {

		List<Strategy> bolist = new ArrayList<Strategy>();

		String hql = "select s from Strategy s,GroupStrategy gr " + "where gr.GroupID!=" + groupid + " and gr.StrategyID=s.ID ";

		PaginationSupport ps = findPageByHQL(hql, pageSize, startIndex);

		return ps;
	}

	public List<Strategy> getStrategiesByClassAndUser(long classid, long userid) {

		String hql = "select s from Strategy s,GroupStrategy gr,GroupUser gu " + "where gu.UserID=" + userid + " and gu.GroupID=gr.GroupID " + "and gr.StrategyID=s.ID and s.StrategyClassID=" + classid;

		List<Strategy> boList = findByHQL(hql);

		return boList;
	}

	public PaginationSupport getStrategiesByClassAndUser(long classid, long userid, int pageSize, int startIndex) {

		String hql = "select s from Strategy s,GroupStrategy gr,GroupUser gu " + "where gu.UserID=" + userid + " and gu.GroupID=gr.GroupID " + "and gr.StrategyID=s.ID and s.StrategyClassID=" + classid;

		PaginationSupport ps = findPageByHQL(hql, pageSize, startIndex);

		return ps;
	}

	@Override
	public List<Strategy> searchStrategiesByName(String name, int size, Long userID) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Strategy.class);

		List<String> keywords = StringUtil.splitKeywords(name);

		if (keywords != null && !keywords.equals("")) {
			String keyword = "%" + keywords.get(0) + "%";
			for (int i = 1; i < keywords.size(); i++) {
				String s = keywords.get(i);
				keyword = keyword + s + "%";
			}
			detachedCriteria.add(Restrictions.like("Name", keyword));
		}

		if (userID.longValue() != Configuration.SUPER_USER_ID) {
			// detachedCriteria.add( Restrictions.eq("UserID", userID));
		}

		List<Strategy> strategies = this.findByCriteria(detachedCriteria, size, 0);

		return strategies;
	}

	@Override
	public List<Strategy> searchStrategiesByName(String name, Long userID) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Strategy.class);

		List<String> keywords = StringUtil.splitKeywords(name);

		if (keywords != null && !keywords.equals("")) {
			String keyword = "%" + keywords.get(0) + "%";
			for (int i = 1; i < keywords.size(); i++) {
				String s = keywords.get(i);
				keyword = keyword + s + "%";
			}
			detachedCriteria.add(Restrictions.like("Name", keyword));
		}

		// if(userID.longValue() != Configuration.SUPER_USER_ID)
		// detachedCriteria.add(Restrictions.or(Restrictions.eq("IsPublic",
		// true), Restrictions.eq("UserID", userID)));

		List<Strategy> strategies = this.getStrategies(detachedCriteria);

		return strategies;
	}

	@Override
	public List<Strategy> searchStrategiesByCategory(String categories, Long userID) {
		String[] CategoryStrs = StringUtil.splitCategories(categories);
		List<Long> vStrategyIDs = getUserViewableStrategyID(userID);
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Strategy.class);
		Criterion criterion = Restrictions.in("ID", vStrategyIDs);
		String categoryMatchStr = StringUtil.categoryString(CategoryStrs);
		if (categoryMatchStr != null) {
			criterion = Restrictions.and(criterion, Restrictions.like("Categories", categoryMatchStr));
		}
		/*
		 * if(CategoryList != null && CategoryList.size() != 0){ Criterion
		 * cCategory; String type = CategoryList.get(0); cCategory =
		 * Restrictions.or(Restrictions.like("Name", "%" + type + "%"),
		 * Restrictions.like("Categories", "%" + type + "%")); for(int i = 1; i
		 * < CategoryList.size(); i++){ type = CategoryList.get(i); Criterion c
		 * = Restrictions.or(Restrictions.like("Name", "%" + type + "%"),
		 * Restrictions.like("Categories", "%" + type + "%")); cCategory =
		 * Restrictions.or(cCategory, c); } criterion =
		 * Restrictions.and(criterion, cCategory); }
		 */
		detachedCriteria.add(criterion);

		List<Strategy> strategies = new ArrayList<Strategy>();
		try {
			strategies = this.getStrategies(detachedCriteria);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strategies;
	}

	private List<Long> getUserViewableStrategyID(Long userID) {
		List<Strategy> strategies;
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Strategy.class);
		// detachedCriteria.add(Restrictions.or(Restrictions.eq("IsPublic",
		// true), Restrictions.or(Restrictions.eq("UserID",
		// Configuration.SUPER_USER_ID), Restrictions.eq("UserID", userID))));
		if (userID.longValue() != Configuration.SUPER_USER_ID) {
			detachedCriteria.add(Restrictions.eq("UserID", userID));
		}
		strategies = this.findByCriteria(detachedCriteria);
		List<Long> strategyIDs = new ArrayList<Long>();
		if (strategies != null && strategies.size() != 0) {
			for (int i = 0; i < strategies.size(); i++) {
				Long ID = strategies.get(i).getID();
				strategyIDs.add(ID);
			}
		}
		return strategyIDs;
	}

	@Override
	public List<Strategy> getStrategies(long userid) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Strategy.class);

		if (userid != com.lti.system.Configuration.SUPER_USER_ID) {
			detachedCriteria.add(Restrictions.or(Restrictions.eq("UserID", userid), Restrictions.eq("UserID", com.lti.system.Configuration.PUBLIC_USER_ID)));
		}

		return this.getStrategies(detachedCriteria);
	}

	@Override
	public PaginationSupport getStrategies(long userid, int pageSize, int startIndex) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Strategy.class);

		if (userid != com.lti.system.Configuration.SUPER_USER_ID) {
			detachedCriteria.add(Restrictions.or(Restrictions.eq("UserID", userid), Restrictions.eq("UserID", com.lti.system.Configuration.PUBLIC_USER_ID)));
		}

		return this.findStrategies(detachedCriteria, pageSize, startIndex);
	}

	public List<Strategy> getPrivateStrategies(long userid) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Strategy.class);

		detachedCriteria.add(Restrictions.eq("UserID", userid));
		// detachedCriteria.add(Restrictions.eq("IsPublic", false));
		detachedCriteria.addOrder(Order.desc("ID"));
		return this.getStrategies(detachedCriteria);
	}

	public PaginationSupport getPrivateStrategies(long userid, int pageSize, int startIndex) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Strategy.class);

		detachedCriteria.add(Restrictions.eq("UserID", userid));

		return this.findStrategies(detachedCriteria, pageSize, startIndex);
	}

	@Override
	public List<Strategy> getPublicStrategies() {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Strategy.class);

		detachedCriteria.add(Restrictions.eq("UserID", 0l));

		return this.getStrategies(detachedCriteria);
	}

	public PaginationSupport getPublicStrategies(int pageSize, int startIndex) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Strategy.class);

		detachedCriteria.add(Restrictions.eq("UserID", 0l));

		return this.findStrategies(detachedCriteria, pageSize, startIndex);
	}

	@Override
	public List<Strategy> getAllPublicStrategies() {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Strategy.class);

		// detachedCriteria.add(Restrictions.eq("IsPublic", true));
		detachedCriteria.addOrder(Order.desc("ID"));

		return this.getStrategies(detachedCriteria);
	}

	public List<Strategy> getMyPublicStrategies(long userid) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Strategy.class);

		detachedCriteria.add(Restrictions.eq("UserID", userid));
		// detachedCriteria.add(Restrictions.eq("IsPublic", true));
		detachedCriteria.addOrder(Order.desc("ID"));

		return this.getStrategies(detachedCriteria);
	}

	public List<Portfolio> getModelPortfolios(long strategyid) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Portfolio.class);
		detachedCriteria.add(Restrictions.eq("MainStrategyID", strategyid));
		detachedCriteria.add(Restrictions.ne("State", Configuration.PORTFOLIO_STATE_TEMP));
		List<Portfolio> bos = findByCriteria(detachedCriteria);
		return bos;

	}

	public List<Portfolio> getModeratePortfolios(long strategyid) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Portfolio.class);
		detachedCriteria.add(Restrictions.eq("MainStrategyID", strategyid));
		detachedCriteria.add(Restrictions.ne("State", Configuration.PORTFOLIO_STATE_TEMP));
		detachedCriteria.add(Restrictions.ilike("Name", "Moderate", MatchMode.END));
		List<Portfolio> bos = findByCriteria(detachedCriteria);
		return bos;
	}

	@Override
	public List<Portfolio> getModelPortfolios(long strategyid, long userID, int size) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Portfolio.class);
		detachedCriteria.add(Restrictions.eq("MainStrategyID", strategyid));
		detachedCriteria.add(Restrictions.ne("State", Configuration.PORTFOLIO_STATE_TEMP));
		List<Portfolio> bos = findByCriteria(detachedCriteria);
		if (bos == null)
			return null;
		List<Portfolio> nps = new ArrayList<Portfolio>();
		for (int i = 0; i < bos.size(); i++) {
			Portfolio p = bos.get(i);
			// p.getIsPublic() == true ||
			if (userID == Configuration.SUPER_USER_ID || p.getUserID().equals(userID)) {
				nps.add(p);
			}
			if (size > 0 && nps.size() >= size)
				break;

		}

		return nps;
	}

	@Override
	public List<Portfolio> getModelPortfolios(long strategyid, long userID) {
		return this.getModelPortfolios(strategyid, userID);
	}

	@Deprecated
	public List<Portfolio> getQuotePortfolios(long strategyid) {

		// DetachedCriteria detachedCriteria =
		// DetachedCriteria.forClass(Portfolio.class);
		//
		// detachedCriteria.add(Restrictions.or(Restrictions.or(Restrictions.eq("AssetAllocationStrategyID",
		// strategyid), Restrictions.eq("CashFlowStrategyID", strategyid)),
		// Restrictions.or(Restrictions.eq("RebalancingStrategyID", strategyid),
		// Restrictions.eq("MainStrategyID", strategyid))));
		//
		// detachedCriteria.add(Restrictions.eq("IsOriginalPortfolio", false));
		//
		// List<Portfolio> pos = findByCriteria(detachedCriteria);
		//
		// DetachedCriteria assetDC = DetachedCriteria.forClass(Asset.class);
		// assetDC.add(Restrictions.eq("AssetStrategyID", strategyid));
		// List<Asset> assets = findByCriteria(assetDC);
		// if (assets != null) {
		// for (int i = 0; i < assets.size(); i++) {
		// Asset asset = assets.get(i);
		// if (asset.getPortfolioID() == null) {
		// getHibernateTemplate().delete(asset);
		// } else {
		// Portfolio p = (Portfolio) getHibernateTemplate().get(Portfolio.class,
		// assets.get(i).getPortfolioID());
		// if (p != null && !pos.contains(p))
		// pos.add(p);
		// }
		//
		// }
		// }
		//
		// return pos;
		return null;
	}

	@Deprecated
	public List<Portfolio> getQuotePortfolios(long strategyid, long userID) {

		// DetachedCriteria detachedCriteria =
		// DetachedCriteria.forClass(Portfolio.class);
		//
		// detachedCriteria.add(Restrictions.or(Restrictions.or(Restrictions.eq("AssetAllocationStrategyID",
		// strategyid), Restrictions.eq("CashFlowStrategyID", strategyid)),
		// Restrictions.or(Restrictions.eq("RebalancingStrategyID", strategyid),
		// Restrictions.eq("MainStrategyID", strategyid))));
		//
		// detachedCriteria.add(Restrictions.eq("IsOriginalPortfolio", false));
		//
		// detachedCriteria.add(Restrictions.eq("UserID", userID));
		//
		// List<Portfolio> pos = findByCriteria(detachedCriteria);
		//
		// DetachedCriteria assetDC = DetachedCriteria.forClass(Asset.class);
		// assetDC.add(Restrictions.eq("AssetStrategyID", strategyid));
		// List<Asset> assets = findByCriteria(assetDC);
		// if (assets != null) {
		// for (int i = 0; i < assets.size(); i++) {
		// Asset asset = assets.get(i);
		// if (asset.getPortfolioID() == null) {
		// getHibernateTemplate().delete(asset);
		// } else {
		// Portfolio p = (Portfolio) getHibernateTemplate().get(Portfolio.class,
		// assets.get(i).getPortfolioID());
		// if (p != null && !pos.contains(p))
		// pos.add(p);
		// }
		//
		// }
		// }
		//
		// return pos;
		return null;
	}

	public static void sharpeRanking(List<PortfolioMPT> mptList) {
		if (mptList == null || mptList.size() == 0)
			return;
		List<PortfolioMPT> positiveSharpe = new ArrayList<PortfolioMPT>();
		List<PortfolioMPT> negativeSharpe = new ArrayList<PortfolioMPT>();
		List<PortfolioMPT> noData = new ArrayList<PortfolioMPT>();

		for (int i = 0; i < mptList.size(); i++) {
			PortfolioMPT p = mptList.get(i);
			if (p.getSharpeRatio() == null)
				noData.add(p);
			if (p.getSharpeRatio() != null && p.getSharpeRatio() >= 0) {
				positiveSharpe.add(p);
			} else if (p.getSharpeRatio() != null && p.getSharpeRatio() < 0) {
				negativeSharpe.add(p);
			}
		}
		List<PortfolioMPT> tmp = new ArrayList<PortfolioMPT>();
		for (int i = negativeSharpe.size() - 1; i >= 0; i--) {
			tmp.add(negativeSharpe.get(i));
		}
		negativeSharpe = tmp;
		List<PortfolioMPT> rankingSharpe = new ArrayList<PortfolioMPT>();
		rankingSharpe.addAll(positiveSharpe);
		rankingSharpe.addAll(negativeSharpe);
		rankingSharpe.addAll(noData);
		mptList.removeAll(mptList);
		mptList.addAll(rankingSharpe);
	}

	@Deprecated
	@Override
	public List<PortfolioMPT> getTopStrategyByMPT(Object[] classid, int year, int sort, Long userID, int size) {
		if (size < 0)
			return null;

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PortfolioMPT.class);

		detachedCriteria.add(Restrictions.in("classID", classid));

		detachedCriteria.add(Restrictions.eq("year", year));

		detachedCriteria.add(Restrictions.eq("isModelPortfolio", true));

		// detachedCriteria.add(Restrictions.eq("isPublic", true));

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

		List<PortfolioMPT> resultMPTs = new ArrayList<PortfolioMPT>();

		List<PortfolioMPT> mpts = findByCriteria(detachedCriteria);

		List<Long> ids = new ArrayList<Long>();

		int count = 0;

		PortfolioManager portfolioManager = (PortfolioManager) ContextHolder.getInstance().getApplicationContext().getBean("portfolioManager");

		if (mpts != null && mpts.size() >= 1) {
			/*
			 * if (sort == PortfolioMPT.SORT_BY_SHARPERATIO)
			 * sharpeRanking(mpts);
			 */
			for (int i = 0; i < mpts.size(); i++) {
				if (!ids.contains(mpts.get(i).getStrategyID())) {
					Strategy s = getBasicStrategy(mpts.get(i).getStrategyID());
					Portfolio p = portfolioManager.getBasicPortfolio(mpts.get(i).getPortfolioID());
					boolean isalive = (p.getState() == Configuration.PORTFOLIO_STATE_ALIVE);
					if (s != null && p != null && isalive && ((((p.getUserID() != null && p.getUserID().equals(userID))) && ((s.getUserID() != null && s.getUserID().longValue() == userID.longValue()))) || userID == Configuration.SUPER_USER_ID)) {
						count++;
						mpts.get(i).setStrategyName(s.getName());
						mpts.get(i).setStrategyUserID(s.getUserID());
						mpts.get(i).setName(p.getName());
						mpts.get(i).setState(isalive ? 1 : 0);
						mpts.get(i).setEndDate(p.getEndDate());
						mpts.get(i).setLastTransactionDate(portfolioManager.getTransactionLatestDate(p.getID()));
						resultMPTs.add(mpts.get(i));
						ids.add(s.getID());
						if (size != 0 && count >= size)
							break;
					}
				}
			}

			if (size > 0) {
				return resultMPTs;
			}
		}
		if (size == 0) {

			String hql = "select new Strategy(ID,Name) from Strategy where (";
			for (int i = 0; i < classid.length; i++) {
				if (i != 0) {
					hql += " or ";
				}
				hql += " StrategyClassID=" + classid[i];
			}
			if (userID.longValue() == Configuration.SUPER_USER_ID) {
				hql += ")";
			} else {
				hql += ") and (UserID=" + userID + ")";
			}
			List<Strategy> all = this.findByHQL(hql);
			for (int i = 0; i < all.size(); i++) {
				Strategy s = all.get(i);
				if (!ids.contains(s.getID())) {
					PortfolioMPT pm = new PortfolioMPT();
					pm.setStrategyName(all.get(i).getName());
					pm.setStrategyID(all.get(i).getID());
					Portfolio p = this.getFirtLiveBasicModelPortfolio(all.get(i).getID());
					if (p != null && (p.getUserID().longValue() == userID.longValue())) {
						pm.setPortfolioID(p.getID());
						pm.setName(p.getName());
						pm.setState((p.getState() == Configuration.PORTFOLIO_STATE_ALIVE) ? 1 : 0);
					}

					resultMPTs.add(pm);
				}
			}
			return resultMPTs;
		}
		return null;
	}

	@Deprecated
	@Override
	public PortfolioMPT getTopStrategyByMPT(Long strategyid, int year, int sort) {
		if (strategyid == 0L)
			return null;

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PortfolioMPT.class);

		detachedCriteria.add(Restrictions.eq("year", year));

		detachedCriteria.add(Restrictions.eq("isModelPortfolio", true));

		detachedCriteria.add(Restrictions.eq("strategyID", strategyid));

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

		List<PortfolioMPT> mpts = findByCriteria(detachedCriteria);

		PortfolioManager portfolioManager = (PortfolioManager) ContextHolder.getInstance().getApplicationContext().getBean("portfolioManager");

		if (mpts != null && mpts.size() >= 1) {
			/*
			 * if (sort == PortfolioMPT.SORT_BY_SHARPERATIO)
			 * sharpeRanking(mpts);
			 */
			for (int i = 0; i < mpts.size(); i++) {
				Strategy s = getBasicStrategy(mpts.get(i).getStrategyID());
				Portfolio p = portfolioManager.getBasicPortfolio(mpts.get(i).getPortfolioID());
				boolean isalive = (p.getState() == Configuration.PORTFOLIO_STATE_ALIVE);

				if (s != null && p != null && isalive) {
					mpts.get(i).setStrategyName(s.getName());
					mpts.get(i).setStrategyUserID(s.getUserID());
					mpts.get(i).setName(p.getName());
					mpts.get(i).setState(isalive ? 1 : 0);
					mpts.get(i).setEndDate(p.getEndDate());
					mpts.get(i).setLastTransactionDate(portfolioManager.getTransactionLatestDate(p.getID()));
					return mpts.get(i);
				}
			}
		}

		return null;
	}

	@Override
	public List<PortfolioMPT> getSortMPTs(Long strategyid, int year, int sort) {
		if (strategyid == 0L)
			return null;

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PortfolioMPT.class);

		detachedCriteria.add(Restrictions.eq("year", year));

		detachedCriteria.add(Restrictions.eq("isModelPortfolio", true));

		detachedCriteria.add(Restrictions.eq("strategyID", strategyid));

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

		List<PortfolioMPT> mpts = findByCriteria(detachedCriteria);

		return mpts;
	}

	/** *********************************************************** */
	/* ==list method==End */
	/** *********************************************************** */

	@Override
	public List<PortfolioMPT> getTopPortfolioInStrategy(long strategyID, int year, int sort, Long userID, int size) {
		if (size < 0)
			return null;

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PortfolioMPT.class);

		detachedCriteria.add(Restrictions.eq("year", year));

		detachedCriteria.add(Restrictions.eq("isModelPortfolio", true));

		detachedCriteria.add(Restrictions.eq("strategyID", strategyID));

		// 2009-04-04: remove the private portfolios when the user is not the
		// administrator
		// if(userID.longValue() != Configuration.SUPER_USER_ID)
		// detachedCriteria.add(Restrictions.or(Restrictions.eq("isPublic",true),
		// Restrictions.eq("userID", userID)));

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

		List<PortfolioMPT> resultMPTs = new ArrayList<PortfolioMPT>();

		List<PortfolioMPT> mpts = findByCriteria(detachedCriteria);

		PortfolioManager portfolioManager = (PortfolioManager) ContextHolder.getInstance().getApplicationContext().getBean("portfolioManager");

		if (mpts == null || mpts.size() == 0)
			return null;
		List<PortfolioMPT> livePortfolioMPT = new ArrayList<PortfolioMPT>();
		List<PortfolioMPT> staticPortfolioMPT = new ArrayList<PortfolioMPT>();
		for (int i = 0; i < mpts.size(); i++) {
			PortfolioMPT pm = mpts.get(i);
			Strategy s = this.get(pm.getStrategyID());
			Portfolio p = portfolioManager.get(pm.getPortfolioID());
			boolean isalive = (p.getState() == Configuration.PORTFOLIO_STATE_ALIVE);

			if (s != null && p != null && isalive && (userID == Configuration.SUPER_USER_ID || p.getUserID().equals(userID))) {
				livePortfolioMPT.add(pm);
			} else if (s != null && p != null && isalive && (userID == Configuration.SUPER_USER_ID || p.getUserID().equals(userID))) {
				staticPortfolioMPT.add(pm);
			}
		}
		if (sort == PortfolioMPT.SORT_BY_SHARPERATIO) {
			sharpeRanking(livePortfolioMPT);
			sharpeRanking(staticPortfolioMPT);
		}
		int count = 0;
		if (livePortfolioMPT.size() != 0) {
			for (int i = 0; i < livePortfolioMPT.size(); i++) {
				resultMPTs.add(livePortfolioMPT.get(i));
				count++;
				if (size > 0 && count >= size)
					break;
			}

		}
		if (count < size && staticPortfolioMPT.size() != 0) {
			for (int i = 0; i < staticPortfolioMPT.size(); i++) {
				resultMPTs.add(staticPortfolioMPT.get(i));
				count++;
				if (size > 0 && count >= size)
					break;
			}
		}
		return resultMPTs;
	}

	// public static void main(String[] args) {
	// StrategyManager strategyManager = (StrategyManager)
	// ContextHolder.getInstance().getApplicationContext().getBean("strategyManager");
	// strategyManager.searchStrategiesByCategory("g,f", 1l);
	// }

	@Override
	public List<Strategy> getStrategiesByMainStrategyID(Long id) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Strategy.class);
		detachedCriteria.add(Restrictions.eq("MainStrategyID", id));
		return this.getStrategies(detachedCriteria);
	}

	@Override
	public List<Strategy> getStrategiesByType(long type) {
		String hql = "from Strategy p where bit_and(p.Type, :typevalue) = " + type;
		Session session = null;

		List result;
		try {
			session = getHibernateTemplate().getSessionFactory().openSession();

			Query query = session.createQuery(hql);
			query.setLong("typevalue", type);

			result = query.list();

			return result;
		} catch (Exception e) {
			throw new RuntimeException("findByHQL sql error.", e);
		} finally {
			if (session != null && session.isOpen())
				session.close();
		}
	}

	@Override
	public List<Strategy> getPlansByType(String name, int planType, int size) {
		String hql = "from Strategy p where bit_and(p.Type, :typevalue) = " + Configuration.STRATEGY_TYPE_401K;
		if (planType != -1) {
			hql += " and p.PlanType=" + planType;
		}
		hql += " and p.Name like '%" + name.trim() + "%'";

		Session session = null;

		List result;
		try {
			session = getHibernateTemplate().getSessionFactory().openSession();

			Query query = session.createQuery(hql);
			query.setLong("typevalue", Configuration.STRATEGY_TYPE_401K);

			if (size != -1) {
				query.setMaxResults(size);
			}

			result = query.list();

			return result;
		} catch (Exception e) {
			throw new RuntimeException("findByHQL sql error.", e);
		} finally {
			if (session != null && session.isOpen())
				session.close();
		}
	}

	@Override
	public List<Strategy> getBasicStrategiesByType(long type) {
		String hql = "select new Strategy(ID,Name,UserID) from Strategy p where bit_and(p.Type, :typevalue) = " + type;
		Session session = null;

		List result;
		try {
			session = getHibernateTemplate().getSessionFactory().openSession();

			Query query = session.createQuery(hql);
			query.setLong("typevalue", type);

			result = query.list();

			return result;
		} catch (Exception e) {
			throw new RuntimeException("findByHQL sql error.", e);
		} finally {
			if (session != null && session.isOpen())
				session.close();
		}
	}

	/*
	 * public static void main(String[] args){ StrategyManager
	 * sm=ContextHolder.getStrategyManager(); List<Strategy>
	 * strs=sm.getStrategiesByType(Configuration.STRATEGY_TYPE_PRESERVED);
	 * for(Strategy s:strs){ System.out.println(s.getName()); } }
	 */

	@Override
	public List<VariableFor401k> getVariable401KByStrategyID(Long id) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(VariableFor401k.class);
		detachedCriteria.add(Restrictions.eq("StrategyID", id));
		return this.getVariable401Ks(detachedCriteria);
	}

	@Override
	public VariableFor401k getVariable401K(Long planid, String symbol) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(VariableFor401k.class);
		detachedCriteria.add(Restrictions.eq("StrategyID", planid));
		detachedCriteria.add(Restrictions.eq("symbol", symbol));
		List<VariableFor401k> vs = this.findByCriteria(detachedCriteria, 1, 0);
		if (vs != null && vs.size() > 0)
			return vs.get(0);
		return null;
	}

	@Override
	public List<VariableFor401k> getVariable401Ks(List<Long> planIDList) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(VariableFor401k.class);
		detachedCriteria.add(Restrictions.isNotNull("name"));
		detachedCriteria.add(Restrictions.isNotNull("symbol"));
		detachedCriteria.add(Restrictions.isNotNull("description"));
		detachedCriteria.add(Restrictions.isNotNull("assetClassName"));
		detachedCriteria.add(Restrictions.in("StrategyID", planIDList));
		return this.findByCriteria(detachedCriteria);
	}

	@Override
	public void updateVariableFor401k(List<VariableFor401k> vks, Long id) {
		for (int i = 0; i < vks.size(); i++) {
			vks.get(i).setStrategyID(id);
		}
		this.saveAll(vks);
	}

	@Override
	public void updateVariableFor401k(List<VariableFor401k> variables) {
		this.saveOrUpdateAll(variables);
	}

	@Override
	public void saveAll(List<VariableFor401k> vks) {
		if (vks == null || vks.size() <= 0) {
			return;
		}
		super.saveAll(vks);
	}

	@Override
	public void saveVk(VariableFor401k vk) {
		getHibernateTemplate().save(vk);
	}

	@Override
	public List<VariableFor401k> parse(byte[] bytes) {
		try {
			if (bytes == null || bytes.length == 0)
				return null;
			String value = (String) ZipObject.ZipBytesToObject(bytes);
			String str = value.replace("com.lti.type.VariableFor401k", "com.lti.service.bo.VariableFor401k");
			List<VariableFor401k> bs = (List<VariableFor401k>) ObjectXMLDecoder.XMLToObject(str);
			return bs;
		} catch (Exception e) {
			// e.printStackTrace();
			return null;
		}
	}

	/**
	 * @author CCD
	 * @param psList
	 *            save all the PlanScore
	 */
	@Override
	public void saveALLPlanScore(List<PlanScore> psList) {
		if (psList != null && psList.size() > 0)
			super.saveAll(psList);
	}

	/**
	 * @author CCD
	 * @param psList
	 *            save or update all the PlanScore
	 */
	@Override
	public void saveOrUpdateAllPlanScore(List<PlanScore> psList) {
		if (psList != null && psList.size() > 0)
			super.saveOrUpdateAll(psList);
	}

	@Override
	public void saveOrUpdatePlanScore(PlanScore planScore) {
		getHibernateTemplate().saveOrUpdate(planScore);
	}

	/**
	 * @author CCD return all the PlanScore
	 */
	public List<PlanScore> getPlanScore() {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PlanScore.class);
		detachedCriteria.add(Restrictions.eq("Status", 0));
		detachedCriteria.addOrder(Order.desc("InvestmentScore"));
		List<PlanScore> planScoreList = findByCriteria(detachedCriteria);
		return planScoreList;
	}

	/**
	 * @author CCD return the planScore with ID in the planIDList
	 */
	public List<PlanScore> getPlanScoreWithIDs(List<Long> planIDList) {
		if (planIDList == null || planIDList.size() == 0)
			return null;
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PlanScore.class);
		detachedCriteria.add(Restrictions.eq("Status", 0));
		detachedCriteria.add(Restrictions.in("PlanID", planIDList));
		detachedCriteria.addOrder(Order.desc("InvestmentScore"));
		List<PlanScore> planScoreList = findByCriteria(detachedCriteria);
		return planScoreList;
	}

	/**
	 * @author CCD
	 * @param planID
	 *            return the planScore by plan ID
	 */
	public PlanScore getPlanScoreByPlanID(Long planID) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PlanScore.class);
		detachedCriteria.add(Restrictions.eq("PlanID", planID));
		List<PlanScore> planScoreList = findByCriteria(detachedCriteria);
		if (planScoreList != null && planScoreList.size() > 0)
			return planScoreList.get(0);
		return null;
	}

	/**
	 * @author CCD
	 * @param planID
	 * @return return the symbolList for the special plan with planID
	 */
	public List<String> getSymbolListForPlan(Long planID) {
		String sql = "SELECT symbol FROM ltisystem.ltisystem_variables401k where strategyid = " + planID;
		List<String> symbolList = null;
		try {
			symbolList = (List<String>) super.findBySQL(sql);
		} catch (Exception e) {
			System.out.println(StringUtil.getStackTraceString(e));
		}
		return symbolList;
	}

	@Override
	public VariableFor401k getVariableFor401kByDescription(String description) {

		long type = Configuration.STRATEGY_TYPE_401K | Configuration.STRATEGY_TYPE_INDEXED;
		List<Strategy> plans = this.getBasicStrategiesByType(type);
		List<Long> planIDList = new ArrayList<Long>();
		if (plans != null && plans.size() > 0) {
			for (Strategy plan : plans)
				planIDList.add(plan.getID());
			DetachedCriteria detachedCriteria = DetachedCriteria.forClass(VariableFor401k.class);
			detachedCriteria.add(Restrictions.eq("description", description));
			detachedCriteria.add(Restrictions.in("StrategyID", planIDList));
			List<VariableFor401k> variableList = this.findByCriteria(detachedCriteria, 1, 0);
			if (variableList != null && variableList.size() == 1)
				return variableList.get(0);
		}
		return null;
	}

	/**
	 * @author CCD
	 * @param planID
	 * @return return the symbolList for the special plan with planID without
	 *         those with memo ="n"
	 */
	public List<String> getSymbolListForPlanWithoutN(Long planID) {
		String sql = "SELECT symbol FROM ltisystem.ltisystem_variables401k where strategyid = " + planID + " and (memo is null or memo!=\'n\')";
		List<String> symbolList = null;
		try {
			symbolList = (List<String>) super.findBySQL(sql);
		} catch (Exception e) {
			System.out.println(StringUtil.getStackTraceString(e));
		}
		return symbolList;
	}

	@Override
	public List<Object[]> getPlanNamesAndIDs(String term, int size) {
		try {
			String sql = "select id,name from " + Configuration.TABLE_STRATEGY + " s where s.type=" + Configuration.STRATEGY_401K_TYPE;
			if (term != null && term.length() != 0) {
				sql += " and name like '%" + term + "%' ";
			}
			if (size > 0) {
				sql += "limit 0," + size;
			}
			return super.findBySQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public StrategyCode getLatestStrategyCode(Long strategyid) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(StrategyCode.class);

		detachedCriteria.add(Restrictions.eq("StrategyID", strategyid));
		detachedCriteria.addOrder(Order.desc("Date"));

		List<StrategyCode> bolist = findByCriteria(detachedCriteria, 1, 0);
		if (bolist != null && bolist.size() > 0)
			return bolist.get(0);
		else
			return null;
	}

	@Override
	public StrategyCode getStrategyCode(Long strategyid, Date date) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(StrategyCode.class);

		detachedCriteria.add(Restrictions.eq("StrategyID", strategyid));
		detachedCriteria.add(Restrictions.eq("Date", date));

		List<StrategyCode> bolist = findByCriteria(detachedCriteria, 1, 0);
		if (bolist != null && bolist.size() > 0)
			return bolist.get(0);
		else
			return null;
	}

	@Override
	public void saveStrategyCode(StrategyCode str) {
		StrategyCode sc = getStrategyCode(str.getStrategyID(), str.getDate());
		if (sc != null) {
			str.setID(sc.getID());
		}
		getHibernateTemplate().saveOrUpdate(str);
	}

	@Override
	public void deleteStrategyCode(Long strategyid, Date date) {
		getHibernateTemplate().delete(getStrategyCode(strategyid, date));
	}

	@Override
	public CacheStrategyItem getCacheStrategyItem(long groupid, long strategyid) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(CacheStrategyItem.class);
		detachedCriteria.add(Restrictions.eq("StrategyID", strategyid));
		detachedCriteria.add(Restrictions.eq("GroupID", groupid));
		detachedCriteria.add(Restrictions.eq("RoleID", Configuration.ROLE_STRATEGY_READ_ID));
		List<CacheStrategyItem> bolist = findByCriteria(detachedCriteria, 1, 0);
		if (bolist.size() >= 1)
			return bolist.get(0);
		else
			return null;
	}

	@Override
	public List<CacheStrategyItem> getCacheStrategyItems(long strategyid) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(CacheStrategyItem.class);
		detachedCriteria.add(Restrictions.eq("StrategyID", strategyid));
		detachedCriteria.add(Restrictions.eq("RoleID", Configuration.ROLE_STRATEGY_READ_ID));
		List<CacheStrategyItem> bolist = findByCriteria(detachedCriteria);
		return bolist;
	}

	@Override
	public List<List<PlanScore>> getTopAndBottomPlanScoreList(int num) {
		Long[] groupIDs = { Configuration.GROUP_ANONYMOUS_ID };
		GroupManager gm = ContextHolder.getGroupManager();
		List<Long> anonymousPlanIDs = gm.getResourceIDByGroups(groupIDs, Configuration.RESOURCE_TYPE_STRATEGY);
		List<PlanScore> topList = getTopOrBottomNPlanScore(anonymousPlanIDs, num, true);
		List<PlanScore> bottomList = getTopOrBottomNPlanScore(anonymousPlanIDs, num, false);
		List<List<PlanScore>> scoreList = new ArrayList<List<PlanScore>>();
		scoreList.add(topList);
		scoreList.add(bottomList);
		return scoreList;
	}

	public List<PlanScore> getTopOrBottomNPlanScore(List<Long> planIDList, int num, boolean top) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PlanScore.class);
		detachedCriteria.add(Restrictions.in("PlanID", planIDList));
		detachedCriteria.add(Restrictions.eq("Status", 0));
		if (top)
			detachedCriteria.addOrder(Order.desc("InvestmentScore"));
		else
			detachedCriteria.addOrder(Order.asc("InvestmentScore"));
		return this.findByCriteria(detachedCriteria, num, 0);
	}

	@Override
	public void addPlanArticle(PlanArticle pa) {
		getHibernateTemplate().save(pa);
	}

	public void updatePlanArticle(PlanArticle pa) {
		getHibernateTemplate().update(pa);
	}

	@Override
	public void removePlanArticle(String title) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PlanArticle.class);
		detachedCriteria.add(Restrictions.eq("ArticleTitle", title));
		List<PlanArticle> planArticleList = findByCriteria(detachedCriteria);

		if (planArticleList != null && planArticleList.size() > 0) {
			for (PlanArticle p : planArticleList) {
				getHibernateTemplate().delete(p);
			}
		}

	}

	@Override
	public List<PlanArticle> getPlanArticles(long planid) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PlanArticle.class);
		detachedCriteria.add(Restrictions.eq("PlanID", planid));
		List<PlanArticle> planArticleList = findByCriteria(detachedCriteria);
		if (planArticleList != null && planArticleList.size() > 0)
			return planArticleList;
		return null;
	}

	public PlanArticle getPlanArticleByID(long id) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PlanArticle.class);
		detachedCriteria.add(Restrictions.eq("ID", id));
		List<PlanArticle> planArticleList = findByCriteria(detachedCriteria);
		if (planArticleList != null && planArticleList.size() > 0)
			return planArticleList.get(0);
		return null;
	}

	public List<PlanArticle> getPlanArticleByTitle(String articleTitle) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PlanArticle.class);
		detachedCriteria.add(Restrictions.eq("ArticleTitle", articleTitle));
		List<PlanArticle> planArticleList = findByCriteria(detachedCriteria);
		if (planArticleList != null && planArticleList.size() > 0)
			return planArticleList;
		return null;
	}

	public List<PlanArticle> getPlanArticleBySymbol(String symbol) {
		String[] symbols = symbol.split(",");
		// DetachedCriteria detachedCriteria = DetachedCriteria
		// .forClass(PlanArticle.class);
		// detachedCriteria.add(
		// Restrictions.or(
		// Restrictions.or(
		// Restrictions.in("Symbols" , symbols),
		// Restrictions.like("Symbols", "%," + symbol + ",%")
		// ),
		// //Restrictions.like("Symbols", "%," + symbol + ",%"),
		// Restrictions.or(Restrictions.ilike("Symbols",symbol + ",%",
		// MatchMode.START), Restrictions.ilike("Symbols","%," + symbol,
		// MatchMode.END))
		// )
		// );
		// List<PlanArticle> planArticleList = findByCriteria(detachedCriteria);
		String hql = "from PlanArticle pa where pa.Symbols like '%";
		String hql1 = "";
		for (int i = 0; i < symbols.length; i++) {
			if (i < symbols.length - 1)
				hql1 = hql1 + symbols[i].trim() + "%' or pa.Symbols like '%";
			else
				hql1 = hql1 + symbols[i].trim() + "%'";
		}
		System.out.println(hql + hql1);
		List<PlanArticle> planArticleList = findByHQL(hql + hql1);
		return planArticleList;
	}

	public static void main(String[] args) {
		StrategyManager strategyManager = ContextHolder.getStrategyManager();
		strategyManager.getPlanArticleBySymbol("aa,bb,cc");
	}

	public AllocationTemplate getAllocationTemplateById(long id) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(AllocationTemplate.class);
		detachedCriteria.add(Restrictions.eq("ID", id));
		List<AllocationTemplate> templateList = findByCriteria(detachedCriteria);
		if (templateList != null && templateList.size() > 0) {
			return templateList.get(0);
		} else
			return null;
	}

	public List<AllocationTemplate> getAllocationTemplateByName(String name) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(AllocationTemplate.class);
		detachedCriteria.add(Restrictions.eq("TemplateName", name));
		List<AllocationTemplate> templateList = findByCriteria(detachedCriteria);
		return templateList;
	}

	public void addAllocationTemplate(AllocationTemplate at) {
		getHibernateTemplate().save(at);
	}

	public void updateAllocationTemplate(AllocationTemplate at) {
		getHibernateTemplate().update(at);
	}

	public void removeAllocationTemplate(String name) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(AllocationTemplate.class);
		detachedCriteria.add(Restrictions.eq("TemplateName", name));
		List<AllocationTemplate> templateList = findByCriteria(detachedCriteria);
		if (templateList != null && templateList.size() > 0) {
			for (AllocationTemplate at : templateList) {
				getHibernateTemplate().delete(at);
			}
		}
	}

	public List<AllocationTemplate> getAllallocationTemplate() {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(AllocationTemplate.class);
		List<AllocationTemplate> templateList = findByCriteria(detachedCriteria);
		return templateList;
	}

	// public static void main(String[] args){
	// try {
	// StrategyManager sm=ContextHolder.getStrategyManager();
	// List<Object[]> objs=sm.searchPlanByThirdParty("abc.com", "vir", 10);
	// for(Object[] oo:objs){
	// System.out.println("#");
	// System.out.println(oo[0]);
	// System.out.println(oo[1]);
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// }

	public PlanScore getLEPlanScoreByValueType(double value, String type) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PlanScore.class);
		detachedCriteria.add(Restrictions.le(type, value));
		detachedCriteria.addOrder(Order.desc(type));
		List<PlanScore> psList = this.findByCriteria(detachedCriteria, 1, 0);
		if (psList != null && psList.size() > 0)
			return psList.get(0);
		return null;
	}

	public PlanScore getGEPlanScoreByValueType(double value, String type) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PlanScore.class);
		detachedCriteria.add(Restrictions.ge(type, value));
		detachedCriteria.addOrder(Order.asc(type));
		List<PlanScore> psList = this.findByCriteria(detachedCriteria, 1, 0);
		if (psList != null && psList.size() > 0)
			return psList.get(0);
		return null;
	}

	public int getPlanType(Long planID) {
		Strategy plan = this.get(planID);
		if (plan != null)
			return plan.getPlanType();
		return 0;
	}

	public void updateStrategySimilarIssues(long id, String message) throws Exception {
		this.executeSQL("update ltisystem_strategy set SimilarIssues= \"" + message + "\" where ID=" + id);
	}

	public List<String> getPlanTickers(long id) {
		List<String> tickerList = new ArrayList<String>();
		List<VariableFor401k> candidateFunds = this.getVariable401KByStrategyID(id);
		if (candidateFunds == null)
			return null;
		for (VariableFor401k va : candidateFunds) {
			String s = va.getSymbol();
			tickerList.add(s);
		}
		return tickerList;
	}

	public List<String> getPlanMinorAssets(List<String> tickerList) {
		SecurityManager securityManager = ContextHolder.getSecurityManager();
		List<String> assetsList = new ArrayList<String>();
		for (String ss : tickerList) {
			Security se = securityManager.getBySymbol(ss);
			if (se != null && se.getAssetClass() != null) {
				String assetName = se.getAssetClass().getName();
				if (!assetsList.contains(assetName))
					assetsList.add(assetName);
			}
		}
		return assetsList;
	}

	public List<String> getPlanMajorAssets(List<String> assetsList) throws Exception {
		return getPlanMajorAssets(assetsList, true);
	}

	public int getMajorAssetCountByPlanID(Long planID) throws Exception {
		List<String> tickerList = getPlanTickers(planID);
		List<String> assetList = getPlanMinorAssets(tickerList);
		List<String> majorList = getPlanMajorAssets(assetList, false);
		if (majorList != null) {

			return majorList.size();
		}

		return 0;
	}

	public List<String> getMajorAssetByPlanID(Long planID) throws Exception {
		List<String> tickerList = getPlanTickers(planID);
		List<String> assetList = getPlanMinorAssets(tickerList);
		List<String> majorList = getPlanMajorAssets(assetList, false);
		if (majorList != null) {

			return majorList;
		}

		return null;
	}

	@Override
	public List<String> getPlanMajorAssets(List<String> assetsList, boolean other) throws Exception {
		List<String> majorList = new ArrayList<String>();
		Map<String, String> assetclass = new HashMap<String, String>();
		File csvFile = new File(URLDecoder.decode(com.lti.service.impl.StrategyManagerImpl.class.getResource("6_main_asset_classes.csv").getFile(), "utf-8"));
		CsvListReader clr = new CsvListReader(new FileReader(csvFile), CsvPreference.STANDARD_PREFERENCE);
		List<String> head = clr.read();
		String[] arrs = new String[head.size()];
		head.toArray(arrs);

		List<String> list = null;

		while ((list = clr.read()) != null) {
			for (int i = 0; i < 7; i++) {
				String cell = list.get(i).trim().toLowerCase();
				if (!cell.equals("")) {
					assetclass.put(cell, arrs[i]);
				}
			}

		}
		for (String asset : assetsList) {
			String major = assetclass.get(asset.trim().toLowerCase());
			if (major != null && other) {
				if (!majorList.contains(major)) {
					majorList.add(major);
				}
			} else if (major != null && !other) {
				if (!majorList.contains(major) && !major.equals("Other")) {
					majorList.add(major);
				}
			}
		}
		return majorList;
	}

	@Override
	public void deletePlanScoreByID(Long ID) throws Exception {
		String sql = "delete from " + Configuration.TABLE_PLANSCORE + " where planid=" + ID;
		this.executeSQL(sql);
	}

	@Override
	public void assignAdminPlanToUser(Long planID, Long userID) throws Exception {
		MYPLANIQPermissionManager mpm = new MYPLANIQPermissionManager();
		Strategy plan = this.get(planID);
		if (plan.getUserID() == Configuration.SUPER_USER_ID) {
			plan.setUserID(userID);
			this.update(plan);
			mpm.afterPlanCreate(userID, planID);
		}
	}

	public String getRiskyOrStableAsset(String assetClassName) {

		String risky = "Risky Asset";
		String stable = "Stable Asset";
		if (assetClassName.contains("High Yield")) {
			return risky;
		}
		if (assetClassName.startsWith("Muni") || assetClassName.endsWith("Muni")) {
			return stable;
		} else if (assetClassName.equalsIgnoreCase("Emerging Markets Bond") || assetClassName.equalsIgnoreCase("FIXED INCOME")) {
			return stable;
		} else if (assetClassName.equalsIgnoreCase("Foreign Corporate Bonds") || assetClassName.equalsIgnoreCase("Foreign Goverment Bonds")) {
			return stable;
		} else if (assetClassName.equalsIgnoreCase("CASH")) {
			return stable;
		} else if (assetClassName.equalsIgnoreCase("Inflation-Protected Bond") || assetClassName.equalsIgnoreCase("Intermediate Government")) {
			return stable;
		} else if (assetClassName.equalsIgnoreCase("Intermediate-Term Bond") || assetClassName.equalsIgnoreCase("INTERNATIONAL BONDS")) {
			return stable;
		} else if (assetClassName.equalsIgnoreCase("INVESTMENT GRADE") || assetClassName.equalsIgnoreCase("LONG GOVERNMENT")) {
			return stable;
		} else if (assetClassName.equalsIgnoreCase("Long-Term Bond") || assetClassName.equalsIgnoreCase("Multisector Bond")) {
			return stable;
		} else if (assetClassName.equalsIgnoreCase("SHORT GOVERNMENT") || assetClassName.equalsIgnoreCase("Short-Term Bond")) {
			return stable;
		} else if (assetClassName.equalsIgnoreCase("ULTRASHORT BOND") || assetClassName.equalsIgnoreCase("US CORPORATE BONDS")) {
			return stable;
		} else if (assetClassName.equalsIgnoreCase("US MUNICIPAL BONDS") || assetClassName.equalsIgnoreCase("US TREASURY BONDS")) {
			return stable;
		} else if (assetClassName.equalsIgnoreCase("WORLD BOND")) {
			return stable;
		}

		return risky;
	}

	public Map<String, List<VariableFor401k>> getSixMajorAssetClassForPlan(long planid) {
		StrategyManager sm = ContextHolder.getStrategyManager();
		SecurityManager sem = ContextHolder.getSecurityManager();
		AssetClassManager acm = ContextHolder.getAssetClassManager();
		Map<String, List<VariableFor401k>> map = new HashMap<String, List<VariableFor401k>>();
		Strategy plan = sm.get(planid);

		//
		List<VariableFor401k> variables = plan.getVariablesFor401k();

		if (variables == null)
			return null;
		for (VariableFor401k var : variables) {
			if (var.getSymbol().equals("unknown"))
				continue;
			try {
				var.setQuality(sem.getBySymbol(var.getSymbol()).getQuality());
			} catch (Exception e) {
				e.printStackTrace();
			}

			String assetClassName = var.getAssetClassName();
			String resultAssetClass = "Other";
			if (acm.isUpperOrSameClass("US EQUITY", assetClassName))
				resultAssetClass = "US EQUITY";
			else if (acm.isUpperOrSameClass("INTERNATIONAL EQUITY", assetClassName))
				resultAssetClass = "INTERNATIONAL EQUITY";
			else if (acm.isUpperOrSameClass("Emerging Market", assetClassName))
				resultAssetClass = "Emerging Market";
			else if (acm.isUpperOrSameClass("COMMODITIES", assetClassName))
				resultAssetClass = "COMMODITIES";
			else if (acm.isUpperOrSameClass("FIXED INCOME", assetClassName))
				resultAssetClass = "FIXED INCOME";
			else if (acm.isUpperOrSameClass("REAL ESTATE", assetClassName))
				resultAssetClass = "REAL ESTATE";

			if (map.get(resultAssetClass) != null) {
				map.get(resultAssetClass).add(var);
			} else {
				List<VariableFor401k> lists = new ArrayList<VariableFor401k>();
				lists.add(var);
				map.put(resultAssetClass, lists);
			}
		}
		Set<String> set = map.keySet();
		for (String ss : set) {
			List<VariableFor401k> lists = map.get(ss);
			if (lists == null) {
				lists = new ArrayList<VariableFor401k>();
			}
			lists = Sort.variableSort(lists);
			map.put(ss, lists);
		}
		return map;
	}

	@Override
	public List<ThirdPartyResource> getThirdPartyResources(String thirdParty) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ThirdPartyResource.class);
		detachedCriteria.add(Restrictions.eq("ThirdParty", thirdParty));
		return this.findByCriteria(detachedCriteria);
	}

	@Override
	public void updateThirdPartyResources(String thirdParty, List<ThirdPartyResource> trs) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ThirdPartyResource.class);
		detachedCriteria.add(Restrictions.eq("ThirdParty", thirdParty));

		List<ThirdPartyResource> otrs = this.findByCriteria(detachedCriteria);
		if (otrs != null && otrs.size() > 0) {
			for (ThirdPartyResource tr : otrs) {
				getHibernateTemplate().delete(tr);
			}
		}

		if (trs != null && trs.size() > 0) {
			for (ThirdPartyResource tr : trs) {
				getHibernateTemplate().save(tr);
			}
		}
	}

	@Override
	public List<ThirdParty> getThirdParties() {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ThirdParty.class);
		return this.findByCriteria(detachedCriteria);
	}

	@Override
	public void updateThirdParties(List<ThirdParty> trs) {

		if (trs != null && trs.size() > 0) {
			for (ThirdParty tr : trs) {
				ThirdParty t = (ThirdParty) getHibernateTemplate().get(ThirdParty.class, tr.getThirdPartyID());
				if (t == null) {
					getHibernateTemplate().save(tr);
				} else {
					t.setParameters(tr.getParameters());
					t.setThirdParty(t.getThirdParty());
					getHibernateTemplate().update(t);
				}

			}
		}
	}

	@Override
	public ThirdParty getThirdParty(Long tid) {

		ThirdParty t = (ThirdParty) getHibernateTemplate().get(ThirdParty.class, tid);
		return t;
	}

	@Override
	public List<Object[]> searchPlanByThirdParty(String thirdparty, String keyword, int size) throws Exception {
		String sql = "SELECT p.id,p.name FROM ltisystem_strategy p, ltisystem_third_party_resource t where t.thirdparty=\"" + thirdparty + "\" and ( (t.resourcetype=1 and p.plantype=t.resourceid ) or (t.resourcetype=0 and p.id=t.resourceid)) and p.name like \"%" + keyword + "%\" limit 0," + size + ";";
		// System.out.println(sql);
		return super.findBySQL(sql);
	}

	@Override
	public List<Long> getModelPortfolioIDs(Long planID) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Portfolio.class);
		detachedCriteria.add(Restrictions.eq("MainStrategyID", planID));
		detachedCriteria.setProjection(Projections.property("ID"));
		return this.findByCriteria(detachedCriteria);
	}

}
