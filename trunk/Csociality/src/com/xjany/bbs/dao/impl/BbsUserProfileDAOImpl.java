package com.xjany.bbs.dao.impl;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.xjany.bbs.dao.BbsUserProfileDAO;
import com.xjany.bbs.entity.BbsUserProfile;

@Repository
public class BbsUserProfileDAOImpl extends GeneriDAOImpl<BbsUserProfile, Integer> implements BbsUserProfileDAO {

}
