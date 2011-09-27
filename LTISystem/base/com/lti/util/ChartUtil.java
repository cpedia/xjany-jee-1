package com.lti.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.ImageIO;


public class ChartUtil {
	public static byte[] PlotPie(String title,Map<String,Double> map) throws Exception{
		Iterator<String> sectors=map.keySet().iterator();
		String products[]=new String[map.size()];
		Double[] sales=new Double[map.size()];
		int i=0;
		double other=-1;
		while(sectors.hasNext()){
			String key=sectors.next();
			if(key.toLowerCase().indexOf("other")!=-1){
				other=map.get(key);
				continue;
			}
			products[i]=key;
			sales[i]=map.get(products[i]);
			i++;
		}
		if(other!=-1){
			products[i]="Other";
			sales[i]=other;
		}
		
		
		return PlotPie(title, products, sales);
	}
	public static class PieColors {

		Color pieColorArray[] = {
		// new Color(210, 60, 60),
				// new Color(60, 210, 60),
				// new Color(60, 60, 210),
				// new Color(120, 60, 120),
				// new Color(60, 120, 210),
				// new Color(210, 120, 60)
				new Color(56,94,15),
				new Color(210,105,30),
				
				new Color(65,105,225),
				
				new Color(115,74,18), 
				
				new Color(255,69,0), 
				
				new Color(3,168,158), 
				
				new Color(218,112,214),
				
				new Color(107,142,35), 
				
				new Color(237,145,33), 
				
				new Color(112,128,105), 
		};
		int curPieColor = 0;

		public Color getPieColor() {
			return pieColorArray[curPieColor];
		}

		public void setNewColor() {
			curPieColor++;
			if (curPieColor >= pieColorArray.length) {
				curPieColor = 0;
			}
		}
	}
	public static byte[] PlotPie(String title,String products[], Double[] sales) throws Exception{
		PieColors pc = new PieColors();
		Color dropShadow = new Color(240, 240, 240);
		int innerOffset = 20;
		int WIDTH = 500;
		int HEIGHT = 250;
		int pieHeight = HEIGHT - (innerOffset * 2);
		int pieWidth = pieHeight;

		int halfWidth = WIDTH / 2;

		Dimension graphDim = new Dimension(WIDTH, HEIGHT);
		Rectangle graphRect = new Rectangle(graphDim);

		Dimension borderDim = new Dimension(halfWidth - 2, HEIGHT - 2);
		Rectangle borderRect = new Rectangle(borderDim);

		BufferedImage bi = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = bi.createGraphics();
		
		RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHints(renderHints);
		g2d.setColor(Color.white);
		g2d.fill(graphRect);
		g2d.setColor(Color.black);
		borderRect.setLocation(1, 1);
		g2d.draw(borderRect);
		borderRect.setLocation((WIDTH / 2) + 1, 1);
		g2d.draw(borderRect);

		int x_pie = innerOffset;
		int y_pie = innerOffset;
		int border = 20;
		Ellipse2D.Double elb = new Ellipse2D.Double(x_pie - border / 2, y_pie - border / 2, pieWidth + border, pieHeight + border);
		g2d.setColor(dropShadow);
		g2d.fill(elb);
		g2d.setColor(Color.black);
		g2d.draw(elb);

		int startAngle = 180;
		int legendWidth = 20;
		int x_legendText = halfWidth + innerOffset / 2 + legendWidth + 5;
		int x_legendBar = halfWidth + innerOffset / 2;
		int textHeight = 20;
		int curElement = 0;
		int y_legend = 0;
		Dimension legendDim = new Dimension(legendWidth, textHeight / 2);
		Rectangle legendRect = new Rectangle(legendDim);
		double pertotal=0.0f;
		int lastElement = products.length-1;
		for (int i = 0; i < products.length; i++) {
			if (sales[i] > 0.0f) {
				if(sales[i]>1){
					for(int kk=0;kk<products.length;kk++){
						System.out.println(products[kk]+": "+sales[kk]);
					}
					if(products.length==1){
						sales[i]=1.0;
					}else{
						throw new RuntimeException("the percentage cannot be more than 100%.");
						
					}
				}
				double perc = sales[i];
				int sweepAngle = (int) (perc * 360);
				if (i == lastElement) {
					 sweepAngle = 180-startAngle;
					 if(sweepAngle<0)sweepAngle+=360;
					 sweepAngle=360-sweepAngle;
				}else{
					pertotal+=perc;
				}
				
				g2d.setColor(pc.getPieColor());
				g2d.fillArc(x_pie, y_pie, pieWidth, pieHeight, startAngle, -sweepAngle);
				startAngle -= sweepAngle;
				if(startAngle<0)startAngle+=360;

				y_legend = curElement * textHeight + innerOffset;
				String display = products[i];
				g2d.setColor(Color.black);
				g2d.drawString(display, x_legendText, y_legend);

				display = " (" + FormatUtil.formatPercentage(perc) + "%)";
				g2d.setColor(Color.red);
				g2d.drawString(display, x_legendText + 120, y_legend);

				g2d.setColor(pc.getPieColor());
				legendRect.setLocation(x_legendBar, y_legend - textHeight / 2);
				g2d.fill(legendRect);
				pc.setNewColor();
				curElement++;
			}
		}
		
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		ImageIO.write(bi, "png", baos);
		return baos.toByteArray();
	}
	
}
