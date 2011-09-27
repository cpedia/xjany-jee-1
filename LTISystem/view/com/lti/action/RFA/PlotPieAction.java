package com.lti.action.RFA;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.action.Action;
import com.lti.service.MutualFundManager;
import com.lti.service.UserManager;
import com.lti.service.bo.MutualFundDailyBeta;
import com.lti.service.bo.User;
import com.lti.system.ContextHolder;
import com.lti.util.FormatUtil;
import com.lti.util.LTIDate;
import com.lti.util.LTIRInterface;
import com.lti.util.StringUtil;
import com.opensymphony.xwork2.ActionSupport;
import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class PlotPieAction extends ActionSupport implements Action {
	private static final long serialVersionUID = 1L;
	static Log log = LogFactory.getLog(PlotPieAction.class);
	private UserManager userManager;

	private MutualFundManager mutualFundManager;

	private String symbol;
	private String indexArray;
	private Date date;

	private String filePath;
	
	private Long createTime;
	
	private Boolean IsRAA;

	public Boolean getIsRAA() {
		return IsRAA;
	}

	public void setIsRAA(Boolean isRAA) {
		IsRAA = isRAA;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setMutualFundManager(MutualFundManager mutualFundManager) {
		this.mutualFundManager = mutualFundManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public String execute() throws Exception {
		User user = userManager.getLoginUser();
		Long userID;
		if (user == null) {
			userID = 0l;
		} else
			userID = user.getID();

		String[] index = indexArray.replace(" ", "").split(",");

		Arrays.sort(index);

		MutualFundDailyBeta mfdb = mutualFundManager.getDailyData(symbol, index, LTIDate.clearHMSM(date),createTime,IsRAA);

		if (mfdb != null) {
			try {
				filePath = PlotPie(symbol, date, index, mfdb.getBetas());
			} catch (RuntimeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return Action.SUCCESS;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getIndexArray() {
		return indexArray;
	}

	public void setIndexArray(String indexArray) {
		this.indexArray = indexArray;
	}

	public static String PlotPie2(String bond, Date date, String index[], Double[] betas) {
		String fileName = StringUtil.getPath(new String[] { ContextHolder.getServletPath(), ContextHolder.getImagePath(), bond + date.getTime() + ".jpg" });
		String main = bond + "  [" + new SimpleDateFormat("yyyy-MM-dd").format(date) + "]";
		String sales = "";
		for (int i = 0; i < betas.length; i++) {
			sales += FormatUtil.formatQuantity(betas[i]);
			if (i != betas.length - 1)
				sales += ",";
		}
		String names = "";
		for (int i = 0; i < index.length; i++) {
			names += "\"" + index[i] + " " + FormatUtil.formatPercentage(betas[i]) + "%\"";
			if (i != index.length - 1)
				names += ",";
		}
		String[] cmds = { "bitmap(file=\"" + fileName + "\", type=\"jpeg\")", "require(grDevices)", "pie.sales <- c(" + sales + ")", "names(pie.sales) <- c(" + names + ")", "pie(pie.sales,main=\"" + main + "\")", "dev.off()" };
		LTIRInterface i = LTIRInterface.getInstance();
		String[] returns = i.RCall(cmds, 0);

		StringBuffer sb = new StringBuffer();
		sb.append("Call:\n\n");
		for (int j = 0; j < cmds.length; j++) {
			sb.append(cmds[j]);
			sb.append("\n");
		}
		sb.append("\n\n\n\nReturn:\n\n");
		if (returns != null) {

			for (int j = 0; j < returns.length; j++) {
				sb.append(returns[j]);
				sb.append("\n");
			}

		}
		log.info(sb.toString().replace("\'", "`").replace("\"", "\\\""));
		return StringUtil.getPath(new String[] { ContextHolder.getImagePath(), bond + date.getTime() + ".jpg" });
	}

	public static String PlotPie(String bond, Date date, String products[], Double[] sales) {
		PieColors pc = new PieColors();
		// Colors Color
		Color dropShadow = new Color(240, 240, 240);
		// inner padding to make sure bars never touch the outer border
		int innerOffset = 20;
		// Set the graph?s outer width & height
		int WIDTH = 400;
		int HEIGHT = 200;
		int pieHeight = HEIGHT - (innerOffset * 2);
		int pieWidth = pieHeight;
		// To make a square (circular) pie
		int halfWidth = WIDTH / 2;
		// Width of the inner graphable area
		int innerWIDTH = WIDTH - (innerOffset * 2);
		// graph dimensions Dimension
		Dimension graphDim = new Dimension(WIDTH, HEIGHT);
		Rectangle graphRect = new Rectangle(graphDim);
		// border dimensions
		Dimension borderDim = new Dimension(halfWidth - 2, HEIGHT - 2);
		Rectangle borderRect = new Rectangle(borderDim);

		// ///////////////////////////////////////////////////////////
		// Set up the graph
		// //////////////////////////////////////////////////////////
		// Set content type
		// Create BufferedImage & Graphics2D
		BufferedImage bi = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = bi.createGraphics();
		// Set Antialiasing RenderingHints
		RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHints(renderHints);
		// Set graph background color to white:
		g2d.setColor(Color.white);
		g2d.fill(graphRect);
		// Draw black border
		g2d.setColor(Color.black);
		borderRect.setLocation(1, 1);
		g2d.draw(borderRect);
		// Now draw border for legend
		borderRect.setLocation((WIDTH / 2) + 1, 1);
		g2d.draw(borderRect);
		// //////////////////////////////////////////////////////////////////
		// Draw data onto the graph:
		// //////////////////////////////////////////////////////////////////
		int x_pie = innerOffset;
		int y_pie = innerOffset;
		int border = 20;
		// Main chart Ellipse
		// Ellipse2D.Double el = new Ellipse2D.Double(x_pie, y_pie, pieWidth,
		// pieHeight);
		Ellipse2D.Double elb = new Ellipse2D.Double(x_pie - border / 2, y_pie - border / 2, pieWidth + border, pieHeight + border);
		// Shadow
		g2d.setColor(dropShadow);
		g2d.fill(elb);
		// Border
		g2d.setColor(Color.black);
		g2d.draw(elb);

		// ///////////////////////////////////////////////////////////////
		// Calculate the total sales
		// ///////////////////////////////////////////////////////////////
		float salesTotal = 0.0f;
		int lastElement = 0;
		for (int i = 0; i < products.length; i++) {
			if (sales[i] > 0.0f) {
				salesTotal += sales[i];
				lastElement = i;
			}
		}
		int startAngle = 0;
		// Legend variables
		int legendWidth = 20;
		int x_legendText = halfWidth + innerOffset / 2 + legendWidth + 5;
		int x_legendBar = halfWidth + innerOffset / 2;
		int textHeight = 20;
		int curElement = 0;
		int y_legend = 0;
		// Dimensions of the legend bar
		Dimension legendDim = new Dimension(legendWidth, textHeight / 2);
		Rectangle legendRect = new Rectangle(legendDim);
		for (int i = 0; i < products.length; i++) {
			if (sales[i] > 0.0f) {
				// Calculate percentage sales float
				double perc = (sales[i] / salesTotal);
				// Calculate new angle
				int sweepAngle = (int) (perc * 360);
				// Check that the last element goes back to 0 position
				if (i == lastElement) {
					sweepAngle = 360 - startAngle;
				}
				// Draw Arc
				g2d.setColor(pc.getPieColor());
				g2d.fillArc(x_pie, y_pie, pieWidth, pieHeight, startAngle, sweepAngle);
				// Increment startAngle with the sweepAngle
				startAngle += sweepAngle;
				// ///////////
				// Draw Legend
				// ///////////
				// Set y position for bar
				y_legend = curElement * textHeight + innerOffset;
				// Display the current product
				String display = products[i];
				g2d.setColor(Color.black);
				g2d.drawString(display, x_legendText, y_legend);
				// Display the total sales
				display = "" + (int) (sales[i] + 0);
				g2d.setColor(Color.black);
				g2d.drawString(display, x_legendText + 80, y_legend);
				// Display the sales percentage
				display = " (" + (int) (perc * 100) + "%)";
				g2d.setColor(Color.red);
				g2d.drawString(display, x_legendText + 110, y_legend);
				// Draw the bar
				g2d.setColor(pc.getPieColor());
				legendRect.setLocation(x_legendBar, y_legend - textHeight / 2);
				g2d.fill(legendRect);
				// Set new pie color
				pc.setNewColor();
				// Increment
				curElement++;
			}
		}
		// //////////////////////////////////////////////
		// Encode the graph
		// ///////////////////////////////////////
		try {
			String path=StringUtil.getPath(new String[] { ContextHolder.getServletPath(),ContextHolder.getImagePath(), bond + date.getTime() + ".jpg" });
			OutputStream output = new FileOutputStream(new File(path));
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(output);
			encoder.encode(bi);
			output.close();
			return ContextHolder.getImagePath().replace("\\", "/")+"/"+bond + date.getTime() + ".jpg";
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ImageFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ContextHolder.getImagePath().replace("\\", "/")+"/"+"error.jpg";
	}

	public static class PieColors {
		Color pieColorArray[] = { new Color(210, 60, 60), new Color(60, 210, 60), new Color(60, 60, 210), new Color(120, 60, 120), new Color(60, 120, 210), new Color(210, 120, 60) };
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
	
	public static void main(String[] args){
		System.out.println(PlotPie("test", new Date(), new String[]{"a","b"}, new Double[]{0.7,0.3}));
	}
}
