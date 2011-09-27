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
public class Alpha_Dynamics_for_Hedge_Fund335 extends SimulateStrategy{
	public Alpha_Dynamics_for_Hedge_Fund335(){
		super();
		StrategyID=335L;
		StrategyClassID=15L;
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


/* String[] a={"FMLSX","MLSAX","MLSBX","MLSCX","GGUIX","GLSRX","GHEAX","GHEBX","GHECX"
,"GHEIX","GHESX","GHERX","AGEAX","AEGCX","AGEDX","AGEIX","AGEPX","ANGLX","ASFFX"
,"ASFTX","AASFX","ALSFX","BPTRX","CELSX","CELBX","CELCX","CELIX","CELRX","COAGX"
,"CRITX","CRMTX","DIAMX","DHFCX","DHLSX","DXMTX","DLSIX","DLSAX","DTTAX","DTTCX"
,"DTTIX","DTTTX","LSGAX","LSGCX","LSGTX","LSGFX","LSVLX","LSVCX","LSVIX","LSVSX"
,"FOTTX","FOATX","FOBTX","FOCTX","FITOX","FORTX","FUSLX","FGLSX","GFLAX","GFLBX"
,"GFLCX","GTWNX","GPHCX","GPHIX","GPSOX","GPSIX","GTTMX","GFEAX","GFECX","GFEIX"
,"GFETX","GFERX","HEOAX","HEOCX","HEOZX","ISTAX","IOLCX","IOLIX","IOLZX","JALSX"
,"JCLSX","JLSIX","JRLSX","JSLSX","JPSAX","JPSCX","JILSX","JTVAX","JTVCX","JTVRX","JTVSX"
,"KSEAX","KSLAX","KSLCX","KSAIX","KSOIX","MYGAX","MYGCX","MYGIX","MYGNX"
,"MYITX","MYICX","MYIIX","MYINX","MAMSX","MCMSX","MISMX","MKMSX","MRSMX","MYMSX"
,"NARFX","NGTTX","ANAEX","ANCEX","ANIEX","ANDEX","ANGAX","ANGCX","ANGIX","ANGZX"
,"OALSX","ORLSX","PALIX","RPCEX","SIELX","SCIQX","SCEIX","SREGX","SRELX","SRIEX"
,"BPLSX","BPLEX","TOPFX","RYSLX","RYSSX","RYSTX","SWHIX","SWHEX","SAOAX","SAOBX"
,"SAOCX","SDCQX","TLSAX","TLSBX"}; lipper hedge */

/* MS long short */
String[] a={
"AASFX", "ALHIX",  "ALPHX", "ANGLX", "ARBNX", "BETAX", "BMNIX", "DDMSX", "DVRIX", "DVRKX", 
"DVRWX", "FGLSX", "FLSIX", "GHESX", "GMNSX", "GSRTX", "GTAPX", "GTEYX", "GTWNX", "HEOZX",
"HSGFX", "IOLZX", "JAMNX", "NARFX", "REAFX", "REAOX", "REYRX", "RTNRX", "SDCQX", "SMIVX",
"SUEIX", "SWHEX", "TFSMX", "TOPFX", "VPDAX","CASH","CASH","CASH"};

//Define a list of equity securities. 

List <Security> list1= getSecurityList(a);
List <Security> list2;
list2=getTopSecurity(list1,purchasingnumber,-21*intervalofmonth,CurrentDate,TimeUnit.DAILY
,SortType.ALPHA,false); 
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
//Buy in the fund with the best or the worst alpha.


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