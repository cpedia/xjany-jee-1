package com.lti.action;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;

import com.lti.service.GroupManager;
import com.lti.service.UserManager;
import com.lti.service.bo.User;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.util.EmailUtil;
import com.lti.util.StringUtil;

import freemarker.template.Template;

public class advisoraction {

	
	UserManager userManager;
	
	List<User> advisors;
	
	public String list(){
		userManager=ContextHolder.getUserManager();
		advisors=userManager.getUsersByGroup(Configuration.GROUP_ADVISOR_ID);
		return Action.SUCCESS;
	}
	
	
	private User advisor;
	private Long ID;

	public User getAdvisor() {
		return advisor;
	}
	public void setAdvisor(User advisor) {
		this.advisor = advisor;
	}
	public Long getID() {
		return ID;
	}
	public void setID(Long iD) {
		ID = iD;
	}
	private String message;
	public String view(){
		GroupManager groupManager=ContextHolder.getGroupManager();
		userManager=ContextHolder.getUserManager();
		if(groupManager.getGroupUser(Configuration.GROUP_ADVISOR_ID, ID)==null){
			message="The advisor doesn't exist.";
			return Action.MESSAGE;
		}
		advisor=userManager.get(ID);
		User loginU=userManager.getLoginUser();
		if(loginU!=null&&loginU.getID()!=Configuration.USER_ANONYMOUS.longValue()){
			if(loginU.getFirstName()!=null&&loginU.getLastName()!=null){
				name=loginU.getFirstName()+" "+loginU.getLastName();
			}else{
				name=loginU.getUserName();
			}
			email=loginU.getEMail();
			phone=loginU.getTelephone();
			
			
		}
		return Action.SUCCESS;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	private String note;
	private String name;
	private String email;
	private String phone;
	private String subject;
	
	public String consult(){
		DateFormat df=new SimpleDateFormat("dd/MM/yyyy");
		try {
			GroupManager groupManager=ContextHolder.getGroupManager();
			userManager=ContextHolder.getUserManager();
			if(groupManager.getGroupUser(Configuration.GROUP_ADVISOR_ID, ID)==null){
				throw new RuntimeException("The advisor deosn't exist.");
			}
			advisor=userManager.get(ID);
			freemarker.template.Configuration conf=new freemarker.template.Configuration();
			conf.setDirectoryForTemplateLoading(new File(ContextHolder.getServletPath(),"/jsp/advisor"));
			Template t=conf.getTemplate("email.ftl");
			StringWriter sw=new StringWriter();
			Map<String,Object> data=new HashMap<String, Object>();
			data.put("name", name);
			data.put("phone", phone);
			data.put("email", email);
			data.put("advisor", advisor);
			data.put("note", note);
			t.process(data, sw);
			
			EmailUtil.sendMail(new String[]{advisor.getEMail()}, "[MPIQ]"+subject, sw.toString());
		} catch (Exception e) {
			ServletActionContext.getResponse().setStatus(500);
			StringBuffer sb=new StringBuffer();
			
			sb.append("Advisor ID:");
			sb.append(ID);
			sb.append("<br>\r\nUser Email:");
			sb.append(email);
			sb.append("<br>\r\nNote:");
			sb.append(note);
			sb.append("<br>\r\n<br>\r\n<br>\r\n");
			sb.append("<pre>");
			sb.append(StringUtil.getStackTraceString(e));
			sb.append("</pre>");
			try {
				EmailUtil.sendMail(new String[]{"wyjfly@gmail.com","caixg@ltisystem.com"}, "["+df.format(new Date())+"][ERROR]Consultation from MPIQ", sb.toString());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			ServletActionContext.getResponse().setStatus(500);
		}
		message="OK!";
		return Action.MESSAGE;
	}
	
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public List<User> getAdvisors() {
		return advisors;
	}
	public void setAdvisors(List<User> advisors) {
		this.advisors = advisors;
	}
	
	
}
