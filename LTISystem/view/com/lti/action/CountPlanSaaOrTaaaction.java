/**
 * 
 */
package com.lti.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.supercsv.io.CsvListReader;
import org.supercsv.io.CsvListWriter;
import org.supercsv.prefs.CsvPreference;

import com.lti.service.PortfolioManager;
import com.lti.service.ProfileManager;
import com.lti.service.StrategyManager;
import com.lti.service.UserManager;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.Profile;
import com.lti.service.bo.Strategy;
import com.lti.service.bo.UserResource;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.util.LTIDate;
import com.lti.util.LTIDownLoader;
import com.lti.util.StringUtil;

/**
 * @author
 * 
 */
public class CountPlanSaaOrTaaaction {
	private File uploadFile;
	private String uploadFileFileName;
	private String message;
	private InputStream fis;
	private String planName;
	private String ids;

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
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

	public File getUploadFile() {
		return uploadFile;
	}

	public void setUploadFile(File uploadFile) {
		this.uploadFile = uploadFile;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getUploadFileFileName() {
		return uploadFileFileName;
	}

	public void setUploadFileFileName(String uploadFileFileName) {
		this.uploadFileFileName = uploadFileFileName;
	}

	public String uploadFile() throws IOException {
		// String systemPath;
		// String sysPath = System.getenv("windir");
		// String str = System.getProperty("os.name").toUpperCase();
		// if (str.indexOf("WINDOWS") == -1)
		// systemPath = "/var/tmp/";
		// else
		// systemPath = sysPath + "\\temp\\";
		// System.out.println(uploadFileFileName);
		if (uploadFile != null) {
			if (uploadFileFileName.equals("plans SAA AND TAA.csv")) {
				String filePath = new LTIDownLoader().systemPath;
//				System.out.println(filePath);
				InputStream stream = new FileInputStream(uploadFile);
				OutputStream bos = new FileOutputStream(filePath
						+ uploadFileFileName);
				int bytesRead = 0;
				byte[] buffer = new byte[8192];
				while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
					bos.write(buffer, 0, bytesRead);
				}
				bos.close();
				stream.close();
				message = "uploadFile success";
			} else {
				message = "the file name must be plans SAA AND TAA.csv";
			}
		}
		return Action.MESSAGE;
	}

