package com.lti.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.lti.action.Action;
import com.lti.service.AssetClassManager;
import com.lti.service.SecurityManager;
import com.lti.service.StrategyManager;
import com.lti.service.bo.AssetClass;
import com.lti.service.bo.Security;
import com.lti.service.bo.VariableFor401k;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.sun.org.apache.regexp.internal.recompile;

public class Parse401KParameters {
	
	
	public static final String K1 = "of America";
	public static final String V1 = "of Amer";
	
	public static final String K2 = "Gr ";
	public static final String V2 = "Growth ";
	
	public static final String K3 = "and";
	public static final String V3 = "&";
	
	public static final String K4 = "Ivy Science and Technology";
	public static final String V4 = "American Funds Fundamental Invs";
	
	public static final String K5 = "Alliance Bernstein";
	public static final String V5 = "AllianceBernstein";
	
	public static final String K6 = "JP Morgan";
	public static final String V6 = "JPMorgan";
	
	public static final String K7 = "Mid-cap";
	public static final String V7 = "Mid cap";
	
	public static final String K8 = "Rainier:";
	public static final String V8 = "Rainier";
	
	public static final String K9 = "Market";
	public static final String V9 = "Mkt";
	
	public static final String K10 = "small";
	public static final String V10 = "sm";
	
	public static final String K11 = "Institutional";
	public static final String V11 = "Inst";
	
	public static final String K12 = "Fidelity Fidelity";
	public static final String V12 = "Fidelity";
	
	public static final String K13 = "Retirement";
	public static final String V13 = "Retirement";
	
	public static final String K14 = "New York";
	public static final String V14 = "NY";
	
	public static final String K15 = "SSgA";
	public static final String V15 = "State Street Global Advisor";
	
	public static final String K16 = "Intermediate";
	public static final String V16 = "interm";
	
	public static final String K17 = "TRP ";
	public static final String V17 = "T. Rowe Price ";
	
	public static final String K18 = "AIMCO";
	public static final String V18 = "Apartment Investment and Management Company";
	
	public static final String K19 = "Advantage";
	public static final String V19 = "Adv";
	
	public static final String K20 = "BGI ";
	public static final String V20 = "Barclays Global Investors ";
	
	public static final String K21 = "PIMCO";
	public static final String V21 = "Pacific Investment Management Company";
	
	public static final String K22 = "Growth & Income";
	public static final String V22 = "G/I";
	
	public static final String K23 = "street";
	public static final String V23 = "St";
	
	public static final String K24 = " INTL";
	public static final String V24 = " international";
	
	public static final String K25 = " Inc ";
	public static final String V25 = " income ";
	
	public static final String K26 = "WFA ";
	public static final String V26 = "Wells Fargo Advantage ";
	
	public static final String K27 = "CL ";
	public static final String V27 = "class ";
	
	public static final String K28 = "GRTH & INC";
	public static final String V28 = "G/I";
	
	public static final String K29 = "Hi Inc Tr";
	public static final String V29 = "HIGH INCOME TRUST";
	
	public static final String K30 = "INT’L";
	public static final String V30 = "international";
	
	public static final String K31 = "AND";
	public static final String V31 = "&";
	
	public static final String K32 = "MNGD";
	public static final String V32 = "Managed";
	
	public static final String K33 = "EMERG";
	public static final String V33 = "Emerging";
	
	public static final String K34 = "EQUIP&SYSTEM";
	public static final String V34 = "Equip/Systems";
	
	public static final String K35 = "ORDINARY";
	public static final String V35 = "ord";
	
	public static final String K36 = "LD";
	public static final String V36 = "lord";
	
	public static final String K37 = "SMCP";
	public static final String V37 = "SMALL CAP";
	
	public static final String K38 = "BLD";
	public static final String V38 = "BLEND";
	
	public static final String K39 = "MIDCP";
	public static final String V39 = "MID CAP";
	
	public static final String K40 = "NB ";
	public static final String V40 = "Neuberger Berman ";
	
	public static final String K41 = "Trust";
	public static final String V41 = "Tr";
	
	public static final String K42 = "TOUCHSTN";
	public static final String V42 = "Touchstone";
	
	public static final String K43 = "sc ";
	public static final String V43 = "Sands Capital ";
	
	public static final String K44 = "SEL ";
	public static final String V44 = "select ";
	
