package com.xjany.common.springmvc;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

/**
 * 异常拦截器
 * 
 * @author : lixiang
 */
public class ExceptionHandler implements HandlerExceptionResolver {
	private static final long serialVersionUID = 1L;

	private static Logger logger = Logger.getLogger(ExceptionHandler.class);

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) {
		logger.error("Catch Exception: ", e);// 把漏网的异常信息记入日志
		Map<String, Object> map = new HashMap<String, Object>();
		StringPrintWriter strintPrintWriter = new StringPrintWriter();
		e.printStackTrace(strintPrintWriter);
		map.put("errorMsg", strintPrintWriter.getString());// 将错误信息传递给view
		return new ModelAndView("error", map);
	}
}
