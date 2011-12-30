package com.xjany.dao.impl;


import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import com.xjany.dao.FileDAO;
import com.xjany.entity.File;

@Repository
public class FileDAOImpl extends GeneriDAOImpl<File, Integer> implements FileDAO{
	public void save(File file,int upId) {
		Session session = sessionFactory.getCurrentSession();
		file.setUpId(upId);
		session.save(file);
	}
	
	@Transactional(readOnly = true)
	public List<File> findByUpId(int upId) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery( "from File as a where a.upId= ? order by path");
		query.setParameter(0, upId);
		List<File> list = query.list();
		return list;
	}
}
