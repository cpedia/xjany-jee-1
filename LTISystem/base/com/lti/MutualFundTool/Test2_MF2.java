package com.lti.MutualFundTool;

import java.util.Date;
import java.util.List;

import org.rosuda.JRI.REXP;

import com.lti.Exception.Security.NoPriceException;
import com.lti.MutualFundTool.Exception.InvalidParametersException;
import com.lti.service.PortfolioManager;
import com.lti.service.SecurityManager;
import com.lti.service.bo.Security;
import com.lti.system.ContextHolder;
import com.lti.type.Interval;
import com.lti.type.TimeUnit;
import com.lti.util.LTIDate;
import com.lti.util.LTIRInterface;
import com.lti.util.StringUtil;

public class Test2_MF2 {

	private static final Date NULL = null;
	private SecurityManager securityManager;
	private PortfolioManager portfolioManager;

	public Test2_MF2() {
		super();
		securityManager = ContextHolder.getSecurityManager();
		portfolioManager = ContextHolder.getPortfolioManager();
	}

	public double[][] MatrixMultiply(double[][] A, double[][] B)throws Exception {
		int i;
		int j;
		int k;
		int m = A.length;
		int n = A[0].length;
		int p = B.length;
		int q = B[0].length;
		double[][] C = new double[m][q];

		if (p != n) {
			throw new InvalidParametersException("The dimention of two Matrix should be mateched!");
		}

		for (i = 0; i < m; i++) {

			for (j = 0; j < q; j++) {
				for (k = 0; k < n; k++) {
					C[i][j] += A[i][k] * B[k][j];
				}
			}
		}
		return C;
	}

