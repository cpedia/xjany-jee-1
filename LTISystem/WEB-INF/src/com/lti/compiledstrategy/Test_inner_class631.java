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
public class Test_inner_class631 extends SimulateStrategy{
	public Test_inner_class631(){
		super();
		StrategyID=631L;
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
	 class IndexClass /*{*/  
{
      public double Amount;
      public String sName;
      
     public IndexClass (double A, String s)/* throws Exception {*/
 {
             Amount = A;
             sName = s;
    }
    public void setAmount (double A) /* throws Exception {*/
 {
            Amount = A;
   }
   public void setName(String s)/* throws Exception {*/  
{
          sName=s;
   }
    public double getAmount() /* throws Exception {*/ 
{
            return Amount;
   }
   public String getName()/* throws Exception {*/ 
{
           return sName;
   }
 }
 class IndexClassCompare implements java.util.Comparator<IndexClass> /*{*/  
{
   
public int compare(IndexClass ic1, IndexClass ic2) /* throws Exception {*/ 
{
      if (ic1.getAmount()<ic2.getAmount() ) {
          return 1;
     } else { return -1;}
   }
}

	//----------------------------------------------------
	//initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void init() throws Exception{
		Thread.currentThread().setContextClassLoader(new com.lti.compiler.LTIClassLoader());
			
IndexClass a=new IndexClass (0.1,"a");
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