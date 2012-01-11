package com.xjany.bbs.service.impl;


import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xjany.bbs.dao.BbsUserProfileDAO;
import com.xjany.bbs.entity.BbsUserProfile;
import com.xjany.bbs.service.BbsUserProfileService;
@Service
@Transactional
public class BbsUserProfileServiceImpl implements BbsUserProfileService {
	
	private BbsUserProfileDAO bbsUserProfileDAO;

	public BbsUserProfileDAO getGroupDAO() {
		return bbsUserProfileDAO;
	}
	@Resource
	public void setGroupDAO(BbsUserProfileDAO bbsUserProfileDAO) {
		this.bbsUserProfileDAO = bbsUserProfileDAO;
	}
	
	@Override
	public void save(BbsUserProfile entity)
	{
		bbsUserProfileDAO.save(entity);
	}

}
