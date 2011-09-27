package com.lti.util;

import java.io.File;

import com.lti.system.Configuration;

public class Edgar11K {
	
	public final static String rootPath = Configuration.get11KDir();
	
//	public static final String K1 = "of America";
//	public static final String V1 = "of Amer";
//	
//	public static final String K2 = "Gr ";
//	public static final String V2 = "Growth ";
//	
//	public static final String K3 = "and";
//	public static final String V3 = "&";
//	
//	public static final String K4 = "Ivy Science and Technology";
//	public static final String V4 = "American Funds Fundamental Invs";
//	
//	public static final String K5 = "Alliance Bernstein";
//	public static final String V5 = "AllianceBernstein";
//	
//	public static final String K6 = "JP Morgan";
//	public static final String V6 = "JPMorgan";
//	
//	public static final String K7 = "Mid-cap";
//	public static final String V7 = "Mid cap";
//	
//	public static final String K8 = "Rainier:";
//	public static final String V8 = "Rainier";
//	
//	public static final String K9 = "Market";
//	public static final String V9 = "Mkt";
//	
//	public static final String K10 = "small";
//	public static final String V10 = "sm";
//	
//	public static final String K11 = "Institutional";
//	public static final String V11 = "Inst";
//	
//	public static final String K12 = "Fidelity Fidelity";
//	public static final String V12 = "Fidelity";
//	
//	public static final String K13 = "Retirement";
//	public static final String V13 = "Retirement";
//	
//	public static final String K14 = "New York";
//	public static final String V14 = "NY";
//	
//	public static final String K15 = "SSgA";
//	public static final String V15 = "State Street Global Advisor";
//	
//	public static final String K16 = "Intermediate";
//	public static final String V16 = "interm";
//	
//	public static final String K17 = "TRP ";
//	public static final String V17 = "T. Rowe Price ";
//	
//	public static final String K18 = "AIMCO";
//	public static final String V18 = "Apartment Investment and Management Company";
//	
//	public static final String K19 = "Advantage";
//	public static final String V19 = "Adv";
//	
//	public static final String K20 = "BGI ";
//	public static final String V20 = "Barclays Global Investors ";
//	
//	public static final String K21 = "PIMCO";
//	public static final String V21 = "Pacific Investment Management Company";
//	
//	public static final String K22 = "Growth & Income";
//	public static final String V22 = "G/I";
//	
//	public static final String K23 = "street";
//	public static final String V23 = "St";
//	
//	public static final String K24 = " INTL";
//	public static final String V24 = " international";
//	
//	public static final String K25 = " Inc ";
//	public static final String V25 = " income ";
//	
//	public static final String K26 = "WFA ";
//	public static final String V26 = "Wells Fargo Advantage ";
//	
//	public static final String K27 = "CL ";
//	public static final String V27 = "class ";
//	
//	public static final String K28 = "GRTH & INC";
//	public static final String V28 = "G/I";
//	
//	public static final String K29 = "Hi Inc Tr";
//	public static final String V29 = "HIGH INCOME TRUST";
//	
//	public static final String K30 = "INT’L";
//	public static final String V30 = "international";
//	
//	public static final String K31 = "AND";
//	public static final String V31 = "&";
//	
//	public static final String K32 = "MNGD";
//	public static final String V32 = "Managed";
//	
//	public static final String K33 = "EMERG";
//	public static final String V33 = "Emerging";
//	
//	public static final String K34 = "EQUIP&SYSTEM";
//	public static final String V34 = "Equip/Systems";
//	
//	public static final String K35 = "ORDINARY";
//	public static final String V35 = "ord";
//	
//	public static final String K36 = "LD";
//	public static final String V36 = "lord";
//	
//	public static final String K37 = "SMCP";
//	public static final String V37 = "SMALL CAP";
//	
//	public static final String K38 = "BLD";
//	public static final String V38 = "BLEND";
//	
//	public static final String K39 = "MIDCP";
//	public static final String V39 = "MID CAP";
//	
//	public static final String K40 = "NB ";
//	public static final String V40 = "Neuberger Berman ";
//	
//	public static final String K41 = "Trust";
//	public static final String V41 = "Tr";
//	
//	public static final String K42 = "TOUCHSTN";
//	public static final String V42 = "Touchstone";
//	
//	public static final String K43 = "sc ";
//	public static final String V43 = "Sands Capital ";
//	
//	public static final String K44 = "SEL ";
//	public static final String V44 = "select ";
//	
//	public static final String K45 = "GR ";
//	public static final String V45 = "growth ";
//	
//	public static final String K46 = "WA ";
//	public static final String V46 = "western asset";
//	
//	public static final String K47 = "GRTH";
//	public static final String V47 = "growth";
//	
//	public static final String K48 = "CONS ";
//	public static final String V48 = "consumer ";
//	
//	public static final String K49 = "INVST";
//	public static final String V49 = "invest";
//	
//	public static final String K50 = "gr ";
//	public static final String V50 = "grade ";
//	
//	public static final String K51 = " BD";
//	public static final String V51 = " bond";
//	
//	public static final String K52 = "Biotechnology";
//	public static final String V52 = "BIOTECH";
//	
//	public static final String K53 = "SVCS";
//	public static final String V53 = "service";
//	
//	public static final String K54 = "GOVT";
//	public static final String V54 = "government";
//	
//	public static final String K55 = " CAP";
//	public static final String V55 = " capital ";
//
//	public static final String K56 = " MGR";
//	public static final String V56 = " manager";
//	
//	public static final String K57 = "Export";
//	public static final String V57 = "exp";
//	
//	public static final String K58 = "Appreciation";
//	public static final String V58 = "app";
//	
//	public static final String K59 = "Retirement";
//	public static final String V59 = "rtmt";
//	
//	public static final String K60 ="STRAT";
//	public static final String V60 = "Strategic";
//	
//	public static final String K61 = "DIV & INC";
//	public static final String V61 = "Dividend & Income";
//	
//	public static final String K62 = " COMP";
//	public static final String V62 = " Composite";
//	
//	public static final String K63 = "NET";
//	public static final String V63 = "network";
//	
//	public static final String K64 = "INFSTR";
//	public static final String V64 = "Infrastructure";
//	
//	public static final String K65 = "LMP ";
//	public static final String V65 = "Legg Mason Partners ";
//	
//	public static final String K66 = "COL/ACORN";
//	public static final String V66 = "Columbia Acorn";
//	
//	public static final String K67 = "ALL/BERN";
//	public static final String V67 = "AllianceBernstein";
//	
//	public static final String K68 = "ALLNZ";
//	public static final String V68 = "Allianz";
//	
//	public static final String K69 = "WFA ";
//	public static final String V69 = "Wells Fargo Advantage ";
//	
//	public static final String K70 = "EQ INC";
//	public static final String V70 = "Equity and Income";
//	
//	public static final String K71 = "AFFILTD";
//	public static final String V71 = "Affiliated";
//	
//	public static final String K72 = "VK ";
//	public static final String V72 = "Van Kampen ";
//	
//	public static final String K73 = "DREY ";
//	public static final String V73 ="Dreyfus ";
//	
//	public static final String K74 = "TMPL";
//	public static final String V74 = "TEMPLETON";
//	
//	public static final String K75 = " DEV";
//	public static final String V75 = " Developing";
//	
//	public static final String K76 = "Long-Term";
//	public static final String V76 = "LT";
//	
//	public static final String K77 = "CALVERT SIF";
//	public static final String V77 = "Calvert Social Investment";
//	
//	public static final String K78 = "MSIF";
//	public static final String V78 = "Morgan Stanley Inst";
//	
//	public static final String K79 = "ABF ";
//	public static final String V79 = "American Beacon ";
//	
//	public static final String K80 = "JBT ";
//	public static final String V80 = "John Bean Technologies ";
//	
//	public static final String K81 = "State Street Global Advisors";
//	public static final String V81 = "SSgA";
//	
//	public static final String K82 = "Artisan Midcap Value Fund";//NOTICE
//	public static final String V82 = "Artisan Midcap Value Fund";
//	
//	public static final String K83 = "Principal Large Capital";
//	public static final String V83 = "Principal Large Cap";
//	
//	public static final String K84 = "Marshall Small Cap";
//	public static final String V84 = "Marshall Small-Cap";
//	
//	public static final String K85 = "Hartford Investment Financial Services";
//	public static final String V85 = "Hartford Fin l Svcs";
//	
//	public static final String K86 = "Massachusetts Financial Services";
//	public static final String V86 = "MFS";
//	
//	public static final String K87 = "Hartford Capital app Fund";
//	public static final String V87 = "Hartford Capital Appreciation";
//	
//	public static final String K88 = "Vanguard Index Trust 500 Fund";
//	public static final String V88 = "Vanguard 500 Index";
//	
//	public static final String K89 = "Putnam International Growth Fund PNPRX";//NOTICE
//	public static final String V89 = "Putnam International Growth Fund PNPRX";
//	
//	public static final String K90 = "Barclays Global Investors LIFEPATH INDEX RETIREMENT";
//	public static final String V90 = "Barclays Global Investors LP Retire";
//	
//	public static final String K91 = "Fifth Third LifeModel Mod Agg INSTL";
//	public static final String V91 = "Fifth Third LifeModel Mod Agrsv Instl";
//	
//	public static final String K92 = "Vanguard Life Strategy";
//	public static final String V92 = "Vanguard LifeStrategy";
//	
//	public static final String K93 = "Vanguard Mid Cap";
//	public static final String V93 = "Vanguard Mid Capitalization";
//	
//	public static final String K94 = "Royce Value Plus Service";
//	public static final String V94 = "Royce Value Plus Svc";
//	
//	public static final String K95 = "Fund Mutual Funds";
//	public static final String V95 = "";
//	
//	public static final String K96 = "Fund Mutual Fund";
//	public static final String V96 = "";
//	
//	public static final String K97 = "Pacific Investment Management Company";
//	public static final String V97 = "PIMCO";
//	
//	public static final String K98 = "LOOMIS SAYLES INVT GRADE";
//	public static final String V98 = "Loomis Sayles Investment Grade";
//	
//	public static final String K99 = "T. Rowe Price";
//	public static final String V99 = "T. Rowe Price Retirement";
//	
//	public static final String K100 = " rtmt Income";
//	public static final String V100 = "";
//
//	public static String[] keys = {K1,K2,K3,K4,K5,K6,K7,K8,K9,K10,
//								  K11,K12,K13,K14,K15,K16,K17,K18,K19,K20,
//								  K21,K22,K23,K24,K25,K26,K27,K28,K29,K30,
//								  K31,K32,K33,K34,K35,K36,K37,K38,K39,K40,
//								  K41,K42,K43,K44,K45,K46,K47,K48,K49,K50,
//								  K51,K52,K53,K54,K55,K56,K57,K58,K59,K60,
//								  K61,K62,K63,K64,K65,K66,K67,K68,K69,K70,
//								  K71,K72,K73,K74,K75,K76,K77,K78,K79,K80,
//								  K81,K82,K83,K84,K85,K86,K87,K88,K89,K90,
//								  K91,K92,K93,K94,K95,K96,K97,K98,K99,K100};
//	
//	public static String[] values = {V1,V2,V3,V4,V5,V6,V7,V8,V9,V10,
//									 V11,V12,V13,V14,V15,V16,V17,V18,V19,V20,
//									 V21,V22,V23,V24,V25,V26,V27,V28,V29,V30,
//									 V31,V32,V33,V34,V35,V36,V37,V38,V39,V40,
//									 V41,V42,V43,V44,V45,V46,V47,V48,V49,V50,
//									 V51,V52,V53,V54,V55,V56,V57,V58,V59,V60,
//									 V61,V62,V63,V64,V65,V66,V67,V68,V69,V70,
//									 V71,V72,V73,V74,V75,V76,V77,V78,V79,V80,
//									 V81,V82,V83,V84,V85,V86,V87,V88,V89,V90,
//									 V91,V92,V93,V94,V95,V96,V97,V98,V99,V100};
	
	
//	/**
//	 * 初始化字符串等价替换文件
//	 * @param fileName
//	 * @return 
//	 * @throws IOException
//	 */
//	public static void initialMapFromSourceFile() throws IOException{
//		equalMap = new HashMap<String, String>();
//		File file = new File(Configuration.get13FDir(), Configuration.MATCHING_RULE_FILENAME);
//		FileReader fr = new FileReader(file);
//		BufferedReader br = new BufferedReader(fr);
//		String line = br.readLine();
//		while(line != null && !line.equals("")){
//			String[] pairs = line.trim().split(",");
//			if(pairs.length == 2){
//				equalMap.put(pairs[0].trim(), pairs[1].trim());
//			}
//			line = br.readLine();
//		}
//	}
	
//	public static void createFileFromFinalString(String fileName) throws IOException{
//		File csv = new File(fileName);
//		BufferedWriter bw = new BufferedWriter(new FileWriter(csv, true));
//		for(int i=0;i<keys.length;++i){
//			 bw.write(keys[i].trim() + "," + values[i].trim());
//			 bw.newLine();
//		}
//		bw.newLine(); 
//		bw.close(); 
//	}
	
