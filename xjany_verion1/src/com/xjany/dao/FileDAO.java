package com.xjany.dao;

import java.util.List;

import com.xjany.entity.File;

public interface FileDAO extends GenericDAO<File,Integer>{
	public void save(File file,int upId);
	public List<File> findByUpId(int upId);
}
