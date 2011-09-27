package com.lti.util;

public class FourZeroOneParsingUtil {
	
	public static double match(String line,String target){
		int llen=line.length();
		int tlen=target.length();
		String lstr=line.toLowerCase();
		String tstr=target.toLowerCase();
		if(lstr.equals(tstr))return Double.MAX_VALUE;
		if(lstr.contains(tstr)||tstr.contains(lstr))return Double.MAX_VALUE*0.5;
		String[] lpairs=lstr.split(" ");
		String[] tpairs=tstr.split(" ");
		
		double weight=0.0;
		for(int i=0;i<lpairs.length;i++){
			for(int j=0;j<tpairs.length;j++){
				if(lpairs[i].equals(tpairs[j])){
					double v=1-Math.abs(i*1.0/llen-j*1.0/tlen);
					weight+=v;
				}
			}
		}
		return weight;
	}
	public static String[] getQuadruple(String line){
		
		return null;
	}
	public static void main(String[] args){
		System.out.println(match("a 1 3 d d a b c", "a 1 2 3 f"));
	}
}
