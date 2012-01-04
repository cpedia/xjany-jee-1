package com.xjany.bbs.dao;

import java.util.List;

import com.xjany.bbs.entity.File;

public interface FileDAO extends GenericDAO<File,Integer>{
	public void save(File file,int upId);
	public List<File> findByUpId(int upId);
}
