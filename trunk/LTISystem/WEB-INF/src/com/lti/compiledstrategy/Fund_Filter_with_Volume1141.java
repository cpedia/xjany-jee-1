package com.lti.compiledstrategy;

import com.lti.Exception.Security.NoPriceException;
import com.lti.Exception.Strategy.ParameterException;
import com.lti.Exception.Strategy.VariableException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.*;

import com.lti.service.bo.*;
import java.util.*;
import com.lti.type.*;
import com.lti.type.finance.*;
import com.lti.type.executor.*;
import com.lti.util.*;
import com.tictactec.ta.lib.*;
import com.lti.util.simulator.ParameterUtil;

@SuppressWarnings({ "deprecation", "unused" })
public class Fund_Filter_with_Volume1141 extends SimulateStrategy{
	public Fund_Filter_with_Volume1141(){
		super();
		StrategyID=1141L;
		StrategyClassID=3L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	private String[] CandidateFund;
	public void setCandidateFund(String[] CandidateFund){
		this.CandidateFund=CandidateFund;
	}
	
	public String[] getCandidateFund(){
		return this.CandidateFund;
	}
	private double VolumeThreshold;
	public void setVolumeThreshold(double VolumeThreshold){
		this.VolumeThreshold=VolumeThreshold;
	}
	
	public double getVolumeThreshold(){
		return this.VolumeThreshold;
	}
	private int AverageVolumeMonth;
	public void setAverageVolumeMonth(int AverageVolumeMonth){
		this.AverageVolumeMonth=AverageVolumeMonth;
	}
	
	public int getAverageVolumeMonth(){
		return this.AverageVolumeMonth;
	}
	private String PlanName;
	public void setPlanName(String PlanName){
		this.PlanName=PlanName;
	}
	
	public String getPlanName(){
		return this.PlanName;
	}
	private int Frequency;
	public void setFrequency(int Frequency){
		this.Frequency=Frequency;
	}
	
	public int getFrequency(){
		return this.Frequency;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		CandidateFund=(String[])ParameterUtil.fetchParameter("String[]","CandidateFund", "VCR,VDC,VIG,VWO,VDE,VEA,VGK,EDV,VFH,VEU,VSS ,VUG,VHT,VYM,VIS,VGT,BIV,VCIT ,BLV,VCLT ,VGLT ,VAW,MGC,MGK,MGV,VO,VOT,VOE,VMBS ,VPL,VNQ,BSV,VGSH ,VB,VBK,VBR,VOX,BND,VTI,VT,VPU,VTV,CASH", parameters);
		VolumeThreshold=(Double)ParameterUtil.fetchParameter("double","VolumeThreshold", "200000", parameters);
		AverageVolumeMonth=(Integer)ParameterUtil.fetchParameter("int","AverageVolumeMonth", "3", parameters);
		PlanName=(String)ParameterUtil.fetchParameter("String","PlanName", ""Vanguard ETF"", parameters);
		Frequency=(Integer)ParameterUtil.fetchParameter("int","Frequency", "monthly", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	
	public String getVariables(){
		StringBuffer sb=new StringBuffer();
		return sb.toString();
	}
	
	public void writeObject(ObjectOutputStream stream) throws IOException{
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
	}
	
	//----------------------------------------------------
	//user defined functions
	//----------------------------------------------------	
		public Long getMonthVolume(Long id,Date endDate) throws Exception {
		Long amount= 0L;
		Date startDate = LTIDate.getNewNYSEMonth(endDate, -3);
		List<SecurityDailyData> dailyDataList= securityManager.getDailyDatas(id, startDate, endDate);
		if(dailyDataList!=null && dailyDataList.size()>0){
			for(int i=0;i<dailyDataList.size();++i){
				amount+= dailyDataList.get(i).getVolume();
			}
			amount/=dailyDataList.size();
		}
	return amount;	
	}

	//----------------------------------------------------
	//initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void init() throws Exception{
		
	}
	//----------------------------------------------------
	//re-initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void reinit() throws Exception{
		
	}
	
	//----------------------------------------------------
	//action code
	//----------------------------------------------------	
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void action() throws Exception{
	
		
		
		
		
		
		if(new Boolean(true).equals(false)){
			
		}
		
		 
		else if ((Frequency.equals("monthly")&&LTIDate.isLastNYSETradingDayOfMonth(CurrentDate)) || (Frequency.equals("quarterly")&&LTIDate.isLastNYSETradingDayOfQuarter(CurrentDate)) || 
(Frequency.equals("yearly")&&LTIDate.isLastNYSETradingDayOfYear(CurrentDate))) {
		   List<String> CandidateFundList = new ArrayList<String>();
List<String> ShortHistoryFundList = new ArrayList<String>();


/*delete the duplicate candidate fund*/

int[] Duplicate = new int[CandidateFund.length];
int k = 0;
for(int i = 0; i < CandidateFund.length; i++)
{
    for(int j = i +1; j < CandidateFund.length; j++)
    {
        if(CandidateFund[i].equals(CandidateFund[j]))
            { Duplicate[i] = 1;  break; }        
    }
    if(Duplicate[i] != 1)
        { Duplicate[i] = 0; k++; }
}

String[] Temp1 = CandidateFund;
CandidateFund = new String[k];
k = 0;
for(int i = 0; i < Duplicate.length; i++)
{
    if(Duplicate[i] == 0)
    { 
        CandidateFund[k] = Temp1[i];
        k++; 
    }
}



/*Exclude the ETFs with too small volume*/

Security Fund = null;
Security Bench= null;
boolean HaveCash = false;
for(int i=0; i< CandidateFund.length; i++)
{
    if(CandidateFund[i].equals("CASH"))
         continue;
    boolean PutThisFund=true;
    try{
        Fund = getSecurity(CandidateFund[i]);
        Bench = getSecurity(Fund.getAssetClass().getBenchmarkID());
     }  catch(Exception e){continue;}           //Omit those funds that Do NOT exist or DO NOT Have benchmarkID 
    
    if(Fund.getSecurityType() == 1 && LTIDate.before(getEarliestAvaliablePriceDate(getSecurity(CandidateFund[i])), LTIDate. getNewNYSEMonth(CurrentDate, -AverageVolumeMonth)))
    {

            Long Volume=0L;
            Date EndDate=CurrentDate;
            for (int j=0;j<AverageVolumeMonth;j++)
            {
                Volume=Volume+getMonthVolume(Fund.getID(),EndDate);
                EndDate=LTIDate.getNewNYSEMonth(EndDate,-(j+1));
            }
            Volume=Volume/AverageVolumeMonth;
           if (Volume >VolumeThreshold) 
               CandidateFundList.add(CandidateFund[i]);}
     }
}
CandidateFundList.add("CASH"); 


/*if writeObject can deal with ArrayList then the below is unneccesary*/
String[] FilterdFund = new String[CandidateFundList.size()];
for(int i = 0; i < FilterdFund.length; i++)
{
    FilterdFund[i] = CandidateFundList.get(i);
}

Object[] Ob = new Object[]{FilterdFund};

DateFormat df=new SimpleDateFormat("yyyy_MM_dd");
df.format(CurrentDate);

		}
		
		
		else{
		
			;
		}
		
	}
	
