package com.lti.action.admin.user;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.lti.service.UserManager;
import com.lti.service.bo.UserMarket;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.util.IPUtil;
import com.lti.util.StringUtil;

public class EMailSenderThread extends Thread {
	private String[] tos;
	private int interval;
	private String subject;
	private String text;
	private String emailsender;
	private String info;
	
	
	public EMailSenderThread(String emailsender, String[] tos, int interval, String subject, String text,String info) {
		super();
		this.emailsender = emailsender;
		this.tos = tos;
		this.interval = interval;
		this.subject = subject;
		this.text = text;
		this.info = info;
	}
	
	
	
	
	private boolean stop=false;
	
	private double percent;
	
	private StringBuffer loginfo;
	
	
	public String getLogString() {
		return loginfo.toString();
	}


	public double getPercent() {
		return percent;
	}


	public boolean isStop() {
		return stop;
	}


	public void setStop(boolean stop) {
		this.stop = stop;
	}

	


	public void run(){
		UserManager userManager=ContextHolder.getUserManager();
		loginfo=new StringBuffer();
		if(tos!=null && tos.length>0){
			List<UserMarket> list = userManager.getMarketEmailbyProperty("isSend", true, true);
			List<String> failEmails = new ArrayList<String>();
			Date startDate = new Date();
			Configuration.writeMarketEmailLog(startDate,"Start sending market user eamils.");
			loginfo.append("Start sending market user eamils.<br>");
			for(int i=0;i<tos.length;i++){
				if(stop)break;
				percent=i*1.0/tos.length;
				String useremail = tos[i];
				Date logdate = new Date();
				List<UserMarket> urlist = userManager.getMarketEmailbyProperty("userEmail", useremail, true);
				UserMarket userM = new UserMarket();
				if(urlist!=null && urlist.size()>0)
					userM = urlist.get(0);
				if (StringUtil.checkEmail(useremail) == false){
					Configuration.writeMarketEmailLog(logdate,"Email address of Market User[" + useremail + "] is illegal.");
					loginfo.append("Email address of Market User[" + useremail + "] is illegal.<br>");
					continue;
				}
				if(!contains(list,useremail)){
					Configuration.writeMarketEmailLog(logdate,"Email address of Market User[" + useremail + "] is not existed or unsubscribed, stop sending emails.");
					loginfo.append("Email address of Market User[" + useremail + "] is not existed or unsubscribed, stop sending emails.<br>");
					continue;
				}
				if(sendLastTime(subject,userM)){
					Configuration.writeMarketEmailLog(logdate,"Email address of Market User[" + useremail + "] has been sent the same as letter last time, stop sending emails.");
					loginfo.append("Email address of Market User[" + useremail + "] has been sent the same as letter last time, stop sending emails.<br>");
					continue;
				}
				
				StringBuffer content=new StringBuffer();
				content.append(info);
				if(!text.equals("null"))
					content.append(text);
				content.append("<br>");
				content.append("<p>If you would prefer not to receive future newsletters from MyPlanIQ, please");
				String url="<a href='http://www.myplaniq.com/LTISystem/jsp/register/refuseMarketEmail.action?action=show&userEmail="+useremail+"'"+"><strong> Opt Out here</strong></a></p><br>";
				content.append(url);
				//tos[i]=MarketEmails.get(i);
				try{
					sendMail(useremail,subject,content.toString(),emailsender);
					Configuration.writeMarketEmailLog(logdate,"Sent eamil of Market User[" + useremail + "] sucessfully.");
					loginfo.append("Sent eamil of Market User[" + useremail + "] sucessfully.<br>");
					userM.setSubject(subject);
					//userM.setUserEmail(useremail);
					userManager.updateMarketEmail(userM);
					Configuration.writeMarketEmailLog(logdate,"Update subject of Market User[" + useremail + "] sucessfully.");
					loginfo.append("Update subject of Market User[" + useremail + "] sucessfully.<br>");
				}catch(Exception e){
					Configuration.writeMarketEmailLog(logdate,StringUtil.getStackTraceString(e));
					Configuration.writeMarketEmailLog(logdate,"Sent eamil of Market User[" + useremail + "] failed.");
					failEmails.add(useremail);
					loginfo.append("Sent eamil of Market User[" + useremail + "] failed.<br>");
					e.printStackTrace();
				}
				
				try {
					Thread.sleep(interval);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}//end for
			try {
				creatFailEmailFile(failEmails);
			} catch (Exception e) {
				e.printStackTrace();
			}
			Date enddate = new Date();
			Configuration.writeMarketEmailLog(enddate,"End sending market user eamils.");
			loginfo.append("End sending market user eamils.<br>");
			
			
		}//end if
	}
	
	public boolean contains(List<UserMarket> list,String email){
		for(int i=0;i<list.size();i++)
			if(email.trim().equalsIgnoreCase(list.get(i).getUserEmail().trim())){
				return true;
			}
		return false;
	}
	
	public void creatFailEmailFile(List<String> failEmails) throws Exception{
		String systemPath;
		String sysPath = System.getenv("windir");
		Date today = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		if(!Configuration.isLinux())systemPath=sysPath+"\\temp\\";
		else systemPath="/var/tmp/";
		String path = systemPath+"FailEmails.csv";	
		FileOutputStream fos = new FileOutputStream(path);
		OutputStreamWriter osw = new OutputStreamWriter(fos);
		osw.write("Fail Emails List. File Create Time : "+sdf.format(today)+"\t\n");
		if(failEmails!=null && failEmails.size()>0){
			for(int i=0;i<failEmails.size();i++){
				osw.write(failEmails.get(i)+"\t\n");
			}
		}
		else
			osw.write("Tnere is no fail email. All the emails have sent sucessfully.");
		osw.close();
		fos.close();
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
	private static boolean debug=true;
	private static DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static void sendMail(String to, String subject,String text,String emailsender) throws Exception {
		JavaMailSender sender = (JavaMailSender) com.lti.system.ContextHolder.getInstance().getApplicationContext().getBean("emsender");
		MimeMessage mailMessage = sender.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage, true, "utf-8");
		
		if(!IPUtil.getLocalIP().startsWith("70.38.112")){
			messageHelper.setTo(new String[]{"ee@foolishb.com","wyjfly@gmail.com","jany.wuhui@gmail.com"});
			messageHelper.setSubject("[Local Market EMail]"+subject);
			messageHelper.setText("to:"+to+"<br>\r\n"+text, true);
		}else{
			messageHelper.setTo(to);
			messageHelper.setSubject(subject);
			messageHelper.setText(text, true);
		}
		
		if(debug){
			System.out.println(df.format(new Date())+" "+to+", "+subject);
		}
		
		messageHelper.setFrom(emailsender);
		
		sender.send(mailMessage);
	}
	
}
