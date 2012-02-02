package com.xjany.bbs.service;

import java.util.List;

import com.xjany.bbs.entity.AllResLibCTG;
import com.xjany.bbs.entity.AllResLibrary;

public interface ResourceService
{
	public AllResLibrary save(AllResLibrary allResLibrary,int parentId);
	public AllResLibrary findById(int id);
	public AllResLibrary deleteById(int id);
	public List<AllResLibrary> findByParentId(int parentId);
	public AllResLibCTG findCTGById(int id);
	public List<AllResLibCTG> findAllCTG();
}
