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
public class JB586 extends SimulateStrategy{
	public JB586(){
		super();
		StrategyID=586L;
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
	

	//----------------------------------------------------
	//initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void init() throws Exception{
		int Data1, Data2, Data3;
int large;
Data1=987; 
Data2    = 10135;
Data3 = 2037;

if (Data1 > Data2)  {
      large = Data1;
} 
else {
     large=Data2;
}

printToLog( "Largest among Data1 and Data2 is " + large);  

if (Data3 > large) {
     large = Data3;
}

printToLog( "Largest is " + large);

int[] M;
int n=6;
M = new int[n];
M[0] = 101;
M[1] = 20445;
M[2] = 20338;
M[3] = 10293;
M[4] = 10425;
M[5] = 211111;


int Big;
int i;


/* if (M[1] > M[0]) {
   Big = M[1];
}
else {
   Big = M[0];
}
*/

Big = M[0];

for(i=1; i<=n-1; i=i+1)
{
if (M[i] > Big) {
   Big = M[i];
}
}

printToLog( "Biggest M is " + Big);

int sum;
sum = 0;

for(i=0; i<=n-1; i=i+1)
{
sum = M[i] + sum;
}

printToLog( "The sum is " + sum);

int Sum;

Sum = M[0] + M[1] + M[2] + M[3] + M[4];
printToLog( "Sum is " + Sum);

double Mean;
Mean =(double)sum/n;

printToLog ( "the mean is " + Mean);

double variance;

variance = 0.0;

for(i=1; i<=n-1; i=i+1)
{
variance = Math.pow(M[i] - Mean, 2.0) + variance;
}

variance = variance/n;

printToLog ( "The variance is " + variance);

double std;

std = Math.sqrt(variance);

printToLog("The standard deviation is " + std);

int m;
m = 3;
int [][] R= new int[m][n];

R[0][0] = 121;
R[0][1] = 123;
R[0][2] = 592;
R[0][3] = -920;
R[0][4] = 205;
R[0][5] = 402;


R[1][0] = 204;
R[1][1] = 103;
R[1][2] = 830;
R[1][3] = 201;
R[1][4] = 98;
R[1][5] = 3049;

R[2][0] = 624;
R[2][1] = -1023;
R[2][2] = 523;
R[2][3] = -420;
R[2][4] = 402;
R[2][5] = -2304;

int j;
double [] mean;
mean = new double [m];

for(j=0; j<=m-1; j++)
{
sum = 0;
for(i=0; i<=n-1; i++)
{
sum = R[j][i] +sum;
}
mean[j] = (double)sum/n;
}

for(j=0; j<=2; j=j+1)
{
printToLog( "The mean[" + j+ "] is " + mean[j]);
}

Double var;
Double [] Variance;
Variance = new Double [3];

for(j=0; j<=2; j=j+1)
{
var = 0.0;
for(i=0; i<=5; i=i+1)
{
var = Math.pow(R[j][i] - mean[j], 2.0) + var;

Variance[j] = var/6.0;
}
}

for(j=0; j<=2; j=j+1)
{
printToLog ( "The standard deviations are " + Math.sqrt(Variance[j]));
}

Double Add;
Add = 0.0;

for(j=0; j<=n-1; j=j+1)
{
Add = (R[0][j]-mean[0])*(R[1][j]-mean[1]) + Add;
}

int c;
Double [][] v = new Double[m][m];

for(i=0; i<=m-1; i++)
{
    for(j=0; j<=m-1; j++)
    {
      v[i][j] = 0.0;
      for(c=0; c<= n-1; c++)
      {
         v[i][j] = (R[i][c] - mean[i])*(R[j][c] - mean[j]) + v[i][j];
      }
     v[i][j] = v[i][j]/n;
     printToLog("v["+i+"]["+j+"] is " + v[i][j]);
   }
}

Double varp;
varp = 0.0;


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