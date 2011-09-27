package com.lti.action.betagain;

import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.lti.action.Action;
import com.lti.service.MutualFundManager;
import com.lti.service.UserManager;
import com.lti.service.bo.BetagainDailyData;
import com.lti.service.bo.User;
import com.lti.system.Configuration;
import com.lti.util.FormatUtil;
import com.lti.util.LTIDate;
import com.opensymphony.xwork2.ActionSupport;

public class MainAction extends ActionSupport implements Action {
	private static final long serialVersionUID = 1L;

	private UserManager userManager;

	private MutualFundManager mutualFundManager;

	private String symbol;
	private Integer interval;
	private Integer gainInterval;
	private String indexArray;
	private String indexArray2;
	private String startDate;
	private String endDate;
	private List<String> index;
	private String type;

	private String LSType;
	private Boolean isWLSorOLS;
	private Boolean isSigmaOne;

	private Date sDate;
	private Date eDate;

	private List<BetagainDailyData> betagainDailyDatas;
	
	private String filename_beta;
	private String filename_gain;
	private String address;
	private String port;
	private List<BetagainDailyData> betaGains;
	private Boolean IsBetaGain;
	private Boolean IsResult;
	private List<Integer> types;
	private String message;

	public List<BetagainDailyData> getBetaGains() {
		return betaGains;
	}

	public void setBetaGains(List<BetagainDailyData> betaGains) {
		this.betaGains = betaGains;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}


