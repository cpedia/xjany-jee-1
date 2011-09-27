package com.lti.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.lti.Exception.Security.NoPriceException;
import com.lti.MutualFundTool.WeightEstimate;
import com.lti.MutualFundTool.Exception.InvalidParametersException;
import com.lti.cache.LTICache;
import com.lti.service.AssetClassManager;
import com.lti.service.HolidayManager;
import com.lti.service.MutualFundManager;
import com.lti.service.SecurityManager;
import com.lti.service.bo.AssetClass;
import com.lti.service.bo.FactorBetaGain;
import com.lti.service.bo.MutualFund;
import com.lti.service.bo.MutualFundDailyBeta;
import com.lti.service.bo.Security;
import com.lti.service.bo.SecurityDailyData;
import com.lti.service.bo.BetagainDailyData;
import com.lti.service.bo.SecurityRanking;
import com.lti.service.bo.Strategy;
import com.lti.service.bo.BetagainDailyData.BetaGainType;
import com.lti.system.ContextHolder;
import com.lti.type.TimeUnit;
import com.lti.util.BetaGainComparator;

import com.lti.util.LTIDate;
import com.lti.util.LTIFactorManager;
import com.lti.util.LTIFactorManager.Factor;

public class MutualFundManagerImpl extends DAOManagerImpl implements MutualFundManager {

	private HashMap<Long,List<Long>> assetClassSecurityList = new HashMap<Long,List<Long>>();
	
	private double[] upper;
	private double[] lower;
	
	@Override
	public void clearDailyBeta(Long mfID) {
		if (mfID == null)
			return;
		deleteByHQL("from MutualFundDailyBeta where MutualFundID=" + mfID);
	}

	@Override
	public void delete(String symbol, String[] index,Boolean IsRAA) {
		List<MutualFund> mfList = get(symbol, index,-1L,IsRAA);
		if (mfList == null)
			return;
		for(int i=0;i<mfList.size();i++)
		{
			MutualFund mf = mfList.get(i);
			clearDailyBeta(mf.getID());
			remove(mf.getID());
		}

	}
	
	

	public void setupLimit(double[] upper,double[] lower) {
		this.upper = upper;
		this.lower = lower;
	}
	
	@Override
//	public void caculateDailyBeta(String symbol, int interval, String[] index,Long createTime, Date startDate, Date endDate, Boolean allowNeg,Boolean withConstraint) throws Exception {
//		SecurityManager securityManager;
//		SecurityManagerImplWithCache s=null;		
//		//withConstraint = false;
//		//List<Double[]> pValueList = new ArrayList<Double[]>();
//		
//		try {
//			s = new SecurityManagerImplWithCache();
//			s.setDailyDataCache(new com.lti.cache.LTICache());
//			s.setSessionFactory((SessionFactory) ContextHolder.getInstance().getApplicationContext().getBean("sessionFactory"));
//			securityManager=s;
//			
//			HolidayManager holidayManager = ContextHolder.getHolidayManager();
//
//			Arrays.sort(index);
//			
//			Security mutualFund = securityManager.get(symbol);
//			if (mutualFund == null)
//				throw new InvalidParametersException("MF parameter is invalid!");
//			
//			delete(symbol, index,withConstraint);
//
//			MutualFund mf = new MutualFund();
//			mf.setStartDate(startDate);
//			mf.setEndDate(endDate);
//			mf.setIndex(index);
//			mf.setSymbol(symbol);
//			mf.setCreateTime(createTime);
//			mf.setIsRAA(true);
//			save(mf);
//			
//			//System.out.println(mf.getID());
//
//			List<Date> dates = holidayManager.getTradingDateList(LTIDate.getNewNYSETradingDay(startDate,-1), LTIDate.getNewNYSETradingDay(endDate,-1));
//			if (dates == null || dates.size() < 1)throw new InvalidParametersException("StartDate or EndDate parameter is invalid!");
//
//			List<MutualFundDailyBeta> mfdbs=new ArrayList<MutualFundDailyBeta>();
//			WeightEstimate we = new WeightEstimate();
//			
//			
//			for (int k = 0; k < dates.size(); k++) {
//				Date date2=dates.get(k);
//				Date[] CDate=new Date[interval+1];
//				for (int i = 0; i < interval + 1; i++) {
//					CDate[i] = LTIDate.getNewNYSETradingDay(date2, -interval - 1 + i);
//				}
//				
//				
//				double[] mfAdjPrice = new double[interval + 1];
//				for (int i = 0; i < interval+1; i++) {
//					try {
//						mfAdjPrice[i] = securityManager.getAdjPrice(mutualFund.getID(), CDate[i]);
//					} catch (NoPriceException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//				
//				if (index == null || index.length < 1)
//					throw new InvalidParametersException("Size of index array is invalid!");
//				double[][] indexAdjPrice = new double[index.length][interval + 1];
//				for (int i = 0; i < index.length; i++) {
//					Security cIndex = securityManager.get(index[i]);
//					if (cIndex == null)
//						throw new InvalidParametersException("Index parameter is invalid!");
//					for(int j=0;j<interval+1;j++){
//						indexAdjPrice[i][j]=securityManager.getAdjPrice(cIndex.getID(), CDate[j]);
//					}
//				}
//				Double d[];
//				int resultSize = 0;
//				
//				if(withConstraint){					
//					we.setLower(lower);
//					we.setUpper(upper);					
//					if(lower!=null && upper!=null)d=we.RAA_WithLimit(interval, mfAdjPrice, indexAdjPrice);
//					else d=we.RAA(interval, mfAdjPrice, indexAdjPrice, allowNeg);
//					resultSize = d.length-1;
//				}
//				else{
//					we.setLower(lower);
//					we.setUpper(upper);
//					d = we.RAA_WithLimit_ForWLS(interval, mfAdjPrice, indexAdjPrice);
//					resultSize = d.length-1;
//				}		
//				
//				MutualFundDailyBeta mfdb = new MutualFundDailyBeta();
//				
//				mfdb.setMutualFundID(mf.getID());
//				
//				mfdb.setBetas(d);
//				mfdb.setDate(date2);
//				mfdb.setRSquare(d[resultSize]);
//				
//				mfdbs.add(mfdb);
//				
//				
//			}
//			saveAll(mfdbs);
//		} catch (Exception e) {
//			throw e;
//		}finally{
//			if(s!=null){
//				LTICache c=s.getDailyDataCache();
//				if(c!=null){
//					c.removeAll();
//					c=null;
//					s.setDailyDataCache(null);
//					System.gc();
//				}
//			}
//			
//			
//			/*if(withConstraint)return null;
//			else return pValueList;*/
//		}
//	}
	//public void calculateDailyBeta(String symbol, int interval, String[] index,Long createTime,Date startDate, Date endDate, Boolean allowNeg,Boolean withConstraint) throws Exception
	//{
		
