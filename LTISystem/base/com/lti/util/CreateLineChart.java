package com.lti.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.TextAnchor;
import com.lti.system.Configuration;


public class CreateLineChart {
	
	private double maxValue = Double.MIN_VALUE;
	private double minValue = Double.MAX_VALUE;
	public byte[] getChart(String title, String subtitle, String xtitle, String ytitle, String[] titles, String[][]dates,double[][]values) throws Exception {
		for (int k = 0; k < values.length; k++) {
			double[] datas = values[k];
			double max = Double.MIN_VALUE;
			double min = Double.MAX_VALUE;
			for (int i = 0; i < datas.length; i++) {
				try {
					double value = datas[i];				
					if (max < value)
						max = value;
					if (min > value)
						min = value;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (max > maxValue)
				maxValue = max;
			if (min < minValue)
				minValue = min;
		}
		File tmpFile = new File(Configuration.getTempDir(), System.currentTimeMillis() + ".png");

		getLineXYChart2(title, subtitle, xtitle, ytitle, maxValue,minValue,600,400,titles, dates, values, tmpFile);

		FileInputStream fis = new FileInputStream(tmpFile);

		byte[] bytes = new byte[(int) tmpFile.length()];
		fis.read(bytes);

		tmpFile.delete();

		return bytes;

	}
public void getLineXYChart2(String title, String subtitle, String xtitle, String ytitle, double maxValue,double minValue,int width,int height,String[] titles, String[][] dates, double[][] values, File out) throws Exception {
		
	    DefaultCategoryDataset dataset = this.createDateSetCollection(titles, dates, values);
	    LineAndShapeRenderer srenderer = new LineAndShapeRenderer();
		JFreeChart chart = ChartFactory.createLineChart(title, xtitle, ytitle, dataset, PlotOrientation.VERTICAL,true, true, false);
		chart.setBackgroundPaint(Color.WHITE);
		Font font = new Font(Font.SERIF, Font.BOLD, 16);
		Font subfont = new Font(Font.SERIF, Font.BOLD, 12);
		chart.setTitle(new TextTitle(title, font));
		chart.addSubtitle(new TextTitle(subtitle, subfont));

		LegendTitle legengTitle = chart.getLegend();
		legengTitle.setItemFont(subfont);

		CategoryPlot plot = chart.getCategoryPlot();
		plot.setBackgroundPaint(Color.WHITE);
		plot.setDomainGridlinePaint(Color.GRAY);
		plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
		plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
		plot.setDomainCrosshairVisible(true);
		plot.setRangeCrosshairVisible(true);
		
		//plot.setDataset(dataset); 
		plot.setRenderer(srenderer);

		LineAndShapeRenderer r =(LineAndShapeRenderer)plot.getRenderer();
		if (r instanceof LineAndShapeRenderer) {
			LineAndShapeRenderer renderer = (LineAndShapeRenderer) r;
			renderer.setBaseShapesVisible(false);
			renderer.setBaseShapesFilled(false);
			renderer.setSeriesPositiveItemLabelPosition(0, new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BOTTOM_LEFT, TextAnchor.BASELINE_LEFT, 0));
			renderer.setSeriesPositiveItemLabelPosition(1, new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BOTTOM_LEFT, TextAnchor.BASELINE_LEFT, 0));
			renderer.setShapesFilled(Boolean.TRUE);
	        renderer.setShapesVisible(true);
	        renderer.setStroke(new BasicStroke(3.0f));
			renderer.setBaseItemLabelsVisible(true);
		}

		NumberAxis numAxis = (NumberAxis) plot.getRangeAxis();
		NumberFormat numFormater = NumberFormat.getPercentInstance();
		numFormater.setMinimumFractionDigits(2);
		numAxis.setNumberFormatOverride(numFormater);
		numAxis.setUpperBound((maxValue - minValue) * 0.2 + maxValue);
		double lb = minValue - (maxValue - minValue) * 0.2;
		numAxis.setLowerBound(lb);
		CategoryAxis axis = (CategoryAxis) plot.getDomainAxis();
		ChartUtilities.saveChartAsPNG(out, chart, width, height);
	}


private DefaultCategoryDataset createDateSetCollection(String[] titles, String[][] dates, double[][] values) {
	DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	for (int k = 0; k < values.length; k++) {
		double[] datas = values[k];
		String[] ndates = dates[k];
		for (int i = 0; i < datas.length; i++) {		
				String date=ndates[i];
				double value = datas[i];
				dataset.addValue(value, titles[k], date);			
		}

	}
	return dataset;
}

}
