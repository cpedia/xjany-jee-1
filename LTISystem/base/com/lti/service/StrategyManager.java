
package com.lti.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.hibernate.criterion.DetachedCriteria;

import com.lti.service.bo.AllocationTemplate;
import com.lti.service.bo.CacheStrategyItem;
import com.lti.service.bo.Data5500;
import com.lti.service.bo.PlanArticle;
import com.lti.service.bo.PlanScore;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.PortfolioMPT;
import com.lti.service.bo.Strategy;
import com.lti.service.bo.StrategyCode;
import com.lti.service.bo.ThirdParty;
import com.lti.service.bo.ThirdPartyResource;
import com.lti.service.bo.VariableFor401k;
import com.lti.type.PaginationSupport;

public interface StrategyManager {
	/**************************************************************/
	/*==basic method==Start                                       */
	/**************************************************************/
	/**
	 * add a strategy
	 * @param strgy
	 * @return
	 */
	long add(Strategy strgy);

	/**
	 * get the strategy by its ID
	 * @param id
	 * @return
	 */
	Strategy get(Long id);

	/**
	 * get the strategy by its name
	 * @param strategyname
	 * @return
	 */
	Strategy get(String strategyname);

	/**
	 * remove a strategy 
	 * and it do not remove the portfolio which use it
	 * @param strID
	 */
	void remove(long strID);

	/**
	 * update a strategy
	 * @param strgy
	 */
	Boolean update(Strategy strgy);
	/**
	 * update strategy list, we just update their are basic information
	 * @param strategies
	 */
	void updateStrategyName(long id,String name)throws Exception;
	
	void updatePortfolioName(long id ,String name) throws Exception;
	
	
	void updateStrategyType(List<Strategy> strategies);
	/**
	 * get the strategys by ID List
	 * @param strategyIDList
	 * @return
	 */
	List<Strategy> getStrategyByIDs(List<Long> strategyIDList);
	/**
	 * get strategies with criteria by page
	 * @param detachedCriteria
	 * @param pageSize
	 * @param startIndex
	 * @return
	 */
	PaginationSupport findStrategies(DetachedCriteria detachedCriteria,	int pageSize, int startIndex);

	/**
	 * get the strategies with criteria
	 * @param detachedCriteria
	 * @return
	 */
	List<Strategy> getStrategies(DetachedCriteria detachedCriteria); 
	/**************************************************************/
	/*==basic method== End                                        */
	/**************************************************************/
	/**************************************************************/
	/*==list method==Start                                        */
	/**************************************************************/
	/**
	 * get all strategies
	 * @return
	 */
	List<Strategy> getStrategies();

	/**
	 * get all strategies by page
	 * @param pageSize
	 * @param startIndex
	 * @return
	 */
	PaginationSupport getStrategies(int pageSize, int startIndex);

	/**
	 * get the strategies with the given class
	 * @param classid
	 * @return
	 */
	List<Strategy> getStrategiesByClass(Object[] classids);

	/**
	 * get the strategies with the given class by page
	 * @param classid
	 * @param pageSize
	 * @param startIndex
	 * @return
	 */
	PaginationSupport getStrategiesByClass(Object[] classids, int pageSize,	int startIndex);
	/**
	 * get the strategies with the given name (like , not equal)
	 * @param strategyname
	 * @return
	 */
	List<Strategy> getStrategiesByName(String strategyname);

	/**
	 * get the strategies with the given name by page (like , not equal)
	 * @param strategyname
	 * @param pageSize
	 * @param startIndex
	 * @return
	 */
	PaginationSupport getStrategiesByName(String strategyname, int pageSize,int startIndex);


	/**
	 * get the strategies with the given user and the given class
	 * @param classid
	 * @param userid
	 * @return
	 */
	List<Strategy> getStrategiesByClassAndUser(long classid, long userid);

	/**
	 * get the strategies with the given user and the given class by page
	 * @param classid
	 * @param userid
	 * @param pageSize
	 * @param startIndex
	 * @return
	 */
	PaginationSupport getStrategiesByClassAndUser(long classid, long userid,int pageSize, int startIndex);