	public String downloadFile() throws IOException {
		String filePath = new LTIDownLoader().systemPath;
		planName = "plans SAA AND TAA.csv";
		String path = filePath + "plans SAA AND TAA.csv";
		File file = new File(path);
		fis = new FileInputStream(file);
		return Action.DOWNLOAD;
	}

	
	public String getPlanSaaAndTaa() throws IOException {
		StrategyManager strategyManager = ContextHolder.getStrategyManager();
		CsvListWriter clw = null;
		// 设置CSV下载存放的文件路径
		String systemPath;
		String sysPath = System.getenv("windir");
		String str = System.getProperty("os.name").toUpperCase();
		if (str.indexOf("WINDOWS") == -1)
			systemPath = "/var/tmp/";
		else
			systemPath = sysPath + "\\temp\\";
		String dateStr = LTIDate.parseDateToString(new Date());
		String logSystemPath = systemPath + "plans SAA AND TAA" + "_" + dateStr
				+ ".csv";

		File file = new File(logSystemPath);
		try {
			clw = new CsvListWriter(new FileWriter(file, false),
					CsvPreference.EXCEL_PREFERENCE);
			
			String[] header = { "planID", "planName", "userID", "userName",
					"SAAFollow", "TAAFollow", "SAACustomized", "TAACustomized",
					"SAAFollowUserSize", "TAAFollowUserSize",
					"SAACustomizedUserSize", "TAACustomizedUserSize" };
			clw.writeHeader(header);
		} catch (IOException e) {
			System.out.println(StringUtil.getStackTraceString(e));
		}
		Date beginDate = new Date();
		List<String> strs = null;
		// 当IDs为空时，读取csv，查找默认的planID
		if (ids == null || ids.length() == 0) {
			String filePath = new LTIDownLoader().systemPath;
			String path = filePath + "plans SAA AND TAA.csv";
			File fileRead = new File(path);
			CsvListReader readerCsv = new CsvListReader(
					new FileReader(fileRead), CsvPreference.EXCEL_PREFERENCE);
			List<String> list;
			while ((list = readerCsv.read()) != null) {
				for (int k = 0; k < list.size(); k++) {
					// System.out.println(list.get(k));
					// System.out.println(strategyManager.get(Long.parseLong(list.get(k)))
					// .getName());
					strs = new ArrayList<String>();
					strs.add(list.get(k));
					strs.add(strategyManager.get(Long.parseLong(list.get(k)))
							.getName());
					strs.add(strategyManager.get(Long.parseLong(list.get(k)))
							.getUserID().toString());
					strs.add(strategyManager.get(Long.parseLong(list.get(k)))
							.getUserName());
					List<Long> listSaaFollow = new ArrayList<Long>();
					listSaaFollow = countOnePlanSaaAndTaa(
							Configuration.STRATEGY_SAA_ID,
							Configuration.USER_RESOURCE_PORTFOLIO_FOLLOW,
							Long.parseLong(list.get(k)));
					strs.add(Long.toString(listSaaFollow.get(0)));
					List<Long> listTaaFollow = new ArrayList<Long>();
					listTaaFollow = countOnePlanSaaAndTaa(
							Configuration.STRATEGY_TAA_ID,
							Configuration.USER_RESOURCE_PORTFOLIO_FOLLOW,
							Long.parseLong(list.get(k)));
					strs.add(Long.toString(listTaaFollow.get(0)));
					List<Long> listSaaCustomize = new ArrayList<Long>();
					listSaaCustomize = countOnePlanSaaAndTaa(
							Configuration.STRATEGY_SAA_ID,
							Configuration.USER_RESOURCE_PORTFOLIO_CUSTOMIZE,
							Long.parseLong(list.get(k)));
					strs.add(Long.toString(listSaaCustomize.get(0)));
					List<Long> listTaaCustomize = new ArrayList<Long>();
					listTaaCustomize = countOnePlanSaaAndTaa(
							Configuration.STRATEGY_TAA_ID,
							Configuration.USER_RESOURCE_PORTFOLIO_CUSTOMIZE,
							Long.parseLong(list.get(k)));
					strs.add(Long.toString(listTaaCustomize.get(0)));
					strs.add(Long.toString(listSaaFollow.get(1)));
					strs.add(Long.toString(listTaaFollow.get(1)));
					strs.add(Long.toString(listSaaCustomize.get(1)));
					strs.add(Long.toString(listTaaCustomize.get(1)));
					clw.write(strs);
				}
			}
		} else if (ids.equals("-1")) {
			// 当IDs为-1时，查找出所有的plan下的SAA和TAA的统计
			List<Strategy> strategyList = new ArrayList<Strategy>();
			strategyList = strategyManager.getStrategies();
//			System.out.println(strategyList.size());
			for (int i = 0; i < strategyList.size(); i++) {
				if (strategyList.get(i).getID() == 0) {
					continue;
				} else {
					strs = new ArrayList<String>();
					strs.add(Long.toString(strategyList.get(i).getID()));
					strs.add(strategyList.get(i).getName());
					strs.add(strategyList.get(i).getUserID().toString());
					strs.add(strategyList.get(i).getUserName());
					List<Long> listSaaFollow = new ArrayList<Long>();
					listSaaFollow = countOnePlanSaaAndTaa(
							Configuration.STRATEGY_SAA_ID,
							Configuration.USER_RESOURCE_PORTFOLIO_FOLLOW,
							strategyList.get(i).getID());
					strs.add(Long.toString(listSaaFollow.get(0)));
					List<Long> listTaaFollow = new ArrayList<Long>();
					listTaaFollow = countOnePlanSaaAndTaa(
							Configuration.STRATEGY_TAA_ID,
							Configuration.USER_RESOURCE_PORTFOLIO_FOLLOW,
							strategyList.get(i).getID());
					strs.add(Long.toString(listTaaFollow.get(0)));
					List<Long> listSaaCustomize = new ArrayList<Long>();
					listSaaCustomize = countOnePlanSaaAndTaa(
							Configuration.STRATEGY_SAA_ID,
							Configuration.USER_RESOURCE_PORTFOLIO_CUSTOMIZE,
							strategyList.get(i).getID());
					strs.add(Long.toString(listSaaCustomize.get(0)));
					List<Long> listTaaCustomize = new ArrayList<Long>();
					listTaaCustomize = countOnePlanSaaAndTaa(
							Configuration.STRATEGY_TAA_ID,
							Configuration.USER_RESOURCE_PORTFOLIO_CUSTOMIZE,
							strategyList.get(i).getID());
					strs.add(Long.toString(listTaaCustomize.get(0)));
					strs.add(Long.toString(listSaaFollow.get(1)));
					strs.add(Long.toString(listTaaFollow.get(1)));
					strs.add(Long.toString(listSaaCustomize.get(1)));
					strs.add(Long.toString(listTaaCustomize.get(1)));
					clw.write(strs);
				}
			}
		} else {
			// 根据IDs下载planID的SAA和TAA
			String[] idArray = ids.split(",");
			for (int i = 0; i < idArray.length; i++) {
				strs = new ArrayList<String>();
				// System.out.println(idArray[i]);
				if (strategyManager.get(Long.parseLong(idArray[i])) != null) {
					strs.add(idArray[i]);
					strs.add(strategyManager.get(Long.parseLong(idArray[i]))
							.getName());
					strs.add(strategyManager.get(Long.parseLong(idArray[i]))
							.getUserID().toString());
					strs.add(strategyManager.get(Long.parseLong(idArray[i]))
							.getUserName());
					List<Long> listSaaFollow = new ArrayList<Long>();
					listSaaFollow = countOnePlanSaaAndTaa(
							Configuration.STRATEGY_SAA_ID,
							Configuration.USER_RESOURCE_PORTFOLIO_FOLLOW,
							Long.parseLong(idArray[i]));
					strs.add(Long.toString(listSaaFollow.get(0)));
					List<Long> listTaaFollow = new ArrayList<Long>();
					listTaaFollow = countOnePlanSaaAndTaa(
							Configuration.STRATEGY_TAA_ID,
							Configuration.USER_RESOURCE_PORTFOLIO_FOLLOW,
							Long.parseLong(idArray[i]));
					strs.add(Long.toString(listTaaFollow.get(0)));
					List<Long> listSaaCustomize = new ArrayList<Long>();
					listSaaCustomize = countOnePlanSaaAndTaa(
							Configuration.STRATEGY_SAA_ID,
							Configuration.USER_RESOURCE_PORTFOLIO_CUSTOMIZE,
							Long.parseLong(idArray[i]));
					strs.add(Long.toString(listSaaCustomize.get(0)));
					List<Long> listTaaCustomize = new ArrayList<Long>();
					listTaaCustomize = countOnePlanSaaAndTaa(
							Configuration.STRATEGY_TAA_ID,
							Configuration.USER_RESOURCE_PORTFOLIO_CUSTOMIZE,
							Long.parseLong(idArray[i]));
					strs.add(Long.toString(listTaaCustomize.get(0)));
					strs.add(Long.toString(listSaaFollow.get(1)));
					strs.add(Long.toString(listTaaFollow.get(1)));
					strs.add(Long.toString(listSaaCustomize.get(1)));
					strs.add(Long.toString(listTaaCustomize.get(1)));
					clw.write(strs);
				} else {
					strs = new ArrayList<String>();
					strs.add("can not find the planID:" + idArray[i]);
					clw.write(strs);
				}
			}
		}
		planName = "plans SAA AND TAA" + "_" + dateStr + ".csv";
		strs = new ArrayList<String>();
		Date endDate = new Date();
		strs.add("downLoad finish!BeginDate is "+beginDate.toString()+" EndDate is "+endDate.toString());
		clw.write(strs);
		clw.close();
		File f = new File(logSystemPath);
		fis = new FileInputStream(f);
		return Action.DOWNLOAD;
	}

