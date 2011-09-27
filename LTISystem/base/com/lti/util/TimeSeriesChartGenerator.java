package com.lti.util;
import java.awt.Color;
import java.awt.Font;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.AbstractXYItemLabelGenerator;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.TextAnchor;

import com.lti.service.SecurityManager;
import com.lti.service.bo.PortfolioDailyData;
import com.lti.service.bo.Security;
import com.lti.service.bo.SecurityDailyData;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;

public class TimeSeriesChartGenerator {
	
	@SuppressWarnings("unchecked")
	public byte[] getLineXYChart(String title, String subtitle, String xtitle, String ytitle, int width,int height,List<String> titles,List<List> datas) throws Exception{
		
		File tmpFile=new File(Configuration.getTempDir(),System.currentTimeMillis()+".png");
		List<String> newTitles = new ArrayList<String>();
		
		for(String ss:titles){
			if(ss.length()>86){
				StringBuffer sb = new StringBuffer();
				for(int i=86;i>1;i--){
					if(ss.charAt(i)==' '){
						sb.append(ss.substring(0, i));
						sb.append("\n");
						sb.append(ss.substring(i+1,ss.length()));
						break;
					}
				}
				
				newTitles.add(sb.toString());
			}else{
				newTitles.add(ss);
			}
		}
		
		getLineXYChart2(title, subtitle, xtitle, ytitle,width,height,newTitles, datas,tmpFile);
		
		FileInputStream fis=new FileInputStream(tmpFile);
		
		byte[] bytes=new byte[(int)tmpFile.length()];
		fis.read(bytes);
		
		tmpFile.delete();
		
		return bytes;
		
	}

	
	public void getLineXYChart2(String title, String subtitle, String xtitle, String ytitle,int width,int height, List<String> titles,List<List> datas,File out) throws Exception{
		XYDataset dataset = this.createDateSetCollection(titles, datas);
		JFreeChart chart = ChartFactory.createTimeSeriesChart(title, xtitle, ytitle, dataset, true, false, false);

		chart.setBackgroundPaint(Color.WHITE);
		Font font = new Font(Font.SERIF, Font.BOLD, 16);
		Font subfont = new Font(Font.SERIF, Font.BOLD, 12);
		chart.setTitle(new TextTitle(title, font));
		chart.addSubtitle(new TextTitle(subtitle, subfont)); 
		LegendTitle legengTitle = chart.getLegend();
		legengTitle.setItemFont(font); 
	

		XYPlot plot = (XYPlot) chart.getPlot();
		plot.setBackgroundPaint(Color.WHITE);
		plot.setDomainGridlinePaint(Color.GRAY);
		plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
		plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
		plot.setDomainCrosshairVisible(true);
		plot.setRangeCrosshairVisible(true);
		

		XYItemRenderer r = plot.getRenderer();
		if (r instanceof XYLineAndShapeRenderer) {
			XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
			renderer.setBaseShapesVisible(false);
			renderer.setBaseShapesFilled(false);
			renderer.setBaseItemLabelGenerator(xyc);
			//renderer.setSeriesItemLabelPaint(0, Color.RED);
			//renderer.setSeriesItemLabelPaint(1, Color.BLUE);
			renderer.setSeriesPositiveItemLabelPosition(0, new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BOTTOM_CENTER,TextAnchor.BASELINE_LEFT,0));
			renderer.setSeriesPositiveItemLabelPosition(1, new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BOTTOM_CENTER,TextAnchor.BASELINE_LEFT,0));
			renderer.setSeriesPaint(0, new Color(0xFF, 0x55, 0x55));
			renderer.setSeriesPaint(1, new Color(0x55, 0x55, 0xFF));
			renderer.setSeriesPaint(2, new Color(0x55, 0xFF, 0x55));
			renderer.setSeriesPaint(3, Color.darkGray);
			renderer.setSeriesPaint(4, new Color(0xFF, 0x55, 0xFF));
			renderer.setSeriesPaint(5, new Color(0x55, 0xFF, 0xFF));
			renderer.setBaseItemLabelsVisible(true);
		}

		NumberAxis numAxis = (NumberAxis) plot.getRangeAxis();
		NumberFormat numFormater = NumberFormat.getNumberInstance();
		numFormater.setMinimumFractionDigits(2);
		numAxis.setNumberFormatOverride(numFormater);
		numAxis.setUpperBound((maxValue-minValue)*0.2+maxValue);
		double lb=minValue-(maxValue-minValue)*0.2;
		lb=(lb<0?0:lb);
		numAxis.setLowerBound(lb);

		DateAxis axis = (DateAxis) plot.getDomainAxis();
		axis.setDateFormatOverride(new SimpleDateFormat("yyyy-MM-dd"));
		int days=(int) ((endDate.getTime()-startDate.getTime())/(24*3600*1000));
		Calendar ca=Calendar.getInstance();
		ca.setTime(endDate);
		ca.add(Calendar.DAY_OF_YEAR, (int) (days*0.05));
		axis.setMaximumDate(ca.getTime());
		
		ChartUtilities.saveChartAsPNG(out, chart, width, height);
		
		
	}
	

	private XYItemLabelGenerator xyc = null;

	private void createXYItemLabelGenerator(int size, double[] maxes, double[] mins, String[] titles) {
		xyc = new MaxMinXYItemLabelGenerator(size, maxes, mins, titles);
	}

	private String[] titles = null;

	public class MaxMinXYItemLabelGenerator extends AbstractXYItemLabelGenerator implements XYItemLabelGenerator {

		private static final long serialVersionUID = 1L;

		private int size = -1;
		private double[] maxes;
		private double[] mins;
		private String[] titles;

		public MaxMinXYItemLabelGenerator(int size, double[] maxes, double[] mins, String[] titles) {
			super();
			this.size = size;
			this.maxes = maxes;
			this.mins = mins;

			double max = maxes[0];
			double min = mins[0];
			for(int i=0;i<maxes.length;i++){
				if (max < maxes[i])
					max = maxes[i];
				if (min > mins[i])
					min = mins[i];
			}


			this.titles = titles;
			TimeSeriesChartGenerator.this.titles = titles;
			numFormater = NumberFormat.getNumberInstance();
			numFormater.setMinimumFractionDigits(0);
			numFormater.setMaximumFractionDigits(0);
		}

		private NumberFormat numFormater = null;
		
		@Override
		public String generateLabel(XYDataset dataset, int series, int item) {
			double value = dataset.getYValue(series, item);
			
			
			if(dataset.getItemCount(series)==(item+1)){
				return numFormater.format(value);
			}
			
			if (value == maxes[series]) {
				return ".";
			} else if (value == mins[series]) {
				return ".";
			}
			
			
			return null;
		}

	}

	private double maxValue=Double.MIN_VALUE;
	private double minValue=Double.MAX_VALUE;
	private Date startDate=null;
	private Date endDate=null;

	private TimeSeriesCollection createDateSetCollection(List<String> titles, List<List> datas) {
		TimeSeriesCollection dataset = new TimeSeriesCollection();
		assert (titles.size() == datas.size());
		int size = titles.size();
		double[] maxes = new double[size];
		double[] mins = new double[size];	
		
		boolean first=true;
		for (int k = 0; k < datas.size(); k++) {
			List values = datas.get(k);									
			Date _startDate=null;
			Date _endDate=null;
			
			for (int i = 0; i < values.size(); i++) {			
					Object o = values.get(i);					
					Date date = null;
					if (o instanceof SecurityDailyData) {
						SecurityDailyData sdd = (SecurityDailyData) o;				
						date = sdd.getDate();
					} else if (o instanceof PortfolioDailyData) {
						PortfolioDailyData pdd = (PortfolioDailyData) o;
						date = pdd.getDate();
					}
					if(i==0){
						if(first)startDate=date;
						_startDate=date;
					}
					if(i==(values.size()-1)){
						if(first)endDate=date;
						_endDate=date;
						
					}			
				
			}
			first=false;
			if(startDate.before(_startDate))startDate=_startDate;
			if(endDate.before(_endDate))endDate=_endDate;
			
		}
		
		boolean hasprice = true;
		do{
			 hasprice = true; 
			for(int k=0;k<datas.size();k++){
				List values = datas.get(k);
				int low=0;
				int high = values.size()-1;
				while(low<=high){
					int mid = (low+high)/2;
					Object o =values.get(mid);
					Date date = null;
					if(o instanceof SecurityDailyData){
						SecurityDailyData sdd = (SecurityDailyData) o;				
						date = sdd.getDate();
					}else if (o instanceof PortfolioDailyData) {
						PortfolioDailyData pdd = (PortfolioDailyData) o;
						date = pdd.getDate();
					}
					if(LTIDate.equals(date, startDate)){
						break;
					}else if(date.after(startDate)){
						high = mid-1;
					}else if(date.before(startDate)){
						low = mid+1;
					}
					
					if(low>high)hasprice = false;
				}
				
				if(!hasprice){
					startDate = LTIDate.getNewNYSETradingDay(startDate,1);
					break;
				}
			}
			
		}while(!hasprice);
		
		for (int k = 0; k < datas.size(); k++) {
			List values = datas.get(k);
			TimeSeries s1 = new TimeSeries(titles.get(k));
			double max = Double.MIN_VALUE;
			double min = Double.MAX_VALUE;
			double mul=1.0;
			for (int i = 0; i < values.size(); i++) {			
					Object o = values.get(i);
					double value = 10000.0;
					Date date = null;
					if (o instanceof SecurityDailyData) {
						SecurityDailyData sdd = (SecurityDailyData) o;
						date = sdd.getDate();
						if(LTIDate.equals(date, startDate)){
							mul = 10000.0/sdd.getAdjClose();
						}
						if(date.after(startDate)||LTIDate.equals(date, startDate))
						  value = sdd.getAdjClose()*mul;					
						
					} else if (o instanceof PortfolioDailyData) {
						PortfolioDailyData pdd = (PortfolioDailyData) o;
						date = pdd.getDate();
						if(LTIDate.equals(date, startDate)){
							mul = 10000.0/pdd.getAmount();
						}
						if(date.after(startDate)||LTIDate.equals(date, startDate))
						   value = pdd.getAmount()*mul;
						//System.out.println(value);
						
					}
				
					if (max < value)
						max = value;
					if (min > value)
						min = value;
					if(date.after(startDate)||LTIDate.equals(date, startDate)){
						s1.addOrUpdate(new org.jfree.data.time.Day(date), value);
					}
									
				
			}
			first=false;
			maxes[k] = max;
			mins[k] = min;
			if(max>maxValue)maxValue=max;
			if(min<minValue)minValue=min;
			
			dataset.addSeries(s1);
		}
		String[] ts = new String[2];
		this.createXYItemLabelGenerator(size, maxes, mins, titles.toArray(ts));
		return dataset;
	}

	public static void main(String[] args) throws Exception{
		//PortfolioManager pm = ContextHolder.getPortfolioManager();
		//List<PortfolioDailyData> pdds1 = pm.getDailydatas(6601);
		//List<PortfolioDailyData> pdds2 = pm.getDailydatas(6605);
		SecurityManager sm=ContextHolder.getSecurityManager();
		Security vbinx=sm.get("vbinx");
		Security vfinx=sm.get("vfinx");

		List<SecurityDailyData> sdds1=sm.getDailydatas(vbinx.getID());
		List<SecurityDailyData> sdds2=sm.getDailydatas(vfinx.getID());

		
		
		List<List> datas = new ArrayList<List>();
		datas.add(sdds1);

		List<String> titles = new ArrayList<String>();
		titles.add(vbinx.getSymbol()+"("+vbinx.getName()+")");

		//TimeSeriesChartGenerator tjf = new TimeSeriesChartGenerator();
		//tjf.getLineXYChart2("1 benchmarks",  "Created by MyPlanIQ.com","Date", "Price", titles, datas,new File("Test.png"));
		//tjf.getLineXYChart2("Four benchmarks", "Created by MyPlanIQ.com", "Date", "Price", "test.png", titles, datas);
	}

}