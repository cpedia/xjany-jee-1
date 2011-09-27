package com.lti.util.validate;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;

import com.lti.service.PortfolioManager;
import com.lti.service.bo.PortfolioDailyData;
import com.lti.service.bo.PortfolioMPT;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.util.LTIDate;

public class PortfolioValidator {
	private List<PortfolioDailyData> portfolioDailyDatas;
	private List<PortfolioMPT> portfolioMPTs;
	private Long portfolioID;
	private static Log log;
	private PortfolioManager portfolioManager;

	public PortfolioValidator(Long portfolioID) {
		this.portfolioID = portfolioID;
		portfolioManager = ContextHolder.getPortfolioManager();
	}

	public void backupInformationFromDB() throws Exception {
		List<PortfolioMPT> mpts = portfolioManager.getEveryYearsMPT(portfolioID);
		List<PortfolioDailyData> pdds = portfolioManager.getDailydatas(portfolioID);
		this.portfolioDailyDatas = new ArrayList<PortfolioDailyData>();
		for (int i = 0; i < pdds.size(); i++) {
			if (LTIDate.isQuarterEnd(pdds.get(i).getDate())) {
				this.portfolioDailyDatas.add(pdds.get(i));
			}
		}

		this.portfolioMPTs = mpts;

		FileOutputStream fo = new FileOutputStream(new File("PortfolioCheckInformation" + portfolioID));
		ObjectOutputStream stream = new ObjectOutputStream(fo);
		stream.writeObject(portfolioID);
		stream.writeObject(this.portfolioDailyDatas.size());
		for (int i = 0; i < this.portfolioDailyDatas.size(); i++) {
			stream.writeObject(this.portfolioDailyDatas.get(i));
		}
		stream.writeObject(this.portfolioMPTs.size());
		for (int i = 0; i < this.portfolioMPTs.size(); i++) {
			stream.writeObject(this.portfolioMPTs.get(i));
		}
		stream.flush();
		stream.close();
		fo.flush();
		fo.close();
	}

	public void backupInformation() throws Exception {

		FileOutputStream fo = new FileOutputStream(new File("PortfolioCheckInformation" + portfolioID));
		ObjectOutputStream stream = new ObjectOutputStream(fo);

		stream.writeObject(portfolioID);
		stream.writeObject(this.portfolioDailyDatas.size());
		for (int i = 0; i < this.portfolioDailyDatas.size(); i++) {
			stream.writeObject(this.portfolioDailyDatas.get(i));
		}
		stream.writeObject(this.portfolioMPTs.size());
		for (int i = 0; i < this.portfolioMPTs.size(); i++) {
			stream.writeObject(this.portfolioMPTs.get(i));
		}
		stream.flush();
		stream.close();
		fo.flush();
		fo.close();

	}

	public void restoreInformation() throws Exception {
		FileInputStream io = new FileInputStream(new File("PortfolioCheckInformation" + portfolioID));
		ObjectInputStream stream = new ObjectInputStream(io);
		portfolioDailyDatas = new ArrayList<PortfolioDailyData>();
		portfolioMPTs = new ArrayList<PortfolioMPT>();
		portfolioID = (Long) stream.readObject();
		Integer pdds_size = (Integer) stream.readObject();
		if (pdds_size != null && pdds_size > 0) {
			for (int i = 0; i < pdds_size; i++) {
				portfolioDailyDatas.add((PortfolioDailyData) stream.readObject());
			}
		}
		Integer mpts_size = (Integer) stream.readObject();
		if (mpts_size != null && mpts_size > 0) {
			for (int i = 0; i < mpts_size; i++) {
				portfolioMPTs.add((PortfolioMPT) stream.readObject());
			}
		}
	}

	public List<PortfolioDailyData> getPortfolioDailyDatas() {
		return portfolioDailyDatas;
	}

	public void setPortfolioDailyDatas(List<PortfolioDailyData> portfolioDailyDatas) {
		this.portfolioDailyDatas = portfolioDailyDatas;
	}

	public List<PortfolioMPT> getPortfolioMPTs() {
		return portfolioMPTs;
	}

	public void setPortfolioMPTs(List<PortfolioMPT> portfolioMPTs) {
		this.portfolioMPTs = portfolioMPTs;
	}

