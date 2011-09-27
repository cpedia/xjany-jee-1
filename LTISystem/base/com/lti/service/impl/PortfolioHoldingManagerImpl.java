package com.lti.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.lti.service.BaseFormulaManager;
import com.lti.service.PortfolioHoldingManager;
import com.lti.service.bo.HoldingItem;
import com.lti.util.LTIDate;

public class PortfolioHoldingManagerImpl extends DAOManagerImpl implements PortfolioHoldingManager,Serializable {
	private static final long serialVersionUID = 1L;

	private BaseFormulaManager baseFormulaManager;

	/** *********************************************************** */
	/* ==fields== end */
	/** *********************************************************** */

	/** *********************************************************** */
	/* ==set method for spring==Start */
	/** *********************************************************** */

	public void setBaseFormulaManager(BaseFormulaManager baseFormulaManager) {

		this.baseFormulaManager = baseFormulaManager;

	}

	@Override
	public void delete(Long ID) throws Exception{
		// TODO Auto-generated method stub
		try {
			deleteByHQL("from PortfolioHolding where ID=" + ID);
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
	}

	@Override
	public void deletePortfolioHolding(Long portfolioID) throws Exception{
		// TODO Auto-generated method stub
		try {
			deleteByHQL("from PortfolioHolding where portfolioID=" + portfolioID);
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
	}

	@Override
	public List<HoldingItem> getHoldingItems(Long portfolioID, Date date){
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(HoldingItem.class);
		detachedCriteria.add(Restrictions.eq("portfolioID", portfolioID));
		detachedCriteria.add(Restrictions.eq("date",date));
		List<HoldingItem> his = findByCriteria(detachedCriteria);
		if(his != null && his.size() != 0)
			return his;
		return null;
	}
	
	
	/*************************added for holding items********************************************/
	@Override
	public void addAllHoldingItems(List<HoldingItem> holdingItemList) throws Exception {
		super.saveAll(holdingItemList);
	}
	@Override
	public void addHoldingItem(HoldingItem holdingItem) throws Exception {
		try{
			getHibernateTemplate().save(holdingItem);
		}catch(Exception e){
			throw e;
		}
	}
	@Override
	public void deleteAllHoldingItems(Long portfolioID) throws Exception {
		try {
			deleteByHQL("from holdingitems where portfolioID=" + portfolioID);
		} catch (Exception e) {
			throw e;
		}
	}
	@Override
	public void deleteHoldingItem(Long ID) throws Exception {
		try {
			deleteByHQL("from holdingitems where ID=" + ID);
		} catch (Exception e) {
			throw e;
		}
	}
	@Override
	public HoldingItem getHoldItem(Long ID) {
		if (ID == null)
			return null;
		return (HoldingItem) getHibernateTemplate().get(HoldingItem.class, ID);
	}
	@Override
	public List<HoldingItem> getAllHoldingItemsAfter(Long portfolioID, Date date) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(HoldingItem.class);
		detachedCriteria.add(Restrictions.eq("portfolioID", portfolioID));
		if(date!=null)detachedCriteria.add(Restrictions.gt("date", date));
		detachedCriteria.addOrder(Order.asc("date"));
		List<HoldingItem>holdingItemList = findByCriteria(detachedCriteria);
		return holdingItemList;
	}
	@Override
	public List<HoldingItem> getLatestHoldingItemsBefore(Long portfolioID, Date date) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(HoldingItem.class);
		detachedCriteria.add(Restrictions.eq("portfolioID", portfolioID));
		detachedCriteria.add(Restrictions.lt("date", date));
		detachedCriteria.addOrder(Order.desc("date"));
		List<HoldingItem> holdingItemList = findByCriteria(detachedCriteria, 1, 0);
		if(holdingItemList != null){
			Date hDate = holdingItemList.get(0).getDate();
			DetachedCriteria detachedCriteria1 = DetachedCriteria.forClass(HoldingItem.class);
			detachedCriteria1.add(Restrictions.eq("portfolioID", portfolioID));
			detachedCriteria1.add(Restrictions.eq("date", hDate));
			holdingItemList = findByCriteria(detachedCriteria1);
		}
		return holdingItemList;
	}
	public List<Date> getHoldingDateAfter(Long portfolioID, Date date){
		String holdDate = LTIDate.parseDateToString(date);
		String sql= "SELECT distinct date FROM ltisystem.ltisystem_holdingitem where date > \'" + holdDate + "\' and portfolioid = " + portfolioID + " order by date desc";
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		SQLQuery query = session.createSQLQuery(sql);
		List<Date> dateList= (List<Date>)query.list();
		return dateList;
	}
	public HoldingItem getLatestHoldingItemsBeforeBySecurityID(Long portfolioID, Long securityID, Date date){
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(HoldingItem.class);
		detachedCriteria.add(Restrictions.eq("portfolioID", portfolioID));
		detachedCriteria.add(Restrictions.lt("date", date));
		detachedCriteria.addOrder(Order.desc("date"));
		List<HoldingItem> holdingItemList = findByCriteria(detachedCriteria, 1, 0);
		if(holdingItemList != null){
			Date hDate = holdingItemList.get(0).getDate();
			DetachedCriteria detachedCriteria1 = DetachedCriteria.forClass(HoldingItem.class);
			detachedCriteria1.add(Restrictions.eq("portfolioID", portfolioID));
			detachedCriteria1.add(Restrictions.eq("securityID", securityID));
			detachedCriteria1.add(Restrictions.eq("date", hDate));
			holdingItemList = findByCriteria(detachedCriteria1);
		}
		if(holdingItemList != null && holdingItemList.size()>0 )
			return holdingItemList.get(0);
		return null;
	}
	@Override
	public void updateHoldingItem(HoldingItem holdingItem) throws Exception {
		try {
			getHibernateTemplate().update(holdingItem);
		} catch (Exception e) {
			throw e;
		}
	}
	@Override
	public HoldingItem getOneHoldItemBefore(Long portfolioID, Long securityID, Date date) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(HoldingItem.class);
		detachedCriteria.add(Restrictions.eq("portfolioID", portfolioID));
		detachedCriteria.add(Restrictions.eq("securityID", securityID));
		detachedCriteria.add(Restrictions.le("date", date));
		detachedCriteria.addOrder(Order.desc("date"));
		List<HoldingItem> holdingItemList = findByCriteria(detachedCriteria, 1, 0);
		if(holdingItemList != null && holdingItemList.size() > 0)
			return holdingItemList.get(0);
		return null;
	}
	@Override
	public List<HoldingItem> getLatestHoldingItems(Long portfolioID, Date date){
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(HoldingItem.class);
		detachedCriteria.add(Restrictions.eq("portfolioID", portfolioID));
		detachedCriteria.add(Restrictions.le("date", date));
		detachedCriteria.addOrder(Order.desc("date"));
		List<HoldingItem> holdingItemList = findByCriteria(detachedCriteria, 1, 0);
		if(holdingItemList != null && holdingItemList.size()> 0){
			Date hDate = holdingItemList.get(0).getDate();
			DetachedCriteria detachedCriteria1 = DetachedCriteria.forClass(HoldingItem.class);
			detachedCriteria1.add(Restrictions.eq("portfolioID", portfolioID));
			detachedCriteria1.add(Restrictions.eq("date", hDate));
			holdingItemList = findByCriteria(detachedCriteria1);
			return holdingItemList;
		}
		return null;
	}
	@Override
	public List<HoldingItem> getAllHoldItemAfter(Long portfolioID, Long securityID, Date date) {
		List<Date> dateList = this.getHoldingDateAfter(portfolioID, date);
		if(dateList==null || dateList.size()==0)
			return null;
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(HoldingItem.class);
		detachedCriteria.add(Restrictions.eq("portfolioID", portfolioID));
		detachedCriteria.add(Restrictions.eq("securityID", securityID));
		detachedCriteria.add(Restrictions.in("date", dateList));
		detachedCriteria.addOrder(Order.desc("date"));
		List<HoldingItem> holdingItemList = findByCriteria(detachedCriteria);
		return holdingItemList;
	}
	@Override
	public HoldingItem getLatestHoldItem(Long portfolioID, Long securityID, Date date) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(HoldingItem.class);
		detachedCriteria.add(Restrictions.eq("portfolioID", portfolioID));
		detachedCriteria.add(Restrictions.eq("securityID", securityID));
		detachedCriteria.add(Restrictions.gt("date", date));
		detachedCriteria.addOrder(Order.desc("date"));
		List<HoldingItem> holdingItemList = findByCriteria(detachedCriteria,1,0);
		if( holdingItemList!=null && holdingItemList.size()>0)
			return holdingItemList.get(0);
		return null;
	}
	@Override
	public List<HoldingItem> getHoldingItemsAfter(Long portfolioID, Long securityID, Date date){
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(HoldingItem.class);
		detachedCriteria.add(Restrictions.eq("portfolioID", portfolioID));
		detachedCriteria.add(Restrictions.eq("securityID", securityID));
		detachedCriteria.add(Restrictions.ge("date", date));
		return findByCriteria(detachedCriteria);
	}

	@Override
	public HoldingItem getHoldingItem(Long portfolioID, Long securityID, Date date) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(HoldingItem.class);
		detachedCriteria.add(Restrictions.eq("portfolioID", portfolioID));
		detachedCriteria.add(Restrictions.eq("securityID", securityID));
		detachedCriteria.add(Restrictions.eq("date", date));
		List<HoldingItem> hiList = findByCriteria(detachedCriteria);
		if(hiList != null && hiList.size() == 1)
			return hiList.get(0);
		return null;
	}
	
}
