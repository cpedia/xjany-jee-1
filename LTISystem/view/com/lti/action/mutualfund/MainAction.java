package com.lti.action.mutualfund;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.sound.midi.SysexMessage;

import org.apache.struts2.components.ActionMessage;

import com.lti.action.Action;
import com.lti.service.CustomizeRegionManager;
import com.lti.service.MutualFundManager;
import com.lti.service.UserManager;
import com.lti.service.bo.CustomizeRegion;
import com.lti.service.bo.MutualFund;
import com.lti.service.bo.MutualFundDailyBeta;
import com.lti.service.bo.User;
import com.lti.system.Configuration;
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
	private Boolean allowNegative;
	private List<String> index;
	private String message;
	
	private Date sDate;
	private Date eDate;
	
	private Long createTime;
	private Boolean IsRAA;
	
	private String lowerArray;
	private String upperArray;
	
	private List<String> indexes;
	
	private List<Double> lowers;
	
	private List<Double> uppers;
	
	private Boolean IsResult;
	
	private int IsFirst;
	
	private boolean WLSorOLS;
	
	private boolean SumToOne;
	
	private boolean withConstraint;
	
	private String actionMessage;
	
	private List<MutualFundDailyBeta> mutualFundDailyBetas;

	public boolean isWLSorOLS() {
		return WLSorOLS;
	}

	public void setWLSorOLS(boolean sorOLS) {
		WLSorOLS = sorOLS;
	}

	public boolean isSumToOne() {
		return SumToOne;
	}

	public void setSumToOne(boolean sumToOne) {
		SumToOne = sumToOne;
	}

	public void setMutualFundManager(MutualFundManager mutualFundManager) {
		this.mutualFundManager = mutualFundManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public void validate(){
		User user = userManager.getLoginUser();
		Long userID;
		if(user == null){
			userID = 0l;
		}
		else
			userID = user.getID();
		
		boolean raa = userManager.HasRole(Configuration.ROLE_RAA, userID);
		if(raa == false){
			this.setActionMessage("You can't execute the RAA tool, because you have no right!Please Login in.");
			addActionError("You can't execute the RAA tool, because you have no right!Please Login in.");
		}
		else
			this.setActionMessage("");
		
		if (IsResult != null && IsResult == true) {
			if (symbol == null) {
				this.addFieldError("symbol", "Symbol doesn't exists!");
				return;
			}
			if (interval == null) {
				this.addFieldError("interval", "Interval is invalidate!");
				return;
			}
			if (indexArray == null) {
				this.addFieldError("indexArray", "Index is invalidate!");
				return;
			}
			if (checkHistory == null) {
				DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
				DateFormat df2 = new SimpleDateFormat("MM/dd/yyyy");
				try {
					sDate = df1.parse(startDate);
					eDate = df1.parse(endDate);
				} catch (Exception e) {
					try {
						sDate = df2.parse(startDate);
						eDate = df2.parse(endDate);
					} catch (Exception ex) {
						this.addFieldError("startDate", "Parse invalidate!");
					}
				}
				if (sDate == null || eDate == null || eDate.before(eDate)) {
					this.addFieldError("startDate", "Date is invalidate!");
					return;
				}

			}
		}
		else
		{
			String[] index = indexArray.replace(" ","").split(",");
			indexes = new ArrayList<String>();
			if(index != null){
				for(int i = 0; i < index.length; i++){
					indexes.add(index[i]);
				}
			}
			String[] upperList = upperArray.replace(" ", "").split(",");
			String[] lowerList = lowerArray.replace(" ", "").split(",");
			
			if((upperList==null || upperList.length==0) && (lowerList==null || lowerList.length==0));
			else{
				int size = index.length;
				uppers = new ArrayList<Double>();
				lowers = new ArrayList<Double>();
				for(int i =0 ;i < size; i++){
					Double upper;
					if(upperList[i]!=null&&!upperList[i].equals("*"))
						upper = Double.parseDouble(upperList[i]);
					else 
						upper = 100000.0;
					uppers.add(upper);
					Double lower;
					if(lowerList[i]!=null&&!lowerList[i].equals("*"))
						lower = Double.parseDouble(lowerList[i]);
					else 
						lower = -100000.0;
					lowers.add(lower);
				}
			}
		}
		IsRAA = true;
	}
	public String execute(){
		try {
			if(IsResult && IsFirst == 0){
				String[] index=indexArray.replace(" ","").split(",");
				
				Arrays.sort(index);
				
				this.index=new ArrayList<String>();
				indexes = new ArrayList<String>();
				indexArray2="";
				for(int i=0;i<index.length;i++){
					this.index.add(index[i]);
					indexArray2+=index[i];
					indexes.add(index[i]);
					if(i!=index.length-1)indexArray2+=",";
				}

				IsRAA = true;
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
					int size = index.length;
					double[] upper = null;
					double[] lower = null;
					String[] upperList = upperArray.replace(" ", "").split(",");
					String[] lowerList = lowerArray.replace(" ", "").split(",");
					if((upperList==null || upperList.length==0) && (lowerList==null || lowerList.length==0));
					else
					{
						upper = new double[size];
						lower = new double[size];
						uppers = new ArrayList<Double>();
						lowers = new ArrayList<Double>();
						for(int i=0;i<size;i++){
							if(upperList[i]!=null&&!upperList[i].equals("*"))
								upper[i] = Double.parseDouble(upperList[i]);
							else 
								upper[i]= 100000.0;
							uppers.add(upper[i]);
							
							if(lowerList[i]!=null&&!lowerList[i].equals("*"))
								lower[i] = Double.parseDouble(lowerList[i]);
							else 
								lower[i]= -100000.0;
							lowers.add(lower[i]);
						}
					}
					mutualFundManager.setupLimit(upper, lower);
					mutualFundManager.calculateDailyBeta(symbol, interval,index,createTime, sDate, eDate, withConstraint,WLSorOLS,SumToOne);
					
				}
				mutualFundDailyBetas=mutualFundManager.getDailyData(symbol, index,createTime,IsRAA);
				
				for(int i=0;i<mutualFundDailyBetas.size();i++){
					MutualFundDailyBeta beta=mutualFundDailyBetas.get(i);
					List<String> betaList=new ArrayList<String>();
					for(int j=0;j<beta.getBetas().length;j++){				
						betaList.add(FormatUtil.formatPercentage(beta.getBetas()[j]));
					}
					
					beta.setBetaList(betaList);
					String RSQuare=FormatUtil.formatQuantity(beta.getRSquare());
					beta.setRSquareString(RSQuare);
				}
				
				return Action.SUCCESS;
			}
			return Action.INPUT;
		} catch (Exception e) {
			message=e.getMessage();
			return Action.INPUT;
		}
	}

	public String resultPage(){
		if(IsFirst == 1)
			IsFirst = 0;
		else
			IsResult = true;
		return Action.SUCCESS;
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


	public Boolean getAllowNegative() {
		return allowNegative;
	}


	public void setAllowNegative(Boolean allowNegative) {
		this.allowNegative = allowNegative;
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

	public String getUpperArray() {
		return upperArray;
	}

	public void setUpperArray(String upperArray) {
		this.upperArray = upperArray;
	}

	public UserManager getUserManager() {
		return userManager;
	}

	public String getLowerArray() {
		return lowerArray;
	}

	public void setLowerArray(String lowerArray) {
		this.lowerArray = lowerArray;
	}

	public List<String> getIndexes() {
		return indexes;
	}

	public void setIndexes(List<String> indexes) {
		this.indexes = indexes;
	}

	public List<Double> getLowers() {
		return lowers;
	}

	public void setLowers(List<Double> lowers) {
		this.lowers = lowers;
	}

	public List<Double> getUppers() {
		return uppers;
	}

	public void setUppers(List<Double> uppers) {
		this.uppers = uppers;
	}

	public Boolean getIsResult() {
		return IsResult;
	}

	public void setIsResult(Boolean isResult) {
		IsResult = isResult;
	}

	public boolean isWithConstraint() {
		return withConstraint;
	}

	public void setWithConstraint(boolean withConstraint) {
		this.withConstraint = withConstraint;
	}

	public String getActionMessage() {
		return actionMessage;
	}

	public void setActionMessage(String actionMessage) {
		this.actionMessage = actionMessage;
	}

	public int getIsFirst() {
		return IsFirst;
	}

	public void setIsFirst(int isFirst) {
		IsFirst = isFirst;
	}

	
}
