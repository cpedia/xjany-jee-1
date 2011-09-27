package com.lti.util;

import java.util.ArrayList;
import java.util.List;

import org.rosuda.JRI.*;

/*import org.rosuda.JRclient.RBool;
import org.rosuda.JRclient.REXP;
import org.rosuda.JRclient.RSrvException;
import org.rosuda.JRclient.Rconnection;*/

public class LTIIRR {

	private LTIRInterface li;
	
	private static LTIIRR instance=null;
	
	
	public static LTIIRR getInstance() {
		if(instance==null)instance=new LTIIRR();
		return instance;
	}

	private LTIIRR()
	{		
		 li = LTIRInterface.getInstance();
	}
	
	public List<Integer> reducePower(List<Integer> ld)
	{
		int p = ld.get(ld.size()-1);
		
		for(int i=0;i<ld.size();i++)
		{
			int cur = ld.get(i);
			ld.set(i, cur/p);
		}
		return ld;
	}
	
	public double getIRR(List<Double> A,List<Integer> B,double A1)
	{		
		
		/*int power = B.get(B.size()-1);
		B = this.reducePower(B);*/
		
		double irr=0;
		double min=0;
		try {
			
			String[] inString = new String[5];
			
			REXP[] outString;
			
			inString[0] = "library(\"polynom\",character.only=TRUE)";
			inString[1] = "x=poly.calc(0:0)";
			
			String tmp = "";
			
			for(int i=0;i<A.size();i++)
			{
				Integer up = (Integer)(B.get(i));
				Double left = (Double)(A.get(i));				
				
				if(i!=0)
				{
					if(left>0)tmp+="+";
				}
				tmp+=left.toString()+"*x^"+up.toString();
			}
			tmp+="+";
			Double BB = (Double)A1;
			tmp+=BB.toString();
			
			inString[2] = "func="+tmp;
			
			inString[3] = "answer=solve(func)";
			
			inString[4] = "LTI_return1=length(answer)";		
			
			outString = li.RCall2(inString, 1);
						
			int len = li.parseInteger(outString[0]);
			
			min = 10000;
			
			for(int i=1;i<=len;i++)
			{
				Integer s = (Integer)i;
				
				String[] inString2 = new String[1];
				String[] outValue = new String[1];
				REXP[] re = new REXP[1];
				
				inString2[0] = "LTI_return1=(Im(answer["+s.toString()+"])==0)";
				
				re = li.RCall2(inString2, 1);
				
				boolean flag = li.parseBool(re[0]);
				
				if(flag)
				{					
					inString2[0] = "LTI_return1=Re(answer["+s.toString()+"])";
					
					re = li.RCall2(inString2, 1);
					
					double value = li.parseDouble(re[0]);
					
					if(min>value && value>=0)
					{
						min = value;
					}
				}		
			}
			
			//System.out.println(min);
			System.out.println("Done");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		irr = Math.pow(min, 252);
		this.li.closeEngine();
		return irr-1;
	}
	
	public static void main(String[] args)
	{
		List<Double> A = new ArrayList<Double>();
		List<Integer> B = new ArrayList<Integer>();
		A.add(1.0);
		B.add(1);
		LTIIRR li = LTIIRR.getInstance();	
		System.out.println(li.getIRR(A, B, 1));
		System.out.println(li.getIRR(A, B, 1));
		System.out.println(li.getIRR(A, B, 1));
		System.out.println(li.getIRR(A, B, 1));
	}
	
}
