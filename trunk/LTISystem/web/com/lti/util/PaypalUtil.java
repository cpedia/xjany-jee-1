package com.lti.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import com.lti.service.bo.UserTransaction;

public class PaypalUtil {
	private final static String PAYPAL_SANDBOX_URL = "https://www.sandbox.paypal.com/cgi-bin/webscr";
	private final static String PAYPAL_URL = "https://www.paypal.com/cgi-bin/webscr";
	private final static String RECEIVER_EMAIL_SANDBOX="seller_1269825668_biz@126.com";
	private final static String RECEIVER_EMAIL="jzhong@ltisystem.com";
	private final static String RECEIVER_EMAIL2="johnz@myplaniq.com";
	public final static String STATUS_VERIFIED = "VERIFIED";
	public final static String STATUS_INVALID = "INVALID";

	HttpServletRequest request;

	public PaypalUtil(HttpServletRequest r) {
		request = r;
	}

	public String getParameterString() throws Exception {
		StringBuffer sb = new StringBuffer();
		Enumeration en = request.getParameterNames();
		boolean first = true;
		while (en.hasMoreElements()) {
			String paramName = (String) en.nextElement();
			String paramValue = request.getParameter(paramName);
			if (!first)
				sb.append("&");
			if (first)
				first = false;
			if (paramName.equals("username"))
				continue;
			sb.append(paramName);
			sb.append("=");
			sb.append(URLEncoder.encode(paramValue, "utf8"));
		}
		return sb.toString();
	}

	public String getAllParameters() {
		StringBuffer sb = new StringBuffer();
		Enumeration en = request.getParameterNames();
		boolean first = true;
		while (en.hasMoreElements()) {
			String paramName = (String) en.nextElement();
			String paramValue = request.getParameter(paramName);
			if (!first)
				sb.append("&");
			if (first)
				first = false;
			sb.append(paramName);
			sb.append(": ");
			sb.append(paramValue);
			sb.append("\r\n");
		}
		return sb.toString();
	}

	public String sendRequest(String parameters, String url) throws Exception {
		URL u = new URL(url);
		URLConnection uc = u.openConnection();
		uc.setDoOutput(true);
		uc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		PrintWriter pw = new PrintWriter(uc.getOutputStream());
		pw.println(parameters);
		pw.close();

		BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
		String res = in.readLine();
		in.close();
		return res;
	}

	public String verify() throws Exception {
		if(!getReceiverEMail().trim().toLowerCase().equals(RECEIVER_EMAIL)&&!getReceiverEMail().trim().toLowerCase().equals(RECEIVER_EMAIL2))return STATUS_INVALID;
		return sendRequest("cmd=_notify-validate&" + getParameterString(), PAYPAL_URL);
	}
	
	public String verify_sandbox() throws Exception {
		if(!getReceiverEMail().trim().equals(RECEIVER_EMAIL_SANDBOX))return STATUS_INVALID;
		return sendRequest("cmd=_notify-validate&" + getParameterString(), PAYPAL_SANDBOX_URL);
	}
	