	public void setMutualFundManager(MutualFundManager mutualFundManager) {
		this.mutualFundManager = mutualFundManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public void ouputbeta() throws Exception {
		int random=(int)Math.random()*1000;
		File file=new File(Configuration.getTempDir()+random+"_beta_"+new Date().getTime());
		filename_beta= file.getName();
		FileWriter sb=new FileWriter(file);
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<Beta><Data>");

		if (indexArray == null) {
			sb.append("</Data></Beta>");
			sb.flush();
			sb.close();
			return;
		}

		String[] index = indexArray.split(",");

		if (index == null) {
			sb.append("</Data></Beta>");
			sb.flush();
			sb.close();
			return;
		}

		Arrays.sort(index);

		if (betagainDailyDatas == null) {
			sb.append("</Data></Beta>");
			sb.flush();
			sb.close();
			return;
		}

		for (int i = 0; i < betagainDailyDatas.size(); i++) {
			BetagainDailyData beta = betagainDailyDatas.get(i);

			String betas = "";
			for (int j = 0; j < beta.getBetas().length; j++) {
				betas += FormatUtil.formatQuantityWithLen(beta.getBetas()[j],6);
				if (j != beta.getBetas().length - 1)
					betas += ",";
			}
			sb.append("<E d='" + beta.getDate() + "' v='" + betas + "'/>");
			sb.append("\n");
		}

		sb.append("</Data></Beta>");

		sb.flush();
		sb.close();
		
		

	}

	public void ouputgain() throws Exception {
		int random=(int)Math.random()*1000;
		File file=new File(Configuration.getTempDir()+random+"_gain_"+new Date().getTime());
		filename_gain= file.getName();
		FileWriter sb=new FileWriter(file);
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<Beta><Data>");

		if (indexArray == null) {
			sb.append("</Data></Beta>");
			sb.flush();
			sb.close();
			return;
		}

		String[] index = indexArray.split(",");

		if (index == null) {
			sb.append("</Data></Beta>");
			sb.flush();
			sb.close();
			return;
		}

		Arrays.sort(index);

		if (betagainDailyDatas == null) {
			sb.append("</Data></Beta>");
			sb.flush();
			sb.close();
			return;
		}

		for (int i = 0; i < betagainDailyDatas.size(); i++) {
			BetagainDailyData beta = betagainDailyDatas.get(i);

			String betas = "";
			for (int j = 0; j < beta.getGains().length; j++) {
				betas += FormatUtil.formatQuantityWithLen(beta.getGains()[j],6);
				if (j != beta.getGains().length - 1)
					betas += ",";
			}
			sb.append("<E d='" + beta.getDate() + "' v='" + betas + "'/>");
			sb.append("\n");
		}

		sb.append("</Data></Beta>");

		sb.flush();
		sb.close();
		
		

	}
	public void validate() {
		User user = userManager.getLoginUser();
		Long userID;
		if (user == null) {
			userID = 0l;
		} else
			userID = user.getID();

		/*boolean betagain = userManager.HasRole(Configuration.ROLE_RAA, userID);
		if (betagain == false) {
			addActionError("You can't execute the RAA tool, because you have no right!");
		}*/

		if (symbol == null) {
			this.addFieldError("symbol", "Symbol doesn't exists!");
			return;
		}
		if (interval == null) {
			this.addFieldError("interval", "Interval is invalidate!");
			return;
		}
		/*if (gainInterval == null) {
			this.addFieldError("gainInterval", "Gain Interval is invalidate!");
			return;
		}*/
		if (indexArray == null) {
			this.addFieldError("indexArray", "Index is invalidate!");
			return;
		}

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

		if(this.getLSType().equalsIgnoreCase("WLS"))
			this.setIsWLSorOLS(Boolean.TRUE);
		else
			this.setIsWLSorOLS(Boolean.FALSE);
	}

	public String execute() throws Exception {
		try {

			String[] index = indexArray.replace(" ", "").split(",");

			Arrays.sort(index);

			this.index = new ArrayList<String>();

			indexArray2 = "";
			for (int i = 0; i < index.length; i++) {
				this.index.add(index[i]);
				indexArray2 += index[i];
				if (i != index.length - 1)
					indexArray2 += ",";
			}

			int size = index.length;

			String[] types = type.replace(" ", "").split(",");
			
			//mutualFundManager.caculateEndBetaGain(symbol, interval, index, types, sDate, eDate, isSigmaOne,isWLSorOLS);
			
			betagainDailyDatas = mutualFundManager.calculateBetaGain(symbol, interval, index, types, sDate, eDate, gainInterval, isSigmaOne,isWLSorOLS);

			for (int i = 0; i < betagainDailyDatas.size(); i++) {
				BetagainDailyData beta = betagainDailyDatas.get(i);
				List<String> betaGainList = new ArrayList<String>();
				Double[] betaGains = beta.getGains();
				Double[] betas = beta.getBetas();
				for (int j = 0, len = betaGains.length; j < len; j++) {
					betaGainList.add(FormatUtil.formatQuantityWithLen(betaGains[j],6) + "(" + FormatUtil.formatQuantityWithLen(betas[j],6) + ")");
				}

				beta.setGainList(betaGainList);
			}

			this.ouputbeta();
			this.ouputgain();
			IsBetaGain = true;
			IsResult = true;
			return Action.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			addActionError(e.getMessage());
			addActionMessage(e.getMessage());
			message = e.getMessage();
			throw e;
			// return Action.INPUT;
		}
	}
	
	public String betaGainTable(){
		String[] index = indexArray.replace(" ", "").split(",");
		String[] types = type.replace(" ", "").split(",");
		this.index = new ArrayList<String>();
		this.types = new ArrayList<Integer>();
		if(index.length != types.length)
		{
			addActionError("Indexes settings are not right!");
			addActionMessage("Indexes settings are not right!");
		}
		indexArray2 = "";
		for (int i = 0; i < index.length; i++) {
			this.index.add(index[i]);
			indexArray2 += index[i];
			this.types.add(Integer.parseInt(types[i]));
			if (i != index.length - 1)
				indexArray2 += ",";
		}
		IsBetaGain = true;
		IsResult = true;
		try {
			betaGains = mutualFundManager.calculateEndBetaGain(symbol, interval, index, types, sDate, eDate, isSigmaOne, isWLSorOLS);
		} catch (Exception e) {
			// TODO: handle exception
			addActionError(e.getMessage());
			addActionMessage(e.getMessage());
			message = e.getMessage();
			return Action.SUCCESS;
		}
		if(betaGains != null && betaGains.size() > 0){
			Double[] betas = betaGains.get(0).getBetas();
			for(int j = 0; j < betas.length; j++){
				String value = FormatUtil.formatQuantity(betas[j]);
				if(!value.equalsIgnoreCase("NA")){
					betas[j] = Double.parseDouble(value);
				}
				else
				{
					betas[j] = null;
				}
			}
			for(int i = 0; i < betaGains.size(); i++){
				BetagainDailyData bdd = betaGains.get(i);
				Double[] gains = bdd.getGains();
				for(int j = 0; j < gains.length; j++){
					String value = FormatUtil.formatPercentage(gains[j]);
					if(!value.equalsIgnoreCase("NA")){
						gains[j] = Double.parseDouble(value);
					}
					else
					{
						gains[j] = null;
					}
				}
			}
		}
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

	public String getIndexArray2() {
		return indexArray2;
	}

	public void setIndexArray2(String indexArray2) {
		this.indexArray2 = indexArray2;
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

	public List<String> getIndex() {
		return index;
	}

	public void setIndex(List<String> index) {
		this.index = index;
	}

	public Date getSDate() {
		return sDate;
	}

	public void setSDate(Date date) {
		sDate = date;
	}

	public Date getEDate() {
		return eDate;
	}

	public void setEDate(Date date) {
		eDate = date;
	}

	public List<BetagainDailyData> getBetagainDailyDatas() {
		return betagainDailyDatas;
	}

	public void setBetagainDailyDatas(List<BetagainDailyData> betagainDailyDatas) {
		this.betagainDailyDatas = betagainDailyDatas;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getGainInterval() {
		return gainInterval;
	}

	public void setGainInterval(Integer gainInterval) {
		this.gainInterval = gainInterval;
	}

	public Boolean getIsSigmaOne() {
		return isSigmaOne;
	}

	public void setIsSigmaOne(Boolean isSigmaOne) {
		this.isSigmaOne = isSigmaOne;
	}

	public String getFilename_beta() {
		return filename_beta;
	}

	public void setFilename_beta(String filename_beta) {
		this.filename_beta = filename_beta;
	}

	public String getFilename_gain() {
		return filename_gain;
	}

	public void setFilename_gain(String filename_gain) {
		this.filename_gain = filename_gain;
	}

	public String getLSType() {
		return LSType;
	}

	public void setLSType(String type) {
		LSType = type;
	}

	public Boolean getIsWLSorOLS() {
		return isWLSorOLS;
	}

	public void setIsWLSorOLS(Boolean isWLSorOLS) {
		this.isWLSorOLS = isWLSorOLS;
	}

	public Boolean getIsBetaGain() {
		return IsBetaGain;
	}

	public void setIsBetaGain(Boolean isBetaGain) {
		IsBetaGain = isBetaGain;
	}

	public Boolean getIsResult() {
		return IsResult;
	}

	public void setIsResult(Boolean isResult) {
		IsResult = isResult;
	}

	public List<Integer> getTypes() {
		return types;
	}

	public void setTypes(List<Integer> types) {
		this.types = types;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
