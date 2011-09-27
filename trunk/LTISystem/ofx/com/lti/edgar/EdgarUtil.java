package com.lti.edgar;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilterInputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import com.lti.ofx.InvestmentAccount;
import com.lti.ofx.InvestmentPosition;
import com.lti.ofx.InvestmentPositionList;
import com.lti.ofx.InvestmentPositionListChoice;
import com.lti.ofx.InvestmentPositionListChoiceItem;
import com.lti.ofx.InvestmentStatementResponse;
import com.lti.ofx.InvestmentStatementResponseMessageSetV1;
import com.lti.ofx.InvestmentStatementResponseMessageSetV1Item;
import com.lti.ofx.InvestmentStatementTransactionResponse;
import com.lti.ofx.OFXResponseType;
import com.lti.ofx.PositionMutualFund;
import com.lti.ofx.PositionOther;
import com.lti.ofx.PositionStock;
import com.lti.ofx.SecurityId;
import com.lti.ofx.Status;
import com.lti.ofx.types.CurrencyEnum;
import com.lti.ofx.types.PositionTypeEnum;
import com.lti.ofx.types.SeverityEnum;
import com.lti.ofx.types.SubAccountEnum;
import com.lti.service.CUSIPManager;
import com.lti.service.CompanyIndexManager;
import com.lti.service.bo.CUSIP;
import com.lti.service.bo.CompanyIndex;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.util.StringUtil;

/**
 * @author Michael Chua
 * 
 */
public class EdgarUtil {

	public final static String Edgar_Server = "ftp://ftp.sec.gov/";

	/**
	 * Download file
	 * 
	 * @param link
	 *            the location of downloading resource
	 * @param filePath
	 *            the local location to store resource downloaded
	 * @throws Exception
	 */
	public static void downloadFile(String link, String filePath) throws Exception {
		URL url = null;
		url = new URL(link);
		FilterInputStream in = (FilterInputStream) url.openStream();
		File fileOut = new File(filePath);
		filePath = fileOut.getAbsolutePath();
		if (!fileOut.exists()) {
			filePath = filePath.replaceAll("(\\\\|/)+", "/");
			File dir = new File(filePath.substring(0, filePath.lastIndexOf("/")));
			if (!dir.exists()) {
				dir.mkdirs();
			}
			fileOut.createNewFile();
		}
		FileOutputStream out = new FileOutputStream(fileOut);
		byte[] bytes = new byte[1024];
		int c;
		while ((c = in.read(bytes)) != -1) {
			out.write(bytes, 0, c);
		}
		in.close();
		out.close();
	}

	/**
	 * uncompress gzip file
	 * 
	 * @param inFilename
	 *            gzip file
	 * @param outFilename
	 *            outpu file
	 * @throws Exception
	 */
	public static void unGZip(String inFilename, String outFilename) throws Exception {
		GZIPInputStream in = new GZIPInputStream(new FileInputStream(inFilename));
		OutputStream out = new FileOutputStream(outFilename);
		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		in.close();
		out.close();
	}

