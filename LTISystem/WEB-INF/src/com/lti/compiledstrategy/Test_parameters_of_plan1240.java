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
public class Test_parameters_of_plan1240 extends SimulateStrategy{
	public Test_parameters_of_plan1240(){
		super();
		StrategyID=1240L;
		StrategyClassID=3L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	private Long PlanID;
	public void setPlanID(Long PlanID){
		this.PlanID=PlanID;
	}
	
	public Long getPlanID(){
		return this.PlanID;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		PlanID=(Long)ParameterUtil.fetchParameter("Long","PlanID", "0", parameters);
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
		private boolean initPlanParameters=false;
        private double RoundtripLimit;
        private int WaitingPeriod;
        private int[] array;
        private void initPlanParameters(){
                if(initPlanParameters)return;
                List<Pair> attrs=com.lti.system.ContextHolder.getStrategyManager().get(PlanID).getAttributes();
                Map<String,String> tmp=new HashMap<String,String>();
                for(Pair p:attrs){
                        tmp.put(p.getPre(),p.getPost());
                 }
                RoundtripLimit=Double.parseDouble(tmp.get("RoundtripLimit"));
                  WaitingPeriod=Integer.parseInt(tmp.get("WaitingPeriod"));
                  String[] arr=tmp.get("Array").split(",");
                     array=new int[arr.length];
                   for(int i=0;i<arr.length;i++)array[i]=Integer.parseInt(arr[i]);
                initPlanParameters=true;
        }

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
	
		
		initPlanParameters();
printToLog("WaitingPeriod"+WaitingPeriod);
printToLog("RoundtripLimit"+RoundtripLimit);
		
		
		
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

//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\Test_parameters_of_plan1240.java:69: |��{�
//~0 java.util.Map<java.lang.String,java.lang.String>
// � java.util.List<com.lti.type.Pair>
//                List<Pair> attrs=com.lti.system.ContextHolder.getStrategyManager().get(PlanID).getAttributes();
//                                                                                                            ^
//1 