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
public class Last_Year_s_Best_Diversified_Equity_Fund3 extends SimulateStrategy{
	public Last_Year_s_Best_Diversified_Equity_Fund3(){
		super();
		StrategyID=3L;
		StrategyClassID=2L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
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
		
		 
		else if (isYearEnd(CurrentDate)) {
		   //try{
List<Security> AllMutualFunds=securityManager.getMutualFunds();
List<Security> AllAvailableMutualFunds = new ArrayList<Security>();
boolean Have12MonthReturn = false;
double fBestReturn = 0, fReturn;
Security BestFund = null,CurFund;

TimePeriod Previous12Month = new TimePeriod(super.getPreviousYear(CurrentDate), CurrentDate); 

for(int i = 0; i < AllMutualFunds.size(); i ++)
{
   // if(LTIDate.before(getEarliestAvaliablePriceDate(AllMutualFunds.get(i)), LTIDate. getNewNYSEMonth(CurrentDate, -12)))
   //   AllAvailableMutualFunds.add(AllMutualFunds.get(i));
    try{
        fReturn = AllMutualFunds.get(i).getReturn(Previous12Month);
        Have12MonthReturn = true;
    }catch(Exception e2){}
    if(Have12MonthReturn)
        AllAvailableMutualFunds.add(AllMutualFunds.get(i));
    Have12MonthReturn = false;
}

fReturn = -10000;

Iterator<Security> it = AllAvailableMutualFunds.iterator(); 
while(it.hasNext()) {
   CurFund=it.next();
   //System.out.println(CurFund.getReturn(Previous12Month)+"  "+CurFund.getID());
   //fReturn = CurFund.getReturn(Previous12Month);
   if (CurFund.isEquity() && CurFund.isDiversified()&& ((fReturn=CurFund.getReturn(Previous12Month) )> fBestReturn)) {
        fBestReturn = fReturn;
        BestFund = CurFund;
   }
}

printToLog("Best diversified equity Fund return = " +  fBestReturn);
printToLog("Best diversified equity Fund : " + BestFund.getSymbol() );

double amount=CurrentPortfolio.getAssetAmount(curAsset,CurrentDate);
CurrentPortfolio.sellAsset(curAsset,CurrentDate);
CurrentPortfolio.buy(curAsset, BestFund.getSymbol(), amount,CurrentDate);

//}catch(Exception e1){printToLog("action error");}
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