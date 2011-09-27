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
public class Sharpe_Dynamics_for_High_Yield_Bonds427 extends SimulateStrategy{
	public Sharpe_Dynamics_for_High_Yield_Bonds427(){
		super();
		StrategyID=427L;
		StrategyClassID=20L;
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
	private String[] Funds;
	public void setFunds(String[] Funds){
		this.Funds=Funds;
	}
	
	public String[] getFunds(){
		return this.Funds;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		AdjustFrequency=(String)ParameterUtil.fetchParameter("String","AdjustFrequency", "month", parameters);
		intervalofmonth=(Integer)ParameterUtil.fetchParameter("int","intervalofmonth", "1", parameters);
		purchasingnumber=(Integer)ParameterUtil.fetchParameter("int","purchasingnumber", "3", parameters);
		Funds=(String[])ParameterUtil.fetchParameter("String[]","Funds", "PHYTX, MWHIX, NAHYX, NHINX, TIHYX, PXHIX, PYHIX, JHHLX, JHYIX, JIHLX, TIHRX, FTYIX, STHBX, FIHBX, AOPIX, DPHYX, PHYPX, AHYFX, HYLDX, PHAYX, AHIYX, BUFHX, HYFAX, LSHIX, RSHIX, FVHIX, LBHIX, RSHYX, SHYYX, SPHIX, IPIMX, JHYUX, JYHRX, LAHYX, PHYZX, IPHIX, OHYFX, WYHIX, DLHRX, IPHYX, JAHYX, JHYFX, LZHYX, STHYX, FYAIX, STHTX, TRHYX, VWEAX, DHOIX, FHIFX, VWEHX,  EKHYX, SSHYX, ACYIX, CIHYX, COHYX, FHYTX, PHYYX, PRHYX, SAMHX, AHBAX, FAGIX, FJSYX, ABHIX, FHNIX, MHHYX, RBSFX, MHYIX, TGHYX, NHFIX, THYUX, EIBIX, GSHIX, JHHDX, JIHDX, PYHRX, CMHYX, HIAYX, PHIYX, THYIX, FAHCX, SAPHX, BHYIX, BRHYX, AYBFX, KHYIX, NHYRX, NYPAX, SHIYX, AHYPX, MGHYX, BHYSX, BIHYX, CHYIX, LHYZX, SGHSX, HAHYX, NCINX, RITFX, SIHYX, TYHYX, WAHYX, CITFX, HHYIX, MHIIX, MHIJX, SHYOX, DNHYX, DLHYX, GHYYX, MPHSX, SHIIX, MHOIX, MAHIX, MPHLX, USHYX, SHYAX, VCHYX, WTLTX, NTHEX, JHCNX, JHAQX, HIFIX, CASH,CASH, CASH", parameters);
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

/* high yield bond funds from Morningstar */

/*String [] Funds = {"FYAIX",  "PHYTX",  "STHTX",  "AHYFX",  "PHAYX",  "LZHYX",  "HYFAX",  "RSHIX",  "FHNIX",  "RSHYX",  "SPHIX",  "FAGIX",  "AOPIX",  "STHBX",  "DIMRX",  "FAHCX",  "LBHIX",  "WYHIX",  "FHYTX",  "HIAYX",  "PRHYX",  "STHYX",  "FVHIX",  "JAHYX",  "BHYSX",  "DLHRX",  "NYPAX",  "PHYYX",  "PYHRX",  "PYHIX",  "RITFX",  "CITFX",  "LAHYX",  "VCHYX",  "FHIFX",  "MHOIX",  "ABHIX",  "AHBAX",  "BUFHX",  "THYUX",  "MHIIX",  "SGHSX",  "MHIJX",  "SSHYX",  "NCINX",  "SAMHX",  "VWEAX",  "CHYIX",  "LBHBX",  "NHFIX",  "VWEHX",  "FJSYX",  "TGHYX",  "AHYPX",  "IPHYX",  "LHYZX",  "CMHYX",  "SHYAX",  "DNHYX",  "USHYX",  "HHYIX",  "NTHEX",  "WTLTX",  "RHIIX",  "ABMRX",  "ACYVX",  "AHIFX",  "AIHBX",  "ATIPX",  "ATPYX",  "FLIIX",  "GSHTX",  "HAHIX",  "HAHTX",  "HHIZX",  "IFUNX",  "IPHIX",  "IPHSX",  "IVHIX",  "JHAQX",  "JHHDX",  "JHHLX",  "JIHDX",  "JIHLX",  "JINAX",  "LHYFX",  "MHOUX",  "MHOWX",  "NEHYX",  "OCHYX",  "PHYPX",  "TAHZX",  "TIHRX",  "TIYRX", "CASH", "CASH", "CASH"}; */

List <Security> list1= getSecurityList(Funds);
List <Security> list2;
list2=getTopSecurity(list1,purchasingnumber,-21*intervalofmonth,CurrentDate,TimeUnit.DAILY
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
printToLog("buy "+alphafund[i].getName());
}
//Buy in the fund with the best  alpha.


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