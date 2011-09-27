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
public class Dow_Theory273 extends SimulateStrategy{
	public Dow_Theory273(){
		super();
		StrategyID=273L;
		StrategyClassID=9L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	private String UnderlyingSecurity;
	public void setUnderlyingSecurity(String UnderlyingSecurity){
		this.UnderlyingSecurity=UnderlyingSecurity;
	}
	
	public String getUnderlyingSecurity(){
		return this.UnderlyingSecurity;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		UnderlyingSecurity=(String)ParameterUtil.fetchParameter("String","UnderlyingSecurity", "^DJI", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	static int n=2048;
double Infinate=1000000;
double Ninfinate=-1000000;
double []DJILocalMax=new double[n];
double []DJTLocalMax=new double[n];
double []DJILocalMin=new double[n];
double []DJTLocalMin=new double[n];
double [][]DJIPullback=new double[3][n];
double [][]DJTPullback=new double[3][n];
int DJIMaxCount;
int DJTMaxCount;
int DJIMinCount;
int DJTMinCount;
int DJIPbLength; //Current length of the pullbacks in the pullback array.
int DJTPbLength; 
boolean NewMax;  //Whether there is a local max inserted into the LocalMax array.
boolean NewMin;  //Whether there is a local min inserted into the Localmin array.
boolean position;
boolean DJIBuy;
boolean DJTBuy;
boolean DJISell;
boolean DJTSell;
boolean isDJTA;
String DJIA="^DJI";
String DJTA="^DJT";
	public String getVariables(){
		StringBuffer sb=new StringBuffer();
		return sb.toString();
	}
	
	public void writeObject(ObjectOutputStream stream) throws IOException{
		return;
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		return;
	}
	
	//----------------------------------------------------
	//user defined functions
	//----------------------------------------------------	
	public void updateBullLocalPrices(String str,double []Max,double []Min,boolean isDJT,Date CurDate) throws Exception {
	Security s=getSecurity(str);
	double CurrentPrice=s.getCurrentPrice(CurDate);
	double PrePrice=s.getCurrentPrice(LTIDate.getNewNYSEWeek(CurDate,-1));
	double PPrePrice=0.0;
	NewMax=false;
	NewMin=false;
	int kmax;
	int kmin;
	if(isDJT){
		kmax=DJTMaxCount;
		kmin=DJTMinCount;
	}
	else{
		kmax=DJIMaxCount;
		kmin=DJIMinCount;
	}
	if(Min[0]==0)
	Min[0]=CurrentPrice;
	else if(Max[0]==0){
		if(Min[0]>CurrentPrice)
		Min[0]=CurrentPrice;
		else if(Min[0]<CurrentPrice){
			Max[0]=CurrentPrice;
			NewMin=true;
		}
	}
	if(Max[0]!=0&&Min[1]==0){
		if(Max[0]<=CurrentPrice)
		Max[0]=CurrentPrice;
		else{
			Min[1]=CurrentPrice;
			NewMax=true;
		}
	}
	else if(Min[1]!=0){
		PPrePrice=s.getCurrentPrice(LTIDate.getNewNYSEWeek(CurDate,-2));
		if(PrePrice>Min[kmin]&&PrePrice>CurrentPrice&&PrePrice>=PPrePrice){
			if(isDJT)
			Max[++DJTMaxCount]=PrePrice;
			else
			Max[++DJIMaxCount]=PrePrice;
			NewMax=true;
		}
		else if(PrePrice<Max[kmax]&&PrePrice<CurrentPrice&&PrePrice<=PPrePrice){
			if(isDJT)
			Min[++DJTMinCount]=PrePrice;
			else
			Min[++DJIMinCount]=PrePrice;
			NewMin=true;
		}
	}
}

public void updateBearLocalPrices(String str,double []Max,double []Min,boolean isDJT,Date CurDate) throws Exception {
	Security s=getSecurity(str);
	double CurrentPrice=s.getCurrentPrice(CurDate);
	double PrePrice=s.getCurrentPrice(LTIDate.getNewNYSEWeek(CurDate,-1));
	double PPrePrice=0.0;
	NewMax=false;
	NewMin=false;
	int kmax=0;
	int kmin=0;
	if(isDJT){
		kmax=DJTMaxCount;
		kmin=DJTMinCount;
	}
	else{
		kmax=DJIMaxCount;
		kmin=DJIMinCount;
	}
	if(Max[0]==0)
	Max[0]=CurrentPrice;
	else if(Min[0]==0){
		if(Max[0]<CurrentPrice)
		Max[0]=CurrentPrice;
		else if(Max[0]>CurrentPrice){
			Min[0]=CurrentPrice;
			NewMax=true;
		}
	}
	if(Min[0]!=0&&Max[1]==0){
		if(Min[0]>=CurrentPrice)
		Min[0]=CurrentPrice;
		else{
			Max[1]=CurrentPrice;
			NewMin=true;
		}
	}
	else if(Max[1]!=0){
		PPrePrice=s.getCurrentPrice(LTIDate.getNewNYSEWeek(CurDate,-2));
		if(PrePrice>Min[kmin]&&PrePrice>CurrentPrice&&PrePrice>=PPrePrice){
			if(isDJT)
			Max[++DJTMaxCount]=PrePrice;
			else
			Max[++DJIMaxCount]=PrePrice;
			NewMax=true;
		}
		else if(PrePrice<Max[kmax]&&PrePrice<CurrentPrice&&PrePrice<=PPrePrice){
			if(isDJT)
			Min[++DJTMinCount]=PrePrice;
			else
			Min[++DJIMinCount]=PrePrice;
			NewMin=true;
		}
	}
}

//The following function is to update the biggest pullback array by far.
public void updateBullPullback(double []Max, double []Min, double [][]Pullback,boolean isDJT) throws Exception {
	int t=0;
	int Pbl=0;
	int kmax;
	int kmin;
	if(isDJT){
		Pbl=DJTPbLength;
		kmax=DJTMaxCount;
		kmin=DJTMinCount;
	}
	else{
		Pbl=DJIPbLength;
		kmax=DJIMaxCount;
		kmin=DJIMinCount;
	}
	double wave=0;
	if(NewMin){
		if(Pullback[0][0]<0){
			if(Min[1]!=0){
				wave=(Max[0]-Min[1])/Max[0];
				Pullback[0][0]=Max[0];
				Pullback[1][0]=Min[1];
				Pullback[2][0]=wave;
				if(isDJT)
				DJTPbLength++;
				else
				DJIPbLength++;
			}
		}
		else{
			t=Pbl-1;
			for(int i=t;i>=0;i--){
				if(Min[kmin]<Pullback[1][i]){
					wave=(Pullback[0][i]-Min[kmin])/Pullback[0][i];
					Pullback[1][i]=Min[kmin];
					Pullback[2][i]=wave;
				}
			}
		}
	}
	if(NewMax){
		Pullback[0][Pbl]=Max[kmax];
		Pullback[1][Pbl]=Infinate;
		if(isDJT)
		DJTPbLength++;
		else
		DJIPbLength++;
	}
					
}

//The following function is to update the biggest pullback array by far.
public void updateBearPullback(double []Max, double []Min, double [][]Pullback,boolean isDJT) throws Exception {
	int t=0;
	int Pbl=0;
	int kmax;
	int kmin;
	if(isDJT){
		Pbl=DJTPbLength;
		kmax=DJTMaxCount;
		kmin=DJTMinCount;
	}
	else{
		Pbl=DJIPbLength;
		kmax=DJIMaxCount;
		kmin=DJIMinCount;
	}
	double wave=0;
	if(NewMax){
		if(Pullback[0][0]<0){
			if(Max[1]!=0){
				wave=(Max[1]-Min[0])/Min[0];
				Pullback[0][0]=Min[0];
				Pullback[1][0]=Max[1];
				Pullback[2][0]=wave;
				if(isDJT)
				DJTPbLength++;
				else
				DJIPbLength++;
			}
		}
		else{
			t=Pbl-1;
			for(int i=t;i>=0;i--){
				if(Max[kmax]>Pullback[1][i]){
					wave=(Max[kmax]-Pullback[0][i])/Pullback[0][i];
					Pullback[1][i]=Max[kmax];
					Pullback[2][i]=wave;
				}
			}
		}
	}
	if(NewMin){
		Pullback[0][Pbl]=Min[kmin];
		Pullback[1][Pbl]=Ninfinate;
		if(isDJT)
		DJTPbLength++;
		else
		DJIPbLength++;
	}					
}


public void initialLocalPrices() throws Exception {
	for(int i=0;i<n;i++){
		DJILocalMax[i]=0;
		DJTLocalMax[i]=0;
		DJILocalMin[i]=0;
		DJTLocalMin[i]=0;
	}
}

public void initialPullback() throws Exception {
	for(int i=0;i<3;i++)
	for(int j=0;j<n;j++){
		DJIPullback[i][j]=-1;
		DJTPullback[i][j]=-1;
	}
}


public void reset() throws Exception {
	int i;
	int j;
	for(i=0;i<=DJIMaxCount;i++){
		DJILocalMax[i]=0;
	}
	for(j=0;j<=DJIMinCount;j++){
		DJILocalMin[j]=0;
	}
	for(i=0;i<=DJTMaxCount;i++){
		DJTLocalMax[i]=0;
	}
	for(j=0;j<=DJTMinCount;j++){
		DJTLocalMin[j]=0;
	}
	DJIMaxCount=0;
	DJTMaxCount=0;
	DJIMinCount=0;
	DJTMinCount=0;
	DJIBuy=false;
	DJTBuy=false;
	DJISell=false;
	DJTSell=false;
}

public int FindMax(double []Max,int t,int MaxCount) throws Exception {
	int k=t;
	double MaxNum=Max[t];
	for(int i=t+1;i<MaxCount;i++){
		if(MaxNum<Max[i]){
			MaxNum=Max[i];
			k=i;
		}
	}
	return k;
}

public int FindMin(double []Min,int t,int MinCount) throws Exception {
	int k=t;
	double MinNum=Min[t];
	for(int i=t+1;i<MinCount;i++){
		if(MinNum>Min[i]){
			MinNum=Min[i];
			k=i;
		}
	}
	return k;
}



public void buyReset() throws Exception {
	int i;
	int j;
	int DJIMaxPos=-1;
	int DJTMaxPos=-1;
	double wave;
	for(i=0;i<3;i++)
	for(j=0;j<DJIPbLength;j++)
	DJIPullback[i][j]=-1;
	for(i=0;i<3;i++)
	for(j=0;j<DJTPbLength;j++)
	DJTPullback[i][j]=-1;
	DJIPbLength=0;
	DJTPbLength=0;
	while(DJIPbLength!=DJIMaxCount){
		int MP=DJIMaxPos+1;
		DJIMaxPos=FindMax(DJILocalMax,MP,DJIMaxCount);
		for(i=MP;i<=DJIMaxPos;i++){
			wave=(DJILocalMax[DJIMaxPos]-DJILocalMin[i])/DJILocalMin[i];
			DJIPullback[0][i]=DJILocalMin[i];
			DJIPullback[1][i]=DJILocalMax[DJIMaxPos];
			DJIPullback[2][i]=wave;
		}
		DJIPbLength=DJIMaxPos;
	}
	DJIPullback[0][++DJIPbLength]=DJILocalMin[DJIMinCount];
	DJIPullback[1][DJIPbLength]=Ninfinate;
	DJIPbLength++;
	//
	while(DJTPbLength!=DJTMaxCount){
		int MP=DJTMaxPos+1;
		DJTMaxPos=FindMax(DJTLocalMax,MP,DJTMaxCount);
		for(i=MP;i<=DJTMaxPos;i++){
			wave=(DJTLocalMax[DJTMaxPos]-DJTLocalMin[i])/DJTLocalMin[i];
			DJTPullback[0][i]=DJTLocalMin[i];
			DJTPullback[1][i]=DJTLocalMax[DJTMaxPos];
			DJTPullback[2][i]=wave;
		}
		DJTPbLength=DJTMaxPos;
	}
	DJTPullback[0][++DJTPbLength]=DJTLocalMin[DJTMinCount];
	DJTPullback[1][DJTPbLength]=Ninfinate;
	DJTPbLength++;
	reset();
}
		
public void sellReset() throws Exception {
	int i;
	int j;
	int DJIMinPos=-1;
	int DJTMinPos=-1;
	double wave;
	for(i=0;i<3;i++)
	for(j=0;j<DJIPbLength;j++)
	DJIPullback[i][j]=-1;
	for(i=0;i<3;i++)
	for(j=0;j<DJTPbLength;j++)
	DJTPullback[i][j]=-1;
	DJIPbLength=0;
	DJTPbLength=0;
	while(DJIPbLength!=DJIMinCount){
		int MP=DJIMinPos+1;
		DJIMinPos=FindMin(DJILocalMin,MP,DJIMinCount);
		for(i=MP;i<=DJIMinPos;i++){
			wave=(DJILocalMax[i]-DJILocalMin[DJIMinPos])/DJILocalMax[i];
			DJIPullback[0][i]=DJILocalMax[i];
			DJIPullback[1][i]=DJILocalMin[DJIMinPos];
			DJIPullback[2][i]=wave;
		}
		DJIPbLength=DJIMinPos;
	}
	DJIPullback[0][++DJIPbLength]=DJILocalMax[DJIMaxCount];
	DJIPullback[1][DJIPbLength]=Infinate;
	DJIPbLength++;
	//
	while(DJTPbLength!=DJTMinCount){
		int MP=DJTMinPos+1;
		DJTMinPos=FindMin(DJTLocalMin,MP,DJTMinCount);
		for(i=MP;i<=DJTMinPos;i++){
			wave=(DJTLocalMax[i]-DJTLocalMin[DJTMinPos])/DJTLocalMax[i];
			DJTPullback[0][i]=DJTLocalMax[i];
			DJTPullback[1][i]=DJTLocalMin[DJTMinPos];
			DJTPullback[2][i]=wave;
		}
		DJTPbLength=DJTMinPos;
	}
	DJTPullback[0][++DJTPbLength]=DJTLocalMax[DJTMaxCount];
	DJTPullback[1][DJTPbLength]=Infinate;
	DJTPbLength++;
	reset();
}



//The following function is to check whether the current price of the index is satisfied the buy condition.
public boolean isBuy(String str,double []Max,double []Min,double [][]Pullback,boolean isDJT,Date CurDate) throws Exception {
	Security s=getSecurity(str);
	int k=0;
	int PbLength=0;
	double bounce=0;
	double CurPrice=s.getCurrentPrice(CurDate);
	double PrePrice=s.getCurrentPrice(LTIDate.getNewNYSEWeek(CurDate,-1));
	if(isDJT)
	PbLength=DJTPbLength;
	else
	PbLength=DJIPbLength;
	updateBullLocalPrices(str,Max,Min,isDJT,CurDate);
	updateBullPullback(Max,Min,Pullback,isDJT);
	if(CurPrice>PrePrice&&PbLength>0){
		for(k=PbLength-1;k>=0;k--){
			if(bounce<=Pullback[0][k]){
				if(Pullback[2][k]>=0.03){
					bounce=Pullback[0][k];
					if(CurPrice>=Pullback[0][k]&&Pullback[1][0]!=Pullback[1][k])
					return true;
				}
			}
		}
	}
	return false;
}

public boolean isSell(String str,double []Max,double []Min,double [][]Pullback,boolean isDJT,Date CurDate) throws Exception {
	Security s=getSecurity(str);
	int k=0;
	int PbLength=0;
	double bounce=Infinate;
	double CurPrice=s.getCurrentPrice(CurDate);
	double PrePrice=s.getCurrentPrice(LTIDate.getNewNYSEWeek(CurDate,-1));
	if(isDJT)
	PbLength=DJTPbLength;
	else
	PbLength=DJIPbLength;
	updateBearLocalPrices(str,Max,Min,isDJT,CurDate);
	updateBearPullback(Max,Min,Pullback,isDJT);
	if(CurPrice<PrePrice&&PbLength>0){
		for(k=PbLength-1;k>=0;k--){
			if(bounce>=Pullback[0][k]){
				if(Pullback[2][k]>=0.03){
					bounce=Pullback[0][k];
					if(CurPrice<=Pullback[0][k]&&Pullback[1][0]!=Pullback[1][k])
					return true;
				}
			}
		}
	}
	return false;
}

	//----------------------------------------------------
	//initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void init() throws Exception{
		initialLocalPrices();
initialPullback();
DJIMaxCount=0;
DJTMaxCount=0;
DJIMinCount=0;
DJTMinCount=0;
DJIPbLength=0;
DJTPbLength=0;
NewMax=false;
NewMin=false;
position=false;
DJIBuy=false;
DJTBuy=false;
DJISell=false;
DJTSell=false;
isDJTA=false;
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
	
		
		boolean isWeekClose=LTIDate.isLastNYSETradingDayOfWeek(CurrentDate);
if(isWeekClose){
   if(!position){
	DJIBuy=isBuy(DJIA,DJILocalMax,DJILocalMin,DJIPullback,isDJTA,CurrentDate);
	DJTBuy=isBuy(DJTA,DJTLocalMax,DJTLocalMin,DJTPullback,!isDJTA,CurrentDate);
   }
   else{
	DJISell=isSell(DJIA,DJILocalMax,DJILocalMin,DJIPullback,isDJTA,CurrentDate);
	DJTSell=isSell(DJTA,DJTLocalMax,DJTLocalMin,DJTPullback,!isDJTA,CurrentDate);
   }
}
		
		
		
		if(new Boolean(true).equals(false)){
			
		}
		
		 
		else if (!position&&DJIBuy&&DJTBuy) {
		   double amount=CurrentPortfolio.getAssetAmount(curAsset, CurrentDate);
CurrentPortfolio.sellAsset(curAsset, CurrentDate);
CurrentPortfolio.buy (curAsset, UnderlyingSecurity, amount, CurrentDate);
position=true;
buyReset();
		}
		else if (position&&DJISell&&DJTSell) {
		   double amount=CurrentPortfolio.getAssetAmount(curAsset, CurrentDate);
CurrentPortfolio.sellAsset(curAsset, CurrentDate);
CurrentPortfolio.buy (curAsset, "CASH", amount, CurrentDate);
position=false;
sellReset();
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