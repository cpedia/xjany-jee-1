package com.lti.bean;

public class SortTypeBean {
	
	public final static int SHARPE = 1;
	public final static int ALPHA = 2;
	public final static int BETA = 3;
	public final static int RSQUARED = 4;
	public final static int RETURN = 5;
	public final static int TREYNOR = 6;
	public final static int STANDARDDIVIATION = 7;
	public final static int DRAWDOWN = 8;
	public final static int ANNULIZEDRETURN = 9;
	
	private String Name;
	
	private int Value;
	
	public static String getMPTName(int mpt){
		String name="";
		switch(mpt){
			case SHARPE: name = "SharpeRatio"; break;
			case ALPHA: name = "Alpha"; break;
			case BETA: name = "Beta"; break;
			case RSQUARED: name = "RSquared"; break;
			case RETURN: name = "Return"; break;
			case TREYNOR: name = "TreynorRatio"; break;
			case STANDARDDIVIATION: name = "StandardDeviation"; break;
			case DRAWDOWN: name = "DrawDown"; break;
			case ANNULIZEDRETURN: name = "AR"; break;
		}
		return name;
	}

	public static int getMPT(String name){
		if(name.toLowerCase().equals("sharperatio"))
			return SHARPE;
		else if(name.toLowerCase().equals("alpha"))
			return ALPHA;
		else if(name.toLowerCase().equals("beta"))
			return BETA;
		else if(name.toLowerCase().equals("rsquared"))
			return RSQUARED;
		else if(name.toLowerCase().equals("return"))
			return RETURN;
		else if(name.toLowerCase().equals("treynorratio"))
			return TREYNOR;
		else if(name.toLowerCase().equals("standarddeviation"))
			return STANDARDDIVIATION;
		else if(name.toLowerCase().equals("drawdown"))
			return DRAWDOWN;
		else if(name.toLowerCase().equals("ar"))
			return ANNULIZEDRETURN;
		else
			return -1;
	}
	
	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public int getValue() {
		return Value;
	}

	public void setValue(int value) {
		Value = value;
	}
	
	

}
