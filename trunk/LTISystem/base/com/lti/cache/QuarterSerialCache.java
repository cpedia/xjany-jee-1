package com.lti.cache;

public class QuarterSerialCache {
	private int offset;
	public double[] splits;
	public double[] dividends;
	public double[] adjcloses;
	public double[] closes;
	
	public QuarterSerialCache(int offset) {
		super();
		this.offset = offset;
	}
	
	public double getSplit(int i){
		return splits[i-offset];
	}

	public double getDividend(int i){
		return dividends[i-offset];
	}
	
	public double getAdjClose(int i){
		return adjcloses[i-offset];
	}
	public double getClose(int i){
		return closes[i-offset];
	}
	
}
