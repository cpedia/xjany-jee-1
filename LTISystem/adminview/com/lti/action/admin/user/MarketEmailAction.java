package com.lti.action.admin.user;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.internet.MimeMessage;

import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.supercsv.io.CsvListWriter;
import org.supercsv.io.ICsvListWriter;
import org.supercsv.prefs.CsvPreference;

import com.lti.action.Action;
import com.lti.service.UserManager;
import com.lti.service.bo.UserMarket;
import com.lti.system.Configuration;
import com.lti.util.CSVEncoder; 
import com.lti.util.EmailValidChecker;
import com.lti.util.IPUtil;
import com.lti.util.LTIDownLoader;
import com.lti.util.StringUtil;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 
 * @author Su Ping
 * 2010/05/25
 */
public class MarketEmailAction extends ActionSupport implements Action{
	
	public final static String EMAIL_HOST = "\"MyPlanIQ Newsletter\"<newsletter@myplaniq.com>";
	public final static String VAILDFI_EMAIL_HOST = "support@validfi.com";
	public final static String EMAIL_SUBJECT = "MyPlanIQ Newsletter";
	public final static String VAILDFI_EMAIL_SUBJECT = "VaildFi Information";
	
	private String findKey;
	private String findKeys;
	private String keyNames;
	private String judges;
	private String keyName;
	public String fileName;
	private boolean complete;
	private boolean more;
	public InputStream inputStream;
	private UserManager userManager;
	private List<String> MarketEmails;
	private List<String> strNames;
	private List<String> mutNames;
	private JavaMailSender sender;
	private String email;
	private File uploadFile;
	private String uploadFileFileName;
	private String emailContent;
	private FileSystemResource sendFilePath;
	private File CSVFileName;
	private String tosStr;
	private String subject;
	private String emailsender;
	private String emailvalid;
	private File sendFileName;
	private String resultString;
	private File CSVEmailFileName;
	private String date;
	private String logString;
	private String[] emailKeys;
	private List<String> Keys;
	
	public String[] getEmailKeys() {
		return emailKeys;
	}

	public void setEmailKeys(String[] emailKeys) {
		this.emailKeys = emailKeys;
	}

	public String getLogString() {
		return logString;
	}

	public void setLogString(String logString) {
		this.logString = logString;
	}
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	public String getResultString() {
		return resultString;
	}

	public void setResultString(String resultString) {
		this.resultString = resultString;
	}
	
	public File getCSVEmailFileName() {
		return CSVEmailFileName;
	}

	public void setCSVEmailFileName(File emailFileName) {
		CSVEmailFileName = emailFileName;
	}

	public File getSendFileName() {
		return sendFileName;
	}

	public void setSendFileName(File sendFileName) {
		this.sendFileName = sendFileName;
	}

	public String getTosStr() {
		return tosStr;
	}

