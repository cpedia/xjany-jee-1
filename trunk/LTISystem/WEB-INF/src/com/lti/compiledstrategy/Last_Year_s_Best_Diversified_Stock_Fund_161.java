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
public class Last_Year_s_Best_Diversified_Stock_Fund_161 extends SimulateStrategy{
	public Last_Year_s_Best_Diversified_Stock_Fund_161(){
		super();
		StrategyID=161L;
		StrategyClassID=9L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	private int AdjustFrequency;
	public void setAdjustFrequency(int AdjustFrequency){
		this.AdjustFrequency=AdjustFrequency;
	}
	
	public int getAdjustFrequency(){
		return this.AdjustFrequency;
	}
	private String[] AllFunds;
	public void setAllFunds(String[] AllFunds){
		this.AllFunds=AllFunds;
	}
	
	public String[] getAllFunds(){
		return this.AllFunds;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		AdjustFrequency=(Integer)ParameterUtil.fetchParameter("int","AdjustFrequency", "2", parameters);
		AllFunds=(String[])ParameterUtil.fetchParameter("String[]","AllFunds", "EZU,  FEZ, VGK, IEV, PVLDX, OAKIX, PWV, HFCVX, AMSTX, ELV, PID, VTV, SSAIX, EFV, TWEIX, VEIPX, DGT,XLG, PFM, FEU, MAVFX, VTRIX, VWNFX, YACKX, DGRIX, FMIHX, HAINX, AAIPX, FDGFX, RYEUX, SGRIX, THPGX, WVALX, DODFX, SSHFX, IVE, SWINX, JENSX, PIPDX, EFA, WIBCX, DIA, BJBIX, DVY, DTLVX, FEQIX, ICEUX, IWD, OAKMX, PHJ, IWW, SWANX, FSTKX, TRVLX, MEQFX, PRDGX, PRGFX, SSGWX, AAGPX, SPY, PRFDX, TORYX, GABBX, SWDIX, PRBLX, UMINX, HAVLX, VQNPX, DSEFX, DREVX, PWP, SWOIX, SWOGX, VHGEX, VFINX, OPIEX, OBDEX, FEQTX, FDIVX, RIMEX, GABEX, JSVAX, DGAGX, CFIMX, VWNDX, SWHIX, SNXFX, FFIDX, SMCDX, SLASX, IWB, BIGRX, OBFOX, IWS, FWWFX, TRBCX, GABAX, TMW, IYY, PRGIX, VTI, IWV, NBSRX, PRSGX, CAMOX, UMEQX, JAWWX, SOPFX, FDVLX, MVALX, RSP, FEAFX, CCEVX, UMVEX, PRITX, BEQGX, FLCSX, IVW, FDEQX, NGUAX, TIVFX, VUG, NBISX, IWF, JMCVX, WPGLX, AGROX, JANSX, WTMVX, VALDX, FMACX, JAEIX, ELG, PREDX,  FAIRX, VWUSX, IWZ, FGRIX, FBGRX, SNIGX, MRVEX, ARGFX, UMBIX, PWC, PWY, NICSX, IJJ, FAMVX, RYSTX, JAGIX, HSGFX, TEQUX, PENNX, GABGX, BRGRX, MDY, MLPIX, DTLGX, MGRIX, NPRTX, NBREX, PMCDX, MUHLX, BERWX, STRGX, RYSRX,CGMFX, LMVTX", parameters);
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
		
		 
		else if ((AdjustFrequency==2 && LTIDate.isNYSETradingDay(CurrentDate) && LTIDate.calculateInterval(startDate, CurrentDate)>=360) || (AdjustFrequency==1 && LTIDate.isNYSETradingDay(CurrentDate) && LTIDate.calculateInterval(startDate, CurrentDate)>=90) || (AdjustFrequency==0 && LTIDate.isNYSETradingDay(CurrentDate) && LTIDate.calculateInterval(startDate, CurrentDate)>=30)) {
		   /* String[] AllFunds= {"EZU",  "FEZ", "VGK", "IEV", "PVLDX", "OAKIX", "PWV", "HFCVX", "AMSTX", "ELV", "PID", "VTV", "SSAIX", "EFV", "TWEIX", "VEIPX", "DGT","XLG", "PFM", "FEU", "MAVFX", "VTRIX", "VWNFX", "YACKX", "DGRIX", "FMIHX", "HAINX", "AAIPX", "FDGFX", "RYEUX", "SGRIX", "THPGX", "WVALX", "DODFX", "SSHFX", "IVE", "SWINX", "JENSX", "PIPDX", "EFA", "WIBCX", "DIA", "BJBIX", "DVY", "DTLVX", "FEQIX", "ICEUX", "IWD", "OAKMX", "PHJ", "IWW", "SWANX", "FSTKX", "TRVLX", "MEQFX", "PRDGX", "PRGFX", "SSGWX", "AAGPX", "SPY", "PRFDX", "TORYX", "GABBX", "SWDIX", "PRBLX", "UMINX", "HAVLX", "VQNPX", "DSEFX", "DREVX", "PWP", "SWOIX", "SWOGX", "VHGEX", "VFINX", "OPIEX", "OBDEX", "FEQTX", "FDIVX", "RIMEX", "GABEX", "JSVAX", "DGAGX", "CFIMX", "VWNDX", "SWHIX", "SNXFX", "FFIDX", "SMCDX", "SLASX", "IWB", "BIGRX", "OBFOX", "IWS", "FWWFX", "TRBCX", "GABAX", "TMW", "IYY", "PRGIX", "VTI", "IWV", "NBSRX", "PRSGX", "CAMOX", "UMEQX", "JAWWX", "SOPFX", "FDVLX", "MVALX", "RSP", "FEAFX", "CCEVX", "UMVEX", "PRITX", "BEQGX", "FLCSX", "IVW", "FDEQX", "NGUAX", "TIVFX", "VUG", "NBISX", "IWF", "JMCVX", "WPGLX", "AGROX", "JANSX", "WTMVX", "VALDX", "FMACX", "JAEIX", "ELG", "PREDX",  "FAIRX", "VWUSX", "IWZ", "FGRIX", "FBGRX", "SNIGX", "MRVEX", "ARGFX", "UMBIX", "PWC", "PWY", "NICSX", "IJJ", "FAMVX", "RYSTX", "JAGIX", "HSGFX", "TEQUX", "PENNX", "GABGX", "BRGRX", "MDY", "MLPIX", "DTLGX", "MGRIX", "NPRTX", "NBREX", "PMCDX", "MUHLX", "BERWX", "STRGX", "RYSRX","CGMFX", "LMVTX"}; */

int i, N; 

double fBestReturn = -100, fReturn;
String BestFundName=null, CurFundName;
Security BestFund, CurFund;

TimePeriod Previous12Month = new TimePeriod(super.getPreviousYear(CurrentDate), CurrentDate); 

N=0;
for (i=0; i<=AllFunds.length-1; i++) {
   CurFundName=AllFunds[i];
   CurFund = getSecurity(CurFundName);
   if (LTIDate.before(getEarliestAvaliablePriceDate(CurFund), 
                                    LTIDate. getNewNYSEMonth(CurrentDate, -12))) {
        N++;
        try { if ((fReturn=CurFund.getReturn(Previous12Month) )> fBestReturn) {
             fBestReturn = fReturn;
             BestFund = CurFund;
             BestFundName = CurFundName;
        }}catch(Exception e){}
   }
}

printToLog("Total Qualified Funds "+Integer.toString(N)+ " BestFund is " + BestFundName);
double Amount=CurrentPortfolio.getAssetAmount(curAsset,CurrentDate);
CurrentPortfolio.sellAsset(curAsset,CurrentDate);
CurrentPortfolio.buy(curAsset, BestFundName, Amount, CurrentDate);
startDate = CurrentDate;

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