	/**
	 * delete file
	 * 
	 * @param name
	 *            the file to be deleted
	 */
	public static void deleteFile(String name) {
		try {
			File f = new File(name);
			f.delete();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}

	public final static String CUSIP_Search_Server = "http://activequote.fidelity.com/mmnet/SymLookup.phtml?QUOTE_TYPE=&searchBy=C&submit=Search";

	/**
	 * inquire symbol by CUSIP number through the fidelity web-site
	 * 
	 * @param cusip
	 * @return
	 * @throws Exception
	 */
	public static String getSymbolByFidelity(String cusip) throws Exception {
		String symbol = null;
		String[] types = new String[] { "E", "M", "I", "A" };
		for (int i = 0; i < types.length; i++) {
			String url = CUSIP_Search_Server + "&scCode=" + types[i] + "&searchFor=" + cusip;
			String tmp = Configuration.getTempDir() + "/" + cusip + "_" + types[i];
			downloadFile(url, tmp);
			Pattern p = Pattern.compile("http://activequote.fidelity.com/webxpress/get_quote\\?QUOTE_TYPE=&SID_VALUE_ID=([^\"]+)");
			FileReader fr = new FileReader(tmp);
			BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();
			boolean found = false;
			while (line != null) {
				Matcher m = p.matcher(line);
				if (m.find()) {
					symbol = m.group(1);
					fr.close();
					br.close();
					found = true;
					break;
				}
				line = br.readLine();
			}
			if (found)
				break;
			br.close();
			fr.close();

		}
		for (int i = 0; i < types.length; i++) {
			String tmp = Configuration.getTempDir() + "/" + cusip + "_" + types[i];
			deleteFile(tmp);
		}
		return symbol;
	}

	/**
	 * extract the symbol from the name of issuer<br>
	 * sometimes the symbol cannot be extracted from the name of issuer because
	 * it has been chopped.
	 * 
	 * @param nameOfIssuer
	 * @return
	 */
	public static String getSymbolByNameOfIssuer(String nameOfIssuer) {
		String symbol = null;
		Pattern p = Pattern.compile(".+\\((.+)\\).*");
		Matcher m = p.matcher(nameOfIssuer);
		if (m.find()) {
			symbol = m.group(1);
		}
		return symbol;
	}

	
	public static String getCheckCode(String cusip8){
		String cusip9=cusip8;
		
		char[] chars=cusip8.toUpperCase().toCharArray();
		int sum=0;
		for(int i=0;i<chars.length;i++){
			char c=chars[i];
			int v=0;
			if(c>='0'&&c<='9'){
				v=c-'0';
			}else if(c>='A'&&c<='Z'){
				v=c-'A'+10;
			}else if (c == '*'){
				v = 36;
			}else if(c == '@'){
		         v = 37;
			} else if( c == '#'){
		         v = 38;
			}
			
			if(i%2==1){
				v=v*2;
			}
			sum+=v/10+v%10;
		}
		int checkcode=(10 - (sum % 10)) % 10;
		cusip9+=checkcode;
		return cusip9;

	}
	
	
	/**
	 * inquire symbol by fidelity or by the name of issuer and store the result
	 * to database
	 * 
	 * @param cusip
	 * @param nameOfIssuer
	 * @return
	 * @throws Exception
	 */
	public static CUSIP getCUSIP(String cusip, String nameOfIssuer) throws Exception {
		CUSIPManager cm = (CUSIPManager) ContextHolder.getInstance().getApplicationContext().getBean("cusipManager");
		CUSIP c = cm.getByCUSIP(cusip);
		if (c != null) {
			return c;
		}

		String symbol = null;
		if (symbol == null) {
			symbol = getSymbolByFidelity(cusip);
		}
		if (symbol == null) {
			symbol = getSymbolByNameOfIssuer(cusip);
		}

		if (symbol == null) {
			symbol = "VF_" + cusip;
		}
		c = new CUSIP();
		c.setCUSIP(cusip);
		c.setSymbol(symbol);
		c.setType("E");
		cm.save(c);

		return c;
	}

	public static SimpleDateFormat EdgarDateFormat = new SimpleDateFormat("yyyyMMdd");

	/**
	 * convert all 13f quarter reports of the same company to one single file
	 * 
	 * @param name
	 *            the company name of the 13f report
	 * @param cis
	 *            the list of quarter reports
	 * @param outputName
	 *            the output file name
	 * @throws Exception
	 */
	public static void convertToFile(String name, List<CompanyIndex> cis, String outputName) throws Exception {
		BufferedWriter bw = new BufferedWriter(new FileWriter(outputName));
		bw.write("=====================================\n");
		bw.write("Basic information\n");
		bw.write("=====================================\n");
		bw.write("Name=" + name);
		bw.write("\nBenchmark=\n");
		bw.write("Symbol=\n");
		bw.write("Rebalancing Strategy=\n");
		bw.write("Rebalancing Strategy Parameters=\n");
		bw.write("Cash Flow Strategy=\n");
		bw.write("Cash Flow Strategy Parameters=\n");
		bw.write("Asset Allocation Strategy=\n");
		bw.write("Asset Allocation Strategy Parameters=\n");
		bw.write("=====================================\n");
		bw.write("Holding information\n");
		bw.write("=====================================\n");
		for (int i = 0; i < cis.size(); i++) {
			try {
				bw.write("#####################################\n");
				downloadFile(Edgar_Server + cis.get(i).getFileName(), name + "_" + i);
				BufferedReader br = new BufferedReader(new FileReader(name + "_" + i));
				String line = br.readLine();
				boolean inTable = false;
				double totalAmount = 0.0;
				StringBuffer sb = new StringBuffer();
				while (line != null) {
					if (line.startsWith("CONFORMED PERIOD OF REPORT:")) {
						bw.write("Conformed Period Of Report=" + EdgarDateFormat.format(EdgarDateFormat.parse(line.substring(27).trim())) + "\n");
					}
					if (line.startsWith("FILED AS OF DATE:")) {
						bw.write("Filed As Of Date=" + EdgarDateFormat.format(EdgarDateFormat.parse(line.substring(17).trim())) + "\n");
					}
					if (line.startsWith("</TABLE>")) {
						inTable = false;
					}
					if (inTable) {
						try {
							String cusip = line.substring(48, 60).trim();
							double value = Double.parseDouble(line.substring(61, 68).trim()) * 1000;
							totalAmount += value;
							double shares = Double.parseDouble(line.substring(69, 75).trim());
							String nameOfIssuer = line.substring(0, 30).trim();
							CUSIP c = getCUSIP(cusip, nameOfIssuer);
							String symbol = c.getSymbol();
							sb.append("  " + symbol + "," + value + "," + shares + "\n");
						} catch (Exception e) {
							sb.append("//parse error:" + line + "\n");
						}

					}
					if (line.startsWith("<TABLE>")) {
						inTable = true;
					}
					line = br.readLine();
				}
				bw.write("Total Amount=" + totalAmount + "\n");
				bw.write(sb.toString());
			} catch (Exception e) {
				bw.write("//Exception:" + e.getMessage() + "\n");
			}
		}

		bw.write("=====================================\n");
		bw.write("Transaction information\n");
		bw.write("=====================================\n");
		bw.flush();
		bw.close();

	}
	


	
//	private static String[] parseLine1(String line){
//		String[] strs = new String[5];
//		Pattern p = Pattern.compile("(.+)\\s{2,30}([A-Z]+)\\s+([\\w]{8,9}|[\\w-]{11})\\s+(\\$?[\\d\\.\\,]+)\\s+([\\d\\.\\,]+)[^$]",Pattern.CASE_INSENSITIVE);
//		Matcher m = p.matcher(line);
//		
//		if (m.find()) {
//			strs[0] = m.group(1).trim();
//			
//			
//			strs[1] = m.group(2).trim();
//			
//			// cusip
//			strs[2] = m.group(3).replace("-", "");
//			if(strs[2].length()==8){
//				strs[2]=getCheckCode(strs[2]);
//			}
//			// amount
//			strs[3] = String.valueOf(Double.parseDouble(m.group(4).replace(",", "").replace("$", "")) * 1000);
//			// shares
//			strs[4] = m.group(5).replace(",", "");
//			
//		}
//
//		Double.parseDouble(strs[3]);
//		Double.parseDouble(strs[4]);
//
//		return strs;
//	}
	private static Pattern p = Pattern.compile("(.+)\\s+([\\w]{9}|([\\w]{6}[ -][\\w]{2}[ -]\\w)|[\\w]{8})[\\s\\\"\\$]+(\\$?[\\d\\.\\,]+)[\\s\\\"\\$]+([\\d\\.\\,]+)[^$]");
	private static Pattern p2 = Pattern.compile("(.+)\\s{2,30}(.+)");
	private static String[] parseLine2(String line) {
		String[] strs = new String[5];
		Matcher m = p.matcher(line);
		
		if (m.find()) {
			strs[0] = m.group(1).trim();
			
			
			
			Matcher m2 = p2.matcher(strs[0]);
			if(m2.find()){
				strs[0]=m2.group(1);
				strs[1]=m2.group(2);
			}else{
				try {
					strs[1] =strs[0].substring(strs[0].lastIndexOf(' ')).trim();
				} catch (Exception e) {
					strs[1]="no title of class";
				}
			}
			
			// cusip
			strs[2] = m.group(2).replace("-", "").replace(" ", "");
			if(strs[2].length()==8){
				strs[2]=getCheckCode(strs[2]);
			}
			// amount
			strs[3] = String.valueOf(Double.parseDouble(m.group(4).replace(",", "").replace("$", "")) * 1000);
			// shares
			strs[4] = m.group(5).replace(",", "");
			
			
		}

		Double.parseDouble(strs[3]);
		Double.parseDouble(strs[4]);

		return strs;
	}

	public final static String TYPE_COM="COM";
	public final static String TYPE_COMMON="COMMON";
	public final static String TYPE_PFD="PFD";
	public final static String TYPE_CONV="CONV";
	public final static String TYPE_INC="INC";
	public final static String TYPE_MF="MF";
	public final static String TYPE_CV_PFD="CV PFD";
	
	public final static String[] TYPES={TYPE_COM,TYPE_PFD,TYPE_CONV,TYPE_INC,TYPE_MF,TYPE_COMMON,TYPE_CV_PFD};
	public static boolean validType(String type){
		for(int i=0;i<TYPES.length;i++){
			if(TYPES[i].equalsIgnoreCase(type))return true;
		}
		return false;
	}
	/**
	 * convert a 13f report to OFX format file
	 * 
	 * @param name
	 *            the name of the company
	 * @param ci
	 *            the 13f index information of the company
	 * @return
	 * @throws Exception
	 */
	public static String convertToOFX(String name, CompanyIndex ci) throws Exception {

		// ofx
		OFXResponseType ofx = new OFXResponseType();

		// ofx -> SIGNONMSGSRSV1
		// SignonResponseMessageSetV1 signonResponseMessageSetV1=new
		// SignonResponseMessageSetV1();
		// ofx.setSIGNONMSGSRSV1(signonResponseMessageSetV1);

		// SignonResponse SONRS=new SignonResponse();
		// signonResponseMessageSetV1.setSONRS(SONRS);

		// ofx -> INVSTMTMSGSRSV1
		InvestmentStatementResponseMessageSetV1 INVSTMTMSGSRSV1 = new InvestmentStatementResponseMessageSetV1();
		ofx.setINVSTMTMSGSRSV1(INVSTMTMSGSRSV1);

		// ofx -> INVSTMTMSGSRSV1 ->
		// vInvestmentStatementResponseMessageSetV1ItemArray ?
		InvestmentStatementResponseMessageSetV1Item[] vInvestmentStatementResponseMessageSetV1ItemArray = new InvestmentStatementResponseMessageSetV1Item[1];
		InvestmentStatementResponseMessageSetV1Item isrmv1t = new InvestmentStatementResponseMessageSetV1Item();
		vInvestmentStatementResponseMessageSetV1ItemArray[0] = isrmv1t;
		INVSTMTMSGSRSV1.setInvestmentStatementResponseMessageSetV1Item(vInvestmentStatementResponseMessageSetV1ItemArray);

		// ofx -> INVSTMTMSGSRSV1 -> INVSTMTTRNRS
		InvestmentStatementTransactionResponse INVSTMTTRNRS = new InvestmentStatementTransactionResponse();
		isrmv1t.setINVSTMTTRNRS(INVSTMTTRNRS);

		// ofx -> INVSTMTMSGSRSV1 -> INVSTMTTRNRS -> TRNUID
		// INVSTMTTRNRS.setTRNUID("1001");

		// ofx -> INVSTMTMSGSRSV1 -> INVSTMTTRNRS -> STATUS
		Status STATUS = new Status();
		STATUS.setCODE("0");
		
		STATUS.setSEVERITY(SeverityEnum.INFO);
		INVSTMTTRNRS.setSTATUS(STATUS);

		// ofx -> INVSTMTMSGSRSV1 -> INVSTMTTRNRS -> INVSTMTRS
		InvestmentStatementResponse INVSTMTRS = new InvestmentStatementResponse();
		INVSTMTTRNRS.setINVSTMTRS(INVSTMTRS);

		// ofx -> INVSTMTMSGSRSV1 -> INVSTMTTRNRS -> INVSTMTRS -> DTASOF
		// INVSTMTRS.setDTASOF(""+(new Date()).getTime());
		// ofx -> INVSTMTMSGSRSV1 -> INVSTMTTRNRS -> INVSTMTRS -> CURDEF
		INVSTMTRS.setCURDEF(CurrencyEnum.USD);

		// ofx -> INVSTMTMSGSRSV1 -> INVSTMTTRNRS -> INVSTMTRS -> INVACCTFROM
		InvestmentAccount INVACCTFROM = new InvestmentAccount();
		INVSTMTRS.setINVACCTFROM(INVACCTFROM);
		INVACCTFROM.setACCTID("10000001");
		INVACCTFROM.setBROKERID("10000001");

		// ofx -> INVSTMTMSGSRSV1 -> INVSTMTTRNRS -> INVSTMTRS -> INVPOSLIST
		InvestmentPositionList INVPOSLIST = new InvestmentPositionList();
		INVSTMTRS.setINVPOSLIST(INVPOSLIST);

		InvestmentPositionListChoice investmentPositionListChoice = new InvestmentPositionListChoice();
		INVPOSLIST.setInvestmentPositionListChoice(investmentPositionListChoice);

		List<InvestmentPositionListChoiceItem> iplist = new ArrayList<InvestmentPositionListChoiceItem>();

		String conformedDate = null;
		String posDate = null;

		File in = new File(Configuration.get13FDir() + "/" + ci.getFileName());
		if (!in.exists()) {
			downloadFile(Edgar_Server + ci.getFileName(), Configuration.get13FDir() + "/" + ci.getFileName());
		}
		BufferedReader br = new BufferedReader(new FileReader(in));
		String line = br.readLine();
		boolean inTable = false;
		double totalAmount = 0.0;

		while (line != null) {
			if (line.toUpperCase().startsWith("CONFORMED PERIOD OF REPORT:")) {
				conformedDate = EdgarDateFormat.format(EdgarDateFormat.parse(line.substring(27).trim()));
				INVSTMTRS.setDTASOF(conformedDate);
				// bw.write("Conformed Period Of Report=" +
				// sdf2.format(sdf1.parse(line.substring(27).trim()))+"\n");
			}
			if (line.toUpperCase().startsWith("FILED AS OF DATE:")) {
				posDate = EdgarDateFormat.format(EdgarDateFormat.parse(line.substring(17).trim()));
				INVSTMTTRNRS.setTRNUID(posDate);
				// bw.write("Filed As Of Date=" +
				// sdf2.format(sdf1.parse(line.substring(17).trim()))+"\n");
			}

			if (line.toUpperCase().startsWith("<TABLE>")) {
				inTable = true;
				break;
			}
			line = br.readLine();
		}

		line = br.readLine();

		while (line != null) {
			if (line.toUpperCase().startsWith("</TABLE>")) {
				inTable = false;
			}
			if (line.toUpperCase().startsWith("<TABLE>")) {
				inTable = true;
			}
			if (inTable) {
				try {
					String[] strs = parseLine2(line);
					String nameOfIssuer = strs[0];
					String type= strs[1];
					String cusip = strs[2];
					double value = Double.parseDouble(strs[3]);
					totalAmount += value;
					double shares = Double.parseDouble(strs[4]);

					CUSIP c = getCUSIP(cusip, nameOfIssuer);
					InvestmentPositionListChoiceItem choiceItem = new InvestmentPositionListChoiceItem();
					InvestmentPosition INVPOS = null;
					if (type.equals("COM")) {

						PositionStock POSSTOCK = new PositionStock();
						choiceItem.setPOSSTOCK(POSSTOCK);

						INVPOS = new InvestmentPosition();
						POSSTOCK.setINVPOS(INVPOS);

					} else if (type.equals("MF")) {
						PositionMutualFund pmf = new PositionMutualFund();
						choiceItem.setPOSMF(pmf);

						INVPOS = new InvestmentPosition();
						pmf.setINVPOS(INVPOS);

					} else {
						PositionOther pmf = new PositionOther();
						choiceItem.setPOSOTHER(pmf);

						INVPOS = new InvestmentPosition();
						pmf.setINVPOS(INVPOS);

					}
					SecurityId sid = new SecurityId();
					INVPOS.setSECID(sid);
					sid.setUNIQUEID(cusip);
					sid.setUNIQUEIDTYPE("CUSIP");

					INVPOS.setHELDINACCT(SubAccountEnum.CASH);
					INVPOS.setPOSTYPE(PositionTypeEnum.LONG);
					INVPOS.setUNITS(String.valueOf((int) shares));
					INVPOS.setUNITPRICE(String.valueOf(value / shares));
					INVPOS.setMKTVAL(String.valueOf(value));
					INVPOS.setDTPRICEASOF(conformedDate);
					StringBuffer memo=new StringBuffer();
					memo.append("Symbol");
					memo.append(":");
					memo.append(c.getSymbol());
					memo.append(",");
					memo.append("Title of class");
					memo.append(":");
					memo.append(type);
					memo.append(",");
					memo.append("Name of issuer");
					memo.append(":");
					memo.append(nameOfIssuer);
					INVPOS.setMEMO(memo.toString());
					iplist.add(choiceItem);
				} catch (Exception e) {
					System.out.println("error("+e.getMessage()+"):"+line);
					//System.out.println(StringUtil.getStackTraceString(e));
				}

			}
			line = br.readLine();
		}

		InvestmentPositionListChoiceItem[] vInvestmentPositionListChoiceItemArray = iplist.toArray(new InvestmentPositionListChoiceItem[0]);
		investmentPositionListChoice.setInvestmentPositionListChoiceItem(vInvestmentPositionListChoiceItemArray);

		STATUS.setMESSAGE("This file generate by validfi.com. -- total amount: "+totalAmount+" -- source: " + ci.getFileName());
		
		String fileName = Configuration.getTempDir() + "/" + StringUtil.getValidName(name) + "/" + StringUtil.getValidName(name) + "." + conformedDate + "." + posDate + ".xml";
		ofx.marshal(new FileWriter(fileName));
		return fileName;

	}

	/**
	 * convert a 13f report to OFX format file
	 * 
	 * @param name
	 *            the name of the company
	 * @param ci
	 *            the 13f index information of the company
	 * @return
	 * @throws Exception
	 */
	public static String getConfirmedDate(CompanyIndex ci) throws Exception {

		String conformedDate = null;

		File in = new File(Configuration.get13FDir() + "/" + ci.getFileName());
		if (!in.exists()) {
			downloadFile(Edgar_Server + ci.getFileName(), Configuration.get13FDir() + "/" + ci.getFileName());
		}
		BufferedReader br = new BufferedReader(new FileReader(in));
		String line = br.readLine();

		while (line != null) {
			if (line.startsWith("CONFORMED PERIOD OF REPORT:")) {
				conformedDate = EdgarDateFormat.format(EdgarDateFormat.parse(line.substring(27).trim()));
				break;
			}
			// if (line.startsWith("FILED AS OF DATE:")) {
			// posDate =
			// EdgarDateFormat.format(EdgarDateFormat.parse(line.substring(17).trim()));
			// }

			line = br.readLine();
		}
		br.close();

		return conformedDate;

	}

	public final static String Latest_Company_Idx = "ftp://ftp.sec.gov/edgar/full-index/company.gz";
	public final static String Latest_Company_Idx_Local = "company.idx";

	/**
	 * update 13f index information of database,<br>
	 * and update all zipped VFOFX files.
	 * 
	 * @throws Exception
	 */
	public static void updateCompanyIndexDateBase() throws Exception {
		EdgarUtil.downloadFile(Latest_Company_Idx, Configuration.getTempDir() + "/" + "company.gz");
		EdgarUtil.unGZip(Configuration.getTempDir() + "/" + "company.gz", Configuration.getTempDir() + "/" + Latest_Company_Idx_Local);
		// new File(Configuration.getTempDir()+"/"+"company.gz").delete();
		CompanyIndexManager cim = (CompanyIndexManager) ContextHolder.getInstance().getApplicationContext().getBean("companyIndexManager");
		cim.importCompanyIndex(Configuration.getTempDir() + "/" + Latest_Company_Idx_Local);
	}

	public static boolean containQuarter(List<String> names, int year, int quarter) {
		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.YEAR, year);
		ca.set(Calendar.MONTH, quarter * 3 - 1);
		ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
		String s = EdgarDateFormat.format(ca.getTime());

		for (int i = 0; i < names.size(); i++) {
			String[] array = names.get(i).split("\\.");
			if (array.length >= 1) {
				for (int j = 0; j < array.length; j++) {
					if (s.equals(array[j])) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static boolean containConfirmedDate(List<String> names, String confirmedDate) {
		for (int i = 0; i < names.size(); i++) {
			String[] array = names.get(i).split("\\.");
			if (array.length >= 1) {
				for (int j = 0; j < array.length; j++) {
					if (confirmedDate.equals(array[j])) {
						return true;
					}
				}
			}
		}
		return false;
	}
	public static String getVfofxName(String name){
		return StringUtil.getValidName(name) + ".vfofx";
	}

	/**
	 * update the VFOFX file (zipped)
	 * 
	 * @param name
	 *            the name of the company
	 * @param cis
	 *            company index
	 * @throws Exception
	 */
	public static void convertToOFX(String name, List<CompanyIndex> cis,boolean flush) throws Exception {
		String vfofxName = Configuration.getVFOFXDir() + "/" + getVfofxName(name);
		File tempDir = new File(Configuration.getTempDir() + "/" + StringUtil.getValidName(name));
		if (!tempDir.exists()) {
			tempDir.mkdirs();
		}

		File vfofx = new File(vfofxName);
		List<String> files = new ArrayList<String>();
		if(!flush){
			if (vfofx.exists()) {
				try {
					ZipFile zipFile = new ZipFile(vfofx);
					Enumeration emu = zipFile.entries();
					// System.out.println(wantDate);
					while (emu.hasMoreElements()) {
						ZipEntry entry = (ZipEntry) emu.nextElement();
						BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry));
						File file = new File(tempDir, entry.getName());
						FileOutputStream fos = new FileOutputStream(file);
						BufferedOutputStream bos = new BufferedOutputStream(fos, 2048);

						int count;
						byte data[] = new byte[2048];
						while ((count = bis.read(data, 0, 2048)) != -1) {
							bos.write(data, 0, count);
						}
						bos.flush();
						bos.close();
						bis.close();
						files.add(file.getAbsolutePath());

					}
					zipFile.close();
				} catch (Exception e) {
					System.out.println(StringUtil.getStackTraceString(e));
				}
			} else {
				File dir = new File(Configuration.getVFOFXDir());
				if (!dir.exists()) {
					dir.mkdirs();
				}
				vfofx.createNewFile();
			}
		}
		

		for (int i = 0; i < cis.size(); i++) {
			CompanyIndex ci = cis.get(i);

			if (!containConfirmedDate(files, getConfirmedDate(ci))) {
				files.add(convertToOFX(name, ci));
			}

		}

		BufferedInputStream origin = null;
		FileOutputStream dest = new FileOutputStream(vfofx);
		ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
		byte data[] = new byte[2048];
		for (int i = 0; i < files.size(); i++) {
			File item = new File(files.get(i));
			if (!item.exists())
				continue;
			FileInputStream fi = new FileInputStream(files.get(i));
			origin = new BufferedInputStream(fi, 2048);
			ZipEntry entry = new ZipEntry(item.getName());
			out.putNextEntry(entry);
			int count;
			while ((count = origin.read(data, 0, 2048)) != -1) {
				out.write(data, 0, count);
			}
			item.delete();
			origin.close();
		}
		out.close();
	}

	public static void write(String name, int i) throws Exception {
		File out = new File(name);
		if (!out.exists()) {
			out.createNewFile();
		}
		FileWriter fw = new FileWriter(out);
		fw.write(String.valueOf(i));
		fw.flush();
		fw.close();
	}

	public static int read(String name, int i) throws Exception {
		File in = new File(name);
		FileReader fr = new FileReader(in);
		char[] chars = new char[1];
		fr.read(chars);
		return Integer.parseInt(String.valueOf(chars[0]));
	}

	public static void convertToOFX(String[] array) {
		CompanyIndexManager cim = (CompanyIndexManager) ContextHolder.getInstance().getApplicationContext().getBean("companyIndexManager");
		for (int i = 0; i < array.length; i++) {
			String name = array[i];
			List<CompanyIndex> cis = cim.get(name, CompanyIndex.Type_13F_HR);
			long t = System.currentTimeMillis();
			try {
				convertToOFX(name, cis,false);
				System.out.println("Convert " + name + " to OFX: " + (System.currentTimeMillis() - t));
			} catch (Exception e) {
				System.out.println("Convert " + name + " to OFX (error message): " + (System.currentTimeMillis() - t));
				System.out.println(StringUtil.getStackTraceString(e));
			}
		}
	}

	public static void main2(String[] args) throws Exception {
		String array[] = { 
				"YALE UNIVERSITY",
				"WITMER ASSET MANAGEMENT",
				"WELLINGTON MANAGEMENT CO LLP",
				"WADDELL & REED FINANCIAL INC",
				"WRA Investments LLC",
				"VINIK ASSET MANAGEMENT L P",
				"VIKING GLOBAL INVESTORS LP",
				"ValueAct Holdings, L.P.",
				"VALINOR MANAGEMENT, LLC",
				"UNIVERSITY OF TEXAS INVESTMENT MANAGMENT CO",
				"US BANCORP \\DE\\",
				"UBS GLOBAL ASSET MANAGEMENT AMERICAS INC",
				"TWO SIGMA INVESTMENTS LLC",
				"TURNER INVESTMENT PARTNERS INC",
				"Tremblant Capital Group",
				"TRAFELET CAPITAL MANAGEMENT, L.P.",
				"Tracer Capital Management L.P.",
				"TOURADJI CAPITAL MANAGEMENT, LP",
				"TIGER VEDA MANAGEMENT LLC",
				"TIGER GLOBAL MANAGEMENT LLC",
				"TIGER CONSUMER MANAGEMENT, LLC",
				"THRIVENT FINANCIAL FOR LUTHERANS",
				"THORNBURG INVESTMENT MANAGEMENT INC",
				"Third Point LLC",
				"THIRD POINT MANAGEMENT CO LLC",
				"THIRD AVENUE MANAGEMENT LLC",
				"THALES FUND MANAGEMENT LLC",
				"Taconic Capital Advisors LP",
				"TACONIC CAPITAL ADVISORS LLC",
				"TPG-Axon Capital Management, LP",
				"TCS CAPITAL MANAGEMENT LLC",
				"SYMPHONY ASSET MANAGEMENT INC",
				"SYMPHONY ASSET MANAGEMENT LLC",
				"SUNTRUST BANKS INC",
				"STEEL PARTNERS II LP",
				"Stark Global Opportunities Management LLC",
				"SOUTHEASTERN ASSET MANAGEMENT INC/TN/",
				"SOROS FUND MANAGEMENT LLC",
				"SIRIOS CAPITAL MANAGEMENT L P",
				"Silver Point Capital L.P.",
				"SILVER POINT CAPITAL FUND LP",
				"SHUMWAY CAPITAL PARTNERS LLC",
				"Sheffield Asset Management",
				"SHAMROCK CAPITAL ADVISORS INC",
				"Seminole Management Co., Inc.",
				"SELECT EQUITY GROUP INC",
				"SELECT EQUITY GROUP INC /NY/ /ADV",
				"SECOND CURVE CAPITAL LLC",
				"SEASONS CAPITAL MANAGEMENT LLC",
				"SCOUT CAPITAL MANAGEMENT LLC",
				"SATELLITE ASSET MANAGEMENT LP",
				"SANDS CAPITAL MANAGEMENT, LLC",
				"SANDS CAPITAL MANAGEMENT, LP",
				"SAC Capital Advisors LP",
				"SAB CAPITAL MANAGEMENT LP",
				"SAB CAPITAL MANAGEMENT LLC",
				"S&E PARTNERS LP",
				"RUANE, CUNNIFF & GOLDFARB INC",
				"ROYCE & ASSOCIATES LLC",
				"ROYCE & ASSOCIATES INC /NY",
				"ROYCE & ASSOCIATES INC /ADV",
				"Robeco Investment Management, Inc.",
				"River Road Asset Management, LLC",
				"RENAISSANCE TECHNOLOGIES LLC",
				"RENAISSANCE TECHNOLOGIES CORP",
				"RELATIONAL INVESTORS LLC",
				"RS INVESTMENT MANAGEMENT CO LLC",
				"RS INVESTMENT MANAGEMENT L P/",
				"RS INVESTMENT MANAGEMENT INC /CA",
				"RBS PARTNERS L P /CT",
				"QVT Financial LP",
				"PZENA INVESTMENT MANAGEMENT LLC",
				"PZENA INVESTMENT MANAGEMENT LLC /ADV",
				"PUTNAM INVESTMENT MANAGEMENT LLC",
				"PROSPECTOR PARTNERS FUND LP",
				"PROSPECTOR PARTNERS LLC",
				"PRIVATE CAPITAL MANAGEMENT INC",
				"PRIVATE CAPITAL MANAGEMENT",
				"PRIVATE CAPITAL MANAGEMENT INC /FL",
				"PIONEER INVESTMENT MANAGEMENT INC",
				"PIONEER INVESTMENT MANAGEMENT INC /ADV",
				"Pershing Square Capital Management, L.P.",
				"PEQUOT CAPITAL MANAGEMENT INC",
				"PEQUOT CAPITAL MANAGEMENT INC/CT/",
				"PENNANT CAPITAL MANAGEMENT LLC",
				"PENNANT CAPITAL MANAGEMENT INC",
				"PAULSON & CO INC",
				"PARAMETRIC PORTFOLIO ASSOCIATES",
				"PARADIGM CAPITAL MANAGEMENT INC/NY",
				"PAR CAPITAL MANAGEMENT INC",
				"PAMET CAPITAL MANAGEMENT, LP",
				"PAMET CAPITAL MANAGEMENT LLC",
				"PABRAI MOHNISH",
				"OPPENHEIMER FUNDS INC",
				"OKUMUS CAPITAL LLC",
				"Noonday Asset Management, L.P.",
				"NEWCASTLE PARTNERS L P",
				"NEUBERGER BERMAN LLC",
				"Nationwide Fund Advisors",
				"NWQ INVESTMENT MANAGEMENT CO LLC",
				"NWQ INVESTMENT MANAGEMENT CO /CA/",
				"NWQ INVESTMENT MANAGEMENT CO INC /MA",
				"MOORE CAPITAL MANAGEMENT, LP",
				"MOORE CAPITAL MANAGEMENT LLC",
				"MOORE CAPITAL MANAGEMENT INC /NEW",
				"MONTAG & CALDWELL INC",
				"MONTAG & CALDWELL INC /GA/ /ADV",
				"Miura Global Management, LLC",
				"MILLENNIUM MANAGEMENT, L.L.C.",
				"MILLENNIUM MANAGEMENT LLC",
				"MAVERICK CAPITAL LTD",
				"MAVERICK CAPITAL LTD /ADV",
				"MASSACHUSETTS INSTITUTE OF TECHNOLOGY",
				"MASSACHUSETTS FINANCIAL SERVICES CO /MA/",
				"MASON STREET ADVISORS LLC",
				"MARSICO CAPITAL MANAGEMENT LLC",
				"MARINER INVESTMENT GROUP LLC",
				"MARINER INVESTMENT GROUP INC",
				"MARATHON ASSET MANAGEMENT LLP",
				"MARATHON ASSET MANAGEMENT LLC",
				"Magnetar Financial LLC",
				"LOTSOFF CAPITAL MANAGEMENT",
				"LORD ABBETT & CO LLC",
				"LORD ABBETT & CO",
				"LOOMIS SAYLES & CO L P",
				"LOOMIS SAYLES & CO LP /MA/ /ADV",
				"LONGVIEW ASSET MANAGEMENT, LLC",
				"LONE PINE CAPITAL LLC",
				"LEVEL GLOBAL INVESTORS LP",
				"LEGG MASON TRUST COMPANY, NATIONAL ASSOCIATION",
				"Legg Mason Investment Counsel, LLC",
				"Legg Mason International Equities",
				"LEGG MASON CAPITAL MANAGEMENT INC",
				"Legg Mason Asset Management (Japan) Co., Ltd.",
				"Legg Mason Investment Counsel, LLC",
				"Legg Mason Private Portfolio Group, LLC",
				"Legg Mason Asset Management (Japan) Co., Ltd.",
				"Legg Mason International Equities",
				"LEGG MASON TRUST COMPANY, NATIONAL ASSOCIATION",
				"LEGG MASON CAPITAL MANAGEMENT INC",
				"LEGG MASON INC",
				"LSV ASSET MANAGEMENT",
				"KLEINHEINZ CAPITAL PARTNERS INC",
				"KINGDON CAPITAL MANAGEMENT LLC",
				"KING STREET CAPITAL MANAGEMENT, L.P.",
				"KING STREET CAPITAL MGMT LLC",
				"KENSICO CAPITAL MANAGEMENT CORP",
				"KAYNE ANDERSON CAPITAL ADVISORS LP",
				"KARSCH CAPITAL MANAGEMENT LP",
				"JENNISON ASSOCIATES LLC",
				"JANUS CAPITAL MANAGEMENT LLC",
				"JANA PARTNERS LLC",
				"JACOBS LEVY EQUITY MANAGEMENT INC",
				"IVORY INVESTMENT MANAGEMENT. L.P.",
				"IVORY INVESTMENT MANAGEMENT LLC",
				"Invesco Private Capital, Inc.",
				"Invesco Ltd.",
				"Invesco Ltd",
				"INVESCO PLC/LONDON/",
				"INVESCO GLOBAL ASSET MANAGEMENT NA INC",
				"INVESCO INSTITUTIONAL NA INC",
				"INVESCO ASSET MANAGEMENT LTD",
				"INVESCO INC",
				"INVESCO CAPITAL MANAGEMENT INC",
				"INVESCO FUNDS GROUP INC",
				"INVESCO /NY/ ASSET MANAGEMENT INC",
				"INVESCO MANAGEMENT & RESEARCH INC",
				"ING INVESTMENT MANAGEMENT, INC.",
				"ING Investment Management (Europe) B.V.",
				"ING INVESTMENT MANAGEMENT CO",
				"ING INVESTMENT MANAGEMENT ADVISORS B V //",
				"ING INVESTMENT MANAGEMENT LLC",
				"ING INVESTMENT MANAGEMENT ADVISORS B V",
				"ING CLARION REAL ESTATE SECURITIES/N L.P.",
				"HUNTER GLOBAL INVESTORS L P",
				"Hound Partners, LLC",
				"HOTCHKIS & WILEY CAPITAL MANAGEMENT LLC",
				"HOPLITE CAPITAL MANAGEMENT LLC",
				"HIGHSIDE CAPITAL MANAGEMENT L P",
				"HIGHFIELDS CAPITAL MANAGEMENT LP",
				"HIGHBRIDGE CAPITAL MANAGEMENT LLC",
				"HARRIS FINANCIAL CORP",
				"HARRIS ASSOCIATES L P",
				"HBK INVESTMENTS L P",
				"GREENLIGHT CAPITAL INC",
				"GOLDENTREE ASSET MANAGEMENT LP",
				"GLENVIEW CAPITAL MANAGEMENT LLC",
				"GLENHILL ADVISORS LLC",
				"GILDER GAGNON HOWE & CO LLC",
				"GAMCO INVESTORS, INC. ET AL",
				"GALLEON MANAGEMENT L P",
				"GE ASSET MANAGEMENT",
				"FRIESS ASSOCIATES LLC",
				"FRIESS ASSOCIATES INC",
				"FRANKLIN RESOURCES INC",
				"Fox Point Capital Management LLC",
				"FORTRESS INVESTMENT GROUP LLC",
				"Fisher Asset Management, LLC",
				"Fine Capital Partners, L.P.",
				"FEDERATED INVESTORS INC /PA/",
				"FARALLON CAPITAL MANAGEMENT LLC",
				"FARALLON CAPITAL MANAGEMENT LLC /ADV",
				"FAIRHOLME CAPITAL MANAGEMENT LLC",
				"Eton Park Capital Management, L.P.",
				"EMINENCE CAPITAL LLC",
				"ELLIOTT MANAGEMENT CORP",
				"EDGE ASSET MANAGEMENT, INC",
				"EATON VANCE MANAGEMENT",
				"EASTBOURNE CAPITAL MANAGEMENT LLC/CA",
				"EASTBOURNE CAPITAL MANAGEMENT LLC /ADV",
				"EARNEST PARTNERS LLC",
				"EAGLE ASSET MANAGEMENT INC",
				"DUQUESNE CAPITAL MANAGEMENT L L C",
				"DREMAN VALUE MANAGEMENT L L C",
				"DODGE & COX",
				"DELAWARE MANAGEMENT BUSINESS TRUST",
				"DEFIANCE ASSET MANAGEMENT LLC",
				"DAVIS SELECTED ADVISERS",
				"DAVIDSON KEMPNER CAPITAL MANAGEMENT LLC",
				"D. E. Shaw & Co., INC.",
				"COLUMBIA WANGER ASSET MANAGEMENT LP",
				"COLUMBIA PARTNERS L L C INVESTMENT MANAGEMENT",
				"COHEN & STEERS INC",
				"COHEN & STEERS CAPITAL MANAGEMENT INC",
				"COBALT CAPITAL MANAGEMENT INC",
				"COATUE MANAGEMENT LLC",
				"CLOVIS CAPITAL MANAGEMENT LP",
				"CLOUGH CAPITAL PARTNERS L P",
				"CLARIUM CAPITAL MANAGEMENT LLC",
				"CITADEL L P",
				"CITADEL LIMITED PARTNERSHIP",
				"Chilton Capital Management Advisors, Inc.",
				"CHIEFTAIN CAPITAL MANAGEMENT INC",
				"CHESAPEAKE PARTNERS MANAGEMENT CO INC/MD",
				"CAXTON ASSOCIATES LP",
				"CAXTON ASSOCIATES LLC",
				"CARLSON CAPITAL L P",
				"Capital World Investors",
				"Capital Research Global Investors",
				"CAPITAL GUARDIAN TRUST CO",
				"CANYON CAPITAL ADVISORS LLC",
				"CANYON CAPITAL ADVISORS LLC /ADV",
				"Cantillon Capital Management LLP",
				"CANTILLON CAPITAL MANAGEMENT LLC",
				"CALAMOS PARTNERS LLC",
				"BROOKSIDE CAPITAL MANAGEMENT LLC",
				"BRIDGEWAY CAPITAL MANAGEMENT INC",
				"BRIDGEWAY CAPITAL MANAGEMENT INC /ADV",
				"Bridgewater Associates, Inc.",
				"BRIDGER MANAGEMENT LLC",
				"BRANDES INVESTMENT PARTNERS, LP",
				"BRANDES INVESTMENT PARTNERS LLC",
				"BRANDES INVESTMENT PARTNERS L P",
				"BOGLE INVESTMENT MANAGEMENT L P /DE/",
				"BLUM CAPITAL PARTNERS LP",
				"BLUE RIDGE CAPITAL HOLDINGS LLC/BLUE RIDGE CAPITAL OFFSHORE",
				"BLUE RIDGE CAPITAL MANAGEMENT LLC",
				"BLACKROCK ADVISORS LLC",
				"Black River Asset Management LLC",
				"BERKSHIRE HATHAWAY INC",
				"BENNETT LAWRENCE MANAGEMENT L L C/NY",
				"BENNETT LAWRENCE MANAGEMENT LLC /ADV",
				"BAUPOST GROUP LLC/MA",
				"BAUPOST GROUP LLC /ADV",
				"BASSWOOD CAPITAL MANAGEMENT LLC",
				"BARROW HANLEY MEWHINNEY & STRAUSS INC",
				"Bank of New York Mellon CORP",
				"BP CAPITAL MANAGEMENT L P",
				"AXIAL CAPITAL MANAGEMENT, LLC",
				"Atticus Capital LP",
				"ATTICUS CAPITAL LLC",
				"ATLANTIC INVESTMENT MANAGEMENT INC",
				"ARTISAN PARTNERS LTD PARTNERSHIP",
				"Arnhold & S. Bleichroeder Advisers, LLC",
				"ARIEL CAPITAL MANAGEMENT LLC",
				"ARIEL CAPITAL MANAGEMENT INC ET AL",
				"ARIEL CAPITAL MANAGEMENT INC",
				"ARGYLL RESEARCH, LLC",
				"APPALOOSA MANAGEMENT LP",
				"ANGELO GORDON & CO LP",
				"ANGELO GORDON & CO LP/NY",
				"AMERICAN CENTURY COMPANIES INC",
				"Amber Capital LP",
				"ALSON CAPITAL PARTNERS LLC",
				"ALLIANZ GLOBAL INVESTORS OF AMERICA L P",
				"Aletheia Research & Management, Inc.",
				"AKRE CAPITAL MANAGEMENT LLC",
				"ACADIAN ASSET MANAGEMENT LLC",
				"ACADIAN ASSET MANAGEMENT INC /MA/",
				"AXA",
				"AXA FINANCIAL INC",
				"AQR CAPITAL MANAGEMENT LLC",
				"ABP Investments US, Inc."
				};
//		CompanyIndexManager cim=ContextHolder.getCompanyIndexManager();
//		for(int i=0;i<array.length;i++){
//			String name=array[i].trim()+'%';
//			List<String> list=cim.findBySQL("SELECT distinct companyname FROM ltisystem.ltisystem_companyindex l where companyname like '"+name+"' and formtype='13f-hr' order by datefiled desc;");
//			if(list!=null&&list.size()!=0){
//				for(int j=0;j<list.size();j++){
//					System.out.println("\""+list.get(j)+"\",");
//				}
//			}else{
//			}
//			
//		}
		//convertToOFX(array);
		//System.out.println(getCheckCode("17275V9A"));
//		String line="NAVISTAR INTL CORP NEW        NOTE    2.500%12/1 63934EAG3  24438  23975000                DEFINED             23975000  0    0";
//		Pattern p = Pattern.compile("(.+)\\s+([\\d\\w]{8,9}|[\\d\\w-]{11})\\s+([\\d\\.\\,]+)\\s+([\\d\\.\\,]+)[^$]");
//		Matcher m = p.matcher(line);
//		if(m.find()){
//			System.out.println(m.group(2));
//		}
		//String line="LEAR CORP                          COM         521865105    188383    10625079    10625079       0       8661184       0     1963895";
		//parseLine(line);
		String abc="                           ADR        71646E100    10,131    91,701 SH            DEFINED         (9)     30,069   0       61,632";
		parseLine2(abc);
		
		File root=new File("C:\\validfi\\13f\\edgar\\data");
		File[] files=root.listFiles();
		int i_s=149;
		int i_e=300;//files.length;
		BufferedWriter bw1=new BufferedWriter(new FileWriter(new File("error.txt")));
		BufferedWriter bw2=new BufferedWriter(new FileWriter(new File("success.txt")));
		i_e=i_e>files.length?files.length:i_e;
		for(int i=i_s;i<i_e;i++){
			File file=files[i];
			System.out.println(i);
			if(file.isDirectory()){
				File[] ts=file.listFiles();
				if(ts!=null&ts.length>0){
					for(int j=0;j<ts.length;j++){
						File t=ts[j];
						System.out.println(t.getAbsolutePath());
						bw1.write(t.getAbsolutePath());
						bw1.write("\r\n");
						bw2.write(t.getAbsolutePath());
						bw2.write("\r\n");
						BufferedReader br=new BufferedReader(new FileReader(t));
						String line=null;
						boolean inTable=false;
						while((line=br.readLine())!=null){
							if (line.startsWith("</TABLE>")) {
								inTable = false;
							}
							if (inTable) {
								String[] strs=null;
								try {
									strs=parseLine2(line);
								} catch (RuntimeException e) {
									//e.printStackTrace();
								}
								
								if(strs==null){
									bw1.write("error:"+line);
									bw1.write("\r\n");
									
								}else{
									bw2.write("ok:"+line);
									bw2.write("\r\n");
									bw2.write("name: " + strs[0]);
									bw2.write("\r\n");
									bw2.write("type: " + strs[1]);
									bw2.write("\r\n");
									bw2.write("cusip: " + strs[2]);
									bw2.write("\r\n");
									bw2.write("amount: " + strs[3]);
									bw2.write("\r\n");
									bw2.write("shares: " + strs[4]);
									bw2.write("\r\n");
									bw2.write("\r\n");
//									System.out.println("ok:"+line);
//									System.out.println("name: " + strs[0]);
//									System.out.println("type: " + strs[1]);
//									System.out.println("cusip: " + strs[2]);
//									System.out.println("amount: " + strs[3]);
//									System.out.println("shares: " + strs[4]);
//									System.out.println();
								}

							}
							if (line.startsWith("<TABLE>")) {
								inTable = true;
							}
							
						}
					}
					bw1.flush();
					bw2.flush();
				}
			}
		}
		bw1.close();
		bw2.close();
	}
	public static void main(String[] args){
		updateCUSIP();
	}
	public static void updateCUSIP(){
		CUSIPManager cm = (CUSIPManager) ContextHolder.getInstance().getApplicationContext().getBean("cusipManager");
		List<CUSIP> cs=cm.getCUSIPs();
		for(int i=cs.size()-1;i<cs.size();i--){
			CUSIP c=cs.get(i);
			if(i%100==0)System.out.println(i);
			String cusip = c.getCUSIP();
			try {
				String symbol = EdgarUtil.getSymbolByFidelity(cusip);
				if(symbol!=null&&!symbol.equals(c.getSymbol())){
					c.setSymbol(symbol);
					cm.update(c);
					System.out.println("Update cusip["+cusip+"] to '"+symbol+"'");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