	public void check() throws Exception {
		List<PortfolioMPT> mpts = portfolioManager.getEveryYearsMPT(portfolioID);
		List<PortfolioDailyData> pdds = portfolioManager.getDailydatas(portfolioID);
		System.out.println("Check portfolioMpt");
		for (int i = 0; i < this.portfolioMPTs.size(); i++) {
			for (int j = 0; j < mpts.size(); j++) {
				if (portfolioMPTs.get(i).getYear().equals(mpts.get(j).getYear())) {
					check(portfolioMPTs.get(i), mpts.get(j), "[" + portfolioID + "]Check MPT of " + portfolioMPTs.get(i).getYear() + ": ");
				}
			}
		}
		System.out.println("Check portfoliodailydata" + this.portfolioDailyDatas.size());
		for (int i = 1; i < this.portfolioDailyDatas.size(); i++) {
			for (int j = 0; j < pdds.size(); j++) {
				if (LTIDate.equals(portfolioDailyDatas.get(i).getDate(), pdds.get(j).getDate())) {
					check(portfolioDailyDatas.get(i), pdds.get(j), "[" + portfolioID + "]Check DailyData of " + portfolioDailyDatas.get(i).getDate() + ": ");
				}
			}
		}
	}

	public static void check(Object source, Object target, String pre) {
		BeanInfo sourceBean = null;
		BeanInfo targetbean = null;
		PropertyDescriptor[] targetpdds = null;
		CheckMpt cm = new CheckMpt();
		try {
			sourceBean = Introspector.getBeanInfo(source.getClass());
			targetbean = Introspector.getBeanInfo(target.getClass());
			targetpdds = targetbean.getPropertyDescriptors();

		} catch (Exception e2) {
			// log.warn("Translate Error! can not get BeanInfo from the source
			// object or the target object!");
			cm.writeLog("Translate Error! can not get BeanInfo from the source object or the target object!");
			return;
		}

		for (int i = 0; i < targetpdds.length; i++) {

			PropertyDescriptor pro = targetpdds[i];
			Method targetReadMethod = pro.getReadMethod();

			if (targetReadMethod != null) {

				PropertyDescriptor[] sourcepds = sourceBean.getPropertyDescriptors();

				for (int j = 0; j < sourcepds.length; j++) {
					if (sourcepds[j].getName().equals(pro.getName())) {
						try {
							Method sourceReadMethod = sourcepds[j].getReadMethod();
							if (!Modifier.isPublic(sourceReadMethod.getDeclaringClass().getModifiers())) {
								sourceReadMethod.setAccessible(true);
							}
							if (!Modifier.isPublic(targetReadMethod.getDeclaringClass().getModifiers())) {
								targetReadMethod.setAccessible(true);
							}
							Double sourceValue = (Double) sourceReadMethod.invoke(source, new Object[0]);
							Double targetValue = (Double) targetReadMethod.invoke(target, new Object[0]);

							if ((sourceValue != null && !sourceValue.equals(targetValue)) || (sourceValue == null && targetValue != null)) {
								// if (log != null)
								// log.warn(pre + pro.getName() + " source:" +
								// sourceValue + " target:" + targetValue);
								System.out.println(pre + pro.getName() + " source: " + sourceValue + "  target: " + targetValue);
								cm.writeLog(pre + pro.getName() + " source:" + sourceValue + " target:" + targetValue);
							}
						} catch (Exception e) {
						}

						break;
					}
				}
			}
		}
	}

