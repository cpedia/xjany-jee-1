package com.lti.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lti.service.bo.FactorBetaGain;
import com.lti.service.bo.MutualFund;
import com.lti.service.bo.MutualFundDailyBeta;
import com.lti.service.bo.BetagainDailyData;
import com.lti.service.bo.Security;
import com.lti.service.bo.SecurityDailyData;
import com.lti.service.bo.SecurityRanking;



public interface MutualFundManager {
	public void update(MutualFund mf);
	public Long save(MutualFund mf);
	public MutualFund get(Long id);
	public void remove(Long id);
	public List<MutualFund> get(String symbol,String[] index,Long createTime,Boolean IsRAA);
	public void delete(String symbol,String[] index,Boolean IsRAA);
	
	public Long saveDailyBeta(MutualFundDailyBeta mfdb);
	public void saveAll(List<MutualFundDailyBeta>  mfdbs);
	
	public void saveAllSecurityRanking(List<SecurityRanking> srs);
	public void saveOrUpdateAllSecurityRanking(List<SecurityRanking> srs);
	public void saveAllFactorBetaGain(List<FactorBetaGain> fbg);
	public void removeAllFactorBetaGain(List<FactorBetaGain> fbg);
	public void clearDailyBeta(Long mfID);
	public List<MutualFundDailyBeta> getDailyData(Long mfID);
	public List<MutualFundDailyBeta> getDailyData(String symbol,String[] index,Long createTime,Boolean IsRAA);
	
	//public void calculateDailyBeta(String symbol, int interval, String[] index,Long createTime,Date startDate, Date endDate, Boolean allowNeg,Boolean withConstraint) throws Exception;
	public void calculateDailyBeta(String symbol, int interval, String[] index,Long createTime, Date startDate, Date endDate, boolean withConstraint,boolean isWLSOrOLS, boolean isSumToOne) throws Exception ;
	MutualFundDailyBeta getDailyData(String symbol, String[] index, Date date,Long createTime,Boolean IsRAA);
	public List<MutualFundDailyBeta> findByHQL(String string);
	
	public void setupLimit(double[] upper,double[] lower);
	
	public List<BetagainDailyData> calculateBetaGain(String symbol,int interval,String[] index,String[] type,Date startDate,Date endDate,int gainInterval,boolean isSigmaOne,boolean isWLSorOLS)throws Exception;
		
	public List<BetagainDailyData> calculateEndBetaGain(String symbol,int interval,String[] index,String[] type,Date startDate,Date endDate,boolean isSigmaOne,boolean isWLSorOLS)throws Exception;
	
	
	public Date getTheEarlyestDate(List<Security> fundList);
	
	public String calculateBetaGainForRanking(String symbol,int interval,String[] index,String[] type,Date endDate,int gainInterval,boolean isSigmaOne,boolean isWLSorOLS)throws Exception;
	public List<FactorBetaGain> calculateBetaGainForFactor(String symbol,int interval,String[] index,String[] type,boolean isSigmaOne,boolean isWLSorOLS)throws Exception;
	public List<SecurityRanking> setTheNumMonthBetaGainTable(List<SecurityRanking> securityRankingList,Date fromDate,Date toDate,int monthCount);
	public void calculateHistoricalBetaGainForAllFunds(int startyear) throws FileNotFoundException, IOException;
	public void calculateHistoricalOneYearBetaGainByAssetClassID(Long acID) throws IOException;
	public void calculateHistoricalThreeYearBetaGainByAssetClassID(Long acID) throws IOException;
	public void calculateFactorBetaGainForAllFunds() throws IOException;
	public void calculateHistoricalBetaGainByAssetClassID(Long acID,int startyear) throws IOException;
	public void updateDailyFactorBetaGainForAllFunds() throws IOException;
	public void updateHistoricalBetaGainForAllFunds() throws IOException;
	public void initialAssetClassMapAndSecurity();
	public List<SecurityRanking> getHistoricalRankingBySymbol(String symbol,int interval);
	public List<FactorBetaGain> getFactorDetailsBySymbol(String symbol);
	public Integer getCurrentRankingBySymbol(String symbol);
	public List<SecurityRanking> getFundRankingByAssetClassName(String name,Date date,Integer size);
	public List<SecurityRanking> getFundRankingByAssetClassID(Long assetClassID,Date date, Integer size);
	public Map<String,List<SecurityRanking>> getRankingFundMap(Date today);
	public HashMap<Long, List<Long>> getAssetClassSecurityList();
	public void test(Long securityID);
	
}