	/**
	 * search all strategies which are public or of which the user is the creator.
	 * @param name
	 * @param userID
	 * @return list of strategies
	 */
	List<Strategy> searchStrategiesByName(String name, Long userID);
	
	/**
	 * search all strategies which are public or of which the user is the creator according to the name and categories string
	 * @param name and categories String
	 * @param userID
	 * @return list of strategies
	 */
	List<Strategy> searchStrategiesByCategory(String categories, Long userID);
	
	
	/**
	 * get the strategies with the given user
	 * @param userid
	 * @return
	 */
	List<Strategy> getPrivateStrategies(long userid);

	/**
	 * get the strategies with the given user by page
	 * @param userid
	 * @param pageSize
	 * @param startIndex
	 * @return
	 */
	PaginationSupport getPrivateStrategies(long userid, int pageSize,int startIndex);

	/**
	 * get the strategies which is created by Guest 
	 * (ID 0)
	 * @return
	 */
	List<Strategy> getPublicStrategies();

	/**
	 * get the strategies ,which is created by Guest, by page 
	 * (ID 0)
	 * @param pageSize
	 * @param startIndex
	 * @return
	 */
	PaginationSupport getPublicStrategies(int pageSize, int startIndex);

	
	List<Strategy> getStrategiesByOtherGroup(long groupid);
	PaginationSupport getStrategiesByOtherGroup(long groupid, int pageSize,
			int startIndex);
	/**************************************************************/
	/*==list method==End                                          */
	/**************************************************************/
	
	List<Portfolio> getQuotePortfolios(long strategyid);
	
	/**
	 * get portfolios belonging to the strategy and user
	 * @param strategyid
	 * @param userID
	 * @return
	 */
	List<Portfolio> getQuotePortfolios(long strategyid, long userID);
	
	List<Portfolio> getModelPortfolios(long strategyid);
	/**
	 * get all the model moderate portfolios for some plan
	 * @param strategyid
	 * @return
	 */
	List<Portfolio> getModeratePortfolios(long strategyid);
	
	List<Portfolio> getModelPortfolios(long strategyid, long userID);

	List<Strategy> getStrategies(long userid);

	PaginationSupport getStrategies(long userid, int pageSize, int startIndex);

	List<Strategy> searchStrategies(Long classid, String q);


	List<PortfolioMPT> getTopStrategyByMPT(Object[] classid, int year, int sort, Long userID, int size);

	/**
	 * @author cherry
	 * get the top portfolios in the certain strategy
	 * @param strategyID
	 * @param year
	 * @param sort
	 * @param userID
	 * @param size
	 * @return
	 */
	List<PortfolioMPT> getTopPortfolioInStrategy(long strategyID, int year, int sort, Long userID, int size);

	List<Object[]> findBySQL(String string)throws Exception;

	PortfolioMPT getTopStrategyByMPT(Long strategyid, int year, int sort);

	List findByHQL(String hql);

	Strategy getBasicStrategy(Long id);

	Portfolio getFirtLiveBasicModelPortfolio(Long id);
	
	List<Strategy> getMyPublicStrategies(long userid) ;
	/**
	 * get All Public Strategies
	 * @return
	 */
	List<Strategy> getAllPublicStrategies();

	List<Portfolio> getModelPortfolios(long strategyid, long userID, int size);

	List<PortfolioMPT> getSortMPTs(Long strategyid, int year, int sort);

	public List<Strategy> getStrategiesByMainStrategyID(Long id);

	public List<Strategy> getStrategiesByType(long type);

	List<VariableFor401k> getVariable401KByStrategyID(Long id);

	void updateVariableFor401k(List<VariableFor401k> variables, Long l);
	/**
	 * just update the variable element
	 * @param variables
	 */
	void updateVariableFor401k(List<VariableFor401k> variables);

	void saveVk(VariableFor401k vk);
	
	List<VariableFor401k> parse(byte[] bytes);

	void saveAll(List<VariableFor401k> vks);
	
	void saveALLPlanScore(List<PlanScore> psList);
	
	void saveOrUpdateAllPlanScore(List<PlanScore> psList);
	
	void saveOrUpdatePlanScore(PlanScore planScore);
	
