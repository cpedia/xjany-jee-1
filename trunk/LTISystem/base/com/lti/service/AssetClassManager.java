package com.lti.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.lti.service.bo.AssetClass;
import com.lti.type.Menu;


public interface AssetClassManager {

	/**
	 * remove asset class by id, single
	 * @param id
	 */
	public void remove(long id);

	public AssetClass get(Long id);

	public Long save(AssetClass assetclass);

	public void saveOrUpdate(AssetClass assetclass);

	public void update(AssetClass assetclass);

	/**
	 * it will delete all the child classes too
	 * @param id
	 */
	public void delete(long id);
	
	public Long add(AssetClass assetclass);

	public List<AssetClass> getChildClass(long id);

	public AssetClass getParent(long id);
	
	public AssetClass getFirstParentClass(long id);
	
	public AssetClass getFirstParentClass(String name);
	
	public boolean isUpperOrSameClass(String parentclassname,String curclassname);

	public void addChildClass(long parentID, AssetClass child);

	public AssetClass get(String name);

	public AssetClass getChildClass(String Name, long parent);
	
	public Menu getMenuByAssetList(List<AssetClass>assetClassList,Integer id);
	
	public boolean isParentAssetClass(AssetClass parentAssetClass,AssetClass childAssetClass);

	public List<AssetClass> getClasses();

	public List<String> getAbsoluteClassName(long id);
	
	public List<AssetClass> getParentAssetClassList(List<AssetClass> childAsstClassList);

	Menu getMenu(String url,String para);

	public void deleteByHQL(String string);
	
	public HashMap<String,List<String>> getAvailableAssetClassSet(List<String> availableAssetClassList,List<String> availableFundList, boolean longTermFlag);
	
	public HashMap<String,List<String>> getAvailableAssetClassSet(List<String> availableAssetClassList,List<String> availableFundList);

	public HashSet<String> getAvailableAssetClassSet(List<String> availableAssetClassList);
	
	public HashMap<String,List<String>> getAvailableAssetClassSet(HashSet<String> availableAssetClassSet ,List<String> availableFundList, boolean hasHighYieldBond, boolean hasLongTermFlag);
	
	public HashMap<String,List<String>> getAvailableAssetClassSet(String[] assetarr, List<String> candidateList, boolean longTermFlag, boolean AllowSectorFund);
	
	public String getAssetClassNameForMutualFundAndETF(String symbol);
	
	public String getAssetClassNameForCEF(String symbol);
	
	public void updateAssetClassForAllFunds();
	
	public void mergeAssetClass(Long originalID, Long targetID);
	
	public void catalogueAssetClass(Long originalID,Long parentID);
	
	public void updateBenchmarkForAssetClass(String fileName);
}