	public double getVersion(){
		return version;
	}
	
}

///usr/apache-tomcat-6.0.16/webapps/LTISystem/WEB-INF/src/com/lti/compiledstrategy/Fund_Filter_with_Volume1141.java:78: ')' expected
//		PlanName=(String)ParameterUtil.fetchParameter("String","PlanName", ""Vanguard ETF"", parameters);
//		                                                                     ^
///usr/apache-tomcat-6.0.16/webapps/LTISystem/WEB-INF/src/com/lti/compiledstrategy/Fund_Filter_with_Volume1141.java:78: ';' expected
//		PlanName=(String)ParameterUtil.fetchParameter("String","PlanName", ""Vanguard ETF"", parameters);
//		                                                                                 ^
///usr/apache-tomcat-6.0.16/webapps/LTISystem/WEB-INF/src/com/lti/compiledstrategy/Fund_Filter_with_Volume1141.java:78: not a statement
//		PlanName=(String)ParameterUtil.fetchParameter("String","PlanName", ""Vanguard ETF"", parameters);
//		                                                                                     ^
///usr/apache-tomcat-6.0.16/webapps/LTISystem/WEB-INF/src/com/lti/compiledstrategy/Fund_Filter_with_Volume1141.java:78: ';' expected
//		PlanName=(String)ParameterUtil.fetchParameter("String","PlanName", ""Vanguard ETF"", parameters);
//		                                                                                               ^
///usr/apache-tomcat-6.0.16/webapps/LTISystem/WEB-INF/src/com/lti/compiledstrategy/Fund_Filter_with_Volume1141.java:226: illegal start of type
//		else{
//		^
///usr/apache-tomcat-6.0.16/webapps/LTISystem/WEB-INF/src/com/lti/compiledstrategy/Fund_Filter_with_Volume1141.java:233: class, interface, or enum expected
//	public double getVersion(){
//	       ^
///usr/apache-tomcat-6.0.16/webapps/LTISystem/WEB-INF/src/com/lti/compiledstrategy/Fund_Filter_with_Volume1141.java:235: class, interface, or enum expected
//	}
//	^
//7 errors