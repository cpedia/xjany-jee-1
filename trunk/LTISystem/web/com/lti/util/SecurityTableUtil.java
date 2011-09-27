package com.lti.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.lti.action.fundcenter.SecurityTableAction;
import com.lti.bean.BLAttributeBean;
import com.lti.bean.MPTProperty;
import com.lti.bean.SortTypeBean;
import com.lti.type.TimeUnit;

public class SecurityTableUtil {
	public static List<MPTProperty> processMPTs(String chosenMPT, String sort){
		if(chosenMPT == null || chosenMPT.equals("")){
			return null;
		}
		String[] tmpMPTPairs = chosenMPT.split(",");
		List<MPTProperty> mpts = new ArrayList<MPTProperty>();
		for(int i = 0; i < tmpMPTPairs.length; i++){
			String tmpMPT = tmpMPTPairs[i];
			if(tmpMPT.contains(">") && tmpMPT.contains("<"))	//format is not right
				continue;
			String[] mptSet;
			if(tmpMPT.contains("<")){
				MPTProperty property = getProperty(tmpMPT, 0);
				if(property != null)
					mpts.add(property);
			}
			else if(tmpMPT.contains(">")){
				MPTProperty property = getProperty(tmpMPT, 1);
				if(property != null)
					mpts.add(property);
			}
			else	//no max or min value
			{
				String[] pair = tmpMPT.split("-");
				MPTProperty property = new MPTProperty();
				property.setChoosed(true);
				if(setPropertyYearName(property, pair) == true) 
					mpts.add(property);
			}
		}
		if(sort != null && !sort.equals("")){
			String[] tmpSortPairs = sort.split(",");
			for(int i = 0; i < tmpSortPairs.length; i++){
				String tmpSort = tmpSortPairs[i];
				String order = "desc";
				String orderMPT = tmpSort;
				if(tmpSort.contains("#")){
					String[] orderpairs = tmpSort.split("#");
					if(orderpairs.length == 2 && (orderpairs[1].equals("asc")))
						order = "asc";
					orderMPT = orderpairs[0];
				}
				String[] pairs = orderMPT.split("-");
				Long year = -1l;
				String name;
				if(pairs.length == 2){
					if(StringUtil.checkNumber(pairs[0]) == true){
						year = Long.parseLong(pairs[0]);
						if(year < 1000){
							year = -1 * year;
						}
					}
					name = pairs[1];
				}
				else
				{
					name = pairs[0];
				}
				if(SortTypeBean.getMPT(name) > -1 && mpts != null){
					MPTProperty m = getMPTProperty(name, year, mpts);
					if(m != null)
						m.setSort(order);
				}
			}
		}
		
		return mpts;
	}
	
	public static MPTProperty getMPTProperty(String name, Long year, List<MPTProperty> mpts){
		if(mpts == null || mpts.size() < 1)
			return null;
		for(int i = 0; i < mpts.size(); i++){
			MPTProperty m = mpts.get(i);
			if(m.getYear() == year && m.getName().equalsIgnoreCase(name)){
				return m;
			}
		}
		return null;
	}
	
	public static Boolean setPropertyYearName(MPTProperty property, String[] pair){
		Long year;
		String mptName;
		if(pair == null || pair.length == 0)
			return false;
		if(pair.length < 2 && SortTypeBean.getMPT(pair[0]) > -1){
			year = -1l;		//set 1 year as default
			mptName = pair[0];
		}
		else if(pair.length == 2 && SortTypeBean.getMPT(pair[1]) > -1){
			year = Long.parseLong(pair[0]);
			if(year < 1000){
				year = -1 * year;
			}
			mptName = pair[1];
		}
		else
			return false;
		property.setYear(year);
		property.setName(mptName);
		return true;
	}
	
