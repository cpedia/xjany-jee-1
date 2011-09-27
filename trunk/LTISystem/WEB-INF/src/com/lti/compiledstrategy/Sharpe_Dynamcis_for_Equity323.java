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
public class Sharpe_Dynamcis_for_Equity323 extends SimulateStrategy{
	public Sharpe_Dynamcis_for_Equity323(){
		super();
		StrategyID=323L;
		StrategyClassID=16L;
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
	Date startDate;


	public String getVariables(){
		StringBuffer sb=new StringBuffer();
		sb.append("startDate: ");
		sb.append(startDate);
		sb.append("\n");
		return sb.toString();
	}
	
	public void writeObject(ObjectOutputStream stream) throws IOException{
		stream.writeObject(startDate);
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		startDate=(Date)stream.readObject();;
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


String[] a= {"EZU",  "FEZ", "VGK", "IEV", "PVLDX", "OAKIX", "PWV", "HFCVX", "AMSTX", "ELV", "PID", "VTV", "SSAIX", "EFV", "TWEIX", "VEIPX", "DGT","XLG", "PFM", "FEU", "MAVFX", "VTRIX", "VWNFX", "YACKX", "DGRIX", "FMIHX", "HAINX", "AAIPX", "FDGFX", "RYEUX", "SGRIX", "THPGX", "WVALX", "DODFX", "SSHFX", "IVE", "SWINX", "JENSX", "PIPDX", "EFA", "WIBCX", "DIA", "BJBIX", "DVY", "DTLVX", "FEQIX", "ICEUX", "IWD", "OAKMX", "PHJ", "IWW", "SWANX", "FSTKX", "TRVLX", "MEQFX", "PRDGX", "PRGFX", "SSGWX", "AAGPX", "SPY", "PRFDX", "TORYX", "GABBX", "SWDIX", "PRBLX", "UMINX", "HAVLX", "VQNPX", "DSEFX", "DREVX", "PWP", "SWOIX", "SWOGX", "VHGEX", "VFINX", "OPIEX", "OBDEX", "FEQTX", "FDIVX", "RIMEX", "GABEX", "JSVAX", "DGAGX", "CFIMX", "VWNDX", "SWHIX", "SNXFX", "FFIDX", "SMCDX", "SLASX", "IWB", "BIGRX", "OBFOX", "IWS", "FWWFX", "TRBCX", "GABAX", "TMW", "IYY", "PRGIX", "VTI", "IWV", "NBSRX", "PRSGX", "CAMOX", "UMEQX", "JAWWX", "SOPFX", "FDVLX", "MVALX", "RSP", "FEAFX", "CCEVX", "UMVEX", "PRITX", "BEQGX", "FLCSX", "IVW", "FDEQX", "NGUAX", "TIVFX", "VUG", "NBISX", "IWF", "JMCVX", "WPGLX", "AGROX", "JANSX", "WTMVX", "VALDX", "FMACX", "JAEIX", "ELG", "PREDX",  "FAIRX", "VWUSX", "IWZ", "FGRIX", "FBGRX", "SNIGX", "MRVEX", "ARGFX", "UMBIX", "PWC", "PWY", "NICSX", "IJJ", "FAMVX", "RYSTX", "JAGIX", "HSGFX", "TEQUX", "PENNX", "GABGX", "BRGRX", "MDY", "MLPIX", "DTLGX", "MGRIX", "NPRTX", "NBREX", "PMCDX", "MUHLX", "BERWX", "STRGX", "RYSRX","CASH","CASH","CASH"};
//Define a list of equity securities. 

List <Security> list1= getSecurityList(a);
List <Security> list2;
list2=getTopSecurity(list1,purchasingnumber,-intervalofmonth,CurrentDate,TimeUnit.MONTHLY
,SortType.SHARPE,false); 
printToLog("no error when getting");
printToLog(list2);

//printToLog(bestsharpe.getName()+bestsharpe.getSharpeRatio(LTIDate.getNewNYSEMonth(CurrentDate,-1),CurrentDate,TimeUnit.DAILY,false));
//printToLog(secondsharpe.getName()+secondsharpe.getSharpeRatio(LTIDate.getNewNYSEMonth(CurrentDate,-1),CurrentDate,TimeUnit.DAILY,false));

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
printToLog("buy "+alphafund[i].getName());
}
//Buy in the fund with the best  alpha.


}

startDate=CurrentDate;
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