	public void writeMptToFile(Long portfolioID) throws Exception {
		
		CheckMpt cm = new CheckMpt();
		String path =Configuration.getTempDir() + portfolioID+"PortfolioMpt.csv";
		BufferedWriter bw = new BufferedWriter(new FileWriter(path));
		bw.write("Year,Alpha,Beta,AR,RSquared,SharpeRatio,StandardDeviation,TreynorRatio,DrawDown\r\n");
		List<PortfolioMPT> mpt = portfolioManager.getEveryYearsMPT(portfolioID);

		for (int i = 0; i < mpt.size(); i++) {
			if (mpt != null) {

				// bw.write(mpt.get(i).getYear() + "," + mpt.get(i).getAlpha() +
				// "," + mpt.get(i).getBeta() + "," + mpt.get(i).getAR() + "," +
				// mpt.get(i).getRSquared() + "," + mpt.get(i).getSharpeRatio()
				// + "," + mpt.get(i).getStandardDeviation() + "," +
				// mpt.get(i).getTreynorRatio() + "," + mpt.get(i).getDrawDown()
				// + "\n");

				bw.write(mpt.get(i).getYear() + ",");
				if (mpt.get(i).getAlpha() != null)
					bw.write(mpt.get(i).getAlpha() + ",");
				else
					bw.write("-0.0,");
				if (mpt.get(i).getBeta() != null)
					bw.write(mpt.get(i).getBeta() + ",");
				else
					bw.write("-0.0" + ",");
				if (mpt.get(i).getAR() != null)
					bw.write(mpt.get(i).getAR() + ",");
				else
					bw.write("-0.0" + ",");
				if (mpt.get(i).getRSquared() != null)
					bw.write(mpt.get(i).getRSquared() + ",");
				else
					bw.write("-0.0" + ",");
				if (mpt.get(i).getSharpeRatio() != null)
					bw.write(mpt.get(i).getSharpeRatio() + ",");
				else
					bw.write("-0.0" + ",");
				if (mpt.get(i).getStandardDeviation() != null)
					bw.write(mpt.get(i).getStandardDeviation() + ",");
				else
					bw.write("-0.0" + ",");
				if (mpt.get(i).getTreynorRatio() != null)
					bw.write(mpt.get(i).getTreynorRatio() + ",");
				else
					bw.write("-0.0" + ",");
				if (mpt.get(i).getDrawDown() != null)
					bw.write(mpt.get(i).getDrawDown() + "\n");
				else
					bw.write("-0.0" + "\n");

			}
		}
		bw.flush();
		bw.close();
		System.out.println(portfolioID + "Save PortfolioMpt to Document Success");

	}

	public void writePddToFile(Long portfolioID) throws Exception {
		CheckMpt cm = new CheckMpt();
		String path =Configuration.getTempDir() + portfolioID+"PortfolioDailydata.csv";
//		BufferedWriter bw = new BufferedWriter(new FileWriter(portfolioID + "PortfolioDailydata.csv"));
          BufferedWriter bw = new BufferedWriter(new FileWriter(path));
		bw.write("DATE,AMOUNT,alpha3,alpha5,beta5,beta3,AR3,AR5,RSquared3,RSquared5,SharpeRatio3,SharpeRatio5,StandardDeviation3,StandardDeviation5,TreynorRatio3,TreynorRatio5,DrawDown3,DrawDown5,DrawDown1,TreynorRatio1,StandardDeviation1,SharpeRatio1,RSquared1,AR1,beta1,alpha1\r\n");
		List<PortfolioDailyData> pdd = portfolioManager.getDailydatas(portfolioID);
		for (int i = 0; i < pdd.size();) {
			if (pdd != null) {

				Date date = pdd.get(i).getDate();
				bw.write(date + ",");

				if (pdd.get(i).getAmount() != null)
					bw.write(pdd.get(i).getAmount() + ",");
				else
					bw.write("-0.0,");

				if (pdd.get(i).getAlpha3() != null)
					bw.write(pdd.get(i).getAlpha3() + ",");
				else
					bw.write("-0.0,");
				if (pdd.get(i).getAlpha5() != null)
					bw.write(pdd.get(i).getAlpha5() + ",");
				else
					bw.write("-0.0,");
				if (pdd.get(i).getBeta5() != null)
					bw.write(pdd.get(i).getBeta5() + ",");
				else
					bw.write("-0.0,");

				if (pdd.get(i).getBeta3() != null)
					bw.write(pdd.get(i).getBeta3() + ",");
				else
					bw.write("-0.0,");

				if (pdd.get(i).getAR3() != null)
					bw.write(pdd.get(i).getAR3() + ",");
				else
					bw.write("-0.0,");
				if (pdd.get(i).getAR5() != null)
					bw.write(pdd.get(i).getAR5() + ",");
				else
					bw.write("-0.0,");
				if (pdd.get(i).getRSquared3() != null)
					bw.write(pdd.get(i).getRSquared3() + ",");
				else
					bw.write("-0.0,");
				if (pdd.get(i).getRSquared5() != null)
					bw.write(pdd.get(i).getRSquared5() + ",");
				else
					bw.write("-0.0,");
				if (pdd.get(i).getSharpeRatio3() != null)
					bw.write(pdd.get(i).getSharpeRatio3() + ",");
				else
					bw.write("-0.0,");
				if (pdd.get(i).getSharpeRatio5() != null)
					bw.write(pdd.get(i).getSharpeRatio5() + ",");
				else
					bw.write("-0.0,");
				if (pdd.get(i).getStandardDeviation3() != null)
					bw.write(pdd.get(i).getStandardDeviation3() + ",");
				else
					bw.write("-0.0,");
				if (pdd.get(i).getStandardDeviation5() != null)
					bw.write(pdd.get(i).getStandardDeviation5() + ",");
				else
					bw.write("-0.0,");
				if (pdd.get(i).getTreynorRatio3() != null)
					bw.write(pdd.get(i).getTreynorRatio3() + ",");
				else
					bw.write("-0.0,");
				if (pdd.get(i).getTreynorRatio5() != null)
					bw.write(pdd.get(i).getTreynorRatio5() + ",");
				else
					bw.write("-0.0,");
				if (pdd.get(i).getDrawDown3() != null)
					bw.write(pdd.get(i).getDrawDown3() + ",");
				else
					bw.write("-0.0,");
				if (pdd.get(i).getDrawDown5() != null)
					bw.write(pdd.get(i).getDrawDown5() + ",");
				else
					bw.write("-0.0,");
				if (pdd.get(i).getDrawDown1() != null)
					bw.write(pdd.get(i).getDrawDown1() + ",");
				else
					bw.write("-0.0,");
				if (pdd.get(i).getTreynorRatio1() != null)
					bw.write(pdd.get(i).getTreynorRatio1() + ",");
				else
					bw.write("-0.0,");
				if (pdd.get(i).getStandardDeviation1() != null)
					bw.write(pdd.get(i).getStandardDeviation1() + ",");
				else
					bw.write("-0.0,");
				if (pdd.get(i).getSharpeRatio1() != null)
					bw.write(pdd.get(i).getSharpeRatio1() + ",");
				else
					bw.write("-0.0,");
				if (pdd.get(i).getRSquared1() != null)
					bw.write(pdd.get(i).getRSquared1() + ",");
				else
					bw.write("-0.0,");

				if (pdd.get(i).getAR1() != null)
					bw.write(pdd.get(i).getAR1() + ",");
				else
					bw.write("-0.0,");

				if (pdd.get(i).getBeta1() != null)
					bw.write(pdd.get(i).getBeta1() + ",");
				else
					bw.write("-0.0,");
				if (pdd.get(i).getAlpha1() != null)
					bw.write(pdd.get(i).getAlpha1() + "\n");
				else
					bw.write("-0.0" + "\n");
			}
			i = i + 300;
		}
		bw.flush();
		bw.close();
		System.out.println(portfolioID + "Save PortfolioDailydata to Document Success");
	}

