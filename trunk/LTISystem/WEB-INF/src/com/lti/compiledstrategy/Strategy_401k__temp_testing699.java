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
public class Strategy_401k__temp_testing699 extends SimulateStrategy{
	public Strategy_401k__temp_testing699(){
		super();
		StrategyID=699L;
		StrategyClassID=3L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	private double RiskyAllocation;
	public void setRiskyAllocation(double RiskyAllocation){
		this.RiskyAllocation=RiskyAllocation;
	}
	
	public double getRiskyAllocation(){
		return this.RiskyAllocation;
	}
	private double  MoneyAllocation;
	public void setMoneyAllocation(double  MoneyAllocation){
		this.MoneyAllocation=MoneyAllocation;
	}
	
	public double  getMoneyAllocation(){
		return this.MoneyAllocation;
	}
	private String[] CandidateAsset;
	public void setCandidateAsset(String[] CandidateAsset){
		this.CandidateAsset=CandidateAsset;
	}
	
	public String[] getCandidateAsset(){
		return this.CandidateAsset;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		RiskyAllocation=(Double)ParameterUtil.fetchParameter("double","RiskyAllocation", "60", parameters);
		MoneyAllocation=(Double )ParameterUtil.fetchParameter("double ","MoneyAllocation", "30", parameters);
		CandidateAsset=(String[])ParameterUtil.fetchParameter("String[]","CandidateAsset", "US EQUITY, International Equity, US EQUITY, Real Estate, Real Estate, US EQUITY,US EQUITY, US EQUITY, US EQUITY, FIXED INCOME, FIXED INCOME, CASH", parameters);
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
	public Map<String,List<Double>> AssetPercentageAllocation(String[] CandidateAsset, double RiskyAllocation, double MoneyAllocation) throws Exception {

int a1=0;int a2=0;int a0=0;int aa=0;

int[] A = new int[CandidateAsset.length];

Map<String,List<Double>> amap=new HashMap<String,List<Double>>();

/*simlify the CandidateAsset */

List <String> Candidate=new ArrayList<String>();

for (int i=0;i<CandidateAsset.length;i++){

	boolean boo=true;

	for (int j=0;j<Candidate.size();j++){

	if (CandidateAsset[i].equals(Candidate.get(j))) {boo=false;}
	
	}

if (boo) Candidate.add(CandidateAsset[i]);

}
/* recognize the Class of CandidateAsset */
for(int i =0; i< Candidate.size(); i++){

if(isUpperOrSameClass("EQUITY", Candidate.get(i)) || isUpperOrSameClass("HIGH YIELD BOND", Candidate.get(i)) || isUpperOrSameClass("HIGH YIELD BONDS", Candidate.get(i)))

{A[i] = 1; a1+=1;}

else if(isUpperOrSameClass("FIXED INCOME", Candidate.get(i)) && !isUpperOrSameClass("HIGH YIELD BOND", Candidate.get(i)) && !isUpperOrSameClass("HIGH YIELD BONDS", Candidate.get(i)))

{A[i] = 2; a2 +=1;}

	else if (isUpperOrSameClass("REAL ESTATE", Candidate.get(i)) || isUpperOrSameClass("COMMODITIES", Candidate.get(i)))	{A[i] = 2; a2 +=1;}
		
		else if(isUpperOrSameClass("CASH", Candidate.get(i)))	{A[i] = 0; a0 +=1;}

}

if ((a1==0)&&(RiskyAllocation!=0)) {printToLog("No candidate Risky_Assets!");}

if ((a2==0)&&((100-RiskyAllocation-MoneyAllocation)!=0)) {printToLog("No candidate Stable_Assets!");}

/* Assign weight */

for (int i=0; i<Candidate.size(); i++){

if (A[i]==1){

List <Double> TempList=new ArrayList<Double>();
TempList.add(new Double(RiskyAllocation/a1/100));
amap.put(Candidate.get(i),TempList);   
printToLog(Candidate.get(i)+(double)(RiskyAllocation/a1/100));

}

else if (A[i]==2){
List <Double> TempList=new ArrayList<Double>();
TempList.add(new Double((100-RiskyAllocation-MoneyAllocation)/a2/100));
amap.put(Candidate.get(i),TempList);    
printToLog(Candidate.get(i)+(double)((100-RiskyAllocation-MoneyAllocation)/a2/100));
}

}
List <Double> TempList=new ArrayList<Double>();
TempList.add(new Double(MoneyAllocation));
amap.put("CASH",TempList);

return amap;

}

	//----------------------------------------------------
	//initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void init() throws Exception{
		printToLog("Hello!~");
Map<String,List<Double>> amap=new HashMap<String,List<Double>>();
amap=AssetPercentageAllocation(CandidateAsset,RiskyAllocation,MoneyAllocation);

	HashMap<String,List<String>> AllAssetFundsMap = new HashMap<String,List<String>>();
	Iterator  iter2 = AllAssetFundsMap.entrySet().iterator(); 
	while (iter2.hasNext()) { 
	    Map.Entry <String,List<String>>entry = (Map.Entry<String,List<String>>) iter2.next(); 
	    String AssetName = entry.getKey(); 
	    List<String> SecurNameList = entry.getValue(); 
	} 
	double rr=0;
	Double tt=30/100.0;
	if (rr<tt){System.out.println("True");}
List<Asset> tess= CurrentPortfolio.getAssetList();

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
		
		
		else{
		
			;
		}
		
	}
	
	public double getVersion(){
		return version;
	}
	
}

///usr/apache-tomcat-6.0.16/webapps/LTISystem/WEB-INF/src/com/lti/compiledstrategy/Strategy_401k__temp_testing699.java:175: cannot find symbol
//symbol  : method getAssetList()
//location: class com.lti.type.executor.SimulateStrategy
//List<Asset> tess= CurrentPortfolio.getAssetList();
//                                  ^
//1 error