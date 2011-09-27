package com.lti.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.exolab.castor.xml.Unmarshaller;

import com.lti.Exception.VfofxHasBeenUpdatedException;
import com.lti.edgar.EdgarUtil;
import com.lti.ofx.InvestmentPosition;
import com.lti.ofx.InvestmentPositionList;
import com.lti.ofx.InvestmentPositionListChoice;
import com.lti.ofx.InvestmentPositionListChoiceItem;
import com.lti.ofx.InvestmentStatementResponse;
import com.lti.ofx.InvestmentStatementResponseMessageSetV1;
import com.lti.ofx.InvestmentStatementResponseMessageSetV1Item;
import com.lti.ofx.InvestmentStatementTransactionResponse;
import com.lti.ofx.OFXResponseType;
import com.lti.service.bo.HoldingItem;
import com.lti.service.bo.Security;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;

/**
 * @author Michael Cai
 * 
 */
public class OFXUtil {

	public static String get(String key, String memo) {
		try {
			String[] strs = memo.split(",");
			for (int i = 0; i < strs.length; i++) {
				String[] pair = strs[i].split(":");
				if (pair[0].trim().toLowerCase().equals(key.toLowerCase()))
					return pair[1].trim();
			}
		} catch (Exception e) {

		}
		return null;

	}

	public static List<HoldingItem> getSecurityItems(String ofxFile) throws Exception {
		List<HoldingItem> sis = new ArrayList<HoldingItem>();
		Unmarshaller unm = new Unmarshaller(OFXResponseType.class);
		unm.setValidation(false);
		OFXResponseType ofx = (OFXResponseType) unm.unmarshal(new FileReader(ofxFile));
		InvestmentStatementResponseMessageSetV1 INVSTMTMSGSRSV1 = ofx.getINVSTMTMSGSRSV1();
		InvestmentStatementResponseMessageSetV1Item[] vInvestmentStatementResponseMessageSetV1ItemArray = INVSTMTMSGSRSV1.getInvestmentStatementResponseMessageSetV1Item();
		InvestmentStatementResponseMessageSetV1Item isrmv1t = vInvestmentStatementResponseMessageSetV1ItemArray[0];
		InvestmentStatementTransactionResponse INVSTMTTRNRS = isrmv1t.getINVSTMTTRNRS();
		InvestmentStatementResponse INVSTMTRS = INVSTMTTRNRS.getINVSTMTRS();
		InvestmentPositionList INVPOSLIST = INVSTMTRS.getINVPOSLIST();
		InvestmentPositionListChoice investmentPositionListChoice = INVPOSLIST.getInvestmentPositionListChoice();
		if(investmentPositionListChoice!=null){
			InvestmentPositionListChoiceItem[] vInvestmentPositionListChoiceItemArray = investmentPositionListChoice.getInvestmentPositionListChoiceItem();
			for (int i = 0; i < vInvestmentPositionListChoiceItemArray.length; i++) {
				InvestmentPositionListChoiceItem choiceItem = vInvestmentPositionListChoiceItemArray[i];
				InvestmentPosition position = getPosition(choiceItem);
				HoldingItem si = new HoldingItem();
				si.setPrice(Double.parseDouble(position.getUNITPRICE()));
				si.setShare(Double.parseDouble(position.getUNITS()));
				si.setCUSIP(position.getSECID().getUNIQUEID());
				String symbol = get("symbol", position.getMEMO());
				if (symbol == null) {
					symbol = EdgarUtil.getSymbolByFidelity(si.getCUSIP());
				}
				si.setSymbol(symbol);
				sis.add(si);
			}
		}
		return sis;
	}

	public static SimpleDateFormat OFXDateFormat = new SimpleDateFormat("yyyyMMdd");

	public static String getZipEntryName(List<String> entryNames, Date CurrentDate, Date lastUpdateDate, boolean useFiledDate) throws Exception {

		Date previousTradingDate = LTIDate.getNewNYSETradingDay(CurrentDate, -1);

		Collections.sort(entryNames);

		String entry = null;
		Date compareDate = null;
		Date nextCompareDate = null;
		for (int i = 0; i < entryNames.size(); i++) {

			if (i == 0) {
				String[] dates = entryNames.get(i).split("\\.");
				String conformedDate = dates[1];
				String filedDate = dates[2];
				if (useFiledDate) {
					compareDate = OFXDateFormat.parse(filedDate);
				} else {
					compareDate = OFXDateFormat.parse(conformedDate);
				}
			} else {
				compareDate = nextCompareDate;
			}

			if (i != entryNames.size() - 1) {
				nextCompareDate = null;
				String[] dates = entryNames.get(i + 1).split("\\.");
				if (useFiledDate) {
					nextCompareDate = OFXDateFormat.parse(dates[2]);
				} else {
					nextCompareDate = OFXDateFormat.parse(dates[1]);
				}

				long d1 = CurrentDate.getTime() - compareDate.getTime();
				long d2 = CurrentDate.getTime() - nextCompareDate.getTime();
				long d3 = compareDate.getTime() - previousTradingDate.getTime();
				if (d1 == 0 || (d1 > 0 && d2 < 0 && d3 > 0)) {
					entry = entryNames.get(i);
					break;
				}

			} else {
				long d1 = CurrentDate.getTime() - compareDate.getTime();
				long d2 = compareDate.getTime() - previousTradingDate.getTime();
				if (d1 >= 0) {
					if (d2 > 0) {
						entry = entryNames.get(i);
					} else {
						if (compareDate.getTime() > lastUpdateDate.getTime()) {
							throw new VfofxHasBeenUpdatedException("The vfofx file has been updated, this portfolio will be re-monitored next time.");
						}
					}

				}
			}
		}
		return entry;

	}