	public static MPTProperty getProperty(String tmpMPT, int o){	//if o is 1 means ">" else if o is 0 means "<"
		String[] mptSet;
		if(o == 1)
			mptSet = tmpMPT.split(">");
		else
			mptSet = tmpMPT.split("<");
		Double maxValue = null;
		Double minValue = null;
		if(mptSet.length == 2){
			String[] pair;
			if(StringUtil.checkNumber(mptSet[0])==true){	//with max value
				if(o == 1)
					maxValue = Double.parseDouble(mptSet[0]);
				else
					minValue = Double.parseDouble(mptSet[0]);
				pair = mptSet[1].split("-");
			}
			else if(StringUtil.checkNumber(mptSet[1])==true){
				if(o == 1)
					minValue = Double.parseDouble(mptSet[1]);
				else
					maxValue = Double.parseDouble(mptSet[1]);
				pair = mptSet[0].split("-");
			}
			else
				return null;
			MPTProperty property = new MPTProperty();
			property.setChoosed(true);
			property.setMax(maxValue);
			property.setMin(minValue);
			if(setPropertyYearName(property, pair) == true) 
				return property;
		}
		if(mptSet.length == 3){
			String[] pair;
			if(StringUtil.checkNumber(mptSet[0])== true && StringUtil.checkNumber(mptSet[2]) == true){	//with max value
				if (o == 1) {
					maxValue = Double.parseDouble(mptSet[0]);
					minValue = Double.parseDouble(mptSet[2]);
				}
				else{
					minValue = Double.parseDouble(mptSet[0]);
					maxValue = Double.parseDouble(mptSet[2]);
				}
				pair = mptSet[1].split("-");
			}
			else
				return null;
			MPTProperty property = new MPTProperty();
			property.setChoosed(true);
			property.setMax(maxValue);
			property.setMin(minValue);
			if(setPropertyYearName(property, pair) == true) 
				return property;
		}
		return null;
	}
	
	public static List<BLAttributeBean> processExtras(String extraAttrs, String sort){
		if(extraAttrs == null || extraAttrs.equals("")){
			return null;
		}
		String[] tmpExtraPairs = extraAttrs.split(",");
		List<BLAttributeBean> attrs = new ArrayList<BLAttributeBean>();
		for(int i = 0; i < tmpExtraPairs.length; i++){
			String tmpExtra = tmpExtraPairs[i];
			if(tmpExtra.contains(">") && tmpExtra.contains("<"))	//format is not right
				continue;
			String[] mptSet;
			if(tmpExtra.contains("<")){
				BLAttributeBean attr = getBLAtrribute(tmpExtra, 0);
				if(attr != null)
					attrs.add(attr);
			}
			else if(tmpExtra.contains(">")){
				BLAttributeBean attr = getBLAtrribute(tmpExtra, 0);
				if(attr != null)
					attrs.add(attr);
			}
			else	//no max or min value
			{
				String[] pair = tmpExtra.split("-");
				BLAttributeBean attr = new BLAttributeBean();
				attr.setChoosed(true);
				if(setBLAttrDetails(attr, pair) == true) 
					attrs.add(attr);
			}
		}
		if(sort != null && !sort.equals("")){
			String[] tmpSortPairs = sort.split(",");
			for(int i = 0; i < tmpSortPairs.length; i++){
				String tmpSort = tmpSortPairs[i];
				String order = "desc";
				String orderAttr = tmpSort;
				if(tmpSort.contains("#")){
					String[] orderpairs = tmpSort.split("#");
					if(orderpairs.length == 2 && (orderpairs[1].equals("asc")))
						order = "asc";
					orderAttr = orderpairs[0];
				}
				String[] pairs = orderAttr.split("-");
				Long year = -1l;
				String name;
				if(pairs.length == 2){
					if(StringUtil.checkNumber(pairs[0]) == true){
						year = Long.parseLong(pairs[0]);
						if(year < 1000){
							year = -1 * year;
						}
					}
					name = pairs[1];
				}
				else
				{
					name = pairs[0];
				}
				if(attrs != null){
					BLAttributeBean b = getBLAttributeBean(name, year, attrs);
					if(b != null)
						b.setSort(order);
				}
			}
		}
		
		return attrs;
	}
	
	public static BLAttributeBean getBLAttributeBean(String name, Long year, List<BLAttributeBean> attrs){
		if(attrs == null || attrs.size() < 1)
			return null;
		for(int i = 0; i < attrs.size(); i++){
			BLAttributeBean b = attrs.get(i);
			if(b.getYear() != null && b.getYear() == year && b.getXMLName().equalsIgnoreCase(name)){
				return b;
			}
		}
		return null;
	}
	
