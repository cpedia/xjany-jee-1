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
public class Doug_Roberts_Follow_the_Fed_Strategy582 extends SimulateStrategy{
	public Doug_Roberts_Follow_the_Fed_Strategy582(){
		super();
		StrategyID=582L;
		StrategyClassID=3L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	private String SmallCapSecurity;
	public void setSmallCapSecurity(String SmallCapSecurity){
		this.SmallCapSecurity=SmallCapSecurity;
	}
	
	public String getSmallCapSecurity(){
		return this.SmallCapSecurity;
	}
	private String LargeCapSecurity;
	public void setLargeCapSecurity(String LargeCapSecurity){
		this.LargeCapSecurity=LargeCapSecurity;
	}
	
	public String getLargeCapSecurity(){
		return this.LargeCapSecurity;
	}
	private String GoldInvestment;
	public void setGoldInvestment(String GoldInvestment){
		this.GoldInvestment=GoldInvestment;
	}
	
	public String getGoldInvestment(){
		return this.GoldInvestment;
	}
	private String LongTermTBond;
	public void setLongTermTBond(String LongTermTBond){
		this.LongTermTBond=LongTermTBond;
	}
	
	public String getLongTermTBond(){
		return this.LongTermTBond;
	}
	private String InterTermTNote;
	public void setInterTermTNote(String InterTermTNote){
		this.InterTermTNote=InterTermTNote;
	}
	
	public String getInterTermTNote(){
		return this.InterTermTNote;
	}
	private String CPIIndex;
	public void setCPIIndex(String CPIIndex){
		this.CPIIndex=CPIIndex;
	}
	
	public String getCPIIndex(){
		return this.CPIIndex;
	}
	private String ShortTermInterest;
	public void setShortTermInterest(String ShortTermInterest){
		this.ShortTermInterest=ShortTermInterest;
	}
	
	public String getShortTermInterest(){
		return this.ShortTermInterest;
	}
	private int Frequency;
	public void setFrequency(int Frequency){
		this.Frequency=Frequency;
	}
	
	public int getFrequency(){
		return this.Frequency;
	}
	private String Threshold;
	public void setThreshold(String Threshold){
		this.Threshold=Threshold;
	}
	
	public String getThreshold(){
		return this.Threshold;
	}
	private boolean Compound;
	public void setCompound(boolean Compound){
		this.Compound=Compound;
	}
	
	public boolean getCompound(){
		return this.Compound;
	}
	private boolean AddTNote;
	public void setAddTNote(boolean AddTNote){
		this.AddTNote=AddTNote;
	}
	
	public boolean getAddTNote(){
		return this.AddTNote;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		SmallCapSecurity=(String)ParameterUtil.fetchParameter("String","SmallCapSecurity", "NAESX", parameters);
		LargeCapSecurity=(String)ParameterUtil.fetchParameter("String","LargeCapSecurity", "VFINX", parameters);
		GoldInvestment=(String)ParameterUtil.fetchParameter("String","GoldInvestment", "GLD", parameters);
		LongTermTBond=(String)ParameterUtil.fetchParameter("String","LongTermTBond", "VUSTX", parameters);
		InterTermTNote=(String)ParameterUtil.fetchParameter("String","InterTermTNote", "VFITX", parameters);
		CPIIndex=(String)ParameterUtil.fetchParameter("String","CPIIndex", "CPIAUCSL", parameters);
		ShortTermInterest=(String)ParameterUtil.fetchParameter("String","ShortTermInterest", "DTB6", parameters);
		Frequency=(Integer)ParameterUtil.fetchParameter("int","Frequency", "1", parameters);
		Threshold=(String)ParameterUtil.fetchParameter("String","Threshold", "0", parameters);
		Compound=(Boolean)ParameterUtil.fetchParameter("boolean","Compound", "true", parameters);
		AddTNote=(Boolean)ParameterUtil.fetchParameter("boolean","AddTNote", "true", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	double ThresholdValue;
double RealRate;
Date StartDate;
String Buy1 = new String(); 
String Buy2 = new String(); 
	public String getVariables(){
		StringBuffer sb=new StringBuffer();
		sb.append("ThresholdValue: ");
		sb.append(ThresholdValue);
		sb.append("\n");
		sb.append("RealRate: ");
		sb.append(RealRate);
		sb.append("\n");
		sb.append("StartDate: ");
		sb.append(StartDate);
		sb.append("\n");
		sb.append("Buy1: ");
		sb.append(Buy1);
		sb.append("\n");
		sb.append("Buy2: ");
		sb.append(Buy2);
		sb.append("\n");
		return sb.toString();
	}
	
	public void writeObject(ObjectOutputStream stream) throws IOException{
		stream.writeObject(ThresholdValue);
		stream.writeObject(RealRate);
		stream.writeObject(StartDate);
		stream.writeObject(Buy1);
		stream.writeObject(Buy2);
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		ThresholdValue=(Double)stream.readObject();;
		RealRate=(Double)stream.readObject();;
		StartDate=(Date)stream.readObject();;
		Buy1=(String)stream.readObject();;
		Buy2=(String)stream.readObject();;
	}
	
	//----------------------------------------------------
	//user defined functions
	//----------------------------------------------------	
	public double getST(Indicator ShortTerm , Date CurDate, boolean Compound)
 throws Exception {
    double ST= ShortTerm.getValue(CurrentDate)/100;
    printToLog("ST = " +  ST);
    if(Compound == true)
    {
        ST = 1+ ST/12;
        for(int i = 1; i<=11; i++)
        {
            ST = ST * (1 + ShortTerm.getValue(LTIDate.getNewNYSETradingDay(CurrentDate,-i))/1200) ;
        }
        ST = ST - 1;
    }
    printToLog("ST compound = " +  ST);
    return ST;
}

public double getCPIChange( Indicator CPI ,  Date CurDate, boolean Compound)
 throws Exception {
    double CPIChange;
    if(Compound == false)
    {
    Date PastDate = LTIDate.getLastYear(CurDate);
    CPIChange = CPI.getValue(CurDate) / CPI.getValue(PastDate) -1;
    }
    else
    {
        Date Date1 ; 
        Date Date2 ; 
        CPIChange = 1 ; 
        for(int i = 0; i<=11; i++)
        {
            Date1 = LTIDate.getNewNYSEMonth(CurDate, -i);
            Date2 = LTIDate.getNewNYSEMonth(CurDate, -i-1);            
            CPIChange = CPIChange * CPI.getValue(Date1)/CPI.getValue(Date2);
        }
        CPIChange= CPIChange - 1;
    }
    return CPIChange;
}

	//----------------------------------------------------
	//initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void init() throws Exception{
		Indicator CPI = getIndicator(CPIIndex);
Indicator ShortTerm = getIndicator(ShortTermInterest);
StartDate = CurrentDate;

double CurCPI = getCPIChange(CPI, CurrentDate,Compound);
double CurST = getST(ShortTerm , CurrentDate,Compound);
RealRate = CurST - CurCPI;

if(Threshold.charAt(0) == 'S')
{
    double RRSMA = RealRate ;
    for(int i = 1; i<=Integer.parseInt(Threshold.substring(3)) -1; i++)
    {
        Date PastDate = LTIDate.getNewNYSETradingDay(CurrentDate,-i);
        RRSMA +=  getST(ShortTerm , PastDate,Compound) - getCPIChange(CPI, PastDate,Compound);
    } 
    ThresholdValue = RRSMA/Integer.parseInt(Threshold.substring(3)) ;
}
else
    ThresholdValue= Double.parseDouble(Threshold.substring(0));   


CurrentPortfolio.sellAssetCollection(CurrentDate);
double TotalAmount = CurrentPortfolio.getCash(); 

Asset CurrentAsset ;


if(RealRate>ThresholdValue)
{
    Buy1 =  LargeCapSecurity;
    Buy2 =  LongTermTBond;
}
else
{
    Buy1 =  SmallCapSecurity;
    Buy2 =  GoldInvestment;
}

CurrentAsset = new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName( "Large or Small Cap" );
CurrentAsset.setClassID(getSecurity(LargeCapSecurity).getClassID());
if(AddTNote == true)
    CurrentAsset.setTargetPercentage(0.33);
else
    CurrentAsset.setTargetPercentage(0.5);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Large or Small Cap", Buy1 ,TotalAmount*0.33, CurrentDate); 

CurrentAsset = new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName( "Treasury or Gold" );
CurrentAsset.setClassID(getSecurity("Cash").getClassID());
if(AddTNote == true)
    CurrentAsset.setTargetPercentage(0.33);
else
    CurrentAsset.setTargetPercentage(0.5);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy( "Treasury or Gold", Buy2 ,TotalAmount*0.33, CurrentDate); 

if(AddTNote == true)
{
CurrentAsset = new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName( "Intermediate Treasury" );
CurrentAsset.setClassID(getSecurity(InterTermTNote).getClassID());
CurrentAsset.setTargetPercentage(0.34);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Intermediate Treasury", InterTermTNote ,TotalAmount*0.34, CurrentDate); 
}

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
	
		
		Indicator CPI = getIndicator(CPIIndex);
Indicator ShortTerm = getIndicator(ShortTermInterest);

		
		
		
		if(new Boolean(true).equals(false)){
			
		}
		
		 
		else if (LTIDate.calculateInterval(StartDate, CurrentDate)>=(30*Frequency) && LTIDate.isNYSETradingDay(CurrentDate)) {
		   StartDate = CurrentDate;
String PastBuy1 = Buy1;
String PastBuy2 = Buy2;


double CurCPI = getCPIChange(CPI, CurrentDate,Compound);
double CurST = getST(ShortTerm , CurrentDate,Compound);
RealRate = CurST - CurCPI;

if(Threshold.charAt(0) == 'S')
{
    Date PastDate = LTIDate.getNewNYSETradingDay(CurrentDate,-Integer.parseInt(Threshold.substring(3)));
    ThresholdValue += (RealRate - ( getST(ShortTerm , PastDate,Compound) -  getCPIChange(CPI, CurrentDate,Compound) ) ) /Integer.parseInt(Threshold.substring(3)) ;
}


if(RealRate>ThresholdValue)
{
    Buy1 =  LargeCapSecurity;
    Buy2 =  LongTermTBond;
}
else
{
    Buy1 =  SmallCapSecurity;
    Buy2 =  GoldInvestment;
}

if (Buy1 != PastBuy1)
{
  if(AddTNote == true)
  {
    double AssetAmount1 = CurrentPortfolio.getAssetAmount( "Large or Small Cap"  ,CurrentDate);
    double AssetAmount2 = CurrentPortfolio.getAssetAmount( "Treasury or Gold" ,CurrentDate);
    double AssetAmount3 = CurrentPortfolio.getAssetAmount( "Intermediate Treasury" ,CurrentDate);
    double TotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);
    if( AssetAmount3 > TotalAmount * 0.34)
    {
        CurrentPortfolio.sell( "Intermediate Treasury" , InterTermTNote , AssetAmount3 - TotalAmount * 0.34, CurrentDate );
    }
    CurrentPortfolio.sell( "Large or Small Cap" , PastBuy1 , AssetAmount1 , CurrentDate );
    CurrentPortfolio.sell( "Treasury or Gold" , PastBuy2 , AssetAmount2 , CurrentDate );
    CurrentPortfolio.buy( "Large or Small Cap" , Buy1 , TotalAmount * 0.33 , CurrentDate );
    CurrentPortfolio.buy( "Treasury or Gold" , Buy2 , TotalAmount * 0.33 , CurrentDate );
    if( AssetAmount3 < TotalAmount * 0.34)
    {
    double CashAmount = CurrentPortfolio.getCash();
    CurrentPortfolio.buy( "Intermediate Treasury" , InterTermTNote, CashAmount , CurrentDate);    
    }
  }
  else
  {
    double AssetAmount1 = CurrentPortfolio.getAssetAmount( "Large or Small Cap"  ,CurrentDate);
    double AssetAmount2 = CurrentPortfolio.getAssetAmount( "Treasury or Gold" ,CurrentDate);
    double TotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);
    CurrentPortfolio.sell( "Large or Small Cap" , PastBuy1 , AssetAmount1 , CurrentDate );
    CurrentPortfolio.sell( "Treasury or Gold" , PastBuy2 , AssetAmount2 , CurrentDate );
    CurrentPortfolio.buy( "Large or Small Cap" , Buy1 , TotalAmount * 0.5 , CurrentDate );
    CurrentPortfolio.buy( "Treasury or Gold" , Buy2 , TotalAmount * 0.5 , CurrentDate );
  }
}


		}
		
		
		else{
		
			;
		}
		
	}
	
	public double getVersion(){
		return version;
	}
	
}

//