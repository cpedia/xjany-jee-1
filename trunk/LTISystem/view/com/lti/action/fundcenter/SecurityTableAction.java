package com.lti.action.fundcenter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.lti.action.Action;
import com.lti.bean.BLAttributeBean;
import com.lti.bean.MPTProperty;
import com.lti.bean.SortTypeBean;
import com.lti.service.AssetClassManager;
import com.lti.service.SecurityManager;
import com.lti.service.bo.AssetClass;
import com.lti.service.bo.Security;
import com.lti.service.bo.SecurityMPT;
import com.lti.system.Configuration;
import com.lti.type.MPT;
import com.lti.util.LTIDate;
import com.lti.util.SecurityMPTSymbolComp;
import com.lti.util.SecurityTableUtil;
import com.lti.util.SecurityUtil;
import com.lti.util.StringUtil;
import com.opensymphony.xwork2.ActionSupport;

public class SecurityTableAction extends ActionSupport implements Action {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private java.lang.String chosenMPT;
	
	private java.lang.Boolean bStartDate;
	
	private java.util.List<SecurityMPT> securityList;
	
	private java.lang.String securityType;
	
	private java.util.List<Integer> securityTypes;
	
	private java.lang.String assetClass;
	
	private java.lang.String extraAttrs;
	
	//using the format as 1-sharpeRatio#asc or 3-ahpha#desc as order by 1 year's sharpe ratio asc or order by 3 years'alpha desc no need for the "-" before the number
	private java.lang.String sort;
	
	private SecurityManager securityManager;
	
	private AssetClassManager assetClassManager;
	
	private java.util.List<String> MPTHead;
	
	private java.util.List<Long> assetClasses;
	
	private java.lang.Boolean isUseAssetClass;
	
	private java.lang.Integer size;
	
	private List<MPTProperty> MPTList;
	
	private List<BLAttributeBean> extras;
	
	private java.lang.Integer sortColumn;
	
	private java.lang.Integer isDesc;
	
	@Override
	public void validate() {
		// TODO Auto-generated method stub
		super.validate();
		/*deal with the MPT settings*/
		if(chosenMPT == null || chosenMPT.equals("")){
			addActionError("No Selected Funds!");
		}		
		
		/*deal with the security types*/
		if(securityType != null && !securityType.equals("")){
			String[] tmpTypes = securityType.split(",");
			securityTypes = new ArrayList<Integer>();
			for(int i = 0; i < tmpTypes.length; i++){
				Integer type = Configuration.getSecurityType(tmpTypes[i]);
				securityTypes.add(type);
			}
		}
		
		
		if(assetClass != null && !assetClass.equals("")){
			isUseAssetClass = true;
			String[] tmpClasses = assetClass.split(",");
			assetClasses = new ArrayList<Long>();
			for(int i = 0; i < tmpClasses.length; i++){
				AssetClass ac = assetClassManager.get(tmpClasses[i]);
				if(ac != null){
					assetClasses.add(ac.getID());
					List<AssetClass> childs = assetClassManager.getChildClass(ac.getID());
					if(childs != null){
						for(int j = 0; j < childs.size(); j++){
							if(assetClasses.contains(childs.get(j).getID()) == false){
								assetClasses.add(childs.get(j).getID());
							}
						}
					}
				}
			}
		}
		else
			isUseAssetClass = false;
		if(size == null)
			size = 0;
	}

	

	@Override
	public String execute() throws Exception {
		// TODO Auto-generated method stub
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Security.class);
		if(isUseAssetClass == true){
			detachedCriteria.add(Restrictions.in("ClassID", assetClasses));
		}
		
