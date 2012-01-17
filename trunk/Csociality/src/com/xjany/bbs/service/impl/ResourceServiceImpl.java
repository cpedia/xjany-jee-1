package com.xjany.bbs.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xjany.bbs.dao.ResourceDAO;
import com.xjany.bbs.entity.AllResLibrary;
import com.xjany.bbs.service.ResourceService;

@Service
@Transactional
public class ResourceServiceImpl implements ResourceService {

	private ResourceDAO resourceDAO;

	@Resource
	public void setResourceDAO(ResourceDAO resourceDAO) {
		this.resourceDAO = resourceDAO;
	}

	@Override
	public AllResLibrary save(AllResLibrary allResLibrary, int parentId) {
		return resourceDAO.save(allResLibrary, parentId);
	}

	@Override
	public List<AllResLibrary> listAllResLibrary(int parentId) {
		return resourceDAO.findByParentId(parentId);
	}

	@Override
	public AllResLibrary findById(int id) {
		return resourceDAO.findById(id);
	}

}
