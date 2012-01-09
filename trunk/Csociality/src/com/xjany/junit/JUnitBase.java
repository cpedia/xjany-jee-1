package com.xjany.junit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;
import org.springframework.web.servlet.mvc.annotation.DefaultAnnotationHandlerMapping;

import com.xjany.bbs.action.front.FileAction;
import com.xjany.bbs.service.FileService;

/**
 * 说明： JUnit测试时使用的基类
 * 所有的测试类都要继承这个类
 * @author lixiang
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"file:WebRoot/config/XjanyFront-servlet.xml",
		"file:WebRoot/config/XjanyAdmin-servlet.xml",
		"file:WebRoot/config/applicationContext.xml" })
public class JUnitBase {
	private static HandlerMapping handlerMapping;
	private static HandlerAdapter handlerAdapter;

	/**
	 * 执行request对象请求的action
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView excuteAction(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String[] configs = { "file:WebRoot/config/XjanyFront-servlet.xml", "file:WebRoot/config/applicationContext.xml" };
		XmlWebApplicationContext context = new XmlWebApplicationContext();
		context.setConfigLocations(configs);
		MockServletContext msc = new MockServletContext();
		context.setServletContext(msc);
		context.refresh();
		msc.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, context);
		handlerMapping = (HandlerMapping) context.getBean(DefaultAnnotationHandlerMapping.class);
		handlerAdapter = (HandlerAdapter) context.getBean(context.getBeanNamesForType(AnnotationMethodHandlerAdapter.class)[0]);
		
		HandlerExecutionChain chain = handlerMapping.getHandler(request);
		
		final ModelAndView model = handlerAdapter.handle(request, response, chain.getHandler());
		return model;
	}
}