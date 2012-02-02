package com.xjany.bbs.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xjany.bbs.dao.GroupDAO;
import com.xjany.bbs.entity.AllUserGroup;
import com.xjany.bbs.service.GroupService;
import com.xjany.common.exception.DaoException;
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
	public List<?> findGroupBySql() {
		String sql = "select bean.groupName from all_user_group bean";
		List<?> list = groupDAO.findBySql(sql);
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
	public AllUserGroup save(AllUserGroup allUserGroup) {
		return groupDAO.save(allUserGroup);
	}
	@Override
	public List<AllUserGroup> findAll() {
		return groupDAO.findAll();
	}
	@Override
	public AllUserGroup deleteById(int id) throws DaoException{
		groupDAO.delete(id);
		return null;
	}

}
