package com.lti.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.lti.bean.BLAttributeBean;
import com.lti.bean.MPTBean;
import com.lti.bean.MPTProperty;
import com.lti.bean.SortTypeBean;
import com.lti.service.PortfolioManager;
import com.lti.service.SecurityManager;
import com.lti.service.StrategyManager;
import com.lti.service.bo.HoldingItem;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.PortfolioMPT;
import com.lti.service.bo.Security;
import com.lti.service.bo.SecurityMPT;
import com.lti.service.bo.Transaction;
import com.lti.service.bo.VariableFor401k;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.type.BLExtraAttr;
import com.lti.type.TimeUnit;

public class SecurityUtil {
	public static void translateSecurityToMPT(List<Security> securities, List<SecurityMPT> mpts){
		if(securities == null)
			return;
		if(mpts == null)
			mpts = new ArrayList<SecurityMPT>();
		for(int i = 0; i < securities.size(); i++){
			Security s = securities.get(i);
			SecurityMPT m = new SecurityMPT();
			m.setSecurityID(s.getID());
			m.setSecurityName(s.getName());
			m.setSymbol(s.getSymbol());
			m.setSecurityType(s.getSecurityType());
			m.setAssetClassID(s.getClassID());
			mpts.add(m);
		}
	}
	
/*
 * Date: 2008-9-8
 * Function: record the mpt result to the certain list
 * **/
	public static void recordMPTItem(List<SecurityMPT> source, List<SecurityMPT> securities, MPTProperty m){
		if(source == null || source.size() == 0){
			securities.removeAll(securities);
			return;
		}
		/*for(int i = 0; i < securities.size(); i++){
			SecurityMPT sm = securities.get(i);
			if(setSecurityMPTItem(source, sm, m) == false){
				securities.remove(i);
				i--;
				continue;
			}
			
		}*/
		int i = 0, j = 0;
		while(i < source.size() && j < securities.size()){
			SecurityMPT se = securities.get(j);
			SecurityMPT s = source.get(i);
			if(se.getSymbol().equals(s.getSymbol())){
				int ref = SortTypeBean.getMPT(m.getName());
				switch(ref){
				case SortTypeBean.ALPHA:{
					if (s.getAlpha() != null) {
						se.getMPTStatistics().add(FormatUtil.formatQuantity(s.getAlpha()));
					}
					else
						se.getMPTStatistics().add(" ");
					break;
				}
				case SortTypeBean.ANNULIZEDRETURN:{
					if (s.getAR() != null) {
						se.getMPTStatistics().add(FormatUtil.formatQuantity(s.getAR()));
					}
					else
						se.getMPTStatistics().add(" ");
					break;
				}
				case SortTypeBean.BETA:{
					if (s.getBeta() != null) {
						se.getMPTStatistics().add(FormatUtil.formatQuantity(s.getBeta()));
					}
					else
						se.getMPTStatistics().add(" ");
					break;
				}
				case SortTypeBean.DRAWDOWN:{
					if (s.getDrawDown() != null) {
						se.getMPTStatistics().add(FormatUtil.formatQuantity(s.getDrawDown()));
					}
					else
						se.getMPTStatistics().add(" ");
					break;
				}
				case SortTypeBean.RSQUARED:{
					
					if (s.getRSquared() != null) {
						se.getMPTStatistics().add(FormatUtil.formatQuantity(s.getRSquared()));
					}
					else
						se.getMPTStatistics().add(" ");
					break;
				}
				case SortTypeBean.SHARPE:{
					if (s.getSharpeRatio() != null) {
						se.getMPTStatistics().add(FormatUtil.formatQuantity(s.getSharpeRatio()));
					}
					else
						se.getMPTStatistics().add(" ");
					break;
				}
				case SortTypeBean.STANDARDDIVIATION:{
					if (s.getStandardDeviation() != null) {
						se.getMPTStatistics().add(FormatUtil.formatQuantity(s.getStandardDeviation()));
					}
					else
						se.getMPTStatistics().add(" ");
					break;
				}
				case SortTypeBean.TREYNOR:{
					if (s.getTreynorRatio() != null) {
						se.getMPTStatistics().add(FormatUtil.formatQuantity(s.getTreynorRatio()));
					}
					else
						se.getMPTStatistics().add(" ");
					break;
				}
				}
				i++; j++;
			}
			else{
				securities.remove(j);
			}
		}
		while(j <  securities.size()){
			securities.remove(j);
		}
	}
	
	public static List<SecurityMPT> translatePortToSecMPT(List<PortfolioMPT> portfolios, List<BLAttributeBean> extras){
		List<SecurityMPT> securities = new ArrayList<SecurityMPT>();
		if(portfolios == null || portfolios.size() == 0)
			return null;
		for(int i = 0; i < portfolios.size(); i++){
			PortfolioMPT p = portfolios.get(i);
			SecurityMPT s = new SecurityMPT();
			//s.setSecurityName("P_" + p.getPortfolioID());
			s.setSecurityName(p.getName());
			s.setSecurityType(Configuration.SECURITY_TYPE_PORTFOLIO);
			s.setSymbol("P_" + p.getPortfolioID());
			s.setSecurityID(p.getPortfolioID());
			s.setAlpha(p.getAlpha());
			s.setAR(p.getAR());
			s.setBeta(p.getBeta());
			s.setDrawDown(p.getDrawDown());
			s.setRSquared(p.getRSquared());
			s.setSharpeRatio(p.getSharpeRatio());
			s.setTreynorRatio(p.getTreynorRatio());
			s.setStandardDeviation(p.getStandardDeviation());
			s.setMPTStatistics(p.getMPTStatistics());
			s.setYear(p.getYear().longValue());
			s.setReturn(p.getAR());
			if(extras != null && extras.size() != 0){
				for(int j = 0; j < extras.size(); j++){
					s.getExtras().add("NA");
				}
			}
			securities.add(s);
		}
		return securities;
	}
	
