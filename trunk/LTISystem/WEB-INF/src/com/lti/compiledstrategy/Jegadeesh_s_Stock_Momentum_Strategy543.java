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
public class Jegadeesh_s_Stock_Momentum_Strategy543 extends SimulateStrategy{
	public Jegadeesh_s_Stock_Momentum_Strategy543(){
		super();
		StrategyID=543L;
		StrategyClassID=9L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	private int HoldingPeriod;
	public void setHoldingPeriod(int HoldingPeriod){
		this.HoldingPeriod=HoldingPeriod;
	}
	
	public int getHoldingPeriod(){
		return this.HoldingPeriod;
	}
	private int PastReturnPeriod;
	public void setPastReturnPeriod(int PastReturnPeriod){
		this.PastReturnPeriod=PastReturnPeriod;
	}
	
	public int getPastReturnPeriod(){
		return this.PastReturnPeriod;
	}
	private String [] nameArray;
	public void setNameArray(String [] nameArray){
		this.nameArray=nameArray;
	}
	
	public String [] getNameArray(){
		return this.nameArray;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		HoldingPeriod=(Integer)ParameterUtil.fetchParameter("int","HoldingPeriod", "3", parameters);
		PastReturnPeriod=(Integer)ParameterUtil.fetchParameter("int","PastReturnPeriod", "3", parameters);
		nameArray=(String [])ParameterUtil.fetchParameter("String []","nameArray", "AIR,ABM,ABD,ACE,ACIW,ADCT,AES,AG,ATG,AKS, AHS,AMR,ASA,T,ATAC,ATMI,AVX,ABAX,ABT, ANF,IAF,FAX,ACN,ACPW,ATVI,ATU,AYI,ADX,ADPT, ADBE,ADTN,AAP,AEIS,AMD,ABCO,ACM,AEG,ARO, AET,ACS,AMG,AFFX,AFL,A,AGYS,AEM,AGU,APD, AYR,ARG,AAI,AKAM,ALK,ALSK,AIN,AMRI,ALB,ACV, ALU,AA,ACL,ALEX,ALGN,Y,AYE,ATI,AGN,ALE, AB,ACG,LNT,ATK,ALD,AW,ALOY,MDRX,ALL,ALNY, ANR,ALTR,MO,AMZN,ACO,DOX,AMED,AEE, AXL,ACAS,AEO,ECOL,AEP,AXP,AFG,AM,AIG,AWR", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	Date startDate;
int CurPeriod=1;
int NumberOfDaysSwitching;
int n;
double originalAmount;
/* String[] nameArray = {"AIR","ABM","ABD","ACE","ACIW","ADCT","AES","AG","ATG","AKS",
"AHS","AMR","ASA","T","ATAC","ATMI","AVX","ABAX","ABT",
"ANF","IAF","FAX","ACN","ACPW","ATVI","ATU","AYI","ADX","ADPT",
"ADBE","ADTN","AAP","AEIS","AMD","ABCO","ACM","AEG","ARO",
"AET","ACS","AMG","AFFX","AFL","A","AGYS","AEM","AGU","APD",
"AYR","ARG","AAI","AKAM","ALK","ALSK","AIN","AMRI","ALB","ACV",
"ALU","AA","ACL","ALEX","ALGN","Y","AYE","ATI","AGN","ALE",
"AB","ACG","LNT","ATK","ALD","AW","ALOY","MDRX","ALL","ALNY",
"ANR","ALTR","MO","AMZN","ACO","DOX","AMED","AEE",
"AXL","ACAS","AEO","ECOL","AEP","AXP","AFG","AM","AIG","AWR"
}; */
	public String getVariables(){
		StringBuffer sb=new StringBuffer();
		sb.append("startDate: ");
		sb.append(startDate);
		sb.append("\n");
		sb.append("CurPeriod: ");
		sb.append(CurPeriod);
		sb.append("\n");
		sb.append("NumberOfDaysSwitching: ");
		sb.append(NumberOfDaysSwitching);
		sb.append("\n");
		sb.append("n: ");
		sb.append(n);
		sb.append("\n");
		sb.append("originalAmount: ");
		sb.append(originalAmount);
		sb.append("\n");
		return sb.toString();
	}
	
	public void writeObject(ObjectOutputStream stream) throws IOException{
		stream.writeObject(startDate);
		stream.writeObject(CurPeriod);
		stream.writeObject(NumberOfDaysSwitching);
		stream.writeObject(n);
		stream.writeObject(originalAmount);
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		startDate=(Date)stream.readObject();;
		CurPeriod=(Integer)stream.readObject();;
		NumberOfDaysSwitching=(Integer)stream.readObject();;
		n=(Integer)stream.readObject();;
		originalAmount=(Double)stream.readObject();;
	}
	
	//----------------------------------------------------
	//user defined functions
	//----------------------------------------------------	
	public  boolean MatchDate(Date CurDate,Date StartDate)
 throws Exception {
 for(int i=0;i<HoldingPeriod;i++)
{
if(CurDate.equals(LTIDate.getNewNYSEMonth(StartDate, i)))
return true;
}
return false;
}

public void Buy(String[] sortArray,Double TotalAmount,Date CurDate,int CurPeriod,int n)
 throws Exception {
Asset CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset-" + CurPeriod);
CurrentAsset.setTargetPercentage(1.0/HoldingPeriod);
CurrentPortfolio.addAsset(CurrentAsset);
for(int m=0;m<n;m++)
{
CurrentPortfolio.buy("Asset-"+CurPeriod, getSecurity(sortArray[m]).getName(), TotalAmount * ((1.0/HoldingPeriod)/n), CurDate);
}
}

	//----------------------------------------------------
	//initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void init() throws Exception{
		List<String> list1=new ArrayList();
Date returnStartDate=LTIDate.getNewNYSEWeek(LTIDate.getNewNYSEMonth(CurrentDate,-PastReturnPeriod),-1);

for(int i=0;i<nameArray.length;i++)
{
try{
 if(!securityManager.getStartDate(nameArray[i]).after(returnStartDate))
 {list1.add(nameArray[i]);}
}catch(Exception e){}
}

String[] selectedArray=(String[])list1.toArray(new String[]{});
printToLog("selected stocks are:");
for(int i=0;i<selectedArray.length;i++){
printToLog(selectedArray[i]);
}
printToLog("selectedArray length is:"+selectedArray.length);
printToLog("holding period is:"+HoldingPeriod);

n=(10*selectedArray.length)/(100*HoldingPeriod);
printToLog("n is:"+n);
if(n>0)
{
startDate=CurrentDate;
NumberOfDaysSwitching=HoldingPeriod*30;
double TotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);
originalAmount=TotalAmount;
printToLog("In Initial\n");

Date beforeOneWeek = LTIDate.getNewNYSEWeek(CurrentDate,-1);
String[]sortArray=getTopSecurityArray(selectedArray,-1*PastReturnPeriod,beforeOneWeek ,TimeUnit.MONTHLY,SortType.RETURN,true);
Buy(sortArray,TotalAmount,CurrentDate,CurPeriod,n);
printToLog("Current Cash is:"+CurrentPortfolio.getCash());

CurPeriod++;
}
else
printToLog("Can not get any stock  \n");
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
		
		 
		else if (n>0&&LTIDate.calculateInterval(startDate, CurrentDate)<NumberOfDaysSwitching&&MatchDate(CurrentDate,startDate)&&CurPeriod<(HoldingPeriod+1)) {
		   List<String> list1=new ArrayList();
Date returnStartDate=LTIDate.getNewNYSEWeek(LTIDate.getNewNYSEMonth(CurrentDate,-PastReturnPeriod),-1);
for(int i=0;i<nameArray.length;i++)
{
try{
 if(!securityManager.getStartDate(nameArray[i]).after(returnStartDate))
 {list1.add(nameArray[i]);}
}catch(Exception e){}
}
String[] selectedArray=(String[])list1.toArray(new String[]{});
/*printToLog("selected stocks are:");
for(int i=0;i<selectedArray.length;i++){
printToLog(selectedArray[i]);
}
printToLog("selectedArray length is:"+selectedArray.length);
*/

n=(10*selectedArray.length)/(100*HoldingPeriod);
printToLog("n is:"+n);

printToLog("In Initial\n");
Date beforeOneWeek = LTIDate.getNewNYSEWeek(CurrentDate,-1);
String[]sortArray=getTopSecurityArray(selectedArray,-1*PastReturnPeriod,beforeOneWeek,TimeUnit.MONTHLY,SortType.RETURN,true);
Buy(sortArray,originalAmount,CurrentDate,CurPeriod,n);
printToLog("Current Cash is:"+CurrentPortfolio.getCash());

CurPeriod++;
		}
		else if (n>0&&CurrentDate.equals(LTIDate.getNewNYSEMonth(startDate, HoldingPeriod))) {
		   List<String> list1=new ArrayList();
Date returnStartDate=LTIDate.getNewNYSEWeek(LTIDate.getNewNYSEMonth(CurrentDate,-PastReturnPeriod),-1);
for(int i=0;i<nameArray.length;i++)
{
try{
 if(!securityManager.getStartDate(nameArray[i]).after(returnStartDate))
 {list1.add(nameArray[i]);}
}catch(Exception e){}
}
String[] selectedArray=(String[])list1.toArray(new String[]{});
/*printToLog("selected stocks are:");
for(int i=0;i<selectedArray.length;i++){
printToLog(selectedArray[i]);
}
printToLog("selectedArray length is:"+selectedArray.length);
*/
startDate=CurrentDate;
int LastPeriod=CurPeriod-HoldingPeriod;


n=(10*selectedArray.length)/(100*HoldingPeriod);
printToLog("n is:"+n);

CurrentPortfolio.sellAndDelAsset("Asset-" + LastPeriod,CurrentDate);

double TotalAmount = CurrentPortfolio.getCash();
originalAmount=TotalAmount;

Date beforeOneWeek = LTIDate.getNewNYSEWeek(CurrentDate,-1);
String[]sortArray=getTopSecurityArray(selectedArray,-1*PastReturnPeriod,beforeOneWeek,TimeUnit.MONTHLY,SortType.RETURN,true);
Buy(sortArray,TotalAmount,CurrentDate,CurPeriod,n);
printToLog("Current Cash is:"+CurrentPortfolio.getCash());

CurPeriod++;
		}
		else if (n>0&&LTIDate.calculateInterval(startDate, CurrentDate)<NumberOfDaysSwitching&&MatchDate(CurrentDate,startDate)&&CurPeriod>(HoldingPeriod+1)) {
		   List<String> list1=new ArrayList();
Date returnStartDate=LTIDate.getNewNYSEWeek(LTIDate.getNewNYSEMonth(CurrentDate,-PastReturnPeriod),-1);
for(int i=0;i<nameArray.length;i++)
{
try{
 if(!securityManager.getStartDate(nameArray[i]).after(returnStartDate))
 {list1.add(nameArray[i]);}
}catch(Exception e){}
}
String[] selectedArray=(String[])list1.toArray(new String[]{});
/*printToLog("selected stocks are:");
for(int i=0;i<selectedArray.length;i++){
printToLog(selectedArray[i]);
}
printToLog("selectedArray length is:"+selectedArray.length);
*/
int LastPeriod=CurPeriod-HoldingPeriod;

n=(10*selectedArray.length)/(100*HoldingPeriod);

List<Security>sellArray=CurrentPortfolio. getAssetSecurity("Asset-" + LastPeriod,CurrentDate);
CurrentPortfolio.sellAndDelAsset("Asset-" + LastPeriod,CurrentDate);

Date beforeOneWeek = LTIDate.getNewNYSEWeek(CurrentDate,-1);
String[]sortArray=getTopSecurityArray(selectedArray,-1*PastReturnPeriod,beforeOneWeek,TimeUnit.MONTHLY,SortType.RETURN,true);
Buy(sortArray,originalAmount,CurrentDate,CurPeriod,n);
printToLog("Current Cash is:"+CurrentPortfolio.getCash());

CurPeriod++;
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