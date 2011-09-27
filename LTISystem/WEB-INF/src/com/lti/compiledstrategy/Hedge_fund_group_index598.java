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
public class Hedge_fund_group_index598 extends SimulateStrategy{
	public Hedge_fund_group_index598(){
		super();
		StrategyID=598L;
		StrategyClassID=9L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	private String[] ComponentFundString;
	public void setComponentFundString(String[] ComponentFundString){
		this.ComponentFundString=ComponentFundString;
	}
	
	public String[] getComponentFundString(){
		return this.ComponentFundString;
	}
	private String Categary;
	public void setCategary(String Categary){
		this.Categary=Categary;
	}
	
	public String getCategary(){
		return this.Categary;
	}
	private boolean UseCategary;
	public void setUseCategary(boolean UseCategary){
		this.UseCategary=UseCategary;
	}
	
	public boolean getUseCategary(){
		return this.UseCategary;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		ComponentFundString=(String[])ParameterUtil.fetchParameter("String[]","ComponentFundString", "P_5486, P_5785, P_5795, P_5827, P_5841, P_5867, P_6029, P_6073, P_6085, P_6099, P_6101, P_6117", parameters);
		Categary=(String)ParameterUtil.fetchParameter("String","Categary", "value", parameters);
		UseCategary=(Boolean)ParameterUtil.fetchParameter("boolean","UseCategary", "true", parameters);
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
	List<Portfolio> ComponentFunds= null;

public List<Portfolio> getComponentFunds()  throws Exception {
       if(ComponentFunds==null){
           if(UseCategary == true)
               ComponentFunds =com.lti.service.CloningCenterManager.get13FPortfolios( Categary, null);
           else
              ComponentFunds =getPortfolioList(ComponentFundString);
       }
       return ComponentFunds;
}

	//----------------------------------------------------
	//initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void init() throws Exception{
		/*
List<Portfolio> portfolios =com.lti.service.CloningCenterManager.get13FPortfolios( "Value", null);

String[] symbols=new String[portfolios.size()];

for(int i=0;i<portfolios.size();i++){

       symbols[i]=portfolios.get(i).getSymbol();

}
printToLog("Symbols = " + symbols);
*/

ComponentFunds = getComponentFunds() ;
for(int i = 0; i<=ComponentFunds.size()-1;i++)
{
   Portfolio p=ComponentFunds.get(i);
    printToLog("start date of" + (i+1) + "  =  "  + p.getStartingDate()+"\r\nID:"+p.getID()+" symbol:"+p.getSymbol());

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
	
		
		Calendar calendar=Calendar.getInstance();
    calendar.setTime(CurrentDate);
    int month=calendar.get(Calendar.MONTH)+1;
    int day=calendar.get(Calendar.DAY_OF_MONTH); 
		
		
		
		if(new Boolean(true).equals(false)){
			
		}
		
		 
		else if ((month == 2 || month == 5 || month == 8|| month == 11) && day == 15) {
		   List<String> AvailableFunds = new ArrayList<String>();
ComponentFunds = getComponentFunds() ;
CurrentPortfolio.sellAsset(curAsset, CurrentDate);
double AssetAmount = CurrentPortfolio.getCash();
/*double AssetAmount = CurrentPortfolio.getAssetAmount(curAsset, CurrentDate);*/

for(int i = 0; i<=ComponentFunds.size()-1;i++)
{
    if(LTIDate.before(ComponentFunds.get(i).getStartingDate(), CurrentDate))
        try{
        ComponentFunds.get(i).getAnnualizedReturn(CurrentDate, LTIDate.getYesterday(CurrentDate));
        AvailableFunds.add(ComponentFunds.get(i).getSymbol());
        }catch(Exception e){
        }
}
printToLog(AvailableFunds.size());

/*
Security s=null;
try{
s.getClosePrice(CurrentDate);
list.add(s);
}catch(Exception e){
}
*/

if(AvailableFunds.size() >= 1)
{
    for(int i = 0; i<= AvailableFunds.size() - 1; i++)
        CurrentPortfolio.buy(curAsset, AvailableFunds.get(i), AssetAmount/AvailableFunds.size(), CurrentDate);
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

//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\Hedge_fund_group_index598.java:86: ~0&÷
//&÷ ¹Õ get13FPortfolios(java.lang.String,<nulltype>)
//Mn { com.lti.service.CloningCenterManager
//               ComponentFunds =com.lti.service.CloningCenterManager.get13FPortfolios( Categary, null);
//                                                                   ^
//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\Hedge_fund_group_index598.java:158: ~0&÷
//&÷ ¹Õ getAnnualizedReturn(java.util.Date,java.util.Date)
//Mn { com.lti.service.bo.Portfolio
//        ComponentFunds.get(i).getAnnualizedReturn(CurrentDate, LTIDate.getYesterday(CurrentDate));
//                             ^
//2 ï