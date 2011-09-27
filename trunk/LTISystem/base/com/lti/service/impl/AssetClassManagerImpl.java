package com.lti.service.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.lti.service.AssetClassManager;
import com.lti.service.SecurityManager;
import com.lti.service.bo.AssetClass;
import com.lti.service.bo.Security;
import com.lti.system.ContextHolder;
import com.lti.type.Menu;
import com.lti.util.Sort;
import com.lti.util.StringUtil;
import com.lti.util.html.ParseHtmlTable;


public class AssetClassManagerImpl extends DAOManagerImpl implements com.lti.service.AssetClassManager, Serializable{
	
	
	private static final long serialVersionUID = 1L;
	

	@Override
	public void remove(long id){
		Object obj = getHibernateTemplate().get(com.lti.service.bo.AssetClass.class, id);
		getHibernateTemplate().delete(obj);
	}

	@Override
	public AssetClass get(Long id) {
		//return (AssetClass) getHibernateTemplate().get(AssetClass.class, id);
		Session s=null;
		try {
			if (id == null)
				return null;
			s=getHibernateTemplate().getSessionFactory().openSession();
			AssetClass ac=(AssetClass) s.get(AssetClass.class, id);
			return ac;
			//return (Portfolio) getHibernateTemplate().get(Portfolio.class, id);
		} catch (RuntimeException e) {
			System.out.println(StringUtil.getStackTraceString(e));
			throw e;
		}finally{
			if(s!=null&&s.isOpen())s.close();
			//long t2=System.currentTimeMillis();
			//System.out.println("read: "+(t2-t1));
		}
	}

	
	@Override
	public Long save(AssetClass assetclass) {
		getHibernateTemplate().save(assetclass);
		return assetclass.getID();
	}

	
	@Override
	public void saveOrUpdate(AssetClass assetclass) {
		getHibernateTemplate().saveOrUpdate(assetclass);
	}

	
	@Override
	public void update(AssetClass assetclass) {
		List<String> acs=this.getAbsoluteClassName(assetclass.getID());
		
		getHibernateTemplate().update(assetclass);
		
	}

	
	@Override
	public void delete(long id) {
		
		Stack<AssetClass> stack=new Stack<AssetClass>();
		
		AssetClass assetClass=this.get(id);
		
		if(assetClass==null)return;
		
		List<String> acs=this.getAbsoluteClassName(id);
		
		stack.push(this.get(id));
		
		while(!stack.empty()){
			
			AssetClass top=stack.pop();
			
			List<AssetClass> list=this.getChildClass(top.getID());
			
			if(list!=null&&list.size()!=0){
				
				stack.push(top);
				
				stack.addAll(list);
				
			}
			
			else{
				
				this.remove(top.getID());
				
			}
		}
		
		
	}
	
	
	@Override
	public Long add(AssetClass assetclass) {
		
		Long id=this.save(assetclass);
		
		return id;
	}
	
	@Override
	public List<com.lti.service.bo.AssetClass> getChildClass(long id) {
		
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(AssetClass.class); 
		
		detachedCriteria.add(Restrictions.eq("ParentID", id));
		
		return findByCriteria(detachedCriteria);
		
	}
	/**
	 * @author CCD
	 * @param id the id of the assetclass
	 * @return return the parent assetclass of whose ID is id just below root
	 */
	@Override
	public AssetClass getFirstParentClass(long id)
	{
		AssetClass curAssetClass = get(id);
		long parentID;
		if(curAssetClass != null)
		{
			parentID = curAssetClass.getParentID();
			while(parentID != 0L)
			{
				curAssetClass =  get(parentID);
				parentID = curAssetClass.getParentID();
			}
		}
		return curAssetClass;
	}
	
