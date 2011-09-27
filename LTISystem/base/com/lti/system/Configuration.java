package com.lti.system;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.configuration.PropertiesConfiguration;

public class Configuration {
	public final static int DefaultPageSize = 20;
	public final static int MaxPageSize = 60;

	public final static String UNDEFINED = "undefined";

	public final static String PORTFOLIO_STRATEGY_CLASS_NAME = "PORTFOLIO STRATEGY";
	public final static String ASSET_STRATEGY_CLASS_NAME = "ASSET STRATEGY";
	public final static String ASSET_ALLOCATION_STRATEGY_CLASS_NAME = "ASSET ALLOCATION STRATEGY";
	public final static String REBALANCING_STRATEGY_CLASS_NAME = "REBALANCING STRATEGY";
	public final static String CASH_FLOW_STRATEGY_CLASS_NAME = "CASH FLOW STRATEGY";
	public final static String CASH_FLOW_DEPOSIT_CLASS_NAME = "DEPOSIT";
	public final static String CASH_FLOW_WITHDRAW_CLASS_NAME = "WITHDRAW";
	public final static String EQUITY = "EQUITY";

	/**
	 * group role relationship except Portfolio or Strategy
	 */
	public final static int RESOURCE_TYPE_ROLE = 0;
	public final static int RESOURCE_TYPE_STRATEGY = 1;
	public final static int RESOURCE_TYPE_PORTFOLIO = 2;
	public final static int RESOURCE_TYPE_URL = 3;
	public final static int RESOURCE_TYPE_METHOD = 4;

	public final static String GROUP_SUPER = "ROLE_SUPERVISOR";
	public final static String GROUP_MEMBER = "IS_AUTHENTICATED_REMEMBERED";
	public final static String GROUP_ANONYMOUS = "IS_AUTHENTICATED_ANONYMOUSLY";
	public final static Long GROUP_SUPER_ID = 3l;
	public final static Long GROUP_MEMBER_ID = 2l;
	public final static Long GROUP_ANONYMOUS_ID = 1l;
	public final static Long GROUP_MPIQ_B_ID = 8l;
	public final static Long GROUP_MPIQ_ID = 10l;
	public final static Long GROUP_VF_B_ID=11l;
	public final static String GROUP_VF_B="VF_B";
	public final static long GROUP_MPIQ_E_ID = 12l;
	public final static long GROUP_MPIQ_P_ID = 13l;
	public final static long GROUP_ADVISOR_ID = 14l;
	/**
	 *  GROUP_SUPER = "ROLE_SUPERVISOR", alias SUPER
	 */
	public final static String SUPER = "SUPER";
	/**
	 * GROUP_MEMBER = "IS_AUTHENTICATED_REMEMBERED", alias USER
	 */
	public final static String MEMBER = "USER";
	public final static String ANONYMOUS = "ANONYMOUS";

	/**
	 * will be deleted
	 */
	public final static Long GROUP_LEVEL1 = 4l;
	public final static Long GROUP_LEVEL2 = 5l;
	
	public final static String GROUP_LEVEL_1 = "Level_1";
	public final static String GROUP_LEVEL_2 = "Level_2";

	public final static Long USER_ANONYMOUS = 0l;

	public final static int LOG_TYPE_SYSTEM = 0;
	public final static int LOG_TYPE_USER = 1;

	public final static int PORTFOLIO_STATE_ALIVE = 1;
	public final static int PORTFOLIO_STATE_STATIC = 0;
	public final static int PORTFOLIO_STATE_EVALUATED = 2;

	
	public final static int PORTFOLIO_UPDATEMODE_RELEASE = 1;
	public final static int PORTFOLIO_UPDATEMODE_NOTMONITOR = 2;
	public final static int PORTFOLIO_UPDATEMODE_DEVELOPMENT = 3;
	public final static int PORTFOLIO_UPDATEMODE_LOWLEVEL = 4;
	public final static int PORTFOLIO_UPDATEMODE_EMAILALERT = 5;
	public final static int PORTFOLIO_UPDATEMODE_PRODUCTION = 6;
	public final static int PORTFOLIO_UPDATEMODE_DATA = 7;
	public final static int PORTFOLIO_UPDATEMODE_PRODUCTION_MODEL = 8;
	
	//public final static String PORTFOLIO_TYPE_PUBLIC = "public";
	@Deprecated
	public final static String PORTFOLIO_TYPE_MY_PUBLIC = "mypublic";
	@Deprecated
	public final static String PORTFOLIO_TYPE_MY_PRIVATE = "myprivate";
	@Deprecated
	public final static String PORTFOLIO_TYPE_MIX = "mix";
	
	public final static long PORTFOLIO_TYPE_MODEL=1;
	public final static long PORTFOLIO_TYPE_FREE=1<<1;
	public final static long PORTFOLIO_TYPE_PRODUCTION=1<<2;
	public final static long PORTFOLIO_TYPE_PERSONAL=1<<3;
	public final static long PORTFOLIO_TYPE_DATA=1<<4;
	public final static long PORTFOLIO_TYPE_COMPOUND=1<<5;
	public final static long PORTFOLIO_TYPE_FULLYPUBLIC=1<<6;
	
	
	
	
	
