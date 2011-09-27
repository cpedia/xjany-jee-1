package com.lti.ta.lib;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.lti.service.SecurityManager;
import com.lti.service.bo.SecurityDailyData;
import com.lti.system.ContextHolder;
import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MAType;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

public class TestTaLib {

	public static void cal(Date[] dates,double[] open,double[] high,double[] low,double[] close,double[] volume)throws Exception{
		Core lib = new Core();
		int len = open.length;
		List tech_inds=new ArrayList<double[]>();
		List<String> titles=new ArrayList<String>();
		
		tech_inds.add(dates);
		titles.add("dates");
		tech_inds.add(open);
		titles.add("open");
		tech_inds.add(high);
		titles.add("high");
		tech_inds.add(low);
		titles.add("low");
		tech_inds.add(close);
		titles.add("close");
		//tech_inds.add(volume);
		//titles.add("volume");
		
		

		int lookback = 0;
		RetCode retCode = RetCode.InternalError;

		MInteger outBegIdx = new MInteger();
		MInteger outNbElement = new MInteger();
		lookback = lib.macdLookback(15, 26, 9);

		
		
		double[] macd = arr(len);
		double[] signal = arr(len);
		double[] hist = arr(len);
		retCode = lib.macd(0, close.length - 1, close, 15, 26, 9, outBegIdx, outNbElement, macd, signal, hist);
		adj(macd,outBegIdx,outNbElement);
		tech_inds.add(macd);
		titles.add("Moving Average Convergence/Divergence");
		
		
		double[] will_r=arr(len);
		lib.willR(0, len-1, high, low, close, 14, outBegIdx, outNbElement, will_r);
		adj(will_r,outBegIdx,outNbElement);
		tech_inds.add(will_r);
		titles.add("Williams' %R");
		
		
		double[] rsi=arr(len);
		lib.rsi(0, len-1, close, 14, outBegIdx, outNbElement, rsi);
		adj(rsi, outBegIdx, outNbElement);
		tech_inds.add(rsi);
		titles.add("Relative Strength Index");
		
		
		double[] sar=arr(len);
		lib.sar(0,len-1, high, low, 0.02, 4, outBegIdx, outNbElement, sar);
		adj(sar, outBegIdx, outNbElement);
		tech_inds.add(sar);
		titles.add("Parabolic SAR");
		
		
		double[] cci=arr(len);
		lib.cci(0, len-1, high, low, close, 7, outBegIdx, outNbElement, cci);
		adj(cci, outBegIdx, outNbElement);
		tech_inds.add(cci);
		titles.add("Commodity Channel Index");
		
		
		double[] mom=arr(len);
		lib.mom(0, len-1, close, 7, outBegIdx, outNbElement, mom);
		adj(mom, outBegIdx, outNbElement);
		tech_inds.add(mom);
		titles.add("Momentum");
		
		
		double[] fk=arr(len);
		double[] fd=arr(len);
		lib.stochF(0, len-1, high, low, close, 7, 7, MAType.Sma, outBegIdx, outNbElement, fk, fd);
		adj(fk, outBegIdx, outNbElement);
		adj(fd, outBegIdx, outNbElement);
		
		double[] k=arr(len);
		double[] d=arr(len);
		lib.stoch(0, len-1, high, low, close, 7, 7, MAType.Sma, 7, MAType.Sma, outBegIdx, outNbElement, k, d);
		adj(k, outBegIdx, outNbElement);
		adj(d, outBegIdx, outNbElement);
		
		tech_inds.add(fk);
		titles.add("Stochastic %k");
		tech_inds.add(fd);
		titles.add("Stochastic %d");
		
		tech_inds.add(d);
		titles.add("Stochastic slow %d");
		
		double[] roc=arr(len);
		lib.roc(0, len-1, close, 7, outBegIdx, outNbElement, roc);
		adj(roc, outBegIdx, outNbElement);
		tech_inds.add(roc);
		titles.add("Rate of change");
		
		
		
		
		double[] ad=arr(len);
		lib.ad(0, len-1, high, low, close, volume, outBegIdx, outNbElement, ad);
		adj(ad, outBegIdx, outNbElement);
		tech_inds.add(ad);
		titles.add("Chaikin A/D Line");
		
		
		
		double[] obv=arr(len);
		lib.obv(0, len-1, close, volume, outBegIdx, outNbElement, obv);	
		adj(obv, outBegIdx, outNbElement);
		tech_inds.add(ad);
		titles.add("On Balance Volume");
		
		
		double[] adx=arr(len);
		lib.adx(0, len-1, high, low, close, 7, outBegIdx, outNbElement, adx);
		adj(adx, outBegIdx, outNbElement);
		tech_inds.add(adx);
		titles.add("Average Directional Movement Index");
		
		
		double[] ema=arr(len);
		lib.ema(0, len-1, close, 7, outBegIdx, outNbElement, ema);
		adj(ema, outBegIdx, outNbElement);
		tech_inds.add(ema);
		titles.add("Exponential Moving Average");
		
		
		
		double[] sma=arr(len);
		lib.sma(0, len-1, close, 7, outBegIdx, outNbElement, sma);
		adj(sma,outBegIdx,outNbElement);
		tech_inds.add(sma);
		titles.add("Simple Moving Average");
		
		
		double[] atr=arr(len);
		lib.atr(0, len-1, high, low, close, 7, outBegIdx, outNbElement, atr);
		adj(atr,outBegIdx,outNbElement);
		tech_inds.add(atr);
		titles.add("Average True Range");
		
		
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		BufferedWriter bw=new BufferedWriter(new FileWriter(new File("gspc.csv")));
		StringBuffer sb=new StringBuffer();
		for(String title:titles){
			sb.append(title).append(",");
		}
		sb.append("\n");
		for(int i=0;i<len;i++){
			Date date=((Date[])(tech_inds.get(0)))[i];
			sb.append(df.format(date)).append(",");
			for(int j=1;j<tech_inds.size();j++){
				sb.append(((double[])(tech_inds.get(j)))[i]).append(",");
			}
			sb.append("\n");
		}
		bw.write(sb.toString());
		bw.close();
		
		System.out.println();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args)throws Exception {
		
		SecurityManager securityManager = ContextHolder.getSecurityManager();
		List<SecurityDailyData> sdds = securityManager.getDailydatas(14l);
		int len = sdds.size();
		double[] open = new double[len];
		double[] high = new double[len];
		double[] low = new double[len];
		double[] close = new double[len];
		double[] volume = new double[len];
		Date[] dates = new Date[len];
		for (int i = 0; i < len; i++) {
			SecurityDailyData sdd = sdds.get(i);
			dates[i] = sdd.getDate();
			open[i] = sdd.getOpen();
			high[i] = sdd.getHigh();
			low[i] = sdd.getLow();
			close[i] = sdd.getClose();
			volume[i]=sdd.getVolume();
		}
		
		cal(dates, open, high, low, close, volume);
		
	}

	public static void adj(double[] arr, MInteger outBegIdx, MInteger outNbElement) {
		for (int i = arr.length - 1; i >= outBegIdx.value; i--) {
			arr[i] = arr[i - outBegIdx.value];
		}

		for (int i = 0; i < outBegIdx.value; i++) {
			arr[i] = Double.NaN;
		}
	}

	public static double[] arr(int len) {
		double[] arr = new double[len];
		for (int i = 0; i < len; i++) {
			arr[i] = Double.NaN;
		}

		return arr;
	}

}
