package com.lti.action.admin.articlecenter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.supercsv.io.CsvListReader;
import org.supercsv.io.CsvListWriter;
import org.supercsv.prefs.CsvPreference;

import com.lti.action.Action;
import com.lti.service.AssetClassManager;
import com.lti.service.SecurityManager;
import com.lti.service.StrategyManager;
import com.lti.service.bo.Security;
import com.lti.service.bo.Strategy;
import com.lti.service.bo.VariableFor401k;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.util.LTIDownLoader;

public class articlecenterAction{
	public String main(){
		
		return Action.SUCCESS;
	}
	
	private String planID;
	public String plan() throws Exception{
		StrategyManager stragetyManager = ContextHolder.getStrategyManager();
		if(planID.equals("0")){
			List<Strategy> planList = stragetyManager.getStrategiesByType(Configuration.STRATEGY_TYPE_401K);
			getFundsNum(planList);
		}else{
			String [] idList = planID.split(",");
			List<Strategy> planList = new ArrayList<Strategy>();
			for(String ss:idList){
				long ID = Long.parseLong(ss);
				Strategy plan = stragetyManager.get(ID);
				if(!plan.is401K())continue;
				
				if(plan!=null){
					planList.add(plan);	
				}				
			}
		 getFundsNum(planList);
		}
	
		return Action.SUCCESS;
	}
	
