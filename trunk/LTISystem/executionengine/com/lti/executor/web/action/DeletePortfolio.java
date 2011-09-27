/**
 * 
 */
package com.lti.executor.web.action;


import com.lti.executor.web.BasePage;
import com.lti.service.PortfolioManager;
import com.lti.system.ContextHolder;
import com.lti.util.StringUtil;

/**
 * @author ccd
 *
 */
public class DeletePortfolio extends BasePage {
	
	public PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();
	
	public DeletePortfolio(){
	}
	
	private String info;

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
	
	@Override
	public String execute(){
		StringBuffer sb=new StringBuffer();
		try{
			String portfolioIDs = (request.getParameter("portfolioIDs"));
			if(portfolioIDs != null){
				String[] portfoioIDStrs = portfolioIDs.split(",");
				for(String portfolioStr : portfoioIDStrs){
					try{
						Long portfolioID = Long.parseLong(portfolioStr);
						if(portfolioID != null){
							portfolioManager.remove(portfolioID);
							sb.append(portfolioStr + ",success");
						}
					}catch(Exception e){
						sb.append(portfolioStr + ",fail");
					}
					sb.append("<br>\r\n");
				}
			}
			sb.append("finish all");
			sb.append("<br>\r\n");
		}catch(Exception e){
			info = StringUtil.getStackTraceString(e);
		}
		info = sb.toString();
		return "info.ftl";
	}
}
