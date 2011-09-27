package com.lti.executor.web.action;

import java.util.Iterator;

import com.lti.executor.ExecutorPool;
import com.lti.executor.web.BasePage;
import com.lti.service.bo.Portfolio;
import com.lti.type.finance.ExecutorPortfolio;
import com.lti.util.IPUtil;
import com.lti.util.StringUtil;

public class Server extends BasePage {

	private String info;

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	@Override
	public String execute() throws Exception {

		String ip = IPUtil.getLocalIP();
		if (ip == null)
			ip = "127.0.0.1";

		try {

			Iterator<Long> iter = ExecutorPool.getInstance().getPool().keySet().iterator();
			StringBuffer sb = new StringBuffer();
			while (iter.hasNext()) {
				Long key = iter.next();
				ExecutorPortfolio ep = (ExecutorPortfolio) ExecutorPool.getInstance().getPool().get(key);
				sb.append(ep.getPortfolioID());
				sb.append(",");
				sb.append(ep.getPortfolioName());
				sb.append(",");
				sb.append(System.currentTimeMillis());
				sb.append("#");
			}
			info = sb.toString();

		} catch (Exception e) {
			info = "<pre>" + StringUtil.getStackTraceString(e) + "</pre>";

		}
		return "info.ftl";
	}

}
