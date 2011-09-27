package com.lti.executor.web.filter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.supercsv.io.CsvListReader;
import org.supercsv.prefs.CsvPreference;

import com.lti.executor.web.PortfoliosFilter;
import com.lti.service.PortfolioManager;
import com.lti.service.bo.Portfolio;
import com.lti.system.ContextHolder;

public class CsvFilter implements PortfoliosFilter {

	private String filename;

	private List<Portfolio> portfolios = new ArrayList<Portfolio>();

	@Override
	public List<Portfolio> getPortfolios(boolean forceMonitor) {
		try {
			PortfolioManager portfolioManager=ContextHolder.getPortfolioManager();
			CsvListReader clr=new CsvListReader(new FileReader(new File(ContextHolder.getServletPath(),filename)), CsvPreference.STANDARD_PREFERENCE);
			List<String> line=clr.read();
			while(line!=null){
				if(line.size()==0)continue;
				String id=line.get(0);
				if(id.equals(""))continue;
				portfolios.add(portfolioManager.get(Long.parseLong(id)));
				line=clr.read();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return portfolios;
	}

	@Override
	public void setParamters(Map parameters) {
		filename = ((String[]) parameters.get("filename"))[0];
	}

}