	public List<Long> countOnePlanSaaAndTaa(long flag, int resourceFlag,
			long planID) {
		StrategyManager strategyManager = ContextHolder.getStrategyManager();
		List<UserResource> listur = new ArrayList<UserResource>();
		UserManager userManager = ContextHolder.getUserManager();
		List<Long> listLong = new ArrayList<Long>();
		// 查找该plan下的所有portfolios
		List<Portfolio> portfolioList = strategyManager
				.getModelPortfolios(planID);
		ProfileManager profileManager = (ProfileManager) ContextHolder
				.getInstance().getApplicationContext()
				.getBean("profileManager");
		List<Profile> userProfileList = profileManager
				.getProfilesByPlanID(planID);
		PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();
		if (userProfileList != null && userProfileList.size() > 0) {
			for (Profile p : userProfileList) {
				Portfolio pp = portfolioManager.get(p.getPortfolioID());
				portfolioList.add(pp);
			}
		}
		// 根据flag判断为SAA或TAA,并加入portfolioOnFlag
		List<Portfolio> portfolioOnFlag = new ArrayList<Portfolio>();
		if (portfolioList != null && portfolioList.size() > 0) {
			for (Portfolio p : portfolioList) {
				Long strategyID = p.getStrategies()
						.getAssetAllocationStrategy().getID();
				if (strategyID.equals(flag)) {
					portfolioOnFlag.add(p);
				}
			}
		}
		long resultCount = 0;
		Set<Long> userSet = new HashSet<Long>();
		if (portfolioOnFlag != null && portfolioOnFlag.size() > 0) {
			for (int i = 0; i < portfolioOnFlag.size(); i++) {
				if (resourceFlag == Configuration.USER_RESOURCE_PORTFOLIO_FOLLOW) {
					listur = userManager
							.getUserResourceByResourceIDAndResourceType(
									portfolioOnFlag.get(i).getID(),
									Configuration.USER_RESOURCE_PORTFOLIO_CUSTOMIZE);
					if (listur.size() == 0 || listur == null) {
						listur = userManager
								.getUserResourceByResourceIDAndResourceType(
										portfolioOnFlag.get(i).getID(),
										Configuration.USER_RESOURCE_PORTFOLIO_FOLLOW);
						if (listur != null && listur.size() > 0) {
							resultCount = resultCount + listur.size();
							for (int m = 0; m < listur.size(); m++) {
								userSet.add(listur.get(m).getUserID());
//								System.out.println(userSet.size());
							}
						}
					}
				} else {
					listur = userManager
							.getUserResourceByResourceIDAndResourceType(
									portfolioOnFlag.get(i).getID(),
									Configuration.USER_RESOURCE_PORTFOLIO_CUSTOMIZE);
					if (listur != null && listur.size() > 0) {
						resultCount = resultCount + listur.size();
						for (int n = 0; n < listur.size(); n++) {
							userSet.add(listur.get(n).getUserID());
						}
					}
				}
			}
		}
		listLong.add(resultCount);
		if(userSet!=null && userSet.size()>0){
			listLong.add((long) userSet.size());
		}else{
			listLong.add((long)0);
		}
		return listLong;
	}
}