	public final static long STRATEGY_TYPE_NORMAL=1;
	public final static long STRATEGY_TYPE_401K=1<<1;//2
	public final static long STRATEGY_TYPE_PRESERVED=1<<2;//4
	public final static long STRATEGY_TYPE_401K_CENTER=1<<3;//8
	public final static long STRATEGY_TYPE_FREE=1<<4;//16
	public final static long STRATEGY_TYPE_INDEXED =1<<5;//32
	public final static long STRATEGY_TYPE_CONSUMER =1<<6;//64
	
	/**
	 * plan type
	 */
	public final static int PLAN_CATEGORY_DEFAULT = 0;
	public final static int PLAN_CATEGORY_RETIREMENT_401K_PLAN = 1;
	public final static int PLAN_CATEGORY_RETIREMENT_403B_PLAN = 2;
	public final static int PLAN_CATEGORY_RETIREMENT_457_PLAN = 3;
	public final static int PLAN_CATEGORY_IRA_TAXABLE_ETF_PLAN = 4;
	public final static int PLAN_CATEGORY_BROKERAGE_MUTUAL_FUND_PLAN = 5;
	public final static int PLAN_CATEGORY_LAZY_PORTFOLIO_PLAN = 6;
	public final static int PLAN_CATEGORY_VARIABLE_ANNUITIES = 7;
	public final static int PLAN_CATEGORY_VARIABLE_UNIVERSAL_LIFE_PLAN = 8;
	public final static int PLAN_CATEGORY_COLLEGE_SAVINGS_529_PLAN = 9;
	
	/**
	 * plan status
	 */
	public final static int PLAN_STATUS_Default = 0;
	public final static int PLAN_STATUS_NO_OPTIONS = 1;
	public final static int PLAN_STATUS_UNMATCHED = 2;
	public final static int PLAN_STATUS_MATCHED = 3;
	public final static int PLAN_STATUS_READY_TO_GENERATE = 4;
	public final static int PLAN_STATUS_LIVE = 5;
	
	
	
	
	public final static String PLANSCORE_TYPE_COVERAGE = "CoverageValue";
	public final static String PLANSCORE_TYPE_FUNDQUALITY = "FundQualityValue";
	public final static String PLANSCORE_TYPE_CAPABILITY = "CapabilityValue";
	public final static String PLANSCORE_TYPE_INVESTMENT = "InvestmentValue";
	/**
	 * publicPortfolio
	 */
	public final static String PORTFOLIO_TYPE_PUBLIC = "publicPortfolio";
	/**
	 * myPortfolio
	 */
	public final static String PORTFOLIO_TYPE_MY = "myPortfolio";
	/**
	 * premiumsPortfolio
	 */
	public final static String PORTFOLIO_TYPE_PREMIUMS = "premiumsPortfolio";
	public final static String PORTFOLIO_TYPE_ALL = "allPortfolio";
	
	
	/**
	 * publicStrategy
	 */
	@Deprecated
	public final static String STRATEGY_TYPE_PUBLIC = "publicStrategy";
	/**
	 * myStrategy
	 */
	@Deprecated
	public final static String STRATEGY_TYPE_MY = "myStrategy";
	/**
	 * premiumsStrategy
	 */
	public final static String STRATEGY_TYPE_PREMIUMS = "premiumsStrategy";
	

	public final static long STRATEGY_SAA_ID=771l;
	public final static long STRATEGY_TAA_ID=1003l;
	
	public final static String STRATEGY_PERSONAL_PORTFOLIO_SIMULATION="personal_portfolio_simulation";
	
	public final static int STRATEY_STATE_ALIVE = 1;
	public final static int STRATEY_STATE_STATIC = 0;

	public final static String PERMISSION_READ_BASE = "A";
	public final static String PERMISSION_READ_COMPLETE = "AA";
	public final static String PERMISSION_BAN = "";

	@Deprecated
	public final static String ROLE_STRATEGY_CREATE_BASE = "ROLE_STRATEGY_CREATE_BASE";
	@Deprecated
	public final static String ROLE_STRATEGY_CREATE_CODE = "ROLE_STRATEGY_CREATE_CODE";
	@Deprecated
	public final static String ROLE_STRATEGY_READ_BASE = "ROLE_STRATEGY_READ_BASE";
	@Deprecated
	public final static String ROLE_STRATEGY_READ_COMPLETE = "ROLE_STRATEGY_READ_COMPLETE";

	public final static String ROLE_PORTFOLIO_CREATE = "ROLE_CREATE_PORTFOLIO";
	public final static String ROLE_PORTFOLIO_READ = "ROLE_PORTFOLIO_READ";
	public final static String ROLE_PORTFOLIO_TRANSACTION_READ_COMPLETE = "ROLE_PORTFOLIO_TRANSACTION_READ_COMPLETE";
	public final static String ROLE_PORTFOLIO_LOG_READ_COMPLETE = "ROLE_PORTFOLIO_LOG_READ_COMPLETE";
	public final static String ROLE_PORTFOLIO_DAILYDATA_READ_COMPLETE = "ROLE_PORTFOLIO_DAILYDATA_READ_COMPLETE";

