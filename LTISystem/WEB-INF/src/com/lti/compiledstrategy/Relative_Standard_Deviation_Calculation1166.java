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
public class Relative_Standard_Deviation_Calculation1166 extends SimulateStrategy{
	public Relative_Standard_Deviation_Calculation1166(){
		super();
		StrategyID=1166L;
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
	private int StandardDeviationMonth;
	public void setStandardDeviationMonth(int StandardDeviationMonth){
		this.StandardDeviationMonth=StandardDeviationMonth;
	}
	
	public int getStandardDeviationMonth(){
		return this.StandardDeviationMonth;
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
		StandardDeviationMonth=(Integer)ParameterUtil.fetchParameter("int","StandardDeviationMonth", "12", parameters);
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


/*------------------------calculate the relative Standard Deviation------------------------------*/

double[] RelativeSD = new double[k];
Date StartDate;
Date EndDate;
Security Fund = null;
Security Bench= null;
double TempPrice1;
double TempPrice2;

for(int i=0; i< CandidateFund.length; i++)
{
    Fund = getSecurity(CandidateFund[i]);
    if(Fund == null) 
        { RelativeSD[i] = -999999;  continue;}  
    if(Fund.getIsClosed() && LTIDate.equals(Fund.getEndDate(),CurrentDate) )
        { RelativeSD[i] = -999999;  continue;}  
    try{Bench = getSecurity(Fund.getAssetClass().getBenchmarkID());}  
        catch(Exception e){RelativeSD[i] = -999999; continue;}            //Omit those funds that DO NOT Have ParentClassID
    try{TempPrice1 = Fund.getAdjClose(CurrentDate); }
        catch(Exception e){RelativeSD[i] = -999999; continue;}          // Omit those funds that DO NOT have currentprice 
    try{TempPrice2 = Bench.getAdjClose(CurrentDate);}
        catch(Exception e){RelativeSD[i] = 999999; continue; }    

    if(LTIDate.before(LTIDate. getNewNYSEMonth(CurrentDate, -StandardDeviationMonth), getEarliestAvaliablePriceDate(Bench)))
        {RelativeSD[i] = 999999; continue; } 
    else
    {
        double BenchSD; double FundSD; int Month;

        if(LTIDate.before(getEarliestAvaliablePriceDate(Fund), LTIDate. getNewNYSEMonth(CurrentDate, -StandardDeviationMonth)))
            Month = StandardDeviationMonth;
        else if(StandardDeviationMonth > 12 && LTIDate.before(getEarliestAvaliablePriceDate(Fund), LTIDate. getNewNYSEMonth(CurrentDate, -12)))
            Month = 12;
        else if(StandardDeviationMonth > 9 && LTIDate.before(getEarliestAvaliablePriceDate(Fund), LTIDate. getNewNYSEMonth(CurrentDate, -9)))
            Month = 9;
        else if(StandardDeviationMonth > 6 && LTIDate.before(getEarliestAvaliablePriceDate(Fund), LTIDate. getNewNYSEMonth(CurrentDate, -6)))
            Month = 6;
        else
            {RelativeSD[i] = 999999; continue; } 

        FundSD = Fund.getStandardDeviation(-Month, CurrentDate, TimeUnit.MONTHLY, false);  
        BenchSD = Bench.getStandardDeviation(-Month, CurrentDate, TimeUnit.MONTHLY, false);
        if(BenchSD == 0)
            {RelativeSD[i] = 999999; continue; } 
        else
            RelativeSD[i] = FundSD / BenchSD;

    }
}

/*-----------------------write Object-----------------------*/

Object[] Ob = new Object[]{CandidateFund, RelativeSD};

java.text.SimpleDateFormat df=new java.text.SimpleDateFormat("yyyy_MM_dd");
String DateString = df.format(CurrentDate);

com.lti.util.PersistentUtil.writeObject(Ob,"MyPlanIQ." + PlanName + "." + DateString +  "." + StandardDeviationMonth +"MonthRelativeSD");





		}
		
		
		else{
		
			;
		}
		
	}
	
	public double getVersion(){
		return version;
	}
	
}

//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\Relative_Standard_Deviation_Calculation1166.java:69:  � ')'
//		PlanName=(String)ParameterUtil.fetchParameter("String","PlanName", ""Vanguard ETF"", parameters);
//		                                                                     ^
//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\Relative_Standard_Deviation_Calculation1166.java:69:  � ';'
//		PlanName=(String)ParameterUtil.fetchParameter("String","PlanName", ""Vanguard ETF"", parameters);
//		                                                                                 ^
//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\Relative_Standard_Deviation_Calculation1166.java:69: /��
//		PlanName=(String)ParameterUtil.fetchParameter("String","PlanName", ""Vanguard ETF"", parameters);
//		                                                                                     ^
//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\Relative_Standard_Deviation_Calculation1166.java:69:  � ';'
//		PlanName=(String)ParameterUtil.fetchParameter("String","PlanName", ""Vanguard ETF"", parameters);
//		                                                                                               ^
//4 