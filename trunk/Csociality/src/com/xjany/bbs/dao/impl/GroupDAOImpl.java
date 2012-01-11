package com.xjany.bbs.dao.impl;

import org.springframework.stereotype.Repository;

import com.xjany.bbs.dao.GroupDAO;
import com.xjany.bbs.entity.AllUserGroup;
@Repository
public class GroupDAOImpl extends GeneriDAOImpl<AllUserGroup,Integer> implements GroupDAO
{
}
