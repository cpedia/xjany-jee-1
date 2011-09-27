package com.lti.action.register;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;
import net.sf.json.util.JSONBuilder;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import org.acegisecurity.providers.encoding.Md5PasswordEncoder;
import org.acegisecurity.userdetails.UserDetails;
import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.lti.action.Action;
import com.lti.permission.MYPLANIQPermissionManager;
import com.lti.service.GroupManager;
import com.lti.service.InviteManager;
import com.lti.service.UserManager;
import com.lti.service.bo.Group;
import com.lti.service.bo.InviteCode;
import com.lti.service.bo.User;
import com.lti.service.bo.UserMarket;
import com.lti.service.bo.UserTransaction;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.system.EmailLoginServiceImpl;
import com.lti.type.EmailLoginDetails;
import com.lti.util.EmailUtil;
import com.lti.util.StringUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import freemarker.template.Template;

public class registerAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	private User user;
	private UserManager userManager;
	private InviteCode inviteCode;
	private InviteManager inviteManage;
	private GroupManager groupManager;
	private JavaMailSenderImpl sender;
	private String actionMessage;
	private String action;
	private String oldPassword;
	private String newPassword;
	private String regType;
	private String type;
	private String userName;
	private String errString;
	private String eStr;
	private String inviteName;
	private String inviteCodecode;
	private String priceItem;
	private String landing;
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getLanding() {
		return landing;
	}

	public void setLanding(String landing) {
		this.landing = landing;
	}

	public String getPriceItem() {
		return priceItem;
	}

	public void setPriceItem(String priceItem) {
		this.priceItem = priceItem;
	}

	private String inviteCodeId;
	private String url;

	public String openRegister() {
		System.out.print(this.getInviteCodeId());
		action = "openRegister";
		String username;
		Object obj = SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();

		if (obj instanceof UserDetails || obj instanceof EmailLoginDetails) {
			username = ((UserDetails) obj).getUsername();
		} else {
			username = obj.toString();
		}

		if (username.equals("anonymousUser")) {
			if (type != null)
				regType = type;
			return Action.INPUT;
		} else
			return Action.ERROR;
	}

	// 邀请码添加页面
	public String addInviteCode() throws Exception {
		this.setInviteCodecode(genRandomNum(7));
		return Action.SUCCESS;
	}

	// 增加邀请码
	public String adminInsertinvite() throws Exception {
		inviteCode = new InviteCode();
		// System.out.print(inviteCodecode);
		if (inviteCodecode == null || inviteCodecode.equals("")) {
			this.setUrl("Sorry!  inviteCode it can not be null");
		} else {
			this.setUrl("http://www.myplaniq.com/LTISystem/jsp/register/openRegister.action?inviteCodeId="
					+ inviteCodecode);
			inviteCode.setDate(new Date());
			if (priceItem == null || priceItem.equals("")) {
				this.setUrl("Sorry!  Need a inviteType");
				return Action.SUCCESS;
			} else {
				try {
					inviteCode.setPriceItem(Double.valueOf(priceItem));
				} catch (Exception e) {
					this.setUrl("Sorry!  InviteType is a number");
					return Action.SUCCESS;
				}
			}
			inviteCode.setInviteCode(inviteCodecode);
			if (inviteName == null)
				this.setInviteName("null");
			inviteCode.setInviteName(inviteName);
			if (middleMan == null)
				this.setMiddleMan("null");
			inviteCode.setMiddleMan(middleMan);
			if (inviteManage.insert(inviteCode) == false) {
				this.setUrl("Sorry!  Haven inviteCode yet");
				return Action.SUCCESS;
			}
			;
		}
		return Action.SUCCESS;
	}

	// 删除邀请码
	public String delInvite() {
		inviteManage.delById(Long.valueOf(this.getInviteCodeId()));
		return Action.SUCCESS;
	}

	// 给已经注册用户提供邀请码转向
	public String invitLogin() {
		return Action.SUCCESS;
	}

	// 邀请码后台页面
	private List<InviteCode> list;

	public List<InviteCode> getList() {
		return list;
	}

	public void setList(List<InviteCode> list) {
		this.list = list;
	}

	public String adminViewInvite() {
		list = inviteManage.getAll();
		for (int i = 0; i < list.size(); i++) { // 查找有多少用户是某种类型的邀请码
			list.get(i).setType(
					"" + userManager.numUser(list.get(i).getID()).size());
		}
		return Action.SUCCESS;
	}

	// 根据邀请码ID显示所有用户
	private List<User> listUser;
	private List<UserTransaction> listUtran;
	private List<UserTransaction> utlist;
	private int countPay;

	public int getCountPay() {
		return countPay;
	}

	public void setCountPay(int countPay) {
		this.countPay = countPay;
	}

	public List<User> getListUser() {
		return listUser;
	}

	public void setListUser(List<User> listUser) {
		this.listUser = listUser;
	}

	public List<UserTransaction> getListUtran() {
		return listUtran;
	}

	public void setListUtran(List<UserTransaction> listUtran) {
		this.listUtran = listUtran;
	}

	public List<UserTransaction> getUtlist() {
		return utlist;
	}

	public void setUtlist(List<UserTransaction> utlist) {
		this.utlist = utlist;
	}

	public String findUserbyInviteId() {
		countPay = 0;
		listUser = userManager.numUser(Long.valueOf(this.getInviteCodeId()));
		for (int i = 0; i < listUser.size(); i++) {
			listUtran = userManager.findUserbyInviteId(listUser.get(i).getID());
			if (listUtran.size() > 0) {
				for (int j = 0; j < listUtran.size(); j++) {
					UserTransaction userTran = new UserTransaction();
					userTran.setID(listUtran.get(j).getID());
					userTran.setAddressName(listUtran.get(j).getAddressName());
					userTran.setItemName(listUtran.get(j).getItemName());
					utlist.add(userTran);
					System.out.println(utlist.get(j).getAddressName());
				}
				countPay++;
			}

		}
		return Action.SUCCESS;
	}

	// 邀请码随机数
	public String genRandomNum(int pwd_len) {
		// 35是因为数组是从0开始的，26个字母+10个数字
		final int maxNum = 36;
		int i; // 生成的随机数
		int count = 0; // 生成的密码的长度
		char[] str = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
				'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
				'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

		StringBuffer pwd = new StringBuffer("");
		Random r = new Random();
		while (count < pwd_len) {
			// 生成随机数，取绝对值，防止生成负数，

			i = Math.abs(r.nextInt(maxNum)); // 生成的数最大为36-1

			if (i >= 0 && i < str.length) {
				pwd.append(str[i]);
				count++;
			}
		}

		return pwd.toString();
	}
	
	private String ajax;
	private String json;
	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	// register for the landing page
	public String landingRegister() throws Exception{
		JSONObject jo = new JSONObject();
		if("true".equals(ajax)){
			if (user != null && userManager.get(user.getUserName()) != null) {
				message = "username";
				jo.accumulate("msg", message);
				json = jo.toString();
				return Action.JSON;
			}
			if (user.getUserName() == null || user.getUserName().equals("")) {
				user.setUserName(user.getEMail());
			}
			if (StringUtil.checkEmail(user.getEMail()) == false) {
				message = "email";
				jo.accumulate("msg", message);
				json = jo.toString();
				return Action.JSON;
			}
			if (user != null && userManager.getUserByEmail(user.getEMail()) != null) {
				message = "email";
				jo.accumulate("msg", message);
				json = jo.toString();
				return Action.JSON;
			}
			if(action!= null && action.equals("check")){
				message = "checksuccess";
				jo.accumulate("msg", message);
				json = jo.toString();
				return Action.JSON;
			}
			Md5PasswordEncoder md5 = new Md5PasswordEncoder();
			user.setPassword(md5.encodePassword(user.getPassword(), null));
			try {
				user.setID(null);
				user.setEnabled(true);
				userManager.add(user);
			} catch (Exception e) {
				this.addActionMessage("Fail to create user" + user.getUserName());
				return Action.ERROR;
			}
			emailConfirm();
			HttpServletRequest request = (HttpServletRequest) ActionContext
					.getContext().get(ServletActionContext.HTTP_REQUEST);
			if (!isf401kdomain(request.getRequestURL().toString())) {
				groupManager.addGroupUser(Configuration.GROUP_MEMBER_ID,
						user.getID());
				groupManager
						.addGroupUser(Configuration.GROUP_MPIQ_ID, user.getID());
			} else {
				// groupManager.addGroupUser(Configuration.GROUP_F401K_ID,
				// user.getID());
				groupManager
						.addGroupUser(Configuration.GROUP_MPIQ_ID, user.getID());
			}
			try {
				MYPLANIQPermissionManager mpm = new MYPLANIQPermissionManager();
				mpm.createOneUserPermission(user.getID());
			} catch (Exception e) {

			}

			saveforUserMarket(user, request.getRequestURL().toString());
			message = "success";
			jo.accumulate("msg", message);
			json = jo.toString();
			return Action.JSON;
		}else{
			if (user != null && userManager.get(user.getUserName()) != null) {
				message = "username";
				return Action.MESSAGE;
			}
			if (user.getUserName() == null || user.getUserName().equals("")) {
				user.setUserName(user.getEMail());
			}
			if (StringUtil.checkEmail(user.getEMail()) == false) {
				message = "email";
				return Action.MESSAGE;
			}
			if (user != null && userManager.getUserByEmail(user.getEMail()) != null) {
				message = "email";
				return Action.MESSAGE;
			}
			if(action!= null && action.equals("check")){
				message = "checksuccess";
				return Action.MESSAGE;
			}
			Md5PasswordEncoder md5 = new Md5PasswordEncoder();
			user.setPassword(md5.encodePassword(user.getPassword(), null));
			try {
				user.setID(null);
				user.setEnabled(true);
				userManager.add(user);
			} catch (Exception e) {
				this.addActionMessage("Fail to create user" + user.getUserName());
				return Action.ERROR;
			}
			emailConfirm();
			HttpServletRequest request = (HttpServletRequest) ActionContext
					.getContext().get(ServletActionContext.HTTP_REQUEST);
			if (!isf401kdomain(request.getRequestURL().toString())) {
				groupManager.addGroupUser(Configuration.GROUP_MEMBER_ID,
						user.getID());
				groupManager
						.addGroupUser(Configuration.GROUP_MPIQ_ID, user.getID());
			} else {
				// groupManager.addGroupUser(Configuration.GROUP_F401K_ID,
				// user.getID());
				groupManager
						.addGroupUser(Configuration.GROUP_MPIQ_ID, user.getID());
			}
			try {
				MYPLANIQPermissionManager mpm = new MYPLANIQPermissionManager();
				mpm.createOneUserPermission(user.getID());
			} catch (Exception e) {

			}

			saveforUserMarket(user, request.getRequestURL().toString());
			message = "success";
			return Action.MESSAGE;
		}
		

		
		

	}
	
	public String register() throws Exception {
		action = "register";
		System.out.print(this.getInviteCodeId());
		if (user != null && userManager.get(user.getUserName()) != null) {
			this.addFieldError("user.UserName",
					"The username has been used, please use another one.");
			if (type != null) {
				regType = type;
				eStr = "The username has been used, please use another one.";
				return "relogin";
			}
			return Action.INPUT;
		}
		if (user.getPassword() == null || user.getPassword().equals("")) {
			this.addFieldError(
					"user.Password",
					getText("The password could not be empty, please enter again."));
			if (type != null) {
				regType = type;
				eStr = "The password could not be empty, please enter again.";
				return "relogin";
			}
			return Action.INPUT;
		}
		if (user.getEMail() == null || user.getEMail().equals("")) {
			this.addActionError("The email can't be empty!");
			if (type != null) {
				regType = type;
				eStr = "The email can't be empty!";
				return "relogin";
			}
			return Action.INPUT;
		}
		if (user.getUserName() == null || user.getUserName().equals("")) {
			user.setUserName(user.getEMail());
		}
		// 判断是否有这个邀请码
		if (this.getInviteCodeId() == null || this.getInviteCodeId().equals("")) {

		} else {
			inviteCode = new InviteCode();
			inviteCode = inviteManage.getInviteId(this.getInviteCodeId());
			if (inviteCode != null) {
				user.setInviteCodeID(inviteCode.getID());
			} else {
				this.addFieldError(
						"inviteCodeId",
						getText("Your invitation code is invalid. If you do not have an invitation code, please leave it blank."));
				if (type != null) {
					regType = type;
					eStr = "This InviteCode is wrong!If you do not have an invitation code, please leave blank.";
					return "relogin";
				}
				return Action.INPUT;
			}
		}

		if (StringUtil.checkEmail(user.getEMail()) == false) {
			this.addActionError("You email address is illegal, please enter again!");
			if (type != null) {
				regType = type;
				eStr = "You email address is illegal, please enter again!";
				return "relogin";
			}
			return Action.INPUT;
		}
		if (user != null && userManager.getUserByEmail(user.getEMail()) != null) {
			this.addFieldError("user.EMail",
					"The email has been used, please use another one.");
			if (type != null) {
				regType = type;
				eStr = "The email has been used, please use another one.";
				return "relogin";
			}
			return Action.INPUT;
		}
		try {
			user.setID(null);
			user.setEnabled(true);
			userManager.add(user);
		} catch (Exception e) {
			this.addActionMessage("Fail to create user" + user.getUserName());
			return Action.ERROR;
		}
		emailConfirm();
		HttpServletRequest request = (HttpServletRequest) ActionContext
				.getContext().get(ServletActionContext.HTTP_REQUEST);
		if (!isf401kdomain(request.getRequestURL().toString())) {
			groupManager.addGroupUser(Configuration.GROUP_MEMBER_ID,
					user.getID());
			groupManager
					.addGroupUser(Configuration.GROUP_MPIQ_ID, user.getID());
		} else {
			// groupManager.addGroupUser(Configuration.GROUP_F401K_ID,
			// user.getID());
			groupManager
					.addGroupUser(Configuration.GROUP_MPIQ_ID, user.getID());
		}
		try {
			MYPLANIQPermissionManager mpm = new MYPLANIQPermissionManager();
			mpm.createOneUserPermission(user.getID());
		} catch (Exception e) {

		}

		saveforUserMarket(user, request.getRequestURL().toString());
		if (type != null && type.equals("subscrlogin")) {
			// regType = type;
			return Action.LOGIN;
		} else
			return Action.SUCCESS;

	}

	public boolean isf401kdomain(String url) {
		String[] strs = Configuration.get("f401kdomain").toString()
				.split("\\|");
		for (int i = 0; i < strs.length; i++) {
			if (url.toLowerCase().contains(strs[i].trim()))
				return true;
		}

		return false;
	}

	/**
	 * after register auto login the system for the subscribe user
	 */
	public String autoLoginForSubscr() {
		User us = userManager.get(userName);
		String username = us.getUserName();
		String password = us.getPassword();
		EmailLoginServiceImpl userDetail = (EmailLoginServiceImpl) com.lti.system.ContextHolder
				.getInstance().getApplicationContext()
				.getBean("emailLoginServiceImpl");
		List<String> authNames = userDetail.AuthoritiesByUsernameQuery(us);
		int authN = authNames.size();
		GrantedAuthority[] authorities = new GrantedAuthority[authN];
		for (int i = 0; i < authN; i++) {
			authorities[i] = new GrantedAuthorityImpl(authNames.get(i)
					.toString());
		}
		UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(
				username, password, authorities);
		result.setDetails(userDetail);
		SecurityContext sc = SecurityContextHolder.getContext();
		sc.setAuthentication(result);
		return Action.SUCCESS;
	}

	/**
	 * save for send market emails for the user
	 * 
	 * @param user
	 * @param url
	 */
	public void saveforUserMarket(User user, String url) {
		UserMarket userMarket = new UserMarket();
		String emailforUM = user.getEMail();
		// boolean hasEM = userManager.hasMarketEmail(emailforUM);
		userMarket.setUserEmail(emailforUM);
		userMarket.setUserFirstname(user.getFirstName() == null ? "" : user
				.getFirstName());
		userMarket.setUserLastname(user.getLastName() == null ? "" : user
				.getLastName());
		userMarket.setUserTelephone(user.getTelephone() == null ? "" : user
				.getTelephone());
		userMarket.setAddressCity(user.getAddressCity() == null ? "" : user
				.getAddressCity());
		userMarket.setAddressCountry(user.getAddressCountry() == null ? ""
				: user.getAddressCountry());
		userMarket.setAddressState(user.getAddressState() == null ? "" : user
				.getAddressState());
		userMarket.setUserAddress(user.getAddress() == null ? "" : user
				.getAddress());
		userMarket.setIsSend(true);
		if (isf401kdomain(url))
			userMarket.setGroupKey2("MPIQ");
		else
			userMarket.setGroupKey2("VF");
		userManager.saveMarketEmail(userMarket);
	}

	private boolean advisor = false;

	public boolean isAdvisor() {
		return advisor;
	}

	public void setAdvisor(boolean advisor) {
		this.advisor = advisor;
	}

	private boolean isfacebook = false;

	public String ViewUserDetails() {
		action = "viewUserDetails";
		if (userManager.getLoginUser() == null) {
			this.addActionError("You haven't logined. Please login first!");
			return Action.ERROR;
		} else {
			this.user = userManager.getLoginUser();
			if (user.getPassword().equals("validfijohnc403")) {
				isfacebook = true;
			}
			List<Group> grs = userManager.getGroupsByUser(user.getID());
			if (grs != null && grs.size() > 0) {
				for (int i = 0; i < grs.size(); i++) {
					if (grs.get(i).getID().longValue() == Configuration.GROUP_ADVISOR_ID) {
						advisor = true;
					}
				}
			}
			if (user != null)
				user.setPassword("");
			return Action.SUCCESS;
		}

	}

	private File logoFile;

	public File getLogoFile() {
		return logoFile;
	}

	public void setLogoFile(File logoFile) {
		this.logoFile = logoFile;
	}

	public String UpdateUserDetails() {

		action = "updateUserDetails";

		if (!"".equals(oldPassword)) {
			if (newPassword.equals("") || newPassword == null) {
				this.addFieldError("password",
						"The password could not be empty, please enter again.");
				return Action.INPUT;
			} else if (oldPassword.equals(userManager.get(user.getID())
					.getPassword()))

				user.setPassword(newPassword);

			else {
				this.addFieldError("password",
						"The old password is not correct, please enter again.");
				return Action.INPUT;
			}
		} else if (userManager.get(user.getID()).getPassword()
				.equals("validfijohnc403")) {
			if (newPassword.equals("") || newPassword == null) {
				this.addFieldError("password",
						"The password could not be empty, please enter again.");
				return Action.INPUT;
			} else {
				user.setPassword(newPassword);
			}
		}

		action = "updateUserDetails";
		if (!userManager.get(user.getUserName()).getID().equals(user.getID())) {
			this.addFieldError("username",
					"You have not right to modify the information.");
			return Action.INPUT;
		} else {
			try {
				user.setEnabled(userManager.get(user.getID()).getEnabled());
				if (user.getPassword() == null || user.getPassword().equals(""))
					user.setPassword(userManager.get(user.getID())
							.getPassword());

				if (logoFile != null && logoFile.exists()) {
					File targetFile = new File(ContextHolder.getServletPath()
							+ "/UserLogos/", user.getID() + ".jpg");
					FileUtils.copyFile(logoFile, targetFile);
					user.setLogo("UserLogos/" + user.getID() + ".jpg");
				}

				List<Group> grs = userManager.getGroupsByUser(user.getID());
				if (grs != null && grs.size() > 0) {
					for (int i = 0; i < grs.size(); i++) {
						if (grs.get(i).getID().longValue() == Configuration.GROUP_ADVISOR_ID) {
							advisor = true;
						}
					}
				}

				if (user.getAddressZip().trim().replaceAll(",+", "").trim()
						.equals("")) {
					user.setAddressZip("");
				}

				userManager.update(user);
			} catch (Exception e) {
				actionMessage = "Failed to update.";
				return Action.INPUT;
			}
			actionMessage = "Update successfully.";
			return Action.SUCCESS;
		}
	}

	public Boolean emailConfirm() {
		String confirm = (String) Configuration.get("email.confirm");
		if (confirm.equals("false"))
			return true;
		else {
			// String host = (String) Configuration.get("email.host");
			// String username = (String) Configuration.get("email.username");
			// String password = (String) Configuration.get("email.password");
			// String from = (String) Configuration.get("email.from");
			String subject = (String) Configuration.get("email.subject");
			// String text = (String) Configuration.get("email.mailbody") +
			// "Your username is: " + user.getUserName();
			// String protocol = (String) Configuration.get("email.protocol");
			// int port = Integer.parseInt((String)
			// Configuration.get("email.port"));

			// sender.setHost(host);
			// sender.setUsername(username);
			// sender.setPassword(password);

			Boolean flag = true;

			if (user.getEMail() != null && !user.getEMail().equals("")) {
				try {
					// SimpleMailMessage msg = new SimpleMailMessage();
					// msg.setFrom(from);
					// msg.setTo(user.getEMail());
					// msg.setSubject(subject);
					// msg.setText(text);
					// sender.setProtocol(protocol);
					// sender.setPort(port);
					EmailUtil.sendMail(new String[] { user.getEMail() },
							subject, generateItem(user.getUserName()));
					// sender.send(msg);
				} catch (Exception e) {
					actionMessage = e.getMessage();
					flag = false;
					e.printStackTrace();
				}

			}
			return flag;
		}
	}

	public static String generateItem(String user) {

		try {
			freemarker.template.Configuration conf = new freemarker.template.Configuration();
			try {
				String path = new registerAction().getClass()
						.getProtectionDomain().getCodeSource().getLocation()
						.getPath();
				if (path.indexOf("WEB-INF") > 0) {
					path = path.substring(0, path.indexOf("WEB-INF/classes"));
				} else {
					throw new IllegalAccessException("");
				}
				System.out.print(path + "jsp");
				conf.setDirectoryForTemplateLoading(new File(
						path + "jsp"));
			} catch (IOException e) {
				ContextHolder.addException(e);
			}
			Template itemplate;
			itemplate = conf.getTemplate("EmailItem.uftl");

			Map<String, Object> data = new HashMap<String, Object>();
			data.put("user", user);
			StringWriter sw = new StringWriter();
			itemplate.process(data, sw);
			return sw.toString();
		} catch (Exception e) {
			ContextHolder.addException(e);
			// e.printStackTrace();
		}
		return null;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public UserManager getUserManager() {
		return userManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public GroupManager getGroupManager() {
		return groupManager;
	}

	public void setGroupManager(GroupManager groupManager) {
		this.groupManager = groupManager;
	}

	public JavaMailSenderImpl getSender() {
		return sender;
	}

	public void setSender(JavaMailSenderImpl sender) {
		this.sender = sender;
	}

	public String getActionMessage() {
		return actionMessage;
	}

	public void setActionMessage(String actionMessage) {
		this.actionMessage = actionMessage;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getRegType() {
		return regType;
	}

	public void setRegType(String regType) {
		this.regType = regType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getErrString() {
		return errString;
	}

	public void setErrString(String errString) {
		this.errString = errString;
	}

	public String getEStr() {
		return eStr;
	}

	public void setEStr(String str) {
		eStr = str;
	}

	public boolean isIsfacebook() {
		return isfacebook;
	}

	public void setIsfacebook(boolean isfacebook) {
		this.isfacebook = isfacebook;
	}

	public InviteCode getInviteCode() {
		return inviteCode;
	}

	public void setInviteCode(InviteCode inviteCode) {
		this.inviteCode = inviteCode;
	}

	public void setInviteManage(InviteManager inviteManage) {
		this.inviteManage = inviteManage;
	}

	public String getInviteName() {
		return inviteName;
	}

	public void setInviteName(String inviteName) {
		this.inviteName = inviteName;
	}

	public String getInviteCodecode() {
		return inviteCodecode;
	}

	public void setInviteCodecode(String inviteCodecode) {
		this.inviteCodecode = inviteCodecode;
	}

	public InviteManager getInviteManage() {
		return inviteManage;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getInviteCodeId() {
		return inviteCodeId;
	}

	public void setInviteCodeId(String inviteCodeId) {
		this.inviteCodeId = inviteCodeId;
	}

	private String middleMan;

	public String getMiddleMan() {
		return middleMan;
	}

	public void setMiddleMan(String middleMan) {
		this.middleMan = middleMan;
	}

}