	public final static String ROLE_PORTFOLIO_OPERATION = "ROLE_PORTFOLIO_OPERATION";
	public final static long ROLE_PORTFOLIO_OPERATION_ID = 3l;
	public final static String ROLE_PORTFOLIO_REALTIME = "ROLE_PORTFOLIO_REALTIME";
	public final static String ROLE_PORTFOLIO_DELAYED = "ROLE_PORTFOLIO_DELAYED";
	public final static long ROLE_PORTFOLIO_REALTIME_ID = 4l;
	public final static long ROLE_PORTFOLIO_DELAYED_ID = 8l;
	public final static String ROLE_STRATEGY_OPERATION = "ROLE_STRATEGY_OPERATION";
	public final static String ROLE_STRATEGY_CODE = "ROLE_STRATEGY_CODE";
	public final static String ROLE_STRATEGY_READ = "ROLE_STRATEGY_READ";
	public final static Long ROLE_STRATEGY_READ_ID = 9l;
	public final static Long ROLE_STRATEGY_CODE_ID = 2l;
	public final static String ROLE_RAA = "ROLE_RAA";
	public final static String ROLE_MVO = "ROLE_MVO";
	public final static String ROLE_BL = "ROLE_BL";
	public final static String ROLE_OWNER = "ROLE_OWNER";

	public final static String ROLE_PORTFOLIO_EXECUTE = "ROLE_PORTFOLIO_EXECUTE";
	
	public final static String ROLE_FUNDTABLE_OPEN10 = "ROLE_FUNDTABLE_OPEN10";
	public final static int FUNTABLE_OPEN10 = 30;
	
	public final static String ROLE_PLAN_CREATE = "ROLE_PLAN_CREATE";
	public final static int PLAN_CREATE5 = 5;
	public final static int PLAN_CREATE10 = 10;
	
	public final static String ROLE_PLAN_USE = "ROLE_PLAN_USE";
	public final static String ROLE_PORTFOLIO_USE = "ROLE_PORTFOLIO_USE";
	public final static int PLAN_USE5 = 5;
	public final static int PLAN_USE10 = 10;
	public final static int PLAN_USE20 = 20;
	public final static int PORTFOLIO_USE5 = 5;
	public final static int PORTFOLIO_USE10 = 10;
	public final static int PORTFOLIO_USE20 = 20;

	/* XML Link Type For Double Click Redirection */
	public static final String LinkType_Security = "SecurityLink";

	public static final String LinkType_Portfolio = "PortfolioLink";

	public static final String LinkType_Strategy = "StrategyLink";

	public final static int PORTFOLIO_STATE_TEMP = 2;

	public final static int PORTFOLIO_RUNNING_STATE_INACTIVE = -1;
	@Deprecated
	public final static int PORTFOLIO_RUNNING_STATE_PREPARING = 0;
	@Deprecated
	public final static int PORTFOLIO_RUNNING_STATE_EXECUTING = 1;
	@Deprecated
	public final static int PORTFOLIO_RUNNING_STATE_COMPUTMPTS = 2;
	public final static int PORTFOLIO_RUNNING_STATE_FINISHED = 4;
	@Deprecated
	public final static int PORTFOLIO_RUNNING_STATE_FAILED = 5;
	public final static int PORTFOLIO_RUUNING_STATE_DAILYEXECUTION_FINISHED = 6;
	@Deprecated
	public final static int PORTFOLIO_RUUNING_STATE_DAILYEXECUTION_FAILED = 7;
	public final static long PORTFOLIO_DEFAULT = 0l;
	public final static long STRATEGY_STATIC = 0l;

	public final static String TRANSACTION_BUY = "BUY";
	public final static String TRANSACTION_SELL = "SELL";
	public final static String TRANSACTION_SELL_ASSET = "SELL_ASSET";
	public final static String TRANSACTION_SELL_AND_DEL_ASSET = "SELL_AND_DEL_ASSET";
	public final static String WITHDRAW = "WITHDRAW";
	public final static String DEPOSIT = "DEPOSIT";
	public final static String TRANSACTION_SHORT_SELL = "SHORT_SELL";
	
	
	public final static int TRANSACTION_TYPE_REAL =1;
	public final static int TRANSACTION_TYPE_SCHEDULE =2;
	public final static int TRANSACTION_TYPE_REINVEST =3;
	public final static int TRANSACTION_TYPE_SPLIT = 4;
	public final static int TRANSACTION_TYPE_VIRTUAL = 5;
	public final static int TRANSACTION_TYPE_MAIL = 6;
	
	public final static int SUGGESTED_PERSONAL_TRANS = 2;
	public final static int REAL_PERSONAL_CASH = 3;

