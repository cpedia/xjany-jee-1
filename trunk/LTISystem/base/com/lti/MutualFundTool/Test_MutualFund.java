package com.lti.MutualFundTool;

import org.rosuda.JRI.REXP;

import com.lti.util.LTIRInterface;

public class Test_MutualFund {
	double mysum(){
	REXP[] re = new REXP[1];
	LTIRInterface li = LTIRInterface.getInstance();
	String[] inString = new String[2];
	inString[0]="a<-c(2,2,3);";
	inString[1]="LTI_return1=a[1]+a[2]+a[3];";
	
	re= li.RCall2(inString, 1);
	li.closeEngine();
	return li.parseDouble(re[0]);
	
	}

	private int parseInteger(REXP[] re) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public static void main(String[] args){
		double a;
		Test_MutualFund k = new Test_MutualFund();
		a=k.mysum();
		System.out.println(a);
	}
}
