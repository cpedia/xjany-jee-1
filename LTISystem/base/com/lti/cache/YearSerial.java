package com.lti.cache;

public class YearSerial {
	public int year;
	public long securityid;
	public String symbol;
	public double[] splits = new double[260];
	public double[] dividends = new double[260];
	public double[] closes = new double[260];
	public double[] adjcloses = new double[260];
	public double[] adjnavs = new double[260];
	public double[] opens = new double[260];

	public long timeStamp=System.currentTimeMillis();
	
	public YearSerial(int year, long securityid) {
		super();
		this.year = year;
		this.securityid = securityid;
		for(int i=0;i<260;i++){
			splits[i] = Double.MIN_VALUE;
			dividends[i] = Double.MIN_VALUE;
			closes[i] = Double.MIN_VALUE;
			adjcloses[i] = Double.MIN_VALUE;
			adjnavs[i] = Double.MIN_VALUE;
			opens[i] = Double.MIN_VALUE;
		}
	}

	public double getSplit(int i){
		return splits[i];
	}

	public double getDividend(int i){
		return dividends[i];
	}
	
	public double getClose(int i){
		return closes[i];
	}
	
	public double getAdjClose(int i){
		return adjcloses[i];
	}
	
	public double getAdjNAV(int i){
		return adjnavs[i];
	}
	
	public double getOpen(int i){
		return opens[i];
	}
	
}
