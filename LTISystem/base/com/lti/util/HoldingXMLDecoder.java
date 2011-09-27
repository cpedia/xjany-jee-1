package com.lti.util;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.dom4j.*;

import com.lti.service.PortfolioManager;
import com.lti.service.bo.Portfolio;
import com.lti.system.ContextHolder;
@Deprecated
public class HoldingXMLDecoder {
//	public static List<Asset> XMLToHoldings(String xml){
//		Document doc = null; 
//		try {
//			doc = DocumentHelper.parseText(xml);
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//			return null;
//		}
//		Element root = doc.getRootElement();
//		List assetList = root.selectNodes("asset");
//		List<Asset> assets = new ArrayList<Asset>();
//		if(assetList != null){
//			for(int i = 0; i < assetList.size(); i++){
//				Element asset = (Element) assetList.get(i);
//				Asset a = new Asset();
//				a.setName(asset.attributeValue("name"));
//				a.setAssetStrategyName(asset.attributeValue("strategyname"));
//				if(asset.attributeValue("strategyID") != null){
//					a.setAssetStrategyID(Long.parseLong(asset.attributeValue("strategyID")));
//				}
//				a.setClassName(asset.attributeValue("type"));
//				if (asset.attributeValue("target") != null) {
//					String targetStr = asset.attributeValue("target");
//					Double target = Double.parseDouble(targetStr);
//					a.setTargetPercentage(target);
//					a.setTargetPercentageStr(FormatUtil.formatQuantity(target * 100) + "%");
//				}
//				//a.setTargetPercentageStr(asset.attributeValue("target"));
//				String percentageStr = asset.attributeValue("actual");
//				if(percentageStr != null){
//					Double percentage = Double.parseDouble(percentageStr);
//					a.setPercentage(percentage);
//				}
//				String amountStr = asset.attributeValue("amount");
//				if(amountStr != null){
//					Double amount = Double.parseDouble(amountStr);
//					a.setAmount(amount);
//				}
//				String paras = asset.attributeValue("paras");
//				if(paras != null && !paras.equals("")){
//					Hashtable<String, String> parameters = PortfolioHoldingUtil.translateStringToPara(paras);
//					a.setAssetStrategyParameter(parameters);
//					//System.out.println(a.getAssetStrategyParameter().size());
//				}
//				//System.out.println(asset.attributeValue("name"));
//				List securityList = asset.selectNodes("security");
//				List<SecurityItem> securities = new ArrayList<SecurityItem>();
//				if(securityList != null){
//					for(int j = 0; j < securityList.size(); j++){
//						Element security = (Element) securityList.get(j);
//						SecurityItem si = new SecurityItem();
//						si.setSymbol(security.attributeValue("symbol"));
//						String sharesStr = security.attributeValue("shares");
//						if(sharesStr != null){
//							Double shares = Double.parseDouble(sharesStr);
//							si.setShares(shares);
//						}
//						String priceStr = security.attributeValue("price");
//						if(priceStr != null){
//							Double price = Double.parseDouble(priceStr);
//							si.setPrice(price);
//						}
//						String se_amountStr = security.attributeValue("amount");
//						if(se_amountStr != null){
//							Double se_amount = Double.parseDouble(se_amountStr);
//							si.setTotalAmount(se_amount);
//						}
//						String se_percentageStr = security.attributeValue("percentage");
//						if(se_percentageStr != null){
//							Double se_percentage = Double.parseDouble(se_percentageStr);
//							si.setPercentage(se_percentage);
//						}
//						si.setReinvesting(Boolean.parseBoolean(security.attributeValue("reinvested")));
//						//System.out.println(security.attribute("symbol").getValue());
//						securities.add(si);
//					}
//				}
//				a.setSecurityList(securities);
//				assets.add(a);
//			}
//		}
//		return assets;
//	}
	
/*	public static void main(String[] args){
		
		try {
			PortfolioManager portfolioManager = (PortfolioManager)com.lti.system.ContextHolder.getInstance().getApplicationContext().getBean("portfolioManager");
			//String xml = HoldingXMLEncoder.holdingToXML(portfolioManager.get(3348L));
			System.out.println(xml);
			List<Asset> assets = XMLToHoldings(xml);
			System.out.println(assets.size());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}*/
}
