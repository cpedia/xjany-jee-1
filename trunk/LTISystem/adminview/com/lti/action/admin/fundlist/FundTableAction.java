package com.lti.action.admin.fundlist;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.lti.action.Action;
import com.lti.bean.K401FileBean;
import com.lti.jobscheduler.FundStateControler;
import com.lti.jobscheduler.FundStateJob;
import com.lti.jobscheduler.TickerSearchControler;
import com.lti.jobscheduler.TickerSearchJob;
import com.lti.service.SecurityManager;
import com.lti.service.StrategyManager;
import com.lti.service.bo.Security;
import com.lti.service.bo.Strategy;
import com.lti.service.bo.VariableFor401k;
import com.lti.system.ContextHolder;
import com.lti.util.FileOperator;
import com.lti.util.Edgar11K;
import com.lti.util.Parse401KParameters;
import com.lti.util.SecurityManagerListener;
import com.lti.util.html.ParseHtmlTable;
import com.lti.system.Configuration;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 
 * @author SuPing
 *	2010/08/02
 */
public class FundTableAction extends ActionSupport implements Action{

	private StrategyManager strategyManager;
	private SecurityManager securityManager;
	public String fileName;
	public InputStream inputStream;
	private String action;
	private String idString;
	private String message;
	
	public String getIdString() {
		return idString;
	}