	public static Date getEndDate(String period1,Date startDate){
		if(period1==null||period1.equals(""))return startDate;
		Calendar ca=Calendar.getInstance();
		ca.setTime(startDate);
		if(period1.toUpperCase().endsWith("D")){
			try {
				ca.add(Calendar.DAY_OF_YEAR, Integer.parseInt(period1.replace("D", "").trim()));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}else if(period1.toUpperCase().endsWith("W")){
			try {
				ca.add(Calendar.DAY_OF_WEEK, 7*Integer.parseInt(period1.replace("W", "").trim()));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}else if(period1.toUpperCase().endsWith("M")){
			try {
				ca.add(Calendar.MONTH, Integer.parseInt(period1.replace("M", "").trim()));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}else if(period1.toUpperCase().endsWith("Y")){
			try {
				ca.add(Calendar.YEAR, Integer.parseInt(period1.replace("Y", "").trim()));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		return ca.getTime();
	}

	public String getPaymentFee() {
		return request.getParameter(KEY_PAYMENT_FEE);
	}

	public String getFirstName() {
		return request.getParameter(KEY_FIRST_NAME);
	}

	public double getMCFee() {
		String mcfee = request.getParameter(KEY_MC_FEE);
		try {
			return Double.parseDouble(mcfee);
		} catch (Exception e) {
			return 0.0;
		}
	}

	public String getBusiness() {
		return request.getParameter(KEY_BUSINESS);
	}

	public double getMCGross() {
		String mcfee = request.getParameter(KEY_MC_GROSS);
		try {
			return Double.parseDouble(mcfee);
		} catch (Exception e) {
			return 0.0;
		}
	}

	public String getSubScrID() {
		return request.getParameter(KEY_SUBSCR_ID);
	}

	public String getPaymentType() {
		return request.getParameter(KEY_PAYMENT_TYPE);
	}

	public String getItemName() {
		return request.getParameter(KEY_ITEM_NAME);
	}

	public String getPayerID() {
		return request.getParameter(KEY_PAYER_ID);
	}

	public String getReceiverID() {
		return request.getParameter(KEY_RECEIVER_ID);
	}

	public String getPayerEMail() {
		return request.getParameter(KEY_PAYER_EMAIL);
	}

	public String getReceiverEMail() {
		return request.getParameter(KEY_RECEIVER_EMAIL);
	}

	public String getPaymentStatus() {
		return request.getParameter(KEY_PAYMENT_STATUS);
	}

	public String getReasonCode() {
		return request.getParameter(KEY_REASON_CODE);
	}

	public double getPaymentGross() {
		String mcfee = request.getParameter(KEY_PAYMENT_GROSS);
		try {
			return Double.parseDouble(mcfee);
		} catch (Exception e) {
			return 0.0;
		}
	}

	public String getLastName() {
		return request.getParameter(KEY_LAST_NAME);
	}

	public Date getPaymentDate() {
		String mcfee = request.getParameter(KEY_PAYMENT_DATE);
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss MMM dd, yyyy z", Locale.US);
			return sdf.parse(mcfee);
		} catch (Exception e) {
			return null;
		}
	}

	public Date getSubscrDate() {
		String mcfee = request.getParameter(KEY_SUBSCR_DATE);
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss MMM dd, yyyy z", Locale.US);
			return sdf.parse(mcfee);
		} catch (Exception e) {
			return null;
		}
	}

	public String getTXNID() {
		return request.getParameter(KEY_TXN_ID);
	}

	public String getParentTXNID() {
		return request.getParameter(KEY_PARENT_TXN_ID);
	}

	public String getTXNType() {
		return request.getParameter(KEY_TXN_TYPE);
	}

	public String getType() {
		String txntype = getTXNType();
		String status = getPaymentStatus();
		if (txntype == null) {
			if (status != null && status.equals(REFUNDED_PAYMENT_STATUS))
				return REFUND_TYPE;
			else 
				return OTHER_TYPE;
		}
		if (txntype.equals(PAYMENT_TXN_TYPE)) {
			if (status != null && status.equals(COMPLETED_PAYMENT_STATUS))
				return PAY_TYPE;
			else return PAY_TYPE+"_"+status;
		}
		if (txntype.equals(CANCEL_TXN_TYPE)) {
			return CANCEL_TYPE;
		}
		if (txntype.equals(SIGNUP_TXN_TYPE)) {
			return TRY_TYPE;
		}
		return OTHER_TYPE;

	}

	public final static String PAY_TYPE = "pay";
	public final static String TRY_TYPE = "try";
	public final static String CANCEL_TYPE = "cancel";
	public final static String REFUND_TYPE = "refund";
	public final static String OTHER_TYPE = "other";

	public final static String KEY_SUBSCR_DATE = "subscr_date";

	public final static String KEY_TXN_TYPE = "txn_type";
	public final static String CANCEL_TXN_TYPE = "subscr_cancel";
	public final static String SIGNUP_TXN_TYPE = "subscr_signup";
	public final static String PAYMENT_TXN_TYPE = "subscr_payment";

	public final static String KEY_PAYMENT_FEE = "payment_fee";
	public final static String KEY_FIRST_NAME = "first_name";
	public final static String KEY_MC_FEE = "mc_fee";
	public final static String KEY_BUSINESS = "business";
	public final static String KEY_MC_GROSS = "mc_gross";
	public final static String KEY_SUBSCR_ID = "subscr_id";
	public final static String KEY_PAYMENT_TYPE = "payment_type";
	public final static String KEY_ITEM_NAME = "item_name";
	public final static String KEY_PAYER_ID = "payer_id";
	public final static String KEY_RECEIVER_ID = "receiver_id";
	public final static String KEY_PAYER_EMAIL = "payer_email";
	public final static String KEY_RECEIVER_EMAIL = "receiver_email";

	public final static String KEY_PAYMENT_STATUS = "payment_status";
	public final static String REFUNDED_PAYMENT_STATUS = "Refunded";
	public final static String COMPLETED_PAYMENT_STATUS = "Completed";

	public final static String KEY_REASON_CODE = "reason_code";
	public final static String REFUND_REASON_CODE = "refund";

	public final static String KEY_PAYMENT_GROSS = "payment_gross";
	public final static String KEY_LAST_NAME = "last_name";
	public final static String KEY_PAYMENT_DATE = "payment_date";

	public final static String KEY_TXN_ID = "txn_id";
	public final static String KEY_PARENT_TXN_ID = "parent_txn_id";

	public UserTransaction getUserTransaction() {
		UserTransaction transaction = new UserTransaction();
		BeanInfo targetbean = null;
		PropertyDescriptor[] propertyDescriptors = null;
		try {
			targetbean = Introspector.getBeanInfo(transaction.getClass());
			propertyDescriptors = targetbean.getPropertyDescriptors();

		} catch (Exception e2) {
		}

		for (int i = 0; i < propertyDescriptors.length; i++) {

			PropertyDescriptor pro = propertyDescriptors[i];
			Method wm = pro.getWriteMethod();

			if (wm != null) {

				if (!Modifier.isPublic(wm.getDeclaringClass().getModifiers())) {
					wm.setAccessible(true);
				}
				Enumeration en = request.getParameterNames();
				while (en.hasMoreElements()) {
					String paramName = (String) en.nextElement();
					String paramValue = request.getParameter(paramName);
					if (pro.getName().toLowerCase().equals(paramName.replace("_", "").toLowerCase())) {
						if(paramValue==null||paramValue.equals(""))break;
						try {
							if (pro.getPropertyType().getName().equals("java.lang.String")) {
								wm.invoke((Object) transaction, new Object[] { paramValue });
							}else if (pro.getPropertyType().getName().equals("java.lang.Double")||pro.getPropertyType().getName().toLowerCase().equals("double")){
								wm.invoke((Object) transaction, new Object[] { Double.parseDouble(paramValue) });
							}else if (pro.getPropertyType().getName().equals("java.lang.Long")||pro.getPropertyType().getName().toLowerCase().equals("long")){
								wm.invoke((Object) transaction, new Object[] { Long.parseLong(paramValue) });
							}else if (pro.getPropertyType().getName().equals("java.util.Date")||pro.getPropertyType().getName().toLowerCase().equals("date")){
								DateFormat sdf = new SimpleDateFormat("HH:mm:ss MMM dd, yyyy z", Locale.US);
								wm.invoke((Object) transaction, new Object[] { sdf.parse(paramValue) });
							}
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						} catch (Exception e) {
							e.printStackTrace();
						}
						break;
					}
				}

			}
		}
		return transaction;
	}

	public static void main(String[] args) {
		String s = "18:46:52 Mar 17, 2010 PDT";
		DateFormat sdf = new SimpleDateFormat("HH:mm:ss MMM dd, yyyy z", Locale.US);
		try {
			sdf.parse(s);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