	public static String extract11KFund(String filePath,String fileName,String oldFileName){
		try {
			
			
			String title = getTitleOf11KPlan(filePath);
			if(title==null) title="Can not get the plan title.";
			StringBuffer contentOfPlan = new StringBuffer();
			contentOfPlan.append("<strong>"+title+"</strong><br>");
			contentOfPlan.append(oldFileName);
			contentOfPlan.append("<br>");
			
			String fileContent = FileOperator.getContent(filePath);
			
			String anotherTitle = null;
			
			int indexOfIdentity = fileContent.indexOf("Identity");
			if(indexOfIdentity<0){
				createPlan(fileName,contentOfPlan.toString());
				return null;
			}
			
			while(indexOfIdentity > 0 && indexOfIdentity < fileContent.length()){
					
				int endOfTable = fileContent.indexOf("</table>", indexOfIdentity)>0?fileContent.indexOf("</table>", indexOfIdentity):fileContent.indexOf("</TABLE>", indexOfIdentity);
					
				if(endOfTable < 0 ){
					break;
				}
					
				int m = indexOfIdentity;
					
				while(m>0 && m<endOfTable){
					int lineOfStart = fileContent.indexOf("<tr", m)>0?fileContent.indexOf("<tr", m):fileContent.indexOf("<TR",m);
					
					if(lineOfStart < 0){
						indexOfIdentity = fileContent.indexOf("Identity", endOfTable);
						break;
					}
					int lineOfEnd = fileContent.indexOf("</tr>", lineOfStart)>0?fileContent.indexOf("</tr>", lineOfStart):fileContent.indexOf("</TR>", lineOfStart);
						
					if(lineOfEnd < 0){
						indexOfIdentity = fileContent.indexOf("Identity", endOfTable);
						break;
					}
					String lineStr = getContentBetweenTags(fileContent,lineOfStart,lineOfEnd);
						
					if(lineStr == null){
						m = lineOfEnd;
						continue;
					}
					
					String tempLineStr = lineStr.toLowerCase();
					if(tempLineStr.contains("common stock of") || tempLineStr.contains("of common stock") || tempLineStr.contains("subtotal") || tempLineStr.contains("participant") || lineStr.contains("Participant Loans")
							|| tempLineStr.contains("brokerage account") 
							||(tempLineStr.contains("corp.") && tempLineStr.contains("common stock"))
							||(tempLineStr.contains("corp ") && tempLineStr.contains("common stock"))
							||(tempLineStr.contains("ind.") && tempLineStr.contains("common stock"))
							||(tempLineStr.contains("ind ") && tempLineStr.contains("common stock"))
							|| tempLineStr.contains("due ")
							||(tempLineStr.contains("corporation") && tempLineStr.contains("common stock"))
							||(tempLineStr.contains("inc.")&& tempLineStr.contains("common stock"))
							||(tempLineStr.contains("inc ")&& tempLineStr.contains("common stock"))
							||(tempLineStr.contains("co.")&& tempLineStr.contains("common stock"))
							||(tempLineStr.contains("co ")&& tempLineStr.contains("common stock"))
							||tempLineStr.contains("contract")
							||tempLineStr.contains("deposited")){
						
							m = lineOfEnd;
							continue;
					}
					
					if(tempLineStr.contains("common stock") && !tempLineStr.contains("fund")){
						m = lineOfEnd;
						continue;
					}
					
					
					String tempStr = lineStr.replaceAll("&#038;"," ").replaceAll("&amp;", " ").replaceAll("&nbsp;"," ").replaceAll("&#150;", " ").
					replaceAll("&#146;", " ").replaceAll("&#160;", " ").replaceAll("&#151;", " ").replaceAll("&"," ").replaceAll(";"," ").replaceAll("\n", " ")
					.replaceAll("\\$","").replaceAll("\r", " ").replaceAll("\\*", "").replaceAll("Mutual fund", " ").replaceAll(" +", " ").trim();
			
					if(tempStr!=null  && tempStr != " " && !tempStr.startsWith("Total")){
						int sN = getStartIndexOfNumber(tempStr);
						if(sN > 0){
							String str = tempStr.substring(0,sN).trim();
							contentOfPlan.append(str.replaceAll(",", ""));
							contentOfPlan.append("<br>");
						}
//						int sN = getStartIndexOfNumber(tempStr);
//						if(sN > 0){
//							String str = tempStr.substring(0,sN).trim();
							
//							for(int j=0; j<rule.size(); j++ ){
//								str = str.replaceAll("\\b(?i)" + rule.get(j) + "\\b", equalMap.get(rule.get(j)));
//							}
//							contentOfPlan.append(str.replaceAll(",", ""));
//							contentOfPlan.append("<br>");
//						}
					}
					
					m = lineOfEnd;
					
				}
				
				indexOfIdentity = fileContent.indexOf("Identity", endOfTable);
			
			}
				
			createPlan(fileName,contentOfPlan.toString());
			System.out.println(contentOfPlan.toString());
			return contentOfPlan.toString();
			
			}catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}


