package com.lti.action.security;
import java.util.*;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.lti.action.Action;
import com.lti.bean.BLAttributeBean;
import com.lti.bean.MPTProperty;
import com.lti.bean.SortTypeBean;
import com.lti.service.AssetClassManager;
import com.lti.service.CustomizeRegionManager;
import com.lti.service.PortfolioManager;
import com.lti.service.SecurityManager;
import com.lti.service.UserManager;
import com.lti.service.bo.AssetClass;
import com.lti.service.bo.CustomizeRegion;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.PortfolioMPT;
import com.lti.service.bo.Security;
import com.lti.service.bo.SecurityMPT;
import com.lti.bean.SecurityType;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.type.BLExtraAttr;
import com.lti.type.TimeUnit;
import com.lti.util.CustomizeUtil;
import com.lti.util.EscapeUnescapeUtil;
import com.lti.util.FormatUtil;
import com.lti.util.GenerateAssetClassTree;
import com.lti.util.LTIDate;
import com.lti.util.SecurityUtil;
import com.lti.util.StringUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;

@Deprecated
public class ScreeningAction extends ActionSupport implements Action {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String Name;
	
	private Integer Sort;
	
	private List<Integer> BasicType;
	
	private List<Integer> AdvancedType;
	
	private List<SecurityType> TypeList;
	
	private List<SortTypeBean> SortList;
	
	private List<MPTProperty> showList;
	
	private List<MPTProperty> MPTList;
	
	private String DefaultYearStr;
	
	private Long DefaultYear;
	
	private Long assetClassID;
	
	private Boolean isUseAssetClass;
	
	private java.lang.String classTree;
	
	private List<SecurityMPT> securityList;
	
	private SecurityManager securityManager;
	
	private UserManager userManager;
	
	private AssetClassManager assetClassManager;
	
	private CustomizeRegionManager customizeRegionManager;
	
	private CustomizeRegion customizeRegion;
	
	private String resultString;
	
	private Map session;
	
	private List<MPTProperty> MPTList2;
	
	private BLExtraAttr ExtraAttrs;
	
	private Map<String,Double> ExtraAttrValue;


	public String getResultString() {
		return resultString;
	}

