package com.lti.action.portfolio;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.struts2.ServletActionContext;

import com.lti.action.Action;
import com.lti.permission.PortfolioPermissionChecker;
import com.lti.service.PortfolioManager;
import com.lti.service.bo.HoldingItem;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.PortfolioInf;
import com.lti.service.bo.Security;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.type.executor.StrategyBasicInf;
import com.lti.type.executor.StrategyInf;
import com.lti.type.finance.Asset;
import com.lti.type.finance.HoldingInf;

public class ParseHoldingInfAction {

	private Long ID;
	private String holdingText;
	private HoldingInf holding;
	private Portfolio portfolio;

	private String message;

	public String parseholding() {

		PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();
		com.lti.service.SecurityManager securityManager = ContextHolder
				.getSecurityManager();
		try {
			HoldingInf holdingInf = new HoldingInf();
			portfolio = portfolioManager.get(ID);
			PortfolioPermissionChecker pc = new PortfolioPermissionChecker(
					portfolio, ServletActionContext.getRequest());
			boolean isOwner = pc.isOwner();
			boolean isAdmin = pc.isAdmin();
			if (!isOwner && !isAdmin) {
				message = "The operation cannot be completed by permission issues.";
				return Action.MESSAGE;
			}
			String[] lines = holdingText.replaceAll("\r\n", "\n").split("\n");
			// 获得页面每一行的数据
 			double allAmount = 0.0; 
			for (String line : lines) {
				String[] arr = line.split("\\s+");
				int len=arr.length;
				String symbol = arr[len-2];
				String assetname = line.substring(0,line.lastIndexOf(symbol)).trim();
				double amount = 0.0;
				try {
					amount = Double.parseDouble(arr[len-1]);
				} catch (Exception e) {
				}
				Asset asset = holdingInf.getAsset(assetname);
				if (asset == null) {
					asset = new Asset();
					asset.setName(assetname);
					try {
						holdingInf.addAsset(asset);
					} catch (Exception e) {
					}
				}
				//
				allAmount = holdingInf.getAmount() + amount;
				holdingInf.setAmount(allAmount);
				double asserAmount = amount;
				asserAmount = asserAmount + asset.getAmount();
				asset.setAmount(asserAmount);
				
				

				HoldingItem hi = new HoldingItem();
				hi.setAssetName(assetname);
				hi.setPortfolioID(ID);
				Security security = securityManager.get(symbol);
				
				
				
				if (security == null)
					continue;
				
				if(portfolio.getStartingDate().before(security.getStartDate())){
					portfolio.setStartingDate(security.getStartDate());
				}
				
				hi.setSecurityID(security.getID());
				hi.setShare(amount);
				hi.setSymbol(security.getSymbol());
				//hi.setDate(portfolio.getStartingDate());
				if (asset.getHoldingItems() == null) {
					asset.setHoldingItems(new ArrayList<HoldingItem>());
				}
				hi.setReInvest(true);
				asset.getHoldingItems().add(hi);
			}
			for(HoldingItem hi:holdingInf.getHoldingItems()){
				double shares = 0.0;
				try {
					double price = securityManager.getPrice(hi.getSecurityID(),
							portfolio.getStartingDate());
					hi.setPrice(price);
					if (price != 0.0) {
						shares = hi.getShare() / price;
					}
				} catch (Exception e) {
					// e.printStackTrace();
				}
				hi.setShare(shares);
				hi.setDate(portfolio.getStartingDate());
			}
			
			
			StrategyInf si = portfolio.getStrategies();
			si.getAssetStrategies().clear();
			for (Asset asset : holdingInf.getAssets()) {
				StrategyBasicInf sbi = new StrategyBasicInf();
				sbi.setAssetName(asset.getName());
				sbi.setID(0l);
				sbi.setName("STATIC");
				sbi.setParameter(new HashMap<String, String>());
//				asset.setTargetPercentage(1.0/holdingInf.getAssets().size());
				DecimalFormat df = new DecimalFormat("#,##0.00"); 
				df.setRoundingMode(RoundingMode.HALF_UP);
				asset.setTargetPercentage(Double.valueOf(df.format(asset.getAmount()
						/ holdingInf.getAmount())));
			}
			portfolioManager.update(portfolio);
			PortfolioInf portfolioInf = portfolioManager.getPortfolioInf(ID,
					Configuration.PORTFOLIO_HOLDING_ORIGINAL);
			if (portfolioInf == null) {
				portfolioInf = new PortfolioInf();
				portfolioInf.setPortfolioID(ID);
				portfolioInf.setHolding(holdingInf);
				portfolioInf.setType(Configuration.PORTFOLIO_HOLDING_ORIGINAL);
				portfolioManager.savePortfolioInf(portfolioInf);
			} else {
				portfolioInf.setHolding(holdingInf);
				portfolioManager.updatePortfolioInf(portfolioInf);
			}
		} catch (Exception e) {
			ServletActionContext.getResponse().setStatus(500);
			message = "Error.";
			e.printStackTrace();
		}
		message = "The operation completed successfully.";

		return Action.MESSAGE;
	}

