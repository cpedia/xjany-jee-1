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
public class RAA_TOP3_Short431 extends SimulateStrategy{
	public RAA_TOP3_Short431(){
		super();
		StrategyID=431L;
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
int freq=20;

int interval=40;
Date NextDate;
String[] Index={"VFINX", "VGTSX", "VGSIX", "QRAAX", "BEGBX","VBMFX", "CASH" };
String[] class1={
"AASFX", "ALHIX",  "ALPHX", "ANGLX", "ARBNX", "BETAX", "BMNIX", "DDMSX", "DVRIX", "DVRKX", 
"DVRWX", "FGLSX", "FLSIX", "GHESX", "GMNSX", "GSRTX", "GTAPX", "GTEYX", "GTWNX", "HEOZX",
"HSGFX", "IOLZX", "JAMNX", "NARFX", "REAFX", "REAOX", "REYRX", "RTNRX", "SDCQX", "SMIVX",
"SUEIX", "SWHEX", "TFSMX", "TOPFX", "VPDAX","CASH","CASH","CASH"};
int n=Index.length;
	public String getVariables(){
		StringBuffer sb=new StringBuffer();
		sb.append("T: ");
		sb.append(T);
		sb.append("\n");
		sb.append("freq: ");
		sb.append(freq);
		sb.append("\n");
		sb.append("interval: ");
		sb.append(interval);
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
		sb.append("n: ");
		sb.append(n);
		sb.append("\n");
		return sb.toString();
	}
	
	public void writeObject(ObjectOutputStream stream) throws IOException{
		stream.writeObject(T);
		stream.writeObject(freq);
		stream.writeObject(interval);
		stream.writeObject(NextDate);
		stream.writeObject(Index);
		stream.writeObject(class1);
		stream.writeObject(n);
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		T=(Integer)stream.readObject();;
		freq=(Integer)stream.readObject();;
		interval=(Integer)stream.readObject();;
		NextDate=(Date)stream.readObject();;
		Index=(String[])stream.readObject();;
		class1=(String[])stream.readObject();;
		n=(Integer)stream.readObject();;
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
CurrentAsset.setName("RAA_TOP3_SHORT");
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
		   List<Security>  TopSecurityArray=new ArrayList<Security>();
List <Security> list2= getSecurityList(class1);
String assetnameArray;
TopSecurityArray=getTopSecurity(list2,3,-1,CurrentDate,TimeUnit.YEARLY,SortType.SHARPE,true);

//get current portfolio's asset list 
List<Asset> list1=CurrentPortfolio.getCurrentAssetList();
Asset as=list1.get(0);
assetnameArray=as.getName();

//get current security amount
double[] CurrentAmount=new double[n];
List <Security> CurrentAssetSecurity=new ArrayList<Security>();
CurrentAssetSecurity=getAssetSecurity(assetnameArray,CurrentDate);
int m=CurrentAssetSecurity.length;
for(int i=0;i<m;i++)
{
CurrentAmount=getSecurityAmount(assetnameArray, CurrentAssetSecurity.get(i).name(),CurrentDate);
}

//get new security amount
double[] NewShare=new double[n];
double[] temp=new double[n+1];
for(i=0;i<3;i++)
{
temp=RAA(interval,CurrentDate,TimeUnit.DAILY,TopSecirityArray.get(i),Index,true);
for(int k=0;k<n;k++)
    NewAmount[k]+=temp[k]*totalAmount/3;    
}

//adjust the portfolio
double AdjustAmount=new double[n];
AdjustAmount=CurrentAmount-NewAmount;
for(int i=0;i<n;i++)
{
if((AjustAmount>0)&&(NewAmount>0))
CurrentPortfolio.sell(assetnameArray, CurrentAssetSecurity.get(i).name(),AjustAmount[i], CurrentDate);  
if((AjustAmount>0)&&(NewAmount<0))
{
CurrentPortfolio.sell(assetnameArray, CurrentAssetSecurity.get(i).name(),CurrentAmount[i], CurrentDate);  
CurrentPortfolio.shortSell(assetnameArray, CurrentAssetSecurity.get(i).name(),-1*NewAmount, CurrentDate);  
}
}

for(int j=0;j<n;j++)
{
if(AjustAmount<0)
CurrentPortfolio.buy(assetnameArray, CurrentAssetSecurity.get(j).name(),-1*AjustAmount[j], CurrentDate);  
}


NextDate=LTIDate.getNewNYSETradingDay(CurrentDate,freq);
printToLog(NextDate);   

		}
		
		
		else{
		
			;
		}
		
	}
	
	public double getVersion(){
		return version;
	}
	
}