	public static final String K45 = "GR ";
	public static final String V45 = "growth ";
	
	public static final String K46 = "WA ";
	public static final String V46 = "western asset";
	
	public static final String K47 = "GRTH";
	public static final String V47 = "growth";
	
	public static final String K48 = "CONS ";
	public static final String V48 = "consumer ";
	
	public static final String K49 = "INVST";
	public static final String V49 = "invest";
	
	public static final String K50 = "gr ";
	public static final String V50 = "grade ";
	
	public static final String K51 = " BD";
	public static final String V51 = " bond";
	
	public static final String K52 = "Biotechnology";
	public static final String V52 = "BIOTECH";
	
	public static final String K53 = "SVCS";
	public static final String V53 = "service";
	
	public static final String K54 = "GOVT";
	public static final String V54 = "government";
	
	public static final String K55 = " CAP";
	public static final String V55 = " capital ";

	public static final String K56 = " MGR";
	public static final String V56 = " manager";
	
	public static final String K57 = "Export";
	public static final String V57 = "exp";
	
	public static final String K58 = "Appreciation";
	public static final String V58 = "app";
	
	public static final String K59 = "Retirement";
	public static final String V59 = "rtmt";
	
	public static final String K60 ="STRAT";
	public static final String V60 = "Strategic";
	
	public static final String K61 = "DIV & INC";
	public static final String V61 = "Dividend & Income";
	
	public static final String K62 = " COMP";
	public static final String V62 = " Composite";
	
	public static final String K63 = "NET";
	public static final String V63 = "network";
	
	public static final String K64 = "INFSTR";
	public static final String V64 = "Infrastructure";
	
	public static final String K65 = "LMP ";
	public static final String V65 = "Legg Mason Partners ";
	
	public static final String K66 = "COL/ACORN";
	public static final String V66 = "Columbia Acorn";
	
	public static final String K67 = "ALL/BERN";
	public static final String V67 = "AllianceBernstein";
	
	public static final String K68 = "ALLNZ";
	public static final String V68 = "Allianz";
	
	public static final String K69 = "WFA ";
	public static final String V69 = "Wells Fargo Advantage ";
	
	public static final String K70 = "EQ INC";
	public static final String V70 = "Equity and Income";
	
	public static final String K71 = "AFFILTD";
	public static final String V71 = "Affiliated";
	
	public static final String K72 = "VK ";
	public static final String V72 = "Van Kampen ";
	
	public static final String K73 = "DREY ";
	public static final String V73 ="Dreyfus ";
	
	public static final String K74 = "TMPL";
	public static final String V74 = "TEMPLETON";
	
	public static final String K75 = " DEV";
	public static final String V75 = " Developing";
	
	public static final String K76 = "Long-Term";
	public static final String V76 = "LT";
	
	public static final String K77 = "CALVERT SIF";
	public static final String V77 = "Calvert Social Investment";
	
	public static final String K78 = "MSIF";
	public static final String V78 = "Morgan Stanley Inst";
	
	public static final String K79 = "ABF ";
	public static final String V79 = "American Beacon ";
	
	public static final String K80 = "JBT ";
	public static final String V80 = "John Bean Technologies ";
	
	public static final String K81 = "State Street Global Advisors";
	public static final String V81 = "SSgA";
	
	public static final String K82 = "Artisan Midcap Value Fund";//NOTICE
	public static final String V82 = "Artisan Midcap Value Fund";
	
	public static final String K83 = "Principal Large Capital";
	public static final String V83 = "Principal Large Cap";
	
	public static final String K84 = "Marshall Small Cap";
	public static final String V84 = "Marshall Small-Cap";
	
	public static final String K85 = "Hartford Investment Financial Services";
	public static final String V85 = "Hartford Fin l Svcs";
	
	public static final String K86 = "Massachusetts Financial Services";
	public static final String V86 = "MFS";
	
	public static final String K87 = "Hartford Capital app Fund";
	public static final String V87 = "Hartford Capital Appreciation";
	
	public static final String K88 = "Vanguard Index Trust 500 Fund";
	public static final String V88 = "Vanguard 500 Index";
	
	public static final String K89 = "Putnam International Growth Fund PNPRX";//NOTICE
	public static final String V89 = "Putnam International Growth Fund PNPRX";
	