	public static void createPlan(String fileName, String content) throws Exception{
		File root=new File(rootPath,"DataFile");
		if(!root.exists()){
			root.mkdirs();
		}
		File planFile = new File(root,fileName+".csv");
		
		if(!planFile.exists()){
			planFile.createNewFile();
		}
		if(content == null){
			content = "check it!!";
		}
		FileOperator.appendMethodB(planFile.getAbsolutePath(), content, false);
	}
	
	
	public static String getContentBetweenTags(String s, int start, int end){
		
		int currentPosition = start;
		StringBuffer sb = new StringBuffer(); 
		if(start<0 || end<0 || start>end){
			return null;
		}
		while(currentPosition < end){
			if(s.charAt(currentPosition)=='<'){
				currentPosition = s.indexOf(">", currentPosition);
				if(currentPosition < end){
					currentPosition++;
				}
			}
			if(currentPosition < end){
				if(s.charAt(currentPosition)!='<'){
					if(s.charAt(currentPosition)!='\r'){
						sb.append(s.charAt(currentPosition));
					}
				currentPosition++;
				}
			}
		}
		return sb.toString();
	}
	
	public static int getStartIndexOfNumber(String str){
		int k = 0;
		while(k < str.length()){
			if(java.lang.Character.isDigit(str.charAt(k))){
				if((k+1<str.length()) && str.charAt(k+1)==','){
					return k-1;
				}else if((k+2<str.length()) && java.lang.Character.isDigit(str.charAt(k+1)) && str.charAt(k+2)==',' ){
					return k-1;
				}else if ((k+3<str.length()) && java.lang.Character.isDigit(str.charAt(k+1)) && java.lang.Character.isDigit(str.charAt(k+2)) && str.charAt(k+3)==','){
					return k-1;
				}else if (k+3<str.length() && java.lang.Character.isDigit(str.charAt(k+1)) && java.lang.Character.isDigit(str.charAt(k+2)) && (java.lang.Character.isDigit(str.charAt(k+3))||!java.lang.Character.isDigit(str.charAt(k+3)))){
					k = k + 4;
					continue;
				}else if((k+2<str.length()) && java.lang.Character.isDigit(str.charAt(k+1)) && java.lang.Character.isDigit(str.charAt(k+2))){
					return k-1;
				}
				else{
					k++;
				}
			}else{
				k++;
			}
		}
		return 0;
	}
	
