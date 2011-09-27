package com.lti.action.RFA;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.struts2.components.ActionMessage;

import com.lti.action.Action;
import com.lti.service.CustomizeRegionManager;
import com.lti.service.MutualFundManager;
import com.lti.service.UserManager;
import com.lti.service.bo.CustomizeRegion;
import com.lti.service.bo.MutualFund;
import com.lti.service.bo.MutualFundDailyBeta;
import com.lti.service.bo.User;
import com.lti.system.ContextHolder;
import com.lti.util.CustomizeUtil;
import com.lti.util.FormatUtil;
import com.lti.util.LTIRInterface;
import com.opensymphony.xwork2.ActionSupport;

public class MainAction extends ActionSupport implements Action {
	private static final long serialVersionUID = 1L;
	
	private UserManager userManager;
	
	private MutualFundManager mutualFundManager;
	
	private String symbol;
	private Integer interval;
	private String indexArray;
	private String indexArray2;
	private String startDate;
	private String endDate;
	private Boolean checkHistory;
	private List<String> index;
	private String message;
	
	private Date sDate;
	private Date eDate;
	
	private Long createTime;
	
	private Boolean IsRAA;

	public Boolean getIsRAA() {
		return IsRAA;
	}

	public void setIsRAA(Boolean isRAA) {
		IsRAA = isRAA;
	}
	
	private List<MutualFundDailyBeta> mutualFundDailyBetas;

	public void setMutualFundManager(MutualFundManager mutualFundManager) {
		this.mutualFundManager = mutualFundManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public void validate(){
		if(symbol==null){
			this.addFieldError("symbol", "Symbol doesn't exists!");
			return;
		}
		if(interval==null){
			this.addFieldError("interval", "Interval is invalidate!");
			return;
		}
		if(indexArray==null){
			this.addFieldError("indexArray", "Index is invalidate!");
			return;
		}
		if(checkHistory==null){
			 DateFormat df1 =new SimpleDateFormat("yyyy-MM-dd");
			 DateFormat df2 =new SimpleDateFormat("MM/dd/yyyy");
			 try
			 {
				 sDate=df1.parse(startDate);
				 eDate=df1.parse(endDate);
			 }
			 catch(Exception e)
			 {
				 try
				 {
					 sDate=df2.parse(startDate);
					 eDate=df2.parse(endDate);
				 }
				 catch(Exception ex)
				 {
					 this.addFieldError("startDate", "Parse invalidate!");
				 }
			 }
			if(sDate==null||eDate==null||eDate.before(eDate)){
				this.addFieldError("startDate", "Date is invalidate!");
				return;
			}				
		}
	}
	public String execute(){
		try {
			User user = userManager.getLoginUser();
			Long userID;
			if(user == null){
				userID = 0l;
			}
			else
				userID = user.getID();
			
			String[] index=indexArray.replace(" ","").split(",");
			
			Arrays.sort(index);
			
			this.index=new ArrayList<String>();
			
			indexArray2="";
			for(int i=0;i<index.length;i++){
				this.index.add(index[i]);
				indexArray2+=index[i];
				if(i!=index.length-1)indexArray2+=",";
			}			

			IsRAA = false;
			if(checkHistory!=null)
			{
				createTime = -1L;
				List<MutualFund> mfList = mutualFundManager.get(symbol, index, createTime, IsRAA);
				if(mfList!=null && mfList.size()>0)
				{
					MutualFund mf = mfList.get(0);
					this.setStartDate(mf.getStartDate().toString());
					this.setEndDate(mf.getEndDate().toString());
				}
			
			}
			else createTime = java.util.Calendar.getInstance().getTime().getTime();
			
			if (checkHistory==null) {
				mutualFundManager.calculateDailyBeta(symbol, interval, index,createTime, sDate, eDate , true,true,true);
			}
			mutualFundDailyBetas=mutualFundManager.getDailyData(symbol, index,createTime,IsRAA);
						
			
			for(int i=0;i<mutualFundDailyBetas.size();i++){
				MutualFundDailyBeta beta=mutualFundDailyBetas.get(i);
				int arrayLength = beta.getBetas().length/2;
				List<String> betaList=new ArrayList<String>();
				for(int j=0;j<arrayLength;j++){
					betaList.add(FormatUtil.formatPercentage(beta.getBetas()[j])+"("+FormatUtil.formatPercentage(beta.getBetas()[j+arrayLength])+")");
				}
				
				beta.setBetaList(betaList);
				String RSQuare=FormatUtil.formatQuantity(beta.getRSquare());
				beta.setRSquareString(RSQuare);
				
			}
			
			return Action.SUCCESS;
		} catch (Exception e) {
			message=e.getMessage();
			e.printStackTrace();
			return Action.INPUT;
		}
	}


	public String getSymbol() {
		return symbol;
	}


	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}


	public Integer getInterval() {
		return interval;
	}


	public void setInterval(Integer interval) {
		this.interval = interval;
	}


	public String getIndexArray() {
		return indexArray;
	}


	public void setIndexArray(String indexArray) {
		this.indexArray = indexArray;
	}


	public String getStartDate() {
		return startDate;
	}


	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}


	public String getEndDate() {
		return endDate;
	}


	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}


	public List<MutualFundDailyBeta> getMutualFundDailyBetas() {
		return mutualFundDailyBetas;
	}


	public void setMutualFundDailyBetas(List<MutualFundDailyBeta> mutualFundDailyBetas) {
		this.mutualFundDailyBetas = mutualFundDailyBetas;
	}


	public List<String> getIndex() {
		return index;
	}


	public void setIndex(List<String> index) {
		this.index = index;
	}


	public Boolean getCheckHistory() {
		return checkHistory;
	}


	public void setCheckHistory(Boolean checkHistory) {
		this.checkHistory = checkHistory;
	}


	public String getIndexArray2() {
		return indexArray2;
	}


	public void setIndexArray2(String indexArray2) {
		this.indexArray2 = indexArray2;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

}
