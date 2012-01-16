package com.xjany.bbs.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xjany.bbs.dao.FileDAO;
import com.xjany.bbs.dao.GenericDAO;
import com.xjany.bbs.dao.GroupDAO;
import com.xjany.bbs.dao.UserDAO;
import com.xjany.bbs.entity.AllUserGroup;
import com.xjany.bbs.service.GroupService;
import com.xjany.common.page.Pagination;
@Service
@Transactional
public class GroupServiceImpl implements GroupService {
	
	private GroupDAO groupDAO;

	public GroupDAO getGroupDAO() {
		return groupDAO;
	}
	@Resource
	public void setGroupDAO(GroupDAO groupDAO) {
		this.groupDAO = groupDAO;
	}
	@Override
	public List findGroupBySql() {
		String sql = "select bean.groupName from all_user_group bean";
		List list = groupDAO.findBySql(sql);
		return list;
	}
	@Override
	public AllUserGroup findById(int id) {
		return groupDAO.findById(id);
	}
	@Override
	public Pagination getPage(int pageNo, int pageSize) {
		Pagination pagination = groupDAO.getPage(pageNo, pageSize);
		return pagination;
	}
	@Override
	public void save(AllUserGroup allUserGroup) {
		groupDAO.save(allUserGroup);
	}

}
