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
public class ETF_Average_Volume_calculation1164 extends SimulateStrategy{
	public ETF_Average_Volume_calculation1164(){
		super();
		StrategyID=1164L;
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
	private String Frequency;
	public void setFrequency(String Frequency){
		this.Frequency=Frequency;
	}
	
	public String getFrequency(){
		return this.Frequency;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		CandidateFund=(String[])ParameterUtil.fetchParameter("String[]","CandidateFund", "VCR,VDC,VIG,VWO,VDE,VEA,VGK,EDV,VFH,VEU,VSS ,VUG,VHT,VYM,VIS,VGT,BIV,VCIT ,BLV,VCLT ,VGLT ,VAW,MGC,MGK,MGV,VO,VOT,VOE,VMBS ,VPL,VNQ,BSV,VGSH ,VB,VBK,VBR,VOX,BND,VTI,VT,VPU,VTV,CASH", parameters);
		AverageVolumeMonth=(Integer)ParameterUtil.fetchParameter("int","AverageVolumeMonth", "3", parameters);
		PlanName=(String)ParameterUtil.fetchParameter("String","PlanName", ""Vanguard ETF"", parameters);
		Frequency=(String)ParameterUtil.fetchParameter("String","Frequency", "monthly", parameters);
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
	
	public double getETFVolume(Long id,Date startDate, Date endDate) throws Exception {
		double amount= 0.0;
		List<SecurityDailyData> dailyDataList= securityManager.getDailyDatas(id, startDate, endDate);
		if(dailyDataList!=null && dailyDataList.size()>0){
			for(int i=0;i<dailyDataList.size();++i){
				amount+= dailyDataList.get(i).getVolume();
			}
			amount /= dailyDataList.size();
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
		   /*------------------------delete the duplicate candidate fund-------------------------*/

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


/*------------------------calculate the ETFs  volume------------------------------*/

double[] ETFVolume = new double[k];
Date StartDate;
Date EndDate;
Security Fund = null;
double TempPrice1;

for(int i=0; i< CandidateFund.length; i++)
{
    Fund = getSecurity(CandidateFund[i]);
    if(Fund == null) 
        { ETFVolume[i] = -999999;  continue;}  
    if(Fund.getIsClosed() && LTIDate.equals(Fund.getEndDate(),CurrentDate) )
        { ETFVolume[i] = -999999;  continue;}  
    try{TempPrice1 = Fund.getAdjClose(CurrentDate); }
        catch(Exception e){ETFVolume[i] = -999999; continue;}          // Omit those funds that DO NOT have currentprice 
  
    if(Fund.getSecurityType() != 1) 
        { ETFVolume[i] = 999999;  continue;}
    else
    {
        if(LTIDate.before(getEarliestAvaliablePriceDate(getSecurity(CandidateFund[i])), LTIDate. getNewNYSEMonth(CurrentDate, -AverageVolumeMonth)))
            StartDate = LTIDate. getNewNYSEMonth(CurrentDate, -AverageVolumeMonth);
        else
            StartDate = getEarliestAvaliablePriceDate(getSecurity(CandidateFund[i]));
        
        EndDate = CurrentDate;
        ETFVolume[i] = getETFVolume(Fund.getID(),StartDate, EndDate);
    }
}

/*-----------------------write Object-----------------------*/

Object[] Ob = new Object[]{CandidateFund, ETFVolume};

java.text.SimpleDateFormat df=new java.text.SimpleDateFormat("yyyy_MM_dd");
String DateString = df.format(CurrentDate);

com.lti.util.PersistentUtil.writeObject(Ob,"MyPlanIQ." + PlanName + "." + DateString +  "." + AverageVolumeMonth +"MonthVolume");





		}
		
		
		else{
		
			;
		}
		
	}
	
	public double getVersion(){
		return version;
	}
	
}

///usr/apache-tomcat-6.0.16/webapps/LTISystem/WEB-INF/src/com/lti/compiledstrategy/ETF_Average_Volume_calculation1164.java:69: ')' expected
//		PlanName=(String)ParameterUtil.fetchParameter("String","PlanName", ""Vanguard ETF"", parameters);
//		                                                                     ^
///usr/apache-tomcat-6.0.16/webapps/LTISystem/WEB-INF/src/com/lti/compiledstrategy/ETF_Average_Volume_calculation1164.java:69: ';' expected
//		PlanName=(String)ParameterUtil.fetchParameter("String","PlanName", ""Vanguard ETF"", parameters);
//		                                                                                 ^
///usr/apache-tomcat-6.0.16/webapps/LTISystem/WEB-INF/src/com/lti/compiledstrategy/ETF_Average_Volume_calculation1164.java:69: not a statement
//		PlanName=(String)ParameterUtil.fetchParameter("String","PlanName", ""Vanguard ETF"", parameters);
//		                                                                                     ^
///usr/apache-tomcat-6.0.16/webapps/LTISystem/WEB-INF/src/com/lti/compiledstrategy/ETF_Average_Volume_calculation1164.java:69: ';' expected
//		PlanName=(String)ParameterUtil.fetchParameter("String","PlanName", ""Vanguard ETF"", parameters);
//		                                                                                               ^
//4 errors