	public void setTosStr(String tosStr) {
		this.tosStr = tosStr;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public File getCSVFileName() {
		return CSVFileName;
	}

	public void setCSVFileName(File fileName) {
		CSVFileName = fileName;
	}

	public List<String> getStrNames() {
		return strNames;
	}

	public void setStrNames(List<String> strNames) {
		this.strNames = strNames;
	}

	public List<String> getMutNames() {
		return mutNames;
	}

	public void setMutNames(List<String> mutNames) {
		this.mutNames = mutNames;
	}
	
	public FileSystemResource getSendFilePath() {
		return sendFilePath;
	}

	public void setSendFilePath(FileSystemResource sendFilePath) {
		this.sendFilePath = sendFilePath;
	}

	public String getEmailContent() {
		return emailContent;
	}

	public void setEmailContent(String emailContent) {
		this.emailContent = emailContent;
	}

	public File getUploadFile() {
		return uploadFile;
	}

	public void setUploadFile(File uploadFile) {
		this.uploadFile = uploadFile;
	}

	public String getUploadFileFileName() {
		return uploadFileFileName;
	}

	public void setUploadFileFileName(String uploadFileFileName) {
		this.uploadFileFileName = uploadFileFileName;
	}
	
	public JavaMailSender getSender() {
		return sender;
	}

	public void setSender(JavaMailSender sender) {
		this.sender = sender;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<String> getMarketEmails() {
		return MarketEmails;
	}

	public void setMarketEmails(List<String> marketEmails) {
		MarketEmails = marketEmails;
	}

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}
	
	public String getFindKey() {
		return findKey;
	}

	public void setFindKey(String findKey) {
		this.findKey = findKey;
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}
	
	public boolean isMore() {
		return more;
	}

	public void setMore(boolean more) {
		this.more = more;
	}
	
	public String getFindKeys() {
		return findKeys;
	}

	public void setFindKeys(String findKeys) {
		this.findKeys = findKeys;
	}

	public String getKeyNames() {
		return keyNames;
	}

	public void setKeyNames(String keyNames) {
		this.keyNames = keyNames;
	}
	
	public String getJudges() {
		return judges;
	}

	public void setJudges(String judges) {
		this.judges = judges;
	}
	
	public UserManager getUserManager() {
		return userManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}
	
	public InputStream getInputStream() {
		return inputStream;
	}
	
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	
	/**
	 * Send the select choose to the market_mainPage.uftl
	 * @return
	 */
	public String mainPage(){
		List<String> list = new ArrayList<String>();
		List<String> sist = new ArrayList<String>();
		List<String> key = new ArrayList<String>();
		list.add("FirstName");
		list.add("LastName");
		list.add("Telephone");
		list.add("Address");
		list.add("Company");
		list.add("Email");
		list.add("State");
		list.add("Country");
		list.add("City");
		list.add("Group Key");
		list.add("Get All");
		strNames =list;
		sist.addAll(list);
		sist.remove(9);
		sist.remove(9);
		sist.add("GroupKey_1");
		sist.add("GroupKey_2");
		sist.add("GroupKey_3");
		sist.add("GroupKey_4");
		sist.add("GroupKey_5");
		mutNames=sist;
		key = userManager.getEmailsGroupKeys();
		Keys = key;
		return Action.SUCCESS;
	}
	
	/**
	 * Mapping the select list of the market_mainPage.uftl
	 * @param string
	 * @return
	 */
	public String strMapping(String string){
		if(string.equals("FirstName"))
			string = "userFirstname";
		if(string.equals("LastName"))
			string = "userLastname";
		if(string.equals("Telephone"))
		    string = "userTelephone";
		if(string.equals("Address"))
			string = "userAddress";
		if(string.equals("Company"))
			string = "userCompany";
		if(string.equals("Email"))
			string = "userEmail";
		if(string.equals("State"))
			string = "addressState";
		if(string.equals("Country"))
			string = "addressCountry";
		if(string.equals("City"))
			string = "addressCity";
		if(string.equals("Group Key"))
			string = "groupKey";
		if(string.equals("GroupKey_1"))
			string = "groupKey1";
		if(string.equals("GroupKey_2"))
			string = "groupKey2";
		if(string.equals("GroupKey_3"))
			string = "groupKey3";
		if(string.equals("GroupKey_4"))
			string = "groupKey4";
		if(string.equals("GroupKey_5"))
			string = "groupKey5";
		if(string.equals("Get All"))
			string = "userEmail";
		return string;
	}
	
	/**
	 * Get the search rules for the market email users
	 * @return
	 * @throws Exception
	 */
	public String OutputMarketEmail() throws Exception{
		List<UserMarket> uslist = new ArrayList<UserMarket>();
		List<UserMarket> ts = new ArrayList<UserMarket>();
		if(!more){
			keyName = this.strMapping(keyName);
			if(keyName.equals("groupKey")){
				String kn = keyName;
				for(int i=1;i<=5;i++){
					keyName=keyName+i;
					ts = userManager.getMarketEmailbyProperty(keyName, findKey, false);//simple rule for search//Group Key
					if(ts!=null && ts.size()>0)
						uslist.addAll(ts);
					keyName = kn;
				}
			}
			else{
				uslist = userManager.getMarketEmailbyProperty(keyName, findKey, false);//simple rule for search
			}
		}
		else{
			String[] KeyNames = keyNames.split(",");
			String[] FindKeys = findKeys.split(",");
			String[] Judges = judges.split(",");
			for(int i=0;i<KeyNames.length;i++)
				KeyNames[i] = this.strMapping(KeyNames[i]);
			uslist = userManager.getMEmailsbyProperty(KeyNames, FindKeys, Judges);//Muti Rules for search
		}
		if(complete)
			createFile(uslist,true);
		else
			createFile(uslist,false);
		return Action.INPUT;
	}
	
	/**
	 * Create the CSV file
	 * @param usList
	 * @param complete
	 * @throws Exception
	 */
	public void createFile(List<UserMarket> usList,boolean complete)throws Exception{	
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
		fileName = "MarketEmailInfos"+df.format(date)+".csv";
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		OutputStreamWriter osw = new OutputStreamWriter(bos);
		if(complete){
			createCSVContent(usList,osw);
			bos.flush();
			inputStream = new ByteArrayInputStream(bos.toByteArray());
			osw.close();
			bos.close();
		}
		else{
			osw.write("Email,FirstName,LastName\r\n");
			if(usList!=null){
				for(int i=0;i<usList.size();i++)
					osw.write(usList.get(i).getUserEmail()+","+usList.get(i).getUserFirstname()+","+usList.get(i).getUserLastname()+",\r\n");
			}
			osw.close();
			inputStream = new ByteArrayInputStream(bos.toByteArray());
			bos.close();
		}
	}
	
	/**
	 * Transform the list for CSV itmes
	 * @param usList
	 * @param os
	 */
	public static void createCSVContent(List<UserMarket> usList,OutputStreamWriter os){
		try{
			ICsvListWriter clw = new CsvListWriter(os, CsvPreference.STANDARD_PREFERENCE);
			if(usList!=null){
				//List<String> headers = com.lti.util.CSVEncoder.getHeaders(usList.get(0).getClass());
				String[] header = {"userEmail","userFirstname","userLastname","userCompany","userAddress","userTelephone","addressCountry","addressState","charcode","groupKey1","groupKey2","groupKey3","groupKey4","groupKey5","isSend"};
				List<String> headers = Arrays.asList(header);
				if(headers!=null)clw.write(headers);
				for (int i = 0; i < usList.size(); i++) {
					UserMarket userMarket = (UserMarket)usList.get(i);
					//List<String> row = com.lti.util.CSVEncoder.getRow(userMarket);
					List<String> row = getRow(userMarket,headers);
					if(row!=null)
						clw.write(row);
				}
			}
			clw.close();
		}catch(Exception e) {
			e.printStackTrace();
		}		
	}
	
	/**
	 * Write the CSV each row
	 * @param obj
	 * @param headers
	 * @return
	 */
	public static List<String> getRow(Object obj,List<String> headers) {
		if (obj == null)
			return null;
		List<String> row=new ArrayList<String>();
		if (com.lti.util.CSVEncoder.isBasicType(obj)) {
			row.add(com.lti.util.CSVEncoder.getValue(obj));
		} else {
			BeanInfo sourceBean = null;
			PropertyDescriptor[] propertyDescriptors = null;
			try {
				sourceBean = Introspector.getBeanInfo(obj.getClass());
				propertyDescriptors = sourceBean.getPropertyDescriptors();
				if (propertyDescriptors == null || propertyDescriptors.length == 0) {
					return null;
				}
			} catch (Exception e2) {
				return null;
			}
			for(int j=0;j<headers.size();j++){
				String header = headers.get(j);
				for (int i = 0; i < propertyDescriptors.length; i++) {
					try {
						PropertyDescriptor pro = propertyDescriptors[i];
						if(!header.equals(pro.getName()))
							continue;
						if (!com.lti.util.CSVEncoder.isBasicType(pro.getPropertyType().getName()))
							continue;
						if (pro.getWriteMethod() == null)
							continue;
						Method rm = pro.getReadMethod();
						if (rm == null)
							continue;
						if (!Modifier.isPublic(rm.getDeclaringClass().getModifiers())) {
							rm.setAccessible(true);
						}
						Object value = rm.invoke(obj, new Object[0]);
						if (value != null) {
							row.add(com.lti.util.CSVEncoder.getValue(value));
						}else{
							row.add("");
						}
					} catch (Exception e) {}
				}
			}
		}
		return row;
	}
	
	/**
	 * Input CSV file into the database
	 * @return
	 */
	public String InputCSVFile(){
		String CSVfile = null;
		boolean hasME = true;
		CSVfile = UploadFile(CSVFileName);
		Date logDate = new Date();
		StringBuffer sb = new StringBuffer();
		StringBuffer loginfo = new StringBuffer();
		try{
			File f = new File(CSVfile);
			BufferedReader br = new BufferedReader(new FileReader(f)); 
			String stemp; 
			while((stemp=br.readLine())!=null){
				sb.append(stemp+"\r\n");
			}
			br.close();
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(IOException e){}
		List<UserMarket>  umlist = com.lti.util.CSVDecoder.CSVToList(sb.toString(), UserMarket.class);
		List<UserMarket>  ualist = new ArrayList<UserMarket>();
		Configuration.writeMarketEmailLog(logDate, "Start to input market users' email CSV file.");
		loginfo.append("Start to input market users' email CSV file.<br>");
		for(int i=0;i<umlist.size();i++){
			String email = umlist.get(i).getUserEmail();
			if(email==null||email.trim().equals(""))continue;
			hasME = userManager.hasMarketEmail(email);
			if(hasME){
				Date logdate= new Date();
				Configuration.writeMarketEmailLog(logdate,"Email address of Market User[" + email + "] is existed in the database.Update this user's info.");
				loginfo.append("Email address of Market User[" + email + "] is existed in the database.Update this user's info.<br>");
				UserMarket usm = umlist.get(i);
				usm.setUserEmail(email);
				//usm.setIsSend(true);
				userManager.saveMarketEmail(usm);
			}else{
				UserMarket uss = umlist.get(i);
				//uss.setIsSend(true);
				if(ualist.size()<1){
					ualist.add(uss);
				}else{
					if(!isSend(ualist,uss.getUserEmail()))
						ualist.add(uss);
				}	
			}
		}
		if(ualist!=null&&ualist.size()>0)
			userManager.saveMarkerEmails(ualist);
		Date endDate = new Date();
		Configuration.writeMarketEmailLog(endDate,"Finish inputing market users' email CSV file.");
		loginfo.append("Finish inputing market users' email CSV file.<br>");
		logString = loginfo.toString();
		return Action.OUTPUT;
	}
	
	public String getEmails(){
		List<UserMarket> uslist = new ArrayList<UserMarket>();
		List<String> mes = new ArrayList<String>();
		String[] KeyNames = keyNames.split(",");
		String[] FindKeys = findKeys.split(",");
		String[] Judges = judges.split(",");
		if(!more)
			uslist = userManager.getMarketEmailbyProperty(keyName, findKey, false);
		else
			uslist = userManager.getMEmailsbyProperty(KeyNames, FindKeys, Judges);
		Iterator ius = uslist.iterator();
		while(ius.hasNext()){
			UserMarket um = (UserMarket)ius.next();
			mes.add(um.getUserEmail());
		}
		MarketEmails = mes;
		return Action.SUCCESS;
	}
	
	private Integer times;
	
	
	
	public Integer getTimes() {
		return times;
	}

	public void setTimes(Integer times) {
		this.times = times;
	}

	/**
	 * Send the emails
	 * @return
	 * @throws Exception
	 */
	public String SendMarketEmails() throws Exception{
		if(senderThread!=null&&senderThread.isAlive()){
			resultString="Failed";
			logString="There is a sending thread already.";
			return Action.OUTPUT;
		}
		
		String[] tos = null;
		String subStr = null;
		String sendStr = null;
		String sendfile = null;
		String emailfile = null;
		String[] emailKey = null; 
		boolean success = true;
		StringBuffer info=new StringBuffer();
		StringBuffer eminfo = new StringBuffer();
		StringBuffer loginfo = new StringBuffer();
		if(subject!=null && !subject.trim().equals(""))
			subStr = subject;
		else 
			subStr = EMAIL_SUBJECT;
		if(emailsender!=null && !emailsender.trim().equals(""))
			sendStr = emailsender;
		else
			sendStr = EMAIL_HOST;
		if(emailKeys!=null&&emailKeys.length>0)
			emailKey = userManager.getMarketEmailsByKey(emailKeys);
		else
			emailKey = null;
		if(CSVEmailFileName!=null&&!CSVEmailFileName.toString().equals("null")){
			emailfile = UploadFile(CSVEmailFileName);
			try{
				File file = new File(emailfile);
				BufferedReader br = new BufferedReader(new FileReader(file)); 
				String stemp; 
				while((stemp=br.readLine())!=null){
					eminfo.append(findEmail(stemp)+";");
				}
				br.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		if(tosStr==null ||tosStr.trim().equals("")){
			if(emailKey==null || emailKey.length==0)
				tos = eminfo.toString().split(";");
			else
				tos = emailKey;
		}
		else
			tos = tosStr.split(";");
		if(sendFileName!=null&&!sendFileName.toString().equals("null")){
			sendfile = UploadFile(sendFileName);
			try{
				File file = new File(sendfile);
				BufferedReader br = new BufferedReader(new FileReader(file)); 
				String stemp; 
				while((stemp=br.readLine())!=null){
					info.append(stemp);
				}
				br.close();
			}catch(FileNotFoundException e){
				success = false;
				e.printStackTrace();
			}catch(IOException e){success = false;}
		}
		
		
		int interval=(int) (60*60*1000/times*0.9);
		senderThread=new EMailSenderThread(sendStr, tos, interval, subStr, emailContent, info.toString());
		senderThread.start();
		
		
		if(success)resultString = "three";
		else resultString = "one";
		logString = loginfo.toString();
		return Action.OUTPUT;
	}
	
	
	public String sendinglog(){
		if(senderThread!=null&&senderThread.isAlive()){
			resultString="Sending status:";
			logString=senderThread.getPercent()*100+"%<br>\r\n"+senderThread.getLogString();
		}else{
			resultString="No running.";
			logString="No running.";
		}
		
		return Action.OUTPUT;
	}
	
	public String stopsending(){
		if(senderThread!=null&&senderThread.isAlive()){
			resultString="Stop thread";
			senderThread.setStop(true);
			logString="ok, please wait for some secs.";
		}else{
			resultString="No running.";
			logString="No running.";
		}
		
		return Action.OUTPUT;
	}
	
	private static EMailSenderThread senderThread;
	
	public boolean isSend(List<UserMarket> list,String email){
		boolean flag=false;
		for(int i=0;i<list.size();i++)
			if(email.equalsIgnoreCase(list.get(i).getUserEmail())){
				flag=true;
				break;
			}
		return flag;
	}
	
	public boolean sendLastTime(String subject,UserMarket userM){
		String DBSubject = userM.getSubject();
		if(DBSubject==null || DBSubject.trim().equals(""))
			return false;
		else if(DBSubject.equals(subject))
			return true;
		else 
			return false;
	}
	
	public String findEmail(String strline) {
		String strs[] = strline.split(",");
		String regx = "^(([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5}){1,25})+([;.](([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5}){1,25})+)*$";
		Pattern p = Pattern.compile(regx);
		String uemail = null;
		for(int i=0;i<strs.length;i++){
			Matcher m = p.matcher(strs[i]);
			if (m.find()){
				uemail = strs[i];
				break;
			}
		}
		return uemail;
	}
	
	
	
	public String LoadFailEmailsFile() throws Exception{
		String systemPath;
		Date today = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		fileName = sdf.format(today)+"_FailEmailList.csv";
		String sysPath = System.getenv("windir");
		if(!Configuration.isLinux())systemPath=sysPath+"\\temp\\";
		else systemPath="/var/tmp/";
		String path = systemPath+"FailEmails.csv";	
		File f = new File(path);
		if(f.exists()){
			inputStream = new FileInputStream(f);
		}
		return Action.INPUT;
	}
	
	public String UploadFile(File fileName){
		LTIDownLoader dl = new LTIDownLoader();
		String filePath = dl.systemPath;
		String sendfile = null;
		try{
			InputStream stream = new FileInputStream(fileName);
			OutputStream bos = new FileOutputStream(filePath + fileName.getName());
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
				bos.write(buffer, 0, bytesRead);
			}
			bos.close();
			stream.close();
			sendfile = filePath + fileName.getName();
		}catch(FileNotFoundException e1){
			e1.printStackTrace();
		}catch(IOException e1){}
		return sendfile;
	}
	
	public String marketEmailLog() throws Exception{
		if(date==null||date.equals(" "))return Action.INPUT;
		String systemPath;
		Date today = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		String sysPath = System.getenv("windir");
		if(!Configuration.isLinux())systemPath=sysPath+"\\temp\\";
		else systemPath="/var/tmp/";
		String path = systemPath+this.getDate()+"MarketEmailLog.txt";	
		File f = new File(path);
		if(f.exists()){
			fileName = this.getDate()+"MarketEmailLog.txt";
			inputStream = new FileInputStream(f);
		}
		else{
			fileName = "NoMarketEmailLogNotice.txt";
			String path2 = systemPath+"NoMarketEmailLogNotice.txt";
			File f2 = new File(path2.toString());
			FileOutputStream fos = new FileOutputStream(path2,true);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			osw.write("The date of download log:"+sdf.format(today)+"\t"+this.getDate()+"'s Market Email has not started operating today!There is no log!\t\n");	
			osw.close();
			fos.close();
			inputStream=new FileInputStream(f2);

		}
		return Action.INPUT;
	}
	
	
	public String emailValid(){
		if(emailvalid!=null){
			Map<Boolean,String> message = EmailValidChecker.checkEmail(emailvalid);
			if(message.containsKey(true))
				logString = "The email address is valid.\nMore Message:\n"+message.get(true); 
			else
				logString = "The email address is invalid.\nMore Message:\n"+message.get(false); 
		}
		return Action.MESSAGE;
	}

	public List<String> getKeys() {
		return Keys;
	}

	public void setKeys(List<String> keys) {
		Keys = keys;
	}

	public String getEmailsender() {
		return emailsender;
	}

	public void setEmailsender(String emailsender) {
		this.emailsender = emailsender;
	}
	
	public String getEmailvalid() {
		return emailvalid;
	}

	public void setEmailvalid(String emailvalid) {
		this.emailvalid = emailvalid;
	}
}
