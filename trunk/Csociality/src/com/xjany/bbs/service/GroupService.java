package com.xjany.bbs.service;

import java.util.List;

import com.xjany.bbs.entity.AllUserGroup;


public interface GroupService {
	public List findGroupBySql();
	public List<AllUserGroup> findById(int id );
}
