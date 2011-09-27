package com.lti.action.admin.customizepage;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.action.Action;
import com.lti.customizepage.PageCompiler;
import com.lti.service.CustomizePageManager;
import com.lti.service.UserManager;
import com.lti.service.bo.CustomizePage;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.opensymphony.xwork2.ActionSupport;

public class SaveAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(SaveAction.class);
	
	
	private String action;
	
	private Long ID;
	
	private String name;
	private java.lang.String template;
	private java.lang.String code;
	private java.lang.String functions;
	
	private java.lang.String title;
	
	private CustomizePageManager customizePageManager;
	
	private Boolean nofck;
	

	public Boolean getNofck() {
		return nofck;
	}

	public void setNofck(Boolean nofck) {
		this.nofck = nofck;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public java.lang.String getTemplate() {
		return template;
	}

	public void setTemplate(java.lang.String template) {
		this.template = template;
	}

	public java.lang.String getCode() {
		return code;
	}

	public void setCode(java.lang.String code) {
		this.code = code;
	}

	public void setCustomizePageManager(CustomizePageManager customizePageManager) {
		this.customizePageManager = customizePageManager;
	}

	private static String templateFromFile=null;
	private static String getTemplateFromFile(){
		if(templateFromFile==null){
			try {
				FileReader fr=new FileReader(new File(ContextHolder.getServletPath()+"/jsp/customizepage/template"));
				BufferedReader br=new BufferedReader(fr);
				String line=br.readLine();
				StringBuffer sb=new StringBuffer();
				while(line!=null){
					sb.append(line);
					sb.append("\n");
					line=br.readLine();
				}
				templateFromFile=sb.toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		return templateFromFile;
	}
	
	public void validate(){

		if(this.action==null||this.action.equals(""))this.action=ACTION_CREATE;
		
		if(this.action.equalsIgnoreCase(ACTION_SAVE)||this.action.equalsIgnoreCase(ACTION_SAVEAS)){
			if(name==null||name.equals("")){
				addFieldError("Name","Empty name is not validate!");
				return;
			}
			
			else if(customizePageManager.get(name)!=null){
				addFieldError("Name","The name has been used, please enter another name!");
				return;
			}
			/*if(template==null||template.equals("")){
				addFieldError("Template","Template is not validate!");
				return;
			}
			if(code==null||code.equals("")){
				addFieldError("Code","Code is not validate!");
				return;
			}*/
		}
		
		else if(this.action.equalsIgnoreCase(ACTION_UPDATE)){
			
			if(name==null||name.equals("")){
				addFieldError("Name","Name is not validate!");
				return;
			}
			else if(customizePageManager.get(name)!=null&&!customizePageManager.get(name).getID().equals(this.ID)){
				addFieldError("Name","The name has been used, please enter another name!");
				return;
			}
			/*if(template==null||template.equals("")){
				addFieldError("Template","Template is not validate!");
				return;
			}
			if(code==null||code.equals("")){
				addFieldError("Code","Code is not validate!");
				return;
			}*/
			
			if(ID==null){
				addFieldError("ID","ID is not validate!");
				return;
			}else{
				CustomizePage cps=customizePageManager.get(ID);
				
				if(cps==null){
					addFieldError("ID","ID is not validate!");
					return;
				}
			}
			
		}
		
		else if(this.action.equalsIgnoreCase(ACTION_DELETE)){
			
			if(ID==null){
				addFieldError("ID","ID is not validate!");
				return;
			}else{
				CustomizePage cps=customizePageManager.get(ID);
				
				if(cps==null){
					addFieldError("ID","ID is not validate!");
					return;
				}
			}
			
			
			
		}
		
		else if(this.action.equalsIgnoreCase(ACTION_VIEW)){
			
			if(ID==null){
				addFieldError("ID","ID is not validate!");
				return;
			}else{
				CustomizePage cps=customizePageManager.get(ID);
				
				if(cps==null){
					addFieldError("ID","ID is not validate!");
					return;
				}
			}
			
			
			
		}else if(this.action.equalsIgnoreCase(ACTION_CREATE)){
			
		}
		
		else{
			
			addFieldError("action","Request is not validate!");
			
		}
		
	}

	@Override
	public String execute() throws Exception {
		
		CustomizePage cp=new CustomizePage();
		
		cp.setID(ID);
		
		cp.setCode(code);
		
		cp.setName(name);
		
		cp.setTemplate(template);
		
		cp.setFunctions(functions);
		
		if(this.action.equalsIgnoreCase(ACTION_SAVE)){
			
			cp.setID(null);
			
			customizePageManager.add(cp);
			
			ID=cp.getID();
			
			action=ACTION_UPDATE;
			
			title="User : "+cp.getName();
			
			try {
				PageCompiler.Complie(cp, ContextHolder.getServletPath());
				PageCompiler.GenerateTemplate(name, ContextHolder.getServletPath(), template);
				addActionMessage("Add successfully!");
			} catch (Exception e) {
				e.printStackTrace();
				addActionMessage("Add successfully, failed to compile!");
			}
			
			
			if(nofck!=null&&nofck){
				return "nofck";
			}
			return Action.INPUT;
			
		}
		
		else if(this.action.equalsIgnoreCase(ACTION_UPDATE)){
			
			customizePageManager.update(cp);
			
			action=ACTION_UPDATE;
			
			title="User : "+cp.getName();
			
			try {
				String s=PageCompiler.Complie(cp, ContextHolder.getServletPath());
				PageCompiler.GenerateTemplate(name, ContextHolder.getServletPath(), template);
				if(s.equals(""))addActionMessage("Update successfully!");
				else {
					addActionMessage(s);
				}
			} catch (Exception e) {
				e.printStackTrace();
				addActionMessage("Update successfully, failed to compile!");
			}
			
			
			if(nofck!=null&&nofck){
				return "nofck";
			}
			return Action.INPUT;
			
		}
		
		else if(this.action.equalsIgnoreCase(ACTION_DELETE)){
			
			customizePageManager.remove(ID);
			
			action=ACTION_CREATE;
			
			try {
				PageCompiler.DeleteTemplate(name, ContextHolder.getServletPath());
				PageCompiler.Delete(name,ContextHolder.getServletPath());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			addActionMessage("Delete successfully!");			
			
			return Action.SUCCESS;
			
		}
		
		else if(this.action.equalsIgnoreCase(ACTION_VIEW)){
			
			cp=customizePageManager.get(ID);
			
			ID=cp.getID();
			
			code=cp.getCode();
			
			name=cp.getName();
			
			template=cp.getTemplate();
			
			functions=cp.getFunctions();
			
			action=ACTION_UPDATE;
			
			title="User : "+cp.getName();
			
			if(nofck!=null&&nofck){
				return "nofck";
			}
			return Action.INPUT;
			
		}else if(this.action.equalsIgnoreCase(ACTION_CREATE)){
			
			action=ACTION_SAVE;
			
			title="New User";
			
			template=getTemplateFromFile();
			
			if(nofck!=null&&nofck){
				return "nofck";
			}
			return Action.INPUT;
		}else if(this.action.equalsIgnoreCase(ACTION_SAVEAS)){
			
			cp.setID(null);
			
			customizePageManager.add(cp);
			
			ID=cp.getID();
			
			action=ACTION_UPDATE;
			
			title="User : "+cp.getName();
			
			try {
				PageCompiler.Complie(cp, ContextHolder.getServletPath());
				PageCompiler.GenerateTemplate(name, ContextHolder.getServletPath(), template);
				addActionMessage("Add successfully!");
			} catch (Exception e) {
				e.printStackTrace();
				addActionMessage("Add successfully, failed to compile!");
			}
			
			
			if(nofck!=null&&nofck){
				return "nofck";
			}
			return Action.INPUT;
		}
		
		return Action.ERROR;

	}

	
	
	
	public String getAction() {
	
		return action;
	}

	
	public void setAction(String action) {
	
		this.action = action;
	}

	
	public Long getID() {
	
		return ID;
	}

	
	public void setID(Long id) {
	
		ID = id;
	}

	

	
	public java.lang.String getTitle() {
		return title;
	}

	public void setTitle(java.lang.String title) {
		this.title = title;
	}

	public java.lang.String getFunctions() {
		return functions;
	}

	public void setFunctions(java.lang.String functions) {
		this.functions = functions;
	}
	

}
