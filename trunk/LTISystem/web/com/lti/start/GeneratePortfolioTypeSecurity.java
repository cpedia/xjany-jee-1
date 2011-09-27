package com.lti.start;
import java.util.List;

import com.lti.service.PortfolioManager;
import com.lti.service.SecurityManager;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.Security;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;


public class GeneratePortfolioTypeSecurity {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PortfolioManager pm=ContextHolder.getPortfolioManager();
		SecurityManager sm=ContextHolder.getSecurityManager();
		List<Security> securities=sm.getSecuritiesByType(Configuration.SECURITY_TYPE_PORTFOLIO);
		if(securities!=null){
			//for(int i=0;i<securities.size();i++){
			//	try {
			//		sm.delete(securities.get(i).getID());
			//	} catch (RuntimeException e) {
			//		// TODO Auto-generated catch block
			//		e.printStackTrace();
			//	}
			//}
		}
		
		List<Portfolio> ps=pm.getCurrentPortfolios();
		
		for(int i=0;i<ps.size();i++){
			//Portfolio p=ps.get(i);
			//pm.updateSymbol(p);
			//System.out.println(p.getName());
		}
	}

}