	public static BLAttributeBean getBLAtrribute(String tmpMPT, int o){	//if o is 1 means ">" else if o is 0 means "<"
		String[] extraSet;
		if(o == 1)
			extraSet = tmpMPT.split(">");
		else
			extraSet = tmpMPT.split("<");
		Double maxValue = null;
		Double minValue = null;
		if(extraSet.length == 2){
			String[] pair;
			if(StringUtil.checkNumber(extraSet[0])==true){	//with max value
				if(o == 1)
					maxValue = Double.parseDouble(extraSet[0]);
				else
					minValue = Double.parseDouble(extraSet[0]);
				pair = extraSet[1].split("-");
			}
			else if(StringUtil.checkNumber(extraSet[1])==true){
				if(o == 1)
					minValue = Double.parseDouble(extraSet[1]);
				else
					maxValue = Double.parseDouble(extraSet[1]);
				pair = extraSet[0].split("-");
			}
			else
				return null;
			BLAttributeBean attr = new BLAttributeBean();
			attr.setChoosed(true);
			attr.setMaxValue(maxValue);
			attr.setMinValue(minValue);
			if(setBLAttrDetails(attr, pair) == true) 
				return attr;
		}
		if(extraSet.length == 3){
			String[] pair;
			if(StringUtil.checkNumber(extraSet[0])== true && StringUtil.checkNumber(extraSet[2]) == true){	//with max value
				if (o == 1) {
					maxValue = Double.parseDouble(extraSet[0]);
					minValue = Double.parseDouble(extraSet[2]);
				}
				else{
					minValue = Double.parseDouble(extraSet[0]);
					maxValue = Double.parseDouble(extraSet[2]);
				}
				pair = extraSet[1].split("-");
			}
			else
				return null;
			BLAttributeBean attr = new BLAttributeBean();
			attr.setChoosed(true);
			attr.setMaxValue(maxValue);
			attr.setMinValue(minValue);
			if(setBLAttrDetails(attr, pair) == true) 
				return attr;
		}
		return null;
	}
	
	public static boolean setBLAttrDetails(BLAttributeBean bean, String[] pair){
		Long year = null;
		if(pair == null || pair.length == 0)
			return false;
		Date startDate;
		Date endDate;
		String time;
		//set the default start date and end date
		if(pair.length < 2){
			year = -1l;
			endDate = LTIDate.clearHMSM(new Date());
			startDate = LTIDate.getLastYear(endDate);
			time = "1y";
			bean.setStartDate(startDate);
			bean.setEndDate(endDate);
			bean.setYear(year);
			setAttributeNames(pair[0], bean);
			
		}
		//get the start date and end date
		else if(pair.length == 2){
			Calendar calendar = Calendar.getInstance();
			if(pair[0].endsWith("m")){
				Long month = Long.parseLong(pair[0].substring(0, pair[0].length() - 1));
				time = month + "m";
				month = month * -1;
				endDate = LTIDate.clearHMSM(new Date());
				calendar.setTime(endDate);
				calendar.add(Calendar.MONTH, month.intValue());
				startDate = calendar.getTime();
				
			}else if(pair[0].endsWith("d")){
				Long day = Long.parseLong(pair[0].substring(0, pair[0].length() - 1));
				time = day + "d";
				day = day * -1;
				endDate = LTIDate.clearHMSM(new Date());
				calendar.setTime(endDate);
				calendar.add(Calendar.DATE, day.intValue());
				startDate = calendar.getTime();
				
			}else if(pair[0].endsWith("y")){
				year = Long.parseLong(pair[0].substring(0, pair[0].length() - 2));
				if(year <= 100){	
					time = year + "y";
					year = year * -1;
					endDate = LTIDate.clearHMSM(new Date());
					calendar.setTime(endDate);
					calendar.add(Calendar.YEAR, year.intValue());
					startDate = calendar.getTime();
				}
				else
				{
					calendar.set(Calendar.YEAR, year.intValue());
					calendar.set(Calendar.MONTH, Calendar.DECEMBER);
					calendar.set(Calendar.DAY_OF_MONTH, 31);
					endDate = calendar.getTime();
					calendar.set(Calendar.MONTH, Calendar.JANUARY);
					calendar.set(Calendar.DAY_OF_MONTH, 1);
					startDate = calendar.getTime();
					time = year.toString();
				}	
			}else if(StringUtil.checkNumber(pair[0]) == true){
				year = Long.parseLong(pair[0]);
				if(year <= 100){		
					time = year + "y";
					year = year * -1;
					endDate = LTIDate.clearHMSM(new Date());
					calendar.setTime(endDate);
					calendar.add(Calendar.YEAR, year.intValue());
					startDate = calendar.getTime();
				}
				else
				{
					calendar.set(Calendar.YEAR, year.intValue());
					calendar.set(Calendar.MONTH, Calendar.DECEMBER);
					calendar.set(Calendar.DAY_OF_MONTH, 31);
					endDate = calendar.getTime();
					calendar.set(Calendar.MONTH, Calendar.JANUARY);
					calendar.set(Calendar.DAY_OF_MONTH, 1);
					startDate = calendar.getTime();
					time = year.toString();
				}	
			}else{
				endDate = LTIDate.clearHMSM(new Date());
				startDate = LTIDate.getLastYear(endDate);
				time = "1y";
			}
			
			bean.setStartDate(startDate);
			bean.setEndDate(endDate);
			bean.setYear(year);
			bean.setTime(time);
			setAttributeNames(pair[1], bean);
		}
		//add other attributes
		else
			return false;
		return true;
	}
	
