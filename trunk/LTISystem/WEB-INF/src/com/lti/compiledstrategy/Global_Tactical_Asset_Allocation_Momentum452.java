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
public class Global_Tactical_Asset_Allocation_Momentum452 extends SimulateStrategy{
	public Global_Tactical_Asset_Allocation_Momentum452(){
		super();
		StrategyID=452L;
		StrategyClassID=3L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	private boolean OneMonth;
	public void setOneMonth(boolean OneMonth){
		this.OneMonth=OneMonth;
	}
	
	public boolean getOneMonth(){
		return this.OneMonth;
	}
	private String[] UnderlyingFunds;
	public void setUnderlyingFunds(String[] UnderlyingFunds){
		this.UnderlyingFunds=UnderlyingFunds;
	}
	
	public String[] getUnderlyingFunds(){
		return this.UnderlyingFunds;
	}
	private boolean Hedge;
	public void setHedge(boolean Hedge){
		this.Hedge=Hedge;
	}
	
	public boolean getHedge(){
		return this.Hedge;
	}
	private int NumFundsChosen;
	public void setNumFundsChosen(int NumFundsChosen){
		this.NumFundsChosen=NumFundsChosen;
	}
	
	public int getNumFundsChosen(){
		return this.NumFundsChosen;
	}
	private double HedgePercent;
	public void setHedgePercent(double HedgePercent){
		this.HedgePercent=HedgePercent;
	}
	
	public double getHedgePercent(){
		return this.HedgePercent;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		OneMonth=(Boolean)ParameterUtil.fetchParameter("boolean","OneMonth", "false", parameters);
		UnderlyingFunds=(String[])ParameterUtil.fetchParameter("String[]","UnderlyingFunds", "VFINX,VGTSX,VFICX,CASH,VWEHX,BEGBX,VGSIX,VIMSX, NAESX, VUSTX, VFISX, VFIIX", parameters);
		Hedge=(Boolean)ParameterUtil.fetchParameter("boolean","Hedge", "false", parameters);
		NumFundsChosen=(Integer)ParameterUtil.fetchParameter("int","NumFundsChosen", "3", parameters);
		HedgePercent=(Double)ParameterUtil.fetchParameter("double","HedgePercent", "1.0", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	Date startDate;
Date LastMonthDate;
/*String[] nameArray = {"VFINX","VGTSX","VFICX","CASH","VWEHX","BEGBX","VGSIX","VIMSX", "NAESX", "VUSTX", "VFISX", "VFIIX"}; */
String[] sortArray;
	public String getVariables(){
		StringBuffer sb=new StringBuffer();
		sb.append("startDate: ");
		sb.append(startDate);
		sb.append("\n");
		sb.append("LastMonthDate: ");
		sb.append(LastMonthDate);
		sb.append("\n");
		sb.append("sortArray: ");
		sb.append(sortArray);
		sb.append("\n");
		return sb.toString();
	}
	
	public void writeObject(ObjectOutputStream stream) throws IOException{
		stream.writeObject(startDate);
		stream.writeObject(LastMonthDate);
		stream.writeObject(sortArray);
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		startDate=(Date)stream.readObject();;
		LastMonthDate=(Date)stream.readObject();;
		sortArray=(String[])stream.readObject();;
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
		
		 
		else if (LTIDate.isLastNYSETradingDayOfMonth (CurrentDate)) {
		   startDate=CurrentDate;
CurrentPortfolio.sellAssetCollection(CurrentDate);
double TotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);

if (!OneMonth) {
/* 12-1 */
      LastMonthDate = LTIDate.getNewNYSEMonth(startDate, -1);
     sortArray=
getTopSecurityArray(UnderlyingFunds,-12,LastMonthDate,TimeUnit.MONTHLY,SortType.RETURN,true);
} else {
/* 1 Month */
    sortArray=
getTopSecurityArray(UnderlyingFunds,-1,CurrentDate,TimeUnit.MONTHLY,SortType.RETURN,true);
}
    
for(int i=0;i<sortArray.length;i++)
{
      printToLog(sortArray[i]);
 }

Asset CurrentAsset ;
for (int i=0; i<=NumFundsChosen-1; i++) {
CurrentAsset = new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset"+i);
CurrentAsset.setClassID(getAssetClassID(getSecurity (sortArray[i]).getName()));
CurrentAsset.setTargetPercentage(1.0/NumFundsChosen);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy(CurrentAsset.getName(), sortArray[i], TotalAmount * (1.0/NumFundsChosen),CurrentDate);
}

/* shorting */
if (Hedge) {
for (int i=0; i<=NumFundsChosen-1; i++) {
   printToLog("shorting "+i+ sortArray[sortArray.length-i-1]);
   CurrentPortfolio.shortSell("Asset"+i, sortArray[sortArray.length-i-1], TotalAmount * HedgePercent*(1.0/NumFundsChosen),CurrentDate);
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