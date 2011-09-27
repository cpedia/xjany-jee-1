package com.lti.action.register;

import java.util.Random;

import org.acegisecurity.providers.encoding.Md5PasswordEncoder;

import com.lti.action.Action;
import com.lti.service.UserManager;
import com.lti.service.bo.User;
import com.lti.system.ContextHolder;
import com.opensymphony.xwork2.ActionSupport;

public class PasswordResetAction extends ActionSupport implements Action {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UserManager userManager;
	private User user;
	private String UserName;
	private String CharCode;
	private String password;
	private String cpassword;
	private String action = "";
	private String curPassword;
	private String passwordMessage;
	private String c;

	public String getC() {
		return c;
	}

	public void setC(String c) {
		this.c = c;
	}

	public String getPasswordMessage() {
		return passwordMessage;
	}

	public void setPasswordMessage(String passwordMessage) {
		this.passwordMessage = passwordMessage;
	}

	public String getCurPassword() {
		return curPassword;
	}

	public void setCurPassword(String curPassword) {
		this.curPassword = curPassword;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public void validate() {
		if (UserName == null || UserName.equalsIgnoreCase("")) {
			return;
		}
		if (CharCode == null || CharCode.equalsIgnoreCase("")) {
			return;
		}
		if (password == null || cpassword == null || password.equalsIgnoreCase("") || cpassword.equalsIgnoreCase("")) {
			return;
		}

	}

	private String message = null;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String execute() throws Exception {
		if (!action.equals("") && action.equalsIgnoreCase("permission")) {
			user = ContextHolder.getUserManager().getLoginUser();
			Md5PasswordEncoder md5 = new Md5PasswordEncoder();
			String dbpassword = user.getPassword();
			curPassword = md5.encodePassword(curPassword, null);
			if (curPassword.equals(dbpassword)) {
				passwordMessage = "You current password is right.";
			} else {
				passwordMessage = "You current password is wrong, please try again.";
			}
			return "meassage";
		} else {
			if(c!=null){
				message="";
				return Action.INPUT;
			}
			// List<User> userlist = userManager.getUsers();
			if (UserName == null || UserName.equalsIgnoreCase("")) {
				// return Action.INPUT;
			}
			if ( CharCode == null) {
				message="The verification code doesn't exist or become invalid.";
				return Action.INPUT;
			}
			// for (int i = 0; i < userlist.size(); i++) {
			user = userManager.getUserByCharCode(CharCode);
			// if (user.getUserName().equalsIgnoreCase(UserName)) {
			if ( user == null) {
				message="The verification code is invalid.";
				return Action.INPUT;
			}
			
			
			if (password.equalsIgnoreCase("") || cpassword.equalsIgnoreCase("")||!password.equalsIgnoreCase(cpassword)) {
				c=CharCode;
				message="The passowrd deosn't match the confirmation password or it is empty.";
				return Action.INPUT;
			}
			String rc = RandomChar();
			user.setPassword(password);
			Md5PasswordEncoder md5 = new Md5PasswordEncoder();
			user.setPassword(md5.encodePassword(user.getPassword(), null));
			user.setCharCode(rc);
			userManager.update(user);
			this.addActionMessage("successfull");
			return Action.SUCCESS;

		}
	}

	public String RandomChar() {

		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int j = 0; j < 20; j++) {
			int r = 0;
			while (true) {
				r = random.nextInt(57) + 65;
				if (r > 90 && r < 97 || r == 0)
					continue;
				break;
			}
			char a = (char) r;
			sb.append(a);
		}
		return sb.toString();
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public UserManager getUserManager() {
		return userManager;
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	public String getCpassword() {
		return cpassword;
	}

	public void setCpassword(String cpassword) {
		this.cpassword = cpassword;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCharCode() {
		return CharCode;
	}

	public void setCharCode(String charCode) {
		CharCode = charCode;
	}

}
