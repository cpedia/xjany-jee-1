package com.lti.action;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.lti.permission.MYPLANIQPermissionManager;
import com.lti.service.InviteManager;
import com.lti.service.PortfolioManager;
import com.lti.service.UserManager;
import com.lti.service.bo.Group;
import com.lti.service.bo.InviteCode;
import com.lti.service.bo.User;
import com.lti.service.bo.UserOperation;
import com.lti.service.bo.UserProfile;
import com.lti.service.bo.UserTransaction;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.util.EmailUtil;
import com.lti.util.FormatUtil;
import com.lti.util.PaypalUtil;
import com.lti.util.StringUtil;
import com.opensymphony.xwork2.ActionSupport;

public class paypalaction extends ActionSupport implements Action {
	private static final long serialVersionUID = -4451320672728104739L;

	private UserManager userManager = null;
	public paypalaction() {
		super();
		userManager = ContextHolder.getUserManager();
		inviteManager=ContextHolder.getinvIteManager();
	}
	private String message;
	private String code;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public String handle_try(User user,PaypalUtil pu,UserTransaction transaction){
		StringBuffer systemStatus=new StringBuffer();
		
		long userid=user.getID();
		
		UserProfile profile=userManager.getUserProfile(userid);
		if(profile==null){
			profile=new UserProfile();
			profile.setUserID(userid);
		}else if(profile.getSubscrId()!=null){
			systemStatus.append("Warning: The user has subscribed before, previous subscr_id: "+profile.getSubscrId());
			systemStatus.append("<br>\r\n");
			systemStatus.append("Warning: The user has subscribed before, previous item name: "+profile.getItemName());
			systemStatus.append("<br>\r\n");
			profile=new UserProfile();
			profile.setUserID(userid);
		}
		Date subscrdate=pu.getSubscrDate();
		Date enddate=pu.getSubscrDate();
		
		boolean offertrial=false;
		String period1=null;
		if(transaction.getPeriod1()!=null&&!transaction.getPeriod1().equals("")){
			offertrial=true;
			period1=transaction.getPeriod1().trim();
			enddate=PaypalUtil.getEndDate(period1, subscrdate);
		}
		systemStatus.append("Trial period: "+period1);
		systemStatus.append("<br>\r\n");
		systemStatus.append("End Date: "+enddate);
		systemStatus.append("<br>\r\n");
		
		boolean islate=false;
		Date laststartdate=profile.getPaymentStartDate();
		if(laststartdate!=null){
			if(subscrdate.before(laststartdate)){
				islate=true;
				systemStatus.append("Warning: the date of this txn is before the date exist in ltisystem database, change nothing.");
				systemStatus.append("<br>\r\n");
				return systemStatus.toString();
			}
		}
		
		//if the user has been tried this service before
		if(userManager.existPayerId(transaction.getPayerId(),transaction.getItemName())&&!islate){
			profile.setUserStatus(UserProfile.PAYMENT_STATUS_LOCKED);
			profile.setSubscrId(transaction.getSubscrId());
			UserOperation uo=new UserOperation();
			uo.setOperationDate(new Date());
			uo.setOperationType(UserOperation.TYPE_PAYPAL);
			uo.setOptCondition(UserOperation.CONDITION_NEW);
			StringBuffer sb=new StringBuffer();
			sb.append("Subscr Id: ");
			sb.append(profile.getSubscrId());
			sb.append("<br>\r\n");
			sb.append("User Name :");
			sb.append(user.getUserName()+"("+userid+")");
			sb.append("<br>\r\n");
			sb.append("The user has tried/subscribe this service before.");
			sb.append("<br>\r\n");
			uo.setOptDescription(sb.toString());
			uo.setUserID(Configuration.SUPER_USER_ID);
			userManager.saveUserOperation(uo);
			try {
				DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				sendEmail("[Locked Trial Event]"+user.getUserName()+" "+df.format(new Date()),sb.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
			systemStatus.append("locked----------------<br>\r\n");
			systemStatus.append(sb);
		}else{
			if(offertrial){
				profile.setUserStatus(UserProfile.PAYMENT_STATUS_NORMAL);
			}
		}
		
		profile.setItemName(transaction.getItemName());
		profile.setPayerEmail(transaction.getPayerEmail());
		profile.setPayerId(transaction.getPayerId());
		profile.setPaymentStartDate(subscrdate);
		profile.setPaymentEnddate(enddate);
		profile.setSubscrId(transaction.getSubscrId());
		profile.setUserID(userid);
		profile.setSubscrId(transaction.getSubscrId());
		profile.setPeriod1(transaction.getPeriod1());
		profile.setPeriod3(transaction.getPeriod3());
		userManager.saveOrUpdateUserProfile(profile);
		
		if(userManager.existPayerId(transaction.getPayerId(),transaction.getItemName())&&!islate){
		}else{
			try {
				if(offertrial){
					userManager.addGroupByItem(userid);
					systemStatus.append("permission changed----------------<br>\r\n");
					List<Group> groups=userManager.getGroupsByUser(userid);
					if(groups!=null&&groups.size()>0){
						for(int i=0;i<groups.size();i++){
							systemStatus.append("group["+groups.get(i).getID()+"]:");
							systemStatus.append(groups.get(i).getName());
							systemStatus.append("<br>\r\n");
						}
					}
				}
			} catch (Exception e) {
				systemStatus.append("Error: "+e.getMessage());
				systemStatus.append("<br>\r\n");
				e.printStackTrace();
			}
			
		}
		//根据新用户组别改变用户数目权限
		try{
			MYPLANIQPermissionManager mpm = new MYPLANIQPermissionManager();
			mpm.changeOneUserPermission(userid);
		}catch(Exception e){
			
		}
		return systemStatus.toString();
	}
	
	public String handle_pay(User user,PaypalUtil pu,UserTransaction transaction){
		StringBuffer systemStatus=new StringBuffer();
		long userid=user.getID();
		UserProfile profile=userManager.getUserProfile(userid);
		if(profile==null||profile.getSubscrId()==null){
			systemStatus.append("The system doesn't have any subscribtion.");
			systemStatus.append("<br>\r\n");
			systemStatus.append("Change nothing.");
			systemStatus.append("<br>\r\n");
			return systemStatus.toString();
		}
		//as long as can make money, checking is redundant
		/*else if(!profile.getSubscrId().equals(pu.getSubScrID())){
			systemStatus.append("The subscr_id doesn't match the one from paypal.");
			systemStatus.append("<br>\r\n");
			systemStatus.append("Change nothing.");
			systemStatus.append("<br>\r\n");
			return systemStatus.toString();
		}*/
		
		Date enddate=PaypalUtil.getEndDate(profile.getPeriod3(), transaction.getPaymentDate());
		
		profile.setItemName(transaction.getItemName());
		profile.setPayerEmail(transaction.getPayerEmail());
		profile.setPayerId(transaction.getPayerId());
		profile.setPaymentStartDate(transaction.getPaymentDate());
		profile.setPaymentEnddate(enddate);
		profile.setSubscrId(transaction.getSubscrId());
		profile.setUserID(userid);
		profile.setUserStatus(UserProfile.PAYMENT_STATUS_NORMAL);
		profile.setAmount(transaction.getPaymentGross());
		profile.setTxnId(transaction.getTxnId());
		userManager.saveOrUpdateUserProfile(profile);
		try {
			userManager.addGroupByItem(userid);
			systemStatus.append("permission changed----------------<br>\r\n");
			List<Group> groups=userManager.getGroupsByUser(userid);
			if(groups!=null&&groups.size()>0){
				for(int i=0;i<groups.size();i++){
					systemStatus.append("group["+groups.get(i).getID()+"]:");
					systemStatus.append(groups.get(i).getName());
					systemStatus.append("<br>\r\n");
				}
			}
		} catch (Exception e) {
			systemStatus.append("Error: "+e.getMessage());
			systemStatus.append("<br>\r\n");
			e.printStackTrace();
		}
		//根据新用户组别改变用户数目权限
		try{
			MYPLANIQPermissionManager mpm = new MYPLANIQPermissionManager();
			mpm.changeOneUserPermission(userid);
		}catch(Exception e){
			
		}
		
		return systemStatus.toString();
	}
	public String handle_cancel(User user,PaypalUtil pu,UserTransaction transaction){
		StringBuffer systemStatus=new StringBuffer();
		long userid=user.getID();
		UserProfile profile=userManager.getUserProfile(userid);
		if(profile==null||profile.getSubscrId()==null){
			systemStatus.append("The system doesn't have any subscribtion information of the user["+user.getUserName()+"].");
			systemStatus.append("<br>\r\n");
			systemStatus.append("Change nothing.");
			systemStatus.append("<br>\r\n");
			return systemStatus.toString();
		}else if(!profile.getSubscrId().equals(pu.getSubScrID())){
			systemStatus.append("The subscr_id doesn't match the one from paypal.");
			systemStatus.append("<br>\r\n");
			systemStatus.append("Change nothing.");
			systemStatus.append("<br>\r\n");
			return systemStatus.toString();
		}
		
		profile.setItemName(transaction.getItemName());
		profile.setPayerEmail(transaction.getPayerEmail());
		profile.setPayerId(transaction.getPayerId());
		profile.setPaymentStartDate(null);
		profile.setPaymentEnddate(null);
		profile.setSubscrId(transaction.getSubscrId());
		profile.setUserID(userid);
		profile.setUserStatus(UserProfile.PAYMENT_STATUS_CANCEL); //当用户cancel之后，用户状态变为"cancel"
		userManager.saveOrUpdateUserProfile(profile);
		try {
			userManager.removeGroupByItem(userid);
			systemStatus.append("permission changed----------------<br>\r\n");
			List<Group> groups=userManager.getGroupsByUser(userid);
			if(groups!=null&&groups.size()>0){
				for(int i=0;i<groups.size();i++){
					systemStatus.append("group["+groups.get(i).getID()+"]:");
					systemStatus.append(groups.get(i).getName());
					systemStatus.append("<br>\r\n");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			systemStatus.append("Error: "+e.getMessage());
			systemStatus.append("<br>\r\n");
		}
		//根据新用户组别改变用户数目权限
		try{
			MYPLANIQPermissionManager mpm = new MYPLANIQPermissionManager();
			mpm.changeOneUserPermission(userid);
			PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();
			portfolioManager.changeUserResourceWhenCanclePaypal(userid, new Date());
		}catch(Exception e){
			
		}
		return systemStatus.toString();
	}
	public String handle_default(User user,PaypalUtil pu){
		
		String type=pu.getType();
		System.out.println(pu.getType());
		UserTransaction transaction=pu.getUserTransaction();
		
		if(transaction.getTimeCreated()==null)transaction.setTimeCreated(new Date());
		
		transaction.setUserID(user.getID());
		
		String result=null;
		if(type.equals(PaypalUtil.TRY_TYPE)){
			result=handle_try(user, pu, transaction);
		}else if(type.equals(PaypalUtil.PAY_TYPE)){
			//wait for 10 secs
			try {
				Thread.sleep(10000l);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			result= handle_pay(user, pu, transaction);
			
		}else if(type.equals(PaypalUtil.CANCEL_TYPE)){
			result= handle_cancel(user, pu, transaction);
		}else{
			result= "Unknow Transaction from paypal.<br>\r\n";
		}
		userManager.saveUserTransaction(transaction);
		return result;
	}
	
	public String admincheckgrp(){
		try{
			message="start charge user group."+new Date();
			UserManager userManager=ContextHolder.getUserManager();
			userManager.changeUserGroup();
			message+="end charge user group."+new Date();
		}catch(Exception e){
			message+=e.getMessage();
		}
		return Action.MESSAGE;
	}

	public static void sendEmail(String subject,String text) throws Exception{
		EmailUtil.sendMail(new String[] { "caixg@ltisystem.com", "jzhong@gmail.com","wyjfly@gmail.com" }, subject, text);
	}
	private Boolean sandbox=false;
	public Boolean getSandbox() {
		return sandbox;
	}

	public void setSandbox(Boolean sandbox) {
		this.sandbox = sandbox;
	}

	public String success() {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse reponse =  ServletActionContext.getResponse();
		PaypalUtil pu = new PaypalUtil(request);
		if(username==null){
			try {
				sendEmail( "[Paypal empty message]", "<pre>\r\nURL:"+request.getRemoteHost()+"\r\n"+pu.getAllParameters()+"</pre>");
			} catch (Exception e) {
				e.printStackTrace();
			}
			message = "";
			return Action.MESSAGE;
		}
		try {
			User u=null;
			u=userManager.get(username);
			
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			String status="";
			for(int i=0;i<5;i++){
				try {
					if(sandbox){
						status = pu.verify_sandbox();
					}
					else {
						status = pu.verify();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(!status.equals(""))break;
				else {
					try {
						Thread.sleep(3000l);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			String title="[Paypal Transaction]"+username+": "+ pu.getType()+". " + sdf.format(new Date());
			if (!status.equals(PaypalUtil.STATUS_VERIFIED)) {
				message = "The message is not from Paypal.com.<br>\r\n";
				title = "[Paypal ERROR]"+username+": "+ pu.getType()+". " + sdf.format(new Date());
			} else {
				message=handle_default(u,pu);
			}
			sendEmail( title, "<pre>\r\nURL:"+request.getRemoteHost()+"\r\n"+pu.getAllParameters()+"</pre><pre>Message: <br>\r\n"+message+"</pre>");
		} catch (Exception e) {
			message = StringUtil.getStackTraceString(e);
			e.printStackTrace();
			try {
				sendEmail( "[Paypal Transaction Error]"+username+"", "<pre>"+pu.getAllParameters()+"</pre><br><br><pre>Message: <br>\r\n"+message+"</pre>");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			//if error, sent 500 code, paypal will send message again later.
			reponse.setStatus(500);
		}
		message = "";
		System.out.println("Username: " + username+"("+pu.getType()+")");
		
		return Action.MESSAGE;
	}


	private String username;
	private String status;
	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	private Date startDate=null;
	private Date endDate=null;
	private String serverName;
	private Integer port;
	public String vf_subscr(){
		return subscr();
	}
	
	
	private UserProfile profile;
	
	
	public UserProfile getProfile() {
		return profile;
	}

	public void setProfile(UserProfile profile) {
		this.profile = profile;
	}

	private User user;
	
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	private double isInvite;

	public double getIsInvite() {
		return isInvite;
	}

	public void setIsInvite(double isInvite) {
		this.isInvite = isInvite;
	}
	private InviteManager inviteManager;

	public void setInviteManager(InviteManager inviteManager) {
		this.inviteManager = inviteManager;
	}

	public String subscr() {
		serverName=ServletActionContext.getRequest().getServerName();
		port=ServletActionContext.getRequest().getServerPort();
		user = userManager.getLoginUser();
		//根据User获得inviteCode的类型
		
		if(user.getInviteCodeID()==null)
			this.setIsInvite((double)0);
		else{
			InviteCode inviteCode = new InviteCode();
			inviteCode = inviteManager.get(user.getInviteCodeID());
			if(inviteCode!=null){
				this.setIsInvite(inviteCode.getPriceItem());
			}else{
				//如果要清空被删的邀请码在这里加上就OK了
				this.setIsInvite((double)0);
			}
		}
		if (user == null || user.getID().equals(Configuration.USER_ANONYMOUS)) {
			message = "Please login first.";
			return Action.MESSAGE;
		}
		profile=userManager.getUserProfile(user.getID());
		if(profile==null){
			profile=new UserProfile();
			profile.setUserID(user.getID());
			userManager.saveOrUpdateUserProfile(profile);
		}
		status = profile.getUserStatus();
		username=user.getUserName();
		if(status.equals(UserProfile.PAYMENT_STATUS_NORMAL)){
			startDate=profile.getPaymentStartDate();
			endDate=profile.getPaymentEnddate();
			message=profile.getItemName();
		}else if(status.equals(UserProfile.PAYMENT_STATUS_WAITING)){
		}else{
		}
		return Action.SUCCESS;
	}
	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String cancel(){
		User user = userManager.getLoginUser();
		if (user == null || user.getID().equals(Configuration.USER_ANONYMOUS)) {
			message = "Please login first.";
			return Action.MESSAGE;
		}
		UserProfile profile=userManager.getUserProfile(user.getID());
		if(profile==null){
			profile=new UserProfile();
			profile.setUserID(user.getID());
			userManager.saveOrUpdateUserProfile(profile);
		}
		status = profile.getUserStatus();
		if(!status.equals(UserProfile.PAYMENT_STATUS_NORMAL)){
			message="You don't subscribe our service.";
		}
		profile.setUserStatus(UserProfile.PAYMENT_STATUS_WAITING_FOR_CANCEL);
		UserOperation uo=new UserOperation();
		uo.setOperationDate(new Date());
		uo.setOperationType(UserOperation.TYPE_PAYPAL);
		uo.setOptCondition(UserOperation.CONDITION_NEW);
		StringBuffer sb=new StringBuffer();
		sb.append("Subscr Id: ");
		sb.append(profile.getSubscrId());
		sb.append("<br>\r\n");
		sb.append("User name: ");
		sb.append(user.getUserName()+"("+user.getID()+")");
		sb.append("<br>\r\n");
		if(profile.getTxnId()==null){
			sb.append("Do not need to refund.");
		}else{
			sb.append("Transaction Id: ");
			sb.append(profile.getTxnId());
			sb.append("<br>\r\n");
			sb.append("Refund Amount: ");
			sb.append(getRefundAmount(profile));
			sb.append("("+profile.getAmount()+")");
			sb.append("<br>\r\n");
		}
		
		uo.setOptDescription(sb.toString());
		uo.setUserID(Configuration.SUPER_USER_ID);
		userManager.saveUserOperation(uo);
		userManager.saveOrUpdateUserProfile(profile);
		try {
			DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			sendEmail("[Cancel Event]"+user.getUserName()+" "+df.format(new Date()),sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		message="ok";
		return Action.MESSAGE;
	}

	public String center() {
		User user = userManager.getLoginUser();
		if (user == null || user.getID().equals(Configuration.USER_ANONYMOUS)) {
			message = "Please login first.";
			return Action.MESSAGE;
		}
		UserProfile profile=userManager.getUserProfile(user.getID());
		if(profile==null){
			profile=new UserProfile();
			profile.setUserID(user.getID());
			userManager.saveOrUpdateUserProfile(profile);
		}
		status = profile.getUserStatus();
		username=user.getUserName();
		if(status.equals(UserProfile.PAYMENT_STATUS_NORMAL)){
			startDate=profile.getPaymentStartDate();
			endDate=profile.getPaymentEnddate();
			message=profile.getItemName();
		}else if(status.equals(UserProfile.PAYMENT_STATUS_WAITING)){
		}else if(status.equals(UserProfile.PAYMENT_STATUS_WAITING_FOR_CANCEL)){
			startDate=profile.getPaymentStartDate();
			endDate=profile.getPaymentEnddate();
			if(profile.getTxnId()!=null){
				message=profile.getItemName()+"(Refund Amount: "+getRefundAmount(profile)+")";
			}else{
				message=profile.getItemName();
			}
			
		}
		else{
		}
		return Action.SUCCESS;
	}

	private String getRefundAmount(UserProfile profile){
		startDate=profile.getPaymentStartDate();
		endDate=profile.getPaymentEnddate();
		if(profile.getTxnId()!=null){
			double period=System.currentTimeMillis()-profile.getPaymentStartDate().getTime();
			int days=(int) (period/24/3600/1000);
			if(days==0)days=1;
			int totaldays=(int) ((profile.getPaymentEnddate().getTime()-profile.getPaymentStartDate().getTime())/24/3600/1000);
			return FormatUtil.formatQuantity(profile.getAmount()-days*1.0/totaldays*profile.getAmount());
		}else{
			return "0.0";
		}
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}


	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	
	public String changetowaiting(){
		User user = userManager.getLoginUser();
		if (user == null || user.getID().equals(Configuration.USER_ANONYMOUS)) {
			message = "Please login first.";
			return Action.MESSAGE;
		}
		UserProfile userProfile = userManager.getUserProfile(user.getID());
		if(userProfile==null){
			userProfile=new UserProfile();
			userProfile.setUserID(user.getID());
			userManager.saveOrUpdateUserProfile(userProfile);
		}
		String status = userProfile.getUserStatus();
		if(!status.equals(UserProfile.PAYMENT_STATUS_NORMAL)){
			userProfile.setUserStatus(UserProfile.PAYMENT_STATUS_WAITING);
			userManager.saveOrUpdateUserProfile(userProfile);
			message="ok";
		}else{
			message="no change.";
		}
		return Action.MESSAGE;
	}
	
	public String changetonowaiting(){
		User user = userManager.getLoginUser();
		if (user == null || user.getID().equals(Configuration.USER_ANONYMOUS)) {
			message = "Please login first.";
			return Action.MESSAGE;
		}
		UserProfile userProfile = userManager.getUserProfile(user.getID());
		if(userProfile==null){
			userProfile=new UserProfile();
			userProfile.setUserID(user.getID());
			userManager.saveOrUpdateUserProfile(userProfile);
		}
		String status = userProfile.getUserStatus();
		if(status.equals(UserProfile.PAYMENT_STATUS_WAITING)){
			userProfile.setUserStatus(UserProfile.PAYMENT_STATUS_INACTIVE);
			userManager.saveOrUpdateUserProfile(userProfile);
			message="ok";
		}else{
			message="no change.";
		}
		return Action.MESSAGE;
	}
	/*
	 
	public String cancel() {
		message = "200 ok";
		System.out.println("cancel");
		return Action.MESSAGE;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	private Map decodedMap;

	private String action_name;
	private String params;

	public String getAction_name() {
		return action_name;
	}

	public void setAction_name(String action_name) {
		this.action_name = action_name;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	private String profileid;

	public String getProfileid() {
		return profileid;
	}

	public void setProfileid(String profileid) {
		this.profileid = profileid;
	}

	public String cancelprofile() throws Exception {
		// NVPCallerServiced object is taken from the session
		NVPCallerServices caller = PaymentUtil.getPaypalCaller();
		if (caller == null) {
			message = "<a href='paypal__login.action'>login</a>";
			return Action.MESSAGE;
		}

		// NVPEncoder object is created and all the name value pairs are loaded
		// into it.
		NVPEncoder encoder = new NVPEncoder();

		encoder.add("METHOD", "ManageRecurringPaymentsProfileStatus");
		encoder.add("PROFILEID", profileid);
		encoder.add("ACTION", "Cancel");

		// encode method will encode the name and value and form NVP string for
		// the request
		String NVPString = encoder.encode();

		// call method will send the request to the server and return the
		// response NVPString
		String ppresponse = (String) caller.call(NVPString);

		// NVPDecoder object is created
		NVPDecoder resultValues = new NVPDecoder();
		// decode method of NVPDecoder will parse the request and decode the
		// name and value pair
		resultValues.decode(ppresponse);

		// checks for Acknowledgement and redirects accordingly to display error
		// messages
		String strAck = resultValues.get("ACK");
		if (strAck != null && !(strAck.equals("Success") || strAck.equals("SuccessWithWarning"))) {
		}
		decodedMap = resultValues.getMap();

		return Action.SUCCESS;
	}

	public String createprofile() {
		HttpServletRequest request = ServletActionContext.getRequest();
		NVPCallerServices caller = PaymentUtil.getPaypalCaller();
		if (caller == null) {
			message = "<a href='paypal__login.action'>login</a>";
			return Action.MESSAGE;
		}

		try {
			// NVPEncoder object is created and all the name value pairs are
			// loaded into it.
			NVPEncoder encoder = new NVPEncoder();

			encoder.add("METHOD", "CreateRecurringPaymentsProfile");
			encoder.add("AMT", (String) request.getParameter("amount"));
			encoder.add("CREDITCARDTYPE", (String) request.getParameter("creditCardType"));
			encoder.add("ACCT", (String) request.getParameter("creditCardNumber"));
			encoder.add("EXPDATE", (String) request.getParameter("expDateMonth") + (String) request.getParameter("expDateYear"));
			encoder.add("CVV2", (String) request.getParameter("cvv2Number"));
			encoder.add("FIRSTNAME", (String) request.getParameter("firstName"));
			encoder.add("LASTNAME", (String) request.getParameter("lastName"));
			encoder.add("STREET", (String) request.getParameter("address1"));
			encoder.add("CITY", (String) request.getParameter("city"));
			encoder.add("STATE", (String) request.getParameter("state"));
			encoder.add("ZIP", (String) request.getParameter("zip"));
			encoder.add("COUNTRYCODE", "US");
			// encoder.add("CURRENCYCODE",(String)request.getParameter("currency"));
			encoder.add("CURRENCYCODE", "USD");

			String day = request.getParameter("pDate");
			String month = request.getParameter("pMonth");
			String year = request.getParameter("pYear");
			String date = year + "-" + month + "-" + day + "T0:0:0";
			encoder.add("PROFILESTARTDATE", date);
			encoder.add("BILLINGPERIOD", (String) request.getParameter("BillingPeriod"));
			encoder.add("BILLINGFREQUENCY", (String) request.getParameter("BillingFrequency"));
			encoder.add("TOTALBILLINGCYCLES", (String) request.getParameter("BillingCycles"));
			encoder.add("DESC", (String) request.getParameter("ProfileDescription"));

			// encode method will encode the name and value and form NVP string
			// for the request
			String NVPString = encoder.encode();

			// call method will send the request to the server and return the
			// response NVPString
			String ppresponse = (String) caller.call(NVPString);

			// NVPDecoder object is created
			NVPDecoder resultValues = new NVPDecoder();
			// decode method of NVPDecoder will parse the request and decode the
			// name and value pair
			resultValues.decode(ppresponse);

			// checks for Acknowledgement and redirects accordingly to display
			// error messages
			String strAck = resultValues.get("ACK");
			if (strAck != null && !(strAck.equals("Success") || strAck.equals("SuccessWithWarning"))) {

				decodedMap = resultValues.getMap();
				return Action.SUCCESS;
			}
			decodedMap = resultValues.getMap();
		} catch (Exception e) {
			e.printStackTrace();
			return Action.ERROR;
		}

		return Action.SUCCESS;
	}

	public Map getDecodedMap() {
		return decodedMap;
	}

	public void setDecodedMap(Map decodedMap) {
		this.decodedMap = decodedMap;
	}

	public String login() {
		return Action.SUCCESS;
	}

	public String loginsucc() throws Exception {
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			APIProfile profile = null;

			profile = ProfileFactory.createSignatureAPIProfile();
			profile.setAPIUsername((String) request.getParameter("apiUsername"));
			profile.setAPIPassword((String) request.getParameter("apiPassword"));
			profile.setSignature((String) request.getParameter("signature"));
			profile.setEnvironment((String) request.getParameter("environment"));
			profile.setSubject((String) request.getParameter("subjectEmail"));
			com.paypal.sdk.services.NVPCallerServices caller = new NVPCallerServices();

			caller.setAPIProfile(profile);
			PaymentUtil.setPaypalCaller(caller);
			message = "OK";
			return Action.MESSAGE;
			// session.setAttribute("caller", caller);
			// response.sendRedirect("Calls.html");
		} catch (Exception e) {
			// session.setAttribute("exception", e);
			// response.sendRedirect("Error.jsp");
			throw e;
		}
	}

	private String transactionid;

	public String getTransactionid() {
		return transactionid;
	}

	public void setTransactionid(String transactionid) {
		this.transactionid = transactionid;
	}

	private UserTransaction transaction;

	public String viewtransaction() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		NVPCallerServices caller = PaymentUtil.getPaypalCaller();
		if (caller == null) {
			message = "<a href='paypal__login.action'>login</a>";
			return Action.MESSAGE;
		}

		// NVPEncoder object is created and all the name value pairs are loaded
		// into it.
		NVPEncoder encoder = new NVPEncoder();
		encoder.add("METHOD", "GetTransactionDetails");
		encoder.add("TRANSACTIONID", transactionid);

		// encode method will encode the name and value and form NVP string for
		// the request
		String strNVPString = encoder.encode();

		// call method will send the request to the server and return the
		// response NVPString
		String ppresponse = (String) caller.call(strNVPString);

		// NVPDecoder object is created
		NVPDecoder resultValues = new NVPDecoder();

		// decode method of NVPDecoder will parse the request and decode the
		// name and value pair
		resultValues.decode(ppresponse);

		transaction = new UserTransaction();

		String payer = (String) resultValues.get("RECEIVERBUSINESS");
		String payerID = (String) resultValues.get("PAYERID");
		String firstName = (String) resultValues.get("FIRSTNAME");
		String lastName = (String) resultValues.get("LASTNAME");
		String transactionID = (String) resultValues.get("TRANSACTIONID");
		String parentTransactionID = (String) resultValues.get("PARENTTRANSACTIONID");
		String grossAmount = (String) resultValues.get("AMT");
		String paymentStatus = resultValues.get("PAYMENTSTATUS");
		String protectionEligibility = (String) resultValues.get("PROTECTIONELIGIBILITY");
		String pendingreason = (String) resultValues.get("PENDINGREASON");
		transaction.setPayerEmail(payer);
		transaction.setPayerId(payerID);
		transaction.setFirstName(firstName);
		transaction.setLastName(lastName);
		transaction.setTxnId(transactionID);
		transaction.setParentTxnId(parentTransactionID);
		transaction.setAmount(Double.parseDouble(grossAmount));
		transaction.setPaymentStatus(paymentStatus);
		transaction.setProtectionEligibility(protectionEligibility);
		//transaction.setPendingreason(pendingreason);

		// checks for Acknowledgement and redirects accordingly to display error
		// messages
		String strAck = resultValues.get("ACK");
		if (strAck != null && !(strAck.equals("Success") || strAck.equals("SuccessWithWarning"))) {
		}

		return Action.SUCCESS;
	}

	public String refund() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		NVPCallerServices caller = PaymentUtil.getPaypalCaller();
		if (caller == null) {
			message = "<a href='paypal__login.action'>login</a>";
			return Action.MESSAGE;
		}
		// NVPEncoder object is created and all the name value pairs are loaded
		// into it.
		NVPEncoder encoder = new NVPEncoder();

		encoder.add("METHOD", "RefundTransaction");
		encoder.add("REFUNDTYPE", (String) request.getParameter("refundType"));
		encoder.add("TRANSACTIONID", (String) request.getParameter("transactionID"));

		if ((request.getParameter("refundType") != null) && ((String) request.getParameter("refundType")).length() > 0 && ((String) request.getParameter("refundType")).equalsIgnoreCase("Partial")) {
			encoder.add("AMT", (String) request.getParameter("amount"));
		}

		encoder.add("NOTE", (String) request.getParameter("memo"));

		// encode method will encode the name and value and form NVP string for
		// the request
		String nVPString = encoder.encode();

		// call method will send the request to the server and return the
		// response NVPString
		String ppresponse = (String) caller.call(nVPString);

		// NVPDecoder object is created
		NVPDecoder decoder = new NVPDecoder();

		// decode method of NVPDecoder will parse the request and decode the
		// name and value pair
		decoder.decode(ppresponse);

		String transactionId = decoder.get("TRANSACTIONID");

		// checks for Acknowledgement and redirects accordingly to display error
		// messages
		String strAck = decoder.get("ACK");
		if (strAck != null && !(strAck.equals("Success") || strAck.equals("SuccessWithWarning"))) {
		}
		//transaction = new PaypalTransaction();
	//	transaction.setTransactionID(request.getParameter("transactionID"));
		//transaction.setGrossAmount(decoder.get("GROSSREFUNDAMT"));
		return Action.SUCCESS;
	}


	private Integer typecode;
	public Integer getTypecode() {
		return typecode;
	}

	public void setTypecode(Integer typecode) {
		this.typecode = typecode;
	}
	 */

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
