package com.xjany.bbs.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xjany.bbs.dao.ResourceDAO;
import com.xjany.bbs.entity.AllResLibCTG;
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
		if(parentResLib!=null){
			parentResLib.setIsNote(1);
			resourceDAO.update(parentResLib);//设置父级有子叶
		}
		return resourceDAO.save(allResLibrary, parentId);
	}

	@Override
	public List<AllResLibrary> findByParentId(int parentId) {
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
	
	@Override
	public AllResLibCTG findCTGById(int id){
		return resourceDAO.findCTGById(id);
	}
	
	@Override
	public List<AllResLibCTG> findAllCTG(){
		return resourceDAO.findAllCTG();
	}
	
	/**
	 * 删除节点下的所有子节点
	 * @param id
	 */
	private void deleteHelp(int id){
		List<?> ids = resourceDAO.findBySql("select bean.libId from all_res_lib bean where parentId = "+id);
		resourceDAO.delete(id);
		for(int i=0;i<ids.size();i++){
			deleteHelp((Integer) ids.get(i));
		}
	}

}