	public static Boolean setSecurityMPTItem(List<SecurityMPT> security, SecurityMPT sm, MPTProperty m){
		if(security == null || security.size() ==0 )
			return false;
		for(int i = 0; i < security.size(); i++){
			SecurityMPT s = security.get(i);
			if(s.getSecurityName().equals(sm.getSecurityName())){
				int ref = SortTypeBean.getMPT(m.getName());
				switch(ref){
					case SortTypeBean.ALPHA:{
						sm.setAlpha(s.getAlpha());
						break;
					}
					case SortTypeBean.ANNULIZEDRETURN:{
						sm.setAR(s.getAR());
						break;
					}
					case SortTypeBean.BETA:{
						sm.setBeta(s.getBeta());
						break;
					}
					case SortTypeBean.DRAWDOWN:{
						sm.setDrawDown(s.getDrawDown());
						break;
					}
					case SortTypeBean.RETURN:{
						sm.setReturn(s.getReturn());
						break;
					}
					case SortTypeBean.RSQUARED:{
						sm.setRSquared(s.getRSquared());
						break;
					}
					case SortTypeBean.SHARPE:{
						sm.setSharpeRatio(s.getSharpeRatio());
						break;
					}
					case SortTypeBean.STANDARDDIVIATION:{
						sm.setStandardDeviation(s.getStandardDeviation());
						break;
					}
					case SortTypeBean.TREYNOR:{
						sm.setTreynorRatio(s.getTreynorRatio());
						break;
					}
				}
				return true;
			}
		}
		return false;
	}
	
