package com.lti.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.lti.service.InviteManager;
import com.lti.service.bo.InviteCode;
import com.lti.service.bo.User;

public class InviteManagerImpl extends DAOManagerImpl implements InviteManager, Serializable {
	private SessionFactory sessionFactory;
	@Override
	public Boolean insert(InviteCode inviteCode) {
		try{
			getHibernateTemplate().save(inviteCode);
			return true;
		}catch(Exception e){
			return false;
		}
		
	}
	
	public InviteCode getInviteId(String inviteCode){
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(InviteCode.class);
		detachedCriteria.add(Restrictions.eq("InviteCode", inviteCode));
		List<com.lti.service.bo.InviteCode> bolist = findByCriteria(detachedCriteria);
		if (bolist.size() >= 1)
			return bolist.get(0);
		else
			return null;
	}
	
	public InviteCode get(long id) {
		// TODO Auto-generated method stub

		return (InviteCode) getHibernateTemplate().get(InviteCode.class, id);
	}
	
	public List<InviteCode> getAll(){
		List<InviteCode> list = new ArrayList<InviteCode>();
		list = getHibernateTemplate().find("from InviteCode inviteCode order by inviteCode.ID");
		return list;
	}
	
	public void delById(long id){
		InviteCode inviteCode = new InviteCode();
		inviteCode = (InviteCode)getHibernateTemplate().get(InviteCode.class, id);
		getHibernateTemplate().delete(inviteCode);
	}

}