	public double[][] Transpose(double[][] A) throws Exception {

		int m = A.length;
		int n = A[0].length;
		double[][] B = new double[n][m];

		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {

				B[j][i] = A[i][j];
			}
		}
		return B;
	}

	public double ComputeR(double[][] A, double[][] B) {
		int T;
		double R1 = 0;

		double AveA = 0;
		double SumR = 0;
		double SumA = 0;
		T = A[0].length;
		
		for (int i = 0; i < T; i++) {

			AveA += A[0][i];
	
		}

		AveA = AveA / T;
	
	
		for (int j = 0; j < T; j++) {
			SumR += (A[0][j] - B[0][j]) * (A[0][j] - B[0][j]);
			SumA += (A[0][j]-AveA) * (A[0][j]-AveA);
		}

		R1 = 1 - (SumR / SumA);
		return R1;

	}

	public double[] Anold(int interval, Date date,TimeUnit tu, String MF,String[] index) throws Exception{
		Security MutualFund=securityManager.get(MF);
		if(MF==null)throw new InvalidParametersException("MF parameter is invalid!");
		Date[] CDate=new Date[interval+1];
		for (int i = 0; i < interval + 1; i++) {

			if (tu == TimeUnit.DAILY) {
				CDate[i] = LTIDate.getNewNYSETradingDay(date, -interval - 1 + i);
			} else if (tu == TimeUnit.WEEKLY) {
				CDate[i] = LTIDate.getNewNYSEWeek(date, -interval - 1 + i);
			} else if (tu == TimeUnit.MONTHLY) {
				CDate[i] = LTIDate.getNewNYSEMonth(date, -interval - 1 + i);
			} else if (tu == TimeUnit.QUARTERLY) {
				CDate[i] = LTIDate.getNewNYSEQuarter(date, -interval - 1 + i);
			} else if (tu == TimeUnit.YEARLY) {
				CDate[i] = LTIDate.getNewNYSEYear(date, -interval - 1 + i);
			} else
				return null;
		}
		double[] mfAdjPrice=new double[interval+1];
		for (int i = 0; i < interval+1; i++) {
			try {
				mfAdjPrice[i] = securityManager.getAdjPrice(MutualFund.getID(), CDate[i]);
			} catch (NoPriceException e) {
				System.out.println(StringUtil.getStackTraceString(e));
				//e.printStackTrace();
			}
		}
		
		double[][] indexAdjPrice=new double[index.length][interval+1];
		for(int i=0;i<indexAdjPrice.length;i++){
			Security cIndex=securityManager.get(index[i]);
			if(cIndex==null)throw new InvalidParametersException("MF parameter is invalid!");
			for(int j=0;j<interval+1;j++){
				indexAdjPrice[i][j]=securityManager.getAdjPrice(cIndex.getID(), CDate[j]);
			}
		}
		
		return Anold(interval, date, mfAdjPrice, indexAdjPrice);
		
	}
	
	
	public double[] Anold(int interval, Date date, double[] MF,	double[][] index) throws Exception{
		if(interval<=0||MF==null||index==null){
			throw new InvalidParametersException("Interval or MF or index parameter is invalid!");
		}
		if(MF.length-1!=interval){
			throw new InvalidParametersException("MF's length does not match invertal!");
		}
		for(int i=0;i<index.length;i++){
			if(index[i]==null){
				throw new InvalidParametersException("Index parameter is invalid!");
			}
			if(index[i].length-1!=interval){
				throw new InvalidParametersException("Index[i]'s length does not match invertal!");
			}
		}
		
		double[][] MFy1 = new double[1][interval + 1];
		double[][] MFY = new double[1][interval];
		double[][] MFx1 = new double[index.length][interval + 1];
		double[][] MFX = new double[index.length][interval];
		double[][] D = new double[index.length][index.length];
		double[][] d = new double[index.length][1];
		double[][] A = new double[index.length][index.length + 1];
		double[] b0 = new double[index.length + 1];

		// get the change ratio of price for MF
		for (int i = 0; i < interval + 1; i++) {
			MFy1[0][i] = MF[i];
		}
		for (int j = 1; j < interval + 1; j++) {
			MFY[0][j - 1] = (MFy1[0][j] - MFy1[0][j - 1]) / MFy1[0][j - 1];
		}

		// get the change ratio of index
		MFx1=index;
		int L = index.length;
		for (int k = 0; k < L; k++) {
			for (int j = 1; j < interval + 1; j++) {
				MFX[k][j - 1] = (MFx1[k][j] - MFx1[k][j - 1]) / MFx1[k][j - 1];
			}

		}

		// calculate matrix D,d:D=MFX*MFX',d=MFX*MFY'
		D = MatrixMultiply( MFX,Transpose(MFX));
		d = MatrixMultiply( MFY,Transpose(MFX));

		// initiate A,b0
		for (int i = 1; i < index.length + 1; i++) {
			b0[i] = 0;
		}
		;
		b0[0] = 1;
		for (int i = 0; i < index.length; i++) {
			A[i][0] = 1;
		}
		for (int i = 0; i < index.length; i++) {
			for (int j = 1; j < index.length + 1; j++) {
				if (i + 1 == j)
					A[i][j] = 1;
				else
					A[i][j] = 0;
			}
		}

		LTIRInterface li = LTIRInterface.getInstance();
		String[] inString = new String[7];
		REXP[] solution = new REXP[1];

		// connect dvec to d with inString[1]
		inString[0] = "library(quadprog);";
		String tmp = "dvec<-c(";

		for (int i = 0; i < index.length - 1; i++) {
			Double left = (Double) d[0][i];
			tmp += left.toString() + ",";
		}
		Double left111 = (Double) d[0][index.length - 1];
		tmp += left111.toString() + ")";
		inString[1] = tmp + ";";

		// connect bvec to b0 with inString[2]
		String tmp2 = "bvec<-c(1,";
		for (int i = 1; i < index.length; i++) {
			Double left1 = (Double) b0[i];
			tmp2 += left1.toString() + ",";
		}
		Double left11 = (Double) b0[index.length];
		tmp2 += left11.toString() + ")";
		inString[2] = tmp2 + ";";

		// connect Amat to A with inString[3]
		String tmp3 = "Amat<-matrix(c(";
		for (int i = 0; i < index.length; i++) {
			for (int j = 0; j < index.length; j++) {
				Double left3 = (Double) A[j][i];
				tmp3 += left3.toString() + ",";
			}
		}
		String s1 = new String();
		for (int i = 0; i < index.length - 1; i++) {
			Integer num = (Integer) index.length;
			s1 = num.toString();
			Double left33 = (Double) A[i][index.length];
			tmp3 += left33.toString() + ",";
		}
		Double left333 = (Double) A[index.length - 1][index.length];
		tmp3 += left333.toString();

		tmp3 += ")," + s1 + "," + s1 + "+1)";
		inString[3] = tmp3 + ";";

		// connect Dmat to D with inString[4]
		String tmp4 = "Dmat<-matrix(c(";
		for (int i = 0; i < index.length - 1; i++) {
			for (int j = 0; j < index.length; j++) {
				Double left4 = (Double) D[j][i];
				tmp4 += left4.toString() + ",";
			}
		}
		for (int i = 0; i < index.length - 1; i++) {
			Double left44 = (Double) D[i][index.length - 1];
			tmp4 += left44.toString() + ",";
		}
		Double left444 = (Double) D[index.length - 1][index.length - 1];
		tmp4 += left444.toString() + ")," + s1 + "," + s1 + ");";
		inString[4] = tmp4;

		// solve.QP to get betas
		inString[5] = "solution=solve.QP(Dmat,dvec,Amat,bvec,meq=1);";
		inString[6] = "LTI_return1=solution[1];";

		solution = li.RCall2(inString, 1);
		li.closeEngine();
		Double[] beta = li.parseVector(solution[0]);

		double[][] beta2 = new double[index.length][1];
		for (int i = 0; i < index.length; i++) {
			beta2[i][0] = (double) beta[i];
		}

		double R = ComputeR(MFY, Transpose(MatrixMultiply(Transpose(MFX), beta2)));
		double[] beta3 = new double[index.length + 1];
		for (int i = 0; i < index.length; i++) {
			beta3[i] = beta2[i][0];
		}
		//convert R square to the last of beta3
		beta3[index.length] = R;
		return beta3;
		
	}

	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
       String[] index = { "VFINX", "VGTSX", "VGSIX", "QRAAX", "BEGBX","VBMFX", "CASH" };
		//String[] index = { "VFINX", "VGTSX", "VGSIX", "QRAAX", "BEGBX","VBMFX", "CASH","PAUAX","PATRX","CPMPX","INDEX","PASCX","WYASX","WCASX","IVAEX","WASYX","WASCX","WBASX","UNASX","WASBX","OWRRX","WASAX","IASEX","LCRIX","LCORX","ETFMX","PAUIX","PAUDX","QOPYX","FIVEX","RNCOX","PAUCX","QOPCX","CLBLX","LAAIX","QOPNX"};
		//String[] index={"^IRX","^FVX","^TNX","XLP","XLK","XLV","XLU","XLB","XLF","XLE","XLY","XLI"};
		String MF="DODBX";
		Test2_MF2 we = new Test2_MF2();
		Date date = new Date();
		date = LTIDate.getDate(2008,7, 1);
		try {
			int interval[] = { 30 };
			    Long t1 = System.currentTimeMillis();
				double[] d = we.Anold(interval[0],date,TimeUnit.DAILY, MF,index);
                int L=d.length;
                System.out.println("The weight of "+MF+" on "+date+" are:");
                Long t2 = System.currentTimeMillis();
				for (int i = 0; i < L-1; i++) {
					System.out.println(index[i]+"\t"+d[i]);
				}
				System.out.println("R square is "+d[L-1]);
				System.out.println("Time: " + (t2 - t1));
			
		

		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println(StringUtil.getStackTraceString(e));
		}

	}

}
