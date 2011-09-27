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
public class ${name} extends SimulateStrategy{
	public ${name}(){
		super();
		StrategyID=${ID?string.computer}L;
		StrategyClassID=${strategyClassID?string.computer}L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	<#if parameters?exists > 
	<#list parameters as parameter>
	private ${parameter.first} ${parameter.second};
	public void set${parameter.second?cap_first}(${parameter.first} ${parameter.second}){
		this.${parameter.second}=${parameter.second};
	}
	
	public ${parameter.first} get${parameter.second?cap_first}(){
		return this.${parameter.second};
	}
	</#list>
	</#if>
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		<#if parameters?exists > 
		<#list parameters as parameter>
		${parameter.second}=(<#if parameter.first="int">Integer<#elseif parameter.first?index_of("[")!=-1>${parameter.first}<#else>${parameter.first?cap_first}</#if>)ParameterUtil.fetchParameter("${parameter.first}","${parameter.second}", "${parameter.third}", parameters);
		</#list>
		</#if>
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	<#if variableCode?exists > 
	${variableCode}
	</#if>
	public String getVariables(){
		StringBuffer sb=new StringBuffer();
		<#if variables?exists > 
		<#list variables as variable>
		sb.append("${variable.post}: ");
		sb.append(${variable.post});
		sb.append("\n");
		</#list>
		</#if>
		return sb.toString();
	}
	
	public void writeObject(ObjectOutputStream stream) throws IOException{
		<#if persistable=="false">
		return;
		</#if>
		<#if persistable=="true">
		<#if variables?exists > 
		<#list variables as variable>
		stream.writeObject(${variable.post});
		</#list>
		</#if>
		</#if>
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		<#if persistable=="false">
		return;
		</#if>
		<#if persistable=="true">
		<#if variables?exists > 
		<#list variables as variable>
		${variable.post}=(<#if variable.pre="int">Integer<#elseif variable.pre="boolean">Boolean<#elseif variable.pre="double">Double<#elseif variable.pre="long">Long<#elseif variable.pre="Float">float<#else>${variable.pre}</#if>)stream.readObject();;
		</#list>
		</#if>
		</#if>
	}
	
	//----------------------------------------------------
	//user defined functions
	//----------------------------------------------------	
	<#if functions?exists > 
	${functions}
	</#if>
	//----------------------------------------------------
	//initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void init() throws Exception{
		<#if initCode?exists > 
		${initCode}
		</#if>
	}
	//----------------------------------------------------
	//re-initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void reinit() throws Exception{
		<#if reinitCode?exists > 
		${reinitCode}
		</#if>
	}
	
	//----------------------------------------------------
	//action code
	//----------------------------------------------------	
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void action() throws Exception{
	
		<#if commonActionCode?exists > 
		
		${commonActionCode}
		
		</#if>
		
		
		if(new Boolean(true).equals(false)){
			
		}
		
		<#if actions?exists >
		 
		<#list actions as action> 
		else if (${action.pre}) {
		   ${action.post}
		}
		</#list>
		
		</#if>
		
		<#if defaultActionCode?exists >
		else{
		
			${defaultActionCode};
		}
		</#if>
		
	}
	
	public double getVersion(){
		return version;
	}
	
}
