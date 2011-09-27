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
public class ETF_Style_Table5449 extends SimulateStrategy{
	public ETF_Style_Table5449(){
		super();
		StrategyID=5449L;
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
		Descriptions=(String[])ParameterUtil.fetchParameter("String[]","Descriptions", "US Stocks, International Developed Stks, Emerging Market Stks, Frontier Market Stks, US Equity REITs, International REITs,Commodities,Gold,Total US Bonds,International Treasury Bonds,US Credit Bonds,US High Yield Bonds,Mortgage Back Bonds,Municipal Bonds, Intermediate Treasuries, Treasury Bills", parameters);
		Funds=(String[])ParameterUtil.fetchParameter("String[]","Funds", "VTI,EFA,VWO,FRN,VNQ,RWX,GSG,GLD,BND,BWX,CFT,JNK,MBB,MUB,IEF,SHV ", parameters);
		TableName=(String)ParameterUtil.fetchParameter("String","TableName", "USLargeCapBlend", parameters);
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

public  Long getMonthVolume(Long id, java.util.Date endDate)  throws Exception {
        java.text.DateFormat _df = new java.text.SimpleDateFormat("yyyy-MM-dd"); 
        Date startDate = LTIDate.getNewNYSEMonth(endDate, -3);  
        String sql = "select sum(volume)*1.0/count(*) from ltisystem_securitydailydata where securityid=" + id;  
      
        sql += " and date >= '" + _df.format(startDate) + "'";  
        sql += " and date <= '" + _df.format(endDate) + "'";  
      
        java.math.BigDecimal v = (java.math.BigDecimal) securityManager.findBySQL(sql).get(0);  
      
        return v.longValue();  
       
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
		   String[][] ReturnTable = new String[Funds.length + 1][7];  
             int i;  
             String SecName;  
             Security Sec;  
   
   
             String FirstRow[] = { "Description", "Symbol", "1 Yr", "3 Yr", "5 Yr", "Avg. Volume(K)", "1 Yr Sharpe" };  
             for (i = 0; i < 7; i++) {  
                 ReturnTable[0][i] = FirstRow[i];  
             }  
             List<List<String>> lists= new ArrayList<List<String>>();  
             for (i = 1; i <= Funds.length; i++) {  
                 SecName = Funds[i - 1];  
                 Sec = getSecurity(SecName);  
                 List<String> sublist = new ArrayList<String>();    
                 double currentPrice = Sec.getAdjClose(CurrentDate);             
                 double OneYr=0.0;
                 double ThreeYr =0.0;
                 double FiveYr = 0.0;
                 try{   
                         OneYr = Sec.getReturn(CurrentDate, TimeUnit.YEARLY, -1);
                	 ThreeYr = java.lang.Math.pow(Sec.getReturn(CurrentDate, TimeUnit.YEARLY, -3)+1, 1.0/3)-1;
		         FiveYr = java.lang.Math.pow(Sec.getReturn(CurrentDate, TimeUnit.YEARLY, -5)+1, 1.0/5)-1;
                    }catch(NoPriceException e){
                	 
                    }
                 
 /* NEED CHANGES: expenses and volume data. Also getSharpeRatio */  
//                 double Expenses = 0.0;  
                 double Volume = 0.0;  
                  double Sharpe = 0.0;
                 try{
                	 Sharpe = Sec.getSharpeRatio(-12, CurrentDate, TimeUnit.MONTHLY, false);
                 }catch(NoPriceException e){
                	 
                 }
                 java.text.DecimalFormat df = new java.text.DecimalFormat("####0.0000");
	         java.text.DecimalFormat df2 = new java.text.DecimalFormat("#,###,###,###");
                
                  try{
                	 Volume = getMonthVolume(Sec.getID(), CurrentDate)/1000; 
                 }catch(Exception e){
                	 
                 }
                 
  
                 
//                 sublist.add(String.valueOf(OneYr));  
//                 sublist.add(String.valueOf(ThreeYr));  
//                 sublist.add(String.valueOf(FiveYr));  
//                 sublist.add(String.valueOf(Expenses));
//                 sublist.add(String.valueOf(Sharpe));  
//                 sublist.add(String.valueOf(Volume));  
//                 sublist.add(Descriptions[i-1]+"  "+SecName);  
//                 lists.add(sublist);  
   
                if(OneYr == 0.0){
                	ReturnTable[i][2]="NA";
                }else{
                	ReturnTable[i][2] = String.valueOf(RoundPercent(OneYr)) + "%";
                }
                	
		if(ThreeYr == 0.0){
			ReturnTable[i][3]="NA";
		}else{
				ReturnTable[i][3] = String.valueOf(RoundPercent(ThreeYr)) + "%";
			}
				
		if(FiveYr == 0.0){
			ReturnTable[i][4]="NA";
			}else{
					ReturnTable[i][4] = String.valueOf(RoundPercent(FiveYr)) + "%";
				}
//                 ReturnTable[i][5] = String.valueOf(RoundPercent(Expenses)) + "%";  
                 ReturnTable[i][5] = String.valueOf(df2.format(Volume));

		if(Sharpe == 0.0){
			ReturnTable[i][6]="NA";
		}else{
			ReturnTable[i][6] = String.valueOf(RoundPercent(Double.parseDouble(df.format(Sharpe)))) + "%";
			}

                 ReturnTable[i][0] = Descriptions[i - 1];  
                 ReturnTable[i][1] = SecName;  
                 ReturnTable[i][1] = "<a target=\"_blank\" href=\"http://www.myplaniq.com/LTISystem/jsp/fundcenter/View.action?symbol=" + SecName + "&type=1&includeHeader=true\">" + SecName + "</a>";  
             }  
   
             printToLog("Writing Table" + CurrentDate);  
             printToLog("Table[0]: " + ReturnTable[0][0] + ReturnTable[0][1] + ReturnTable[0][2]);  
             printToLog("Table[1]: " + ReturnTable[1][0] + ReturnTable[1][1] + ReturnTable[1][2]);  
             java.text.SimpleDateFormat dateformats =new java.text.SimpleDateFormat("MM/dd/yyyy");   
             String Datetime = dateformats.format(CurrentDate);  
   
//             lists = Sort.filesort(lists);  
//             int tablesize;  
//             if(lists.size()>=20){  
//                  tablesize=5;  
//             }else{  
//                  tablesize = 3;  
//             }  
//             double[][] topvalues = new double[tablesize][5];  
//             String[][] topdates = new String[tablesize][5];  
//             String[]toptitles = new String[tablesize];  
//             double[][] lowvalues = new double[tablesize][5];  
//             String[][] lowdates = new String[tablesize][5];  
//             String[]lowtitles = new String[tablesize];  
//             for(int j=0;j<tablesize;j++){  
// /* NEED CHANGES */  
//                 topvalues[j][0]=( Double.parseDouble(lists.get(j).get(5)) )/ 52;  
//                 topvalues[j][1]=( Double.parseDouble(lists.get(j).get(4)) )/ 26;  
//                 topvalues[j][2]=( Double.parseDouble(lists.get(j).get(3)) )/ 13;  
//                 topvalues[j][3]=( Double.parseDouble(lists.get(j).get(2)) )/ 4;  
//                 topvalues[j][4]=  Double.parseDouble(lists.get(j).get(1)) ;  
//                 toptitles[j] = lists.get(j).get(6);  
//                 topdates[j][0] = "52 Weeks";  
//                 topdates[j][1] = "26 Weeks";  
//                 topdates[j][2] = "13 Weeks";  
//                 topdates[j][3] = "4 Weeks";  
//                 topdates[j][4] = "1 Weeks";  
//             }  
//             for(int j=lists.size()-1,k=0;j>lists.size()-tablesize-1;j--,k++){  
//                 lowvalues[k][0]=( Double.parseDouble(lists.get(j).get(5)) )/ 52;  
//                 lowvalues[k][1]=( Double.parseDouble(lists.get(j).get(4)) )/ 26;  
//                 lowvalues[k][2]=( Double.parseDouble(lists.get(j).get(3)) )/ 13;  
//                 lowvalues[k][3]=( Double.parseDouble(lists.get(j).get(2)) )/ 4;  
//                 lowvalues[k][4]=  Double.parseDouble(lists.get(j).get(1));  
//                 lowtitles[k] = lists.get(j).get(6);  
//                 lowdates[k][0] = "52 Weeks";  
//                 lowdates[k][1] = "26 Weeks";  
//                 lowdates[k][2] = "13 Weeks";  
//                 lowdates[k][3] = "4 Weeks";  
//                 lowdates[k][4] = "1 Weeks";  
//             }  
//   /*************************** create indicator table****************************************************/  
//             boolean haslastweek = true;  
//             Object obj=new Object();  
//              String[][]lastReturnTable=new String[Funds.length][8];  
//             try{  
//                  obj=PersistentUtil.readGlobalObject(TableName);  
//                 }catch(Exception e){  
//                   haslastweek = false;  
//                   }  
//             if(haslastweek){  
//                lastReturnTable = (String[][])obj;  
//               }  
//             List<List<String>> tableList = new ArrayList<List<String>>();  
//             for(int k = 0;k<Funds.length;k++){  
//                 List<String> list = new ArrayList<String>();  
//                 String value1 = ReturnTable[k+1][7].replace("%", "");  
//                 String value2="";  
//                 if(haslastweek){  
//                      value2 = lastReturnTable[k+1][7].replace("%", "");  
//                 } else{  
//                      value2 = "0";  
//             }  
//                 list.add(value1);  
//                 list.add(value2);  
//                 list.add(ReturnTable[k+1][0]);  
//                 list.add(Funds[k]);  
//                 if(Double.parseDouble(value1) > Double.parseDouble(value2)){  
//                     list.add("^");  
//                 }else{  
//                     list.add("v");  
//                 }  
//                 tableList.add(list);  
//             }  
//               
//             tableList=Sort.filesort(tableList);  
//               
//             Date date = new Date();  
//             date = LTIDate.clearHMSM(date);  
//             Calendar ca = Calendar.getInstance();  
//             Calendar lca = Calendar.getInstance();  
//             ca.setTime(date);  
//             while(ca.get(Calendar.DAY_OF_WEEK)!=caculateday+1){  
//                 ca.add(Calendar.DAY_OF_YEAR, -1);  
//             }  
//             lca.setTime(ca.getTime());  
//             lca.add(Calendar.DAY_OF_YEAR, -7);  
//             java.text.SimpleDateFormat dateformat = new java.text.SimpleDateFormat("MM/dd");   
//             String dates = dateformat.format(ca.getTime());  
//             String ldates = dateformat.format(lca.getTime());  
//             String[][]indicatortable = new String[Funds.length][6];  
//               
//             for(int j=0;j<tableList.size();j++){  
//                 indicatortable[j][0]=tableList.get(j).get(2);  
//                 indicatortable[j][1]=tableList.get(j).get(3);  
//                 indicatortable[j][2]=tableList.get(j).get(0)+"%";  
//                 indicatortable[j][3]=tableList.get(j).get(1)+"%";  
//                 indicatortable[j][4]=tableList.get(j).get(4);  
//                 if(j==0){  
//                     indicatortable[j][5] = dates;  
//                 }else{  
//                     indicatortable[j][5] =ldates;  
//                 }  
//                   
//             }  
  /*******************************************************************************************************/   
//             PersistentUtil.writeGlobalObject(topvalues, TableName+".topvalues", StrategyID, PortfolioID);  
//             PersistentUtil.writeGlobalObject(toptitles, TableName+".toptitles", StrategyID, PortfolioID);  
//             PersistentUtil.writeGlobalObject(topdates, TableName+".topdates", StrategyID, PortfolioID);  
//             PersistentUtil.writeGlobalObject(lowvalues, TableName+".lowvalues", StrategyID, PortfolioID);  
//             PersistentUtil.writeGlobalObject(lowtitles, TableName+".lowtitles", StrategyID, PortfolioID);  
//             PersistentUtil.writeGlobalObject(lowdates, TableName+".lowdates", StrategyID, PortfolioID);  
//             PersistentUtil.writeGlobalObject(indicatortable, TableName+".datas", StrategyID, PortfolioID);  
             PersistentUtil.writeGlobalObject(ReturnTable, TableName, StrategyID, PortfolioID);  
             PersistentUtil.writeGlobalObject(Datetime, TableName+".time", StrategyID, PortfolioID); 
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