	public static final String K90 = "Barclays Global Investors LIFEPATH INDEX RETIREMENT";
	public static final String V90 = "Barclays Global Investors LP Retire";
	
	public static final String K91 = "Fifth Third LifeModel Mod Agg INSTL";
	public static final String V91 = "Fifth Third LifeModel Mod Agrsv Instl";
	
	public static final String K92 = "Vanguard Life Strategy";
	public static final String V92 = "Vanguard LifeStrategy";
	
	public static final String K93 = "Vanguard Mid Cap";
	public static final String V93 = "Vanguard Mid Capitalization";
	
	public static final String K94 = "Royce Value Plus Service";
	public static final String V94 = "Royce Value Plus Svc";
	
	public static final String K95 = "Fund Mutual Funds";
	public static final String V95 = "";
	
	public static final String K96 = "Fund Mutual Fund";
	public static final String V96 = "";
	
	public static final String K97 = "Pacific Investment Management Company";
	public static final String V97 = "PIMCO";
	
	public static final String K98 = "LOOMIS SAYLES INVT GRADE";
	public static final String V98 = "Loomis Sayles Investment Grade";
	
	public static final String K99 = "T. Rowe Price";
	public static final String V99 = "T. Rowe Price Retirement";
	
	public static final String K100 = " rtmt Income";
	public static final String V100 = "";

	public static String[] keys = {K1,K2,K3,K4,K5,K6,K7,K8,K9,K10,
								  K11,K12,K13,K14,K15,K16,K17,K18,K19,K20,
								  K21,K22,K23,K24,K25,K26,K27,K28,K29,K30,
								  K31,K32,K33,K34,K35,K36,K37,K38,K39,K40,
								  K41,K42,K43,K44,K45,K46,K47,K48,K49,K50,
								  K51,K52,K53,K54,K55,K56,K57,K58,K59,K60,
								  K61,K62,K63,K64,K65,K66,K67,K68,K69,K70,
								  K71,K72,K73,K74,K75,K76,K77,K78,K79,K80,
								  K81,K82,K83,K84,K85,K86,K87,K88,K89,K90,
								  K91,K92,K93,K94,K95,K96,K97,K98,K99,K100};
	
	public static String[] values = {V1,V2,V3,V4,V5,V6,V7,V8,V9,V10,
									 V11,V12,V13,V14,V15,V16,V17,V18,V19,V20,
									 V21,V22,V23,V24,V25,V26,V27,V28,V29,V30,
									 V31,V32,V33,V34,V35,V36,V37,V38,V39,V40,
									 V41,V42,V43,V44,V45,V46,V47,V48,V49,V50,
									 V51,V52,V53,V54,V55,V56,V57,V58,V59,V60,
									 V61,V62,V63,V64,V65,V66,V67,V68,V69,V70,
									 V71,V72,V73,V74,V75,V76,V77,V78,V79,V80,
									 V81,V82,V83,V84,V85,V86,V87,V88,V89,V90,
									 V91,V92,V93,V94,V95,V96,V97,V98,V99,V100};
	
	private String originalString;

	private SecurityManager securityManager;

	// private AssetClassManager assetClassManager;

	public static Map<Long, AssetClass> cachedAssets = null;
	public static Map<Long, Security> cachedSecurities = null;
	private static HashMap<String,String> equalMap = null;
	private static Object obj=new Object();
	public static AssetClass getAsset(Long id) {
		synchronized (obj) {
			return cachedAssets.get(id);
		}
		
	}

	public static Security getSecurity(Long symbol) {
		synchronized (obj) {
			return cachedSecurities.get(symbol);
		}
		
	}
	
	/**
	 * 初始化字符串等价替换文件
	 * @param fileName
	 * @return 
	 * @throws IOException
	 */
	public static void initialMapFromSourceFile() throws IOException{
		equalMap = new HashMap<String, String>();
		File file = new File(Configuration.get13FDir(), Configuration.MATCHING_RULE_FILENAME);
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		String line = br.readLine();
		while(line != null && !line.equals("")){
			String[] pairs = line.trim().split(",");
			if(pairs.length == 2){
				equalMap.put(pairs[0].trim(), pairs[1].trim());
			}
			line = br.readLine();
		}
		if(equalMap == null || equalMap.size() == 0){
			equalMap = new HashMap<String, String>();
			for(int i=0; i<keys.length; i++){
				equalMap.put(keys[i].trim(), values[i].trim());
			}
		}
	}
	
