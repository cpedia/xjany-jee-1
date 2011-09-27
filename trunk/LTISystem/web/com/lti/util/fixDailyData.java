package com.lti.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.lti.service.SecurityManager;
import com.lti.service.bo.Security;
import com.lti.service.bo.SecurityDailyData;
import com.lti.system.ContextHolder;
import com.lti.type.TimeUnit;

public class fixDailyData {

	public static void main(String[] args)
	{

		LTIDownLoader ld = new LTIDownLoader();
		//List<String> sList = ld.getNameString();
		
		System.out.println("****start update daily data*****");		
		
		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
		
		Date cur = new Date();
		//ld.writeLog("ALL Security UpDate", cur, " start update daily data");
				
		try {
			
			List<String> symbols = new ArrayList<String>();
			HashMap<String,Date> lastDailyDataMap = new HashMap<String,Date>();
			HashMap<String,Date> lastDividendDataMap = new HashMap<String,Date>();
			
			List<Security> securityList = securityManager.getSecurities();
			
			List<Date> navEndDateList = new ArrayList<Date>();			
			
			List<Date> fundEndDateList = new ArrayList<Date>();			
			List<String> fundList = new ArrayList<String>();
			
			Date date = new Date();			
			Date date2 = new Date();
			date2 = LTIDate.getDate(1900, 0, 0);
			
			//delete update files
			ld.deleteFile(true);
			
			List<String> seList = new ArrayList<String>();
			
			List<String> navList = new ArrayList<String>();			

			for(int i=0;i<securityList.size();i++)			
			//for(int i=3000,size = ;i<size;i++)//
			{ 				
							
				if(i==securityList.size())break;				
				
				Security tempSecurity = securityList.get(i);
				//Security tempSecurity = securityManager.getBySymbol(sList.get(i));
				if(tempSecurity == null)continue;
				if(tempSecurity.getManual() !=null && tempSecurity.getManual()==1)continue;
				
				if(tempSecurity.getSymbol() == null )
				{
					//ld.writeLog("Unknown Security", date, "security has no symbol");
					continue;
				}
				if(tempSecurity.getSecurityType() == null)
				{
					//ld.writeLog(tempSecurity.getSymbol(), date, "security has no security type");
					continue;
				}
				
				String symbol = tempSecurity.getSymbol();
								
				if(tempSecurity.getSecurityType() == 6)continue;
								
				System.out.println(symbol+" "+i);
								
				/*********************************************************************/
				//if(!symbol.equalsIgnoreCase("APB") )continue;
				if(tempSecurity.getManual()!=null && tempSecurity.getManual()==0)
				{
					Date t_startDate = LTIDate.getDate(2009, 2, 26);
					Date t_endDate = LTIDate.getDate(2009, 3, 5);
					
					Date t_dividendDate = tempSecurity.getDividendLastDate();
					Date t_priceDate = tempSecurity.getPriceLastDate();
					Date t_navDate = tempSecurity.getNavLastDate();
					
					if(t_dividendDate!=null && t_startDate.before(t_dividendDate))tempSecurity.setDividendLastDate(t_startDate);
					if(t_priceDate!=null && t_startDate.before(t_priceDate))
					{
						tempSecurity.setPriceLastDate(t_startDate);
						tempSecurity.setEndDate(t_priceDate);
					}
					if(t_navDate!=null && t_startDate.before(t_navDate))tempSecurity.setNavLastDate(t_startDate);
					securityManager.saveOrUpdate(tempSecurity);
					List<SecurityDailyData> sdds = securityManager.getDailyDatas(tempSecurity.getID(), t_startDate, t_endDate);
					securityManager.removeAll(sdds);
				}
				/**********************************************************************/
				
				if(symbol.equalsIgnoreCase("CASH") || symbol.equalsIgnoreCase("CN CASH")){
					continue;
				}				
				
				int type = ld.getUpdateType(symbol);
				
				if(type != 1)
				{
					//Date tmpDate = securityManager.getDailyDataLastDate(tempSecurity.getID());
					Date tmpDate = tempSecurity.getPriceLastDate();
					if(tmpDate == null)tmpDate = securityManager.getDailyDataLastDate(tempSecurity.getID());
					fundList.add(symbol);
					fundEndDateList.add(tmpDate);
					continue;
				}
				
				try {				
					long id = tempSecurity.getID();
					
					SecurityDailyData staticData = new SecurityDailyData();
					
					//Date dividendDate = securityManager.getDividendLastDate(id);
					Date dividendDate = tempSecurity.getDividendLastDate();
					if(dividendDate == null)dividendDate = securityManager.getDividendLastDate(id);
					if(dividendDate == null)dividendDate = date2;
					dividendDate = LTIDate.add(dividendDate, 1);
					
					//Date dailyDataDate = securityManager.getDailyDataLastDate(id);
					Date dailyDataDate = tempSecurity.getPriceLastDate();
					if(dailyDataDate == null)dailyDataDate = securityManager.getDailyDataLastDate(id);
					if(dailyDataDate == null)dailyDataDate = date2;
										
					if(tempSecurity.getSecurityType() == 2 && type == 1){
						System.out.println("yes "+symbol+" "+i);
						
						//Date navDate = securityManager.getNAVLastDate(id);
						Date navDate = tempSecurity.getNavLastDate();
						if(navDate == null)navDate = securityManager.getNAVLastDate(id);
						if(navDate == null) navDate = date2;
						
						ld.saveCEFToFile(symbol, navDate,date,"n",true);
						navList.add(symbol);
						navEndDateList.add(navDate);
					}
					
					//saveToFile(symbol, dailyDataDate, date, "d",true);//save daily data
					
					
					ld.saveToFile(symbol, dividendDate, date, "v",true);//save dividends
					
					symbols.add(symbol);
					lastDailyDataMap.put(symbol, dailyDataDate);
					
										
					Thread.sleep(500); 
				} catch (Exception e) {
					
					//ld.writeLog(symbol,date,"DownLoadError");
					//ld.writeLog(symbol,date,e.toString());
					
					e.printStackTrace();
				}
			}
			
			
			
			lastDividendDataMap = ld.upLoadFile("v",false,true);
			HashMap<String,Date> newLastDailyDataMap = new HashMap<String,Date>();
			for(int i=0;i<symbols.size();i++)
			{
				String symbol = symbols.get(i);
				Date dividendDataDate = lastDividendDataMap.get(symbol);
				Date dailyDataDate = lastDailyDataMap.get(symbol);
				if(dividendDataDate == null)dividendDataDate = dailyDataDate;
				else{
					if(dailyDataDate.before(dividendDataDate))
						dailyDataDate = dailyDataDate;
					else
						dailyDataDate = dividendDataDate;
				}
				
				newLastDailyDataMap.put(symbol, dailyDataDate);
				dailyDataDate = LTIDate.getNewTradingDate(dailyDataDate,TimeUnit.DAILY,-1);
				ld.saveToFile(symbol, dailyDataDate, date, "d",true);
			}
			
			//when update fromStart = false, for reload database fromStart = true;
			ld.upLoadFileAndAdjust(false,true,newLastDailyDataMap,false);
			
			ld.uploadNAV(true);
			ld.updateAdjNAV(navList,navEndDateList);
			
			ld.updateCash("CASH","^IRX");
			
			/*try {
				if(ld.getUpdateOption() != 1)
				{
					ld.downLoadAndUpdateFund(fundList, fundEndDateList);
					ld.updateCash("CN CASH", "050003.OF");
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}*/			

			//ld.checkSplit();			
			
			//ld.checkIndicatorUpdate();
			
			System.out.println("Done.");
			
			//ld.writeLog("All Security", date, "Finish all update data.");
			
			/*try{
				ld.downLoadAndUpdateSP();
			}catch (Exception e){
				System.out.println("down load and update SP "+date+ "Error."+e.toString());
				ld.writeLog("down load and update SP ", date, "Error."+e.toString());
			}
			
			securityManager.getAllSecurityMPT(true);*/
			
			
		} catch (Exception e) {			
			/*log.debug("Error: "+e);
			Date date = new Date();
			ld.writeLog("UpdateDailyData", date, e.toString());*/
			e.printStackTrace();
			
		}
	
	}
	
}