	public void setResultString(String resultString) {
		this.resultString = resultString;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public List<Integer> getBasicType() {
		return BasicType;
	}

	  

	public void setTypeList() {
		TypeList = new ArrayList<SecurityType>();
		SecurityType st = new SecurityType();
		st.setName("Mutual Fund");
		st.setType(Configuration.getSecurityType("Mutual Fund"));
		TypeList.add(st);
		st = new SecurityType();
		st.setName("ETF");
		st.setType(Configuration.getSecurityType("ETF"));
		TypeList.add(st);
		st = new SecurityType();
		st.setName("Closed End Fund");
		st.setType(Configuration.getSecurityType("Closed End Fund"));
		TypeList.add(st);
		st = new SecurityType();
		st.setName("Benchmark");
		st.setType(Configuration.getSecurityType("Benchmark"));
		TypeList.add(st);
		st = new SecurityType();
		st.setName("Stock");
		st.setType(Configuration.getSecurityType("Stock"));
		TypeList.add(st);
		st = new SecurityType();
		st.setName("Portfolio");
		st.setType(Configuration.getSecurityType("Portfolio"));
		TypeList.add(st);
	}

	public List<SortTypeBean> getSortList() {
		return SortList;
	}

	public void setSortList() {
		SortList = new ArrayList<SortTypeBean>();
		SortTypeBean stb = new SortTypeBean();
		stb.setName("RETURN");
		stb.setValue(SortTypeBean.RETURN);
		SortList.add(stb);
		stb = new SortTypeBean();
		stb.setName("SHARPE");
		stb.setValue(SortTypeBean.SHARPE);
		SortList.add(stb);
		stb = new SortTypeBean();
		stb.setName("ALPHA");
		stb.setValue(SortTypeBean.ALPHA);
		SortList.add(stb);
		stb = new SortTypeBean();
		stb.setName("BETA");
		stb.setValue(SortTypeBean.BETA);
		SortList.add(stb);
		stb = new SortTypeBean();
		stb.setName("RSQUARED");
		stb.setValue(SortTypeBean.RSQUARED);
		SortList.add(stb);
		stb = new SortTypeBean();
		stb.setName("TREYNOR");
		stb.setValue(SortTypeBean.TREYNOR);
		SortList.add(stb);
		stb = new SortTypeBean();
		stb.setName("STANDARDDIVIATION");
		stb.setValue(SortTypeBean.STANDARDDIVIATION);
		SortList.add(stb);
		stb = new SortTypeBean();
		stb.setName("DRAWDOWN");
		stb.setValue(SortTypeBean.DRAWDOWN);
		SortList.add(stb);
		stb = new SortTypeBean();
		stb.setName("ANNULIZEDRETURN");
		stb.setValue(SortTypeBean.ANNULIZEDRETURN);
		SortList.add(stb);
	}
	
	


	public CustomizeRegion getCustomizeRegion() {
		return customizeRegion;
	}

	public void setCustomizeRegion(CustomizeRegion customizeRegion) {
		this.customizeRegion = customizeRegion;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public void setCustomizeRegionManager(
			CustomizeRegionManager customizeRegionManager) {
		this.customizeRegionManager = customizeRegionManager;
	}

	@Override
	public void validate() {
		//set the customize regions
		
	}

	public String view(){
		this.setSortList();
		this.setTypeList();
		this.setMPTList();
		this.setMPTList2();
		ExtraAttrs = new BLExtraAttr();
		
		Long userID = userManager.getLoginUser().getID();
		customizeRegion = customizeRegionManager.get(CustomizeUtil.SECURITY_SCREENING);
		CustomizeUtil.setRegion(customizeRegion, userID);

		classTree = GenerateAssetClassTree.getTreeList(assetClassManager.getClasses());
		assetClassID = 1l;
		
		return Action.SUCCESS;
	}

	
	@SuppressWarnings("unchecked")
	public String normalScreening(){
		Long userID = userManager.getLoginUser().getID();
		customizeRegion = customizeRegionManager.get(CustomizeUtil.SECURITY_SCREENING_RESULT);
		CustomizeUtil.setRegion(customizeRegion, userID);
		//manage the conditions
		showList = new ArrayList<MPTProperty>();
		translateMPTCondition(MPTList2, showList);
		Boolean isNameForPortfolio = false;
		List<PortfolioMPT> portfolios = new ArrayList<PortfolioMPT>();
		ExtraAttrs.translateAttributes();
		//Name = EscapeUnescapeUtil.unescape(Name);
		if((Name == null || Name.equals("")) && (BasicType == null || BasicType.size() == 0)){
			BasicType = new ArrayList<Integer>();
			BasicType.add(Configuration.SECURITY_TYPE_BENCHMARK);
			BasicType.add(Configuration.SECURITY_TYPE_CLOSED_END_FUND);
			BasicType.add(Configuration.SECURITY_TYPE_ETF);
			BasicType.add(Configuration.SECURITY_TYPE_MUTUAL_FUND);
			BasicType.add(Configuration.SECURITY_TYPE_PORTFOLIO);
			BasicType.add(Configuration.SECURITY_TYPE_STOCK);
		}
//		if(PortfolioUtil.getPortfolioType(BasicType) != null){
//			DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Portfolio.class);
//			if(PortfolioUtil.getPortfolioIDFromSymbol(Name) != null)
//				detachedCriteria.add(Restrictions.eq("ID", PortfolioUtil.getPortfolioIDFromSymbol(Name)));
//			else if(Name != null && !Name.equals("")){
//				List<String> keywords = StringUtil.splitKeywords(Name);
//				
//				if(keywords != null && !keywords.equals("")){
//					String keyword = "%" + keywords.get(0) + "%";
//					for(int i = 1; i < keywords.size(); i++){
//						String s = keywords.get(i);
//						keyword = keyword + s + "%";
//					}
//					detachedCriteria.add(Restrictions.like("Name", keyword));
//				}
//				//detachedCriteria.add(Restrictions.like("Name", "%" + Name + "%"));
//				//if(userID != Configuration.SUPER_USER_ID)
//					//detachedCriteria.add(Restrictions.or(Restrictions.eq("IsPublic", true), Restrictions.eq("UserID", userID)));
//			}
//			PortfolioUtil.choosePortfolioMPTBySQL(portfolios, MPTList2, detachedCriteria);
//			PortfolioUtil.setShowList(portfolios, showList);
//			BasicType.remove(PortfolioUtil.getPortfolioType(BasicType));
//			isNameForPortfolio = true;
//		}
		securityList = new ArrayList<SecurityMPT>();
		if ((Name != null  && !Name.equals("") && isNameForPortfolio == false) || (BasicType != null && BasicType.size() != 0)) {
			DetachedCriteria detachedCriteria2 = DetachedCriteria.forClass(Security.class);
			if (Name != null && !Name.equals("")) {
				List<String> keywords = StringUtil.splitKeywords(Name);
				
				if(keywords != null && !keywords.equals("")){
					String keyword = "%" + keywords.get(0) + "%";
					for(int i = 1; i < keywords.size(); i++){
						String s = keywords.get(i);
						keyword = keyword + s + "%";
					}
					detachedCriteria2.add(Restrictions.or(Restrictions.like("Name",keyword), Restrictions.like("Symbol", keyword)));
				}
				//detachedCriteria2.add(Restrictions.or(Restrictions.like("Name","%" + Name + "%"), Restrictions.like("Symbol", "%" + Name + "%")));
			}
			if (BasicType != null && BasicType.size() != 0) {
				detachedCriteria2.add(Restrictions.in("SecurityType", BasicType));
			}
			if (isUseAssetClass != null && isUseAssetClass == true) {
				List<AssetClass> assetClasses = assetClassManager.getChildClass(assetClassID);
				List<Long> assetClassIDs = new ArrayList<Long>();
				assetClassIDs.add(assetClassID);
				for(int i = 0; i < assetClasses.size(); i++){
					assetClassIDs.add(assetClasses.get(i).getID());
				}
				detachedCriteria2.add(Restrictions.in("ClassID", assetClassIDs));
			}
			SecurityUtil.basicSearch(securityList, MPTList2, ExtraAttrs.getExtraAttributes(), BasicType, detachedCriteria2);
			SecurityUtil.setShowList(securityList, showList);
		}
		if(portfolios != null && portfolios.size() > 0){
			List<SecurityMPT> portToSecurities = SecurityUtil.translatePortToSecMPT(portfolios, ExtraAttrs.getExtraAttributes());
			securityList.addAll(portToSecurities);
		}
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
		}
		ActionContext ac = ActionContext.getContext();
		session = ac.getSession();
		session.put("securities", securityList);
		session.put("mpts", MPTList2);
		if(isUseAssetClass == null)
			isUseAssetClass = false;
		session.put("isUsedAssetClass", isUseAssetClass);
		session.put("extraAttrs", ExtraAttrs);
		session.put("showList", showList);
		return Action.SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	public String advancedScreening(){
		Long userID = userManager.getLoginUser().getID();
		customizeRegion = customizeRegionManager.get(CustomizeUtil.SECURITY_SCREENING_RESULT);
		CustomizeUtil.setRegion(customizeRegion, userID);
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Security.class);
		if(Name != null && !Name.equals("")){
			detachedCriteria.add(Restrictions.or(Restrictions.like("Name", Name), Restrictions.like("Symbol", Name)));
		}
		if(AdvancedType !=null && AdvancedType.size() != 0){
			detachedCriteria.add(Restrictions.in("SecurityType", AdvancedType));
		}
		if(isUseAssetClass == true){
			detachedCriteria.add(Restrictions.eq("ClassID", assetClassID));
		}
		List<Security> securities = securityManager.getSecurities(detachedCriteria);
		translateMPTCondition(MPTList, showList);
		//translateDates(MPTList);
		securityList = new ArrayList<SecurityMPT>();
		//SecurityUtil.translateSecurityToMPT(securities, securityList);
		SecurityUtil.basicSearch(securityList, MPTList2, ExtraAttrs.getExtraAttributes(), BasicType, detachedCriteria);
		if(MPTList != null && MPTList.size() != 0){	
			for(int i = 0; i < MPTList.size(); i++){
				MPTProperty mpt = MPTList.get(i);
				if(mpt.getChoosed() != null && mpt.getChoosed() == true){
					chooseSecurities(securities, securityList, mpt);
				}
			}
		}
		ActionContext ac = ActionContext.getContext();
		session = ac.getSession();
		session.put("securities", securityList);
		session.put("mpts", MPTList);
		if(isUseAssetClass == null)
			isUseAssetClass = false;
		session.put("isUsedAssetClass", isUseAssetClass);
		session.put("showList", showList);
		return Action.SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	public String screeningOutput(){
		session =(Map) ActionContext.getContext().get(ActionContext.SESSION);
		List<MPTProperty> mpts =(List<MPTProperty>) session.get("mpts");
		List<SecurityMPT> securities = (List<SecurityMPT>) session.get("securities");
		Boolean isUseAssetClass = (Boolean) session.get("isUsedAssetClass");
		BLExtraAttr ExtraAttrs = (BLExtraAttr) session.get("extraAttrs");
		List<MPTProperty> showList = (List<MPTProperty>) session.get("showList");
		try {
			resultString = generateXML(securities, mpts, isUseAssetClass, ExtraAttrs, showList);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(resultString);
		return Action.SUCCESS;
	}
	
	private String generateXML(List<SecurityMPT> securities, List<MPTProperty> mpts, Boolean isUseAssetClass, BLExtraAttr ExtraAttrs, List<MPTProperty> showList){
		StringBuffer sb = new StringBuffer();
		sb.append( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<Root>");
		tableHead(sb, mpts, isUseAssetClass, ExtraAttrs, showList);
		List<BLAttributeBean> Attrs = ExtraAttrs.getExtraAttributes();
		if(securities == null || securities.size() == 0 ){
			sb.append("</Root>");
			return sb.toString();
		}
		sb.append("<Data>");
		for(int i = 0; i < securities.size(); i++){
			SecurityMPT s = securities.get(i);
			sb.append("<security>");
			sb.append("<Name>" + s.getSecurityName() + "</Name>");
			sb.append("<Symbol>" + s.getSymbol() + "</Symbol>");
			String type = Configuration.getSecurityTypeName(s.getSecurityType());
			sb.append("<Type>" + type + "</Type>");
			String linkType;
			if(s.getSymbol().startsWith("P_")){
				linkType = Configuration.LinkType_Portfolio;
				String symbol = s.getSymbol();
				//Long portfolioID = PortfolioUtil.getPortfolioIDFromSymbol(symbol);
				//if(portfolioID != null)
				//	sb.append("<ID>" + portfolioID + "</ID>");
			}
			else
			{
				linkType = Configuration.LinkType_Security;
				sb.append("<ID>" + s.getSecurityID() + "</ID>");
			}
			sb.append("<LinkType>" + linkType + "</LinkType>");
			
			if(isUseAssetClass == true){
				String assetClassName = " ";
				if(s.getAssetClassID()!=null){
					assetClassName = assetClassManager.get(s.getAssetClassID()).getName();
				}
				sb.append("<AssetClass>" + assetClassName + "</AssetClass>");
			}
			if(Attrs != null){
				for(int j = 0; j < Attrs.size(); j++){
					BLAttributeBean bab = Attrs.get(j);
					if(bab.getXMLName() != null && bab.getXMLName().equals(BLAttributeBean.XML_DISCOUNTRATE) && s.getSecurityType().equals(bab.getSecurityType())){
						//Double value = s.getDiscountRate();
						String valueStr = s.getExtras().get(j);
						sb.append("<" + bab.getXMLName() + ">" + valueStr + "</" + bab.getXMLName() + ">");
					}
					else if(bab.getXMLName() != null && bab.getXMLName().equals(BLAttributeBean.XML_DISCOUNTRATE) && !s.getSecurityType().equals(bab.getSecurityType())){
						sb.append("<" + bab.getXMLName() + ">" + "NA" + "</" + bab.getXMLName() + ">");
					}
				}
			}
			if( mpts != null && mpts.size() != 0 || (showList != null && showList.size() != 0))
				MPTShow(s, sb, mpts, showList);
			sb.append("</security>");
		}
		sb.append("</Data>");
		sb.append("<Link>");
		sb.append("<SecurityLink>");
		sb.append("<Head>" + "/LTISystem/jsp/security/Save.action?ID=" + "</Head>");
		sb.append("<Tail>" + "&action=view" + "</Tail>");
		sb.append("</SecurityLink>");
		sb.append("<PortfolioLink>");
		sb.append("<Head>" + "/LTISystem/jsp/portfolio/Edit.action?ID=" + "</Head>");
		sb.append("<Tail>" + "&action=view" + "</Tail>");
		sb.append("</PortfolioLink>");
		sb.append("</Link>");
		sb.append("</Root>");
		return sb.toString();
	}
	
	
	
	public static void translateMPTCondition(List<MPTProperty> mpts, List<MPTProperty> showList){
		if(mpts == null || mpts.size() == 0)
			return;
		for(int i = 0; i < mpts.size(); i++){
			MPTProperty m = mpts.get(i);
			if(m.getChoosed() == null || m.getChoosed() == false){
				mpts.remove(i);
				i--;
				continue;
			}
			if((m.getMaxStr() == null || m.getMaxStr().equals("")) && (m.getMinStr() == null || m.getMinStr().equals(""))){
				m.setYear(-1l);
				showList.add(m);
				mpts.remove(i);				
				i--;
				continue;
			}
			if(m.getMaxStr() != null && !m.getMaxStr().equals(""))
				m.setMax(StringUtil.percentageToDouble(m.getMaxStr()));
			if(m.getMinStr() != null && !m.getMinStr().equals(""))
				m.setMin(StringUtil.percentageToDouble(m.getMinStr()));
		}
	}
	

	
	private void tableHead(StringBuffer sb, List<MPTProperty> mpts, Boolean isUseAssetClass, BLExtraAttr ExtraAttrs, List<MPTProperty> showList){
		sb.append("<tablehead>");
		sb.append("<column>Name</column>");
		sb.append("<column>Symbol</column>");
		sb.append("<column>Type</column>");
		if(isUseAssetClass == true)
			sb.append("<column>AssetClass</column>");
		List<BLAttributeBean> Attrs = ExtraAttrs.getExtraAttributes();
		if(Attrs != null && Attrs.size() != 0){
			for(int i = 0; i < Attrs.size(); i++){
				BLAttributeBean bab = Attrs.get(i);
				if(bab.getChoosed() != null && bab.getChoosed() == true){
					sb.append("<column>" + bab.getXMLName()  + "</column>");
				}
			}
		}
		if((mpts != null && mpts.size() != 0) || (showList != null && showList.size() != 0)){
			int showNum = 0;
			if (mpts != null && mpts.size() != 0) {
				MPTProperty mpt = mpts.get(0);
				if (mpt.getStartingDate() != null && mpt.getYear() == null) {
					sb.append("<column>StartingDate</column>");
					sb.append("<column>EndDate</column>");
				}
				showNum = mpts.size();
			}
			if(showList != null && showList.size() != 0){
				showNum += showList.size();
			}
			for(int i = 0;i < showNum; i++){
				//int show = ShowList.get(i);
				MPTProperty m;
				if (mpts != null && mpts.size() != 0 && i < mpts.size()) {
					m = mpts.get(i);
					if(m.getChoosed() == null || m.getChoosed() == false || (m.getMax() == null && m.getMin() == null))
						continue;
				}
				else
				{
					int j = i - mpts.size();
					m = showList.get(j);
				}
				int show = SortTypeBean.getMPT(m.getName());
				switch (show){
					case SortTypeBean.ALPHA:{
						if(m.getYear() == null)
							sb.append("<column>" + SortTypeBean.getMPTName(SortTypeBean.ALPHA) + "</column>");
						else
							sb.append("<column>" + SortTypeBean.getMPTName(SortTypeBean.ALPHA) + m.getYear() + "</column>");
						break;
					}
					case SortTypeBean.ANNULIZEDRETURN:{
						if(m.getYear() == null)
							sb.append("<column>" + SortTypeBean.getMPTName(SortTypeBean.ANNULIZEDRETURN) + "</column>");
						else
							sb.append("<column>" + SortTypeBean.getMPTName(SortTypeBean.ANNULIZEDRETURN) + m.getYear() + "</column>");
						break;
					}
					case SortTypeBean.BETA:{
						if(m.getYear() == null)
							sb.append("<column>" + SortTypeBean.getMPTName(SortTypeBean.BETA) + "</column>");
						else
							sb.append("<column>" + SortTypeBean.getMPTName(SortTypeBean.BETA) + m.getYear() + "</column>");
						break;
					}
					case SortTypeBean.DRAWDOWN:{
						if(m.getYear() == null)
							sb.append("<column>" + SortTypeBean.getMPTName(SortTypeBean.DRAWDOWN) + "</column>");
						else
							sb.append("<column>" + SortTypeBean.getMPTName(SortTypeBean.DRAWDOWN) + m.getYear() + "</column>");
						break;
					}
					case SortTypeBean.RETURN:{
						if(m.getYear() == null)
							sb.append("<column>" + SortTypeBean.getMPTName(SortTypeBean.RETURN) + "</column>");
						else
							sb.append("<column>" + SortTypeBean.getMPTName(SortTypeBean.RETURN) + m.getYear() + "</column>");
						break;
					}
					case SortTypeBean.RSQUARED:{
						if(m.getYear() == null)
							sb.append("<column>" + SortTypeBean.getMPTName(SortTypeBean.RSQUARED) + "</column>");
						else
							sb.append("<column>" + SortTypeBean.getMPTName(SortTypeBean.RSQUARED) + m.getYear() + "</column>");
						break;
					}
					case SortTypeBean.SHARPE:{
						if(m.getYear() == null)
							sb.append("<column>" + SortTypeBean.getMPTName(SortTypeBean.SHARPE) + "</column>");
						else
							sb.append("<column>" + SortTypeBean.getMPTName(SortTypeBean.SHARPE) + m.getYear() + "</column>");
						break;
					}
					case SortTypeBean.STANDARDDIVIATION:{
						if(m.getYear() == null)
							sb.append("<column>" + SortTypeBean.getMPTName(SortTypeBean.STANDARDDIVIATION) + "</column>");
						else
							sb.append("<column>" + SortTypeBean.getMPTName(SortTypeBean.STANDARDDIVIATION) + m.getYear() + "</column>");
						break;
					}
					case SortTypeBean.TREYNOR:{
						if(m.getYear() == null)
							sb.append("<column>" + SortTypeBean.getMPTName(SortTypeBean.TREYNOR) + "</column>");
						else
							sb.append("<column>" + SortTypeBean.getMPTName(SortTypeBean.TREYNOR) + m.getYear() + "</column>");
						break;
					}
				}
			}
			
		}
		sb.append("</tablehead>");
	}
	
	private void MPTShow(SecurityMPT s, StringBuffer sb, List<MPTProperty> mpts, List<MPTProperty> showList){
		int showNum = 0;
		if (mpts != null && mpts.size() != 0) {
			showNum = mpts.size();
		}
		if(showList != null && showList.size() != 0)
			showNum += showList.size();
		for(int i = 0; i < showNum; i++){		
			MPTProperty m;
			if (mpts != null && mpts.size() != 0 && i < mpts.size()) {
				m = mpts.get(i);
				if (m.getChoosed() == null || m.getChoosed() == false || (m.getMax() == null && m.getMin() == null))
					continue;
			}
			else{
				int j = i - mpts.size();
				m = showList.get(j);
			}
			int show = SortTypeBean.getMPT(m.getName());
			//Double value;
			if(m.getStartingDate() != null && m.getYear() == null){
				sb.append("<StartingDate>" + m.getStartingDate() + "</StartingDate>");
				sb.append("<EndDate>" + m.getEndDate() + "</EndDate>");
			}
			switch (show){
				case SortTypeBean.ALPHA:{
					//value = s.getAlpha();
					String valueStr = s.getMPTStatistics().get(i);
					if(m.getYear() != null)
						sb.append("<" + SortTypeBean.getMPTName(SortTypeBean.ALPHA) + m.getYear() + ">" + valueStr + "</" + SortTypeBean.getMPTName(SortTypeBean.ALPHA) + m.getYear() + ">");
					else
						sb.append("<" + SortTypeBean.getMPTName(SortTypeBean.ALPHA)+ ">" + valueStr + "</" + SortTypeBean.getMPTName(SortTypeBean.ALPHA) + ">");
					break;
				}
				case SortTypeBean.ANNULIZEDRETURN:{
					//value = s.getAR();
					String valueStr = s.getMPTStatistics().get(i);
					if(m.getYear() != null)
						sb.append("<" + SortTypeBean.getMPTName(SortTypeBean.ANNULIZEDRETURN) + m.getYear() + ">" + valueStr + "</" + SortTypeBean.getMPTName(SortTypeBean.ANNULIZEDRETURN) + m.getYear() + ">");
					else
						sb.append("<" + SortTypeBean.getMPTName(SortTypeBean.ANNULIZEDRETURN) + ">" + valueStr + "</" + SortTypeBean.getMPTName(SortTypeBean.ANNULIZEDRETURN) + ">");
					break;
				}
				case SortTypeBean.BETA:{
					//value = s.getBeta();
					String valueStr = s.getMPTStatistics().get(i);
					if(m.getYear() != null)
						sb.append("<" + SortTypeBean.getMPTName(SortTypeBean.BETA) + m.getYear() +">" + valueStr + "</" + SortTypeBean.getMPTName(SortTypeBean.BETA) + m.getYear() + ">");
					else
						sb.append("<" + SortTypeBean.getMPTName(SortTypeBean.BETA) +">" + valueStr + "</" + SortTypeBean.getMPTName(SortTypeBean.BETA) + ">");
					break;
				}
				case SortTypeBean.DRAWDOWN:{
					//value = s.getDrawDown();
					String valueStr = s.getMPTStatistics().get(i);
					if(m.getYear() != null)
						sb.append("<" + SortTypeBean.getMPTName(SortTypeBean.DRAWDOWN) + m.getYear() + ">" + valueStr + "</" + SortTypeBean.getMPTName(SortTypeBean.DRAWDOWN) + m.getYear() +">");
					else
						sb.append("<" + SortTypeBean.getMPTName(SortTypeBean.DRAWDOWN) + ">" + valueStr + "</" + SortTypeBean.getMPTName(SortTypeBean.DRAWDOWN) + ">");
					break;
				}
				case SortTypeBean.RETURN:{
					//value = s.getReturn();
					String valueStr = s.getMPTStatistics().get(i);
					if(m.getYear() != null)
						sb.append("<" + SortTypeBean.getMPTName(SortTypeBean.RETURN) + m.getYear() + ">" + valueStr + "</" + SortTypeBean.getMPTName(SortTypeBean.RETURN) + m.getYear() + ">");
					else
						sb.append("<" + SortTypeBean.getMPTName(SortTypeBean.RETURN) + ">" + valueStr + "</" + SortTypeBean.getMPTName(SortTypeBean.RETURN) + ">");
					break;
				}
				case SortTypeBean.RSQUARED:{
					//value = s.getRSquared();
					String valueStr = s.getMPTStatistics().get(i);
					if(m.getYear() != null)
						sb.append("<" + SortTypeBean.getMPTName(SortTypeBean.RSQUARED) + m.getYear() + ">" + valueStr + "</" + SortTypeBean.getMPTName(SortTypeBean.RSQUARED) + m.getYear() + ">");
					else
						sb.append("<" + SortTypeBean.getMPTName(SortTypeBean.RSQUARED) + ">" + valueStr + "</" + SortTypeBean.getMPTName(SortTypeBean.RSQUARED) + ">");

					break;
				}
				case SortTypeBean.SHARPE:{
					//value = s.getSharpeRatio();
					String valueStr = s.getMPTStatistics().get(i);
					if(m.getYear() != null)
						sb.append("<" + SortTypeBean.getMPTName(SortTypeBean.SHARPE) + m.getYear() + ">" + valueStr + "</" + SortTypeBean.getMPTName(SortTypeBean.SHARPE) + m.getYear() + ">");
					else
						sb.append("<" + SortTypeBean.getMPTName(SortTypeBean.SHARPE) + ">" + valueStr + "</" + SortTypeBean.getMPTName(SortTypeBean.SHARPE) + ">");
					break;
				}
				case SortTypeBean.STANDARDDIVIATION:{
					//value = s.getStandardDeviation();
					String valueStr = s.getMPTStatistics().get(i);
					if(m.getYear() != null)
						sb.append("<" + SortTypeBean.getMPTName(SortTypeBean.STANDARDDIVIATION) + m.getYear() + ">" + valueStr + "</" + SortTypeBean.getMPTName(SortTypeBean.STANDARDDIVIATION) + m.getYear() + ">");
					else
						sb.append("<" + SortTypeBean.getMPTName(SortTypeBean.STANDARDDIVIATION) + ">" + valueStr + "</" + SortTypeBean.getMPTName(SortTypeBean.STANDARDDIVIATION) + ">");
					break;
				}
				case SortTypeBean.TREYNOR:{
					//value = s.getTreynorRatio();
					String valueStr = s.getMPTStatistics().get(i);
					if(m.getYear() != null)
						sb.append("<" + SortTypeBean.getMPTName(SortTypeBean.TREYNOR) + m.getYear() + ">" + valueStr + "</" + SortTypeBean.getMPTName(SortTypeBean.TREYNOR) + m.getYear() + ">");
					else
						sb.append("<" + SortTypeBean.getMPTName(SortTypeBean.TREYNOR) + ">" + valueStr + "</" + SortTypeBean.getMPTName(SortTypeBean.TREYNOR) + ">");
					break;
				}
			}
		}
	}
	
/*	private void translateDates(List<MPTProperty> list){
		if(list == null || list.size() == 0)
			return;
		for(int i = 0; i < list.size(); i++){
			MPTProperty m = list.get(i);
			if(m.getChoosed() == null || m.getChoosed() == false)
				continue;
			SimpleDateFormat s = new SimpleDateFormat("MM/dd/yyyy");
			Date endDate;
			Date startingDate;
			if(m.getEndDateStr() != null){
				try {
					endDate = s.parse(m.getEndDateStr());
				} catch (Exception e) {
					// TODO: handle exception
					endDate = new Date();
				}
			}
			else
				endDate = new Date();
			if(m.getStartingDateStr() != null){
				try {
					startingDate = s.parse(m.getStartingDateStr());
				} catch (Exception e) {
					// TODO: handle exception
					startingDate = LTIDate.getLastYear(endDate);
				}
			}
			else
				startingDate = LTIDate.getLastYear(endDate);
			m.setEndDate(endDate);
			m.setStartingDate(startingDate);
		}
	}*/
	
	public static void chooseSecurities(List<Security> securities, List<SecurityMPT> securityMPTs, MPTProperty m){
		if(securities == null ||(securityMPTs != null && securityMPTs.size() != securities.size()))
			return;
		if(securityMPTs == null)
			SecurityUtil.translateSecurityToMPT(securities, securityMPTs);
		int ref = SortTypeBean.getMPT(m.getName());
		Double min = m.getMin();
		Double max = m.getMax();
		if(min == null && max == null)
			return;
		Double value;
		for(int i = 0; i < securities.size(); i++){
			Security s = securities.get(i);
			SecurityMPT mpt = securityMPTs.get(i); 
			try {
				switch(ref){
					case SortTypeBean.ALPHA:{
						value = s.getAlpha(m.getStartingDate(),m.getEndDate(),TimeUnit.DAILY);
						mpt.setAlpha(value);
						break;
					}
					case SortTypeBean.ANNULIZEDRETURN:{
						value = s.getAnnualizedReturn(m.getStartingDate(), m.getEndDate());
						mpt.setAR(value);
						break;
					}
					case SortTypeBean.BETA:{
						value = s.getBeta(m.getStartingDate(),m.getEndDate(),TimeUnit.DAILY);
						mpt.setBeta(value);
						break;
					}
					case SortTypeBean.DRAWDOWN:{
						value = s.getDrawDown(m.getStartingDate(),m.getEndDate(),TimeUnit.DAILY);
						mpt.setDrawDown(value);
						break;
					}
					case SortTypeBean.RETURN:{
						value = s.getReturn(m.getStartingDate(),m.getEndDate());
						mpt.setReturn(value);
						break;
					}
					case SortTypeBean.RSQUARED:{
						value = s.getRSquared(m.getStartingDate(), m.getEndDate(), TimeUnit.DAILY);
						mpt.setRSquared(value);
						break;
					}
					case SortTypeBean.SHARPE:{
						value = s.getSharpeRatio(m.getStartingDate(),m.getEndDate(),TimeUnit.DAILY);
						mpt.setSharpeRatio(value);
						break;
					}
					case SortTypeBean.STANDARDDIVIATION:{
						value = s.getStandardDeviation(m.getStartingDate(),m.getEndDate(),TimeUnit.DAILY);
						mpt.setStandardDeviation(value);
						break;
					}
					case SortTypeBean.TREYNOR:{
						value = s.getTreynorRatio(m.getStartingDate(),m.getEndDate(),TimeUnit.DAILY);
						mpt.setTreynorRatio(value);
						break;
					}
					default:
						value = 0.0;
						break;
				}
			} catch (Exception e) {
				// TODO: handle exception
				securities.remove(i);
				securityMPTs.remove(i);
				i--;
				continue;
			}
			
			if(value == null || (m.getMin() != null && value < m.getMin())){
				securities.remove(i);
				securityMPTs.remove(i);
				i--;
				continue;
			}
			if(value == null || (m.getMax() != null && value > m.getMax())){
				securities.remove(i);
				securityMPTs.remove(i);
				i--;
				continue;
			}
		}
	}
	
	public void setSecurityManager(SecurityManager securityManager) {
		this.securityManager = securityManager;
	}


	public Integer getSort() {
		return Sort;
	}

	public void setSort(Integer sort) {
		Sort = sort;
	}

	public String getDefaultYearStr() {
		return DefaultYearStr;
	}

	public void setDefaultYearStr(String DefaultYearStr) {
		this.DefaultYearStr = DefaultYearStr;
	}

	public Long getDefaultYear() {
		return DefaultYear;
	}

	public void setYear(Long Year) {
		this.DefaultYear = Year;
	}

	public List<SecurityMPT> getSecurityList() {
		return securityList;
	}

	public void setSecurityList(List<SecurityMPT> securityList) {
		this.securityList = securityList;
	}

	@SuppressWarnings("unchecked")
	public Map getSession() {
		return session;
	}

	@SuppressWarnings("unchecked")
	public void setSession(Map session) {
		this.session = session;
	}

	public List<MPTProperty> getMPTList() {
		return MPTList;
	}

	public void setMPTList() {
		MPTList = new ArrayList<MPTProperty>();
		MPTProperty m = new MPTProperty();
		m.setName(SortTypeBean.getMPTName(SortTypeBean.ANNULIZEDRETURN));
		MPTList.add(m);
		m = new MPTProperty();
		m.setName(SortTypeBean.getMPTName(SortTypeBean.SHARPE));
		MPTList.add(m);
		m = new MPTProperty();
		m.setName(SortTypeBean.getMPTName(SortTypeBean.ALPHA));
		MPTList.add(m);
		m = new MPTProperty();
		m.setName(SortTypeBean.getMPTName(SortTypeBean.BETA));
		MPTList.add(m);
		m = new MPTProperty();
		m.setName(SortTypeBean.getMPTName(SortTypeBean.RSQUARED));
		MPTList.add(m);
		m = new MPTProperty();
		m.setName(SortTypeBean.getMPTName(SortTypeBean.RETURN));
		MPTList.add(m);
		m = new MPTProperty();
		m.setName(SortTypeBean.getMPTName(SortTypeBean.TREYNOR));
		MPTList.add(m);
		m = new MPTProperty();
		m.setName(SortTypeBean.getMPTName(SortTypeBean.STANDARDDIVIATION));
		MPTList.add(m);
		m = new MPTProperty();
		m.setName(SortTypeBean.getMPTName(SortTypeBean.DRAWDOWN));
		MPTList.add(m);
	}
	
	public void setMPTList2()
	{
		MPTList2 = new ArrayList<MPTProperty>();
		MPTProperty m = new MPTProperty();
		m.setName(SortTypeBean.getMPTName(SortTypeBean.ANNULIZEDRETURN));
		MPTList2.add(m);
		m = new MPTProperty();
		m.setName(SortTypeBean.getMPTName(SortTypeBean.SHARPE));
		MPTList2.add(m);
		m = new MPTProperty();
		m.setName(SortTypeBean.getMPTName(SortTypeBean.ALPHA));
		MPTList2.add(m);
		m = new MPTProperty();
		m.setName(SortTypeBean.getMPTName(SortTypeBean.BETA));
		MPTList2.add(m);
		m = new MPTProperty();
		m.setName(SortTypeBean.getMPTName(SortTypeBean.RSQUARED));
		MPTList2.add(m);
		m = new MPTProperty();
		m.setName(SortTypeBean.getMPTName(SortTypeBean.RETURN));
		MPTList2.add(m);
		m = new MPTProperty();
		m.setName(SortTypeBean.getMPTName(SortTypeBean.TREYNOR));
		MPTList2.add(m);
		m = new MPTProperty();
		m.setName(SortTypeBean.getMPTName(SortTypeBean.STANDARDDIVIATION));
		MPTList2.add(m);
		m = new MPTProperty();
		m.setName(SortTypeBean.getMPTName(SortTypeBean.DRAWDOWN));
		MPTList2.add(m);
	}

	public AssetClassManager getAssetClassManager() {
		return assetClassManager;
	}

	public void setAssetClassManager(AssetClassManager assetClassManager) {
		this.assetClassManager = assetClassManager;
	}

	public java.lang.String getClassTree() {
		return classTree;
	}

	public void setClassTree(java.lang.String classTree) {
		this.classTree = classTree;
	}

	public Long getAssetClassID() {
		return assetClassID;
	}

	public void setAssetClassID(Long assetClassID) {
		this.assetClassID = assetClassID;
	}

	public void setMPTList(List<MPTProperty> list) {
		MPTList = list;
	}
	
/*	public static void main(String[] args){
		String Name = "BEGBX";
		List<Integer> Type = new ArrayList<Integer>();
		SecurityManager securityManager =(SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
		Type.add(Configuration.SECURITY_TYPE_MUTUAL_FUND);
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(SecurityMPT.class);
		detachedCriteria.add(Restrictions.in("SecurityType", Type));
		
		List<MPTProperty> MPTList = new ArrayList<MPTProperty>();
		MPTProperty m = new MPTProperty();
		m.setName(SortTypeBean.getMPTName(SortTypeBean.ANNULIZEDRETURN));
		m.setChoosed(true);
		m.setMin(0.05);
		//m.setStartingDateStr("09/01/2006");
		//m.setEndDateStr("07/28/2007");
		m.setYear(2004l);
		MPTList.add(m);
		m = new MPTProperty();
		m.setName(SortTypeBean.getMPTName(SortTypeBean.SHARPE));
		m.setChoosed(true);
		//m.setStartingDateStr("09/01/2006");
		//m.setEndDateStr("07/28/2007");
		m.setMin(0.01);
		m.setYear(2005l);
		MPTList.add(m);
		translateMPTCondition(MPTList);
		addFirstMPTCondition(detachedCriteria, MPTList);
		List<SecurityMPT> securityList = securityManager.getSecurityByMPT(detachedCriteria);
		//List<SecurityMPT> securities = new ArrayList<SecurityMPT>();
		chooseSecuritiesBySQL(securityList, MPTList);
		//translateDates(MPTList);
		
		//List<Security> sList = securityManager.getSecurities(detachedCriteria);
		
		//List<SecurityMPT> mpts = new ArrayList<SecurityMPT>();
		
		//SecurityUtil.translateSecurityToMPT(sList, mpts);
		if(securityList == null || securityList.size() == 0){
			System.out.println("no data");
		}
		else{
			for(int i = 0; i < MPTList.size(); i++){
				m = MPTList.get(i);
				chooseSecurities(sList, mpts, m);
			}
			for(int i = 0; i < securityList.size(); i++){
				SecurityMPT mpt = securityList.get(i);
				System.out.println(mpt.getSymbol() + "  " + mpt.getSecurityType() + "  " + mpt.getAR() + "  " + mpt.getAlpha());
			}
			System.out.println(securityList.size());
			//System.out.println(sList.size());
		}
		//assetClassID = 1l;
	}*/

	public List<MPTProperty> getMPTList2() {
		return MPTList2;
	}

	public void setMPTList2(List<MPTProperty> list2) {
		MPTList2 = list2;
	}

	public Boolean getIsUseAssetClass() {
		return isUseAssetClass;
	}

	public void setIsUseAssetClass(Boolean isUseAssetClass) {
		this.isUseAssetClass = isUseAssetClass;
	}

	public BLExtraAttr getExtraAttrs() {
		return ExtraAttrs;
	}

	public void setExtraAttrs(BLExtraAttr extraAttrs) {
		ExtraAttrs = extraAttrs;
	}

	public List<Integer> getAdvancedType() {
		return AdvancedType;
	}

	public void setAdvancedType(List<Integer> advancedType) {
		AdvancedType = advancedType;
	}

	public void setBasicType(List<Integer> basicType) {
		BasicType = basicType;
	}

	public List<SecurityType> getTypeList() {
		return TypeList;
	}

	public void setTypeList(List<SecurityType> typeList) {
		TypeList = typeList;
	}

	public Map<String, Double> getExtraAttrValue() {
		return ExtraAttrValue;
	}

	public void setExtraAttrValue(Map<String, Double> extraAttrValue) {
		ExtraAttrValue = extraAttrValue;
	}

	public List<MPTProperty> getShowList() {
		return showList;
	}

	public void setShowList(List<MPTProperty> showList) {
		this.showList = showList;
	}

}