	public void setIdString(String idString) {
		this.idString = idString;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public StrategyManager getStrategyManager() {
		return strategyManager;
	}

	public void setStrategyManager(StrategyManager strategyManager) {
		this.strategyManager = strategyManager;
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public SecurityManager getSecurityManager() {
		return securityManager;
	}

	public void setSecurityManager(SecurityManager securityManager) {
		this.securityManager = securityManager;
	}
	
	public String main(){
		return Action.SUCCESS;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Get all of the files in the folder
	 * @return
	 */
	public static List<String> getRexPath(){
		String sysPath;
		String str = System.getProperty("os.name").toUpperCase();
		ArrayList<String> files = new ArrayList<String>();
		ArrayList<String> tfiles = new ArrayList<String>();
		if (str.indexOf("WINDOWS") != -1){
			//sysPath=System.getenv("windir")+"\\temp\\";	
			sysPath="E:\\SuPing WorkPlace\\edgar_test\\";
			//sysPath="E:\\SuPing WorkPlace\\edgar\\data\\";
			filelist(files,sysPath,"WINDOWS");
		}
		else {
			sysPath = "/home/sshadmin/validfi/11k/401k/";	
			filelist(files,sysPath,"LINUX");
		}
		try{
			for(int i=0;i<files.size();i++){
				if(files.get(i).endsWith("txt"))
					tfiles.add(files.get(i));
		}
		}catch(Exception e){e.printStackTrace();}
		return tfiles;
	}
	
	/**
	 * @author SuPing
	 * get the file directory list
	 * 2009/10/22
	 */
	public static void filelist(List<String> lst,String path,String system){   
		File f = new File(path);
		String delimiter = null;
		//if(!f.exists()) f.mkdirs(); 
		if(system.equals("WINDOWS")) 
			delimiter = "\\";
		else if(system.equals("LINUX"))
			delimiter = "/";
		if(f.isDirectory()){   
			lst.add(f.getAbsolutePath()+delimiter);   
			String dirs[] = f.list();   
			for(int i=0;dirs!=null&&i<dirs.length;i++){   
				filelist(lst,f.getAbsolutePath()+delimiter+dirs[i],system);   
			}   
		}   
		if(f.isFile())lst.add(f.getAbsolutePath());   
	}
	
	/**
	 * Create the 401k HTML file
	 * @param fileName
	 * @param ID
	 */
	public void getContent(String fileName,Long ID){
		String content = null;
		try {
			content = FileOperator.getContent(fileName);
			if(content == null){
				content = "check it!!";
			}
			File root=new File(Configuration.get11KDir()+"PlanFile");
			if(!root.exists()){
				root.mkdirs();
			}
			File planFile = new File(root,ID+".htm");
			
			if(!planFile.exists()){
				planFile.createNewFile();
			}
			FileOperator.appendMethodB(planFile.getAbsolutePath(), content, false);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void get401KContent(String fileName,Long ID){
		try{
			File root=new File(Configuration.get11KDir()+"PlanFile");
			if(!root.exists()){
				root.mkdirs();
			}
			File planFile = new File(root,ID+".htm");
			if(!planFile.exists()){
				planFile.createNewFile();
			}
			copyFile(fileName, planFile.toString());
		}
		catch(Exception e){
			System.out.println(e.toString());
			e.printStackTrace();
		}
	}
	
	/** 
     * copy a file 
     * @param oldPath String c:/A.txt 
     * @param newPath String f:/B.html
     * @return boolean 
     */ 
   public void copyFile(String oldPath, String newPath) { 
       try { 
           int bytesum = 0; 
           int byteread = 0; 
           File oldfile = new File(oldPath); 
           if (oldfile.exists()) { 
               InputStream inStream = new FileInputStream(oldPath); //读入原文件 
               FileOutputStream fs = new FileOutputStream(newPath); 
               byte[] buffer = new byte[1444]; 
               int length; 
               while ( (byteread = inStream.read(buffer)) != -1) { 
                   bytesum += byteread; //字节数 文件大小 
                   //System.out.println(bytesum); 
                   fs.write(buffer, 0, byteread); 
               } 
               inStream.close(); 
           } 
       } 
       catch (Exception e) { 
           System.out.println(e.toString()); 
           e.printStackTrace(); 

       } 
   } 
	
	/**
	 * Create the 401k complete report
	 * @param failFiles
	 * @param largeFiles
	 * @param sucFiles
	 * @param exsitFiles
	 * @throws Exception
	 */
	public void creat401kFile(List<String> failFiles,List<String> largeFiles,List<String> smallFiles,List<K401FileBean> sucFiles,List<String> exsitFiles,List<String> nullContentFiles) throws Exception{
		String systemPath;
		String sysPath = System.getenv("windir");
		Date today = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		if(!Configuration.isLinux())systemPath=sysPath+"\\401kfile\\";
		else systemPath="/home/sshadmin/validfi/11k/401k/";
		String path = systemPath+"401kFiles.csv";	
		FileOutputStream fos = new FileOutputStream(path);
		OutputStreamWriter osw = new OutputStreamWriter(fos);
		osw.write("401K Files List. File Create Time : "+sdf.format(today)+"\t\n\t\n");
		if(failFiles!=null){
			osw.write("\r\nThe fail file list("+failFiles.size()+"):\r\n");
			if(failFiles.size()>0){
				for(int i=0;i<failFiles.size();i++){
					osw.write(failFiles.get(i)+"\t\n");
				}
			}
		}
		if(largeFiles!=null){
			osw.write("\r\nThe large file list("+largeFiles.size()+"):\r\n");
			if(largeFiles.size()>0){
				for(int i=0;i<largeFiles.size();i++){
					osw.write(largeFiles.get(i)+"\t\n");
				}
			}
		}
		if(smallFiles!=null){
			osw.write("\r\nThe small file list("+smallFiles.size()+"):\r\n");
			if(smallFiles.size()>0){
				for(int i=0;i<smallFiles.size();i++){
					osw.write(smallFiles.get(i)+"\t\n");
				}
			}
		}
		if(nullContentFiles!=null){
			osw.write("\r\nThe null content file list("+nullContentFiles.size()+"):\r\n");
			if(nullContentFiles.size()>0){
				for(int i=0;i<nullContentFiles.size();i++){
					osw.write(nullContentFiles.get(i)+"\t\n");
				}
			}
		}
		if(sucFiles!=null){
			osw.write("\r\nThe completely finished file list("+sucFiles.size()+"):\r\n");
			if(sucFiles.size()>0){
				osw.write("File Path,Web Link,Plan Name\r\n");
				for(int i=0;i<sucFiles.size();i++){
					osw.write(sucFiles.get(i).getFilePath()+","+"http://www.myplaniq.com/LTISystem/f401k_fundtable.action?ID="+sucFiles.get(i).getID()+","+sucFiles.get(i).getPlanName().replaceAll(",", "")+"\t\n");
				}
			}
		}
		if(exsitFiles!=null ){
			if(exsitFiles.size()>0){
				osw.write("\r\nThe exsit file list("+exsitFiles.size()+"):\r\n");
				for(int i=0;i<exsitFiles.size();i++){
					osw.write(exsitFiles.get(i)+"\t\n");
				}
			}
		}
		osw.close();
		fos.close();
	}
	
	/**
	 * The action for download the 401k file
	 * @return
	 * @throws Exception
	 */
	public String Load401kFile() throws Exception{
		String systemPath;
		Date today = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		String sysPath = System.getenv("windir");
		if(!Configuration.isLinux())systemPath=sysPath+"\\401kfile\\";
		else systemPath="/home/sshadmin/validfi/11k/401k/";
		String path = systemPath+"401kFiles.csv";	
		File f = new File(path);
		if(f.exists()){
			fileName = sdf.format(today)+"_401kFiles.csv";
			inputStream = new FileInputStream(f);
		}
		else{
			fileName = "No401kFileLists.csv";
			String path2 = systemPath+"No401kFileLists.csv";
			File f2 = new File(path2.toString());
			FileOutputStream fos = new FileOutputStream(path2,true);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			osw.write("The date of download log:"+sdf.format(today)+"\t's FundTable has not started operating today!There is no log!\t\n");	
			osw.close();
			fos.close();
			inputStream=new FileInputStream(f2);
		}
		return Action.DOWNLOAD;
	}
	
	/**
	 * Get all of the 401k files' fund description into database and create the new plan.
	 */
	public void complete401kFile(){
		strategyManager = ContextHolder.getStrategyManager();
		List<String> files = getRexPath();
		int size = files.size();
		FundStateControler.state = "Execute FundTable List";
		FundStateControler.current_count = 0;
		FundStateControler.total_count = size;
		List<String> largeFiles = new ArrayList<String>();
		List<String> smallFiles = new ArrayList<String>();
		List<String> failFiles = new ArrayList<String>();
		List<K401FileBean> sucFiles = new ArrayList<K401FileBean>();
		List<String> exsitFiles = new ArrayList<String>();
		List<String> nullContentFiles = new ArrayList<String>();
		for(int i=0;i<size;i++){
			String fileName = "";
			try{
				Strategy strategy = new Strategy();
				fileName = files.get(i);
				File f = new File(fileName);
				long flength = f.length()/1024;
				if(flength<10) {
					smallFiles.add(fileName);
					continue;
				}
				if(flength>2500){
					largeFiles.add(fileName);
					continue;
				}
				//System.out.println(fileName+"////"+f.length());
				String planName = Edgar11K.getTitleOf11KPlan(fileName);
				Date date = new Date();
				if(planName==null) {
					DateFormat df = new SimpleDateFormat("MMddhhmmss");
					planName = "Unknown Plan "+fileName;
				}
				strategy.setName(planName);
				strategy.set401K(true);
				strategy.setStrategyClassID(1l);
				//strategy.setClassName("PORTFOLIO STRATEGY");
				strategy.setDescription("");
				strategy.setReference("");
				strategy.setCategories("");
				strategy.setShortDescription("");
				strategy.setSimilarIssues("");
				//strategy.setUserName("Admin");
				strategy.setUserID(Configuration.SUPER_USER_ID);
				//strategy.setUpdateTime(date);
				Long ID = strategyManager.add(strategy);
				if(ID.equals(-1l)){
					Strategy se = strategyManager.get(planName);
					List<VariableFor401k> vks = strategyManager.getVariable401KByStrategyID(se.getID());
					if(vks!=null && vks.size()>0){
						exsitFiles.add(fileName);
						continue;
					}
					else
					ID = se.getID();
				}
				getContent(fileName,ID);//create the plan's HTML file
				Parse401KParameters pp = new Parse401KParameters();
				String originalString =  Edgar11K.extract11KFund(fileName,ID.toString(),fileName);
				if(originalString!=null && !originalString.trim().equals("")){
					K401FileBean kf = new K401FileBean();
					int k = originalString.indexOf("<br>");
					int l = originalString.indexOf("<br>", k+1);
					originalString = originalString.substring(l,originalString.length()).replaceAll("<br>","\r\n");
					if(originalString.trim().equals("")){
						nullContentFiles.add(fileName);
						continue;
					}
					pp.setOriginalString(originalString);
					pp.execute();
					List<VariableFor401k> variables = pp.getVariables();
					for(int j=0;j<variables.size();j++){
						VariableFor401k vk = variables.get(j);
						String assetClassName = vk.getAssetClassName();
						if(assetClassName!=null){
							if(assetClassName.indexOf("|")<=0)
								vk.setAssetClassName(assetClassName);
							else{
								String[] acName = assetClassName.split("\\|");
								vk.setAssetClassName(acName[0]);
							}
						}else{
							vk.setAssetClassName("");
						}
						String symbol = vk.getSymbol();
						if(symbol!=null){
							if(symbol.indexOf("|")<=0)
								vk.setSymbol(symbol);
							else{
								String[] sym = symbol.split("\\|");
								vk.setSymbol(sym[0]);
							}
						}else{
							vk.setSymbol("");
						}
						String name = vk.getName();
						if(name!=null){
							if(name.indexOf("|")<=0)
								vk.setName(name);
							else{
								String[] nas = name.split("\\|");
								vk.setName(nas[0]);
							}
						}else{
							vk.setName("");
						}
					}
					//System.out.println(i+":ddd");
					strategyManager.updateVariableFor401k(variables,ID);
					kf.setFilePath(fileName);
					kf.setID(ID);
					kf.setPlanName(planName);
					sucFiles.add(kf);
				}
				FundStateControler.current_count=i+1;
			}
			catch(Exception e){
				failFiles.add(fileName);
			}
		}
		try {
			creat401kFile(failFiles,largeFiles,smallFiles,sucFiles,exsitFiles,nullContentFiles);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String execute401kFile(){
		if(action.equals("execute")){
			try {
				FundStateJob  fs = new FundStateJob();
				fs.run();
					
			} catch (RuntimeException e) {
					return Action.ERROR;
			}
			return Action.SUCCESS;
		}
		else if(action.equals("stop")){
			try {
				FundStateJob  fs = new FundStateJob();
				fs.cancel();
				
			} catch (RuntimeException e) {
				return Action.ERROR;
			}
			return Action.SUCCESS;
		}
		else return Action.ERROR;
	}
	
	/**
	 * Get the plan's ticker from finance.yahoo.com
	 * @param planName
	 * @return
	 */
	public List<String> getTickerByPlan(String planName){
		String html="html";
		int num=0;
		planName = URLEncoder.encode(planName.trim());
		String urlString = "http://finance.yahoo.com/q?s="+planName;
		try{
			while(html.equals("html")&&num<=3){
				
				try {
					StringBuffer sb = new StringBuffer();
					URL url = new URL(urlString);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					InputStreamReader isr = new InputStreamReader(conn.getInputStream());
					BufferedReader br = new BufferedReader(isr);
					String temp;
					while ((temp = br.readLine()) != null) {
						sb.append(temp).append("\n");
					}
					br.close();
					isr.close();
					html = sb.toString();
				} catch (Exception e) {
					String meg = e.toString();
					if(meg.indexOf("400 for URL:")>0){
						planName = meg.substring(meg.lastIndexOf("=")+1, meg.length());
						urlString = "http://finance.yahoo.com/q/cq?d=v1&s="+URLEncoder.encode(planName.trim());
					}
				}
				num++;
					if(html==null) html="html";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		html=com.lti.util.html.ParseHtmlTable.cleanHtml(html);
		html = com.lti.util.html.ParseHtmlTable.extractTableContent(html.toCharArray(),0, html.length()-1);
		//html content
		//IBM#JUL 30#128.40#0.00#0.00%#2,419###
		//AAPL#JUL 30#257.25#0.00#0.00%#185,117###
		List<String> tickers = new ArrayList<String>();
		String[] symStrings = html.split("###\n");
		for(int i=0;i<symStrings.length;i++){
			if(symStrings[i].indexOf("#")>0)
				tickers.add(symStrings[i].split("#")[0]);
			else
				tickers.add("--");
		}
		return tickers;
	}
	
	/**
	 * Get the tickers which are in the ids
	 * @param ids
	 * @return
	 */
	public Map<String,List<String>> getTickers(List<Long> ids){
		if(ids.size()==1 && ids.get(0).equals(0l))
			return null;
		strategyManager = ContextHolder.getStrategyManager();
		Map<String,List<String>> planAndTickers = new HashMap<String,List<String>>();
		TickerSearchControler.state = "Execute Ticker Search";
		TickerSearchControler.current_count = 0;
		TickerSearchControler.total_count = ids.size();
		for(int i=0;i<ids.size();i++){
			Strategy se = strategyManager.get(ids.get(i));
			String planName = se.getName();
			List<String> tickers = this.getTickerByPlan(planName);
			planAndTickers.put(planName, tickers);
			TickerSearchControler.current_count = i+1;
		}
		return planAndTickers;
	}
	
	/**
	 * Use these tickers to update the plans' descriptions. 
	 * @param planAndTickers
	 * @throws Exception
	 */
	/*
	public void completeDesc(Map<String,List<String>> planAndTickers) throws Exception{
		securityManager = ContextHolder.getSecurityManager();
		Set<String> ptSet = planAndTickers.keySet();
		Iterator pts = ptSet.iterator();
		TickerSearchControler.state = "Update the plan's description by the tickers";
		TickerSearchControler.current_count = 0;
		TickerSearchControler.total_count = ptSet.size();
		while(pts.hasNext()){
			String planName = (String)pts.next();
			List<String> tickers = planAndTickers.get(planName);
			Strategy se = strategyManager.get(planName);
			StringBuffer descString = new StringBuffer();
			for(int i=0;i<tickers.size();i++){
				if(tickers.get(i).equals("--")) continue;
				Security st = securityManager.get(tickers.get(i));
				String secName = "";
				if(st==null){
					try{
						secName = PersonalPortfolioUtil.getSecurityInformation(tickers.get(i));
					}catch(Exception e){
						secName = "Unknown Name";
					}
				}else{
					secName = st.getName();
				}
				descString.append(secName+" (Ticker:"+tickers.get(i)+") has the \""+planName+"\".<br>");
			}
			if(se.getDescription()!=null && !se.getDescription().trim().equals("") && !se.getDescription().trim().equals("--"))
				se.setDescription(descString.toString()+se.getDescription());
			else 
				se.setDescription(descString.toString());
			strategyManager.update(se);
			TickerSearchControler.current_count++;
		}
	}
	*/
	public static String getSecurityInformation(String symbol){
	       Security se = new Security();
			String name=null;
			try{
				String html="html";
				int num=0;
				int beginIndex, endIndex;
				while(html.equals("html")&&num<=30){
					html=ParseHtmlTable.getHtml("http://quote.morningstar.com/switch.html?ticker="+symbol);
					++num;
					if(html==null)html="html";
				}
				//TICKERNOTFOUND表示没有找到该FUND以及类似的
				//TICKERFOUND表示没有找到该FUND但找到类似的
				if(html.contains("TICKERFOUND") || html.contains("TICKERNOTFOUND"))
					return "Unknow Name";
				/******************** security type *********************/
				beginIndex = html.indexOf("http://quote.morningstar.com/")+29;
				endIndex = html.indexOf('/', beginIndex);
				String securityType = html.substring(beginIndex, endIndex);
				if(securityType.equalsIgnoreCase("CEF"))
					se.setSecurityType(Configuration.SECURITY_TYPE_CLOSED_END_FUND);
				else if(securityType.equalsIgnoreCase("ETF"))
					se.setSecurityType(Configuration.SECURITY_TYPE_ETF);
				else if(securityType.equalsIgnoreCase("FUND"))
					se.setSecurityType(Configuration.SECURITY_TYPE_MUTUAL_FUND);
				else if(securityType.equalsIgnoreCase("STOCK"))
		
				/******************** security name *********************/
				if(se.getSecurityType() != null){
					if(se.getSecurityType()==5){
						beginIndex = html.indexOf("Name")+5;
						beginIndex = html.indexOf("Name", beginIndex)+5;
						beginIndex = html.indexOf("Name", beginIndex)+5;
						beginIndex = html.indexOf("Name", beginIndex)+6;
						endIndex = html.indexOf('\"', beginIndex);
						name = html.substring(beginIndex, endIndex);
					}else{
						beginIndex = html.indexOf("description");
						if(beginIndex != -1){
							beginIndex += 22;
							endIndex = html.indexOf('(', beginIndex);
							name = html.substring(beginIndex, endIndex);
						}else
							name = "";
					}
					se.setName(name);
				}
			}
			catch(Exception e){
				e.getStackTrace();
			}
			return name;
		}
	/**
	 * Create the tickers CSV file after the operation.
	 * @param planAndTickers
	 * @throws Exception
	 */
	public void creatTickerFile(Map<String,List<String>> planAndTickers) throws Exception{
		strategyManager = ContextHolder.getStrategyManager();
		securityManager = ContextHolder.getSecurityManager();
		String systemPath;
		String sysPath = System.getenv("windir");
		Date today = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		if(!Configuration.isLinux())systemPath=sysPath+"\\401kfile\\";
		else systemPath="/home/sshadmin/validfi/11k/401k/";
		String path = systemPath+"TickerFiles.csv";	
		File reportFile=new File(path);
		reportFile.getParentFile().mkdirs();
		FileOutputStream fos = new FileOutputStream(reportFile);
		OutputStreamWriter osw = new OutputStreamWriter(fos);
		osw.write("401K Plan Ticker List. File Create Time : "+sdf.format(today)+"\t\n\t\n");
		osw.write("401K Plan Name,Web Link,Tickers,Company Name,Tickers,Company Name\r\n");
		Set<String> ptSet = planAndTickers.keySet();
		Iterator pts = ptSet.iterator();
		TickerSearchControler.state = "Write the Ticker Search Report";
		TickerSearchControler.current_count = 0;
		TickerSearchControler.total_count = ptSet.size();
		while(pts.hasNext()){
			String planName = (String)pts.next();
			List<String> tickers = planAndTickers.get(planName);
			Strategy se = strategyManager.get(planName);
			StringBuffer symString = new StringBuffer();
			symString.append(planName.replaceAll(",", "")+","+"http://www.myplaniq.com/LTISystem/f401k_view.action?ID="+se.getID()+",");
			//System.out.println(planName+":");
			for(int i=0;i<tickers.size();i++){
				String secName = "";
				Security st = securityManager.get(tickers.get(i));
				if(st==null){
					try{
						secName = getSecurityInformation(tickers.get(i));
					}catch(Exception e){
						secName = "Unknown Name";
					}
				}else{
					secName = st.getName();
				}
				System.out.println(secName);
				if(i!=tickers.size()-1){
					symString.append(tickers.get(i)+",");
					symString.append(secName+",");
				}
				else{
					symString.append(tickers.get(i)+",");
					symString.append(secName);
				}
				//System.out.println(tickers.get(i));
			}
			osw.write(symString.toString()+"\r\n");
			TickerSearchControler.current_count++;
		}
		osw.close();
		fos.close();
	}
	
	public void tickerTool(List<Long> ids){
		Map<String,List<String>> planAndTickers = getTickers(ids);
		if(planAndTickers==null){
			
		}else{
			try {
				//completeDesc(planAndTickers);
				creatTickerFile(planAndTickers);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	private boolean isTSUpdating = false;
	public String executeTickerSearch(){
		if(isTSUpdating == true){
			System.out.println("Excution is already running!");
			return Action.SUCCESS;
		}
		List<Long> ids = new ArrayList<Long>();
		if(idString!=null && !idString.trim().equals("")){
			String[] idStr = idString.split(",");
			List<Long> list = new ArrayList<Long>();
			try{
				for(int i=0;i<idStr.length;i++){
					list.add(Long.parseLong(idStr[i]));
				}
				ids = list;
			}catch(Exception e){
				ids.add(0l);
			}
		}
		else
			ids.add(0l);
		//TickerSearchControler.planIDs = ids;
		try {
//			TickerSearchJob ft = new TickerSearchJob();
//			ft.run();
//			synchronized (this) {
//				wait();
//			}
			isTSUpdating=true;
			Date startDate = new Date();
			System.out.println("Start excuting ticker data at["+startDate+"]");
			//SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
			try {				
				tickerTool(ids);
				isTSUpdating=false;	
			} catch (RuntimeException e) {
				e.printStackTrace();
			}finally{
				isTSUpdating=false;
			}
			Date endDate=new Date();			
			System.out.println("Complete excution at["+endDate+"]");
		} catch (RuntimeException e) {
			message = "Failed";
				return Action.SUCCESS;
		}
		message = "Successful";
		return Action.SUCCESS;
	}
	
	public String LoadTickerFile() throws Exception{
		String systemPath;
		Date today = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		String sysPath = System.getenv("windir");
		if(!Configuration.isLinux())systemPath=sysPath+"\\401kfile\\";
		else systemPath="/home/sshadmin/validfi/11k/401k/";
		String path = systemPath+"TickerFiles.csv";	
		File f = new File(path);
		if(f.exists()){
			fileName = sdf.format(today)+"_TickerFiles.csv";
			inputStream = new FileInputStream(f);
		}
		else{
			fileName = "No401kFileLists.csv";
			String path2 = systemPath+"NoTickerFiles.csv.csv";
			File f2 = new File(path2.toString());
			FileOutputStream fos = new FileOutputStream(path2,true);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			osw.write("The date of download log:"+sdf.format(today)+"\t's Ticker Search has not started operating today!There is no log!\t\n");	
			osw.close();
			fos.close();
			inputStream=new FileInputStream(f2);
		}
		return Action.DOWNLOAD;
	}

	public static void main(String agrs[]){
		FundTableAction ft = new FundTableAction();
		/*ft.complete401kFile();
		Long[] ids = {1298l,1299l,1300l,1301l,1302l,1303l,1304l,1305l,1306l,1307l,1308l,1309l,1310l,1311l,1312l,1313l,1314l,1315l,1316l,1317l,1318l,1320l,1321l,1322l,1323l,1325l,1326l,1327l,1328l,1329l,1331l,1332l,1333l,1334l,1336l,1337l,1338l,1339l,1340l,1341l,1342l,1343l,1344l,1345l,1346l,1347l,1348l,1349l,1350l,1351l,1356l,1357l,1358l,1359l,1360l,1361l,1362l,1363l,1364l,1365l,1366l,1367l};
		List<Long> idsList = new ArrayList<Long>();
		for(long id: ids){
			idsList.add(id);
		}
		Map<String,List<String>> planAndTickers = ft.getTickers(idsList);
		try {
			ft.creatTickerFile(planAndTickers);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Set<String> ptSet = planAndTickers.keySet();
		Iterator pts = ptSet.iterator();
		while(pts.hasNext()){
			String planName = (String)pts.next();
			List<String> tickers = planAndTickers.get(planName);
			System.out.println(planName+":");
			for(int i=0;i<tickers.size();i++){
				System.out.println(tickers.get(i));
			}
		}
		List<String> files = getRexPath();
		System.out.println(files.size());
		for(int i=0;i<files.size();i++)
		System.out.println(files.get(i));*/
	}
}
