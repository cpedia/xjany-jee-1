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
public class RAA_TOP3_Clone2395 extends SimulateStrategy{
	public RAA_TOP3_Clone2395(){
		super();
		StrategyID=395L;
		StrategyClassID=3L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	private String MF1;
	public void setMF1(String MF1){
		this.MF1=MF1;
	}
	
	public String getMF1(){
		return this.MF1;
	}
	private double per1;
	public void setPer1(double per1){
		this.per1=per1;
	}
	
	public double getPer1(){
		return this.per1;
	}
	private String MF2;
	public void setMF2(String MF2){
		this.MF2=MF2;
	}
	
	public String getMF2(){
		return this.MF2;
	}
	private double per2;
	public void setPer2(double per2){
		this.per2=per2;
	}
	
	public double getPer2(){
		return this.per2;
	}
	private String MF3;
	public void setMF3(String MF3){
		this.MF3=MF3;
	}
	
	public String getMF3(){
		return this.MF3;
	}
	private double per3;
	public void setPer3(double per3){
		this.per3=per3;
	}
	
	public double getPer3(){
		return this.per3;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		MF1=(String)ParameterUtil.fetchParameter("String","MF1", "MDLTX", parameters);
		per1=(Double)ParameterUtil.fetchParameter("double","per1", "1.0", parameters);
		MF2=(String)ParameterUtil.fetchParameter("String","MF2", "PRLAX", parameters);
		per2=(Double)ParameterUtil.fetchParameter("double","per2", "0.0", parameters);
		MF3=(String)ParameterUtil.fetchParameter("String","MF3", "FLATX", parameters);
		per3=(Double)ParameterUtil.fetchParameter("double","per3", "0.0", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	Date NextDate;
int interval=30;
	public String getVariables(){
		StringBuffer sb=new StringBuffer();
		sb.append("NextDate: ");
		sb.append(NextDate);
		sb.append("\n");
		sb.append("interval: ");
		sb.append(interval);
		sb.append("\n");
		return sb.toString();
	}
	
	public void writeObject(ObjectOutputStream stream) throws IOException{
		stream.writeObject(NextDate);
		stream.writeObject(interval);
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		NextDate=(Date)stream.readObject();;
		interval=(Integer)stream.readObject();;
	}
	
	//----------------------------------------------------
	//user defined functions
	//----------------------------------------------------	
	

	//----------------------------------------------------
	//initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void init() throws Exception{
		NextDate=LTIDate.getNewNYSETradingDay(CurrentDate,interval);
double TotalAmount = CurrentPortfolio.getTotalAmount();
Asset CurrentAsset;


CurrentAsset = new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName(MF1);
CurrentAsset.setClassID(getAssetClassID("HYBRID ASSETS"));
CurrentAsset.setTargetPercentage(per1);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy(CurrentAsset.getName(), "VFINX", TotalAmount * per1*0.1,CurrentDate);

CurrentPortfolio.buy(CurrentAsset.getName(), "VGTSX", TotalAmount * per1*0.1,CurrentDate);
CurrentPortfolio.buy(CurrentAsset.getName(), "VGSIX", TotalAmount * per1*0.1,CurrentDate);
CurrentPortfolio.buy(CurrentAsset.getName(), "QRAAX", TotalAmount * per1*0.1,CurrentDate);
CurrentPortfolio.buy(CurrentAsset.getName(), "BEGBX", TotalAmount * per1*0.1,CurrentDate);
CurrentPortfolio.buy(CurrentAsset.getName(), "VBMFX", TotalAmount * per1*0.1,CurrentDate);
CurrentPortfolio.buy(CurrentAsset.getName(), "CASH", TotalAmount * per1*0.4,CurrentDate);


CurrentAsset = new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName(MF2);CurrentAsset.setClassID(getAssetClassID("HYBRID ASSETS"));
CurrentAsset.setTargetPercentage(per2);CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy(CurrentAsset.getName(), "VFINX", TotalAmount * per2*0.1,CurrentDate);
CurrentPortfolio.buy(CurrentAsset.getName(), "VGTSX", TotalAmount * per2*0.1,CurrentDate);
CurrentPortfolio.buy(CurrentAsset.getName(), "VGSIX", TotalAmount * per2*0.1,CurrentDate);
CurrentPortfolio.buy(CurrentAsset.getName(), "QRAAX", TotalAmount * per2*0.1,CurrentDate);
CurrentPortfolio.buy(CurrentAsset.getName(), "BEGBX", TotalAmount * per2*0.1,CurrentDate);
CurrentPortfolio.buy(CurrentAsset.getName(), "VBMFX", TotalAmount * per2*0.1,CurrentDate);
CurrentPortfolio.buy(CurrentAsset.getName(), "CASH", TotalAmount * per2*0.4,CurrentDate);

CurrentAsset = new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName(MF3);CurrentAsset.setClassID(getAssetClassID("HYBRID ASSETS"));
CurrentAsset.setTargetPercentage(per3);CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy(CurrentAsset.getName(), "VFINX", TotalAmount * per3*0.1,CurrentDate);
CurrentPortfolio.buy(CurrentAsset.getName(), "VGTSX", TotalAmount * per3*0.1,CurrentDate);
CurrentPortfolio.buy(CurrentAsset.getName(), "VGSIX", TotalAmount * per3*0.1,CurrentDate);
CurrentPortfolio.buy(CurrentAsset.getName(), "QRAAX", TotalAmount * per3*0.1,CurrentDate);
CurrentPortfolio.buy(CurrentAsset.getName(), "BEGBX", TotalAmount * per3*0.1,CurrentDate);
CurrentPortfolio.buy(CurrentAsset.getName(), "VBMFX", TotalAmount * per3*0.1,CurrentDate);
CurrentPortfolio.buy(CurrentAsset.getName(), "CASH", TotalAmount * per3*0.4,CurrentDate);	 				

	 				

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
	
		
		printToLog(CurrentDate);
		
		
		
		if(new Boolean(true).equals(false)){
			
		}
		
		 
		else if (NextDate.equals(CurrentDate)) {
		   String[] index={"VFINX", "VGTSX", "VGSIX", "QRAAX", "BEGBX","VBMFX", "CASH" };
String[] MF={MF1,MF2,MF3};
double[] per={per1,per2,per3};
int m=per.length;
int n=index.length;
int t=0;
String[] assetnameArray=new String[m];
double totalAmount=CurrentPortfolio.getTotalAmount(CurrentDate);
printToLog(NextDate);  

if((per1+per2+per3)!=1)
{
printToLog("the percentage of three mutual fund you enter doesn't equal 1 ! ");
return;
} 
if(MF==null)
{printToLog("the mutual fund you enter is NULL,please enter at least one fund!");
return;
}


//get current portfolio's asset list 
List<Asset> list1=CurrentPortfolio.getCurrentAssetList();
for(int i=0;i<m;i++)
{
Asset as=list1.get(i);
assetnameArray[i]=as.getName();
}

CurrentPortfolio.balance(CurrentDate);
for(int i=0;i<m;i++)
{
CurrentPortfolio.sellAsset(assetnameArray[i],CurrentDate);
}

for(int j=0;j<m;j++)
{
if((MF[j]!=null)&&(per[j]!=0))
{
double[] newAmount=new double[n];
double[] temp=new double[n+1];
temp=RAA(interval,CurrentDate,TimeUnit.DAILY,MF[j],index);
for(int k=0;k<n;k++)
   {  
    newAmount[k]=per[j]*temp[k]*totalAmount;
   CurrentPortfolio.buy(assetnameArray[j], index[k],newAmount[k] , CurrentDate);    
  }
}
else
;
}
NextDate=LTIDate.getNewNYSETradingDay(CurrentDate,interval);
printToLog(NextDate);   
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