	private void getFundsNum(List<Strategy> planList) throws Exception{
		
		SecurityManager securityManager = ContextHolder.getSecurityManager();
		AssetClassManager assetClassManager = ContextHolder.getAssetClassManager();
		List <String> headList = new ArrayList<String>();
		LTIDownLoader ldl = new LTIDownLoader();
		String filePath = ldl.systemPath+"plan.csv";
		File  planFile = new File(filePath);
		FileOutputStream fos = new FileOutputStream(planFile);
		OutputStreamWriter osw = new OutputStreamWriter(fos);
		CsvListWriter clw = new CsvListWriter(osw, CsvPreference.STANDARD_PREFERENCE);
		String []head={"PlanID","PlanName","Equity(1)","US Equity(2)","US Large Cap(3)","LARGE VALUE(4)","LARGE BLEND(4)",
			       "LARGE GROWTH(4)","US Mid Cap(3)","MID-CAP VALUE(4)","MID-CAP BLEND(4)","MID-CAP GROWTH(4)","US Small Cap(3)",
			       "SMALL VALUE(4)","SMALL BLEND(4)","SMALL GROWTH(4)","Micro Cap(3)","US Equity Moderate Allocation(3)",
			       "International Equity(2)","Foreigh Small Cap(3)","Foreigh Small Value(4)","Developed Countries(3)",
			       "Europe Stock(4)","Greece Equity(4)","Europe Large-Cap Blend Equity(4)","Japan Stock(4)","Emerging Market(3)",
			       "China Equity(4)","Diversified Emerging MKTS(4)","Latin America Stock(4)","China Region(4)",
			       "Foreign Small/Mid Cap(3)","Foreign Small/Mid Value(4)","Foreign Small/Mid Growth(4)",
			       "Pacific/Asia Equity(3)","Diversified Pacific/Asia(4)","Pacific/Asia Ex-Japan STK(4)",
			       "Foreign Large Cap(3)","Foreign Large Growth(4)","Foreigh Large Blend(4)","Foreign Large Value(4)",
			       "World Stock(3)","Fixed INCOME(1)", "US Municipal Bonds(2)","Hign Yield Muni(3)","US Corporate Bonds(2)",
			       "Investement Grade(3)","High Yield Bond(3)","Intermediate-Term Bond(3)","Long-Term Bond(3)","Short-Term Bond(3)",
			       "Ultrashort Bonds(3)","International Bonds(2)","Foregin Government Bonds(3)","Fordign Corporate Bonds(3)","Foreign Hign Yield Bonds(3)","World Bond(3)",
			       "Emerging Markets Bond(3)","US Treasury Bonds(2)","Short Government(3)","Long Government(3)","Intermediate Government(3)",
			       "Money Market(2)","Bank Load(2)","Multisector Bond(2)","Inflation-Protected Bond(2)","Cash Asset(1)","Cash(2)","CD(2)",
			       "HYBRID ASSETS(1)","Balance Fund(2)","Moderate Allocation(3)","Conservative Allocation(3)","World Allocation(3)",
			       "Retirement Income(3)","Target Date 2000-2010(3)","Target Date 2011-2015(3)","Target Date 2016-20120(3)",
			       "Target Date 2021-2025(3)","Target Date 2031-2035(3)","Target Date 2041-2045(3)","Target Date 2026-2030(3)",
			       "Target Date 2036-2040(3)","Target Date 2050+(3)", "Aggressive Allocation(3)","Preferred Securities(2)","Convertible Securities(2)",
			       "Convertibles(2)","REAL ESTATE(1)","US Real Estate(2)","Non-US Real Estate(2)","Global Real Estate(2)",
			       "Commodities(1)","Commodities Broad Basket(2)","Commodities Precious Metals(2)","Commodities Agriculture(2)","Commodities Industrial Metals(2)",
			       "Commodities Energy(2)","Commodities Miscellaneous(2)","HEDGES(1)","Long-Shout(1)","Alternative(1)","Bear Market(2)",
			       "Currency(2)","Market Neutral(2)","Sector Equity(1)","Precious Metals(2)","Miscellaneous(2)","Utilities(2)","Health(2)",
			       "Communications(2)","Natural Resources(2)","Technology(2)","Financial(2)","Consumer Staples(2)",
			       "Consumer Discretionary(2)","Industrials(2)","Energy(2)","SPECIALTY-REAL ESTATE(2)","Equity Energy(2)",
			       "SPECIALTY-UTILITIES(2)","SPECIALTY-FINANCIAL(2)","SPECIALTY-HEALTH(2)","EQUITY PRECIOUS METALS(2)",
			       "SPECIALTY-TECHNOLOGY(2)","SPECIALTY-NATURAL RES(2)","SPECIALTY-COMMUNICATIONS(2)","SPECIALTY-PRECIOUS METALS(2)",
			       "MISCELLANEOUS SECTOR(2)","Miscellaneous-Income and Real Property(2)","Capital Protected(1)"};
		for(String s: head){
			headList.add(s);
		}
		clw.write(headList);
		
		for(Strategy plan :planList){
			int []number = new int[129];
			for(int i=0;i<129;i++) number[i] = 0;
			List<VariableFor401k> candidateFunds = plan.getVariablesFor401k();
			for(VariableFor401k var: candidateFunds){
				Security se = securityManager.getBySymbol(var.getSymbol());
				if(se!=null && se.getAssetClass()!=null){
					String assetName = se.getAssetClass().getName();
					if(assetClassManager.isUpperOrSameClass("EQUITY", assetName)){
						number[0]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("US EQUITY", assetName)){
						number[1]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("US Large Cap", assetName)){
						number[2]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("LARGE VALUE", assetName)){
						number[3]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("LARGE BLEND", assetName)){
						number[4]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("LARGE GROWTH", assetName)){
						number[5]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("US Mid Cap", assetName)){
						number[6]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("MID-CAP VALUE", assetName)){
						number[7]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("MID-CAP BLEND", assetName)){
						number[8]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("Mid-Cap Growth", assetName)){
						number[9]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("US Small Cap", assetName)){
						number[10]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("SMALL VALUE", assetName)){
						number[11]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("SMALL BLEND", assetName)){
						number[12]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("Small Growth", assetName)){
						number[13]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("Micro Cap", assetName)){
						number[14]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("US Equity Moderate Allocation", assetName)){
						number[15]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("INTERNATIONAL EQUITY", assetName)){
						number[16]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("Foreign Small Cap", assetName)){
						number[17]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("Foreign Small Value", assetName)){
						number[18]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("Developed Countries", assetName)){
						number[19]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("EUROPE STOCK", assetName)){
						number[20]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("Greece Equity", assetName)){
						number[21]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("Europe Large-Cap Blend Equity", assetName)){
						number[22]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("JAPAN STOCK", assetName)){
						number[23]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("Emerging Market", assetName)){
						number[24]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("China Equity", assetName)){
						number[25]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("DIVERSIFIED EMERGING MKTS", assetName)){
						number[26]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("Latin America Stock", assetName)){
						number[27]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("China Region", assetName)){
						number[28]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("Foreign Small/Mid Cap", assetName)){
						number[29]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("Foreign Small/Mid Value", assetName)){
						number[30]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("FOREIGN SMALL/MID GROWTH", assetName)){
						number[31]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("Pacific/Asia Equity", assetName)){
						number[32]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("DIVERSIFIED PACIFIC/ASIA", assetName)){
						number[33]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("PACIFIC/ASIA EX-JAPAN STK", assetName)){
						number[34]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("Foreign Large Cap", assetName)){
						number[35]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("Foreign Large Growth", assetName)){
						number[36]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("Foreign Large Blend", assetName)){
						number[37]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("Foreign Large Value", assetName)){
						number[38]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("WORLD STOCK", assetName)){
						number[39]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("FIXED INCOME", assetName)){
						number[40]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("US MUNICIPAL BONDS", assetName)){
						number[41]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("Hign Yield Muni", assetName)){
						number[42]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("US CORPORATE BONDS", assetName)){
						number[43]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("INVESTMENT GRADE", assetName)){
						number[44]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("High Yield Bond", assetName)){
						number[45]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("Intermediate-Term Bond", assetName)){
						number[46]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("Long-Term Bond", assetName)){
						number[47]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("Short-Term Bond", assetName)){
						number[48]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("ULTRASHORT BOND", assetName)){
						number[49]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("INTERNATIONAL BONDS", assetName)){
						number[50]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("Foreign Goverment Bonds", assetName)){
						number[51]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("Foreign Corporate Bonds", assetName)){
						number[52]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("Foreign Hign Yield Bonds", assetName)){
						number[53]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("WORLD BOND", assetName)){
						number[54]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("Emerging Markets Bond", assetName)){
						number[55]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("US TREASURY BONDS", assetName)){
						number[56]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("SHORT GOVERNMENT", assetName)){
						number[57]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("LONG GOVERNMENT", assetName)){
						number[58]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("Intermediate Government", assetName)){
						number[59]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("Money Market", assetName)){
						number[60]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("Bank Load", assetName)){
						number[61]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("Multisector Bond", assetName)){
						number[62]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("Inflation-Protected Bond", assetName)){
						number[63]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("CASH ASSET", assetName)){
						number[64]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("CASH", assetName)){
						number[65]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("CD", assetName)){
						number[66]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("HYBRID ASSETS", assetName)){
						number[67]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("BALANDE FUND", assetName)){
						number[68]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("Moderate Allocation", assetName)){
						number[69]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("Conservative Allocation", assetName)){
						number[70]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("World Allocation", assetName)){
						number[71]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("Retirement Income", assetName)){
						number[72]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("Target Date 2000-2010", assetName)){
						number[73]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("Target Date 2011-2015", assetName)){
						number[74]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("Target Date 2016-2020", assetName)){
						number[75]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("Target Date 2021-2025", assetName)){
						number[76]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("Target Date 2031-205", assetName)){
						number[77]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("Target Date 2041-2045", assetName)){
						number[78]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("Target Date 2026-2030", assetName)){
						number[79]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("Target Date 2036-2040", assetName)){
						number[80]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("Target Date 2050+", assetName)){
						number[81]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("Aggressive Allocation", assetName)){
						number[82]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("PREFERRED SECURITIES", assetName)){
						number[83]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("CONVERTIBLE SECURITIES", assetName)){
						number[84]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("Convertibles", assetName)){
						number[85]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("REAL ESTATE", assetName)){
						number[86]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("US Real Estate", assetName)){
						number[87]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("Non-US Real Estate", assetName)){
						number[88]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("Global Real Estate", assetName)){
						number[89]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("COMMODITIES", assetName)){
						number[90]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("COMMODITIES BROAD BASKET", assetName)){
						number[91]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("Commodities Precious Metals", assetName)){
						number[92]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("Commodities Agriculture", assetName)){
						number[93]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("Commodities Industrial Metals", assetName)){
						number[94]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("Commodities Energy", assetName)){
						number[95]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("Commodities Miscellaneous", assetName)){
						number[96]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("HEDGES", assetName)){
						number[97]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("Long-Short", assetName)){
						number[98]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("Alternative", assetName)){
						number[99]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("Bear Market", assetName)){
						number[100]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("Currency", assetName)){
						number[101]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("Market Neutral", assetName)){
						number[102]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("SECTOR EQUITY", assetName)){
						number[103]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("Precious Metals", assetName)){
						number[104]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("Miscellaneous", assetName)){
						number[105]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("Utilities", assetName)){
						number[106]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("Health", assetName)){
						number[107]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("Communications", assetName)){
						number[108]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("Natural Resources", assetName)){
						number[109]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("Technology", assetName)){
						number[110]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("Financial", assetName)){
						number[111]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("Consumer Staples", assetName)){
						number[112]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("Consumer Discretionary", assetName)){
						number[113]+=1;
					}					
					if(assetClassManager.isUpperOrSameClass("Industrials", assetName)){
						number[114]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("Energy", assetName)){
						number[115]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("SPECIALTY-REAL ESTATE", assetName)){
						number[116]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("Equity Energy", assetName)){
						number[117]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("SPECIALTY-UTILITIES", assetName)){
						number[118]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("SPECIALTY-FINANCIAL", assetName)){
						number[119]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("SPECIALTY-HEALTH", assetName)){
						number[120]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("EQUITY PRECIOUS METALS", assetName)){
						number[121]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("SPECIALTY-TECHNOLOGY", assetName)){
						number[122]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("SPECIALTY-NATURAL RES", assetName)){
						number[123]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("SPECIALTY-COMMUNICATIONS", assetName)){
						number[124]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("SPECIALTY-PRECIOUS METALS", assetName)){
						number[125]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("MISCELLANEOUS SECTOR", assetName)){
						number[126]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("Miscellaneous-Income and Real Property", assetName)){
						number[127]+=1;
					}
					if(assetClassManager.isUpperOrSameClass("Capital Protected", assetName)){
						number[128]+=1;
					}

					
				}
			}
			
			List<String> lists = new ArrayList<String>();
			lists.add(plan.getID().toString());
			lists.add(plan.getName());
			for(int i:number){
				lists.add(String.valueOf(i));
			}
			clw.write(lists);
			
		}
		clw.close();
		fos.close();
	}
	
