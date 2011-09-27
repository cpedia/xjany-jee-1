package com.lti.util;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.lti.listener.impl.SimulatorTransactionProcessor;
import com.lti.service.GroupManager;
import com.lti.service.PortfolioManager;
import com.lti.service.UserManager;
import com.lti.service.bo.EmailNotification;
import com.lti.service.bo.HoldingItem;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.PortfolioInf;
import com.lti.service.bo.Transaction;
import com.lti.service.bo.User;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;

import freemarker.template.Template;

public class EmailUtil {


	public static String generateItem(Portfolio portfolio,boolean isMPIQ,Date lastSentDate,Date currentDate,List<Transaction> transactions,List<Transaction> scheduletransactions,List<HoldingItem> holdingitems,List<HoldingItem> scheduleholdingitems,boolean sendVirtualMail) {
		
		try {
			freemarker.template.Configuration conf = new freemarker.template.Configuration();
			try {
				conf.setDirectoryForTemplateLoading(new File(EmailUtil.class.getResource(".").getFile()));
			} catch (IOException e) {
				ContextHolder.addException(e);
			}
			Template itemplate = conf.getTemplate("EmailItem.uftl");
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("isMPIQ", isMPIQ);
			data.put("portfolio", portfolio);
			data.put("transactions", transactions);
			data.put("scheduletransactions", scheduletransactions);
			data.put("holdingitems", holdingitems);
			data.put("scheduleholdingitems", scheduleholdingitems);
			data.put("previousDate", lastSentDate);
			data.put("currentDate", currentDate);
			data.put("sendVirtualMail", sendVirtualMail);
			StringWriter sw = new StringWriter();
			itemplate.process(data, sw);
			return sw.toString();
		} catch (Exception e) {
			ContextHolder.addException(e);
			//e.printStackTrace();
		}
		return null;
	}

	/**
	 * add a header/body/title to the eamil content.
	 * @param username
	 * @param text
	 * @return
	 */
	public static String generateEmailBody(boolean isMPIQ, String username, String text) {
		try {
			Template template = null;
			freemarker.template.Configuration conf = new freemarker.template.Configuration();
			try {
				conf.setDirectoryForTemplateLoading(new File(EmailUtil.class.getResource(".").getFile()));
			} catch (IOException e) {
				ContextHolder.addException(e);
			}
			template = conf.getTemplate("EmailAlert.uftl");
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("username", username);
			data.put("content", text);
			data.put("isMPIQ", isMPIQ);
			StringWriter sw = new StringWriter();
			template.process(data, sw);
			return sw.toString();
		} catch (Exception e) {
			ContextHolder.addException(e);
		}
		return null;
	}
	
	public static boolean Preview=false;
	
