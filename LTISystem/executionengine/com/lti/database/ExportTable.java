package com.lti.database;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import com.lti.service.PortfolioManager;
import com.lti.service.bo.Portfolio;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.util.FileOperator;

public class ExportTable {

	
	public static void main(String[] args)throws Exception{
		PortfolioManager pm=ContextHolder.getPortfolioManager();
		List<BigInteger> emailids=pm.findBySQL("select distinct(portfolioid) from "+Configuration.TABLE_EMAILNOTIFICATION);
		List<BigInteger> nopids=pm.findBySQL("select portfolioid from ltisystem_portfoliostate where DelayPieChart is null");
		StringBuffer sb=new StringBuffer();
		sb.append("<h1>Portfolios with Email Alert</h1>");
		sb.append("<table width=100% border=0>");
		for(BigInteger bd:emailids){
			long id=bd.longValue();
			Portfolio p=pm.getBasicPortfolio(id);
			sb.append("<tr>");
			sb.append("<td>");
			sb.append(id);
			sb.append("</td>");
			sb.append("<td>");
			sb.append("<a href='http://www.myplaniq.com/LTISystem/jsp/portfolio/ViewPortfolio.action?ID="+id+"'>");
			sb.append(p.getName());
			sb.append("</a>");
			sb.append("</td>");
			sb.append("<td>");
			if(nopids.contains(id)){
				sb.append("Broken?");
			}
			sb.append("</td>");
			sb.append("</tr>");
			sb.append("\r\n");
		}
		sb.append("</table>");
		
		
		sb.append("<h1>Broken Portfolios?</h1>");
		sb.append("<table width=100% border=0>");
		for(BigInteger bd:nopids){
			long id=bd.longValue();
			if(emailids.contains(id))continue;
			Portfolio p=pm.getBasicPortfolio(id);
			if(p==null)continue;
			sb.append("<tr>");
			sb.append("<td>");
			sb.append(id);
			sb.append("</td>");
			sb.append("<td>");
			sb.append("<a href='http://www.myplaniq.com/LTISystem/jsp/portfolio/ViewPortfolio.action?ID="+id+"'>");
			sb.append(p.getName());
			sb.append("</a>");
			sb.append("</td>");
			sb.append("<td>");
			if(nopids.contains(id)){
				sb.append("Broken?");
			}
			sb.append("</td>");
			sb.append("</tr>");
			sb.append("\r\n");
		}
		sb.append("</table>");
		
		FileOperator.appendMethodA("report.html", sb.toString());
	}
}
