package com.lti.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.supercsv.io.CsvListReader;
import org.supercsv.io.CsvListWriter;
import org.supercsv.prefs.CsvPreference;

import com.lti.service.UserManager;
import com.lti.service.bo.User;
import com.lti.service.bo.UserProfile;
import com.lti.service.bo.UserTransaction;
import com.lti.system.ContextHolder;
import com.lti.util.LTIDate;
import com.lti.util.LTIDownLoader;

public class userCountaction {
	private String planName;
	private InputStream fis;
	private String userDate;

	public String getUserDate() {
		return userDate;
	}

	public void setUserDate(String userDate) {
		this.userDate = userDate;
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public InputStream getFis() {
		return fis;
	}

	public void setFis(InputStream fis) {
		this.fis = fis;
	}

	// 下载日统计情况
	public String userStatisticsByDay() throws IOException, ParseException {
		CsvListWriter clw = null;
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("yyyy_MM_dd");
		Date dateParse = df.parse(df.format(date));
		// 设置下载存放路径
		String filePath = new LTIDownLoader().systemPath;
		String path = filePath + "Statistic Of User Development.csv";
		File file = new File(path);

		if (userDate == null || userDate.length() == 0) {
			clw = new CsvListWriter(new FileWriter(file, true),
					CsvPreference.EXCEL_PREFERENCE);
			CsvListReader reader = new CsvListReader(new FileReader(file),
					CsvPreference.EXCEL_PREFERENCE);
			CsvListReader readerAgain = new CsvListReader(new FileReader(file),
					CsvPreference.EXCEL_PREFERENCE);
			String[] str = null;
			str = reader.getCSVHeader(true);
			readerAgain.getCSVHeader(true);
			// 判断是否需要增加cvs头部
			if (reader == null || str == null) {
				String[] header = { "date", "total_register",
						"total_subscriber", "total_basic", "total_expert",
						"total_pro", "new_register", "new_subscriber",
						"new_cancellation", "new_basic", "cancel_basic",
						"new_expert", "cancel_expert", "new_pro", "cancel_pro",
						"new_payment" };
				clw.writeHeader(header);
				List<String> strs = new ArrayList<String>();
				strs = getStrs(date, 10);
				clw.write(strs);
			} else {
				List<String> listStr;

				// 获取csv最后一行的日期
				String readDateStr = "";
				int lines = 0;
				while ((listStr = reader.read()) != null) {
					readDateStr = listStr.get(0);
					lines++;
				}

				Date readDate = df.parse(readDateStr);
				// 如果为现在时间，则重新计算一次，把原来的那条记录覆盖掉
				if (readDateStr.equals(df.format(date))) {
					clw = new CsvListWriter(new FileWriter(file),
							CsvPreference.EXCEL_PREFERENCE);
					String[] header = { "date", "total_register",
							"total_subscriber", "total_basic", "total_expert",
							"total_pro", "new_register", "new_subscriber",
							"new_cancellation", "new_basic", "cancel_basic",
							"new_expert", "cancel_expert", "new_pro",
							"cancel_pro", "new_payment" };
					clw.writeHeader(header);
					for (int i = 0; i < lines-1; i++) {
						List<String> lStr = new ArrayList<String>();
						String strAgain = readerAgain.read().toString();
						String[] strArray2 = strAgain.split("]");
						String[] strArray = strArray2[0].split(",");
						for (int j = 0; j < strArray.length; j++) {
							if (j == 0) {
								lStr.add(strArray[0].substring(1, 11));
							} else {
								lStr.add(strArray[j]);
							}
						}
						clw.write(lStr);
					}
					List<String> strs = new ArrayList<String>();
					strs = getStrs(date, 10);
					clw.write(strs);
				} else if (dateParse.getTime() / 1000 - readDate.getTime()
						/ 1000 == 86400) {
					// 检测csv中最后一条记录是否为昨天记录，为真，则直接把今天数据加入下一条
					List<String> strs = new ArrayList<String>();
					strs = getStrs(date, 10);
					clw.write(strs);
				} else {
					// 计算出最后一行的日期跟当前日期的差值,将中间的差值补上
					Date today = new Date();
					String startDateStr= readDateStr.substring(0,4)+"-"+readDateStr.substring(5,7)+"-"+readDateStr.substring(8,10);
					Date startDate = LTIDate.parseStringToDate(startDateStr);
					while(LTIDate.before(startDate, today)){
						startDate = LTIDate.getTomorrow(startDate);
						List<String> strs = new ArrayList<String>();
						strs = getStrs(startDate, 10);
						clw.write(strs);
					}
				}
			}
		} else {
			clw = new CsvListWriter(new FileWriter(file),
					CsvPreference.EXCEL_PREFERENCE);
			String[] header = { "date", "total_register", "total_subscriber",
					"total_basic", "total_expert", "total_pro", "new_register",
					"new_subscriber", "new_cancellation", "new_basic",
					"cancel_basic", "new_expert", "cancel_expert", "new_pro",
					"cancel_pro", "new_payment" };
			clw.writeHeader(header);
			// 转换文本框中的日期
			Date readDate = df.parse(userDate);
			if (dateParse.getTime() / 1000 - readDate.getTime() / 1000 == 86400) {
				List<String> strs = new ArrayList<String>();
				strs = getStrs(date, 10);
				clw.write(strs);
			} else {
				// 先统计文本框当天的信息
				List<String> strs1 = new ArrayList<String>();
				strs1 = getStrs(df.parse(userDate), 10);
				clw.write(strs1);
				// 计算出文本框的日期跟当前日期的差值
				Date today = new Date();
				String startDateStr= userDate.substring(0,4)+"-"+userDate.substring(5,7)+"-"+userDate.substring(8,10);
				Date startDate = LTIDate.parseStringToDate(startDateStr);
				while(LTIDate.before(startDate, today)){
					startDate = LTIDate.getTomorrow(startDate);
					List<String> strs = new ArrayList<String>();
					strs = getStrs(startDate, 10);
					clw.write(strs);
				}
//				long differ = dateParse.getTime() / 1000 - readDate.getTime()
//						/ 1000;
//				for (int i = 0; i < differ / 86400; i++) {
//					long readDateLong = readDate.getTime() / 1000 + 86400;
//					readDate.setTime(readDateLong * 1000);
//					List<String> strs = new ArrayList<String>();
//					strs = getStrs(readDate, 10);
//					clw.write(strs);
//				}
			}
		}
		clw.close();

		// 下载生成csv
		planName = "Statistic Of User Development.csv";
		File f = new File(path);
		fis = new FileInputStream(f);
		return Action.DOWNLOAD;
	}

	// 月统计情况
	public String userStatisticsByMonth() throws IOException, ParseException {
		CsvListWriter clw = null;
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("yyyy_MM");
		DateFormat dfMonth = new SimpleDateFormat("MM");
		DateFormat dfYear = new SimpleDateFormat("yyyy");
		String dateFormat = df.format(date);
//		Date dateParse = df.parse(df.format(date));
		// 设置下载存放路径
		String filePath = new LTIDownLoader().systemPath;
		String path = filePath + "Statistic Of User Development By Month.csv";
		File file = new File(path);

		if (userDate == null || userDate.length() == 0) {
			clw = new CsvListWriter(new FileWriter(file, true),
					CsvPreference.EXCEL_PREFERENCE);
			CsvListReader reader = new CsvListReader(new FileReader(file),
					CsvPreference.EXCEL_PREFERENCE);
			CsvListReader readerAgain = new CsvListReader(new FileReader(file),
					CsvPreference.EXCEL_PREFERENCE);
			String[] str = null;
			str = reader.getCSVHeader(true);
			readerAgain.getCSVHeader(true);
			// 判断是否需要增加cvs头部
			if (reader == null || str == null) {
				String[] header = { "date", "total_register",
						"total_subscriber", "total_basic", "total_expert",
						"total_pro", "new_register", "new_subscriber",
						"new_cancellation", "new_basic", "cancel_basic",
						"new_expert", "cancel_expert", "new_pro", "cancel_pro",
						"new_payment" };
				clw.writeHeader(header);
				List<String> strs = new ArrayList<String>();
				strs = getStrs(date, 7);
				clw.write(strs);
			} else {
				List<String> listStr;
				String readDateStr = "";
				// 获取csv最后一行的日期
				int lines = 0;
				while ((listStr = reader.read()) != null) {
					readDateStr = df.format(df.parse(listStr.get(0)))
							.substring(0, 7);
					lines++;
				}
				Date readDate = df.parse(readDateStr);
				//如果月份为当前月份，则删除最后一行，重新写入csv中
				if (dateFormat.equals(readDateStr)) {
					clw = new CsvListWriter(new FileWriter(file),
							CsvPreference.EXCEL_PREFERENCE);
					String[] header = { "date", "total_register",
							"total_subscriber", "total_basic", "total_expert",
							"total_pro", "new_register", "new_subscriber",
							"new_cancellation", "new_basic", "cancel_basic",
							"new_expert", "cancel_expert", "new_pro",
							"cancel_pro", "new_payment" };
					clw.writeHeader(header);
					for (int i = 0; i < lines-1; i++) {
						List<String> lStr = new ArrayList<String>();
						String strAgain = readerAgain.read().toString();
						String[] strArray2 = strAgain.split("]");
						String[] strArray = strArray2[0].split(",");
						for (int j = 0; j < strArray.length; j++) {
							if (j == 0) {
								lStr.add(strArray[0].substring(1, 8));
							} else {
								lStr.add(strArray[j]);
							}
						}
						clw.write(lStr);
					}
					List<String> strs = new ArrayList<String>();
					strs = getStrs(date, 7);
					clw.write(strs);
				} else {
					// 计算出最后一行的月份跟当前月份的差值
					Integer readMonth = Integer.parseInt(dfMonth
							.format(readDate));
					Integer nowMonth = Integer.parseInt(dfMonth.format(date));
					Integer readYear = Integer
							.parseInt(dfYear.format(readDate));
					Integer nowYear = Integer.parseInt(dfYear.format(date));
					if (readYear.equals(nowYear)) {
						int differMonth = nowMonth - readMonth;
						for (int m = 0; m < differMonth; m++) {
							List<String> strs = new ArrayList<String>();
							readMonth = readMonth + 1;
							String dateByNew = nowYear + "_" + readMonth;
							strs = getStrs(df.parse(dateByNew), 7);
							clw.write(strs);
						}
					} else {
						int differYear = nowYear - readYear;
						for (int n = 0; n <= differYear; n++) {
							// 判断是否累加到同一年
							if (readYear.equals(nowYear)) {
								int differMonth = nowMonth;
								for (int m = 1; m <= differMonth; m++) {
									List<String> strs = new ArrayList<String>();
									String dateByNew = nowYear + "_" + m;
									strs = getStrs(df.parse(dateByNew), 7);
									clw.write(strs);
								}
								readYear = readYear + 1;
							} else {
								// 计算readYear剩几个月
								if (n == 0) {
									int differMonth = 12 - readMonth;
									for (int m = 0; m < differMonth; m++) {
										List<String> strs = new ArrayList<String>();
										readMonth = readMonth + 1;
										String dateByNew = readYear + "_"
												+ readMonth;
										strs = getStrs(df.parse(dateByNew), 7);
										clw.write(strs);
									}
									readYear = readYear + 1;
								} else {
									for (int m = 1; m < 13; m++) {
										List<String> strs = new ArrayList<String>();
										String dateByNew = readYear + "_" + m;
										strs = getStrs(df.parse(dateByNew), 7);
										clw.write(strs);
									}
									readYear = readYear + 1;
								}
							}
						}
					}
				}
			}
		} else {
			clw = new CsvListWriter(new FileWriter(file),
					CsvPreference.EXCEL_PREFERENCE);
			String[] header = { "date", "total_register", "total_subscriber",
					"total_basic", "total_expert", "total_pro", "new_register",
					"new_subscriber", "new_cancellation", "new_basic",
					"cancel_basic", "new_expert", "cancel_expert", "new_pro",
					"cancel_pro", "new_payment" };
			clw.writeHeader(header);
			// 转换文本框中的日期
			Date readDate = df.parse(userDate);
			if (dateFormat.equals(df.format(readDate))) {
				 List<String> strs = new ArrayList<String>();
				 strs = getStrs(date, 7);
				 clw.write(strs);
			} else {
				// 计算出文本框的月份跟当前月份的差值
				Integer readMonth = Integer.parseInt(dfMonth.format(readDate));
				Integer nowMonth = Integer.parseInt(dfMonth.format(date));
				Integer readYear = Integer.parseInt(dfYear.format(readDate));
				Integer nowYear = Integer.parseInt(dfYear.format(date));
				if (readYear.equals(nowYear)) {
					int differMonth = nowMonth - readMonth;
					for (int m = 0; m <= differMonth; m++) {
						List<String> strs = new ArrayList<String>();
						String dateByNew = nowYear + "_" + readMonth;
						strs = getStrs(df.parse(dateByNew), 7);
						readMonth = readMonth + 1;
						clw.write(strs);
					}
				} else {
					int differYear = nowYear - readYear;
					for (int n = 0; n <= differYear; n++) {
						// 判断是否累加到同一年
						if (readYear.equals(nowYear)) {
							int differMonth = nowMonth;
							for (int m = 1; m <= differMonth; m++) {
								List<String> strs = new ArrayList<String>();
								String dateByNew = nowYear + "_" + m;
								strs = getStrs(df.parse(dateByNew), 7);
								clw.write(strs);
							}
						} else {
							// 计算readYear剩几个月
							if (n == 0) {
								int differMonth = 12 - readMonth;
								for (int m = 0; m <= differMonth; m++) {
									List<String> strs = new ArrayList<String>();
									String dateByNew = readYear + "_"
											+ readMonth;
									strs = getStrs(df.parse(dateByNew), 7);
									readMonth = readMonth + 1;
									clw.write(strs);
								}
								readYear = readYear + 1;
							} else {
								for (int m = 1; m < 13; m++) {
									List<String> strs = new ArrayList<String>();
									String dateByNew = readYear + "_" + m;
									strs = getStrs(df.parse(dateByNew), 7);
									clw.write(strs);
								}
								readYear = readYear + 1;
							}
						}
					}
				}
			}
		}
		clw.close();

		// 下载生成csv
		planName = "Statistic Of User Development By Month.csv";
		File f = new File(path);
		fis = new FileInputStream(f);
		return Action.DOWNLOAD;
	}

	public List<String> getStrs(Date date, int num) throws ParseException {
		UserManager userManager = ContextHolder.getUserManager();
		List<String> strs = null;
		Date dateNow = new Date();
		strs = new ArrayList<String>();
		DateFormat df = new SimpleDateFormat("yyyy_MM_dd");
		String dateNowStr = df.format(dateNow);
		strs.add(df.format(date).substring(0, num));
		if (dateNowStr.subSequence(0, num).equals(
				df.format(date).substring(0, num))) {
			List<User> total_register = userManager.getUsers();
			strs.add(Integer.toString(total_register.size()));
			List<UserProfile> total_basic = userManager
					.getUserProfileByStatusAndItemName("normal", "basic");
			List<UserProfile> total_expert = userManager
					.getUserProfileByStatusAndItemName("normal", "expert");
			List<UserProfile> total_pro = userManager
					.getUserProfileByStatusAndItemName("normal", "pro");

			long total_subscriber = total_basic.size() + total_expert.size()
					+ total_pro.size();
			strs.add(Long.toString(total_subscriber));

			strs.add(Integer.toString(total_basic.size()));
			strs.add(Integer.toString(total_expert.size()));
			strs.add(Integer.toString(total_pro.size()));
		} else {
			strs.add("----");
			strs.add("----");
			strs.add("----");
			strs.add("----");
			strs.add("----");
		}

		List<User> new_register = userManager.getUsersByCreatedDate(df.format(
				date).substring(0, num));
		strs.add(Long.toString(new_register.size()));

		List<UserTransaction> new_basic = userManager
				.getUserTransactionByTimeAndTxnType(df.format(date).substring(
						0, num), "subscr_signup", "basic");
		List<UserTransaction> new_expert = userManager
				.getUserTransactionByTimeAndTxnType(df.format(date).substring(
						0, num), "subscr_signup", "expert");
		List<UserTransaction> new_pro = userManager
				.getUserTransactionByTimeAndTxnType(df.format(date).substring(
						0, num), "subscr_signup", "pro");

		long new_subscriber = new_basic.size() + new_expert.size()
				+ new_pro.size();
		strs.add(Long.toString(new_subscriber));

		List<UserTransaction> cancel_basic = userManager
				.getUserTransactionByTimeAndTxnType(df.format(date).substring(
						0, num), "subscr_cancel", "basic");
		List<UserTransaction> cancel_expert = userManager
				.getUserTransactionByTimeAndTxnType(df.format(date).substring(
						0, num), "subscr_cancel", "expert");
		List<UserTransaction> cancel_pro = userManager
				.getUserTransactionByTimeAndTxnType(df.format(date).substring(
						0, num), "subscr_cancel", "pro");

		long new_cancellation = cancel_basic.size() + cancel_expert.size()
				+ cancel_pro.size();
		strs.add(Long.toString(new_cancellation));

		strs.add(Integer.toString(new_basic.size()));
		strs.add(Integer.toString(cancel_basic.size()));
		strs.add(Integer.toString(new_expert.size()));
		strs.add(Integer.toString(cancel_expert.size()));
		strs.add(Integer.toString(new_pro.size()));
		strs.add(Integer.toString(cancel_pro.size()));

		List<UserTransaction> list_new_payment = userManager
				.getUserTransactionByPaymentDateAndTxnType(df.format(date)
						.substring(0, num), "subscr_payment");

		if (list_new_payment != null && list_new_payment.size() != 0) {
			double new_payment = 0;
			for (int i = 0; i < list_new_payment.size(); i++) {
				new_payment = new_payment
						+ list_new_payment.get(i).getPaymentGross();
			}
			strs.add(Double.toString(new_payment));
		} else {
			strs.add("0");
		}
		return strs;
	}
}