	public final static String TRANSACTION_BUY_AT_OPEN = "BUY_AT_OPEN";
	public final static String TRANSACTION_SELL_AT_OPEN = "SELL_AT_OPEN";
	public final static String TRANSACTION_SELL_ASSET_AT_OPEN = "SELL_ASSET_AT_OPEN";
	public final static String TRANSACTION_SELL_AND_DEL_ASSET_AT_OPEN = "SELL_AND_DEL_ASSET_AT_OPEN";
	public final static String TRANSACTION_SHORT_SELL_AT_OPEN = "SHORT_SELL_AT_OPEN";
	public final static String TRANSACTION_SELL_ALL_ASSET_AT_OPEN = "SELL_ALL_ASSET_AT_OPEN";
	public final static String TRANSACTION_SELL_ALL_ASSET = "SELL_ALL_ASSET";
	public final static String TRANSACTION_REINVEST = "REINVEST";
	public final static String TRANSACTION_SPLIT = "SPLIT";
	public final static String TRANSACTION_VIRTUAL = "TRANSACTION_VIRTUAL";

	
	public final static int PORTFOLIO_UPDATE_FORCEMONITOR = 1;
	@Deprecated
	public final static int PORTFOLIO_UPDATE_MONITORIFCANNOTUPDATE = 2;
	public final static int PORTFOLIO_UPDATE_IGNOREIFCANNOTUPDATE = 3;
	public final static int PORTFOLIO_UPDATE_RECONSTRUCT = 4;
	
	public final static int SECURITY_TYPE_ETF = 1;
	public final static int SECURITY_TYPE_CLOSED_END_FUND = 2;
	public final static int SECURITY_TYPE_BENCHMARK = 3;
	public final static int SECURITY_TYPE_MUTUAL_FUND = 4;
	public final static int SECURITY_TYPE_STOCK = 5;
	public final static int SECURITY_TYPE_PORTFOLIO = 6;
	public final static int SECURITY_TYPE_INDEX = 7;
	
	
	public final static int MPT_TYPE_SECURITY = 0;
	public final static int MPT_TYPE_PORTFOLIO = 1;
	
	/**
	 * compound of two securities
	 */
	public final static int SECURITY_TYPE_COMPOUND = 8;
	
	public final static long CASHID = 890l;

	public final static int PERMISSION_PORTFOLIO_OWNER = 1;
	public final static int PERMISSION_PORTFOLIO_HIGH = 2;
	public final static int PERMISSION_PORTFOLIO_LOW = 3;
	public final static int PERMISSION_PORTFOLIO_PRIVATE = 4;

	public final static String EMAIL_HOST = "support@myplaniq.com";
	public final static String EMAIL_SUBJECT = "Transaction Update From ValidFi";
	public final static String MPIQ_EMAIL_SUBJECT = "Transaction Update From MyPlanIQ";

	public final static int PORTFOLIO_DELAY_DAYS = 30;
	
	public final static String MATCHING_RULE_FILENAME = "matchrule.csv";
	public final static String MATCHING_RULE_VAFUND_FILENAME = "vafundmatchrule.csv";

	public final static int getSecurityType(String name) {
		if (name.toLowerCase().equalsIgnoreCase("mutual fund") || name.equalsIgnoreCase("mutual fund".trim())) {
			return 4;
		}
		if (name.toLowerCase().equalsIgnoreCase("etf") || name.equalsIgnoreCase("etf".trim())) {
			return 1;
		}
		if (name.toLowerCase().equalsIgnoreCase("closed end fund") || name.equalsIgnoreCase("closed end fund".trim()) || name.equalsIgnoreCase("CEF")) {
			return 2;
		}
		if (name.toLowerCase().equalsIgnoreCase("benchmark") || name.equalsIgnoreCase("benchmark".trim())) {
			return 3;
		}
		if (name.toLowerCase().equalsIgnoreCase("stock") || name.equalsIgnoreCase("stock".trim())) {
			return 5;
		}
		if (name.toLowerCase().equalsIgnoreCase("portfolio") || name.equalsIgnoreCase("portfolio".trim())) {
			return 6;
		}
		return 0;
	}

	public static String getSecurityTypeName(int s) {
		if (s == 4) {
			return "Mutual Fund";
		}
		if (s == 1) {
			return "ETF";
		}
		if (s == 2) {
			return "CEF";
		}
		if (s == 3) {
			return "Benchmark";
		}
		if (s == 5) {
			return "Stock";
		}
		if (s == 6) {
			return "Portfolio";
		} else
			return Configuration.UNDEFINED;
	}

	// will be removed
	public final static long SUPER_USER_ID = 1;
	public final static long PUBLIC_USER_ID = 0;
	public final static long GUEST_UESR_ID = 0;

