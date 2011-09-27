package com.lti.action.flash;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.action.Action;
import com.lti.service.MutualFundManager;
import com.lti.service.bo.MutualFundDailyBeta;
import com.lti.util.FormatUtil;
import com.opensymphony.xwork2.ActionSupport;

public class OutputBetaAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(OutputBetaAction.class);

	private String resultString;
	
	private String symbol;
	
	private String indexArray;
	
	private Long createTime;
	
	private Boolean IsRAA;
		
	private MutualFundManager mutualFundManager;
	
	public void setMutualFundManager(MutualFundManager mutualFundManager) {
		this.mutualFundManager = mutualFundManager;
	}
	

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
		
		List<MutualFundDailyBeta> mutualFundDailyBetas=mutualFundManager.getDailyData(symbol, index,createTime,IsRAA);
				
		if(mutualFundDailyBetas==null){
			sb.append("</Data></Beta>");
			resultString=sb.toString();
			return Action.SUCCESS;
		}
		
		for(int i=0;i<mutualFundDailyBetas.size();i++){
			MutualFundDailyBeta beta=mutualFundDailyBetas.get(i);
			
			String betas="";
			for(int j=0;j<beta.getBetas().length;j++){
				betas+=FormatUtil.formatQuantityWithLen(beta.getBetas()[j],6);
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


	public Long getCreateTime() {
		return createTime;
	}


	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}


	public Boolean getIsRAA() {
		return IsRAA;
	}


	public void setIsRAA(Boolean isRAA) {
		IsRAA = isRAA;
	}
}
