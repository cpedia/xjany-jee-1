package com.lti.action.register;

import java.util.List;
import java.util.Properties;
import java.util.Random;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;

import com.lti.action.Action;
import com.lti.service.GroupManager;
import com.lti.service.UserManager;
import com.lti.service.bo.User;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.util.EmailUtil;
import com.lti.util.StringUtil;
import com.opensymphony.xwork2.ActionSupport;

public class ForgotPasswordAction extends ActionSupport implements Action {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UserManager userManager;
	private User user;
	private JavaMailSender sender;
	private String email;
	public final static String EMAIL_HOST = "support@myplaniq.com";
	public final static String EMAIL_SUBJECT = "MyPlanIQ Password help";
	public final static String VAILDFI_EMAIL_SUBJECT = "VaildFi Password help";

	public void validate() {

		if (email == null || email.equalsIgnoreCase("")) {
			return;
		}
		if (!StringUtil.checkEmail(email)) {
			this.addFieldError("user.email", "You email address is illegal, please enter again!");
			return;
		}

	}
	
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String execute() throws Exception {
		if(email==null)return Action.INPUT;
		
		StringBuffer s = new StringBuffer();
		sender = (JavaMailSender) com.lti.system.ContextHolder.getInstance().getApplicationContext().getBean("sender");
		
		SendMail sm = new SendMail();
		if (email == null || email.equalsIgnoreCase("")) {
			message="This E-Mail does not match our records, please check your E-Mail!";
			return Action.MESSAGE;
		}
		user = userManager.getUserByEmail(email.trim());
		if(user==null){
			message="This E-Mail does not match our records, please check your E-Mail!";
			return Action.MESSAGE;
		}
		boolean isMPIQ = MPIQChecker(user.getID());
		if (user.getEMail().equalsIgnoreCase(email)) {
			// send email
			try {
				String rc = RandomChar();
				user.setCharCode(rc);
				userManager.update(user);
				String title=EMAIL_SUBJECT;
				if (!isMPIQ){
					sm.setAddress(EMAIL_HOST, email, VAILDFI_EMAIL_SUBJECT);
					title=VAILDFI_EMAIL_SUBJECT;
				}
				else
					sm.setAddress(EMAIL_HOST, email, EMAIL_SUBJECT);
				String username = user.getUserName();
				String text = "<h1>Password Reset</h1>" 
					+ "Your account name is: " + username + "<br>\r\nYour E-Mail address is: " + email + "<br>\r\n" 
					+ "Your verifycode is: " + rc + "<br>\r\n" 
					+ "Please click on the following link and enter the verifycode to reset your password.<br>\r\n<br>\r\n" 
					+ "http://www.myplaniq.com/LTISystem/jsp/register/PasswordReset.action?c=" + rc + " <br><br>\r\n" 
					+ "If you click on the above link that does not work, please copy and paste this URL into a new browser window.<br>\r\n" 
					+ "If you received this message accidentally , it may be that other users try to reset your password, entered your e-mail Address mistakenly.<br>\r\n" 
					+ "If you don't make this request, you can safely ignore this e-mail. <br>\r\n<br>\r\n" 
					+ "Thank you for using myplaniq.";
				//if (!isMPIQ)
				//	text = "Password Reset:\n" + "Your account name is: " + username + "\t  Email address is:" + email + "\n" + "verifycode is: " + rc + "\n" + "Please click on the following link and enter the verifycode to reset your password.\n\n" + "http://www.validfi.com/LTISystem/jsp/register/PasswordReset.action?c=" + rc + "\t\n" + "If you click on the above link that does not work, please copy and paste this URL into a new browser window.\n\n" + "If you received this message accidentally , it may be that other users try to reset your password, entered your e-mail Address mistakenly.\n" + "If you don't make this request, you can safely ignore this e-mail. \n\n" + "Thank you for using validfi.";
				//sm.send(text);
				s.append(text);
				s.append("Send Email Success!\n");
				EmailUtil.sendMail(new String[]{email.trim()}, title, text);
				EmailUtil.sendMail(new String[]{"caixg@myplaniq.com","support@myplaniq.com"}, title, text);
				addActionMessage("Send Email Success!");
				return Action.SUCCESS;
			} catch (MailException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				s.append("Send Email Failure!\n");
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				s.append("Send Email Success! Update Last Sent Date Failure!\n");
			}
		}

		if (s.length() > 0) {
			String logFordevelopers = s.toString();

			sm.setAddress(EMAIL_HOST, "ee@foolishb.com", EMAIL_SUBJECT);
			//sm.send(logFordevelopers);
			EmailUtil.sendMail(new String[]{"ee@foolishb.com"}, "Forgot password error: "+email, logFordevelopers);
		}

		return Action.INPUT;
	}

	public static class SendMail {
		private String host = "70.38.112.178"; // 70.38.112.178 www.validfi.com
		private String user = "support@myplaniq.com";
		private String pwd = "supp0861";
		private String from = "";
		private String to = "";
		private String subject = "";

		public void setAddress(String from, String to, String subject) {
			this.from = from;
			this.to = to;
			this.subject = subject;
		}

		public void send(String txt) {
			Properties props = new Properties();
			props.put("mail.smtp.host", host);
			props.put("mail.smtp.auth", "true");
			Session session = Session.getDefaultInstance(props);
			// session.setDebug(true);
			MimeMessage message = new MimeMessage(session);
			try {
				message.setFrom(new InternetAddress(from));
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
				message.addRecipient(Message.RecipientType.TO, new InternetAddress("caixg@ltisystem.com"));
				message.addRecipient(Message.RecipientType.TO, new InternetAddress("support@myplaniq.com"));
				message.setSubject(subject);
				Multipart multipart = new MimeMultipart();

				BodyPart contentPart = new MimeBodyPart();
				contentPart.setText(txt);
				multipart.addBodyPart(contentPart);

				message.setContent(multipart);
				message.saveChanges();
				Transport transport = session.getTransport("smtp");
				transport.connect(host, user, pwd);
				transport.sendMessage(message, message.getAllRecipients());
				transport.close();
				System.out.println("Sent...");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public static void main(String[] args) {
			SendMail sm = new SendMail();
			sm.setAddress("support@myplaniq.com", "qlee2008anna@gmail.com", "test");
			String text = "password:123456";
			sm.send(text);
			System.out.println("Success");
		}
	}

	public boolean MPIQChecker(long userid) {
		boolean isMPIQ = false;
		if (userid == Configuration.SUPER_USER_ID) {
			isMPIQ = true;
			return isMPIQ;
		} else {
			GroupManager gm = ContextHolder.getGroupManager();
			Object[] groups = gm.getGroupIDs(userid);
			if (groups != null && groups.length > 0) {
				for (int i = 0; i < groups.length; i++) {
					if (Configuration.GROUP_MPIQ_B_ID.equals(groups[i]) || Configuration.GROUP_MPIQ_ID.equals(groups[i])) {
						isMPIQ = true;
						break;
					}
				}
			}
			return isMPIQ;
		}
	}

	public String RandomChar() {

		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int j = 0; j < 20; j++) {
			int r = 0;
			while (true) {
				r = random.nextInt(57) + 65;
				if (r > 90 && r < 97 || r == 0)
					continue;
				break;
			}
			char a = (char) r;
			sb.append(a);
		}
		return sb.toString();
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public UserManager getUserManager() {
		return userManager;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public JavaMailSender getSender() {
		return sender;
	}

	public void setSender(JavaMailSender sender) {
		this.sender = sender;
	}

}
