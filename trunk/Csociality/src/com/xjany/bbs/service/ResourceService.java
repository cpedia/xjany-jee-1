package com.xjany.bbs.service;

import java.util.List;

import com.xjany.bbs.entity.AllResLibrary;

public interface ResourceService
{
	public AllResLibrary save(AllResLibrary allResLibrary,int parentId);
	public List<AllResLibrary> listAllResLibrary(int parentId);
	public AllResLibrary findById(int id);
	public AllResLibrary deleteById(int id);
}
