package com.lti.action.MVO;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.lti.action.Action;
import com.lti.service.UserManager;
import com.lti.service.bo.User;
import com.lti.system.Configuration;
import com.lti.type.TimeUnit;
import com.lti.util.LTIMVOInterface;
import com.opensymphony.xwork2.ActionSupport;

public class MainAction extends ActionSupport implements Action{
	private static final long serialVersionUID = 1L;
	
	private String banchmarkArray;
	private String[] banchmarks;
	private List<String> securityList;
	private String startDate;
	private Date start;
	private Date end;
	private String message;
	private String endDate;
	private double RAF;
	private String unit;
	private TimeUnit timeUnit;
	private List<Double> resultWeights;
	private List<String> weights;
	private String lower;
	private String upper;
	private String expected;
	private List<Double> lowerLimits;
	private List<Double> upperLimits;
	private List<Double> expectedExcessAnnualReturns;
	private String filePathName;
	private UserManager userManager;
	
	private double[][] covariences;
	private String[][] covarienceStrings;
	
	private LTIMVOInterface li;

	public void validate(){
		
		message="";
		
		User user = userManager.getLoginUser();
		Long userID;
		if(user == null){
			userID = 0l;
		}
		else
			userID = user.getID();
		
		boolean raa = userManager.HasRole(Configuration.ROLE_RAA, userID);
		if(raa == false){
			addActionError("You can't execute the MVO tool, because you have no right!");
			message = "You can't execute the MVO tool, because you have no right!";
			return;
		}
		banchmarks=banchmarkArray.replace(" ", "").split(",");
		String[] ss=lower.replace(" ", "").split(",");
		lowerLimits=new ArrayList<Double>();
		upperLimits=new ArrayList<Double>();
		expectedExcessAnnualReturns=new ArrayList<Double>();
		for(String s:ss)
		{
			Double d=Double.parseDouble(s);
			lowerLimits.add(d);
		}
		ss=upper.replace(" ", "").split(",");
		for(String s:ss)
		{
			Double d=Double.parseDouble(s);
			upperLimits.add(d);
		}
		ss=expected.split(",");
		for(String s:ss)
		{
			s=s.replace(" ", "");
			if(s == null || s.length()==0)
				expectedExcessAnnualReturns.add(null);
			else
			{
				Double d=Double.parseDouble(s);
				expectedExcessAnnualReturns.add(d);
			}
		}
		
		if(banchmarks==null)
		{
			message="No banchmark setted, please set at less one.";
			//this.addActionError(message);
			//addFieldError("result", message);
			return;
		}
		if(!startDate.matches("\\d{1,2}/\\d{1,2}/\\d{4}")&&!startDate.matches("\\d{4}-\\d{1,2}-\\d{1,2}"))
		{
			message="Please format the start date to YYYY-MM-DD or MM/DD/YYYY";
			//this.addActionError(message);
			//addFieldError("result", message);
			return;
		}
		if(!endDate.matches("\\d{1,2}/\\d{1,2}/\\d{4}")&&!endDate.matches("\\d{4}-\\d{1,2}-\\d{1,2}"))
		{
			message="Please format the end date to YYYY-MM-DD or MM/DD/YYYY";
			//this.addActionError(message);//addFieldError("result", message);
			return;
		}
		try{
			SimpleDateFormat dateFormat=new SimpleDateFormat ("MM/dd/yyyy");
			start=dateFormat.parse(startDate);
			end=dateFormat.parse(endDate);
		}catch(Exception pe)
		{
			message="Please format the date to YYYY-MM-DD or MM/DD/YYYY";
			//this.addActionError(message);//addFieldError("result", message);
			return;
		}
		if(unit==null)
		{
			message="Time unit is not allowed to be empty.";
			//this.addActionError(message);//addFieldError("result", message);
			return;
		}
		if(RAF<0d||RAF>20d)
		{
			message="RAF should be in the range of 0~20.";
			//this.addActionError(message);//addFieldError("result", message);
			return;
		}
		if(unit.equalsIgnoreCase("monthly"))
		{
			timeUnit=TimeUnit.MONTHLY;
		}
		else if(unit.equalsIgnoreCase("weekly"))
		{
			timeUnit=TimeUnit.WEEKLY;
		}
		else if(unit.equalsIgnoreCase("yearly"))
		{
			timeUnit=TimeUnit.YEARLY;
		}
		else
		{
			message="Time Unit is illegal.";
			//this.addActionError(message);//addFieldError("result", message);
			return;
		}
		

		securityList=new ArrayList<String>();
		for(String s:banchmarks)
			securityList.add(s);
		li = new LTIMVOInterface();
		li.createModel(securityList, lowerLimits, upperLimits, expectedExcessAnnualReturns, RAF, start, end, timeUnit);
		
		message="";
		boolean flag=false;
		List<Double> returns = li.checkExpectedReturns();
		//annualized to one year by ccd
		for(int i=0;i<returns.size();++i)
		{
			double temp=returns.get(i);
			if(timeUnit==TimeUnit.YEARLY){
				//do nothing
			}else if(timeUnit ==TimeUnit.MONTHLY){
				returns.set(i, temp*12);
			}else if(timeUnit == TimeUnit.WEEKLY){
				returns.set(i, temp*52);
			}
		}

		for(int i=0;i<returns.size();i++){
			if(returns.get(i)<0.0)
			{
				flag=true;
				message += securityList.get(i)+"'s expected excess annual return is "+returns.get(i)+" which is smaller than 0.\n";
			}
		}
		if(flag)
		{
			//this.addActionError(message);//addFieldError("result", message);
		}
		
	}
	
