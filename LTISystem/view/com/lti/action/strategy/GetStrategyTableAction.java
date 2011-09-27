package com.lti.action.strategy;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.lti.action.Action;
import com.lti.action.getstartedaction;
import com.lti.bean.StrategyItem;
import com.lti.permission.MPIQChecker;
import com.lti.permission.PublicMaker;
import com.lti.permission.ValidFiChecker;
import com.lti.service.GroupManager;
import com.lti.service.JforumManager;
import com.lti.service.PortfolioManager;
import com.lti.service.StrategyClassManager;
import com.lti.service.StrategyManager;
import com.lti.service.UserManager;
import com.lti.service.bo.CacheStrategyItem;
import com.lti.service.bo.PortfolioMPT;
import com.lti.service.bo.Strategy;
import com.lti.service.bo.User;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.util.FormatUtil;
import com.lti.util.StringUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class GetStrategyTableAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(GetStrategyTableAction.class);
	private String url="";
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	private int pageCount = 0;
	public int getPageCount() {
		return pageCount;
	}
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	private int page=1;
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	
	private String urlPrefix;
	
	
	public String getUrlPrefix() {
		return urlPrefix;
	}

	public void setUrlPrefix(String urlPrefix) {
		this.urlPrefix = urlPrefix;
	}
	
	private String portfolioUrlPrefix;
	
	private String allPlanPageSize;

	public String getAllPlanPageSize() {
		return allPlanPageSize;
	}
	public void setAllPlanPageSize(String allPlanPageSize) {
		this.allPlanPageSize = allPlanPageSize;
	}

	private Boolean isSearch = false;

	public Boolean getIsSearch() {
		return isSearch;
	}
	public void setIsSearch(Boolean isSearch) {
		this.isSearch = isSearch;
	}

	private Boolean isGoogleSearch = false;
	public Boolean getIsGoogleSearch() {
		return isGoogleSearch;
	}

	public void setIsGoogleSearch(Boolean isGoogleSearch) {
		this.isGoogleSearch = isGoogleSearch;
	}

	private StrategyManager strategyManager;

	private StrategyClassManager strategyClassManager;

	private Boolean redirect;
	
	private Long userID;
	private String groupIDs;
	private Boolean owner = false;
	private boolean permission=false;
	private boolean showUser=false;
	
	public boolean isShowUser() {
		return showUser;
	}
	public void setShowUser(boolean showUser) {
		this.showUser = showUser;
	}
	public boolean isPermission() {
		return permission;
	}

	public void setPermission(boolean permission) {
		this.permission = permission;
	}

	private Boolean admin = false;
	private String mainStrategyName=null;

	private PortfolioManager portfolioManager;

	private UserManager userManager;
	private GroupManager groupManager;

	private Boolean sortByDesc = true;
	
	private Integer size = 0;

	private String keyword;
	private String q="";


	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}

	private Integer strategyClassID;
	private String categories;

	private Integer sortType = com.lti.service.bo.PortfolioMPT.SORT_BY_SHARPERATIO;
	private Integer sortYear = PortfolioMPT.LAST_ONE_YEAR;

	private Boolean includeID = false;
	private Boolean includeCategory = false;
	private Boolean includePortfolio = true;
	private Boolean includeLastValidDate = false;
	private Boolean includeLastTransactionDate = false;
	private Boolean includeModify = false;
	private Boolean hasMore=false;
	private String year = "-1,-3,-5";
	private String mpt = "ar,beta,sharperatio";
	private String title=null;
	private Boolean expired = false;
	private Boolean includeApprove = false;
	
	private Long type=Configuration.STRATEGY_TYPE_NORMAL;

	private List<StrategyItem> items;

	public Integer getSortYear() {
		return sortYear;
	}

	public void setSortYear(Integer sortYear) {
		this.sortYear = sortYear;
	}
	
	public Boolean getIncludeModify() {
		return includeModify;
	}
	public void setIncludeModify(Boolean includeModify) {
		this.includeModify = includeModify;
	}

	private String ids;
	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}
	
	private String getHql() throws Exception{
		Long[] groupids=getGroups();
		StringBuffer sb=new StringBuffer();
		if (owner) {
			sb.append("from CacheStrategyItem gpi where gpi.UserID=");
			sb.append(userID);
			sb.append(" and gpi.GroupID=0");
			
		} else if (userID.equals(Configuration.SUPER_USER_ID)&&admin) {
			sb.append("from CacheStrategyItem gpi where gpi.GroupID=0");
			
		} else {
			sb.append("from CacheStrategyItem gpi where ");
			
			if(groupids.length==1){
				sb.append("gpi.GroupID=");
				sb.append(groupids[0]);
			}else{
				for (int i = 0; i < groupids.length; i++) {
					Long gid = groupids[i];
					if(i==0){
						sb.append("(");
						sb.append("gpi.GroupID=");
						sb.append(gid);
					}else {
						sb.append(" or ");
						sb.append("gpi.GroupID=");
						sb.append(gid);
					}
					if(i==groupids.length-1){
						sb.append(")");
					}
				}
			}
			
					
		}
		if(keyword!=null&&!keyword.equals("")&&!keyword.equals("nothing")&&q.equals("nothing")){
			sb.append(" and gpi.StrategyName like '%");
			sb.append(keyword);
			sb.append("%'");
		}
		
		if(q!=null&&!q.equals("")&&!q.equals("nothing")){
			sb.append(" and gpi.StrategyName like '%");
			sb.append(q);
			sb.append("%'");
		}
		
		if (categories != null && !categories.equals("")) {
			String[] CategoryStrs = StringUtil.splitCategories(categories);
			String categoryMatchStr = StringUtil.categoryString(CategoryStrs);
			sb.append(" and gpi.Categories like '%");
			sb.append(categoryMatchStr);
			sb.append("%'");
		}
		
		if (mainStrategyName != null && !mainStrategyName.equals("")) {
			Strategy mainStrategy=strategyManager.get(mainStrategyName);
			sb.append(" and gpi.MainStrategyID=");
			sb.append(mainStrategy.getID());
		}
		sb.append(" and bit_and(gpi.Type,");
		sb.append(type);
		sb.append(") >0 ");
		
		if (strategyClassID != null) {
			sb.append(" and gpi.ClassID=");
			sb.append(strategyClassID);

		}
		
		if(ids!=null){
			String[] strs=ids.split("\\|");
			sb.append(" and (");
			for(int i=0;i<strs.length;i++){
				long id=Long.parseLong(strs[i]);
				sb.append("gpi.StrategyID=");
				sb.append(id);
				if(i!=strs.length-1)sb.append(" or ");
			}
			sb.append(")");
		}
		
		if (sortYear == PortfolioMPT.LAST_ONE_YEAR) {
			if (sortType == PortfolioMPT.SORT_BY_SHARPERATIO) {
				sb.append(" order by gpi.SharpeRatio1");
			} else if (sortType == PortfolioMPT.SORT_BY_AR) {
				sb.append(" order by gpi.AR1");
			}
		} else if (sortYear == PortfolioMPT.LAST_THREE_YEAR) {
			if (sortType == PortfolioMPT.SORT_BY_SHARPERATIO) {
				sb.append(" order by gpi.SharpeRatio3");
			} else if (sortType == PortfolioMPT.SORT_BY_AR) {
				sb.append(" order by gpi.AR3");
			}
		} else if (sortYear == PortfolioMPT.LAST_FIVE_YEAR) {
			if (sortType == PortfolioMPT.SORT_BY_SHARPERATIO) {
				sb.append(" order by gpi.SharpeRatio3");
			} else if (sortType == PortfolioMPT.SORT_BY_AR) {
				sb.append(" order by gpi.AR5");
			}
		}
		if (sortByDesc) {
			sb.append(" desc");
		} else {
			sb.append(" asc");
		}
		
		return sb.toString();
	}
	
	
	
	
	
	private boolean inGroups(Object[] groups,Object group){
		if(group.equals(Configuration.GROUP_ANONYMOUS_ID)){
			return true;
		}
		for(int i=0;i<groups.length;i++){
			if(groups[i].equals(group))return true;
		}
		return false;
	}
	private Integer width;
	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	private Long[] getGroups() throws Exception{
		HttpServletRequest request = ServletActionContext.getRequest();
		String servername=request.getServerName();
		boolean isf401k=Configuration.isF401KDomain(servername);
		if (groupIDs == null || groupIDs.equals("")) {
			if (userID.equals(Configuration.USER_ANONYMOUS)) {
				groupIDs = Configuration.GROUP_ANONYMOUS_ID + "";
				return new Long[]{Configuration.GROUP_ANONYMOUS_ID};
			} else {
				groupIDs = Configuration.GROUP_ANONYMOUS_ID + ",";
				if(isf401k){
					groupIDs += Configuration.GROUP_MPIQ_ID;
					return new Long[]{Configuration.GROUP_ANONYMOUS_ID, Configuration.GROUP_MPIQ_ID};
				}
				else{
					groupIDs += Configuration.GROUP_MEMBER_ID;
					return new Long[]{Configuration.GROUP_ANONYMOUS_ID, Configuration.GROUP_MEMBER_ID};
				}
				
			}
		}
		Object[] groups = groupManager.getGroupIDs(userID);
		String[] ss = groupIDs.split(",");
		Long[] ids = new Long[ss.length];
		for (int i = 0; i < ids.length; i++) {
			Long g= Long.parseLong(ss[i]);
			if(inGroups(groups, g)||userID.equals(Configuration.SUPER_USER_ID)){
				ids[i]=g;
			}
			else{
				throw new Exception("Invalid access.");
			}
		}
		return ids;
	}
	
	private int inStrategies(Long id){
		for(int i=0;i<items.size();i++){
			if(items.get(i).getID().equals(id))return i;
		}
		return -1;
	}

	private String message;
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	private Boolean hasSubscred=false;
	public Boolean getHasSubscred() {
		return hasSubscred;
	}

	public void setHasSubscred(Boolean hasSubscred) {
		this.hasSubscred = hasSubscred;
	}

	@Override
	public String execute() throws Exception {
		User user = userManager.getLoginUser();
		userID = user.getID();

		MPIQChecker fc=new MPIQChecker(userID);
		ValidFiChecker vc=new ValidFiChecker(userID);
		if(fc.hasSubscred()||vc.hasSubscred()){
			hasSubscred=true;
		}
		
		String hql = null;
		String hqlPerKeyWord  = null;
		try{
			hql=getHql();
		}catch(Exception e){
			message=e.getMessage();
			return Action.MESSAGE;
		}
		List<CacheStrategyItem> strategies = strategyManager.findByHQL(hql);
		//List<CacheStrategyItem> strategiesPerWord = strategyManager.findByHQL(hqlPerKeyWord );

		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		
		if (strategies != null && strategies.size() > 0) {
			
			List<Long> planIDList = userManager.getResourceIDListByUserIDAndResourceType(userID, Configuration.USER_RESOURCE_PLAN_CREATE);
			List<Long> expiredPlanIDList = userManager.getResourceIDListByUserIDAndResourceType(userID, Configuration.USER_RESOURCE_PLAN_CREATE_EXPIRED);
			//getstartedaction.writeTo(keyword, user.getUserName(), true);
			getstartedaction.writeTo(q, user.getUserName(), true);
			items = new ArrayList<StrategyItem>();
			for (int i = 0; i < strategies.size(); i++) {
				CacheStrategyItem s = strategies.get(i);
				if(owner&&type.longValue()!=Configuration.STRATEGY_TYPE_CONSUMER){//过滤掉expired的
					if(expired){//只选择过期的
						if(expiredPlanIDList == null || !expiredPlanIDList.contains(s.getStrategyID()))
							continue;
					}else if(planIDList == null || !planIDList.contains(s.getStrategyID()))//选择不过期的
						continue;
				}
				if(s.getStrategyID().equals(0l))continue;
				if(s.getStrategyName()==null)continue;
				int pos=inStrategies (s.getStrategyID());
				if (pos!=-1) {
					if(s.getEndDate()==null)continue;
					Date ed1=null;
					try {
						ed1=sdf.parse(items.get(pos).getLastValidDate());
						if(ed1.after(s.getEndDate()))continue;
					} catch (Exception e) {
					}
					items.remove(pos);
				} 
				StrategyItem item = new StrategyItem();
				item.setID(s.getStrategyID());
				item.setName(s.getStrategyName());
				item.setCategoryName(s.getCategories());
				item.setPortfolioID(s.getPortfolioID());
				item.setPortfolioName(s.getPortfolioName()==null?"NA":s.getPortfolioName());
				item.setLastValidDate(s.getEndDate()==null?"":sdf.format(s.getEndDate()));
				item.setLastTransactionDate(s.getLastTransactionDate()==null?"":sdf.format(s.getLastTransactionDate()));

				item.setAR1(FormatUtil.formatPercentage(s.getAR1()));
				item.setSharpeRatio1(FormatUtil.formatPercentage(s.getSharpeRatio1()));
				item.setAR3(FormatUtil.formatPercentage(s.getAR3()));
				item.setSharpeRatio3(FormatUtil.formatPercentage(s.getSharpeRatio3()));
				item.setAR5(FormatUtil.formatPercentage(s.getAR5()));
				item.setSharpeRatio5(FormatUtil.formatPercentage(s.getSharpeRatio5()));
				
				item.setUserName(s.getUserName());
				item.setUserID(s.getUserID());

				if(permission){
					PublicMaker publicMaker=new PublicMaker(item);
					item.setPublic(publicMaker.isPublic());
				}
				
				items.add(item);
				if (size != 0 && items.size() >= size) {
					hasMore = true;
					break;
				}
			}
			try{
				pageCount = (Integer.valueOf(allPlanPageSize) + items.size() - 1)/Integer.valueOf(allPlanPageSize);
			}catch(Exception e){
				//e.printStackTrace();
			}
		}else if(isSearch == true){
			getstartedaction.writeTo(q, user.getUserName(), false);
			isGoogleSearch=true;
		}else{
			getstartedaction.writeTo(q, user.getUserName(), false);
		}
		return Action.SUCCESS;

	}


	private Long ID;
    public String approve(){
    	message = "";
    	if(ID==null){
    		message="failed";
    		return Action.MESSAGE;
    	}
    	StrategyManager strategyManager = ContextHolder.getStrategyManager();
    	UserManager userManager = ContextHolder.getUserManager();
    	JforumManager jm=ContextHolder.getJforumManager();
    	Strategy plan = strategyManager.get(ID);
    	SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
    	String date = sdf.format(new Date());
    	long userID = plan.getUserID();
    	User user = userManager.get(userID);
    	String userName = "";
    	if(user!=null){
    		userName = user.getUserName();
    	}else{
    		 userName = "Admin";
    	}
    	
    	String text = "The plan was originally created by our user "+userName+". It was approved on "+date+".";
    	jm.addComment(61,plan.getID(), user.getUserName(), plan.getName(), text, "abc.com");

    	plan.setConsumerPlan(false);
    	plan.setUserID(1l);
    	strategyManager.update(plan);
    	message = "success";
    	return Action.MESSAGE;
    }
	
	
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public Integer getSortType() {
		return sortType;
	}

	public void setSortType(Integer sortType) {
		this.sortType = sortType;
	}

	public List<StrategyItem> getItems() {
		return items;
	}

	public void setItems(List<StrategyItem> items) {
		this.items = items;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getCategories() {
		return categories;
	}

	public void setCategories(String categories) {
		this.categories = categories;
	}

	public void setPortfolioManager(PortfolioManager portfolioManager) {
		this.portfolioManager = portfolioManager;
	}

	public void setStrategyManager(StrategyManager strategyManager) {
		this.strategyManager = strategyManager;
	}

	public void setStrategyClassManager(StrategyClassManager strategyClassManager) {
		this.strategyClassManager = strategyClassManager;
	}


	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Boolean getSortByDesc() {
		return sortByDesc;
	}

	public void setSortByDesc(Boolean sortByDesc) {
		this.sortByDesc = sortByDesc;
	}

	public Integer getStrategyClassID() {
		return strategyClassID;
	}

	public void setStrategyClassID(Integer strategyClassID) {
		this.strategyClassID = strategyClassID;
	}

	public String getGroupIDs() {
		return groupIDs;
	}

	public void setGroupIDs(String groupIDs) {
		this.groupIDs = groupIDs;
	}

	public void setGroupManager(GroupManager groupManager) {
		this.groupManager = groupManager;
	}


	public Boolean getIncludeID() {
		return includeID;
	}

	public void setIncludeID(Boolean includeID) {
		this.includeID = includeID;
	}

	public Boolean getIncludeCategory() {
		return includeCategory;
	}

	public void setIncludeCategory(Boolean includeCategory) {
		this.includeCategory = includeCategory;
	}


	public Boolean getIncludePortfolio() {
		return includePortfolio;
	}

	public void setIncludePortfolio(Boolean includePortfolio) {
		this.includePortfolio = includePortfolio;
	}

	public Boolean getIncludeLastValidDate() {
		return includeLastValidDate;
	}

	public void setIncludeLastValidDate(Boolean includeLastValidDate) {
		this.includeLastValidDate = includeLastValidDate;
	}

	public Boolean getIncludeLastTransactionDate() {
		return includeLastTransactionDate;
	}

	public void setIncludeLastTransactionDate(Boolean includeLastTransactionDate) {
		this.includeLastTransactionDate = includeLastTransactionDate;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMpt() {
		return mpt;
	}

	public void setMpt(String mpt) {
		this.mpt = mpt;
	}

	public Boolean getOwner() {
		return owner;
	}

	public void setOwner(Boolean owner) {
		this.owner = owner;
	}

	public Boolean getAdmin() {
		return admin;
	}

	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMainStrategyName() {
		return mainStrategyName;
	}

	public void setMainStrategyName(String mainStrategyName) {
		this.mainStrategyName = mainStrategyName;
	}

	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}

	protected Date lastUpdated = new Date();

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	protected String updateTime = System.currentTimeMillis() + "";

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getPortfolioUrlPrefix() {
		return portfolioUrlPrefix;
	}

	public void setPortfolioUrlPrefix(String portfolioUrlPrefix) {
		this.portfolioUrlPrefix = portfolioUrlPrefix;
	}

	public Boolean getHasMore() {
		return hasMore;
	}

	public void setHasMore(Boolean hasMore) {
		this.hasMore = hasMore;
	}

	public Boolean getRedirect() {
		return redirect;
	}

	public void setRedirect(Boolean redirect) {
		this.redirect = redirect;
	}

	public Boolean getExpired() {
		return expired;
	}

	public void setExpired(Boolean expired) {
		this.expired = expired;
	}
	public Boolean getIncludeApprove() {
		return includeApprove;
	}
	public void setIncludeApprove(Boolean includeApprove) {
		this.includeApprove = includeApprove;
	}
	public Long getID() {
		return ID;
	}
	public void setID(Long iD) {
		ID = iD;
	}

}
