package com.lti.util;

import java.util.List;

import com.lti.bean.PortfolioItem;
import com.lti.bean.StrategyItem;
import com.lti.system.Configuration;

public class StrategyUtil {
	public static String generateXML(List<StrategyItem> strategies){
		StringBuffer sb = new StringBuffer();
		sb.append( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<Root>");
		tableHead(sb);
		if(strategies != null){
			strategyShow(strategies, sb);
		}
		sb.append("<Link>");
		sb.append("<SecurityLink>");
		sb.append("<Head>" + "/LTISystem/jsp/security/Save.action?ID=" + "</Head>");
		sb.append("<Tail>" + "&action=view" + "</Tail>");
		sb.append("</SecurityLink>");
		sb.append("<PortfolioLink>");
		sb.append("<Head>" + "/LTISystem/jsp/portfolio/Edit.action?ID=" + "</Head>");
		sb.append("<Tail>" + "&action=view" + "</Tail>");
		sb.append("</PortfolioLink>");
		sb.append("<StrategyLink>");
		sb.append("<Head>" + "/LTISystem/jsp/strategy/View.action?ID=" + "</Head>");
		sb.append("<Tail>" + "&action=view" + "</Tail>");
		sb.append("</StrategyLink>");
		sb.append("</Link>");
		sb.append("</Root>");
		return sb.toString();
	}
	
	public static void tableHead(StringBuffer sb){
		sb.append("<tablehead>");
		sb.append("<column>StrategyName</column>");
		sb.append("<column>PortfolioName</column>");
		sb.append("<column>LastValidDate</column>");
		sb.append("<column>" + PortfolioItem.AR_LASTONEYEAR + "</column>");
		sb.append("<column>" + PortfolioItem.AR_LASTTHREEYEAR + "</column>");
		sb.append("<column>" + PortfolioItem.AR_LASTFIVEYEAR + "</column>");
		sb.append("<column>" + PortfolioItem.SHARPE_LASTONEYEAR + "</column>");
		sb.append("<column>" + PortfolioItem.SHARPE_LASTTHREEYEAR + "</column>");
		sb.append("<column>" + PortfolioItem.SHARPE_LASTFIVEYEAR + "</column>");
		sb.append("<column>State</column>");
		sb.append("</tablehead>");
	}
	
	public static void strategyShow(List<StrategyItem> strategies, StringBuffer sb){
		sb.append("<Data>");
		for(int i = 0; i < strategies.size(); i++){
			StrategyItem s = strategies.get(i);
			sb.append("<security>");
			sb.append("<ID P=\"Number\">" + s.getID() + "</ID>");
			sb.append("<StrategyName>" + s.getName() + "</StrategyName>");
			sb.append("<PortfolioName>" + ((s.getPortfolioName() != null)?s.getPortfolioName():"NA") + "</PortfolioName>");
			sb.append("<LastValidDate>" + ((s.getLastValidDate() != null)?s.getLastValidDate():"NA") + "</LastValidDate>");
			sb.append("<" + PortfolioItem.AR_LASTONEYEAR + " P=\"Number\">" + ((s.getPortfolioName() != null)?s.getAR1():"NA")+ "</" + PortfolioItem.AR_LASTONEYEAR + ">");
			sb.append("<" + PortfolioItem.AR_LASTTHREEYEAR + " P=\"Number\">" + ((s.getPortfolioName() != null)?s.getAR3():"NA") + "</" + PortfolioItem.AR_LASTTHREEYEAR + ">");
			sb.append("<" + PortfolioItem.AR_LASTFIVEYEAR + " P=\"Number\">" + ((s.getPortfolioName() != null)?s.getAR5():"NA") + "</" + PortfolioItem.AR_LASTFIVEYEAR + ">");
			sb.append("<" + PortfolioItem.SHARPE_LASTONEYEAR + " P=\"Number\">" + ((s.getPortfolioName() != null)?s.getSharpeRatio1():"NA") + "</" + PortfolioItem.SHARPE_LASTONEYEAR + ">");
			sb.append("<" + PortfolioItem.SHARPE_LASTTHREEYEAR + " P=\"Number\">" + ((s.getPortfolioName() != null)?s.getSharpeRatio3():"NA") + "</" + PortfolioItem.SHARPE_LASTTHREEYEAR + ">");
			sb.append("<" + PortfolioItem.SHARPE_LASTFIVEYEAR + " P=\"Number\">" + ((s.getPortfolioName() != null)?s.getSharpeRatio5():"NA") + "</" + PortfolioItem.SHARPE_LASTFIVEYEAR + ">");
			sb.append("<State>" + ((s.getPortfolioName() != null)?s.getSharpeRatio5():"NA") + "</State>");
			sb.append("<LinkType>" + Configuration.LinkType_Strategy + "</LinkType>");
			sb.append("</security>");
		}
		sb.append("</Data>");
	}
}