	@Override
	public String execute() throws Exception {
		// TODO Auto-generated method stub
		
		//if(message.length()!=0){
		//	return Action.ERROR;
		//}
		
		resultWeights=li.getMVOWeightsWithLimits();
		weights=new ArrayList<String>();
		for(Double r:resultWeights)
		{
			weights.add(r.toString());
			System.out.println(resultWeights);
		}
		
		/*************************************************************/
		covariences = li.getCorrelationMatrix();
		int size = securityList.size(); 
		covarienceStrings = new String[size+1][size+1];		
		covarienceStrings[0][0]=" ";
		for(int i=1;i<=size;i++)
		{
			covarienceStrings[0][i]=securityList.get(i-1);
			covarienceStrings[i][0]=securityList.get(i-1);
		}
		DecimalFormat df = new DecimalFormat("0.0000");
		for(int i=1;i<=size;i++)
		{
			for(int j=1;j<=size;j++)
			{
				covarienceStrings[i][j]=df.format(covariences[i-1][j-1]);
			}
		}
		/**************************************************************/
		
		this.filePathName = li.PlotPie();
		
		return Action.SUCCESS;
	}

	public String getBanchmarkArray() {
		return banchmarkArray;
	}

	public void setBanchmarkArray(String banchmarkArray) {
		this.banchmarkArray = banchmarkArray;
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

	public double getRAF() {
		return RAF;
	}

	public void setRAF(double raf) {
		RAF = raf;
	}


	public String[] getBanchmarks() {
		return banchmarks;
	}
	public void setBanchmarks(String[] banchmarks) {
		this.banchmarks = banchmarks;
	}
	public TimeUnit getTimeUnit() {
		return timeUnit;
	}
	public void setTimeUnit(TimeUnit timeUnit) {
		this.timeUnit = timeUnit;
	}
	public Date getStart() {
		return start;
	}
	public void setStart(Date start) {
		this.start = start;
	}
	public Date getEnd() {
		return end;
	}
	public void setEnd(Date end) {
		this.end = end;
	}
	public String getMesage() {
		return message;
	}
	public void setMesage(String mesage) {
		this.message = mesage;
	}
	public List<String> getSecurityList() {
		return securityList;
	}
	public void setSecurityList(List<String> securityList) {
		this.securityList = securityList;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public List<Double> getResultWeights() {
		return resultWeights;
	}
	public void setResultWeights(List<Double> resultWeights) {
		this.resultWeights = resultWeights;
	}
	public String getLower() {
		return lower;
	}
	public void setLower(String lower) {
		this.lower = lower;
	}
	public String getUpper() {
		return upper;
	}
	public void setUpper(String upper) {
		this.upper = upper;
	}
	public List<Double> getLowerLimits() {
		return lowerLimits;
	}
	public void setLowerLimits(List<Double> lowerLimits) {
		this.lowerLimits = lowerLimits;
	}
	public List<Double> getUpperLimits() {
		return upperLimits;
	}
	public void setUpperLimits(List<Double> upperLimits) {
		this.upperLimits = upperLimits;
	}

	public List<String> getWeights() {
		return weights;
	}

	public void setWeights(List<String> weights) {
		this.weights = weights;
	}

	public String getFilePathName() {
		return filePathName;
	}

	public void setFilepath(String filePathName) {
		this.filePathName = filePathName;
	}

	
	public String getExpected() {
		return expected;
	}

	public void setExpected(String expected) {
		this.expected = expected;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public double[][] getCovariences() {
		return covariences;
	}

	public void setCovariences(double[][] covariences) {
		this.covariences = covariences;
	}

	public String[][] getCovarienceStrings() {
		return covarienceStrings;
	}

	public void setCovarienceStrings(String[][] covarienceStrings) {
		this.covarienceStrings = covarienceStrings;
	}

	public List<Double> getExpectedExcessAnnualReturns() {
		return expectedExcessAnnualReturns;
	}

	public void setExpectedExcessAnnualReturns(
			List<Double> expectedExcessAnnualReturns) {
		this.expectedExcessAnnualReturns = expectedExcessAnnualReturns;
	}
	
	

}