	/**
	 * filename portfolioID + "PortfolioMpt.csv"
	 * 
	 * @param filename
	 * @return
	 * @throws Exception
	 */
	public static List<PortfolioMPT> getMptsFromFile(String filename) throws Exception {
		CheckMpt cm = new CheckMpt();
		String path =Configuration.getTempDir() +filename;
		FileReader fr = new FileReader(new File(path));
		BufferedReader br = new BufferedReader(fr);
		String line = null;
		List<PortfolioMPT> mpts = new ArrayList<PortfolioMPT>();
		while ((line = br.readLine()) != null) {
			String[] strs = line.split(",");
			if (strs == null || strs.length != 9)
				continue;
			int year = 0;
			try {
				year = Integer.parseInt(strs[0]);
			} catch (RuntimeException e) {
				continue;
			}
			Double alpha = 0.0;
			try {
				alpha = Double.parseDouble(strs[1]);
			} catch (RuntimeException e) {
				e.printStackTrace();
			}

			Double beta = 0.0;
			try {
				beta = Double.parseDouble(strs[2]);
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
			Double ar = 0.0;
			try {
				ar = Double.parseDouble(strs[3]);
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
			Double rSquared = 0.0;
			try {
				rSquared = Double.parseDouble(strs[4]);
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
			Double sharpeRatio = 0.0;
			try {
				sharpeRatio = Double.parseDouble(strs[5]);
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
			Double standardDeviation = 0.0;
			try {
				standardDeviation = Double.parseDouble(strs[6]);
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
			Double treynorRatio = 0.0;
			try {
				treynorRatio = Double.parseDouble(strs[7]);
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
			Double drawDown = 0.0;
			try {
				drawDown = Double.parseDouble(strs[8]);
			} catch (RuntimeException e) {
				e.printStackTrace();
			}

			PortfolioMPT mpt = new PortfolioMPT();
			mpt.setYear(year);
			mpt.setAlpha(alpha);
			mpt.setBeta(beta);
			mpt.setAR(ar);
			mpt.setRSquared(rSquared);
			mpt.setSharpeRatio(sharpeRatio);
			mpt.setStandardDeviation(standardDeviation);
			mpt.setTreynorRatio(treynorRatio);
			mpt.setDrawDown(drawDown);
			mpts.add(mpt);

		}
		return mpts;
	}

	/**
	 * filename portfolioID + "PortfolioDailydata.csv"
	 * 
	 * @param filename
	 * @return
	 * @throws Exception
	 */
	public static List<PortfolioDailyData> getDailyDataFromFile(String filename) throws Exception {
		CheckMpt cm = new CheckMpt();
		String path = Configuration.getTempDir()+filename;
		FileReader fr = new FileReader(new File(path));
		BufferedReader br = new BufferedReader(fr);
		String line = null;
		line = br.readLine();

		List<PortfolioDailyData> pdds = new ArrayList<PortfolioDailyData>();
		while ((line = br.readLine()) != null) {
			String[] strs = line.split(",");
			// if (strs == null || strs.length != 26)
			if (strs == null)
				continue;
			Date date = new Date();
			try {
				strs[0] = trim(strs[0]);
				String[] sd = strs[0].split(" ");
				date = LTIDate.parseStringToDate(sd[0]);
			} catch (RuntimeException e) {
				e.printStackTrace();
				// continue;
			}

			Double amount = 0.0;
			try {
				amount = Double.parseDouble(strs[1]);
			} catch (RuntimeException e1) {

				e1.printStackTrace();
			}

			Double alpha3 = 0.0;
			try {

				alpha3 = Double.parseDouble(strs[2]);
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
			Double alpha5 = 0.0;
			try {

				alpha5 = Double.parseDouble(strs[3]);
			} catch (RuntimeException e) {
				e.printStackTrace();
			}

			Double beta3 = 0.0;
			try {

				beta3 = Double.parseDouble(strs[5]);
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
			Double beta5 = 0.0;
			try {

				beta5 = Double.parseDouble(strs[4]);
			} catch (RuntimeException e) {
				e.printStackTrace();
			}

			Double ar3 = 0.0;
			try {

				ar3 = Double.parseDouble(strs[6]);
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
			Double ar5 = 0.0;
			try {

				ar5 = Double.parseDouble(strs[7]);
			} catch (RuntimeException e) {
				e.printStackTrace();
			}

			Double rSquared3 = 0.0;
			try {

				rSquared3 = Double.parseDouble(strs[8]);
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
			Double rSquared5 = 0.0;
			try {

				rSquared5 = Double.parseDouble(strs[9]);
			} catch (RuntimeException e) {
				e.printStackTrace();
			}

			Double sharpeRatio3 = 0.0;
			try {

				sharpeRatio3 = Double.parseDouble(strs[10]);
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
			Double sharpeRatio5 = 0.0;
			try {

				sharpeRatio5 = Double.parseDouble(strs[11]);
			} catch (RuntimeException e) {
				e.printStackTrace();
			}

			Double standardDeviation3 = 0.0;
			try {

				standardDeviation3 = Double.parseDouble(strs[12]);
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
			Double standardDeviation5 = 0.0;
			try {

				standardDeviation5 = Double.parseDouble(strs[13]);
			} catch (RuntimeException e) {
				e.printStackTrace();
			}

			Double treynorRatio3 = 0.0;
			try {

				treynorRatio3 = Double.parseDouble(strs[14]);
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
			Double treynorRatio5 = 0.0;
			try {

				treynorRatio5 = Double.parseDouble(strs[15]);
			} catch (RuntimeException e) {
				e.printStackTrace();
			}

			Double drawDown3 = 0.0;
			try {

				drawDown3 = Double.parseDouble(strs[16]);
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
			Double drawDown5 = 0.0;
			try {
				drawDown5 = Double.parseDouble(strs[17]);
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
			Double drawDown1 = 0.0;
			try {
				drawDown1 = Double.parseDouble(strs[18]);
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
			Double treynorRatio1 = 0.0;
			try {
				treynorRatio1 = Double.parseDouble(strs[19]);
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
			Double standardDeviation1 = 0.0;
			try {
				standardDeviation1 = Double.parseDouble(strs[20]);
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
			Double sharpeRatio1 = 0.0;
			try {
				sharpeRatio1 = Double.parseDouble(strs[21]);
			} catch (RuntimeException e) {
				e.printStackTrace();
			}

			Double rSquared1 = 0.0;
			try {
				rSquared1 = Double.parseDouble(strs[22]);
			} catch (RuntimeException e) {
				e.printStackTrace();
			}

			Double ar1 = 0.0;
			try {
				ar1 = Double.parseDouble(strs[23]);
			} catch (RuntimeException e) {
				e.printStackTrace();
			}

			Double beta1 = 0.0;
			try {
				beta1 = Double.parseDouble(strs[24]);
			} catch (RuntimeException e) {
				e.printStackTrace();
			}

			Double alpha1 = 0.0;
			try {
				alpha1 = Double.parseDouble(strs[25]);
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
			PortfolioDailyData pdd = new PortfolioDailyData();

			pdd.setDate(date);
			pdd.setAmount(amount);
			pdd.setAlpha1(alpha1);
			pdd.setAlpha3(alpha3);
			pdd.setAlpha5(alpha5);
			pdd.setBeta1(beta1);
			pdd.setBeta3(beta3);
			pdd.setBeta5(beta5);
			pdd.setAR1(ar1);
			pdd.setAR3(ar3);
			pdd.setAR5(ar5);
			pdd.setRSquared1(rSquared1);
			pdd.setRSquared3(rSquared3);
			pdd.setRSquared5(rSquared5);
			pdd.setSharpeRatio1(sharpeRatio1);
			pdd.setSharpeRatio3(sharpeRatio3);
			pdd.setSharpeRatio5(sharpeRatio5);
			pdd.setStandardDeviation1(standardDeviation1);
			pdd.setStandardDeviation3(standardDeviation3);
			pdd.setStandardDeviation5(standardDeviation5);
			pdd.setTreynorRatio1(treynorRatio1);
			pdd.setTreynorRatio3(treynorRatio3);
			pdd.setTreynorRatio5(treynorRatio5);
			pdd.setDrawDown1(drawDown1);
			pdd.setDrawDown3(drawDown3);
			pdd.setDrawDown5(drawDown5);
			pdds.add(pdd);
		}
		return pdds;
	}

	public static String trim(String s) {

		if (s == null)
			return null;
		char[] a = s.toCharArray();
		int start = 0;
		int end = a.length - 1;
		StringBuffer sb = new StringBuffer();
		if (a[start] == '\"')
			start++;
		if (a[end] == '\"')
			end--;
		for (int i = start; i <= end; i++)
			sb.append(a[i]);

		return sb.toString();
	}

	public static void main(String[] args) throws Exception {
		Long portfolioID = 3220L;
		PortfolioValidator validator = new PortfolioValidator(portfolioID);
		// List<PortfolioMPT> mpts=new ArrayList<PortfolioMPT>();
		// PortfolioMPT mpt=new PortfolioMPT();
		// mpt.setYear(2006);
		// mpt.setAlpha(0.11);
		// mpt.setRSquared(0.411506399492498);
		// mpts.add(mpt);
		// validator.setPortfolioMPTs(mpts);
		// //
		// List<PortfolioDailyData> pdds=new ArrayList<PortfolioDailyData>();
		// PortfolioDailyData pdd=new PortfolioDailyData();
		// pdd.setDate(LTIDate.getDate(1991, 1, 9));
		// pdd.setAmount(10010.5882297132);
		// pdds.add(pdd);
		// pdd=new PortfolioDailyData();
		// pdd.setDate(LTIDate.getDate(1991, 1, 10));
		// pdd.setAmount(10010.5882297132);
		// pdds.add(pdd);
		// validator.setPortfolioDailyDatas(pdds);

		try {
//			 validator.backupInformationFromDB();
//			 validator.restoreInformation();
//			 validator.writeMptToFile(portfolioID);
//			validator.writePddToFile(portfolioID);
			
			validator.setPortfolioMPTs(getMptsFromFile(portfolioID + "PortfolioMpt.csv"));
			validator.setPortfolioDailyDatas(getDailyDataFromFile(portfolioID + "PortfolioDailydata.csv"));
			validator.check();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Log getLog() {
		return log;
	}

	public static void setLog(Log log) {
		PortfolioValidator.log = log;
	}

}