	public static void chooseSecuritiesBySQL(List<SecurityMPT> securities, List<MPTProperty> mpts, DetachedCriteria detachedCriteria){
		SecurityManager securityManager =(SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
		List<Security> securityList = securityManager.getSecurities(detachedCriteria);
		SecurityUtil.translateSecurityToMPT(securityList, securities);
		if(mpts == null || mpts.size() == 0 || securities == null || securities.size() == 0)
			return;
		for(int i = 0; i < mpts.size(); i++){
			MPTProperty m = mpts.get(i);
			//security type
			List<String> symbols = SecurityUtil.getSecurityMPTSymbols(securities);
			detachedCriteria = DetachedCriteria.forClass(SecurityMPT.class);
			detachedCriteria.add(Restrictions.eq("Year", m.getYear()));
			detachedCriteria.add(Restrictions.in("Symbol", symbols));

			int ref = SortTypeBean.getMPT(m.getName());
			Double max = m.getMax();
			Double min = m.getMin();
			switch(ref){
				case SortTypeBean.ALPHA:{
					if(max != null){
						detachedCriteria.add(Restrictions.le("Alpha", max));
					}
					if(min != null){
						detachedCriteria.add(Restrictions.ge("Alpha", min));
					}
					break;
				}
				case SortTypeBean.ANNULIZEDRETURN:{
					if(max != null){
						detachedCriteria.add(Restrictions.le("AR", max));
					}
					if(min != null){
						detachedCriteria.add(Restrictions.ge("AR", min));
					}
					break;
				}
				case SortTypeBean.BETA:{
					if(max != null){
						detachedCriteria.add(Restrictions.le("Beta", max));
					}
					if(min != null){
						detachedCriteria.add(Restrictions.ge("Beta", min));
					}
					break;
				}
				case SortTypeBean.DRAWDOWN:{
					if(max != null){
						detachedCriteria.add(Restrictions.le("DrawDown", max));
					}
					if(min != null){
						detachedCriteria.add(Restrictions.ge("DrawDown", min));
					}
					break;
				}
				case SortTypeBean.RETURN:{
					if(max != null){
						detachedCriteria.add(Restrictions.le("Return", max));
					}
					if(min != null){
						detachedCriteria.add(Restrictions.ge("Return", min));
					}
					break;
				}
				case SortTypeBean.RSQUARED:{
					if(max != null){
						detachedCriteria.add(Restrictions.le("RSquared", max));
					}
					if(min != null){
						detachedCriteria.add(Restrictions.ge("RSquared", min));
					}
					break;
				}
				case SortTypeBean.SHARPE:{
					if(max != null){
						detachedCriteria.add(Restrictions.le("SharpeRatio", max));
					}
					if(min != null){
						detachedCriteria.add(Restrictions.ge("SharpeRatio", min));
					}
					break;
				}
				case SortTypeBean.STANDARDDIVIATION:{
					if(max != null){
						detachedCriteria.add(Restrictions.le("StandardDeviation", max));
					}
					if(min != null){
						detachedCriteria.add(Restrictions.ge("StandardDeviation", min));
					}
					break;
				}
				case SortTypeBean.TREYNOR:{
					if(max != null){
						detachedCriteria.add(Restrictions.le("TreynorRatio", max));
					}
					if(min != null){
						detachedCriteria.add(Restrictions.ge("TreynorRatio", min));
					}
					break;
				}
				
			}
			
			
			List<SecurityMPT> list = securityManager.getSecurityByMPT(detachedCriteria);
			Comparator<SecurityMPT> comparator = new SecurityMPTSymbolComp();
			Collections.sort(list, comparator);
			Collections.sort(securities, comparator);
			SecurityUtil.recordMPTItem(list, securities, m);
		}
	}
	
	public static List<String> getSecurityMPTNames(List<SecurityMPT> securities){
		List<String> names = new ArrayList<String>();
		if(securities == null || securities.size() == 0)
			return null;
		for(int i = 0; i < securities.size(); i++){
			names.add(securities.get(i).getSecurityName());
		}
		return names;
	}
	
	public static List<String> getSecurityMPTSymbols(List<SecurityMPT> securities){
		List<String> symbols = new ArrayList<String>();
		if(securities == null || securities.size() == 0)
			return null;
		for(int i = 0; i < securities.size(); i++){
			symbols.add(securities.get(i).getSymbol());
		}
		return symbols;
	}
	
	public static List<BLAttributeBean> getExtraAttrByType(List<BLAttributeBean> ExtraAttr, Integer type){
		if(ExtraAttr == null || ExtraAttr.size() == 0)
			return null;
		List<BLAttributeBean> AttrInType = new ArrayList<BLAttributeBean>();
		for(int i = 0; i < ExtraAttr.size(); i++){
			BLAttributeBean bl = ExtraAttr.get(i);
			if(bl.getSecurityType() == type){
				AttrInType.add(bl);
			}
		}
		return AttrInType;
	}
	
	public static List<BLAttributeBean> getExtraAttrInDB(List<BLAttributeBean> ExtraAttr, Integer type){
		if(ExtraAttr == null || ExtraAttr.size() == 0)
			return null;
		List<BLAttributeBean> AttrInType = new ArrayList<BLAttributeBean>();
		for(int i = 0; i < ExtraAttr.size(); i++){
			BLAttributeBean bl = ExtraAttr.get(i);
			if(bl.getSecurityType() == type && (bl.getIsFromDatabase() != null && bl.getIsFromDatabase() == true)){
				AttrInType.add(bl);
			}
		}
		return AttrInType;
	}
	
	public static List<BLAttributeBean> getExtraAttrNotInDB(List<BLAttributeBean> ExtraAttr){
		if(ExtraAttr == null || ExtraAttr.size() == 0)
			return null;
		List<BLAttributeBean> AttrInType = new ArrayList<BLAttributeBean>();
		for(int i = 0; i < ExtraAttr.size(); i++){
			BLAttributeBean bl = ExtraAttr.get(i);
			if(bl.getIsFromDatabase() == null || bl.getIsFromDatabase() == false){
				AttrInType.add(bl);
			}
		}
		return AttrInType;
	}
	
	public static List<String> getSecurityNames(List<Security> securities){
		List<String> names = new ArrayList<String>();
		if(securities == null || securities.size() == 0)
			return null;
		for(int i = 0; i < securities.size(); i++){
			Security s = securities.get(i);
			names.add(s.getName());
		}
		return names;
	}
	
	public static List<String> getSecuritySymbols(List<Security> securities){
		List<String> symbols = new ArrayList<String>();
		if(securities == null || securities.size() == 0)
			return null;
		for(int i = 0; i < securities.size(); i++){
			Security s = securities.get(i);
			symbols.add(s.getSymbol());
		}
		return symbols;
	}
	
	public static void setShowList(List<SecurityMPT> securities, List<MPTProperty> showList){
		if(showList == null || showList.size() == 0){
			return;
		}
		SecurityManager securityManager =(SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");

		List<String> names = getSecurityMPTNames(securities);
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(SecurityMPT.class);
		detachedCriteria.add(Restrictions.eq("Year", -1l));
		detachedCriteria.add(Restrictions.in("SecurityName", names));
		List<SecurityMPT> source = securityManager.getSecurityByMPT(detachedCriteria);
		Comparator<SecurityMPT> comparator = new SecurityMPTSymbolComp();
		Collections.sort(securities, comparator);
		Collections.sort(source, comparator);
		int i = 0, j = 0;
		while(i < source.size() && j < source.size()){
			SecurityMPT src = source.get(i);
			SecurityMPT se = securities.get(j);
			if(src.getSymbol().equals(se.getSymbol())){
				for(int k = 0; k < showList.size(); k++){
					MPTProperty m = showList.get(k);
					int ref = SortTypeBean.getMPT(m.getName());
					switch(ref){
						case SortTypeBean.ALPHA:{
							if (src.getAlpha() != null) {
								se.getMPTStatistics().add(FormatUtil.formatQuantity(src.getAlpha()));
							}
							else
								se.getMPTStatistics().add("NA");
							break;
						}
						case SortTypeBean.ANNULIZEDRETURN:{
							if (src.getAR() != null) {
								se.getMPTStatistics().add(FormatUtil.formatQuantity(src.getAR()));
							}
							else
								se.getMPTStatistics().add("NA");
							break;
						}
						case SortTypeBean.BETA:{
							if (src.getBeta() != null) {
								se.getMPTStatistics().add(FormatUtil.formatQuantity(src.getBeta()));
							}
							else
								se.getMPTStatistics().add("NA");
							break;
						}
						case SortTypeBean.DRAWDOWN:{
							if (src.getDrawDown() != null) {
								se.getMPTStatistics().add(FormatUtil.formatQuantity(src.getDrawDown()));
							}
							else
								se.getMPTStatistics().add("NA");
							break;
						}
						case SortTypeBean.RSQUARED:{
							if (src.getRSquared() != null) {
								se.getMPTStatistics().add(FormatUtil.formatQuantity(src.getRSquared()));
							}
							else
								se.getMPTStatistics().add("NA");
							break;
						}
						case SortTypeBean.SHARPE:{
							if (src.getSharpeRatio() != null) {
								se.getMPTStatistics().add(FormatUtil.formatQuantity(src.getSharpeRatio()));
							}
							else
								se.getMPTStatistics().add("NA");
							break;
						}
						case SortTypeBean.STANDARDDIVIATION:{
							if (src.getStandardDeviation() != null) {
								se.getMPTStatistics().add(FormatUtil.formatQuantity(src.getStandardDeviation()));
							}
							else
								se.getMPTStatistics().add("NA");
							break;
						}
						case SortTypeBean.TREYNOR:{
							if (src.getTreynorRatio() != null) {
								se.getMPTStatistics().add(FormatUtil.formatQuantity(src.getTreynorRatio()));
							}
							else
								se.getMPTStatistics().add("NA");
							break;
						}
						case SortTypeBean.RETURN:{
							if (src.getReturn() != null) {
								se.getMPTStatistics().add(FormatUtil.formatQuantity(src.getReturn()));
							}
							else
								se.getMPTStatistics().add("NA");
							break;
						}
					}//end switch
				}//end set mpts
				i++; j++;
			}
			else
			{
				for(int k = 0; k < showList.size(); k++){
					se.getMPTStatistics().add("NA");
				}
				j++;
			}
		}
		while(j < securities.size()){
			SecurityMPT se = securities.get(j);
			for(int k = 0; k < showList.size(); k++){
				se.getMPTStatistics().add("NA");
			}
			j++;
		}
	}
	
	public static void basicSearch(List<SecurityMPT> securityList, List<MPTProperty> mpts, List<BLAttributeBean> ExtraAttr, List<Integer> type, DetachedCriteria detachedCriteria){
		chooseSecuritiesBySQL(securityList, mpts, detachedCriteria);
		List<Security> securities;
		if(ExtraAttr != null && ExtraAttr.size() != 0){
			securities = SecurityUtil.getSecuritiesWithExtraAttr(securityList, ExtraAttr, type);
			mergeSecurities(securities, securityList, ExtraAttr);
		}
	}
	
	public static List<Security> getSecuritiesWithExtraAttr(List<SecurityMPT> securityList, List<BLAttributeBean> ExtraAttr, List<Integer> type){
		//SecurityManager securityManager =(SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Security.class);
		if(type == null || type.size() == 0){
			return null;
		}
		List<String> names = SecurityUtil.getSecurityMPTNames(securityList);
		if(names == null)
			return null;
		detachedCriteria.add(Restrictions.in("Name", names));
		List<Security> securities = new ArrayList<Security>();
		for(int i = 0; i < type.size(); i++){
			Integer t = type.get(i);
			List<BLAttributeBean> extraAttrInDB = getExtraAttrInDB(ExtraAttr, t);
			List<Security> securityOfType = getSecuritiesFromDB(detachedCriteria, extraAttrInDB, t);
			if(securityOfType != null)
				securities.addAll(securityOfType);
		}
		System.out.println(securities.size());
		List<BLAttributeBean> extraAttrNotInDB = getExtraAttrNotInDB(ExtraAttr);
		getSecuritiesByAPI(securities, extraAttrNotInDB);
		System.out.println(securities.size());
		return securities;
	}
	
	public static void mergeSecurities(List<Security> securities, List<SecurityMPT> securityList, List<BLAttributeBean> extraAtrrs){
		if(securityList == null || securityList.size() == 0 )
			return;
		/*for(int i = 0; i < securityList.size(); i++){
			SecurityMPT s_mpt = securityList.get(i);
			if(isInSecuirties(s_mpt, securities) == false){
				securityList.remove(i);
				i--;
				continue;
			}
			
		}*/
		if(securities == null || securities.size() == 0){
			securityList.removeAll(securityList);
			return;
		}
		Comparator<Security> SecurityComparator = new SecuritySymbolComp();
		Collections.sort(securities, SecurityComparator);
		Comparator<SecurityMPT> SeMPTComparator = new SecurityMPTSymbolComp();
		Collections.sort(securityList, SeMPTComparator);
		int i = 0, j = 0;
		while(i < securityList.size() && j < securities.size()){
			SecurityMPT seMPT = securityList.get(i);
			Security se = securities.get(j);
			if(seMPT.getSymbol().equals(se.getSymbol())){
				ArrayList<String> extras = new ArrayList<String>();
				for(int k = 0; k < extraAtrrs.size(); k++){
					BLAttributeBean attr = extraAtrrs.get(k);
					if(attr.getIsFromDatabase() == false){
						if(se.getMPTs() != null && se.getMPTs().get(attr.getXMLName()) != null)
							extras.add(FormatUtil.formatQuantity(se.getMPTs().get(attr.getXMLName())));
						else
							extras.add(" ");
					}
					//the extra attributes from database we need to set in separately
					else
					{
						extras.add(" ");
					}
				}
				seMPT.setExtras(extras);
				i++; j++;
			}
			else
				securityList.remove(i);
		}
	}
	
	public static Boolean isInSecuirties(SecurityMPT s_mpt, List<Security> securities){
		if(securities == null || securities.size() == 0)
			return false;
		for(int i = 0; i < securities.size(); i++){
			Security s = securities.get(i);
			if(s.getName().equals(s_mpt.getSecurityName())){
				if(s.getMPTs() != null){
					Hashtable<String, Double> mpts = s.getMPTs();
					Iterator<String> iter = mpts.keySet().iterator();
					Map<String, String> extraAttrs = new TreeMap<String, String>();
					while(iter.hasNext()){
						String key = (String) iter.next();
						String value = FormatUtil.formatQuantity(mpts.get(key));
						extraAttrs.put(key, value);
						if(key.equals(BLAttributeBean.XML_DISCOUNTRATE)){
							Double discountRate = (Double) mpts.get(key);
							s_mpt.setDiscountRate(discountRate);
						}
					}
					s_mpt.setExtraAttrs(extraAttrs);
				}
				return true;
			}
		}
		return false;
	}
	
	public static List<Security> getSecuritiesFromDB(DetachedCriteria detachedCriteria, List<BLAttributeBean> AttrInDB, Integer Type){
		SecurityManager securityManager =(SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
		List<Security> securitiesOfType = new ArrayList<Security>();
		detachedCriteria.add(Restrictions.eq("SecurityType", Type));
		if(AttrInDB == null){	
			securitiesOfType = securityManager.getSecurities(detachedCriteria);
			return securitiesOfType;
		}
		for(int i = 0; i < AttrInDB.size(); i++){
			BLAttributeBean bl = AttrInDB.get(i);
			if(bl.getIsSingleValue() != null && bl.getIsSingleValue() == true){
				int DataType = bl.getDataType();
				switch(DataType){
					case BLAttributeBean.DataType_I:{
						detachedCriteria.add(Restrictions.eq(bl.getDataBaseName(), bl.getValueForInt()));
						break;
					}
					case BLAttributeBean.DataType_L:{
						detachedCriteria.add(Restrictions.eq(bl.getDataBaseName(), bl.getValueForLong()));
						break;
					}
					case BLAttributeBean.DataType_D:{
						detachedCriteria.add(Restrictions.eq(bl.getDataBaseName(), bl.getValueForDouble()));
						break;
					}
					case BLAttributeBean.DataType_S:{
						detachedCriteria.add(Restrictions.eq(bl.getDataBaseName(), bl.getValueForStr()));
						break;
					}
				}
			}
			else
			{
				int DataType = bl.getDataType();
				
				switch(DataType){
					case BLAttributeBean.DataType_I:{
						Integer max = (Integer) bl.getMaxValue();
						Integer min = (Integer) bl.getMinValue();
						if(max != null)
							detachedCriteria.add(Restrictions.le(bl.getDataBaseName(),max));
						if(min != null)
							detachedCriteria.add(Restrictions.ge(bl.getDataBaseName(),min));
						break;
					}
					case BLAttributeBean.DataType_L:{
						Long max = (Long) bl.getMaxValue();
						Long min = (Long) bl.getMinValue();
						if(max != null)
							detachedCriteria.add(Restrictions.le(bl.getDataBaseName(),max));
						if(min != null)
							detachedCriteria.add(Restrictions.ge(bl.getDataBaseName(),min));
						break;
					}
					case BLAttributeBean.DataType_D:{
						Double max = (Double) bl.getMaxValue();
						Double min = (Double) bl.getMinValue();
						if(max != null)
							detachedCriteria.add(Restrictions.le(bl.getDataBaseName(),max));
						if(min != null)
							detachedCriteria.add(Restrictions.ge(bl.getDataBaseName(),min));
						break;
					}
				}
			}
		}
		securitiesOfType = securityManager.getSecurities(detachedCriteria);
		return securitiesOfType;
	}
	
	public static void getSecuritiesByAPI(List<Security> securities, List<BLAttributeBean> Attrs){
		if(securities == null || securities.size() ==0)
			return;
		for(int i = 0; i < securities.size(); i++){
			Security s = securities.get(i);
			if(ChooseSecurities(s, Attrs) == false){
				securities.remove(i);
				i--;
				continue;
			}
		}
	}
	
	public static Boolean ChooseSecurities(Security s, List<BLAttributeBean> Attrs){
		for(int i = 0; i < Attrs.size(); i++){
			BLAttributeBean bl = Attrs.get(i);
			String AttrName = bl.getAttributeName();
			if(!s.getSecurityType().equals(bl.getSecurityType()))
				break;
			//check which attribute
			if(AttrName.equals(BLAttributeBean.DISCOUNTRATE)){
				Date startingDate;
				Date endDate = new Date();
				if(bl.getPeriod() != null){
					startingDate = LTIDate.add(endDate, -1 * bl.getPeriod() * 30);
				}
				else	//set the starting date is 24 months ago by default
				{
					startingDate = LTIDate.add(endDate, (-2) * 365);
				}
				Double max = (Double) bl.getMaxValue();
				Double min = (Double) bl.getMinValue();
				Double discountRate = s.getAverageDiscountRate(startingDate, endDate, TimeUnit.DAILY);
				
				if(discountRate == null)
					return false;
				if(max != null && discountRate > max)
					return false;
				if(min != null && discountRate < min)
					return false;
				Hashtable<String, Double> mpts = new Hashtable<String, Double>();
				mpts.put(BLAttributeBean.XML_DISCOUNTRATE, discountRate);
				s.setMPTs(mpts);
			}
		}
		return true;
	}
	
	public static List<SecurityMPT> selectSecurityMPTs(List<MPTProperty> mpts, List<Integer> types, List<BLAttributeBean> extras, DetachedCriteria detachedCriteria, int size){
		Map<Long, List<MPTProperty>> yearMap = categoryByYear(mpts);
		SecurityManager securityManager =(SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
		if(types != null && types.size() > 0 ){
			detachedCriteria.add(Restrictions.in("SecurityType", types));
		}
		List<Security> securityList = securityManager.getSecurities(detachedCriteria);
		List<SecurityMPT> securityMPTList = new ArrayList<SecurityMPT>();
		translateSecurityToMPT(securityList, securityMPTList);
		if(yearMap == null || yearMap.size() < 1){		
			return securityMPTList;
		}
		Iterator<Long> years = yearMap.keySet().iterator();
		while (years.hasNext()) {
			Long year = (Long) years.next();
			List<MPTProperty> mptList = yearMap.get(year);
			detachedCriteria = DetachedCriteria.forClass(SecurityMPT.class);
			detachedCriteria.add(Restrictions.eq("Year", year));
			List<String> names = getSecurityMPTNames(securityMPTList);
			if(names != null && names.size() > 0)
				detachedCriteria.add(Restrictions.in("SecurityName", names));
			Boolean select = false;
			Boolean sorted = false;
			for(int i = 0; i < mptList.size(); i++){
				MPTProperty m = mptList.get(i);
				int ref = SortTypeBean.getMPT(m.getName());
				Double max = m.getMax();
				Double min = m.getMin();
				String sort = m.getSort();
				switch(ref){
				case SortTypeBean.ALPHA:{
					if(max != null){
						detachedCriteria.add(Restrictions.le("Alpha", max));
						select = true;
					}
					if(min != null){
						detachedCriteria.add(Restrictions.ge("Alpha", min));
						select = true;
					}
					if(sort != null && sort.equalsIgnoreCase("asc")){
						detachedCriteria.addOrder(Order.asc("Alpha"));
						sorted = true;
					}
					else if(sort != null && sort.equalsIgnoreCase("desc"))
					{
						detachedCriteria.addOrder(Order.desc("Alpha"));
						sorted = true;
					}
					break;
				}
				case SortTypeBean.ANNULIZEDRETURN:{
					if(max != null){
						detachedCriteria.add(Restrictions.le("AR", max));
						select = true;
					}
					if(min != null){
						detachedCriteria.add(Restrictions.ge("AR", min));
						select = true;
					}
					if(sort != null && sort.equalsIgnoreCase("asc")){
						detachedCriteria.addOrder(Order.asc("AR"));
						sorted = true;
					}
					else if(sort != null && sort.equalsIgnoreCase("desc"))
					{
						detachedCriteria.addOrder(Order.desc("AR"));
						sorted = true;
					}
					break;
				}
				case SortTypeBean.BETA:{
					if(max != null){
						detachedCriteria.add(Restrictions.le("Beta", max));
						select = true;
					}
					if(min != null){
						detachedCriteria.add(Restrictions.ge("Beta", min));
						select = true;
					}
					if(sort != null && sort.equalsIgnoreCase("asc")){
						detachedCriteria.addOrder(Order.asc("Beta"));
						sorted = true;
					}
					else if(sort != null && sort.equalsIgnoreCase("desc"))
					{
						detachedCriteria.addOrder(Order.desc("Beta"));
						sorted = true;
					}
					break;
				}
				case SortTypeBean.DRAWDOWN:{
					if(max != null){
						detachedCriteria.add(Restrictions.le("DrawDown", max));
						select = true;
					}
					if(min != null){
						detachedCriteria.add(Restrictions.ge("DrawDown", min));
						select = true;
					}
					if(sort != null && sort.equalsIgnoreCase("asc")){
						detachedCriteria.addOrder(Order.asc("DrawDown"));
						sorted = true;
					}
					else if(sort != null && sort.equalsIgnoreCase("desc"))
					{
						detachedCriteria.addOrder(Order.desc("DrawDown"));
						sorted = true;
					}
					break;
				}
				case SortTypeBean.RETURN:{
					if(max != null){
						detachedCriteria.add(Restrictions.le("Return", max));
						select = true;
					}
					if(min != null){
						detachedCriteria.add(Restrictions.ge("Return", min));
						select = true;
					}
					if(sort != null && sort.equalsIgnoreCase("asc")){
						detachedCriteria.addOrder(Order.asc("Return"));
						sorted = true;
					}
					else if(sort != null && sort.equalsIgnoreCase("desc"))
					{
						detachedCriteria.addOrder(Order.desc("Return"));
						sorted = true;
					}
					break;
				}
				case SortTypeBean.RSQUARED:{
					if(max != null){
						detachedCriteria.add(Restrictions.le("RSquared", max));
						select = true;
					}
					if(min != null){
						detachedCriteria.add(Restrictions.ge("RSquared", min));
						select = true;
					}
					if(sort != null && sort.equalsIgnoreCase("asc")){
						detachedCriteria.addOrder(Order.asc("RSquared"));
						sorted = true;
					}
					else if(sort != null && sort.equalsIgnoreCase("desc"))
					{
						detachedCriteria.addOrder(Order.desc("RSquared"));
						sorted = true;
					}
					break;
				}
				case SortTypeBean.SHARPE:{
					if(max != null){
						detachedCriteria.add(Restrictions.le("SharpeRatio", max));
						select = true;
					}
					if(min != null){
						detachedCriteria.add(Restrictions.ge("SharpeRatio", min));
						select = true;
					}
					if(sort != null && sort.equalsIgnoreCase("asc")){
						detachedCriteria.addOrder(Order.asc("SharpeRatio"));
						sorted = true;
					}
					else if(sort != null && sort.equalsIgnoreCase("desc"))
					{
						detachedCriteria.addOrder(Order.desc("SharpeRatio"));
						sorted = true;
					}
					break;
				}
				case SortTypeBean.STANDARDDIVIATION:{
					if(max != null){
						detachedCriteria.add(Restrictions.le("StandardDeviation", max));
						select = true;
					}
					if(min != null){
						detachedCriteria.add(Restrictions.ge("StandardDeviation", min));
						select = true;
					}
					if(sort != null && sort.equalsIgnoreCase("asc")){
						detachedCriteria.addOrder(Order.asc("StandardDeviation"));
						sorted = true;
					}
					else if(sort != null && sort.equalsIgnoreCase("desc"))
					{
						detachedCriteria.addOrder(Order.desc("StandardDeviation"));
						sorted = true;
					}
					break;
				}
				case SortTypeBean.TREYNOR:{
					if(max != null){
						detachedCriteria.add(Restrictions.le("TreynorRatio", max));
						select = true;
					}
					if(min != null){
						detachedCriteria.add(Restrictions.ge("TreynorRatio", min));
						select = true;
					}
					if(sort != null && sort.equalsIgnoreCase("asc")){
						detachedCriteria.addOrder(Order.asc("TreynorRatio"));
						sorted = true;
					}
					else if(sort != null && sort.equalsIgnoreCase("desc"))
					{
						detachedCriteria.addOrder(Order.desc("TreynorRatio"));
						sorted = true;
					}
					break;
				}
				
			}
			}
			List<SecurityMPT> list = securityManager.getSecurityByMPT(detachedCriteria);
			if(sorted == true)
				if(list != null && size>0 && list.size() > size){
					list = list.subList(0, size);
					select = true;
				}
			SecurityUtil.adjustMPTList(list, securityMPTList, mptList, select);
		}
		return securityMPTList;
	}
	
	
	/**
	 * @author cherry 2009-6-9
	 * @param source	new security list
	 * @param securities old security list
	 * @param mpts
	 * @param select when the source is selected then we must remove the securites that doesn't fit or else we need to record the content as null
	 */
	public static void adjustMPTList(List<SecurityMPT> source, List<SecurityMPT> securities, List<MPTProperty> mpts, Boolean select){
		if(source == null || source.size() == 0){
			securities.removeAll(securities);
			return;
		}
		Comparator<SecurityMPT> comparator = new SecurityMPTSymbolComp();
		Collections.sort(securities, comparator);
		Collections.sort(source, comparator); 
		int i = 0, j = 0;
		while(i < source.size() && j < securities.size()){
			SecurityMPT se = securities.get(j);
			SecurityMPT s = source.get(i);
			if(se.getSymbol().equals(s.getSymbol())){
				if(mpts == null || mpts.size() == 0)
					continue;
				int k = 0;
				while (k < mpts.size()) {
					MPTProperty m = mpts.get(k);
					int ref = SortTypeBean.getMPT(m.getName());
					switch (ref) {
					case SortTypeBean.ALPHA: {
						if (s.getAlpha() != null) {
							se.getMPTStatistics().add(FormatUtil.formatPercentage(s.getAlpha()));
						} else
							se.getMPTStatistics().add("NA");
						break;
					}
					case SortTypeBean.ANNULIZEDRETURN: {
						if (s.getAR() != null) {
							se.getMPTStatistics().add(FormatUtil.formatPercentage(s.getAR()));
						} else
							se.getMPTStatistics().add("NA");
						break;
					}
					case SortTypeBean.BETA: {
						if (s.getBeta() != null) {
							se.getMPTStatistics().add(FormatUtil.formatPercentage(s.getBeta()));
						} else
							se.getMPTStatistics().add("NA");
						break;
					}
					case SortTypeBean.DRAWDOWN: {
						if (s.getDrawDown() != null) {
							se.getMPTStatistics().add(FormatUtil.formatPercentage(s.getDrawDown()));
						} else
							se.getMPTStatistics().add("NA");
						break;
					}
					case SortTypeBean.RSQUARED: {

						if (s.getRSquared() != null) {
							se.getMPTStatistics().add(FormatUtil.formatPercentage(s.getRSquared()));
						} else
							se.getMPTStatistics().add("NA");
						break;
					}
					case SortTypeBean.SHARPE: {
						if (s.getSharpeRatio() != null) {
							se.getMPTStatistics().add(FormatUtil.formatPercentage(s.getSharpeRatio()));
						} else
							se.getMPTStatistics().add("NA");
						break;
					}
					case SortTypeBean.STANDARDDIVIATION: {
						if (s.getStandardDeviation() != null) {
							se.getMPTStatistics().add(FormatUtil.formatPercentage(s.getStandardDeviation()));
						} else
							se.getMPTStatistics().add("NA");
						break;
					}
					case SortTypeBean.TREYNOR: {
						if (s.getTreynorRatio() != null) {
							se.getMPTStatistics().add(FormatUtil.formatPercentage(s.getTreynorRatio()));
						} else
							se.getMPTStatistics().add("NA");
						break;
					}
					}
					k++;
				}
				i++;j++;
			}
			else{
				if(s.getSymbol().compareTo(se.getSymbol()) > 0){
					if(select == true)
						securities.remove(j);
					else
					{
						for(int m = 0; m < mpts.size(); m++){
							se.getMPTStatistics().add("NA");
						}
						j++;
					}
				}
				else
					i++;
			}
		}
		if(select == false){
			while(j <  securities.size()){
				SecurityMPT se = securities.get(j);
				for(int m = 0; m < mpts.size(); m++){
					se.getMPTStatistics().add("NA");
				}
				j++;
			}
		}
		else
		{
			while(j <  securities.size()){
				securities.remove(j);
			}
		}
	}
	
	public static Map<Long, List<MPTProperty>> categoryByYear(List<MPTProperty> mpts){
		if(mpts == null || mpts.size() < 1)
			return null;
		Comparator<MPTProperty> comparator = new MPTPropertyYearComp();
		Collections.sort(mpts, comparator);
		Map<Long, List<MPTProperty>> yearMap = new TreeMap<Long, List<MPTProperty>>();
		//List<MPTProperty> mptList = new ArrayList<MPTProperty>();
		for(int i = 0; i < mpts.size(); i++){
			MPTProperty m = mpts.get(i);
			Long year = m.getYear();
			if(yearMap.containsKey(year)){
				List<MPTProperty> mptList = yearMap.get(year);
				mptList.add(m);
			}
			else
			{
				List<MPTProperty> mptList = new ArrayList<MPTProperty>();
				mptList.add(m);
				yearMap.put(year, mptList);
			}
		}
		return yearMap;
	}
	
	/**
	 * get securities' extra mpts such as discount rate
	 * @author cherry 2009-6-12
	 * @param securityMPTList
	 * @param extras
	 * @param size
	 * @return
	 */
	public static List<SecurityMPT> selectMPTsWithEA(List<SecurityMPT> securityMPTList, List<BLAttributeBean> attrs, int size){
		if(attrs == null || attrs.size() < 1){
			return securityMPTList;
		}
		if(securityMPTList == null || securityMPTList.size() == 0)
			return null;
		SecurityManager securityManager =(SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
		List<String> symbols = getSecurityMPTSymbols(securityMPTList);
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Security.class);
		detachedCriteria.add(Restrictions.in("Symbol", symbols));
		List<Security> securities = securityManager.getSecurities(detachedCriteria);
		if(securities == null)
			return securityMPTList;
		Comparator<Security> securityComparator = new SecuritySymbolComp();
		Comparator<SecurityMPT> securityMPTComparator = new SecurityMPTSymbolComp();
		Collections.sort(securities, securityComparator);
		Collections.sort(securityMPTList, securityMPTComparator);
		
		for(int i = 0; i < attrs.size(); i++){
			BLAttributeBean attr = attrs.get(i);
			securityMPTList = calExtraAttribute(securities, securityMPTList, attr, size);
		}
		return securityMPTList;
	}
	
	/**
	 * @author cherry 2009-6-25
	 * set the discountRate of the security which is in the securityMPTList
	 * @param securities
	 * @param securityMPTList
	 * @param attr
	 * @param size
	 * @return
	 */
	public static List<SecurityMPT> calExtraAttribute(List<Security> securities, List<SecurityMPT> securityMPTList, BLAttributeBean attr, int size){
		if(securityMPTList == null || securities == null)
			return securityMPTList;
		int j = 0, k = 0;
		while(j < securities.size() && k < securityMPTList.size()){
			Security se = securities.get(j);
			SecurityMPT s = securityMPTList.get(k);
			Double value;
			if(se.getSymbol().compareTo(s.getSymbol()) < 0){
				j++;
				continue;
			}
			else if(se.getSymbol().compareTo(s.getSymbol()) > 0){
				value = null;
				k++;
			}
			else
			{
				if(se.getSecurityType().longValue() == Configuration.SECURITY_TYPE_CLOSED_END_FUND){
					try {
						Date startDate;
						startDate = (attr.getStartDate() == null)?LTIDate.getLastYear(attr.getEndDate()):attr.getStartDate();
						if(attr.getXMLName().equalsIgnoreCase(BLAttributeBean.XML_DISCOUNTRATE)){
							value = se.getAverageDiscountRate(startDate, attr.getEndDate(), TimeUnit.DAILY);
						}else if(attr.getXMLName().equalsIgnoreCase(BLAttributeBean.XML_NAV_ALPHA)){
							value = se.getAlpha(startDate, attr.getEndDate(), TimeUnit.DAILY, true);
						}else if(attr.getXMLName().equalsIgnoreCase(BLAttributeBean.XML_NAV_AR)){
							value = se.getAnnualizedReturn(startDate, attr.getEndDate(), true);
						}else if(attr.getXMLName().equalsIgnoreCase(BLAttributeBean.XML_NAV_BETA)){
							value = se.getBeta(startDate, attr.getEndDate(), TimeUnit.DAILY, true);
						}else if(attr.getXMLName().equalsIgnoreCase(BLAttributeBean.XML_NAV_DRAWDOWN)){
							value = se.getDrawDown(startDate, attr.getEndDate(), TimeUnit.DAILY, true);
						}else if(attr.getXMLName().equalsIgnoreCase(BLAttributeBean.XML_NAV_RETURN)){
							value = se.getAnnualizedReturn(startDate, attr.getEndDate(), true);
						}else if(attr.getXMLName().equalsIgnoreCase(BLAttributeBean.XML_NAV_RSQUARED)){
							value = se.getRSquared(startDate, attr.getEndDate(), TimeUnit.DAILY, true);
						}else if(attr.getXMLName().equalsIgnoreCase(BLAttributeBean.XML_NAV_SHARPE)){
							value = se.getSharpeRatio(startDate, attr.getEndDate(), TimeUnit.DAILY, true);
						}else if(attr.getXMLName().equalsIgnoreCase(BLAttributeBean.XML_NAV_STANDARDDIVIATION)){
							value = se.getStandardDeviation(startDate, attr.getEndDate(), TimeUnit.DAILY, true);
						}else if(attr.getXMLName().equalsIgnoreCase(BLAttributeBean.XML_NAV_TREYNOR)){
							value = se.getTreynorRatio(startDate, attr.getEndDate(), TimeUnit.DAILY, true);
						}
						else{
							value = null;
						}
					} catch (Exception e) {
						// TODO: handle exception
						value = null;
					}
				}
				else
				{
					value = null;
				}
				k++;j++;
			}
			if(value != null && attr.getMinValue() != null && value.doubleValue() < (Double)attr.getMinValue())
				continue;
			if(value != null && attr.getMaxValue() != null && value.doubleValue() > (Double)attr.getMaxValue())
				continue;
			if(s.getExtras() != null){
				s.getExtras().add(FormatUtil.formatPercentage(value));
			}
			else
			{
				List<String> values = new ArrayList<String>();
				values.add(FormatUtil.formatPercentage(value));
				s.setExtras(values);
			}
		}
		if(attr.getSort() != null && size > 0 && securityMPTList.size() > size){
			if(attr.getSort().equalsIgnoreCase("asc")){
				securityMPTList = securityMPTList.subList(0, size);
			}
			else
			{
				securityMPTList = securityMPTList.subList(securityMPTList.size() - size + 1, securityMPTList.size());
			}
		}
		return securityMPTList;
	}
	/**
	 * in the 401k transactions, replace the symbols for their descriptions
	 * 返回是否为plan下的portfolio
	 * @param transactions
	 * @author SuPing 2010/02/01
	 */
	public static boolean usedescription(Portfolio p,List<Transaction> trs,boolean sd) {
		if(trs==null)return false;
		boolean st=false;
		StrategyManager strategyManager = ContextHolder.getStrategyManager();
		SecurityManager securityManager = ContextHolder.getSecurityManager();
		
		Long planID = null;
		try {
			planID = Long.parseLong(p.getStrategies().getAssetAllocationStrategy().getParameter().get("PlanID"));
		} catch (Exception e) {
		}
		
		for (int i = 0; i < trs.size(); i++) {
			Transaction t = trs.get(i);
			if(sd){
				t.setDate(LTIDate.getNewNYSETradingDay(t.getDate(), 1));
			}
			// show the SecurityName
			if (t.getSecurityName() == null || t.getSecurityName().trim().equals("")) {
				if (t.getSecurityID() != null) {
					Security security = securityManager.get(t.getSecurityID());
					if(security!=null){
						t.setSecurityName(security.getName());
						if (t.getSymbol() == null)
							t.setSymbol(security.getSymbol());
					}
				} else
					t.setSecurityName(t.getSymbol());
			}

			if (planID != null) {
				st = true;
				VariableFor401k vk = strategyManager.getVariable401K(planID, t.getSymbol());
				if (vk != null && vk.getDescription() != null && !vk.getDescription().equals(""))
					t.setDescription401k(vk.getDescription());
				else {
					t.setDescription401k(t.getSecurityName());
				}

			} else {
				st = false;

			}
		}
		return st;
	}
	
	public static boolean usedescription(Portfolio p,List<HoldingItem> his){
		boolean st=false;
		Long planID=null;
		try {
			planID=Long.parseLong(p.getStrategies().getAssetAllocationStrategy().getParameter().get("PlanID"));
			st=true;
		} catch (Exception e) {
		}
		if(his!=null&&his.size()>0){
			StrategyManager strategyManager = ContextHolder.getStrategyManager();
			SecurityManager securityManager = ContextHolder.getSecurityManager();
			for(HoldingItem hi:his){
				
				hi.setDescription(securityManager.get(hi.getSymbol()).getName());
				
				if(planID!=null){
					VariableFor401k vk=strategyManager.getVariable401K(planID, hi.getSymbol());
					if(vk!=null&&vk.getDescription()!=null&&!vk.getDescription().equals(""))hi.setFundDescription(vk.getDescription());
				}
				
				if(hi.getFundDescription()==null||hi.getFundDescription().equals("")) {
					hi.setFundDescription(hi.getDescription());
				}
			}
		}
		return st;
	}
}
