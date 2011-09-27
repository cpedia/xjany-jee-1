package com.lti.action;

import java.util.List;

import org.apache.struts2.ServletActionContext;

import com.lti.service.bo.User;
import com.lti.service.bo.UserProfile;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.util.PersistentUtil;
import com.opensymphony.xwork2.ActionSupport;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

public class TemplateAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	
	protected Boolean checkPermission(){
		return true;
	}

	
	
	
	@Override
	public String execute() throws Exception {
		hostname=ServletActionContext.getRequest().getServerName();
		return Action.SUCCESS;

	}





	public class PersistentTemplateMethodModel implements TemplateMethodModel {
		public Object exec(List arguments) throws TemplateModelException {
			try {
				return PersistentUtil.readObject((String)arguments.get(0));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "Object did not exist.";
		}
	}
	public class PersistentTemplateMethodModel2 implements TemplateMethodModel {
		public Object exec(List arguments) throws TemplateModelException {
			try {
				return PersistentUtil.readGlobalObject((String)arguments.get(0));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "Object did not exist.";
		}
	}
	
	private String hostname="";
	
	
	
	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	private PersistentTemplateMethodModel2 readGlobalObject=new PersistentTemplateMethodModel2();
	
	
	public PersistentTemplateMethodModel2 getReadGlobalObject() {
		return readGlobalObject;
	}

	public void setReadGlobalObject(PersistentTemplateMethodModel2 readGlobalObject) {
		this.readGlobalObject = readGlobalObject;
	}

	private PersistentTemplateMethodModel readObject=new PersistentTemplateMethodModel();

	public PersistentTemplateMethodModel getReadObject() {
		return readObject;
	}

	public void setReadObject(PersistentTemplateMethodModel readObject) {
		this.readObject = readObject;
	}

}
