/*
 * class:LTIDownLoader
 * author:chaos
 * date:
 */

package com.lti.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StreamTokenizer;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.supercsv.io.CsvListWriter;
import org.supercsv.prefs.CsvPreference;

import com.lti.action.admin.loaddata.ImportSecurityDataAction;
import com.lti.action.admin.loaddata.UpdateAdjustCloseAction;
import com.lti.jobscheduler.DailyUpdateControler;
import com.lti.jobscheduler.FinanStateControler;
import com.lti.jobscheduler.FinanYearlyStateControler;
import com.lti.service.AssetClassManager;
import com.lti.service.DataManager;
import com.lti.service.FinancialStatementManager;
import com.lti.service.HolidayManager;
import com.lti.service.IndicatorManager;
import com.lti.service.MutualFundManager;
import com.lti.service.PortfolioManager;
import com.lti.service.SecurityManager;
import com.lti.service.bo.AssetClass;
import com.lti.service.bo.Holiday;
import com.lti.service.bo.Indicator;
import com.lti.service.bo.PortfolioDailyData;
import com.lti.service.bo.Security;
import com.lti.service.bo.SecurityDailyData;
import com.lti.service.bo.TradingDate;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.type.Interval;
import com.lti.type.TimeUnit;
import com.lti.util.html.ParseHtmlTable;

/*
 * author: chaos
 */
public class LTIDownLoader {

	public final static ThreadLocal<Map> SESSION = new ThreadLocal<Map>();

	static Log log = LogFactory.getLog(LTIDownLoader.class);

	private static int BUFFER_SIZE = 8096;// buffer size

	private static int UPDATE_INTERNATIONAL = 1;

	private static int UPDATE_CHINA = 2;

	private static int UPDATE_ALL = 3;

	private static int NORMAL_SECURITY = 1;

	private static int CHINA_OPEN_FUND = 2;

	private static int CHINA_CLOSE_FUND = 3;

	private DataManager dataManager;

	private SecurityManager securityManager;

	private MutualFundManager mutualFundManager;

	private AssetClassManager assetClassManager;

	public String systemPath;

	private HashMap<String, Date> dateMap = new HashMap<String, Date>();

	private String logFile;
	private String eodlogFile;
	private Date logDate;

	private String fastUpdateDetailLogFile;
	private String fastUpdateSimpleLogFile;
	private int updateNum = 1;

	private FinancialStatementManager fsm;

	private IndicatorManager indicatorManager;

	public String getSystemPath() {
		String sysPath = System.getenv("windir");
		if (!isLinux())
			sysPath += "\\temp\\";
		else
			sysPath = "/var/tmp/";
		return sysPath;
	}

	public LTIDownLoader() {
		super();
		securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
		dataManager = (DataManager) ContextHolder.getInstance().getApplicationContext().getBean("dataManager");
		mutualFundManager = (MutualFundManager) ContextHolder.getInstance().getApplicationContext().getBean("mutualFundManager");
		assetClassManager = (AssetClassManager) ContextHolder.getInstance().getApplicationContext().getBean("assetClassManager");
		fsm = (FinancialStatementManager) ContextHolder.getInstance().getApplicationContext().getBean("financialStatementManager");

		systemPath = getSystemPath();

		Date date = new Date();
		Integer year = date.getYear() + 1900;
		Integer month = date.getMonth() + 1;
		Integer day = date.getDate();
		String monthS = month > 9 ? (month.toString()) : ("0" + month.toString());
		String dayS = day > 9 ? (day.toString()) : ("0" + day.toString());
		this.logFile = systemPath + year.toString() + monthS + dayS + "downloadlog.html";
		this.eodlogFile = systemPath + year.toString() + monthS + dayS + "EoDlog.html";
		this.fastUpdateDetailLogFile = systemPath + LTIDate.parseDateToString(date) + "fastdetail1.csv";
		this.fastUpdateSimpleLogFile = systemPath + LTIDate.parseDateToString(date) + "fastsimple1.csv";
		if (this.getUpdateOption() != this.UPDATE_INTERNATIONAL)
			systemPath += "cn/";
		else
			systemPath += "us/";

		File rootFile = new File(systemPath);
		if (!rootFile.isDirectory())
			System.out.println(rootFile.mkdir());

		rootFile = new File(systemPath + "cn");
		if (!rootFile.isDirectory())
			System.out.println(rootFile.mkdir());

		rootFile = new File(systemPath + "us");
		if (!rootFile.isDirectory())
			System.out.println(rootFile.mkdir());

		this.logDate = new Date();
	}

	// function: get one security's source url
	@SuppressWarnings("deprecation")
	public String getUrl(String name, Date startDate, Date endDate, String KeyWord) {
		int a, b, c, d, e, f;
		if (startDate == null) {
			a = 0;
			b = 1;
			c = 1990;
		} else {
			a = startDate.getMonth();
			b = startDate.getDate();
			c = startDate.getYear() + 1900;
		}

		d = endDate.getMonth();
		e = endDate.getDate();
		f = endDate.getYear() + 1900;

		String strUrl = "http://ichart.finance.yahoo.com/table.csv?s=" + name + "&a=" + a + "&b=" + b + "&c=" + c + "&d=" + d + "&e=" + e + "&f=" + f + "&g=" + KeyWord + "&ignore=.csv";

		return strUrl;
	}

	public String getCEFUrl(String name, Date startDate, Date endDate, String KeyWord) {
		int a, b, c, d, e, f;
		if (startDate == null) {
			a = 0;
			b = 1;
			c = 1990;
		} else {
			a = startDate.getMonth();
			b = startDate.getDate();
			c = startDate.getYear() + 1900;
		}

		d = endDate.getMonth();
		e = endDate.getDate();
		f = endDate.getYear() + 1900;

		String strUrl = "http://ichart.finance.yahoo.com/table.csv?s=x" + name + "x&a=" + a + "&b=" + b + "&c=" + c + "&d=" + d + "&e=" + e + "&f=" + f + "&g=d" + "&ignore=.csv";

		return strUrl;
	}

	// function: get the user's system style, if it's linux return true ,else
	// return false.
	public boolean isLinux() {
		String str = System.getProperty("os.name").toUpperCase();
		if (str.indexOf("WINDOWS") == -1) {
			return true;
		}
		return false;
	}

	public String getTrueString(String k) {
		if (k == "" || k == null || k.length() == 0 || k.length() == 1)
			return k;
		k = k.trim();
		if (k.length() == 0) {
			return k;
		}
		if (k.charAt(k.length() - 1) == ' ') {
			k = k.substring(0, k.length() - 1);
		}
		return k;
	}

	// function: get one security's document name to save the data
	@SuppressWarnings("deprecation")
	public String getDocName(String name, Date startDate, Date endDate, String KeyWord, boolean update) {
		// String doc="C://temp//";
		// int abc = 1;
		String doc = systemPath;

		if (update)
			doc += "update/";

		if (KeyWord.equalsIgnoreCase("v")) {
			doc += "v/";
		} else if (KeyWord.equalsIgnoreCase("d")) {
			doc += "d/";
		} else {
			doc += "nav/";
		}

		int a, b, c, d, e, f;
		if (startDate == null) {
			a = 0;
			b = 1;
			c = 1990;
		} else {
			a = startDate.getMonth();
			b = startDate.getDate();
			c = startDate.getYear() + 1900;
		}

		d = endDate.getMonth();
		e = endDate.getDate();
		f = endDate.getYear() + 1900;

		doc += name + "_" + c + "-" + a + "-" + b + "_" + f + "-" + d + "-" + e + ".csv";

		return doc;
	}