	public static void main(String[] args) {

		PortfolioManager pm = ContextHolder.getPortfolioManager();
		List<Portfolio> ps = pm.getSimplePortfolios(-1, -1);
		for (Portfolio p : ps) {
			ParseHoldingInfAction ph = new ParseHoldingInfAction();
			ph.setID(p.getID());
			ph.formatholding();
			System.out.println(p.getName());
			System.out.println(ph.getHoldingText());
			System.out.println("\r\n\r\n\r\n\r\n");
		}
	}

	public String formatholding() {
		PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();
		HoldingInf holdingInf = null;
		try {
			holdingInf = portfolioManager.getOriginalHolding(ID);
		} catch (Exception e1) {
		}

		com.lti.service.SecurityManager securityManager = ContextHolder
				.getSecurityManager();
		portfolio = portfolioManager.get(ID);
		PortfolioPermissionChecker pc = new PortfolioPermissionChecker(
				portfolio, ServletActionContext.getRequest());
		boolean isOwner = pc.isOwner();
		boolean isAdmin = pc.isAdmin();
		if (!isOwner && !isAdmin) {
			message = "The operation cannot be completed by permission issues.";
			return Action.MESSAGE;
		}

		StringBuffer sb = new StringBuffer();
		if (holdingInf == null || portfolio == null) {
			holdingText = "";
			return Action.SUCCESS;
		}
		if (holdingInf.getAssets() != null && holdingInf.getAssets().size() > 0) {
			for (Asset asset : holdingInf.getAssets()) {
				if (asset.getHoldingItems() != null
						&& asset.getHoldingItems().size() > 0) {
					for (HoldingItem hi : asset.getHoldingItems()) {
						sb.append(asset.getName());
						sb.append("\t");
						sb.append(hi.getSymbol());
						sb.append("\t");
						try {
							sb.append(hi.getShare()
									* securityManager.getPrice(
											hi.getSecurityID(),
											portfolio.getStartingDate()));
						} catch (Exception e) {
							sb.append("0.0");
						}
						sb.append("\r\n");
					}
				}
			}
		}
		holdingText = sb.toString();
		return Action.SUCCESS;
	}

	public Long getID() {
		return ID;
	}

	public void setID(Long iD) {
		ID = iD;
	}

	public String getHoldingText() {
		return holdingText;
	}

	public void setHoldingText(String holdingText) {
		this.holdingText = holdingText;
	}

	public HoldingInf getHolding() {
		return holding;
	}

	public void setHolding(HoldingInf holding) {
		this.holding = holding;
	}

	public Portfolio getPortfolio() {
		return portfolio;
	}

	public void setPortfolio(Portfolio portfolio) {
		this.portfolio = portfolio;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
