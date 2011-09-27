package com.lti.action.betagain;

import java.util.List;

import com.lti.action.Action;
import com.lti.service.AssetClassManager;
import com.lti.service.bo.AssetClass;
import com.lti.service.bo.Security;
import com.lti.service.SecurityManager;
import com.lti.system.ContextHolder;
import com.lti.util.LTIFactorManager;
import com.lti.util.LTIFactorManager.Factor;
import com.opensymphony.xwork2.ActionSupport;
/**
 * get the factor for each fund
 * @author CCD
 *
 */
public class GetFactorListAction extends ActionSupport implements Action{
	private static final long serialVersionUID = 1342L;
	private String Symbol;
	private String factorList;
	
	public String getSymbol() {
		return this.Symbol;
	}
	public void setSymbol(String Symbol) {
		this.Symbol = Symbol;
	}
	public String getFactorList() {
		return factorList;
	}
	public void setFactorList(String factorList) {
		this.factorList = factorList;
	}
	
	/**
	 * get the factors according to the symbol, result formatted as "f1.name,f1.type#f2.name,f2.type#"
	 * 
	 */
	public String execute() throws Exception {
		AssetClassManager assetClassManager=(AssetClassManager) ContextHolder.getInstance().getApplicationContext().getBean("assetClassManager");
		SecurityManager securityManager = (SecurityManager)ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
		AssetClass ac=null;
		Security s=null;
		LTIFactorManager lfm=LTIFactorManager.getInstance("RAA");
		s=securityManager.get(Symbol);
		ac=assetClassManager.get(s.getClassID());
		List<Factor> list=lfm.getFactorsForSecurity(Symbol);
		factorList="";
		Factor factor;
		for(int i=0;i<list.size();++i)
		{
			factor=list.get(i);
			if(factor!=null)
				factorList+=factor.getName()+","+factor.getType()+"#";
		}
		return  Action.SUCCESS;
	}
	
	public static void  main(String[] args) throws Exception
	{
		GetFactorListAction gf=new GetFactorListAction();
		gf.setSymbol("spy");
		gf.execute();
		System.out.println(gf.getFactorList());
		
	}


	
}
