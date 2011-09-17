package com.jeecms.cms.manager.assist.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.cms.dao.assist.XjanyPicListDao;
import com.jeecms.cms.entity.assist.XjanyPicList;
import com.jeecms.cms.manager.assist.XjanyPicListMng;
import com.jeecms.common.hibernate3.Updater;

@Service
@Transactional
public class XjanyPicListMngImpl implements XjanyPicListMng {
	@Transactional(readOnly = true)
	public List<XjanyPicList> getList() {
		List<XjanyPicList> list = dao.getList();
		return list;
	}


	@Transactional(readOnly = true)
	public XjanyPicList findById(Integer id) {
		XjanyPicList entity = dao.findById(id);
		return entity;
	}

	public int updateViews(Integer id) {
		
		return 0;
	}

	public XjanyPicList save(XjanyPicList bean) {
		dao.save(bean);
		return bean;
	}

	public XjanyPicList update(XjanyPicList bean) {
		Updater<XjanyPicList> updater = new Updater<XjanyPicList>(bean);
		bean = dao.updateByUpdater(updater);
		return bean;
	}


	public XjanyPicList deleteById(Integer id) {
		XjanyPicList bean = dao.deleteById(id);
		return bean;
	}

	public XjanyPicList[] deleteByIds(Integer[] ids) {
		XjanyPicList[] beans = new XjanyPicList[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	private XjanyPicListDao dao;

	@Autowired
	public void setDao(XjanyPicListDao dao) {
		this.dao = dao;
	}

}