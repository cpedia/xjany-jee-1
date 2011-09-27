package com.lti.service;

import java.util.List;

import com.lti.service.bo.CUSIP;

public interface CUSIPManager {
	/**
	 * remove asset class by id, single
	 * @param id
	 */
	public void remove(long id);

	public CUSIP get(long id);

	public long save(CUSIP c);

	public void saveOrUpdate(CUSIP c);

	public void update(CUSIP c);

	public CUSIP getBySymbol(String symbol);
	
	public CUSIP getByCUSIP(String cusip);
	
	public String getCUSIP(String symbol);
	
	public String getSymbol(String cusip);
	
	public void deleteByHQL(String string);
	
	public List<CUSIP> getCUSIPs();
}
