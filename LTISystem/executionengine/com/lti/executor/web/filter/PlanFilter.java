package com.lti.executor.web.filter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.supercsv.io.CsvListReader;
import org.supercsv.prefs.CsvPreference;

import com.lti.executor.web.PortfoliosFilter;
import com.lti.service.PortfolioManager;
import com.lti.service.bo.Portfolio;
import com.lti.system.ContextHolder;
import com.lti.util.LTIDate;
import com.lti.util.LTIDownLoader;

public class PlanFilter implements PortfoliosFilter {

	private long[] ids;
	
	private List<Portfolio> portfolios=new ArrayList<Portfolio>();
	
	private Date endDate = null;
	
	private Integer dateMode = 0;
	
	@Override
	public List<Portfolio> getPortfolios(boolean forceMonitor) {
		PortfolioManager pm=ContextHolder.getPortfolioManager();
		if(ids== null || ids.length == 0){
			LTIDownLoader ltiDownLoader = new LTIDownLoader();
			CsvListReader clr;
			try {
				clr = new CsvListReader(new FileReader(new File(ltiDownLoader.systemPath + "Batch_PlanID.csv")), CsvPreference.STANDARD_PREFERENCE);
				List<String> line=clr.read();
				while(line!=null){
					if(line.size()==0)continue;
					String id=line.get(0);
					if(id.equals(""))continue;
					List<Portfolio> ps=pm.getSimplePortfolios(Long.parseLong(id),-1);
					if(ps != null && ps.size() !=0)
						portfolios.addAll(ps);
					line=clr.read();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			for(long id:ids){
				List<Portfolio> ps=pm.getSimplePortfolios(id,-1);
				if(ps!=null&&ps.size()!=0){
					portfolios.addAll(ps);
				}
			}
		}
		portfolios = FilterUtil.skipUpdated(portfolios, forceMonitor);
		if(endDate != null){
			portfolios = FilterUtil.skipByEndDate(portfolios, endDate, dateMode, forceMonitor);
		} 
		return portfolios;
	}

	@Override
	public void setParamters(Map parameters) {
		String _ids=((String[]) parameters.get("ids"))[0];
		if(!_ids.trim().equals("")){
			String[] _strs=_ids.split(",");
			ids=new long[_strs.length];
			for(int i=0;i<_strs.length;i++){
				ids[i]=Long.parseLong(_strs[i].trim());
			}
		}
		Object oDate = parameters.get("endDate");
		if(oDate != null){
			String _endDate = ((String[]) oDate)[0];
			endDate = LTIDate.parseStringToDate(_endDate);
		}
		Object oMode = parameters.get("dateMode");
		if(oMode != null){
			String _dateMode = ((String[])oMode)[0];
			dateMode = Integer.parseInt(_dateMode);
		}
	}


}