	public static Date getNextUpdateDate(String name, Date CurrentDate, boolean useFiledDate) throws Exception {
		String fileName = Configuration.getVFOFXDir() + "/" + EdgarUtil.getVfofxName(name);
		ZipFile zipFile = new ZipFile(fileName);

		Enumeration emu = zipFile.entries();
		List<String> entryNames = new ArrayList<String>();
		while (emu.hasMoreElements()) {
			ZipEntry e = (ZipEntry) emu.nextElement();
			entryNames.add(e.getName());
		}
		zipFile.close();
		Collections.sort(entryNames);

		long[] dtimes = new long[entryNames.size()+2];

		dtimes[0]=0;
		for (int i = 0; i < entryNames.size(); i++) {
			String[] dates = entryNames.get(i).split("\\.");
			String conformedDate = dates[1];
			String filedDate = dates[2];
			if (useFiledDate) {
				dtimes[i+1] = OFXDateFormat.parse(filedDate).getTime();
			} else {
				dtimes[i+1] = OFXDateFormat.parse(conformedDate).getTime();
			}
		}
		dtimes[dtimes.length-1]=Long.MAX_VALUE;
		
		long time=CurrentDate.getTime();
		for(int i=1;i<dtimes.length-1;i++){
			if(time>=dtimes[i-1]&&time<dtimes[i+1]){
				if(dtimes[i+1]!=Long.MAX_VALUE)return new Date(dtimes[i+1]);
			}
			
		}
		return null;
	}

	/**
	 * Get securityitems from vfofx file
	 * 
	 * @param name
	 *            the 13f name
	 * @param CurrentDate
	 *            'Current Date'
	 * @param year
	 *            the wanted year
	 * @param quarter
	 *            the wanted quarter
	 * @param useFiledDate
	 *            if it's true, the wantedDate must be after filedDate
	 * @return
	 * @throws Exception
	 */
	public static List<HoldingItem> getSecurityItems(String name, Date CurrentDate, Date lastUpdateDate, boolean useFiledDate) throws Exception {
		String fileName = Configuration.getVFOFXDir() + "/" + EdgarUtil.getVfofxName(name);

		List<HoldingItem> sis = null;

		// File f=new File(fileName);
		ZipFile zipFile = new ZipFile(fileName);

		// System.out.println(wantDate);
		Enumeration emu = zipFile.entries();
		List<String> entryNames = new ArrayList<String>();
		while (emu.hasMoreElements()) {
			ZipEntry e = (ZipEntry) emu.nextElement();
			entryNames.add(e.getName());
		}

		String entryName = getZipEntryName(entryNames, CurrentDate, lastUpdateDate, useFiledDate);

		if (entryName != null) {
			ZipEntry entry = zipFile.getEntry(entryName);
			BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry));
			File file = new File(entry.getName());
			if(!file.exists()){
				file.createNewFile();
			}
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

			sis = getSecurityItems(entry.getName());

			new File(entry.getName()).delete();
		}

		zipFile.close();

		return sis;
	}

	public static InvestmentPosition getPosition(InvestmentPositionListChoiceItem choiceItem) {
		if (choiceItem.getPOSSTOCK() != null)
			return choiceItem.getPOSSTOCK().getINVPOS();
		else if (choiceItem.getPOSMF() != null)
			return choiceItem.getPOSMF().getINVPOS();
		else if (choiceItem.getPOSDEBT() != null)
			return choiceItem.getPOSDEBT().getINVPOS();
		else if (choiceItem.getPOSOPT() != null)
			return choiceItem.getPOSOPT().getINVPOS();
		else if (choiceItem.getPOSOTHER() != null)
			return choiceItem.getPOSOTHER().getINVPOS();
		else {
			return null;
		}
	}
	public static void main(String[] args) throws Exception {
		Security s1=ContextHolder.getSecurityManager().get("BRK/B");
		System.out.println(s1);
	}

	public static void main2(String[] args) throws Exception {
		List<String> entryNames = new ArrayList<String>();
		entryNames.add("1990-01-02");
		entryNames.add("1990-01-03");
		entryNames.add("1990-01-04");
		entryNames.add("1990-01-05");
		entryNames.add("1990-01-06");
		entryNames.add("1980-12-02");
		entryNames.add("1990-12-02");
		entryNames.add("1993-01-02");
		entryNames.add("1992-01-02");

		Collections.sort(entryNames);
		for (int i = 0; i < entryNames.size(); i++) {
			System.out.println(entryNames.get(i));
		}
	}

}
