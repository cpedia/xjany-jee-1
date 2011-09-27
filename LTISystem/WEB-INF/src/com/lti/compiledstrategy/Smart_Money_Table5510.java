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
public class Smart_Money_Table5510 extends SimulateStrategy{
	public Smart_Money_Table5510(){
		super();
		StrategyID=5510L;
		StrategyClassID=3L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	private String[] Symbols;
	public void setSymbols(String[] Symbols){
		this.Symbols=Symbols;
	}
	
	public String[] getSymbols(){
		return this.Symbols;
	}
	private int WeeksNum;
	public void setWeeksNum(int WeeksNum){
		this.WeeksNum=WeeksNum;
	}
	
	public int getWeeksNum(){
		return this.WeeksNum;
	}
	private int interval;
	public void setInterval(int interval){
		this.interval=interval;
	}
	
	public int getInterval(){
		return this.interval;
	}
	private boolean IsWLSorOLS;
	public void setIsWLSorOLS(boolean IsWLSorOLS){
		this.IsWLSorOLS=IsWLSorOLS;
	}
	
	public boolean getIsWLSorOLS(){
		return this.IsWLSorOLS;
	}
	private boolean IsSumToOne;
	public void setIsSumToOne(boolean IsSumToOne){
		this.IsSumToOne=IsSumToOne;
	}
	
	public boolean getIsSumToOne(){
		return this.IsSumToOne;
	}
	private boolean Constraint;
	public void setConstraint(boolean Constraint){
		this.Constraint=Constraint;
	}
	
	public boolean getConstraint(){
		return this.Constraint;
	}
	private String[] index;
	public void setIndex(String[] index){
		this.index=index;
	}
	
	public String[] getIndex(){
		return this.index;
	}
	private double[] lower;
	public void setLower(double[] lower){
		this.lower=lower;
	}
	
	public double[] getLower(){
		return this.lower;
	}
	private double[] upper;
	public void setUpper(double[] upper){
		this.upper=upper;
	}
	
	public double[] getUpper(){
		return this.upper;
	}
	private int caculateday;
	public void setCaculateday(int caculateday){
		this.caculateday=caculateday;
	}
	
	public int getCaculateday(){
		return this.caculateday;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		Symbols=(String[])ParameterUtil.fetchParameter("String[]","Symbols", "PGMAX", parameters);
		WeeksNum=(Integer)ParameterUtil.fetchParameter("int","WeeksNum", "-5", parameters);
		interval=(Integer)ParameterUtil.fetchParameter("int","interval", "30", parameters);
		IsWLSorOLS=(Boolean)ParameterUtil.fetchParameter("boolean","IsWLSorOLS", "true", parameters);
		IsSumToOne=(Boolean)ParameterUtil.fetchParameter("boolean","IsSumToOne", "true", parameters);
		Constraint=(Boolean)ParameterUtil.fetchParameter("boolean","Constraint", "true", parameters);
		index=(String[])ParameterUtil.fetchParameter("String[]","index", "SPY,EFA,EEM,AGG,CASH", parameters);
		lower=(double[])ParameterUtil.fetchParameter("double[]","lower", "0.0,0.0,0.0,0.0,0.0", parameters);
		upper=(double[])ParameterUtil.fetchParameter("double[]","upper", "1.0,1.0,1.0,1.0,1.0", parameters);
		caculateday=(Integer)ParameterUtil.fetchParameter("int","caculateday", "5", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	boolean ToCheck=false;
	public String getVariables(){
		StringBuffer sb=new StringBuffer();
		sb.append("ToCheck: ");
		sb.append(ToCheck);
		sb.append("\n");
		return sb.toString();
	}
	
	public void writeObject(ObjectOutputStream stream) throws IOException{
		stream.writeObject(ToCheck);
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		ToCheck=(Boolean)stream.readObject();;
	}
	
	//----------------------------------------------------
	//user defined functions
	//----------------------------------------------------	
	public static double RoundPercent(double Rval)  throws Exception {
  Rval = Rval * 10000;
  double tmp = Math.round(Rval);
  return (double)tmp/100.0;
}

public static String symbol2Asset(String sym)  throws Exception {
  if (sym.equalsIgnoreCase("vbmfx"))return "USBond";
  if (sym.equalsIgnoreCase("begbx"))return "IntlBond";
  if (sym.equalsIgnoreCase("vgtsx")) return "IntlStk";
  if (sym.equalsIgnoreCase("vtsmx"))return "USStk";
  if (sym.equalsIgnoreCase("veiex"))return "EmergStk";
  if (sym.equalsIgnoreCase("vgsix")) return "USReits";
  if (sym.equalsIgnoreCase("dbc"))return "Commodities";
  if (sym.equalsIgnoreCase("bwx"))return "IntlBond";
  if (sym.equalsIgnoreCase("ciu")) return "IntGrdBnd";
  if (sym.equalsIgnoreCase("csj"))return "ShrtGrdBnd";
  if (sym.equalsIgnoreCase("emb"))return "EmergBnd";
  if (sym.equalsIgnoreCase("hyg")) return "JunkBnd";
  if (sym.equalsIgnoreCase("ief"))return "IntGov";
  if (sym.equalsIgnoreCase("lqd"))return "LongGrdBnd";
  if (sym.equalsIgnoreCase("mbb")) return "MortgageBnd";
  if (sym.equalsIgnoreCase("shy"))return "ShrtGov";
  if (sym.equalsIgnoreCase("tlt")) return "LongGov";
  return sym;

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
	
		
		ToCheck=false;
Calendar ca1=Calendar.getInstance();
ca1.setTime(CurrentDate);
ca1.set(Calendar.DAY_OF_WEEK, caculateday+1);
while(!LTIDate.isNYSETradingDay(ca1.getTime())){
	ca1.add(Calendar.DAY_OF_YEAR, -1);
}
if(LTIDate.equals(CurrentDate, ca1.getTime()))ToCheck=true;
		
		
		
		if(new Boolean(true).equals(false)){
			
		}
		
		 
		else if (ToCheck) {
		   			com.lti.service.MutualFundManager mutualFundManager = com.lti.system.ContextHolder.getMutualFundManager();
			java.text.DateFormat df1 = new java.text.SimpleDateFormat("yyyy-MM-dd");
			Long createTime = java.util.Calendar.getInstance().getTime().getTime();

			java.util.Calendar ca = java.util.Calendar.getInstance();
		        ca.add(java.util.Calendar.DATE, 1);
		        Date dt = ca.getTime();
		        Date endDate = df1.parse(df1.format(dt));
			Date startDate = df1.parse(df1.format(LTIDate.getNewDate(new Date(),TimeUnit.WEEKLY, WeeksNum)));
			boolean IsRAA = true;
			for(String symbol:Symbols){
				mutualFundManager.setupLimit(upper, lower);
				mutualFundManager.calculateDailyBeta(symbol, interval, index, createTime, startDate, endDate, Constraint, IsWLSorOLS, IsSumToOne);
				
				List<MutualFundDailyBeta> mutualFundDailyBetas = mutualFundManager.getDailyData(symbol, index, createTime, IsRAA);
				for(int i=0;i<mutualFundDailyBetas.size();i++){
					MutualFundDailyBeta beta=mutualFundDailyBetas.get(i);
					List<String> betaList=new ArrayList<String>();
					for(int j=0;j<beta.getBetas().length;j++){				
						betaList.add(FormatUtil.formatPercentage(beta.getBetas()[j]));
					}
					
					beta.setBetaList(betaList);
					String RSQuare=FormatUtil.formatQuantity(beta.getRSquare());
					beta.setRSquareString(RSQuare);
				}
				List<MutualFundDailyBeta> fridayBetas = new ArrayList<MutualFundDailyBeta>();				
				for(MutualFundDailyBeta beta :mutualFundDailyBetas){
					if(LTIDate.isWeekDay(beta.getDate(), 6)){
						fridayBetas.add(beta);
					}
				}
				
                                 int limit = fridayBetas.size()-4;
				for(int i =0;i<limit;i++){
					fridayBetas.remove(0);
                                        printToLog(fridayBetas.size());
				}

			        String[][] returnTable = new String[fridayBetas.size()+1][index.length+1];    	
				returnTable[0][0] = "Date";
				for(int j=0;j<index.length;j++){
					returnTable[0][j+1]=symbol2Asset(index[j]);
				}
				
				for(int i=0;i<fridayBetas.size();i++){
						returnTable[i+1][0] = fridayBetas.get(i).getDate().toString();
						for(int j =0;j<index.length;j++){							
							returnTable[i+1][j+1] = fridayBetas.get(i).getBetaList().get(j);
						}
						
						
				}
				 PersistentUtil.writeGlobalObject(returnTable, "SmartTable."+symbol, StrategyID, PortfolioID);  
				
			}
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