	public static void setAttributeNames(String name, BLAttributeBean bean){
		if(name.equalsIgnoreCase(BLAttributeBean.XML_DISCOUNTRATE)){
			bean.setAttributeName(BLAttributeBean.DISCOUNTRATE);
			bean.setXMLName(BLAttributeBean.XML_DISCOUNTRATE);
		}else if(name.equalsIgnoreCase(BLAttributeBean.XML_NAV_ALPHA)){
			bean.setAttributeName(BLAttributeBean.NAV_ALPHA);
			bean.setXMLName(BLAttributeBean.XML_NAV_ALPHA);
		}else if(name.equalsIgnoreCase(BLAttributeBean.XML_NAV_AR)){
			bean.setAttributeName(BLAttributeBean.NAV_AR);
			bean.setXMLName(BLAttributeBean.XML_NAV_AR);
		}else if(name.equalsIgnoreCase(BLAttributeBean.XML_NAV_BETA)){
			bean.setAttributeName(BLAttributeBean.NAV_BETA);
			bean.setXMLName(BLAttributeBean.XML_NAV_BETA);
		}else if(name.equalsIgnoreCase(BLAttributeBean.XML_NAV_DRAWDOWN)){
			bean.setAttributeName(BLAttributeBean.NAV_DRAWDOWN);
			bean.setXMLName(BLAttributeBean.XML_NAV_DRAWDOWN);
		}else if(name.equalsIgnoreCase(BLAttributeBean.XML_NAV_RETURN)){
			bean.setAttributeName(BLAttributeBean.NAV_RETURN);
			bean.setXMLName(BLAttributeBean.XML_NAV_RETURN);
		}else if(name.equalsIgnoreCase(BLAttributeBean.XML_NAV_RSQUARED)){
			bean.setAttributeName(BLAttributeBean.NAV_RSQUARED);
			bean.setXMLName(BLAttributeBean.XML_NAV_RSQUARED);
		}else if(name.equalsIgnoreCase(BLAttributeBean.XML_NAV_SHARPE)){
			bean.setAttributeName(BLAttributeBean.NAV_SHARPE);
			bean.setXMLName(BLAttributeBean.XML_NAV_SHARPE);
		}else if(name.equalsIgnoreCase(BLAttributeBean.XML_NAV_STANDARDDIVIATION)){
			bean.setAttributeName(BLAttributeBean.NAV_STANDARDDIVIATION);
			bean.setXMLName(BLAttributeBean.XML_NAV_STANDARDDIVIATION);
		}else if(name.equalsIgnoreCase(BLAttributeBean.XML_NAV_TREYNOR)){
			bean.setAttributeName(BLAttributeBean.NAV_TREYNOR);
			bean.setXMLName(BLAttributeBean.XML_NAV_TREYNOR);
		}
	}
	public static void main(String[] args){
		/*String chosenMPT = "1-alpha>0,0<3-AR<1";
		String sort = "3-AR#asc";
		SecurityTableAction sta = new SecurityTableAction();
		List<MPTProperty> mpts = sta.processMPTs(chosenMPT, sort);*/
		String extraAttr = "0<2-discountRate<5";
		String sort = "2-discountRate#asc";
		List<BLAttributeBean> attrs = SecurityTableUtil.processExtras(extraAttr, sort);
		for(int i = 0; i < attrs.size(); i++){
			BLAttributeBean b = attrs.get(i);
			System.out.println(b.getYear()+ b.getXMLName() + "  " + b.getMinValue() + "  " + b.getMaxValue() + "  " + b.getSort());
			System.out.print(b.getStartDate().toString() + "   " + b.getEndDate().toString());
		}
	}
}