	public static void initCache() {
		synchronized (obj) {
			cachedAssets = new HashMap<Long, AssetClass>();
			cachedSecurities = new HashMap<Long, Security>();
			AssetClassManager assetClassManager = ContextHolder.getAssetClassManager();
			SecurityManager securityManager = ContextHolder.getSecurityManager();
			List<AssetClass> acs = assetClassManager.getClasses();
			for (int i = 0; i < acs.size(); i++) {
				cachedAssets.put(acs.get(i).getID(), acs.get(i));
				try {
					Security s = securityManager.get(acs.get(i).getBenchmarkID());
					cachedSecurities.put(s.getID(), s);
				} catch (Exception e) {
				}
			}//for
		}// synchronized
		
	}

	private List<VariableFor401k> variables;

	private final static String SEPARATOR = "\t|(\\s{2,10})";

	private String separator;

	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}

	private String[] split(String line, String sep) {
		if (sep == null)
			sep = SEPARATOR;
		return line.split(sep);
	}

	private String getNext(String[] strs, int count) {
		int pos = 0;
		for (int i = 0; i < strs.length; i++) {
			if (strs[i] != null) {
				pos++;
				if (pos == count)
					return strs[i];
			}
		}
		return null;
	}

	private void nullNext(String[] strs, int count) {
		int pos = 0;
		for (int i = 0; i < strs.length; i++) {
			if (strs[i] != null) {
				pos++;
				if (pos == count)
					strs[i] = null;
			}
		}
	}

	private String getSingalDigit(String[] strs) {
		for (int i = 0; i < strs.length; i++) {
			if (strs[i] != null && strs[i].length() == 1 && strs[i].matches("\\d")) {
				String s = strs[i];
				strs[i] = null;
				return s;
			}
		}
		return null;
	}
	
	private static Integer DEFUALT_REDEMPTION=3;

	/**
	 * 从索引的description中精确匹配做为第一结果,如果找不到，就让variable内容全部为空
	 * @param description
	 */
	private VariableFor401k accurateSearch(String description){
		StrategyManager sm = (StrategyManager) ContextHolder.getStrategyManager();
		VariableFor401k variable = sm.getVariableFor401kByDescription(description);
		return variable;
	}
	/**
	 * 从索引的description中模糊匹配做为第三结果,如果找不到，就让variable内容全部为空
	 * @param description
	 * @return
	 */
	private VariableFor401k fuzzySearch(String description){
		VariableFor401k variable = FundTableCachingUtil.find(description);
		return variable;
	}
	
	
