package com.lti.service;

import java.util.List;

import com.lti.service.bo.StrategyClass;
import com.lti.type.Menu;


public interface StrategyClassManager  {

	/**
	 * remove asset class by id, single
	 * @param id
	 */
	public void remove(long id);

	public StrategyClass get(Long id);

	public Long save(StrategyClass strategyclass);

	public void saveOrUpdate(StrategyClass strategyclass);

	public void update(StrategyClass strategyclass);

	/**
	 * it will delete all the child classes too
	 * @param id
	 */
	public void delete(long id);
	
	public Long add(StrategyClass strategyclass);

	public List<StrategyClass> getChildClass(long id);

	public StrategyClass getParent(long id);

	public void addChildClass(long parentID, StrategyClass child);

	public StrategyClass get(String name);

	public StrategyClass getChildClass(String Name, long parent);

	public List<StrategyClass> getClasses();

	public List<String> getAbsoluteClassName(long id);

	Menu getMenu(String url, String para);

	Object[] getClassIDs(Long classid);
	
}