	//}
	public void calculateDailyBeta(String symbol, int interval, String[] index,Long createTime, Date startDate, Date endDate,boolean withConstraint, boolean isWLSorOLS, boolean isSumToOne) throws Exception {
		SecurityManager securityManager;
		SecurityManagerImplWithCache s=null;
		
		//withConstraint = false;
		//List<Double[]> pValueList = new ArrayList<Double[]>();
		
		try {
			s = new SecurityManagerImplWithCache();
			s.setDailyDataCache(new com.lti.cache.LTICache());
			s.setSessionFactory((SessionFactory) ContextHolder.getInstance().getApplicationContext().getBean("sessionFactory"));
			securityManager=s;
			boolean factorIsCash=false;
			boolean[] isFFStyle = new boolean[index.length];
			double[][] weights = new double[interval][interval];
			HolidayManager holidayManager = ContextHolder.getHolidayManager();

			Arrays.sort(index);
			
			Security mutualFund = securityManager.get(symbol);
			if (mutualFund == null)
				throw new InvalidParametersException("MF parameter is invalid!");
			
			delete(symbol, index,withConstraint);

			MutualFund mf = new MutualFund();
			mf.setStartDate(startDate);
			mf.setEndDate(endDate);
			mf.setIndex(index);
			mf.setSymbol(symbol);
			mf.setCreateTime(createTime);
			mf.setIsRAA(true);
			save(mf);
			
			List<Date> dates = holidayManager.getTradingDateList(LTIDate.getNewNYSETradingDay(startDate,-1), LTIDate.getNewNYSETradingDay(endDate,-1));
			if (dates == null || dates.size() < 1)throw new InvalidParametersException("StartDate or EndDate parameter is invalid!");

			List<MutualFundDailyBeta> mfdbs=new ArrayList<MutualFundDailyBeta>();
			WeightEstimate we = new WeightEstimate();
			we.setLower(lower);
			we.setUpper(upper);
			for(int i=0;i<interval;++i)
				weights[i][i] = Math.sqrt(i+1);
			we.setWeights(weights);
			
			for (int k = 0; k < dates.size(); k++) {
				Date date2=dates.get(k);
				Date[] CDate=new Date[interval+1];
				for (int i = 0; i < interval + 1; i++) {
					CDate[i] = LTIDate.getNewNYSETradingDay(date2, -interval - 1 + i);
				}
				
				double[] mfAdjPrice = new double[interval + 1];
				for (int i = 0; i < interval+1; i++) {
					try {
						mfAdjPrice[i] = securityManager.getAdjPrice(mutualFund.getID(), CDate[i]);
					} catch (NoPriceException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				if (index == null || index.length < 1)
					throw new InvalidParametersException("Size of index array is invalid!");
				double[][] indexAdjPrice = new double[index.length][interval + 1];
				
				for (int i = 0; i < index.length; i++) {
					Security cIndex = securityManager.get(index[i]);
					if (cIndex == null)
						throw new InvalidParametersException("Index parameter is invalid!");
					for(int j=0;j<interval+1;j++){
						indexAdjPrice[i][j]=securityManager.getAdjPrice(cIndex.getID(), CDate[j]);
					}
					isFFStyle[i] = index[i].indexOf("^FF_")>=0;
				}
				
				if(index.length==1 && index[0].equalsIgnoreCase("CASH"))
					factorIsCash=true;
				Double d[];	
				if(withConstraint){						
					d=we.RAA_QP(interval, mfAdjPrice,indexAdjPrice ,isWLSorOLS, isSumToOne);
				}
				else{
					d =we.RAA_LM(interval, mfAdjPrice, indexAdjPrice, isWLSorOLS,isFFStyle,factorIsCash,isSumToOne);
				}		
				Double[] beta= new Double[index.length];
				for(int i=0;i<index.length;++i)
					beta[i]=d[i];
				MutualFundDailyBeta mfdb = new MutualFundDailyBeta();
				
				mfdb.setMutualFundID(mf.getID());
				mfdb.setBetas(beta);
				mfdb.setDate(date2);
				mfdb.setRSquare(d[index.length]);
				mfdbs.add(mfdb);
			}
			saveAll(mfdbs);
		} catch (Exception e) {
			throw e;
		}finally{
			if(s!=null){
				LTICache c=s.getDailyDataCache();
				if(c!=null){
					c.removeAll();
					c=null;
					s.setDailyDataCache(null);
					System.gc();
				}
			}
		}
	}

	@Override
	public MutualFund get(Long id) {

		if (id == null)
			return null;

		MutualFund mf = (MutualFund) getHibernateTemplate().get(MutualFund.class, id);

		return mf;
	}

	@Override
	public List<MutualFund> get(String symbol, String[] index,Long createTime,Boolean IsRAA) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(MutualFund.class);

		detachedCriteria.add(Restrictions.eq("Symbol", symbol));

		if (index == null) {
			return null;
		} else {
			// Arrays.sort(index);
			// StringBuffer sb=new StringBuffer();
			// for (int i = 0; i < index.length; i++) {
			// sb.append(index[i]).append(HibernateStringArray.SPLITTER);
			// }
			// detachedCriteria.add(Restrictions.eq("Index", sb.toString()));
			detachedCriteria.add(Restrictions.eq("Index", index));
			if(createTime!=-1)detachedCriteria.add(Restrictions.eq("CreateTime", createTime));
			detachedCriteria.add(Restrictions.eq("IsRAA", IsRAA));
		}

		List<MutualFund> bolist = findByCriteria(detachedCriteria);

		return bolist;

	}

	@Override
	public void remove(Long id) {
		MutualFund mf = get(id);
		if (mf == null)
			return;
		getHibernateTemplate().delete(mf);

	}

	@Override
	public Long save(MutualFund mf) {
		if (mf == null)
			return null;
		else {
			getHibernateTemplate().save(mf);
			return mf.getID();
		}

	}

	@Override
	public void saveAll(List<MutualFundDailyBeta> mfdbs) {
		super.saveAll(mfdbs);

	}

	@Override
	public Long saveDailyBeta(MutualFundDailyBeta mfdb) {
		if (mfdb == null)
			return null;
		else {
			getHibernateTemplate().save(mfdb);
			return mfdb.getID();
		}

	}

	@Override
	public void update(MutualFund mf) {
		if (mf == null)
			return;
		else {
			getHibernateTemplate().update(mf);
		}
	}


	@Override
	public List<MutualFundDailyBeta> getDailyData(Long mfID) {
		// TODO Auto-generated method stub
		if (mfID == null)
			return null;
		return findByHQL("from MutualFundDailyBeta where MutualFundID=" + mfID);
	}

	@Override
	public List<MutualFundDailyBeta> getDailyData(String symbol, String[] index,Long createTime,Boolean IsRAA) {
		// TODO Auto-generated method stub
		List<MutualFund> mfList = get(symbol, index,createTime,IsRAA);
		if(mfList==null || mfList.size()==0)return null;
		MutualFund mf = mfList.get(0);
		
		if (mf == null)
			return null;
		else{
			//System.out.println(symbol+" "+mf.getID());
			return getDailyData(mf.getID());
		}
	}
	@Override
	public MutualFundDailyBeta getDailyData(String symbol, String[] index,Date date,Long createTime,Boolean IsRAA) {
		// TODO Auto-generated method stub
		List<MutualFund> mfList = get(symbol, index,createTime,IsRAA);
		if(mfList==null || mfList.size()==0)return null;
		MutualFund mf = mfList.get(0);
		
		if (mf == null)
			return null;
		else{
			Long mfID=mf.getID();
			DetachedCriteria detachedCriteria = DetachedCriteria.forClass(MutualFundDailyBeta.class);
			detachedCriteria.add(Restrictions.eq("MutualFundID", mfID));
			detachedCriteria.add(Restrictions.eq("Date", LTIDate.clearHMSM(date)));
			
			List<MutualFundDailyBeta> bolist = findByCriteria(detachedCriteria, 1, 0);

			if (bolist.size() >= 1) {

				return bolist.get(0);

			}

			else
				return null;
		}
	}
	
	public List<BetagainDailyData> calculateBetaGain(String symbol,int interval,String[] index,String[] type,Date startDate,Date endDate,int gainInterval,boolean isSigmaOne,boolean isWLSorOLS)throws Exception{
		SecurityManager securityManager;
		SecurityManagerImplWithCache s=null;		
		
		try {
			s = new SecurityManagerImplWithCache();
			s.setDailyDataCache(new com.lti.cache.LTICache());
			s.setSessionFactory((SessionFactory) ContextHolder.getInstance().getApplicationContext().getBean("sessionFactory"));
			securityManager=s;
			
			HolidayManager holidayManager = ContextHolder.getHolidayManager();

			Arrays.sort(index);
			
			Security mutualFund = securityManager.get(symbol);
			if (mutualFund == null)
				throw new InvalidParametersException("MF parameter is invalid!");
			Long mutualFundID = mutualFund.getID();
						
			boolean[] isFFStyle = new boolean[index.length];
			
			Long[] cIndex = new Long[index.length];
			for(int i=0;i<index.length;i++){
				Security tmp = securityManager.get(index[i]);
				if (tmp == null)
					throw new InvalidParametersException("Index parameter is invalid!");
				cIndex[i]=tmp.getID();
				
				isFFStyle[i] = index[i].indexOf("^FF_")>=0;
			}
			
			if (index == null || index.length < 1)
				throw new InvalidParametersException("Size of index array is invalid!");

			List<Date> dates = holidayManager.getTradingDateList(LTIDate.getNewNYSETradingDay(startDate,-1), LTIDate.getNewNYSETradingDay(endDate,-1));
			if (dates == null || dates.size() < 1)throw new InvalidParametersException("StartDate or EndDate parameter is invalid!");

			List<Date> dates2 = holidayManager.getTradingDateList(LTIDate.getNewNYSETradingDay(startDate,-interval-1), LTIDate.getNewNYSETradingDay(endDate,-1));
			List<BetagainDailyData> betagains=new ArrayList<BetagainDailyData>();
			WeightEstimate we = new WeightEstimate();			
					
			Long cashID = securityManager.get("CASH").getID();
			List<Double> riskValue = new ArrayList<Double>();
			List<Double> mfValue = new ArrayList<Double>();
			List<List<Double>> indexValue = new ArrayList<List<Double>>();
			for(int k=0,size=dates2.size();k<size;k++){
				Date tmpDate = dates2.get(k);

				try {
					double mfV = securityManager.getAdjPrice(mutualFundID, tmpDate);
					mfValue.add(mfV);
					
					double riskV = securityManager.getAdjPrice(cashID, tmpDate);
					riskValue.add(riskV);
					
				} catch (NoPriceException e) {
					e.printStackTrace();
				}
			}		
			
			for(int j=0;j<index.length;j++){
				List<Double> oneIndexValue = new ArrayList<Double>();
				boolean flag = type[j].equalsIgnoreCase("1")?true:false;
				if(!flag){
					double curValue = 1;
					for(int k=0,size=dates2.size();k<size;k++)
					{
						Date tmpDate = dates2.get(k);
						double curReturn = securityManager.getAdjPrice(cIndex[j], tmpDate);
						double newValue = curValue*(1+curReturn);
						oneIndexValue.add(newValue);
						curValue = newValue; 
					}
				}					
				else{
					for(int k=0,size=dates2.size();k<size;k++){
						Date tmpDate = dates2.get(k);
						oneIndexValue.add(securityManager.getAdjPrice(cIndex[j], tmpDate));
					}
				}
				indexValue.add(oneIndexValue);
			}
			
			
			int mfStart=0;

			Double[][] betas = new Double[dates.size()][index.length];
			
			for (int k = 0; k < dates.size(); k++) {
				
				double[] mfAdjPrice = new double[interval + 1];
				double[] riskPrice = new double[interval+1];
				double[][] indexAdjPrice = new double[index.length][interval + 1];
				
				for(int i=0;i<interval+1;i++){
					
					mfAdjPrice[i]=mfValue.get(mfStart+i);
					riskPrice[i] = riskValue.get(mfStart+i);
					for(int j=0;j<index.length;j++){
						indexAdjPrice[j][i]=indexValue.get(j).get(mfStart+i);
					}
				}
				mfStart+=1;
				Double[] d={};
				we.setCashList(riskPrice);
				if(isSigmaOne)d=we.RAA(interval, mfAdjPrice, indexAdjPrice, true);
				else d=we.RAA_NoConstraint_ForWLS(interval, mfAdjPrice, indexAdjPrice,isWLSorOLS,isFFStyle);										
								
				betas[k]=d;
			}	
			
			double[] sigmas = new double[index.length];
			
			for(int k=0;k<dates.size();k++){
				
				Date date2=dates.get(k);
				BetagainDailyData bgdata = new BetagainDailyData();
				bgdata.setDate(date2);
				bgdata.setFundID(mutualFundID);
								
				Double[] betaGains = new Double[index.length];
				
				if(k==0)
					for(int j=0;j<index.length;j++)
						sigmas[j]=0;
				
				else if(k>0)
					for(int j=0;j<index.length;j++){
						double preValue = indexValue.get(j).get(k-1);
						double curValue = indexValue.get(j).get(k);
						double dailyReturn = Math.log(curValue/preValue);
						double beta = betas[k][j]-betas[k-1][j];
						double betagain = beta*dailyReturn;
						sigmas[j]+=(betagain);
						
					}
				
				
				if(k>gainInterval){				
					for(int j=0;j<index.length;j++){
						double preValue = indexValue.get(j).get(k-gainInterval-1);
						double curValue = indexValue.get(j).get(k-gainInterval);
						double dailyReturn = Math.log(curValue/preValue);
						double beta = betas[k-gainInterval][j]-betas[k-gainInterval-1][j];
						double preBetagain = beta*dailyReturn;
						sigmas[j]-=(preBetagain);
						//System.out.println(preBetagain);
					}
				}
				
				if(k>=gainInterval){
					for(int j=0;j<index.length;j++){
						betaGains[j]=sigmas[j];
					}
				}
				
				bgdata.setBetas(betas[k]);
				bgdata.setGains(betaGains);
				betagains.add(bgdata);
				
			}
			return betagains;
		} catch (Exception e) {			
			throw e;
		}
	}
	
	public List<BetagainDailyData> calculateEndBetaGain(String symbol,int interval,String[] index,String[] type,Date startDate,Date endDate,boolean isSigmaOne,boolean isWLSorOLS)throws Exception{
		SecurityManager securityManager;
		SecurityManagerImplWithCache s=null;		
		
		try {
			s = new SecurityManagerImplWithCache();
			s.setDailyDataCache(new com.lti.cache.LTICache());
			s.setSessionFactory((SessionFactory) ContextHolder.getInstance().getApplicationContext().getBean("sessionFactory"));
			securityManager=s;
			
			HolidayManager holidayManager = ContextHolder.getHolidayManager();

			Arrays.sort(index);
			
			Security mutualFund = securityManager.get(symbol);
			if (mutualFund == null)
				throw new InvalidParametersException("MF parameter is invalid!");
			Long mutualFundID = mutualFund.getID();
			
			boolean[] isFFStyle = new boolean[index.length];
			// cIndex restore the factors's ids
			Long[] cIndex = new Long[index.length];
			for(int i=0;i<index.length;i++){
				Security tmp = securityManager.get(index[i]);
				if (tmp == null)
					throw new InvalidParametersException("Index parameter is invalid!");
				cIndex[i]=tmp.getID();
				
				
				isFFStyle[i] = index[i].indexOf("^FF_")>=0;
			}
			
			if (index == null || index.length < 1)
				throw new InvalidParametersException("Size of index array is invalid!");
			//dates restore tradingday between startDate and endDate
			List<Date> dates = holidayManager.getTradingDateList(LTIDate.getNewNYSETradingDay(startDate,-1), LTIDate.getNewNYSETradingDay(endDate,-1));
			if (dates == null || dates.size() < 1)throw new InvalidParametersException("StartDate or EndDate parameter is invalid!");
			// dates2 restore tradingday between startDate-interval and endDate
			List<Date> dates2 = holidayManager.getTradingDateList(LTIDate.getNewNYSETradingDay(startDate,-interval-1), LTIDate.getNewNYSETradingDay(endDate,-1));
			List<BetagainDailyData> betagains=new ArrayList<BetagainDailyData>();
			WeightEstimate we = new WeightEstimate();			
			
			boolean factorIsCash=false;
			if(index.length==1 && index[0].equalsIgnoreCase("CASH"))
				factorIsCash=true;
			Long cashID = securityManager.get("CASH").getID();
			List<Double> riskValue = new ArrayList<Double>();
			List<Double> mfValue = new ArrayList<Double>();
			List<List<Double>> indexValue = new ArrayList<List<Double>>();
			for(int k=0,size=dates2.size();k<size;k++){
				Date tmpDate = dates2.get(k);

				try {
					double mfV = securityManager.getAdjPrice(mutualFundID, tmpDate);
					mfValue.add(mfV);
					
					double riskV = securityManager.getAdjPrice(cashID, tmpDate);
					riskValue.add(riskV);
					
				} catch (NoPriceException e) {
					e.printStackTrace();
				}
			}		
			
			for(int j=0;j<index.length;j++){
				List<Double> oneIndexValue = new ArrayList<Double>();
				boolean flag = type[j].equalsIgnoreCase("1")?true:false;
				if(!flag){
					double curValue = 1;
					for(int k=0,size=dates2.size();k<size;k++)
					{
						Date tmpDate = dates2.get(k);
						double curReturn = securityManager.getAdjPrice(cIndex[j], tmpDate);
						double newValue = curValue*(1+curReturn);
						oneIndexValue.add(newValue);
						curValue = newValue; 
					}
				}					
				else{
					for(int k=0,size=dates2.size();k<size;k++){
						Date tmpDate = dates2.get(k);
						oneIndexValue.add(securityManager.getAdjPrice(cIndex[j], tmpDate));
					}
				}
				indexValue.add(oneIndexValue);
			}			
			
			Double[][] betas = new Double[dates.size()][index.length];
			
			int[] gainIntervals = BetaGainType.getBetaGainTypeDays();
			
			int calStart=0;
			for(int i=gainIntervals.length-1;i>=0;i--)
			{
				if(dates.size()>=gainIntervals[i]){
					calStart = dates.size()-gainIntervals[i];
					break;
				}
			}
			

			if(calStart>0)calStart--;
			int mfStart=calStart;
			for (int k = calStart; k < dates.size(); k++) {
				
				double[] mfAdjPrice = new double[interval + 1];
				double[] riskPrice = new double[interval + 1];
				double[][] indexAdjPrice = new double[index.length][interval + 1];
				
				for(int i=0;i<interval+1;i++){
					
					mfAdjPrice[i]=mfValue.get(mfStart+i);
					riskPrice[i]=riskValue.get(mfStart+i);
					for(int j=0;j<index.length;j++){
						indexAdjPrice[j][i]=indexValue.get(j).get(mfStart+i);
					}
				}
				mfStart+=1;
				Double[] d={};
				we.setCashList(riskPrice);
				if(isSigmaOne)d=we.RAA(interval, mfAdjPrice, indexAdjPrice, true);
				else d=we.RAA_NoConstraint_ForWLS(interval, mfAdjPrice, indexAdjPrice,isWLSorOLS,isFFStyle,factorIsCash);
								
				betas[k]=d;
			}	
			
			Date endDate2=dates.get(dates.size()-1);
			for(int k1=0;k1<gainIntervals.length;k1++)
			{
				
				int gainInterval = gainIntervals[k1];
				if(gainInterval>=dates.size())break;
				
				BetagainDailyData bgdata = new BetagainDailyData();
				bgdata.setDate(endDate2);
				bgdata.setFundID(mutualFundID);
								
				Double[] betaGains = new Double[index.length];
				for(int j=0;j<index.length;j++)
					betaGains[j]=new Double(0.0);
				
				for(int k=dates.size()-gainInterval;k<dates.size();k++)
				{				
					for(int j=0;j<index.length;j++){
						double preValue = indexValue.get(j).get(k-1);
						double curValue = indexValue.get(j).get(k);
						double dailyReturn = Math.log(curValue/preValue);
						double beta = betas[k][j]-betas[k-1][j];
						double betagain = beta*dailyReturn;
						betaGains[j]+=(betagain);						
					}
				}
				
					for(int j=0;j<index.length;j++){
						System.out.print(betaGains[j]+"+"+betas[j]+" ");
					}
					System.out.println();
					
				bgdata.setBetas(betas[dates.size()-1]);
				bgdata.setGains(betaGains);
				bgdata.setBetaGainType(BetaGainType.getBetaGainType(gainInterval));
				betagains.add(bgdata);		
			}
			return betagains;
		} catch (Exception e) {			
			throw e;
		}
	}
	/**
	 * calculate for gaininterval=66 as historical data
	 * @author CCD
	 * @param symbol a fund's symbol
	 * @param interval the interval days to calculate beta
	 * @param index[] the fund's factors' names
	 * @param type[] the fund's factors' type
	 * @param startDate 
	 * @param endDate
	 * @param gainInterval the interval tradingdays to calculate betagain
	 * @param isSigmaOne
	 * @param isWLSorOLS
	 */
	public String calculateBetaGainForRanking(String symbol,int interval,String[] index,String[] type,Date endDate,int gainInterval,boolean isSigmaOne,boolean isWLSorOLS)throws Exception{
		SecurityManager securityManager;
		SecurityManagerImplWithCache s=null;		
		String result="";
		try {
			s = new SecurityManagerImplWithCache();
			s.setDailyDataCache(new com.lti.cache.LTICache());
			s.setSessionFactory((SessionFactory) ContextHolder.getInstance().getApplicationContext().getBean("sessionFactory"));
			securityManager=s;
			HolidayManager holidayManager = ContextHolder.getHolidayManager();
			Security mutualFund = securityManager.get(symbol);
			if (mutualFund == null)
				throw new InvalidParametersException("MF parameter is invalid!");
			Long mutualFundID = mutualFund.getID();
			
			boolean[] isFFStyle = new boolean[index.length];
			// cIndex restore the factors's ids
			Long[] cIndex = new Long[index.length];
			for(int i=0;i<index.length;i++){
				Security tmp = securityManager.get(index[i]);
				if (tmp == null)
					throw new InvalidParametersException("Index parameter is invalid!");
				cIndex[i]=tmp.getID();
				
				
				isFFStyle[i] = index[i].indexOf("^FF_")>=0;
			}
			
			if (index == null || index.length < 1)
				throw new InvalidParametersException("Size of index array is invalid!");
			//dates restore tradingday between startDate and endDate
			Date startDate=LTIDate.getNDaysAgo(endDate, 100);
			List<Date> dates = holidayManager.getTradingDateList(LTIDate.getNewNYSETradingDay(startDate,-1), LTIDate.getNewNYSETradingDay(endDate,-1));
			if (dates == null || dates.size() < 1)throw new InvalidParametersException("StartDate or EndDate parameter is invalid!");
			// dates2 restore tradingday between startDate-interval and endDate
			List<Date> dates2 = holidayManager.getTradingDateList(LTIDate.getNewNYSETradingDay(startDate,-interval-1), LTIDate.getNewNYSETradingDay(endDate,-1));
			List<BetagainDailyData> betagains=new ArrayList<BetagainDailyData>();
			WeightEstimate we = new WeightEstimate();			
			double[][] weights = new double[interval][interval];
			for(int i=0;i<interval;++i)
				weights[i][i]=Math.sqrt(i+1);
			we.setWeights(weights);
			Long cashID = securityManager.get("CASH").getID();
			List<Double> riskValue = new ArrayList<Double>();
			List<Double> mfValue = new ArrayList<Double>();
			List<List<Double>> indexValue = new ArrayList<List<Double>>();
			for(int k=0,size=dates2.size();k<size;k++){
				Date tmpDate = dates2.get(k);

				try {
					double mfV = securityManager.getAdjPrice(mutualFundID, tmpDate);
					mfValue.add(mfV);
					
					double riskV = securityManager.getAdjPrice(cashID, tmpDate);
					riskValue.add(riskV);
					
				} catch (NoPriceException e) {
					return "NOPRICE";
				}
			}		
			for(int j=0;j<index.length;j++){
				List<Double> oneIndexValue = new ArrayList<Double>();
				boolean flag = type[j].equalsIgnoreCase("1")?true:false;
				try{
					if(!flag){
						double curValue = 1;
						for(int k=0,size=dates2.size();k<size;k++)
						{
							Date tmpDate = dates2.get(k);
							double curReturn = securityManager.getAdjPrice(cIndex[j], tmpDate);
							double newValue = curValue*(1+curReturn);
							oneIndexValue.add(newValue);
							curValue = newValue; 
						}
					}					
					else{
						for(int k=0,size=dates2.size();k<size;k++){
							Date tmpDate = dates2.get(k);
							oneIndexValue.add(securityManager.getAdjPrice(cIndex[j], tmpDate));
						}
					}
					indexValue.add(oneIndexValue);
				}catch(NoPriceException e){
					return "NOPRICE";
				}
			}			
			Double[][] betas = new Double[dates.size()][index.length];
			int calStart=0;
			if(dates.size()>=gainInterval){
				calStart = dates.size()-gainInterval;
			}
			if(calStart>1)calStart--;
			int mfStart=calStart;
			for (int k = calStart; k < dates.size(); k++) {
				
				double[] mfAdjPrice = new double[interval + 1];
				double[] riskPrice = new double[interval + 1];
				double[][] indexAdjPrice = new double[index.length][interval + 1];
				
				for(int i=0;i<interval+1;i++){
					
					mfAdjPrice[i]=mfValue.get(mfStart+i);
					riskPrice[i]=riskValue.get(mfStart+i);
					for(int j=0;j<index.length;j++){
						indexAdjPrice[j][i]=indexValue.get(j).get(mfStart+i);
					}
				}
				mfStart+=1;
				Double[] d={};
				we.setCashList(riskPrice);
				if(isSigmaOne)d=we.RAA(interval, mfAdjPrice, indexAdjPrice, true);
				else d=we.RAA_NoConstraint_ForWLS(interval, mfAdjPrice, indexAdjPrice,isWLSorOLS,isFFStyle);
								
				betas[k]=d;
			}	
			double deltaBetaGain=0,indexGain=0,betaGainRanking=0;
			double preValue,curValue,dailyReturn,beta;
			int k=dates.size()-gainInterval;
			if(k<calStart+1)
				k=calStart+1;
			for(;k<dates.size();k++)
			{		
				deltaBetaGain=0;
				indexGain=0;
				for(int j=0;j<index.length;j++){
					preValue = indexValue.get(j).get(k-1);
					curValue = indexValue.get(j).get(k);
					dailyReturn = Math.log(curValue/preValue);
					beta = betas[k][j]-betas[k-1][j];
					deltaBetaGain += beta*dailyReturn;
					indexGain += betas[k][j]*dailyReturn;						
				}
				if(Math.abs(indexGain)>0.00000001)
					betaGainRanking += deltaBetaGain / Math.abs(indexGain);
			}
			return result+betaGainRanking;
		} catch (Exception e) {			
			throw e;
		}
	}
	/**
	 * 
	 * calculate the betagain for the factor 
	 * the first column is betas
	 * @author CCD
	 * @return a list with betas and betagain for all factors and the "AAAAAA" factor which stands for Sum
	 */
	public List<FactorBetaGain> calculateBetaGainForFactor(String symbol,int interval,String[] index,String[] type,boolean isSigmaOne,boolean isWLSorOLS)throws Exception{
		SecurityManager securityManager;
		SecurityManagerImplWithCache s=null;		

		try {
			s = new SecurityManagerImplWithCache();
			s.setDailyDataCache(new com.lti.cache.LTICache());
			s.setSessionFactory((SessionFactory) ContextHolder.getInstance().getApplicationContext().getBean("sessionFactory"));
			securityManager=s;
			HolidayManager holidayManager = ContextHolder.getHolidayManager();
			Security security = securityManager.get(symbol);
			if (security == null)
				throw new InvalidParametersException("symbol parameter is invalid!");
			Long securityID = security.getID();
			
			boolean[] isFFStyle = new boolean[index.length];
			// cIndex restore the factors's ids
			Long[] cIndex = new Long[index.length];
			for(int i=0;i<index.length;i++){
				Security tmp = securityManager.get(index[i]);
				if (tmp == null)
					throw new InvalidParametersException("Index parameter is invalid!");
				cIndex[i]=tmp.getID();
				isFFStyle[i] = index[i].indexOf("^FF_")>=0;
			}
			
			if (index == null || index.length < 1)
				throw new InvalidParametersException("Size of index array is invalid!");
			//dates restore tradingday between startDate and endDate
			Date endDate = security.getEndDate();
		
			Date startDate =LTIDate.getLastYear(endDate);
			startDate = LTIDate.getLastMonthDate(startDate);
		
			List<Date> dates = holidayManager.getTradingDateList(LTIDate.getNewNYSETradingDay(startDate,-1), LTIDate.getNewNYSETradingDay(endDate,-1));
			if (dates == null || dates.size() < 1)throw new InvalidParametersException("StartDate or EndDate parameter is invalid!");
			// dates2 restore tradingday between startDate-interval and endDate
			List<Date> dates2 = holidayManager.getTradingDateList(LTIDate.getNewNYSETradingDay(startDate,-interval-1), LTIDate.getNewNYSETradingDay(endDate,-1));
			List<BetagainDailyData> betagains=new ArrayList<BetagainDailyData>();
			WeightEstimate we = new WeightEstimate();			
			
			Long cashID = securityManager.get("CASH").getID();
			List<Double> riskValue = new ArrayList<Double>();
			List<Double> mfValue = new ArrayList<Double>();
			List<List<Double>> indexValue = new ArrayList<List<Double>>();
			for(int k=0,size=dates2.size();k<size;k++){
				Date tmpDate = dates2.get(k);
				try {
					double mfV = securityManager.getAdjPrice(securityID, tmpDate);
					mfValue.add(mfV);
					double riskV = securityManager.getAdjPrice(cashID, tmpDate);
					riskValue.add(riskV);
				} catch (NoPriceException e) {
					continue;
				}
			}		
			for(int j=0;j<index.length;j++){
				List<Double> oneIndexValue = new ArrayList<Double>();
				boolean flag = type[j].equalsIgnoreCase("1")?true:false;
				try{
					if(!flag){
						double curValue = 1;
						for(int k=0,size=dates2.size();k<size;k++)
						{
							Date tmpDate = dates2.get(k);
							double curReturn = securityManager.getAdjPrice(cIndex[j], tmpDate);
							double newValue = curValue*(1+curReturn);
							oneIndexValue.add(newValue);
							curValue = newValue; 
						}
					}					
					else{
						for(int k=0,size=dates2.size();k<size;k++){
							Date tmpDate = dates2.get(k);
							oneIndexValue.add(securityManager.getAdjPrice(cIndex[j], tmpDate));
						}
					}
					indexValue.add(oneIndexValue);
				}catch(NoPriceException e){
					return null;
				}
			}
			/**
			 * 0 one month
			 * 1 three months
			 * 2 six months
			 * 3 one year
			 * 4 three year
			 * *******************************/
			int[] gainIntervals = BetaGainType.getBetaGainTypeDays();
			
			Double[][] betas = new Double[dates.size()][index.length];
			int calStart=0;
			if(dates.size()>=gainIntervals[3]){
				calStart = dates.size()-gainIntervals[3];
			}
			if(calStart>1)calStart--;
			int mfStart=calStart;
			for (int k = calStart; k < dates.size(); k++) {
				
				double[] mfAdjPrice = new double[interval + 1];
				double[] riskPrice = new double[interval + 1];
				double[][] indexAdjPrice = new double[index.length][interval + 1];
				
				for(int i=0;i<interval+1;i++){
					
					mfAdjPrice[i]=mfValue.get(mfStart+i);
					riskPrice[i]=riskValue.get(mfStart+i);
					for(int j=0;j<index.length;j++){
						indexAdjPrice[j][i]=indexValue.get(j).get(mfStart+i);
					}
				}
				mfStart+=1;
				Double[] d={};
				we.setCashList(riskPrice);
				if(isSigmaOne)
					d=we.RAA(interval, mfAdjPrice, indexAdjPrice, true);
				else 
					d=we.RAA_NoConstraint_ForWLS(interval, mfAdjPrice, indexAdjPrice,isWLSorOLS,isFFStyle);
								
				betas[k]=d;
			}	
			/**************************************************************/
	
			int monthInterval,i,j,k;
			double preValue , curValue, dailyReturn, deltabeta, betagain,indexgain,deltabetagain;
			double deltaSumBetaGain,deltaSumIndexGain;
			Double sumBeta;
			
			sumBeta=security.getBeta(startDate, endDate, TimeUnit.QUARTERLY);
			if(sumBeta.isNaN())
				sumBeta=0.0;
			double[][] betaGains = new double[5][index.length+1];
			double[] sumBetaGains = new double[5];
			List<FactorBetaGain> factorBetaGainList = new ArrayList<FactorBetaGain>(index.length);
			FactorBetaGain factorBetaGain=null;
			
			for(i=0;i<index.length;++i)
			{
				factorBetaGain= new FactorBetaGain();
				factorBetaGain.setSymbol(symbol);
				factorBetaGain.setFactor(index[i]);
				factorBetaGain.setLastDate(endDate);
				j=dates.size()-1;
				while(j>0 && (betas[j][i]==null || betas[j][i].isNaN()))
					--j;
				if(j>=0)
					factorBetaGain.setBeta(betas[j][i]);
				else
					factorBetaGain.setBeta(0.0);
				factorBetaGainList.add(factorBetaGain);
			}
			for(i=0;i<5;++i)
			{
				sumBetaGains[i]=0.0;
				for(j=0;j<index.length+1;++j)
					betaGains[i][j]=0.0;
			}
			for(i=0,k=dates.size()-1,monthInterval=0;i<4;++i)
			{
				for(j=0;j<index.length;++j)
				{
					int temp= (i+3)%4;
					betaGains[i][j]=betaGains[temp][j];
					sumBetaGains[i]=sumBetaGains[temp];
				}
				for(;monthInterval<gainIntervals[i];++monthInterval,--k)
				{
					deltaSumBetaGain=0.0;
					deltaSumIndexGain=0.0;
					for(j=0;j<index.length;++j){
						preValue = indexValue.get(j).get(k-1);
						curValue = indexValue.get(j).get(k);
						dailyReturn = Math.log(curValue/preValue);
						deltabeta = betas[k][j]-betas[k-1][j];
						deltabetagain = deltabeta*dailyReturn;
						deltaSumBetaGain+=deltabetagain;
						indexgain = betas[k][j]* dailyReturn;
						deltaSumIndexGain+=indexgain;
						if(Math.abs(indexgain)>0.00001)
							betaGains[i][j]+=deltabetagain / Math.abs(indexgain);
					}
					if(Math.abs(deltaSumIndexGain)>0.00001)
						sumBetaGains[i]+=deltaSumBetaGain / Math.abs(deltaSumIndexGain);
				}
			}
			for(i=0;i<index.length;++i)
			{
				factorBetaGainList.get(i).setOneMonth(betaGains[0][i]);
				factorBetaGainList.get(i).setThreeMonth(betaGains[1][i]);
				factorBetaGainList.get(i).setHalfYear(betaGains[2][i]);
				factorBetaGainList.get(i).setOneYear(betaGains[3][i]);
			}
			factorBetaGain = new FactorBetaGain();
			factorBetaGain.setSymbol(symbol);
			factorBetaGain.setFactor("AAAAAA");
			factorBetaGain.setLastDate(endDate);
			factorBetaGain.setBeta(sumBeta);
			factorBetaGain.setOneMonth(sumBetaGains[0]);
			factorBetaGain.setThreeMonth(sumBetaGains[1]);
			factorBetaGain.setHalfYear(sumBetaGains[2]);
			factorBetaGain.setOneYear(sumBetaGains[3]);
			factorBetaGainList.add(factorBetaGain);
			return factorBetaGainList;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * get the earlyest startdate of all the funds
	 * @author CCD
	 * @param fundList the funds to get the earlyest startdate
	 * @return return the earlyest startdate of all the funds
	 */
	public Date getTheEarlyestDate(List<Security> fundList)
	{
		SecurityManager securityManager = (SecurityManager)ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
		int i;
		Security se =null;
		Date theEarlyestDate= new Date();
		Date tmpDate;
		for(i=0;i<fundList.size();++i)
		{
			tmpDate = securityManager.getStartDate(fundList.get(i).getID());
			if(tmpDate!=null && tmpDate.before(theEarlyestDate))
			{
				theEarlyestDate =  tmpDate;
			}
		}
		return theEarlyestDate;
	}		
	/**
	 * initial the assetclassmap in the tree with height=2
	 * set the securities belong to the same assetclass in the same list
	 * @author CCD
	 */
	public void initialAssetClassMapAndSecurity()
	{
		SecurityManager securityManager = (SecurityManager)ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
		int i,j;
		Long singleAssetClassID;
		
		List<Long> assetClassID = new ArrayList<Long>();
		List<Long> securityIDList = null;
		List<Security> securityList = new ArrayList<Security>();
		AssetClass ac=null;
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(AssetClass.class);
		DetachedCriteria detachedCriteria2 = DetachedCriteria.forClass(AssetClass.class);
		detachedCriteria.add(Restrictions.eq("ParentID", 0L));
		List<AssetClass> assetClassList = findByCriteria(detachedCriteria);
		if(assetClassList==null||assetClassList.size()==0)
			return;
		
		for(i=0;i<assetClassList.size();++i)
		{
			ac=assetClassList.get(i);
			assetClassID.add(ac.getID());
		}
		detachedCriteria2.add(Restrictions.in("ParentID", assetClassID));
		assetClassList.clear();
		assetClassList = findByCriteria(detachedCriteria2);
		/***assetClassMap  make a link between ac.ID and an index in fundBetaGainList in calculateHistoryBetaGain**/
		int totalsize=0;
		for(i=0;i<assetClassList.size();++i)
		{
			singleAssetClassID=assetClassList.get(i).getID();
			try{
				securityList=securityManager.getFundsByClass(singleAssetClassID);
				if(securityList!=null && securityList.size()>0)
				{
					securityIDList = new ArrayList<Long>(securityList.size());
					for(j=0;j<securityList.size();++j)
					{
						securityIDList.add(securityList.get(j).getID());
					}
					assetClassSecurityList.put(singleAssetClassID, securityIDList);
					System.out.println("There is a success "+ singleAssetClassID+"  with size "+securityIDList.size() );
				}
				
				
				totalsize+=securityList.size();
			}catch(Exception e){
				System.out.println("There is an exception "+ singleAssetClassID);
				continue;
			}
		}
		Iterator<Long> iterator = assetClassSecurityList.keySet().iterator(); 
		while(iterator.hasNext())
		{
			singleAssetClassID=iterator.next();
			System.out.println(singleAssetClassID);
			System.out.println(assetClassSecurityList.get(singleAssetClassID));
		}
		System.out.println();
		System.out.println(totalsize);
	}
	/** 
	 * calculate the daily factor betagain for all the funds 
	 * @author CCD
	 * @throws IOException 
	 */
	public void calculateFactorBetaGainForAllFunds() throws IOException
	{
		PrintWriter writer = new PrintWriter(new FileWriter(new File("d:FactorDetails.txt")));
		AssetClassManager assetClassManager=(AssetClassManager) ContextHolder.getInstance().getApplicationContext().getBean("assetClassManager");
		SecurityManager securityManager = (SecurityManager)ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
		String symbol,result=null;
		int count=0,interval=64;
		String[] index=null;
		String[] type=null;
		double betaGain=0;
		Long acID;
		Factor factor;
		Security se;
		AssetClass ac;
		List<Factor> factorList=null;
		List<FactorBetaGain> factorBetaGainList;
		List<FactorBetaGain> factorBetaGainListForAll= new ArrayList<FactorBetaGain>();
		boolean isSigmaOne=false,isWLSorOLS=true,isOK=true;
		FactorBetaGain factorBetaGain;
		
		LTIFactorManager lfm=LTIFactorManager.getInstance("RAA");
		initialAssetClassMapAndSecurity();
		Iterator<Long> iterator = assetClassSecurityList.keySet().iterator(); 
		
		while(iterator.hasNext()){
			acID=iterator.next();
			List<Long> securityIDs = assetClassSecurityList.get(acID);
			if(securityIDs!=null && securityIDs.size()>0){
				for(int j=0;j<securityIDs.size();++j)
				{
					se = securityManager.get(securityIDs.get(j));
					ac = se.getAssetClass();
					symbol = se.getSymbol();
					try{
						factorList=lfm.getFactorsForAsset(ac.getID());
					}catch(Exception e){
						System.out.println("There is an exception here");
						System.out.println(ac.getID());
						isOK=false;
					}
					if(factorList!=null && factorList.size()!=0)
					{
						index = new String[factorList.size()];
						type = new String[factorList.size()];
						for(int k=0;k<factorList.size();++k)
						{
							factor = factorList.get(k);
							index[k] = factor.getName();
							type[k] = factor.getType();
						}
					}
					else
						continue;
					try {
						/*********************calculate betagain*******************************/
						factorBetaGainList=calculateBetaGainForFactor(symbol,interval,index,type,isSigmaOne,isWLSorOLS);
						if(factorBetaGainList!=null &&factorBetaGainList.size()>0)
						{
							++count;
							factorBetaGainListForAll.addAll(factorBetaGainList);
							System.out.println("ccd test "+count);
							//fmb=new FundMonthlyBetaGain(se,monthEndDate,betaGain);
							
							//fundBetaGainList.add(fmb);
							//System.out.println(assetClassNum + " " + se.getID() + " " +betaGain);
							
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			try{
				if(factorBetaGainListForAll!= null && factorBetaGainListForAll.size()>0)
					saveAllFactorBetaGain(factorBetaGainListForAll);
				writer.println("There is a success at AssetClassID = "+acID +" with size = "+factorBetaGainListForAll.size());
				System.out.println("There is a success at AssetClassID = "+acID +" with size = "+factorBetaGainListForAll.size());
			}catch(Exception e){
				e.printStackTrace();
				System.out.println("There is an exception at AssetClassID = "+acID+" with size = "+factorBetaGainListForAll.size());
				writer.println("There is an exception at AssetClassID = "+acID+" with size = "+factorBetaGainListForAll.size());
			}
			writer.flush();
			factorBetaGainListForAll.clear();
		}
		writer.close();
		
	}
	/** 
	 * Update daily factor betagain for all the funds 
	 * @author CCD
	 * @throws IOException 
	 */
	public void updateDailyFactorBetaGainForAllFunds() throws IOException
	{
		AssetClassManager assetClassManager=(AssetClassManager) ContextHolder.getInstance().getApplicationContext().getBean("assetClassManager");
		SecurityManager securityManager = (SecurityManager)ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
		int interval=64;
		String[] index=null;
		String[] type=null;
		List<Factor> factorList=null;
		List<FactorBetaGain> originalFactorBetaGainList,newFactorBetaGainList;
		boolean isSigmaOne=false,isWLSorOLS=true;
		
		LTIFactorManager lfm=LTIFactorManager.getInstance("RAA");
		initialAssetClassMapAndSecurity();
		Iterator<Long> iterator = assetClassSecurityList.keySet().iterator(); 
		
		while(iterator.hasNext()){
			Long acID=iterator.next();
			List<Long> securityIDs = assetClassSecurityList.get(acID);
			if(securityIDs!=null && securityIDs.size()>0){
				for(int j=0;j<securityIDs.size();++j)
				{
					Security se = securityManager.get(securityIDs.get(j));
					AssetClass ac = se.getAssetClass();
					String symbol = se.getSymbol();
					try{
						factorList=lfm.getFactorsForAsset(ac.getID());
					}catch(Exception e){
						e.printStackTrace();
					}
					if(factorList!=null && factorList.size()!=0)
					{
						DetachedCriteria detachedCriteria=DetachedCriteria.forClass(FactorBetaGain.class);
						detachedCriteria.add(Restrictions.eq("Symbol", symbol));
						originalFactorBetaGainList=findByCriteria(detachedCriteria);
						if(originalFactorBetaGainList!=null && originalFactorBetaGainList.size()>0)
						{
							if(!LTIDate.before(originalFactorBetaGainList.get(0).getLastDate(), se.getEndDate()))
								continue;
						}
						index = new String[factorList.size()];
						type = new String[factorList.size()];
						for(int k=0;k<factorList.size();++k)
						{
							Factor factor = factorList.get(k);
							index[k] = factor.getName();
							type[k] = factor.getType();
						}
						try {
							newFactorBetaGainList=calculateBetaGainForFactor(symbol,interval,index,type,isSigmaOne,isWLSorOLS);
							if(newFactorBetaGainList!=null && newFactorBetaGainList.size()>0)
							{
								try{
									saveAllFactorBetaGain(newFactorBetaGainList);
									removeAllFactorBetaGain(originalFactorBetaGainList);
								}catch(Exception e){
									continue;
								}
							}
						} catch (Exception e) {
							continue;
						}	
					}
				}
			}
		}
	}
	/** 
	 * update the historical betagain for all the funds on the first date of each month
	 * @author CCD unfinish
	 * @throws IOException 
	 */
	public void updateHistoricalBetaGainForAllFunds() throws IOException
	{
		AssetClassManager assetClassManager=(AssetClassManager) ContextHolder.getInstance().getApplicationContext().getBean("assetClassManager");
		SecurityManager securityManager = (SecurityManager)ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
		
		Date monthEndDate= new Date();
		monthEndDate=LTIDate.getFirstDateOfMonth(monthEndDate);
		int i,j,k,count=0,interval=64;
		
		//List<FundMonthlyBetaGain> fundBetaGainList = new ArrayList<FundMonthlyBetaGain>();
		List<SecurityRanking> fundBetaGainList = new ArrayList<SecurityRanking>();
		
		String[] index=null;
		String[] type=null;
		double betaGain=0;
		
		Factor factor;
		List<Factor> factorList=null;
		List<SecurityRanking> securityRankingList;
		LTIFactorManager lfm=LTIFactorManager.getInstance("RAA");
		
		SecurityRanking securityRanking = null;
		
		boolean isSigmaOne=false,isWLSorOLS=true;
		
		initialAssetClassMapAndSecurity();
		Iterator<Long> iterator = assetClassSecurityList.keySet().iterator(); 
		while(iterator.hasNext())
		{
			Long acID=iterator.next();
			List<Long> securityIDs = assetClassSecurityList.get(acID);
			if(securityIDs!=null && securityIDs.size()>0){
				for(j=0;j<securityIDs.size();++j)
				{
					Security se = securityManager.get(securityIDs.get(j));
					AssetClass ac = se.getAssetClass();
					String symbol = se.getSymbol();
					try{
						factorList=lfm.getFactorsForAsset(ac.getID());
					}catch(Exception e){
						e.printStackTrace();
					}
					if(factorList!=null && factorList.size()!=0)
					{
						DetachedCriteria detachedCriteria=DetachedCriteria.forClass(SecurityRanking.class);
						detachedCriteria.add(Restrictions.eq("Symbol", symbol));
						detachedCriteria.addOrder(Order.desc("EndDate"));
						securityRankingList=findByCriteria(detachedCriteria, 1, 0);
						if(securityRankingList!=null &&securityRankingList.size()>0)
						{
							
							if(!LTIDate.after(monthEndDate, securityRankingList.get(0).getEndDate()))
									continue;
						}
						index = new String[factorList.size()];
						type = new String[factorList.size()];
						for(k=0;k<factorList.size();++k)
						{
							factor = factorList.get(k);
							index[k] = factor.getName();
							type[k] = factor.getType();
						}
						String result;
						try{
							result=calculateBetaGainForRanking(symbol,interval,index,type,monthEndDate,64,isSigmaOne,isWLSorOLS);
						}catch(Exception e){
							continue;
						}
						if(!result.equalsIgnoreCase("NOPRICE"))
						{
							betaGain = Double.parseDouble(result);
							securityRanking = new SecurityRanking();
							securityRanking.setEndDate(monthEndDate);
							securityRanking.setBetaGain(betaGain);
							securityRanking.setSecurityID(se.getID());
							securityRanking.setAssetClassID(se.getClassID());
							securityRanking.setInterval(3);
							securityRanking.setSecondClassID(acID);
							securityRanking.setSymbol(symbol);
							fundBetaGainList.add(securityRanking);
						}
					}
				}
				if(fundBetaGainList.size()>0)
				{
					DetachedCriteria detachedCriteria= DetachedCriteria.forClass(SecurityRanking.class);
					detachedCriteria.add(Restrictions.eq("SecondClassID", acID));
					detachedCriteria.add(Restrictions.eq("EndDate",monthEndDate));
					List<SecurityRanking> originalSecurityRankingList=findByCriteria(detachedCriteria);
					if(originalSecurityRankingList!=null && originalSecurityRankingList.size()>0)
						fundBetaGainList.addAll(originalSecurityRankingList);
					Comparator comp = new BetaGainComparator();
					Collections.sort(fundBetaGainList,comp);
					int onepart= fundBetaGainList.size()/5;
					Integer[] parts = new Integer[5];
					parts[0]=onepart;
					for(j=1;j<4;++j)
					{
						parts[j]=parts[j-1]+onepart;
					}
					parts[4]=fundBetaGainList.size();
					/*****************set ranking info***************************/
					for(k=0,j=0;j<5;++j)
					{
						for(;k<parts[j];++k)
						{
							fundBetaGainList.get(k).setRanking(5-j);
						}
					}
					saveOrUpdateAllSecurityRanking(fundBetaGainList);
					fundBetaGainList.clear();
				}
			}
		}
}
	/** 
	 * calculate the historical betagain for all the funds
	 * @author CCD
	 * @param startyear from which year to initial the historical betagain ranking
	 * @throws IOException 
	 */
	public void calculateHistoricalBetaGainForAllFunds(int startyear) throws IOException
	{
		//PrintWriter writer = new PrintWriter(new FileWriter(new File("d:result_test.txt")));
		AssetClassManager assetClassManager=(AssetClassManager) ContextHolder.getInstance().getApplicationContext().getBean("assetClassManager");
		SecurityManager securityManager = (SecurityManager)ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
		Security se=null;
		AssetClass ac=null;
		int i,j,k,count=0,interval=64;
		
		
		Date startDate,monthStartDate,monthEndDate,today;
		Calendar tmpCa = Calendar.getInstance();
		tmpCa.set(startyear,Calendar.JANUARY,1);
		monthEndDate =  LTIDate.clearHMSM(tmpCa.getTime());
		tmpCa = Calendar.getInstance();
		tmpCa.set(startyear, Calendar.APRIL, 1);
		/*********** today stands for 2009.1.1 ****************/
		today=LTIDate.clearHMSM(tmpCa.getTime());
		
		List<SecurityRanking> fundBetaGainList = new ArrayList<SecurityRanking>();
		
		String symbol,result=null;
		String[] index=null;
		String[] type=null;
		double betaGain=0;
		
		Factor factor;
		List<Factor> factorList=null;
		LTIFactorManager lfm=LTIFactorManager.getInstance("RAA");
		
		SecurityRanking securityRanking = null;
		
		boolean isSigmaOne=false,isWLSorOLS=true,isOK=true;

		Long acID;
		
		
		initialAssetClassMapAndSecurity();
		
		while(!LTIDate.equals(monthEndDate, today))
		{
			/**************calculate the monthly betagain of each fund****************/
			Iterator<Long> iterator = assetClassSecurityList.keySet().iterator(); 
			while(iterator.hasNext())
			{
				acID=iterator.next();
				List<Long> securityIDs = assetClassSecurityList.get(acID);
				if(securityIDs!=null && securityIDs.size()>0){
					for(j=0;j<securityIDs.size();++j)
					{
						se = securityManager.get(securityIDs.get(j));
						ac = se.getAssetClass();
						symbol = se.getSymbol();
						try{
							factorList=lfm.getFactorsForAsset(ac.getID());
						}catch(Exception e){
							continue;
						}
						if(factorList!=null && factorList.size()!=0)
						{
							index = new String[factorList.size()];
							type = new String[factorList.size()];
							for(k=0;k<factorList.size();++k)
							{
								factor = factorList.get(k);
								index[k] = factor.getName();
								type[k] = factor.getType();
							}
							try{
								result=calculateBetaGainForRanking(symbol,interval,index,type,monthEndDate,64,isSigmaOne,isWLSorOLS);
							}catch(Exception e){
								continue;
							}
							if(!result.equalsIgnoreCase("NOPRICE"))
							{
								betaGain = Double.parseDouble(result);
								securityRanking = new SecurityRanking();
								securityRanking.setEndDate(monthEndDate);
								securityRanking.setBetaGain(betaGain);
								securityRanking.setSecurityID(se.getID());
								securityRanking.setAssetClassID(se.getClassID());
								securityRanking.setInterval(3);
								securityRanking.setSecondClassID(acID);
								securityRanking.setSymbol(symbol);
								fundBetaGainList.add(securityRanking);
							}
						}
					}
					if(fundBetaGainList.size()>0)
					{
						Comparator comp = new BetaGainComparator();
						Collections.sort(fundBetaGainList,comp);
					//	writer.println("The list for the assetclass "+ acID +"with size = "+fundBetaGainList.size());
					//	writer.println("**************************************************");
						
						int onepart= fundBetaGainList.size()/5;
						Integer[] parts = new Integer[5];
						parts[0]=onepart;
						for(j=1;j<4;++j)
						{
							parts[j]=parts[j-1]+onepart;
						}
						parts[4]=fundBetaGainList.size();
						/*****************set ranking info***************************/
						for(k=0,j=0;j<5;++j)
							for(;k<parts[j];++k)
								fundBetaGainList.get(k).setRanking(5-j);
						saveAllSecurityRanking(fundBetaGainList);
						fundBetaGainList.clear();
						//writer.println();
						//writer.println();
						//writer.flush();
					}
				}
				//writer.println(monthEndDate+" "+acID+" finished");
				System.out.println(monthEndDate+" "+acID+" finished");
			}
			//writer.println(monthEndDate+" finished");
			System.out.println(monthEndDate+" finished");
			
			monthEndDate = LTIDate.getNextMonthFirstDay(monthEndDate);
		}
		//writer.close();
	}
	/**
	 * set the n month beta gain table for calculation of one year betagain
	 */
	public List<SecurityRanking> setTheNumMonthBetaGainTable(List<SecurityRanking> securityRankingList,Date fromDate,Date toDate,int monthCount)
	{
		List<SecurityRanking> securityRankingRow= new ArrayList<SecurityRanking>(monthCount);
		int index=0;
		Date startDate=fromDate;
		Date endDate=toDate;
		while(LTIDate.before(startDate, endDate)||LTIDate.equals(startDate, endDate))
		{
			if(index<securityRankingList.size())
			{
			
				SecurityRanking securityRanking = securityRankingList.get(index);
				if(LTIDate.equals(securityRanking.getEndDate(),endDate)){
					securityRankingRow.add(securityRanking);
					++index;
				}
				else{
					/**data in enddate is lost, we set null and add it to the row**/
					securityRankingRow.add(null);
				}
			}
			else
				securityRankingRow.add(null);
			endDate=LTIDate.getLastMonthDate(endDate);
		}
		return securityRankingRow;
	}
	
	/**
	 * calculate the historical betagain special assetclass by acID
	 * @param startyear calculate from startyear-01-01 to  startyear+1-01-01
	 */
	public void calculateHistoricalBetaGainByAssetClassID(Long acID,int startyear) throws IOException{
		PrintWriter writer = new PrintWriter(new FileWriter(new File("d:moderate_allocation.txt")));
		AssetClassManager assetClassManager=(AssetClassManager) ContextHolder.getInstance().getApplicationContext().getBean("assetClassManager");
		SecurityManager securityManager = (SecurityManager)ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
		Security se=null;
		AssetClass ac=null;
		int i,j,k,count=0,interval=64;
		
		Date startDate,monthStartDate,monthEndDate,today;
		Calendar tmpCa = Calendar.getInstance();
		tmpCa.set(startyear,Calendar.JANUARY,1);
		monthEndDate =  LTIDate.clearHMSM(tmpCa.getTime());
		tmpCa = Calendar.getInstance();
		tmpCa.set(startyear+2, Calendar.JANUARY, 1);
		/*********** today stands for 2009.1.1 ****************/
		today=LTIDate.clearHMSM(tmpCa.getTime());
		
		List<SecurityRanking> fundBetaGainList = new ArrayList<SecurityRanking>();
		
		String symbol,result=null;
		String[] index=null;
		String[] type=null;
		double betaGain=0;
		
		Factor factor;
		List<Factor> factorList=null;
		LTIFactorManager lfm=LTIFactorManager.getInstance("RAA");
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(AssetClass.class);
		SecurityRanking securityRanking = null;
		List<Security> securityList;
		boolean isSigmaOne=false,isWLSorOLS=true,isOK=true;
		securityList=securityManager.getFundsByClass(acID);
		
		factorList=lfm.getFactorsForAsset(acID);
		if(factorList!=null && factorList.size()>0)
		{
			if(factorList.size()>2)
				interval=30;
			else
				interval=20;
		}
		
		
		while(!LTIDate.equals(monthEndDate, today))
		{
			/**************calculate the monthly betagain of each fund****************/
			if(securityList!=null && securityList.size()>0){
				for(j=0;j<securityList.size();++j)
				{
					se = securityList.get(j);
					ac = se.getAssetClass();
					symbol = se.getSymbol();
					try{
						factorList=lfm.getFactorsForAsset(ac.getID());
					}catch(Exception e){
						continue;
					}
					if(factorList!=null && factorList.size()!=0)
					{
						index = new String[factorList.size()];
						type = new String[factorList.size()];
						for(k=0;k<factorList.size();++k)
						{
							factor = factorList.get(k);
							index[k] = factor.getName();
							type[k] = factor.getType();
						}
						try{
							result=calculateBetaGainForRanking(symbol,interval,index,type,monthEndDate,64,isSigmaOne,isWLSorOLS);
						}catch(Exception e){
							continue;
						}
						if(!result.equalsIgnoreCase("NOPRICE"))
						{
							betaGain = Double.parseDouble(result);
							securityRanking = new SecurityRanking();
							securityRanking.setEndDate(monthEndDate);
							securityRanking.setBetaGain(betaGain);
							securityRanking.setSecurityID(se.getID());
							securityRanking.setAssetClassID(se.getClassID());
							securityRanking.setInterval(3);
							securityRanking.setSecondClassID(acID);
							securityRanking.setSymbol(symbol);
							fundBetaGainList.add(securityRanking);
						}
					}
				}
				if(fundBetaGainList.size()>0)
				{
					Comparator comp = new BetaGainComparator();
					Collections.sort(fundBetaGainList,comp);
					writer.println("The list for the assetclass "+ acID +"with size = "+fundBetaGainList.size());
					writer.println("**************************************************");
					
					int onepart= fundBetaGainList.size()/5;
					Integer[] parts = new Integer[5];
					parts[0]=onepart;
					for(j=1;j<4;++j)
					{
						parts[j]=parts[j-1]+onepart;
					}
					parts[4]=fundBetaGainList.size();
					/*****************set ranking info***************************/
					for(k=0,j=0;j<5;++j)
						for(;k<parts[j];++k)
							fundBetaGainList.get(k).setRanking(5-j);
					saveAllSecurityRanking(fundBetaGainList);
					fundBetaGainList.clear();
					writer.println();
					writer.println();
					writer.flush();
				}
			}
			writer.println(monthEndDate+" "+acID+" finished");
			System.out.println(monthEndDate+" "+acID+" finished");
			monthEndDate = LTIDate.getNextMonthFirstDay(monthEndDate);
		}
		writer.close();
	}
	/**
	 * calcualte one year accumulate betagain for certain assetclass
	 * @param acID id for sepcial assetclass
	 * @throws IOException 
	 */
	public void calculateHistoricalHalfYearBetaGainByAssetClassID(Long acID) throws IOException{
		PrintWriter writer = new PrintWriter(new FileWriter(new File("d:moderate_allocation_oneyear.txt")));
		AssetClassManager assetClassManager=(AssetClassManager) ContextHolder.getInstance().getApplicationContext().getBean("assetClassManager");
		SecurityManager securityManager = (SecurityManager)ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
		Security se=null;
		int totalSize=0;
		List<Security> securityList= securityManager.getFundsByClass(acID);
		List<List<SecurityRanking>> securityRankingList= new ArrayList<List<SecurityRanking>>();
		List<SecurityRanking> singleSecurityRankingList=null;
		List<SecurityRanking> oneYearSecurityRankingList=null;
		List<SecurityRanking> securityRankingRow=null;
		SecurityRanking singleSecurityRanking=null;
		SecurityRanking securityRanking=null;
		Date lastDate=new Date();
		lastDate=LTIDate.getDate(2009,9,1);
		Date toDate=LTIDate.getDate(2009,9,1);
		Date fromDate=LTIDate.getDate(2002,1,1);
		if(securityList!=null &&securityList.size()>0)
		{
			/**get the historical 3 month betagain list for each security**/
			for(int i=0;i<securityList.size();++i)
			{
				DetachedCriteria detachedCriteria = DetachedCriteria.forClass(SecurityRanking.class);
				detachedCriteria.add(Restrictions.eq("SecurityID", securityList.get(i).getID()));
				detachedCriteria.add(Restrictions.eq("Interval", 3));
				detachedCriteria.add(Restrictions.le("EndDate", lastDate));
				detachedCriteria.addOrder(Order.desc("EndDate"));
				/**if some month don't have data , we will set the month data null to we get the continuous datas **/
				singleSecurityRankingList=findByCriteria(detachedCriteria);
				if(singleSecurityRankingList!=null && singleSecurityRankingList.size()>0)
				{
					securityRankingRow= setTheNumMonthBetaGainTable(singleSecurityRankingList,fromDate,toDate,93);
					securityRankingList.add(securityRankingRow);
				}
			}
			if(securityRankingList.size()>0){
				Date endDate=LTIDate.getDate(2009,9,1);
				singleSecurityRankingList=securityRankingList.get(0);
				totalSize=singleSecurityRankingList.size();
				double betaGain=0.0;
				boolean notContinue=false;
				for(int i=totalSize-1;i>8;--i)
				{
					oneYearSecurityRankingList= new ArrayList<SecurityRanking>();
					for(int j=0;j<securityRankingList.size();++j){
						notContinue=false;
						singleSecurityRankingList=securityRankingList.get(j);
						singleSecurityRanking=new SecurityRanking();
						securityRanking=singleSecurityRankingList.get(i);
						if(securityRanking==null)
							continue;
						singleSecurityRanking.setSecurityID(securityRanking.getSecurityID());
						singleSecurityRanking.setSymbol(securityRanking.getSymbol());
						singleSecurityRanking.setAssetClassID(securityRanking.getAssetClassID());
						singleSecurityRanking.setSecondClassID(acID);
						singleSecurityRanking.setEndDate(endDate);
						singleSecurityRanking.setInterval(12);
						betaGain=0;
						/**calculate for each month i */
						for(int k=0,m=i;k<2;++k,m-=3){
							securityRanking = singleSecurityRankingList.get(m);
							if(securityRanking==null)
							{
								notContinue=true;
								break;
							}
							betaGain+=securityRanking.getBetaGain();
						}
						if(notContinue)
							continue;
						singleSecurityRanking.setBetaGain(betaGain);
						oneYearSecurityRankingList.add(singleSecurityRanking);
					}
					if(oneYearSecurityRankingList.size()>0){
						Comparator comp = new BetaGainComparator();
						Collections.sort(oneYearSecurityRankingList,comp);
						
						int onepart= oneYearSecurityRankingList.size()/5;
						Integer[] parts = new Integer[5];
						parts[0]=onepart;
						for(int j=1;j<4;++j)
						{
							parts[j]=parts[j-1]+onepart;
						}
						parts[4]=oneYearSecurityRankingList.size();
						/*****************set ranking info***************************/
						for(int k=0,j=0;j<5;++j)
							for(;k<parts[j];++k)
								oneYearSecurityRankingList.get(k).setRanking(5-j);
						saveAllSecurityRanking(oneYearSecurityRankingList);
					}
					writer.println(endDate+ " has finished...");
					endDate=LTIDate.getLastMonthDate(endDate);
				}
				writer.println("Done");
			}
		}
		writer.flush();
		writer.close();
	}
	/**
	 * calcualte one year accumulate betagain for certain assetclass
	 * @param acID id for sepcial assetclass
	 * @throws IOException 
	 */
	public void calculateHistoricalOneYearBetaGainByAssetClassID(Long acID) throws IOException{
		PrintWriter writer = new PrintWriter(new FileWriter(new File("d:moderate_allocation_oneyear.txt")));
		AssetClassManager assetClassManager=(AssetClassManager) ContextHolder.getInstance().getApplicationContext().getBean("assetClassManager");
		SecurityManager securityManager = (SecurityManager)ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
		Security se=null;
		int totalSize=0;
		List<Security> securityList= securityManager.getFundsByClass(acID);
		List<List<SecurityRanking>> securityRankingList= new ArrayList<List<SecurityRanking>>();
		List<SecurityRanking> singleSecurityRankingList=null;
		List<SecurityRanking> oneYearSecurityRankingList=null;
		List<SecurityRanking> securityRankingRow=null;
		SecurityRanking singleSecurityRanking=null;
		SecurityRanking securityRanking=null;
		Date lastDate=new Date();
		lastDate=LTIDate.getDate(2009,10,1);
		Date toDate=LTIDate.getDate(2009,10,1);
		Date fromDate=LTIDate.getDate(2002,1,1);
		if(securityList!=null &&securityList.size()>0)
		{
			/**get the historical 3 month betagain list for each security**/
			for(int i=0;i<securityList.size();++i)
			{
				DetachedCriteria detachedCriteria = DetachedCriteria.forClass(SecurityRanking.class);
				detachedCriteria.add(Restrictions.eq("SecurityID", securityList.get(i).getID()));
				detachedCriteria.add(Restrictions.eq("Interval", 3));
				detachedCriteria.add(Restrictions.le("EndDate", lastDate));
				detachedCriteria.addOrder(Order.desc("EndDate"));
				/**if some month don't have data , we will set the month data null to we get the continuous datas **/
				singleSecurityRankingList=findByCriteria(detachedCriteria);
				if(singleSecurityRankingList!=null && singleSecurityRankingList.size()>0)
				{
					securityRankingRow= setTheNumMonthBetaGainTable(singleSecurityRankingList,fromDate,toDate,93);
					securityRankingList.add(securityRankingRow);
				}
			}
			if(securityRankingList.size()>0){
				Date endDate=LTIDate.getDate(2009,10,1);
				singleSecurityRankingList=securityRankingList.get(0);
				totalSize=singleSecurityRankingList.size();
				double betaGain=0.0;
				boolean notContinue=false;
				for(int i=totalSize-1;i>8;--i)
				{
					oneYearSecurityRankingList= new ArrayList<SecurityRanking>();
					for(int j=0;j<securityRankingList.size();++j){
						notContinue=false;
						singleSecurityRankingList=securityRankingList.get(j);
						singleSecurityRanking=new SecurityRanking();
						securityRanking=singleSecurityRankingList.get(i);
						if(securityRanking==null)
							continue;
						singleSecurityRanking.setSecurityID(securityRanking.getSecurityID());
						singleSecurityRanking.setSymbol(securityRanking.getSymbol());
						singleSecurityRanking.setAssetClassID(securityRanking.getAssetClassID());
						singleSecurityRanking.setSecondClassID(acID);
						singleSecurityRanking.setEndDate(endDate);
						singleSecurityRanking.setInterval(12);
						betaGain=0;
						/**calculate for each month i */
						for(int k=0,m=i;k<4;++k,m-=3){
							securityRanking = singleSecurityRankingList.get(m);
							if(securityRanking==null)
							{
								notContinue=true;
								break;
							}
							betaGain+=securityRanking.getBetaGain();
						}
						if(notContinue)
							continue;
						singleSecurityRanking.setBetaGain(betaGain);
						oneYearSecurityRankingList.add(singleSecurityRanking);
					}
					if(oneYearSecurityRankingList.size()>0){
						Comparator comp = new BetaGainComparator();
						Collections.sort(oneYearSecurityRankingList,comp);
						
						int onepart= oneYearSecurityRankingList.size()/5;
						Integer[] parts = new Integer[5];
						parts[0]=onepart;
						for(int j=1;j<4;++j)
						{
							parts[j]=parts[j-1]+onepart;
						}
						parts[4]=oneYearSecurityRankingList.size();
						/*****************set ranking info***************************/
						for(int k=0,j=0;j<5;++j)
							for(;k<parts[j];++k)
								oneYearSecurityRankingList.get(k).setRanking(5-j);
						saveAllSecurityRanking(oneYearSecurityRankingList);
					}
					writer.println(endDate+ " has finished...");
					endDate=LTIDate.getLastMonthDate(endDate);
				}
				writer.println("Done");
			}
		}
		writer.flush();
		writer.close();
	}
	/**
	 * calcualte three year accumulate betagain for certain assetclass
	 * @param acID id for sepcial assetclass
	 * @throws IOException 
	 */
	public void calculateHistoricalThreeYearBetaGainByAssetClassID(Long acID) throws IOException{
		PrintWriter writer = new PrintWriter(new FileWriter(new File("d:moderate_allocation.txt")));
		AssetClassManager assetClassManager=(AssetClassManager) ContextHolder.getInstance().getApplicationContext().getBean("assetClassManager");
		SecurityManager securityManager = (SecurityManager)ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
		Security se=null;
		int totalSize=0;
		List<Security> securityList= securityManager.getFundsByClass(acID);
		List<List<SecurityRanking>> securityRankingList= new ArrayList<List<SecurityRanking>>();
		List<SecurityRanking> singleSecurityRankingList=null;
		List<SecurityRanking> threeYearSecurityRankingList=null;
		List<SecurityRanking> securityRankingRow=null;
		SecurityRanking securityRanking=null;
		SecurityRanking singleSecurityRanking=null;
		Date lastDate=new Date();
		lastDate=LTIDate.getDate(2009,4,1);
		Date toDate=LTIDate.getDate(2009,4,1);
		Date fromDate=LTIDate.getDate(2004,10,1);
		boolean notContinue=false;
		
		if(securityList!=null &&securityList.size()>0)
		{
			/**get the historical one year betagain list for each security**/
			for(int i=0;i<securityList.size();++i)
			{
				DetachedCriteria detachedCriteria = DetachedCriteria.forClass(SecurityRanking.class);
				detachedCriteria.add(Restrictions.eq("SecurityID", securityList.get(i).getID()));
				detachedCriteria.add(Restrictions.eq("Interval", 12));
				detachedCriteria.add(Restrictions.le("EndDate", lastDate));
				detachedCriteria.addOrder(Order.desc("EndDate"));
				singleSecurityRankingList=findByCriteria(detachedCriteria);
				if(singleSecurityRankingList!=null && singleSecurityRankingList.size()>0)
				{
					securityRankingRow=setTheNumMonthBetaGainTable(singleSecurityRankingList, fromDate, toDate, 55);
					securityRankingList.add(securityRankingRow);
				}
			}
			if(securityRankingList.size()>0){
				Date endDate=LTIDate.getDate(2009,4,1);
				singleSecurityRankingList=securityRankingList.get(0);
				totalSize=singleSecurityRankingList.size();
				double betaGain=0.0;
				for(int i=totalSize-1;i>23;--i)
				{
					threeYearSecurityRankingList= new ArrayList<SecurityRanking>();
					for(int j=0;j<securityRankingList.size();++j){
						notContinue=false;
						singleSecurityRankingList=securityRankingList.get(j);
						singleSecurityRanking=new SecurityRanking();
						securityRanking=singleSecurityRankingList.get(i);
						if(securityRanking==null)
							continue;
						singleSecurityRanking.setSecurityID(securityRanking.getSecurityID());
						singleSecurityRanking.setSymbol(securityRanking.getSymbol());
						singleSecurityRanking.setAssetClassID(securityRanking.getAssetClassID());
						singleSecurityRanking.setSecondClassID(acID);
						singleSecurityRanking.setEndDate(endDate);
						singleSecurityRanking.setInterval(36);
						betaGain=0;
						/**calculate for each month i */
						for(int k=0,m=i;k<3;++k,m-=12){
							securityRanking=singleSecurityRankingList.get(m);
							if(securityRanking==null)
							{
								notContinue=true;
								break;
							}
							betaGain+=singleSecurityRankingList.get(m).getBetaGain();
						}
						if(notContinue)
							continue;
						singleSecurityRanking.setBetaGain(betaGain);
						threeYearSecurityRankingList.add(singleSecurityRanking);
					}
					if(threeYearSecurityRankingList.size()>0){
						Comparator comp = new BetaGainComparator();
						Collections.sort(threeYearSecurityRankingList,comp);
						
						int onepart= threeYearSecurityRankingList.size()/5;
						Integer[] parts = new Integer[5];
						parts[0]=onepart;
						for(int j=1;j<4;++j)
						{
							parts[j]=parts[j-1]+onepart;
						}
						parts[4]=threeYearSecurityRankingList.size();
						/*****************set ranking info***************************/
						for(int k=0,j=0;j<5;++j)
							for(;k<parts[j];++k)
								threeYearSecurityRankingList.get(k).setRanking(5-j);
						saveAllSecurityRanking(threeYearSecurityRankingList);
					}
					writer.println(endDate+ " has finished...");
					endDate=LTIDate.getLastMonthDate(endDate);
				}
				writer.println("Done");
			}
		}
	}
	/**
	 * get the historical betaGain  by security Symbol
	 * @author CCD
	 * @param symbol a fund's symbol
	 * @param interval
	 * 		interval=3 return the short-term ranking
	 * 		interval=12 return the mid-term ranking
	 * 		interval=36 return the long-term ranking
	 * @return return the historical betagain ranking of the symbol
	 */
	public List<SecurityRanking> getHistoricalRankingBySymbol(String symbol,int interval)
	{
		SecurityManager securityManager =(SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
		Long securityID = securityManager.get(symbol).getID();
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(SecurityRanking.class);
		detachedCriteria.add(Restrictions.eq("SecurityID", securityID));
		detachedCriteria.add(Restrictions.eq("Interval", interval));
		detachedCriteria.addOrder(Order.desc("EndDate"));
		List<SecurityRanking> securityRankingList= findByCriteria(detachedCriteria);
		if(securityRankingList!=null && securityRankingList.size()>0)
			return securityRankingList;
		return null;	
	}
	/**
	 * get the factor betaGain  by security Symbol
	 * @author CCD
	 * @param symbol the symbol of the fund
	 * @return the list(0) is stands for the factor(1),and it's formatted like "symbol factor 1 3 6 12"
	 */
	public List<FactorBetaGain> getFactorDetailsBySymbol(String symbol){
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(FactorBetaGain.class);
		detachedCriteria.add(Restrictions.eq("Symbol",symbol));
		detachedCriteria.addOrder(Order.desc("Factor"));
		List<FactorBetaGain> factorBetaGainList= findByCriteria(detachedCriteria);
		if(factorBetaGainList!=null && factorBetaGainList.size()>0)
			return factorBetaGainList;
		return null;
	}
	/**
	 * get the current Ranking  by security Symbol
	 * @author CCD
	 * @param symbol the symbol of the fund
	 * @return the current Ranking of the fund
	 */
	public Integer getCurrentRankingBySymbol(String symbol){
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(SecurityRanking.class);
		detachedCriteria.add(Restrictions.eq("Symbol",symbol));
		Date newestDate= new Date();
		Calendar tmpCa = Calendar.getInstance();
		tmpCa.set(newestDate.getYear(),newestDate.getMonth(),1);
		newestDate = LTIDate.clearHMSM(tmpCa.getTime());
		detachedCriteria.add(Restrictions.eq("EndDate", newestDate));
		List<SecurityRanking> securityRankingList= findByCriteria(detachedCriteria);
		if(securityRankingList!=null && securityRankingList.size()>0)
			return securityRankingList.get(0).getRanking();
		else
		{
			newestDate = LTIDate.getLastMonthDate(newestDate);
			securityRankingList= findByCriteria(detachedCriteria);
			if(securityRankingList!=null && securityRankingList.size()>0)
				return securityRankingList.get(0).getRanking();
		}
		return -1;
	}
	/**
	 * get all the fund's current ranking by assetClass name
	 * @author CCD
	 * @param name the assetClass's name
	 */
	public List<SecurityRanking> getFundRankingByAssetClassName(String name,Date date, Integer size){
		AssetClassManager assetClassManager=(AssetClassManager) ContextHolder.getInstance().getApplicationContext().getBean("assetClassManager");
		Long assetClassID = assetClassManager.get(name).getID();
		return getFundRankingByAssetClassID(assetClassID,date,size);
	}
	/**
	 * get all the fund's current ranking by assetClass ID
	 * @author CCD
	 * @param name the assetClass's name
	 */
	public List<SecurityRanking> getFundRankingByAssetClassID(Long assetClassID,Date date, Integer size){
		AssetClassManager assetClassManager=(AssetClassManager) ContextHolder.getInstance().getApplicationContext().getBean("assetClassManager");
		SecurityManager securityManager = (SecurityManager)ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
		List<Security> securityList=null;
		List<Long> securityIDList = null;
		int i;
		securityList=securityManager.getFundsByClass(assetClassID);
		if(securityList!=null && securityList.size()>0)
		{
			securityIDList = new ArrayList<Long>(securityList.size());
			for(i=0;i<securityList.size();++i)
				securityIDList.add(securityList.get(i).getID());
		}
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(SecurityRanking.class);
		detachedCriteria.add(Restrictions.eq("EndDate", date));
		detachedCriteria.add(Restrictions.in("SecurityID", securityIDList));
		detachedCriteria.addOrder(Order.desc("BetaGain"));
		if(size==0)
			return findByCriteria(detachedCriteria);
		return findByCriteria(detachedCriteria,size,0);
	}
	/**
	 * get all the assetClass's ranking fund
	 * @author CCD
	 */
	public Map<String,List<SecurityRanking>> getRankingFundMap(Date today)
	{
		initialAssetClassMapAndSecurity();
		AssetClassManager assetClassManager = (AssetClassManager)ContextHolder.getInstance().getApplicationContext().getBean("assetClassManager");
		HashMap<String,List<SecurityRanking>> securityRankingMap = new HashMap<String,List<SecurityRanking>>();
		Iterator<Long> iterator = assetClassSecurityList.keySet().iterator();
		Long acID;
		int i;
		while(iterator.hasNext())
		{
			acID=iterator.next();
			List<Long> securityIDs = assetClassSecurityList.get(acID);
			if(securityIDs!=null && securityIDs.size()>0)
			{
				List<SecurityRanking> singleSecurityRankingList=null;
				DetachedCriteria detachedCriteria = DetachedCriteria.forClass(SecurityRanking.class);
				detachedCriteria.add(Restrictions.in("SecurityID", securityIDs));
				detachedCriteria.add(Restrictions.eq("EndDate",today));
				detachedCriteria.addOrder(Order.desc("Ranking"));
				//singleSecurityRankingList=findByCriteria(detachedCriteria,10,0);
				singleSecurityRankingList=findByCriteria(detachedCriteria);
				if(singleSecurityRankingList!=null && singleSecurityRankingList.size()>0)
					securityRankingMap.put(assetClassManager.get(acID).getName(),singleSecurityRankingList);
			}
		}
		return securityRankingMap;
	}
	/**
	 * save  the betagain ranking info for one month
	 * @author CCD
	 * @param srs the historical data for betagain ranking for one month
	 */
	public void saveAllSecurityRanking(List<SecurityRanking> srs) {
		if (srs == null || srs.size() <= 0){
			return;
		}
		super.saveAll(srs);
	}
	/**
	 * save or update the betagain ranking info for one month
	 * @author CCD
	 * @param srs the historical data for betagain ranking for one month
	 */
	public void saveOrUpdateAllSecurityRanking(List<SecurityRanking> srs)
	{
		if(srs==null|| srs.size()<=0)
			return ;
		super.saveOrUpdateAll(srs);
	}
	
	
	public void saveAllFactorBetaGain(List<FactorBetaGain> fbg) {
		if (fbg == null || fbg.size() <= 0){
			return;
		}
		super.saveAll(fbg);
	}
	
	public void removeAllFactorBetaGain(List<FactorBetaGain> fbg) {
		if (fbg == null || fbg.size() <= 0){
			return;
		}
		super.removeAll(fbg);	
	}
	
	
	public void test(Long securityID)
	{
		SecurityManager securityManager=(SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
		Security se;
		AssetClass ac;
		String symbol;
		se = securityManager.get(securityID);
		ac = se.getAssetClass();
		symbol = se.getSymbol();
		List<Factor> factorList=null;
		LTIFactorManager lfm=LTIFactorManager.getInstance("RAA");
		String[] index=null;
		String[] type=null;
		Factor factor;
		int interval=64;
		boolean isSigmaOne=false;
		boolean isWLSorOLS=true;
		List<FactorBetaGain> factorBetaGainList;
		try{
			factorList=lfm.getFactorsForAsset(ac.getID());
		}catch(Exception e){
			System.out.println("There is an exception here");
			System.out.println(ac.getID());
		}
		if(factorList!=null && factorList.size()!=0)
		{
			index = new String[factorList.size()];
			type = new String[factorList.size()];
			for(int k=0;k<factorList.size();++k)
			{
				factor = factorList.get(k);
				index[k] = factor.getName();
				type[k] = factor.getType();
			}
		}
		try {
			/*********************calculate betagain*******************************/
			factorBetaGainList=calculateBetaGainForFactor(symbol,interval,index,type,isSigmaOne,isWLSorOLS);
			if(factorBetaGainList!=null &&factorBetaGainList.size()>0)
			{
				for(int i=0;i<factorBetaGainList.size();++i)
				{
					System.out.println(factorBetaGainList.get(i).toString());
				}
			}
			else
				System.out.println("none");
		}catch(Exception e){
			
		}
	}
	
	public static void main(String[] args)
	{
		MutualFundManager mutualFundManager=(MutualFundManager) ContextHolder.getInstance().getApplicationContext().getBean("mutualFundManager");
//		AssetClassManager assetClassManager=(AssetClassManager) ContextHolder.getInstance().getApplicationContext().getBean("assetClassManager");
//		mutualFundManager.initialAssetClassMapAndSecurity();
//		Iterator<Long> iterator = mutualFundManager.getAssetClassSecurityList().keySet().iterator(); 
//		Long acID;
//		while(iterator.hasNext()){
//			acID=iterator.next();
//			System.out.println(assetClassManager.get(acID).getName());
//			System.out.println(acID);
//		}
		//mutualFundManager.test(8775L);
		try {
			mutualFundManager.calculateHistoricalBetaGainForAllFunds(2009);
			//mutualFundManager.calculateHistoricalBetaGainByAssetClassID(9L, 2004);
			//mutualFundManager.calculateHistoricalOneYearBetaGainByAssetClassID(65L);
			//mutualFundManager.calculateHistoricalThreeYearBetaGainByAssetClassID(65L);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public HashMap<Long, List<Long>> getAssetClassSecurityList() {
		return assetClassSecurityList;
	}
}