//	private void addVariable(String line) {
//		String[] items = split(line, separator);
//		
//		VariableFor401k variable = new VariableFor401k();
//		variable.setDescription("");
//
//		String d = getSingalDigit(items);
//		if (d != null){
//			try {
//				variable.setRedemption(Integer.parseInt(d));
//			} catch (Exception e) {
//				variable.setRedemption(DEFUALT_REDEMPTION);
//			}
//		}
//		else{
//			variable.setRedemption(DEFUALT_REDEMPTION);
//		}
//
//		boolean findSecurity = false;
//		for (int i = 1; i < 3; i++) {
//			String name = getNext(items, i);
//			if (name == null)
//				break;
//			Security s = null;
//			s = securityManager.get(name);
//			if (s != null) {
//				AssetClass ac = null;
//				if (s.getClassID() != null)
//					ac = getAsset(s.getClassID());// assetClassManager.get(s.getClassID());
//				if (ac == null) {
//					variable.setAssetClassName("unknown asset name");
//					variable.setMemo("n");
//				} else {
//					variable.setAssetClassName(ac.getName());
//				}
//				variable.setSymbol( s.getSymbol());
//				variable.setName( s.getName());
//				findSecurity = true;
//				nullNext(items, i);
//				break;
//			}
//		}
//		if (!findSecurity) {
//			for (int i = 1; i < 3; i++) {
//				String name = getNext(items, i);
//				if (name == null)
//					break;
//				if (name.contains("*"))
//					break;
//				List<Security> ss = SecurityCachingUtil.find(name, 5);
//				if (ss != null&&ss.size()>0) {
//					StringBuffer symbols=new StringBuffer();
//					StringBuffer acs=new StringBuffer();
//					StringBuffer names=new StringBuffer();
//					for (int j = 0; j < ss.size(); j++) {
//						Security s = null;
//						s = ss.get(j);
//						AssetClass ac = null;
//						if (s.getClassID() != null)
//							ac = getAsset(s.getClassID());// assetClassManager.get(s.getClassID());
//						if (ac == null) {
//							acs.append("unknown asset name");
//						} else {
//							acs.append(ac.getName());
//						}
//						symbols.append( s.getSymbol());
//						names.append( s.getName().replace("|", "_"));
//						if(j!=ss.size()-1){
//							symbols.append("|");
//							acs.append("|");
//							names.append("|");
//						}
//					}// end j for
//					variable.setName(names.toString());
//					variable.setAssetClassName(acs.toString());
//					variable.setSymbol(symbols.toString());
//					findSecurity = true;
//					nullNext(items, i);
//					break;
//				}// end if
//				
//
//			}// end i for
//		}
//
//		if (!findSecurity) {
//			for (int i = 1; i < 3; i++) {
//				String name = getNext(items, i);
//				if (name == null)
//					break;
//				AssetClass ac = null;
//				ac = AssetClassCachingUtil.find(name);
//				if (ac != null) {
//					variable.setAssetClassName(ac.getName());
//					Security s = null;
//					if (ac.getBenchmarkID() != null)
//						s = getSecurity(ac.getBenchmarkID());// securityManager.get(ac.getBenchmarkID());
//					if (s != null) {
//						variable.setSymbol( s.getSymbol());
//						variable.setName( s.getName());
//					}
//					break;
//				}else{
//					variable.setMemo("n");
//				}
//			}
//		}
//
//		String sep_replace = ",";
//		if (sep_replace.equals(separator)) {
//			sep_replace = " ";
//		}
//		
//		if (items.length > 4 && items[4] != null && !items[4].equals("")) {
//			variable.setDescription(items[4]);
//		} else {
//			variable.setDescription(line.replaceAll(separator, sep_replace));
//		}
//		if (items.length > 5 && items[5] != null && !items[5].equals("")) {
//			variable.setMemo(items[5]);
//		}
//
//		variables.add(variable);
//		
//	}
	
	private VariableFor401k nameSearch(String[] items, String description){
		return nameSearch(items, description, false);
	}
	/**
	 * 从索引的name中模糊匹配做为第二结果,如果找不到，就让variable内容全部为空，如果isSymbol为true, 还要排除CEF
	 * @param items
	 * @param description
	 * @param isSymbol
	 */
	private VariableFor401k nameSearch(String[] items, String description, boolean isSymbol){
		VariableFor401k variable = new VariableFor401k();
		String d = getSingalDigit(items);
		if (d != null){
			try {
				variable.setRedemption(Integer.parseInt(d));
			} catch (Exception e) {
				variable.setRedemption(DEFUALT_REDEMPTION);
			}
		}
		else{
			variable.setRedemption(DEFUALT_REDEMPTION);
		}

		boolean findSecurity = false;
		for (int i = 1; i < 3; i++) {
			String name = getNext(items, i);
			if (name == null)
				break;
			Security s = null;
			s = securityManager.get(name);
			if (s != null) {
				if(!isSymbol && s.getSecurityType() != null && s.getSecurityType() == Configuration.SECURITY_TYPE_CLOSED_END_FUND)
					continue;
				AssetClass ac = null;
				if (s.getClassID() != null)
					ac = getAsset(s.getClassID());// assetClassManager.get(s.getClassID());
				if (ac == null) {
					variable.setAssetClassName("unknown asset name");
					variable.setMemo("n");
				} else {
					variable.setAssetClassName(ac.getName());
				}
				variable.setSymbol( s.getSymbol());
				variable.setName( s.getName());
				findSecurity = true;
				nullNext(items, i);
				break;
			}
		}
		if (!findSecurity) {
			for (int i = 1; i < 3; i++) {
				String name = getNext(items, i);
				if (name == null)
					break;
				if (name.contains("*"))
					break;
				List<Security> ss = SecurityCachingUtil.find(name, 1, isSymbol);
				if (ss != null && ss.size()>0) {
					Security s = ss.get(0);
					variable.setName(s.getName());
					variable.setSymbol(s.getSymbol());
					AssetClass ac = null;
					if (s.getClassID() != null)
						ac = getAsset(s.getClassID());// assetClassManager.get(s.getClassID());
					if (ac == null)
						variable.setAssetClassName("unknown asset name");
					else
						variable.setAssetClassName(ac.getName());
					findSecurity = true;
					nullNext(items, i);
					break;
				}
			}
		}

		if (!findSecurity) {
			for (int i = 1; i < 3; i++) {
				String name = getNext(items, i);
				if (name == null)
					break;
				AssetClass ac = null;
				ac = AssetClassCachingUtil.find(name);
				if (ac != null) {
					variable.setAssetClassName(ac.getName());
					Security s = null;
					if (ac.getBenchmarkID() != null)
						s = getSecurity(ac.getBenchmarkID());// securityManager.get(ac.getBenchmarkID());
					if (s != null) {
						variable.setSymbol( s.getSymbol());
						variable.setName( s.getName());
					}
					break;
				}else{
					variable.setMemo("n");
				}
			}
		}
		if (items.length > 5 && items[5] != null && !items[5].equals("")) {
			variable.setMemo(items[5]);
		}
		variable.setDescription(description);
		
		return variable;
	}
	
	private void addVariable(String line) {
		String[] items = split(line, separator);
		boolean isSymbol = false;
		Double quality = 0.0;
		SecurityManager securityManager = ContextHolder.getSecurityManager();
		try {
			quality = securityManager.getSecuritiesByName(line).get(0).getQuality();
		} catch (Exception e) {
		}
		VariableFor401k variable = new VariableFor401k();
		String description = "";
		String sep_replace = ",";
		if (sep_replace.equals(separator)) {
			sep_replace = " ";
		}
		if (items.length > 4 && items[4] != null && !items[4].equals("")) 
			description = items[4];
		else
			description = line.replaceAll(separator, sep_replace);
		
		String replacedDescription = description;
		if(description.length() < 6)
			isSymbol = true;
		Iterator<String> it = equalMap.keySet().iterator();
		while(it.hasNext()){
			String key = it.next();
			replacedDescription = replacedDescription.replaceAll("\\b(?i)" + key + "\\b", equalMap.get(key));
		}
		VariableFor401k[] vs = new VariableFor401k[3];
		vs[0] = accurateSearch(replacedDescription);
		vs[1] = nameSearch(items, replacedDescription, isSymbol);
		vs[2] = fuzzySearch(replacedDescription);
		StringBuffer symbols=new StringBuffer();
		StringBuffer acs=new StringBuffer();
		StringBuffer names=new StringBuffer();
		StringBuffer memos=new StringBuffer();
		for(int i=0;i<3;++i){
			if(i==0 && vs[0] == null)
				continue;
			if(symbols.length() > 0){
				symbols.append("|");
				acs.append("|");
				names.append("|");
				memos.append("|");
			}
			symbols.append(vs[i].getSymbol());
			acs.append(vs[i].getAssetClassName());
			names.append(vs[i].getName());
			if(vs[i]==null)
				memos.append("n");
			else if(vs[i].getMemo()== null)
				memos.append(" ");
			else
				memos.append(vs[i].getMemo());
				
		}
		variable.setAssetClassName(acs.toString());
		variable.setSymbol(symbols.toString());
		variable.setName(names.toString());
		variable.setMemo(memos.toString());
		variable.setDescription(description);
		variable.setRedemption(vs[1].getRedemption());
		variable.setQuality(quality);
		variables.add(variable);
	}

	public String execute() throws IOException {
		if (cachedAssets == null)
			initCache();
		initialMapFromSourceFile();
		securityManager = ContextHolder.getSecurityManager();
		// assetClassManager = ContextHolder.getAssetClassManager();
		
		
		String[] lines = originalString.replace("\r", "").split("\n");
		variables = new ArrayList<VariableFor401k>();
		if (separator == null)
			separator = SEPARATOR;
	
		for (int i = 0; i < lines.length; i++) {
			String line = lines[i].trim().replace("#", "@=@");
			if (line.equals(""))
				continue;
			addVariable(line);

		}
		return Action.SUCCESS;
	}


	public String getOriginalString() {
		return originalString;
	}

	public void setOriginalString(String originalString) {
		this.originalString = originalString;
	}

	public List<VariableFor401k> getVariables() {
		return variables;
	}

	public void setVariables(List<VariableFor401k> variables) {
		this.variables = variables;
	}
}