		MPTList = SecurityTableUtil.processMPTs(chosenMPT, sort);
		extras = SecurityTableUtil.processExtras(extraAttrs, sort);
		securityList = SecurityUtil.selectSecurityMPTs(MPTList, securityTypes, null, detachedCriteria, size);
		securityList = SecurityUtil.selectMPTsWithEA(securityList, extras, size);
		if(securityList != null && securityList.size() > size)
			securityList = securityList.subList(0, size - 1);
		Comparator<SecurityMPT> comparator = new SecurityMPTSymbolComp();
		Collections.sort(securityList, comparator);
		if(securityList != null && securityList.size() != 0){
			for(int i = 0; i < securityList.size(); i++){
				SecurityMPT s = securityList.get(i);
				String assetClassName = " ";
				if(s.getAssetClassID()!=null){
					assetClassName = assetClassManager.get(s.getAssetClassID()).getName();
				}
				s.setAssetClassName(assetClassName);
				s.setSecurityTypeName(Configuration.getSecurityTypeName(s.getSecurityType()));
			}
			
			Map<Long, List<MPTProperty>> yearMap = SecurityUtil.categoryByYear(MPTList);
			if(yearMap != null && yearMap.size() > 0){
				Iterator<Long> years = yearMap.keySet().iterator();
				MPTList.removeAll(MPTList);
				while (years.hasNext()) {
					Long year = (Long) years.next();
					List<MPTProperty> properties = yearMap.get(year);
					if(properties != null)
						MPTList.addAll(properties);
				}
			}
			if(isUseAssetClass)
				sortColumn=4;
			else
				sortColumn=3;
			for(int i=0;i<MPTList.size();++i)
			{
				if(MPTList.get(i).getSort()!=null)
				{
					sortColumn+=i;
					if(MPTList.get(i).getSort().equalsIgnoreCase("desc"))
						isDesc=1;
					else
						isDesc=0;
					break;
				}
			}
		}
		
		return Action.SUCCESS;
	}

	
	public java.lang.Boolean getBStartDate() {
		return bStartDate;
	}

	public void setBStartDate(java.lang.Boolean startDate) {
		bStartDate = startDate;
	}

	public java.util.List<SecurityMPT> getSecurityList() {
		return securityList;
	}

	public void setSecurityList(java.util.List<SecurityMPT> securityList) {
		this.securityList = securityList;
	}

	public java.lang.String getSecurityType() {
		return securityType;
	}

	public void setSecurityType(java.lang.String securityType) {
		this.securityType = securityType;
	}

	public java.lang.String getAssetClass() {
		return assetClass;
	}

	public void setAssetClass(java.lang.String assetClass) {
		this.assetClass = assetClass;
	}

	public java.lang.String getExtraAttrs() {
		return extraAttrs;
	}

	public void setExtraAttrs(java.lang.String extraAttrs) {
		this.extraAttrs = extraAttrs;
	}

	public void setSecurityManager(SecurityManager securityManager) {
		this.securityManager = securityManager;
	}

	public void setAssetClassManager(AssetClassManager assetClassManager) {
		this.assetClassManager = assetClassManager;
	}


	public java.util.List<String> getMPTHead() {
		return MPTHead;
	}



	public void setMPTHead(java.util.List<String> head) {
		MPTHead = head;
	}



	public java.lang.String getChosenMPT() {
		return chosenMPT;
	}



	public void setChosenMPT(java.lang.String chosenMPT) {
		this.chosenMPT = chosenMPT;
	}


	public java.lang.Boolean getIsUseAssetClass() {
		return isUseAssetClass;
	}



	public void setIsUseAssetClass(java.lang.Boolean isUseAssetClass) {
		this.isUseAssetClass = isUseAssetClass;
	}



	public java.lang.String getSort() {
		return sort;
	}



	public void setSort(java.lang.String sort) {
		this.sort = sort;
	}



	public java.lang.Integer getSize() {
		return size;
	}



	public void setSize(java.lang.Integer size) {
		this.size = size;
	}



	public List<MPTProperty> getMPTList() {
		return MPTList;
	}



	public void setMPTList(List<MPTProperty> list) {
		MPTList = list;
	}



	public List<BLAttributeBean> getExtras() {
		return extras;
	}



	public void setExtras(List<BLAttributeBean> extras) {
		this.extras = extras;
	}



	public java.lang.Integer getSortColumn() {
		return sortColumn;
	}



	public void setSortColumn(java.lang.Integer sortColumn) {
		this.sortColumn = sortColumn;
	}



	public java.lang.Integer getIsDesc() {
		return isDesc;
	}



	public void setIsDesc(java.lang.Integer isDesc) {
		this.isDesc = isDesc;
	}	

}
