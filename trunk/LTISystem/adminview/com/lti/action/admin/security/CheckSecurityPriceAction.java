/**
 * 
 */
package com.lti.action.admin.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.text.AbstractDocument.Content;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.supercsv.io.CsvListWriter;
import org.supercsv.prefs.CsvPreference;

import com.lti.action.Action;
import com.lti.jobscheduler.DailyUpdateControler;
import com.lti.service.bo.Security;
import com.lti.system.ContextHolder;
import com.lti.util.LTIDate;
import com.lti.util.LTIDownLoader;
import com.lti.util.StringUtil;
import com.lti.service.SecurityManager;

/**
 * @author ccd
 *
 */
public class CheckSecurityPriceAction implements Action{
	public String dateStr;
	public InputStream inputStream;
	public String fileName;
	public String update;
	@Override
	public String execute(){
		if(DailyUpdateControler.isUpdating){
			return Action.SUCCESS;
		}
		SecurityManager sm = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
		LTIDownLoader downLoader = new LTIDownLoader();
		List<Security> updateList = new ArrayList<Security>();
		CsvListWriter clw = null;
		String systemPath;
		String sysPath = System.getenv("windir");
		if(!downLoader.isLinux())
			systemPath=sysPath+"\\temp\\";
		else 
			systemPath="/var/tmp/";
		fileName = systemPath+"checkPrice_" + dateStr+ ".csv"; 
		Date today = LTIDate.clearHMSM(new Date());
		Date startDate = LTIDate.getDate(1900, 0, 0);
		Date lastDate = LTIDate.parseStringToDate(dateStr);
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Security.class);
		detachedCriteria.add(Restrictions.lt("SecurityType", 5));
		detachedCriteria.add(Restrictions.or(Restrictions.isNull("EndDate"), Restrictions.lt("EndDate", lastDate)));
		List<Security> securityList = sm.getSecurities(detachedCriteria);
		//Mode = 1 means yahoo is newer than system, we need to update
		//Mode = 2 means yahoo has stopped 
		String[] header = {"ID","Symbol","EndDate","YahooEndDate","Mode","IsMarked"};
		File file = new File(fileName);
		try {
			clw = new CsvListWriter(new FileWriter(file), CsvPreference.EXCEL_PREFERENCE);
			clw.writeHeader(header);
			if(securityList!=null){
				for(int i=0;i<securityList.size();++i){
					Security se = securityList.get(i);
					Date endDate = se.getEndDate();
					List<String> con = new ArrayList<String>();
					con.add(se.getID().toString());
					con.add(se.getSymbol());
					//get the enddate in yahoo and compare with it
					Date yahooEndDate = downLoader.getLastDailyDataDateFromYahooBySymbol(se.getSymbol(), startDate, today);
					if(endDate!=null)
						con.add(LTIDate.parseDateToString(endDate));
					else
						con.add("NULL");
					if(yahooEndDate!=null){
						con.add(LTIDate.parseDateToString(yahooEndDate));
						if(endDate!=null){
							if(LTIDate.before(endDate, yahooEndDate))
								con.add("need update");
							else if(LTIDate.before(yahooEndDate, lastDate)){
								if(LTIDate.calculateInterval(yahooEndDate, today)>15)
									con.add("Fund may be stopped(more than 15 days)");
								else
									con.add("Fund may be stopped");
							}
						}else{
							con.add("need update");
							updateList.add(se);
						}
					}else{
						con.add("NULL");
						con.add("Yahoo no data");
					}
					con.add(se.getIsClosed().toString());
					clw.write(con);
				}
			}
			clw.close();
			File f = new File(fileName);
			inputStream = new FileInputStream(f);
			if(update!=null && update.equalsIgnoreCase("Y"))
				downLoader.updateDailyDataByList(updateList);
		} catch (IOException e) {
			System.out.println(StringUtil.getStackTraceString(e));
		}finally{
			if(clw != null) try {clw.close();} catch (IOException e){}
		}
		return Action.SUCCESS;
	}
	public String getDateStr() {
		return dateStr;
	}
	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}
	public InputStream getInputStream() {
		return inputStream;
	}
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getUpdate() {
		return update;
	}
	public void setUpdate(String update) {
		this.update = update;
	}
}