	public static int getStartIndexOfNumber(String str,int index){
		int k = index;
		while(k < str.length()){
			if(java.lang.Character.isDigit(str.charAt(k))){
				if((k+1<str.length()) && str.charAt(k+1)==','){
					return k-1;
				}else if((k+2<str.length()) && java.lang.Character.isDigit(str.charAt(k+1)) && str.charAt(k+2)==',' ){
					return k-1;
				}else if ((k+3<str.length()) && java.lang.Character.isDigit(str.charAt(k+1)) && java.lang.Character.isDigit(str.charAt(k+2)) && str.charAt(k+3)==','){
					return k-1;
				}else if (k+3<str.length() && java.lang.Character.isDigit(str.charAt(k+1)) && java.lang.Character.isDigit(str.charAt(k+2)) && (java.lang.Character.isDigit(str.charAt(k+3))||!java.lang.Character.isDigit(str.charAt(k+3)))){
					k = k + 4;
					continue;
				}else if((k+2<str.length()) && java.lang.Character.isDigit(str.charAt(k+1)) && java.lang.Character.isDigit(str.charAt(k+2))){
					return k-1;
				}
				else{
					k++;
				}
			}else{
				k++;
			}
		}
		return 0;
	}
	
	
	public static String getTitleOf11KPlan(String filePath){
		String fileContent;
		String title;
		try {
			
			int end;
			fileContent = FileOperator.getContent(filePath);
			int start = fileContent.indexOf("Full title of the plan and") > 0 ? fileContent.indexOf("Full title of the plan and"):fileContent.indexOf("Full title of the Plan and the address");
			
			if(start < 0){
				start = fileContent.indexOf("Commission file number");
				if(start < 0){
					return null;
				}
				int indexOfTitle = fileContent.indexOf("(Full title of the plan)")>0?fileContent.indexOf("(Full title of the plan)"):fileContent.indexOf("(Full title of the Plan)");
				if(indexOfTitle < 0){
					return null;
				}
				title = getContentBetweenTags(fileContent, start+32, indexOfTitle).replaceAll("&#038;"," ").replaceAll("&amp;", " ").replaceAll("&nbsp;"," ")
				.replaceAll("&#150;", " ").replaceAll("&#146;", " ").replaceAll("&#160;", " ").replaceAll("&#151;", " ").replaceAll("\n", " ").replaceAll("&"," ").replaceAll(";"," ").replaceAll("\r", " ").replaceAll("\\*", "").replaceAll(" +", " ").trim();
				
				int indexOfPlan =  title.indexOf("plan")>0 ? title.indexOf("plan") : title.indexOf("Plan");
				
				if(indexOfPlan < 0){
					indexOfPlan = title.indexOf("PLAN");
				}
				title = title.substring(0, indexOfPlan+4);

				return title;
				
			}else{
				end = fileContent.indexOf("Name of issuer ");
				if(end == -1){
					return null;
					
				}
				int k = fileContent.indexOf(":", start)+1;

				title = getContentBetweenTags(fileContent,k,end).replaceAll("&#038;"," ").replaceAll("&amp;", " ").replaceAll("&nbsp;"," ")
				.replaceAll("&#150;", " ").replaceAll("&#146;", " ").replaceAll("&#160;", " ").replaceAll("&#151;", " ").replaceAll("\n", " ").replaceAll("&"," ").replaceAll(";"," ").replaceAll("\r", " ").replaceAll("\\*", "").replaceAll(" +", " ").trim();
				
				int indexOfPlan =  title.indexOf("plan")>0 ? title.indexOf("plan") : title.indexOf("Plan");
				if(indexOfPlan < 0){
					indexOfPlan = title.indexOf("PLAN");
				}
				title = title.substring(0, indexOfPlan+4);

				return title;
			}
			
		}catch (Exception e) {
			//e.printStackTrace();
			title = null;
		}
	return title;
 }
	
