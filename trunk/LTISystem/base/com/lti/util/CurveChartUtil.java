package com.lti.util;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

public class CurveChartUtil{
	public static byte[] drawChart(String title,Date[] dates,double[] values,int len)throws Exception{
		int width=dates.length/255*60;
		if(width<600)width=600;
		if(width>1200)width=1200;
		width=920;
		int height=400;
		return drawChart( title, dates, values, width, height,len);
	}
	
	public static byte[] drawChart(String title,Date[] dates,double[] values,int width,int height,int len)throws Exception{
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		Graphics g = image.getGraphics();
		g.setColor(Color.white);
		g.fillRect(0, 0, width, height);
		
		paint(g, title, dates, values, width, height,len);
		
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		ImageIO.write(image, "png", baos);
		return baos.toByteArray();
	}
	
	@Deprecated
	public static void drawChart(String filename,String title,Date[] dates,double[][] values)throws Exception{
		int width=dates.length/255*60;
		if(width<600)width=600;
		if(width>1200)width=1200;
		width=920;
		int height=400;
		
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		Graphics g = image.getGraphics();
		g.setColor(Color.white);
		g.fillRect(0, 0, width, height);
		
		
		for (int i = 0; i < values.length; i++) {
			paint(g, title, dates, values[i], width, height, values[i].length);
		}
		FileOutputStream out=new FileOutputStream(new File(filename));
		ImageIO.write(image, "png", out);
		//JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
		//encoder.encode(image);
		out.close();
	}
	
	

	public static final int wSpace=60;
	public static final int hSpace=30;
	
	private static void paintTitle(Graphics2D g2,String title,int width,int height){
		Font font = g2.getFont().deriveFont(12.0f);
		g2.setFont(font);
		g2.setPaint(Color.GRAY);
		g2.drawString(title, (width - title.length() * 5) / 2, hSpace/2);
	}
	
	private static void paintBorder(Graphics2D g2,String title,int width,int height){
		g2.setStroke(new BasicStroke(1.0f));
		g2.draw(new Line2D.Double(wSpace, height - hSpace, width - wSpace, height - hSpace));
		g2.draw(new Line2D.Double(wSpace,  hSpace, width - wSpace,  hSpace));
		
		g2.draw(new Line2D.Double(wSpace, height - hSpace,	wSpace, hSpace));
		g2.draw(new Line2D.Double(width - wSpace, height - hSpace,	width - wSpace, hSpace));
	}
	
	private static void drawString(Graphics2D g2,Color col,String text,int x,int y){
		Color ocol=g2.getColor();
		g2.setColor(col);
		g2.drawString(text, x,y);
		g2.setColor(ocol);
	}
	
	public static void paint(Graphics g,String title,Date[] _dates,double[] _values,int width,int height,int len) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, width, height);

		paintTitle(g2, title, width, height);
				
		paintBorder(g2, title, width, height);
		
		FontMetrics metrics = g2.getFontMetrics();
		Font font = g2.getFont().deriveFont(12.0f);
		
		double maxValue=Double.MIN_VALUE;
		double minValue=Double.MAX_VALUE;
		for(int i=0;i<len;i++){
			if(maxValue<_values[i])maxValue=_values[i];
			if(minValue>_values[i])minValue=_values[i];
		}
		
		long MAX_VALUE=1l;
		long tmp=(long)maxValue;
		while((tmp/10)!=0){
			MAX_VALUE*=10;
			tmp/=10;
		}
		if(tmp>5){
			MAX_VALUE=(tmp+1)*MAX_VALUE;
		}else{
			long tmpMAX_VALUE=(long)((tmp+0.5)*MAX_VALUE);
			if(tmpMAX_VALUE<maxValue){
				MAX_VALUE=(tmp+1)*MAX_VALUE;
			}else{
				MAX_VALUE=tmpMAX_VALUE;
			}
		}
		
		
		double widthPer = (width - wSpace*2) *1.0/ (len);

		g2.setPaint(Color.lightGray);
		g2.setStroke(new BasicStroke(1.0f));
		for (int i = 0; i < 3; i++) {
			String ylabel = String.valueOf((int)(MAX_VALUE*i/2));
			drawString(g2,Color.gray,ylabel, wSpace-metrics.stringWidth(ylabel),(height - hSpace)-i*(height-60)/2);
			if(i==1)g2.draw(new Line2D.Double(wSpace, (height - hSpace)-i*(height-60)/2,width-	wSpace , (height - hSpace)-i*(height-60)/2));
		}
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf1=new SimpleDateFormat("yy");
		// draw x title;
		GeneralPath path = new GeneralPath(GeneralPath.WIND_EVEN_ODD, len - 1);
		boolean writeMax=false;
		boolean writeMin=false;
		for (int i = 0; i < len; i++) {
			font = g2.getFont().deriveFont(10.0f);
			g2.setFont(font);
			g2.setPaint(Color.LIGHT_GRAY);
			
			if(_dates[i]!=null){
				String datestr=null;
				int xPoint=-1;
				if(i==0||i==len-1){
					datestr=sdf.format(_dates[i]);
					xPoint=(int)(wSpace + widthPer * i - metrics.stringWidth(datestr) / 2);
					drawString(g2,Color.GRAY,datestr, xPoint, height - 20);
				}else{
					datestr=sdf1.format(_dates[i]);
					xPoint=(int)(wSpace + widthPer * i - metrics.stringWidth(datestr) / 2);
					drawString(g2,Color.GRAY,datestr, xPoint, height - 10);
					g2.draw(new Line2D.Double(wSpace + widthPer * i, height - hSpace,	wSpace + widthPer * i, hSpace));
				}
				
			}
			
			if (i == 0) {
				long yPoint=Math.round(height - hSpace -	(_values[i]/MAX_VALUE) * (height - hSpace*2));
				String beginStr=FormatUtil.formatQuantity(_values[0]);
				drawString(g2,Color.GRAY,beginStr,  wSpace-metrics.stringWidth(beginStr), (int)yPoint+10);
				path.moveTo(wSpace,yPoint);
			}
			else {
				long yPoint=height - hSpace -Math.round((_values[i]/MAX_VALUE) * (height - hSpace*2));
				g2.setStroke(new BasicStroke(1.0f));
				path.lineTo(Math.round(widthPer * i) + wSpace,	yPoint);
				
				g2.setPaint(Color.LIGHT_GRAY);
				if(_values[i]==minValue||_values[i]==maxValue){
					String valueString=FormatUtil.formatQuantity(_values[i]);
					if(_values[i]==minValue&&writeMin==false){
						yPoint+=20;
						valueString="Low:"+valueString;
						writeMin=true;
						drawString(g2,Color.GRAY,valueString,  (int)Math.round(widthPer * i) + wSpace-metrics.stringWidth(valueString)/2, (int)yPoint);
					}else if(_values[i]==maxValue&&writeMax==false){
						valueString="High:"+valueString;
						yPoint-=10;
						writeMax=true;
						drawString(g2,Color.GRAY,valueString,  (int)Math.round(widthPer * i) + wSpace-metrics.stringWidth(valueString)/2, (int)yPoint);
					}
					
				}
			}
		}
		String lastValue=FormatUtil.formatQuantity(_values[len-1]);
		lastValue=" "+lastValue;
		
		int xPoint=(int) (wSpace + widthPer * (len-1));
		drawString(g2,Color.GRAY,lastValue,  xPoint,(int) Math.round(height - hSpace -	_values[len-1]/MAX_VALUE * (height - hSpace*2)));
		g2.setColor(new Color(0,128,255));
		g2.draw(path);
	}

}