	// function: save the data source to local document
	public String saveToFile(String name, Date startDate, Date endDate, String KeyWord, boolean update) {
		try {
			name.trim();
			KeyWord.trim();
			String strUrl = getUrl(name, startDate, endDate, KeyWord);
			String strName = getDocName(name, startDate, endDate, KeyWord, update);

			URL url = new URL(strUrl);
			URLConnection urlConnection = (URLConnection) url.openConnection();
			urlConnection.setAllowUserInteraction(false);

			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			String inputLine;
			StringBuffer tempHtml = new StringBuffer();

			// System.out.println("Start!!!!");
			while ((inputLine = in.readLine()) != null) {
				tempHtml.append(inputLine + "\n");
			}
			// System.out.println("End!!!!");
			BufferedWriter buff = new BufferedWriter(new FileWriter(strName));
			for (int i = 0; i < tempHtml.length(); i++)
				buff.write(tempHtml.charAt(i));
			buff.flush();
			buff.close();

			// System.out.println(name+" "+strName);

			// System.out.println(strName+" Success.");

			Date cur = new Date();
			this.writeLog(name, cur, strName + " save to file success");
			return strName;
		} catch (Exception e) {

			log.debug("Error: " + e);
			// System.out.println("Error:"+e);

			Date cur = new Date();
			// this.writeLog(name, cur, e.toString());

			try {
				name.trim();
				KeyWord.trim();
				String decodeName = java.net.URLDecoder.decode(name, "utf-8");
				String strUrl = getUrl(decodeName, startDate, endDate, KeyWord);
				String strName = getDocName(name, startDate, endDate, KeyWord, update);

				URL url = new URL(strUrl);
				URLConnection urlConnection = (URLConnection) url.openConnection();
				urlConnection.setAllowUserInteraction(false);

				BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
				String inputLine;
				StringBuffer tempHtml = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					tempHtml.append(inputLine + "\n");
				}

				BufferedWriter buff = new BufferedWriter(new FileWriter(strName));
				for (int i = 0; i < tempHtml.length(); i++)
					buff.write(tempHtml.charAt(i));
				buff.flush();
				buff.close();

				// System.out.println(name+" "+strName);

				// System.out.println(strName+" Success.");

				this.writeLog(name, cur, strName + " save to file success");
				return strName;
			} catch (Exception e2) {
				log.debug("Error: " + e2);
				System.out.println("Error:" + e2);
				// this.writeLog(name, cur, e2.toString());
				this.writeLog(name + ":Error", cur, "save daily price to file error:" + e2.toString());
			}
		}
		return null;
	}

	public String saveCEFToFile(String name, Date startDate, Date endDate, String KeyWord, boolean update) throws IOException {
		try {
			name.trim();
			KeyWord.trim();
			String strUrl = getCEFUrl(name, startDate, endDate, KeyWord);
			String strName = getDocName(name, startDate, endDate, KeyWord, update);

			URL url = new URL(strUrl);
			URLConnection urlConnection = (URLConnection) url.openConnection();
			urlConnection.setAllowUserInteraction(false);

			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			String inputLine;
			StringBuffer tempHtml = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				tempHtml.append(inputLine + "\n");
			}

			BufferedWriter buff = new BufferedWriter(new FileWriter(strName));
			for (int i = 0; i < tempHtml.length(); i++)
				buff.write(tempHtml.charAt(i));
			buff.flush();
			buff.close();

			// System.out.println(name+" "+strName);

			// System.out.println(strName+" Success.");

			Date cur = new Date();
			this.writeLog(name, cur, strName + " save to CEFfile success");
			return strName;
		} catch (Exception e) {

			log.debug("Error: " + e);
			System.out.println("Error:" + e);

			Date cur = new Date();
			this.writeLog(name + ":Error", cur, " save to CEFfile error" + e.toString());
		}
		return null;
	}

	// function: get all security type from a document (return all securitys'
	// symbol)
	public String[] getSecurityType() {
		try {
			String file = systemPath + "securityType.txt";
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String[] type;
			String line = "";
			line = br.readLine();
			String tmpLine;
			while (line != null) {
				tmpLine = br.readLine();
				if (tmpLine == null)
					break;
				line = line + "," + tmpLine;
			}
			type = line.split(",");
			return type;
		} catch (Exception e) {

			log.debug("Error: " + e);
			System.out.println("Error: " + e);

		}
		return null;
	}

	// function: get the security symbol from a complete file name
	public String checkFile(String fileName) {
		try {
			String[] temps;
			temps = fileName.split("_");
			return temps[0];
		} catch (Exception e) {
			System.out.println("Error: " + e);
		}
		return null;

	}

	public Date upLoadOneFile(String type, String symbol, String fileName, boolean flush, boolean update) throws Exception {
		try {
			File file = new File(fileName);
			if (file != null && file.isFile()) {
				if (type.equals("d"))
					loadToDB(symbol, fileName, flush);
				else if (type.equals("v"))
					return loadToDB2(symbol, fileName, flush);
				// System.out.println("Done one.");
				log.info("success: " + symbol);
				Date cur = new Date();
				if (type.equals("v"))
					this.writeLog(symbol, cur, "upload dividend to database success");
				if (type.equals("d"))
					this.writeLog(symbol, cur, "upload dailydata to database success");
			}
		} catch (Exception e) {
			log.debug("Error: " + e);
			throw e;
		}
		return null;
	}

	// function: save the data from document to data base
	public HashMap<String, Date> upLoadFile(String type, boolean flush, boolean update) throws Exception {
		/**** part 2:upload dividend ***/
		HashMap<String, Date> lastDateList = new HashMap<String, Date>();
		int part2_success_count = 0;
		int part2_fail_count = 0;

		String path = systemPath;
		if (update)
			path = systemPath + "update/";
		if (isLinux())
			path += type + "/";
		else
			path += type + "//";

		String currentFile, securityName = "";

		File rootFile = new File(path);
		File[] files = rootFile.listFiles(); // 获取rootFile目录里的全部文件和文件夹
		System.out.println(files.length);
		int i, sum;
		sum = files.length;
		DailyUpdateControler.total_count = sum;
		for (i = 0; i < sum; i++) {
			try {
				DailyUpdateControler.current_count = i + 1;
				if (files[i].isFile()) {
					currentFile = files[i].toString();
					securityName = checkFile(files[i].getName());

					// System.out.println(securityName);

					if (type.equals("d")) {
						loadToDB(securityName, currentFile, flush);
					} else if (type.equals("v")) {
						lastDateList.put(securityName, loadToDB2(securityName, currentFile, flush));// 1.下载对应temp目录的dividend文件，2.把对应的security名字和对应最小的dividendlastdate日期放到一个map里
					}
					// System.out.println("Done one.");
					log.info("success:" + securityName);

					Date cur = new Date();
					if (type.equals("v"))
						this.writeLog(securityName, cur, "upload dividend to database success");
					if (type.equals("d"))
						this.writeLog(securityName, cur, "upload dailydata to database success");
				}
				part2_success_count++;
			} catch (Exception e) {
				Date date = new Date();
				if (type.equals("v")) {
					this.writeLog(securityName + ":Error", date, "upload dividend to database error");
				}
				if (type.equals("d")) {
					this.writeLog(securityName + ":Error", date, "upload dailydata to database error");
				}
				part2_fail_count++;
			}
		}
		// System.out.println("UpLoad Done");
		log.info("All success.");
		Date cur = new Date();
		// this.writeLog("ALL Security", cur,
		// " All upload to database success");
		this.writeLog("ALL Security UpDate part 2 End", cur, "\n************************************\n Success count:" + part2_success_count + ";Error count:" + part2_fail_count + " \n************************************\n");
		part2EndTime = System.currentTimeMillis();
		return lastDateList;
	}

	public void upLoadFileAndAdjust(boolean flush, boolean update, HashMap<String, Date> map, boolean fromStart) {
		/**** part 4:upload daily price ***/

		String path = systemPath;
		if (update)
			path = systemPath + "update/";
		if (isLinux())
			path += "d/";
		else
			path += "d//";

		String currentFile, securityName = "";

		File rootFile = new File(path.toString());
		File[] files = rootFile.listFiles();

		int i, sum;
		sum = files.length;

		DailyUpdateControler.total_count = sum;
		int part4_success_count = 0;
		int part4_fail_count = 0;
		for (i = 0; i < sum; i++) {
			try {
				DailyUpdateControler.current_count = i + 1;
				if (files[i].isFile()) {
					currentFile = files[i].toString();
					securityName = checkFile(files[i].getName());

					// System.out.println(securityName);

					Date lastDate = map.get(securityName);

					loadToDBAndAdjust(securityName, currentFile, flush, lastDate, fromStart);

					// System.out.println("Done one.");
					log.info("success:" + securityName);

					Date cur = new Date();

					this.writeLog(securityName, cur, "upload dailydata and adjust to database success");
					part4_success_count++;
				}
			} catch (Exception e) {
				log.debug("Error: " + e);
				this.writeLog(securityName + ":Error", new Date(), "upload dailydata and adjust to database error");
				part4_fail_count++;
			}
		}
		// System.out.println("UpLoad Done");
		log.info("All success.");
		Date cur = new Date();
		// this.writeLog("ALL Security", cur,
		// " All upload to database success");
		part4EndTime = System.currentTimeMillis();
		this.writeLog("ALL Security UpDate part 4 End", cur, "\n************************************\n Success count:" + part4_success_count + ";Error count:" + part4_fail_count + " \n************************************\n");
	}

	// function : save to database for daily data
	public void loadToDB(String securityName, String fileName, boolean flush) throws Exception {
		try {
			dataManager.UpdateDailyData(securityName, fileName, flush, this.logDate);
		} catch (Exception e) {

			log.debug("Error: " + e);
			e.printStackTrace();

			throw e;
		}
	}

	public void loadToDBAndAdjust(String securityName, String fileName, boolean flush, Date lastDate, boolean fromStart) throws Exception {
		try {
			dataManager.UpdateDailyDataAndAdjust(securityName, fileName, flush, lastDate, fromStart, this.logDate);
		} catch (Exception e) {

			log.debug("Error: " + e);
			e.printStackTrace();

			throw e;
		}
	}

	// function : save to database for dividends
	public Date loadToDB2(String securityName, String fileName, boolean flush) throws Exception {
		boolean success = false;
		try {
			success = true;
			return dataManager.UpdateDividends(fileName, securityName, flush);
		} catch (Exception e) {
			success = false;
			log.debug("Error: " + e);
			e.printStackTrace();
			throw e;
		} finally {
			if (success == false)
				return null;
		}
	}

	/*
	 * parse security attribute only for csv document
	 */
	public String[] split(String s) throws Exception {
		// System.out.println(s);
		String old = new String(s);
		List<String> attList = new ArrayList<String>();
		boolean start = false;
		int quoteStart = 0;
		int commaStart = 0;
		for (int i = 0; i < old.length(); i++) {
			char tmpC = old.charAt(i);
			if (i == old.length() - 1) {
				if (start && tmpC != '"') {
					Exception e = new Exception("line: " + s + "; No more quote to match the first one!");
					throw e;
				} else {
					String tt;
					if (tmpC == ',')
						tt = old.substring(commaStart, i);
					else
						tt = old.substring(commaStart, i + 1);
					attList.add(tt);
				}
			} else if (tmpC == ',') {
				if (start) {
					continue;
				} else {
					if (i != quoteStart) {
						String tt = old.substring(commaStart, i);
						attList.add(tt);
					}
					commaStart = i + 1;
				}
			} else if (tmpC == '"') {
				if (start) {
					String tt = old.substring(quoteStart, i);
					attList.add(tt);
					quoteStart = i + 1;
					start = false;
				} else {
					start = true;
					quoteStart = i + 1;
				}
			}
		}

		String[] result = new String[attList.size()];
		for (int i = 0; i < attList.size(); i++) {
			result[i] = attList.get(i);
		}
		return result;
	}

	// function: batch load all the security(include all the attribute)from
	// local document to the data base
	public void batchLoadAttribute(String file, boolean flash, boolean overWrite) throws UpLoadException, Exception {

		List<Date> endDateList = new ArrayList<Date>();

		List<SecurityDailyData> firstDataList = new ArrayList<SecurityDailyData>();

		List<Date> navEndDateList = new ArrayList<Date>();

		List<String> openFundList = new ArrayList<String>();
		List<String> closeFundList = new ArrayList<String>();

		try {
			// delete the normal files
			this.deleteFile(false);

			AssetClassManager assetClassManager;
			assetClassManager = (AssetClassManager) ContextHolder.getInstance().getApplicationContext().getBean("assetClassManager");

			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);

			boolean isNewSecurity;
			boolean isFirstOne = true;
			String[] lastAttribute = null;
			List<Date> startDate = new ArrayList<Date>();
			List<Date> endDate = new ArrayList<Date>();
			List<String> symbols = new ArrayList<String>();
			List<String> CEF = new ArrayList<String>();
			List<Date> CEFstartDate = new ArrayList<Date>();
			List<Date> CEFendDate = new ArrayList<Date>();

			DateFormat sf = new SimpleDateFormat("yyyy-MM-dd");

			List<SecurityDailyData> staticDataList = new ArrayList<SecurityDailyData>();

			String line = "";
			line = br.readLine();// ignore the first line
			// if the file is not with the right format
			// this is a dead loop
			// please notice that
			boolean starFlag = false;
			while (line != null) {

				if (!line.equals("") && line.charAt(0) == '*') {
					starFlag = true;
					break;
				}
				line = br.readLine();
			}
			if (starFlag == false) {
				UpLoadException upE = new UpLoadException("Wrong format!");
				upE.setErrorMsg("Wrong format!");
				throw upE;
			}
			line = br.readLine();
			while (line != null) {
				isNewSecurity = false;
				line = line.trim();
				// String[] s = line.split(",");
				String[] s = this.split(line);

				String[] tmpS = new String[11];
				for (int i = 0; i < s.length; i++)
					tmpS[i] = s[i];
				for (int i = s.length; i < 11; i++) {
					tmpS[i] = null;
				}
				s = tmpS;

				Security security;
				if (s[DataHeader.SYMBOL] == null || s[DataHeader.SYMBOL].length() == 0) {

					UpLoadException upE = new UpLoadException();
					upE.setErrorMsg(line + " is null at symbol!");
					throw upE;
					// line = br.readLine();
					// continue;
				}
				String k = this.getTrueString(s[DataHeader.SYMBOL]);

				// 严禁使用CASH
				if (k.equalsIgnoreCase("CASH"))
					continue;

				security = securityManager.getBySymbol(k);

				// System.out.println(k);
				ImportSecurityDataAction.addMessage(k, SESSION.get());

				int isManual = 0;
				if (security == null) {
					security = new Security();
					security.setSymbol(k);
					isNewSecurity = true;
				} else {
					isManual = security.getManual();
					// System.out.println("Manual is:"+isManual);
					if (!overWrite) {
						line = br.readLine();
						if (isFirstOne)
							lastAttribute = s;
						isFirstOne = false;
						continue;

					}
				}

				if (isFirstOne) {
					if (s.length < 11) {
						UpLoadException upE = new UpLoadException();
						upE.setErrorMsg("No Enough Information for security " + k + "!");
						throw upE;
					}

					lastAttribute = s;
					if (s[DataHeader.NAME] != null)
						security.setName(this.getTrueString(s[DataHeader.NAME]));
					else {
						UpLoadException upE = new UpLoadException();
						upE.setErrorMsg("Security " + k + " has no NAME!");
						throw upE;
					}
					if (s[DataHeader.SECURITY_TYPE] != null)
						security.setSecurityType(Configuration.getSecurityType(this.getTrueString(s[DataHeader.SECURITY_TYPE])));
					else {
						UpLoadException upE = new UpLoadException();
						upE.setErrorMsg("Security " + k + " has no Security Type!");
						throw upE;
					}

					/******************* For AssetClass ************************************************************************/
					String acName = this.getTrueString(s[DataHeader.ASSET_CLASS]);
					AssetClass ac = assetClassManager.get(acName);
					long classid = 0;
					if (ac != null)
						classid = ac.getID();
					else if (acName != null && acName.length() > 0) {
						ac = new AssetClass();
						ac.setBenchmarkID(91L);
						ac.setName(acName);
						ac.setParentID(0L);
						assetClassManager.save(ac);
						classid = ac.getID();
					}

					String subACName = this.getTrueString(s[DataHeader.ASSET_TYPE]);
					if (subACName != null && subACName.length() > 0) {
						AssetClass subAC = assetClassManager.getChildClass(subACName, classid);
						if (subAC != null)
							classid = subAC.getID();
						else if (subACName != null && subACName.length() > 0) {
							subAC = new AssetClass();
							subAC.setBenchmarkID(91L);
							subAC.setName(subACName);
							subAC.setParentID(classid);
							assetClassManager.save(subAC);
							classid = subAC.getID();
						}
					}

					String subSubACName = this.getTrueString(s[DataHeader.SUBCLASS]);
					if (subSubACName != null && subSubACName.length() > 0) {
						AssetClass subSubAC = assetClassManager.getChildClass(subSubACName, classid);
						if (subSubAC != null)
							classid = subSubAC.getID();
						else if (subSubACName != null && subSubACName.length() > 0) {
							subSubAC = new AssetClass();
							subSubAC.setBenchmarkID(91L);
							subSubAC.setParentID(classid);
							subSubAC.setName(subSubACName);
							assetClassManager.save(subSubAC);
							classid = subSubAC.getID();
						}
					}

					if (security.getClassID() == null || classid != security.getClassID())
						security.setClassID(classid);

					/************************************************************************************************************/

					if (s[DataHeader.DIVERSIFIED] != null) {
						if (s[DataHeader.DIVERSIFIED].equalsIgnoreCase("0"))
							security.setDiversified(false);
						else
							security.setDiversified(true);
					}

					if (s[DataHeader.STARTDATE] == null) {
						UpLoadException upE = new UpLoadException();
						upE.setErrorMsg("Security " + k + " has no Start Date!");
						throw upE;
					}
					if (s[DataHeader.ENDDATE] == null) {
						UpLoadException upE = new UpLoadException();
						upE.setErrorMsg("Security " + k + " has no End Date!");
						throw upE;
					}

					startDate.add((Date) sf.parseObject(this.getTrueString(s[DataHeader.STARTDATE])));
					endDate.add((Date) sf.parseObject(this.getTrueString(s[DataHeader.ENDDATE])));

					if (s[DataHeader.MANUAL] != null) {
						if (s[DataHeader.MANUAL].equalsIgnoreCase("0"))
							security.setManual(0);
						else if (s[DataHeader.MANUAL].equalsIgnoreCase("1"))
							security.setManual(1);
						else if (s[DataHeader.MANUAL].equalsIgnoreCase("2"))
							security.setManual(2);
					} else {
						UpLoadException upE = new UpLoadException();
						upE.setErrorMsg("Security " + k + " has no Manual Type!");
						throw upE;
					}

					/**********************************************************************************************************/
					Date tempStartDate = (Date) sf.parseObject(this.getTrueString(s[DataHeader.STARTDATE]));
					SecurityDailyData staticData = new SecurityDailyData();
					Date date2 = LTIDate.add(tempStartDate, -1);

					int type = this.getUpdateType(k);
					if (type == this.NORMAL_SECURITY) {
						staticData = securityManager.getLatestDailydata(security.getID(), date2);
						endDateList.add(date2);
						staticDataList.add(staticData);
						symbols.add(k);
					} else if (type == this.CHINA_OPEN_FUND) {
						openFundList.add(k);
					} else if (type == this.CHINA_CLOSE_FUND) {
						closeFundList.add(k);
					}
					/**********************************************************************************************************/

					if (security.getSecurityType().equals((Integer) 2) && type == this.NORMAL_SECURITY) {
						CEF.add(security.getSymbol());
						CEFstartDate.add((Date) sf.parseObject(this.getTrueString(s[DataHeader.STARTDATE])));
						CEFendDate.add((Date) sf.parseObject(this.getTrueString(s[DataHeader.ENDDATE])));
						navEndDateList.add(date2);
					}
					/***********************************************************************************************************/
					isFirstOne = false;
				} else {
					String temps;
					long classid = 0;
					AssetClass ac = new AssetClass();

					Date tempStartDate = null, tempEndDate = null;

					if (s.length < 10) {
						UpLoadException upE = new UpLoadException();
						upE.setErrorMsg(line + " has not enouth infomation!");
						throw upE;
					}

					for (int i = 0; i <= 10; i++) {
						if (i >= s.length) {
							temps = lastAttribute[i];
						} else if (s[i] == "" || s[i] == null || s[i].length() == 0) {
							temps = lastAttribute[i];
						} else {
							temps = s[i];
							lastAttribute[i] = temps;
						}
						temps.trim();
						temps = this.getTrueString(temps);
						switch (i) {
						case 0:// DataHeader.Name:
							if (temps != null)
								security.setName(temps);
							else {
								UpLoadException upE = new UpLoadException();
								upE.setErrorMsg("Security " + k + " has no Security Name!");
								throw upE;
							}
							break;
						case 2:// DataHeader.SECURITY_TYPE:
							if (temps != null)
								security.setSecurityType(Configuration.getSecurityType(temps));
							else {
								UpLoadException upE = new UpLoadException();
								upE.setErrorMsg("Security " + k + " has no Security Type!");
								throw upE;
							}
							break;
						case 3:// DataHeader.ASSET_CLASS:
							/*
							 * ac = assetClassManager.get(temps); classid =
							 * (ac==null)?classid:ac.getID();
							 */
							String acName = this.getTrueString(temps);
							ac = assetClassManager.get(acName);
							// classid = 0;
							if (acName != null && acName.length() > 0) {
								if (ac != null)
									classid = ac.getID();
								else if (acName != null && acName.length() > 0) {
									// System.out.println("new1    "+acName);
									ac = new AssetClass();
									ac.setBenchmarkID(91L);
									ac.setName(acName);
									ac.setParentID(0L);
									assetClassManager.save(ac);
									classid = ac.getID();
								}
							}
							break;
						case 4:// DataHeader.ASSET_TYPE:
							/*
							 * ac = assetClassManager.getChildClass(temps,
							 * classid); classid =
							 * (ac==null)?classid:ac.getID();
							 */
							String subACName = this.getTrueString(temps);
							if (subACName != null && subACName.length() > 0) {
								AssetClass subAC = assetClassManager.getChildClass(subACName, classid);
								if (subAC != null)
									classid = subAC.getID();
								else if (subACName != null && subACName.length() > 0) {
									// System.out.println("new2    "+subACName);
									subAC = new AssetClass();
									subAC.setBenchmarkID(91L);
									subAC.setName(subACName);
									subAC.setParentID(classid);
									assetClassManager.save(subAC);
									classid = subAC.getID();
								}
							}
							break;
						case 5:// DataHeader.SUBCLASS:
							/*
							 * ac = assetClassManager.getChildClass(temps,
							 * classid); classid =
							 * (ac==null)?classid:ac.getID();
							 */
							String subSubACName = this.getTrueString(temps);
							if (subSubACName != null && subSubACName.length() > 0) {
								AssetClass subSubAC = assetClassManager.getChildClass(subSubACName, classid);
								if (subSubAC != null)
									classid = subSubAC.getID();
								else if (subSubACName != null && subSubACName.length() > 0) {
									// System.out.println("new3    "+subSubACName);
									subSubAC = new AssetClass();
									subSubAC.setBenchmarkID(91L);
									subSubAC.setParentID(classid);
									subSubAC.setName(subSubACName);
									assetClassManager.save(subSubAC);
									classid = subSubAC.getID();
								}

							}
							break;
						case 6:// DataHeader.NAV:
							break;
						case 7:// DataHeader.DIVERSIFIED:
							if (temps != null)
								security.setDiversified(Boolean.parseBoolean(temps));
							break;
						case 8:
							tempStartDate = (Date) sf.parseObject(temps);
							startDate.add((Date) sf.parseObject(temps));
							break;
						case 9:
							tempEndDate = (Date) sf.parseObject(temps);
							endDate.add((Date) sf.parseObject(temps));
							break;
						case 10:
							if (temps != null)
								security.setManual(Integer.parseInt(temps));
							break;
						default:
							break;
						}
					}
					if (security.getClassID() == null || classid != security.getClassID())
						security.setClassID(classid);
					/**********************************************************************************************************/
					SecurityDailyData staticData = new SecurityDailyData();
					Date date2 = LTIDate.add(tempStartDate, -1);

					int type = this.getUpdateType(k);
					if (type == this.NORMAL_SECURITY) {
						staticData = securityManager.getLatestDailydata(security.getID(), date2);
						endDateList.add(date2);
						staticDataList.add(staticData);
						symbols.add(k);
					} else if (type == this.CHINA_OPEN_FUND) {
						openFundList.add(k);
					} else if (type == this.CHINA_CLOSE_FUND) {
						closeFundList.add(k);
					}
					/**********************************************************************************************************/

					if (security.getSecurityType().equals((Integer) 2)) {
						CEF.add(security.getSymbol());
						CEFstartDate.add((Date) sf.parseObject(this.getTrueString(s[DataHeader.STARTDATE])));
						CEFendDate.add((Date) sf.parseObject(this.getTrueString(s[DataHeader.ENDDATE])));
						navEndDateList.add(date2);
					}
					/***********************************************************************************************************/
					// if(s.length == lastAttribute.length)lastAttribute = s;
				}

				if (isNewSecurity) {
					securityManager.add(security);
				} else {
					if (overWrite)
						securityManager.update(security);
				}

				line = br.readLine();

			}

			if (symbols.size() == 0) {
				UpLoadException upE = new UpLoadException();
				upE.setErrorMsg("No Security In The File!");
				throw upE;
			}

			for (int i = 0; i < symbols.size(); i++) {
				Security se = securityManager.getBySymbol(symbols.get(i));
				if (se.getManual() == 1) {
					symbols.remove(i);
					endDateList.remove(i);
					continue;
				}
				if (flash && se.getManual() == 0)
					securityManager.clearDailyData(se.getID());

				saveToFile(symbols.get(i), startDate.get(i), endDate.get(i), "d", false);
				saveToFile(symbols.get(i), startDate.get(i), endDate.get(i), "v", false);

				SecurityDailyData tmp = securityManager.getLatestDailydata(securityManager.getBySymbol(symbols.get(i)).getID(), endDateList.get(i));

				firstDataList.add(tmp);

				Thread.sleep(500);
				ImportSecurityDataAction.addMessage(symbols.get(i) + " Done", SESSION.get());
				// System.out.println(symbols.get(i)+" Done");
			}
			ImportSecurityDataAction.addMessage("Start uploading file...", SESSION.get());

			upLoadFile("v", flash, false);
			upLoadFile("d", flash, false);

			ImportSecurityDataAction.addMessage("Start updating adjust price...", SESSION.get());
			this.updateAdjust(symbols, endDateList, firstDataList, staticDataList);

			ImportSecurityDataAction.addMessage("Start saving CEF to file...", SESSION.get());
			for (int i = 0; i < CEF.size(); i++) {
				this.saveCEFToFile(CEF.get(i), CEFstartDate.get(i), CEFendDate.get(i), "n", false);
				Thread.sleep(500);
			}

			ImportSecurityDataAction.addMessage("Start uploading NAV...", SESSION.get());
			uploadNAV(false);

			ImportSecurityDataAction.addMessage("Start updating NAV...", SESSION.get());
			this.updateAdjNAV(CEF, navEndDateList);

			/************************/
			/* because we build */
			/* up our old data */
			/* from our own data */
			/* doc so we do not */
			/* down load Chinese */
			/* fund data here. */
			/************************/

			for (int i = 0; i < symbols.size(); i++) {
				String symbol = symbols.get(i);
				Security se = securityManager.getBySymbol(symbol);
				try {
					if (se != null) {
						securityManager.getStartDate(se.getID());// if startDate
																	// is null,
																	// it will
																	// update it
																	// by using
																	// SQL
						securityManager.getEndDate(se.getID());
						se = securityManager.get(se.getID());
						se.setPriceLastDate(se.getEndDate());
						securityManager.update(se);
					}
				} catch (Exception e) {
					ImportSecurityDataAction.addMessage("update security end date : " + symbol + " , update fail", SESSION.get());
				}
				try {
					// System.out.println("start computing MPT");
					if (se != null)
						securityManager.getOneSecurityMPT(se.getID());
				} catch (Exception e) {
					System.out.println("computing MPT error");
					ImportSecurityDataAction.addMessage("compute mpt : " + symbol + " , update fail", SESSION.get());
				}
			}

			// System.out.println("Load Success");
			ImportSecurityDataAction.addMessage("Load Success", SESSION.get());

			Date cur = new Date();
			this.writeLog("All", cur, "Load Security Success");

		} catch (IOException e) {

			UpLoadException upE = new UpLoadException();
			ImportSecurityDataAction.addMessage("Down load File Error or File not Found!", SESSION.get());

			upE.setErrorMsg("Down load File Error or File not Found!");
			throw upE;

		} finally {
			ImportSecurityDataAction.addMessage("Load Success", SESSION.get());
		}
	}

	public void batchLoadCEFAttribute(String file, boolean flash) {
		try {
			// delete normal files
			this.deleteFile(false);

			AssetClassManager assetClassManager;
			assetClassManager = (AssetClassManager) ContextHolder.getInstance().getApplicationContext().getBean("assetClassManager");

			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);

			boolean isNewSecurity;
			boolean isFirstOne = true;

			String[] lastAttribute = null;
			List<Date> startDate = new ArrayList<Date>();
			List<Date> endDate = new ArrayList<Date>();
			List<String> symbols = new ArrayList<String>();

			DateFormat sf = new SimpleDateFormat("yyyy-MM-dd");

			String line = "";
			line = br.readLine();// ignore the header
			line = br.readLine();
			while (line != null) {
				isNewSecurity = false;
				line = line.trim();
				String[] s = line.split(",");
				Security security;
				String k = s[DataHeader.SYMBOL] = this.getTrueString(s[DataHeader.SYMBOL].split("/")[0].trim());
				security = securityManager.getBySymbol(k);
				symbols.add(k);
				if (security == null) {
					security = new Security();
					security.setSymbol(k);
					isNewSecurity = true;
				}
				if (isFirstOne) {
					lastAttribute = s;
					String[] name = s[DataHeader.NAME].split(" \\(");// ignore
																		// the
																		// "("
					k = this.getTrueString(name[0].trim());
					security.setName(k);
					security.setSecurityType(Configuration.getSecurityType(this.getTrueString(s[DataHeader.SECURITY_TYPE])));
					String kk = this.getTrueString(s[DataHeader.ASSET_CLASS].trim());
					AssetClass ac = assetClassManager.get(kk);
					long classid = 0;
					if (ac != null)
						classid = ac.getID();

					ac = assetClassManager.getChildClass(this.getTrueString(s[DataHeader.ASSET_TYPE]), classid);
					if (ac != null)
						classid = ac.getID();

					ac = assetClassManager.getChildClass(this.getTrueString(s[DataHeader.SUBCLASS]), classid);
					if (ac != null)
						classid = ac.getID();

					security.setClassID(classid);

					isFirstOne = false;
				} else {
					String temps;
					long classid = 0;
					AssetClass ac = new AssetClass();
					if (s.length == lastAttribute.length)
						lastAttribute = s;
					for (int i = 0; i < lastAttribute.length; i++) {
						if (i >= s.length) {
							temps = lastAttribute[i];
						} else if (s[i] == null) {
							temps = lastAttribute[i];
						} else {
							temps = s[i];
						}
						temps.trim();
						temps = this.getTrueString(temps);
						switch (i) {
						case 0:// DataHeader.Name:
							String[] name = temps.split(" \\(");// ignore the
																// "("
							k = this.getTrueString(name[0].trim());
							security.setName(k);
							break;
						case 2:// DataHeader.SECURITY_TYPE:
							security.setSecurityType(Configuration.getSecurityType(temps));
							break;
						case 3:// DataHeader.ASSET_CLASS:
							ac = assetClassManager.get(temps);
							classid = (ac == null) ? classid : ac.getID();
							break;
						case 4:// DataHeader.ASSET_TYPE:
							ac = assetClassManager.getChildClass(temps, classid);
							classid = (ac == null) ? classid : ac.getID();
							break;
						case 5:// DataHeader.SUBCLASS:
							ac = assetClassManager.getChildClass(temps, classid);
							classid = (ac == null) ? classid : ac.getID();
							security.setClassID(classid);
							break;
						case 6:// DataHeader.NAV:
							break;
						case 7:// DataHeader.DIVERSIFIED:
							security.setDiversified(Boolean.parseBoolean(temps));
							break;
						case 8:
							startDate.add((Date) sf.parseObject(temps));
							break;
						case 9:
							endDate.add((Date) sf.parseObject(temps));
							break;
						default:
							break;
						}
					}
				}

				if (isNewSecurity) {
					securityManager.add(security);
				} else {
					securityManager.update(security);
				}

				line = br.readLine();

			}

			for (int i = 0; i < symbols.size(); i++) {
				saveToFile(symbols.get(i), startDate.get(i), endDate.get(i), "d", false);
				saveToFile(symbols.get(i), startDate.get(i), endDate.get(i), "v", false);
			}
			upLoadFile("d", flash, false);
			upLoadFile("v", flash, false);

			// System.out.println("Load Success");

			Date cur = new Date();
			this.writeLog("All CEF", cur, "Load CEF Success");

		} catch (Exception e) {
			log.debug("Error: " + e);
			e.printStackTrace();
		}
	}

	// function: export all security(include all the attributes)to local file
	public void exportSecurityToDoc(String filename) throws IOException {
		try {
			String path = filename;

			AssetClassManager assetClassManager;
			assetClassManager = (AssetClassManager) ContextHolder.getInstance().getApplicationContext().getBean("assetClassManager");

			List<Security> securityList = securityManager.getSecurities();

			BufferedWriter buff = new BufferedWriter(new FileWriter(path));

			buff.write("Description (optional)	symbol	security type	Asset Class	Sub Class	Sub Sub Class		Diversified\n****** START HERE ******			**** DO NOT MODIFY THIS LINE ****\n");

			Date date = new Date();
			String endDate = LTIDate.parseDateToString(date);
			String startDate = "1900-1-1";

			for (int i = 0; i < securityList.size(); i++) {
				// buff.write(arg0);
				Security tempSecurity = securityList.get(i);
				if (tempSecurity.getSecurityType() == 6)
					continue;
				String name = tempSecurity.getName();
				String symbol = tempSecurity.getSymbol();
				String securityType = Configuration.getSecurityTypeName(tempSecurity.getSecurityType());
				String assetClass = assetClassManager.get(tempSecurity.getClassID()).getName();
				String diversified;
				if (tempSecurity.isDiversified())
					diversified = "1";
				else
					diversified = "0";
				String manual = tempSecurity.getManual().toString();
				/*
				 * if(tempSecurity.getManual())manual="1"; else manual="0";
				 */
				buff.write(name + "\t\t," + symbol + "\t," + securityType + "\t," + assetClass + "\t," + ",\t,\t," + diversified + "\t," + startDate + "\t," + endDate + "\t," + manual + ",\n");
			}
			buff.flush();
			buff.close();
			// System.out.println("Save to Document Success");
		} catch (Exception e) {

			log.debug("Error: " + e);
			e.printStackTrace();
		}
	}

	public void deleteFile(boolean update) {
		File rootFile;
		File[] files;

		rootFile = new File(systemPath + "spdatas");
		if (!rootFile.isDirectory())
			System.out.println(rootFile.mkdir());

		if (!update) {
			rootFile = new File(systemPath + "v");

			if (!rootFile.isDirectory())
				System.out.println(rootFile.mkdir());

			files = rootFile.listFiles();
			for (int i = 0; i < files.length; i++) {
				files[i].delete();
			}

			rootFile = new File(systemPath + "d");
			if (!rootFile.isDirectory())
				rootFile.mkdir();

			files = rootFile.listFiles();
			for (int i = 0; i < files.length; i++) {
				files[i].delete();
			}

			rootFile = new File(systemPath + "nav");
			if (!rootFile.isDirectory())
				System.out.println(rootFile.mkdir());

			files = rootFile.listFiles();
			for (int i = 0; i < files.length; i++) {
				files[i].delete();
			}

			rootFile = new File(systemPath + "updateopenfund");
			if (!rootFile.isDirectory())
				System.out.println(rootFile.mkdir());

			rootFile = new File(systemPath + "updateclosefund");
			if (!rootFile.isDirectory())
				System.out.println(rootFile.mkdir());

			rootFile = new File(systemPath + "updatemoneymarket");
			if (!rootFile.isDirectory())
				System.out.println(rootFile.mkdir());

		}

		else {
			rootFile = new File(systemPath + "update");
			if (!rootFile.isDirectory())
				System.out.println(rootFile.mkdir());

			rootFile = new File(systemPath + "update/" + "v");

			if (!rootFile.isDirectory())
				System.out.println(rootFile.mkdir());

			files = rootFile.listFiles();
			for (int i = 0; i < files.length; i++) {
				files[i].delete();
			}

			rootFile = new File(systemPath + "update/" + "d");
			if (!rootFile.isDirectory())
				rootFile.mkdir();

			files = rootFile.listFiles();
			for (int i = 0; i < files.length; i++) {
				files[i].delete();
			}

			rootFile = new File(systemPath + "update/" + "nav");
			if (!rootFile.isDirectory())
				System.out.println(rootFile.mkdir());

			files = rootFile.listFiles();
			for (int i = 0; i < files.length; i++) {
				files[i].delete();
			}

			rootFile = new File(systemPath + "update/" + "updateopenfund");
			if (!rootFile.isDirectory())
				System.out.println(rootFile.mkdir());

			files = rootFile.listFiles();
			for (int i = 0; i < files.length; i++) {
				files[i].delete();
			}

			rootFile = new File(systemPath + "update/" + "updateclosefund");
			if (!rootFile.isDirectory())
				System.out.println(rootFile.mkdir());

			files = rootFile.listFiles();
			for (int i = 0; i < files.length; i++) {
				files[i].delete();
			}

			rootFile = new File(systemPath + "update/" + "updatemoneymarket");
			if (!rootFile.isDirectory())
				System.out.println(rootFile.mkdir());

			files = rootFile.listFiles();
			for (int i = 0; i < files.length; i++) {
				files[i].delete();
			}
		}
	}

	// function: update each security's daily data(should be used everyday)
	@SuppressWarnings("deprecation")
	public void updateDailyDataOLD() {
		System.out.println("****start update daily data*****");

		Date cur = new Date();
		this.writeLog("ALL Security UpDate", cur, " start update daily data");

		try {
			//
			List<Security> securityList = securityManager.getSecurities();

			List<Date> endDateList = new ArrayList<Date>();

			List<SecurityDailyData> firstDataList = new ArrayList<SecurityDailyData>();

			List<SecurityDailyData> staticDataList = new ArrayList<SecurityDailyData>();

			List<Date> navEndDateList = new ArrayList<Date>();

			List<Date> fundEndDateList = new ArrayList<Date>();
			List<String> fundList = new ArrayList<String>();

			Date date = new Date();
			Date date2 = new Date();
			date2 = LTIDate.getDate(1900, 0, 0);

			// delete update files
			this.deleteFile(true);

			List<String> seList = new ArrayList<String>();

			List<String> navList = new ArrayList<String>();

			for (int i = 0; i < securityList.size(); i++)//
			{

				if (i == securityList.size())
					break;

				Security tempSecurity = securityList.get(i);

				if (tempSecurity.getSymbol() == null) {
					this.writeLog("Unknown Security", date, "security has no symbol");
					continue;
				}
				if (tempSecurity.getSecurityType() == null) {
					this.writeLog(tempSecurity.getSymbol(), date, "security has no security type");
					continue;
				}

				String symbol = tempSecurity.getSymbol();

				/*
				 * if( !symbol.equalsIgnoreCase("NPV") &&
				 * !symbol.equalsIgnoreCase("PCM") &&
				 * !symbol.equalsIgnoreCase("PYM") &&
				 * !symbol.equalsIgnoreCase("PGM") &&
				 * !symbol.equalsIgnoreCase("VGHCX") &&
				 * !symbol.equalsIgnoreCase("QQQQ") &&
				 * !symbol.equalsIgnoreCase("IWN") &&
				 * !symbol.equalsIgnoreCase("FXI") &&
				 * !symbol.equalsIgnoreCase("ICF"))
				 * 
				 * continue;
				 */
				if (!symbol.equalsIgnoreCase("AGG"))
					continue;

				// for portfolio to security
				if (tempSecurity.getSecurityType() == 6)
					continue;

				// System.out.println(symbol+" "+i);

				if (symbol.equalsIgnoreCase("CASH") || symbol.equalsIgnoreCase("CN CASH")) {
					continue;
				}

				int type = this.getUpdateType(symbol);

				if (type != this.NORMAL_SECURITY) {
					Date tmpDate = securityManager.getDailyDataLastDate(tempSecurity.getID());
					fundList.add(symbol);
					fundEndDateList.add(tmpDate);
					continue;
				}

				try {
					long id = tempSecurity.getID();

					SecurityDailyData staticData = new SecurityDailyData();

					Date dividendDate = securityManager.getDividendLastDate(id);
					if (dividendDate == null)
						dividendDate = date2;

					Date dailyDataDate = securityManager.getDailyDataLastDate(id);
					if (dailyDataDate == null)
						dailyDataDate = date2;

					if (tempSecurity.getSecurityType() == 2 && type == this.NORMAL_SECURITY) {
						// System.out.println("yes "+symbol+" "+i);

						Date navDate = securityManager.getNAVLastDate(id);
						if (navDate == null)
							navDate = date2;

						this.saveCEFToFile(symbol, navDate, date, "n", true);
						navList.add(symbol);
						navEndDateList.add(navDate);
					}

					saveToFile(symbol, dailyDataDate, date, "d", true);// save
																		// daily
																		// data
					saveToFile(symbol, dividendDate, date, "v", true);// save
																		// dividends

					staticData = securityManager.getLatestDailydata(id, dailyDataDate);

					seList.add(tempSecurity.getSymbol());
					endDateList.add(dailyDataDate);
					staticDataList.add(staticData);
					Thread.sleep(500);
				} catch (Exception e) {

					this.writeLog(symbol, date, "DownLoadError");
					this.writeLog(symbol, date, e.toString());

					e.printStackTrace();
				}
			}

			long s1 = System.currentTimeMillis();

			upLoadFile("v", false, true);
			upLoadFile("d", false, true);

			int index1 = 0;

			for (int i = 0; i < seList.size(); i++) {
				String se = seList.get(i);
				if (se == null || se.equalsIgnoreCase("CASH") || se.equalsIgnoreCase("CN CASH"))
					continue;

				SecurityDailyData firstData = new SecurityDailyData();

				date2 = endDateList.get(index1);
				index1++;

				Security tmpSe = securityManager.get(se);

				firstData = securityManager.getLatestDailydata(tmpSe.getID(), date2);

				firstDataList.add(firstData);
			}

			this.updateAdjust(seList, endDateList, firstDataList, staticDataList);

			this.uploadNAV(true);
			this.updateAdjNAV(navList, navEndDateList);

			/*
			 * this.updateCash("CASH","^IRX");
			 * 
			 * this.checkIndicatorUpdate();
			 * 
			 * if(this.getUpdateOption() != this.UPDATE_INTERNATIONAL) {
			 * this.downLoadAndUpdateFund(fundList, fundEndDateList);
			 * this.updateCash("CN CASH", "050003.OF"); }
			 * 
			 * 
			 * this.checkSplit();
			 * 
			 * System.out.println("Done.");
			 * 
			 * 
			 * this.writeLog("All Security", date, "Finish all update data.");
			 * 
			 * securityManager.getAllSecurityMPT(false);
			 */

		} catch (Exception e) {
			log.debug("Error: " + e);
			Date date = new Date();
			this.writeLog("UpdateDailyData", date, e.toString());
			e.printStackTrace();

		}
	}

	private List<String> getNameString() {
		List<String> sList = new ArrayList<String>();
		try {

			FileReader fr = new FileReader("C://a.csv");
			BufferedReader br = new BufferedReader(fr);
			while (true) {
				String line = br.readLine();
				if (line == null)
					break;
				// if(line.indexOf("AdjClose")>0)
				{
					String name = line.split(" ")[0];
					sList.add(name);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sList;
	}

	public void updateDailyDataByList(List<Security> securityList) {
		System.out.println("****start update daily data*****");
		Date initialStartDate = LTIDate.getDate(1900, 0, 0);
		Date newStartDate = LTIDate.add(initialStartDate, 1);
		if (securityList != null && securityList.size() > 0) {
			try {
				int totalCount = securityList.size();

				List<Security> securities = new ArrayList<Security>();

				HashMap<String, Date> lastDailyDataMap = new HashMap<String, Date>();

				List<Date> navEndDateList = new ArrayList<Date>();

				Date date = new Date();

				// delete update files
				this.deleteFile(true);

				List<String> navList = new ArrayList<String>();

				/**** part 1:update cef & dividend ***/
				for (int i = 0; i < totalCount; i++) {
					DailyUpdateControler.current_count = i + 1;
					Security security = securityList.get(i);
					if (security == null || security.getManual() != null && security.getManual() == 1)
						continue;
					if (security.getSymbol() == null) {
						this.writeLog("Unknown Security", date, "security has no symbol");
						continue;
					}
					if (security.getSecurityType() == null) {
						this.writeLog(security.getSymbol(), date, "security has no security type");
						continue;
					}
					String symbol = security.getSymbol();

					if (symbol.equalsIgnoreCase("CASH") || symbol.equalsIgnoreCase("CN CASH") || symbol.equalsIgnoreCase("TSPG")) {
						continue;
					}
					try {
						long id = security.getID();
						Date dividendDate = security.getDividendLastDate();
						Date dailyDataDate = security.getPriceLastDate();
						if (dailyDataDate == null)
							dailyDataDate = newStartDate;
						if (security.getSecurityType() == 2) {
							Date navDate = security.getNavLastDate();
							if (navDate == null) {
								navDate = initialStartDate;
							}
							navList.add(symbol);
							navEndDateList.add(navDate);
						}
						if (dividendDate == null)
							dividendDate = newStartDate;
						saveToFile(symbol, dividendDate, date, "v", true);// save
																			// dividends}
						securities.add(security);
						lastDailyDataMap.put(symbol, dailyDataDate);
					} catch (Exception e) {
						System.out.println(StringUtil.getStackTraceString(e));
					}
				}

				/**** part 2:upload dividend ***/
				upLoadFile("v", false, true);
				HashMap<String, Date> newLastDailyDataMap = new HashMap<String, Date>();

				/**** part 3:update daily price ***/
				totalCount = securities.size();
				for (int i = 0; i < totalCount; i++) {
					String symbol = null;
					try {
						Security security = securities.get(i);
						symbol = security.getSymbol();
						Date dailyDataDate = lastDailyDataMap.get(symbol);
						newLastDailyDataMap.put(symbol, dailyDataDate);
						dailyDataDate = LTIDate.getNewTradingDate(dailyDataDate, TimeUnit.DAILY, -1);
						saveToFile(symbol, dailyDataDate, date, "d", true);
					} catch (Exception e) {
						System.out.println(StringUtil.getStackTraceString(e));
					}

				}
				/**** part 4:upload daily price ***/
				try {
					upLoadFileAndAdjust(false, true, newLastDailyDataMap, false);
				} catch (Exception e) {
					System.out.println(StringUtil.getStackTraceString(e));
				}

				/**** part 5:upload nav ***/
				try {
					this.uploadNAV(true);
				} catch (Exception e) {
					System.out.println(StringUtil.getStackTraceString(e));
				}

				/**** part 6:update adj nav ***/
				try {
					this.updateAdjNAV(navList, navEndDateList);
				} catch (Exception e) {
					System.out.println(StringUtil.getStackTraceString(e));
				}
			} catch (Exception e) {
				System.out.println(StringUtil.getStackTraceString(e));

			}
		} else {
			System.out.println("Congratulation: The update list is empty!!!");
		}
	}

	/**
	 * Step 1, download all the dividends for the funds into files
	 * 
	 * @author CCD
	 * @throws Exception
	 */
	public void downLoadDividend() throws Exception {
		Date today = LTIDate.clearHMSM(new Date());
		Date defaultStartDate = LTIDate.getSystemStartDate();
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Security.class);
		detachedCriteria.add(Restrictions.ne("SecurityType", 6));
		List<Security> securityList = securityManager.getSecurities(detachedCriteria);
		int totalCount = securityList.size();
		DailyUpdateControler.state = "Update CEF&Dividend";
		DailyUpdateControler.total_count = securityList.size();
		this.writeLog("Update Daily Data", today, "\n************************************\n start download dividend\n************************************\n");
		for (int i = 0; i < totalCount; ++i) {
			Security security = securityList.get(i);
			if (security == null || security.getManual() != null && security.getManual() == 1)
				continue;
			if (security.getSymbol() == null) {
				this.writeLog("Unknown Security", today, "security has no symbol");
				continue;
			}
			if (security.getSecurityType() == null) {
				this.writeLog(security.getSymbol(), today, "security has no security type");
				continue;
			}
			String symbol = security.getSymbol();
			Date dividendDate = security.getDividendLastDate();
			if (dividendDate == null)
				dividendDate = defaultStartDate;
			saveToFile(symbol, dividendDate, today, "v", true);
		}
	}

	/**
	 * Step 2, upload the dividends according to the downloaded files
	 * 
	 * @throws Exception
	 */
	public HashMap<String, Date> uploadDividend(Date today, String type, boolean update, boolean fresh) throws Exception {
		DailyUpdateControler.state = "Upload Dividend";
		HashMap<String, Date> lastDateList = new HashMap<String, Date>();
		try {
			String path = systemPath;
			if (update)
				path = systemPath + "update/";
			if (isLinux())
				path += type + "/";
			else
				path += type + "//";

			File rootFile = new File(path.toString());
			File[] files = rootFile.listFiles();

			int totalCount = files.length;
			DailyUpdateControler.total_count = totalCount;
			for (int i = 0; i < totalCount; i++) {
				DailyUpdateControler.current_count = i + 1;
				if (files[i].isFile()) {
					String fileName = files[i].toString();
					String symbol = checkFile(files[i].getName());
					uploadDividendToDB(fileName, symbol, fresh);
					lastDateList.put(symbol, uploadDividendToDB(fileName, symbol, fresh));
					log.info("success:" + symbol);
					this.writeLog(symbol, today, "upload dividend to database success");
				}
			}
			log.info("All dividends success");
			this.writeLog("ALL Security", today, " All dividends upload to database success");
		} catch (Exception e) {
			log.debug("Error: " + e);
			throw e;
		}
		return lastDateList;
	}

	/**
	 * Step 3, download all the prices for the funds into files
	 */
	public void downLoadPrice(List<Security> securities) {
		Date today = LTIDate.clearHMSM(new Date());
		Date defaultStartDate = LTIDate.getSystemStartDate();
		DailyUpdateControler.state = "Update Daily Price";
		int totalCount = securities.size();
		DailyUpdateControler.total_count = totalCount;
		for (int i = 0; i < totalCount; ++i) {
			DailyUpdateControler.current_count = i + 1;
			Security security = securities.get(i);
			String symbol = security.getSymbol();
			try {
				Date priceLastDate = security.getPriceLastDate();
				Date newDividendDate = security.getNewDividendDate();
				if (priceLastDate == null)
					priceLastDate = defaultStartDate;
				if (newDividendDate != null && LTIDate.after(priceLastDate, newDividendDate))
					priceLastDate = newDividendDate;
				priceLastDate = LTIDate.getNewTradingDate(priceLastDate, TimeUnit.DAILY, -1);
				saveToFile(symbol, priceLastDate, today, "d", true);

				// Date dividendDataDate = lastDividendDataMap.get(symbol);
				// Date lastLastDividendDate =
				// lastLastDividendDataMap.get(symbol);
				// Date endDate = security.getEndDate();
				// if(endDate!=null&&LTIDate.equals(security.getEndDate(),
				// today)){
				// if(dividendDataDate==null)
				// continue;
				// else
				// if(lastLastDividendDate!=null&&LTIDate.equals(dividendDataDate,lastLastDividendDate))
				// continue;
				// else if(dailyDataDate.after(dividendDataDate))
				// dailyDataDate = dividendDataDate;
				// }
			} catch (Exception e) {
				this.writeLog(symbol, today, "DownLoad Price Error");
			}
		}
	}

	public Date uploadDividendToDB(String fileName, String symbol, boolean flush) throws Exception {
		boolean success = false;
		try {
			success = true;
			return dataManager.UpdateDividends(fileName, symbol, flush);
		} catch (Exception e) {
			success = false;
			log.debug("Error: " + e);
			e.printStackTrace();
			throw e;
		} finally {
			if (success == false)
				return null;
		}
	}
	Long part1StartTime;
	Long part1EndTime;
	Long part2StartTime;
	Long part2EndTime;
	Long part3StartTime;
	Long part3EndTime;
	Long part4StartTime;
	Long part4EndTime;
	Long part5StartTime;
	Long part5EndTime;
	Long part6StartTime;
	Long part6EndTime;
	Long part7StartTime;
	Long part7EndTime;
	Long part8StartTime;
	Long part8EndTime;
	Long part9StartTime;
	Long part9EndTime;
	Long part11StartTime;
	Long part11EndTime;
	Long part12StartTime;
	Long part12EndTime;
	Long part13StartTime;
	Long part13EndTime;
	Long part14StartTime;
	Long part14EndTime;
	Long part15StartTime;
	Long part15EndTime;

	public void updateDailyData() {
		System.out.println("****start update daily data*****");

		Date today = new Date();
		today = LTIDate.clearHMSM(today);
		boolean isWeekEnd = LTIDate.isWeekEnd(today);
		this.logDate = today;
		Date initialStartDate = LTIDate.getDate(1900, 0, 0);
		Date newStartDate = LTIDate.add(initialStartDate, 1);
		this.writeLog("ALL Security UpDate", today, "\n************************************\n start update daily data\n************************************\n");
		boolean isDownLoad;
		try {

			DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Security.class);
			detachedCriteria.add(Restrictions.ne("SecurityType", 6));
			if (!LTIDate.isSaturday(today))
				detachedCriteria.add(Restrictions.ne("SecurityType", 5));
			List<Security> securityList = securityManager.getSecurities(detachedCriteria);
			int totalCount = securityList.size();

			List<Security> securities = new ArrayList<Security>();

			HashMap<String, Date> lastDailyDataMap = new HashMap<String, Date>();

			List<Date> navEndDateList = new ArrayList<Date>();

			List<Date> fundEndDateList = new ArrayList<Date>();
			List<String> fundList = new ArrayList<String>();

			Date date = new Date();
			// delete update files
			this.deleteFile(true);

			List<String> navList = new ArrayList<String>();
			/**** part 1:update cef & dividend ***/
			this.writeLog("ALL Security UpDate part 1 start", new Date(), "\n************************************\n part 1:save CEF and dividend to file start\n************************************\n");
			part1StartTime = System.currentTimeMillis();
			DailyUpdateControler.state = "Update CEF&Dividend";
			DailyUpdateControler.total_count = securityList.size();
			int part1_success_count = 0;
			int part1_fail_count = 0;
			for (int i = 0; i < totalCount; i++) {
				DailyUpdateControler.current_count = i + 1;
				isDownLoad = false;
				Security security = securityList.get(i);
				if (security == null || security.getManual() != null && security.getManual() == 1)
					continue;
				if (security.getSymbol() == null) {
					this.writeLog("Unknown Security", date, "security has no symbol");
					continue;
				}
				if (security.getSecurityType() == null) {
					this.writeLog(security.getSymbol(), date, "security has no security type");
					continue;
				}
				String symbol = security.getSymbol();

				if (symbol.equalsIgnoreCase("CASH") || symbol.equalsIgnoreCase("CN CASH") || symbol.equalsIgnoreCase("TSPGFUND") || symbol.equalsIgnoreCase("STABLEVALUE")) {
					continue;
				}
				try {
					Date dividendDate = security.getDividendLastDate(); // 数据库最近的分红日期
					Date dailyDataDate = security.getPriceLastDate(); // 数据库Price的最新日期
					if (dailyDataDate == null)
						dailyDataDate = newStartDate;
					if (security.getSecurityType() == 2) {
						Date navDate = security.getNavLastDate(); // 数据库最新的nav日期
						if (navDate != null) {
							this.saveCEFToFile(symbol, navDate, date, "n", true);
							isDownLoad = true;
						} else if (isWeekEnd) {
							// 对于NavLastDate为null的security，从initialStartDate+1开始尝试下载NAV
							navDate = initialStartDate;
							this.saveCEFToFile(symbol, navDate, date, "n", true);
							isDownLoad = true;
						}
						if (isDownLoad) {
							navList.add(symbol);
							navEndDateList.add(navDate);
						}
					}
					if (dividendDate != null) {

						saveToFile(symbol, dividendDate, date, "v", true);// save
																			// dividends}
						// lastLastDividendDataMap.put(symbol, dividendDate);

					} else if (isWeekEnd) {
						// 对于DividendLastDate为null的security，从initialStartDate+1开始尝试下载dividend
						dividendDate = newStartDate;
						saveToFile(symbol, dividendDate, date, "v", true);// save
																			// dividends}
						// lastLastDividendDataMap.put(symbol, dividendDate);
					}
					securities.add(security);
					lastDailyDataMap.put(symbol, dailyDataDate);
					part1_success_count++;
				} catch (Exception e) {
					part1_fail_count++;
					this.writeLog(symbol + ":Error", date, "DownLoadError" + e.toString());
					e.printStackTrace();
				}
			}
			this.writeLog("ALL Security UpDate part 1 End", new Date(), "\n************************************\n Success count:" + part1_success_count + ";Error count:" + part1_fail_count + " \n************************************\n");
			part1EndTime = System.currentTimeMillis();
			System.out.println(part1EndTime - part1StartTime);
			HashMap<String, Date> lastDividendDataMap = new HashMap<String, Date>();
			/**** part 2:upload dividend ***/
			this.writeLog("ALL Security UpDate part 2 start", new Date(), "\n************************************\n part 2:save dividend to database start\n************************************\n");
			part2StartTime = System.currentTimeMillis();
			DailyUpdateControler.state = "Upload Dividend";
			// lastDividendDataMap = upLoadFile("v",false,true);
			lastDividendDataMap = upLoadFile("v", false, true);
			HashMap<String, Date> newLastDailyDataMap = new HashMap<String, Date>();

			/**** part 3:update daily price ***/
			this.writeLog("ALL Security UpDate part 3 start", new Date(), "\n************************************\n part 3:update daily price to file start\n************************************\n");
			part3StartTime = System.currentTimeMillis();
			DailyUpdateControler.state = "Update Daily Price";
			totalCount = securities.size();
			DailyUpdateControler.total_count = totalCount;
			int part3_success_count = 0;
			int part3_fail_count = 0;
			for (int i = 0; i < totalCount; i++) {
				String symbol = null;
				try {
					DailyUpdateControler.current_count = i + 1;
					Security security = securities.get(i);
					symbol = security.getSymbol();
					Date dailyDataDate = lastDailyDataMap.get(symbol);
					Date dividendDataDate = lastDividendDataMap.get(symbol);
					Date lastLastDividendDate = security.getDividendLastDate();
					Date endDate = security.getEndDate();
					if (!LTIDate.isLastNYSETradingDayOfWeek(today) && LTIDate.equals(security.getEndDate(), today)) {

					} else if (endDate != null) {
						// &&LTIDate.equals(security.getEndDate(), today)
						if (dividendDataDate == null) {
							newLastDailyDataMap.put(symbol, dailyDataDate);
							dailyDataDate = LTIDate.getNewTradingDate(dailyDataDate, TimeUnit.DAILY, -1);
							saveToFile(symbol, dailyDataDate, date, "d", true);
							part3_success_count++;
							continue;
						} else if (lastLastDividendDate != null && LTIDate.equals(dividendDataDate, lastLastDividendDate)) {
							newLastDailyDataMap.put(symbol, dailyDataDate);
							dailyDataDate = LTIDate.getNewTradingDate(dailyDataDate, TimeUnit.DAILY, -1);
							saveToFile(symbol, dailyDataDate, date, "d", true);
							part3_success_count++;
							continue;
						} else if (dailyDataDate.after(dividendDataDate)) {
							dailyDataDate = dividendDataDate;
							navEndDateList.remove(i);
							navEndDateList.add(i, dividendDataDate);
						}
					}
					newLastDailyDataMap.put(symbol, dailyDataDate);
					dailyDataDate = LTIDate.getNewTradingDate(dailyDataDate, TimeUnit.DAILY, -1);
					saveToFile(symbol, dailyDataDate, date, "d", true);
					part3_success_count++;
				} catch (Exception e) {
					this.writeLog(symbol + ":Error", date, "DownLoad Price Error");
					part3_fail_count++;
				}

			}
			part3EndTime = System.currentTimeMillis();
			this.writeLog("ALL Security UpDate part 3 End", new Date(), "\n************************************\n Success count:" + part3_success_count + ";Error count:" + part3_fail_count + " \n************************************\n");
			// when update fromStart = false, for reload database fromStart =
			// true;
			date = new Date();
			/**** part 4:upload daily price ***/
			this.writeLog("ALL Security UpDate part 4 start", new Date(), "\n************************************\n part 4:update daily price to database start\n************************************\n");
			part4StartTime = System.currentTimeMillis();
			try {
				DailyUpdateControler.state = "Upload Daily Price";
				upLoadFileAndAdjust(false, true, newLastDailyDataMap, false);
			} catch (Exception e) {
				part4EndTime = System.currentTimeMillis();
				this.writeLog("UpdateDailyData: upload daily price error", date, "part4 error:" + e.toString());
			}

			/**** part 5:upload nav ***/
			this.writeLog("ALL Security UpDate part 5 start", new Date(), "\n************************************\n part 5:update NAV to File start\n************************************\n");
			part5StartTime = System.currentTimeMillis();
			try {
				DailyUpdateControler.state = "Upload Nav";
				this.uploadNAV(true);
			} catch (Exception e) {
				part5EndTime = System.currentTimeMillis();
				this.writeLog("UpdateDailyData: upload nav error", date, "part5 error:" + e.toString());
			}

			/**** part 6:update adj nav ***/
			this.writeLog("ALL Security UpDate part 6 start", new Date(), "\n************************************\n part 6:update NAV to database start\n************************************\n");
			part6StartTime = System.currentTimeMillis();
			try {
				DailyUpdateControler.state = "Update AdjNav";
				this.updateAdjNAV(navList, navEndDateList);
			} catch (Exception e) {
				part6EndTime = System.currentTimeMillis();
				this.writeLog("UpdateDailyData: update adjNAV error", date, "part6 error:" + e.toString());
			}

			/**** part 7:update cash ***/
			this.writeLog("ALL Security UpDate part 7 start", new Date(), "\n************************************\n part 7:update cash start\n************************************\n");
			part7StartTime = System.currentTimeMillis();
			DailyUpdateControler.state = "Update Cash";
			DailyUpdateControler.total_count = 1;
			DailyUpdateControler.current_count = 1;
			try {
				this.updateCash("CASH", "^IRX");
			} catch (Exception e) {
				this.writeLog("UpdateDailyData: update CASH error", date, e.toString());
			}
			try {
				this.updateTSPGFund("TSPGFUND", "^TNX", "^FVX", false);
			} catch (Exception e) {
				this.writeLog("UpdateDailyData: update TSPGFUND error", date, e.toString());
			}

			try {
				this.updateTSPGFund("STABLEVALUE", "^TNX", "^FVX", false);
			} catch (Exception e) {
				this.writeLog("UpdateDailyData: update STABLEVALUE error", date, e.toString());
			}

			try {
				this.updateSpecialFund("QQQQ", "QQQ");
			} catch (Exception e) {
				this.writeLog("UpdateDailyData: update QQQQ error", date, e.toString());
			}
			part7EndTime = System.currentTimeMillis();
			this.writeLog("ALL Security UpDate part 7 end", new Date(), "\n************************************\n part 7:update cash End \n************************************\n");

			/**** part 8:update famabenchmark ***/
			this.writeLog("ALL Security UpDate part 8 start", new Date(), "\n************************************\n part 8:update famabenchmark start\n************************************\n");
			part8StartTime = System.currentTimeMillis();
			try {
				this.updateFamaBenchMark();
			} catch (Exception e) {
				this.writeLog("UpdateDailyData: update FamaBenchMark error", date, e.toString());
			}
			part8EndTime = System.currentTimeMillis();
			this.writeLog("ALL Security UpDate part 8 end", new Date(), "\n************************************\n part 8:update famabenchmark End\n************************************\n");

			this.writeLog("ALL Security UpDate part 9 and part10 start", new Date(), "\n************************************\n part 9 and part 10:update CN Fund and CN Cash start\n************************************\n");
			part9StartTime = System.currentTimeMillis();
			try {
				if (this.getUpdateOption() != this.UPDATE_INTERNATIONAL) {
					/**** part 9:update CN fund ****/
					DailyUpdateControler.state = "Update CN Fund";
					this.downLoadAndUpdateFund(fundList, fundEndDateList);
					/**** part 10:update CN CASH ****/
					DailyUpdateControler.state = "Update CN Cash";
					DailyUpdateControler.total_count = 1;
					DailyUpdateControler.current_count = 1;
					this.updateCash("CN CASH", "050003.OF");
					this.writeLog("Update update CN fund and CN Cash", new Date(), "update CN fund and CN Cash success");
				}
			} catch (Exception e1) {
				this.writeLog("Update update CN fund and CN Cash", new Date(), "update CN fund and CN Cash Error:" + e1.toString());
				e1.printStackTrace();
			}
			part9EndTime = System.currentTimeMillis();
			this.writeLog("ALL Security UpDate part 9 and part10 end", new Date(), "\n************************************\n part 9 and part 10:update CN Fund and CN Cash end\n************************************\n");

			/**** part 11:check split ****/
			this.writeLog("ALL Security UpDate part 11 start", new Date(), "\n************************************\n part11:check split start\n************************************\n");
			part11StartTime = System.currentTimeMillis();
			DailyUpdateControler.state = "Check Split";
			try {
				this.checkSplit();
			} catch (Exception e) {
				this.writeLog("UpdateDailyData: check split error", date, e.toString());
			}
			part11EndTime = System.currentTimeMillis();
			this.writeLog("ALL Security UpDate part 11 end", new Date(), "\n************************************\n part11:check split end\n************************************\n");

			/**** part 12:check indicator ****/
			this.writeLog("ALL Security UpDate part 12 start", new Date(), "\n************************************\n part12:Update Indicator start\n************************************\n");
			part12StartTime = System.currentTimeMillis();
			DailyUpdateControler.state = "Update Indicator";
			DailyUpdateControler.total_count = 1;
			DailyUpdateControler.current_count = 1;
			try {
				this.checkIndicatorUpdate();
			} catch (Exception e) {
				this.writeLog("UpdateDailyData: check indicator update error", date, e.toString());
			}
			part12EndTime = System.currentTimeMillis();
			this.writeLog("ALL Security UpDate part 12 end", new Date(), "\n************************************\n part12:Update Indicator end\n************************************\n");

			// System.out.println("Done.");

			// this.writeLog("All Security", date,
			// "\r\n************************************\r\nFinish all update data.\r\nPlease see more detail in the FinancialLog.\r\n************************************\r\n");
			this.writeLog("ALL Security UpDate part 13 start", new Date(), "\n************************************\n part13:download and update S&P start\n************************************\n");
			part13StartTime = System.currentTimeMillis();
			try {
				/**** part 13:download and update S&P ****/
				DailyUpdateControler.state = "Update S&P Earning&PE";
				downLoadAndUpdateSP();
			} catch (Exception e) {
				this.writeLog("UpdateDailyData: download and update sp error", date, e.toString());
				// System.out.println("down load and update SP "+date+
				// "Error."+e.toString());
				// this.writeLog("down load and update SP ", date,
				// "Error."+e.toString());
			}
			part13EndTime = System.currentTimeMillis();
			this.writeLog("ALL Security UpDate part 13 end", new Date(), "\n************************************\n part13:download and update S&P end\n************************************\n");

			String updateMode = (String) Configuration.get("UPDATE_MODE");

			System.out.println(updateMode);

			/**** part 14: update financial statement ****/
			this.writeLog("ALL Security UpDate part 14 start", new Date(), "\n************************************\n part14:update financial statement start\n************************************\n");
			part14StartTime = System.currentTimeMillis();
			try {
				this.updateFinancialStatement(updateMode);
			} catch (Exception e) {
				this.writeLog("UpdateDailyData: update financialstatement error", date, e.toString());
			}
			part14EndTime = System.currentTimeMillis();
			this.writeLog("ALL Security UpDate part 14 end", new Date(), "\n************************************\n part14:update financial statement end\n************************************\n");

			/**** part 15: update other ****/
			this.writeLog("ALL Security UpDate part 15 start", new Date(), "\n************************************\n part15:update other start\n************************************\n");
			part15StartTime = System.currentTimeMillis();
			try {
				this.doSomeOtherUpdateJob();
			} catch (Exception e) {
				this.writeLog("UpdateDailyData: update shillerSP500 error", date, e.toString());
			}
			part15EndTime = System.currentTimeMillis();
		} catch (Exception e) {
			log.debug("Error: " + e);
			Date date = new Date();
			this.writeLog("UpdateDailyData", date, e.toString());
			e.printStackTrace();
			this.writeLog("ALL Security UpDate part 15 end", new Date(), "\n************************************\n part15:update other end\n************************************\n");
			Long a = part1EndTime - part1StartTime;
			Long b = part2EndTime - part2StartTime;
			Long c = part3EndTime - part3StartTime;
			Long d = part4EndTime - part4StartTime;
			Long e1 = part5EndTime - part5StartTime;
			Long f = part6EndTime - part6StartTime;
			Long g = part7EndTime - part7StartTime;
			Long h = part8EndTime - part8StartTime;
			Long i = part9EndTime - part9StartTime;
			Long j = part11EndTime - part11StartTime;
			Long k = part12EndTime - part12StartTime;
			Long l = part13EndTime - part13StartTime;
			Long m = part14EndTime - part14StartTime;
			Long n = part15EndTime - part15StartTime;
			String strOut = "Use Time:\nPart1:" + a + " Part2:" + b +" Part3:" + c + " Part4:" + d + " Part5:" + e1 + " Part6:" + f +" Part7:" + g + " Part8:" + h + " Part9 and Part10:" + i + " Part11:" + j + " Part12:" + k + " Part13:" + l + " Part14：" + m + " Part15:"+ n;
			System.out.println(strOut);
			this.writeLog("Use Time:\nPart1:" + a + " Part2:" + b +" Part3:" + c + " Part4:" + d + " Part5:" + e1 + " Part6:" + f +" Part7:" + g + " Part8:" + h + " Part9 and Part10:" + i + " Part11:" + j + " Part12:" + k + " Part13:" + l + " Part14：" + m + " Part15:"+ n, new Date(), "\n************************************\n other end\n************************************\n");

		}
	}

	/**
	 * test
	 * 
	 * @author YZW
	 */
	public void testUpdateDailyDataNew(List<Security> securityList) {

		System.out.println("****start update daily data*****");

		Date today = new Date();
		today = LTIDate.clearHMSM(today);
		boolean isWeekEnd = LTIDate.isWeekEnd(today);
		this.logDate = today;
		Date initialStartDate = LTIDate.getDate(1900, 0, 0);
		Date newStartDate = LTIDate.add(initialStartDate, 1);
		this.writeLog("ALL Security UpDate", today, "\n************************************\n start update daily data\n************************************\n");
		boolean isDownLoad;
		try {

			// DetachedCriteria detachedCriteria =
			// DetachedCriteria.forClass(Security.class);
			// detachedCriteria.add(Restrictions.ne("SecurityType", 6));
			// if(!LTIDate.isSaturday(today))
			// detachedCriteria.add(Restrictions.ne("SecurityType", 5));
			// List<Security> securityList =
			// securityManager.getSecurities(detachedCriteria);
			int totalCount = securityList.size();

			List<Security> securities = new ArrayList<Security>();

			HashMap<String, Date> lastDailyDataMap = new HashMap<String, Date>();

			HashMap<String, Date> lastLastDividentDate = new HashMap<String, Date>();

			List<Date> navEndDateList = new ArrayList<Date>();

			List<Date> fundEndDateList = new ArrayList<Date>();
			List<String> fundList = new ArrayList<String>();

			Date date = new Date();
			// delete update files
			this.deleteFile(true);

			List<String> navList = new ArrayList<String>();
			/**** part 1:update cef & dividend ***/

			this.writeLog("ALL Security UpDate part 1 start", new Date(), "\n************************************\n part 1:save CEF and dividend to file start\n************************************\n");
			DailyUpdateControler.state = "Update CEF&Dividend";
			DailyUpdateControler.total_count = securityList.size();
			int part1_success_count = 0;
			int part1_fail_count = 0;

			for (int i = 0; i < totalCount; i++) {
				DailyUpdateControler.current_count = i + 1;
				isDownLoad = false;
				Security security = securityList.get(i);
				if (security == null || security.getManual() != null && security.getManual() == 1)
					continue;
				if (security.getSymbol() == null) {
					System.out.println("Unknown Security " + date + " security has no symbol");
					continue;
				}
				if (security.getSecurityType() == null) {
					System.out.println(security.getSymbol() + date + " security has no security type");
					continue;
				}
				String symbol = security.getSymbol();

				if (symbol.equalsIgnoreCase("CASH") || symbol.equalsIgnoreCase("CN CASH") || symbol.equalsIgnoreCase("TSPGFUND") || symbol.equalsIgnoreCase("STABLEVALUE")) {
					continue;
				}
				try {
					Date dividendDate = security.getDividendLastDate();
					Date dailyDataDate = security.getPriceLastDate();
					if (dailyDataDate == null)
						dailyDataDate = newStartDate;
					if (security.getSecurityType() == 2) {
						Date navDate = security.getNavLastDate();
						if (navDate != null) {
							isDownLoad = true;
							this.saveCEFToFile(symbol, navDate, date, "n", true);
						} else if (isWeekEnd) {
							isDownLoad = true;
							navDate = initialStartDate;
							this.saveCEFToFile(symbol, navDate, date, "n", true);
						}
						if (isDownLoad) {
							navList.add(symbol);
							navEndDateList.add(navDate);
						}
					}
					if (dividendDate != null) {
						saveToFile(symbol, dividendDate, date, "v", true);// save
																			// dividends}
						// lastLastDividendDataMap.put(symbol, dividendDate);

					} else if (isWeekEnd) {
						dividendDate = newStartDate;
						saveToFile(symbol, dividendDate, date, "v", true);// save
																			// dividends}
						// lastLastDividendDataMap.put(symbol, dividendDate);
					}
					securities.add(security);
					lastLastDividentDate.put(symbol, dividendDate);
					lastDailyDataMap.put(symbol, dailyDataDate); // Price最近的日期
					part1_success_count++;
				} catch (Exception e) {
					part1_fail_count++;
					this.writeLog(symbol, date, "DownLoadError");
					this.writeLog(symbol, date, e.toString());
					e.printStackTrace();
				}
			}
			this.writeLog("ALL Security UpDate part 1 End", new Date(), "\n************************************\n Success count:" + part1_success_count + ";Error count:" + part1_fail_count + " \n************************************\n");
			HashMap<String, Date> lastDividendDataMap = new HashMap<String, Date>();
			/**** part 2:upload dividend ***/
			this.writeLog("ALL Security UpDate part 2 start", new Date(), "\n************************************\n part 2:save dividend to database start\n************************************\n");
			DailyUpdateControler.state = "Upload Dividend";
			// lastDividendDataMap = upLoadFile("v",false,true);
			lastDividendDataMap = upLoadFile("v", false, true);
			HashMap<String, Date> newLastDailyDataMap = new HashMap<String, Date>();

			/**** part 3:update daily price ***/
			this.writeLog("ALL Security UpDate part 3 start", new Date(), "\n************************************\n part 3:update daily price to file start\n************************************\n");
			DailyUpdateControler.state = "Update Daily Price";
			totalCount = securities.size();
			DailyUpdateControler.total_count = totalCount;
			int part3_success_count = 0;
			int part3_fail_count = 0;
			for (int i = 0; i < totalCount; i++) {
				String symbol = null;
				try {
					DailyUpdateControler.current_count = i + 1;
					Security security = securities.get(i);
					symbol = security.getSymbol();
					Date dailyDataDate = lastDailyDataMap.get(symbol); // 获取lastDailyDataMap集合中对应的symbol的Price最新日期
					Date dividendDataDate = lastDividendDataMap.get(symbol); // 下载的diviend文件中最小日期
					Date lastLastDividendDate = security.getDividendLastDate(); // 获取数据库里对应的security的最近diviend日期
					Date endDate = security.getEndDate(); // 获取数据库里对应的security最近更新日期
					if (endDate != null) {
						if (dividendDataDate == null) {
							newLastDailyDataMap.put(symbol, dailyDataDate);
							dailyDataDate = LTIDate.getNewTradingDate(dailyDataDate, TimeUnit.DAILY, -1);// 获取最新的Price日期的前一个交易日期
							saveToFile(symbol, dailyDataDate, date, "d", true);// 保存到文件
																				// n=nav,d=daily
																				// price,v=dividend
							part3_success_count++;
							continue;
						} else if (lastLastDividendDate != null && LTIDate.equals(dividendDataDate, lastLastDividendDate)) {
							// 正常情况
							newLastDailyDataMap.put(symbol, dailyDataDate);
							dailyDataDate = LTIDate.getNewTradingDate(dailyDataDate, TimeUnit.DAILY, -1);
							saveToFile(symbol, dailyDataDate, date, "d", true);
							part3_success_count++;
							continue;
						} else if (dailyDataDate.after(dividendDataDate)) {
							// ???
							dailyDataDate = dividendDataDate;
							navEndDateList.remove(i);
							navEndDateList.add(i, LTIDate.getNewTradingDate(dividendDataDate, TimeUnit.DAILY, -1));
						}
					}
					newLastDailyDataMap.put(symbol, dailyDataDate); // 把下载的diviend文件中最小日期或最新price日期和对应的symbol放到新的集合中
					dailyDataDate = LTIDate.getNewTradingDate(dailyDataDate, TimeUnit.DAILY, -1);
					saveToFile(symbol, dailyDataDate, date, "d", true);
					part3_success_count++;
				} catch (Exception e) {
					this.writeLog(symbol + ":Error", date, "DownLoad Price Error");
					part3_fail_count++;
				}

			}
			this.writeLog("ALL Security UpDate part 3 End", new Date(), "\n************************************\n Success count:" + part3_success_count + ";Error count:" + part3_fail_count + " \n************************************\n");
			// when update fromStart = false, for reload database fromStart =
			// true;
			date = new Date();
			/**** part 4:upload daily price ***/
			this.writeLog("ALL Security UpDate part 4 start", new Date(), "\n************************************\n part 4:update daily price to database start\n************************************\n");
			try {
				DailyUpdateControler.state = "Upload Daily Price";
				upLoadFileAndAdjust(false, true, newLastDailyDataMap, false);
			} catch (Exception e) {

				this.writeLog("UpdateDailyData: upload daily price error", date, e.toString());
			}

			/**** part 5:upload nav ***/
			this.writeLog("ALL Security UpDate part 5 start", new Date(), "\n************************************\n part 5:update NAV to File start\n************************************\n");
			try {
				DailyUpdateControler.state = "Upload Nav";
				this.uploadNAV(true);
			} catch (Exception e) {
				this.writeLog("UpdateDailyData: upload nav error", date, e.toString());
			}

			/**** part 6:update adj nav ***/
			this.writeLog("ALL Security UpDate part 6 start", new Date(), "\n************************************\n part 6:update NAV to database start\n************************************\n");
			try {
				DailyUpdateControler.state = "Update AdjNav";
				this.updateAdjNAV(navList, navEndDateList);
			} catch (Exception e) {
				this.writeLog("UpdateDailyData: update adjNAV error", date, e.toString());
			}

			/**** part 7:update cash ***/
			// DailyUpdateControler.state="Update Cash";
			// DailyUpdateControler.total_count=1;
			// DailyUpdateControler.current_count=1;
			// try{
			// this.updateCash("CASH","^IRX");
			// }catch(Exception e){
			// this.writeLog("UpdateDailyData: update CASH error", date,
			// e.toString());
			// }
			// try{
			// this.updateTSPGFund("TSPGFUND", "^TNX", "^FVX", false);
			// }catch(Exception e){
			// this.writeLog("UpdateDailyData: update TSPGFUND error", date,
			// e.toString());
			// }
			//
			// try{
			// this.updateTSPGFund("STABLEVALUE", "^TNX", "^FVX", false);
			// }catch(Exception e){
			// this.writeLog("UpdateDailyData: update STABLEVALUE error", date,
			// e.toString());
			// }
			//
			// try{
			// this.updateSpecialFund("QQQQ", "QQQ");
			// }catch(Exception e){
			// this.writeLog("UpdateDailyData: update QQQQ error", date,
			// e.toString());
			// }

			/**** part 8:update famabenchmark ***/
			// try{
			// this.updateFamaBenchMark();
			// }catch(Exception e){
			// this.writeLog("UpdateDailyData: update FamaBenchMark error",
			// date, e.toString());
			// }
			//
			// try {
			// if(this.getUpdateOption() != this.UPDATE_INTERNATIONAL)
			// {
			// /****part 9:update CN fund****/
			// DailyUpdateControler.state="Update CN Fund";
			// this.downLoadAndUpdateFund(fundList, fundEndDateList);
			// /****part 10:update CN CASH****/
			// DailyUpdateControler.state="Update CN Cash";
			// DailyUpdateControler.total_count=1;
			// DailyUpdateControler.current_count=1;
			// this.updateCash("CN CASH", "050003.OF");
			// }
			// } catch (Exception e1) {
			// e1.printStackTrace();
			// }

			/**** part 11:check split ****/
			// DailyUpdateControler.state="Check Split";
			// try{
			// this.checkSplit();
			// }catch(Exception e){
			// this.writeLog("UpdateDailyData: check split error", date,
			// e.toString());
			// }

			/**** part 12:check indicator ****/
			// DailyUpdateControler.state="Update Indicator";
			// DailyUpdateControler.total_count=1;
			// DailyUpdateControler.current_count=1;
			// try{
			// this.checkIndicatorUpdate();
			// }catch(Exception e){
			// this.writeLog("UpdateDailyData: check indicator update error",
			// date, e.toString());
			// }
			//
			//
			// //System.out.println("Done.");
			//
			// this.writeLog("All Security", date,
			// "\r\n************************************\r\nFinish all update data.\r\nPlease see more detail in the FinancialLog.\r\n************************************\r\n");
			//
			// try{
			// /****part 13:download and update S&P****/
			// DailyUpdateControler.state="Update S&P Earning&PE";
			// downLoadAndUpdateSP();
			// }catch (Exception e){
			// this.writeLog("UpdateDailyData: download and update sp error",
			// date, e.toString());
			// //System.out.println("down load and update SP "+date+
			// "Error."+e.toString());
			// //this.writeLog("down load and update SP ", date,
			// "Error."+e.toString());
			// }
			//
			// String updateMode = (String)Configuration.get("UPDATE_MODE");
			//
			// System.out.println(updateMode);
			//
			// /****part 14: update financial statement****/
			// try{
			// this.updateFinancialStatement(updateMode);
			// }catch(Exception e){
			// this.writeLog("UpdateDailyData: update financialstatement error",
			// date, e.toString());
			// }
			//
			// /****part 15: update other****/
			// try{
			// this.doSomeOtherUpdateJob();
			// }catch(Exception e){
			// this.writeLog("UpdateDailyData: update shillerSP500 error", date,
			// e.toString());
			// }
			this.writeLog("ALL Security UpDate 6 part finish", new Date(), "\r\n************************************\r\nFinish all update data.\r\nPlease see more detail in the FinancialLog.\r\n************************************\r\n");

		} catch (Exception e) {
			log.debug("Error: " + e);
			Date date = new Date();
			this.writeLog("UpdateDailyData", date, e.toString());
			e.printStackTrace();

		}
	}

	/**
	 * @author CCD
	 */
	public void updateDailyDataNew() {

		System.out.println("****start update daily data*****");

		Date today = new Date();
		today = LTIDate.clearHMSM(today);
		boolean isWeekEnd = LTIDate.isWeekEnd(today);
		this.logDate = today;
		Date initialStartDate = LTIDate.getDate(1900, 0, 0);
		Date newStartDate = LTIDate.add(initialStartDate, 1);
		this.writeLog("ALL Security UpDate", today, "\n************************************\n start update daily data\n************************************\n");
		boolean isDownLoad;
		try {
			DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Security.class);
			detachedCriteria.add(Restrictions.ne("SecurityType", 6));
			List<Security> securityList = securityManager.getSecurities(detachedCriteria);
			int totalCount = securityList.size();

			List<Security> securities = new ArrayList<Security>();

			HashMap<String, Date> lastDailyDataMap = new HashMap<String, Date>();
			HashMap<String, Date> lastDividendDataMap = new HashMap<String, Date>();
			HashMap<String, Date> lastLastDividendDataMap = new HashMap<String, Date>();

			List<Date> navEndDateList = new ArrayList<Date>();

			List<Date> fundEndDateList = new ArrayList<Date>();
			List<String> fundList = new ArrayList<String>();

			Date date = new Date();

			// delete update files
			this.deleteFile(true);

			List<String> navList = new ArrayList<String>();

			/**** part 1:update cef & dividend ***/

			DailyUpdateControler.state = "Update CEF&Dividend";
			DailyUpdateControler.total_count = securityList.size();

			for (int i = 0; i < totalCount; i++) {
				DailyUpdateControler.current_count = i + 1;
				isDownLoad = false;
				Security security = securityList.get(i);
				if (security == null || security.getManual() != null && security.getManual() == 1)
					continue;
				if (security.getSymbol() == null) {
					this.writeLog("Unknown Security", date, "security has no symbol");
					continue;
				}
				if (security.getSecurityType() == null) {
					this.writeLog(security.getSymbol(), date, "security has no security type");
					continue;
				}
				String symbol = security.getSymbol();

				if (symbol.equalsIgnoreCase("CASH") || symbol.equalsIgnoreCase("CN CASH") || symbol.equalsIgnoreCase("TSPG")) {
					continue;
				}
				try {
					Date dividendDate = security.getDividendLastDate();
					Date dailyDataDate = security.getPriceLastDate();
					if (dailyDataDate == null)
						dailyDataDate = newStartDate;
					if (security.getSecurityType() == 2) {
						Date navDate = security.getNavLastDate();
						if (navDate != null) {
							isDownLoad = true;
							this.saveCEFToFile(symbol, navDate, date, "n", true);
						} else if (isWeekEnd) {
							isDownLoad = true;
							navDate = initialStartDate;
							this.saveCEFToFile(symbol, navDate, date, "n", true);
						}
						if (isDownLoad) {
							navList.add(symbol);
							navEndDateList.add(navDate);
						}
					}
					if (dividendDate != null) {
						saveToFile(symbol, dividendDate, date, "v", true);// save
																			// dividends}
						lastLastDividendDataMap.put(symbol, dividendDate);

					} else if (isWeekEnd) {
						dividendDate = newStartDate;
						saveToFile(symbol, dividendDate, date, "v", true);// save
																			// dividends}
						lastLastDividendDataMap.put(symbol, dividendDate);
					}
					securities.add(security);
					lastDailyDataMap.put(symbol, dailyDataDate);
				} catch (Exception e) {
					this.writeLog(symbol, date, "DownLoadError");
					this.writeLog(symbol, date, e.toString());
					e.printStackTrace();
				}
			}

			/**** part 2:upload dividend ***/
			DailyUpdateControler.state = "Upload Dividend";
			lastDividendDataMap = upLoadFile("v", false, true);
			HashMap<String, Date> newLastDailyDataMap = new HashMap<String, Date>();

			/**** part 3:update daily price ***/
			DailyUpdateControler.state = "Update Daily Price";
			totalCount = securities.size();
			DailyUpdateControler.total_count = totalCount;
			for (int i = 0; i < totalCount; i++) {
				String symbol = null;
				try {
					DailyUpdateControler.current_count = i + 1;
					Security security = securities.get(i);
					symbol = security.getSymbol();
					Date dailyDataDate = lastDailyDataMap.get(symbol);
					Date dividendDataDate = lastDividendDataMap.get(symbol);
					Date lastLastDividendDate = lastLastDividendDataMap.get(symbol);
					Date endDate = security.getEndDate();
					if (endDate != null && LTIDate.equals(security.getEndDate(), today)) {
						if (dividendDataDate == null)
							continue;
						else if (lastLastDividendDate != null && LTIDate.equals(dividendDataDate, lastLastDividendDate))
							continue;
						else if (dailyDataDate.after(dividendDataDate))
							dailyDataDate = dividendDataDate;
					}
					newLastDailyDataMap.put(symbol, dailyDataDate);
					dailyDataDate = LTIDate.getNewTradingDate(dailyDataDate, TimeUnit.DAILY, -1);
					saveToFile(symbol, dailyDataDate, date, "d", true);
				} catch (Exception e) {
					this.writeLog(symbol, date, "DownLoad Price Error");
				}

			}
			// when update fromStart = false, for reload database fromStart =
			// true;
			/**** part 4:upload daily price ***/
			DailyUpdateControler.state = "Upload Daily Price";
			upLoadFileAndAdjust(false, true, newLastDailyDataMap, false);

			/**** part 5:upload nav ***/
			DailyUpdateControler.state = "Upload Nav";
			this.uploadNAV(true);

			/**** part 6:update adj nav ***/
			DailyUpdateControler.state = "Update AdjNav";
			this.updateAdjNAV(navList, navEndDateList);

			/**** part 7:update cash ***/
			DailyUpdateControler.state = "Update Cash";
			DailyUpdateControler.total_count = 1;
			DailyUpdateControler.current_count = 1;
			this.updateCash("CASH", "^IRX");

			/**** part 8:update famabenchmark ***/
			this.updateFamaBenchMark();

			try {
				if (this.getUpdateOption() != this.UPDATE_INTERNATIONAL) {
					/**** part 9:update CN fund ****/
					DailyUpdateControler.state = "Update CN Fund";
					this.downLoadAndUpdateFund(fundList, fundEndDateList);
					/**** part 10:update CN CASH ****/
					DailyUpdateControler.state = "Update CN Cash";
					DailyUpdateControler.total_count = 1;
					DailyUpdateControler.current_count = 1;
					this.updateCash("CN CASH", "050003.OF");
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}

			/**** part 11:check split ****/
			DailyUpdateControler.state = "Check Split";
			this.checkSplit();
			/**** part 12:check indicator ****/
			DailyUpdateControler.state = "Update Indicator";
			DailyUpdateControler.total_count = 1;
			DailyUpdateControler.current_count = 1;
			this.checkIndicatorUpdate();

			// System.out.println("Done.");

			this.writeLog("All Security", date, "\r\n************************************\r\nFinish all update data.\r\nPlease see more detail in the FinancialLog.\r\n************************************\r\n");

			try {
				/**** part 13:download and update S&P ****/
				DailyUpdateControler.state = "Update S&P Earning&PE";
				downLoadAndUpdateSP();
			} catch (Exception e) {
				// System.out.println("down load and update SP "+date+
				// "Error."+e.toString());
				// this.writeLog("down load and update SP ", date,
				// "Error."+e.toString());
			}

			String updateMode = (String) Configuration.get("UPDATE_MODE");

			// System.out.println(updateMode);

			/**** part 14: update financial statement ****/

			this.updateFinancialStatement(updateMode);
			/**** part 15: update other ****/
			this.doSomeOtherUpdateJob();

		} catch (Exception e) {
			log.debug("Error: " + e);
			Date date = new Date();
			this.writeLog("UpdateDailyData", date, e.toString());
			e.printStackTrace();

		}
	}

	/**
	 * @author SuPing
	 * @param updateMode
	 *            Add a independent method for update 2009-11-6
	 */
	public void updateFinanStateData() {
		this.updateFinancialStatement(false);
	}

	public void updateFinancialStatement(String updateMode) {
		Date date = new Date();
		if (updateMode.equalsIgnoreCase("weekly")) {
			if (LTIDate.isWeekEnd(date))
				this.updateFinancialStatement(true);
		} else if (updateMode.equalsIgnoreCase("monthly")) {
			if (LTIDate.isMonthEnd(date))
				this.updateFinancialStatement(true);
		} else {
			if (LTIDate.isQuarterEnd(date))
				this.updateFinancialStatement(true);
		}
	}

	/**
	 * modify by SuPing Add some signal for the independent FinancialStatement
	 * Update Process 2009-11-10
	 * 
	 * @param flg
	 */
	public void updateFinancialStatement(boolean flg) {
		/**** part 14: update financial statement ****/
		if (flg)
			DailyUpdateControler.state = "Update Stocks Financial Statement";
		else if (!flg) {
			FinanStateControler.state = "Update Stocks Financial Statement";
			FinanStateControler.current_count = 0;
		}
		Date date = new Date();
		Configuration.writeFinancialLog("All Stocks", date, "\r\n\n************************************\r\nStart update Financial Statement.\r\n************************************\r\n");
		List<Security> seList = securityManager.getSecuritiesByType(5);
		List<String> a = securityManager.getAllStocks();
		if (flg)
			DailyUpdateControler.total_count = seList.size();
		else if (!flg)
			FinanStateControler.total_count = seList.size();
		for (int i = 0; i < seList.size(); i++) {

			fsm.downloadFinancialStatement(seList.get(i).getSymbol());
			System.out.println(seList.get(i).getSymbol() + " is done." + FinanStateControler.current_count);

			if (flg)
				DailyUpdateControler.current_count = i + 1;
			else if (!flg)
				FinanStateControler.current_count = i + 1;
			// fsm.downloadFinancialStatement(seList.get(i).getSymbol(), date);
		}
		Configuration.writeFinancialLog("All Stocks", date, "\r\n\n************************************\r\n FinancialStatement checking time starting...\r\n************************************\r\n");
		Set<String> symbolSet = new HashSet<String>();
		// symbolSet=fsm.chkBICStatement(securityManager, a);//完全封装方法
		String[] type = { "balancestatement", "cashflow", "incomestatement" };
		String symbol = null;
		if (!flg) {
			FinanStateControler.state = "Check Stocks Financial Statement";
			FinanStateControler.current_count = 0;
			FinanStateControler.total_count = a.size() * 3;
		}
		for (int i = 0; i < type.length; i++)
			for (int j = 0; j < a.size(); j++) {
				symbol = fsm.chkBICStatement(a.get(j), type[i]);
				FinanStateControler.current_count++;
				if (symbol != null) {
					symbolSet.add(symbol);
					System.out.println(symbol + ": has checked!");
				} else
					continue;
			}

		String[] symList = (String[]) symbolSet.toArray(new String[symbolSet.size()]);
		Configuration.writeFinancialLog("Some Stocks", date, "\r\n\n************************************\r\n FinancialStatement start make up...\r\n************************************\r\n");
		if (!flg) {
			FinanStateControler.state = "Make up Stocks Financial Statement";
			FinanStateControler.current_count = 0;
			FinanStateControler.total_count = symList.length;
		}
		for (int i = 0; i < symList.length; i++) {
			fsm.downloadFinancialStatement(symList[i]);
			if (!flg)
				FinanStateControler.current_count = i + 1;
		}
	}

	/**
	 * @author SuPing Add for the independentYearly FinancialStatetment Data
	 *         2010-01-04
	 */
	public void updateYearlyFinanStateData() {
		this.updateYearlyFinancialStatement();
	}

	/**
	 * @author SuPing Add some signal for the independent Yearly
	 *         FinancialStatement Update Process 2010-01-04
	 */
	public void updateYearlyFinancialStatement() {
		FinanYearlyStateControler.state = "Update Stocks Yearly Financial Statement";
		FinanYearlyStateControler.current_count = 0;
		Date date = new Date();
		Configuration.writeFinancialLog("All Stocks", date, "\r\n\n************************************\r\nStart update Yearly Financial Statement.\r\n************************************\r\n");
		List<Security> seList = securityManager.getSecuritiesByType(5);
		// List<String> a=securityManager.getAllStocks();
		FinanYearlyStateControler.total_count = seList.size();
		for (int i = 0; i < seList.size(); i++) {

			fsm.downloadYearlyFinancialStatement(seList.get(i).getSymbol());
			// System.out.println(seList.get(i).getSymbol()+" is done."+FinanYearlyStateControler.current_count);

			DailyUpdateControler.current_count = i + 1;
			FinanYearlyStateControler.current_count = i + 1;
			// fsm.downloadFinancialStatement(seList.get(i).getSymbol(), date);
		}
		Configuration.writeFinancialLog("All Stocks", date, "\r\n\n************************************\r\n FinancialStatement checking time starting...\r\n************************************\r\n");
		Set<String> symbolSet = new HashSet<String>();
		// symbolSet=fsm.chkBICStatement(securityManager, a);//完全封装方法
		String[] type = { "balancestatement_yearly", "cashflow_yearly", "incomestatement_yearly" };
		String symbol = null;
		FinanYearlyStateControler.state = "Check Stocks Yearly Financial Statement";
		FinanYearlyStateControler.current_count = 0;
		FinanYearlyStateControler.total_count = seList.size() * 3;
		for (int i = 0; i < type.length; i++)
			for (int j = 0; j < seList.size(); j++) {
				symbol = fsm.chkYBICStatement(seList.get(i).getSymbol(), type[i]);
				FinanYearlyStateControler.current_count++;
				if (symbol != null) {
					symbolSet.add(symbol);
					System.out.println(symbol + ": has checked!");
				} else
					continue;
			}
		String[] symList = (String[]) symbolSet.toArray(new String[symbolSet.size()]);
		Configuration.writeFinancialLog("Some Stocks", date, "\r\n\n************************************\r\n FinancialStatement start make up...\r\n************************************\r\n");
		FinanYearlyStateControler.state = "Make up Stocks Yearly Financial Statement";
		FinanYearlyStateControler.current_count = 0;
		FinanYearlyStateControler.total_count = symList.length;
		for (int i = 0; i < symList.length; i++) {
			fsm.downloadYearlyFinancialStatement(symList[i]);
			FinanYearlyStateControler.current_count = i + 1;
		}
	}

	public void doSomeOtherUpdateJob() {
		// securityManager.getAllSecurityMPT(cur,true);
		Date date = new Date();

		/* update Relative Strength every month end */
		DailyUpdateControler.state = "Update Relative Strength Data";
		this.writeLog("All Stocks", date, "\n************************************\nStart update Relative Strength data.\n************************************\n");
		if (LTIDate.isLastNYSETradingDayOfMonth(date))
			securityManager.getAllStockRS(date, true);
		else {
			DailyUpdateControler.total_count = 1;
			DailyUpdateControler.current_count = 1;
		}
		this.writeLog("Shiller S&P500", date, "\n************************************\nStart update Shiller S&P500 data.\n************************************\n");
		this.downLoadAndUpdateShillerSP();
	}

	/*
	 * Interface: get the Update Option
	 */
	public int getUpdateOption() {
		String option = Configuration.getLanguage();
		if (option.equalsIgnoreCase(Configuration.LANGUAGE_US)) {
			return this.UPDATE_INTERNATIONAL;
		} else if (option.equalsIgnoreCase(Configuration.LANGUAGE_CN)) {
			return this.UPDATE_CHINA;
		}

		else
			return this.UPDATE_INTERNATIONAL;
	}

	/********************************************************************************************************************************/
	/********************************************************************************************************************************/

	// function: save all useful economic Indicators to local document
	public void saveEIToDoc(String filename) {
		try {
			String path = systemPath + filename;

			IndicatorManager indicatorManager;
			indicatorManager = (IndicatorManager) ContextHolder.getInstance().getApplicationContext().getBean("indicatorManager");
			List<Indicator> indicatorList = indicatorManager.getList();

			BufferedWriter buff = new BufferedWriter(new FileWriter(path));
			for (int i = 0; i < indicatorList.size(); i++) {
				// buff.write(arg0);
				Indicator tempIndicator = indicatorList.get(i);
				String name = tempIndicator.getSymbol();
				String dis = tempIndicator.getDescription();
				buff.write(name + "\t\t" + dis + "\n");
			}
			buff.flush();
			buff.close();
			// System.out.println("Save to Document Success");
		} catch (Exception e) {

			log.debug("Error: " + e);
			e.printStackTrace();
		}
	}

	// function: load all economic(attributes) indicators from local to data
	// base
	public void batchLoadEcon(String file) {
		try {
			boolean isNewIndicator;
			IndicatorManager indicatorManager;
			indicatorManager = (IndicatorManager) ContextHolder.getInstance().getApplicationContext().getBean("indicatorManager");

			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);

			String line = "";
			line = br.readLine();

			while (line != null) {
				if (line.indexOf("\\") < 0) {
					line = br.readLine();
					continue;
				}
				isNewIndicator = false;
				line.trim();
				String[] s = line.split(".csv");
				String[] k = s[0].replaceAll("\\\\", "/").split("/");
				String name = k[k.length - 1].trim();
				String dis = s[1].trim();

				Indicator indicator;
				indicator = indicatorManager.get(name);
				if (indicator == null) {
					indicator = new Indicator();
					indicator.setDescription(dis);
					indicator.setSymbol(name);
					isNewIndicator = true;
					// System.out.println(name);
				}

				if (isNewIndicator) {
					indicatorManager.add(indicator);
				} else {
					indicator.setDescription(dis);
					indicator.setSymbol(name);
					indicatorManager.update(indicator);
				}
				line = br.readLine();
			}
			// System.out.println("Success.");

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	// function: batch load all economic daily data from local files to data
	// base
	// rewrite the old ones
	public void loadEcomToDB(String path) {
		try {

			String ecomName;

			File rootFile = new File(path.toString());
			File[] files = rootFile.listFiles();

			int i, sum;
			sum = files.length;

			for (i = 0; i < sum; i++) {
				if (files[i].isFile()) {
					String[] tmp = files[i].getName().split(".csv");
					ecomName = tmp[0];
					try {
						dataManager.updateEcom(files[i].toString(), ecomName);
					} catch (Exception e) {

						log.debug("Error: " + e);
						e.printStackTrace();
					}
				} else {
					loadEcomToDB(path + "/" + files[i].getName());
				}
			}
			// System.out.println("UpLoad Done");

		} catch (Exception e) {
			log.debug("Error: " + e);
		}
	}

	// do not manage the old ones
	public void UpdateIndicatorToDB(String path) {

		try {

			String ecomName;

			File rootFile = new File(path.toString());
			File[] files = rootFile.listFiles();

			int i, sum;
			sum = files.length;

			for (i = 0; i < sum; i++) {
				if (files[i].isFile()) {
					String[] tmp = files[i].getName().split(".csv");
					ecomName = tmp[0];

					try {
						dataManager.updateEcom2(files[i].toString(), ecomName);
						// System.out.println("Done one");
					} catch (Exception e) {

						log.debug("Error: " + e);
						e.printStackTrace();
					}
				} else {
					UpdateIndicatorToDB(path + "/" + files[i].getName());
				}
			}
			// System.out.println("UpLoad Done");

		} catch (Exception e) {
			log.debug("Error: " + e);
		}
	}

	public Date getNewIndicatorUpdate() {
		try {
			String strUrl = "http://research.stlouisfed.org/fred2/downloaddata";
			String strName = this.systemPath + "/indicatorHTML.txt";

			URL url = new URL(strUrl);
			URLConnection urlConnection = (URLConnection) url.openConnection();
			urlConnection.setAllowUserInteraction(false);

			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			String inputLine;
			StringBuffer tempHtml = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				tempHtml.append(inputLine + "\n");
			}

			BufferedWriter buff = new BufferedWriter(new FileWriter(strName));
			for (int i = 0; i < tempHtml.length(); i++)
				buff.write(tempHtml.charAt(i));
			buff.flush();
			buff.close();

			// System.out.println(strName+" Success.");

			FileReader fr = new FileReader(strName);
			BufferedReader br = new BufferedReader(fr);

			String line = "";
			int time = 0;
			Date date = null;
			while (true) {
				line = br.readLine();
				if (line == null)
					break;

				if (time == 2) {
					int index = 0;
					date = new Date();
					date = LTIDate.clearHMSM(date);
					for (int i = 0; i < line.length(); i++) {
						if (line.charAt(i) == '-') {
							if (index == 0) {
								String year = line.substring(i - 4, i);
								date.setYear(Integer.parseInt(year) - 1900);
								index++;
							} else if (index == 1) {
								String month = line.substring(i - 2, i);
								date.setMonth(Integer.parseInt(month) - 1);

								String day = line.substring(i + 1, i + 3);
								date.setDate(Integer.parseInt(day));
								index++;
							}
						}
					}
					break;
				}

				if (line.indexOf("<td bgcolor=\"#f6f6f6\">") != -1) {
					time++;
				}

			}
			return date;
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void checkIndicatorUpdate() {
		Date today = new Date();
		if (today.getDay() != 1)
			return;

		Date date = this.getNewIndicatorUpdate();

		Date lastDate = this.getIndicatorLastDate();

		if (lastDate == null || date == null || lastDate.before(date)) {
			this.setIndicatorLastDate(date);
			this.updateIndicator();
		}

	}

	public Date getIndicatorLastDate() {
		Date date = new Date();
		date = LTIDate.clearHMSM(date);

		String strName = this.systemPath + "indicatorDate.txt";
		try {
			FileReader fr = new FileReader(strName);
			BufferedReader br = new BufferedReader(fr);

			String line = "";
			line = br.readLine();
			date.setYear(Integer.parseInt(line) - 1900);
			line = br.readLine();
			date.setMonth(Integer.parseInt(line) - 1);
			line = br.readLine();
			date.setDate(Integer.parseInt(line));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Get Indicator Last UpDate date Error");
			return null;
		}

		return date;
	}

	public void setIndicatorLastDate(Date date) {
		String strName = this.systemPath + "indicatorDate.txt";
		try {
			BufferedWriter buff = new BufferedWriter(new FileWriter(strName));

			int ss = date.getYear() + 1900;
			String s = Integer.toString(ss);
			buff.write(s + "\r\n");
			ss = date.getMonth() + 1;
			s = Integer.toString(ss);
			buff.write(s + "\r\n");
			ss = date.getDate() + 1;
			s = Integer.toString(ss);
			buff.write(s + "\r\n");

			buff.flush();
			buff.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void downLoadIndicator() {
		try {
			this.downloadCBOEIndicator();

			String strUrl = "http://research.stlouisfed.org/fred2/downloaddata/FRED2_csv_2.zip";
			String strName = this.systemPath + "/FRED2_csv_2.zip";

			URL url = new URL(strUrl);
			URLConnection urlConnection = (URLConnection) url.openConnection();
			urlConnection.setAllowUserInteraction(false);

			urlConnection.connect();

			FileOutputStream fos = null;
			BufferedInputStream bis = null;

			bis = new BufferedInputStream(urlConnection.getInputStream());
			fos = new FileOutputStream(strName);

			System.out.println("Getting URL and Downloading the file...");

			int size = 0;
			byte[] buf = new byte[BUFFER_SIZE];

			while ((size = bis.read(buf)) != -1)
				fos.write(buf, 0, size);

			fos.close();
			bis.close();

			// System.out.println(strName+" Success.");

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// unzip
	public void zipExtracting(String filePath, String zipFile) {
		int BUFFER = 2048;
		BufferedOutputStream dest = null;
		BufferedInputStream is = null;
		ZipEntry entry = null;
		ZipFile zipfile = null;
		Enumeration enu = null;
		try {
			zipfile = new ZipFile(zipFile);
			enu = zipfile.entries();
			while (enu.hasMoreElements()) {
				entry = (ZipEntry) enu.nextElement();
				if (entry.isDirectory()) {
					new File(filePath + entry.getName()).mkdirs();
					continue;
				}
				is = new BufferedInputStream(zipfile.getInputStream(entry));
				int count;
				byte data[] = new byte[BUFFER];
				FileOutputStream fos = new FileOutputStream(filePath + entry.getName());
				dest = new BufferedOutputStream(fos, BUFFER);
				while ((count = is.read(data, 0, BUFFER)) != -1) {
					dest.write(data, 0, count);
				}
				dest.flush();
				dest.close();
				is.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (dest != null) {
					dest.flush();
					dest.close();
				}
				if (is != null) {
					is.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void updateIndicator() {
		this.downLoadIndicator();
		this.zipExtracting(this.systemPath, this.systemPath + "/FRED2_csv_2.zip");
		this.batchLoadEcon(this.systemPath + "/FRED2_csv_2/README_SERIES_ID_SORT.txt");
		this.UpdateIndicatorToDB(this.systemPath + "/FRED2_csv_2/");

		this.loadCBOEIndicator(this.systemPath + "totalpc.csv");
		this.loadCBOEIndicator(this.systemPath + "indexPC.csv");
		this.loadCBOEIndicator(this.systemPath + "equityPC.csv");

		/************* Update the Indicator SPDIVIDEND *********************/
		try {
			dataManager.UpdateSP500Dividend(this.systemPath + "spdatas/" + "SPData.xls");
			System.out.println("Indicator SPDIVIDEND is updated!");
		} catch (Exception e) {

		}

		File file = new File(this.systemPath + "/FRED2_csv_2/");
		file.deleteOnExit();

	}

	/********************************************************************************************************************************/
	/********************************************************************************************************************************/

	// function: change the format such as "Tue, 01 Jan 2008" to standard
	// structure of date
	public Date getFormatDate(String str) {
		Date date;
		String[] s = str.split(",");
		String[] detail = s[1].split(" ");
		String strDate = detail[3];
		if (detail[2].equalsIgnoreCase("Jan"))
			strDate += "-01-";
		else if (detail[2].equalsIgnoreCase("Feb"))
			strDate += "-02-";
		else if (detail[2].equalsIgnoreCase("Mar"))
			strDate += "-03-";
		else if (detail[2].equalsIgnoreCase("Apr"))
			strDate += "-04-";
		else if (detail[2].equalsIgnoreCase("May"))
			strDate += "-05-";
		else if (detail[2].equalsIgnoreCase("Jun"))
			strDate += "-06-";
		else if (detail[2].equalsIgnoreCase("Jul"))
			strDate += "-07-";
		else if (detail[2].equalsIgnoreCase("Aug"))
			strDate += "-08-";
		else if (detail[2].equalsIgnoreCase("Sep"))
			strDate += "-09-";
		else if (detail[2].equalsIgnoreCase("Oct"))
			strDate += "-10-";
		else if (detail[2].equalsIgnoreCase("Nov"))
			strDate += "-11-";
		else if (detail[2].equalsIgnoreCase("Dec"))
			strDate += "-12-";

		strDate += detail[1];

		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			date = (Date) sf.parseObject(strDate);
			return date;
		} catch (ParseException e) {

			e.printStackTrace();
		}
		return null;
	}

	// function: load all holidays data from local file to data base
	public int loadHolidayTODB(String filename) {
		Date today = new Date();
		int year = today.getYear();

		try {
			boolean isNewHoliday;
			HolidayManager holidayManager;
			holidayManager = (HolidayManager) ContextHolder.getInstance().getApplicationContext().getBean("holidayManager");

			FileReader fr = new FileReader(filename);
			BufferedReader br = new BufferedReader(fr);

			String line = "";
			line = br.readLine();
			line = br.readLine();

			while (line != null) {
				if (line.length() == 0) {
					line = br.readLine();
					continue;
				}
				isNewHoliday = false;
				line.trim();
				String[] s = line.split(",");
				// if we change the input format of the date, then we should not
				// use this function any more.
				// Date date = getFormatDate(s[0]);
				// format like "yyyy-mm-dd:
				Date date = LTIDate.parseStringToDate(s[0]);
				int tmpYear = date.getYear();
				if (tmpYear > year)
					year = tmpYear;

				String name = s[1].trim();

				Holiday holiday;
				holiday = holidayManager.get(date);
				if (holiday == null) {
					holiday = new Holiday();
					holiday.setDescription("");
					holiday.setSymbol(name);
					holiday.setDate(date);
					isNewHoliday = true;
				}
				if (isNewHoliday) {
					holidayManager.save(holiday);
				} else {
					holiday.setDescription("");
					holiday.setSymbol(name);
					holiday.setDate(date);
					holidayManager.update(holiday);
				}
				// System.out.println(date+" "+name+" success.");
				line = br.readLine();
			}
			// System.out.println("All Success.");

		} catch (Exception e) {

			e.printStackTrace();
		}
		return year;
	}

	public void uploadHolidayAndSetTradingDate(String file) {
		HolidayManager hm = (HolidayManager) ContextHolder.getInstance().getApplicationContext().getBean("holidayManager");

		List<Holiday> hlist = hm.getHolidays();

		Date startdate = hlist.get(hlist.size() - 1).getDate();
		startdate = LTIDate.add(startdate, 1);

		int year = this.loadHolidayTODB(file);

		Date today = LTIDate.getDate(year + 1900 + 1, 1, 1);

		while (startdate.before(today)) {
			if (hm.isHoliday(startdate)) {
				startdate = LTIDate.add(startdate, 1);
				continue;
			}
			if (!LTIDate.isWeekDay(startdate)) {
				startdate = LTIDate.add(startdate, 1);
				continue;
			}
			TradingDate td = new TradingDate();
			td.setTradingDate(startdate);
			hm.saveTradingDate(td);
			startdate = LTIDate.add(startdate, 1);
		}
	}

	// public void uploadPortfolioUpdateMode(String filename){
	// PortfolioManager pm = (PortfolioManager)
	// ContextHolder.getInstance().getApplicationContext().getBean("portfolioManager");
	// List<Portfolio> pl = new ArrayList<Portfolio>();
	// CsvListReader clr = null;
	// try{
	// File file = new File(filename);
	// clr = new CsvListReader(new FileReader(file),
	// CsvPreference.EXCEL_PREFERENCE);
	// List<String[]> content = new ArrayList<String[]>();
	// List<String> line = new ArrayList<String>();
	// clr.getCSVHeader(true);
	// while((line = clr.read())!=null){
	// content.add(line.toArray(new String[]{}));
	// }
	// if(content.size()>0){
	// for(int i=0;i<content.size();++i){
	// try{
	// String[] c =content.get(i);
	// if(c.length!=4)
	// continue;
	// Long id = Long.parseLong(c[0]);
	// Portfolio p = pm.get(id);
	// if(p!=null){
	// Integer mode = Integer.parseInt(c[2]);
	// if(0<mode){
	// Portfolio pp ;
	// if(p.getIsOriginalPortfolio() == false)
	// pp = pm.get(p.getOriginalPortfolioID());
	// else
	// pp = pm.get(p.getPortfolioID());
	// if(pp!=null && !pp.getUpdateMode().equals(mode)){
	// pp.setUpdateMode(mode);
	// pm.update(pp);
	// }
	// if(!p.getUpdateMode().equals(mode)){
	// p.setUpdateMode(mode);
	// pm.update(p);
	// }
	// }
	// }
	// }catch(Exception e){
	// e.printStackTrace();
	// continue;
	// }
	// }
	// }
	// clr.close();
	// }catch(Exception e){
	// if(clr!=null)
	// try {
	// clr.close();
	// } catch (IOException e1) {
	// }
	// }
	// }
	// public void savePortfolioUpdateModeToFile(String filename, int
	// updateMode){
	// CsvListWriter clw = null;
	// try {
	// PortfolioManager pm = (PortfolioManager)
	// ContextHolder.getInstance().getApplicationContext().getBean("portfolioManager");
	// List<Portfolio> pl = null;
	// if(updateMode != 0)
	// pl = pm.getPortfolios(updateMode);
	// else
	// pl = pm.getAllPortfolios();
	// File file = new File(filename);
	// clw = new CsvListWriter(new FileWriter(file),
	// CsvPreference.EXCEL_PREFERENCE);
	//
	// //BufferedWriter buff = new BufferedWriter(new FileWriter(filename));
	// String[] header =
	// {"PortfolioID","PortfolioName","UpdateMode","UserName"};
	//
	// clw.writeHeader(header);
	// if( pl != null && pl.size()>0){
	// for(int i=0;i<pl.size();i++)
	// {
	// Portfolio p = pl.get(i);
	// List<String> c = new ArrayList<String>();
	// c.add(p.getID().toString());
	// c.add(p.getName());
	// c.add(p.getUpdateMode().toString());
	// if(p.getUserName()!=null)
	// c.add(p.getUserName());
	// else
	// c.add("NULL");
	// clw.write(c);
	// }
	// }
	// clw.close();
	// } catch (Exception e) {
	// log.debug("Error: "+e);
	// e.printStackTrace();
	// }finally{
	// if(clw != null)
	// try {
	// clw.close();
	// } catch (IOException e) {
	// }
	// }
	// }
	// function: load all holidays data from data base to local file
	public void saveHolidayToFile(String filename) {
		try {
			String path = systemPath;

			HolidayManager holidayManager;
			holidayManager = (HolidayManager) ContextHolder.getInstance().getApplicationContext().getBean("holidayManager");
			List<Holiday> holidayList = holidayManager.getHolidays();

			BufferedWriter buff = new BufferedWriter(new FileWriter(filename));
			buff.write("Date,Description,\n");
			for (int i = 0; i < holidayList.size(); i++) {
				// buff.write(arg0);
				Holiday tempHoliday = holidayList.get(i);
				Date date = tempHoliday.getDate();
				String name = tempHoliday.getSymbol();
				// String dis = tempHoliday.getDescription();
				buff.write(date + "," + name + ",\n");
			}
			buff.flush();
			buff.close();
			// System.out.println("Save to Document Success:"+path);
		} catch (Exception e) {

			log.debug("Error: " + e);
			e.printStackTrace();
		}
	}

	/********************************************************************************************************************************/
	/********************************************************************************************************************************/

	/*
	 * get a hash map of cef
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, String> getCEFMap(String filename) {
		HashMap<String, String> map = new HashMap<String, String>();

		try {
			FileReader fr = new FileReader(filename);
			BufferedReader br = new BufferedReader(fr);

			String line = "";
			line = br.readLine();
			while (line != null) {
				String[] str = line.trim().split(" ");
				String[] str2 = str[0].trim().split("=");
				String num = str2[1];
				String name = str[1];
				map.put(name, num);
				line = br.readLine();
			}
			// System.out.println("Done");
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

		return map;
	}

	public void saveCEFToDoc(String filename) {
		try {
			HashMap<String, String> map = this.getCEFMap(filename);
			List<Security> list = securityManager.getSecurities();
			// list = list.subList(186, list.size()-1);
			for (int i = 0; i < list.size(); i++) {
				Security tmpSecurity = list.get(i);

				if (tmpSecurity.getSecurityType() != 2)
					continue;

				String name = this.getTrueString(tmpSecurity.getSymbol());
				String num = (String) map.get(name);
				if (num == null)
					continue;
				String strUrl = "http://www.etfconnect.com/select/fundpages/displayChartData.asp?name=Neuberger+Berman+Income+Opportunity+Fund&ticker=" + name + "&MFID=" + num + "&hdrtxt=Share+Price+and+NAV+History&qsChart=type%3DETFCv2%26id%3D114710%26w%3D500px%26h%3D200px%26dtatyp%3Dsharenavall&dtatyp=sharenavall";
				String strName;
				if (this.isLinux())
					strName = systemPath + "html/" + name;
				else
					strName = systemPath + "html\\" + name;

				URL url = new URL(strUrl);
				URLConnection urlConnection = (URLConnection) url.openConnection();
				urlConnection.setAllowUserInteraction(false);

				BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
				String inputLine;
				StringBuffer tempHtml = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					tempHtml.append(inputLine + "\n");
				}

				BufferedWriter buff = new BufferedWriter(new FileWriter(strName));
				for (int j = 0; j < tempHtml.length(); j++)
					buff.write(tempHtml.charAt(j));
				buff.flush();
				buff.close();

				// System.out.println(name+" "+strName);

				// System.out.println(strName+" Success.");
			}
		} catch (MalformedURLException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public void uploadOneNAV(String symbol, String fileName) {
		try {
			if (fileName != null) {
				loadToDB3(symbol, fileName);
				Date cur = new Date();
				this.writeLog(symbol, cur, "upload One Security's NAV Success");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.debug("Error: " + e);
		}
	}

	public void uploadNAV(boolean update) {
		/**** part 5:upload nav ***/

		String path = systemPath;
		if (update)
			path = systemPath + "update/";
		if (isLinux())
			path += "nav/";
		else
			path += "nav//";

		String currentFile, securityName = "";

		File rootFile = new File(path.toString());
		File[] files = rootFile.listFiles();

		int i, sum;
		sum = files.length;

		DailyUpdateControler.total_count = sum;
		int part5_success_count = 0;
		int part5_fail_count = 0;
		for (i = 0; i < sum; i++) {
			try {
				DailyUpdateControler.current_count = i + 1;
				if (files[i].isFile()) {
					currentFile = files[i].toString();
					securityName = checkFile(files[i].getName());
					loadToDB3(securityName, currentFile);
					// System.out.println("Done one."+securityName);

					Date cur = new Date();
					this.writeLog(securityName, cur, "upload One Security's NAV Success");
					part5_success_count++;
				}
			} catch (Exception e) {
				this.writeLog(securityName + ":Error", new Date(), "upload One Security's NAV Failed");
				part5_fail_count++;
			}
		}
		// System.out.println("UpLoad Done");

		Date cur = new Date();
		// this.writeLog("All NAV", cur, "Load NAV Success");
		part5EndTime = System.currentTimeMillis();
		this.writeLog("ALL Security UpDate part 5 End", cur, "\n************************************\n Success count:" + part5_success_count + ";Error count:" + part5_fail_count + " \n************************************\n");
	}

	public String parseHtml(String name, String path, boolean update) throws IOException {
		int flag = 0;
		String file = systemPath;
		if (update)
			file = systemPath + "update/";
		if (isLinux())
			file += "nav/" + name;
		else
			file += "nav\\" + name;
		BufferedWriter buff = new BufferedWriter(new FileWriter(file));
		try {
			FileReader fr = new FileReader(path);
			BufferedReader br = new BufferedReader(fr);
			NAVHtmlTokenizer ht = new NAVHtmlTokenizer(br);
			while (true) {
				int temp = ht.nextHtml();
				if (temp == NAVHtmlTokenizer.HTML_EOF)
					break;
				if (temp == NAVHtmlTokenizer.HTML_DATA) {
					flag++;
					ht.nextHtml();
					if (flag == 1) {
						String date = ht.sval;
						String[] s = date.trim().split("/");
						date = s[2] + "-" + s[0] + "-" + s[1];
						buff.write(date);
						// System.out.print(date);
					} else if (flag == 3) {
						flag = 0;
						String NAV = ht.sval;
						NAV = NAV.substring(1, NAV.length() - 1);
						buff.write("," + NAV + "\n");
						// System.out.println(","+NAV);
					}
				}
			}
			buff.flush();
			buff.close();
			// System.out.println("Done.");
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
		return file;
	}

	private void loadToDB3(String securityName, String currentFile) throws Exception {
		try {
			// String file = parseHtml(securityName,currentFile);
			// dataManager.UpdateNAV(currentFile, securityName);
			dataManager.UpdateNAV2(currentFile, securityName);
		} catch (Exception e) {

			log.debug("Error: " + e);
			e.printStackTrace();
			throw e;
		}
	}

	public void getAllAdjNAV(boolean update) {
		try {
			String path = systemPath;

			if (update)
				path += "update/";

			if (isLinux())
				path += "nav/";
			else
				path += "nav//";

			String securityName;

			File rootFile = new File(path.toString());
			File[] files = rootFile.listFiles();

			int i, sum;
			sum = files.length;

			for (i = 0; i < sum; i++) {
				if (files[i].isFile()) {
					securityName = checkFile(files[i].getName());
					// System.out.println("Start:"+securityName);
					dataManager.UpdateAdjNAV(securityName);
					// System.out.println(i+" Done one."+securityName);

					Date cur = new Date();
					this.writeLog(securityName, cur, "Get one NAV Success");
				}
			}
			// System.out.println("Update AdjNAV Done");

			Date cur = new Date();
			this.writeLog("All NAV", cur, "Get All NAV Success");
		} catch (Exception e) {
			log.debug("Error: " + e);

		}
	}

	public void updateAdjNAV(List<Date> endDateList) {
		// System.out.println("Start Update Adjust NAV");
		try {
			List<Security> securityList = new ArrayList<Security>();
			securityList = securityManager.getSecurities();

			int num = 0;

			for (int i = 0; i < securityList.size(); i++) {//

				if (securityList.get(i).getSecurityType() != 2)
					continue;

				String securityName = securityList.get(i).getSymbol();

				if (securityName.equalsIgnoreCase("CASH") || securityName.equalsIgnoreCase("CN CASH"))
					continue;

				Date date = endDateList.get(num);

				num++;

				// System.out.println("Start:"+securityName);

				dataManager.UpdateAdjNAV(securityName, date);

				// System.out.println(i+"Done one."+securityName);

				Date cur = new Date();
				this.writeLog(securityName, cur, "Update one NAV Success");
			}
			// System.out.println("Up date Adjust NAV Done!");

			Date cur = new Date();
			this.writeLog("All NAV", cur, "UpDate All Adjust NAV Success");
		} catch (Exception e) {
			e.printStackTrace();
			log.debug("Error: " + e);
		}
	}

	public void updateOneAdjNAV(String symbol, Date endDate) {
		if (symbol.equalsIgnoreCase("CASH") || symbol.equalsIgnoreCase("CN CASH"))
			return;
		try {
			dataManager.UpdateAdjNAV(symbol, endDate);
			Date cur = new Date();
			this.writeLog(symbol, cur, "Update one NAV Success");
		} catch (Exception e) {
			e.printStackTrace();
			log.debug("Error: " + e);
		}

	}

	public void updateAdjNAV(List<String> seList, List<Date> endDateList) {
		/**** part 6:update adj nav ***/
		// System.out.println("Start Update Adjust NAV");

		int num = 0;
		int part6_success_count = 0;
		int part6_fail_count = 0;
		DailyUpdateControler.total_count = seList.size();
		int sum = seList.size();
		for (int i = 0; i < sum; i++) {// seList.size()
			String securityName = "";
			try {
				DailyUpdateControler.current_count = i + 1;
				securityName = seList.get(i);

				if (securityName.equalsIgnoreCase("CASH") || securityName.equalsIgnoreCase("CN CASH"))
					continue;

				Date date = endDateList.get(num);

				num++;

				// System.out.println("Start:"+securityName);

				dataManager.UpdateAdjNAV(securityName, date);

				// System.out.println(i+"Done one."+securityName);

				Date cur = new Date();
				this.writeLog(securityName, cur, "Update one NAV Success");
				part6_success_count++;
			} catch (Exception e) {
				this.writeLog(securityName + "Error", new Date(), "Update one NAV Failed");
				part6_fail_count++;
			}
		}
		// System.out.println("Up date Adjust NAV Done!");

		Date cur = new Date();
		// this.writeLog("All NAV", cur, "Update All Adjust NAV Success");
		part6EndTime = System.currentTimeMillis();
		this.writeLog("ALL Security UpDate part 6 End", cur, "\n************************************\n Success count:" + part6_success_count + ";Error count:" + part6_fail_count + " \n************************************\n");
	}

	/********************************************************************************************************************************/
	/********************************************************************************************************************************/

	public void updateAdjust(List<Date> endDateList, List<SecurityDailyData> firstDataList, List<SecurityDailyData> staticDataList) {
		try {
			List<Security> securityList = new ArrayList<Security>();
			securityList = securityManager.getSecurities();
			for (int i = 0; i < securityList.size(); i++) {// securityList.size()

				String securityName = securityList.get(i).getSymbol();

				if (securityName.equalsIgnoreCase("CASH") || securityName.equalsIgnoreCase("CN CASH"))
					continue;

				Date date = endDateList.get(i);
				SecurityDailyData sd1 = firstDataList.get(i);
				SecurityDailyData sd2 = staticDataList.get(i);

				// System.out.println("Start:"+securityName);

				dataManager.UpdateAdjClose(securityName, date, sd1, sd2);

				// System.out.println(i+"Done one."+securityName);

				Date cur = new Date();
				this.writeLog(securityName, cur, "Adjust one Security adjust close Success");
			}
			// System.out.println("Up date AdjClose Done!");

			Date cur = new Date();
			this.writeLog("All security AdjustClose", cur, "Update All Security adujustClose Success");
		} catch (Exception e) {
			e.printStackTrace();
			log.debug("Error: " + e);
		}
	}

	public void updateAdjustSecurity(String symbol, Date endDate, SecurityDailyData firstData, SecurityDailyData staticData) {
		try {
			dataManager.UpdateAdjClose(symbol, endDate, firstData, staticData);
			Date cur = new Date();
			this.writeLog(symbol, cur, "Adjust one Security adjust close Success");
		} catch (Exception e) {
			e.printStackTrace();
			log.debug("Error: " + e);
		}
	}

	public void updateAdjust(List<String> seList, List<Date> endDateList, List<SecurityDailyData> firstDataList, List<SecurityDailyData> staticDataList) {
		try {
			for (int i = 0; i < seList.size(); i++) {// seList.size()

				String securityName = seList.get(i);

				Date date = endDateList.get(i);
				SecurityDailyData sd1 = firstDataList.get(i);
				SecurityDailyData sd2 = staticDataList.get(i);

				// System.out.println("Start:"+securityName);

				dataManager.UpdateAdjClose(securityName, date, sd1, sd2);

				// System.out.println(i+" Done one."+securityName);

				Date cur = new Date();
				this.writeLog(securityName, cur, "Adjust one Security adjust close Success");
			}
			// System.out.println("Up date AdjClose Done!");

			Date cur = new Date();
			this.writeLog("All security AdjustClose", cur, "Update All Security adujustClose Success");
		} catch (Exception e) {
			e.printStackTrace();
			log.debug("Error: " + e);
		}
	}

	/********************************************************************************************************************************/
	/********************************************************************************************************************************/

	/*
	 * build up CEF daily data (old methord)
	 */
	public void buildupCEFWithOldSource() {
		File rootFile = new File(systemPath + "nav");
		if (!rootFile.isDirectory())
			System.out.println(rootFile.mkdir());

		File[] files = rootFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			files[i].delete();
		}

		rootFile = new File(systemPath + "html");
		if (!rootFile.isDirectory())
			rootFile.mkdir();

		files = rootFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			files[i].delete();
		}

		String path = systemPath;
		if (isLinux())
			path = systemPath + "ETFMap.txt";
		else
			path = systemPath + "ETFMap.txt";
		this.saveCEFToDoc(path);

		rootFile = new File(systemPath + "html");
		files = rootFile.listFiles();

		if (isLinux())
			path = systemPath + "html/";
		else
			path = systemPath + "html\\";

		for (int i = 0; i < files.length; i++) {
			String securityName = checkFile(files[i].getName());
			try {
				this.parseHtml(securityName, path + securityName, false);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		this.uploadNAV(false);
		this.getAllAdjNAV(false);
	}

	public void buildupCEF() {
		File rootFile = new File(systemPath + "nav");
		if (!rootFile.isDirectory())
			System.out.println(rootFile.mkdir());

		File[] files = rootFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			files[i].delete();
		}

		List<Security> securityList = new ArrayList<Security>();
		securityList = securityManager.getSecuritiesByType(2);

		Date startDate = LTIDate.getDate(1900, 1, 1);
		Date endDate = new Date();

		for (int i = 0; i < securityList.size(); i++) {
			String securityName = securityList.get(i).getSymbol();
			try {
				this.saveCEFToFile(securityName, startDate, endDate, "n", false);
				Thread.sleep(500);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		this.uploadNAV(false);
		this.getAllAdjNAV(false);
	}

	/*
	 * change the asset class of each cef security
	 */
	public void updateCEF(String file) {
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);

			String line = "";
			while (true) {
				line = br.readLine();
				if (line == null)
					break;
				String[] s = line.trim().split(",");
				String[] ss = s[0].trim().split("/");
				String name = ss[0].trim();
				String style = s[1].trim();
				Security security = securityManager.getBySymbol(name);
				if (security != null) {
					security.setClassID(Long.parseLong(style));
					securityManager.update(security);
					// System.out.println("success:"+name);
				} else
					System.out.println("fail:" + name);
			}

			// System.out.println("Update CEF Done");
		} catch (Exception e) {
			log.debug("Error: " + e);
		}

	}

	/********************************************************************************************************************************/
	/********************************************************************************************************************************/

	/*
	 * down load a portfolio's data to local document
	 */
	public void downLoadPortfolio(long id) throws Exception {
		BufferedWriter buff = new BufferedWriter(new FileWriter("C://a.txt"));
		PortfolioManager portfolioManager = (PortfolioManager) ContextHolder.getInstance().getApplicationContext().getBean("portfolioManager");
		List<PortfolioDailyData> aa = portfolioManager.getDailydatas(id);

		for (int i = 0; i < aa.size(); i++) {
			PortfolioDailyData p = aa.get(i);
			int year = p.getDate().getYear() + 1900;
			int month = p.getDate().getMonth() + 1;
			buff.write(year + "-" + month + "-" + p.getDate().getDate() + "," + p.getAmount() + ",\r\n");
		}
		buff.flush();
		buff.close();
	}

	/*
	 * down load a security's data to local document
	 */
	public void downLoadSecurity(long id) throws Exception {
		BufferedWriter buff = new BufferedWriter(new FileWriter("C://b.txt"));
		Interval in = new Interval();
		Date startDate = LTIDate.getDate(2006, 12, 29);
		Date endDate = LTIDate.getDate(2008, 1, 1);
		in.setStartDate(startDate);
		in.setEndDate(endDate);
		List<SecurityDailyData> aa = securityManager.getDailyDatas(id, in.getStartDate(), in.getEndDate());

		for (int i = 0; i < aa.size(); i++) {
			SecurityDailyData p = aa.get(i);
			int year = p.getDate().getYear() + 1900;
			int month = p.getDate().getMonth() + 1;
			buff.write(year + "-" + month + "-" + p.getDate().getDate() + "," + p.getAdjClose() + ",\r\n");
		}
		buff.flush();
		buff.close();
	}

	/********************************************************************************************************************************/
	/********************************************************************************************************************************/

	/*
	 * build up the security cash
	 */
	public void buildUpCash() {

		List<SecurityDailyData> irxList = securityManager.getDailydatas(securityManager.get("^IRX").getID(), true);
		double preValue = 1;
		long id = securityManager.get("CASH").getID();

		List<SecurityDailyData> sdds = new ArrayList<SecurityDailyData>();
		for (int i = 0; i < irxList.size(); i++) {
			SecurityDailyData sd = new SecurityDailyData();
			double irxPrice = irxList.get(i).getClose();
			double dividend = irxPrice / 36500;
			double adjPrice = preValue * (1 + dividend);
			preValue = adjPrice;
			Date date = irxList.get(i).getDate();
			sd.setSecurityID(id);
			date = LTIDate.clearHMSM(date);
			sd.setDate(date);
			sd.setOpen(1.0);
			sd.setClose(1.0);
			sd.setDividend(dividend);
			sd.setAdjClose(adjPrice);
			// securityManager.saveDailyData(sd);
			sdds.add(sd);
		}
		securityManager.saveOrUpdateAll(sdds);
		// System.out.println("Success build up cash");
	}

	public void buildUpCNCash() {

		List<SecurityDailyData> irxList = securityManager.getDailydatas(securityManager.get("050003.OF").getID(), true);
		double preValue = 1;
		long id = securityManager.get("CN CASH").getID();

		List<SecurityDailyData> sdds = new ArrayList<SecurityDailyData>();
		for (int i = 0; i < irxList.size(); i++) {
			SecurityDailyData sd = new SecurityDailyData();
			double irxPrice = irxList.get(i).getAdjClose();
			double dividend = irxPrice / 36500;
			double adjPrice = preValue * (1 + dividend);
			preValue = adjPrice;
			Date date = irxList.get(i).getDate();
			sd.setSecurityID(id);
			date = LTIDate.clearHMSM(date);
			sd.setDate(date);
			sd.setOpen(1.0);
			sd.setClose(1.0);
			sd.setDividend(dividend);
			sd.setAdjClose(adjPrice);
			// securityManager.saveDailyData(sd);
			sdds.add(sd);
		}
		securityManager.saveOrUpdateAll(sdds);
		// System.out.println("Success build up cn cash");
	}

	/**
	 * 通过newFund的securitydailydata来更新oldFund的securitydailydata
	 * 
	 * @param oldFund
	 * @param newFund
	 * @throws Exception
	 */
	public void updateSpecialFund(String oldFund, String newFund) throws Exception {
		Security se = securityManager.get(oldFund);
		Security newSe = securityManager.get(newFund);
		if (newSe == null)
			this.importOneSecurity(newFund, true, true);
		long oldFundID = se.getID();
		long newFundID = securityManager.get(newFund).getID();

		Date lastDate = securityManager.getDailyDataLastDate(oldFundID);

		if (lastDate == null) {
			Date newLastDate = securityManager.getStartDate(newFundID);
			if (newLastDate != null)
				lastDate = newLastDate;
		} else
			lastDate = LTIDate.getNewNYSETradingDay(lastDate, 1);
		Date today = LTIDate.clearHMSM(new Date());
		List<SecurityDailyData> sddList = securityManager.getDailyDatas(newFundID, lastDate, today);
		if (sddList != null && sddList.size() > 0) {
			for (SecurityDailyData sdd : sddList) {
				sdd.setID(null);
				sdd.setSecurityID(oldFundID);
			}
		}
		se.setDividendLastDate(newSe.getDividendLastDate());
		se.setStartDate(newSe.getStartDate());
		se.setNavLastDate(newSe.getNavLastDate());
		se.setEndDate(newSe.getEndDate());
		se.setPriceLastDate(newSe.getPriceLastDate());
		securityManager.saveAll(sddList);
		securityManager.update(se);
	}

	/**
	 * 
	 * @param TSPGFund
	 * @param source1
	 *            ^TNX
	 * @param source2
	 *            ^FVX
	 */
	public void updateTSPGFund(String TSPGFund, String source1, String source2, boolean flush) throws Exception {
		Security se = securityManager.get(TSPGFund);
		long TSPGFundID = se.getID();
		long sourceID1 = securityManager.get(source1).getID();
		long sourceID2 = securityManager.get(source2).getID();
		Date lastDate = securityManager.getDailyDataLastDate(TSPGFundID);
		double preValue = 1;
		Date curDate = new Date();
		if (lastDate == null) {
			Date lastDate1 = securityManager.getStartDate(sourceID1);
			if (lastDate1 != null)
				lastDate = lastDate1;
			Date lastDate2 = securityManager.getStartDate(sourceID2);
			if (lastDate2 != null && (lastDate == null || LTIDate.after(lastDate2, lastDate)))
				lastDate = lastDate2;
			curDate = lastDate;
		} else {
			curDate = LTIDate.getNewNYSETradingDay(lastDate, 1);
			preValue = securityManager.getAdjPrice(TSPGFundID, lastDate);
		}
		Date today = LTIDate.clearHMSM(new Date());
		List<SecurityDailyData> sdds = new ArrayList<SecurityDailyData>();
		while (curDate.before(today)) {
			curDate = LTIDate.clearHMSM(curDate);
			SecurityDailyData sd1 = securityManager.getDailydata(sourceID1, curDate, true);
			SecurityDailyData sd2 = securityManager.getDailydata(sourceID2, curDate, true);
			if (sd1 == null || sd2 == null) {
				// System.out.println("Fail at "+curDate);
				this.writeLog(TSPGFund, curDate, "fail...");
				curDate = LTIDate.getTomorrow(curDate);
				continue;
			} else {
				boolean newOne = false;
				double sourcePrice1 = sd1.getClose();
				double sourcePrice2 = sd1.getClose();
				double dividend = (sourcePrice1 + sourcePrice2) / 2 / 36500;// average
																			// price
				double adjPrice = preValue * (1 + dividend);
				preValue = adjPrice;
				SecurityDailyData newSd;
				if (flush) {
					newSd = new SecurityDailyData();
				} else {
					newSd = securityManager.getDailydata(TSPGFundID, curDate, true);
					if (newSd == null) {
						newSd = new SecurityDailyData();
						newOne = true;
					}
				}
				newSd.setSecurityID(TSPGFundID);
				curDate = LTIDate.clearHMSM(curDate);
				newSd.setDate(curDate);
				newSd.setClose(1.0);
				newSd.setOpen(1.0);
				newSd.setSplit(1.0);
				newSd.setDividend(dividend);
				newSd.setAdjClose(adjPrice);
				if (flush)
					sdds.add(newSd);
				else {
					if (newOne)
						securityManager.saveDailyData(newSd);
					else
						securityManager.updateDailyData(newSd);
				}
				this.writeLog(TSPGFund, curDate, "Update success");
				if (se.getEndDate() == null || LTIDate.after(curDate, se.getEndDate())) {
					se.setPriceLastDate(curDate);
					se.setEndDate(curDate);
				}
			}
			curDate = LTIDate.getTomorrow(curDate);
		}
		if (flush && sdds != null) {
			securityManager.deleteSecurityDataCascade(TSPGFundID);
			securityManager.saveAll(sdds);
		}
		securityManager.update(se);
		this.writeLog(TSPGFund, today, "Update + " + TSPGFund + " Success");
	}

	/*
	 * update the security cash
	 */
	public void updateCash(String cash, String source) throws Exception {

		long cashID = securityManager.get(cash).getID();
		long irxID = securityManager.get(source).getID();

		Date lastDate = securityManager.getDailyDataLastDate(cashID);

		double preValue = 1;
		Date curDate = new Date();

		if (lastDate == null) {
			lastDate = securityManager.getStartDate(irxID);
			preValue = 1;
			curDate = lastDate;
		} else {
			curDate = LTIDate.getNewNYSETradingDay(lastDate, 1);
			preValue = securityManager.getAdjPrice(cashID, lastDate);
		}
		Date today = new Date();
		while (curDate.before(today)) {
			// System.out.println(curDate);
			curDate = LTIDate.clearHMSM(curDate);
			SecurityDailyData sd = securityManager.getDailydata(irxID, curDate, true);
			if (sd == null) {
				// System.out.println("Fail at "+curDate);
				this.writeLog("Cash", curDate, "fail...");
				curDate = LTIDate.getTomorrow(curDate);
				continue;
			} else {
				boolean newOne = false;
				double irxPrice = sd.getClose();
				double dividend = irxPrice / 36500;
				double adjPrice = preValue * (1 + dividend);
				preValue = adjPrice;
				SecurityDailyData newSd;
				newSd = securityManager.getDailydata(cashID, curDate, true);
				if (newSd == null) {
					newSd = new SecurityDailyData();
					newOne = true;
				}
				newSd.setSecurityID(cashID);
				curDate = LTIDate.clearHMSM(curDate);
				newSd.setDate(curDate);
				newSd.setClose(1.0);
				newSd.setOpen(1.0);
				newSd.setSplit(1.0);
				newSd.setDividend(dividend);
				newSd.setAdjClose(adjPrice);
				if (newOne)
					securityManager.saveDailyData(newSd);
				else
					securityManager.updateDailyData(newSd);

				this.writeLog("Cash", curDate, "Update success");

				Security se = securityManager.get(cashID);
				if (LTIDate.after(curDate, se.getEndDate())) {
					se.setPriceLastDate(curDate);
					se.setEndDate(curDate);
					securityManager.update(se);
					this.writeLog("Cash", curDate, "set End Date success!");
				}
			}
			curDate = LTIDate.getTomorrow(curDate);
		}

		Date cur = new Date();
		this.writeLog("Cash", cur, "Update Cash Success");
	}

	/********************************************************************************************************************************/
	/********************************************************************************************************************************/

	public void setSplit() {
		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");

		List<Security> slist = securityManager.getSecurities();

		for (int i = 0; i < slist.size(); i++) {
			Security tmp = slist.get(i);

			List<SecurityDailyData> ttlist = new ArrayList<SecurityDailyData>();

			// System.out.println(tmp.getSymbol());

			// if(!tmp.getSymbol().equalsIgnoreCase("STHBX"))continue;

			List<SecurityDailyData> sdds = securityManager.getDailydatas(tmp.getID(), true);

			for (int j = 0; j < sdds.size(); j++) {
				SecurityDailyData sd = sdds.get(j);
				if (sd.getSplit() != null) {
					if (Math.abs(sd.getSplit() - 1.0) <= 0.05) {
						sd.setSplit(1.0);
					} else if (Math.abs(sd.getSplit() - 2.0) <= 0.05) {
						sd.setSplit(2.0);
					} else if (Math.abs(sd.getSplit() - 3.0) <= 0.05) {
						sd.setSplit(3.0);
					}
				} else {
					sd.setSplit(1.0);
				}

				if (j != 0) {
					SecurityDailyData pre = sdds.get(j - 1);
					sd.setAdjClose(sd.getClose() * sd.getSplit() * (1 + sd.getDividend() / (pre.getClose() - sd.getDividend())) * pre.getAdjClose() / pre.getClose());
				}

				ttlist.add(sd);
			}
			securityManager.saveOrUpdateAll(ttlist);

			// System.out.println(tmp.getSymbol()+" Done");
		}
	}

	public void checkSplit() {
		File rootFile = new File("checkSplit.html");
		rootFile.delete();
		/**** part 11:check split ****/
		try {
			FileOutputStream stream = new FileOutputStream("checkSplit.html", true);
			OutputStreamWriter writer = new OutputStreamWriter(stream);
			writer.write("Security with wrong Split:\n\n");

			SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");

			List<Security> slist = securityManager.getSecurities();

			Date cur = new Date();
			this.writeLog("Check Split Start", cur, "=====================");

			DailyUpdateControler.total_count = slist.size();

			for (int i = 0; i < slist.size(); i++) {
				DailyUpdateControler.current_count = i + 1;
				Security tmp = slist.get(i);
				try {
					if (tmp.getSymbol().indexOf(".OF") > 0)
						continue;
					if (tmp.getSymbol().indexOf(".CEF") > 0)
						continue;
					if (tmp.getManual() != null && tmp.getManual() == 1)
						continue;
					// System.out.println(tmp.getSymbol());

					List<SecurityDailyData> sdds = securityManager.getDailydatas(tmp.getID(), false);

					for (int j = 1; j < sdds.size(); j++) {
						SecurityDailyData sd = sdds.get(j);
						SecurityDailyData sdd = sdds.get(j - 1);

						if (sd.getClose() == null || sd.getSplit() == null)
							continue;
						if (sdd.getClose() == null || sdd.getSplit() == null)
							continue;

						if (sd.getDividend() == null)
							sd.setDividend(0.0);
						/*
						 * if(sd.getDividend() ==0.0 &&
						 * sdd.getClose()/sd.getClose() >=1.8 &&
						 * (Math.abs(sd.getSplit()-1) <= 0.1) ) {
						 * System.out.println
						 * ("Small Split: "+tmp.getSymbol()+"  "+sd.getSplit());
						 * System.out.println(sdd.getDate()+" "+sdd.getClose());
						 * System.out.println(sd.getDate()+" "+sd.getClose());
						 * System.out.println("====================");
						 * writer.write
						 * (tmp.getSymbol()+"  "+sdd.getDate()+"\n");
						 * 
						 * this.writeLog(tmp.getSymbol(), sd.getDate(),
						 * "Small Split"); break; }
						 * 
						 * if(sd.getSplit()!=null &&
						 * sdd.getClose()/sd.getClose() <=1.1 &&
						 * sd.getSplit()>=1.8 ) {
						 * System.out.println("Big Split: "
						 * +tmp.getSymbol()+"  "+sd.getSplit());
						 * System.out.println(sdd.getDate()+" "+sdd.getClose());
						 * System.out.println(sd.getDate()+" "+sd.getClose());
						 * System.out.println("====================");
						 * writer.write
						 * (tmp.getSymbol()+"  "+sdd.getDate()+"\n");
						 * 
						 * this.writeLog(tmp.getSymbol(), sd.getDate(),
						 * "Big Split"); break; }
						 * 
						 * if(LTIDate.equals(sd.getDate(),sdd.getDate())) {
						 * System.out.println("ERROR Date"+tmp.getSymbol());
						 * System.out.println(sdd.getDate()+" "+sdd.getClose());
						 * System.out.println(sd.getDate()+" "+sd.getClose());
						 * System.out.println("====================");
						 * writer.write
						 * (tmp.getSymbol()+"  "+sdd.getDate()+"\n");
						 * 
						 * this.writeLog(tmp.getSymbol(), sd.getDate(),
						 * "ERROR DATE"); break;
						 * 
						 * } /*double sda=sd.getAdjClose(); int len1=0;
						 * if(sda<10)len1=4; else if(sda<100)len1=5; else
						 * if(sda<1000)len1=6; else len1=7;
						 * if(j==sdds.size()-2)break; SecurityDailyData sd3 =
						 * sdds.get(j+1); SecurityDailyData sd4 = sdds.get(j+2);
						 * if(sd3.getAdjClose().toString().length()==len1
						 * &&sd4.getAdjClose().toString().length()==len1 &&
						 * sd.getAdjClose().toString().length()==len1 &&
						 * sdd.getAdjClose().toString().length()>len1+2) {
						 * System
						 * .out.println("Error Split: "+tmp.getSymbol()+"  "
						 * +sd.getSplit());
						 * System.out.println(sdd.getDate()+" "+
						 * sdd.getAdjClose());
						 * System.out.println(sd.getDate()+" "
						 * +sd.getAdjClose());
						 * System.out.println("===================="); //break;
						 * }*
						 */

						/*
						 * double todayDividend = 1 + sd.getDividend()
						 * /sd.getClose();
						 * 
						 * double totalDividend = todayDividend *
						 * sdd.getAdjClose() / sdd.getClose();
						 * 
						 * 
						 * double adjPrice = totalDividend * sd.getSplit() *
						 * sd.getClose();
						 */

						double todayDividend = 1 + sd.getDividend() / (sdd.getClose() - sd.getDividend());

						double totalDividend = todayDividend * sdd.getAdjClose();
						if (sdd.getClose() != 0)
							totalDividend /= sdd.getClose();

						double adjPrice = totalDividend * sd.getSplit() * sd.getClose();
						if (Math.abs(adjPrice - sd.getAdjClose()) > 1) {
							// System.out.println("Error Adjust Close: "+tmp.getSymbol()+"  "+sd.getSplit());
							// System.out.println(sdd.getDate()+" "+sdd.getAdjClose());
							// System.out.println(sd.getDate()+" "+sd.getAdjClose());
							// System.out.println("====================");
							// break;
							this.writeLog(tmp.getSymbol(), sd.getDate(), sd.getDate() + "Error AdjClose");
							break;
						}

						if (sdd.getNAV() != null) {
							/*
							 * if(sd.getNAV() == null) {
							 * System.out.println("Error NAV Close: "
							 * +tmp.getSymbol()+"  "+sd.getSplit());
							 * System.out.println
							 * (sdd.getDate()+" "+sdd.getAdjNAV());
							 * System.out.println
							 * (sd.getDate()+" "+sd.getAdjNAV());
							 * System.out.println("====================");
							 * this.writeLog(tmp.getSymbol(),sd.getDate(),
							 * "Error NAV"); break; }
							 */
							if (sd.getNAV() != null) {
								double value1 = sd.getAdjNAV() / sd.getNAV();
								double value2 = sd.getAdjClose() / sd.getClose();
								if (Math.abs(value1 - value2) > 0.1) {
									// System.out.println("Error Adjust NAV: "+tmp.getSymbol()+"  "+sd.getSplit());
									// System.out.println(sdd.getDate()+" "+sdd.getAdjNAV());
									// System.out.println(sd.getDate()+" "+sd.getAdjNAV());
									// System.out.println("====================");
									// break;
									this.writeLog(tmp.getSymbol(), sd.getDate(), "Error AdjNAV");
									break;

								}
							}
						}
					}
				} catch (Exception e) {
					this.writeLog(tmp.getSymbol(), cur, "Check Split Error");
					System.out.println("Check Split Error: " + tmp.getSymbol());
				}

			}
			writer.close();
			stream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void writeLog(String name, Date endDate, String log) {
		try {
			// Date time = new Date();
			FileOutputStream stream = new FileOutputStream(this.logFile, true);
			OutputStreamWriter writer = new OutputStreamWriter(stream);
			writer.write(name + " :Event time is:" + endDate + "; Log Information:" + log + "\n");
			writer.close();
			stream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void uploadSecurityDailyDataInCSV(String file) {
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();

			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");

			List<SecurityDailyData> sdList = new ArrayList<SecurityDailyData>();

			while (true) {
				line = br.readLine();
				if (line == null)
					break;
				SecurityDailyData sdd = new SecurityDailyData();
				String[] ss = line.split(",");
				sdd.setSecurityID(Long.parseLong(ss[1]));
				Date dt = (Date) sf.parseObject(ss[2].split(" ")[0].substring(1));
				sdd.setDate(dt);
				sdd.setClose(Double.parseDouble(ss[3]));
				sdd.setOpen(Double.parseDouble(ss[10]));
				sdd.setHigh(Double.parseDouble(ss[11]));
				sdd.setLow(Double.parseDouble(ss[12]));
				sdd.setVolume(Long.parseLong(ss[13]));
				sdd.setAdjClose(Double.parseDouble(ss[14]));
				sdd.setDividend(Double.parseDouble(ss[15]));
				sdd.setSplit(Double.parseDouble(ss[16]));
				if (ss.length <= 17 || ss[17] == null || ss[17].length() == 0)
					sdd.setNAV(null);
				else
					sdd.setNAV(Double.parseDouble(ss[17]));
				if (ss.length <= 18 || ss[18] == null || ss[18].length() == 0)
					sdd.setAdjNAV(null);
				else
					sdd.setAdjNAV(Double.parseDouble(ss[18]));
				sdList.add(sdd);
			}
			securityManager.saveOrUpdateAll(sdList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/***********************************************************************************************************/
	public void downLoadAndUpdateFund(List<String> fundList, List<Date> fundEndDateList) {
		this.downLoadCloseFundUpdateSite();

		HashMap<String, String> closeMap = this.parseCloseFundUpdateSite();
		/**** part 9:update CN fund ****/
		DailyUpdateControler.total_count = fundList.size();
		for (int i = 0; i < fundList.size(); i++) {
			DailyUpdateControler.current_count = i + 1;
			String name = fundList.get(i);
			Security se = securityManager.get(name);

			String symbol = name.substring(0, name.indexOf("."));

			int type = this.getUpdateType(name);
			if (type == this.CHINA_OPEN_FUND) {
				if (!se.getAssetClass().getName().equalsIgnoreCase("货币基金")) {
					this.downLoadOneUpdateSite(name, "open");
					this.parseOneOpenUpdateSite(this.systemPath + "/updateopenfund/" + symbol, fundList.get(i), fundEndDateList.get(i));
				} else {
					this.downLoadOneUpdateSite(name, "market");
					this.parseOneMoneyMarketUpdateSite(this.systemPath + "/updatemoneymarket/" + symbol, fundList.get(i), fundEndDateList.get(i));
				}
			} else if (type == this.CHINA_CLOSE_FUND) {
				// this.downLoadOneUpdateSite(name,"close");
				String price = closeMap.get(symbol);
				Date date = dateMap.get(symbol);
				if (price == null)
					continue;
				Calendar ca = Calendar.getInstance();
				ca.setTime(date);
				if (ca.get(Calendar.DAY_OF_WEEK) != Calendar.FRIDAY)
					continue;
				if (!date.after(fundEndDateList.get(i)))
					continue;
				this.parseOneCloseUpdateSite(this.systemPath + "/updateclosefund/" + symbol, fundList.get(i), date, price);
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	public void downLoadOneUpdateSite(String symbol, String type) {
		Date date = new Date();

		try {

			symbol = symbol.substring(0, symbol.indexOf("."));

			String strUrl;
			String strName;
			if (type.equalsIgnoreCase("open")) {
				strUrl = "http://biz.finance.sina.com.cn/fundinfo/open/lsjz.php?fund_code=" + symbol;
				strName = this.systemPath + "/updateopenfund/" + symbol;
			} else if (type.equalsIgnoreCase("close")) {
				strUrl = "http://biz.finance.sina.com.cn/fundinfo/close/lsjz.php?fund_code=" + symbol;
				strName = this.systemPath + "/updateclosefund/" + symbol;
			} else
			// market
			{
				strUrl = "http://biz.finance.sina.com.cn/fundinfo/open/lsjz.php?fund_code=" + symbol;
				strName = this.systemPath + "/updatemoneymarket/" + symbol;
			}

			URL url = new URL(strUrl);
			URLConnection urlConnection = (URLConnection) url.openConnection();
			urlConnection.setAllowUserInteraction(false);

			urlConnection.connect();

			FileOutputStream fos = null;
			BufferedInputStream bis = null;

			bis = new BufferedInputStream(urlConnection.getInputStream());
			fos = new FileOutputStream(strName);

			// System.out.println("Getting URL and Downloading the file...");

			int size = 0;
			byte[] buf = new byte[BUFFER_SIZE];

			while ((size = bis.read(buf)) != -1)
				fos.write(buf, 0, size);

			fos.close();
			bis.close();

			// System.out.println(strName+" Success.");

		} catch (MalformedURLException e) {
			this.writeLog("OpenFundUpdate:" + symbol, date, "error");
			e.printStackTrace();
		} catch (IOException e) {
			this.writeLog("OpenFundUpdate:" + symbol, date, "error");
			e.printStackTrace();
		}

	}

	public void parseOneOpenUpdateSite(String path, String fundName, Date date) {
		try {

			FileReader fr = new FileReader(path);
			BufferedReader br = new BufferedReader(fr);

			String line = "";
			while (true) {
				line = br.readLine();
				if (line == null)
					break;

				if (line.indexOf("<td>单位净值增长率(%)</td>") < 0)
					continue;

				else {
					while (true) {
						line = br.readLine();
						line = br.readLine();

						if (line.equalsIgnoreCase("</table>"))
							break;

						line = br.readLine();

						if (line.length() < 19)
							break;

						line = br.readLine();

						int startIndex = line.indexOf("blank") + 6;
						int endIndex = line.indexOf("</a></td>");

						if (startIndex > endIndex)
							break;

						String dateString = line.substring(startIndex, endIndex);
						Date tmpDate = LTIDate.parseStringToDate(dateString);

						if (date != null && !tmpDate.after(date))
							break;

						line = br.readLine();
						endIndex = line.indexOf("</a></td>");
						String NAV = line.substring(startIndex, endIndex);

						line = br.readLine();
						endIndex = line.indexOf("</a></td>");
						String AdjNAV = line.substring(startIndex, endIndex);

						this.addOneFundDailyData(fundName, NAV, NAV, AdjNAV, tmpDate, "open");

						line = br.readLine();

					}
					break;
				}

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("Done: " + date);
		}

	}

	public void parseOneMoneyMarketUpdateSite(String path, String fundName, Date date) {

		try {
			List<Double> addList = new ArrayList<Double>();
			List<Date> dateList = new ArrayList<Date>();
			List<SecurityDailyData> sdds = new ArrayList<SecurityDailyData>();

			FileReader fr = new FileReader(path);
			BufferedReader br = new BufferedReader(fr);

			Security se = securityManager.get(fundName);

			if (se == null)
				return;

			long id = se.getID();

			String line = "";
			while (true) {
				line = br.readLine();
				if (line == null)
					break;

				if (line.indexOf("<td>最近七日收益所折算的年资产收益率(元)</td>") < 0)
					continue;

				else {
					while (true) {
						line = br.readLine();
						line = br.readLine();

						if (line.equalsIgnoreCase("</table>"))
							break;

						line = br.readLine();

						if (line.length() < 19)
							break;

						line = br.readLine();

						int startIndex = line.indexOf("<td>") + 4;
						int endIndex = line.indexOf("</td>");

						if (startIndex > endIndex)
							break;

						String dateString = line.substring(startIndex, endIndex);
						Date tmpDate = LTIDate.parseStringToDate(dateString);

						if (date != null && !tmpDate.after(date))
							break;

						line = br.readLine();
						startIndex = line.indexOf("<td align=center>") + 17;
						endIndex = line.indexOf("</td>");
						String ADD = line.substring(startIndex, endIndex);

						double add = Double.parseDouble(ADD);

						addList.add(add);
						dateList.add(tmpDate);

						line = br.readLine();

					}
					break;
				}

			}

			double originalAdj = 1.0;

			SecurityDailyData original = null;
			if (date != null)
				original = securityManager.getDailydata(id, date, true);
			if (original == null || date == null) {
				originalAdj = 1.0;
			} else {
				originalAdj = original.getAdjClose();
			}

			for (int i = dateList.size() - 1; i >= 0; i--) {
				Date curDate = dateList.get(i);
				double add = addList.get(i);
				SecurityDailyData tmp = securityManager.getDailydata(id, curDate, true);
				if (tmp == null) {
					tmp = new SecurityDailyData();
				}

				tmp.setDate(curDate);
				tmp.setClose(1.0);
				tmp.setOpen(1.0);
				tmp.setHigh(1.0);
				tmp.setLow(1.0);

				tmp.setDividend(0.0);
				tmp.setNAV(1.0);
				tmp.setSplit(1.0);

				add = add / 10000;
				originalAdj += add;

				tmp.setAdjClose(originalAdj);
				tmp.setAdjNAV(originalAdj);

				tmp.setSecurityID(se.getID());

				sdds.add(tmp);
			}

			securityManager.saveOrUpdateAll(sdds);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("Done: " + date);
		}

	}

	public void downLoadCloseFundUpdateSite() {
		Date date = new Date();
		try {

			String strUrl = "http://biz.finance.sina.com.cn/fundinfo/share/stslv.php";
			String strName = this.systemPath + "/updateclosefund/all";

			URL url = new URL(strUrl);
			URLConnection urlConnection = (URLConnection) url.openConnection();
			urlConnection.setAllowUserInteraction(false);

			urlConnection.connect();

			FileOutputStream fos = null;
			BufferedInputStream bis = null;

			bis = new BufferedInputStream(urlConnection.getInputStream());
			fos = new FileOutputStream(strName);

			System.out.println("Getting URL and Downloading the file...");

			int size = 0;
			byte[] buf = new byte[BUFFER_SIZE];

			while ((size = bis.read(buf)) != -1)
				fos.write(buf, 0, size);

			fos.close();
			bis.close();

			// System.out.println(strName+" Success.");

		} catch (MalformedURLException e) {
			this.writeLog("CloseFundUpdate", date, "error");
			e.printStackTrace();
		} catch (IOException e) {
			this.writeLog("CloseFundUpdate:", date, "error");
			e.printStackTrace();
		}
	}

	public HashMap<String, String> parseCloseFundUpdateSite() {
		HashMap<String, String> map = new HashMap<String, String>();

		try {
			String path = this.systemPath + "/updateclosefund/all";
			FileReader fr = new FileReader(path);
			BufferedReader br = new BufferedReader(fr);

			String line = "";
			while (true) {
				line = br.readLine();
				if (line == null)
					break;

				if (line.indexOf("<td>升贴水率(%)</td>") < 0)
					continue;

				else {
					while (true) {
						line = br.readLine();
						line = br.readLine();

						if (line.equalsIgnoreCase("</table>"))
							break;

						line = br.readLine();

						if (line.length() < 15)
							break;

						int startIndex = line.indexOf("<td>") + 4;
						int endIndex = line.indexOf("</td>");

						if (startIndex > endIndex)
							break;

						String symbol = line.substring(startIndex, endIndex);

						line = br.readLine();

						line = br.readLine();
						startIndex = line.indexOf("<td>") + 4;
						endIndex = line.indexOf("</td>");
						String dateString = line.substring(startIndex, endIndex);

						line = br.readLine();

						startIndex = line.indexOf("<td>") + 4;
						endIndex = line.indexOf("</td>");
						String price = line.substring(startIndex, endIndex);

						map.put(symbol, price);
						dateMap.put(symbol, LTIDate.parseStringToDate(dateString));

						line = br.readLine();
						line = br.readLine();
						line = br.readLine();

					}
					break;
				}

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return map;
	}

	public void parseOneCloseUpdateSite(String path, String fundName, Date date, String close) {
		try {

			FileReader fr = new FileReader(path);
			BufferedReader br = new BufferedReader(fr);

			String line = "";
			while (true) {
				line = br.readLine();
				if (line == null)
					break;

				if (line.indexOf("<td>单位净值增长率(%)</td>") < 0)
					continue;

				else {
					while (true) {
						line = br.readLine();
						line = br.readLine();

						if (line.equalsIgnoreCase("</table>"))
							break;

						line = br.readLine();

						if (line.length() < 19)
							break;

						line = br.readLine();

						int startIndex = line.indexOf("blank") + 6;
						int endIndex = line.indexOf("</a></td>");

						if (startIndex > endIndex)
							break;

						String dateString = line.substring(startIndex, endIndex);
						Date tmpDate = LTIDate.parseStringToDate(dateString);

						// if(date != null && !tmpDate.after(date))break;

						line = br.readLine();
						endIndex = line.indexOf("</a></td>");
						String NAV = line.substring(startIndex, endIndex);

						line = br.readLine();
						endIndex = line.indexOf("</a></td>");
						String AdjNAV = line.substring(startIndex, endIndex);

						line = br.readLine();

						if (date != null && !LTIDate.equals(tmpDate, date))
							continue;
						else if (date != null && tmpDate.before(date))
							break;
						else {
							this.addOneFundDailyData(fundName, close, NAV, AdjNAV, tmpDate, "open");
							break;
						}
					}
					break;
				}

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("Done: " + date);
		}

	}

	/*************************************************************************************************************/
	/*************************************************************************************************************/

	/* for build up historical data */
	public void buildUpOpenFund() {
		File rootFile = new File(systemPath + "/openfund/");
		if (!rootFile.isDirectory())
			rootFile.mkdir();

		File[] files = rootFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			files[i].delete();
		}

		this.downLoadAllOpenFund();
		this.parseAllOpenSite();
	}

	public void parseAllOpenSite() {
		File rootFile = new File(systemPath + "/openfund/");

		File[] files = rootFile.listFiles();

		for (int i = 0; i < files.length; i++) {
			String fileName = files[i].getName();
			Date date = LTIDate.parseStringToDate(fileName);
			this.parseOneOpenSite(systemPath + "/openfund/" + fileName, date);
		}
	}

	public void downLoadAllOpenFund() {
		try {
			Date preDate = LTIDate.getDate(2001, 1, 1);
			Date today = new Date();
			while (true) {
				if (preDate.after(today))
					break;
				this.downLoadOneSiteOfOpenFund(preDate);
				preDate = LTIDate.add(preDate, 1);

				// System.out.println("Done:"+preDate);
				Thread.sleep(500);

			}
			System.out.println("Done down load all the chinese open fund");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void downLoadOneSiteOfOpenFund(Date date) {
		try {
			String dateString = "";
			Calendar ca = Calendar.getInstance();
			ca.setTime(date);
			Integer year = ca.get(Calendar.YEAR);
			Integer month = ca.get(Calendar.MONTH) + 1;
			Integer day = ca.get(Calendar.DAY_OF_MONTH);
			dateString = year.toString();
			if (month < 10)
				dateString += "0";
			dateString += month.toString();
			if (day < 10)
				dateString += "0";
			dateString += day.toString();

			String strUrl = "http://biz.finance.sina.com.cn/fundinfo/open/lsjz_jzrq.php?jzrq=" + dateString;
			String strName = this.systemPath + "/openfund/" + dateString;

			URL url = new URL(strUrl);
			URLConnection urlConnection = (URLConnection) url.openConnection();
			urlConnection.setAllowUserInteraction(false);

			urlConnection.connect();

			FileOutputStream fos = null;
			BufferedInputStream bis = null;

			bis = new BufferedInputStream(urlConnection.getInputStream());
			fos = new FileOutputStream(strName);

			// System.out.println("Getting URL and Downloading the file...");

			int size = 0;
			byte[] buf = new byte[BUFFER_SIZE];

			while ((size = bis.read(buf)) != -1)
				fos.write(buf, 0, size);

			fos.close();
			bis.close();

			// System.out.println(strName+" Success.");

		} catch (MalformedURLException e) {
			this.writeLog("OpenFund", date, "error");
			e.printStackTrace();
		} catch (IOException e) {
			this.writeLog("OpenFund", date, "error");
			e.printStackTrace();
		}
	}

	public void parseOneOpenSite(String name, Date date) {
		try {

			FileReader fr = new FileReader(name);
			BufferedReader br = new BufferedReader(fr);

			String line = "";
			while (true) {
				line = br.readLine();
				if (line == null)
					break;

				if (line.indexOf("<td>净值增长率(%)</td>") < 0)
					continue;

				else {
					while (true) {
						line = br.readLine();
						line = br.readLine();
						if (line.equalsIgnoreCase("</table>"))
							break;
						line = br.readLine();

						if (line.length() < 9)
							break;

						int startIndex = 5;
						int endIndex = line.indexOf("</td>");

						if (startIndex > endIndex)
							break;

						String fundName = line.substring(startIndex, endIndex);

						line = br.readLine();
						endIndex = line.indexOf("</td>");
						String NAV = line.substring(startIndex, endIndex);

						line = br.readLine();
						endIndex = line.indexOf("</td>");
						String AdjNAV = line.substring(startIndex, endIndex);

						this.addOneFundDailyData(fundName, NAV, NAV, AdjNAV, date, "open");

						line = br.readLine();

					}
					break;
				}

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("Done: " + date);
		}
	}

	/*************************************************************************************************************/
	/*************************************************************************************************************/

	public void buildupCloseFund() {
		File root = new File(systemPath + "/closefund/");
		File[] files = root.listFiles();
		for (int i = 0; i < files.length; i++) {
			this.parseOneCloseFundDoc(files[i].getName());
		}
	}

	public void parseOneCloseFundDoc(String path) {
		/*
		 * 收盘价(元) 单位净值(元) 复权单位净值(元) 累计单位净值(元) 复权单位净值增长率(%) 2008-7-31 0.73 0.454
		 * 0.5372 0.679 -2.79
		 */

		String name = path.substring(0, 10);

		Security se = securityManager.get(name);
		if (se == null)
			return;

		long id = se.getID();

		List<SecurityDailyData> sdds = new ArrayList<SecurityDailyData>();

		path = this.systemPath + "/closefund/" + path;
		try {
			FileReader fr = new FileReader(path);
			BufferedReader br = new BufferedReader(fr);

			String line = "";
			line = br.readLine();
			while (true) {
				line = br.readLine();
				if (line == null)
					break;
				if (line.indexOf("数据来源") >= 0)
					break;
				if (line.length() == 0)
					continue;

				String[] tmp = line.split(",");

				Date date = LTIDate.parseStringToDate(tmp[0]);

				if (tmp[1] == null || tmp[1].length() == 0)
					continue;

				SecurityDailyData sdd = securityManager.getDailydata(id, date, true);
				if (sdd == null)
					sdd = new SecurityDailyData();

				sdd.setDate(date);

				double clo = Double.parseDouble(tmp[1]);
				sdd.setClose(clo);
				sdd.setOpen(clo);
				sdd.setHigh(clo);
				sdd.setLow(clo);

				sdd.setDividend(0.0);
				sdd.setSplit(1.0);

				double nav = Double.parseDouble(tmp[2]);
				sdd.setNAV(nav);

				double adj = Double.parseDouble(tmp[4]);

				sdd.setAdjNAV(adj);
				sdd.setAdjClose(adj);

				sdd.setSecurityID(id);

				sdds.add(sdd);

			}

			securityManager.saveOrUpdateAll(sdds);
			// System.out.println("Done:"+name);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*************************************************************************************************************/

	public void addOneFundDailyData(String name, String close, String NAV, String AdjNAV, Date date, String type) {
		Security se = securityManager.get(name);

		if (se == null) {
			System.out.println(name + " is not exits.");
			return;
		}

		SecurityDailyData sdd = null;

		boolean flag = false;

		sdd = securityManager.getDailydata(se.getID(), date, true);

		if (sdd == null) {
			sdd = new SecurityDailyData();
			flag = true;
		}

		double adj = Double.parseDouble(AdjNAV);
		double nav = Double.parseDouble(NAV);
		double clo = Double.parseDouble(close);

		sdd.setDate(date);

		if (type.equalsIgnoreCase("open")) {
			sdd.setAdjClose(adj);
			sdd.setClose(nav);
			sdd.setOpen(nav);
			sdd.setHigh(nav);
			sdd.setLow(nav);

			sdd.setDividend(0.0);
			sdd.setNAV(nav);
			sdd.setAdjNAV(adj);
			sdd.setSplit(1.0);
			sdd.setSecurityID(se.getID());
		} else if (type.equalsIgnoreCase("close")) {
			double adjclose = adj * clo / nav;
			sdd.setAdjClose(adjclose);

			sdd.setClose(clo);
			sdd.setOpen(clo);
			sdd.setHigh(clo);
			sdd.setLow(clo);

			sdd.setDividend(0.0);
			sdd.setNAV(nav);
			sdd.setAdjNAV(adj);
			sdd.setSplit(1.0);
			sdd.setSecurityID(se.getID());
		}

		if (flag)
			securityManager.saveDailyData(sdd);
		else
			securityManager.updateDailyData(sdd);

	}

	/****************************************************************************/

	public void buildupMoneyMarket() {
		File root = new File(systemPath + "/moneymarket/");
		File[] files = root.listFiles();
		for (int i = 0; i < files.length; i++) {
			this.parseOneMoneyMarketDoc(files[i].getName());
		}
	}

	public void parseOneMoneyMarketDoc(String path) {
		/*
		 * 每万份基金单位收益(元) 最近七日收益所折算的年资产收益率(%) 2008-7-31 0.9022 3.538
		 */

		String name = path.substring(0, 9);

		Security se = securityManager.get(name);

		if (se == null)
			return;

		long id = se.getID();

		List<SecurityDailyData> sdds = new ArrayList<SecurityDailyData>();

		List<Double> adds = new ArrayList<Double>();

		path = this.systemPath + "/moneymarket/" + path;
		try {
			FileReader fr = new FileReader(path);
			BufferedReader br = new BufferedReader(fr);

			String line = "";
			line = br.readLine();

			while (true) {
				line = br.readLine();

				if (line == null)
					break;
				if (line.length() == 0)
					continue;
				if (line.indexOf("数据来源") >= 0)
					break;

				String[] tmp = line.split(",");

				Date date = LTIDate.parseStringToDate(tmp[0]);

				SecurityDailyData sdd = securityManager.getDailydata(id, date, true);
				if (sdd == null)
					sdd = new SecurityDailyData();

				sdd.setDate(date);
				sdd.setClose(1.0);
				sdd.setOpen(1.0);
				sdd.setHigh(1.0);
				sdd.setLow(1.0);

				sdd.setDividend(0.0);
				sdd.setNAV(1.0);
				sdd.setSplit(1.0);

				double add = Double.parseDouble(tmp[1]);
				adds.add(add / 10000.0);

				sdd.setSecurityID(id);

				sdds.add(sdd);
			}

			List<SecurityDailyData> sdds2 = new ArrayList<SecurityDailyData>();

			double amount = 1.0;

			for (int i = sdds.size() - 1; i >= 0; i--) {
				double tmpAdd = adds.get(i);
				SecurityDailyData tmp = sdds.get(i);
				amount += tmpAdd;
				tmp.setAdjClose(amount);
				tmp.setAdjNAV(amount);
				sdds2.add(tmp);
			}

			securityManager.saveOrUpdateAll(sdds2);
			// System.out.println("Done:"+name);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/****************************************************************************/
	/****************************************************************************/
	public void checkSecurityDailyData() {
		List<Security> securityList = securityManager.getSecurities();

		for (int i = 0; i < securityList.size(); i++) {
			long id = securityList.get(i).getID();
			String name = securityList.get(i).getSymbol();
			List<SecurityDailyData> sdds = securityManager.getDailydatas(id, true);
			Date startDate = sdds.get(0).getDate();
			Date endDate = sdds.get(sdds.size() - 1).getDate();
			Date today = startDate;
			int index = 0;
			while (true) {
				if (today.after(endDate))
					break;

				if (!LTIDate.equals(today, sdds.get(index).getDate())) {
					if (!LTIDate.isNYSETradingDay(today)) {
						today = LTIDate.add(today, 1);
						continue;
					} else {
						System.out.println("Error:ID" + id + " " + today);
						// this.writeLog(id+name, today, "Hole Error");
						today = LTIDate.add(today, 1);
						continue;
					}
				}

				index++;

				today = LTIDate.add(today, 1);
			}
			// System.out.println("Done one");
		}
	}

	/****************************************************************************/
	/****************************************************************************/

	public int getUpdateType(String symbol) {
		if (symbol.indexOf(".OF") > 0)
			return this.CHINA_OPEN_FUND;
		else if (symbol.indexOf(".CEF") > 0)
			return this.CHINA_CLOSE_FUND;
		else
			return this.NORMAL_SECURITY;
	}

	/****************************************************************************/
	/****************************************************************************/

	public void buildupCNCash() {
		long id = securityManager.get("CN CASH").getID();
		Date endDate = securityManager.getDailydatas(id, true).get(0).getDate();
		Date curDate = LTIDate.getDate(1990, 1, 1);

		List<SecurityDailyData> seList = new ArrayList<SecurityDailyData>();

		while (true) {
			if (LTIDate.equals(curDate, endDate))
				break;

			SecurityDailyData se = new SecurityDailyData();
			se.setAdjClose(1.0);
			se.setAdjNAV(1.0);
			se.setClose(1.0);
			se.setDividend(0.0);
			se.setHigh(1.0);
			se.setLow(1.0);
			se.setNAV(1.0);
			se.setOpen(1.0);
			se.setSecurityID(id);
			se.setSplit(1.0);
			se.setDate(curDate);

			seList.add(se);

			curDate = LTIDate.getNewNYSETradingDay(curDate, 1);

		}

		securityManager.saveOrUpdateAll(seList);
	}

	/**************************************************************************************************
	 * Download the SP 500 data and update the earing and PE of ^GSPC
	 **************************************************************************************************/
	public void downLoadAndUpdateSP() {
		Date date = new Date();
		Integer year = date.getYear() + 1900;
		Integer month = date.getMonth() + 1;
		Integer day = date.getDate();
		String monthS = month > 9 ? (month.toString()) : ("0" + month.toString());
		String dayS = day > 9 ? (day.toString()) : ("0" + day.toString());
		String file = systemPath + "spdatas/" + "SPData.xls";

		this.downLoadSPData();
		try {
			dataManager.UpdateSP500EaringAndPE(file, this.logDate);

			this.writeLog("S&PData", date, "down and upload Success");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.writeLog("S&PData", date, "Down error:" + e.toString());
		}
		System.out.println("S&P updating done!");
	}

	/**
	 * @author CCD downLoad and update shiller S&P500 historical data
	 */
	public void downLoadAndUpdateShillerSP() {
		Date date = new Date();
		Integer year = date.getYear() + 1900;
		Integer month = date.getMonth() + 1;
		Integer day = date.getDate();
		String monthS = month > 9 ? (month.toString()) : ("0" + month.toString());
		String dayS = day > 9 ? (day.toString()) : ("0" + day.toString());
		String file = systemPath + "spdatas/" + "ShillerSPData.xls";
		System.out.println(file);
		this.downLoadShillerSPData();
		try {
			dataManager.UpdateShillerSP500Data(file, this.logDate);

			this.writeLog("Shiller S&P Data", date, "down and upload Success");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.writeLog("S&PData", date, "Down error:" + e.toString());
		}
		System.out.println("S&P updating done!");
	}

	/**************************************************************************************************
	 * @author CCD added on 2009-10-12 downLoad the .xls from
	 *         http://www.econ.yale.edu/~shiller/data/ie_data.xls
	 **************************************************************************************************/
	public void downLoadShillerSPData() {
		Date date = new Date();
		Integer year = date.getYear() + 1900;
		Integer month = date.getMonth() + 1;
		Integer day = date.getDate();
		String monthS = month > 9 ? (month.toString()) : ("0" + month.toString());
		String dayS = day > 9 ? (day.toString()) : ("0" + day.toString());
		String file = systemPath + "spdatas/" + "ShillerSPData.xls";
		try {
			String strName = file;

			URL url = new URL("http://www.econ.yale.edu/~shiller/data/ie_data.xls");
			URLConnection urlConnection = (URLConnection) url.openConnection();
			urlConnection.setAllowUserInteraction(false);

			urlConnection.connect();

			FileOutputStream fos = null;
			BufferedInputStream bis = null;

			bis = new BufferedInputStream(urlConnection.getInputStream());
			fos = new FileOutputStream(strName);

			int size = 0;
			byte[] buf = new byte[BUFFER_SIZE];

			while ((size = bis.read(buf)) != -1)
				fos.write(buf, 0, size);

			fos.close();
			bis.close();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**************************************************************************************************
	 * http://www2.standardandpoors.com/spf/xls/index/SP500EPSEST.XLS
	 **************************************************************************************************/
	public void downLoadSPData() {
		Date date = new Date();
		Integer year = date.getYear() + 1900;
		Integer month = date.getMonth() + 1;
		Integer day = date.getDate();
		String monthS = month > 9 ? (month.toString()) : ("0" + month.toString());
		String dayS = day > 9 ? (day.toString()) : ("0" + day.toString());
		String file = systemPath + "spdatas/" + "SPData.xls";
		try {
			String strName = file;

			URL url = new URL("http://www2.standardandpoors.com/spf/xls/index/SP500EPSEST.XLS");
			URLConnection urlConnection = (URLConnection) url.openConnection();
			urlConnection.setAllowUserInteraction(false);

			urlConnection.connect();

			FileOutputStream fos = null;
			BufferedInputStream bis = null;

			bis = new BufferedInputStream(urlConnection.getInputStream());
			fos = new FileOutputStream(strName);

			int size = 0;
			byte[] buf = new byte[BUFFER_SIZE];

			while ((size = bis.read(buf)) != -1)
				fos.write(buf, 0, size);

			fos.close();
			bis.close();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/******************************************************************************************************/
	public void downLoadFamaBenchMark(int type) {
		try {
			String strUrl = "http://mba.tuck.dartmouth.edu/pages/faculty/ken.french/ftp/F-F_Research_Data_Factors_daily.zip";
			String strName = this.systemPath + "/F-F_Research_Data_Factors_daily.zip";

			if (type == 2) {
				strUrl = "http://mba.tuck.dartmouth.edu/pages/faculty/ken.french/ftp/F-F_Momentum_Factor_daily.zip";
				strName = this.systemPath + "F-F_Momentum_Factor_daily.zip";
			}
			if (type == 3) {
				strUrl = "http://mba.tuck.dartmouth.edu/pages/faculty/ken.french/ftp/49_Industry_Portfolios_daily.zip";
				strName = this.systemPath + "49_Industry_Portfolios_daily.zip";
			}

			URL url = new URL(strUrl);
			URLConnection urlConnection = (URLConnection) url.openConnection();
			urlConnection.setAllowUserInteraction(false);

			urlConnection.connect();

			FileOutputStream fos = null;
			BufferedInputStream bis = null;

			bis = new BufferedInputStream(urlConnection.getInputStream());
			fos = new FileOutputStream(strName);

			System.out.println("Getting URL and Downloading the file...");

			int size = 0;
			byte[] buf = new byte[BUFFER_SIZE];

			while ((size = bis.read(buf)) != -1)
				fos.write(buf, 0, size);

			fos.close();
			bis.close();

			// System.out.println(strName+" Success.");

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void saveOneFFFactors(String name) {
		AssetClassManager am = (AssetClassManager) ContextHolder.getInstance().getApplicationContext().getBean("assetClassManager");
		long usEquityID = am.get("US EQUITY").getID();
		Security se = new Security();
		se.setClassID(usEquityID);
		se.setSecurityType(7);
		se.setName(name);
		se.setSymbol(name);
		se.setManual(0);
		se.setPriceLastDate(null);
		se.setEndDate(null);
		securityManager.saveOrUpdate(se);
	}

	public void buildUpFiveFactors() {
		Security se = this.securityManager.getBySymbol("^FF_MOM");
		if (se == null)
			this.saveOneFFFactors("^FF_MOM");
		se = this.securityManager.getBySymbol("^FF_RMRF");
		if (se == null)
			this.saveOneFFFactors("^FF_RMRF");
		se = this.securityManager.getBySymbol("^FF_SMB");
		if (se == null)
			this.saveOneFFFactors("^FF_SMB");
		se = this.securityManager.getBySymbol("^FF_HML");
		if (se == null)
			this.saveOneFFFactors("^FF_HML");
		se = this.securityManager.getBySymbol("^FF_RF");
		if (se == null)
			this.saveOneFFFactors("^FF_RF");
	}

	public boolean isFFUpdated(String line, Date lastPriceDate) {
		if (lastPriceDate == null)
			return false;

		String[] fileMessage = line.split(" ");
		String lastDateMessage = null;
		for (int i = 0; i < fileMessage.length; i++) {
			char c = fileMessage[i].charAt(0);
			if (c <= '9' && c >= '0') {
				lastDateMessage = fileMessage[i];
				break;
			}
		}
		if (lastDateMessage != null) {
			lastDateMessage += "01";
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
			try {
				Date lastDateFromMessage = sf.parse(lastDateMessage);
				Date tmpLastPriceDate = lastPriceDate;
				tmpLastPriceDate.setDate(1);
				if (!lastDateFromMessage.after(tmpLastPriceDate))
					return true;
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	// Missing data are indicated by -99.99 or -999.
	public void uploadMomentumFactor(String file) {

		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);

			Security se = this.securityManager.getBySymbol("^FF_MOM");
			if (se == null)
				return;

			long securityID = se.getID();

			Date lastPriceDate = se.getPriceLastDate();

			String line = "";
			line = br.readLine();
			Date date = null;

			if (this.isFFUpdated(line, lastPriceDate))
				return;

			List<SecurityDailyData> sdds = new ArrayList<SecurityDailyData>();

			while (line != null) {
				if (line.length() == 0 || line.charAt(0) > '9' || line.charAt(0) < '0') {
					line = br.readLine();
					continue;
				}
				line.trim();
				String[] k = line.split("\\s+");

				date = LTIDate.parseStringToDate(k[0]);

				double price = Double.parseDouble(k[1]);

				if (price != -99.99 && price != -999) {
					if (lastPriceDate == null || (lastPriceDate != null && lastPriceDate.before(date))) {
						SecurityDailyData sdd = new SecurityDailyData();
						price = price / 100.0;// because the price is percentage
												// return
						sdd.setDate(date);
						sdd.setSecurityID(securityID);
						sdd.setAdjClose(price);
						sdd.setClose(price);
						sdd.setHigh(price);
						sdd.setLow(price);
						sdd.setOpen(price);
						sdd.setSplit(1.0);
						sdds.add(sdd);
					}
				}

				line = br.readLine();
			}

			if (date != null) {
				se.setPriceLastDate(date);
				se.setEndDate(date);
				securityManager.saveOrUpdate(se);
			}

			securityManager.saveOrUpdateAll(sdds);

			System.out.println("Fama French Momentum Update Success.");
			this.writeLog("^FF_MOM", new Date(), "^FF_MOM update success");

		} catch (Exception e) {
			this.writeLog("^FF_MOM", new Date(), e.toString());
			e.printStackTrace();
		}

	}

	public void uploadResearchFactor(String file) {

		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);

			Security RMRF = this.securityManager.getBySymbol("^FF_RMRF");
			Security SMB = this.securityManager.getBySymbol("^FF_SMB");
			Security HML = this.securityManager.getBySymbol("^FF_HML");
			Security RF = this.securityManager.getBySymbol("^FF_RF");

			if (RMRF == null || SMB == null || HML == null || RF == null) {
				this.writeLog("RMRF SMB HML RF", new Date(), "do not exist!");
				return;
			}

			long RMRF_ID = RMRF.getID();
			long SMB_ID = SMB.getID();
			long HML_ID = HML.getID();
			long RF_ID = RF.getID();

			Date RMRF_lastPriceDate = RMRF.getPriceLastDate();
			Date SMB_lastPriceDate = SMB.getPriceLastDate();
			Date HML_lastPriceDate = HML.getPriceLastDate();
			Date RF_lastPriceDate = RF.getPriceLastDate();

			String line = "";
			line = br.readLine();
			Date date = null;

			if (this.isFFUpdated(line, RMRF_lastPriceDate) && this.isFFUpdated(line, SMB_lastPriceDate) && this.isFFUpdated(line, HML_lastPriceDate) && this.isFFUpdated(line, RF_lastPriceDate))
				return;

			List<SecurityDailyData> sdds = new ArrayList<SecurityDailyData>();

			while (line != null) {
				if (line.length() == 0 || line.charAt(0) > '9' || line.charAt(0) < '0') {
					line = br.readLine();
					continue;
				}
				line.trim();
				String[] k = line.split("\\s+");

				date = LTIDate.parseStringToDate(k[0]);
				double price = Double.parseDouble(k[1]);

				if (price != -99.99 && price != -999) {
					if (RMRF_lastPriceDate == null || (RMRF_lastPriceDate != null && RMRF_lastPriceDate.before(date))) {
						SecurityDailyData sdd = new SecurityDailyData();
						price = price / 100.0;// because the price is percentage
												// return
						sdd.setDate(date);
						sdd.setSecurityID(RMRF_ID);
						sdd.setAdjClose(price);
						sdd.setClose(price);
						sdd.setHigh(price);
						sdd.setLow(price);
						sdd.setOpen(price);
						sdd.setSplit(1.0);
						sdds.add(sdd);
					}
				}

				price = Double.parseDouble(k[2]);
				if (price != -99.99 && price != -999) {
					if (SMB_lastPriceDate == null || (SMB_lastPriceDate != null && SMB_lastPriceDate.before(date))) {
						SecurityDailyData sdd = new SecurityDailyData();
						price = price / 100.0;// because the price is percentage
												// return
						sdd.setDate(date);
						sdd.setSecurityID(SMB_ID);
						sdd.setAdjClose(price);
						sdd.setClose(price);
						sdd.setHigh(price);
						sdd.setLow(price);
						sdd.setOpen(price);
						sdd.setSplit(1.0);
						sdds.add(sdd);
					}
				}

				price = Double.parseDouble(k[3]);
				if (price != -99.99 && price != -999) {
					if (HML_lastPriceDate == null || (HML_lastPriceDate != null && HML_lastPriceDate.before(date))) {
						SecurityDailyData sdd = new SecurityDailyData();
						price = price / 100.0;// because the price is percentage
												// return
						sdd.setDate(date);
						sdd.setSecurityID(HML_ID);
						sdd.setAdjClose(price);
						sdd.setClose(price);
						sdd.setHigh(price);
						sdd.setLow(price);
						sdd.setOpen(price);
						sdd.setSplit(1.0);
						sdds.add(sdd);
					}
				}

				price = Double.parseDouble(k[4]);
				if (price != -99.99 && price != -999) {
					if (RF_lastPriceDate == null || (RF_lastPriceDate != null && RF_lastPriceDate.before(date))) {
						SecurityDailyData sdd = new SecurityDailyData();
						price = price / 100.0;// because the price is percentage
												// return
						sdd.setDate(date);
						sdd.setSecurityID(RF_ID);
						sdd.setAdjClose(price);
						sdd.setClose(price);
						sdd.setHigh(price);
						sdd.setLow(price);
						sdd.setOpen(price);
						sdd.setSplit(1.0);
						sdds.add(sdd);
					}
				}
				line = br.readLine();
			}

			if (date != null) {
				RMRF.setPriceLastDate(date);
				RMRF.setEndDate(date);
				securityManager.saveOrUpdate(RMRF);

				SMB.setPriceLastDate(date);
				SMB.setEndDate(date);
				securityManager.saveOrUpdate(SMB);

				HML.setPriceLastDate(date);
				HML.setEndDate(date);
				securityManager.saveOrUpdate(HML);

				RF.setPriceLastDate(date);
				RF.setEndDate(date);
				securityManager.saveOrUpdate(RF);
			}

			securityManager.saveOrUpdateAll(sdds);

			System.out.println("Fama French Daily Data Update Success.");
			this.writeLog("^FF_RMRF,^FF_SMB,^FF_HML,^FF_RF", new Date(), "^FF_RMRF,^FF_SMB,^FF_HML,^FF_RF update success");

		} catch (Exception e) {
			this.writeLog("^FF_RMRF,^FF_SMB,^FF_HML,^FF_RF", new Date(), e.toString());
			e.printStackTrace();
		}

	}

	private List<Security> _securityList = new ArrayList<Security>();
	private List<Long> _securityIDList = new ArrayList<Long>();
	private List<Date> _lastPriceDateList = new ArrayList<Date>();
	private Date min_lastpriceDate = null;

	private void processIndustryNewFactor(String preLine, int type) {

		_securityIDList.clear();
		_lastPriceDateList.clear();
		min_lastpriceDate = null;

		String[] symbols = preLine.split("\\s+");

		AssetClassManager am = (AssetClassManager) ContextHolder.getInstance().getApplicationContext().getBean("assetClassManager");
		long usEquityID = am.get("US EQUITY").getID();

		String typeString = "^FF_Value_";
		if (type == 2)
			typeString = "^FF_Equal_";

		for (int i = 0; i < symbols.length; i++) {

			if (symbols[i] == null || symbols[i].length() == 0)
				continue;

			String tmpSymbol = typeString + symbols[i];
			Security tmp = securityManager.getBySymbol(tmpSymbol);

			if (tmp == null) {
				tmp = new Security();
				tmp.setClassID(usEquityID);
				tmp.setSecurityType(7);
				tmp.setName(tmpSymbol);
				tmp.setSymbol(tmpSymbol);
				tmp.setManual(0);
				tmp.setPriceLastDate(null);
				tmp.setEndDate(null);
				securityManager.saveOrUpdate(tmp);
			}

			_securityIDList.add(tmp.getID());

			Date tmpLastDate = tmp.getPriceLastDate();
			_lastPriceDateList.add(tmpLastDate);
			if (min_lastpriceDate == null || (min_lastpriceDate != null && tmpLastDate != null && min_lastpriceDate.after(tmpLastDate)))
				min_lastpriceDate = tmpLastDate;

			_securityList.add(tmp);
		}
	}

	public void uploadIndustryFactor(String file) {

		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);

			List<SecurityDailyData> sdds = new ArrayList<SecurityDailyData>();

			Date date = null;

			int lineNum = 0;

			boolean newSecurity = false;

			String line = "", preLine = "";
			line = br.readLine();

			String firstLine = line;

			int type = 1;
			while (line != null) {
				if (line.length() == 0 || line.charAt(0) > '9' || line.charAt(0) < '0') {
					preLine = line;
					line = br.readLine();
					newSecurity = true;
					continue;
				}

				if (newSecurity) {
					this.processIndustryNewFactor(preLine, type);
					type++;
					newSecurity = false;
				}

				if (this.isFFUpdated(firstLine, min_lastpriceDate))
					return;

				// System.out.println(line);
				line.trim();
				String[] k = line.split("\\s+");

				date = LTIDate.parseStringToDate(k[0]);
				if (min_lastpriceDate != null && !date.after((min_lastpriceDate))) {
					line = br.readLine();
					continue;
				}

				double price = 0;

				for (int i = 0, len = _securityIDList.size(); i < len; i++) {
					price = Double.parseDouble(k[i + 1]);
					Date lastPriceDate = _lastPriceDateList.get(i);
					if (price != -99.99 && price != -999) {
						if (lastPriceDate == null || (lastPriceDate != null && lastPriceDate.before(date))) {
							SecurityDailyData sdd = new SecurityDailyData();
							price = price / 100.0;// because the price is
													// percentage return
							sdd.setDate(date);
							sdd.setSecurityID(_securityIDList.get(i));
							sdd.setAdjClose(price);
							sdd.setClose(price);
							sdd.setHigh(price);
							sdd.setLow(price);
							sdd.setOpen(price);
							sdd.setSplit(1.0);
							sdds.add(sdd);

							lineNum++;
						}
					}
				}

				line = br.readLine();

				if (lineNum >= 2000) {
					lineNum = 0;
					securityManager.saveOrUpdateAll(sdds);
					sdds.clear();
				}
			}

			if (sdds != null && sdds.size() > 0)
				securityManager.saveOrUpdateAll(sdds);

			if (date != null) {
				for (int i = 0, len = _securityList.size(); i < len; i++) {
					Security se = _securityList.get(i);
					se.setPriceLastDate(date);
					se.setEndDate(date);
					securityManager.saveOrUpdate(se);
				}
			}

			br.close();
			fr.close();

			// System.out.println("49 Industry Factors Update Success.");
			this.writeLog("49 Industry Factors", new Date(), "Industry Factors update success");

		} catch (Exception e) {
			this.writeLog("Industry Factors", new Date(), e.toString());
			e.printStackTrace();
		}

	}

	public void updateFamaBenchMark() {
		Date date = new Date();
		if (date.getDay() != 1)
			return;
		DailyUpdateControler.state = "Update FamaBenchMark";
		DailyUpdateControler.total_count = 3;
		DailyUpdateControler.current_count = 1;
		this.buildUpFiveFactors();
		this.downLoadFamaBenchMark(1);
		this.downLoadFamaBenchMark(2);
		this.downLoadFamaBenchMark(3);
		this.zipExtracting(this.systemPath, this.systemPath + "/F-F_Momentum_Factor_daily.zip");
		this.zipExtracting(this.systemPath, this.systemPath + "/F-F_Research_Data_Factors_daily.zip");
		this.zipExtracting(this.systemPath, this.systemPath + "/49_Industry_Portfolios_daily.zip");
		this.uploadMomentumFactor(this.systemPath + "/F-F_Momentum_Factor_daily.txt");
		DailyUpdateControler.current_count = 2;
		this.uploadResearchFactor(this.systemPath + "/F-F_Research_Data_Factors_daily.txt");
		DailyUpdateControler.current_count = 3;
		this.uploadIndustryFactor(this.systemPath + "/49_Industry_Portfolios_daily.txt");

		File file = new File(this.systemPath + "/F-F_Research_Data_Factors_daily.txt");
		file.deleteOnExit();
		file = new File(this.systemPath + "/F-F_Momentum_Factor_daily.txt");
		file.deleteOnExit();
		file = new File(this.systemPath + "/F-F_Momentum_Factor_daily.zip");
		file.deleteOnExit();
		file = new File(this.systemPath + "/F-F_Research_Data_Factors_daily.zip");
		file.deleteOnExit();
		file = new File(this.systemPath + "/49_Industry_Portfolios_daily.zip");
		file.deleteOnExit();
		file = new File(this.systemPath + "/49_Industry_Portfolios_daily.txt");
		file.deleteOnExit();
	}

	/******************************************************************************************************/

	/*****************************************************************************************************/
	/*** Data Format of quickly last date daily data update *************************/
	/*** http://finance.yahoo.com/d/quotes.csv?s=AGG&f=sd1ohgvl1 *************************/
	/*** s=symbol d1=date o=open h=high g=low v=volume l1=latestPrice *************************/
	/*****************************************************************************************************/

	public void deleteFastFile(String path) {

		File rootFile = new File(systemPath + path);
		if (!rootFile.isDirectory())
			System.out.println(rootFile.mkdir());

		File[] files = rootFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			files[i].delete();
		}
	}

	/*****************************************************************************************/

	public void saveFastFile(List<String> names, int index, String path) {

		if (names == null || names.size() == 0)
			return;

		String sb = names.get(0);
		for (int i = 1, size = names.size(); i < size; i++)
			sb += "+" + names.get(i);

		String strUrl = "http://finance.yahoo.com/d/quotes.csv?s=" + sb + "&f=sd1ohgvl1";
		String strName = this.systemPath + path + "/" + index + ".txt";

		Date cur = new Date();

		try {

			URL url = new URL(strUrl);
			URLConnection urlConnection = (URLConnection) url.openConnection();
			urlConnection.setAllowUserInteraction(false);

			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			String inputLine;
			StringBuffer tempHtml = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				tempHtml.append(inputLine + "\n");
			}
			BufferedWriter buff = new BufferedWriter(new FileWriter(strName));
			for (int i = 0; i < tempHtml.length(); i++)
				buff.write(tempHtml.charAt(i));
			buff.flush();
			buff.close();

			// System.out.println(strName+" Success.");

			this.writeEoDLog(strName, cur, strName + " save to file success");
		} catch (Exception e) {

			log.debug("Error: " + e);
			System.out.println("Error:" + e);

			this.writeEoDLog(sb, cur, e.toString());

			try {

				URL url = new URL(strUrl);
				URLConnection urlConnection = (URLConnection) url.openConnection();
				urlConnection.setAllowUserInteraction(false);

				BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
				String inputLine;
				StringBuffer tempHtml = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					tempHtml.append(inputLine + "\n");
				}
				BufferedWriter buff = new BufferedWriter(new FileWriter(strName));
				for (int i = 0; i < tempHtml.length(); i++)
					buff.write(tempHtml.charAt(i));
				buff.flush();
				buff.close();

				// System.out.println(strName+" Success.");

				this.writeEoDLog(strName, cur, strName + " save to file success");
			} catch (Exception e2) {
				log.debug("Error: " + e2);
				System.out.println("Error:" + e2);
				this.writeEoDLog(sb, cur, e2.toString());
			}
		}

	}

	public Set<Long> uploadFastFile(String path, boolean nav) {
		Set<Long> securityIDSet = new HashSet<Long>();
		try {
			Date cur = new Date();
			path = this.systemPath + path + "/";
			File rootFile = new File(path);
			File[] files = rootFile.listFiles();

			int i, sum;
			sum = files.length;

			for (i = 0; i < sum; i++) {
				if (files[i].isFile()) {
					try {
						Set<Long> oneFileIDSet = uploadOneFastFile(path + files[i].getName(), nav);
						if (oneFileIDSet != null)
							securityIDSet.addAll(oneFileIDSet);
					} catch (Exception e) {
						this.writeEoDLog(files[i].getName(), cur, "Upload Error.");
						System.out.println("error in file " + i);
						continue;
					}
				}
			}
			log.info("All success.");
		} catch (Exception e) {
			log.debug("Error: " + e);
			e.printStackTrace();
		}
		return securityIDSet;
	}

	public Set<Long> uploadOneFastFile(String path, boolean nav) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			String line = br.readLine();
			SecurityDailyData sdd;
			Set<Long> securityIDSet = new HashSet<Long>();
			List<SecurityDailyData> sdds = new ArrayList<SecurityDailyData>();
			while (true) {
				if (line == null)
					break;
				{
					if (nav)
						sdd = this.parseOneNAVFastLine(line);
					else
						sdd = this.parseOneFastLine(line);
				}

				if (sdd != null) {
					sdds.add(sdd);
					securityIDSet.add(sdd.getSecurityID());
				}
				line = br.readLine();
			}
			securityManager.saveOrUpdateAll(sdds);
			if (nav)
				securityManager.updateSecurityNAVLastDate(sdds);
			else
				securityManager.updateSecurityEndDate(sdds);
			br.close();
			return securityIDSet;
		} catch (Exception e) {
			log.debug("Error: " + e);
			e.printStackTrace();

			try {
				BufferedReader br = new BufferedReader(new FileReader(path));
				String line = br.readLine();
				SecurityDailyData sdd;
				Set<Long> securityIDSet = new HashSet<Long>();
				List<SecurityDailyData> sdds = new ArrayList<SecurityDailyData>();
				while (true) {
					if (line == null)
						break;
					if (nav)
						sdd = this.parseOneNAVFastLine(line);
					else
						sdd = this.parseOneFastLine(line);
					if (sdd != null) {
						sdds.add(sdd);
						securityIDSet.add(sdd.getSecurityID());
					}
					line = br.readLine();
				}
				securityManager.saveOrUpdateAll(sdds);
				if (nav)
					securityManager.updateSecurityNAVLastDate(sdds);
				else
					securityManager.updateSecurityEndDate(sdds);
				br.close();
				return securityIDSet;
			} catch (Exception lastException) {
				System.out.println("Can not recover from pre Error!");
				this.writeEoDLog(e.toString(), new Date(), "Can not recover from pre Error!");
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 不能出现hole
	 * 
	 * @param line
	 * @return
	 */
	public SecurityDailyData parseOneFastLine(String line) {
		try {
			String[] k = this.split(line.trim());
			String symbol = k[0];

			if (k[1].equalsIgnoreCase("N/A"))
				return null;

			Date date = LTIDate.parseStringToDate(k[1]);
			if (date == null)
				return null;

			// date = LTIDate.clearHMSM(date);
			// Date today = new Date();
			// today = LTIDate.clearHMSM(today);
			// if(!LTIDate.equals(date, today))return null;

			if (k[6].equalsIgnoreCase("N/A")) {
				return null;
			} else {

				double price = Double.parseDouble(k[6]);
				double open = price;
				if (!k[2].equalsIgnoreCase("N/A"))
					open = Double.parseDouble(k[2]);
				double high = price;
				if (!k[3].equalsIgnoreCase("N/A"))
					high = Double.parseDouble(k[3]);
				double low = price;
				if (!k[4].equalsIgnoreCase("N/A"))
					low = Double.parseDouble(k[4]);
				long volume = 0;
				if (!k[5].equalsIgnoreCase("N/A"))
					volume = Long.parseLong(k[5]);

				Security se = securityManager.getBySymbol(symbol);
				if (se == null || ((se.getPriceLastDate() != null) && !date.after(se.getPriceLastDate())))
					return null;

				/**************************** 防止出现hole ***********************/
				Date expectedDate = LTIDate.getNewNYSETradingDay(se.getEndDate(), 1);
				if (!LTIDate.equals(expectedDate, date))
					return null;
				/**************************** 防止出现hole ***********************/

				SecurityDailyData pre = securityManager.getLatestDailydata(se.getID());
				if (pre != null && pre.getDate() != null && date.before(pre.getDate()))
					return null;

				SecurityDailyData sdd;
				if (LTIDate.equals(date, pre.getDate()))
					sdd = pre;
				else
					sdd = new SecurityDailyData();

				sdd.setSecurityID(se.getID());
				sdd.setClose(price);
				sdd.setDate(date);
				sdd.setDividend(0.0);// make sure dividend == 0.0
				sdd.setHigh(high);
				sdd.setLow(low);
				sdd.setVolume(volume);
				sdd.setOpen(open);
				sdd.setSplit(1.0); // make sure split = 1.0

				/*******************************************************************/
				double adjClose = price;
				double split = 1;
				double todayDividend = 1;
				double totalDividend = 1;
				double preAdj = 0;
				if (pre != null && pre.getAdjClose() != null) {
					preAdj = pre.getAdjClose();
					totalDividend = todayDividend * preAdj;
				}
				if (pre != null && pre.getClose() != null && pre.getClose() != 0)
					totalDividend /= pre.getClose();

				adjClose = totalDividend * split * price;
				/*******************************************************************/
				sdd.setAdjClose(adjClose);
				this.writeEoDLog(symbol, new Date(), symbol + " load to database success.");
				// 将更新成功的表加入到log中
				this.writeFastUpdateLog(updateNum, "success", "PRICE", symbol, date);
				return sdd;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(line);
		}
		return null;
	}

	public SecurityDailyData parseOneNAVFastLine(String line) {
		try {
			String[] k = this.split(line.trim());
			String symbol = k[0].substring(1, k[0].length() - 1);

			if (k[1].equalsIgnoreCase("N/A"))
				return null;

			Date date = LTIDate.parseStringToDate(k[1]);
			if (date == null)
				return null;

			date = LTIDate.clearHMSM(date);
			// Date today = new Date();
			// today = LTIDate.clearHMSM(today);
			// if(!LTIDate.equals(date, today))return null;

			if (k[6].equalsIgnoreCase("N/A")) {
				return null;
			} else {

				double nav = Double.parseDouble(k[6]);

				Security se = securityManager.getBySymbol(symbol);
				if (se == null || ((se.getNavLastDate() != null) && !date.after(se.getNavLastDate())))
					return null;

				/**************************** 防止出现hole ***********************/
				Date expectedDate = LTIDate.getNewNYSETradingDay(se.getNavLastDate(), 1);
				if (!LTIDate.equals(expectedDate, date))
					return null;
				/**************************** 防止出现hole ***********************/
				// 取出nav last date 的security daily data
				SecurityDailyData pre = securityManager.getDailydata(se.getID(), se.getNavLastDate(), false);
				if (pre == null)
					return null;
				// 取出date 的security daily data
				SecurityDailyData sdd = securityManager.getDailydata(se.getID(), date, false);
				if (sdd != null) {
					sdd.setNAV(nav);
				} else {
					sdd = new SecurityDailyData();
					sdd.setNAV(nav);
					sdd.setSecurityID(se.getID());
					sdd.setDate(date);
					sdd.setDividend(0.0);// make sure dividend == 0.0
					sdd.setHigh(0.0);
					sdd.setLow(0.0);
					sdd.setVolume(0L);
					sdd.setOpen(0.0);
					sdd.setSplit(1.0); // make sure split = 1.0
				}

				/*******************************************************************/
				double adjNAV = nav;
				double split = 1;
				double todayDividend = 1;
				double totalDividend = 1;
				double preAdj = 0;
				if (pre != null && pre.getAdjNAV() != null) {
					preAdj = pre.getAdjNAV();
					totalDividend = todayDividend * preAdj;
				}
				if (pre != null && pre.getNAV() != null && pre.getNAV() != 0)
					totalDividend /= pre.getNAV();

				adjNAV = totalDividend * split * nav;
				/*******************************************************************/
				sdd.setAdjNAV(adjNAV);
				this.writeEoDLog(symbol, new Date(), symbol + " load to database success.");
				// 将更新成功的表加入到log中
				this.writeFastUpdateLog(updateNum, "success", "NAV", symbol, date);
				return sdd;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(line);
		}
		return null;
	}

	public void writeFastUpdateLog(int num, String state, String type, String symbol, Date endDate) {
		try {
			Date currentTime = new Date();
			FileOutputStream stream = new FileOutputStream(this.fastUpdateDetailLogFile, true);
			OutputStreamWriter writer = new OutputStreamWriter(stream);
			writer.write(state + "," + type + "," + symbol + "," + LTIDate.parseDateToString(endDate) + "," + currentTime + "\n");
			writer.close();
			stream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void fastUpdateDailyData(String path, int uNum) throws FileNotFoundException {

		Date today = new Date();
		updateNum = uNum;
		this.fastUpdateDetailLogFile = getSystemPath() + LTIDate.parseDateToString(today) + "fastdetail" + updateNum + ".csv";
		this.fastUpdateSimpleLogFile = getSystemPath() + LTIDate.parseDateToString(today) + "fastsimple" + updateNum + ".csv";

		this.writeEoDLog("Fast Daily Data upDate", today, "\n****************************************\nFast Daily Data upDate started!\n****************************************\n");
		System.out.println("Start Fast Daily Data upDate!");
		long startTime = System.currentTimeMillis();

		List<Security> securityList = new ArrayList<Security>();
		List<Security> toBeUpdatePriceList = new ArrayList<Security>();
		List<Security> toBeUpdateNAVList = new ArrayList<Security>();
		Set<Long> updateSecurityIDSet = new HashSet<Long>();
		// 只拿出没有更新到当天的那部分security
		securityList = securityManager.getSecuritiesBeforeEndDate(today);
		int successPriceCount = 0, failPriceCount = 0, successNAVCount = 0, failNAVCount = 0;
		try {
			for (int i = 0, size = securityList.size(); i < size; i++) {

				Security tempSecurity = securityList.get(i);
				if (tempSecurity == null || (tempSecurity.getIsClosed() != null && tempSecurity.getIsClosed()) || tempSecurity.getSymbol() == null || tempSecurity.getSecurityType() == 6 || (tempSecurity.getManual() != null && tempSecurity.getManual() == 1))
					continue;

				String symbol = tempSecurity.getSymbol();

				if (symbol.equalsIgnoreCase("CASH") || symbol.equalsIgnoreCase("CN CASH") || symbol.equalsIgnoreCase("TSPGFUND"))
					continue;

				if (symbol.indexOf("^FF") == 0)
					continue;

				if (tempSecurity.getSecurityType() == Configuration.SECURITY_TYPE_CLOSED_END_FUND && tempSecurity.getNavLastDate() != null && LTIDate.before(tempSecurity.getNavLastDate(), today))
					toBeUpdateNAVList.add(tempSecurity);
				if (tempSecurity.getEndDate() != null && LTIDate.before(tempSecurity.getEndDate(), today))
					toBeUpdatePriceList.add(tempSecurity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int downLoadIndex = 1;
		// 开始quote security price
		this.deleteFastFile(path);
		if (toBeUpdatePriceList != null && toBeUpdatePriceList.size() > 0) {
			List<String> symbolList = new ArrayList<String>();
			for (int i = 0, sum = 0, size = toBeUpdatePriceList.size(); i < size; ++i) {
				symbolList.add(toBeUpdatePriceList.get(i).getSymbol());
				++sum;
				if (sum >= 200 || i == size - 1) {
					this.saveFastFile(symbolList, downLoadIndex, path);
					downLoadIndex++;
					symbolList.clear();
					sum = 0;
				}
			}
		}
		this.writeEoDLog("Fast Daily Update: ", new Date(), " DownLoad Price Successfully.");
		// 开始upload security price
		try {
			updateSecurityIDSet = this.uploadFastFile(path, false);
		} catch (Exception e) {
			this.writeEoDLog("Fast Daily Update: ", new Date(), " Upload Fail.\n");
			System.out.println("Fast Daily update Error:");
			e.printStackTrace();
		}
		// 检查哪些列入更新范围但没有更新的
		successPriceCount = updateSecurityIDSet.size();
		for (int i = 0, size = toBeUpdatePriceList.size(); i < size; i++) {
			if (updateSecurityIDSet == null || !updateSecurityIDSet.contains(toBeUpdatePriceList.get(i).getID())) {// 写入没有完成date的
				++failPriceCount;
				this.writeFastUpdateLog(updateNum, "fail", "PRICE", toBeUpdatePriceList.get(i).getSymbol(), toBeUpdatePriceList.get(i).getEndDate());
			}
		}
		// 之前下载的都是price
		this.deleteFastFile(path);
		// 开始quote security NAV
		if (toBeUpdateNAVList != null && toBeUpdateNAVList.size() > 0) {
			List<String> symbolList = new ArrayList<String>();
			for (int i = 0, sum = 0, size = toBeUpdateNAVList.size(); i < size; ++i) {
				Security tempSecurity = toBeUpdateNAVList.get(i);
				symbolList.add("X" + tempSecurity.getSymbol() + "X");
				++sum;
				if (sum >= 200 || i == size - 1) {
					this.saveFastFile(symbolList, downLoadIndex, path);
					downLoadIndex++;
					symbolList.clear();
					sum = 0;
				}
			}
		}
		// 开始upload security nav
		try {
			updateSecurityIDSet = this.uploadFastFile(path, true);
		} catch (Exception e) {
			this.writeEoDLog("Fast Daily Update: ", new Date(), " Upload Fail.\n");
			System.out.println("Fast Daily update Error:");
			e.printStackTrace();
		}
		successNAVCount = updateSecurityIDSet.size();
		for (int i = 0, size = toBeUpdateNAVList.size(); i < size; i++) {
			if (updateSecurityIDSet == null || !updateSecurityIDSet.contains(toBeUpdateNAVList.get(i).getID())) {// 写入没有完成update的
				++failNAVCount;
				this.writeFastUpdateLog(updateNum, "fail", "NAV", toBeUpdateNAVList.get(i).getSymbol(), toBeUpdateNAVList.get(i).getEndDate());
			}
		}
		// 开始更新CASH
		try {
			this.updateCash("CASH", "^IRX");
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 开始更新TSPGFUND
		try {
			this.updateTSPGFund("TSPGFUND", "^TNX", "^FVX", false);
		} catch (Exception e) {
			this.writeLog("UpdateDailyData: update TSPGFUND error", today, e.toString());
		}
		try {
			this.updateSpecialFund("QQQQ", "QQQ");
		} catch (Exception e) {
			this.writeLog("UpdateDailyData: update QQQQ error", today, e.toString());
		}
		// 开始更新SP
		try {
			this.downLoadAndUpdateSP();
		} catch (Exception e) {
			this.writeLog("UpdateDailyData: update GSPC PE error", today, e.toString());
		}
		long endTime = System.currentTimeMillis();
		this.writeEoDLog("ALL Security(Fast update daily data)", new Date(), " All upload to database success");
		this.writeEoDLog("All security", new Date(), "Total Time is: " + (endTime - startTime));
		try {
			FileOutputStream stream = new FileOutputStream(this.fastUpdateSimpleLogFile, true);
			OutputStreamWriter writer = new OutputStreamWriter(stream);
			writer.write(LTIDate.getDate(startTime) + "," + LTIDate.getDate(endTime) + "," + successPriceCount + "," + failPriceCount + "," + successNAVCount + "," + failNAVCount);
			writer.close();
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void writeEoDLog(String name, Date endDate, String log) {
		try {
			Date time = new Date();
			FileOutputStream stream = new FileOutputStream(this.eodlogFile, true);
			OutputStreamWriter writer = new OutputStreamWriter(stream);
			writer.write(name + " :Check time is " + time + "; Event time is:" + endDate + "; Log Information:" + log + "\n");
			writer.close();
			stream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Html parser class function: parse NAV html document author : chaos
	 */
	class NAVHtmlTokenizer extends StreamTokenizer {

		static final int HTML_TEXT = -1;
		static final int HTML_UNKNOWN = -2;
		static final int HTML_EOF = -3;
		static final int HTML_IMAGE = -4;
		static final int HTML_FRAME = -5;
		static final int HTML_BACKGROUND = -6;
		static final int HTML_APPLET = -7;
		static final int HTML_DATA = -8;

		boolean outsideTag = true;

		public NAVHtmlTokenizer(BufferedReader r) {
			super(r);
			this.resetSyntax();
			this.wordChars(0, 255);
			int a = '<';
			this.ordinaryChar(a);
			a = '>';
			this.ordinaryChar(a);
		} // end of constructor

		public int nextHtml() {
			try {
				switch (this.nextToken()) {
				case StreamTokenizer.TT_EOF:

					return HTML_EOF;
				case '<':
					outsideTag = false;
					return nextHtml();
				case '>': // out
					outsideTag = true;
					return nextHtml();
				case StreamTokenizer.TT_WORD:

					if (allWhite(sval))
						return nextHtml();

					else if (sval.toUpperCase().indexOf("FRAME") != -1 && !outsideTag) // 标记FRAME
						return HTML_FRAME;

					else if (sval.toUpperCase().indexOf("IMG") != -1 && !outsideTag) // 标记IMG
						return HTML_IMAGE;

					else if (sval.toUpperCase().indexOf("BACKGROUND") != -1 && !outsideTag)
						return HTML_BACKGROUND;

					else if (sval.toUpperCase().indexOf("APPLET") != -1 && !outsideTag) // 标记APPLET
						return HTML_APPLET;

					else if (sval.equalsIgnoreCase("td class=\"ChartDataCol\"") && !outsideTag)
						return HTML_DATA;

				default:

					return HTML_UNKNOWN;
				} // end of case

			} catch (IOException e) {
				System.out.println("Error:" + e.getMessage());
			}
			return HTML_UNKNOWN;
		} // end of nextHtml

		protected boolean allWhite(String s) {
			if (s.trim().length() == 0)
				return true;
			else
				return false;
		}// end of allWhite

	}

	/****************************************************************************************************/
	/**** CBOE Put/Call Ratio Indicators ****/
	/**** http://www.cboe.com/publish/ScheduledTask/MktData/datahouse ****/
	/***************************************************************************************************/

	public void downloadCBOEIndicator() {
		try {
			/* download the CBOE Total Put/Call Ratio */
			String[] strUrls = { "http://www.cboe.com/publish/ScheduledTask/MktData/datahouse/totalpc.csv", "http://www.cboe.com/publish/ScheduledTask/MktData/datahouse/indexPC.csv", "http://www.cboe.com/publish/ScheduledTask/MktData/datahouse/equityPC.csv" };
			String[] strNames = { "/totalpc.csv", "/indexPC.csv", "/equityPC.csv" };
			for (int i = 0; i < 3; i++) {
				String strUrl = strUrls[i];
				String strName = this.systemPath + strNames[i];

				URL url = new URL(strUrl);
				URLConnection urlConnection = (URLConnection) url.openConnection();
				urlConnection.setAllowUserInteraction(false);

				urlConnection.connect();

				FileOutputStream fos = null;
				BufferedInputStream bis = null;

				bis = new BufferedInputStream(urlConnection.getInputStream());
				fos = new FileOutputStream(strName);

				// System.out.println("Getting URL and Downloading the file...");

				int size = 0;
				byte[] buf = new byte[BUFFER_SIZE];

				while ((size = bis.read(buf)) != -1)
					fos.write(buf, 0, size);

				fos.close();
				bis.close();

				// System.out.println(strName+" Success.");
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadCBOEIndicator(String path) {
		/* select the date and put/call ratios from the csv file */
		List<String> tmp = new ArrayList();
		String filePath = path;
		String ecom;

		/* read the downloaded file */
		try {
			File f = new File(filePath);
			BufferedReader br = new BufferedReader(new FileReader(f));
			String line = "";
			line = br.readLine();
			while ((line = br.readLine()) != null) {
				if (line.charAt(0) > 47 && line.charAt(0) < 58) {
					String[] data = line.split(",");

					/* change the format of date to "yyyy-mm-dd" */
					String[] tmpD = data[0].split("/");
					data[0] = tmpD[2] + "-" + tmpD[0] + "-" + tmpD[1];
					tmp.add(data[0].trim() + "," + data[data.length - 1].trim() + "," + "\n");
				}
			}
			br.close();
			f.delete();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		/* write the date and value columns into the original file */
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;
		try {
			fos = new FileOutputStream(filePath);
			osw = new OutputStreamWriter(fos, "utf-8");
			bw = new BufferedWriter(osw);
			bw.write("DATE,VALUE\r\n");
			for (int i = 0; i < tmp.size(); i++) {
				bw.write(tmp.get(i));
			}
			bw.flush();
			bw.close();
			osw.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (filePath.contains("totalpc"))
			ecom = "CBOE-T";
		else if (filePath.contains("indexPC"))
			ecom = "CBOE-I";
		else
			ecom = "CBOE-E";

		try {
			dataManager.updateEcom2(filePath, ecom);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * update daily factor beta gain details
	 * 
	 * @author CCD
	 */
	public void updateDailyFactorBetaGain() {
		/** part 17: update daily factor beta gain **/
		try {
			mutualFundManager.updateDailyFactorBetaGainForAllFunds();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * update historical beta gain ranking
	 * 
	 * @author CCD
	 */
	public void updateMonthlyHistoricalBetaGain() {
		/** part 18: update monthly beta gain ranking **/
		try {
			mutualFundManager.updateHistoricalBetaGainForAllFunds();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void testUpdateDailyData(List<Security> securityList) {
		// List<String> sList = this.getNameString();

		System.out.println("****start update daily data*****");

		Date cur = new Date();
		this.logDate = cur;
		this.writeLog("ALL Security UpDate", cur, "\n************************************\n start update daily data\n************************************\n");

		try {

			List<String> symbols = new ArrayList<String>();
			HashMap<String, Date> lastDailyDataMap = new HashMap<String, Date>();
			HashMap<String, Date> lastDividendDataMap = new HashMap<String, Date>();

			// List<Security> securityList = securityManager.getSecurities();
			SecurityManager sm = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
			// securityList = securityList.subList(0, 400);
			List<Date> navEndDateList = new ArrayList<Date>();

			List<Date> fundEndDateList = new ArrayList<Date>();
			List<String> fundList = new ArrayList<String>();

			Date date = new Date();
			Date date2 = new Date();
			date2 = LTIDate.getDate(1900, 0, 0);

			// delete update files
			this.deleteFile(true);

			List<String> seList = new ArrayList<String>();

			List<String> navList = new ArrayList<String>();

			/**** part 1:update cef & dividend ***/
			int totalCount = securityList.size();
			for (int i = 0; i < securityList.size(); i++) {
				System.out.println("update dividend :" + i + "  " + totalCount);
				if (i == securityList.size())
					break;

				Security tempSecurity = securityList.get(i);

				System.out.println(tempSecurity.getSymbol());
				// Security tempSecurity =
				// securityManager.getBySymbol(sList.get(i));
				if (tempSecurity == null)
					continue;

				if (tempSecurity.getSymbol() == null) {
					this.writeLog("Unknown Security", date, "security has no symbol");
					continue;
				}
				if (tempSecurity.getSecurityType() == null) {
					this.writeLog(tempSecurity.getSymbol(), date, "security has no security type");
					continue;
				}

				String symbol = tempSecurity.getSymbol();

				if (tempSecurity.getSecurityType() == 6)
					continue;

				// System.out.println(symbol+" "+i);

				/********************************************************************/
				// if(!symbol.equalsIgnoreCase("GLD") )continue;
				/*
				 * if(tempSecurity.getPriceLastDate()!=null &&
				 * tempSecurity.getPriceLastDate().getYear()+1900==2009) {
				 * tempSecurity.setDividendLastDate(null);
				 * tempSecurity.setPriceLastDate(null);
				 * tempSecurity.setNavLastDate(null);
				 * securityManager.saveOrUpdate(tempSecurity);
				 * securityManager.clearDailyData(tempSecurity.getID()); } else
				 * if(tempSecurity.getPriceLastDate()==null) {
				 * tempSecurity.setDividendLastDate(null);
				 * tempSecurity.setPriceLastDate(null);
				 * tempSecurity.setNavLastDate(null);
				 * securityManager.saveOrUpdate(tempSecurity);
				 * securityManager.clearDailyData(tempSecurity.getID()); }
				 * 
				 * /*************************************************************
				 * ********
				 */

				if (symbol.equalsIgnoreCase("CASH") || symbol.equalsIgnoreCase("CN CASH")) {
					continue;
				}

				int type = this.getUpdateType(symbol);

				if (type != this.NORMAL_SECURITY) {
					// Date tmpDate =
					// securityManager.getDailyDataLastDate(tempSecurity.getID());
					Date tmpDate = tempSecurity.getPriceLastDate();
					if (tmpDate == null)
						tmpDate = securityManager.getDailyDataLastDate(tempSecurity.getID());
					fundList.add(symbol);
					fundEndDateList.add(tmpDate);
					continue;
				}

				try {
					long id = tempSecurity.getID();

					SecurityDailyData staticData = new SecurityDailyData();

					// Date dividendDate =
					// securityManager.getDividendLastDate(id);
					Date dividendDate = tempSecurity.getDividendLastDate();
					/*** don't go through the security daily data table */
					// if(dividendDate == null)dividendDate =
					// securityManager.getDividendLastDate(id);
					if (dividendDate == null)
						dividendDate = date2;
					dividendDate = LTIDate.add(dividendDate, 1);

					// Date dailyDataDate =
					// securityManager.getDailyDataLastDate(id);
					Date dailyDataDate = tempSecurity.getPriceLastDate();
					/*** don't go through the security daily data table */
					// if(dailyDataDate == null)dailyDataDate =
					// securityManager.getDailyDataLastDate(id);
					if (dailyDataDate == null)
						dailyDataDate = date2;

					if (tempSecurity.getSecurityType() == 2 && type == this.NORMAL_SECURITY) {
						// System.out.println("yes "+symbol+" "+i);

						// Date navDate = securityManager.getNAVLastDate(id);
						Date navDate = tempSecurity.getNavLastDate();
						/*** don't go through the security daily data table */
						// if(navDate == null)navDate =
						// securityManager.getNAVLastDate(id);
						if (navDate == null)
							navDate = date2;
						this.saveCEFToFile(symbol, navDate, date, "n", true);
						navList.add(symbol);
						navEndDateList.add(navDate);
					}

					// saveToFile(symbol, dailyDataDate, date, "d",true);//save
					// daily data

					saveToFile(symbol, dividendDate, date, "v", true);// save
																		// dividends

					symbols.add(symbol);
					lastDailyDataMap.put(symbol, dailyDataDate);

					Thread.sleep(500);
				} catch (Exception e) {

					this.writeLog(symbol, date, "DownLoadError");
					this.writeLog(symbol, date, e.toString());

					e.printStackTrace();
				}
			}

			System.out.println("upload dividend");
			/**** part 2:upload dividend ***/
			DailyUpdateControler.state = "Upload Dividend";
			lastDividendDataMap = upLoadFile("v", false, true);
			HashMap<String, Date> newLastDailyDataMap = new HashMap<String, Date>();
			System.out.println("update daily price");
			/**** part 3:update daily price ***/
			DailyUpdateControler.state = "Update Daily Price";
			DailyUpdateControler.total_count = symbols.size();
			totalCount = symbols.size();
			for (int i = 0; i < symbols.size(); i++) {
				DailyUpdateControler.current_count = i + 1;
				String symbol = symbols.get(i);
				Date dividendDataDate = lastDividendDataMap.get(symbol);
				Date dailyDataDate = lastDailyDataMap.get(symbol);
				if (dividendDataDate == null)
					dividendDataDate = dailyDataDate;
				else {
					if (dailyDataDate.before(dividendDataDate))
						dailyDataDate = dailyDataDate;
					else
						dailyDataDate = dividendDataDate;
				}

				newLastDailyDataMap.put(symbol, dailyDataDate);
				dailyDataDate = LTIDate.getNewTradingDate(dailyDataDate, TimeUnit.DAILY, -1);
				saveToFile(symbol, dailyDataDate, date, "d", true);
			}
			System.out.println("upload daily price");
			// when update fromStart = false, for reload database fromStart =
			// true;
			/**** part 4:upload daily price ***/
			DailyUpdateControler.state = "Upload Daily Price";
			upLoadFileAndAdjust(false, true, newLastDailyDataMap, false);

			System.out.println("upload nav");
			/**** part 5:upload nav ***/
			DailyUpdateControler.state = "Upload Nav";
			this.uploadNAV(true);
			System.out.println("update nav");
			/**** part 6:update adj nav ***/
			DailyUpdateControler.state = "Update AdjNav";
			this.updateAdjNAV(navList, navEndDateList);

			System.out.println("Done.");

			this.writeLog("All Security", date, "\n************************************\nFinish all update data.\n************************************\n");

		} catch (Exception e) {
			log.debug("Error: " + e);
			Date date = new Date();
			this.writeLog("UpdateDailyData", date, e.toString());
			e.printStackTrace();

		}
	}

	/**
	 * get the enddate from yahoo
	 * 
	 * @param symbol
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public Date getLastDailyDataDateFromYahooBySymbol(String symbol, Date startDate, Date endDate) {
		String strUrl = getUrl(symbol, startDate, endDate, "d");
		URL url;
		try {
			url = new URL(strUrl);
			URLConnection urlConnection = (URLConnection) url.openConnection();
			urlConnection.setAllowUserInteraction(false);
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			String inputLine;
			StringBuffer tempHtml = new StringBuffer();
			// read the header
			inputLine = in.readLine();
			// read the first data to get the enddate
			if ((inputLine = in.readLine()) != null) {
				String dateStr = inputLine.split(",")[0];
				if (dateStr != null)
					return LTIDate.parseStringToDate(dateStr);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void simulateDailyUpdate() {

		System.out.println("****start update daily data*****");
		long t1 = System.currentTimeMillis();
		long t2;
		Date cur = new Date();
		this.logDate = cur;
		this.writeLog("ALL Security UpDate", cur, "\n************************************\n start update daily data\n************************************\n");

		try {

			List<String> symbols = new ArrayList<String>();
			HashMap<String, Date> lastDailyDataMap = new HashMap<String, Date>();
			HashMap<String, Date> lastDividendDataMap = new HashMap<String, Date>();

			SecurityManager sm = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
			Date curDate = new Date();
			DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Security.class);
			detachedCriteria.add(Restrictions.ne("SecurityType", 6));
			int day = curDate.getDay();
			if (day != 6)
				detachedCriteria.add(Restrictions.ne("SecurityType", 5));

			Security security = securityManager.get(851L);
			List<Security> securityList = securityManager.getSecurities(detachedCriteria);
			securityList = securityList.subList(0, 50);
			securityList.add(security);
			int size = securityList.size();
			for (int i = 0; i < size; ++i) {
				security = securityList.get(i);
				System.out.println(security.getSymbol());
				Security benchmark = sm.get(security.getAssetClass().getBenchmarkID());
				securityList.add(benchmark);
			}
			List<Date> navEndDateList = new ArrayList<Date>();

			Date date = new Date();
			Date date2 = new Date();
			date2 = LTIDate.getDate(1900, 0, 0);

			// delete update files
			this.deleteFile(true);

			List<String> seList = new ArrayList<String>();

			List<String> navList = new ArrayList<String>();
			/**** part 1:update cef & dividend ***/
			for (int i = 0; i < securityList.size(); i++) {
				DailyUpdateControler.current_count = i + 1;
				if (i == securityList.size())
					break;

				Security tempSecurity = securityList.get(i);
				if (tempSecurity == null)
					continue;
				if (tempSecurity.getManual() != null && tempSecurity.getManual() == 1)
					continue;

				if (tempSecurity.getSymbol() == null) {
					this.writeLog("Unknown Security", date, "security has no symbol");
					continue;
				}

				String symbol = tempSecurity.getSymbol();

				if (symbol.equalsIgnoreCase("CASH") || symbol.equalsIgnoreCase("CN CASH")) {
					continue;
				}

				int type = this.getUpdateType(symbol);

				try {

					Date dividendDate = tempSecurity.getDividendLastDate();
					/*** don't go through the security daily data table */
					if (dividendDate == null)
						dividendDate = date2;
					dividendDate = LTIDate.add(dividendDate, 1);

					Date dailyDataDate = tempSecurity.getPriceLastDate();
					/*** don't go through the security daily data table */
					if (dailyDataDate == null)
						dailyDataDate = date2;

					if (tempSecurity.getSecurityType() == 2 && type == this.NORMAL_SECURITY) {

						Date navDate = tempSecurity.getNavLastDate();
						/*** don't go through the security daily data table */
						if (navDate == null)
							navDate = date2;
						this.saveCEFToFile(symbol, navDate, date, "n", true);
						navList.add(symbol);
						navEndDateList.add(navDate);
					}

					saveToFile(symbol, dividendDate, date, "v", true);// save
																		// dividends
					symbols.add(symbol);
					lastDailyDataMap.put(symbol, dailyDataDate);

					Thread.sleep(500);
				} catch (Exception e) {

					this.writeLog(symbol, date, "DownLoadError");

					e.printStackTrace();
				}
			}

			/**** part 2:upload dividend ***/
			DailyUpdateControler.state = "Upload Dividend";
			lastDividendDataMap = upLoadFile("v", false, true);
			HashMap<String, Date> newLastDailyDataMap = new HashMap<String, Date>();

			/**** part 3:update daily price ***/
			DailyUpdateControler.state = "Update Daily Price";
			DailyUpdateControler.total_count = symbols.size();
			for (int i = 0; i < symbols.size(); i++) {
				DailyUpdateControler.current_count = i + 1;
				String symbol = symbols.get(i);
				Date dividendDataDate = lastDividendDataMap.get(symbol);
				Date dailyDataDate = lastDailyDataMap.get(symbol);
				if (dividendDataDate == null)
					dividendDataDate = dailyDataDate;
				else {
					if (dailyDataDate.before(dividendDataDate))
						dailyDataDate = dailyDataDate;
					else
						dailyDataDate = dividendDataDate;
				}

				newLastDailyDataMap.put(symbol, dailyDataDate);
				dailyDataDate = LTIDate.getNewTradingDate(dailyDataDate, TimeUnit.DAILY, -1);
				saveToFile(symbol, dailyDataDate, date, "d", true);
			}

			// when update fromStart = false, for reload database fromStart =
			// true;
			/**** part 4:upload daily price ***/
			DailyUpdateControler.state = "Upload Daily Price";
			upLoadFileAndAdjust(false, true, newLastDailyDataMap, false);

			/**** part 5:upload nav ***/
			DailyUpdateControler.state = "Upload Nav";
			this.uploadNAV(true);

			/**** part 6:update adj nav ***/
			DailyUpdateControler.state = "Update AdjNav";
			this.updateAdjNAV(navList, navEndDateList);

			/**** part 7:update cash ***/
			DailyUpdateControler.state = "Update Cash";
			DailyUpdateControler.total_count = 1;
			DailyUpdateControler.current_count = 1;
			this.updateCash("CASH", "^IRX");

			t2 = System.currentTimeMillis();
			System.out.println("Update 400 securities\nCost Time: " + (t2 - t1));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @author CCD
	 * @param today
	 *            download dividends from yahoo
	 */
	public void downLoadDividendAndNAV(Date today, List<Security> securityList, boolean isWeekEnd) {
		Date initialStartDate = new Date();
		initialStartDate = LTIDate.getDate(1900, 0, 0);

		int type, totalCount = securityList.size();
		Security security;
		String symbol;
		for (int i = 0; i < totalCount; ++i) {
			security = securityList.get(i);
			if (security == null)
				continue;
			symbol = security.getSymbol();
			type = this.getUpdateType(symbol);
			Date lastDividendDate = security.getDividendLastDate();
			Date lastNAVDate = security.getNavLastDate();
			try {
				if (lastDividendDate == null) {
					if (isWeekEnd)
						saveToFile(symbol, initialStartDate, today, "v", true);// save
																				// dividends
				} else
					saveToFile(symbol, lastDividendDate, today, "v", true);// save
																			// dividends
				if (security.getSecurityType() == 2 && type == this.NORMAL_SECURITY) {
					if (lastNAVDate == null) {
						if (isWeekEnd)
							saveCEFToFile(symbol, initialStartDate, today, "n", true);
					} else
						saveCEFToFile(symbol, lastNAVDate, today, "n", true);
				}
			} catch (Exception e) {
				writeLog(symbol, today, "DownLoadError");
				writeLog(symbol, today, e.toString());
			}
		}
		writeLog("DownLoad dividends & NAV", today, String.valueOf(totalCount));
	}

	private void setSecurityType(Security se, String securityType) {
		if (securityType.equalsIgnoreCase("CEF"))
			se.setSecurityType(Configuration.SECURITY_TYPE_CLOSED_END_FUND);
		else if (securityType.equalsIgnoreCase("ETF"))
			se.setSecurityType(Configuration.SECURITY_TYPE_ETF);
		else if (securityType.equalsIgnoreCase("FUND"))
			se.setSecurityType(Configuration.SECURITY_TYPE_MUTUAL_FUND);
		else if (securityType.equalsIgnoreCase("STOCK"))
			se.setSecurityType(Configuration.SECURITY_TYPE_STOCK);
	}

	public Security getSecurityInformation(String symbol, Date logDate, boolean overWrite) {
		AssetClassManager am = (AssetClassManager) ContextHolder.getInstance().getApplicationContext().getBean("assetClassManager");
		Security se = securityManager.getBySymbol(symbol);
		boolean isIndex = symbol.startsWith("^");
		if (se == null) {
			se = new Security();
			se.setSymbol(symbol);
			se.setManual(0);
			overWrite = true;
		} else {
			se.setManual(0);
			se.setEndDate(null);
			se.setStartDate(null);
			se.setMptLastDate(null);
			se.setPriceLastDate(null);
			se.setNavLastDate(null);
			se.setDividendLastDate(null);
			securityManager.deleteSecurityDataCascade(se.getID());
		}
		boolean morningstarFound = false;
		try {
			if (overWrite) {
				String html = "html";
				int num = 0;
				int beginIndex, endIndex;
				while (html.equals("html") && num <= 30) {
					html = ParseHtmlTable.getHtml("http://quote.morningstar.com/switch.html?ticker=" + symbol);
					++num;
					if (html == null)
						html = "html";
				}
				// TICKERNOTFOUND表示没有找到该FUND以及类似的
				// TICKERFOUND表示没有找到该FUND但找到类似的
				if (html.contains("TICKERFOUND") || html.contains("TICKERNOTFOUND")) {
					if (!isIndex)
						return null;
					else {
						se.setSecurityType(Configuration.SECURITY_TYPE_INDEX);
						se.setClassID(0L);
						se.setManual(0);
						securityManager.saveOrUpdate(se);
						return se;
					}
				} else
					morningstarFound = true;
				if (morningstarFound) {
					/******************** security type *********************/
					beginIndex = html.indexOf("http://quote.morningstar.com/") + 29;
					endIndex = html.indexOf('/', beginIndex);
					String securityType = html.substring(beginIndex, endIndex);
					setSecurityType(se, securityType);
					/******************** security name *********************/
					String name = "";
					if (se.getSecurityType() != null) {
						if (se.getSecurityType() == Configuration.SECURITY_TYPE_STOCK) {
							beginIndex = html.indexOf("Name") + 5;
							beginIndex = html.indexOf("Name", beginIndex) + 5;
							beginIndex = html.indexOf("Name", beginIndex) + 5;
							beginIndex = html.indexOf("Name", beginIndex) + 6;
							endIndex = html.indexOf('\"', beginIndex);
							name = html.substring(beginIndex, endIndex);
						} else {
							beginIndex = html.indexOf("description");
							if (beginIndex != -1) {
								beginIndex += 22;
								endIndex = html.indexOf('(', beginIndex);
								name = html.substring(beginIndex, endIndex);
							} else
								name = "";
						}
						se.setName(name);
						/******************** asset class name *********************/
						if (se.getSecurityType() == 5) {
							se.setClassID(0L);
						} else {
							beginIndex = html.indexOf("Name", 0) + 7;
							endIndex = html.indexOf("\"", beginIndex);
							// System.out.println(beginIndex + " " + endIndex);
							// System.out.println(html.substring(beginIndex,
							// endIndex));
							// assetClassName.toCharArray();
							char[] c = html.substring(beginIndex, endIndex).toCharArray();
							String assetClassName = "";
							for (int i = 0; i < c.length; ++i) {
								if (c[i] != '\\')
									assetClassName += c[i];
							}
							assetClassName.replace("\\", "");
							// System.out.println(assetClassName);
							// morning star找不到asset class
							if (assetClassName.equals("") || assetClassName.equals("ull,") || assetClassName.equals("rt"))
								se.setClassID(0L);
							else {
								AssetClass assetClass = am.get(assetClassName);
								if (assetClass == null) {
									assetClass = new AssetClass();
									assetClass.setName(assetClassName);
									assetClass.setBenchmarkID(91L);
									assetClass.setParentID(0L);
									assetClassManager.add(assetClass);
								}
								se.setClassID(assetClass.getID());
							}
						}
					} else {
						se.setSecurityType(0);
						se.setClassID(0L);
					}
				}
				/******************** manual *********************/
				se.setManual(0);
				if (morningstarFound) {
					securityManager.saveOrUpdate(se);
					return se;
				}
			} else
				return se;

		} catch (Exception e) {
			writeLog(symbol + "Update Security", logDate, "Update info error from morning star: " + symbol);
			return null;
		}
		return null;
	}

	public List<String> updateOneSecurityNameAndAssetClassFromMorningStar(Security se) {
		boolean change = false;
		List<String> strList = new ArrayList<String>();
		strList.add(se.getID().toString());
		strList.add(se.getSymbol());
		String html = "html";
		int num = 0;
		int beginIndex, endIndex;
		String oAssetName = "";
		if (se.getClassID() != null) {
			AssetClass ac = assetClassManager.get(se.getClassID());
			if (ac != null)
				oAssetName = ac.getName();
		}
		String oName = "";
		if (se.getName() != null)
			oName = se.getName();
		strList.add(oAssetName);
		strList.add(oName);
		while (html.equals("html") && num <= 30) {
			html = ParseHtmlTable.getHtml("http://quote.morningstar.com/switch.html?ticker=" + se.getSymbol());
			++num;
			if (html == null)
				html = "html";
		}
		// TICKERNOTFOUND表示没有找到该FUND以及类似的
		// TICKERFOUND表示没有找到该FUND但找到类似的
		if (html.contains("TICKERFOUND") || html.contains("TICKERNOTFOUND")) {
			strList.add("");
			strList.add("");
			return null;
		}
		/******************** security type *********************/
		beginIndex = html.indexOf("http://quote.morningstar.com/") + 29;
		endIndex = html.indexOf('/', beginIndex);
		String securityType = html.substring(beginIndex, endIndex);
		setSecurityType(se, securityType);
		/******************** security name *********************/
		String name = "";
		if (se.getSecurityType() == 5) {
			beginIndex = html.indexOf("Name") + 5;
			beginIndex = html.indexOf("Name", beginIndex) + 5;
			beginIndex = html.indexOf("Name", beginIndex) + 5;
			beginIndex = html.indexOf("Name", beginIndex) + 6;
			endIndex = html.indexOf('\"', beginIndex);
			name = html.substring(beginIndex, endIndex);
		} else {
			beginIndex = html.indexOf("description");
			if (beginIndex != -1) {
				beginIndex += 22;
				endIndex = html.indexOf('(', beginIndex);
				name = html.substring(beginIndex, endIndex);
			} else
				name = "";
		}
		if (!name.equals("")) {
			se.setName(name);
			change = true;
		}
		/******************** asset class name *********************/
		String assetClassName = "";
		if (se.getSecurityType() == Configuration.SECURITY_TYPE_CLOSED_END_FUND) {
			beginIndex = html.indexOf("MorningstarCategory", 0);
			int preBeginIndex = beginIndex;
			int count = 1;
			while (beginIndex != -1 && count < 6) {
				preBeginIndex = beginIndex;
				beginIndex = html.indexOf("MorningstarCategory", beginIndex + 19);
				++count;
			}
			if (beginIndex == -1)
				beginIndex = preBeginIndex;
			beginIndex = html.indexOf("\"", beginIndex) + 1;
			endIndex = html.indexOf("\"", beginIndex);
			char[] c = html.substring(beginIndex, endIndex).toCharArray();
			for (int i = 0; i < c.length; ++i) {
				if (c[i] != '\\')
					assetClassName += c[i];
			}
			assetClassName.replace("\\", "");
		} else {
			beginIndex = html.indexOf("Name", 0) + 7;
			endIndex = html.indexOf("\"", beginIndex);
			// assetClassName.toCharArray();
			char[] c = html.substring(beginIndex, endIndex).toCharArray();
			for (int i = 0; i < c.length; ++i) {
				if (c[i] != '\\')
					assetClassName += c[i];
			}
			assetClassName.replace("\\", "");
		}
		// morning star找不到asset class
		if (assetClassName.equals("") || assetClassName.equals("ull,") || assetClassName.equals("rt"))
			assetClassName = "";
		if (!assetClassName.equals("")) {
			AssetClass localVersion = assetClassManager.get(assetClassName);
			if (localVersion == null) {
				localVersion = new AssetClass();
				localVersion.setName(assetClassName);
				localVersion.setParentID(0L);
				localVersion.setBenchmarkID(91L);
				assetClassManager.add(localVersion);
			}
			assetClassName = localVersion.getName();
			se.setClassID(localVersion.getID());
			change = true;
		}
		strList.add(assetClassName);
		strList.add(name);
		if (change) {
			securityManager.update(se);
			return strList;
		}
		return null;
	}

	public void updateAllSecurityNameAndAssetClassFromMorningStar() throws IOException {
		File file = new File("updatesecurity.csv");
		String[] header = { "ID", "SYMBOL", "OASSETNAME", "ONAME", "NASSETNAME", "NNAME" };
		CsvListWriter clw = new CsvListWriter(new FileWriter(file), CsvPreference.EXCEL_PREFERENCE);
		clw.writeHeader(header);
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Security.class);
		detachedCriteria.add(Restrictions.eq("SecurityType", Configuration.SECURITY_TYPE_MUTUAL_FUND));
		List<Security> securityList = securityManager.getSecurities(detachedCriteria);
		if (securityList != null && securityList.size() > 0) {
			for (Security se : securityList) {
				try {
					List<String> strList = updateOneSecurityNameAndAssetClassFromMorningStar(se);
					if (strList != null)
						clw.write(strList);
				} catch (Exception e) {

				}
			}
		}
		clw.close();
	}

	public Security importOneSecurity(String symbol, boolean flush, boolean overWrite) {
		Date logDate = new Date();
		Security se = getSecurityInformation(symbol, logDate, overWrite);
		if (se == null) {
			ImportSecurityDataAction.addMessage("Update basic information : " + symbol + " , update fail", SESSION.get());
			return null;
		}
		try {
			this.writeLog(symbol + " import daily Data", logDate, "start ");
			Date initialStartDate = new Date();
			initialStartDate = LTIDate.getDate(1900, 0, 0);
			Date startDate = LTIDate.add(initialStartDate, -1);
			// download dividend and price and nav
			// ImportSecurityDataAction.addMessage(symbol+" Start",
			// SESSION.get());
			this.writeLog(symbol + " import daily Data", logDate, "download dividend and price ");

			try {
				String priceFileName = saveToFile(symbol, startDate, logDate, "d", false);
				String divFileName = saveToFile(symbol, startDate, logDate, "v", false);
				// upload dividend and price
				// ImportSecurityDataAction.addMessage("Start uploading file...",
				// SESSION.get());
				// this.writeLog(symbol + " Update daily Data", logDate,
				// "upload dividend and price ");
				this.upLoadOneFile("v", symbol, divFileName, flush, false);
				this.upLoadOneFile("d", symbol, priceFileName, flush, false);
				this.updateAdjustSecurity(symbol, startDate, null, null);
			} catch (Exception e) {
				// update adjust price
				// ImportSecurityDataAction.addMessage("Start updating adjust price...",
				// SESSION.get());
				this.writeLog(symbol + " import daily Data", logDate, "adjust price ");
			}

			if (se.getSecurityType().equals(Configuration.SECURITY_TYPE_CLOSED_END_FUND)) {
				// ImportSecurityDataAction.addMessage("Start saving CEF to file...",
				// SESSION.get());
				this.writeLog(symbol + " import daily Data", logDate, "download NAV");
				String navFileName = saveCEFToFile(symbol, startDate, logDate, "n", false);
				// ImportSecurityDataAction.addMessage("Start updating NAV...",
				// SESSION.get());
				this.writeLog(symbol + " import daily Data", logDate, "upload NAV");
				this.uploadOneNAV(symbol, navFileName);
				// ImportSecurityDataAction.addMessage("Start updating adjust NAV...",
				// SESSION.get());
				this.writeLog(symbol + " import daily Data", logDate, "adjust NAV");
				this.updateOneAdjNAV(symbol, startDate);
			}
		} catch (Exception e) {
			ImportSecurityDataAction.addMessage("import daily data : " + symbol + " , update fail", SESSION.get());
		}
		try {
			// set the startdate for the security
			securityManager.getStartDate(se.getID());// if startDate is null, it
														// will update it by
														// using SQL
			securityManager.getEndDate(se.getID());
			se = securityManager.get(se.getID());
			se.setPriceLastDate(se.getEndDate());
			securityManager.update(se);
		} catch (Exception e) {
			ImportSecurityDataAction.addMessage("update security end date : " + symbol + " , update fail", SESSION.get());
		}
		try {
			// System.out.println("start computing MPT");
			securityManager.getOneSecurityMPT(se.getID());
		} catch (Exception e) {
			// System.out.println("computing MPT error");
			ImportSecurityDataAction.addMessage("compute mpt : " + symbol + " , update fail", SESSION.get());
			return se;
		}
		ImportSecurityDataAction.addMessage("Load " + symbol + " Success", SESSION.get());
		return se;
	}

	public void batchUpdateAdjustClose(String file, Date startDate) {
		try {
			DataManager dataManager = ContextHolder.getDataManager();
			SecurityManager securityManager = ContextHolder.getSecurityManager();
			List<String> symbols = new ArrayList<String>();
			// this.deleteFile(false);
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();
			startDate = LTIDate.getFirstNYSETradingDayOfYear(startDate);
			List<SecurityDailyData> riskFreeList = securityManager.getDailyDatas(Configuration.CASHID, startDate, new Date());
			while (line != null) {
				try {
					String symbol = line.split(",")[0].trim();
					boolean success = dataManager.updateAdjustCloseFromStartDate(symbol, startDate, riskFreeList);
					String state = "success";
					if (!success)
						state = "fail";
					UpdateAdjustCloseAction.addMessage("Update " + symbol + " " + state, SESSION.get());
				} catch (Exception e) {

				}
				line = br.readLine();
			}
			UpdateAdjustCloseAction.addMessage("Load Success", SESSION.get());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void batchImportSecurity(String file, boolean flush, boolean overWrite) {

		Date logDate = new Date();
		this.writeLog("All", logDate, "Start Importing Security");

		try {
			List<String> symbols = new ArrayList<String>();
			// this.deleteFile(false);
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();
			while (line != null) {
				try {
					String symbol = line.split(",")[0].trim();
					Security se = securityManager.getBySymbol(symbol);
					if (se != null && DailyUpdateControler.isUpdating) {
						// daily update is running and we don't update the
						// security exists in our database
						ImportSecurityDataAction.addMessage("Daily update is running and " + symbol + " exists, update fail", SESSION.get());
						line = br.readLine();
						continue;
					}
					importOneSecurity(symbol, flush, overWrite);
					line = br.readLine();
				} catch (Exception e) {
					// System.out.println(StringUtil.getStackTraceString(e));
				}
			}
			// System.out.println("Load Success");
			ImportSecurityDataAction.addMessage("Load Success", SESSION.get());
			this.writeLog("All", logDate, "Load Security Success");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<Security> batchImportSecurity(Set<String> symbolSet) {
		if (symbolSet != null) {
			List<Security> securityList = new ArrayList<Security>();
			for (String symbol : symbolSet) {
				Security se = importOneSecurity(symbol, true, true);
				if (se != null)
					securityList.add(se);
			}
			return securityList;
		}
		return null;
	}

	//
	// /**
	// *
	// * @param se original security
	// * @param newID new security ID
	// * @param copyEndDate trading date
	// * @param endDate trading date
	// * @return
	// */
	// public List<SecurityDailyData> createDailyDataAsLast(Security se, Long
	// newID, Date copyEndDate, Date endDate){
	//
	// List<SecurityDailyData> sdds = new ArrayList<SecurityDailyData>();
	// Date actualEndDate = se.getEndDate();
	// if(endDate != null && actualEndDate != null){
	// //copy the original daily datas
	// if(copyEndDate == null || LTIDate.after(copyEndDate, actualEndDate))
	// copyEndDate = actualEndDate;
	// List<SecurityDailyData> oldSdds =
	// securityManager.getDailyDatas(se.getID(), se.getStartDate(),
	// copyEndDate);
	// for(int i=0;i<oldSdds.size();++i){
	// SecurityDailyData sdd = oldSdds.get(i);
	// sdd.setSecurityID(newID);
	// sdd.setID(null);
	// sdds.add(sdd);
	// }
	// //create the new daily data with price equals the lastest one
	// SecurityDailyData lastSdd = oldSdds.get(oldSdds.size() - 1);
	// Date startDate = LTIDate.getNewNYSETradingDay(copyEndDate, 1);
	// if(startDate != null && LTIDate.after(endDate, startDate)){
	// List<Date> tradingDateList = LTIDate.getTradingDates(startDate, endDate);
	// if(tradingDateList != null && tradingDateList.size()>0){
	// for(int i=0;i<tradingDateList.size();++i){
	// SecurityDailyData sdd = lastSdd.clone();
	// sdd.setDate(tradingDateList.get(i));
	// sdd.setSecurityID(newID);
	// sdd.setID(null);
	// sdds.add(sdd);
	// }
	// }
	// }
	// }
	// return sdds;
	// }
	//
	// public void createSandBoxSecurity(String symbol, Date copyEndDate, Date
	// endDate){
	// Security security = securityManager.getBySymbol(symbol);
	// Security benchmark =
	// securityManager.get(security.getAssetClass().getBenchmarkID());
	// Security virtualSecurity = null;
	// Security virtualBenchmark = null;
	// if(security != null && endDate != null && security.getEndDate() != null){
	// try {
	// Date seEndDate = security.getEndDate();
	// String virtualSymbol = "SANDBOX_" + security.getSymbol();
	// String virtualName = "SANDBOX_" + security.getName();
	// virtualSecurity = securityManager.getBySymbol(virtualSymbol);
	// //make sure copyEndDate and endDate is trading date and copyEndDate is
	// not after seEndDate
	// if(copyEndDate == null || LTIDate.after(copyEndDate, seEndDate))
	// copyEndDate = seEndDate;
	// else
	// copyEndDate = LTIDate.getRecentNYSETradingDayForward(copyEndDate);
	// endDate = LTIDate.getRecentNYSETradingDayForward(endDate);
	// if(LTIDate.after(copyEndDate, endDate))
	// endDate = copyEndDate;
	// boolean newFlag = false;
	//
	// if(virtualSecurity == null){//it doesn't exist and we now need to create
	// one
	// virtualSecurity.setSymbol(virtualSymbol);
	// virtualSecurity.setName(virtualName);
	// virtualSecurity.setClassID(security.getClassID());
	// virtualSecurity.setManual(security.getManual());
	// virtualSecurity.setSecurityType(Configuration.SECURITY_TYPE_SANDBOX);
	// virtualSecurity.setStartDate(security.getStartDate());
	// virtualSecurity.setIsClosed(false);
	// virtualSecurity.setEndDate(endDate);
	// securityManager.add(virtualSecurity);
	// SandBoxSecurity sbs =
	// securityManager.getSandBoxSecurity(virtualSecurity.getID());
	// if(sbs == null){//create the sand box information for the sand box
	// security
	// sbs.setSecurityID(virtualSecurity.getID());
	// sbs.setSymbol(virtualSecurity.getSymbol());
	// sbs.setCopyEndDate(copyEndDate);
	// securityManager.saveSandBoxSecurity(sbs);
	// }
	// }
	// } catch (SecurityException e) {
	// StringUtil.getStackTraceString(e);
	// }
	//
	//
	//
	//
	// }
	// try {
	// //save security
	// //check if it exists
	// String virtualBenchSymbol = "SANDBOX_" + benchmark.getSymbol();
	// String virtualBenchName = "SANDBOX_" + benchmark.getName();
	// virtualBenchmark = securityManager.getBySymbol(virtualBenchSymbol);
	// if(virtualBenchmark == null){
	// virtualBenchmark = new Security();
	// virtualBenchmark.setSymbol(virtualBenchSymbol);
	// virtualBenchmark.setName(virtualBenchName);
	// }
	// //save securitydailydata
	// List<SecurityDailyData> sdds = this.createDailyDataAsLast(security,
	// virtualSecurity.getID(), copyEndDate, endDate);
	// if(sdds != null && sdds.size()>0){
	// virtualSecurity.setEndDate(sdds.get(sdds.size()-1).getDate());
	// //update security end date
	// securityManager.updateEndDateAndPriceLastDate(virtualSecurity);
	// }
	// //save security daily data
	// securityManager.saveAll(sdds);
	// //compute and save mpt and update MPT last date
	// Date sDate = sdds.get(0).getDate();
	// sDate = LTIDate.getNewTradingDate(sDate, TimeUnit.DAILY, -5);
	// Date eDate = sdds.get(sdds.size() - 1).getDate();
	// eDate = LTIDate.getNewTradingDate(eDate, TimeUnit.DAILY, 5);
	// List<SecurityDailyData> benchs =
	// securityManager.getDailyDatas(virtualSecurity.getAssetClass().getBenchmarkID(),
	// sDate, eDate);
	// securityManager.getOneSecurityMPT(virtualSecurity.getID(), sdds, benchs,
	// true);
	// virtualSecurity.setMptLastDate(sdds.get(sdds.size()-1).getDate());
	// securityManager.updateEndDateAndPriceLastDate(virtualSecurity);
	// } catch (SecurityException e) {
	// StringUtil.getStackTraceString(e);
	// } catch (Exception e) {
	// StringUtil.getStackTraceString(e);
	// }
	// }

	@SuppressWarnings("deprecation")
	public static void main(String args[]) throws Exception {
		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
		// securityManager.getAllSecurityMPT(new Date(), false);
		//

		// System.out.println((String)Configuration.get("UPDATE_MODE"));
		// LTIDownLoader k = new LTIDownLoader();
		// k.updateCash("CASH", "^IRX");
		// SecurityManager
		// sm=(SecurityManager)ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
		// Long startTime=System.currentTimeMillis();
		// sm.getNAVLastDate(969L);
		// Long endTime=System.currentTimeMillis();
		// System.out.println(endTime);
		// System.out.println(startTime);
		// System.out.println("Cost time is: "+(endTime-startTime));

		// SecurityManager securityManager = (SecurityManager)
		// ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
		// downLoader.testUpdateDailyData();
		// downLoader.getSecurityInformation("AGG");
		// downLoader.getSecurityInformation("EVV");
		// downLoader.getSecurityInformation("VFINX");
		// downLoader.getSecurityInformation("BEGBX");
		// downLoader.getSecurityInformation("VFISX");
		// downLoader.downLoadAndUpdateShillerSP();
		// downLoader.batchImportSecurity(file, true, true);
		// Date logdate = new Date();
		// securityManager.updateAllSecurityMPT(logdate);
		// LTIDownLoader downLoader = new LTIDownLoader();
		// Date copyEndDate = LTIDate.getDate(2010, 6, 1);
		// Date endDate = LTIDate.getDate(2010, 6, 23);
		// downLoader.createSandBoxSecurity("VBMFX", copyEndDate, endDate);
		// downLoader.importOneSecurity("^FVX", true, true);
		// downLoader.updateTSPGFund("TSPGFund", "^TNX", "^FVX", true);
		// SecurityManager sm = (SecurityManager)
		// ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
		// //Security cash = sm.get("CASH");
		// Date lastDate = LTIDate.getDate(2010, 7, 23);
		// DetachedCriteria detachedCriteria =
		// DetachedCriteria.forClass(Security.class);
		// detachedCriteria.add(Restrictions.lt("SecurityType", 5));
		// detachedCriteria.add(Restrictions.lt("EndDate", lastDate));
		// //detachedCriteria.add(Restrictions.lt("EndDate",
		// cash.getEndDate()));
		// List<Security> securityList = sm.getSecurities(detachedCriteria);
		// int size = securityList.size();
		// for(int i=0;i<securityList.size();++i){
		// System.out.println("Current: "+ i+"//"+size);
		// Security security = securityList.get(i);
		// Date endDate = security.getEndDate();
		// SecurityDailyData sdd = sm.getLatestDailydata(security.getID());
		// boolean update = false;
		// if(sdd!=null && sdd.getClose()==0){
		// System.out.println(security.getID()+ "," + security.getSymbol());
		// }
		// }
		// downLoader.getSecurityInformation("SCHC", new Date(), true);
		// downLoader.updateDailyData();
		// downLoader.downLoadAndUpdateShillerSP();
		// downLoader.getSecurityInformation("CF", new Date(), true);
		// downLoader.getSecurityInformation("FTAWX", new Date(), true);
		// downLoader.downLoadAndUpdateShillerSP();
		// downLoader.uploadFastFile("fast1");
		// downLoader.downLoadAndUpdateShillerSP();
		// downLoader.simulateDailyUpdate();
		// securityManager.updateAllSecurityMPT(logdate);
		// downLoader.downLoadAndUpdateShillerSP();
		// k.updateDailyData();

		// k.updateFamaBenchMark();
		// k.updateDailyData();
		// k.saveToFile("ADRE", LTIDate.getDate(2002, 11, 15),
		// LTIDate.getDate(2009, 5, 23), "v", true);
		// k.doSomeOtherUpdateJob();
		// k.fastUpdateDailyData("fast1");
		// String[] symbolArray = {"VTSMX", "VGSIX", "VEIEX", "VGTSX", "VBINX",
		// "BEGBX", "VWEHX", "VBMFX", "VBINX", "ECMBX", "JCTFX", "JCTFX", "BSC",
		// "BGIBX", "DNCGX", "FINPX", "NWMIX", "CBOFX", "HPEYX"};

		List<Security> security = new ArrayList<Security>();
		security.add(securityManager.get(10371l));
		LTIDownLoader lti = new LTIDownLoader();
		lti.testUpdateDailyDataNew(security);
		System.out.println("end");
		// DataManager dataManager = ContextHolder.getDataManager();
		// SecurityManager securityManager = ContextHolder.getSecurityManager();
		// Date startDate = LTIDate.getDate(2009, 01, 01);

		// for(String symbol : symbolArray)

		// List<SecurityDailyData> riskFreeList =
		// securityManager.getDailyDatas(Configuration.CASHID, startDate, new
		// Date());
		// System.out.println(dataManager.updateAdjustCloseFromStartDate("BRUFX",
		// startDate, riskFreeList));

		// LTIDownLoader k = new LTIDownLoader();
		// String symbol = "XAWPX";
		// System.out.println(symbol.substring(1, symbol.length() - 1));
		// k.updateSpecialFund("QQQQ", "QQQ");
		// k.fastUpdateDailyData("fast1", 1);
		// Security security = securityManager.getBySymbol("PGMAX");
		// List<Security> securityList = new ArrayList<Security>();
		// securityList.add(security);
		// k.updateDailyDataByList(securityList);
		// for(Security se: securityList){
		// try{
		// securityManager.getOneSecurityMPT(se.getID());
		// }catch(Exception e){
		//
		// }
		// }

		// k.updateTSPGFund("STABLEVALUE", "^TNX", "^FVX", true);
		// LTIDownLoader k = new LTIDownLoader();
		// k.fastUpdateDailyData("fast1");
		// k.getSecurityInformation("BBBBBB", new Date() , true);
		// SecurityManager securityManager = ContextHolder.getSecurityManager();
		// Security se = securityManager.get(189L);
		// k.updateOneSecurityNameAndAssetClassFromMorningStar(se);
		/*** do mutualfund ***/
		// k.updateAllSecurityNameAndAssetClassFromMorningStar();
		// k.importOneSecurity("^DJU", true, true);
		// k.uploadOneFastFile("C://WINDOWS//Temp//us//fast//1.txt");
		// k.updateFamaBenchMark();
		/*
		 * String[] sb =
		 * k.split("\"PCG\",\"3/25/2009\",40.47,41.06,39.80,2654914,40.32");
		 * for(int i=0;i<sb.length;i++)System.out.println(sb[i]);
		 * 
		 * /*sb = k.split(
		 * "\"Tweedy,Browne Global Value\",TBGVX,Mutual Fund,Foreign Small/Mid Value"
		 * ); for(int i=0;i<sb.length;i++)System.out.println(sb[i]);
		 * 
		 * sb =
		 * k.split("\"AGG\",\"3/24/2009\",100.83,101.18,100.64,934035,101.33");
		 * for(int i=0;i<sb.length;i++)System.out.println(sb[i]);
		 * //k.uploadHolidayAndSetTradingDate("C://abc.csv");
		 * //k.saveHolidayToFile("C://sbb.csv");
		 * //securityManager.getAllSecurityMPT(false); /*for(int i=0;i<5;i++) {
		 * List<com.lti.service.bo.FundAlert> list =
		 * securityManager.getFundAlertBySecurityID(i); int size =
		 * securityManager.getDailydatas((long)i, true).size(); int num1 =
		 * 0,num2 = 0; int num11 = 0,num22 = 0; for(int j=0;j<list.size();j++) {
		 * com.lti.service.bo.FundAlert fa = list.get(j); if(fa.getPointType()
		 * == 2.0)num2++; else if(fa.getPointType() == 1.0)num1++; else
		 * if(fa.getPointType() == -1.0)num11++; else if(fa.getPointType() ==
		 * -2.0)num22++; } }
		 */
		// k.uploadSecurityDailyDataInCSV("C://GLD.csv");
		// k.updateDailyData();
		// k.checkSplit();
		// securityManager.getAllSecurityMPT(true);
		/*
		 * Date d1 = LTIDate.getDate(1984, 4, 2); Date d2 =
		 * LTIDate.getDate(2008, 11, 24); k.saveToFile("^FTSE", d1, d2, "d",
		 * false);
		 */
		// k.checkSecurityDailyData();
		// k.batchLoadAttribute("C://Nasdaq100.csv",true,true);
		// k.batchLoadAttribute("C://allSecurity.csv",true);
		// k.exportSecurityToDoc("C://security.csv");
		// k.buildupCloseFund();
		// k.buildUpOpenFund();
		// k.buildUpCNCash();
		// k.setSplit();
		// k.writeLog("dfd", new Date(), "dfdf");
		// k.updateDailyData();
		/*
		 * (System.getProperties()).list(System.out); String classPath =
		 * System.getProperty("java.class.path",".");
		 */

		// k.buildupCNCash();
		// k.setSplit();
		// Long securityID1 = 6946L;
		// Long securityID2 = 6452L;
		// //Date date1 = LTIDate.getDate(2010, 8, 30);
		// //Date date2 = LTIDate.getDate(2010, 8, 31);
		// Date date3 = LTIDate.getDate(2010, 9, 2);
		// PortfolioManager pm = (PortfolioManager)
		// ContextHolder.getInstance().getApplicationContext().getBean("portfolioManager");
		// //pm.setHoldingRecord(securityID1, date1);
		// //pm.setHoldingRecord(securityID1, date2);
		// pm.setHoldingRecord(securityID2, date3);
	}

	public int getUpdateNum() {
		return updateNum;
	}

	public void setUpdateNum(int updateNum) {
		this.updateNum = updateNum;
	}

}