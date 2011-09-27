/**
 * 
 */
package com.lti.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.supercsv.io.CsvListWriter;
import org.supercsv.prefs.CsvPreference;

import com.lti.system.ContextHolder;
import com.lti.system.Configuration;
import com.lti.service.PortfolioManager;
import com.lti.service.SecurityManager;
import com.lti.service.bo.Security;
import com.lti.service.bo.SecurityDailyData;
/**
 * @author CCD
 *
 */
public class SecurityPriceUtil {

	public static void setDelta(double d){
		delta = d;
	}
	
	private static double delta = 0.15;
	
	public static List<Security> getFunds(){
		SecurityManager sm = (SecurityManager) ContextHolder.getSecurityManager();
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Security.class);
		detachedCriteria.add(Restrictions.lt("SecurityType", Configuration.SECURITY_TYPE_PORTFOLIO));
		return sm.getSecurities(detachedCriteria);
	}
	
	/**
	 * 返回一个Map,出现crash的securityID和crash日期
	 * @return
	 * @throws IOException 
	 */
	public static void checkSecurityCrash() throws IOException{
		File file = new File("a.csv");
		String[] header = {"ID","SYMBOL","TYPE","DATE","DELTA","DESCRIPTION"};
		CsvListWriter clw = new CsvListWriter(new FileWriter(file), CsvPreference.EXCEL_PREFERENCE);
		clw.writeHeader(header);
		SecurityManager sm = (SecurityManager) ContextHolder.getSecurityManager();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		List<Security> securityList = getFunds();
		int count = 0;
		for(Security se: securityList){
			++count;
			System.out.println(se.getSymbol() + " " + count);
			if(se != null && se.getEndDate() != null){
				List<SecurityDailyData> sdds = sm.getDailydatas(se.getID());
				if(sdds != null && sdds.size() > 0){
					List<String> content = new ArrayList<String>();
					if(sdds.get(0).getAdjClose() == null){
						content.add(se.getID().toString());
						content.add(se.getSymbol());
						content.add(se.getSecurityType().toString());
						content.add(df.format(sdds.get(0).getDate()));
						content.add(String.valueOf(0));
						content.add("EMPTY");
						clw.write(content);
					}
					else{
						double prePrice = sdds.get(0).getAdjClose();
						for(int i=1;i<sdds.size();++i){
							if(sdds.get(i).getAdjClose() == null){
								content.add(se.getID().toString());
								content.add(se.getSymbol());
								content.add(se.getSecurityType().toString());
								content.add(df.format(sdds.get(i).getDate()));
								content.add(String.valueOf(0));
								content.add("EMPTY");
								clw.write(content);
								break;
							}
							double curPrice = sdds.get(i).getAdjClose();
							double d = Math.abs( curPrice - prePrice ) / prePrice;
							prePrice = curPrice;
							if( d >= delta) {
								content.add(se.getID().toString());
								content.add(se.getSymbol());
								content.add(se.getSecurityType().toString());
								content.add(df.format(sdds.get(i).getDate()));
								content.add(String.valueOf(d));
								content.add("CRASH");
								clw.write(content);
								break;
							}
						}
					}
				}
			}
			
			if(count>500)
				break;
		}
		clw.close();
	}
	
	public static void checkPortfolioCrash(){
		PortfolioManager pm = (PortfolioManager) ContextHolder.getPortfolioManager();
	}
	
	public static void main(String[] args) throws IOException{
		SecurityPriceUtil.checkSecurityCrash();
	}
}
