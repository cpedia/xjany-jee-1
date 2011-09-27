package com.lti.system;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.acegisecurity.AuthenticationException;
import org.acegisecurity.ui.AbstractProcessingFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import com.lti.service.bo.User;

public class AcegiAjaxFilter extends OncePerRequestFilter {
	
	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse arg1, FilterChain arg2)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		//检查提交的变量中是否有ajax变量，没有就直接交给acegi默认处理
        if (request.getParameter("ajax") == null) {
        	arg2.doFilter(request, arg1);
            return;
        }

        RedirectResponseWrapper redirectResponseWrapper = new RedirectResponseWrapper(arg1);

 

        //acegi的filter chain处理认证,redirectResponseWrapper用于获取acegi认证处理后的跳转路径

        arg2.doFilter(request, redirectResponseWrapper);

        if (redirectResponseWrapper.getRedirect() != null) {
        	request.setCharacterEncoding("UTF-8");
        	arg1.setContentType("text/plain;charset=utf-8");

        	arg1.setHeader("Cache-Control", "no-cache");
        	arg1.setDateHeader("Expires", 0);
        	arg1.setHeader("Pragma", "no-cache");

            String redirectURL = redirectResponseWrapper.getRedirect();

            //创建JSONObject对象，用于返回认证结果，便于ajax页面局部刷新

            JSONObject json=new JSONObject();
           
            try{

                //在acegi认证失败跳转的url加上login_error=1

                //此外判断是否成功
                if (redirectURL.indexOf("login_error=1") == -1){
                    json.put("success",true);
                    //获取用户登录信息
                    User user= (User) request.getSession().getAttribute("user");
                   
                    if (user!=null ){
                        json.put("name",user.getUserName());
                        json.put("lastIp",user.getID());
                    }

                }else{//登录失败
                    json.put("success",false);
                    String errorMsg= ((AuthenticationException) request.getSession().getAttribute(AbstractProcessingFilter.ACEGI_SECURITY_LAST_EXCEPTION_KEY)).getMessage();
                    json.put("errorMsg",errorMsg);
                }

            }catch(JSONException e){
                logger.error("AcegiAjaxFilter JSONException");
                logger.error("message:"+e.getMessage());
            }catch(Exception e){
                logger.error("AcegiAjaxFilter Exception");
                logger.error("message:"+e.getMessage());
            }
            
            String returnback=request.getParameter("jsoncallback")+"("+json.toString()+")";
            
            //把json数据写入response返回到页面
            arg1.getOutputStream().write(returnback.getBytes("UTF-8"));
        }
	}
}