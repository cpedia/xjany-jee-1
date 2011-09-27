package com.lti.system;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.views.freemarker.FreemarkerManager;
import org.apache.struts2.views.freemarker.ScopesHashModel;

import com.lti.customizepage.BasePage;
import com.opensymphony.xwork2.util.ValueStack;

import freemarker.template.ObjectWrapper;
import freemarker.template.SimpleHash;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateModelIterator;

public class LTIFreeMarkerManager extends FreemarkerManager {

	@Override
	public SimpleHash buildTemplateModel(ValueStack stack, Object action, ServletContext servletContext, HttpServletRequest request, HttpServletResponse response, ObjectWrapper wrapper) {
		SimpleHash model = super.buildTemplateModel(stack, action, servletContext, request, response, wrapper);
		if(action instanceof BasePage){
			BasePage bp=(BasePage)action;
			Map root=bp.getRoot();
			if(root!=null){
				Iterator iter=root.keySet().iterator();
				while(iter.hasNext()){
					Object key=iter.next();
					model.put((String) key, root.get(key));
				}
			}
		}
		return model;
	}

}
