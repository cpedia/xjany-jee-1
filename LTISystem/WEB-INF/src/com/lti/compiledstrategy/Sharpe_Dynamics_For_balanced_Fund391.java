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
public class Sharpe_Dynamics_For_balanced_Fund391 extends SimulateStrategy{
	public Sharpe_Dynamics_For_balanced_Fund391(){
		super();
		StrategyID=391L;
		StrategyClassID=2L;
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
/* MS */
String[] balancefund={"WYASX","WCASX","IVAEX","WASYX","WASCX","WBASX","UNASX","WASBX"
,"OWRRX","WASAX","IASEX","LCRIX","LCORX","ETFMX","HCMFX","PAUIX","PAUDX","QOPYX"
,"FIVEX","EQGIX","RNCOX","PAUCX","QOPCX","CLBLX","LAAIX","QOPNX","LAALX","PAAIX","PAALX","PASDX","PAUAX","PATRX","CPMPX","INDEX","PASCX","AZNIX","EXDAX","QOPBX","WARYX"
,"DNAIX","COTZX","AZNDX","APHAX","FSAYX","IMUCX","PDPCX","DCAIX","AMSR","CTFDX"
,"FSASX","PBAIX","APHS","NCHPX","CFUNX","PCBSX","QVOPX","PURIX","AZNCX","ICMBX"
,"PASAX","USTRX","JHAAX","WARCX","PASBX","FSACX","FMRCX","EXBAX","GRSPX","CGIIX"
,"MAFDX","BRBCX","ALRDX","CGNRX","SRTDX","CVTCX","JHAGX","TEBRX","MNBAX","MAFCX"
,"MBEYX","CTFBX","SRTCX","IMUAX","FMIRX","PDPAX","MBECX","PFTRX","ALRCX","OPTIX"
,"CLHAX","VPAAX","AZNAX","CMFCX","DAAIX","UNFDX","CTFAX","WRRBX","OARAX"
,"RSALX","FAAGX","IDRYX","NCCPX","CBIBX","FSGBX","RSACX","VAARX","VAAPX","FMAAX"
,"PCBAX","MAFBX","JHAEX","MASIX","JHAFX","CVTRX","SBX","RSSCX","NWBEX","RAAKX"
,"ALRBX","CVTYX","MAFAX","ALRAX","STRTX","MBEBX","SRTAX","OPFAX","FLMFX"
,"PWTYX","HCMTX","HAECX","MAMBX","MATAX","HAEAX","PWTAX","FBMGX","MAMCX"
,"RAACX","IASFX","FMNAX","DXCSX","MBEAX","FLDFX","IMRBX","IMSAX","KPAAX","GAABX"
,"IMRFX","GUAAX","HAEBX","PWTBX","BRUFX","KMDNX","XUEMX","SIRIX","PFGTX","SALAX"
,"FDCSX","FDYIX","MNMIX","FMIIX","SIRAX","KMDYX","KMDAX","MNBIX","SALRX","MFLDX"
,"CNIIX","ETFPX","CNIAX","SIRRX","FDYSX","FDASX","KMDCX","MNCIX","SALIX","FDTSX"
,"PALPX","SALVX","FDBSX","MIMNX","CASH","CASH","CASH"};
//Define a list of equity securities. 
List <Security> list1= getSecurityList(balancefund);
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