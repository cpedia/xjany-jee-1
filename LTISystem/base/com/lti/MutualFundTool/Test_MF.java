package com.lti.MutualFundTool;

import java.util.Date;

import com.lti.type.TimeUnit;
import com.lti.util.LTIDate;

public class Test_MF {
	
	public static void main(String[] arg) throws Exception{
		
		Date date1 = new Date();
		String[] MF={"WASAX"};
				//"LCRIX","LCORX","PAUIX","FIVEX","LAAIX","LAALX","INDEX","EXDAX","WARYX","COTZX","ICMBX","WARCX","PASBX","FSACX","FMRCX","GRSPX","CGIIX","BRBCX","CGNRX","CVTCX","TEBRX","MNBAX","MBEYX","CTFBX","FMIRX","MBECX","PFTRX","VPAAX","CMFCX","UNFDX","CTFAX","WRRBX","DODBX"};
		
		
		//String[] Index = { "VFINX", "VGTSX", "VGSIX", "QRAAX", "BEGBX","VBMFX", "CASH" };
		//String[] Index={"^IRX","^FVX","^TNX","XLP","XLK","XLV","XLU","XLB","XLF","XLE","XLY","XLI"};
		String[] Index={"CASH", "VGSIX", "QRAAX","BEGBX","VBMFX","VFINX","VGTSX","XLP","XLK","XLV","XLU","XLB","XLF","XLE","XLY","XLI"};
		//String[] Index={"^IRX","^FVX","^TNX","XLP","XLK","XLV","XLU","XLB","XLF","XLE","XLY","XLI"};
		Double[][] beta;
		int Interval;
		int n;
		int L;
		
		Interval=40;
		date1 = LTIDate.getDate(2008,6, 30);
		
		WeightEstimate we = new WeightEstimate();
		
	
        n=Index.length;
        L=MF.length;
        beta=new Double[L][n+1];
        for(int i=0;i<L;i++)
        beta[i]=we.RAA(Interval,date1,TimeUnit.DAILY, MF[i],Index,false,true);
        
        for(int i=0;i<L;i++){
        	System.out.println("\n"+MF[i]+"\n");
          /*	for(int j=0;j<n+1;j++){
        
        		System.out.println(beta[i][j]);
        	}
        	System.out.println("\n");*/
    
        System.out.println("CASH:"+"\t"+beta[i][0]);
        	System.out.println("STOCK"+"\t"+(beta[i][5]+beta[i][6]+beta[i][7]+beta[i][8]+beta[i][9]+beta[i][10]+beta[i][11]+beta[i][12]+beta[i][13]+beta[i][14]+beta[i][15]));
        	System.out.println("BOND:"+"\t"+(beta[i][3]+beta[i][4]));
        	System.out.println("Others:"+"\t"+(beta[i][1]+beta[i][2]));
        	System.out.println("Rsquare:"+beta[i][16]);
        	
        }
}
}
	
	
	