	/**
	 * this API is called by the outter code
	 */
	public static void sendEmails(boolean p) {
		Preview=p;
		if(!Configuration.getSendMail()){
			try {
				sendMail(new String[]{"support@myplaniq.com", "caixg@ltisystem.com","jany.wuhui@gmail.com","wyjfly@gmail.com"},"["+df.format(new Date())+"]Email Alert Summarize", "The EMail Alert has been disabled.");
			} catch (Exception e) {
				//e.printStackTrace();
			}
			return;
		}
		String info=null;
		try {
			info=_sendEmails();
		} catch (Throwable e) {
			info="<pre>"+StringUtil.getStackTraceString(e)+"</pre>";
			ContextHolder.addException(e);
		}
		info="<a href='http://www.myplaniq.com/LTISystem/jsp/ajax/DownloadFile.action?name=email.log&isImageCache=false'>logs</a><br>"+info;
		try {
			sendMail(new String[]{"support@myplaniq.com", "caixg@ltisystem.com", "jany.wuhui@gmail.com","wyjfly@gmail.com"},"["+df.format(new Date())+"]Email Alert Summarize", info);
		} catch (Exception e1) {
			ContextHolder.addException(e1);
		}
	}
	/**
	 * The implement of sending emails.
	 */
	private static String _sendEmails() {
		int portfolioSize=0;
		int userSize=0;
		int sendSuccSize=0;
		int sendErrSize=0;
		
		UserManager userManager = ContextHolder.getUserManager();
		PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();

		String sql = "select distinct(portfolioID),lastsentdate from " + Configuration.TABLE_EMAILNOTIFICATION;
		List<Object[]> portfolioIDs = null;
		try {
			portfolioIDs = userManager.findBySQL(sql);
		} catch (Exception e) {
			ContextHolder.addException(e);
		}
		if (portfolioIDs == null || portfolioIDs.size() == 0) {
			return "the table of email notification is empty, exit sending emails API.";
		}
		portfolioSize=portfolioIDs.size();

		Map<Long, String> contents = new HashMap<Long, String>();
		Map<Long, Boolean> isSAA = new HashMap<Long, Boolean>();
		Map<Long, Date> lastDates = new HashMap<Long, Date>();
		StringBuffer ignore_text=new StringBuffer();
		ignore_text.append("<pre>"); 
		for (int i = 0; i < portfolioIDs.size(); i++) {
			Long id = ((BigInteger) portfolioIDs.get(i)[0]).longValue();
			//Long id = 8233l;
			Portfolio p = portfolioManager.get(id);
			if (p == null) {
				//clean(id);
				continue;
			}
			
			try {
				if(p.getStrategies().getAssetAllocationStrategy().getID().longValue()==Configuration.STRATEGY_SAA_ID){
					isSAA.put(p.getID(), true);
				}
				//isSAA.put(p.getID(), false);
			} catch (Exception e) {
				isSAA.put(p.getID(), false);
			}
			
			Date lastSentDate = (Date) portfolioIDs.get(i)[1];
			if (lastSentDate == null) {
				continue;
			}
			boolean sendVirtualMail=false;
			List<Transaction> transactions = portfolioManager.getTransactionsAfter(id, lastSentDate);//real
			try{
				if(p.getStrategies().getAssetAllocationStrategy().getID().longValue()==Configuration.STRATEGY_SAA_ID||p.getStrategies().getAssetAllocationStrategy().getID().longValue()==Configuration.STRATEGY_TAA_ID){
					if(transactions!=null&&transactions.size()!=0){
						ignore_text.append("[").append(p.getID()).append("]");
						ignore_text.append(p.getName()).append("\n");
						for(Transaction t:transactions){
							ignore_text.append(df.format(t.getDate()));
							ignore_text.append("\t");
							ignore_text.append(t.getSymbol());
							ignore_text.append("\t");
							ignore_text.append(t.getShare());
							ignore_text.append("\n");
						}
						ignore_text.append("\n\n");
						transactions=null;
						
					}
				}
			}catch(Exception e){
				
			}
			
			PortfolioInf pinf= portfolioManager.getPortfolioInf(id, Configuration.PORTFOLIO_HOLDING_CURRENT);
			List<HoldingItem> holdingitems = null;
			if(pinf!=null&&pinf.getHolding()!=null){
				holdingitems = pinf.getHolding().getHoldingItems();
			}
		
			
			
			List<Transaction> scheduletransactions = null;
			List<HoldingItem> scheduleholdingitems = null;
			PortfolioInf epinf=portfolioManager.getPortfolioInf(id, Configuration.PORTFOLIO_HOLDING_EXPECTED);
			if(epinf!=null&&epinf.getHolding()!=null&&epinf.getHolding().getTransactionProcessor() instanceof SimulatorTransactionProcessor){
				SimulatorTransactionProcessor stp=(SimulatorTransactionProcessor) epinf.getHolding().getTransactionProcessor();
				if(stp.getScheduleTransactions()!=null&&stp.getScheduleTransactions().size()>0&&stp.getScheduleTransactions().get(0).getDate().after(lastSentDate)){
					scheduletransactions=stp.getScheduleTransactions();
					scheduleholdingitems=epinf.getHolding().getHoldingItems();
				}
			}
			
			boolean sempty = false;
			if (scheduletransactions == null || scheduletransactions.size() == 0) {
				sempty = true;
			}
			
			if(sempty&&(transactions==null||transactions.size()==0)){
				transactions = portfolioManager.getTransactionsAfter(id, Configuration.TRANSACTION_TYPE_MAIL, lastSentDate);
				if(transactions!=null&&transactions.size()>0){
					sendVirtualMail=true;
				}
			}
			
			boolean tempty = false;
			if (transactions == null || transactions.size() == 0) {
				tempty = true;
			}
			
			
			if(tempty&&sempty){
				continue;
			}
            
			
			
			Date lastDate=null;
			if(!tempty){
				lastDate=transactions.get(transactions.size() - 1).getDate();
			}else{
				lastDate=scheduletransactions.get(scheduletransactions.size() - 1).getDate();
			}
			if(!sempty){
				Date tmpdate=scheduletransactions.get(scheduletransactions.size() - 1).getDate();
				if(tmpdate.after(lastDate))lastDate=tmpdate;
			}
			
			SecurityUtil.usedescription(p, transactions, false);
			SecurityUtil.usedescription(p, scheduletransactions, true);
			
			SecurityUtil.usedescription(p, holdingitems);
			SecurityUtil.usedescription(p, scheduleholdingitems);
			
			//Transaction t = new Transaction();
			String text = generateItem(p,true, lastSentDate, new Date(), transactions, scheduletransactions, holdingitems, scheduleholdingitems,sendVirtualMail);

			if (text == null)
				continue;

			contents.put(id, text);

			lastDates.put(id, lastDate);
			//log.info("Portfolio[" + id + "] done.");
		}
		ignore_text.append("</pre>");
		try {
			sendMail(new String[]{"wyjfly@gmail.com"}, "["+df.format(new Date())+"][IGNORE][COMPLETE]",ignore_text.toString());
		} catch (Exception e1) {
		}
		
		//log.info("End generating transactions contents.");

		//log.info("Start to send emails.");
		List<Long> userIDs = userManager.getUsersFromEN();
		StringBuffer allEmails=new StringBuffer();
		userSize=userIDs.size();
		for (int i = 0; i < userIDs.size(); i++) {
			StringBuffer sb = new StringBuffer();
			Long userID = userIDs.get(i);
			User user = userManager.get(userID);
			if(user==null)continue;
			String username = user.getUserName();
			boolean[] permission = VF_MPIQChecker(userID);
			boolean isSubscred = permission[0];
			boolean isMPIQ = permission[1];
			//TODO:??
			isMPIQ=true;
			//if(!isSubscred) {
				//log.info(username+"[" + userID + "] is not a subscriber.");
			//	continue;
			//}
			//boolean isMPIQ = MPIQChecker(userID);
			List<EmailNotification> userENs = userManager.getEmailNotificationsByUser(userID);
			for (int j = 0; j < userENs.size(); j++) {
				EmailNotification en = userENs.get(j);
				String content=null;
				content = contents.get(en.getPortfolioID());
				Boolean _is_raa=isSAA.get(en.getPortfolioID());
				if(_is_raa==null)_is_raa=false;
				if (content != null && !content.equals("") &&(isSubscred||_is_raa))
					sb.append(content);
				
			}
			
			
			if (sb.length() != 0) {
				if (user.getEMail() != null && !user.getEMail().equals("")) {
					String to = user.getEMail();
					if (StringUtil.checkEmail(to) == false){
						//log.info("Email address of User[" + username + "] is illegal.");
						continue;
					}
					
					String text = generateEmailBody(isMPIQ, username, sb.toString());
					
					
					try {
						if (text != null) {
							sendMail(to, text,isMPIQ);
							allEmails.append(text);
							sendSuccSize++;
						}
						
					} catch (Exception e) {
						ContextHolder.addException(e);
						sendErrSize++;
					}
				}
			} else {
			}
		}
		
		Iterator<Long> id_iter = lastDates.keySet().iterator();
		while (id_iter.hasNext()) {
			Long id = id_iter.next();
			try {
				if(!Preview)userManager.updateEmailLastSentDate(id, lastDates.get(id));
				System.out.println("Update the last sent date of portfolio[" + id + "] to "+lastDates.get(id)+".");
			} catch (Exception e) {
				ContextHolder.addException(e);
			}
		}
		StringBuffer info=new StringBuffer();
		info.append("Portfolio size: ");
		info.append(portfolioSize);
		info.append("<br>");
		info.append("User size: ");
		info.append(userSize);
		info.append("<br>");
		info.append("The number of eamils sent successfully: ");
		info.append(sendSuccSize);
		info.append("<br>");
		info.append("The number of eamils sent un-successfully: ");
		info.append(sendErrSize);
		info.append("<br>");
		info.append("<br>");
		info.append("<br>");
		info.append(allEmails);
		return info.toString();
	}
	public static DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
	public static void sendMail(String to, String text,boolean isMPIQ) throws Exception{
		String datestr=df.format(new Date());
		if(isMPIQ)
			sendMail(new String[]{to}, "[" + datestr + "]" + Configuration.MPIQ_EMAIL_SUBJECT,text);
		else
			sendMail(new String[]{to}, "[" + datestr + "]" + Configuration.EMAIL_SUBJECT,text);
	}
	public static void sendMail(String[] tos, String subject,String text) throws Exception {
		sendAttachment(tos, subject, text, null, null);
	}
	public static void sendAttachment(String[] tos, String subject,String text,String filename,File file) throws Exception {
		JavaMailSender sender = (JavaMailSender) com.lti.system.ContextHolder.getInstance().getApplicationContext().getBean("sender");
		MimeMessage mailMessage = sender.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage, true, "utf-8");
		if(!IPUtil.getLocalIP().startsWith("70.38.112")||Preview){
			messageHelper.setTo(new String[]{"ee@foolishb.com","jany.wuhui@gmail.com","wyjfly@gmail.com"});
			messageHelper.setSubject("[Testing]"+(Preview?"[Preview]":"")+subject);
			messageHelper.setText("To: "+Arrays.toString(tos)+"<br>\r\r"+text, true);
			System.out.println(Arrays.toString(tos));
			//System.out.println(text);
		}else{
			messageHelper.setTo(tos);
			messageHelper.setSubject(subject);
			messageHelper.setText(text, true);
			System.out.println("Server.....");
		}
		
