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
public class Short_2_O_and_C575 extends SimulateStrategy{
	public Short_2_O_and_C575(){
		super();
		StrategyID=575L;
		StrategyClassID=2L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	private String ShortSecurity1;
	public void setShortSecurity1(String ShortSecurity1){
		this.ShortSecurity1=ShortSecurity1;
	}
	
	public String getShortSecurity1(){
		return this.ShortSecurity1;
	}
	private String ShortSecurity2;
	public void setShortSecurity2(String ShortSecurity2){
		this.ShortSecurity2=ShortSecurity2;
	}
	
	public String getShortSecurity2(){
		return this.ShortSecurity2;
	}
	private double ShortPercent1;
	public void setShortPercent1(double ShortPercent1){
		this.ShortPercent1=ShortPercent1;
	}
	
	public double getShortPercent1(){
		return this.ShortPercent1;
	}
	private String AdjustFrequency;
	public void setAdjustFrequency(String AdjustFrequency){
		this.AdjustFrequency=AdjustFrequency;
	}
	
	public String getAdjustFrequency(){
		return this.AdjustFrequency;
	}
	private double ShortPercent2;
	public void setShortPercent2(double ShortPercent2){
		this.ShortPercent2=ShortPercent2;
	}
	
	public double getShortPercent2(){
		return this.ShortPercent2;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		ShortSecurity1=(String)ParameterUtil.fetchParameter("String","ShortSecurity1", "SRS", parameters);
		ShortSecurity2=(String)ParameterUtil.fetchParameter("String","ShortSecurity2", "URE", parameters);
		ShortPercent1=(Double)ParameterUtil.fetchParameter("double","ShortPercent1", "0.5", parameters);
		AdjustFrequency=(String)ParameterUtil.fetchParameter("String","AdjustFrequency", "month", parameters);
		ShortPercent2=(Double)ParameterUtil.fetchParameter("double","ShortPercent2", "0.5", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	double TotalAmount;
double CSPercent1, CSPercent2;
double Shares1, Shares2;
	public String getVariables(){
		StringBuffer sb=new StringBuffer();
		sb.append("TotalAmount: ");
		sb.append(TotalAmount);
		sb.append("\n");
		sb.append("CSPercent1: ");
		sb.append(CSPercent1);
		sb.append("\n");
		sb.append("CSPercent2: ");
		sb.append(CSPercent2);
		sb.append("\n");
		sb.append("Shares1: ");
		sb.append(Shares1);
		sb.append("\n");
		sb.append("Shares2: ");
		sb.append(Shares2);
		sb.append("\n");
		return sb.toString();
	}
	
	public void writeObject(ObjectOutputStream stream) throws IOException{
		stream.writeObject(TotalAmount);
		stream.writeObject(CSPercent1);
		stream.writeObject(CSPercent2);
		stream.writeObject(Shares1);
		stream.writeObject(Shares2);
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		TotalAmount=(Double)stream.readObject();;
		CSPercent1=(Double)stream.readObject();;
		CSPercent2=(Double)stream.readObject();;
		Shares1=(Double)stream.readObject();;
		Shares2=(Double)stream.readObject();;
	}
	
	//----------------------------------------------------
	//user defined functions
	//----------------------------------------------------	
	

	//----------------------------------------------------
	//initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void init() throws Exception{
		Security S1=getSecurity(ShortSecurity1);
Security S2=getSecurity(ShortSecurity2);

CurrentPortfolio.sellAsset(curAsset, CurrentDate);
TotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);

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
	
		
		   /* open */
   CurrentPortfolio.sellAssetAtOpen(curAsset, CurrentDate);

   TotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);
   CurrentPortfolio.shortSellAtOpen(curAsset, ShortSecurity1, TotalAmount*ShortPercent1, CurrentDate);
   CurrentPortfolio.shortSellAtOpen(curAsset, ShortSecurity2, TotalAmount*ShortPercent2, CurrentDate);    

    /* close */
    CurrentPortfolio.sellAssetAtClose(curAsset, CurrentDate);

   TotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);
   CurrentPortfolio.shortSellAtClose(curAsset, ShortSecurity1, TotalAmount*ShortPercent1, CurrentDate);
   CurrentPortfolio.shortSellAtClose(curAsset, ShortSecurity2, TotalAmount*ShortPercent2, CurrentDate);

		
		
		
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