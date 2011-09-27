package com.lti.action;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.struts2.ServletActionContext;
import org.supercsv.io.CsvListReader;
import org.supercsv.io.CsvListWriter;
import org.supercsv.prefs.CsvPreference;

import com.lti.bean.ModelPortfoliosBean;
import com.lti.jobscheduler.DailyUpdateControler;
import com.lti.permission.MPIQChecker;
import com.lti.permission.MYPLANIQPermissionManager;
import com.lti.permission.PortfolioPermissionChecker;
import com.lti.permission.PublicMaker;
import com.lti.permission.SubscrPlanChecker;
import com.lti.rmi.PostBean;
import com.lti.service.AssetClassManager;
import com.lti.service.DataManager;
import com.lti.service.GroupManager;
import com.lti.service.JforumManager;
import com.lti.service.PortfolioManager;
import com.lti.service.ProfileManager;
import com.lti.service.SecurityManager;
import com.lti.service.StrategyManager;
import com.lti.service.UserManager;
import com.lti.service.bo.AssetClass;
import com.lti.service.bo.CachePortfolioItem;
import com.lti.service.bo.CompanyFund;
import com.lti.service.bo.Data5500;
import com.lti.service.bo.HoldingItem;
import com.lti.service.bo.JforumPostText;
import com.lti.service.bo.JforumTopics;
import com.lti.service.bo.PlanArticle;
import com.lti.service.bo.PlanScore;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.PortfolioDailyData;
import com.lti.service.bo.PortfolioInf;
import com.lti.service.bo.PortfolioMPT;
import com.lti.service.bo.Profile;
import com.lti.service.bo.Security;
import com.lti.service.bo.SecurityDailyData;
import com.lti.service.bo.Strategy;
import com.lti.service.bo.Transaction;
import com.lti.service.bo.User;
import com.lti.service.bo.UserPermission;
import com.lti.service.bo.UserResource;
import com.lti.service.bo.VAFund;
import com.lti.service.bo.VariableFor401k;
import com.lti.service.impl.PortfolioManagerImpl;
import com.lti.service.impl.SecurityManagerImpl;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.type.Pair;
import com.lti.type.SecurityState;
import com.lti.type.executor.StrategyBasicInf;
import com.lti.type.executor.StrategyInf;
import com.lti.type.finance.Asset;
import com.lti.type.finance.HoldingInf;
import com.lti.util.BaseFormulaUtil;
import com.lti.util.CSVReader;
import com.lti.util.Edgar11K;
import com.lti.util.FileOperator;
import com.lti.util.FundTableCachingUtil;
import com.lti.util.LTIDate;
import com.lti.util.LTIDownLoader;
import com.lti.util.Parse401KParameters;
import com.lti.util.PersistentUtil;
import com.lti.util.PlanRankingUtil;
import com.lti.util.StringUtil;
import com.lti.util.VAFundUtil;
import com.opensymphony.xwork2.ActionSupport;

/**
 * f401k_xxxx.action
 * 
 * 构造函数初始化isAdmin和userID的变量
 * 
 * @category 页面
 * 
 */