	public String getPlanSta() throws Exception{
		LTIDownLoader ltd = new LTIDownLoader();
		String filePath = ltd.systemPath+"plan.csv";
		File planFile = new File(filePath);
		fis = new FileInputStream(planFile);
		planName = planFile.getName();
		return Action.DOWNLOAD;
	}
	
	private File uploadFile;
	private String uploadFileFileName;
	public String mutualFunds() throws Exception{
		if (uploadFile != null) {
			if (uploadFileFileName.endsWith(".csv")) {
				InputStream stream = new FileInputStream(uploadFile);
				OutputStream bos = new FileOutputStream(URLDecoder.decode(com.lti.action.mutualfund.ArticleAction.class.getResource("mutualFunds.csv").getFile(), "utf-8"));
				int bytesRead = 0;
				byte[] buffer = new byte[8192];
				while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
					bos.write(buffer, 0, bytesRead);
				}
				bos.close();
				stream.close();
				
			}

		}
		return Action.SUCCESS;
	}
	
	private InputStream fis;
	private String planName;
	public String downloadCsv() throws Exception{
		File mutualFile = new File(URLDecoder.decode(com.lti.action.mutualfund.ArticleAction.class.getResource("mutualFunds.csv").getFile(), "utf-8"));
		fis = new FileInputStream(mutualFile);
		planName = mutualFile.getName();
		return Action.DOWNLOAD;
	}
	public String getPlanID() {
		return planID;
	}
	public void setPlanID(String planID) {
		this.planID = planID;
	}
	public File getUploadFile() {
		return uploadFile;
	}
	public void setUploadFile(File uploadFile) {
		this.uploadFile = uploadFile;
	}
	public InputStream getFis() {
		return fis;
	}
	public void setFis(InputStream fis) {
		this.fis = fis;
	}
	public String getPlanName() {
		return planName;
	}
	public void setPlanName(String planName) {
		this.planName = planName;
	}
	public String getUploadFileFileName() {
		return uploadFileFileName;
	}
	public void setUploadFileFileName(String uploadFileFileName) {
		this.uploadFileFileName = uploadFileFileName;
	}
}