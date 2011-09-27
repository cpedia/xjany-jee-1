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
public class Guru_Allocator_RAA_Fund_Clone_Diversified_Index702 extends SimulateStrategy{
	public Guru_Allocator_RAA_Fund_Clone_Diversified_Index702(){
		super();
		StrategyID=702L;
		StrategyClassID=3L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	private String FundToClone;
	public void setFundToClone(String FundToClone){
		this.FundToClone=FundToClone;
	}
	
	public String getFundToClone(){
		return this.FundToClone;
	}
	private String[] index;
	public void setIndex(String[] index){
		this.index=index;
	}
	
	public String[] getIndex(){
		return this.index;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		FundToClone=(String)ParameterUtil.fetchParameter("String","FundToClone", "HSGFX", parameters);
		index=(String[])ParameterUtil.fetchParameter("String[]","index", "VFINX, VGTSX, VGSIX, VEIEX, VFISX, VFITX, VUSTX, VFSTX, VFICX, VWESX, VWEHX, VFIIX, VIPSX, BEGBX", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	Date NextDate;
int interval=30;
int NumberOfGurus=1;
Date startDate;
Date date;

String[] class1={"HSGFX"};
/* {"HSGFX", "HSTRX", "GGHEX","LCORX", "PASDX","WASYX","VAAPX", "VWINX","GLRBX","FPACX",  "TFSMX", "SWHIX", "DHFCX", "GATEX"};  */
int s=class1.length-1;


	public String getVariables(){
		StringBuffer sb=new StringBuffer();
		sb.append("NextDate: ");
		sb.append(NextDate);
		sb.append("\n");
		sb.append("interval: ");
		sb.append(interval);
		sb.append("\n");
		sb.append("NumberOfGurus: ");
		sb.append(NumberOfGurus);
		sb.append("\n");
		sb.append("startDate: ");
		sb.append(startDate);
		sb.append("\n");
		sb.append("date: ");
		sb.append(date);
		sb.append("\n");
		sb.append("class1: ");
		sb.append(class1);
		sb.append("\n");
		sb.append("s: ");
		sb.append(s);
		sb.append("\n");
		return sb.toString();
	}
	
	public void writeObject(ObjectOutputStream stream) throws IOException{
		stream.writeObject(NextDate);
		stream.writeObject(interval);
		stream.writeObject(NumberOfGurus);
		stream.writeObject(startDate);
		stream.writeObject(date);
		stream.writeObject(class1);
		stream.writeObject(s);
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		NextDate=(Date)stream.readObject();;
		interval=(Integer)stream.readObject();;
		NumberOfGurus=(Integer)stream.readObject();;
		startDate=(Date)stream.readObject();;
		date=(Date)stream.readObject();;
		class1=(String[])stream.readObject();;
		s=(Integer)stream.readObject();;
	}
	
	//----------------------------------------------------
	//user defined functions
	//----------------------------------------------------	
	

	//----------------------------------------------------
	//initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void init() throws Exception{
		startDate=CurrentDate;
date=CurrentDate;



CurrentPortfolio.sellAssetCollection(CurrentDate);

NextDate=LTIDate.getNewNYSETradingDay(CurrentDate,interval);
double TotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);
Asset CurrentAsset;


CurrentAsset = new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Fund_Clone");
CurrentAsset.setClassID(getAssetClassID("HYBRID ASSETS"));
CurrentAsset.setTargetPercentage(1.0);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy(CurrentAsset.getName(), "VFINX", TotalAmount * 0.1,CurrentDate);
CurrentPortfolio.buy(CurrentAsset.getName(), "VGTSX", TotalAmount * 0.1,CurrentDate);
CurrentPortfolio.buy(CurrentAsset.getName(), "VGSIX", TotalAmount * 0.1,CurrentDate);
CurrentPortfolio.buy(CurrentAsset.getName(), "QRAAX", TotalAmount * 0.1,CurrentDate);
CurrentPortfolio.buy(CurrentAsset.getName(), "BEGBX", TotalAmount * 0.1,CurrentDate);
CurrentPortfolio.buy(CurrentAsset.getName(), "VBMFX", TotalAmount * 0.1,CurrentDate);
CurrentPortfolio.buy(CurrentAsset.getName(), "CASH", TotalAmount * 0.4,CurrentDate);
	 				

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
		
		 
		else if (NextDate.equals(CurrentDate)) {
		   //String[] index={"VFINX", "VGTSX", "VGSIX", "QRAAX", "BEGBX","VBMFX", "CASH" };
//String[] index={"VFINX", "VBMFX", "CASH" };

String[] MF={FundToClone};
// ,class1[s-1],class1[s-2], class1[s-3], class1[s-4]};
int n=index.length;
String assetnameArray;

double totalAmount=CurrentPortfolio.getTotalAmount(CurrentDate);
printToLog(NextDate);  



//get current portfolio's asset list 
List<Asset> list1=CurrentPortfolio.getCurrentAssetList();

Asset as=list1.get(0);
assetnameArray=as.getName();

CurrentPortfolio.sellAsset(assetnameArray,CurrentDate);

double[] newAmount=new double[n];
for(int j=0;j<NumberOfGurus;j++)
{
double[] temp=new double[n+1];
temp=RAA(interval,CurrentDate,TimeUnit.DAILY,MF[j],index,false);
for(int k=0;k<n;k++)
   {  
    newAmount[k] += (1.0/NumberOfGurus)*temp[k]*totalAmount;
  }
}

for(int k=0;k<n;k++)
 {  
   CurrentPortfolio.buy(assetnameArray, index[k],newAmount[k] , CurrentDate);    
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