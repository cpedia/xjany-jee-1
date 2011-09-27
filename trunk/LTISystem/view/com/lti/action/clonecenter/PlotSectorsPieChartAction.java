package com.lti.action.clonecenter;

import com.lti.service.PortfolioManager;
import com.lti.service.bo.Portfolio;
import com.lti.system.ContextHolder;
import com.lti.util.StringUtil;

public class PlotSectorsPieChartAction {

	private Long ID;

	private String name;

	PortfolioManager portfolioManager;

	private String filePath;
	
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Long getID() {
		return ID;
	}

	public void setID(Long id) {
		ID = id;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String execute(){

//		try {
//			portfolioManager = ContextHolder.getPortfolioManager();
//			Portfolio p=portfolioManager.get(ID);
//			Map<String,Double> sectors=p.getSectors();
//			name=p.getName();
//			long time = System.currentTimeMillis();
//			filePath=ContextHolder.getImagePath().replace("\\", "/")+ StringUtil.getValidName(name) + time + ".jpg";
//			ChartUtil.PlotPie(name, sectors,ContextHolder.getServletPath()+filePath);
//			
//		} catch (NoPriceException e) {
//			filePath=ContextHolder.getImagePath().replace("\\", "/")+"error.jpg";
//		}
		portfolioManager = ContextHolder.getPortfolioManager();
		Portfolio p=portfolioManager.get(ID);
		name=p.getName();
		filePath=ContextHolder.getImagePath().replace("\\", "/")+ "clonecenter/"+StringUtil.getValidName(name) +  ".jpg";
		return "success";
	}


}
