package com.lti.service;

import java.util.List;

import com.lti.service.bo.CompanyIndex;


public interface CompanyIndexManager {
	/**
	 * remove asset class by id, single
	 * @param id
	 */
	public void remove(long id);

	public CompanyIndex get(long id);

	public void save(CompanyIndex c);

	public void saveOrUpdate(CompanyIndex c);

	public void update(CompanyIndex c);

	public List<CompanyIndex> getAll();

	public List<CompanyIndex> get(String name,String type);
	
	public void deleteByHQL(String string);
	
	public void importCompanyIndex(String file) throws Exception;

	public CompanyIndex get(String filename);
	
	public List findBySQL(String sql)throws Exception;

	
}
