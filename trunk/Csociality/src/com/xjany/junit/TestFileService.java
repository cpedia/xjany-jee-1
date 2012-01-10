package com.xjany.junit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.test.context.junit4.*;

import com.xjany.bbs.action.front.FileAction;
import com.xjany.bbs.service.FileService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:/WebRoot/config/*.xml" })
public class TestFileService {

	private static HandlerMapping handlerMapping;
	private static HandlerAdapter handlerAdapter;

	@Test
	public void list() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

		request.setRequestURI("/file?method=recycle");
		request.addParameter("File.id", "5");
		request.setMethod("post");

		// HandlerAdapter
		final ModelAndView mav = this.excuteAction(request, response);

		// Assert logic
		Assert.assertEquals("hotels/search", mav.getViewName());
	}

	/**
	 * 执行request对象请求的action
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView excuteAction(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HandlerExecutionChain chain = handlerMapping.getHandler(request);
		final ModelAndView model = handlerAdapter.handle(request, response, chain.getHandler());
		return model;
	}
}