	@Override
	public boolean isUpperOrSameClass(String parentclassname,String curclassname){
		AssetClass son = this.get(curclassname);
		AssetClass parent = this.get(parentclassname);
		long  parentID;
		if(son != null && parent != null)
		{
			parentID = son.getID();
			while(parentID != 0L && parentID != parent.getID())
			{
				son = get(son.getParentID());
				parentID = son.getID();
			}
			if(parentID == parent.getID())
				return true;
		}
		return false;
	}
	/**
	 * @author CCD
	 * @param name the assetclassname of the assetclass
	 * @return return the parent assetclass of whose assetname is name just below root
	 */
	@Override
	public AssetClass getFirstParentClass(String name)
	{
		return getFirstParentClass(this.get(name).getID());
	}
	@Override
	public com.lti.service.bo.AssetClass getParent(long id) {
		
		AssetClass self=this.get(id);
		
		AssetClass parent=this.get(self.getParentID());
		
		return parent;
		
	}
	
	@Override
	public void addChildClass(long parentID, com.lti.service.bo.AssetClass child) {
		
		AssetClass entity=new AssetClass();
		
		com.lti.util.CopyUtil.Translate(child, entity);
		
		entity.setParentID(parentID);
		
		this.save(entity);
		
	}

	
	@Override
	public com.lti.service.bo.AssetClass get(String name) {
		
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(AssetClass.class); 
		
		detachedCriteria.add(Restrictions.eq("Name", name));
		
		List<AssetClass> acs= findByCriteria(detachedCriteria);
		
		if(acs.size()>=1)return acs.get(0);
		
		else return null;
	}
	
	@Override
	public AssetClass getChildClass(String Name, long parent) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(AssetClass.class); 
		
		detachedCriteria.add(Restrictions.eq("Name", Name));
		
		detachedCriteria.add(Restrictions.eq("ParentID", parent));
		
		List<AssetClass> acs= findByCriteria(detachedCriteria);
		
		if(acs.size()>=1)return acs.get(0);
		
