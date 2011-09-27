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
public class Total_Return_Table_Wed4966 extends SimulateStrategy{
	public Total_Return_Table_Wed4966(){
		super();
		StrategyID=4966L;
		StrategyClassID=3L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	private String[] Descriptions;
	public void setDescriptions(String[] Descriptions){
		this.Descriptions=Descriptions;
	}
	
	public String[] getDescriptions(){
		return this.Descriptions;
	}
	private String[] Funds;
	public void setFunds(String[] Funds){
		this.Funds=Funds;
	}
	
	public String[] getFunds(){
		return this.Funds;
	}
	private String TableName;
	public void setTableName(String TableName){
		this.TableName=TableName;
	}
	
	public String getTableName(){
		return this.TableName;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		Descriptions=(String[])ParameterUtil.fetchParameter("String[]","Descriptions", "US Stocks, International Developed Stks, Emerging Market Stks, Frontier Market Stks, US Equity REITs, International REITs,Commodities,Gold,Total US Bonds,International Treasury Bonds,US Credit Bonds,US High Yield Bonds,Mortgage Back Bonds,Municipal Bonds, Intermediate Treasuries, Treasury Bills", parameters);
		Funds=(String[])ParameterUtil.fetchParameter("String[]","Funds", "VTI,EFA,VWO,FRN,VNQ,RWX,GSG,GLD,BND,BWX,CFT,JNK,MBB,MUB,IEF,SHV ", parameters);
		TableName=(String)ParameterUtil.fetchParameter("String","TableName", "MajorAssetReturnTable", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	Date FridayDate= new Date();
	public String getVariables(){
		StringBuffer sb=new StringBuffer();
		sb.append("FridayDate: ");
		sb.append(FridayDate);
		sb.append("\n");
		return sb.toString();
	}
	
	public void writeObject(ObjectOutputStream stream) throws IOException{
		stream.writeObject(FridayDate);
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		FridayDate=(Date)stream.readObject();;
	}
	
	//----------------------------------------------------
	//user defined functions
	//----------------------------------------------------	
	public static double RoundPercent(double Rval)  throws Exception {
  Rval = Rval * 10000;
  double tmp = Math.round(Rval);
  return (double)tmp/100.0;
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
	
		
		FridayDate.setTime(CurrentDate.getTime() + 2*(1 * 24 * 60 * 60 * 1000));
		
		
		
		if(new Boolean(true).equals(false)){
			
		}
		
		 
		else if (LTIDate.isLastNYSETradingDayOfWeek(FridayDate)) {
		   String[][] ReturnTable = new String[Funds.length + 1][8];
			int i;
			String SecName;
			Security Sec;


			String FirstRow[] = { "Description", "Symbol", "1 Week", "4 Weeks ", "13 Weeks", "26 Weeks", "52 Weeks", "Trend Score" };
			for (i = 0; i < 8; i++) {
				ReturnTable[0][i] = FirstRow[i];
			}
			List<List<String>> lists= new ArrayList<List<String>>();
			for (i = 1; i <= Funds.length; i++) {
				SecName = Funds[i - 1];
				Sec = getSecurity(SecName);
                List<String> sublist = new ArrayList<String>();               
				double oneweek = Sec.getReturn(CurrentDate, TimeUnit.WEEKLY, -1);				
				
				double fourweek = Sec.getReturn(CurrentDate, TimeUnit.WEEKLY, -4);

				double thirteenweek = Sec.getReturn(CurrentDate, TimeUnit.WEEKLY, -13);

				double twentysixweek = Sec.getReturn(CurrentDate, TimeUnit.WEEKLY, -26);

				double fiftytwoweek = Sec.getReturn(CurrentDate, TimeUnit.WEEKLY, -52);

				double average = (oneweek + fourweek + thirteenweek + twentysixweek + fiftytwoweek) / 5;
				
				sublist.add(String.valueOf(average));
				sublist.add(String.valueOf(oneweek));
				sublist.add(String.valueOf(fourweek));
				sublist.add(String.valueOf(thirteenweek));
				sublist.add(String.valueOf(twentysixweek));
				sublist.add(String.valueOf(fiftytwoweek));
				sublist.add(Descriptions[i-1]+"  "+SecName);
				lists.add(sublist);

				ReturnTable[i][2] = String.valueOf(RoundPercent(oneweek)) + "%";
				ReturnTable[i][3] = String.valueOf(RoundPercent(fourweek)) + "%";
				ReturnTable[i][4] = String.valueOf(RoundPercent(thirteenweek)) + "%";
				ReturnTable[i][5] = String.valueOf(RoundPercent(twentysixweek)) + "%";
				ReturnTable[i][6] = String.valueOf(RoundPercent(fiftytwoweek)) + "%";
				ReturnTable[i][7] = String.valueOf(RoundPercent(average)) + "%";
				ReturnTable[i][0] = Descriptions[i - 1];
				ReturnTable[i][1] = SecName;
				ReturnTable[i][1] = "<a target=\"_blank\" href=\"http://www.validfi.com/LTISystem/jsp/fundcenter/View.action?symbol=" + SecName + "&type=1&includeHeader=true\">" + SecName + "</a>";
			}

			printToLog("Writing Table" + CurrentDate);
			printToLog("Table[0]: " + ReturnTable[0][0] + ReturnTable[0][1] + ReturnTable[0][2]);
			printToLog("Table[1]: " + ReturnTable[1][0] + ReturnTable[1][1] + ReturnTable[1][2]);

            lists = Sort.filesort(lists);
            int tablesize;
            if(lists.size()>=20){
            	 tablesize=5;
            }else{
            	tablesize = 3;
            }
            double[][] topvalues = new double[tablesize][5];
        	String[][] topdates = new String[tablesize][5];
        	String[]toptitles = new String[tablesize];
        	double[][] lowvalues = new double[tablesize][5];
        	String[][] lowdates = new String[tablesize][5];
        	String[]lowtitles = new String[tablesize];
        	for(int j=0;j<tablesize;j++){
        		topvalues[j][0]=( Double.parseDouble(lists.get(j).get(5)) )/ 52;
        		topvalues[j][1]=( Double.parseDouble(lists.get(j).get(4)) )/ 26;
        		topvalues[j][2]=( Double.parseDouble(lists.get(j).get(3)) )/ 13;
        		topvalues[j][3]=( Double.parseDouble(lists.get(j).get(2)) )/ 4;
        		topvalues[j][4]=  Double.parseDouble(lists.get(j).get(1)) ;
        		toptitles[j] = lists.get(j).get(6);
        		topdates[j][0] = "52 Weeks";
        		topdates[j][1] = "26 Weeks";
        		topdates[j][2] = "13 Weeks";
        		topdates[j][3] = "4 Weeks";
        		topdates[j][4] = "1 Weeks";
        	}
        	for(int j=lists.size()-1,k=0;j>lists.size()-tablesize-1;j--,k++){
        		lowvalues[k][0]=( Double.parseDouble(lists.get(j).get(5)) )/ 52;
        		lowvalues[k][1]=( Double.parseDouble(lists.get(j).get(4)) )/ 26;
        		lowvalues[k][2]=( Double.parseDouble(lists.get(j).get(3)) )/ 13;
        		lowvalues[k][3]=( Double.parseDouble(lists.get(j).get(2)) )/ 4;
        		lowvalues[k][4]=  Double.parseDouble(lists.get(j).get(1));
        		lowtitles[k] = lists.get(j).get(6);
        		lowdates[k][0] = "52 Weeks";
        		lowdates[k][1] = "26 Weeks";
        		lowdates[k][2] = "13 Weeks";
        		lowdates[k][3] = "4 Weeks";
        		lowdates[k][4] = "1 Weeks";
        	}
  /*************************** create indicator table****************************************************/
            boolean haslastweek = true;
            Object obj=new Object();
             String[][]lastReturnTable=new String[Funds.length][8];
            try{
                 obj=PersistentUtil.readGlobalObject(TableName);
                }catch(Exception e){
                  haslastweek = false;
                  }
            if(haslastweek){
               lastReturnTable = (String[][])obj;
              }
            List<List<String>> tableList = new ArrayList<List<String>>();
            for(int k = 0;k<Funds.length;k++){
            	List<String> list = new ArrayList<String>();
            	String value1 = ReturnTable[k+1][7].replace("%", "");
                String value2="";
                if(haslastweek){
            	     value2 = lastReturnTable[k+1][7].replace("%", "");
                } else{
                     value2 = "0";
          	}
            	list.add(value1);
            	list.add(value2);
            	list.add(ReturnTable[k+1][0]);
            	list.add(Funds[k]);
            	if(Double.parseDouble(value1) > Double.parseDouble(value2)){
            		list.add("^");
            	}else{
            		list.add("v");
            	}
            	tableList.add(list);
            }
            
            tableList=Sort.filesort(tableList);
            
            Date date = new Date();
            date = LTIDate.clearHMSM(date);
            Calendar ca = Calendar.getInstance();
            Calendar lca = Calendar.getInstance();
            ca.setTime(date);
            while(ca.get(Calendar.DAY_OF_WEEK)!=6){
            	ca.add(Calendar.DAY_OF_YEAR, -1);
            }
            lca.setTime(ca.getTime());
            lca.add(Calendar.DAY_OF_YEAR, -7);
            java.text.SimpleDateFormat dateformat = new java.text.SimpleDateFormat("MM/dd"); 
            String dates = dateformat.format(ca.getTime());
            String ldates = dateformat.format(lca.getTime());
            String[][]indicatortable = new String[Funds.length][6];
            
            for(int j=0;j<tableList.size();j++){
            	indicatortable[j][0]=tableList.get(j).get(2);
            	indicatortable[j][1]=tableList.get(j).get(3);
            	indicatortable[j][2]=tableList.get(j).get(0)+"%";
            	indicatortable[j][3]=tableList.get(j).get(1)+"%";
            	indicatortable[j][4]=tableList.get(j).get(4);
            	if(j==0){
            		indicatortable[j][5] = dates;
            	}else{
            		indicatortable[j][5] =ldates;
            	}
            	
            }
 /*******************************************************************************************************/ 
        	PersistentUtil.writeGlobalObject(topvalues, TableName+".topvalues", StrategyID, PortfolioID);
        	PersistentUtil.writeGlobalObject(toptitles, TableName+".toptitles", StrategyID, PortfolioID);
        	PersistentUtil.writeGlobalObject(topdates, TableName+".topdates", StrategyID, PortfolioID);
        	PersistentUtil.writeGlobalObject(lowvalues, TableName+".lowvalues", StrategyID, PortfolioID);
        	PersistentUtil.writeGlobalObject(lowtitles, TableName+".lowtitles", StrategyID, PortfolioID);
        	PersistentUtil.writeGlobalObject(lowdates, TableName+".lowdates", StrategyID, PortfolioID);
                PersistentUtil.writeGlobalObject(indicatortable, TableName+".datas", StrategyID, PortfolioID);
	        PersistentUtil.writeGlobalObject(ReturnTable, TableName, StrategyID, PortfolioID);

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