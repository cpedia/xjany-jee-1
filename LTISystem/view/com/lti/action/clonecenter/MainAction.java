package com.lti.action.clonecenter;

import java.util.List;

import com.lti.action.Action;
import com.lti.util.PersistentUtil;
import com.opensymphony.xwork2.ActionSupport;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

public class MainAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;



	@Override
	public String execute() throws Exception {
		
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
	
	private PersistentTemplateMethodModel readObject=new PersistentTemplateMethodModel();

	public PersistentTemplateMethodModel getReadObject() {
		return readObject;
	}

	public void setReadObject(PersistentTemplateMethodModel readObject) {
		this.readObject = readObject;
	}

}
