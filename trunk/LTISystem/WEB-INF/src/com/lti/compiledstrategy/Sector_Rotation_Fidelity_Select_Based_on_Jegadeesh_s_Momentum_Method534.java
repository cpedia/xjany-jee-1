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
public class Sector_Rotation_Fidelity_Select_Based_on_Jegadeesh_s_Momentum_Method534 extends SimulateStrategy{
	public Sector_Rotation_Fidelity_Select_Based_on_Jegadeesh_s_Momentum_Method534(){
		super();
		StrategyID=534L;
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
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		HoldingPeriod=(Integer)ParameterUtil.fetchParameter("int","HoldingPeriod", "3", parameters);
		PastReturnPeriod=(Integer)ParameterUtil.fetchParameter("int","PastReturnPeriod", "3", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	Date startDate;
int CurPeriod=1;
int NumberOfDaysSwitching;
int n;
double originalAmount;
String[] nameArray = {"FSAIX", "FSAVX", "FSRBX",  "FBIOX",
"FSLBX",
"FSCHX",
"FSDCX",
"FDCPX",
"FSHOX",
"FSCPX",
"FDFAX",
"FSDAX",
"FSELX",
"FSENX",
"FSESX",
"FSLEX",
"FIDSX",
"FSAGX",
"FSPHX",
"FSVLX",
"FSCGX",
"FCYIX",
"FSPCX",
"FBSOX",
"FDLSX",
"FSDPX",
"FSHCX",
"FSMEX",
"FBMPX",
"FSNGX",
"FNARX",
"FNINX",
"FSPFX",
"FPHAX",
"FSRPX",
"FSCSX",
"FSPTX",
"FSTCX",
"FSRFX",
"FSUTX",
"FWRLX",
"FSRRX",
"FIUIX"};

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
		sb.append("nameArray: ");
		sb.append(nameArray);
		sb.append("\n");
		return sb.toString();
	}
	
	public void writeObject(ObjectOutputStream stream) throws IOException{
		stream.writeObject(startDate);
		stream.writeObject(CurPeriod);
		stream.writeObject(NumberOfDaysSwitching);
		stream.writeObject(n);
		stream.writeObject(originalAmount);
		stream.writeObject(nameArray);
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		startDate=(Date)stream.readObject();;
		CurPeriod=(Integer)stream.readObject();;
		NumberOfDaysSwitching=(Integer)stream.readObject();;
		n=(Integer)stream.readObject();;
		originalAmount=(Double)stream.readObject();;
		nameArray=(String[])stream.readObject();;
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

public void InitialBuy(String[] sortArray,Double TotalAmount,Date CurDate,int CurPeriod,int n)
 throws Exception {
Asset CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset-" + CurPeriod);
CurrentAsset.setTargetPercentage(1.0/HoldingPeriod);
CurrentPortfolio.addAsset(CurrentAsset);
for(int m=0;m<n;m++)
{
CurrentPortfolio.buy("Asset-" + CurPeriod, getSecurity(sortArray[m]).getName(), TotalAmount * ((1.0/HoldingPeriod)/n),CurDate);
}
}

public void Buy(String[] sortArray,List<Security>sellArray,Double TotalAmount,Date CurDate,int CurPeriod,int n)
 throws Exception {
Date DateAfterOneWeek = LTIDate.getNewNYSEWeek(CurDate, 1);
Asset CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset-" + CurPeriod);
CurrentAsset.setTargetPercentage(1.0/HoldingPeriod);
CurrentPortfolio.addAsset(CurrentAsset);
for(int m=0;m<n;m++)
{
if(sellArray.contains(getSecurity(sortArray[m])))
CurrentPortfolio.buy("Asset-"+CurPeriod, getSecurity(sortArray[m]).getName(), TotalAmount * ((1.0/HoldingPeriod)/n), DateAfterOneWeek);
else
CurrentPortfolio.buy("Asset-"+CurPeriod, getSecurity(sortArray[m]).getName(), TotalAmount * ((1.0/HoldingPeriod)/n), CurDate);
}
}

	//----------------------------------------------------
	//initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void init() throws Exception{
		List<String> list1=new ArrayList();
for(int i=0;i<nameArray.length;i++)
{
try{
 if(!securityManager.getStartDate(nameArray[i]).after(LTIDate.getNewNYSEMonth(CurrentDate,-PastReturnPeriod)))
 {list1.add(nameArray[i]);}
}catch(Exception e){}
}
String[] selectedArray=(String[])list1.toArray(new String[]{});
n=(10*selectedArray.length)/(100*HoldingPeriod);
if(n>0)
{
startDate=CurrentDate;
NumberOfDaysSwitching=HoldingPeriod*30;
double TotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);
originalAmount=TotalAmount;
printToLog("In Initial\n");

String[]sortArray=getTopSecurityArray(selectedArray,-1*PastReturnPeriod,CurrentDate,TimeUnit.MONTHLY,SortType.RETURN,true);
InitialBuy(sortArray,TotalAmount,CurrentDate,CurPeriod,n);
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
for(int i=0;i<nameArray.length;i++)
{
try{
 if(!securityManager.getStartDate(nameArray[i]).after(LTIDate.getNewNYSEMonth(CurrentDate,-PastReturnPeriod)))
 {list1.add(nameArray[i]);}
}catch(Exception e){}
}
String[] selectedArray=(String[])list1.toArray(new String[]{});
n=(10*selectedArray.length)/(100*HoldingPeriod);

printToLog("In Initial\n");
String[]sortArray=getTopSecurityArray(selectedArray,-1*PastReturnPeriod,CurrentDate,TimeUnit.MONTHLY,SortType.RETURN,true);
InitialBuy(sortArray,originalAmount,CurrentDate,CurPeriod,n);
printToLog("Current Cash is:"+CurrentPortfolio.getCash());

CurPeriod++;

		}
		else if (n>0&&CurrentDate.equals(LTIDate.getNewNYSEMonth(startDate, HoldingPeriod))) {
		   List<String> list1=new ArrayList();
for(int i=0;i<nameArray.length;i++)
{
try{
 if(!securityManager.getStartDate(nameArray[i]).after(LTIDate.getNewNYSEMonth(CurrentDate,-PastReturnPeriod)))
 {list1.add(nameArray[i]);}
}catch(Exception e){}
}
String[] selectedArray=(String[])list1.toArray(new String[]{});
startDate=CurrentDate;
int LastPeriod=CurPeriod-HoldingPeriod;
n=(10*selectedArray.length)/(100*HoldingPeriod);

List<Security>sellArray=CurrentPortfolio. getAssetSecurity("Asset-" + LastPeriod,CurrentDate);
CurrentPortfolio.sellAndDelAsset("Asset-" + LastPeriod,CurrentDate);

double TotalAmount = CurrentPortfolio.getCash();
originalAmount=TotalAmount;

String[]sortArray=getTopSecurityArray(selectedArray,-1*PastReturnPeriod,CurrentDate,TimeUnit.MONTHLY,SortType.RETURN,true);
Buy(sortArray,sellArray,TotalAmount,CurrentDate,CurPeriod,n);
printToLog("Current Cash is:"+CurrentPortfolio.getCash());

CurPeriod++;
		}
		else if (n>0&&LTIDate.calculateInterval(startDate, CurrentDate)<NumberOfDaysSwitching&&MatchDate(CurrentDate,startDate)&&CurPeriod>(HoldingPeriod+1)) {
		   List<String> list1=new ArrayList();
for(int i=0;i<nameArray.length;i++)
{
try{
 if(!securityManager.getStartDate(nameArray[i]).after(LTIDate.getNewNYSEMonth(CurrentDate,-PastReturnPeriod)))
 {list1.add(nameArray[i]);}
}catch(Exception e){}
}
String[] selectedArray=(String[])list1.toArray(new String[]{});
int LastPeriod=CurPeriod-HoldingPeriod;
n=(10*selectedArray.length)/(100*HoldingPeriod);

List<Security>sellArray=CurrentPortfolio. getAssetSecurity("Asset-" + LastPeriod,CurrentDate);
CurrentPortfolio.sellAndDelAsset("Asset-" + LastPeriod,CurrentDate);

String[]sortArray=getTopSecurityArray(selectedArray,-1*PastReturnPeriod,CurrentDate,TimeUnit.MONTHLY,SortType.RETURN,true);
Buy(sortArray,sellArray,originalAmount,CurrentDate,CurPeriod,n);
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