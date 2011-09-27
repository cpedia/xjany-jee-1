package com.lti.action.ajax;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.lti.action.Action;
import com.lti.service.PortfolioManager;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.PortfolioDailyData;
import com.lti.service.bo.PortfolioState;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.util.CurveChartUtil;
import com.lti.util.LTIDate;
import com.lti.util.StringUtil;
import com.opensymphony.xwork2.ActionSupport;

public class DownloadFileAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	public String path;

	public Boolean tmp = false;

	public Boolean getTmp() {
		return tmp;
	}

	public void setTmp(Boolean tmp) {
		this.tmp = tmp;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public InputStream inputStream;

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	private Boolean isImageCache = false;

	public Boolean getIsImageCache() {
		return isImageCache;
	}

	public void setIsImageCache(Boolean isImageCache) {
		this.isImageCache = isImageCache;
	}

	private Integer imageType = 0;
	private Long ID;
	private static final int R_T_C = 0;
	private static final int D_C = 1;
	private static final int R_T_PC = 2;
	private static final int D_PC = 3;

	public Integer getImageType() {
		return imageType;
	}

	public void setImageType(Integer imageType) {
		this.imageType = imageType;
	}

	public Long getID() {
		return ID;
	}

	public void setID(Long iD) {
		ID = iD;
	}

	private int width=-1;
	
	
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public String execute() throws Exception {
		if (!isImageCache) {
			if (name == null)
				return Action.ERROR;
			if (name.indexOf('\\') != -1 || name.indexOf('/') != -1)
				return Action.ERROR;
			if (name.endsWith(".jsp") || name.endsWith("ftl") || name.endsWith("java") || name.endsWith("class")) {
				return Action.ERROR;
			}
		}

		String path = null;
		if (!tmp) {
			if (isImageCache) {
				// from portfolios state
				PortfolioState ps = ContextHolder.getPortfolioManager().getPortfolioState(ID);
				byte[] bytes = null;
				if (imageType == R_T_C) {
					bytes = ps.getRealtimeChart();
				} else if (imageType == D_C) {
					bytes = ps.getDelayChart();
				} else if (imageType == R_T_PC) {
					bytes = ps.getRealtimePieChart();
				} else if (imageType == D_PC) {
					bytes = ps.getDelayPieChart();
				}
				
				if (width!=-1) {
					PortfolioManager pm=ContextHolder.getPortfolioManager();
					Portfolio portfolio=pm.get(ID);
					List<PortfolioDailyData> pdds=pm.getDailydatas(ID);
					double[] values = new double[pdds.size()];
					Date[] dates = new Date[pdds.size()];

					// 计算欲显示的纵坐标
					Calendar ca = Calendar.getInstance();
					ca.setTime(pdds.get(0).getDate());
					Date delayDate = LTIDate.getHoldingDateMonthEnd(pdds.get(pdds.size() - 1).getDate());
					int year = ca.get(Calendar.YEAR);
					int len = 0;
					for (int i = 0; i < pdds.size(); i++) {
						if (i == 0) {
							dates[i] = pdds.get(i).getDate();
						} else if (i == pdds.size() - 1) {
							dates[i] = pdds.get(i).getDate();
						} else {
							ca.setTime(pdds.get(i).getDate());
							int nyear = ca.get(Calendar.YEAR);
							if (nyear != year) {
								year = nyear;
								dates[i - 1] = pdds.get(i - 1).getDate();
							}
						}
						if (!delayDate.before(pdds.get(i).getDate()))
							len = i + 1;
						values[i] = pdds.get(i).getAmount();
					}
					
					if (imageType == R_T_C) {
						bytes = CurveChartUtil.drawChart(portfolio.getName(), dates, values, width,400 ,values.length);
					} else if (imageType == D_C) {
						if (len != 0)
							dates[len - 1] = pdds.get(len - 1).getDate();
						bytes = CurveChartUtil.drawChart(portfolio.getName(), dates, values, width,400 ,len);
					} 
					
					
					
					
				}
				inputStream = new ByteArrayInputStream(bytes);
				HttpServletResponse response = ServletActionContext.getResponse();
				response.setHeader("Pragma", "No-cache");
				response.setHeader("Cache-Control", "no-cache");
				response.setHeader("Cache-Control", "no-store");
				response.setDateHeader("Expires", 0);
				response.setContentType("image/jpeg");
				return Action.SUCCESS;
			} else {
				path = ContextHolder.getServletPath() + "/ExecutorEngine";
			}
		} else
			path = Configuration.getTempDir();
		File f = new File(StringUtil.getPath(new String[] { path, name }));
		inputStream = new FileInputStream(f);
		return Action.SUCCESS;
	}

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
