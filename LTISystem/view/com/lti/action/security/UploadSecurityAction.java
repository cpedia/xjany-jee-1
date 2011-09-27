/**
 * 
 */
package com.lti.action.security;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.supercsv.io.CsvListReader;
import org.supercsv.io.CsvListWriter;
import org.supercsv.prefs.CsvPreference;

import com.lti.action.Action;
import com.lti.service.SecurityManager;
import com.lti.type.TimePara;
import com.lti.type.TimeUnit;
import com.lti.util.BaseFormulaUtil;
import com.lti.util.LTIDate;
import com.lti.util.LTIDownLoader;

/**
 * @author YZW
 * 
 */
public class UploadSecurityAction {
	private File uploadFile;

	private String message;
	private String uploadFileFileName;
	private SecurityManager securityManager;
	private InputStream fis;
	private String planName;

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public InputStream getFis() {
		return fis;
	}

	public void setFis(InputStream fis) {
		this.fis = fis;
	}

	public void setSecurityManager(SecurityManager securityManager) {
		this.securityManager = securityManager;
	}

	public File getUploadFile() {
		return uploadFile;
	}

	public void setUploadFile(File uploadFile) {
		this.uploadFile = uploadFile;
	}

	public String getUploadFileFileName() {
		return uploadFileFileName;
	}

	public void setUploadFileFileName(String uploadFileFileName) {
		this.uploadFileFileName = uploadFileFileName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String execute() throws IOException {
		String systemPath;
		String sysPath = System.getenv("windir");
		String str = System.getProperty("os.name").toUpperCase();
		if (str.indexOf("WINDOWS") == -1)
			systemPath = "/var/tmp/";
		else
			systemPath = sysPath + "\\temp\\";
		if (uploadFile != null) {
			if (uploadFileFileName.equals("File Of Security.csv")) {
				InputStream stream = new FileInputStream(uploadFile);
				OutputStream bos = new FileOutputStream(systemPath
						+ uploadFileFileName);
				int bytesRead = 0;
				byte[] buffer = new byte[8192];
				while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
					bos.write(buffer, 0, bytesRead);
				}
				bos.close();
				stream.close();
				message = "uploadFile success";
			} else {
				message = "the file name must be File Of Security.csv";
			}
		}
		return Action.MESSAGE;
	}

	public String downloadFile() throws Exception{
		String systemPath;
		String sysPath = System.getenv("windir");
		String str = System.getProperty("os.name").toUpperCase();
		if (str.indexOf("WINDOWS") == -1)
			systemPath = "/var/tmp/";
		else
			systemPath = sysPath + "\\temp\\";
		planName = "File Of Security.csv";
		String path = systemPath + "File Of Security.csv";
		File file = new File(path);
		fis = new FileInputStream(file);
		return Action.DOWNLOAD;
	}
	
	public String checkSecurity() throws IOException {
		CsvListWriter clw = null;
		String systemPath;
		String sysPath = System.getenv("windir");
		String str = System.getProperty("os.name").toUpperCase();
		if (str.indexOf("WINDOWS") == -1)
			systemPath = "/var/tmp/";
		else
			systemPath = sysPath + "\\temp\\";
		String path = systemPath + "File Of Security.csv";
		String dateStr = LTIDate.parseDateToString(new Date());
		String resultPath = systemPath + "Security Results" + ".csv";
		File fileWrite = new File(resultPath);
		File fileRead = new File(path);
		clw = new CsvListWriter(new FileWriter(fileWrite),
				CsvPreference.EXCEL_PREFERENCE);
		CsvListReader readerCsv = new CsvListReader(new FileReader(fileRead),
				CsvPreference.EXCEL_PREFERENCE);
		List<String> listStr;
		List<String> year = new ArrayList<String>();
		int totalFailNum = 0;
		while ((listStr = readerCsv.read()) != null) {
			List<String> line = new ArrayList<String>();
			int failNum = 0;
			for (int i = 0; i < listStr.size(); i++) {
				line.add(listStr.get(i));
				if (listStr.get(i).equalsIgnoreCase("YEAR")) {
					for (int m = i + 1; m <= listStr.size() - i; m++) {
						year.add(listStr.get(m));
						line.add(listStr.get(m));
					}
					line.add("checkFailNum");
					break;
				}
				if (listStr.get(i).equalsIgnoreCase("AR")) {
					int k = 0;
					for (int n = i + 1; n <= listStr.size() - i; n++) {
						double ar = securityManager.getSecurityMPT(
								securityManager.get(listStr.get(0)).getID(),
								Long.parseLong(year.get(k))).getAR();
						k++;
						double differAR = ar
								- Double.parseDouble(listStr.get(n));
						if (Math.abs(differAR) < Math.abs(ar * 0.05)) {
							line.add("true");
						} else {
							line.add("fale");
							failNum++;
						}
					}
					line.add(Integer.toString(failNum));
					break;
				}
			}
			clw.write(line);
			if (failNum > 0) {
				totalFailNum++;
			}
		}
		List<String> line = new ArrayList<String>();
		line.add("Total number of checking failures is " + totalFailNum);
		clw.write(line);
		clw.close();

		planName = "Security Results" + "_" + dateStr + ".csv";
		File f = new File(resultPath);
		fis = new FileInputStream(f);

		return Action.DOWNLOAD;
	}

