package com.lti.service.bo;

public class CompanyIndex extends com.lti.service.bo.base.CompanyIndex {
	
	private static final long serialVersionUID = 1L;
	
	public final static String Type_10KSB="10KSB";
	public final static String Type_10QSB="10QSB";
	public final static String Type_10QSB_A="10QSB/A";
	public final static String Type_10KSB40="10KSB40";
	public final static String Type_10KT405="10KT405";
	public final static String Type_10KSB_A="10KSB/A";
	public final static String Type_10KSB40_A="10KSB40/A";
	public final static String Type_10KT405_A="10KT405/A";
	public final static String Type_13F_HR="13F-HR";
	public final static String Type_13F_HR_A="13F-HR/A";
	public final static String Type_N_Q="N-Q";
	public final static String Type_N_Q_A="N-Q/A";
	
	
	private int count;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	public boolean equals(Object o){
		return o==null?false:this.getFileName().equals(((CompanyIndex)o).getFileName())?true:false;
	}


}
