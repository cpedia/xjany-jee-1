package com.xjany.common.springmvc;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class EncodeFilter implements Filter {

	private String encode = "UTF-8";

	private String decode(String data) {
		try {
			return new String(data.getBytes("ISO-8859-1"), encode);
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}

	public void destroy() {
	}

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain fc) throws IOException, ServletException {
		HttpServletRequest hs = (HttpServletRequest) req;
		for (Object k : hs.getParameterMap().keySet()) {
			String[] vals = hs.getParameterValues(k.toString());
			if (vals.length > 0) {
				for (int i = 0; i < vals.length; i++) {
					vals[i] = decode(vals[i]);
				}
				hs.setAttribute(k.toString(), vals);
			} else {
				hs.setAttribute(k.toString(), decode(vals[0]));
			}

		}
		fc.doFilter(hs, res);
	}

	public void init(FilterConfig fc) throws ServletException {
		if (fc.getInitParameter("encode") != null) {
			encode = fc.getInitParameter("encode");
		}

	}

}