public class f401kaction extends ActionSupport implements Action
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static f401kaction curThread = null;
	public static Boolean isUpdating = false;
	/**
	 * 记录当前用户的ID
	 */
	private long userID = -1l;

	/**
	 * 判断当前用户是否为所读记录的所有者
	 */
	private boolean isOwner = false;
	/**
	 * 判断当前用户是否为超级用户
	 */
	private boolean isAdmin = false;
	/**
	 * 输出到message页面的内容
	 */
	private String message = "";
	/**
	 * 当前访问的action的名字
	 */
	@Deprecated
	private String action_name;
	/**
	 * 跳转时所带的参数
	 */
	@Deprecated
	private String params;

	/**
	 * 初始化userID和isAdmin
	 */
	public f401kaction()
	{
		userID = ContextHolder.getUserManager().getLoginUser().getID();
		if (userID == Configuration.SUPER_USER_ID)
		{
			isAdmin = true;
		}
	}

	/**
	 * 与主页面相关的论坛主题
	 */
	private List<JforumTopics> topics_homepage;

	/**
	 * 401k主页面
	 * 
	 * @param topics_homepage
	 */
	public String main()
	{
		JforumManager jforumManager = ContextHolder.getJforumManager();
		int forumId1 = jforumManager.getForumId(Configuration.JFORUM_Announcements);
		int forumId2 = jforumManager.getForumId(Configuration.JFORUM_FORUMS);
		topics_homepage = jforumManager.getTopicsByForumIDDesc(forumId1, forumId2);
		return Action.SUCCESS;
	}

	/**
	 * 待显示的plan
	 */
	private List<Strategy> plans;
	/**
	 * Center plan
	 */
	private Strategy centerplan;
	/**
	 * 待显示的strategies
	 */
	private List<Strategy> strategies;

	/**
	 * Plan的fund table
	 */
	private List<VariableFor401k> variables;

	/**
	 * plan管理页面action
	 * 
	 * @param centerplan
	 * @param strategies
	 * @param plans
	 *            待显示的所有plans，为了快速读取,不读取所有的plans
	 * @param variables
	 */

	private String radio;
	private String radioArea;

	public String getRadio()
	{
		return radio;
	}

	public void setRadio(String radio)
	{
		this.radio = radio;
	}

	public String getRadioArea()
	{
		return radioArea;
	}

	public void setRadioArea(String radioArea)
	{
		this.radioArea = radioArea;
	}

	public String admin()
	{
		StrategyManager strategyManager = ContextHolder.getStrategyManager();
		centerplan = strategyManager.get(Configuration.STRATEGY_401K);
		strategies = strategyManager.getStrategiesByMainStrategyID(centerplan.getID());
		plans = strategyManager.getBasicStrategiesByType(Configuration.STRATEGY_TYPE_401K);
		variables = centerplan.getVariablesFor401k();
		return Action.SUCCESS;
	}

	/**
	 * 处理空的variable
	 * 
	 * @param variables
	 * @param isCenter
	 */
	private void processVariables()
	{
		if (variables != null)
		{
			Iterator<VariableFor401k> vIter = variables.iterator();
			while (vIter.hasNext())
			{
				VariableFor401k v = vIter.next();
				if (isCenter)
				{
					if (v.getPortfolioName() == null || v.getPortfolioName().equals(""))
					{
						vIter.remove();
						continue;
					}
					if (v.getPortfolioID() == null)
					{
						vIter.remove();
						continue;
					}
				} else
				{
					if (v.getAssetClassName() == null || v.getAssetClassName().equals(""))
					{
						vIter.remove();
						continue;
					}
					if (v.getName() == null || v.getName().equals(""))
					{
						vIter.remove();
						continue;
					}
					if (v.getSymbol() == null || v.getSymbol().equals(""))
					{
						vIter.remove();
						continue;
					}
					if (v.getRedemption() == null)
					{
						vIter.remove();
						continue;
					}
				}
			}
		}
	}

	/**
	 * 判断是一般的plan或中央plan
	 */
	private boolean isCenter = false;

	/**
	 * 更新plan的varialbes
	 * 
	 * @param ID
	 * @param message
	 * @param isAdmin
	 * @param userID
	 * 
	 */
	public String updatevariables()
	{
		try
		{
			StrategyManager strategyManager = ContextHolder.getStrategyManager();
			Strategy strategy = strategyManager.get(ID);
			if (strategy == null)
			{
				message = "The strategy does not exist.";
				return Action.MESSAGE;
			}
			if (!isAdmin && userID != strategy.getUserID())
			{
				message = "You don't have the right to change it.";
				return Action.MESSAGE;
			}

			if (strategy.getName().equals(Configuration.STRATEGY_401K))
				isCenter = true;

			processVariables();
			strategy.setVariablesFor401k(variables);
			strategyManager.update(strategy);

			Strategy stra = strategyManager.get(ID);
			List<String> tickerList = new ArrayList<String>();
			tickerList = ContextHolder.getStrategyManager().getPlanTickers(ID);
			List<String> assetsList = new ArrayList<String>();
			assetsList = ContextHolder.getStrategyManager().getPlanMinorAssets(tickerList);
			List<String> majorList = new ArrayList<String>();
			majorList = ContextHolder.getStrategyManager().getPlanMajorAssets(assetsList, false);
			String similarIssues = "";
			similarIssues = similarIssues + "The plan consists of " + tickerList.size() + " funds. ";
			similarIssues = similarIssues + "It covers " + majorList.size() + " major asset classes and " + assetsList.size() + " minor asset classes. ";
			similarIssues = similarIssues + "The major asset classes it covers are";
			for (int i = 0; i < majorList.size(); i++)
			{
				similarIssues = similarIssues + " " + majorList.get(i);
				if (i < majorList.size() - 2)
				{
					similarIssues += ",";
				}
				if (i == majorList.size() - 2)
				{
					similarIssues += " and";
				}
			}
			similarIssues += ".";
			stra.setSimilarIssues(similarIssues);
			strategyManager.update(stra);

			fundBackup();

			message = "ok.";

		} catch (Exception e)
		{
			message = "Fail to update variables.-1\r\n" + StringUtil.getStackTraceString(e);
		}
		return Action.MESSAGE;
	}

	private String userContent;

	public String getUserContent()
	{
		return userContent;
	}

	public void setUserContent(String userContent)
	{
		this.userContent = userContent;
	}

	public String updateusercontent()
	{
		try
		{
			StrategyManager strategyManager = ContextHolder.getStrategyManager();
			Strategy strategy = strategyManager.get(ID);
			if (strategy == null)
			{
				message = "The strategy does not exist.";
				ServletActionContext.getResponse().setStatus(500);
				return Action.MESSAGE;
			}
			if (!isAdmin && userID != strategy.getUserID())
			{
				message = "You don't have the right to change it.";
				ServletActionContext.getResponse().setStatus(500);
				return Action.MESSAGE;
			}

			strategy.setUserContent(userContent);
			strategyManager.update(strategy);

			message = "ok.";

		} catch (Exception e)
		{
			message = "Fail to update variables.-1\r\n" + StringUtil.getStackTraceString(e);
			ServletActionContext.getResponse().setStatus(500);
			e.printStackTrace();
		}
		return Action.MESSAGE;
	}

	/**
	 * 待查看的plan
	 */
	private Strategy plan;
	/**
	 * 待查看的plan的model portfolios
	 */
	private List<ModelPortfoliosBean> modelPortfoliosBeans;
	/**
	 * plan 的ID和Name
	 */
	Map<String, String> planIDAndName = new HashMap<String, String>();
	/**
	 * plan 对应的forum的topic
	 */
	private long topicID;
	/**
	 * plan 对应的forum
	 */
	private long forumID;
	/**
	 * 所有回复
	 */
	List<PostBean> postList;
	/**
	 * plan 的分数
	 */
	private PlanScore planScore;

	/**
	 * 是否有fundtable
	 */
	private Boolean haveFundtable = true;

	public Boolean getHaveFundtable()
	{
		return haveFundtable;
	}

	public void setHaveFundtable(Boolean haveFundtable)
	{
		this.haveFundtable = haveFundtable;
	}

	private boolean rflag;
	private List<PlanArticle> showArticles;
	private Data5500 showData5500;
	private Map<String,List<VariableFor401k>> fundtableMap;

	public Map<String, List<VariableFor401k>> getFundtableMap() {
		return fundtableMap;
	}

	public void setFundtableMap(Map<String, List<VariableFor401k>> fundtableMap) {
		this.fundtableMap = fundtableMap;
	}

	public Data5500 getShowData5500()
	{
		return showData5500;
	}

	public void setShowData5500(Data5500 showData5500)
	{
		this.showData5500 = showData5500;
	}

	/**
	 * 查看plan的页面
	 * 
	 * @param ID
	 * @param plan
	 * @param planScore
	 * @param message
	 * @param isAdmin
	 * @param isOwner
	 * @param variables
	 * @param userID
	 * @param planIDAndName
	 * @param centerplan
	 * @param strategies
	 * @param modelPortfoliosBeans
	 * 
	 */
	public String view()
	{
		MPIQChecker fc = new MPIQChecker(ContextHolder.getUserManager().getLoginUser().getID());
		MYPLANIQPermissionManager mpm = new MYPLANIQPermissionManager();
		boolean hasSubscred = fc.hasSubscred();
		StrategyManager strategyManager = ContextHolder.getStrategyManager();
		UserManager userManager = ContextHolder.getUserManager();
		plan = strategyManager.get(ID);
		shortDescription = filterHtml(plan.getDescription());
		if (shortDescription.length() > 180)
		{
			Character s;
			int count = 0;
			for (int i = 0; i < shortDescription.length(); i++)
			{
				s = shortDescription.charAt(i);
				if (s.toString().equals(" "))
				{
					count++;
					if (count == 30)
					{
						shortDescription = shortDescription.substring(0, i);
						break;
					} else
						continue;
				}
				if (i == shortDescription.length() + 1)
				{
					shortDescription = shortDescription.substring(0, shortDescription.length());
				}
			}

		} else
			shortDescription = shortDescription.substring(0, shortDescription.length());
		planScore = strategyManager.getPlanScoreByPlanID(ID);
		ticker = plan.getTicker();
		List<PlanArticle> articleList = strategyManager.getPlanArticles(ID);
		showArticles = new ArrayList<PlanArticle>();
		if (articleList != null)
		{
			for (PlanArticle p : articleList)
			{
				if (p.getDisplay() == true)
				{
					showArticles.add(p);
				}
			}
		}
		if (plan == null || !plan.is401K())
		{
			message = "The plan does not exist.";
			return Action.MESSAGE;
		}
		if (isAdmin || (plan.getUserID() != null && plan.getUserID().equals(userID)))
		{
			isOwner = true;
		} else
		{
			isOwner = false;
		}
		boolean read = true;
		if (!isOwner)
		{
			read = false;
			read = ContextHolder.getUserManager().HaveRole(Configuration.ROLE_STRATEGY_READ, userID, ID, Configuration.RESOURCE_TYPE_STRATEGY);
		}

		if (!read)
		{
			message = "You are trying to access a private plan. Please retrun to the previous page or the home page. Further questions, please contact support@myplaniq.com . Thank you for your suggestions.";
			return Action.MESSAGE;
		}
		variables = plan.getVariablesFor401k();
		rflag = true;
		boolean hasView = mpm.hasUserPlanFundTable(userID, ID);
		if (userID != Configuration.SUPER_USER_ID && !isOwner && !hasView)
		{
			rflag = false;
			List<Long> planIDs = userManager.getResourceIDListByUserIDAndResourceType(userID, Configuration.USER_RESOURCE_PLAN_FUNDTABLE);
			for (int i = 0; i < planIDs.size(); i++)
			{
				Long planID = planIDs.get(i);
				Strategy pa = strategyManager.get(planID);
				if (pa != null)
				{
					String planName = strategyManager.get(planID).getName();
					planIDAndName.put(planID.toString(), planName);
				}
			}
		}
		this.setPlanIDAndName(planIDAndName);

		centerplan = strategyManager.get(Configuration.STRATEGY_401K);
		strategies = strategyManager.getStrategiesByMainStrategyID(centerplan.getID());
		modelPortfoliosBeans = new ArrayList<ModelPortfoliosBean>(strategies.size());
		List<Portfolio> portfolios = strategyManager.getModelPortfolios(ID);

		for (int i = 0; i < strategies.size(); i++)
		{
			Strategy s = strategies.get(i);
			ModelPortfoliosBean scb = new ModelPortfoliosBean();
			scb.setStrategyID(s.getID());
			scb.setStrategyName(s.getName());
			modelPortfoliosBeans.add(scb);
			if (portfolios == null || portfolios.size() == 0)
				continue;
			List<CachePortfolioItem> items = new ArrayList<CachePortfolioItem>();
			for (int j = 0; j < portfolios.size(); j++)
			{
				Portfolio p = portfolios.get(j);
				if (p.getStrategies() != null && p.getStrategies().getAssetAllocationStrategy().getID() != null && p.getStrategies().getAssetAllocationStrategy().getID().longValue() == s.getID().longValue())
				{
					CachePortfolioItem cpi = null;
					List<CachePortfolioItem> pitems = null;
					if (hasSubscred)
					{
						pitems = strategyManager.findByHQL("from CachePortfolioItem cp where cp.PortfolioID=" + p.getID() + " and cp.GroupID=0 and cp.RoleID=" + Configuration.ROLE_PORTFOLIO_REALTIME_ID);
					} else
					{
						pitems = strategyManager.findByHQL("from CachePortfolioItem cp where cp.PortfolioID=" + p.getID() + " and cp.GroupID=" + Configuration.GROUP_MPIQ_ID + " and cp.RoleID=" + Configuration.ROLE_PORTFOLIO_DELAYED_ID);
					}
					// strategyManager.findByHQL("from CachePortfolioItem cp
					// where cp.PortfolioID="
					// + p.getID() + " and cp.GroupID=0 and cp.RoleID=" +
					// Configuration.ROLE_PORTFOLIO_REALTIME_ID);
					if (pitems != null && pitems.size() > 0)
					{
						cpi = pitems.get(0);
					} else
					{
						cpi = new CachePortfolioItem();
						cpi.setPortfolioID(p.getID());
						cpi.setPortfolioName(p.getName());
					}
					items.add(cpi);

				}
			}
			if (items.size() > 0)
				scb.setModelPortfolios(items);

		}

		try
		{
			double score = strategyManager.getPlanScoreByPlanID(ID).getInvestmentScore() * 100;

			int count = 0;
			if (score == 0)
				count = 0;
			else if (score < 10)
				count = 1;
			else if (score < 35)
				count = 2;
			else if (score < 65)
				count = 3;
			else if (score < 85)
				count = 4;
			else
				count = 5;
			ranksize = count;
		} catch (Exception e)
		{
			ranksize = 0;
		}

		if (strategyManager.get(ID).getStatus() == 1 || strategyManager.get(ID).getStatus() == 2)
		{
			haveFundtable = false;
		}
		showData5500 = strategyManager.getData5500ByPlanID(ID);
		if (showData5500 != null)
		{
			String spons_Num = showData5500.getSPONS_DFE_PHONE_NUM();
			if (spons_Num != null && !spons_Num.equals(""))
			{
				spons_Num = "(" + spons_Num.substring(0, 3) + ") " + spons_Num.substring(3, 6) + "-" + spons_Num.substring(6);
				showData5500.setSPONS_DFE_PHONE_NUM(spons_Num);
			}

			String spons_Ein = showData5500.getSPONS_DFE_EIN();
			if (spons_Ein != null && !spons_Ein.equals(""))
			{
				spons_Ein = spons_Ein.substring(0, 2) + "-" + spons_Ein.substring(2);
				showData5500.setSPONS_DFE_EIN(spons_Ein);
			}

			String admin_Num = showData5500.getADMIN_PHONE_NUM();
			if (admin_Num != null && !admin_Num.equals(""))
			{
				admin_Num = "(" + admin_Num.substring(0, 3) + ") " + admin_Num.substring(3, 6) + "-" + admin_Num.substring(6);
				showData5500.setADMIN_PHONE_NUM(admin_Num);
			}

			String admin_Ein = showData5500.getADMIN_EIN();
			if (admin_Ein != null && !admin_Ein.equals(""))
			{
				admin_Ein = admin_Ein.substring(0, 2) + "-" + admin_Ein.substring(2);
				showData5500.setADMIN_EIN(admin_Ein);
			}
		}
		return Action.SUCCESS;
	}

	// 过滤html <> 标签
	private final static String regxpForHtml = "<([^>]*)>"; // 过滤所有以<开头以>结尾的标签

	public String filterHtml(String str)
	{
		Pattern pattern = Pattern.compile(regxpForHtml);
		Matcher matcher = pattern.matcher(str);
		StringBuffer sb = new StringBuffer();
		boolean result1 = matcher.find();
		while (result1)
		{
			matcher.appendReplacement(sb, "");
			result1 = matcher.find();
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	private int ranksize = 0;

	public int getRanksize()
	{
		return ranksize;
	}

	public void setRanksize(int ranksize)
	{
		this.ranksize = ranksize;
	}

	/**
	 * 获取分析过的ticker?为何更新variables
	 * 
	 * @param originalString
	 * @param ID
	 * @param message
	 * @param isCenter
	 * @param action_name
	 * @param params
	 * 
	 */
	public String getparsedticker()
	{
		Parse401KParameters pp = new Parse401KParameters();
		pp.setOriginalString(originalString);
		String[] lines = pp.getOriginalString().replace("\r", "").split("\n");
		StrategyManager strategyManager = ContextHolder.getStrategyManager();
		com.lti.service.SecurityManager securityManager = ContextHolder.getSecurityManager();
		AssetClassManager assetClassManager = ContextHolder.getAssetClassManager();
		Strategy strategy = strategyManager.get(ID);
		if (strategy == null)
		{
			message = "The strategy does not exist.";
			return Action.MESSAGE;
		}
		if (!isAdmin && userID != strategy.getUserID())
		{
			message = "You are trying to access a private plan. Please retrun to the previous page or the home page. Further questions, please contact support@myplaniq.com . Thank you for your suggestions.";
			return Action.MESSAGE;
		}

		if (strategy.getName().equals(Configuration.STRATEGY_401K))
		{
			isCenter = true;

		}
		List<VariableFor401k> vs = null;

		if (vs == null)
		{
			vs = new ArrayList<VariableFor401k>();
		}
		for (int i = 0; i < lines.length; i++)
		{
			VariableFor401k v = new VariableFor401k();

			if (lines[i].equals(""))
			{
				continue;
			} else
			{
				Security s = securityManager.get(lines[i]);
				if (s != null)
					v.setName(s.getName());
				else
				{
					v.setName("unknown fund name");
					v.setMemo("n");
				}
				v.setSymbol(lines[i]);
				v.setDescription(v.getName());
				v.setRedemption(3);
				if (s != null && s.getClassID() != null)
				{
					AssetClass ac = assetClassManager.get(s.getClassID());
					if (ac != null)
						v.setAssetClassName(ac.getName());
				} else
				{
					v.setAssetClassName("unknown asset name");
					v.setMemo("n");
				}
				vs.add(v);
				continue;
			}

		}
		strategy.setVariablesFor401k(vs);
		strategyManager.update(strategy);
		message = "ok.";
		action_name = "createplan";
		params = "ID=" + ID;
		return Action.REDIRECT;
	}

	/**
	 * 为true表明追加variables
	 */
	private Boolean append = false;
	/**
	 * 上传的文件
	 */
	private File upload;
	/**
	 * 上传文件的内容
	 */
	private String uploadContentType;
	/**
	 * 上传文件的名字
	 */
	private String uploadFileName;

	/**
	 * 从文件中更新variables
	 * 
	 * @param ID
	 * @param message
	 * @param isAdmin
	 * @param userID
	 * @param isCenter
	 * @param append
	 * @param uploadFileName
	 * @param fileList
	 * 
	 */
	public String updatevariablesfromfile()
	{
		StrategyManager strategyManager = ContextHolder.getStrategyManager();
		com.lti.service.SecurityManager securityManager = ContextHolder.getSecurityManager();
		AssetClassManager assetClassManager = ContextHolder.getAssetClassManager();
		Strategy strategy = strategyManager.get(ID);
		if (strategy == null)
		{
			message = "The strategy does not exist.";
			return Action.MESSAGE;
		}
		if (!isAdmin && userID != strategy.getUserID())
		{
			message = "You are trying to access a private plan. Please retrun to the previous page or the home page. Further questions, please contact support@myplaniq.com . Thank you for your suggestions.";
			return Action.MESSAGE;
		}

		if (strategy.getName().equals(Configuration.STRATEGY_401K))
			isCenter = true;

		try
		{
			if (!uploadFileName.toLowerCase().endsWith(".csv"))
				return Action.ERROR;
			CSVReader csv = new CSVReader(new FileReader(upload));
			List<String> list = null;
			List<VariableFor401k> vs = null;
			if (append)
			{
				vs = strategy.getVariablesFor401k();
			}
			if (vs == null)
			{
				vs = new ArrayList<VariableFor401k>();
			}
			while ((list = csv.readLineAsList()) != null)
			{
				VariableFor401k v = new VariableFor401k();

				if (list.get(0).equals(""))
					continue;
				if (list.size() == 1)
				{
					Security s = securityManager.get(list.get(0));
					if (s != null)
						v.setName(s.getName());
					else
					{
						v.setName("unknown fund name");
						v.setMemo("n");
					}
					v.setSymbol(list.get(0));
					v.setDescription(list.get(0));
					v.setRedemption(3);
					if (s != null && s.getClassID() != null)
					{
						AssetClass ac = assetClassManager.get(s.getClassID());
						if (ac != null)
							v.setAssetClassName(ac.getName());
					} else
					{
						v.setAssetClassName("unknown asset name");
						v.setMemo("n");
					}
					vs.add(v);
					continue;
				}

				Security s = securityManager.get(list.get(1));
				if (s != null)
					v.setName(s.getName());
				else
				{
					v.setName("unknown fund name");
					v.setMemo("n");
				}

				int size = list.size();
				v.setAssetClassName(list.get(0));
				v.setSymbol(list.get(1));
				if (size >= 3)
				{
					try
					{
						v.setRedemption(Integer.parseInt(list.get(2)));
					} catch (Exception e)
					{
					}
				}
				if (size >= 4)
				{
					v.setDescription(list.get(3));
				}
				if (size >= 5)
				{
					v.setMemo(list.get(4));
				}
				vs.add(v);
			}
			strategy.setVariablesFor401k(vs);
			strategyManager.update(strategy);
			csv.close();

			File file = new File(Configuration.getFundTable() + "/" + strategy.getName() + "/");
			String[] fileName = file.list();
			fileList = Arrays.asList(fileName);

			message = "ok.";
		} catch (Exception e)
		{
			e.printStackTrace();
			message = "ERROR:-1;Fail to upload fund table from file.";
			return Action.MESSAGE;
		}
		action_name = "fundtable";
		params = "ID=" + ID;
		return Action.REDIRECT;

	}

	/*-------------------------------------------------------*/

	/**
	 * fund内容？
	 */
	private String fundContent;
	/**
	 * 旧文件的名字？
	 */
	private String fileNameOfOldFile;

	public String getFileNameOfOldFile()
	{
		return fileNameOfOldFile;
	}

	public void setFileNameOfOldFile(String fileNameOfOldFile)
	{
		this.fileNameOfOldFile = fileNameOfOldFile;
	}

	/**
	 * 从源文件上传？
	 * 
	 * @param upload
	 * @param ID
	 * @param fundContent
	 * @param fileNameOfOldFile
	 */
	public String uploadfromsourcefile()
	{

		try
		{
			String content = FileOperator.getContent(upload.getAbsolutePath());

			if (content == null)
			{
				content = "check it!!";
			}
			File root = new File(Configuration.get11KDir() + "PlanFile");
			if (!root.exists())
			{
				root.mkdirs();
			}
			File planFile = new File(root, ID + ".htm");

			if (!planFile.exists())
			{
				planFile.createNewFile();
			}
			FileOperator.appendMethodB(planFile.getAbsolutePath(), content, false);

			fundContent = Edgar11K.extract11KFund(upload.getAbsolutePath(), ID.toString(), fileNameOfOldFile);

			if (fundContent == null || fundContent == "")
			{
				action_name = "fundtable";
				params = "ID=" + ID;
				return Action.REDIRECT;
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		action_name = "fundtable";
		params = "ID=" + ID;
		return Action.REDIRECT;
	}

	public String uploadmatchingrulefile()
	{
		try
		{
			if (upload != null)
				FileOperator.saveToDir(upload, Configuration.MATCHING_RULE_FILENAME, Configuration.get13FDir());
			message = "upload successful";
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return Action.MESSAGE;
	}

	/*-------------------------------------------------------*/

	/**
	 * 供下载的文件流
	 */
	private InputStream fis;

	public InputStream getFis()
	{
		return fis;
	}

	public void setFis(InputStream fis)
	{
		this.fis = fis;
	}

	/**
	 * 下载plan文件
	 * 
	 * @param planName
	 * @param fis
	 */
	public String downloadPlanFile() throws IOException
	{

		File root = new File(Configuration.get11KDir() + "PlanFile");

		if (!root.exists())
		{
			root.mkdirs();
		}
		File planFile = new File(root, planName);

		if (!planFile.exists())
		{

			planFile.createNewFile();

		}
		fis = new FileInputStream(planFile);

		return Action.DOWNLOAD;
	}

	/**
	 * 将所有401k的plan设置为公开的
	 * 
	 * @param message
	 */
	public String adminbatchpublic()
	{
		StrategyManager sm = ContextHolder.getStrategyManager();
		List<Strategy> plans = sm.getStrategiesByType(Configuration.STRATEGY_TYPE_401K);
		StringBuffer sb = new StringBuffer();
		for (Strategy plan : plans)
		{
			ID = plan.getID();
			makepublic();
			sb.append(message);
			sb.append("<br>\r\n<br>\r\n<br>\r\n");
		}
		message = sb.toString();
		return Action.MESSAGE;
	}

	/**
	 * 将所有401k的plan设置为不公开的
	 * 
	 * @param message
	 */
	public String adminbatchprivate()
	{
		StrategyManager sm = ContextHolder.getStrategyManager();
		List<Strategy> plans = sm.getStrategiesByType(Configuration.STRATEGY_TYPE_401K);
		StringBuffer sb = new StringBuffer();
		for (Strategy plan : plans)
		{
			ID = plan.getID();
			makeprivate();
			sb.append(message);
			sb.append("<br>\r\n<br>\r\n<br>\r\n");
		}
		message = sb.toString();
		return Action.MESSAGE;
	}

	private boolean portfolioFlag = true;

	public boolean isPortfolioFlag()
	{
		return portfolioFlag;
	}

	public void setPortfolioFlag(boolean portfolioFlag)
	{
		this.portfolioFlag = portfolioFlag;
	}

	/**
	 * 将plan及其下的model portfolio设置为公开 需要将权限设置封装
	 * 
	 * @ID
	 */
	public String makepublic()
	{
		if (ID == null)
		{
			message = "The operation cannot be executed for the ID is null.";
			return Action.MESSAGE;
		}

		Strategy plan = ContextHolder.getStrategyManager().get(ID);
		if (plan == null)
		{
			message = "The operation cannot be executed for the plan doesn't exist.";
			return Action.MESSAGE;
		}
		if (isAdmin || userID == plan.getUserID().longValue())
		{

		} else
		{
			message = "You are trying to access a private plan. Please retrun to the previous page or the home page. Further questions, please contact support@myplaniq.com . Thank you for your suggestions.";
			return Action.MESSAGE;
		}
		StringBuffer sb = new StringBuffer();
		if (portfolioFlag)
		{
			PublicMaker planPublicMaker = new PublicMaker(plan);
			try
			{
				planPublicMaker.makePublic();
				sb.append("Add " + plan.getName() + "[" + ID + "] to group F401K.<br>\r\n");
				sb.append("Add " + plan.getName() + "[" + ID + "] to group ANONYMOUS.<br>\r\n");
				sb.append("Add " + plan.getName() + "[" + ID + "] to group F401K_UN_SUBSCR.<br>\r\n");
			} catch (Exception e)
			{
				sb.append("<br>\r\n");
				sb.append(StringUtil.getStackTraceString(e));
			}
			sb.append("<br>\r\n");
			sb.append("<br>\r\n");
		}
		PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();
		List<Portfolio> portfolios = portfolioManager.getModelPortfolios(ID);
		if (portfolios != null && portfolios.size() > 0)
		{
			for (Portfolio portfolio : portfolios)
			{
				try
				{
					PublicMaker portfolioPM = new PublicMaker(portfolio);
					portfolioPM.makePublic();
					sb.append("Add " + portfolio.getName() + "[" + portfolio.getID() + "][realtime] to group F401K.<br>\r\n");
					sb.append("Add " + portfolio.getName() + "[" + portfolio.getID() + "][delaytime] to group ANONYMOUS.<br>\r\n");
					sb.append("Add " + portfolio.getName() + "[" + portfolio.getID() + "][delaytime] to group F401K_UN_SUBSCR.<br>\r\n");
					PortfolioMPT mpt = portfolioManager.getPortfolioMPT(portfolio.getID(), PortfolioMPT.DELAY_LAST_FIVE_YEAR);
					if (mpt != null)
						portfolioManager.updatePortfolioMPT(mpt);
					mpt = portfolioManager.getPortfolioMPT(portfolio.getID(), PortfolioMPT.DELAY_LAST_THREE_YEAR);
					if (mpt != null)
						portfolioManager.updatePortfolioMPT(mpt);
					mpt = portfolioManager.getPortfolioMPT(portfolio.getID(), PortfolioMPT.DELAY_LAST_ONE_YEAR);
					if (mpt != null)
						portfolioManager.updatePortfolioMPT(mpt);

				} catch (Exception e)
				{
					sb.append("<br>\r\n");
					sb.append(StringUtil.getStackTraceString(e));
				}
			}
		}
		message = sb.toString();

		if (!isAdmin)
		{
			message = "Done!";
		}

		return Action.MESSAGE;
	}

	/**
	 * 将plan及其下的model portfolio设置为不公开 需要将权限设置封装
	 * 
	 * @ID
	 */
	public String makeprivate()
	{
		if (ID == null)
		{
			message = "The operation cannot be executed for the ID is null.";
			return Action.MESSAGE;
		}
		Strategy plan = ContextHolder.getStrategyManager().get(ID);
		if (plan == null)
		{
			message = "The operation cannot be executed for the plan doesn't exist.";
			return Action.MESSAGE;
		}

		if (isAdmin || userID == plan.getUserID().longValue())
		{

		} else
		{
			message = "You are trying to access a private plan. Please retrun to the previous page or the home page. Further questions, please contact support@myplaniq.com . Thank you for your suggestions.";
			return Action.MESSAGE;
		}

		PublicMaker planPublicMaker = new PublicMaker(plan);
		StringBuffer sb = new StringBuffer();

		try
		{
			planPublicMaker.makePrivate();
			sb.append("Remove " + plan.getName() + "[" + ID + "] to group F401K.<br>\r\n");
			sb.append("Remove " + plan.getName() + "[" + ID + "] to group ANONYMOUS.<br>\r\n");
			sb.append("Remove " + plan.getName() + "[" + ID + "] to group F401K_UN_SUBSCR.<br>\r\n");
		} catch (Exception e)
		{
			sb.append("<br>\r\n");
			sb.append(StringUtil.getStackTraceString(e));
		}
		sb.append("<br>\r\n");
		sb.append("<br>\r\n");

		List<Portfolio> portfolios = ContextHolder.getPortfolioManager().getModelPortfolios(ID);
		if (portfolios != null && portfolios.size() > 0)
		{
			for (Portfolio portfolio : portfolios)
			{
				try
				{
					PublicMaker portfolioPM = new PublicMaker(portfolio);
					portfolioPM.makePrivate();
					sb.append("Remove " + portfolio.getName() + "[" + portfolio.getID() + "][realtime] to group F401K.<br>\r\n");
					sb.append("Remove " + portfolio.getName() + "[" + portfolio.getID() + "][delaytime] to group ANONYMOUS.<br>\r\n");
					sb.append("Remove " + portfolio.getName() + "[" + portfolio.getID() + "][delaytime] to group F401K_UN_SUBSCR.<br>\r\n");
				} catch (Exception e)
				{
					sb.append("<br>\r\n");
					sb.append(StringUtil.getStackTraceString(e));
				}
			}
		}
		message = sb.toString();

		if (!isAdmin)
		{
			message = "Done!";
		}

		return Action.MESSAGE;
	}

	/*-------------------------------------------------------*/
	private String name;
	private boolean turnView = false;

	private boolean consumer = false;

	public boolean isConsumer()
	{
		return consumer;
	}

	public void setConsumer(boolean consumer)
	{
		this.consumer = consumer;
	}

	/**
	 * 检查是否有权限创建 检查数目限制
	 * 
	 * @return
	 */
	public String checkcreateplan()
	{
		message = "true";
		MYPLANIQPermissionManager mpm = new MYPLANIQPermissionManager();
		if (!isAdmin)
		{
			int operationCode = mpm.canPlanCreate(userID, consumer);
			if (operationCode == mpm.PERMISSION_LIMIT)
			{
				message = "You need to subscribe as a Expert User or a Pro User to get the permission of creating plans.";
				return Action.MESSAGE;
			} else if (operationCode == mpm.COUNT_LIMIT)
			{
				message = "You have reached the maximum number of your own plans. To create a new plan, please delete one of your plans in the \"My Portfolio\" page, or subscribe as a higher level user to obtain the permission for more own plans.";
				return Action.MESSAGE;
			}
		}
		return Action.MESSAGE;
	}

	/**
	 * 检查plan是否重名
	 * 
	 * @return
	 */
	public String checkplanname()
	{
		StrategyManager strategyManager = ContextHolder.getStrategyManager();
		message = "true";
		name = StringUtil.getValidName2(name).trim();
		Strategy temp = strategyManager.get(name);
		if (temp != null)
			message = "false";
		return Action.MESSAGE;
	}

	/**
	 * 创建一个plan，只有Admin才有权限
	 */
	public String create() throws Exception
	{
		try
		{
			MYPLANIQPermissionManager mpm = new MYPLANIQPermissionManager();
			if (!isAdmin)
			{
				int operationCode = mpm.canPlanCreate(userID, consumer);
				if (operationCode == mpm.PERMISSION_LIMIT)
				{
					message = "You need to subscribe as a Expert User or a Pro User to get the permission of creating plans.";
					ServletActionContext.getResponse().setStatus(500);
					return Action.MESSAGE;
				} else if (operationCode == mpm.COUNT_LIMIT)
				{
					message = "You have reached the maximum number of your own plans. To create a new plan, please delete one of your plans in the \"My Portfolio\" page, or subscribe as a higher level user to obtain the permission for more own plans.";
					ServletActionContext.getResponse().setStatus(500);
					return Action.MESSAGE;
				}
			}
			user = ContextHolder.getUserManager().getLoginUser();
			StrategyManager strategyManager = ContextHolder.getStrategyManager();
			Strategy strategy = strategyManager.get(Configuration.STRATEGY_401K);
			Strategy newStrategy = strategy.clone();
			newStrategy.setVariablesFor401k(null);
			newStrategy.setID(null);
			newStrategy.setUserName(user.getUserName());
			newStrategy.setUserID(user.getID());
			newStrategy.setName(name);
			newStrategy.set401K(true);
			newStrategy.setConsumerPlan(consumer);
			Long planID = strategyManager.add(newStrategy);
			if (planID == -1)
			{
				message = "The plan name has existed, please enter a new name.";
				return Action.MESSAGE;
			}
			mpm.afterPlanCreate(userID, planID, consumer);
			if (consumer)
			{
				PublicMaker planPublicMaker = new PublicMaker(newStrategy);
				planPublicMaker.makePublic();
			}

			if (turnView)
				message = planID + "";
			else
				message = "ok";
		} catch (RuntimeException e)
		{
			e.printStackTrace();
			message = "ERROR:-1;Fail to create plan";
		}
		return Action.MESSAGE;
	}

	private String type;

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	// 对planName进行处理比较，若相等返回true，否则返回false
	public Strategy compareToPlanName(List<Strategy> preStrategy, String pName)
	{
		List<Strategy> changeStrategy = new ArrayList<Strategy>();
		for (Strategy s : preStrategy)
		{
			if (s.getName().contains("&"))
			{
				String planName = s.getName();
				planName = planName.replace("&", "and");
				s.setName(planName);
			}
			if (s.getName().contains("(K)"))
			{
				String planName = s.getName();
				planName = planName.replace("(K)", "K");
				s.setName(planName);
			}
			if (s.getName().contains(","))
			{
				String planName = s.getName();
				planName = planName.replace(",", "");
				s.setName(planName);
			}
			if (s.getName().contains("，"))
			{
				String planName = s.getName();
				planName = planName.replace("，", "");
				s.setName(planName);
			}
			changeStrategy.add(s);
		}
		if (pName.contains("&"))
		{
			pName = pName.replace("&", "and");
		}
		if (pName.contains("(K)"))
		{
			pName = pName.replace("(K)", "K");
		}
		if (pName.contains(","))
		{
			pName = pName.replace(",", "");
		}
		if (pName.contains("，"))
		{
			pName = pName.replace("，", "");
		}
		for (int i = 0; i < changeStrategy.size(); i++)
		{
			if (pName.equalsIgnoreCase(changeStrategy.get(i).getName()))
			{
				return changeStrategy.get(i);
			}
		}
		return null;
	}

	// 对planName进行处理
	public String dealWithName(String name)
	{
		if (name.contains("&"))
		{
			name = name.replace("&", "and");
		}
		if (name.contains("(K)"))
		{
			name = name.replace("(K)", "K");
		}
		if (name.contains(","))
		{
			name = name.replace(",", "");
		}
		if (name.contains("，"))
		{
			name = name.replace("，", "");
		}
		return name;
	}

	public String createData5500() throws Exception
	{
		StrategyManager strategyManager = ContextHolder.getStrategyManager();
		message = "";
		String systemPath;
		String sysPath = System.getenv("windir");
		String str = System.getProperty("os.name").toUpperCase();
		if (str.indexOf("WINDOWS") == -1)
			systemPath = "/var/tmp/";
		else
			systemPath = sysPath + "\\temp\\";
		String fileName[] = uploadFileFileName.split(",");
		// 生成结果CVS
		CsvListWriter clw = null;
		String resultCSV = systemPath + "Data5500 Result.csv";
		File resultFile = new File(resultCSV);
		clw = new CsvListWriter(new FileWriter(resultFile, false), CsvPreference.EXCEL_PREFERENCE);
		String[] header =
		{ "PlanName", "strategyState", "5500State" };
		clw.writeHeader(header);
		// 获得数据库中的所有plans
		List<Strategy> preStrategy = strategyManager.getStrategies();

		for (int m = 0; m < fileName.length; m++)
		{
			// 每个新表的表名
			List<String> wriPlanName = new ArrayList<String>();
			wriPlanName.add("Table name : " + fileName[m]);
			clw.write(wriPlanName);
			// 读取要写入的CVS
			System.out.println(systemPath + fileName[m]);
			File file = new File(systemPath + fileName[m]);
			CsvListReader reader = new CsvListReader(new FileReader(file), CsvPreference.EXCEL_PREFERENCE);
			String[] col = null;
			long count = 0l;
			col = reader.getCSVHeader(true);
			Map<String, Integer> colMap = new HashMap<String, Integer>();
			// 获得相应列的排号
			if (col != null && col.length > 0)
			{
				for (int i = 0; i < col.length; i++)
				{
					colMap.put(col[i], i);
				}
				// 如果ACK_ID不存在，则将FILING_ID赋值给它
				if (colMap.get("ACK_ID") == null && colMap.get("FILING_ID") != null)
				{
					colMap.put("ACK_ID", colMap.get("FILING_ID"));
				}
			}

			List<String> strList = new ArrayList<String>();
			try
			{
				while ((strList = reader.read()) != null)
				{
					List<String> wriList = new ArrayList<String>();
					// 写入planname
					wriList.add(this.dealWithName(StringUtil.getValidName2(strList.get(colMap.get("ACK_ID"))).trim()));
					// 如果为data5500，获得TYPE_PENSION_BNFT_CODE，查看是否包含2；若为其他则置为2，再从下面判断
					String typeCode;
					if (type.equalsIgnoreCase("Data5500"))
					{
						typeCode = strList.get(colMap.get("TYPE_PENSION_BNFT_CODE")).trim();
					} else
					{
						typeCode = "2";
					}
					if (typeCode.contains("2"))
					{
						// 在strategy表中创建plan
						Strategy temp = new Strategy();
						if (m == 0 && type.equalsIgnoreCase("Data5500"))
						{
							// 判断plan表中是否存在该plan
							name = StringUtil.getValidName2(strList.get(colMap.get("PLAN_NAME"))).trim();
							// Strategy temp = strategyManager.get(name);
							temp = this.compareToPlanName(preStrategy, name);
							name = this.dealWithName(name);
							System.out.println(name);
							if (temp == null || temp.getID() == null)
							{
								// 创建plan置入strategy表中
								try
								{
									MYPLANIQPermissionManager mpm = new MYPLANIQPermissionManager();
									user = ContextHolder.getUserManager().getLoginUser();
									Strategy strategy = strategyManager.get(Configuration.STRATEGY_401K);
									Strategy newStrategy = strategy.clone();
									newStrategy.setVariablesFor401k(null);
									newStrategy.setID(null);
									newStrategy.setUserName(user.getUserName());
									newStrategy.setUserID(user.getID());
									newStrategy.setName(name);
									newStrategy.setStatus(1);
									newStrategy.set401K(true);
									Long planID = strategyManager.add(newStrategy);
									if (planID == -1)
									{
										wriList.add("The plan name has existed, please enter a new name.");
									}
									mpm.afterPlanCreate(userID, planID);
									// 把它置为public
									PublicMaker planPublicMaker = new PublicMaker(newStrategy);
									planPublicMaker.makePublic();
									wriList.add("Create Plan success to strategy.");
								} catch (RuntimeException e)
								{
									// e.printStackTrace();
									wriList.add("The plan has existed in strategy.(-1)");
								}
							} else
							{
								wriList.add("The plan has existed in strategy.");
							}
						}
						// 如果不为模板将进行对plan存在的判断
						boolean isPlanExist = false;
						// 根据ACK_ID判断是否存在于data5500表中，若存在则不用增加，若不存在再根据相应条件判断
						Data5500 isExitData5500 = new Data5500();
						isExitData5500 = strategyManager.getByACK(strList.get(colMap.get("ACK_ID")));
						if (m == 0 && type.equalsIgnoreCase("Data5500"))
						{
							isPlanExist = true;
						} else if (m > 0 && type.equalsIgnoreCase("Data5500"))
						{
							String planName = StringUtil.getValidName2(strList.get(colMap.get("PLAN_NAME"))).trim();
							planName = this.dealWithName(planName);
							if (strategyManager.get(planName) != null)
							{
								isPlanExist = true;
							} else
							{
								isPlanExist = false;
								wriList.add("Can't find the plan in strategy.");
							}
						} else if (type.equalsIgnoreCase("SCH_H"))
						{
							Data5500 data5500 = strategyManager.getByACK(strList.get(colMap.get("ACK_ID")));
							if (data5500 != null)
							{
								isPlanExist = true;
							} else
							{
								isPlanExist = false;
								wriList.add("Can't find the plan in strategy.");
								wriList.add("Update F_SCH_H into Data5500 Fail.");
							}
						}

						if (isPlanExist && type.equalsIgnoreCase("Data5500") && isExitData5500 == null)
						{
							// 添加该plan相关5500信息
							Data5500 data5500 = new Data5500();
							data5500.setACK_ID(strList.get(colMap.get("ACK_ID")).trim());
							data5500.setFORM_PLAN_YEAR_BEGIN_DATE(strList.get(colMap.get("FORM_PLAN_YEAR_BEGIN_DATE")).trim());
							data5500.setPLAN_NAME(this.dealWithName(strList.get(colMap.get("PLAN_NAME"))));
							data5500.setSPONSOR_DFE_NAME(strList.get(colMap.get("SPONSOR_DFE_NAME")).trim());
							Long planId;
							// 解决老的plan name中符号问题
							// System.out.println(strList.get(colMap.get("PLAN_NAME")));
							if (temp != null && temp.getID() != null)
							{
								planId = temp.getID();
							} else
							{
								planId = strategyManager.get(name).getID();
							}
							data5500.setPLAN_ID(planId);
							data5500.setSPONS_DFE_LOC_US_ADDRESS1(strList.get(colMap.get("SPONS_DFE_LOC_US_ADDRESS1")).trim());
							data5500.setSPONS_DFE_LOC_US_ADDRESS2(strList.get(colMap.get("SPONS_DFE_LOC_US_ADDRESS2")).trim());
							data5500.setSPONS_DFE_LOC_US_CITY(strList.get(colMap.get("SPONS_DFE_LOC_US_CITY")).trim());
							data5500.setSPONS_DFE_LOC_US_STATE(strList.get(colMap.get("SPONS_DFE_LOC_US_STATE")).trim());
							data5500.setSPONS_DFE_LOC_US_ZIP(strList.get(colMap.get("SPONS_DFE_LOC_US_ZIP")).trim());
							data5500.setSPONS_DFE_EIN(strList.get(colMap.get("SPONS_DFE_EIN")).trim());
							data5500.setSPONS_DFE_PHONE_NUM(strList.get(colMap.get("SPONS_DFE_PHONE_NUM")).trim());
							data5500.setADMIN_NAME(strList.get(colMap.get("ADMIN_NAME")).trim());
							data5500.setADMIN_US_ADDRESS1(strList.get(colMap.get("ADMIN_US_ADDRESS1")).trim());
							data5500.setADMIN_US_ADDRESS2(strList.get(colMap.get("ADMIN_US_ADDRESS2")).trim());
							data5500.setADMIN_US_CITY(strList.get(colMap.get("ADMIN_US_CITY")).trim());
							data5500.setADMIN_US_STATE(strList.get(colMap.get("ADMIN_US_STATE")).trim());
							data5500.setADMIN_US_ZIP(strList.get(colMap.get("ADMIN_US_ZIP")).trim());
							data5500.setADMIN_PHONE_NUM(strList.get(colMap.get("ADMIN_PHONE_NUM")).trim());
							data5500.setADMIN_SIGNED_NAME(strList.get(colMap.get("ADMIN_SIGNED_NAME")).trim());
							data5500.setADMIN_EIN(strList.get(colMap.get("ADMIN_EIN")).trim());
							data5500.setSPONS_SIGNED_NAME(strList.get(colMap.get("SPONS_SIGNED_NAME")).trim());
							if (strList.get(colMap.get("TOT_PARTCP_BOY_CNT")) == null || strList.get(colMap.get("TOT_PARTCP_BOY_CNT")).equals(""))
							{
								data5500.setTOT_PARTCP_BOY_CNT(0l);
							} else
							{
								data5500.setTOT_PARTCP_BOY_CNT(Long.parseLong(strList.get(colMap.get("TOT_PARTCP_BOY_CNT"))));
							}
							data5500.setTYPE_PENSION_BNFT_CODE(strList.get(colMap.get("TYPE_PENSION_BNFT_CODE")).trim());
							// 判断ACK_ID是否存在，若为NULL则进行add，若不为NULL则进行update
							if (strategyManager.getByACK(strList.get(colMap.get("ACK_ID"))) == null)
							{

								Long id = strategyManager.add(data5500);
								if (id != null && id != 0)
								{
									wriList.add("Add to data5500 Success.");
								} else
								{
									wriList.add("Add to data5500 Fail.");
								}
								count++;
							} else
							{
								boolean flag = false;
								data5500.setID(strategyManager.getByACK(strList.get(colMap.get("ACK_ID"))).getID());
								flag = strategyManager.update(data5500);
								if (flag == true)
								{
									wriList.add("The plan has existed in data5500 And had update Data5500 Success.");
								} else if (flag == false)
								{
									wriList.add("The plan has existed in data5500 And had update Data5500 Fail.");
								}
								count++;
							}
						}
						if (isPlanExist && type.equalsIgnoreCase("SCH_H"))
						{
							boolean flag = false;
							Data5500 data5500 = new Data5500();
							data5500 = strategyManager.getByACK(strList.get(colMap.get("ACK_ID")));
							System.out.println(strList.get(colMap.get("ACK_ID")));
							if (data5500 != null && !data5500.getACK_ID().equalsIgnoreCase(""))
							{
								if (strList.get(colMap.get("TOT_ASSETS_BOY_AMT")).trim().equals("") || strList.get(colMap.get("TOT_ASSETS_BOY_AMT")).trim() == null)
								{
									data5500.setTOT_ASSETS_BOY_AMT(0l);
								} else
								{
									data5500.setTOT_ASSETS_BOY_AMT(Long.parseLong(strList.get(colMap.get("TOT_ASSETS_BOY_AMT")).trim()));
								}
								if (strList.get(colMap.get("TOT_ASSETS_EOY_AMT")).trim().equals("") || strList.get(colMap.get("TOT_ASSETS_EOY_AMT")).trim() == null)
								{
									data5500.setTOT_ASSETS_EOY_AMT(0l);
								} else
								{
									data5500.setTOT_ASSETS_EOY_AMT(Long.parseLong(strList.get(colMap.get("TOT_ASSETS_EOY_AMT")).trim()));
								}
								if (strList.get(colMap.get("TOT_CONTRIB_AMT")).trim().equals("") || strList.get(colMap.get("TOT_CONTRIB_AMT")).trim() == null)
								{
									data5500.setTOT_CONTRIB_AMT(0l);
								} else
								{
									data5500.setTOT_CONTRIB_AMT(Long.parseLong(strList.get(colMap.get("TOT_CONTRIB_AMT")).trim()));
								}
								if (strList.get(colMap.get("TOT_INCOME_AMT")).trim().equals("") || strList.get(colMap.get("TOT_INCOME_AMT")).trim() == null)
								{
									data5500.setTOT_INCOME_AMT(0l);
								} else
								{
									data5500.setTOT_INCOME_AMT(Long.parseLong(strList.get(colMap.get("TOT_INCOME_AMT")).trim()));
								}
								if (strList.get(colMap.get("TOT_DISTRIB_BNFT_AMT")).trim().equals("") || strList.get(colMap.get("TOT_DISTRIB_BNFT_AMT")).trim() == null)
								{
									data5500.setTOT_DISTRIB_BNFT_AMT(0l);
								} else
								{
									data5500.setTOT_DISTRIB_BNFT_AMT(Long.parseLong(strList.get(colMap.get("TOT_DISTRIB_BNFT_AMT")).trim()));
								}
								flag = strategyManager.update(data5500);
							}
							if (flag)
							{
								wriList.add("Find the plan in strategy.");
								wriList.add("Update F_SCH_H into Data5500 Success.");
							} else
							{
								wriList.add("Find the plan in strategy.");
								wriList.add("Update F_SCH_H into Data5500 Fail.");
							}
							count++;
						}
					} else
					{
						wriList.add("The plan don't contain 2.");
						wriList.add("Not updating data5500.");
					}
					clw.write(wriList);
				}
			} catch (Exception e)
			{
				e.printStackTrace();
				clw.close();
				planName = "Data5500 Result.csv";
				File f = new File(resultCSV);
				fis = new FileInputStream(f);
				return Action.DOWNLOAD;
			}
			List<String> listStr = new ArrayList<String>();
			listStr.add("The total numbers are " + count);
			clw.write(listStr);
		}
		clw.close();
		planName = "Data5500 Result.csv";
		File f = new File(resultCSV);
		fis = new FileInputStream(f);
		return Action.DOWNLOAD;
	}

	// public String downloadData5500() throws Exception {
	// String systemPath;
	// String sysPath = System.getenv("windir");
	// String str = System.getProperty("os.name").toUpperCase();
	// if (str.indexOf("WINDOWS") == -1)
	// systemPath = "/var/tmp/";
	// else
	// systemPath = sysPath + "\\temp\\";
	// planName = "Data5500.csv";
	// String path = systemPath + "Data5500.csv";
	// File file = new File(path);
	// fis = new FileInputStream(file);
	// return Action.DOWNLOAD;
	// }

	private File uploadFile;
	private String uploadFileFileName;

	public File getUploadFile()
	{
		return uploadFile;
	}

	public void setUploadFile(File uploadFile)
	{
		this.uploadFile = uploadFile;
	}

	public String getUploadFileFileName()
	{
		return uploadFileFileName;
	}

	public void setUploadFileFileName(String uploadFileFileName)
	{
		this.uploadFileFileName = uploadFileFileName;
	}

	public String upLoadData5500() throws Exception
	{
		message = "";
		String msg = "";
		if (uploadFile != null)
		{
			String systemPath;
			String sysPath = System.getenv("windir");
			String str = System.getProperty("os.name").toUpperCase();
			if (str.indexOf("WINDOWS") == -1)
				systemPath = "/var/tmp/";
			else
				systemPath = sysPath + "\\temp\\";
			String[] fileName = uploadFileFileName.trim().split(",");
			for (int i = 0; i < fileName.length; i++)
			{

				InputStream stream = new FileInputStream(uploadFile);
				OutputStream bos = new FileOutputStream(systemPath + fileName[i]);
				int bytesRead = 0;
				byte[] buffer = new byte[8192];
				while ((bytesRead = stream.read(buffer, 0, 8192)) != -1)
				{
					bos.write(buffer, 0, bytesRead);
				}
				bos.close();
				stream.close();
				if (i == fileName.length - 1)
				{
					msg = msg.trim() + fileName[i];
				} else
				{
					msg = msg.trim() + fileName[i] + ",";
				}
			}
			message = "<script>parent.callback(\"" + msg + "\")</script>";
		}

		return Action.MESSAGE;
	}

	// 上传testprice cvs
	public String upLoadTestPrice() throws Exception
	{
		String systemPath;
		String sysPath = System.getenv("windir");
		String str = System.getProperty("os.name").toUpperCase();
		if (str.indexOf("WINDOWS") == -1)
			systemPath = "/var/tmp/";
		else
			systemPath = sysPath + "\\temp\\";
		if (uploadFile != null)
		{
			if (uploadFileFileName.equals("TestAdjClose.csv"))
			{
				InputStream stream = new FileInputStream(uploadFile);
				OutputStream bos = new FileOutputStream(systemPath + uploadFileFileName);
				int bytesRead = 0;
				byte[] buffer = new byte[8192];
				while ((bytesRead = stream.read(buffer, 0, 8192)) != -1)
				{
					bos.write(buffer, 0, bytesRead);
				}
				bos.close();
				stream.close();
				message = "<script>parent.callbackAdjClose(\"" + "uploadFile success" + "\")</script>";
			} else
			{
				message = "<script>parent.callbackAdjClose(\"" + "the file name must be TestAdjClose.csv" + "\")</script>";
			}
		}
		return Action.MESSAGE;
	}

	// adjPrice测试方法
	public String testAdjClose() throws Exception
	{
		SecurityManager securityManager = ContextHolder.getSecurityManager();
		String systemPath;
		String sysPath = System.getenv("windir");
		String str = System.getProperty("os.name").toUpperCase();
		if (str.indexOf("WINDOWS") == -1)
			systemPath = "/var/tmp/";
		else
			systemPath = sysPath + "\\temp\\";
		String path = systemPath + "TestAdjClose.csv";
		File fileRead = new File(path);
		CsvListReader readerCsv = new CsvListReader(new FileReader(fileRead), CsvPreference.EXCEL_PREFERENCE);
		List<String> listStr;
		List<Security> listSecurity = new ArrayList<Security>();
		while ((listStr = readerCsv.read()) != null)
		{
			listSecurity.add(securityManager.get(Long.parseLong(listStr.get(0))));
		}
		LTIDownLoader ltiDownLoader = new LTIDownLoader();
		ltiDownLoader.testUpdateDailyDataNew(listSecurity);
		message = "The operation of adjPrice is finished.";
		return Action.MESSAGE;
	}

	// 上传adjnav cvs
	public String upLoadAdjNav() throws Exception
	{
		String systemPath;
		String sysPath = System.getenv("windir");
		String str = System.getProperty("os.name").toUpperCase();
		if (str.indexOf("WINDOWS") == -1)
			systemPath = "/var/tmp/";
		else
			systemPath = sysPath + "\\temp\\";
		if (uploadFile != null)
		{
			if (uploadFileFileName.equals("TestAdjNav.csv"))
			{
				InputStream stream = new FileInputStream(uploadFile);
				OutputStream bos = new FileOutputStream(systemPath + uploadFileFileName);
				int bytesRead = 0;
				byte[] buffer = new byte[8192];
				while ((bytesRead = stream.read(buffer, 0, 8192)) != -1)
				{
					bos.write(buffer, 0, bytesRead);
				}
				bos.close();
				stream.close();
				message = "<script>parent.callback1(\"" + "uploadFile success" + "\")</script>";
			} else
			{
				message = "<script>parent.callback1(\"" + "the file name must be TestAdjNav.csv" + "\")</script>";
			}
		}
		return Action.MESSAGE;
	}
	
	public String cashUpdateForPriceUpdate(){
		message ="";
		LTIDownLoader ldl = new LTIDownLoader();

		try {
			ldl.updateCash("CASH", "^IRX");
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		try {
			ldl.updateTSPGFund("TSPGFUND", "^TNX", "^FVX", false);
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		try {
			ldl.updateTSPGFund("STABLEVALUE", "^TNX", "^FVX", false);
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		try {
			ldl.updateSpecialFund("QQQQ", "QQQ");
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		message = "Success to update Cash and TSPGFUND";
		return Action.MESSAGE;
	}

	// 调整adjnav
	public String testAdjNav() throws Exception
	{
		SecurityManager securityManager = ContextHolder.getSecurityManager(); // 实例化
		DataManager dataManager = ContextHolder.getDataManager();
		String systemPath;
		message = "";
		String sysPath = System.getenv("windir");
		String str = System.getProperty("os.name").toUpperCase(); // 改成大写,os.name:操作系统的名称
		// 调整路径
		if (str.indexOf("WINDOWS") == -1)
			systemPath = "/var/tmp/";
		else
			systemPath = sysPath + "\\temp\\";
		String path = systemPath + "TestAdjNav.csv";
		File fileRead = new File(path);
		CsvListReader readerCsv = new CsvListReader(new FileReader(fileRead), CsvPreference.EXCEL_PREFERENCE); // 读取EXCEL文件
		List<String> listStr;
		List<Security> listSecurity = new ArrayList<Security>();

		while ((listStr = readerCsv.read()) != null)
		{
			listSecurity.add(securityManager.get(Long.parseLong(listStr.get(0))));
		}
		for (Security se : listSecurity)
		{
			boolean success = true;
			if (dateStr.equals("") || dateStr == "")
			{
				List<SecurityDailyData> sdds = securityManager.getNAVList(se.getID());
				if ((sdds != null) && (sdds.size() > 0) && (sdds.get(0).getNAV() != null && sdds.get(0).getNAV() > 0.0) && (sdds.get(0).getAdjNAV() == null || sdds.get(0).getAdjNAV() == 0.0))
				{
					sdds.get(0).setAdjNAV(sdds.get(0).getNAV());
					securityManager.updateDailyData(sdds.get(0));
					success = dataManager.updateAdjustNAVFromStartDate(se.getSymbol(), sdds.get(0).getDate());
				} else
				{
					success = dataManager.updateAdjustNAVFromStartDate(se.getSymbol(), sdds.get(0).getDate());
				}
			} else
			{
				success = dataManager.updateAdjustNAVFromStartDate(se.getSymbol(), LTIDate.parseStringToDate(dateStr));
			}
			if (!success)
				message = message.trim() + se.getSymbol() + ",fail" + "<br>\r\n";
			else
				message = message.trim() + se.getSymbol() + ",success" + "<br>\r\n";
		}
		return Action.MESSAGE;
	}
	
	

	/*-------------------------------------------------------*/
	/**
	 * 将一个plan另存为
	 */
	public String saveas()
	{
		long newID = -1;
		try
		{
			if (!isAdmin)
			{
				message = "You are trying to access a private plan. Please retrun to the previous page or the home page. Further questions, please contact support@myplaniq.com . Thank you for your suggestions.";
				return Action.MESSAGE;
			}
			StrategyManager strategyManager = ContextHolder.getStrategyManager();
			plan = strategyManager.get(ID);
			if (plan == null || userID == Configuration.USER_ANONYMOUS)
			{
				message = "the plan does not exist or you cannot execute this operation.";
				return Action.MESSAGE;
			}

			Strategy newPlan = plan.clone();
			newPlan.setID(null);
			newPlan.setUserID(userID);
			newPlan.setName(name);
			newID = strategyManager.add(newPlan);
			if (newID == -1)
			{
				message = "Cannot create new plan with the name [" + name + "].";
				return Action.MESSAGE;
			} else
			{
				message = "OK";
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			message = "ERROR:-1;Fail to save as the plan, please check the name of the plan.";
			return Action.MESSAGE;
		}
		action_name = "view";
		params = "ID=" + newID;
		return Action.REDIRECT;

	}

	public String modify()
	{
		long newID = -1;
		try
		{
			MYPLANIQPermissionManager mpm = new MYPLANIQPermissionManager();
			if (!isAdmin)
			{
				int operationCode = mpm.canPlanCreate(userID, true);
				if (operationCode == mpm.PERMISSION_LIMIT)
				{
					message = "You need to subscribe as a Expert User or a Pro User to get the permission of creating plans.";
					ServletActionContext.getResponse().setStatus(500);
					return Action.MESSAGE;
				} else if (operationCode == mpm.COUNT_LIMIT)
				{
					message = "You have reached the maximum number of your own plans. To create a new plan, please delete one of your plans in the \"My Portfolio\" page, or subscribe as a higher level user to obtain the permission for more own plans.";
					ServletActionContext.getResponse().setStatus(500);
					return Action.MESSAGE;
				}
			}
			user = ContextHolder.getUserManager().getLoginUser();
			StrategyManager strategyManager = ContextHolder.getStrategyManager();
			plan = strategyManager.get(ID);
			if (plan == null || userID == Configuration.USER_ANONYMOUS)
			{
				message = "the plan does not exist or you cannot execute this operation.";
				return Action.MESSAGE;
			}

			Strategy newPlan = plan.clone();
			newPlan.setID(null);
			newPlan.setUserID(userID);
			newPlan.setName(user.getUserName() + "_" + plan.getName());
			newPlan.setDescription("");
			newPlan.setConsumerPlan(true);
			newID = strategyManager.add(newPlan);
			if (newID == -1)
			{
				message = "Cannot create new plan with the name [" + name + "].";
				return Action.MESSAGE;
			} else
			{
				message = "OK";
			}
			mpm.afterPlanCreate(userID, newID, consumer);
		} catch (Exception e)
		{
			e.printStackTrace();
			message = "ERROR:-1;Fail to save as the plan, please check the name of the plan.";
			return Action.MESSAGE;
		}
		action_name = "view";
		params = "ID=" + newID;
		return Action.REDIRECT;

	}

	/*-------------------------------------------------------*/

	/**
	 * strategy或plan的id
	 */
	private Long ID;
	/**
	 * strategy所属的strategy的id
	 */
	private Long MainStrategyID;

	/**
	 * 更改一个strategy的main strategy id
	 * 
	 * @param isAdmin
	 * @param MainStrategyID
	 * @param userID
	 */
	public String updatemainstrategyid()
	{
		try
		{
			StrategyManager strategyManager = ContextHolder.getStrategyManager();
			GroupManager gm = ContextHolder.getGroupManager();
			Strategy strategy = strategyManager.get(ID);
			Strategy center = strategyManager.get(Configuration.STRATEGY_401K);
			if (strategy == null)
			{
				message = "The strategy does not exist.";
				return Action.MESSAGE;
			}

			if (!isAdmin && userID != strategy.getUserID())
			{
				message = "You are trying to access a private plan. Please retrun to the previous page or the home page. Further questions, please contact support@myplaniq.com . Thank you for your suggestions.";
				return Action.MESSAGE;
			}
			if (MainStrategyID == -1)
				MainStrategyID = null;
			strategy.setMainStrategyID(MainStrategyID);

			strategyManager.update(strategy);

			message = "ok";
			return Action.MESSAGE;
		} catch (Exception e)
		{
			message = StringUtil.getStackTraceString(e);
			return Action.MESSAGE;
		}
	}

	/*-------------------------------------------------------*/
	/**
	 * ??
	 */
	private String originalString;

	/**
	 * 从originalstring分析出variables
	 * 
	 * @param originalString
	 * @param variables
	 * @throws IOException
	 */
	public String getparsedvariables() throws IOException
	{
		Parse401KParameters pp = new Parse401KParameters();
		pp.setOriginalString(originalString);
		pp.execute();
		variables = pp.getVariables();
		return Action.SUCCESS;
	}

	/**
	 * 批量删除
	 */
	public String adminbatchdelete()
	{
		StrategyManager sm = ContextHolder.getStrategyManager();
		List<Strategy> plans = sm.getStrategiesByType(Configuration.STRATEGY_TYPE_401K);
		StringBuffer messages = new StringBuffer();
		for (int i = 0; i < plans.size(); i++)
		{
			messages.append("Deleting for " + plans.get(i).getName());
			messages.append("<br>\r\n");
			ID = plans.get(i).getID();
			try
			{
				admindelete();
			} catch (Exception e)
			{
				// e.printStackTrace();
				messages.append(StringUtil.getStackTraceString(e));
			}
			messages.append(message);
		}
		message = messages.toString();
		return Action.MESSAGE;
	}

	private boolean batch = false;

	/**
	 * 批量产生portfolio
	 */
	public String adminbatchgenerate()
	{
		StrategyManager sm = ContextHolder.getStrategyManager();
		List<Strategy> plans = sm.getStrategiesByType(Configuration.STRATEGY_TYPE_401K);
		StringBuffer messages = new StringBuffer();
		batch = true;
		for (int i = 0; i < plans.size(); i++)
		{
			messages.append("Generating for " + plans.get(i).getName());
			messages.append("<br>\r\n");

			ID = plans.get(i).getID();
			try
			{
				generate();
			} catch (Exception e)
			{
				// e.printStackTrace();
				messages.append(StringUtil.getStackTraceString(e));
			}
			messages.append(message);
		}
		batch = false;
		message = messages.toString();
		return Action.MESSAGE;
	}

	public String adminbatchexecute()
	{
		return Action.SUCCESS;
	}

	public String adminbatchmonitor()
	{
		return Action.SUCCESS;
	}

	public String adminbatchstop()
	{
		return Action.SUCCESS;
	}

	public String adminbatchexecuteplans()
	{
		return Action.SUCCESS;
	}

	public String adminbatchmonitorplans()
	{
		return Action.SUCCESS;
	}

	private String hostname;

	public String getHostname()
	{
		return hostname;
	}

	public void setHostname(String hostname)
	{
		this.hostname = hostname;
	}

	public String _generate() throws Exception
	{
		StrategyManager strategyManager = ContextHolder.getStrategyManager();
		GroupManager gm = ContextHolder.getGroupManager();

		Strategy plan = strategyManager.get(ID);
		Date startingdate = null;
		try
		{
			startingdate = parseDate(plan.getAttributes().get("startingdate"));
		} catch (RuntimeException e)
		{
		}
		PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();
		Strategy strategy = strategyManager.get(Configuration.STRATEGY_401K);

		List<VariableFor401k> configurations = strategy.getVariablesFor401k();
		List<VariableFor401k> variables = plan.getVariablesFor401k();
		StringBuffer AssetClass = new StringBuffer();
		StringBuffer CandidateFunds = new StringBuffer();
		StringBuffer RedemptionLimit = new StringBuffer();
		StringBuffer WatingPeriod = new StringBuffer();
		StringBuffer RoundtripLimit = new StringBuffer();

		for (int i = 0; i < variables.size(); i++)
		{
			if (variables.get(i).getMemo() != null && variables.get(i).getMemo().toLowerCase().startsWith("n"))
				continue;
			AssetClass.append(variables.get(i).getAssetClassName());
			CandidateFunds.append(variables.get(i).getSymbol());
			RedemptionLimit.append(variables.get(i).getRedemption());
			WatingPeriod.append(variables.get(i).getWaitingPeriod());
			RoundtripLimit.append(variables.get(i).getRoundtripLimit());

			if (i != variables.size() - 1)
			{
				AssetClass.append(",");
				CandidateFunds.append(",");
				RedemptionLimit.append(",");
				WatingPeriod.append(",");
				RoundtripLimit.append(",");
			}
		}

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < configurations.size(); i++)
		{
			String portfolioName = plan.getName() + " " + configurations.get(i).getPortfolioName();
			Portfolio portfolio = portfolioManager.get(portfolioName);
			boolean isnew = false;
			if (portfolio == null)
			{
				portfolio = portfolioManager.get(configurations.get(i).getPortfolioID()).clone();
				isnew = true;
				portfolio.setID(null);
			}
			StrategyInf si = portfolio.getStrategies();
			if (si == null)
			{
				si = new StrategyInf();
				portfolio.setStrategies(si);
			}
			Map<String, String> ht = portfolio.getStrategies().getAssetAllocationStrategy().getParameter();
			if (ht == null)
			{
				ht = new HashMap<String, String>();
				portfolio.getStrategies().getAssetAllocationStrategy().setParameter(ht);
			}

			ht.put("AssetClass", AssetClass.toString());
			ht.put("CandidateFund", CandidateFunds.toString());
			ht.put("RedemptionLimit", RedemptionLimit.toString());
			ht.put("WaitingPeriod", WatingPeriod.toString());
			ht.put("RoundtripLimit", RoundtripLimit.toString());
			ht.put("PlanID", plan.getID() + "");

			portfolio.setName(portfolioName);

			portfolio.setUserID(Configuration.SUPER_USER_ID);
			portfolio.setState(Configuration.PORTFOLIO_STATE_ALIVE);
			portfolio.setProduction(true);
			portfolio.setMainStrategyID(plan.getID());
			if (startingdate != null)
			{
				portfolio.setStartingDate(startingdate);
			}

			if (!isnew)
			{
				portfolioManager.update(portfolio);
				sb.append("[update][" + portfolio.getID() + "]");
				sb.append(portfolioName);
				sb.append("\r\n");
			} else
			{
				portfolioManager.save(portfolio);
				sb.append("[create][" + portfolio.getID() + "]");
				sb.append(portfolioName);
				sb.append("\r\n");
			}
		}
		return sb.toString();
	}

	/*-------------------------------------------------------*/
	/**
	 * 根据template portfolios产生portfolios
	 * 
	 * @param ID
	 */
	public String generate() throws Exception
	{
		UserManager userManager = ContextHolder.getUserManager();
		User u = userManager.getLoginUser();
//		if (!u.getID().equals(Configuration.SUPER_USER_ID) && !u.getID().equals(plan.getUserID()))
//		{
//			return Action.ERROR;
//		}

		message = "<pre>" + _generate() + "</pre>";

		return Action.MESSAGE;
	}

	// public String setParametersForPlan(Long planID){
	//
	// }

	// public String saveandupdateparameter(){
	// save();
	// setParametersForPlan(planID);
	// }

	/**
	 * 根据template portfolios产生portfolios
	 * 
	 * @param ID
	 */
	public String adminupdateparameter() throws Exception
	{
		UserManager userManager = ContextHolder.getUserManager();
		User u = userManager.getLoginUser();
		StrategyManager strategyManager = ContextHolder.getStrategyManager();
		ProfileManager profileManager = (ProfileManager) ContextHolder.getInstance().getApplicationContext().getBean("profileManager");
		Strategy plan = strategyManager.get(ID);
		// if (!u.getID().equals(Configuration.SUPER_USER_ID)
		// && !u.getID().equals(plan.getUserID())) {
		// return Action.ERROR;
		// }

		// setParameterForPlan(planID);

		PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();

		List<VariableFor401k> variables = plan.getVariablesFor401k();
		StringBuffer AssetClass = new StringBuffer();
		StringBuffer CandidateFunds = new StringBuffer();
		StringBuffer RedemptionLimit = new StringBuffer();
		StringBuffer WatingPeriod = new StringBuffer();
		StringBuffer RoundtripLimit = new StringBuffer();

		for (int i = 0; i < variables.size(); i++)
		{
			if (variables.get(i).getMemo() != null && variables.get(i).getMemo().toLowerCase().startsWith("n"))
				continue;
			AssetClass.append(variables.get(i).getAssetClassName());
			CandidateFunds.append(variables.get(i).getSymbol());
			RedemptionLimit.append(variables.get(i).getRedemption());
			WatingPeriod.append(variables.get(i).getWaitingPeriod());
			RoundtripLimit.append(variables.get(i).getRoundtripLimit());

			if (i != variables.size() - 1)
			{
				AssetClass.append(",");
				CandidateFunds.append(",");
				RedemptionLimit.append(",");
				WatingPeriod.append(",");
				RoundtripLimit.append(",");
			}
		}

		StringBuffer sb = new StringBuffer();
		List<Portfolio> modelPortfolioList = strategyManager.getModelPortfolios(ID);
		List<Profile> userProfileList = profileManager.getProfilesByPlanID(ID);
		if (userProfileList != null && userProfileList.size() > 0)
		{
			for (Profile p : userProfileList)
			{
				Portfolio pp = portfolioManager.get(p.getPortfolioID());
				modelPortfolioList.add(pp);
			}
		}
		for (int i = 0; i < modelPortfolioList.size(); i++)
		{
			Portfolio portfolio = modelPortfolioList.get(i);

			if (portfolio != null)
			{
				Map<String, String> ht = portfolio.getStrategies().getAssetAllocationStrategy().getParameter();
				if (ht == null)
				{
					ht = new HashMap<String, String>();
					portfolio.getStrategies().getAssetAllocationStrategy().setParameter(ht);
				}
				ht.put("AssetClass", AssetClass.toString());
				ht.put("CandidateFund", CandidateFunds.toString());
				ht.put("RedemptionLimit", RedemptionLimit.toString());
				ht.put("WaitingPeriod", WatingPeriod.toString());
				ht.put("RoundtripLimit", RoundtripLimit.toString());
				ht.put("PlanID", plan.getID() + "");

				portfolioManager.update(portfolio);
				sb.append("[update][" + portfolio.getID() + "]");
				sb.append(portfolio.getName());
				sb.append("\r\n");
			}
		}
		message = "<pre>" + sb.toString() + "</pre>";

		return Action.MESSAGE;
	}

	public String applyUpdateparameter()
	{
		UserManager userManager = ContextHolder.getUserManager();
		User u = userManager.getLoginUser();
		StrategyManager strategyManager = ContextHolder.getStrategyManager();
		ProfileManager profileManager = (ProfileManager) ContextHolder.getInstance().getApplicationContext().getBean("profileManager");
		Strategy plan = strategyManager.get(ID);
		if (!u.getID().equals(plan.getUserID()))
		{
			return Action.ERROR;
		}

		// setParameterForPlan(planID);

		PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();

		List<VariableFor401k> variables = plan.getVariablesFor401k();
		StringBuffer AssetClass = new StringBuffer();
		StringBuffer CandidateFunds = new StringBuffer();
		StringBuffer RedemptionLimit = new StringBuffer();
		StringBuffer WatingPeriod = new StringBuffer();
		StringBuffer RoundtripLimit = new StringBuffer();

		for (int i = 0; i < variables.size(); i++)
		{
			if (variables.get(i).getMemo() != null && variables.get(i).getMemo().toLowerCase().startsWith("n"))
				continue;
			AssetClass.append(variables.get(i).getAssetClassName());
			CandidateFunds.append(variables.get(i).getSymbol());
			RedemptionLimit.append(variables.get(i).getRedemption());
			WatingPeriod.append(variables.get(i).getWaitingPeriod());
			RoundtripLimit.append(variables.get(i).getRoundtripLimit());

			if (i != variables.size() - 1)
			{
				AssetClass.append(",");
				CandidateFunds.append(",");
				RedemptionLimit.append(",");
				WatingPeriod.append(",");
				RoundtripLimit.append(",");
			}
		}

		StringBuffer sb = new StringBuffer();
		List<Portfolio> modelPortfolioList = strategyManager.getModelPortfolios(ID);
		List<Profile> userProfileList = profileManager.getProfilesByPlanID(ID);
		if (userProfileList != null && userProfileList.size() > 0)
		{
			for (Profile p : userProfileList)
			{
				Portfolio pp = portfolioManager.get(p.getPortfolioID());
				modelPortfolioList.add(pp);
			}
		}
		for (int i = 0; i < modelPortfolioList.size(); i++)
		{
			Portfolio portfolio = modelPortfolioList.get(i);

			if (portfolio != null)
			{
				Map<String, String> ht = portfolio.getStrategies().getAssetAllocationStrategy().getParameter();
				if (ht == null)
				{
					ht = new HashMap<String, String>();
					portfolio.getStrategies().getAssetAllocationStrategy().setParameter(ht);
				}
				ht.put("AssetClass", AssetClass.toString());
				ht.put("CandidateFund", CandidateFunds.toString());
				ht.put("RedemptionLimit", RedemptionLimit.toString());
				ht.put("WaitingPeriod", WatingPeriod.toString());
				ht.put("RoundtripLimit", RoundtripLimit.toString());
				ht.put("PlanID", plan.getID() + "");

				portfolioManager.update(portfolio);
				sb.append("[update][" + portfolio.getID() + "]");
				sb.append(portfolio.getName());
				sb.append("\r\n");
			}
		}
		message = "<pre>" + sb.toString() + "</pre>";

		return Action.MESSAGE;
	}

	/*-------------------------------------------------------*/
	/**
	 * 删除plan下的所有model portfolio
	 * 
	 * @param iD
	 */
	public String admindelete() throws Exception
	{
		UserManager userManager = ContextHolder.getUserManager();
		User u = userManager.getLoginUser();
		StrategyManager strategyManager = ContextHolder.getStrategyManager();
		Strategy plan = strategyManager.get(ID);

		if (!u.getID().equals(Configuration.SUPER_USER_ID) && !u.getID().equals(plan.getUserID()))
		{
			return Action.ERROR;
		}
		PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();

		List<Portfolio> portfolios = portfolioManager.getModelPortfolios(ID);

		if (portfolios == null || portfolios.size() == 0)
		{
			message = "Nothing to delete.";
			return Action.MESSAGE;
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < portfolios.size(); i++)
		{
			sb.append(portfolios.get(i).getName());
			sb.append("\r\n");
			portfolioManager.remove(portfolios.get(i).getID());
		}
		message = "<pre>" + sb.toString() + "</pre>";

		return Action.MESSAGE;
	}

	/*-------------------------------------------------------*/
	private List<Portfolio> portfolios;
	private int portfolioUpdate = Configuration.PORTFOLIO_UPDATE_IGNOREIFCANNOTUPDATE;
	private String runInServerFlag = "Server";
	private String host;

	/**
	 * 执行plan下的所有portfolio
	 * 
	 * @param ID
	 * @param portfolios
	 */
	public String execute()
	{
		UserManager userManager = ContextHolder.getUserManager();
		User u = userManager.getLoginUser();
		StrategyManager strategyManager = ContextHolder.getStrategyManager();
		Strategy plan = strategyManager.get(ID);
		if (!u.getID().equals(Configuration.SUPER_USER_ID) && !u.getID().equals(plan.getUserID()))
		{
			return Action.ERROR;
		}
		portfolios = strategyManager.getModelPortfolios(ID);
		hostname = ServletActionContext.getRequest().getServerName();
		return Action.SUCCESS;
	}

	/*-------------------------------------------------------*/
	/**
	 * plan的description
	 */
	private String description;
	/**
	 * plan的short description
	 */
	private String shortDescription;
	/**
	 * plan的reference
	 */
	private String reference;
	/**
	 * plan 的 starting date，如果存在，产生plan下的portfolios时会将其覆盖template
	 * portfolio的starting date
	 */
	private String startingdate;
	private List<String> fileList;

	private Date parseDate(String datestr)
	{
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		Date d = null;
		try
		{
			d = sdf1.parse(datestr);
			return d;
		} catch (ParseException e)
		{
		}
		SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yyyy");
		try
		{
			d = sdf2.parse(datestr);
			return d;
		} catch (ParseException e)
		{
		}

		return d;
	}

	private List<Pair> attributes;

	/**
	 * 获取plan的parameters
	 * 
	 * @param plan
	 * @param attributes
	 */
	public String ajaxgetparameters()
	{
		plan = ContextHolder.getStrategyManager().get(ID);
		Map<String, String> attrs = plan.getAttributes();
		attributes = new ArrayList<Pair>();
		if (attrs != null)
		{
			Iterator<String> iter = attrs.keySet().iterator();
			while (iter.hasNext())
			{
				String key = iter.next();
				String value = attrs.get(key);
				attributes.add(new Pair(key, value));
			}
		}
		return Action.SUCCESS;
	}

	/**
	 * 设置plan的parameters
	 * 
	 * @param plan
	 * @param attributes
	 */
	public String adminsaveparameters()
	{
		plan = ContextHolder.getStrategyManager().get(ID);
		Map<String, String> attrs = new HashMap<String, String>();
		if (attributes != null)
		{
			for (Pair p : attributes)
			{
				attrs.put(p.getPre(), p.getPost());
			}
		}

		plan.setAttributes(new HashMap<String, String>());

		ContextHolder.getStrategyManager().update(plan);
		return Action.MESSAGE;
	}

	/**
	 * 更改plan的内容
	 * 
	 * @param message
	 * @param isAdmin
	 * @param userID
	 * @param description
	 * @param shortDescription
	 * @param name
	 * @param reference
	 * @param startingdate
	 * @param ID
	 * @throws Exception
	 */
	public String save() throws Exception
	{
		try
		{
			StrategyManager strategyManager = ContextHolder.getStrategyManager();
			PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();
			Strategy strategy = strategyManager.get(ID);
			List<Portfolio> portfolios = portfolioManager.getModelPortfolios(ID);
			if (strategy == null)
			{
				message = "The strategy does not exist.";
				return Action.MESSAGE;
			}
			if (!isAdmin && userID != strategy.getUserID())
			{
				message = "You are trying to access a private plan. Please retrun to the previous page or the home page. Further questions, please contact support@myplaniq.com . Thank you for your suggestions.";
				return Action.MESSAGE;
			}
			if (description != null)
			{
				strategy.setDescription(description);
			}
			if (shortDescription != null && !shortDescription.equals(""))
			{
				strategy.setShortDescription(shortDescription);
			}
			if (reference != null && !reference.equals(""))
			{
				strategy.setReference(reference);
			}
			if (name != null && !name.equals(""))
			{
				strategy.setName(name);
				for (Portfolio p : portfolios)
				{
					String pname = p.getName();
					if (p.getUserID() == 1 && pname.toLowerCase().contains("strategic asset allocation growth"))
					{
						pname = name + " Strategic Asset Allocation Growth";
					} else if (p.getUserID() == 1 && pname.toLowerCase().contains("strategic asset allocation moderate"))
					{
						pname = name + " Strategic Asset Allocation Moderate";
					} else if (p.getUserID() == 1 && pname.toLowerCase().contains("strategic asset allocation conservative"))
					{
						pname = name + " Strategic Asset Allocation Conservative";
					} else if (p.getUserID() == 1 && pname.toLowerCase().contains("tactical asset allocation growth"))
					{
						pname = name + " Tactical Asset Allocation Growth";
					} else if (p.getUserID() == 1 && pname.toLowerCase().contains("tactical asset allocation moderate"))
					{
						pname = name + " Tactical Asset Allocation Moderate";
					} else if (p.getUserID() == 1 && pname.toLowerCase().contains("tactical asset allocation conservative"))
					{
						pname = name + " Tactical Asset Allocation Conservative";
					}
					if (p.getUserID() == 1)
					{
						strategyManager.updatePortfolioName(p.getID(), pname);
					}
				}
			}
			if (startingdate != null && !startingdate.equals(""))
			{
				Date startingDate = parseDate(startingdate.substring(startingdate.indexOf(":") + 1).replaceFirst(" ", ""));
				if (startingDate != null)
				{
					SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
					Map<String, String> attrs = strategy.getAttributes();
					if (attrs == null)
					{
						attrs = new HashMap<String, String>();
					}
					attrs.put("startingdate", sdf.format(startingDate));
					strategy.setAttributes(attrs);
				}
			}

			strategyManager.update(strategy);
			action_name = "view";
			params = "ID=" + ID;
			return Action.REDIRECT;
		} catch (RuntimeException e)
		{
			e.printStackTrace();
			message = "ERROR:-1;Fail to save the plan.";
			return Action.MESSAGE;
		}
	}

	/**
	 * 删除data object 并且保存fund table
	 * 
	 * @return
	 */
	public String saveandremovedata()
	{
		removedata();
		return updatevariables();
	}

	/*-------------------------------------------------------*/
	private String planName;

	public String getPlanName()
	{
		return planName;
	}

	public void setPlanName(String planName)
	{
		this.planName = planName;
	}

	private String dataContent;

	public String getDataContent()
	{
		return dataContent;
	}

	public void setDataContent(String dataContent)
	{
		this.dataContent = dataContent;
	}

	private boolean isPublic = false;

	public boolean isPublic()
	{
		return isPublic;
	}

	public void setPublic(boolean isPublic)
	{
		this.isPublic = isPublic;
	}

	private int planType;
	private int status;
	private String ticker;

	/**
	 * 修改plan的类型
	 * 
	 * @return
	 */
	public String updateplantype()
	{
		StrategyManager strategyManager = ContextHolder.getStrategyManager();
		plan = strategyManager.get(ID);
		plan.setPlanType(planType);
		strategyManager.update(plan);
		message = "successful";
		return Action.MESSAGE;
	}

	/**
	 * 修改plan的状态
	 * 
	 * @return
	 */
	public String updateplanstatus()
	{
		StrategyManager strategyManager = ContextHolder.getStrategyManager();
		plan = strategyManager.get(ID);
		plan.setStatus(status);
		strategyManager.update(plan);
		message = "successful";
		return Action.MESSAGE;
	}

	/**
	 * 修改plan的ticker
	 * 
	 * @return
	 */
	public String updateplanticker()
	{
		StrategyManager strategyManager = ContextHolder.getStrategyManager();
		plan = strategyManager.get(ID);
		plan.setTicker(ticker);
		strategyManager.update(plan);
		message = "successful";
		return Action.MESSAGE;
	}

	private List<String> companyList;

	private List<String> categoryList;

	private String company = "";

	private String category1 = "";

	private String category2 = "";

	private String category3 = "";

	private List<CompanyFund> companyFundList;

	private List<VAFund> vaFundList;

	private List<VAFund> vaFundSearchList;

	private String searchType;

	private int searchCategory;

	/**
	 * companyfund分类
	 * 
	 * @return
	 */
	public String companyfundtable()
	{
		SecurityManager securityManager = (SecurityManager) ContextHolder.getSecurityManager();
		companyList = securityManager.getCompanyNameList();
		companyFundList = securityManager.getCompanyFundListByCompanyAndCategorys(company, category1, category2, category3);
		categoryList = securityManager.getAssetNameListByCompanyName(company);
		return Action.SUCCESS;
	}

	/**
	 * 从originalstring分析出vafunds
	 * 
	 * @return
	 * @throws IOException
	 */
	public String vafundtable() throws IOException
	{
		vaFundList = new ArrayList<VAFund>();
		if (originalString != null)
		{
			String[] lines = originalString.replace("\r", "").split("\n");
			for (int i = 0; i < lines.length; i++)
			{
				String line = lines[i].replace("#", "@=@");
				if (line.equals(""))
					continue;
				vaFundList.add(VAFundUtil.vaFundSearch(line));
			}
		}
		return Action.SUCCESS;
	}

	private String barronName;
	private String fullName;
	private int fundIndex;

	public String updatevafunds()
	{
		SecurityManager securityManager = (SecurityManager) ContextHolder.getSecurityManager();
		processVAFunds();
		securityManager.addAllVAFunds(vaFundList);
		message = checkSecurity(vaFundList);
		return Action.MESSAGE;
	}

	private String checkSecurity(List<VAFund> vaFundList)
	{
		String mesg = "";
		LTIDownLoader ltiDownLoader = new LTIDownLoader();
		AssetClassManager assetClassManager = (AssetClassManager) ContextHolder.getAssetClassManager();
		SecurityManager securityManager = (SecurityManager) ContextHolder.getSecurityManager();
		Set<String> symbolSet = new HashSet<String>();
		String suc = "successful:\n";
		String fail = "fail:\n";
		List<VAFund> toBeModified = new ArrayList<VAFund>();
		if (vaFundList != null && vaFundList.size() > 0)
		{
			for (VAFund vaFund : vaFundList)
			{
				if (vaFund.getTicker().equalsIgnoreCase("NoTicker"))
					continue;
				Security se = securityManager.getBySymbol(vaFund.getTicker());
				if (se == null)
				{
					symbolSet.add(vaFund.getTicker());
					toBeModified.add(vaFund);
				}
			}
			List<Security> securityList = ltiDownLoader.batchImportSecurity(symbolSet);
			for (VAFund vaFund : toBeModified)
			{
				boolean found = false;
				for (Security se : securityList)
				{
					if (vaFund.getTicker().equalsIgnoreCase(se.getSymbol()))
					{
						String assetName = "NoResult";
						if (se.getClassID() != null)
						{
							AssetClass ac = assetClassManager.get(se.getClassID());
							if (ac != null)
								assetName = ac.getName();
						}
						vaFund.setAssetName(assetName);
						vaFund.setFundName(se.getName());
						vaFund.setMemo("");
						found = true;
						suc += vaFund.getTicker() + " ";
						break;
					}
				}
				if (!found)
				{
					fail += vaFund.getTicker() + " ";
					vaFund.setAssetName("NoResult");
					vaFund.setFundName("NoResult");
					vaFund.setMemo("n");
				}
			}
			mesg = suc + "\n" + fail + "\n";
			if (toBeModified.size() > 0)
				securityManager.saveOrUpdateVAFunds(toBeModified);
		}
		mesg += "check finish\n";
		return mesg;
	}

	private void processVAFunds()
	{
		if (vaFundList != null)
		{
			Iterator<VAFund> vIter = vaFundList.iterator();
			while (vIter.hasNext())
			{
				VAFund v = vIter.next();
				if (v.getAssetName() == null || v.getAssetName().equals(""))
				{
					vIter.remove();
					continue;
				}
				if (v.getFundName() == null || v.getFundName().equals(""))
				{
					vIter.remove();
					continue;
				}
				if (v.getTicker() == null || v.getTicker().equals(""))
				{
					vIter.remove();
					continue;
				}
			}
		}
	}

	public String searchvafundtable() throws IOException
	{
		vaFundList = new ArrayList<VAFund>();
		if (searchType.equals("A"))
		{
			VAFund vaFund = VAFundUtil.BarronNameSearch(barronName);
			if (vaFund != null)
				vaFundList.add(vaFund);
		} else if (searchType.equals("B"))
		{
			fullName = VAFundUtil.getFullName(barronName);
			try
			{
				vaFundList = VAFundUtil.fullNameSearch(fullName, 20);
			} catch (Exception e)
			{
			}
		} else if (searchType.equals("C"))
		{
			try
			{
				fullName = VAFundUtil.getFullName(barronName);
				if (searchCategory == 1)
					vaFundList = VAFundUtil.MSFullNameSearchHasTicker(fullName, 20);
				else
					vaFundList = VAFundUtil.MSFullNameSearch(fullName, 20);
			} catch (Exception e)
			{
			}
		}
		return Action.SUCCESS;
	}

	/**
	 * 获得vafundtable的数据
	 * 
	 * @return
	 */
	public String getvafundtablelist()
	{
		SecurityManager securityManager = (SecurityManager) ContextHolder.getSecurityManager();
		companyFundList = securityManager.getCompanyFundListByCompanyAndCategorys(company, category1, category2, category3);
		return Action.SUCCESS;
	}

	public String getcategorylist()
	{
		SecurityManager securityManager = (SecurityManager) ContextHolder.getSecurityManager();
		companyList = securityManager.getCompanyNameList();
		categoryList = securityManager.getAssetNameListByCompanyName(company);
		if (categoryList != null && categoryList.size() > 0)
		{
			message = categoryList.get(0);
			for (int i = 1; i < categoryList.size(); ++i)
				message += "#" + categoryList.get(i);
		}
		return Action.MESSAGE;
	}

	/**
	 * fund table页面，包括：fund table 及各种parse工具
	 * 
	 * @throws Exception
	 */
	public String fundtable() throws Exception
	{
		MYPLANIQPermissionManager mpm = new MYPLANIQPermissionManager();
		StrategyManager strategyManager = ContextHolder.getStrategyManager();

		SecurityManager securityManager = (SecurityManager) ContextHolder.getSecurityManager();

		UserManager userManager = ContextHolder.getUserManager();
		user = ContextHolder.getUserManager().getLoginUser();
		userID = user.getID();
		if (userID == Configuration.USER_ANONYMOUS)
		{
			message = "Pleast login in first.";
			return Action.MESSAGE;
		}
		plan = strategyManager.get(ID);
		if (plan == null || !plan.is401K())
		{
			message = "The plan does not exist.";
			return Action.MESSAGE;
		}
		isOwner = mpm.hasUserPlanCreate(userID, ID);
		if (!isOwner && !userManager.HaveRole(Configuration.ROLE_STRATEGY_READ, userID, ID, Configuration.RESOURCE_TYPE_STRATEGY))
		{
			message = "You are trying to access a private plan. Please retrun to the previous page or the home page. Further questions, please contact support@myplaniq.com . Thank you for your suggestions.";
			return Action.MESSAGE;
		}
		int operationCode = mpm.canPlanFundTable(userID, ID);
		if (operationCode == mpm.COUNT_LIMIT)
		{
			String url = "<a href='http://localhost/LTISystem/f401k_view.action?ID=";
			String urls = "";
			int curPlanFundTableNum = mpm.getCurPlanFundTableNum(userID);
			List<Long> planIDList = mpm.getCurPlanFundTableIDList(userID);
			message = "You have already opened " + curPlanFundTableNum + " plans' fundtables. please send e-mail to support@myplaniq.com to request to increase your quota.<br>";
			for (int i = 0; i < planIDList.size(); i++)
			{
				Long planID = planIDList.get(i);
				String planName = strategyManager.get(planID).getName();
				urls = urls + url + planID + "'>" + planName + "</a><br>";
			}
			message += "You are only able to open those fundtables which you opened before.<br>" + urls;
			return Action.MESSAGE;
		}
		PublicMaker publicMaker = new PublicMaker(plan);

		isPublic = publicMaker.isPublic();
		// 成功允许访问
		variables = plan.getVariablesFor401k();
		planType = plan.getPlanType();
		status = plan.getStatus();
		ticker = plan.getTicker();
		List<Security> listSecurity = new ArrayList<Security>();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		for (VariableFor401k variable : variables)
		{
			if (variable.getSymbol().equalsIgnoreCase("unknow"))
				continue;
			Security se = (Security) securityManager.getBySymbol(variable.getSymbol());
			if (se != null && se.getStartDate() != null)
				variable.setStartDate(df.format(se.getStartDate()));
			if (se != null && se.getEndDate() != null)
				variable.setEndDate(df.format(se.getEndDate()));
			try
			{
				listSecurity.add(se);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		File planFile = new File(Configuration.get11KDir() + "/" + "PlanFile/" + ID + ".htm");
		if (planFile.exists())
		{
			planName = planFile.getName();
		}

		File data = new File(Configuration.get11KDir() + "/" + "DataFile/" + ID + ".csv");
		if (data.exists())
		{
			try
			{
				dataContent = FileOperator.getContent(data.getAbsolutePath());
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		File file = new File(Configuration.getFundTable() + "/" + plan.getName() + "/");
		String[] fileName = file.list();
		if (fileName != null)
		{
			fileList = Arrays.asList(fileName);
		}
		mpm.afterPlanFundTable(userID, ID);
		int curPlanFundTableNum = mpm.getCurPlanFundTableNum(userID);
		int allowPlanFundTableNum = mpm.getAllowPlanFundTableNum(userID);
		message = "You have already opened " + curPlanFundTableNum + " plans' fundtables. You can open " + allowPlanFundTableNum + " more plans' funtables";

		return Action.SUCCESS;
	}

	/**
	 * fund table页面，包括：fund table 及各种parse工具
	 * 
	 * @param plan
	 * @param user
	 * @param message
	 * @param isAdmin
	 * @param isOwner
	 * @param variables
	 * @param fileList
	 */
	public String fundtable_old()
	{

		StrategyManager strategyManager = ContextHolder.getStrategyManager();
		UserManager userManager = ContextHolder.getUserManager();
		plan = strategyManager.get(ID);
		user = userManager.getLoginUser();
		Long userID = user.getID();
		if (plan == null || !plan.is401K())
		{
			message = "The plan does not exist.";
			return Action.MESSAGE;
		}
		if (isAdmin || (plan.getUserID() != null && plan.getUserID().equals(userID)))
		{
			isOwner = true;
		} else
		{
			isOwner = false;
		}
		boolean read = true;
		if (!isOwner)
		{
			read = false;
			read = userManager.HaveRole(Configuration.ROLE_STRATEGY_READ, userID, ID, Configuration.RESOURCE_TYPE_STRATEGY);
		}

		if (!read)
		{
			message = "You are trying to access a private plan. Please retrun to the previous page or the home page. Further questions, please contact support@myplaniq.com . Thank you for your suggestions.";
			return Action.MESSAGE;
		}

		boolean open = true;
		boolean hasplan = false;
		int planIDNum = 0;

		PublicMaker publicMaker = new PublicMaker(plan);

		isPublic = publicMaker.isPublic();

		if (userID != Configuration.SUPER_USER_ID && !isOwner)
		{
			open = false;
			open = userManager.HaveRole(Configuration.ROLE_FUNDTABLE_OPEN10, userID, ID, Configuration.RESOURCE_TYPE_ROLE);
			if (!open)
			{
				variables = plan.getVariablesFor401k();
				return Action.SUCCESS;
			} else
			{
				hasplan = userManager.hasPlan(userID, ID);
				if (hasplan)
				{
					variables = plan.getVariablesFor401k();
					return Action.SUCCESS;
				} else
				{
					planIDNum = userManager.numUserFundTableID(userID);
					if (planIDNum + 1 <= Configuration.FUNTABLE_OPEN10)
					{
						userManager.saveUserFundTableID(userID, ID);
						variables = plan.getVariablesFor401k();
						return Action.SUCCESS;
					} else
					{
						List<Long> planIDs = userManager.findUserPlanID(userID);
						String url = "<a href='http://localhost/LTISystem/f401k_view.action?ID=";
						String urls = "";
						message = "You have already opened 30 plans' fundtables. please send e-mail to support@myplaniq.com to request to increase your quota.<br>";
						for (int i = 0; i < planIDs.size(); i++)
						{
							Long planID = planIDs.get(i);
							String planName = strategyManager.get(planID).getName();
							urls = urls + url + planID + "'>" + planName + "</a><br>";
						}
						message = message + "You are only able to open those fundtables which you opened before.<br>" + urls;
						return Action.MESSAGE;
					}
				}
			}
		}
		variables = plan.getVariablesFor401k();
		planType = plan.getPlanType();
		status = plan.getStatus();
		ticker = plan.getTicker();

		File planFile = new File(Configuration.get11KDir() + "/" + "PlanFile/" + ID + ".htm");
		if (planFile.exists())
		{
			planName = planFile.getName();
		}

		File data = new File(Configuration.get11KDir() + "/" + "DataFile/" + ID + ".csv");
		if (data.exists())
		{
			try
			{
				dataContent = FileOperator.getContent(data.getAbsolutePath());
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		File file = new File(Configuration.getFundTable() + "/" + plan.getName() + "/");
		String[] fileName = file.list();
		if (fileName != null)
		{
			fileList = Arrays.asList(fileName);
		}
		
		//fundtableMap
		fundtableMap = strategyManager.getSixMajorAssetClassForPlan(plan.getID());
		return Action.SUCCESS;
	}

	/**
	 * 创建fund table index 的索引（for VariableFor401k）
	 * 
	 * @return
	 */
	public String createfundtableindex()
	{
		List<Long> planIDList = new ArrayList<Long>();
		if (planIDArray != null && !planIDArray.equals(""))
		{
			String[] planIDs = planIDArray.split(",");
			for (String planID : planIDs)
			{
				planIDList.add(Long.parseLong(planID));
			}
		}
		try
		{
			FundTableCachingUtil.initialize(planIDList);
			message = "create fund table index successfully";
		} catch (Exception e)
		{
			message = "create fund table index fail";
		}
		return Action.MESSAGE;
	}

	public String createvafundtableindex()
	{
		try
		{
			VAFundUtil.initialize();
			message = "create fund table index successfully";
		} catch (Exception e)
		{
			message = "create fund table index fail";
		}
		return Action.MESSAGE;
	}

	/**
	 * 添加fund table index
	 * 
	 * @return
	 */
	public String addfundtableindex()
	{
		List<Long> planIDList = new ArrayList<Long>();
		if (planIDArray != null && !planIDArray.equals(""))
		{
			String[] planIDs = planIDArray.split(",");
			for (String planID : planIDs)
			{
				planIDList.add(Long.parseLong(planID));
			}
		}
		try
		{
			FundTableCachingUtil.addIndexByPlanID(planIDList);
			message = "add fund table index by plan id listsuccessfully";
		} catch (Exception e)
		{
			message = "add fund table index by plan id fail";
		}
		return Action.MESSAGE;
	}

	public String deletefundtableindex()
	{
		List<Long> planIDList = new ArrayList<Long>();
		if (planIDArray != null && !planIDArray.equals(""))
		{
			String[] planIDs = planIDArray.split(",");
			for (String planID : planIDs)
			{
				planIDList.add(Long.parseLong(planID));
			}
		}
		try
		{
			FundTableCachingUtil.deleteIndexByPlanID(planIDList);
			message = "delete fund table index by plan id list successfully";
		} catch (Exception e)
		{
			message = "delete fund table index by plan id list fail";
		}
		return Action.MESSAGE;
	}

	public String checksecurity()
	{
		LTIDownLoader ltiDownLoader = new LTIDownLoader();
		StrategyManager strategyManager = (StrategyManager) ContextHolder.getStrategyManager();
		AssetClassManager assetClassManager = (AssetClassManager) ContextHolder.getAssetClassManager();
		SecurityManager securityManager = (SecurityManager) ContextHolder.getSecurityManager();
		plan = strategyManager.get(ID);
		variables = plan.getVariablesFor401k();
		Set<String> symbolSet = new HashSet<String>();
		List<VariableFor401k> variableList = new ArrayList<VariableFor401k>();
		String suc = "successful:\n";
		String fail = "fail:\n";
		List<VariableFor401k> toBeModified = new ArrayList<VariableFor401k>();
		if (variables != null && variables.size() > 0)
		{
			for (VariableFor401k variable : variables)
			{
				if (variable.getSymbol().equalsIgnoreCase("unknow"))
					continue;
				Security se = securityManager.getBySymbol(variable.getSymbol());
				if (se == null)
				{
					symbolSet.add(variable.getSymbol());
					toBeModified.add(variable);
				}
			}
			List<Security> securityList = ltiDownLoader.batchImportSecurity(symbolSet);
			for (VariableFor401k variable : toBeModified)
			{
				boolean found = false;
				for (Security se : securityList)
				{
					if (variable.getSymbol().equalsIgnoreCase(se.getSymbol()))
					{
						String assetName = assetClassManager.get(se.getClassID()).getName();
						variable.setAssetClassName(assetName);
						variable.setName(se.getName());
						variable.setMemo("");
						found = true;
						suc += variable.getSymbol() + " ";
						break;
					}
				}
				if (!found)
				{
					fail += variable.getSymbol() + " ";
					variable.setAssetClassName("unknow");
					variable.setName("unknow");
					variable.setSymbol("unknow");
					variable.setMemo("n");
				}
			}
			message = suc + "\n" + fail + "\n";
			if (toBeModified.size() > 0)
				strategyManager.updateVariableFor401k(toBeModified);
		}
		message += "check finish\n";
		return Action.MESSAGE;
	}

	// ---------------------------------------------------------
	private String symbolsStr = new String();

	public String getSymbolsStr()
	{
		return symbolsStr;
	}

	public void setSymbolsStr(String symbolsStr)
	{
		this.symbolsStr = symbolsStr;
	}

	public String checkSymbol()
	{
		LTIDownLoader ltiDownLoader = new LTIDownLoader();
		AssetClassManager assetClassManager = (AssetClassManager) ContextHolder.getAssetClassManager();
		Set<String> symbolSet = new HashSet<String>();
		String successSymbol = "";
		String failSymbol = "";
		String[] symbolArray = symbolsStr.split("\\|");
		for (int i = 0; i < symbolArray.length - 1; i++)
		{
			symbolSet.add(symbolArray[i]);
		}
		List<Security> securityList = ltiDownLoader.batchImportSecurity(symbolSet);
		for (int i = 0; i < symbolArray.length; i++)
		{
			if (securityList.size() != 0)
			{
				boolean found = false;
				for (Security se : securityList)
				{
					if (symbolArray[i].equalsIgnoreCase(se.getSymbol()))
					{
						if (successSymbol == "")
						{
							successSymbol = assetClassManager.get(se.getClassID()).getName() + "*" + "/t";
						} else
						{
							successSymbol += assetClassManager.get(se.getClassID()).getName() + "*" + "/t";
						}
						successSymbol += se.getSymbol() + "/t";
						successSymbol += se.getName() + "/t";
						successSymbol += 3 + "/t";
						successSymbol += se.getName() + "/t";
						successSymbol += " " + "/n";
						found = true;
					}
				}
				if (!found)
				{
					if (failSymbol == "")
					{
						failSymbol = symbolArray[i] + "|";
					} else
					{
						failSymbol += symbolArray[i] + "|";
					}
				}
			} else
			{
				if (failSymbol == "")
				{
					failSymbol = symbolArray[i] + "|";
				} else
				{
					failSymbol += symbolArray[i] + "|";
				}
			}
		}
		if (successSymbol == "")
		{
			message = "" + "#" + failSymbol;
		} else if (failSymbol == "")
		{
			message = successSymbol + "#" + "";
		} else
		{
			message = successSymbol + "#" + failSymbol;
		}
		return Action.MESSAGE;
	}

	private String[] arraySymbol;

	public String[] getArraySymbol()
	{
		return arraySymbol;
	}

	public void setArraySymbol(String[] arraySymbol)
	{
		this.arraySymbol = arraySymbol;
	}

	public String getBenchMarks()
	{
		arraySymbol = symbolsStr.trim().split("\\|");
		return Action.SUCCESS;
	}

	public String getBenchMarksByAssertClass()
	{
		AssetClassManager assetClassManager = (AssetClassManager) ContextHolder.getAssetClassManager();
		SecurityManager securityManager = (SecurityManager) ContextHolder.getSecurityManager();
		String assetClassIDs[];
		assetClassIDs = symbolsStr.trim().split("\\.");
		Long assetClassID = Long.parseLong(assetClassIDs[0]);
		if (assetClassID == -1)
		{
			if (message == "" || message == null)
			{
				message = "NotFound" + "|";
			} else
			{
				message += "NotFound" + "|";
			}
			message += "NotFound" + "|";
			message += "NotFound" + "|";
			message += 3 + "|";
			message += "NotFound" + "|";
			message += "n" + "|";
		} else
		{
			AssetClass ac = assetClassManager.get(assetClassID);
			Security se = securityManager.get(ac.getBenchmarkID());
			if (message == "" || message == null)
			{
				message = ac.getName() + "|";
			} else
			{
				message += ac.getName() + "|";
			}
			message += se.getSymbol() + "|";
			message += se.getName() + "|";
			message += 3 + "|";
			message += se.getName() + "|";
			message += "p" + "|";
		}
		return Action.MESSAGE;
	}

	public String getUserPortfolios()
	{
		UserManager userManager = ContextHolder.getUserManager();
		PortfolioManager pm = (PortfolioManager) ContextHolder.getInstance().getApplicationContext().getBean("portfolioManager");
		MYPLANIQPermissionManager mpm = new MYPLANIQPermissionManager();
		PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();
		message = "";
		Date date = new Date();
		List<Long> followIDList = userManager.getResourceIDListByUserIDAndResourceType(userID, Configuration.USER_RESOURCE_PORTFOLIO_FOLLOW);
		List<Long> portfolioList = new ArrayList<Long>();
		if (followIDList != null && followIDList.size() > 0)
		{
			for (Long portID : followIDList)
			{
				Portfolio portfolio = new Portfolio();
				portfolio = pm.get(portID);
				if (portfolio != null)
				{
					// if(portfolio != null &&
					// !portfolio.isPlanPortfolio()){//判断非TAA/SAA
					// boolean iscustomize =
					// mpm.hasUserPortfolioCustomize(userID,
					// portfolio);
					boolean type = portfolio.isType(Configuration.PORTFOLIO_TYPE_COMPOUND);
					if (!type)
					{
						portfolioList.add(portID);
					}
				}
			}
		}
		Date monthBefore = LTIDate.getNDaysAgo(date, 30);
		for (int i = 0; i < portfolioList.size(); i++)
		{
			pm.getLatestDate(portfolioList.get(i));
			if (pm.getLatestDate(portfolioList.get(i)) == null)
			{
				portfolioList.remove(i);
			}
			if (LTIDate.before(pm.getLatestDate(portfolioList.get(i)), monthBefore))
			{
				portfolioList.remove(i);
			}
			if (pm.get(portfolioList.get(i)).getStartingDate() == pm.get(portfolioList.get(i)).getEndDate())
			{
				portfolioList.remove(i);
			}

		}
		if (portfolioList.size() > 0)
		{
			for (int i = 0; i < portfolioList.size(); i++)
			{
				message = message + portfolioManager.get(portfolioList.get(i)).getName() + "|";
			}
		} else
		{
			message = "";
		}
		return Action.MESSAGE;
	}

	public String checkPortfolioName()
	{
		PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();
		message = "true";
		name = StringUtil.getValidName2(name).trim();
		Portfolio temp = portfolioManager.get(name);
		if (temp != null)
			message = "false";
		return Action.MESSAGE;
	}

	public String checkCreatePortfolio()
	{
		message = "true";
		// MYPLANIQPermissionManager mpm = new MYPLANIQPermissionManager();
		UserManager userManager = ContextHolder.getUserManager();
		if (!isAdmin)
		{
			UserPermission userPermission = userManager.getUserPermissionByUserID(userID);
			if (userPermission == null || userPermission.getCurPortfolioFollowNum() >= userPermission.getMaxPortfolioFollowNum())
			{
				message = "You need to subscribe as a Expert User or a Pro User to get the permission of creating portfolios.";
			}
		}
		return Action.MESSAGE;
	}

	private String pfNames;

	public String getPfNames()
	{
		return pfNames;
	}

	public void setPfNames(String pfNames)
	{
		this.pfNames = pfNames;
	}

	private String amounts;

	public String getAmounts()
	{
		return amounts;
	}

	public void setAmounts(String amounts)
	{
		this.amounts = amounts;
	}

	public String createPortfolio() throws Exception
	{
		UserManager userManager = ContextHolder.getUserManager();
		SecurityManager securityManager = ContextHolder.getSecurityManager();
		PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();
		MYPLANIQPermissionManager mpm = new MYPLANIQPermissionManager();
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");

		Portfolio old = portfolioManager.get(33041L);
		Portfolio portfolio = old.clone();

		PortfolioPermissionChecker pc = new PortfolioPermissionChecker(old, ServletActionContext.getRequest());
		int operationCode = mpm.canPortfolioCustomize(userID, old, null);
		if (operationCode == mpm.PERMISSION_ADV_LIMIT)
		{
			if (pc.isAnonymous())
				message = "You need to subscribe to follow the Advanced portfolios. Please login or register first.";
			else
				message = "You need to subscribe as Expert User or Professional User to follow the advanced portfolios. Please subscribe for two weeks trial.";
			return Action.MESSAGE;
		} else if (operationCode == mpm.COUNT_LIMIT)
		{
			message = "You have already customized/followed " + mpm.getMaxPortfolioFollowNum(userID) + " portfolio(s)";
			return Action.MESSAGE;
		}

		portfolio.setName(name);
		portfolio.setUserID(userID);
		portfolio.setCash(0);
		portfolio.setUserName(userManager.get(userID).getUserName());
		portfolio.setType(Configuration.PORTFOLIO_TYPE_COMPOUND);
		portfolio.setMainStrategyID(null);

		StrategyInf sinf = new StrategyInf();
		sinf.setAssetAllocationStrategy(new StrategyBasicInf());
		sinf.setCashFlowStrategy(new StrategyBasicInf());
		sinf.setRebalancingStrategy(new StrategyBasicInf());
		portfolio.setStrategies(sinf);

		String[] pfNamesArray = pfNames.split("\\|");
		Date startDate = df.parse(startingdate);
		for (int k = 0; k < pfNamesArray.length; k++)
		{
			Date beginDate = portfolioManager.get(pfNamesArray[k].trim()).getStartingDate();
			if (!LTIDate.after(startDate, beginDate))
			{
				startDate = beginDate;
			}
		}

		while (!LTIDate.isNYSEHoliday(startDate))
		{
			startDate = LTIDate.getNDaysAgo(startDate, -1);
		}
		startDate = LTIDate.getNDaysAgo(startDate, -1);
		portfolio.setStartingDate(startDate);

		PortfolioInf pif = new PortfolioInf();

		portfolioManager.save(portfolio);
		ID = portfolio.getID();

		String[] amountArray = amounts.split("\\|");
		HoldingInf nhi = new HoldingInf(ID, 0, portfolio.getStartingDate());

		Asset asset = new Asset();
		asset.setName("CoumpoundAsset");
		List<HoldingItem> his = new ArrayList<HoldingItem>();

		for (int i = 0; i < pfNamesArray.length; i++)
		{
			HoldingItem hi = new HoldingItem();
			long portfolioId = portfolioManager.get(pfNamesArray[i].trim()).getID();
			Security port = securityManager.get("P_" + portfolioId);
			Date portStartDate = startDate;
			while (portfolioManager.getPortfolioAmountByDate(portfolioId, df1.format(portStartDate)) == null)
			{
				portStartDate = LTIDate.getNDaysAgo(portStartDate, -1);
			}
			// System.out.println(portStartDate);
			// System.out.println(
			// portfolioManager.getPortfolioAmountByDate(portfolioId, df1
			// .format(portStartDate)));
			hi.setShare(Double.parseDouble(amountArray[i]) / portfolioManager.getPortfolioAmountByDate(portfolioId, df1.format(portStartDate)));
			hi.setSecurityID(port.getID());
			hi.setSymbol(port.getSymbol());
			hi.setDescription(port.getName());

			his.add(hi);
		}
		asset.setHoldingItems(his);
		nhi.addAsset(asset);
		nhi.setPortfolioID(ID);
		nhi.setPortfolioName(portfolio.getName());
		nhi.setCurrentDate(portfolio.getStartingDate());

		pif.setHolding(nhi);
		pif.setPortfolioID(ID);
		pif.setType(Configuration.PORTFOLIO_HOLDING_ORIGINAL);
		portfolioManager.savePortfolioInf(pif);

		mpm.afterPortfolioCustomize(userID, ID, null);

		message = Long.toString(ID);

		// Security s = new Security();
		// s.getAssetClass().getFullName();
		return Action.MESSAGE;
	}

//	public static void main(String[] args)
//	{
//		// PortfolioManager portfolioManager =
//		// ContextHolder.getPortfolioManager();
//		//
//		// Double d = portfolioManager.getPortfolioAmountByDate(32326,
//		// "2003-03-04");
//		// System.out.println(d);
//
//		// System.out.println(LTIDate.isNYSETradingDay(LTIDate.getDate(2001, 01,
//		// 02)));
//	}

	/*-------------------------------------------------------*/
	/**
	 * ？？？创建plan？？
	 * 
	 * @param plan
	 * @param ID
	 * @param isAdmin
	 * @param isOwner
	 * @param message
	 * @param variables
	 */
	public String createplan()
	{
		StrategyManager strategyManager = ContextHolder.getStrategyManager();
		plan = strategyManager.get(ID);
		if (plan == null || !plan.getType().equals(Configuration.STRATEGY_TYPE_401K))
		{
			message = "The plan does not exist.";
			return Action.MESSAGE;
		}
		if (isAdmin || (plan.getUserID() != null && plan.getUserID().equals(userID)))
		{
			isOwner = true;
		} else
		{
			isOwner = false;
		}
		boolean read = true;
		if (!isOwner)
		{
			read = false;
			read = ContextHolder.getUserManager().HaveRole(Configuration.ROLE_STRATEGY_READ, userID, ID, Configuration.RESOURCE_TYPE_STRATEGY);
		}

		if (!read)
		{
			message = "You are trying to access a private plan. Please retrun to the previous page or the home page. Further questions, please contact support@myplaniq.com . Thank you for your suggestions.";
			return Action.MESSAGE;
		}
		variables = plan.getVariablesFor401k();
		return Action.SUCCESS;
	}

	/*-------------------------------------------------------*/
	private Profile profile;
	private User user;
	private String fileName;
	private String filecontent;

	/**
	 * 获取用户信息及其的profile信息
	 * 
	 * @param profiel
	 * @param user
	 */
	public String myaccount()
	{
		profile = ((ProfileManager) ContextHolder.getInstance().getApplicationContext().getBean("profileManager")).get(0l, userID, 0l);
		user = ContextHolder.getUserManager().getLoginUser();
		user.setPassword("");
		return Action.SUCCESS;
	}

	/**
	 * 备份fund table到html
	 * 
	 * @param plan
	 */
	public void fundBackup() throws Exception
	{
		StrategyManager strategyManager = ContextHolder.getStrategyManager();
		plan = strategyManager.get(ID);

		File root = new File(Configuration.getFundTable(), plan.getName());
		if (!root.exists())
		{
			root.mkdirs();
		}

		Date date = new Date();
		DateFormat df = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
		File fundBkFile = new File(root, df.format(date) + ".htm");
		if (!fundBkFile.exists())
		{
			fundBkFile.createNewFile();
		}
		List<VariableFor401k> variables = plan.getVariablesFor401k();
		freemarker.template.Configuration conf = new freemarker.template.Configuration();
		conf.setDirectoryForTemplateLoading(new File(ContextHolder.getServletPath()));
		freemarker.template.Template itemplate = null;
		itemplate = conf.getTemplate("f401kaction_fund.ftl");

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("variables", variables);

		BufferedWriter fr = new BufferedWriter(new FileWriter(fundBkFile));
		itemplate.process(data, fr);
		fr.close();
	}

	/*-------------------------------------------------------*/

	/**
	 * 查看文件内容
	 * 
	 * @param plan
	 * @param filecontent
	 */
	public String filecontent() throws Exception
	{

		StrategyManager strategyManager = ContextHolder.getStrategyManager();
		plan = strategyManager.get(ID);

		File file = new File(Configuration.getFundTable() + "/" + plan.getName() + "/" + fileName);
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line = br.readLine();
		StringBuffer sb = new StringBuffer();
		while (line != null)
		{
			sb.append(line);
			sb.append("\r\n");
			line = br.readLine();
		}
		br.close();

		filecontent = sb.toString();

		return Action.SUCCESS;

	}

	/*-------------------------------------------------------*/
	private int PID;
	private JforumTopics jforumTopic;

	public String news()
	{
		JforumManager jforumManager = (JforumManager) ContextHolder.getJforumManager();
		jforumTopic = jforumManager.getTopicByTopicId(PID);

		if (this.startIndex == null)
			this.startIndex = 0;
		if (this.pageSize == null)
			this.pageSize = Configuration.DefaultPageSize;

		int forumId1 = jforumManager.getForumId(Configuration.JFORUM_Announcements);
		int forumId2 = jforumManager.getForumId(Configuration.JFORUM_FORUMS);

		announcementsList = jforumManager.getTopicsByForumIDDesc(forumId1);
		jTopicsOfNewsAndCommentaries = jforumManager.getAllTopicsByForumIdDesc(forumId2, pageSize, startIndex, Configuration.TOPICS_NUMBER);

		return Action.SUCCESS;
	}

	/*-------------------------------------------------------*/
	private Integer startIndex;
	private Integer pageSize;
	private com.lti.type.PaginationSupport jTopics;

	public String blogs()
	{

		if (this.startIndex == null)
			this.startIndex = 0;
		if (this.pageSize == null)
			this.pageSize = Configuration.DefaultPageSize;

		JforumManager jforumManager = (JforumManager) ContextHolder.getJforumManager();
		int forumId = jforumManager.getForumId(Configuration.JFORUM_FORUMS);
		jTopics = jforumManager.getAllTopicsByForumIdDesc(forumId, pageSize, startIndex, Configuration.TOPICS_NUMBER);

		return Action.SUCCESS;
	}

	/*-------------------------------------------------------*/
	private com.lti.type.PaginationSupport jTopicsOfAnnouncements;
	private com.lti.type.PaginationSupport jTopicsOfNewsAndCommentaries;
	private com.lti.type.PaginationSupport jTopicsOfOne;

	public com.lti.type.PaginationSupport getjTopicsOfOne()
	{
		return jTopicsOfOne;
	}

	public void setjTopicsOfOne(com.lti.type.PaginationSupport jTopicsOfOne)
	{
		this.jTopicsOfOne = jTopicsOfOne;
	}

	private com.lti.type.PaginationSupport jTopicsOfRetirement;
	private List<JforumTopics> announcementsList;

	public String blog()
	{

		if (this.startIndex == null)
			this.startIndex = 0;
		if (this.pageSize == null)
			this.pageSize = Configuration.DefaultPageSize;

		JforumManager jforumManager = (JforumManager) ContextHolder.getJforumManager();
		int forumId1 = jforumManager.getForumId(Configuration.JFORUM_Announcements);
		int forumId2 = jforumManager.getForumId(Configuration.JFORUM_FORUMS);
		int forumId3 = jforumManager.getForumId(Configuration.JFORUM_Retirement);
		announcementsList = jforumManager.getTopicsByForumIDDesc(forumId1);
		jTopicsOfNewsAndCommentaries = jforumManager.getAllTopicsByForumIdDesc(forumId2, pageSize, startIndex, Configuration.TOPICS_NUMBER);
		jTopicsOfRetirement = jforumManager.getAllTopicsByForumIdDesc(forumId3, 3, startIndex, Configuration.TOPICS_NUMBER);
		return Action.SUCCESS;
	}

	/*-------------------------------------------------------*/
	private String formusCategories = null;
	private boolean isMain = false;
	private boolean isAnnouncements = false;
	private String setPageSize = "3";
	private boolean isprePage = false;
	private String formuslist = "";

	public String getFormuslist()
	{
		return formuslist;
	}

	public void setFormuslist(String formuslist)
	{
		this.formuslist = formuslist;
	}

	private String setHowCategories = "1";

	public String getSetHowCategories()
	{
		return setHowCategories;
	}

	public void setSetHowCategories(String setHowCategories)
	{
		this.setHowCategories = setHowCategories;
	}

	public boolean getIsprePage()
	{
		return isprePage;
	}

	public void setIsprePage(boolean isprePage)
	{
		this.isprePage = isprePage;
	}

	public String getFormusCategories()
	{
		return formusCategories;
	}

	public void setFormusCategories(String formusCategories)
	{
		this.formusCategories = formusCategories;
	}

	public boolean getIsMain()
	{
		return isMain;
	}

	public void setIsMain(boolean isMain)
	{
		this.isMain = isMain;
	}

	public boolean getIsAnnouncements()
	{
		return isAnnouncements;
	}

	public void setIsAnnouncements(boolean isAnnouncements)
	{
		this.isAnnouncements = isAnnouncements;
	}

	public String getSetPageSize()
	{
		return setPageSize;
	}

	public void setSetPageSize(String setPageSize)
	{
		this.setPageSize = setPageSize;
	}

	public String allblog()
	{
		System.out.print(startIndex);
		if (this.startIndex == null)
			this.startIndex = 0;
		if (this.pageSize == null)
			this.pageSize = Configuration.DefaultPageSize;

		JforumManager jforumManager = (JforumManager) ContextHolder.getJforumManager();
		int forumId1 = jforumManager.getForumId(Configuration.JFORUM_Announcements);
		announcementsList = jforumManager.getTopicsByForumIDDesc(forumId1);
		if (setHowCategories != "1" && !formuslist.equals(""))
		{
			String[] categroyList = new String[Integer.valueOf(setHowCategories)];
			categroyList = formuslist.split("\\|");
			Integer[] forumIdList = new Integer[Integer.valueOf(setHowCategories)];
			for (int count = 0; count < Integer.valueOf(setHowCategories); count++)
			{
				forumIdList[count] = jforumManager.getForumId(categroyList[count]);
			}

			if (isprePage == true)
			{
				jTopicsOfOne = jforumManager.getAllTopicsListByForumIdDesc(forumIdList, Integer.valueOf(setPageSize), startIndex, Configuration.TOPICS_NUMBER);
			} else
			{
				jTopicsOfOne = jforumManager.getAllTopicsListByForumIdDesc(forumIdList, Integer.valueOf(setPageSize), 0, Configuration.TOPICS_NUMBER);
			}
		} else if (formusCategories != null)
		{
			int forumId4 = jforumManager.getForumId(formusCategories);
			if (isprePage == true)
			{
				jTopicsOfOne = jforumManager.getAllTopicsByForumIdDesc(forumId4, Integer.valueOf(setPageSize), startIndex, Configuration.TOPICS_NUMBER);
			} else
			{
				jTopicsOfOne = jforumManager.getAllTopicsByForumIdDesc(forumId4, Integer.valueOf(setPageSize), 0, Configuration.TOPICS_NUMBER);
			}
		}

		return Action.SUCCESS;
	}

	/*-------------------------------------------------------*/
	private String tagCategory;

	public String getTagCategory()
	{
		return tagCategory;
	}

	public void setTagCategory(String tagCategory)
	{
		this.tagCategory = tagCategory;
	}

	public String blogtest()
	{

		if (this.startIndex == null)
			this.startIndex = 0;
		if (this.pageSize == null)
			this.pageSize = Configuration.DefaultPageSize;

		JforumManager jforumManager = (JforumManager) ContextHolder.getJforumManager();
		int forumId1 = jforumManager.getForumId(Configuration.JFORUM_Announcements);
		int forumId2 = jforumManager.getForumId(Configuration.JFORUM_FORUMS);
		int forumId3 = jforumManager.getForumId(Configuration.JFORUM_Retirement);
		announcementsList = jforumManager.getTopicsByForumIDDesc(forumId1);
		jTopicsOfNewsAndCommentaries = jforumManager.getAllTopicsByForumIdDesc(forumId2, pageSize, startIndex, Configuration.TOPICS_NUMBER);
		jTopicsOfRetirement = jforumManager.getAllTopicsByForumIdDesc(forumId3, 3, startIndex, Configuration.TOPICS_NUMBER);
		return Action.SUCCESS;
	}

	/*-------------------------------------------------------*/
	private List<JforumTopics> jTList;

	public String announcementsAndNews2()
	{

		if (this.startIndex == null)
			this.startIndex = 0;
		if (this.pageSize == null)
			this.pageSize = Configuration.DefaultPageSize;

		JforumManager jforumManager = (JforumManager) ContextHolder.getJforumManager();
		int forumId1 = jforumManager.getForumId(Configuration.JFORUM_Announcements);
		int forumId2 = jforumManager.getForumId(Configuration.JFORUM_FORUMS);

		jTList = jforumManager.getTopicsByForumIDDesc(forumId1);
		// jTopicsOfAnnouncements =
		// jforumManager.getAllTopicsByForumIdDesc(forumId1, pageSize,
		// startIndex, Configuration.TOPICS_NUMBER);
		jTopicsOfNewsAndCommentaries = jforumManager.getAllTopicsByForumIdDesc(forumId2, pageSize, startIndex, Configuration.TOPICS_NUMBER);

		return Action.SUCCESS;
	}

	/*-------------------------------------------------------*/
	public String announcementsAndNews()
	{

		if (this.startIndex == null)
			this.startIndex = 0;
		if (this.pageSize == null)
			this.pageSize = Configuration.DefaultPageSize;

		JforumManager jforumManager = (JforumManager) ContextHolder.getJforumManager();
		int forumId1 = jforumManager.getForumId(Configuration.JFORUM_FORUMS);
		int forumId2 = jforumManager.getForumId(Configuration.JFORUM_Announcements);
		jTopics = jforumManager.getTopicByForumIDDesc(forumId1, forumId2, pageSize, startIndex, Configuration.TOPICS_NUMBER);

		return Action.SUCCESS;
	}

	/*-------------------------------------------------------*/
	private String symbol;
	private com.lti.type.PaginationSupport symbols;
	private List<PlanArticle> articles;
	private StrategyManager strategyManager;
	private Boolean isSymbolList = false;

	public StrategyManager getStrategyManager()
	{
		return strategyManager;
	}

	public void setStrategyManager(StrategyManager strategyManager)
	{
		this.strategyManager = strategyManager;
	}

	public Boolean getIsSymbolList()
	{
		return isSymbolList;
	}

	public void setIsSymbolList(Boolean isSymbolList)
	{
		this.isSymbolList = isSymbolList;
	}

	public String symbols()
	{

		if (this.startIndex == null)
			this.startIndex = 0;
		if (this.pageSize == null)
			this.pageSize = Configuration.DefaultPageSize;
		if (symbol == null)
		{
			symbol = Configuration.DEFAULT_SYMBOL;
		}
		JforumManager jforumManager = (JforumManager) ContextHolder.getJforumManager();
		try
		{
			symbols = jforumManager.getAllTopicsBySymbolDesc(symbol.replace(" ", ""), pageSize, startIndex, Configuration.TOPICS_NUMBER);
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		strategyManager = ContextHolder.getStrategyManager();
		articles = strategyManager.getPlanArticleBySymbol(symbol);
		articles = filesSorter(articles);

		return Action.SUCCESS;
	}

	public static List<PlanArticle> filesSorter(List<PlanArticle> fls)
	{
		Collections.sort(fls, new Comparator<PlanArticle>()
		{
			public int compare(PlanArticle o1, PlanArticle o2)
			{
				int result = o1.getDate().compareTo(o2.getDate());
				if (result < 0)
					return 1;
				else
					return 0;
			}
		});
		return fls;
	}

	/*-------------------------------------------------------*/
	public String announcements()
	{
		if (this.startIndex == null)
			this.startIndex = 0;
		if (this.pageSize == null)
			this.pageSize = 10;

		JforumManager jforumManager = (JforumManager) ContextHolder.getJforumManager();
		int forumId = jforumManager.getForumId(Configuration.JFORUM_Announcements);
		jTopics = jforumManager.getAllTopicsByForumIdDesc(forumId, pageSize, startIndex, Configuration.TOPICS_NUMBER);

		return Action.SUCCESS;
	}

	/*-------------------------------------------------------*/

	public long getForumID()
	{
		return forumID;
	}

	public com.lti.type.PaginationSupport getjTopics()
	{
		return jTopics;
	}

	public void setjTopics(com.lti.type.PaginationSupport jTopics)
	{
		this.jTopics = jTopics;
	}

	public com.lti.type.PaginationSupport getSymbols()
	{
		return symbols;
	}

	public void setSymbols(com.lti.type.PaginationSupport symbols)
	{
		this.symbols = symbols;
	}

	public String getSymbol()
	{
		return symbol;
	}

	public void setSymbol(String symbol)
	{
		this.symbol = symbol;
	}

	public JforumTopics getJforumTopic()
	{
		return jforumTopic;
	}

	public void setJforumTopic(JforumTopics jforumTopic)
	{
		this.jforumTopic = jforumTopic;
	}

	public Integer getStartIndex()
	{
		return startIndex;
	}

	public void setStartIndex(Integer startIndex)
	{
		this.startIndex = startIndex;
	}

	public Integer getPageSize()
	{
		return pageSize;
	}

	public void setPageSize(Integer pageSize)
	{
		this.pageSize = pageSize;
	}

	public int getPID()
	{
		return PID;
	}

	public void setPID(int pID)
	{
		PID = pID;
	}

	public void setForumID(long forumID)
	{
		this.forumID = forumID;
	}

	public String removedata()
	{
		message = "not ok.";
		try
		{
			PersistentUtil.writeGlobalObject("", "TAA_Data_Plan_" + ID, ID, 0l);
			PersistentUtil.writeGlobalObject("", "SAA_Data_Plan_" + ID, ID, 0l);
			PersistentUtil.writeGlobalObject("", "HoldingInfoPlan_" + ID, ID, 0l);
			message = "ok.";
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return Action.MESSAGE;

	}

	/*-------------------------------------------------------*/

	private List<PlanScore> planScoreList;
	private List<PlanScore> planScoreListWithIDs;
	private List<PlanScore> planScoreTopList = new ArrayList<PlanScore>();
	private List<PlanScore> planScoreBottomList = new ArrayList<PlanScore>();
	private String planIDArray = new String();
	private String planNameArray = new String();
	Map<String, CachePortfolioItem> SAAMap = null;
	Map<String, CachePortfolioItem> TAAMap = null;
	String moderateSAANames = new String();
	String moderateTAANames = new String();
	private String endDate;

	private String getRating(int score)
	{
		String rating = "";
		if (score == 0)
			rating = "unrated";
		if (score < 10)
			rating = "poor";
		else if (score < 35)
			rating = "below average";
		else if (score < 65)
			rating = "average";
		else if (score < 85)
			rating = "above average";
		else
			rating = "great";
		return rating;
	}

	public String recalculaterating()
	{
		PlanRankingUtil pru = new PlanRankingUtil();
		try
		{
			pru.calculateOnePlan(ID);
			message = "successful";
		} catch (Exception e)
		{
			message = "fail";
		}
		return Action.MESSAGE;
	}

	public String recalculatefundquality()
	{
		PlanRankingUtil pru = new PlanRankingUtil();
		int fundQualityScore = (int) ((pru.reCalculatePlanFundQuality(ID) + 0.005) * 100);
		message = "Fund Quality : " + String.valueOf(fundQualityScore) + "% " + getRating(fundQualityScore);
		return Action.MESSAGE;
	}

	public String recalculatecoverage()
	{
		PlanRankingUtil pru = new PlanRankingUtil();
		int coverageScore = (int) ((pru.reCalculatePlanCoverage(ID) + 0.005) * 100);
		message = "Diversification : " + String.valueOf(coverageScore) + "% " + getRating(coverageScore);
		return Action.MESSAGE;
	}

	/**
	 * @author WCW
	 * @return
	 * @throws Exception
	 */
	public String gettickerlist() throws Exception
	{
		StrategyManager strategyManager = ContextHolder.getStrategyManager();
		SecurityManager securityManager = ContextHolder.getSecurityManager();
		plan = strategyManager.get(ID);
		PlanScore planScore = strategyManager.getPlanScoreByPlanID(ID);
		String diversfiction = String.valueOf((int) ((planScore.getCoverageScore() + 0.005) * 100)) + "%";
		String fundquality = String.valueOf((int) ((planScore.getFundQualityScore() + 0.005) * 100)) + "%";
		String buliding = String.valueOf((int) ((planScore.getCapabilityScore() + 0.005) * 100)) + "%";
		String overall = String.valueOf((int) ((planScore.getInvestmentScore() + 0.005) * 100)) + "%";
		List<VariableFor401k> candidateFunds = plan.getVariablesFor401k();
		if (candidateFunds == null)
		{
			return null;
		}
		/**
		 * ********************* get symbols of Plan
		 * ***********************************
		 */
		List<String> tickerList = new ArrayList<String>();
		List<String> assetsList = new ArrayList<String>();
		for (VariableFor401k va : candidateFunds)
		{
			String s = va.getSymbol();
			tickerList.add(s);
		}
		/**
		 * ********************** get MinorAssets and it's symbols
		 * **************************************
		 */
		Map<String, List<String>> tickerAndAssets = new HashMap<String, List<String>>();
		for (String ss : tickerList)
		{
			Security se = securityManager.getBySymbol(ss);
			if (se != null)
			{
				String assetName = se.getAssetClass().getName();
				if (!assetsList.contains(assetName) && !assetName.equals("ROOT"))
					assetsList.add(assetName);
			}
		}

		for (String ss : assetsList)
		{
			List<String> tmpList = new ArrayList<String>();
			for (String symbol : tickerList)
			{
				Security sec = securityManager.getBySymbol(symbol);
				if (sec != null)
				{
					String str = sec.getAssetClass().getName();
					if (str.equalsIgnoreCase(ss))
						tmpList.add(symbol);
				}
			}
			tickerAndAssets.put(ss, tmpList);
		}
		/**
		 * ************************* get Major Assets and it's minor assets
		 * ****************************************
		 */
		Map<String, String> assetclass = new HashMap<String, String>();
		File csvFile = new File(URLDecoder.decode(com.lti.action.f401kaction.class.getResource("6_main_asset_classes.csv").getFile(), "utf-8"));
		CsvListReader clr = new CsvListReader(new FileReader(csvFile), CsvPreference.STANDARD_PREFERENCE);
		List<String> head = clr.read();
		String[] arrs = new String[head.size()];
		head.toArray(arrs);

		List<String> list = null;

		while ((list = clr.read()) != null)
		{
			for (int i = 0; i < 7; i++)
			{
				String cell = list.get(i).trim();
				if (!cell.equals(""))
				{
					assetclass.put(cell, arrs[i]);
				}
			}

		}

		List<String> majorList = new ArrayList<String>();
		for (String asset : assetsList)
		{
			String major = assetclass.get(asset.trim());
			if (major != null)
			{
				if (!major.equals("Other") && !majorList.contains(major))
				{
					majorList.add(major);
				}
			}
		}

		Map<String, List<String>> majorAndMinorAsset = new HashMap<String, List<String>>();
		for (String major : majorList)
		{
			List<String> tempList = new ArrayList<String>();
			for (String key : assetsList)
			{
				String value = assetclass.get(key);
				if (major.equalsIgnoreCase(value))
				{
					tempList.add(key);
				}
			}
			majorAndMinorAsset.put(major, tempList);
		}
		/**
		 * *********************************************************************
		 * *****************
		 */
		message = "";
		message = message + "The Plan consists of " + tickerList.size() + " Funds" + "\n\n";
		message += "The MajorAssets Class and MinorAssets Class\n";
		for (String major : majorAndMinorAsset.keySet())
		{
			int amount = 0;
			message += major;
			List<String> list_minor = majorAndMinorAsset.get(major);

			for (int k = 0; k < list_minor.size(); k++)
			{
				String ss = list_minor.get(k);
				amount = amount + tickerAndAssets.get(ss).size();
			}
			message = message + "(" + amount + ")";
			message += ":";
			for (int i = 0; i < list_minor.size(); i++)
			{
				String s = list_minor.get(i);
				int minorsize = tickerAndAssets.get(s).size();
				if (i != 0)
				{
					message += ",";
				}
				message += s;
				message = message + "(" + minorsize + ")";
			}
			message += "\n";
		}
		message += "\n";
		message += "The MinorAssets Class and Funds\n";
		for (String minor : tickerAndAssets.keySet())
		{
			message += minor;
			message += ":";
			List<String> list_funds = tickerAndAssets.get(minor);
			for (int j = 0; j < list_funds.size(); j++)
			{
				String ss = list_funds.get(j);
				if (j != 0)
				{
					message += ",";
				}
				message += ss;
			}
			message += "\n";
		}

		message += "\n";
		message += "Rating Result\n";
		message += "Diversification:" + diversfiction + "\n";
		message += "Fund Quality:" + fundquality + "\n";
		message += "Portfolio Building:" + buliding + "\n";
		message += "Overall Score:" + overall + "\n";
		return Action.MESSAGE;
	}

	/**
	 * @author CCD
	 * @return
	 */
	public String showplanscorelist()
	{
		StrategyManager strategyManager = ContextHolder.getStrategyManager();
		List<List<PlanScore>> scoreList = strategyManager.getTopAndBottomPlanScoreList(20);
		planScoreTopList = scoreList.get(0);
		planScoreBottomList = scoreList.get(1);
		return Action.SUCCESS;
	}

	public String showplanscorelisttable()
	{
		StrategyManager strategyManager = ContextHolder.getStrategyManager();
		List<List<PlanScore>> scoreList = strategyManager.getTopAndBottomPlanScoreList(20);
		planScoreTopList = scoreList.get(0);
		planScoreBottomList = scoreList.get(1);
		return Action.SUCCESS;
	}

	public String showallplanscorelist()
	{
		StrategyManager strategyManager = ContextHolder.getStrategyManager();
		planScoreList = strategyManager.getPlanScore();
		return Action.SUCCESS;
	}

	public String showplanscorewithids()
	{
		StrategyManager strategyManager = ContextHolder.getStrategyManager();
		List<Long> planIDList = new ArrayList<Long>();
		String[] planIDs = null;
		if (planIDArray != null && !planIDArray.trim().equals(""))
		{
			planIDs = planIDArray.split(",");
			for (int i = 0; i < planIDs.length; ++i)
				planIDList.add(Long.parseLong(planIDs[i]));
			planScoreListWithIDs = strategyManager.getPlanScoreWithIDs(planIDList);
		} else
			planScoreListWithIDs = strategyManager.getPlanScore();

		return Action.SUCCESS;
	}

	private String from;
	private String to;
	private Long fromID;
	private Long toID;
	private List<String> ignoreNames;

	public List<String> getIgnoreNames()
	{
		return ignoreNames;
	}

	public void setIgnoreNames(List<String> ignoreNames)
	{
		this.ignoreNames = ignoreNames;
	}

	public String plancomparetool()
	{
		StrategyManager strategyManager = ContextHolder.getStrategyManager();
		List<Long> planIDList = new ArrayList<Long>();
		String[] planNames = null;
		ignoreNames = new ArrayList<String>();
		Date today = new Date();
		endDate = LTIDate.parseDateToString(today);
		if (planNameArray != null && !planNameArray.trim().equals(""))
		{
			planNames = planNameArray.split("\r\n");
			from = planNames[0];
			to = planNames[1];
			for (int i = 0; i < planNames.length; ++i)
			{
				Strategy str = strategyManager.get(planNames[i]);
				if (str != null)
				{
					planIDList.add(str.getID());
				}
			}

			// plan score list compare
			planScoreListWithIDs = strategyManager.getPlanScoreWithIDs(planIDList);
			for (int i = 0; i < planNames.length; ++i)
			{
				boolean in = false;
				if (planScoreListWithIDs != null)
				{
					for (int j = 0; j < planScoreListWithIDs.size(); j++)
					{
						if (planScoreListWithIDs.get(j).getPlanName().toLowerCase().equals(planNames[i].toLowerCase().trim()))
						{
							in = true;
							break;
						}
					}
				}
				if (!in)
				{
					ignoreNames.add(planNames[i]);
				}
			}
			if (planIDList.size() < 2)
			{
				return Action.SUCCESS;
			}
			fromID = planIDList.get(0);
			toID = planIDList.get(1);
			// ar and sharperation compare
			SAAMap = new HashMap<String, CachePortfolioItem>();
			TAAMap = new HashMap<String, CachePortfolioItem>();
			moderateSAANames = new String();
			moderateTAANames = new String();
			for (int i = 0; i < planIDList.size(); ++i)
			{
				List<Portfolio> portfolios = strategyManager.getModeratePortfolios(planIDList.get(i));
				boolean hasTAA = false;
				boolean hasSAA = false;
				for (int j = 0; j < portfolios.size(); ++j)
				{
					Portfolio p = portfolios.get(j);
					List<CachePortfolioItem> pitems = null;
					if (p.getName().endsWith("Tactical Asset Allocation Moderate") && !hasTAA)
					{
						if (!moderateTAANames.equals(""))
							moderateTAANames += ",";
						moderateTAANames += p.getSymbol();
						pitems = strategyManager.findByHQL("from CachePortfolioItem cp where cp.PortfolioID=" + p.getID() + " and cp.GroupID=0 and cp.RoleID=" + Configuration.ROLE_PORTFOLIO_REALTIME_ID);
						if (pitems != null && pitems.size() > 0)
							TAAMap.put(planNames[i], pitems.get(0));
						else
						{
							CachePortfolioItem cpi = new CachePortfolioItem();
							cpi.setPortfolioID(p.getID());
							cpi.setPortfolioName(p.getSymbol());
							TAAMap.put(planNames[i], cpi);
						}
						hasTAA = true;
					} else if (p.getName().endsWith("Strategic Asset Allocation Moderate") && !hasSAA)
					{
						if (!moderateSAANames.equals(""))
							moderateSAANames += ",";
						moderateSAANames += p.getSymbol();
						pitems = strategyManager.findByHQL("from CachePortfolioItem cp where cp.PortfolioID=" + p.getID() + " and cp.GroupID=0 and cp.RoleID=" + Configuration.ROLE_PORTFOLIO_REALTIME_ID);
						if (pitems != null && pitems.size() > 0)
							SAAMap.put(planNames[i], pitems.get(0));
						else
						{
							CachePortfolioItem cpi = new CachePortfolioItem();
							cpi.setPortfolioID(p.getID());
							cpi.setPortfolioName(p.getName());
							SAAMap.put(planNames[i], cpi);
						}
						hasSAA = true;
					}
				}
			}
		}
		return Action.SUCCESS;
	}

	private boolean createRole = false;

	public boolean getCreateRole()
	{
		return createRole;
	}

	public void setCreateRole(boolean createRole)
	{
		this.createRole = createRole;
	}

	private int MaxPlanCreatedNumber;
	private int CurrentPortfolioNumber;
	private int CurrentPlanCreatedNumber;
	private int MaxPortfolioUsedNumber;

	public String userPlans()
	{
		user = ContextHolder.getUserManager().getLoginUser();
		UserManager userManager = ContextHolder.getUserManager();
		UserPermission ups = userManager.getUserPermissionByUserID(user.getID());
		SubscrPlanChecker spc = new SubscrPlanChecker();
		boolean hasCreate = spc.hasPlanCreateRole(user.getID());
		MaxPlanCreatedNumber = ups.getMaxPlanCreateNum();
		CurrentPortfolioNumber = ups.getCurPortfolioFollowNum();
		CurrentPlanCreatedNumber = ups.getCurPlanCreateNum();
		MaxPortfolioUsedNumber = ups.getMaxPortfolioFollowNum();
		if (hasCreate && user.getID() != Configuration.SUPER_USER_ID)
			createRole = true;
		else
			createRole = false;
		return Action.SUCCESS;
	}

	public int getMaxPortfolioUsedNumber()
	{
		return MaxPortfolioUsedNumber;
	}

	public void setMaxPortfolioUsedNumber(int maxPortfolioUsedNumber)
	{
		MaxPortfolioUsedNumber = maxPortfolioUsedNumber;
	}

	public int getMaxPlanCreatedNumber()
	{
		return MaxPlanCreatedNumber;
	}

	public void setMaxPlanCreatedNumber(int maxPlanCreatedNumber)
	{
		MaxPlanCreatedNumber = maxPlanCreatedNumber;
	}

	public int getCurrentPortfolioNumber()
	{
		return CurrentPortfolioNumber;
	}

	public void setCurrentPortfolioNumber(int currentPortfolioNumber)
	{
		CurrentPortfolioNumber = currentPortfolioNumber;
	}

	public int getCurrentPlanCreatedNumber()
	{
		return CurrentPlanCreatedNumber;
	}

	public void setCurrentPlanCreatedNumber(int currentPlanCreatedNumber)
	{
		CurrentPlanCreatedNumber = currentPlanCreatedNumber;
	}

	public String getFrom()
	{
		return from;
	}

	public void setFrom(String from)
	{
		this.from = from;
	}

	public String getTo()
	{
		return to;
	}

	public void setTo(String to)
	{
		this.to = to;
	}

	public Long getFromID()
	{
		return fromID;
	}

	public void setFromID(Long fromID)
	{
		this.fromID = fromID;
	}

	public Long getToID()
	{
		return toID;
	}

	public void setToID(Long toID)
	{
		this.toID = toID;
	}

	public String planrollover()
	{
		if (from != null)
			planNameArray = from;
		if (to != null)
			planNameArray += "\r\n" + to;
		return plancomparetool();
	}

	/*-------------------------------------------------------*/
	// The weight for calculating security quality
	private Double securityAlphaWeight = 0.25;
	private Double securityTreynorWeight = 0.25;
	private Double securityARWeight = 0.5;

	// The weight for calculating portfolio construction capability
	private Double portfolioARWeight = 0.5;
	private Double portfolioSortinoWeight = 0.2;
	private Double portfolioSharpeWeight = 0.1;
	private Double portfolioTreynorWeight = 0.0;
	private Double portfolioDrawDownWeight = 0.1;
	private Double portfolioWinningWeight = 0.1;

	// The weight for plan construction capability
	private Double planTAAWeight = 0.8;
	private Double planSAAWeight = 0.2;

	// The weight for plan overall investment
	private Double planFundQualityWeight = 0.3;
	private Double planCoverageWeight = 0.3;
	private Double planCapabilityWeight = 0.4;

	/**
	 * @author CCD
	 * @return
	 */
	public String calculateplanscore()
	{
		PlanRankingUtil pru = new PlanRankingUtil(securityAlphaWeight, securityTreynorWeight, securityARWeight, portfolioARWeight, portfolioSortinoWeight, portfolioSharpeWeight, portfolioTreynorWeight, portfolioDrawDownWeight, portfolioWinningWeight, planTAAWeight, planSAAWeight, planFundQualityWeight, planCoverageWeight, planCapabilityWeight);
		try
		{
			pru.rankPlan();
			message = "successful";
		} catch (Exception e)
		{
			System.out.println(StringUtil.getStackTraceString(e));
			message = "fail";
		}
		return Action.MESSAGE;
	}

	private String dateStr = null;
	private int updateNum;
	private List<SecurityState> securityPriceSuccessStateList;
	private List<SecurityState> securityPriceFailStateList;
	private List<SecurityState> cefPriceSuccessStateList;
	private List<SecurityState> cefPriceFailStateList;
	private List<SecurityState> etfPriceSuccessStateList;
	private List<SecurityState> etfPriceFailStateList;
	private List<SecurityState> securityNAVSuccessStateList;
	private List<SecurityState> securityNAVFailStateList;
	private List<String> updateSummary;

	/**
	 * 获得更新明细 updateTime, 1或2 dateStr, 日期
	 * 
	 * @return
	 */
	public String getfastupdatereport()
	{
		LTIDownLoader ltiDownLoader = new LTIDownLoader();
		securityPriceSuccessStateList = new ArrayList<SecurityState>();
		securityPriceFailStateList = new ArrayList<SecurityState>();
		securityNAVSuccessStateList = new ArrayList<SecurityState>();
		securityNAVFailStateList = new ArrayList<SecurityState>();
		String detailPath = ltiDownLoader.getSystemPath() + dateStr + Configuration.fastUpdateDetailLogFilePath + updateNum + ".csv";
		try
		{
			CSVReader csv = new CSVReader(new FileReader(new File(detailPath)));
			List<String> line = new ArrayList<String>();
			while ((line = csv.readLineAsList()) != null)
			{
				SecurityState ss = new SecurityState();
				ss.setSymbol(line.get(2));
				ss.setEndDate(line.get(3));
				ss.setUpdateTime(line.get(4));
				if (line.get(0).equals("success"))
				{
					if (line.get(1).equals("PRICE"))
						securityPriceSuccessStateList.add(ss);
					else
						securityNAVSuccessStateList.add(ss);
				} else
				{
					if (line.get(1).equals("PRICE"))
						securityPriceFailStateList.add(ss);
					else
						securityNAVFailStateList.add(ss);
				}
			}
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		String simplePath = ltiDownLoader.getSystemPath() + dateStr + Configuration.fastUpdateSimpleLogFilePath + updateNum + ".csv";
		try
		{
			CSVReader csv = new CSVReader(new FileReader(new File(simplePath)));
			List<String> line = csv.readLineAsList();
			if (line != null)
			{
				updateSummary = new ArrayList<String>();
				for (String ln : line)
					// startTime, endTime, pSuccess, pFail, nSuccess, nFail
					updateSummary.add(ln);
			}
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return Action.SUCCESS;
	}

	/**
	 * @return
	 */
	public String getdatabasesecurityreport()
	{
		SecurityManager securityManager = ContextHolder.getSecurityManager();
		securityPriceSuccessStateList = new ArrayList<SecurityState>();
		securityPriceFailStateList = new ArrayList<SecurityState>();
		etfPriceSuccessStateList = new ArrayList<SecurityState>();
		etfPriceFailStateList = new ArrayList<SecurityState>();
		cefPriceSuccessStateList = new ArrayList<SecurityState>();
		cefPriceFailStateList = new ArrayList<SecurityState>();
		securityNAVSuccessStateList = new ArrayList<SecurityState>();
		securityNAVFailStateList = new ArrayList<SecurityState>();
		Calendar ca = Calendar.getInstance();
		ca.setTimeInMillis(System.currentTimeMillis());
		if (ca.get(Calendar.HOUR_OF_DAY) < 16)
		{
			ca.add(Calendar.DAY_OF_YEAR, -1);
		}
		Date today = LTIDate.getRecentNYSETradingDay(ca.getTime());
		if (dateStr != null && !dateStr.equals(""))
			today = LTIDate.parseStringToDate(dateStr);
		List<Security> securityList = securityManager.getSecurities();
		if (securityList != null)
		{
			for (Security se : securityList)
			{
				SecurityState ss = new SecurityState(se.getSymbol(), LTIDate.parseDateToString(se.getEndDate()), null);
				if (se.getIsClosed() != null && se.getIsClosed() || se.getSecurityType() == null)
					continue;
				switch (se.getSecurityType())
				{
				case Configuration.SECURITY_TYPE_ETF:
					if (se.getEndDate() == null || LTIDate.before(se.getEndDate(), today))
						etfPriceFailStateList.add(ss);
					else
						etfPriceSuccessStateList.add(ss);
					break;
				case Configuration.SECURITY_TYPE_MUTUAL_FUND:
					if (se.getEndDate() == null || LTIDate.before(se.getEndDate(), today))
						securityPriceFailStateList.add(ss);
					else
						securityPriceSuccessStateList.add(ss);
					break;
				case Configuration.SECURITY_TYPE_CLOSED_END_FUND:
					if (se.getEndDate() == null || LTIDate.before(se.getEndDate(), today))
						cefPriceFailStateList.add(ss);
					else
						cefPriceSuccessStateList.add(ss);
					SecurityState sss = new SecurityState(se.getSymbol(), LTIDate.parseDateToString(se.getNavLastDate()), null);
					if (se.getNavLastDate() == null || LTIDate.before(se.getNavLastDate(), today))
						securityNAVFailStateList.add(sss);
					else
						securityNAVSuccessStateList.add(sss);
					break;
				default:
					break;
				}
			}
		}
		updateSummary = new ArrayList<String>();
		updateSummary.add(String.valueOf(securityPriceSuccessStateList.size()));
		updateSummary.add(String.valueOf(securityPriceFailStateList.size()));
		updateSummary.add(String.valueOf(etfPriceSuccessStateList.size()));
		updateSummary.add(String.valueOf(etfPriceFailStateList.size()));
		updateSummary.add(String.valueOf(cefPriceSuccessStateList.size()));
		updateSummary.add(String.valueOf(cefPriceFailStateList.size()));
		updateSummary.add(String.valueOf(securityNAVSuccessStateList.size()));
		updateSummary.add(String.valueOf(securityNAVFailStateList.size()));
		return Action.SUCCESS;
	}

	/**
	 * 
	 * @return
	 */
	public String getdailyexecutionreport()
	{
		securityPriceSuccessStateList = new ArrayList<SecurityState>();
		securityPriceFailStateList = new ArrayList<SecurityState>();
		Calendar ca = Calendar.getInstance();
		ca.setTimeInMillis(System.currentTimeMillis());
		if (ca.get(Calendar.HOUR_OF_DAY) < 16)
		{
			ca.add(Calendar.DAY_OF_YEAR, -1);
		}
		Date today = LTIDate.getRecentNYSETradingDay(ca.getTime());
		if (dateStr == null || dateStr.equals(""))
			dateStr = LTIDate.parseDateToString(today);
		String title = "";
		switch (updateNum)
		{
		case 0:// Daily Execution
			title = "Daily executing by quartz using update mode.";
			break;
		case 1:// Fast Daily Execution1
			title = "Fast daily executing1 by quartz(EMail Only).";
			break;
		case 2:// Fast Daily Execution2
			title = "Fast daily executing2 by quartz(EMail Only).";
			break;
		}
		title += dateStr + ".csv";
		String toDoNum = "0";
		try
		{
			// portfolioID, portfolioName, endDate|N/A, success|fail, beginTime,
			// endTime, cost Time, success | getStackTraceString
			CSVReader csv = new CSVReader(new FileReader(new File(ContextHolder.getServletPath() + "/" + title)));
			List<String> line = new ArrayList<String>();
			line = csv.readLineAsList();
			if (line != null)
				toDoNum = line.get(0);
			while ((line = csv.readLineAsList()) != null)
			{
				System.out.println(line);
				if (line.size() == 0 || line.get(0).length() == 0 || !Character.isDigit(line.get(0).charAt(0)))
					continue;
				String symbol = null, endDate = null, updateTime = null, state = null;
				if (line.size() > 1)
					symbol = line.get(1);
				if (line.size() > 2)
					endDate = line.get(2);
				if (line.size() > 3)
					state = line.get(3);
				if (line.size() > 6)
					updateTime = line.get(6);
				SecurityState ss = new SecurityState(symbol, endDate, updateTime);
				if (state != null && state.equals("success"))
					securityPriceSuccessStateList.add(ss);
				else
					securityPriceFailStateList.add(ss);
			}
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		updateSummary = new ArrayList<String>();
		updateSummary.add(dateStr);
		updateSummary.add(String.valueOf(securityPriceSuccessStateList.size()));
		updateSummary.add(String.valueOf(securityPriceFailStateList.size()));
		updateSummary.add(toDoNum);
		return Action.SUCCESS;
	}

	/**
	 * 
	 * @return
	 */
	public String getdatabaseportfolioreport()
	{
		securityPriceSuccessStateList = new ArrayList<SecurityState>();
		securityPriceFailStateList = new ArrayList<SecurityState>();
		Calendar ca = Calendar.getInstance();
		ca.setTimeInMillis(System.currentTimeMillis());
		if (ca.get(Calendar.HOUR_OF_DAY) < 16)
		{
			ca.add(Calendar.DAY_OF_YEAR, -1);
		}
		Date expectedEndDate = LTIDate.getRecentNYSETradingDay(ca.getTime());
		if (dateStr != null && !dateStr.equals(""))
			expectedEndDate = LTIDate.parseStringToDate(dateStr);
		PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();
		List<Portfolio> portfolioList = portfolioManager.getSimplePortfolios(-1, -1);
		if (portfolioList != null)
		{
			for (Portfolio portfolio : portfolioList)
			{
				SecurityState ss = new SecurityState(portfolio.getName(), LTIDate.parseDateToString(portfolio.getEndDate()), null);
				if (portfolio.getEndDate() == null || LTIDate.before(portfolio.getEndDate(), expectedEndDate))
					securityPriceFailStateList.add(ss);
				else
					securityPriceSuccessStateList.add(ss);
			}
		}
		updateSummary = new ArrayList<String>();
		updateSummary.add(String.valueOf(securityPriceSuccessStateList.size()));
		updateSummary.add(String.valueOf(securityPriceFailStateList.size()));
		return Action.SUCCESS;
	}

	/**
	 * 
	 * @return
	 */
	public String getsecuritymptreport()
	{
		securityPriceSuccessStateList = new ArrayList<SecurityState>();
		securityPriceFailStateList = new ArrayList<SecurityState>();
		updateSummary = new ArrayList<String>();
		Calendar ca = Calendar.getInstance();
		ca.setTimeInMillis(System.currentTimeMillis());
		if (ca.get(Calendar.HOUR_OF_DAY) < 16)
		{
			ca.add(Calendar.DAY_OF_YEAR, -1);
		}
		Date today = LTIDate.getRecentNYSETradingDay(ca.getTime());
		if (dateStr != null && !dateStr.equals(""))
			today = LTIDate.parseStringToDate(dateStr);
		LTIDownLoader ltiDownLoader = new LTIDownLoader();
		String mptPath = ltiDownLoader.getSystemPath() + "MPTUpdateLog_" + LTIDate.parseDateToString(today) + ".csv";
		// Date,CostTime,SecurityID,Symbol,EndDate,MPTLastDate,State,Other
		try
		{
			CSVReader csv = new CSVReader(new FileReader(new File(mptPath)));
			List<String> line = new ArrayList<String>();
			line = csv.readLineAsList();
			while ((line = csv.readLineAsList()) != null)
			{
				if (line.get(0).equals("Complete"))
				{// The last summary
					break;
				}
				SecurityState ss = new SecurityState(line.get(3), line.get(5), line.get(1));
				if (line.get(6).equals("SUCCESS"))
					securityPriceSuccessStateList.add(ss);
				else
					securityPriceFailStateList.add(ss);
			}
			line = csv.readLineAsList();
			updateSummary.add(LTIDate.parseDateToString(today));// end date
			if (line != null)
			{
				updateSummary.add(line.get(1));// cost time
				updateSummary.add(line.get(2));// total count
				updateSummary.add(line.get(3));// fail count
			} else
			{
				updateSummary.add("0");
				updateSummary.add(securityPriceSuccessStateList.size() + "");
				updateSummary.add(securityPriceFailStateList.size() + "");
			}
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return Action.SUCCESS;
	}

	public String getdatabasesecuritymptreport()
	{
		SecurityManager securityManager = ContextHolder.getSecurityManager();
		securityPriceSuccessStateList = new ArrayList<SecurityState>();
		securityPriceFailStateList = new ArrayList<SecurityState>();
		updateSummary = new ArrayList<String>();
		Calendar ca = Calendar.getInstance();
		ca.setTimeInMillis(System.currentTimeMillis());
		if (ca.get(Calendar.HOUR_OF_DAY) < 16)
		{
			ca.add(Calendar.DAY_OF_YEAR, -1);
		}
		Date today = LTIDate.getRecentNYSETradingDay(ca.getTime());
		if (dateStr != null && !dateStr.equals(""))
			today = LTIDate.parseStringToDate(dateStr);
		List<Security> securityList = securityManager.getSecurities();
		if (securityList != null)
		{
			for (Security se : securityList)
			{
				SecurityState ss = new SecurityState(se.getSymbol(), LTIDate.parseDateToString(se.getEndDate()), null);
				if (se.getIsClosed() != null && se.getIsClosed() || se.getSecurityType() == null)
					continue;
				if (se.getMptLastDate() == null || LTIDate.before(se.getMptLastDate(), today))
					securityPriceFailStateList.add(ss);
				else
					securityPriceSuccessStateList.add(ss);
			}
		}
		updateSummary = new ArrayList<String>();
		updateSummary.add(LTIDate.parseDateToString(today));
		updateSummary.add(securityPriceSuccessStateList.size() + "");
		updateSummary.add(securityPriceFailStateList.size() + "");
		return Action.SUCCESS;
	}

	public String downLoadSAAFollower() throws IOException
	{
		StrategyManager strategyManager = ContextHolder.getStrategyManager();
		getCountTAAOrSAA(Configuration.STRATEGY_SAA_ID, Configuration.USER_RESOURCE_PORTFOLIO_FOLLOW);
		planName = strategyManager.get(ID).getName() + "_SAAFollower.csv";
		String dateStr = LTIDate.parseDateToString(new Date());
		String systemPath;
		String sysPath = System.getenv("windir");
		if (!isLinux())
			systemPath = sysPath + "\\temp\\";
		else
			systemPath = "/var/tmp/";

		String portfolios = systemPath + ID + "_" + dateStr + ".csv";
		File f = new File(portfolios);
		fis = new FileInputStream(f);
		return Action.DOWNLOAD;
	}

	public String downLoadTAAFollower() throws IOException
	{
		StrategyManager strategyManager = ContextHolder.getStrategyManager();
		getCountTAAOrSAA(Configuration.STRATEGY_TAA_ID, Configuration.USER_RESOURCE_PORTFOLIO_FOLLOW);
		planName = strategyManager.get(ID).getName() + "_TAAFollower.csv";
		String dateStr = LTIDate.parseDateToString(new Date());
		String systemPath;
		String sysPath = System.getenv("windir");
		if (!isLinux())
			systemPath = sysPath + "\\temp\\";
		else
			systemPath = "/var/tmp/";
		String portfolios = systemPath + ID + "_" + dateStr + ".csv";
		File f = new File(portfolios);
		fis = new FileInputStream(f);
		return Action.DOWNLOAD;
	}

	public String downLoadCustomizedSAA() throws IOException
	{
		StrategyManager strategyManager = ContextHolder.getStrategyManager();
		getCountTAAOrSAA(Configuration.STRATEGY_SAA_ID, Configuration.USER_RESOURCE_PORTFOLIO_CUSTOMIZE);
		planName = strategyManager.get(ID).getName() + "_SAACustomized.csv";
		String dateStr = LTIDate.parseDateToString(new Date());
		String systemPath;
		String sysPath = System.getenv("windir");
		if (!isLinux())
			systemPath = sysPath + "\\temp\\";
		else
			systemPath = "/var/tmp/";
		String portfolios = systemPath + ID + "_" + dateStr + ".csv";
		File f = new File(portfolios);
		fis = new FileInputStream(f);
		return Action.DOWNLOAD;
	}

	public String downLoadCustomizedTAA() throws IOException
	{
		StrategyManager strategyManager = ContextHolder.getStrategyManager();
		getCountTAAOrSAA(Configuration.STRATEGY_TAA_ID, Configuration.USER_RESOURCE_PORTFOLIO_CUSTOMIZE);
		planName = strategyManager.get(ID).getName() + "_TAACustomized.csv";
		String dateStr = LTIDate.parseDateToString(new Date());
		String systemPath;
		String sysPath = System.getenv("windir");
		if (!isLinux())
			systemPath = sysPath + "\\temp\\";
		else
			systemPath = "/var/tmp/";
		String portfolios = systemPath + ID + "_" + dateStr + ".csv";
		File f = new File(portfolios);
		fis = new FileInputStream(f);
		return Action.DOWNLOAD;
	}

	public boolean isLinux()
	{
		String str = System.getProperty("os.name").toUpperCase();
		if (str.indexOf("WINDOWS") == -1)
		{
			return true;
		}
		return false;
	}

	// 统计plan中portfolio下saa和taa的数量，分为customize和follow
	public void getCountTAAOrSAA(long flag, int resourceFlag) throws IOException
	{
		StrategyManager strategyManager = ContextHolder.getStrategyManager();
		CsvListWriter clw = null;
		String systemPath;
		String sysPath = System.getenv("windir");
		String str = System.getProperty("os.name").toUpperCase();
		if (str.indexOf("WINDOWS") == -1)
			systemPath = "/var/tmp/";
		else
			systemPath = sysPath + "\\temp\\";
		String dateStr = LTIDate.parseDateToString(new Date());
		String logFileName = systemPath + ID + "_" + dateStr + ".csv";
		File file = new File(logFileName);
		try
		{
			clw = new CsvListWriter(new FileWriter(file, false), CsvPreference.EXCEL_PREFERENCE);
			String[] header =
			{ "user_id", "account_name", "email" };
			clw.writeHeader(header);
		} catch (IOException e)
		{
			System.out.println(StringUtil.getStackTraceString(e));
		}
		List<UserResource> listur = new ArrayList<UserResource>();
		UserManager userManager = ContextHolder.getUserManager();
		List<String> strs = null;
		// 查找该plan下的所有portfolios
		List<Portfolio> portfolioList = strategyManager.getModelPortfolios(ID);
		ProfileManager profileManager = (ProfileManager) ContextHolder.getInstance().getApplicationContext().getBean("profileManager");
		List<Profile> userProfileList = profileManager.getProfilesByPlanID(ID);
		PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();
		if (userProfileList != null && userProfileList.size() > 0)
		{
			for (Profile p : userProfileList)
			{
				Portfolio pp = portfolioManager.get(p.getPortfolioID());
				portfolioList.add(pp);
			}
		}
		// 根据flag判断为SAA或TAA,并加入portfolioOnFlag
		List<Portfolio> portfolioOnFlag = new ArrayList<Portfolio>();
		if (portfolioList != null && portfolioList.size() > 0)
		{
			for (Portfolio p : portfolioList)
			{
				Long strategyID = p.getStrategies().getAssetAllocationStrategy().getID();
				if (strategyID.equals(flag))
				{
					portfolioOnFlag.add(p);
				}
			}
		}

		long resultCount = 0;
		Set<Long> userSet = new HashSet<Long>();
		if (portfolioOnFlag != null && portfolioOnFlag.size() > 0)
		{
			for (int i = 0; i < portfolioOnFlag.size(); i++)
			{
				if (resourceFlag == Configuration.USER_RESOURCE_PORTFOLIO_FOLLOW)
				{
					listur = userManager.getUserResourceByResourceIDAndResourceType(portfolioOnFlag.get(i).getID(), Configuration.USER_RESOURCE_PORTFOLIO_CUSTOMIZE);
					if (listur == null || listur.size() == 0)
					{
						listur = userManager.getUserResourceByResourceIDAndResourceType(portfolioOnFlag.get(i).getID(), Configuration.USER_RESOURCE_PORTFOLIO_FOLLOW);
						if (listur != null && listur.size() > 0)
						{
							for (int j = 0; j < listur.size(); j++)
							{
								strs = new ArrayList<String>();
								strs.add(Long.toString(listur.get(j).getUserID()));
								strs.add(userManager.get(listur.get(j).getUserID()).getUserName());
								if (userManager.get(listur.get(j).getUserID()).getEMail() == null)
								{
									strs.add("NULL");
								} else
								{
									strs.add(userManager.get(listur.get(j).getUserID()).getEMail());
								}
								clw.write(strs);
							}
							strs = new ArrayList<String>();
							strs.add(portfolioOnFlag.get(i).getName());
							strs.add("totalNum");
							strs.add(Integer.toString(listur.size()));
							clw.write(strs);
						}
						resultCount = resultCount + listur.size();
						for (int m = 0; m < listur.size(); m++)
						{
							userSet.add(listur.get(m).getUserID());
						}
					}
				} else
				{
					listur = userManager.getUserResourceByResourceIDAndResourceType(portfolioOnFlag.get(i).getID(), Configuration.USER_RESOURCE_PORTFOLIO_CUSTOMIZE);
					if (listur != null && listur.size() > 0)
					{
						for (int j = 0; j < listur.size(); j++)
						{
							strs = new ArrayList<String>();
							strs.add(Long.toString(listur.get(j).getUserID()));
							strs.add(userManager.get(listur.get(j).getUserID()).getUserName());
							if (userManager.get(listur.get(j).getUserID()).getEMail() == null)
							{
								strs.add("NULL");
							} else
							{
								strs.add(userManager.get(listur.get(j).getUserID()).getEMail());
							}
							clw.write(strs);
						}
						strs = new ArrayList<String>();
						strs.add(portfolioOnFlag.get(i).getName());
						strs.add("totalNum");
						strs.add(Integer.toString(listur.size()));
						clw.write(strs);
					}
					resultCount = resultCount + listur.size();
					for (int n = 0; n < listur.size(); n++)
					{
						userSet.add(listur.get(n).getUserID());
					}
				}
			}
			strs = new ArrayList<String>();
			strs.add("downLoad finish");
			strs.add("portfolioTotalNum");
			strs.add(Long.toString(resultCount));
			clw.write(strs);
			strs = new ArrayList<String>();
			strs.add("downLoad finish");
			strs.add("userTotalNum");
			strs.add(Long.toString(userSet.size()));
			clw.write(strs);
			clw.close();
		}
	}

	// public long countBySAAAndTAA(long flag, int resourceFlag) {
	// StrategyManager strategyManager = ContextHolder.getStrategyManager();
	// List<UserResource> listur = new ArrayList<UserResource>();
	// UserManager userManager = ContextHolder.getUserManager();
	// // 查找该plan下的所有portfolios
	// List<Portfolio> portfolioList = strategyManager.getModelPortfolios(ID);
	// ProfileManager profileManager = (ProfileManager) ContextHolder
	// .getInstance().getApplicationContext()
	// .getBean("profileManager");
	// List<Profile> userProfileList = profileManager.getProfilesByPlanID(ID);
	// PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();
	// if (userProfileList != null && userProfileList.size() > 0) {
	// for (Profile p : userProfileList) {
	// Portfolio pp = portfolioManager.get(p.getPortfolioID());
	// portfolioList.add(pp);
	// }
	// }
	// // 根据flag判断为SAA或TAA,并加入portfolioOnFlag
	// List<Portfolio> portfolioOnFlag = new ArrayList<Portfolio>();
	// if (portfolioList != null && portfolioList.size() > 0) {
	// for (Portfolio p : portfolioList) {
	// Long strategyID = p.getStrategies()
	// .getAssetAllocationStrategy().getID();
	// if (strategyID.equals(flag)) {
	// portfolioOnFlag.add(p);
	// }
	// }
	// }
	// long resultCount = 0;
	// if (portfolioOnFlag != null && portfolioOnFlag.size() > 0) {
	// for (int i = 0; i < portfolioOnFlag.size(); i++) {
	// if (resourceFlag == Configuration.USER_RESOURCE_PORTFOLIO_FOLLOW) {
	// listur = userManager
	// .getUserResourceByResourceIDAndResourceType(
	// portfolioOnFlag.get(i).getID(),
	// Configuration.USER_RESOURCE_PORTFOLIO_CUSTOMIZE);
	// if (listur == null || listur.size() == 0) {
	// listur = userManager
	// .getUserResourceByResourceIDAndResourceType(
	// portfolioOnFlag.get(i).getID(),
	// Configuration.USER_RESOURCE_PORTFOLIO_FOLLOW);
	// if (listur != null && listur.size() > 0) {
	// resultCount = resultCount + listur.size();
	// }
	// }
	// } else {
	// listur = userManager
	// .getUserResourceByResourceIDAndResourceType(
	// portfolioOnFlag.get(i).getID(),
	// Configuration.USER_RESOURCE_PORTFOLIO_CUSTOMIZE);
	// if (listur != null && listur.size() > 0) {
	// resultCount = resultCount + listur.size();
	// }
	// }
	// }
	// }
	// return resultCount;
	// }
	//
	// public String getNumBySAAAndTAA() {
	// message = Long.toString(countBySAAAndTAA(Configuration.STRATEGY_SAA_ID,
	// Configuration.USER_RESOURCE_PORTFOLIO_FOLLOW))+"|";
	// message = message +
	// Long.toString(countBySAAAndTAA(Configuration.STRATEGY_TAA_ID,
	// Configuration.USER_RESOURCE_PORTFOLIO_FOLLOW))+"|";
	// message = message +
	// Long.toString(countBySAAAndTAA(Configuration.STRATEGY_SAA_ID,
	// Configuration.USER_RESOURCE_PORTFOLIO_CUSTOMIZE))+"|";
	// message = message +
	// Long.toString(countBySAAAndTAA(Configuration.STRATEGY_TAA_ID,
	// Configuration.USER_RESOURCE_PORTFOLIO_CUSTOMIZE));
	// return Action.MESSAGE;
	// }

	// Blairl
	private String jforumID;
	private String symbolsItem;

	public String getSymbolsItem()
	{
		return symbolsItem;
	}

	public void setSymbolsItem(String symbolsItem)
	{
		this.symbolsItem = symbolsItem;
	}

	public String getJforumID()
	{
		return jforumID;
	}

	public void setJforumID(String jforumID)
	{
		this.jforumID = jforumID;
	}

	public String addSymbols()
	{
		StringBuffer sb;
		StringBuffer symbolsItemsbuf = new StringBuffer();
		String[] symbolsItems;
		String postText;
		int postId;
		JforumManager jforumManager = ContextHolder.getJforumManager();
		JforumPostText jt = new JforumPostText();
		postId = jforumManager.getPostIdbyTopicId(Integer.parseInt(jforumID));
		jt = jforumManager.getPostTextByPostID(postId);
		sb = new StringBuffer(jt.getPostText());
		symbolsItems = symbolsItem.split(",");
		for (int i = 0; i < symbolsItems.length; i++)
		{
			jforumManager.addNew(symbolsItems[i], postId, jt.getPostSubject());
			symbolsItems[i] = "<a href=\'/LTISystem/f401k_symbols.action?symbol=" + symbolsItems[i] + "\'>" + symbolsItems[i] + "</a>,";
			symbolsItemsbuf = symbolsItemsbuf.append(symbolsItems[i]);
		}
		if (jt.getPostText().contains("Symbols:"))
		{
			sb.insert(sb.indexOf("Symbols:") + 8, symbolsItemsbuf);
		} else
		{
			sb.insert(sb.length(), "<p>Symbols:" + symbolsItemsbuf + "</p>");
		}
		postText = sb.toString();
		jt.setPostText(postText);
		jt.setID(postId);
		System.out.print(postText);

		jforumManager.update(jt);
		message = "sucess!";
		return Action.MESSAGE;
	}

	public Double getSecurityAlphaWeight()
	{
		return securityAlphaWeight;
	}

	public void setSecurityAlphaWeight(Double securityAlphaWeight)
	{
		this.securityAlphaWeight = securityAlphaWeight;
	}

	public Double getSecurityTreynorWeight()
	{
		return securityTreynorWeight;
	}

	public void setSecurityTreynorWeight(Double securityTreynorWeight)
	{
		this.securityTreynorWeight = securityTreynorWeight;
	}

	public Double getSecurityARWeight()
	{
		return securityARWeight;
	}

	public void setSecurityARWeight(Double securityARWeight)
	{
		this.securityARWeight = securityARWeight;
	}

	public Double getPortfolioARWeight()
	{
		return portfolioARWeight;
	}

	public void setPortfolioARWeight(Double portfolioARWeight)
	{
		this.portfolioARWeight = portfolioARWeight;
	}

	public Double getPortfolioSortinoWeight()
	{
		return portfolioSortinoWeight;
	}

	public void setPortfolioSortinoWeight(Double portfolioSortinoWeight)
	{
		this.portfolioSortinoWeight = portfolioSortinoWeight;
	}

	public Double getPortfolioSharpeWeight()
	{
		return portfolioSharpeWeight;
	}

	public void setPortfolioSharpeWeight(Double portfolioSharpeWeight)
	{
		this.portfolioSharpeWeight = portfolioSharpeWeight;
	}

	public Double getPortfolioTreynorWeight()
	{
		return portfolioTreynorWeight;
	}

	public void setPortfolioTreynorWeight(Double portfolioTreynorWeight)
	{
		this.portfolioTreynorWeight = portfolioTreynorWeight;
	}

	public Double getPortfolioDrawDownWeight()
	{
		return portfolioDrawDownWeight;
	}

	public void setPortfolioDrawDownWeight(Double portfolioDrawDownWeight)
	{
		this.portfolioDrawDownWeight = portfolioDrawDownWeight;
	}

	public Double getPortfolioWinningWeight()
	{
		return portfolioWinningWeight;
	}

	public void setPortfolioWinningWeight(Double portfolioWinningWeight)
	{
		this.portfolioWinningWeight = portfolioWinningWeight;
	}

	public Double getPlanTAAWeight()
	{
		return planTAAWeight;
	}

	public void setPlanTAAWeight(Double planTAAWeight)
	{
		this.planTAAWeight = planTAAWeight;
	}

	public Double getPlanSAAWeight()
	{
		return planSAAWeight;
	}

	public void setPlanSAAWeight(Double planSAAWeight)
	{
		this.planSAAWeight = planSAAWeight;
	}

	public Double getPlanFundQualityWeight()
	{
		return planFundQualityWeight;
	}

	public void setPlanFundQualityWeight(Double planFundQualityWeight)
	{
		this.planFundQualityWeight = planFundQualityWeight;
	}

	public Double getPlanCoverageWeight()
	{
		return planCoverageWeight;
	}

	public void setPlanCoverageWeight(Double planCoverageWeight)
	{
		this.planCoverageWeight = planCoverageWeight;
	}

	public Double getPlanCapabilityWeight()
	{
		return planCapabilityWeight;
	}

	public void setPlanCapabilityWeight(Double planCapabilityWeight)
	{
		this.planCapabilityWeight = planCapabilityWeight;
	}

	/*-------------------------------------------------------*/
	public List<PlanScore> getPlanScoreList()
	{
		return planScoreList;
	}

	public void setPlanScoreList(List<PlanScore> planScoreList)
	{
		this.planScoreList = planScoreList;
	}

	public String getPlanIDArray()
	{
		return planIDArray;
	}

	public void setPlanIDArray(String planIDArray)
	{
		this.planIDArray = planIDArray;
	}

	public List<PlanScore> getPlanScoreListWithIDs()
	{
		return planScoreListWithIDs;
	}

	public void setPlanScoreListWithIDs(List<PlanScore> planScoreListWithIDs)
	{
		this.planScoreListWithIDs = planScoreListWithIDs;
	}

	public boolean getIsOwner()
	{
		return isOwner;
	}

	public void setIsOwner(boolean isOwner)
	{
		this.isOwner = isOwner;
	}

	public boolean isAdmin()
	{
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin)
	{
		this.isAdmin = isAdmin;
	}

	public List<Strategy> getPlans()
	{
		return plans;
	}

	public void setPlans(List<Strategy> plans)
	{
		this.plans = plans;
	}

	public Strategy getCenterplan()
	{
		return centerplan;
	}

	public void setCenterplan(Strategy centerplan)
	{
		this.centerplan = centerplan;
	}

	public Profile getProfile()
	{
		return profile;
	}

	public void setProfile(Profile profile)
	{
		this.profile = profile;
	}

	public List<Strategy> getStrategies()
	{
		return strategies;
	}

	public void setStrategies(List<Strategy> strategies)
	{
		this.strategies = strategies;
	}

	public List<VariableFor401k> getVariables()
	{
		return variables;
	}

	public void setVariables(List<VariableFor401k> variables)
	{
		this.variables = variables;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public Long getID()
	{
		return ID;
	}

	public Strategy getPlan()
	{
		return plan;
	}

	public void setPlan(Strategy plan)
	{
		this.plan = plan;
	}

	public Boolean getAppend()
	{
		return append;
	}

	public void setAppend(Boolean append)
	{
		this.append = append;
	}

	public File getUpload()
	{
		return upload;
	}

	public void setUpload(File upload)
	{
		this.upload = upload;
	}

	public String getUploadContentType()
	{
		return uploadContentType;
	}

	public void setUploadContentType(String uploadContentType)
	{
		this.uploadContentType = uploadContentType;
	}

	public String getUploadFileName()
	{
		return uploadFileName;
	}

	public void setUploadFileName(String uploadFileName)
	{
		this.uploadFileName = uploadFileName;
	}

	public List<ModelPortfoliosBean> getModelPortfoliosBeans()
	{
		return modelPortfoliosBeans;
	}

	public void setModelPortfoliosBeans(List<ModelPortfoliosBean> modelPortfoliosBeans)
	{
		this.modelPortfoliosBeans = modelPortfoliosBeans;
	}

	public void setID(Long id)
	{
		ID = id;
	}

	public User getUser()
	{
		return user;
	}

	public void setUser(User user)
	{
		this.user = user;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Long getMainStrategyID()
	{
		return MainStrategyID;
	}

	public void setMainStrategyID(Long mainStrategyID)
	{
		MainStrategyID = mainStrategyID;
	}

	public String getOriginalString()
	{
		return originalString;
	}

	public void setOriginalString(String originalString)
	{
		this.originalString = originalString;
	}

	public String getAction_name()
	{
		return action_name;
	}

	public void setAction_name(String action_name)
	{
		this.action_name = action_name;
	}

	public String getParams()
	{
		return params;
	}

	public void setParams(String params)
	{
		this.params = params;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getShortDescription()
	{
		return shortDescription;
	}

	public void setShortDescription(String shortDescription)
	{
		this.shortDescription = shortDescription;
	}

	public String getReference()
	{
		return reference;
	}

	public void setReference(String reference)
	{
		this.reference = reference;
	}

	public String getStartingdate()
	{
		return startingdate;
	}

	public void setStartingdate(String startingdate)
	{
		this.startingdate = startingdate;
	}

	public List<Portfolio> getPortfolios()
	{
		return portfolios;
	}

	public void setPortfolios(List<Portfolio> portfolios)
	{
		this.portfolios = portfolios;
	}

	public long getUserID()
	{
		return userID;
	}

	public void setUserID(long userID)
	{
		this.userID = userID;
	}

	public Map<String, String> getPlanIDAndName()
	{
		return planIDAndName;
	}

	public void setPlanIDAndName(Map<String, String> planIDAndName)
	{
		this.planIDAndName = planIDAndName;
	}

	public boolean isRflag()
	{
		return rflag;
	}

	public void setRflag(boolean rflag)
	{
		this.rflag = rflag;
	}

	public List<String> getFileList()
	{
		return fileList;
	}

	public void setFileList(List<String> fileList)
	{
		this.fileList = fileList;
	}

	public String getFileName()
	{
		return fileName;
	}

	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	public String getFilecontent()
	{
		return filecontent;
	}

	public void setFilecontent(String filecontent)
	{
		this.filecontent = filecontent;
	}

	public long getTopicID()
	{
		return topicID;
	}

	public void setTopicID(long topicID)
	{
		this.topicID = topicID;
	}

	public List<PostBean> getPostList()
	{
		return postList;
	}

	public void setPostList(List<PostBean> postList)
	{
		this.postList = postList;
	}

	public PlanScore getPlanScore()
	{
		return planScore;
	}

	public void setPlanScore(PlanScore planScore)
	{
		this.planScore = planScore;
	}

	public List<PlanScore> getPlanScoreTopList()
	{
		return planScoreTopList;
	}

	public void setPlanScoreTopList(List<PlanScore> planScoreTopList)
	{
		this.planScoreTopList = planScoreTopList;
	}

	public List<PlanScore> getPlanScoreBottomList()
	{
		return planScoreBottomList;
	}

	public void setPlanScoreBottomList(List<PlanScore> planScoreBottomList)
	{
		this.planScoreBottomList = planScoreBottomList;
	}

	public com.lti.type.PaginationSupport getjTopicsOfAnnouncements()
	{
		return jTopicsOfAnnouncements;
	}

	public void setjTopicsOfAnnouncements(com.lti.type.PaginationSupport jTopicsOfAnnouncements)
	{
		this.jTopicsOfAnnouncements = jTopicsOfAnnouncements;
	}

	public com.lti.type.PaginationSupport getjTopicsOfNewsAndCommentaries()
	{
		return jTopicsOfNewsAndCommentaries;
	}

	public void setjTopicsOfNewsAndCommentaries(com.lti.type.PaginationSupport jTopicsOfNewsAndCommentaries)
	{
		this.jTopicsOfNewsAndCommentaries = jTopicsOfNewsAndCommentaries;
	}

	public String getRunInServerFlag()
	{
		return runInServerFlag;
	}

	public void setRunInServerFlag(String runInServerFlag)
	{
		this.runInServerFlag = runInServerFlag;
	}

	public List<JforumTopics> getAnnouncementsList()
	{
		return announcementsList;
	}

	public void setAnnouncementsList(List<JforumTopics> announcementsList)
	{
		this.announcementsList = announcementsList;
	}

	public List<JforumTopics> getjTList()
	{
		return jTList;
	}

	public void setjTList(List<JforumTopics> jTList)
	{
		this.jTList = jTList;
	}

	public List<JforumTopics> getTopics_homepage()
	{
		return topics_homepage;
	}

	public void setTopics_homepage(List<JforumTopics> topicsHomepage)
	{
		topics_homepage = topicsHomepage;
	}

	public int getPortfolioUpdate()
	{
		return portfolioUpdate;
	}

	public void setPortfolioUpdate(int portfolioUpdate)
	{
		this.portfolioUpdate = portfolioUpdate;
	}

	public String getPlanNameArray()
	{
		return planNameArray;
	}

	public void setPlanNameArray(String planNameArray)
	{
		this.planNameArray = planNameArray;
	}

	public Map<String, CachePortfolioItem> getSAAMap()
	{
		return SAAMap;
	}

	public void setSAAMap(Map<String, CachePortfolioItem> sAAMap)
	{
		SAAMap = sAAMap;
	}

	public Map<String, CachePortfolioItem> getTAAMap()
	{
		return TAAMap;
	}

	public void setTAAMap(Map<String, CachePortfolioItem> tAAMap)
	{
		TAAMap = tAAMap;
	}

	public String getModerateSAANames()
	{
		return moderateSAANames;
	}

	public void setModerateSAANames(String moderateSAANames)
	{
		this.moderateSAANames = moderateSAANames;
	}

	public String getModerateTAANames()
	{
		return moderateTAANames;
	}

	public void setModerateTAANames(String moderateTAANames)
	{
		this.moderateTAANames = moderateTAANames;
	}

	public String getEndDate()
	{
		return endDate;
	}

	public void setEndDate(String endDate)
	{
		this.endDate = endDate;
	}

	public List<Pair> getAttributes()
	{
		return attributes;
	}

	public void setAttributes(List<Pair> attributes)
	{
		this.attributes = attributes;
	}

	public boolean isTurnView()
	{
		return turnView;
	}

	public void setTurnView(boolean turnView)
	{
		this.turnView = turnView;
	}

	public String getHost()
	{
		return host;
	}

	public void setHost(String host)
	{
		this.host = host;
	}

	public List<PlanArticle> getArticles()
	{
		return articles;
	}

	public void setArticles(List<PlanArticle> articles)
	{
		this.articles = articles;
	}

	public List<PlanArticle> getShowArticles()
	{
		return showArticles;
	}

	public void setShowArticles(List<PlanArticle> showArticles)
	{
		this.showArticles = showArticles;
	}

	public int getPlanType()
	{
		return planType;
	}

	public void setPlanType(int planType)
	{
		this.planType = planType;
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	public String getTicker()
	{
		return ticker;
	}

	public void setTicker(String ticker)
	{
		this.ticker = ticker;
	}

	public List<String> getCompanyList()
	{
		return companyList;
	}

	public void setCompanyList(List<String> companyList)
	{
		this.companyList = companyList;
	}

	public List<String> getCategoryList()
	{
		return categoryList;
	}

	public void setCategoryList(List<String> categoryList)
	{
		this.categoryList = categoryList;
	}

	public String getCompany()
	{
		return company;
	}

	public void setCompany(String company)
	{
		this.company = company;
	}

	public String getCategory1()
	{
		return category1;
	}

	public void setCategory1(String category1)
	{
		this.category1 = category1;
	}

	public String getCategory2()
	{
		return category2;
	}

	public void setCategory2(String category2)
	{
		this.category2 = category2;
	}

	public String getCategory3()
	{
		return category3;
	}

	public void setCategory3(String category3)
	{
		this.category3 = category3;
	}

	public List<CompanyFund> getCompanyFundList()
	{
		return companyFundList;
	}

	public void setCompanyFundList(List<CompanyFund> companyFundList)
	{
		this.companyFundList = companyFundList;
	}

	public List<VAFund> getVaFundList()
	{
		return vaFundList;
	}

	public void setVaFundList(List<VAFund> vaFundList)
	{
		this.vaFundList = vaFundList;
	}

	public List<VAFund> getVaFundSearchList()
	{
		return vaFundSearchList;
	}

	public void setVaFundSearchList(List<VAFund> vaFundSearchList)
	{
		this.vaFundSearchList = vaFundSearchList;
	}

	public String getSearchType()
	{
		return searchType;
	}

	public void setSearchType(String searchType)
	{
		this.searchType = searchType;
	}

	public String getBarronName()
	{
		return barronName;
	}

	public void setBarronName(String barronName)
	{
		this.barronName = barronName;
	}

	public int getFundIndex()
	{
		return fundIndex;
	}

	public void setFundIndex(int fundIndex)
	{
		this.fundIndex = fundIndex;
	}

	public int getSearchCategory()
	{
		return searchCategory;
	}

	public void setSearchCategory(int searchCategory)
	{
		this.searchCategory = searchCategory;
	}

	public String getFullName()
	{
		return fullName;
	}

	public void setFullName(String fullName)
	{
		this.fullName = fullName;
	}

	public int getUpdateNum()
	{
		return updateNum;
	}

	public void setUpdateNum(int updateNum)
	{
		this.updateNum = updateNum;
	}

	public List<SecurityState> getSecurityPriceSuccessStateList()
	{
		return securityPriceSuccessStateList;
	}

	public void setSecurityPriceSuccessStateList(List<SecurityState> securityPriceSuccessStateList)
	{
		this.securityPriceSuccessStateList = securityPriceSuccessStateList;
	}

	public List<SecurityState> getSecurityPriceFailStateList()
	{
		return securityPriceFailStateList;
	}

	public void setSecurityPriceFailStateList(List<SecurityState> securityPriceFailStateList)
	{
		this.securityPriceFailStateList = securityPriceFailStateList;
	}

	public List<String> getUpdateSummary()
	{
		return updateSummary;
	}

	public void setUpdateSummary(List<String> updateSummary)
	{
		this.updateSummary = updateSummary;
	}

	public String getDateStr()
	{
		return dateStr;
	}

	public void setDateStr(String dateStr)
	{
		this.dateStr = dateStr;
	}

	public List<SecurityState> getCefPriceSuccessStateList()
	{
		return cefPriceSuccessStateList;
	}

	public void setCefPriceSuccessStateList(List<SecurityState> cefPriceSuccessStateList)
	{
		this.cefPriceSuccessStateList = cefPriceSuccessStateList;
	}

	public List<SecurityState> getCefPriceFailStateList()
	{
		return cefPriceFailStateList;
	}

	public void setCefPriceFailStateList(List<SecurityState> cefPriceFailStateList)
	{
		this.cefPriceFailStateList = cefPriceFailStateList;
	}

	public List<SecurityState> getEtfPriceSuccessStateList()
	{
		return etfPriceSuccessStateList;
	}

	public void setEtfPriceSuccessStateList(List<SecurityState> etfPriceSuccessStateList)
	{
		this.etfPriceSuccessStateList = etfPriceSuccessStateList;
	}

	public List<SecurityState> getEtfPriceFailStateList()
	{
		return etfPriceFailStateList;
	}

	public void setEtfPriceFailStateList(List<SecurityState> etfPriceFailStateList)
	{
		this.etfPriceFailStateList = etfPriceFailStateList;
	}

	public List<SecurityState> getSecurityNAVSuccessStateList()
	{
		return securityNAVSuccessStateList;
	}

	public void setSecurityNAVSuccessStateList(List<SecurityState> securityNAVSuccessStateList)
	{
		this.securityNAVSuccessStateList = securityNAVSuccessStateList;
	}

	public List<SecurityState> getSecurityNAVFailStateList()
	{
		return securityNAVFailStateList;
	}

	public void setSecurityNAVFailStateList(List<SecurityState> securityNAVFailStateList)
	{
		this.securityNAVFailStateList = securityNAVFailStateList;
	}

	public com.lti.type.PaginationSupport getjTopicsOfRetirement()
	{
		return jTopicsOfRetirement;
	}

	public void setjTopicsOfRetirement(com.lti.type.PaginationSupport jTopicsOfRetirement)
	{
		this.jTopicsOfRetirement = jTopicsOfRetirement;
	}
	
	SecurityManager securityManager = ContextHolder.getSecurityManager();
	PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();
	private static int process;
	private static int sumProcess;
	public int array(int[] a)
	{
		java.util.Arrays.sort(a);
		return a[a.length - 1];
	}

	public String process()
	{
		int[] a =
		{ securityManager.getProcess(), process};
		int[] b =
		{ securityManager.getSumProcess(), sumProcess};
		int i = array(a);
		int j = array(b);
		message = i + "sum=" + j;
		return Action.MESSAGE;
	}
	
	private Long stop = 0l;
	
	public Long getStop() {
		return stop;
	}

	public void setStop(Long stop) {
		this.stop = stop;
	}

	Thread thread = new Thread(){
		public void run(){
			try	{
				if(dd.equals("updateAr") && radioArea.equals("a"))
				{
					isUpdating = true;
					securityManager.updateAllAr();
				} else if(dd.equals("updateMpt") && radioArea.equals("a"))
				{
					isUpdating = true;
					securityManager.updateAllMpt();
				}
			}finally{
				isUpdating = false;
			}
		}
	};
	
	
	public String updateAr(){
		dd = "updateAr";
		try{
			if (radio.equals("Security")){
				if (radioArea.equals("a")){
					if(!isUpdating)	{
						thread.start();
					}else
						message = "isUpdating";
				} else{
					if (securityManager.updateAr(radioArea))
						message = "SUCCESSid=" + radioArea.trim().toUpperCase();
					else
						message = "ERRORid=" + radioArea.trim().toUpperCase();
				}
			} else if (radio.equals("Portfolio")){
				if (radioArea.equals("0")){
					updateAllPortfolioAr();
				} else{
					if (updatePortfolioAr(Long.parseLong(radioArea.trim())))
						message = "SUCCESSid=" + radioArea.trim().toUpperCase();
					else
						message = "ERRORid=" + radioArea.trim().toUpperCase();
				}
			}
		} catch (Exception e){
			message = "ERRORid=" + radioArea.trim().toUpperCase();
		}
		return Action.MESSAGE;
	}
	public static String dd;
	public String updateMpt()
	{
		dd = "updateMpt";
		try
		{
			if(stop == 1)
			{
				securityManager.setIsRun(false);
				isUpdating = false;
				message = "Stoped";
			}
			else if(radio.equals("Security"))
			{
				if (radioArea.equals("a"))
				{
					if(!isUpdating){
						thread.start();
				}else
					message = "isUpdating";
				}else
				{
					if (securityManager.updateMpt(radioArea))
						message = "SUCCESSid=" + radioArea.trim().toUpperCase();
					else
						message = "ERRORid=" + radioArea.trim().toUpperCase();
				}
			}else if (radio.equals("Portfolio"))
			{
				
				if (radioArea.equals("0"))
				{
					updateAllPortfolioMpt();
				} else
				{
					if(updatePortfolioMpt(Long.parseLong(radioArea.trim())))
						message = "SUCCESSid=" + radioArea.trim().toUpperCase();
					else
						message = "ERRORid=" + radioArea.trim().toUpperCase();
				}
			}
		} catch (Exception e)
		{
			message = "ERRORid=" + radioArea.trim().toUpperCase();
			e.printStackTrace();
		}
		return Action.MESSAGE;
	}
	public boolean updatePortfolioAr(long id) {
		PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();
		boolean b = false;
		BaseFormulaUtil baseFormulaUtil = new BaseFormulaUtil();
		try {
			Portfolio portfolio = portfolioManager.get(id);
			List<PortfolioDailyData> pdds = portfolioManager.getDailydatas(portfolio.getID());
			List<PortfolioMPT> portfolioMPTList = portfolioManager.getEveryYearsMPT(portfolio.getID());
			List<PortfolioMPT> list = baseFormulaUtil.computePortfolioMPTsForAR(portfolio.getID(), portfolio.getStartingDate(), pdds.get(pdds.size() - 1), portfolioMPTList);
			portfolioManager.saveMPTs(list);
			b = true;
		} catch (Exception e) {
			b = false;
		}
		return b;
	}
	public void updateAllPortfolioAr() {
		BaseFormulaUtil baseFormulaUtil = new BaseFormulaUtil();
		List<Portfolio> portfolioes = portfolioManager.getPortfolios();
		sumProcess = portfolioes.size();
		for (int i = 0; i < portfolioes.size(); i++) {
			List<PortfolioDailyData> pdds = portfolioManager.getDailydatas(portfolioes.get(i).getID());
			List<PortfolioMPT> portfolioMPTList = portfolioManager.getEveryYearsMPT(portfolioes.get(i).getID());
			try {
				List<PortfolioMPT> list = baseFormulaUtil.computePortfolioMPTsForAR(portfolioes.get(i).getID(), portfolioes.get(i).getStartingDate(), pdds.get(pdds.size() - 1), portfolioMPTList);
				portfolioManager.saveMPTs(list);
			} catch (Exception e) {
			}
			process = i;
		}
	}
	public boolean updatePortfolioMpt(long id) {
		boolean b = false;
		BaseFormulaUtil baseFormulaUtil = new BaseFormulaUtil();
		SecurityManager securityManager = ContextHolder.getSecurityManager();
		StrategyManager strategyManager = ContextHolder.getStrategyManager();
		Long classID = null;

		List<Transaction> transactionList = null;

		try {
			Portfolio portfolio = portfolioManager.get(id);
			classID = strategyManager.get(portfolio.getMainStrategyID()).getStrategyClassID();
			if (classID != null && classID == 5l) {
				transactionList = portfolioManager.getCashFlowTransaction(portfolio.getID());
			}
			List<PortfolioDailyData> pdds = portfolioManager.getDailydatas(portfolio.getID());
			List<PortfolioMPT> portfolioMPTList = portfolioManager.getEveryYearsMPT(portfolio.getID());
			long cashID = securityManager.get("CASH").getID();

			Date startDate = pdds.get(0).getDate();
			Date endDate = pdds.get(pdds.size() - 1).getDate();

			List<SecurityDailyData> sdds = securityManager.getDailyDatas(14l, LTIDate.getNewWeekDay(startDate, -1), LTIDate.getNewWeekDay(endDate, 1));
			List<SecurityDailyData> fdds = securityManager.getDailyDatas(cashID, startDate, endDate);
			List<PortfolioMPT> simulateMPTs = baseFormulaUtil.computePortfolioMPTs(portfolio, classID, pdds, sdds, fdds, portfolioMPTList, transactionList);
			System.out.println(portfolio.getName());

			portfolioManager.saveMPTs(simulateMPTs);
			b = true;
		} catch (Exception e) {
			b = false;
			e.printStackTrace();
		}
		return b;
	}

	public void updateAllPortfolioMpt() {
		SecurityManager securityManager = ContextHolder.getSecurityManager();
		BaseFormulaUtil baseFormulaUtil = new BaseFormulaUtil();
		List<Portfolio> portfolioes = portfolioManager.getPortfolios();
		StrategyManager strategyManager = ContextHolder.getStrategyManager();
		Long classID = null;
		List<Transaction> transactionList = null;
		sumProcess = portfolioes.size();
		for (int i = 0; i < portfolioes.size(); i++) {
			Portfolio portfolio = portfolioManager.get(portfolioes.get(i).getID());
			List<PortfolioDailyData> pdds = portfolioManager.getDailydatas(portfolio.getID());
			try {
				classID = strategyManager.get(portfolio.getMainStrategyID()).getStrategyClassID();
				if (classID != null && classID == 5l) {
					transactionList = portfolioManager.getCashFlowTransaction(portfolio.getID());
				}
				List<PortfolioMPT> portfolioMPTList = portfolioManager.getEveryYearsMPT(portfolio.getID());
				long cashID = securityManager.get("CASH").getID();

				Date startDate = pdds.get(0).getDate();
				Date endDate = pdds.get(pdds.size() - 1).getDate();

				List<SecurityDailyData> sdds = securityManager.getDailyDatas(14l, LTIDate.getNewWeekDay(startDate, -1), LTIDate.getNewWeekDay(endDate, 1));
				List<SecurityDailyData> fdds = securityManager.getDailyDatas(cashID, startDate, endDate);
				List<PortfolioMPT> simulateMPTs = baseFormulaUtil.computePortfolioMPTs(portfolio, classID, pdds, sdds, fdds, portfolioMPTList, transactionList);
				System.out.println(portfolio.getName());
				portfolioManager.saveMPTs(simulateMPTs);
			} catch (Exception e) {
			}
			process = i;
		}
	}
	
	public String listQuality()
	{
		List<Object[]> list = securityManager.getQuality(symbol);
		for(Object[] date : list)
		{
			message = message + date[1] + ":" +date[0] +"|";
		}
		return Action.MESSAGE;
	}
	/*-------------------------------------------------------*/
}