	List<PlanScore> getPlanScore();
	
	List<PlanScore> getPlanScoreWithIDs(List<Long> planIDList);
	
	PlanScore getPlanScoreByPlanID(Long planID);
	
	void deletePlanScoreByID(Long ID) throws Exception;
	
	List<String> getPlanNames(String term, int size);
	
	List<Object[]> getPlanNamesAndIDs(String term, int size);
	
	
	List<String> getSymbolListForPlan(Long planID);
	
	List<String> getSymbolListForPlanWithoutN(Long planID);

	StrategyCode getLatestStrategyCode(Long id);

	StrategyCode getStrategyCode(Long strategyid, Date date);

	void saveStrategyCode(StrategyCode str);

	void deleteStrategyCode(Long strategyid, Date date);

	CacheStrategyItem getCacheStrategyItem(long groupid,long strategyid);

	List<CacheStrategyItem> getCacheStrategyItems(long strategyid);

	VariableFor401k getVariable401K(Long planid, String symbol);

	List<Strategy> getBasicStrategiesByType(long type);

	List<VariableFor401k> getVariable401Ks(List<Long> planIDList);
	
	List<List<PlanScore>> getTopAndBottomPlanScoreList(int num);
	
	PlanScore getLEPlanScoreByValueType(double value, String type);
	
	PlanScore getGEPlanScoreByValueType(double value, String type);
	
	List<PlanScore> getTopOrBottomNPlanScore(List<Long> planIDList, int num, boolean top);

	
	public void addPlanArticle(PlanArticle pa);
	public void updatePlanArticle(PlanArticle pa);
	public void removePlanArticle(String title);
	public PlanArticle getPlanArticleByID(long id);
	public List<PlanArticle> getPlanArticles(long planid);
	public List<PlanArticle> getPlanArticleByTitle(String articleTitle);
    public List<PlanArticle> getPlanArticleBySymbol(String symbol);


	VariableFor401k getVariableFor401kByDescription(String description);
	
	
	public int getPlanType(Long planID);

	List<Strategy> getPlansByType(String name, int planType, int size);
	
	public void updateStrategySimilarIssues(long id,String message) throws Exception;
	
	public List<String> getPlanTickers(long id);
	
	public List<String> getPlanMinorAssets(List<String> tickerList);
	
	public List<String> getPlanMajorAssets(List<String> assetsList)throws Exception;
	
	public List<String> getPlanMajorAssets(List<String> assetsList, boolean other)throws Exception;
	
	public int getMajorAssetCountByPlanID(Long planID) throws Exception;
	
	public void assignAdminPlanToUser(Long planID, Long userID) throws Exception;
	
	public List<AllocationTemplate> getAllocationTemplateByName(String name);
	
	public void addAllocationTemplate(AllocationTemplate at);
	
	public void updateAllocationTemplate(AllocationTemplate at);
	
	public void removeAllocationTemplate(String name);
	
	public List<AllocationTemplate> getAllallocationTemplate();
	
	public List<String> getMajorAssetByPlanID(Long planID) throws Exception;
	
	public AllocationTemplate getAllocationTemplateById(long id);
	
	public String getRiskyOrStableAsset(String assetClassName);
	//添加data500
	public Long add(Data5500 data5500);
	//根据ACKID查找Data5500
	public Data5500 getByACK(String ack_id);
	//更新data500
	public Boolean update(Data5500 data5500);
	//根据planid查找data5500
	public Data5500 getData5500ByPlanID(Long planID);
	public Map<String,List<VariableFor401k>> getSixMajorAssetClassForPlan(long planid);
	List<Strategy> searchStrategiesByName(String keyword, int size, Long userAnonymous);

	public List<ThirdPartyResource> getThirdPartyResources(String thirdParty);

	void updateThirdPartyResources(String thirdparty, List<ThirdPartyResource> trs);

	List<Object[]> searchPlanByThirdParty(String thirdparty, String keyword, int size) throws Exception;

	List<ThirdParty> getThirdParties();

	void updateThirdParties(List<ThirdParty> trs);

	ThirdParty getThirdParty(Long tid);

	List<Long> getModelPortfolioIDs(Long planID);
}
