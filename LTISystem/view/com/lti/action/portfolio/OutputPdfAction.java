package com.lti.action.portfolio;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.struts2.ServletActionContext;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.pdf.PdfWriter;
import com.lti.action.Action;
import com.lti.action.articleaction;
import com.lti.bean.MPTBean;
import com.lti.permission.PortfolioPermissionChecker;
import com.lti.service.PortfolioManager;
import com.lti.service.bo.HoldingItem;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.PortfolioDailyData;
import com.lti.service.bo.PortfolioInf;
import com.lti.service.bo.PortfolioMPT;
import com.lti.service.bo.PortfolioState;
import com.lti.service.bo.Security;
import com.lti.service.bo.SecurityDailyData;
import com.lti.service.bo.Strategy;
import com.lti.system.ContextHolder;
import com.lti.type.finance.Asset;
import com.lti.type.finance.HoldingInf;
import com.lti.util.LTIDate;
import com.lti.util.SecurityUtil;
import com.lti.util.StringUtil;
import com.lti.util.TimeSeriesChartGenerator;
import com.opensymphony.xwork2.ActionSupport;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class OutputPdfAction extends ActionSupport {
	private static final long serialVersionUID = 13L;
	private InputStream pdfStream;

	public InputStream getPdfStream() {
		return pdfStream;
	}

	public void setPdfStream(InputStream pdfStream) {
		this.pdfStream = pdfStream;
	}

	private Long ID;
	public Long getID() {
		return ID;
	}

	public void setID(Long iD) {
		ID = iD;
	}
	
	
	
	//http://localhost/LTISystem/article_viewchart.action?portfolioID=5198&securityID=1144,91&width=680&height=500&Type=1
	public byte[] getChart(boolean realtime)throws Exception{
		articleaction ac=new articleaction();
		ac.setPortfolioID(portfolio.getID()+"");
		ac.setSecurityID("1144,91");
		ac.setType(realtime?0:1);
		ac.viewchart();
		return ac.getBytes();
	}
	
	
	
	
	PortfolioManager portfolioManager;
	public String execute() throws Exception {
		portfolioManager=ContextHolder.getPortfolioManager();
		portfolio=portfolioManager.get(ID);
		
		portfolioName=portfolio.getName();
		
		PortfolioPermissionChecker pc = new PortfolioPermissionChecker(portfolio, ServletActionContext.getRequest());
		boolean operation = pc.hasOperationRole();
		boolean isAdmin = pc.isAdmin();
		boolean isOwner = pc.isOwner();
		boolean realtime = pc.hasRealtimeRole();
		HoldingInf holding=null;
		if (!realtime) {
			PortfolioInf _pif=portfolioManager.getPortfolioInf(ID, com.lti.system.Configuration.PORTFOLIO_HOLDING_DELAY);
			if(_pif!=null)holding = _pif.getHolding();
		} else {
			PortfolioInf _pif=portfolioManager.getPortfolioInf(ID, com.lti.system.Configuration.PORTFOLIO_HOLDING_CURRENT);
			if(_pif!=null)holding = _pif.getHolding();
		}
		
		PortfolioState ps = ContextHolder.getPortfolioManager().getPortfolioState(ID);
		File piefile=File.createTempFile(portfolio.getID()+"piechart", "png");
		if(!realtime){
			FileOutputStream fos=new FileOutputStream(piefile);
			fos.write(ps.getDelayPieChart());
			fos.flush();
			fos.close();
		}else{
			FileOutputStream fos=new FileOutputStream(piefile);
			fos.write(ps.getRealtimePieChart());
			fos.flush();
			fos.close();
		}
		
		File chartfile=File.createTempFile(portfolio.getID()+"chart", "png");
		byte[] bytes=getChart(realtime);
		FileOutputStream fos=new FileOutputStream(chartfile);
		fos.write(bytes);
		fos.flush();
		fos.close();
		
		String validName=StringUtil.getValidName(portfolio.getName());
		Configuration conf = new Configuration();
		conf.setDirectoryForTemplateLoading(new File(ContextHolder.getServletPath(),"/jsp/portfolio"));
		Template t = conf.getTemplate("pdf.uftl");
		Map<Object, Object> data=new HashMap<Object, Object>();
		
		getMPT();
		data.put("MPTBeans", MPTBeans);
		String piechart="nochart.jpg";
		//piechart=ViewPortfolioAction.plotHodingChart(portfolioManager, portfolio);
		data.put("piechart", piefile.getAbsolutePath());
		data.put("portfolio", portfolio);
		data.put("chart", chartfile.getAbsolutePath());
		SecurityUtil.usedescription(portfolio,holding.getHoldingItems());
		data.put("holding", holding);
		try {
			if(portfolio.getStrategies().getAssetAllocationStrategy().getParameter().get("PlanID")!=null){
				String planid=portfolio.getStrategies().getAssetAllocationStrategy().getParameter().get("PlanID");
				Strategy plan=ContextHolder.getStrategyManager().get(Long.parseLong(planid));
				data.put("plan", plan);
			}
		} catch (Exception e1) {
		}
		
		data.put("host", ServletActionContext.getRequest().getServerName());
		
		
		data.put("total", holding.getAmount());
		StringWriter sw=new StringWriter();
		t.process(data, sw);
		String htmText = sw.toString();
		
		
		Document document = new Document(PageSize.A4);
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		PdfWriter.getInstance(document, buffer);
		document.open();
		StringReader sbis = new StringReader(htmText);
		List<Element> objects = HTMLWorker.parseToList(sbis, null, new HashMap<String, Object>());
		for (Element element : objects) {
			document.add(element);
		}

		document.close();

		this.pdfStream = new ByteArrayInputStream(buffer.toByteArray());
		buffer.close();
		return Action.SUCCESS;
	}
	private Date startDate;
	private Date endDate;
	private List<MPTBean> MPTBeans;
	private Portfolio portfolio;
	private String portfolioName="detail.pdf";
	
	public String getPortfolioName() {
		return portfolioName;
	}

	public void setPortfolioName(String portfolioName) {
		this.portfolioName = portfolioName;
	}

	public void getMPT(){
		boolean realtime=true;
		
		MPTBeans = new ArrayList<MPTBean>();

		// List<MPT> MPTs = portfolio.getEveryYearMPTS(TimeUnit.DAILY);

		List<PortfolioMPT> MPTs = portfolioManager.getEveryYearsMPT(ID);
		int num = 0;
		MPTBean bean1 = null;
		MPTBean bean3 = null;
		MPTBean bean5 = null;
		MPTBean bean0 = null;
		if (MPTs != null || MPTs.size() > 0) {
			for (int i = 0; i < MPTs.size(); i++) {
				PortfolioMPT mpt = MPTs.get(i);
				MPTBean bean = new MPTBean();

				switch (mpt.getYear()) {
				case PortfolioMPT.FROM_STARTDATE_TO_ENDDATE:
					if (!realtime)
						continue;
					bean0 = bean;
					break;
				case PortfolioMPT.LAST_ONE_YEAR:
					if (!realtime)
						continue;
					bean1 = bean;
					break;
				case PortfolioMPT.LAST_THREE_YEAR:
					if (!realtime)
						continue;
					bean3 = bean;
					break;
				case PortfolioMPT.LAST_FIVE_YEAR:
					if (!realtime)
						continue;
					bean5 = bean;
					break;
				case PortfolioMPT.DELAY_FROM_STARTDATE_TO_ENDDATE:
					if (realtime)
						continue;
					else
						mpt.setYear(PortfolioMPT.FROM_STARTDATE_TO_ENDDATE);
					bean0 = bean;
					break;
				case PortfolioMPT.DELAY_LAST_ONE_YEAR:
					if (realtime)
						continue;
					else
						mpt.setYear(PortfolioMPT.LAST_ONE_YEAR);
					bean1 = bean;
					break;
				case PortfolioMPT.DELAY_LAST_THREE_YEAR:
					if (realtime)
						continue;
					else
						mpt.setYear(PortfolioMPT.LAST_THREE_YEAR);
					bean3 = bean;
					break;
				case PortfolioMPT.DELAY_LAST_FIVE_YEAR:
					if (realtime)
						continue;
					else
						mpt.setYear(PortfolioMPT.LAST_FIVE_YEAR);
					bean5 = bean;
					break;
				default:
					MPTBeans.add(bean);
					break;
				}

				if (mpt.getAlpha() == null || mpt.getAlpha() > 10000.0)
					bean.setAlpha("NA");
				else
					bean.setAlpha(com.lti.util.FormatUtil.formatPercentage(mpt.getAlpha()));

				if (mpt.getBeta() == null || mpt.getBeta() > 10000.0)
					bean.setBeta("NA");
				else
					bean.setBeta(com.lti.util.FormatUtil.formatQuantity(mpt.getBeta()));

				if (mpt.getAR() == null || mpt.getAR() > 10000.0)
					bean.setAR("NA");
				else
					bean.setAR(com.lti.util.FormatUtil.formatPercentage(mpt.getAR()));

				if (mpt.getRSquared() == null || mpt.getRSquared() > 10000.0)
					bean.setRSquared("NA");
				else
					bean.setRSquared(com.lti.util.FormatUtil.formatQuantity(mpt.getRSquared()));

				if (mpt.getSharpeRatio() == null || mpt.getSharpeRatio() > 10000.0)
					bean.setSharpeRatio("NA");
				else
					bean.setSharpeRatio(com.lti.util.FormatUtil.formatPercentage(mpt.getSharpeRatio()));

				if (mpt.getStandardDeviation() == null || mpt.getStandardDeviation() > 10000.0)
					bean.setStandardDeviation("NA");
				else
					bean.setStandardDeviation(com.lti.util.FormatUtil.formatQuantity(mpt.getStandardDeviation()));

				if (mpt.getTreynorRatio() == null || mpt.getTreynorRatio() > 10000.0)
					bean.setTreynorRatio("NA");
				else
					bean.setTreynorRatio(com.lti.util.FormatUtil.formatQuantity(mpt.getTreynorRatio()));

				if (mpt.getDrawDown() == null || mpt.getDrawDown() > 10000.0)
					bean.setDrawDown("NA");
				else
					bean.setDrawDown(com.lti.util.FormatUtil.formatQuantity(mpt.getDrawDown()));

				if (mpt.getSortinoRatio() == null || mpt.getSortinoRatio() > 10000.0)
					bean.setSortinoRatio("NA");
				else
					bean.setSortinoRatio(com.lti.util.FormatUtil.formatQuantity(mpt.getSortinoRatio()));

				mpt.setYearString();
				bean.setStartDate(mpt.getStartDate());
				bean.setEndDate(mpt.getEndDate());
				bean.setYear(mpt.getYear());
				bean.setYearString(translateYearString(mpt.getYear()));
				num++;

			}

		}
		if (bean1 != null) {
			MPTBeans.add(bean1);
		}
		if (bean3 != null) {
			MPTBeans.add(bean3);
		}
		if (bean5 != null) {
			MPTBeans.add(bean5);
		}

		if (bean0 != null) {
			MPTBeans.add(bean0);
		}
	}
	private String translateYearString(Integer year) {
		String yearString = "";
		if (year > 0)
			yearString = year.toString();
		else {
			switch (year) {
			case PortfolioMPT.FROM_STARTDATE_TO_ENDDATE:
				yearString = getText("from.starting.to.end");
				break;
			case PortfolioMPT.LAST_ONE_YEAR:
				yearString = getText("last.one.years");
				break;
			case PortfolioMPT.LAST_THREE_YEAR:
				yearString = getText("last.three.years");
				break;
			case PortfolioMPT.LAST_FIVE_YEAR:
				yearString = getText("last.five.years");
				break;
			default:
				break;
			}
		}
		return yearString;
	}
}
