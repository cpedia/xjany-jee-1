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
		AllResLibrary parentResLib = resourceDAO.findById(parentId);
		parentResLib.setIsNote(1);
		resourceDAO.update(parentResLib);//设置父级有子叶
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

	@Override
	public AllResLibrary deleteById(int id) {
		deleteHelp(id);
		return null;
	}
	
	private int deleteHelp(int id){
		List ids = resourceDAO.findBySql("select bean.libId from all_res_lib bean where parentId = "+id);
		if(ids.size()<1) {
			resourceDAO.delete(id);
			return 1;
		}
		for(int i=0;i<ids.size();i++){
			deleteHelp((Integer) ids.get(i));
		}
		return 0;
	}

}
