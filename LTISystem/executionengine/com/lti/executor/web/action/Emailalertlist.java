package com.lti.executor.web.action;

import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.supercsv.io.CsvListWriter;
import org.supercsv.prefs.CsvPreference;

import com.lti.executor.web.BasePage;
import com.lti.service.PortfolioManager;
import com.lti.service.bo.Portfolio;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;

public class Emailalertlist extends BasePage {

	
	
	private String info;

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	private List<Portfolio> portfolios;
	
	public List<Portfolio> getPortfolios() {
		return portfolios;
	}

	public void setPortfolios(List<Portfolio> portfolios) {
		this.portfolios = portfolios;
	}

	private DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
	private String getString(Object o){
		if(o==null)return "NA";
		if(o instanceof Date){
			return df.format(o);
		}
		return o.toString();
	}
	private String filename;
	@Override
	public String execute() throws Exception {
		ip=request.getServerName();
		filename=System.currentTimeMillis()+"emailalert.log.csv";
		CsvListWriter clw=new CsvListWriter(new FileWriter(new File(filename)), CsvPreference.STANDARD_PREFERENCE);
		PortfolioManager pm=ContextHolder.getPortfolioManager();
		List<Object[]> lists=pm.findBySQL("select id,name,updatemode,enddate,lasttransactiondate from "+Configuration.TABLE_PORTFOLIO+" where isoriginalportfolio=0 and state=1");
		List<String> headers=new ArrayList<String>();
		headers.add("ID");
		headers.add("Name");
		headers.add("Update mode");
		headers.add("End Date");
		headers.add("Last T Date");
		headers.add("Send Date");
		headers.add("Send Date");
		headers.add("Status");
		headers.add("Memo");
		clw.write(headers);
		for(int i=0;i<lists.size();i++){
			Object[] os=lists.get(i);
			List<String> line=new ArrayList<String>();
			for(Object o:os){
				if(o==null){
					line.add("");
				}else{
					line.add(getString(o));
				}
				
			}
			String memo="";
			List<Object> alerts=pm.findBySQL("select lastsentdate from "+Configuration.TABLE_EMAILNOTIFICATION+" where portfolioid="+os[0]+" order by lastsentdate asc");
			if(alerts!=null&&alerts.size()>0){
				line.add(getString(alerts.get(0)));
				line.add(getString(alerts.get(alerts.size()-1)));
				Date d=(Date) alerts.get(0);
				Date t=(Date) os[4];
				if(t!=null&&d.before(t)){
					memo="*";
				}
			}else{
				line.add("NA");
				line.add("NA");
			}
			List<Object> state=pm.findBySQL("select state from "+Configuration.TABLE_PORTFOLIO_STATE+" where portfolioid="+os[0]);
			if(state!=null&&state.size()>0){
				Integer st=(Integer) state.get(0);
				if(st==Configuration.PORTFOLIO_RUNNING_STATE_FINISHED){
					line.add("Executed");
				}else if(st==Configuration.PORTFOLIO_RUUNING_STATE_DAILYEXECUTION_FINISHED){
					line.add("Updated");
				}else{
					line.add("ERROR");
				}
			}
			line.add(memo);
			clw.write(line);
		}
		clw.close();
		return "emailalertlist.ftl";
	}

	private String ip;
	
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

}