	public static int getStartIndexOfNewLine(String str,int index){
		int k = index;
		while(k+2<str.length() && (str.charAt(k)=='\r'|| str.charAt(k)=='\n'||str.charAt(k)==' '||java.lang.Character.isDigit(str.charAt(k)))){
			if(str.charAt(k)=='\r'&& (k+1)<str.length() && str.charAt(k+1)=='\n'){
				k = str.indexOf("\n", k);
				if(k<0 || k+2 >=str.length()){
					return -1;
				}else{
					k = k + 1;
				}
			}else if(str.charAt(k)=='\n'){
				if(k<0 || k+2 >=str.length()){
					return -1;
				}else{
					k = k + 1;
				}
			}else if(str.charAt(k)==' '){
				k = str.indexOf("\n", k);
				if(k<0 || k+2 >=str.length()){
					return -1;
				}else{
					k = k + 1;
				}
			}else if(java.lang.Character.isDigit(str.charAt(k))){
				k = str.indexOf("\n", k);
				if(k<0 || k+2 >=str.length()){
					return -1;
				}else{
					k = k + 1;
				}
			}
			
		}
		if(k<str.length()){
			return k;
		}else{
			return -1;
		}
		
	}
	
	
	public static void main(String[] args) throws Exception{
		//extract11KFund("C:\\validfi\\11k\\edgar\\data\\18748\\0000891092-10-003417.txt", "0018748", "0018748");
		//createFileFromFinalString("F:\\matchrule.csv");
		String first = "abcdefg";
		String second = first;
		second = second.replaceAll("cd", "ab");
		System.out.println(first);
		System.out.println(second);
	}
	
}
