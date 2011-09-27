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
public class TempTestingMap663 extends SimulateStrategy{
	public TempTestingMap663(){
		super();
		StrategyID=663L;
		StrategyClassID=3L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	private String[] CandidateAsset;
	public void setCandidateAsset(String[] CandidateAsset){
		this.CandidateAsset=CandidateAsset;
	}
	
	public String[] getCandidateAsset(){
		return this.CandidateAsset;
	}
	private double RiskyAllocation;
	public void setRiskyAllocation(double RiskyAllocation){
		this.RiskyAllocation=RiskyAllocation;
	}
	
	public double getRiskyAllocation(){
		return this.RiskyAllocation;
	}
	private double MoneyAllocation;
	public void setMoneyAllocation(double MoneyAllocation){
		this.MoneyAllocation=MoneyAllocation;
	}
	
	public double getMoneyAllocation(){
		return this.MoneyAllocation;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		CandidateAsset=(String[])ParameterUtil.fetchParameter("String[]","CandidateAsset", "Large Growth,Large Value,SHORT TERM,Muni Ohio,CASH,CASH,CASH,CD", parameters);
		RiskyAllocation=(Double)ParameterUtil.fetchParameter("double","RiskyAllocation", "70", parameters);
		MoneyAllocation=(Double)ParameterUtil.fetchParameter("double","MoneyAllocation", "10", parameters);
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
	public Map<String,Double> AssetPercentageAllocation(String[] CandidateAsset, double RiskyAllocation, double MoneyAllocation) throws Exception {
int a1=0;int a2=0;int a0=0;int aa=0;
int[] A = new int[CandidateAsset.length];
Map<String,Double> amap=new HashMap<String,Double>();

/*�CandidateAsset d�
*/
List <String> Candidate=new ArrayList<String>();

for (int i=0;i<CandidateAsset.length;i++){
	boolean boo=true;
	for (int j=0;j<Candidate.size();j++){
	if (CandidateAsset[i].equals(Candidate.get(j))) {boo=false;}
	}
	if (boo) Candidate.add(CandidateAsset[i]);
}

for(int i =0; i< Candidate.size(); i++){
    if(isUpperOrSameClass("EQUITY", Candidate.get(i)) || isUpperOrSameClass("HIGH YIELD BOND", Candidate.get(i)) || isUpperOrSameClass("HIGH YIELD BONDS", Candidate.get(i))) 
        {A[i] = 1; a1+=1;}
    if(isUpperOrSameClass("FIXED INCOME", Candidate.get(i)) && !isUpperOrSameClass("HIGH YIELD BOND", Candidate.get(i)) && !isUpperOrSameClass("HIGH YIELD BONDS", Candidate.get(i)))
        {A[i] = 2; a2 +=1;} 
    if(isUpperOrSameClass("CASH", Candidate.get(i))) 
        {A[i] = 0; a0 +=1;} 
}
if ((a1==0)&&(RiskyAllocation!=0)) {printToLog("No candidate Risky_Assets!");}
if ((a2==0)&&((100-RiskyAllocation-MoneyAllocation)!=0)) {printToLog("No candidate Stable_Assets!");}

for (int i=0; i<Candidate.size(); i++){
	if (A[i]==1){
	amap.put(Candidate.get(i),new Double(1.0/a1));
	printToLog(Candidate.get(i)+(double)(1.0/a1));
	}
	else if (A[i]==2){
	amap.put(Candidate.get(i),new Double(1.0/a2));
	printToLog(Candidate.get(i)+(double)(1.0/a2));
	}
	else if ((A[i]==0)&&(a0!=0)) 
{amap.put(Candidate.get(i),new Double(1.0/a0));
	printToLog(Candidate.get(i)+(double)(1.0/a0));}
}
if (a0==0){amap.put("CASH",new Double(MoneyAllocation));}
return amap;
}

	//----------------------------------------------------
	//initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void init() throws Exception{
		Map<String,Double> amap=AssetPercentageAllocation(CandidateAsset,RiskyAllocation,MoneyAllocation);

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

//