		else return null;
	}

	public boolean isParentAssetClass(AssetClass parentAssetClass,AssetClass childAssetClass){

		long curParentID = parentAssetClass.getID();
		long parentID = childAssetClass.getParentID();
		if(curParentID == parentID){
			return true;
		}else
			return false;
	}
	
	
	@Override
	public List<AssetClass> getClasses() {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(AssetClass.class); 
		
		List<AssetClass> acs= findByCriteria(detachedCriteria);
		
		return acs;
	}
	
	@Override
	public List<String> getAbsoluteClassName(long id){
		
		if(id==0l)return null;
		
		List<String> returnList=new ArrayList<String>();
		
		Stack<String> stack=new Stack<String>();
		
		try {
			
			AssetClass ac=this.get(id);
			if(ac==null)return null;
			
			stack.push(ac.getName());
			
			while(ac.getParentID()!=0){
				ac=this.get(ac.getParentID());
				//avoid circle
				if(stack.indexOf(ac.getName())!=-1)return null;
				stack.push(ac.getName());
			}
			
			String className="";
			while(!stack.empty()){
				className=stack.pop();
				returnList.add(className);
			}
			
			className=className.substring(0, className.length()-2);
			
			return returnList;
			
		} catch (RuntimeException e) {
			
			//e.printStackTrace();
			
			return null;
			
		}
	}
	
	public List<AssetClass> getParentAssetClassList(List<AssetClass> childAsstClassList){
		List<AssetClass> lists = new ArrayList<AssetClass>();
		for(AssetClass ac: childAsstClassList){
			long id = ac.getParentID();
			if(id!=0l){
			   AssetClass parentAssetClass = get(id);
			   if(parentAssetClass!=null && !lists.contains(parentAssetClass)){
				   lists.add(parentAssetClass);
			   }
			}
		}
		return lists;
	}
	
	 public Menu getMenuByAssetList(List<AssetClass>assetClassList,Integer id){ 
		 Menu menu = new Menu();
		 AssetClass ac = this.get(id.longValue());
		 if(ac == null)return null;
		 if(assetClassList == null && assetClassList.size()==0)
			 return null;
		 
		 menu.setText(ac.getName());
		 menu.setId(id);
		 List<AssetClass> acs = new ArrayList<AssetClass>();
		 for(AssetClass a:assetClassList){
			 boolean result = isParentAssetClass(ac, a);
			 if(result){
				 acs.add(a);
			 }
		 }
		 if(acs==null||acs.size()==0){
			 menu.setLeaf(true);			 
		 }else{
			 List<Menu> menus = new ArrayList<Menu>();
			 menu.setLeaf(false);
			 menu.setExpanded(true);
			 for(int i=0;i<acs.size();i++){
				 Menu m = this.getMenuByAssetList(assetClassList, acs.get(i).getID().intValue());
				 if(m!=null)menus.add(m);
			 }
			 menus = Sort.menuSort(menus);
			 menu.setChildren(menus);
		 }
		 
		 return menu;
	 }
	
	private List<Integer> ids;
	
	private Menu getMenu(Integer id,String url,String para){
		Menu menu=new Menu();
		AssetClass ac=this.get(id.longValue());
		if(ac==null)return null;
		
		if(ids.contains(ac.getID().intValue()))return null;
		else ids.add(ac.getID().intValue());
		
		menu.setText(ac.getName());
		menu.setId(id);
		menu.setHref(url+para+"="+menu.getId());
		List<AssetClass> acs=this.getChildClass(id.longValue());
		if(acs==null||acs.size()==0){
			menu.setLeaf(true);
			menu.setCls("file");
			
		}else{
			List<Menu> menus=new ArrayList<Menu>();
			menu.setLeaf(false);
			menu.setExpanded(true);
			menu.setCls("folder");
			for(int i=0;i<acs.size();i++){
				Menu m=this.getMenu(acs.get(i).getID().intValue(),url,para);
				if(m!=null)menus.add(m);
			}
			menu.setChildren(menus);
		}
		
		return menu;
	}
	@Override
	public synchronized Menu getMenu(String url,String para){
		if(url==null&&url.length()<=1)return null;
		if(url.indexOf('?')!=-1)url=url+"&";
		else url=url+"?";
		
		
		ids=new ArrayList<Integer>();
		ids.add(0);
		Menu menu=new Menu();
		menu.setId(0);
		
		menu.setLeaf(false);
		menu.setCls("folder");
		menu.setText("Asset Class");
		menu.setExpanded(true);
		List<AssetClass> acs=this.getChildClass(0l);
		if(acs!=null){
			List<Menu> menus=new ArrayList<Menu>();
			for(int i=0;i<acs.size();i++){
				Menu m=this.getMenu(acs.get(i).getID().intValue(),url,para);
				if(m!=null)menus.add(m);
			}
			menu.setChildren(menus);
		}
		return menu;
	}
	
	private static void print(Menu m,String pre){
		if(!m.isLeaf()){
			System.out.println(pre+m.getId()+": "+m.getText());
			List<Menu> ms=m.getChildren();
			if(ms!=null&&ms.size()>0){
				for(int i=0;i<ms.size();i++){
					print(ms.get(i),"\t"+pre);
				}
			}
		}
	}
	public HashSet<String> getAvailableAssetClassSet(List<String> availableAssetClassList){
		HashSet<String> availableAssetClassSet = new HashSet<String>();
		for(int i=0;i<availableAssetClassList.size();++i){
			String assetClassName = availableAssetClassList.get(i);
			AssetClass assetClass = this.get(assetClassName);
			if(assetClass!=null)
				availableAssetClassSet.add(assetClassName);
		}
		return availableAssetClassSet;
	}
	/**
	 * @author CCD
	 * @param availableAssetClassMap
	 * @param availableFundList
	 * @param hasHighYieldBond
	 * @return
	 */
	public HashMap<String,List<String>> getAvailableAssetClassSet(HashSet<String> availableAssetClassSet ,List<String> availableFundList, boolean hasHighYieldBond, boolean hasLongTermBond){
		SecurityManager securityManager =  (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
		HashMap<String,List<String>> availableAssetClassFundMap = new HashMap<String,List<String>>();
		for(int i=0;i<availableFundList.size();++i){
			String symbol = availableFundList.get(i);
			Security security = securityManager.getBySymbol(symbol);
			if(security==null || security.getClassID()==null)
				continue;
			AssetClass assetClass = get(security.getClassID());
			String assetClassName = assetClass.getName();
			String lowerCaseName = assetClassName.toLowerCase();
			if(symbol.equalsIgnoreCase("CASH")){
				assetClassName = "CASH";
			}else if(assetClassName.equalsIgnoreCase("EQUITY")){
				assetClassName = "US EQUITY";
			}else if(hasLongTermBond && lowerCaseName.contains("long") && !assetClassName.equalsIgnoreCase("Long-Short")){
				assetClassName="Long-Term Bond";
			}else if(hasHighYieldBond && lowerCaseName.contains("high yield")){
				assetClassName = "High Yield Bond";
			}else{
				while(!availableAssetClassSet.contains(assetClassName) && !assetClassName.equalsIgnoreCase("ROOT"))
				{
					assetClass = get(assetClass.getParentID());
					assetClassName = assetClass.getName();
					lowerCaseName = assetClassName.toLowerCase();
					if(hasLongTermBond && lowerCaseName.contains("long") && !assetClassName.equalsIgnoreCase("Long-Short")){
						assetClassName="Long-Term Bond";
						break;
					}else if(hasHighYieldBond && lowerCaseName.contains("high yield")){
						assetClassName = "High Yield Bond";
						break;
					}
				}
			}
			if(availableAssetClassSet.contains(assetClassName) || assetClassName.equals("CASH")){
				List<String> availFundList = availableAssetClassFundMap.get(assetClassName);
				if(availFundList != null)
					availFundList.add(security.getSymbol());
				else{
					availFundList = new ArrayList<String>();
					availFundList.add(security.getSymbol());
					availableAssetClassFundMap.put(assetClassName, availFundList);
				}
			}
		}
		return availableAssetClassFundMap;
	}
	/**
	 * @author CCD
	 */
	public HashMap<String,List<String>> getAvailableAssetClassSet(List<String> availableAssetClassList,List<String> availableFundList){
		return getAvailableAssetClassSet(availableAssetClassList, availableFundList, false);
	}
	/**
	 * @author CCD
	 * @param availableAssetClassList
	 * @param availableFundList
	 * @return a map, of which the key is an available assetClass, and the value of it is a list of available fund
	 * added on 2009/12/12 and tested
	 */
	public HashMap<String,List<String>> getAvailableAssetClassSet(List<String> availableAssetClassList,List<String> availableFundList, boolean longTermFlag){
		SecurityManager securityManager =  (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
		HashMap<String,List<String>> availableAssetClassFundMap = new HashMap<String,List<String>>();
		HashMap<String,Boolean> availableAssetClassMap = new HashMap<String,Boolean>();
		if(availableAssetClassList!=null && availableAssetClassList.size()==1 && availableAssetClassList.get(0).equalsIgnoreCase("ROOT")){
			availableAssetClassFundMap.put("ROOT", availableFundList);
			return availableAssetClassFundMap;
		}
		boolean hasHighYieldBond=false;
		boolean hasLongTermBond=false;
		for(int i=0;i<availableAssetClassList.size();++i){
			String assetClassName = availableAssetClassList.get(i);
			AssetClass assetClass = this.get(assetClassName);
			if(assetClass!=null)
			{
				if(assetClassName.equalsIgnoreCase("High Yield Bond"))
					hasHighYieldBond = true;
				if(longTermFlag){
					if(assetClassName.equalsIgnoreCase("Long-Term Bond"))
						hasLongTermBond = true;
				}
				availableAssetClassMap.put(assetClass.getName(),Boolean.TRUE);
			}
		}
		for(int i=0;i<availableFundList.size();++i){
			String symbol = availableFundList.get(i);
			Security security = securityManager.getBySymbol(symbol);
			if(security==null || security.getClassID()==null)
				continue;
			AssetClass assetClass = get(security.getClassID());
			String assetClassName = assetClass.getName();
			String lowerCaseName = assetClassName.toLowerCase();
			if(symbol.equalsIgnoreCase("CASH")){
				assetClassName = "CASH";
			}else if(assetClassName.equalsIgnoreCase("EQUITY")){
				assetClassName = "US EQUITY";
			}else if(hasLongTermBond && lowerCaseName.contains("long") && !assetClassName.equalsIgnoreCase("Long-Short")){
				assetClassName="Long-Term Bond";
			}else if(hasHighYieldBond && lowerCaseName.contains("high yield")){
				assetClassName = "High Yield Bond";
			}else{
				while(!availableAssetClassMap.containsKey(assetClassName) && !assetClassName.equalsIgnoreCase("ROOT"))
				{
					assetClass = get(assetClass.getParentID());
					assetClassName = assetClass.getName();
					lowerCaseName = assetClassName.toLowerCase();
					if(hasLongTermBond && lowerCaseName.contains("long") && !assetClassName.equalsIgnoreCase("Long-Short")){
						assetClassName="Long-Term Bond";
						break;
					}else if(hasHighYieldBond && lowerCaseName.contains("high yield")){
						assetClassName = "High Yield Bond";
						break;
					}
				}
			}
			if(availableAssetClassMap.containsKey(assetClassName) || assetClassName.equals("CASH")){
				List<String> availFundList = availableAssetClassFundMap.get(assetClassName);
				if(availFundList != null)
					availFundList.add(security.getSymbol());
				else{
					availFundList = new ArrayList<String>();
					availFundList.add(security.getSymbol());
					availableAssetClassFundMap.put(assetClassName, availFundList);
				}
			}
		}
		return availableAssetClassFundMap;
	}
	
	public HashMap<String,List<String>> getAvailableAssetClassSet(String[] assetarr, List<String> candidateList, boolean longTermFlag, boolean AllowSectorFund){
	    AssetClassManager assetClassManager = ContextHolder.getAssetClassManager();
	    SecurityManager securityManager = ContextHolder.getSecurityManager();
	    HashMap<String, List<String>> availableAssetClassFundMap = new HashMap<String, List<String>>();
	    HashMap<String, Boolean> availableAssetClassMap = new HashMap<String, Boolean>();
	    if (assetarr != null && assetarr.length == 1 && assetarr[0].equalsIgnoreCase("ROOT")) {
	      
	      availableAssetClassFundMap.put("ROOT", candidateList);
	      return availableAssetClassFundMap;
	    }
	    boolean hasHighYieldBond = false;
	    boolean hasLongTermBond = false;
	    for (int i = 0; i < assetarr.length; ++i) {
	      String assetClassName = assetarr[i];
	      AssetClass assetClass = assetClassManager.get(assetClassName);
	      if (assetClass != null) {
	        if (assetClassName.equalsIgnoreCase("High Yield Bond"))
	          hasHighYieldBond = true;
	        if (longTermFlag) {
	          if (assetClassName.equalsIgnoreCase("Long-Term Bond"))
	            hasLongTermBond = true;
	        }
	        availableAssetClassMap.put(assetClass.getName(), Boolean.TRUE);
	      }
	    }
	   
	    for(String symbol:candidateList) {
	     // CandidateFundType candidateFundType = candidatemap.get(symbol);
	      Security security = securityManager.getBySymbol(symbol);
	      if (security == null || security.getClassID() == null)
	        continue;
	      AssetClass assetClass = security.getAssetClass();

	      String assetClassName = assetClass.getName();
	      String lowerCaseName = assetClassName.toLowerCase();
	      if (symbol.equalsIgnoreCase("CASH")) {
	        assetClassName = "CASH";
	      } else if (assetClassName.equalsIgnoreCase("EQUITY")) {
	        assetClassName = "US EQUITY";
	      } else if (hasLongTermBond && lowerCaseName.contains("long") && !assetClassName.equalsIgnoreCase("Long-Short")) {
	        assetClassName = "Long-Term Bond";
	      } else if (hasHighYieldBond && lowerCaseName.contains("high yield")) {
	        assetClassName = "High Yield Bond";
	      } else {
	        while (!availableAssetClassMap.containsKey(assetClassName) && !assetClassName.equalsIgnoreCase("ROOT")) {
	          assetClass = assetClassManager.get(assetClass.getParentID());
	          assetClassName = assetClass.getName();
	          lowerCaseName = assetClassName.toLowerCase();
	          if (hasLongTermBond && lowerCaseName.contains("long") && !assetClassName.equalsIgnoreCase("Long-Short")) {
	            assetClassName = "Long-Term Bond";
	            break;
	          } else if (hasHighYieldBond && lowerCaseName.contains("high yield")) {
	            assetClassName = "High Yield Bond";
	            break;
	          }else if (assetClassName.equalsIgnoreCase("SECTOR EQUITY")) {
	            break;
	          }
	        }
	      }
	      if(AllowSectorFund && assetClassName.equalsIgnoreCase("SECTOR EQUITY"))      //Added on 04/25/2011 by WYJ, treat Sector funds as US Equity
	        assetClassName = "US EQUITY";
	      if (availableAssetClassMap.containsKey(assetClassName) || assetClassName.equals("CASH")) {
	        List<String> availFundList = availableAssetClassFundMap.get(assetClassName);
	        if (availFundList != null)
	          availFundList.add(symbol);
	        else {
	          availFundList = new ArrayList<String>();
	          availFundList.add(symbol);
	          availableAssetClassFundMap.put(assetClassName, availFundList);
	        }
	      }
	    }
	    return availableAssetClassFundMap;
}
	/**
	 * @author CCD
	 * @param symbol the fund's symbol
	 * @return return the asset class name(category) for the mutualfund or ETF with symbol from yahoo
	 * added by CCD on 2010-01-18
	 */
	public String getAssetClassNameForMutualFundAndETF(String symbol){
		String html="html";
		int num=0;
		try{
			while(html.equals("html")&&num<=30){
				html=ParseHtmlTable.getHtml("http://finance.yahoo.com/q/pr?s="+symbol);
				num++;
				if(html==null) html="html";
			}
			html=ParseHtmlTable.cleanHtml(html);
			int c = ParseHtmlTable.countTable(html);
			int flag = 0;
			for(int i=0;i<c;i++){
				String tmp = ParseHtmlTable.getTable(html, i);
				if(tmp!=null&&tmp.contains("CATEGORY:")){
					flag=i;
					break;
				}
			}		
			String targetTable=ParseHtmlTable.getTable(html, flag);
			String result=ParseHtmlTable.extractTableContent(targetTable.toCharArray(), 0, targetTable.length()-1,200,1,200);
			String assetClassName = result.split("#")[1];
			return assetClassName;
		}catch(Exception e){
			return null;
		}
	}
	/**
	 * @author CCD
	 * @param symbol the CEF's symbol
	 * @return return the asset class name(category) for the CEF with symbol from morningstar
	 * added by CCD on 2010-01-18
	 */
	public String getAssetClassNameForCEF(String symbol){
		String html="html";
		int num=0;
		try{
			while(html.equals("html")&&num<=30){
				html=ParseHtmlTable.getHtml("http://quote.morningstar.com/cef/f.aspx?t="+symbol);
				num++;
				if(html==null) html="html";
			}
			int startIndex = html.indexOf("Name",0)+7;
			int endIndex = html.indexOf("\"", startIndex);
			
			
			//assetClassName.toCharArray();
			char[] c = html.substring(startIndex, endIndex).toCharArray();
			String assetClassName="";
			for(int i=0;i<c.length;++i){
				if(c[i]!='\\')
					assetClassName += c[i];
			}
			assetClassName.replace("\\", "");
			return assetClassName;
		}catch(Exception e){
			return null;
		}
	}
	/**
	 * @author CCD
	 * update all the mutual funds and ETFs 's asset class according to they category from yahoo
	 * added by CCD on 2010-01-16
	 */
	public void updateAssetClassForAllFunds(){
		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Security.class);
		detachedCriteria.add(Restrictions.or(Restrictions.or(Restrictions.eq("SecurityType", 1), Restrictions.eq("SecurityType", 4)),Restrictions.eq("SecurityType",2)));
		List<Security> securityList = securityManager.getSecurities(detachedCriteria);
		List<AssetClass> newAssetClassList = new ArrayList<AssetClass>();
		List<Security> updateSecurityList = new ArrayList<Security>();
		if(securityList!=null && securityList.size()>0){
			System.out.println(securityList.size());
			for(int i=0;i<securityList.size();++i){
				System.out.println(i);
				//System.out.println(i);
				Security security = securityList.get(i);
				String symbol = security.getSymbol();
				String assetClassName;
				if(security.getSecurityType()==2)
					assetClassName = this.getAssetClassNameForCEF(symbol);
				else
					assetClassName = this.getAssetClassNameForMutualFundAndETF(symbol);
				if(assetClassName!=null){
					AssetClass ac = this.get(security.getClassID());
					if(assetClassName.equalsIgnoreCase("N/A"))
						continue;
					if(ac==null || !assetClassName.equalsIgnoreCase(ac.getName())){
						//check if the assetclassname exists in our database
						
						AssetClass yahooAC = this.get(assetClassName);
						if(yahooAC==null){
							//create the new asset class and set it for the fund
							System.out.println(symbol+"   "+assetClassName);
							yahooAC = new AssetClass();
							yahooAC.setName(assetClassName);
							yahooAC.setParentID(0L);// just below ROOT
							this.save(yahooAC);
							yahooAC = this.get(assetClassName);
						}
						security.setClassID(yahooAC.getID());
						updateSecurityList.add(security);
					}
				}
			}
			securityManager.saveOrUpdateAllSecurity(updateSecurityList);
		}
	}
	/**
	 * @author CCD
	 * merge a asset class with ID equals originalID to the asset class with ID equals targetID
	 * added by CCD on 2010-01-18
	 */
	public void mergeAssetClass(Long originalID, Long targetID){
		try {
			String[] updateSQL = new String[4];
			//update asset table
			updateSQL[0] = "update ltisystem_asset set CLASSID = "+ targetID + " where CLASSID=" + originalID;
			//update security table
			updateSQL[1] = "update ltisystem_security set ASSETCLASSID = "+ targetID + " where ASSETCLASSID=" + originalID;
			//update mpt table
			updateSQL[2] = "update ltisystem_securitympt set AssetClassID = "+ targetID + " where AssetClassID=" + originalID;
			//update assetclass table
			updateSQL[3] = "delete from ltisystem_assetclass where ID = " + originalID;
			List<String> SQLList = new ArrayList<String>();
			for(int i=0;i<4;++i)
				SQLList.add(updateSQL[i]);
			this.executeSQL(SQLList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * change asset class with ID equals originalID 's PARENTID to parentID
	 * @author CCD
	 * @param originalID
	 * @param parentID
	 * added by CCD on 2010-01-18
	 */
	public void catalogueAssetClass(Long originalID,Long parentID){
		try {
			String updateSQL = "update ltisystem_assetclass set PARENTID = "+ parentID + " where ID=" + originalID;
			this.executeSQL(updateSQL);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @author CCD
	 * @param fileName the absolute fileName
	 * update the benchmark for each asset class
	 * added by CCD on 2010-01-18
	 */
	public void updateBenchmarkForAssetClass(String fileName){
		try {
			SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
			POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(fileName));
			HSSFWorkbook wb=new HSSFWorkbook(fs);
			//Choosing the work sheet "stock Data".
			HSSFSheet sheet= wb.getSheet("BenchMark");
			boolean start=false;
			String assetClassName = null,symbol =null;
			System.out.println("start");
			for (Iterator<HSSFRow> rit = (Iterator<HSSFRow>)sheet.rowIterator(); rit.hasNext(); ) {
				HSSFRow row = rit.next();
				try{
					assetClassName=null;
					symbol=null;
					if(row.getCell(0)!=null)
						assetClassName = row.getCell(0).toString();
					if(row.getCell(1)!=null)
						symbol = row.getCell(1).toString();
					AssetClass assetClass= this.get(assetClassName);
					Security security = securityManager.getBySymbol(symbol);
					if(assetClass!=null){
						if(security!=null)
							assetClass.setBenchmarkID(security.getID());
						else
							assetClass.setBenchmarkID(null);
						this.update(assetClass);
					}
					System.out.println("Success: " + assetClassName + "   "+ symbol);
				}catch(Exception e){
					System.out.println("Fail: " + assetClassName + "   " + symbol);
					continue;
				}
			}
			System.out.println("finsish");
		}catch (FileNotFoundException e) {
			//System.out.println("File not found");
		} catch (IOException e) {
			e.printStackTrace();
		}   
	}
	public static void main(String[] args){
		com.lti.service.AssetClassManager am=ContextHolder.getAssetClassManager();
		List<String> availableAssetClassList = new ArrayList<String>();
		availableAssetClassList.add("US EQUITY");
		//availableAssetClassList.add("Long-Term Bond");
		availableAssetClassList.add("Long-Short");
		//availableAssetClassList.add("Muni New York Long");
		availableAssetClassList.add("FIXED INCOME");
		List<String> availableFundList = new ArrayList<String>();
		availableFundList.add("VTSMX");
		availableFundList.add("CASH");
		availableFundList.add("MHN");//long
		availableFundList.add("RAA");//long
		availableFundList.add("BNA");//long term
		availableFundList.add("OLA");//long-short
		HashMap<String,List<String>> hm = am.getAvailableAssetClassSet(availableAssetClassList, availableFundList);
		if(hm!=null){
			Set<String> ss = hm.keySet();
			Iterator it= ss.iterator();
			while(it.hasNext()){
				String key = (String) it.next();
				System.out.println(key);
				System.out.println(hm.get(key).toString());
			}
		}
		//am.getAssetClassNameForFund("FCNKX");
		//am.getAssetClassNameForCEF("RQI");
		//am.mergeAssetClass(259L, 257L);
		//am.updateAssetClassForAllFunds();
		//am.getAssetClassNameForMutualFundAndETF("SCHC");
		//am.updateBenchmarkForAssetClass("E:\\BenchMark0125.xls");
		//System.out.println(am.getAssetClassNameForMutualFundAndETF("GDX"));
//		am.updateAssetClassForAllMutualFundsAndETFs();
//		System.out.println(am.getFirstParentClass("INVESTMENT GRADE").getID());
//		System.out.println(am.isUpperOrSameClass("BALANCE FUND", "Moderate Allocation"));
//		System.out.println(am.isUpperOrSameClass("hybrid ASSETS", "moDerate Allocation"));
//		System.out.println(am.isUpperOrSameClass("Moderate Allocation", "Moderate Allocation"));
//		System.out.println(am.isUpperOrSameClass("US TREASURY BONDS", "Moderate Allocation"));
	
		//Menu u=am.getMenu("", "");
		//print(u,"");
	}

	
}
	