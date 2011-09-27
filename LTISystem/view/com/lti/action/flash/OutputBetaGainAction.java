package com.lti.action.flash;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.action.Action;
import com.lti.service.bo.BetagainDailyData;
import com.lti.util.FormatUtil;
import com.opensymphony.xwork2.ActionSupport;

public class OutputBetaGainAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(OutputBetaGainAction.class);

	private String resultString;
	
	private String symbol;
	
	private String indexArray;
	
	private List<BetagainDailyData> betagainDailyDatas;

	@Override
	public String execute() throws Exception {
		
		
		StringBuffer sb=new StringBuffer();
		
		
		sb.append( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		
		sb.append("<Beta><Data>");
		
		if(indexArray==null){
			sb.append("</Data></Beta>");
			resultString=sb.toString();
			return Action.SUCCESS;
		}
		
		String[] index=indexArray.split(",");
		
		if(index==null){
			sb.append("</Data></Beta>");
			resultString=sb.toString();
			return Action.SUCCESS;
		}
		
		
		Arrays.sort(index);
				
		if(betagainDailyDatas==null){
			sb.append("</Data></Beta>");
			resultString=sb.toString();
			return Action.SUCCESS;
		}
		
		for(int i=0;i<betagainDailyDatas.size();i++){
			BetagainDailyData beta=betagainDailyDatas.get(i);
			
			String betas="";
			for(int j=0;j<beta.getBetas().length;j++){
				betas+=FormatUtil.formatQuantity(beta.getBetas()[j]);
				if(j!=beta.getBetas().length-1)betas+=",";
			}
			sb.append("<E d='"+beta.getDate()+"' v='"+betas+"'/>");
		}
		
		sb.append("</Data></Beta>");
		
		resultString=sb.toString();
		return Action.SUCCESS;

	}//end excute

	
	public String getResultString() {
		return resultString;
	}

	public void setResultString(String resultString) {
		this.resultString = resultString;
	}


	public String getSymbol() {
		return symbol;
	}


	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}


	public String getIndexArray() {
		return indexArray;
	}


	public void setIndexArray(String indexArray) {
		this.indexArray = indexArray;
	}


	public List<BetagainDailyData> getBetagainDailyDatas() {
		return betagainDailyDatas;
	}


	public void setBetagainDailyDatas(List<BetagainDailyData> betagainDailyDatas) {
		this.betagainDailyDatas = betagainDailyDatas;
	}
}
