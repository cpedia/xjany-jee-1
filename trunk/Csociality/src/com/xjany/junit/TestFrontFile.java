package com.xjany.junit;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.test.context.junit4.*;

import com.xjany.bbs.action.front.FileAction;

@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration(locations = {"file:WebRoot/config/XjanyFront-servlet.xml" })
public class TestFrontFile {  
    
    private static HandlerMapping handlerMapping;  
    private static HandlerAdapter handlerAdapter;   
  
  
    @Test  
    public void list() throws Exception {  
        MockHttpServletRequest request = new MockHttpServletRequest();  
        MockHttpServletResponse response = new MockHttpServletResponse();  
          
        request.setRequestURI("/file?method=recycle");  
        request.setMethod("POST");  
          
        //HandlerMapping  
        HandlerExecutionChain chain = handlerMapping.getHandler(request);  
        Assert.assertEquals(true, chain.getHandler() instanceof FileAction);  
          
        //HandlerAdapter  
        final ModelAndView mav = handlerAdapter.handle(request, response, chain.getHandler());  
          
        //Assert logic  
        Assert.assertEquals("hotels/search", mav.getViewName());  
    }  
}  