//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\RAA_TOP3_Short431.java:166: ~0&÷
//&÷ ØÏ length
//Mn ¥ã java.util.List<com.lti.service.bo.Security>
//int m=CurrentAssetSecurity.length;
//                          ^
//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\RAA_TOP3_Short431.java:169: ~0&÷
//&÷ ¹Õ name()
//Mn { com.lti.service.bo.Security
//CurrentAmount=getSecurityAmount(assetnameArray, CurrentAssetSecurity.get(i).name(),CurrentDate);
//                                                                           ^
//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\RAA_TOP3_Short431.java:175: ~0&÷
//&÷ ØÏ i
//Mn { com.lti.compiledstrategy.RAA_TOP3_Short431
//for(i=0;i<3;i++)
//    ^
//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\RAA_TOP3_Short431.java:175: ~0&÷
//&÷ ØÏ i
//Mn { com.lti.compiledstrategy.RAA_TOP3_Short431
//for(i=0;i<3;i++)
//        ^
//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\RAA_TOP3_Short431.java:175: ~0&÷
//&÷ ØÏ i
//Mn { com.lti.compiledstrategy.RAA_TOP3_Short431
//for(i=0;i<3;i++)
//            ^
//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\RAA_TOP3_Short431.java:177: ~0&÷
//&÷ ØÏ i
//Mn { com.lti.compiledstrategy.RAA_TOP3_Short431
//temp=RAA(interval,CurrentDate,TimeUnit.DAILY,TopSecirityArray.get(i),Index,true);
//                                                                  ^
//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\RAA_TOP3_Short431.java:177: ~0&÷
//&÷ ØÏ TopSecirityArray
//Mn { com.lti.compiledstrategy.RAA_TOP3_Short431
//temp=RAA(interval,CurrentDate,TimeUnit.DAILY,TopSecirityArray.get(i),Index,true);
//                                             ^
//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\RAA_TOP3_Short431.java:179: ~0&÷
//&÷ ØÏ NewAmount
//Mn { com.lti.compiledstrategy.RAA_TOP3_Short431
//    NewAmount[k]+=temp[k]*totalAmount/3;    
//    ^
//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\RAA_TOP3_Short431.java:179: ~0&÷
//&÷ ØÏ totalAmount
//Mn { com.lti.compiledstrategy.RAA_TOP3_Short431
//    NewAmount[k]+=temp[k]*totalAmount/3;    
//                          ^
//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\RAA_TOP3_Short431.java:183: |¹„{‹
//~0 double[]
//  double
//double AdjustAmount=new double[n];
//                    ^
//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\RAA_TOP3_Short431.java:184: ~0&÷
//&÷ ØÏ NewAmount
//Mn { com.lti.compiledstrategy.RAA_TOP3_Short431
//AdjustAmount=CurrentAmount-NewAmount;
//                           ^
//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\RAA_TOP3_Short431.java:187: ~0&÷
//&÷ ØÏ AjustAmount
//Mn { com.lti.compiledstrategy.RAA_TOP3_Short431
//if((AjustAmount>0)&&(NewAmount>0))
//    ^
//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\RAA_TOP3_Short431.java:187: ~0&÷
//&÷ ØÏ NewAmount
//Mn { com.lti.compiledstrategy.RAA_TOP3_Short431
//if((AjustAmount>0)&&(NewAmount>0))
//                     ^
//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\RAA_TOP3_Short431.java:188: ~0&÷
//&÷ ¹Õ name()
//Mn { com.lti.service.bo.Security
//CurrentPortfolio.sell(assetnameArray, CurrentAssetSecurity.get(i).name(),AjustAmount[i], CurrentDate);  
//                                                                 ^
//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\RAA_TOP3_Short431.java:188: ~0&÷
//&÷ ØÏ AjustAmount
//Mn { com.lti.compiledstrategy.RAA_TOP3_Short431
//CurrentPortfolio.sell(assetnameArray, CurrentAssetSecurity.get(i).name(),AjustAmount[i], CurrentDate);  
//                                                                         ^
//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\RAA_TOP3_Short431.java:189: ~0&÷
//&÷ ØÏ AjustAmount
//Mn { com.lti.compiledstrategy.RAA_TOP3_Short431
//if((AjustAmount>0)&&(NewAmount<0))
//    ^
//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\RAA_TOP3_Short431.java:189: ~0&÷
//&÷ ØÏ NewAmount
//Mn { com.lti.compiledstrategy.RAA_TOP3_Short431
//if((AjustAmount>0)&&(NewAmount<0))
//                     ^
//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\RAA_TOP3_Short431.java:191: ~0&÷
//&÷ ¹Õ name()
//Mn { com.lti.service.bo.Security
//CurrentPortfolio.sell(assetnameArray, CurrentAssetSecurity.get(i).name(),CurrentAmount[i], CurrentDate);  
//                                                                 ^
//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\RAA_TOP3_Short431.java:192: ~0&÷
//&÷ ¹Õ name()
//Mn { com.lti.service.bo.Security
//CurrentPortfolio.shortSell(assetnameArray, CurrentAssetSecurity.get(i).name(),-1*NewAmount, CurrentDate);  
//                                                                      ^
//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\RAA_TOP3_Short431.java:192: ~0&÷
//&÷ ØÏ NewAmount
//Mn { com.lti.compiledstrategy.RAA_TOP3_Short431
//CurrentPortfolio.shortSell(assetnameArray, CurrentAssetSecurity.get(i).name(),-1*NewAmount, CurrentDate);  
//                                                                                 ^
//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\RAA_TOP3_Short431.java:198: ~0&÷
//&÷ ØÏ AjustAmount
//Mn { com.lti.compiledstrategy.RAA_TOP3_Short431
//if(AjustAmount<0)
//   ^
//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\RAA_TOP3_Short431.java:199: ~0&÷
//&÷ ¹Õ name()
//Mn { com.lti.service.bo.Security
//CurrentPortfolio.buy(assetnameArray, CurrentAssetSecurity.get(j).name(),-1*AjustAmount[j], CurrentDate);  
//                                                                ^
//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\RAA_TOP3_Short431.java:199: ~0&÷
//&÷ ØÏ AjustAmount
//Mn { com.lti.compiledstrategy.RAA_TOP3_Short431
//CurrentPortfolio.buy(assetnameArray, CurrentAssetSecurity.get(j).name(),-1*AjustAmount[j], CurrentDate);  
//                                                                           ^
//23 ï