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
public class BL_RAA_FlexibleFunds381 extends SimulateStrategy{
	public BL_RAA_FlexibleFunds381(){
		super();
		StrategyID=381L;
		StrategyClassID=3L;
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
	int T=30;
int freq=40;
Date NextDate;
String[] Index={"VFINX", "VGTSX", "VGSIX", "QRAAX", "BEGBX","VBMFX", "CASH" };
/*String[] class1={"EZU", "OLCVX", "FEZ", "VGK", "IEV", "PVLDX", "OAKIX", "PWV", "HFCVX", "AMSTX", "ELV", "PID", "VTV", "SSAIX", "EFV", "TWEIX", "VEIPX", "DGT","XLG", "PFM", "FEU", "MAVFX", "VTRIX", "VWNFX", "YACKX", "DGRIX", "FMIHX", "HAINX", "AAIPX", "FDGFX", "RYEUX", "SGRIX", "THPGX", "WVALX", "DODFX", "SSHFX", "IVE", "SWINX", "JENSX", "PIPDX", "EFA", "WIBCX", "DIA", "BJBIX", "DVY", "DTLVX", "FEQIX", "ICEUX", "IWD", "OAKMX", "PHJ", "IWW", "SWANX", "FSTKX", "TRVLX", "MEQFX", "PRDGX", "PRGFX", "SSGWX", "AAGPX", "SPY", "PRFDX", "TORYX", "GABBX", "SWDIX", "PRBLX", "UMINX", "HAVLX", "VQNPX", "DSEFX", "DREVX", "PWP", "SWOIX", "SWOGX", "VHGEX", "VFINX", "OPIEX", "OBDEX", "FEQTX", "FDIVX", "RIMEX", "GABEX", "JSVAX", "DGAGX", "CFIMX", "VWNDX", "SWHIX", "SNXFX", "FFIDX", "SMCDX", "SLASX", "IWB", "BIGRX", "OBFOX", "IWS", "FWWFX", "TRBCX", "GABAX", "TMW", "IYY", "PRGIX", "VTI", "IWV", "NBSRX", "PRSGX", "CAMOX", "UMEQX", "JAWWX", "SOPFX", "FDVLX", "MVALX", "RSP", "FEAFX", "CCEVX", "UMVEX", "PRITX", "BEQGX", "FLCSX", "IVW", "FDEQX", "NGUAX", "TIVFX", "VUG", "NBISX", "IWF", "JMCVX", "WPGLX", "AGROX", "JANSX", "WTMVX", "VALDX", "FMACX", "JAEIX", "ELG", "PREDX", "WAGEX", "FAIRX", "VWUSX", "IWZ",  "FGRIX", "FBGRX", "SNIGX", "MRVEX", "ARGFX", "UMBIX", "PWC", "PWY", "NICSX", "IJJ", "FAMVX", "RYSTX", "JAGIX", "HSGFX", "TEQUX", "PENNX", "GABGX", "BRGRX", "MDY", "MLPIX", "DTLGX", "MGRIX", "NPRTX", "NBREX", "PMCDX", "MUHLX", "BERWX", "STRGX", "RYSRX"};*/

String[] class1={"WYASX","WCASX","IVAEX","WASYX","WASCX","WBASX","UNASX","WASBX"
,"OWRRX","WASAX","IASEX","LCRIX","LCORX","ETFMX","HCMFX","PAUIX","PAUDX","QOPYX"
,"FIVEX","EQGIX","RNCOX","PAUCX","QOPCX","CLBLX","LAAIX","QOPNX","LAALX","PAAIX","PAALX","PASDX","PAUAX","PATRX","CPMPX","INDEX","PASCX","AZNIX","EXDAX","QOPBX","WARYX"
,"DNAIX","COTZX","AZNDX","APHAX","FSAYX","IMUCX","PDPCX","DCAIX","AMSR","CTFDX"
,"FSASX","PBAIX","APHS","NCHPX","CFUNX","PCBSX","QVOPX","PURIX","AZNCX","ICMBX"
,"PASAX","USTRX","JHAAX","WARCX","PASBX","FSACX","FMRCX","EXBAX","GRSPX","CGIIX"
,"MAFDX","BRBCX","ALRDX","CGNRX","SRTDX","CVTCX","JHAGX","TEBRX","MNBAX","MAFCX"
,"MBEYX","CTFBX","SRTCX","IMUAX","FMIRX","PDPAX","MBECX","PFTRX","ALRCX","OPTIX"
,"CLHAX","VPAAX","IASCX","AZNAX","CMFCX","DAAIX","UNFDX","CTFAX","WRRBX","OARAX"
,"RSALX","FAAGX","IDRYX","NCCPX","CBIBX","FSGBX","RSACX","VAARX","VAAPX","FMAAX"
,"PCBAX","MAFBX","JHAEX","MASIX","JHAFX","CVTRX","SBX","RSSCX","NWBEX","RAAKX"
,"ALRBX","CVTYX","MAFAX","ALRAX","STRTX","MBEBX","PCALX","SRTAX","OPFAX","FLMFX"
,"PWTYX","HCMTX","HAECX","MAMBX","MATAX","HAEAX","PWTAX","FBMGX","MAMCX"
,"RAACX","IASFX","FMNAX","DXCSX","MBEAX","FLDFX","IMRBX","IMSAX","KPAAX","GAABX"
,"IMRFX","GUAAX","HAEBX","PWTBX","BRUFX","KMDNX","XUEMX","SIRIX","PFGTX","SALAX"
,"FDCSX","FDYIX","MNMIX","FMIIX","SIRAX","KMDYX","KMDAX","MNBIX","SALRX","MFLDX"
,"CNIIX","ETFPX","CNIAX","SIRRX","FDYSX","FDASX","KMDCX","MNCIX","SALIX","FDTSX"
,"PALPX","SALVX","FDBSX","MIMNX"};
	public String getVariables(){
		StringBuffer sb=new StringBuffer();
		sb.append("T: ");
		sb.append(T);
		sb.append("\n");
		sb.append("freq: ");
		sb.append(freq);
		sb.append("\n");
		sb.append("NextDate: ");
		sb.append(NextDate);
		sb.append("\n");
		sb.append("Index: ");
		sb.append(Index);
		sb.append("\n");
		sb.append("class1: ");
		sb.append(class1);
		sb.append("\n");
		return sb.toString();
	}
	
	public void writeObject(ObjectOutputStream stream) throws IOException{
		stream.writeObject(T);
		stream.writeObject(freq);
		stream.writeObject(NextDate);
		stream.writeObject(Index);
		stream.writeObject(class1);
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		T=(Integer)stream.readObject();;
		freq=(Integer)stream.readObject();;
		NextDate=(Date)stream.readObject();;
		Index=(String[])stream.readObject();;
		class1=(String[])stream.readObject();;
	}
	
	//----------------------------------------------------
	//user defined functions
	//----------------------------------------------------	
	

	//----------------------------------------------------
	//initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void init() throws Exception{
		NextDate=LTIDate.getNewNYSETradingDay(CurrentDate,freq);
Asset CurrentAsset=new Asset();
double TotalAmount=CurrentPortfolio.getTotalAmount(CurrentDate);
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("BL_RAA");
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
printToLog("TEST");
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
		   System.out.println(CurrentDate);
int K;   // the number of securities
int v=3;   // the number of views
List<Security>  securityArray=new ArrayList<Security>();;
List<Double>  marketWeight=new ArrayList<Double>();
List<List<Double>>  myWeight=new ArrayList<List<Double>> ();
List<Double>  confidence=new ArrayList<Double>();
List<Double> viewReturn=new ArrayList<Double>();
BigDecimal delta;

List<Asset> list1=CurrentPortfolio.getCurrentAssetList();
Asset asset1=list1.get(0);

securityArray=CurrentPortfolio. getAssetSecurity(asset1.getName(),CurrentDate);
if(securityArray==null)
{
printToLog("securityArray is null!");
 }

else{
K=securityArray.size();

double[] C=new double[K];
for(int i=0;i<K;i++)
{
C[i]=1.0;
}

List<Security>  TopSecurityArray=new ArrayList<Security>();
List <Security> list2= getSecurityList(class1);
TopSecurityArray=getTopSecurity(list2,v,-1,CurrentDate,TimeUnit.YEARLY,SortType.SHARPE,false);
double[] Q=new double[v];
for(int i=0;i<v;i++)
{
Q[i]=TopSecurityArray.get(i). getReturn ( CurrentDate,TimeUnit.YEARLY,-1,false);
}

double[][] P=new double[v][K];
for(int i=0;i<v;i++)
{
double[] temp=new double[K+1];
temp=RAA(T,CurrentDate,TimeUnit.DAILY,TopSecurityArray.get(i).getName(),Index,false);
for(int j=0;j<K;j++)
{
P[i][j]=temp[j];
}
}

//set marketWeight
LTIBLInterface lb = new LTIBLInterface();
for(int i=1;i<=K;i++)
{ 
double e0=1.0/K;
marketWeight.add(e0);
}
lb.setMarketWeights(marketWeight);

//set myweight
int m=P.length;
int n=P[0].length;

List<List<Double>> b = new ArrayList<List<Double>>();
for(int i=0;i<m;i++)
{
 List<Double> a = new ArrayList<Double>();
 for(int j=0;j<n;j++)
{
Double e1=P[i][j];		
      a.add(e1);
}
b.add(a);
}
lb.setMyWeight(b);

//set confidence
List<Double> omi = new ArrayList<Double>();
for(int i=0;i<C.length;i++)
{
Double e2=C[i];
omi.add(e2);
}
lb.setOmigaMatrix(omi);

//set Tao
BigDecimal bd2 = new BigDecimal(m*1.0/K);
lb.setTao(bd2);

//caculate sigma		
List<List<Double>> c1= new ArrayList<List<Double>>();

for(int i=0;i<K;i++)
{
List<Double> d = new ArrayList<Double>();
for(int j=0;j<T;j++)
{
Date date1;
Date date2;
date1=LTIDate.getNewNYSETradingDay(CurrentDate, -1*j);
date2=LTIDate.getNewNYSETradingDay(CurrentDate, -1*j-1);
Security se=securityArray.get(i);
double temp=(se.getAdjClose(date1)-se.getAdjClose(date2))/se.getAdjClose(date2);
d.add(temp);
}
c1.add(d);
}
lb.setSigma(lb.calculateSigma(c1));		

//set view return		
List<Double> e = new ArrayList<Double>();
for(int i=0;i<Q.length;i++)
{
Double e3=Q[i];
e.add(e3);
}
lb.setTargetValue(e);

//set delta	
BigDecimal bd3 = new BigDecimal(3.0);
lb.setDelta(bd3);	
//lb.setDelta(lb.calculateDelta2());

List<BigDecimal>  BLweight=new  ArrayList<BigDecimal>();


double totalAmount1=CurrentPortfolio.getTotalAmount(CurrentDate);
CurrentPortfolio.sellAsset(asset1.getName(),CurrentDate);

BLweight=lb.getmodifedWeights3();
//jz: somehow, there is very small -0.004xxx weight. We need to find out why. for now, get rid of them.
for(int i=0;i<K; i++) 
   printToLog(BLweight.get(i));	

double total=0.0;
for(int j=0;j<K;j++)
{
   double newWeight=BLweight.get(j).doubleValue();

      CurrentPortfolio.buy(asset1.getName(), securityArray.get(j).getName(),totalAmount1*newWeight , CurrentDate);    

}
NextDate=LTIDate.getNewNYSETradingDay(CurrentDate,freq);
printToLog(NextDate);   

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