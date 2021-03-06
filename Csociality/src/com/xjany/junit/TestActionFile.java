package com.xjany.junit;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;


public class TestActionFile extends JUnitBase{
	@Test
	public void list() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

		request.setServletPath("/checkUser.do");
		request.addParameter("id", "5");   
		request.setMethod("get");

		// HandlerAdapter
		final ModelAndView mav = excuteAction(request,response);

		// Assert logic
		Assert.assertEquals("login", mav.getViewName());
	}
}