	public final static String TABLE_ASSET = "ltisystem_asset";
	public final static String TABLE_PROFILE = "ltisystem_profile";
	public final static String TABLE_GROUP_ROLE = "ltisystem_group_role";
	public final static String TABLE_PORTFOLIO = "ltisystem_portfolio";
	public final static String TABLE_PORTFOLIOINF = "ltisystem_portfolioinf";
	public final static String TABLE_LOG = "ltisystem_log";
	public final static String TABLE_PORTFOLIO_DAILY_DATA = "ltisystem_portfoliodailydata";
	public final static String TABLE_SECURITY_DAILY_DATA = "ltisystem_securitydailydata";
	public final static String TABLE_SECURITY = "ltisystem_security";
	public final static String TABLE_TRANACTION = "ltisystem_transaction";
	public final static String TABLE_PORTFOLIO_MPT = "ltisystem_portfoliompt";
	public final static String TABLE_PORTFOLIO_HOLDINGS = "ltisystem_holdings";
	public final static String TABLE_PORTFOLIO_STATE = "ltisystem_portfoliostate";
	public final static String TABLE_STRATEGY = "ltisystem_strategy";
	public final static String TABLE_EMAILNOTIFICATION = "ltisystem_emailnotification";
	public final static String TABLE_COMPANY_INDEX = "ltisystem_companyindex";
	public final static String TABLE_COMPANY = "ltisystem_company";
	public final static String TABLE_VARIABLEFOR401K="ltisystem_variable401k";
	public final static String TABLE_USERFUNDTABLE="ltisystem_userfundtable";
	public final static String TABLE_PORTFOLIO_HOLDINGITEM = "ltisystem_holdingitem";
	public final static String TABLE_PORTFOLIO_FOLLOWDATE = "ltisystem_portfoliofollowdate";
	public final static String TABLE_PERSONALPORTFOLIO_TRANSACTION = "ltisystem_personaltransaction";
	public final static String TABLE_HOLDINGRECORD = "ltisystem_holdingrecord";
	public final static String TABLE_USERRESOURCE = "ltisystem_userresource";
	public final static String TABLE_PORTFOLIOPERFORMANCE = "ltisystem_portfolioperformance";
	public final static String TABLE_COMPANYFUND = "ltisystem_companyfund";
	public final static String TABLE_PORTFOLIOFOLLOWDATE = "ltisystem_portfoliofollowdate";
	public final static String TABLE_PLANSCORE = "ltisystem_planscore";
	
	public final static String CURRENT_ASSET = "curAsset";

	public final static String LANGUAGE_US = "US";
	public final static String LANGUAGE_CN = "CN";

	public static String getLanguage() {
		return (String) Configuration.get("language");
	}

	public static String getCashSymbol() {
		return (String) Configuration.get("cashsymbol");
	}

	public final static String SANDBOX_PREFIX = "SANDBOX_";
	public final static String CASH_SYMBOL = "CASH";
	public final static String VERSION_FILENAME = "version.txt";
	
	public static String getCashSymbol(boolean sandBox){
		String cashSymbol = CASH_SYMBOL;
		if(sandBox)
			cashSymbol = SANDBOX_PREFIX + cashSymbol;
		return cashSymbol;
	}
	
