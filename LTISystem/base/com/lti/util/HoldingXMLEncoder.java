package com.lti.util;

import java.beans.XMLEncoder;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.*; 

import org.dom4j.*;

import com.lti.system.ContextHolder;
import com.lti.service.PortfolioManager;
import com.lti.service.bo.Portfolio;
@Deprecated
public class HoldingXMLEncoder {
	
//	public static String objectXmlEncoder(Object obj) throws Exception {
//
//		java.io.ByteArrayOutputStream fos = new java.io.ByteArrayOutputStream();
//		XMLEncoder encoder = new XMLEncoder(fos);
//		encoder.writeObject(obj);
//		encoder.flush();
//		encoder.close();
//		fos.close();
//		return fos.toString();
//	}
//	
//	public static String holdingToXML(Portfolio portfolio, Date date){
//		portfolio.translateAssetList(date);
//		List<Asset> assets = portfolio.getAssetList();
//		if(assets == null || assets.size() < 1){
//			return null;
//		}
//		Document doc = DocumentHelper.createDocument(); 
//		
//		//create the root element
//		Element root = doc.addElement("Holdings"); 
//		//add the root element to the doc 
//
//		for(int i = 0; i < assets.size(); i++){
//			Asset a = assets.get(i);
//			Element asset = root.addElement("asset");
//			asset.addAttribute("name", a.getName());
//			asset.addAttribute("type", a.getClassName());
//			asset.addAttribute("strategyname", a.getAssetStrategyName());
//			asset.addAttribute("strategyID", a.getAssetStrategyID().toString());
//			Hashtable<String, String> parameters = a.getAssetStrategyParameter();
//			String paras = "";
//			if(parameters != null){
//				paras = PortfolioHoldingUtil.translateParaToString(parameters);
//			}
//			asset.addAttribute("paras", paras);
//			asset.addAttribute("target", a.getTargetPercentage().toString());
//			if(a.getPercentage() != null && a.getAmount() != null){
//				asset.addAttribute("actual", a.getPercentage().toString());
//				asset.addAttribute("amount", a.getAmount().toString());
//			}
//			else
//			{
//				asset.addAttribute("actual", "");
//				asset.addAttribute("amount", "");
//			}
//			//asset.addAttribute("portfolioID", portfolio.getPortfolioID().toString());
//			//Element securities = asset.addElement("Securities");
//			List<SecurityItem> securityList = a.getSecurityList();
//			if(securityList != null && securityList.size() > 0){
//				for(int j = 0; j < securityList.size(); j++){
//					Element security = asset.addElement("security");
//					SecurityItem si = securityList.get(j);
//					security.addAttribute("symbol", si.getSymbol());
//					security.addAttribute("shares", (si.getShares()==null)?null:si.getShares().toString());
//					security.addAttribute("price", (si.getPrice()==null)?null:si.getPrice().toString());
//					security.addAttribute("reinvested", si.getReinvesting().toString());
//					security.addAttribute("amount", (si.getPercentage()==null)?null:si.getTotalAmount().toString());
//					security.addAttribute("percentage", (si.getPercentage()==null)?null:si.getPercentage().toString());
//				}
//			}
//		}
//
//		String s = doc.asXML();
//		return s;
//	}
//	
//	public static void main(String[] args){
//		
//		try {
//			PortfolioManager portfolioManager = (PortfolioManager)com.lti.system.ContextHolder.getInstance().getApplicationContext().getBean("portfolioManager");
//			System.out.println(holdingToXML(portfolioManager.get(3348L), new Date()));
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}
//		
//	}
}
