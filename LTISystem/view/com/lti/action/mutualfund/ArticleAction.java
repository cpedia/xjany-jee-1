package com.lti.action.mutualfund;

import java.io.File;
import java.io.FileReader;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.supercsv.io.CsvListReader;
import org.supercsv.prefs.CsvPreference;

import com.lti.action.Action;
import com.lti.service.MutualFundManager;
import com.lti.service.bo.MutualFundDailyBeta;
import com.lti.system.ContextHolder;
import com.lti.type.TimeUnit;
import com.lti.util.FormatUtil;
import com.lti.util.LTIDate;

public class ArticleAction {

	private List<MutualFundDailyBeta> fridayBetas;
	private String[] index;
	private String symbol;
	public String getmutualFund() throws Exception{
		MutualFundManager mutualFundManager = ContextHolder.getMutualFundManager();
	    Long createTime = java.util.Calendar.getInstance().getTime().getTime();
		Date startDate;
		Date endDate;
		boolean IsRAA = true;
		DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
		
		File mutualFile = new File(URLDecoder.decode(com.lti.action.mutualfund.ArticleAction.class.getResource("mutualFunds.csv").getFile(), "utf-8"));
		CsvListReader mReader = new CsvListReader(new FileReader(mutualFile), CsvPreference.STANDARD_PREFERENCE);
		
		
		List<String> weeksNum = mReader.read();
		int num = Integer.parseInt(weeksNum.get(1));
		
		endDate = df1.parse(df1.format(new Date()));
		startDate = df1.parse(df1.format(LTIDate.getNewDate(new Date(),TimeUnit.WEEKLY, num)));
		
		List<String> intervalList = mReader.read();
		int interval = Integer.parseInt(intervalList.get(1));
		
		List<String> wlsList = mReader.read();		
		boolean IsWLSorOLS ;
		if(wlsList.get(1).equalsIgnoreCase("true")){
			IsWLSorOLS = true;
		}else{
			IsWLSorOLS = false;
		}
		
		List<String> sumList = mReader.read();
		boolean IsSumToOne;
		if(sumList.get(1).equalsIgnoreCase("true")){
			IsSumToOne = true;
		}else{
			IsSumToOne = false;
		}
		
		List<String> withConsList = mReader.read();
		boolean withCons ;
		if(withConsList.get(1).equalsIgnoreCase("true")){
			withCons = true;
		}else{
			withCons = false;
		}
		
		List<String> indexList;
		List<String> fundList = new ArrayList<String>();
		List<String> upperList = new ArrayList<String>();
		List<String> lowerList = new ArrayList<String>();
		while((indexList = mReader.read()) != null){
			fundList.add(indexList.get(0));
			upperList.add(indexList.get(2));
			lowerList.add(indexList.get(1));
		}
		index = new String[fundList.size()];
		for(int j=0;j<fundList.size();j++){
			index[j] = fundList.get(j);
		}
		Arrays.sort(index);
		double[] upper = new double[upperList.size()];
		for(int j=0;j<upperList.size();j++){
			upper[j] = Double.parseDouble(upperList.get(j));
		}
		
		double []lower = new double[lowerList.size()];
		for(int k =0;k<lowerList.size();k++){
			lower[k] = Double.parseDouble(lowerList.get(k));
		}
		
		mutualFundManager.setupLimit(upper, lower);
		mutualFundManager.calculateDailyBeta(symbol, interval, index, createTime, startDate, endDate, withCons, IsWLSorOLS, IsSumToOne);
		
		List<MutualFundDailyBeta> mutualFundDailyBetas = mutualFundManager.getDailyData(symbol, index, createTime, IsRAA);
		for(int i=0;i<mutualFundDailyBetas.size();i++){
			MutualFundDailyBeta beta=mutualFundDailyBetas.get(i);
			List<String> betaList=new ArrayList<String>();
			for(int j=0;j<beta.getBetas().length;j++){				
				betaList.add(FormatUtil.formatPercentage(beta.getBetas()[j]));
			}
			
			beta.setBetaList(betaList);
			String RSQuare=FormatUtil.formatQuantity(beta.getRSquare());
			beta.setRSquareString(RSQuare);
		}
		fridayBetas = new ArrayList<MutualFundDailyBeta>();
		for(MutualFundDailyBeta beta :mutualFundDailyBetas){
			if(LTIDate.isWeekDay(beta.getDate(), 6)){
				fridayBetas.add(beta);
			}
		}
		
		int limit = fridayBetas.size()-4;
		for(int i =0;i<limit;i++){
			fridayBetas.remove(0);
		}
		return Action.SUCCESS;
	}

	public List<MutualFundDailyBeta> getFridayBetas() {
		return fridayBetas;
	}

	public void setFridayBetas(List<MutualFundDailyBeta> fridayBetas) {
		this.fridayBetas = fridayBetas;
	}

	public String[] getIndex() {
		return index;
	}

	public void setIndex(String[] index) {
		this.index = index;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
}