	public String importAndSecurity() throws IOException, NumberFormatException, ParseException {
		CsvListWriter clw = null;
		CsvListWriter clwResult = null;
		String systemPath;
		LTIDownLoader ltiDownLoader = new LTIDownLoader();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat dfByY = new SimpleDateFormat("yyyy");
		BaseFormulaUtil bfUtil = new BaseFormulaUtil();
		Date date = new Date();
		
		String sysPath = System.getenv("windir");
		String str = System.getProperty("os.name").toUpperCase();
		if (str.indexOf("WINDOWS") == -1)
			systemPath = "/var/tmp/";
		else
			systemPath = sysPath + "\\temp\\";
		String path = systemPath + "File Of Security.csv";
		String writePath = systemPath +"Daily Of Security.csv";
		String resultPath = systemPath + "Security Results" + ".csv";
		File fileResult = new File(resultPath);
		File fileRead = new File(path);
		File fileWrite = new File(writePath);
		CsvListReader readerCsv = new CsvListReader(new FileReader(fileRead),
				CsvPreference.EXCEL_PREFERENCE);
		clw = new CsvListWriter(new FileWriter(fileWrite),
				CsvPreference.EXCEL_PREFERENCE);
		clwResult = new CsvListWriter(new FileWriter(fileResult),
				CsvPreference.EXCEL_PREFERENCE); 
		List<String> listStr;
		List<String> strResult = new ArrayList<String>();
		strResult.add("");
		strResult.add("YEAR");
		strResult.add("2011");
		strResult.add("2010");
		strResult.add("2009");
		strResult.add("2008");
		strResult.add("2007");
		strResult.add("2006");
		strResult.add("2005");
		strResult.add("2004");
		strResult.add("2003");
		strResult.add("2002");
		strResult.add("2001");
		strResult.add("-1");
		strResult.add("-3");
		strResult.add("-5");
		strResult.add("FailNum");
		clwResult.write(strResult);
		long checkFail = 0;
		while ((listStr = readerCsv.read()) != null) {
			long checkFailNum = 0;
			String symbol = listStr.get(0);
			Date securityStartDate = securityManager.getStartDate(symbol);
			String strUrl = ltiDownLoader.getUrl(symbol, securityManager.getStartDate(symbol), securityManager.getEndDate(symbol), "d");
			URL url = new URL(strUrl);
			URLConnection urlConnection = (URLConnection) url.openConnection();
			urlConnection.setAllowUserInteraction(false);
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			String line;
			List<String> listLine = new ArrayList<String>();
			//将该security下载的数据放入listLine缓存中
			while ((line = in.readLine()) != null) {
				listLine.add(line);
			}
			
			//取得要计算时间的adjustClose
			HashMap<String,Double> adjustMap = new HashMap<String, Double>();
			String firstDate = df.format(date);
			Date oneYear = new Date();
			Date threeYear = new Date();
			Date fiveYear = new Date();
			for(int i = 0;i<listLine.size();i++){
				String lineStr = listLine.get(i);
				System.out.println(lineStr);
				//获得当天的adjustClose
				if(i==1){
					String[] arrayStr = lineStr.split(",");
					firstDate = arrayStr[0];
					adjustMap.put(firstDate, Double.parseDouble(arrayStr[6]));
					oneYear = LTIDate.getNewTradingDateBackward(df.parse(firstDate), TimeUnit.DAILY, TimePara.workingday);
					threeYear = LTIDate.getNewTradingDateBackward(df.parse(firstDate), TimeUnit.DAILY, TimePara.workingday*3);
					fiveYear = LTIDate.getNewTradingDateBackward(df.parse(firstDate), TimeUnit.DAILY, TimePara.workingday*5);
				}
				//获得当年的第一天交易日期的adjustClose
				if(!LTIDate.before(LTIDate.getFirstNYSETradingDayOfYear(df.parse(firstDate)), securityStartDate)){
					if(lineStr.contains(df.format(LTIDate.getFirstNYSETradingDayOfYear(df.parse(firstDate))))){
						String[] arrayStr = lineStr.split(",");
						adjustMap.put(df.format(LTIDate.getFirstNYSETradingDayOfYear(df.parse(firstDate))), Double.parseDouble(arrayStr[6]));
					}
				}
				//获得一年交易日期的adjustClose
				if(!LTIDate.before(oneYear, securityStartDate)){
					if(lineStr.contains(df.format(oneYear))){
						String[] arrayStr = lineStr.split(",");
						adjustMap.put(df.format(oneYear), Double.parseDouble(arrayStr[6]));
					}
				}
				//获得三年交易日期的adjustClose
				if(!LTIDate.before(threeYear, securityStartDate)){
					if(lineStr.contains(df.format(threeYear))){
						String[] arrayStr = lineStr.split(",");
						adjustMap.put(df.format(threeYear), Double.parseDouble(arrayStr[6]));
					}
				}
				//获得五年交易日期的adjustClose
				if(!LTIDate.before(fiveYear, securityStartDate)){
					if(lineStr.contains(df.format(fiveYear))){
						String[] arrayStr = lineStr.split(",");
						adjustMap.put(df.format(fiveYear), Double.parseDouble(arrayStr[6]));
					}
				}
				//获得security开始日期的adjustClose
				if(lineStr.contains(df.format(securityStartDate))){
					String[] arrayStr = lineStr.split(",");
					adjustMap.put(df.format(securityStartDate), Double.parseDouble(arrayStr[6]));
				}
				//取得十年中每一年第一个交易日和最后一个交易日的adjustClose，放入hashmap中
				Date startDate = new Date();
				for(int j = 0;j<=10;j++){
					startDate = LTIDate.getLastYear(startDate);
					if(lineStr.contains(df.format(LTIDate.getFirstNYSETradingDayOfYear(startDate)))){
						String[] arrayStr = lineStr.split(",");
						adjustMap.put(df.format(LTIDate.getFirstNYSETradingDayOfYear(startDate)), Double.parseDouble(arrayStr[6]));
						break;
					}
					if(lineStr.contains(df.format(LTIDate.getLastNYSETradingDayOfYear(startDate)))){
						String[] arrayStr = lineStr.split(",");
						adjustMap.put(df.format(LTIDate.getLastNYSETradingDayOfYear(startDate)), Double.parseDouble(arrayStr[6]));
						break;
					}
				}
			}
			HashMap<Integer,Double> arMap = new HashMap<Integer, Double>();
			List<String> strLine1 = new ArrayList<String>();
			List<String> strLine2 = new ArrayList<String>();
//			List<String> strResult1 = new ArrayList<String>();
			List<String> strResult2 = new ArrayList<String>();
			
			strLine1.add(symbol);
			strLine1.add("YEAR");
			strLine2.add(symbol);
			strLine2.add("AR");
//			strResult1.add(symbol);
//			strResult1.add("YEAR");
			strResult2.add(symbol);
			strResult2.add("AR");
			//计算目前这一年的AR，置入arMap中
			Date startDate = df.parse(firstDate);
			strLine1.add(Integer.toString(LTIDate.getYear(startDate)));
//			strResult1.add(Integer.toString(LTIDate.getYear(startDate)));
			if(adjustMap.get(df.format(startDate))!=null&&adjustMap.get(df.format(LTIDate.getLastNYSETradingDayOfYear(LTIDate.getLastYear(startDate))))!=null){
				double arNow = adjustMap.get(df.format(startDate))/adjustMap.get(df.format(LTIDate.getLastNYSETradingDayOfYear(LTIDate.getLastYear(startDate))))-1;
				arMap.put(LTIDate.getYear(startDate), arNow);
				strLine2.add(Double.toString(arNow));
				double arByOwn = 0.0;
				try{
					arByOwn = securityManager.getSecurityMPT(
							securityManager.get(symbol).getID(),
							Long.parseLong(dfByY.format(startDate))).getAR();
				}catch(Exception e){
					arByOwn = 0;
				}
				double differ = arByOwn - arNow;
				if(Math.abs(differ)<Math.abs(arByOwn*0.05)){
					strResult2.add("TRUE");
				}else{
					strResult2.add("FALSE");
					checkFailNum++;
				}
			}else if(adjustMap.get(df.format(startDate))!=null&&adjustMap.get(df.format(LTIDate.getLastNYSETradingDayOfYear(LTIDate.getLastYear(startDate))))==null&&LTIDate.before(LTIDate.getLastYear(startDate), securityStartDate)){
				double arNow = adjustMap.get(df.format(startDate))/adjustMap.get(df.format(securityStartDate))-1;
				arMap.put(LTIDate.getYear(startDate), arNow);
				strLine2.add(Double.toString(arNow));
				double arByOwn = securityManager.getSecurityMPT(
						securityManager.get(symbol).getID(),
						Long.parseLong(dfByY.format(startDate))).getAR();
				double differ = arByOwn - arNow;
				if(Math.abs(differ)<Math.abs(arByOwn*0.05)){
					strResult2.add("TRUE");
				}else{
					strResult2.add("FALSE");
					checkFailNum++;
				}
			}else{
				strLine2.add("0");
				strResult2.add("No Price");
			}
			for(int i = 0;i < 10;i++){
				//计算每一年的AR
				startDate = LTIDate.getLastYear(startDate);
				if((adjustMap.get(df.format(LTIDate.getLastNYSETradingDayOfYear(startDate)))!=null)&&(adjustMap.get(df.format(LTIDate.getLastNYSETradingDayOfYear(LTIDate.getLastYear(startDate))))!=null)){
					double ar = adjustMap.get(df.format(LTIDate.getLastNYSETradingDayOfYear(startDate)))/adjustMap.get(df.format(LTIDate.getLastNYSETradingDayOfYear(LTIDate.getLastYear(startDate))))-1;
					strLine1.add(Integer.toString(LTIDate.getYear(startDate)));
//					strResult1.add(Integer.toString(LTIDate.getYear(startDate)));
					arMap.put(LTIDate.getYear(startDate),ar);
					strLine2.add(Double.toString(ar));
					double arByOwn = securityManager.getSecurityMPT(
							securityManager.get(symbol).getID(),
							Long.parseLong(dfByY.format(startDate))).getAR();
					double differ = arByOwn - ar;
					if(Math.abs(differ)<Math.abs(arByOwn*0.05)){
						strResult2.add("TRUE");
					}else{
						strResult2.add("FALSE");
						checkFailNum++;
					}
					//如果security的开始日期比要计算的这年日期晚，则置AR为0
				}else if(LTIDate.before(startDate, securityStartDate)){
					strLine1.add(Integer.toString(LTIDate.getYear(startDate)));
					arMap.put(LTIDate.getYear(startDate), 0.0);
					strLine2.add(Double.toString(0.0));
					strResult2.add("TRUE");
					//计算开始年的AR
				}else if((adjustMap.get(df.format(LTIDate.getLastNYSETradingDayOfYear(startDate)))!=null)&&adjustMap.get(df.format(securityStartDate))!=null&&(adjustMap.get(df.format(LTIDate.getLastNYSETradingDayOfYear(LTIDate.getLastYear(startDate))))==null)){
					double ar = adjustMap.get(df.format(LTIDate.getLastNYSETradingDayOfYear(startDate)))/adjustMap.get(df.format(securityStartDate))-1;
					strLine1.add(Integer.toString(LTIDate.getYear(startDate)));
//					strResult1.add(Integer.toString(LTIDate.getYear(startDate)));
					arMap.put(LTIDate.getYear(startDate),ar);
					strLine2.add(Double.toString(ar));
					double arByOwn = securityManager.getSecurityMPT(
							securityManager.get(symbol).getID(),
							Long.parseLong(dfByY.format(startDate))).getAR();
					double differ = arByOwn - ar;
					if(Math.abs(differ)<Math.abs(arByOwn*0.05)){
						strResult2.add("TRUE");
					}else{
						strResult2.add("FALSE");
						checkFailNum++;
					}
				}else{
					strLine1.add(Integer.toString(LTIDate.getYear(startDate)));
					strLine2.add(Double.toString(0.0));
					strResult2.add("No Price");
					checkFailNum++;
				}
				
			}
			strLine1.add("-1");
			strLine1.add("-3");
			strLine1.add("-5");
//			strResult1.add("-1");
//			strResult1.add("-3");
//			strResult1.add("-5");
//			strResult1.add("FailNum");
			//计算一年、三年、五年的AR
//			double arOneY = bfUtil.computeAnnualizedReturn(adjustMap.get(firstDate), adjustMap.get(df.format(oneYear)), LTIDate.calculateInterval(oneYear, df.parse(firstDate)));
//			double arThreeY = bfUtil.computeAnnualizedReturn(adjustMap.get(firstDate), adjustMap.get(df.format(threeYear)), LTIDate.calculateInterval(threeYear, df.parse(firstDate)));
//			double arFiveY = bfUtil.computeAnnualizedReturn(adjustMap.get(firstDate), adjustMap.get(df.format(fiveYear)), LTIDate.calculateInterval(fiveYear, df.parse(firstDate)));
			//判断是否满足一年
//			System.out.println(adjustMap.get(df.format(oneYear)));
//			System.out.println(adjustMap.get(threeYear));
//			System.out.println(adjustMap.get(fiveYear));
			if((!LTIDate.before(oneYear, securityStartDate))&&(adjustMap.get(df.format(oneYear))!=null)){
				double arOneY = bfUtil.computeAnnualizedReturn(adjustMap.get(firstDate), adjustMap.get(df.format(oneYear)), 252);
				double arByOwnOne = securityManager.getSecurityMPT(
						securityManager.get(symbol).getID(),
						Long.parseLong("-1")).getAR();
				double differOne = arByOwnOne-arOneY;
				if(Math.abs(differOne)<Math.abs(arByOwnOne*0.05)){
					strResult2.add("TRUE");
				}else{
					strResult2.add("FALSE");
					checkFailNum++;
				}
				strLine2.add(Double.toString(arOneY));
				//判断是否满足三年
				if(!LTIDate.before(threeYear, securityStartDate)&&adjustMap.get(df.format(threeYear))!=null){
					double arThreeY = bfUtil.computeAnnualizedReturn(adjustMap.get(firstDate), adjustMap.get(df.format(threeYear)), 252*3);
					double arByOwnThree = securityManager.getSecurityMPT(
							securityManager.get(symbol).getID(),
							Long.parseLong("-3")).getAR();
					double differThree = arByOwnThree - arThreeY;
					if(Math.abs(differThree)<Math.abs(arByOwnThree*0.05)){
						strResult2.add("TRUE");
					}else{
						strResult2.add("FALSE");
						checkFailNum++;
					}
					strLine2.add(Double.toString(arThreeY));
					//判断是否满足五年
					if(!LTIDate.before(fiveYear, securityStartDate)&&adjustMap.get(df.format(fiveYear))!=null){
						double arFiveY = bfUtil.computeAnnualizedReturn(adjustMap.get(firstDate), adjustMap.get(df.format(fiveYear)), 252*5);
						double arByOwnFive = securityManager.getSecurityMPT(
								securityManager.get(symbol).getID(),
								Long.parseLong("-5")).getAR();
						double differFive = arByOwnFive - arFiveY;
						if(Math.abs(differFive)<Math.abs(arByOwnFive*0.05)){
							strResult2.add("TRUE");
						}else{
							strResult2.add("FALSE");
							checkFailNum++;
						}
						strLine2.add(Double.toString(arFiveY));
					}else{
						strResult2.add("No Price");
						strLine2.add(Double.toString(0.0));
					}
				}else{
					strResult2.add("No Price");
					strLine2.add(Double.toString(0.0));
					strResult2.add("No Price");
					strLine2.add(Double.toString(0.0));
				}
			}else{
				strResult2.add("No Price");
				strLine2.add(Double.toString(0.0));
				strResult2.add("No Price");
				strLine2.add(Double.toString(0.0));
				strResult2.add("No Price");
				strLine2.add(Double.toString(0.0));
			}
			strResult2.add(Long.toString(checkFailNum));
			clw.write(strLine1);
			clw.write(strLine2);
//			clwResult.write(strResult1);
			clwResult.write(strResult2);
			if(checkFailNum>0){
				checkFail++;
			}
		}
		clw.close();
		List<String> failStr = new ArrayList<String>();
		failStr.add("The Total Fail Number is "+checkFail);
		clwResult.write(failStr);
		clwResult.close();
//		try {
////			EmailUtil.sendAttachment(new String[]{"terencew@myplaniq.com","yeziboy@sina.com"}, "CheckAR_Result["+df.format(date)+"]", "", "CheckAR_Result["+df.format(date)+"].csv", fileResult);
//			EmailUtil.sendMail(new String[]{"yeziboy@sina.com"}, "test", "测试");
//			System.out.println("发送！");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		planName = "Daily Of Security" + "_" + df.format(date) + ".csv";
		planName =  "Security Results" + "_" + df.format(date) + ".csv";
//		File f = new File(writePath);
		File f = new File(resultPath);
		fis = new FileInputStream(f);
		return Action.DOWNLOAD;
	}
}
