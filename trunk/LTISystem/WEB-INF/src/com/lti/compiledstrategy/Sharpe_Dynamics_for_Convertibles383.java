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
public class Sharpe_Dynamics_for_Convertibles383 extends SimulateStrategy{
	public Sharpe_Dynamics_for_Convertibles383(){
		super();
		StrategyID=383L;
		StrategyClassID=1L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	private String AdjustFrequency;
	public void setAdjustFrequency(String AdjustFrequency){
		this.AdjustFrequency=AdjustFrequency;
	}
	
	public String getAdjustFrequency(){
		return this.AdjustFrequency;
	}
	private int intervalofmonth;
	public void setIntervalofmonth(int intervalofmonth){
		this.intervalofmonth=intervalofmonth;
	}
	
	public int getIntervalofmonth(){
		return this.intervalofmonth;
	}
	private int purchasingnumber;
	public void setPurchasingnumber(int purchasingnumber){
		this.purchasingnumber=purchasingnumber;
	}
	
	public int getPurchasingnumber(){
		return this.purchasingnumber;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		AdjustFrequency=(String)ParameterUtil.fetchParameter("String","AdjustFrequency", "month", parameters);
		intervalofmonth=(Integer)ParameterUtil.fetchParameter("int","intervalofmonth", "1", parameters);
		purchasingnumber=(Integer)ParameterUtil.fetchParameter("int","purchasingnumber", "3", parameters);
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
		
		 
		else if ((AdjustFrequency.equals("year") &&LTIDate.isLastNYSETradingDayOfYear (CurrentDate))
 || (AdjustFrequency.equals("quarter") && LTIDate.isLastNYSETradingDayOfQuarter(CurrentDate)) 
|| (AdjustFrequency.equals("month") && LTIDate.isLastNYSETradingDayOfMonth(CurrentDate))) {
		   Security[] alphafund=new Security[purchasingnumber];
//List <Security > list1=getSecurityByClass( CurrentPortfolio.getAsset(curAsset).getAssetClassID());


String[] convertibles={"CNCVX","CCVIX","CALBX","CCVCX","CICVX","CCVRX","PACIX","NCVBX"
,"PHIKX","NCIAX","RPFCX","DCSBX","DCSCX","DCSYX","FCVSX","FISCX","FROTX","GAGCX"
,"GRVAX","GRVBX","GRVCX","GRVIX","GRVSX","GRVRX","HCSIX","NAKSX","NANBX","NRTLX"
,"NAIQX","SCRAX","SCVSX","SMCLX","SCVYX","LACFX","LBCFX","LACCX","LBFFX","LCFYX"
,"LCFPX","LBCQX","LCFRX","MCOAX","MCSVX","MCCVX","MCINX","MCFAX","CNSAX","CNSBX"
,"CNSCX","CNSDX","NIGIX","NIGTX","NIGFX","NOIEX","RCVAX","RCVBX","RCVCX","RCVGX"
,"RCVNX","PFCAX","PFCIX","PCONX","PCNBX","PRCCX","PCNMX","PCVRX","PCGYX","ICVAX"
,"ICVBX","ICVLX","VALCX","ACHBX","ACHAX","ACHCX","ACHIX","VCVSX","SBFCX"};


//Define a list of equity securities. 

List <Security> list1= getSecurityList(convertibles);
List <Security> list2;
list2=getTopSecurity(list1,purchasingnumber,-intervalofmonth,CurrentDate,TimeUnit.MONTHLY
,SortType.SHARPE,false); 
printToLog("no error when getting");
printToLog(list2);

if(list2!=null)
{


for(int i=0;i<=list2.size()-1;i++)
{alphafund[i]=list2.get(i); }

double amount=CurrentPortfolio.getAssetAmount(curAsset,CurrentDate);
CurrentPortfolio.sellAsset(curAsset,CurrentDate);
//Sell current position;

for(int i=0;i<=list2.size()-1;i++)

{
CurrentPortfolio.buy(curAsset, alphafund[i].getName(),amount/list2.size()
,CurrentDate);
}
//Buy in the fund with the best alpha.


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