	public static Object get(String key) {
		try {
			PropertiesConfiguration config = new PropertiesConfiguration("system-config.properties");
			String configFileName = config.getString("config_file");
			PropertiesConfiguration sysConfigFile = new PropertiesConfiguration(configFileName);
			if (sysConfigFile != null) {
				Object object = sysConfigFile.getProperty(key);
				if (object == null) {
					object = config.getProperty(key);
				}
				return object;
			} 
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	public static boolean getSendMail() {
		try {
			PropertiesConfiguration config = new PropertiesConfiguration("emailalert.properties");
				Object object = config.getProperty("SEND_MAIL");
				return Boolean.parseBoolean((String) object);
		} catch (Exception e) {
		}
		return false;
	}
	

	public static String getTempDir() {
		String path = null;
		if (isLinux()) {

			path = (String) get("DIR_TEMP_LINUX");
		} else {
			path = (String) get("DIR_TEMP_WINDOWS");
		}
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		return file.getAbsolutePath() + "/";
	}
	
	public static String getImageCacheDir() {
		String path = null;
		if (isLinux()) {
			path = (String) get("DIR_IMAGECACHE_LINUX");
		} else {
			path = (String) get("DIR_IMAGECACHE_WINDOWS");
		}
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		return file.getAbsolutePath() + "/";
	}
	
	public static String getVersionDir() {
		String path = null;
		if(isLinux())
			path = (String) get("DIR_VERSION_LINUX");
		else
			path = (String) get("DIR_VERSION_WINDOWS");
		File file = new File(path);
		if(!file.exists())
			file.mkdirs();
		return file.getAbsolutePath() + "/";
	}
	
	public static String getAssetClassCacheDir() {
		String path = null;
		if (isLinux()) {
			path = (String) get("DIR_ASSET_CLASS_CACHE_LINUX");
		} else {
			path = (String) get("DIR_ASSET_CLASS_CACHE_WINDOWS");
		}
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		return file.getAbsolutePath() + "/";
	}
	
	public static String getFundTableCacheDir() {
		String path = null;
		if (isLinux()) {
			path = (String) get("DIR_FUNDTABLE_CACHE_LINUX");
		} else {
			path = (String) get("DIR_FUNDTABLE_CACHE_WINDOWS");
		}
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		return file.getAbsolutePath() + "/";
	}
	
	public static String getVAFundCacheDir() {
		String path = null;
		if (isLinux()) {
			path = (String) get("DIR_VAFUND_CACHE_LINUX");
		} else {
			path = (String) get("DIR_VAFUND_CACHE_WINDOWS");
		}
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		return file.getAbsolutePath() + "/";
	}
	
	public static String getCompanyFundCacheDir() {
		String path = null;
		if (isLinux()) {
			path = (String) get("DIR_COMPANYFUND_CACHE_LINUX");
		} else {
			path = (String) get("DIR_COMPANYFUND_CACHE_WINDOWS");
		}
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		return file.getAbsolutePath() + "/";
	}
	
	public static String getSecurityCacheDir() {
		String path = null;
		if (isLinux()) {
			path = (String) get("DIR_SECURITY_CACHE_LINUX");
		} else {
			path = (String) get("DIR_SECURITY_CACHE_WINDOWS");
		}
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		return file.getAbsolutePath() + "/";
	}

	public static String getObjectDir() {
		String path = null;
		if (isLinux()) {

			path = (String) get("DIR_OBJECT_LINUX");
		} else {
			path = (String) get("DIR_OBJECT_WINDOWS");
		}
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		return file.getAbsolutePath() + "/";
	}

	public static String get13FDir() {
		if (isLinux()) {
			return (String) get("DIR_13F_LINUX");
		} else {
			return (String) get("DIR_13F_WINDOWS");
		}
	}
	public static String get11KDir() {
		if (isLinux()) {
			return (String) get("DIR_11K_LINUX");
		} else {
			return (String) get("DIR_11K_WINDOWS");
		}
	}
    
	public static String getVFOFXDir() {
		if (isLinux()) {
			return (String) get("DIR_VFOFX_LINUX");
		} else {
			return (String) get("DIR_VFOFX_WINDOWS");
		}
	}

	public static void set(String key, String value) {
		try {
			PropertiesConfiguration config = new PropertiesConfiguration("system-config.properties");
			config.setProperty(key, value);
			config.save();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	public static void setSendMail(boolean f) {
		try {
			PropertiesConfiguration config = new PropertiesConfiguration("emailalert.properties");
			config.setProperty("SEND_MAIL", ""+f);
			config.save();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public final static String CONFIDENCE_DAILY = "Daily";
	public final static String CONFIDENCE_WEEKLY = "Weekly";
	public final static String CONFIDENCE_MONTHLY = "Monthly";

	public static boolean isLinux() {
		String str = System.getProperty("os.name").toUpperCase();
		if (str.indexOf("WINDOWS") == -1) {
			return true;
		}
		return false;
	}

	public final static String DAILY_EXECUTION_RESET_MODE = "DAILY_EXECUTION_RESET_MODE";
	public final static String DAILY_EXECUTION_RESET_DATE = "DAILY_EXECUTION_RESET_DATE";

	public static void setDailyExecutionResetDate(Date d) {
		if (d == null)
			return;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		set(DAILY_EXECUTION_RESET_DATE, sdf.format(d));
	}

	public static Date getDailyExecutionResetDate() {
		String s = (String) get(DAILY_EXECUTION_RESET_DATE);
		if (s != null) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
				return sdf.parse(s);
			} catch (Exception e) {
			}
		}
		return null;
	}

	public final static String DAILY_EXECUTION_RESET_MODE_NONE = "none";
	public final static String DAILY_EXECUTION_RESET_MODE_WEEK_END = "weekend";
	public final static String DAILY_EXECUTION_RESET_MODE_MONTH_END = "monthend";

	public static String getLogPath(Date date, String name){
		String systemPath;
		String sysPath = System.getenv("windir");
		if (!Configuration.isLinux())
			systemPath = sysPath + "\\temp\\";
		else
			systemPath = "/var/tmp/";

		// Date date = new Date();
		Integer year = date.getYear() + 1900;
		Integer month = date.getMonth() + 1;
		Integer day = date.getDate();
		String monthS = month > 9 ? (month.toString()) : ("0" + month.toString());
		String dayS = day > 9 ? (day.toString()) : ("0" + day.toString());
		return systemPath + year.toString() + monthS + dayS + name;
	}
	
	public static String getLogPath(Date date) {
		return getLogPath(date, "downloadlog.html");
	}
		
	public static String getFinancialLogPath(Date date) {
		return getLogPath(date, "FinancialUpdateLog.html");
	}
	
	public static String getMarketEmailLogPath(Date date){
		return getLogPath(date, "MarketEmailLog.txt");
	}
	
	public static void writeLog(String name, Date logDate, Date endDate, String log) {
		try {
			endDate = new Date();
			FileOutputStream stream = new FileOutputStream(getLogPath(logDate), true);
			OutputStreamWriter writer = new OutputStreamWriter(stream);
			writer.write(name + ":Event time is: " + endDate + "; Log Information: " + log + "\n");
			writer.close();
			stream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public static void writePortfolioUpdateLog(String name, Date endDate, String log){
		try{
			endDate = new Date();
			String path = getLogPath(endDate,"portfolioUpdateLog.html");
			FileOutputStream stream = new FileOutputStream(path, true);
			OutputStreamWriter writer = new OutputStreamWriter(stream);
			writer.write(name + ":Event time is: " + endDate + "; Log Information: " + log + "\n");
			writer.close();
			stream.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void writeFinancialLog(String name, Date logDate, int year, int quarter, String log) {
		try {
			FileOutputStream stream = new FileOutputStream(getFinancialLogPath(logDate), true);
			OutputStreamWriter writer = new OutputStreamWriter(stream);
			String str="";
			if(quarter==1) str="1st Quater";
			else if(quarter==2) str="2nd Quater";
			else if(quarter==3) str="3rd Quater";
			else if(quarter==4) str="4th Quater";
			writer.write(name + ":\t" + log + ":" + year + "-" + str + ".\t" + "LogDate:" + logDate + "\t" + "\r\n");
			writer.close();
			stream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void writeMarketEmailLog(Date logDate,String log){
		try {
			FileOutputStream stream = new FileOutputStream(getMarketEmailLogPath(logDate), true);
			OutputStreamWriter writer = new OutputStreamWriter(stream);
			writer.write("LogDate:"+logDate+"\t"+log+"\r\n");
			writer.close();
			stream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void writeYFinancialLog(String name, Date logDate, int year, String log) {
		try {
			FileOutputStream stream = new FileOutputStream(getFinancialLogPath(logDate), true);
			OutputStreamWriter writer = new OutputStreamWriter(stream);
			writer.write(name + ":\t" + log + ":" + year +".\t" + "LogDate:" + logDate + "\t" + "\r\n");
			writer.close();
			stream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @author SuPing
	 * @param string
	 * @param logDate
	 * 2009-10-26
	 */
	public static void writeFinancialChkLog(String string, Date logDate){
		try {
			FileOutputStream stream = new FileOutputStream(getFinancialLogPath(logDate), true);
			OutputStreamWriter writer = new OutputStreamWriter(stream);
			writer.write(string+ "\t" +"LogDate:" + logDate + "\t" + "\r\n");
			writer.close();
			stream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void writeFinancialLog(String inf,Date date,String str){
		try {
			FileOutputStream stream = new FileOutputStream(getFinancialLogPath(date), true);
			OutputStreamWriter writer = new OutputStreamWriter(stream);
			writer.write(inf+ "\t" +"LogDate:" + date + "\t" + "str" +"\r\n");
			writer.close();
			stream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static String getFundTable() {
		return getTempDir();
	}
	
	public final static String JFORUM_FORUMS = "News and Commentaries";
	public final static String JFORUM_Announcements = "Announcements";
	public final static String JFORUM_Retirement = "Retirement Investing News and Articles";
	public final static int TOPICS_NUMBER=5;
	public final static String DEFAULT_SYMBOL="SPY";
	
	public final static String SECTOR_TECHNOLOGY = "TECHNOLOGY";
	public final static String SECTOR_FINANCIAL = "FINANCIAL";
	public final static String SECTOR_UTILITIES = "UTILITIES";
	public final static String SECTOR_BASIC_MATERIALS = "BASIC MATERIALS";
	public final static String SECTOR_CONGLOMERATES = "CONGLOMERATES";
	public final static String SECTOR_CONSUMER_GOODS = "CONSUMER GOODS";
	public final static String SECTOR_HEALTHCARE = "HEALTHCARE";
	public final static String SECTOR_INDUSTRIAL_GOODS = "INDUSTRIAL GOODS";
	public final static String SECTOR_SERVICES = "SERVICES";
	public final static String SECTOR_OTHERS = "OTHERS";
	public final static String SECTOR_TOP_SECTOR = "Top Sector";

	public final static String CLONE_CENTER_TOP_10 = "Top 10";
	public final static String CLONE_CENTER_TOTAL_POSITION = "Total Position";
	
	@Deprecated
	public final static Integer STRATEGY_NORMAL_TYPE=0;
	@Deprecated
	public final static Integer STRATEGY_PRESERVED_TYPE=1;
	@Deprecated
	public final static Integer STRATEGY_401K_TYPE=2;
	@Deprecated
	public final static String STRATEGY_401K="401K Plan Center";
	public final static String GROUP_MPIQ_B="MPIQ_B";
	
	public final static int PRICE_TYPE_CLOSE=1;
	public final static int PRICE_TYPE_OPEN=2;
	public final static int PRICE_TYPE_ADJCLOSE=3;
	public final static int PRICE_TYPE_ADJNAV=4;
	
	public final static int AMOUNT_TYPE_EQUITY=1;
	public final static int AMOUNT_TYPE_COLLECTION=2;
	
	//for evergreenplans' forumPosts
	public final static String FORUM_NAME_F401K = "Plan Discussions";
	public final static String CATEGORY_NAME_F401K = "Discussion Forums";
	
	//ltisystem_userresource
	public final static int USER_RESOURCE_STRATEGY_OR_401KPLAN = 1;
	public final static int USER_RESOURCE_PORTFOLIO = 2;
	public final static int USER_RESOURCE_FUNDTABLE = 3;
	public final static int USER_RESOURCE_PAYMENTITEM = 4;
	public final static int USER_RESOURCE_STRATEGY_OR_401KPLAN_CREATE = 5;
	public final static int USER_RESOURCE_STRATEGY_OR_401KPLAN_CREATE_AND_USE = 6;
	
	//用户创建plan
	public final static int USER_RESOURCE_PLAN_CREATE = 10;
	//用户customize portfolio 使用plan
	public final static int USER_RESOURCE_PLAN_REFERENCE = 11;
	//用户查看FUNDTABLE
	public final static int USER_RESOURCE_PLAN_FUNDTABLE = 12;
	//用户Customize portfolio
	public final static int USER_RESOURCE_PORTFOLIO_CUSTOMIZE = 13;
	//用户Follow portfolio
	public final static int USER_RESOURCE_PORTFOLIO_FOLLOW = 14;
	//用户查看Real Time
	public final static int USER_RESOURCE_PORTFOLIO_REALTIME = 15;
	
	public final static int USER_RESOURCE_PORTFOLIO_ON_PLAN_PAGE = 16;
	
	public final static int USER_RESOURCE_PORTFOLIO_FOLLOW_EXPIRED = 17;
	
	public final static int USER_RESOURCE_PORTFOLIO_CUSTOMIZE_EXPIRED = 18;
	
	public final static int USER_RESOURCE_PLAN_CREATE_EXPIRED = 19;
	
	public final static int USER_RESOURCE_PLAN_FUNDTABLE_EXPIRED = 20;
	
	
	public final static int MYPLANIQ_PERMISSION_MESSAGE_PORTFOLIO_FOLLOW_SUCCESS_TYPE = 0;
	public final static String MYPLANIQ_PERMISSION_MESSAGE_PORTFOLIO_FOLLOW_SUCCESS = "This portfolio has been added to \" My Followed Public Portfolios\" table. You will receive rebalance email alerts of this portfolio. You can customize/follow {n} more portfolio(s)";
	public final static int MYPLANIQ_PERMISSION_MESSAGE_FOLLOWSAA_TYPE = 1;
	public final static String MYPLANIQ_PERMISSION_MESSAGE_FOLLOWSAA = "You need to register for free to follow SAA portfolios. Please login or register first.";
	public final static int MYPLANIQ_PERMISSION_MESSAGE_FOLLOWTAA_TYPE = 2;
	public final static String MYPLANIQ_PERMISSION_MESSAGE_FOLLOWTAA = "You need to subscribe as Basic User to follow the TAA portfolios. Please subscribe for one month free trial.";
	public final static int MYPLANIQ_PERMISSION_MESSAGE_FOLLOWADV_TYPE = 3;
	public final static String MYPLANIQ_PERMISSION_MESSAGE_FOLLOWADV = "You need to subscribe as Expert User to follow the ADV portfolios.";
	public final static int MYPLANIQ_PERMISSION_MESSAGE_PORTFOLIO_FOLLOW_COUNT_TYPE = 4;
	public final static String MYPLANIQ_PERMISSION_MESSAGE_PORTFOLIO_FOLLOW_COUNT_ = "You have customize/follow {n} portfolios.";
	
	


	//ltisystem_useroperation
	//OperationType
	public final static int USER_OPERATION_PAYMENT = 1;
	public final static int USER_OPERATION_STRATEGY_OR_401KPLAN = 2;
	public final static int USER_OPERATION_PORTFOLIO = 3;
	public final static int USER_OPERATION_FUNDTABLE = 4;
	//OptDescription
	public final static String USER_OPERATION_PAYMENT_FREETRY = "FREE_TRY";
	public final static String USER_OPERATION_PAYMENT_SUBSCRIBE = "SUBSCRIBE";
	public final static String USER_OPERATION_PAYMENT_CANCEL = "CANCEL";
	public final static String USER_OPERATION_PAYMENT_REFUND = "REFUND";
	public final static String USER_OPERATION_PAYMENT_OTHER = "OTHER";

	public final static String USER_OPERATION_STRATEGY_OR_401KPLAN_ID = "CHOOSE STRATEGY_OR_401KPLAN";
	public final static String USER_OPERATION_PORTFOLIO_ID = "CHOOSE PORTFOLIO";
	public final static String USER_OPERATION_PORTFOLIO_PLAN_ID = "CHOOSE PLAN FUNDTABLE";

	//OptCondition
	public final static String USER_OPERATION_SUCCESS = "SUCCESS";
	public final static String USER_OPERATION_UNBEGIN = "UNBEGIN";
	public final static String USER_OPERATION_FAILURE = "FAILURE";

	//ltisystem_userpayment
	public final static int USER_PAYTYPE_FREETRY = 1;
	public final static int USER_PAYTYPE_SUBSCRIBE = 2;
	public final static int USER_PAYTYPE_CANCEL = 3;
	public final static int USER_PAYTYPE_REFUND = 4;
	public final static int USER_PAYTYPE_OTHER = 5;
	public static final long PORTFOLIO_HOLDING_ORIGINAL = 1;
	public static final long PORTFOLIO_HOLDING_CURRENT = 2;
	public static final long PORTFOLIO_HOLDING_DELAY = 3;
	public static final long PORTFOLIO_HOLDING_EXPECTED = 4;
	
	public static final String ATTRIBUTE_KEY_CONFIDENCE = "confidence";
	public static final String ATTRIBUTE_KEY_STARTINGDATE = "startingdate";
	public static final String ATTRIBUTE_KEY_MARGINRATE = "marginrate";
	
	
	public static boolean isF401KDomain(String url){
		boolean flag=false;
		String[] strs=Configuration.get("f401kdomain").toString().split("\\|");
		for(int i=0;i<strs.length;i++){
			if(url.toLowerCase().contains(strs[i].trim())){
				flag=true;
				break;
			}
		}
		return flag;
	}
	
	public static final String fastUpdateDetailLogFilePath = "fastdetail";
	public static final String fastUpdateSimpleLogFilePath = "fastsimple";
}