		//messageHelper.setTo(new String[]{"ee@foolishb.com","wyjfly@gmail.com"});
		messageHelper.setFrom(Configuration.EMAIL_HOST);
		if(filename!=null&&file!=null)messageHelper.addAttachment(filename, file);
		
		
		sender.send(mailMessage);
	}
	
	/**
	 * check the permission and check the user's group and send email by different template
	 * @param userid
	 * @return
	 * 2010/05/01
	 */
	private static boolean[] VF_MPIQChecker(long userid){
		boolean[] permission = new boolean[2];
		//boolean isSubscred = permission[0];
		//boolean isMPIQ = permission[1];
		boolean isMPIQ = false; 
		boolean isSubscred = false;
		if(userid==Configuration.SUPER_USER_ID){
			isMPIQ = true;
			isSubscred = true;
			permission = new boolean[]{isSubscred,isMPIQ};
			return permission;
		}
		else{
			GroupManager gm=ContextHolder.getGroupManager();
			Object[] groups=gm.getGroupIDs(userid);
			if(groups!=null&&groups.length>0){
				for(int i=0;i<groups.length;i++){
					if(Configuration.GROUP_MPIQ_B_ID.equals(groups[i])||Configuration.GROUP_VF_B_ID.equals(groups[i]))
						isSubscred = true;
					if(Configuration.GROUP_MPIQ_B_ID.equals(groups[i])){
						isMPIQ = true;
						break;
					}
				}
			}
			permission = new boolean[]{isSubscred,isMPIQ};
			return permission;
		}
	}
	
	public static void main(String[] args){
		sendEmails(true);
	}
	
	public static void sendEmails(){
		sendEmails(false);
	}
	
	
}
