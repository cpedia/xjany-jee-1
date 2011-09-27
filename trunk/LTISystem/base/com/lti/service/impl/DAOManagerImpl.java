package com.lti.service.impl;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.lti.system.ContextHolder;
import com.lti.type.PaginationSupport;
import com.lti.util.HibernateUtils;
import com.lti.util.StringUtil;

public class DAOManagerImpl extends HibernateDaoSupport implements Serializable {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("rawtypes")
	public void saveAll(Collection objs) {
		if (objs == null || objs.size() == 0)
			return;

		Session session = null;
		org.hibernate.Transaction tx = null;

		try {
			session = getHibernateTemplate().getSessionFactory().openSession();
			tx = session.beginTransaction();
			for (int i = 0; i < objs.size(); i++) {
				session.save(((List) objs).get(i));
				if (i % 2000 == 1999 && i != (objs.size() - 1)) {
					session.flush();
					session.clear();
					tx.commit();
					tx = session.beginTransaction();
				}
			}
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

	@SuppressWarnings("rawtypes")
	public void saveOrUpdateAll(Collection objs) {
		if (objs == null || objs.size() == 0)
			return;

		Session session = null;
		org.hibernate.Transaction tx = null;
		try {
			session = getHibernateTemplate().getSessionFactory().openSession();
			tx = session.beginTransaction();
			for (int i = 0; i < objs.size(); i++) {
				session.saveOrUpdate(((List) objs).get(i));
				if (i % 2000 == 1999 && i != (objs.size() - 1)) {
					session.flush();
					session.clear();
					tx.commit();
					tx = session.beginTransaction();
				}
			}
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
	@SuppressWarnings("rawtypes")
	public void removeAll(Collection objs) {
		if (objs == null || objs.size() == 0)
			return;

		Session session = null;
		org.hibernate.Transaction tx = null;
		try {
			session = getHibernateTemplate().getSessionFactory().openSession();
			tx = session.beginTransaction();
			for (int i = 0; i < objs.size(); i++) {
				session.delete(((List) objs).get(i));
				if (i % 2000 == 1999 && i != (objs.size() - 1)) {
					session.flush();
					session.clear();
					tx.commit();
					tx = session.beginTransaction();
				}
			}
			session.flush();
			tx.commit();
			session.clear();
			session.close();
		} catch (Exception e) {
			if (tx.isActive())
				tx.rollback();
			if (session.isOpen())
				session.close();
			throw new RuntimeException("remove error.", e);
		}

	}

	@SuppressWarnings("rawtypes")
	public List findByCriteria(final DetachedCriteria detachedCriteria) {
		Session session = null;

		List list;
		try {
			session = getHibernateTemplate().getSessionFactory().openSession();
			Criteria criteria = detachedCriteria.getExecutableCriteria(session);
			list = criteria.list();
			return list;
		} catch (Exception e) {
			throw new RuntimeException("findPageByCriteria sql error.", e);
		} finally {
			if (session != null && session.isOpen())
				session.close();
		}

	}

	@SuppressWarnings("rawtypes")
	public List findByCriteria(final DetachedCriteria detachedCriteria, final int pageSize, final int startIndex) {

		Session session = null;

		List list;
		try {
			session = getHibernateTemplate().getSessionFactory().openSession();
			Criteria criteria = detachedCriteria.getExecutableCriteria(session);
			list = criteria.setFirstResult(startIndex).setMaxResults(pageSize).list();
		} catch (Exception e) {
			throw new RuntimeException("findPageByCriteria sql error.", e);
		} finally {
			if (session != null && session.isOpen())
				session.close();
		}
		return list;

	}
	@SuppressWarnings("rawtypes")
	public PaginationSupport findPageByCriteria(final DetachedCriteria detachedCriteria, final int pageSize, final int startIndex) {

		Session session = null;

		PaginationSupport ps;
		try {
			session = getHibernateTemplate().getSessionFactory().openSession();
			Criteria criteria = detachedCriteria.getExecutableCriteria(session);
			int totalCount = ((Integer) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
			criteria.setProjection(null);
			List items;
			if (pageSize != 0) {
				items = criteria.setFirstResult(startIndex).setMaxResults(pageSize).list();
				ps = new PaginationSupport(items, totalCount, pageSize, startIndex);
			} else {
				items = criteria.setFirstResult(startIndex).list();
				ps = new PaginationSupport(items, totalCount, totalCount - startIndex, startIndex);
			}
			return ps;
		} catch (Exception e) {
			throw new RuntimeException("findPageByCriteria sql error.", e);
		} finally {
			if (session != null && session.isOpen())
				session.close();
		}

	}

	@SuppressWarnings("rawtypes")
	public void deleteByHQL(final String hql) {

		Session session = null;

		List list;
		try {
			session = getHibernateTemplate().getSessionFactory().openSession();
			list = session.createQuery(hql).list();
			session.close();
		} catch (Exception e) {
			throw new RuntimeException("deleteByHQL sql error.", e);
		} finally {
			if (session != null && session.isOpen())
				session.close();
		}

		if (list != null && list.size() != 0)
			getHibernateTemplate().deleteAll(list);

	}

	@SuppressWarnings("rawtypes")
	public PaginationSupport findPageByHQL(final String hql, final int pageSize, final int startIndex) {

		Session session = null;

		PaginationSupport ps;
		try {
			session = getHibernateTemplate().getSessionFactory().openSession();

			String countQueryString = " select count (*) " + HibernateUtils.removeSelect(HibernateUtils.removeOrders(hql));
			Query counterQuery = session.createQuery(countQueryString);

			List countList = counterQuery.list();
			Integer totalCount = ((Long) countList.get(0)).intValue();

			Query query = session.createQuery(hql);

			List items = query.setFirstResult(startIndex).setMaxResults(pageSize).list();
			ps = new PaginationSupport(items, totalCount, pageSize, startIndex);

			return ps;
		} catch (Exception e) {
			throw new RuntimeException("findByHQL sql error.", e);
		} finally {
			if (session != null && session.isOpen())
				session.close();
		}

	}
	@SuppressWarnings("rawtypes")
	public List findByHQL(final String hql) {
		return findByHQL(hql, 0);
	}

	@SuppressWarnings("rawtypes")
	public List findByHQL(final String hql, int size) {

		Session session = null;

		List result;
		try {
			session = getHibernateTemplate().getSessionFactory().openSession();

			Query query = session.createQuery(hql);
			if (size != 0) {
				query.setFirstResult(0);
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

	public void executeSQL(String sql) throws Exception {
		if (sql == null)
			return;
		List<String> sqls = new ArrayList<String>();
		sqls.add(sql);
		executeSQL(sqls);
	}

	public void executeSQL(List<String> sqls) throws Exception {
		if (sqls == null)
			return;

		Session session = null;
		Transaction tx = null;
		try {
			session = getHibernateTemplate().getSessionFactory().openSession();
			tx = session.beginTransaction();
			for (int i = 0; i < sqls.size(); i++) {
				Query query = session.createSQLQuery(sqls.get(i));
				query.executeUpdate();
			}
			tx.commit();
		} catch (Exception e) {
			throw new RuntimeException("execute sql error.", e);
		} finally {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
			if (session != null && session.isOpen())
				session.close();
		}
	}

	@SuppressWarnings("rawtypes")
	public List findBySQL(String sql) throws Exception {
		if (sql == null)
			return null;
		Session session = null;
		try {
			session = getHibernateTemplate().getSessionFactory().openSession();
			Query query = session.createSQLQuery(sql);
			List list = query.list();
			return list;
		} catch (Exception e) {
			throw e;
		} finally {
			if (session != null && session.isOpen())
				session.close();
		}
	}
	@SuppressWarnings({ "unused" })
	public static void main(String[] args) {
		SecurityManagerImpl sm = (SecurityManagerImpl) ContextHolder.getSecurityManager();
		for (long i = 0; i < 10; i++) {
			System.out.println(sm.get(i));
		}
		DAOManagerImpl dao = (DAOManagerImpl) sm;
		List<Session> ss = new ArrayList<Session>();
		for (int i = 0; i < 40; i++) {
			try {
				// ss.add(dao.test());
			} catch (RuntimeException e) {
				System.out.println(i);
				// e.printStackTrace();
				System.out.println(StringUtil.getStackTraceString(e));
			}
		}
		for (long i = 0; i < 10; i++) {
			System.out.println(sm.get(i));
		}
	}

	// public List<Strategy> searchPlan(String portfolioIDs)
	// {
	// List<Portfolio> list = searchPortfolio(portfolioIDs);
	// Long[] longAry = new Long[list.size()];
	// String[] str = new String[list.size()];
	// Date[] dates = new Date[list.size()];
	// for(int i = 0; i <list.size(); i++)
	// {
	// longAry[i] = list.get(i).getMainStrategyID();
	// //获取Portfolio表中的MainStrategyID
	// dates[i] = list.get(i).getEndDate(); //获取Portfolio表中的MainStrategyID
	// if(dates[i] == dates[i-1])
	// str[i] = null;
	// }
	// Collections.sort(dates);
	// Session session = null;
	// session = getHibernateTemplate().getSessionFactory().openSession();
	// Query query = session.createQuery("from Strategy s where s.ID in (:id)");
	// query.setParameterList("id", longAry);
	// List<Strategy> l = query.list();
	// session.flush();
	// session.clear();
	// session.close();
	// return l;
	// }

	/*
	 * whj
	 */
	public static String display(String[] a) {
		String msg = "";
		Hashtable<String, Integer> h = new Hashtable<String, Integer>();
		Integer value;
		for (int i = 0; i < a.length; i++) {
			value = h.get(a[i]);
			if (value != null)
				value++;
			else
				value = 1;
			h.put(a[i], value);
		}
		Enumeration<String> keys = h.keys();
		Integer maxValue = 0;
		// String maxValueKey = "";
		String key;
		while (keys.hasMoreElements()) {
			key = keys.nextElement();
			value = h.get(key);
			if (value > maxValue) {
				maxValue = value;
				// maxValueKey = key;
			}
			System.out.println(key + " All update: " + value + "s.");
			msg += key + " All update: " + value + "s.name=";
		}
		// System.out.println("更新次数最多的是" + maxValueKey + "，共更新" + maxValue +
		// "个。\nnext\n");
		return msg;
	}

	/*
	 * whj
	 */
	public static Date convertDate(String adateStrteStr, String format) {
		java.util.Date date = null;
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
			date = simpleDateFormat.parse(adateStrteStr);

			simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			System.out.println(simpleDateFormat.format(date));

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return date;
	}
	// public List<EmailNotification> inRortfolio(String portfolio)
	// {
	// Session session = null;
	// session = getHibernateTemplate().getSessionFactory().openSession();
	// String[] strAry = portfolio.split(",");
	// Long[] longAry = new Long[strAry.length];
	// for (int i = 0; i < strAry.length; i++)
	// {
	// longAry[i] = java.lang.Long.parseLong(strAry[i]);
	// }
	// Query query =
	// session.createQuery("from EmailNotification e where e.UserID=1 and e.PortfolioID in (:portfolioID)");
	// query.setParameterList("portfolioID", longAry);
	// List<EmailNotification> listEmail = query.list();
	// session.flush();
	// session.clear();
	// session.close();
	// return listEmail;
	// }
	//
	// public List<EmailNotification> arrangementPortfolio(String portfolioID)
	// {
	// String[] strAry = portfolioID.split(",");
	// List<EmailNotification> portfolioList = inRortfolio(portfolioID);
	//
	// String[] twoArray = new String[strAry.length+portfolioList.size()];
	// System.arraycopy(strAry, 0, twoArray, 0, strAry.length);
	// System.arraycopy(portfolioList.toArray(new String[0]), 0, twoArray,
	// strAry.length, portfolioList.size());
	//
	// //得到相同元素
	// portfolioList.retainAll(Arrays.asList(strAry));
	// //此时rs中的数据即为相同的数据1,3,4
	//
	// //不能通过Arrays.asList(twoArray))得到，因为该方法得到的一个List对象是size固定的
	// List<EmailNotification> listportfolioID = new
	// ArrayList<EmailNotification>();
	// listportfolioID.add((EmailNotification) Arrays.asList(twoArray));
	// listportfolioID.removeAll(portfolioList);
	// return listportfolioID;
	// }

}
