package com.lti.service.bo;

import java.io.Serializable;
import java.util.List;

public class BetagainDailyData implements Serializable {
	
	private java.lang.Double[] gains;
	private List<String> gainList;
	private java.lang.Double[] betas;
	protected java.util.Date Date;	
	protected java.lang.Long fundID;
	
	protected java.lang.Integer betaGainType;
	
	public java.lang.Double[] getGains() {
		return gains;
	}
	public void setGains(java.lang.Double[] gains) {
		this.gains = gains;
	}
	public List<String> getGainList() {
		return gainList;
	}
	public void setGainList(List<String> gainList) {
		this.gainList = gainList;
	}
	public java.util.Date getDate() {
		return Date;
	}
	public void setDate(java.util.Date date) {
		Date = date;
	}
	public java.lang.Long getFundID() {
		return fundID;
	}
	public void setFundID(java.lang.Long fundID) {
		this.fundID = fundID;
	}

	public java.lang.Double[] getBetas() {
		return betas;
	}
	public void setBetas(java.lang.Double[] betas) {
		this.betas = betas;
	}
	public java.lang.Integer getBetaGainType() {
		return betaGainType;
	}
	public void setBetaGainType(java.lang.Integer betaGainType) {
		this.betaGainType = betaGainType;
	}
	

	public static class BetaGainType{
		protected static final int OneMonth = 1;
		protected static final int ThreeMonth = 3;
		protected static final int SixMonth = 6;
		protected static final int OneYear = 10;
		protected static final int ThreeYear = 30;
		
		public static int getBetaGainType(int days)
		{
			if(days==22)return OneMonth;
			if(days==22*3)return ThreeMonth;
			if(days==22*6)return SixMonth;
			if(days==252)return OneYear;
			if(days==252*3)return ThreeYear;
			return -1;
		}
		
		public static int[] getBetaGainTypeDays(){
			int[] values = {22,22*3,22*6,252,252*3};
			return values;
		}
	}
}
