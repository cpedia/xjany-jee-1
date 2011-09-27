package com.lti.action.admin.loaddata;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.action.Action;
import com.lti.action.admin.loaddata.ImportSecurityDataAction.DownloadThread;
import com.lti.jobscheduler.DailyUpdateControler;
import com.lti.service.DataManager;
import com.lti.service.SecurityManager;
import com.lti.service.bo.Security;
import com.lti.service.bo.SecurityDailyData;
import com.lti.system.ContextHolder;
import com.lti.util.LTIDate;
import com.lti.util.LTIDownLoader;
import com.lti.util.StringUtil;
import com.lti.util.UpLoadException;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class LoadSecurityDataAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(LoadSecurityDataAction.class);
	
	private Boolean diversified;
	
	private String seucrityNames;
	
	private Date startDate1;
	
	private Date endDate;
	
	private File upFile;

	public void validate(){
		
		if(upFile == null || upFile != null){
			
		}
		
		if(this.diversified==null){
			this.diversified=false;
		}
		if(this.seucrityNames==null||this.seucrityNames.equals("")){
			
			addFieldError("seucrityNames","Seucrity names is not validate!");
			
			return;
		}
		
		if(startDate1==null||endDate==null||endDate.before(startDate1)){
			
			addFieldError("startDate","Dates is not validate!");
			
			return;
		}
		
	}
	
	public class DownloadThread extends Thread{
		
		private Map session;
		private File upFile;	
		private LTIDownLoader downLoader;
		private boolean diversified;
		private String securityNames;
		private Date startDate1;		
		private Date endDate;
		
		public DownloadThread(Map session, LTIDownLoader downLoader, String securityNames,File uploadFile,boolean diversified,Date startDate1,Date endDate) {
			super();
			this.session = session;
			this.downLoader = downLoader;			
			this.upFile=uploadFile;
			this.diversified=diversified;
			this.securityNames=securityNames;
			this.startDate1=startDate1;
			this.endDate=endDate;
		}

		public void run(){
			if(DailyUpdateControler.isUpdating){
				addMessage("it's udpating daily data now, please try later",session);
				return;
			}
			try {
				DailyUpdateControler.isUpdating=true;
				LTIDownLoader.SESSION.set(session);
				SecurityManager securityManager = ContextHolder.getSecurityManager();
								
				this.downLoader.deleteFile(false);
				String[] names = this.securityNames.split(",");
				if(this.upFile != null)
				{
					String filePath = this.downLoader.systemPath;
					
					InputStream stream = new FileInputStream(upFile);				

					String fileName = filePath;
					if(this.downLoader.isLinux())
						fileName = fileName + "d/" + names[0];
					else
						fileName = fileName + "d\\" + names[0];

					OutputStream bos = new FileOutputStream(fileName);

					int bytesRead = 0;
					
					byte[] buffer = new byte[8192];
					while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
						bos.write(buffer, 0, bytesRead);
					}
					bos.close();
					stream.close();
					
					Security security = new Security();
					security = securityManager.get(names[0]);	
					
					if(security == null)
					{
						UpLoadException e = new UpLoadException("Error:No Such Security!");
						e.setErrorMsg("Error:No Such Security!");
						throw e;
					}
					

					Date date2 = LTIDate.getNewNYSETradingDay(startDate1, -1);
					
					this.downLoader.saveToFile(names[0], date2, endDate, "v",false);
					
					
					SecurityDailyData firstData1 = new SecurityDailyData();
					
					firstData1 = securityManager.getLatestDailydata(security.getID(), date2);	
					
					this.downLoader.upLoadFile("v",false,false);
					
					this.downLoader.upLoadFile("d",false,false);
					SecurityDailyData firstData2 = new SecurityDailyData();
					
					firstData2 = securityManager.getLatestDailydata(security.getID(), date2);				
					
					DataManager dataManager = (DataManager) ContextHolder.getInstance().getApplicationContext().getBean("dataManager");				
					dataManager.UpdateAdjClose(names[0],date2,firstData2,firstData1);
					
					if(security.getSecurityType()!=null)
					{
						if(security.getSecurityType().equals((Integer)2))
						{
							this.downLoader.saveCEFToFile(names[0], date2, endDate, "n",false);
							
							this.downLoader.uploadNAV(false);
							
							dataManager.UpdateAdjNAV(names[0],endDate);
						}
					}
					
				}
				
				else if(this.upFile == null)
				{									
					List<Date> endDateList = new ArrayList<Date>();
					
					List<SecurityDailyData> firstDataList = new ArrayList<SecurityDailyData>();
					
					List<Date> navEndDateList = new ArrayList<Date>();
					
					List<String> CEF = new ArrayList<String>();
					
					List<String> symbols = new ArrayList<String>(); 
					
					List<Date> CEFstartDate = new ArrayList<Date>();
					List<Date> CEFendDate = new ArrayList<Date>();
					
					List<SecurityDailyData> staticDailyDataList = new ArrayList<SecurityDailyData>();
					
					Date date2 = LTIDate.getNewNYSETradingDay(startDate1, -1);
					
					for(int i=0;i<names.length;i++){
						
						Security se = securityManager.get(names[i]);
						
						if(se == null)
						{
							UpLoadException e = new UpLoadException("Error:No Such Security!");
							e.setErrorMsg("Error:No Such Security!");
							throw e;
						}
						
						this.downLoader.saveToFile(names[i], date2, endDate, "d",false);
						this.downLoader.saveToFile(names[i], date2, endDate, "v",false);
						
						Security security = new Security();
						security = securityManager.get(names[i]);	
						
						if(security == null)continue;
		
						symbols.add(names[i]);
											
						SecurityDailyData staticData = new SecurityDailyData();				
						
						staticData = securityManager.getLatestDailydata(security.getID(), date2);	
											
						endDateList.add(date2);
						
						staticDailyDataList.add(staticData);
						
						if(security.getSecurityType()!=null)
						{
							if(security.getSecurityType().equals((Integer)2))
							{
								CEF.add(security.getSymbol());
								CEFstartDate.add(startDate1);
								CEFendDate.add(endDate);

								navEndDateList.add(date2);						
							}
						}
						
					}
					
					this.downLoader.upLoadFile("v",false,false);
					
					this.downLoader.upLoadFile("d",false,false);
					
					for(int i=0;i<names.length;i++)
					{
						Security security = new Security();
						security = securityManager.get(names[i]);	
						
						if(security == null)continue;
						

						SecurityDailyData firstData = new SecurityDailyData();
						
						
						firstData = securityManager.getLatestDailydata(security.getID(), date2);	
						
						firstDataList.add(firstData);
					}
					
					this.downLoader.updateAdjust(symbols, endDateList, firstDataList, staticDailyDataList);
					
					for(int i = 0;i<CEF.size();i++)
					{
						this.downLoader.saveCEFToFile(CEF.get(i), CEFstartDate.get(i), CEFendDate.get(i), "n",false);
						Thread.sleep(500);
					}
					this.downLoader.uploadNAV(false);
					
					this.downLoader.updateAdjNAV(CEF, navEndDateList);
											
					System.out.println("Load Success");
				}
				
			
				
			} catch (Exception e) {
				addMessage(StringUtil.getStackTraceString(e),session);
				e.printStackTrace();
			}finally{

				if(upFile!=null)upFile.delete();
				DailyUpdateControler.isUpdating=false;
			}
		}
	}
	
	public static void addMessage(String s,Map session){
		if(session==null)return;
		synchronized(session){
			List<String> list=(List<String>)session.get("LoadSecurityDataAction");
			if(list==null)list=new ArrayList<String>();
			list.add(s);
			session.put("LoadSecurityDataAction",list);
		}
	}
	
	public static String getMessage(Map session){
		if(session==null)return "No Message!";
		synchronized(session){
			List<String> list=(List<String>)session.get("LoadSecurityDataAction");
			if(list==null||list.size()==0)return "No Message!";
			StringBuffer sb=new StringBuffer();
			for(int i=0;i<list.size();i++){
				sb.append(list.get(i));
				sb.append("\n"); 
			}
			session.remove("LoadSecurityDataAction");
			return sb.toString();
		}
	}

	public String execute() throws Exception {
		
		String[] names = this.seucrityNames.split(",");	
		
		if(names==null||names.length==0){
			//add message
			return Action.ERROR;
		}
				
		LTIDownLoader dl = new LTIDownLoader();
		
		DownloadThread dr=new DownloadThread(ActionContext.getContext().getSession(), dl, this.seucrityNames, this.upFile,this.diversified,this.startDate1,this.endDate);
		dr.start();
				
		return Action.SUCCESS;
	}

	public Boolean getDiversified() {
		return diversified;
	}

	public void setDiversified(Boolean diversified) {
		this.diversified = diversified;
	}

	public String getSeucrityNames() {
		return seucrityNames;
	}

	public void setSeucrityNames(String seucrityNames) {
		this.seucrityNames = seucrityNames;
	}

	public Date getStartDate1() {
		return startDate1;
	}

	public void setStartDate1(Date startDate1) {
		this.startDate1 = startDate1;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public File getUpFile(){
		return upFile;
	}
	
	public void setUpFile(File upFile){
		this.upFile = upFile;
	}

}
