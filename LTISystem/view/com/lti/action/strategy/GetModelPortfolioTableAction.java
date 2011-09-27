package com.lti.action.strategy;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.lti.action.Action;
import com.lti.bean.PortfolioItem;
import com.lti.bean.StrategyItem;
import com.lti.permission.MPIQChecker;
import com.lti.permission.ValidFiChecker;
import com.lti.service.GroupManager;
import com.lti.service.PortfolioManager;
import com.lti.service.StrategyClassManager;
import com.lti.service.StrategyManager;
import com.lti.service.UserManager;
import com.lti.service.bo.CachePortfolioItem;
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

public class GetModelPortfolioTableAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(GetModelPortfolioTableAction.class);
	
	
	private PortfolioManager portfolioManager;
	private StrategyManager strategyManager;
	
	private String urlPrefix=null;
	
	private List<PortfolioItem> portfolios;
	private Boolean paged = false;
	public Boolean getPaged() {
		return paged;
	}

	public void setPaged(Boolean paged) {
		this.paged = paged;
	}

	private String ids;
	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	private String getHql(){
		StringBuffer sb=new StringBuffer();
		sb.append("from CachePortfolioItem gpi where gpi.GroupID=0 and gpi.RoleID=4");
		if(ids!=null){
			String[] strs=ids.split("\\|");
			sb.append(" and (");
			for(int i=0;i<strs.length;i++){
				long id=Long.parseLong(strs[i]);
				sb.append("gpi.PortfolioID=");
				sb.append(id);
				if(i!=strs.length-1)sb.append(" or ");
			}
			sb.append(")");
		}
		sb.append(" order by EndDate desc");
		return sb.toString();
	}
	private List<StrategyItem> items;
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
	
	private Boolean expired = false;
	@Override
	public String execute() throws Exception {
		portfolioManager = ContextHolder.getPortfolioManager();
		strategyManager = ContextHolder.getStrategyManager();
		String hql = getHql();
		
		List<CachePortfolioItem> ps = portfolioManager.findByHQL(hql);

		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

		if (ps != null && ps.size() > 0) {
			Map<Long, CachePortfolioItem> map=new HashMap<Long, CachePortfolioItem>();
			for(CachePortfolioItem cpi:ps){
				if(cpi.getMainStrategyID()!=null){
					map.put(cpi.getPortfolioID(), cpi);
				}
			}
			
			items = new ArrayList<StrategyItem>();
			String[] idarr=ids.split("\\|");
			for (int i = 0; i < idarr.length; i++) {
				Long id=Long.parseLong(idarr[i].trim());
				CachePortfolioItem s=map.get(id);
				if(s==null)continue;
				StrategyItem item = new StrategyItem();
				if(s.getMainStrategyID()!=null){
					item.setID(s.getMainStrategyID());
					item.setName(strategyManager.get(s.getMainStrategyID()).getName());
				}else{
					item.setID(0l);
					item.setName("STATIC");
				}
				
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

				items.add(item);
			}

		}
		return Action.SUCCESS;

	}

	public String getUrlPrefix() {
		return urlPrefix;
	}

	public void setUrlPrefix(String urlPrefix) {
		this.urlPrefix = urlPrefix;
	}

	public List<PortfolioItem> getPortfolios() {
		return portfolios;
	}

	public void setPortfolios(List<PortfolioItem> portfolios) {
		this.portfolios = portfolios;
	}

	public List<StrategyItem> getItems() {
		return items;
	}

	public void setItems(List<StrategyItem> items) {
		this.items = items;
	}

	
	
	
	private String portfolioUrlPrefix;
	private Boolean redirect=false;
	
	private Boolean owner = false;
	private Boolean admin = false;
	private String mainStrategyName=null;
	private Boolean sortByDesc = true;
	private Integer size = 0;
	private String keyword;
	private Integer strategyClassID;
	private String categories;
	private Integer sortType = com.lti.service.bo.PortfolioMPT.SORT_BY_SHARPERATIO;
	private Integer sortYear = PortfolioMPT.LAST_ONE_YEAR;
	private Boolean includeID = false;
	private Boolean includeCategory = false;
	private Boolean includePortfolio = true;
	private Boolean includeLastValidDate = false;
	private Boolean includeLastTransactionDate = false;
	private Boolean hasMore=false;
	private String year = "-1,-3,-5";
	private String mpt = "ar,beta,sharperatio";
	private String title=null;
	private Long type=Configuration.STRATEGY_TYPE_NORMAL;
	private Integer width;

	private String message;
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	private Boolean hasSubscred=true;
	public StrategyManager getStrategyManager() {
		return strategyManager;
	}

	public void setStrategyManager(StrategyManager strategyManager) {
		this.strategyManager = strategyManager;
	}

	public String getPortfolioUrlPrefix() {
		return portfolioUrlPrefix;
	}

	public void setPortfolioUrlPrefix(String portfolioUrlPrefix) {
		this.portfolioUrlPrefix = portfolioUrlPrefix;
	}

	public Boolean getRedirect() {
		return redirect;
	}

	public void setRedirect(Boolean redirect) {
		this.redirect = redirect;
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

	public String getMainStrategyName() {
		return mainStrategyName;
	}

	public void setMainStrategyName(String mainStrategyName) {
		this.mainStrategyName = mainStrategyName;
	}

	public Boolean getSortByDesc() {
		return sortByDesc;
	}

	public void setSortByDesc(Boolean sortByDesc) {
		this.sortByDesc = sortByDesc;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Integer getStrategyClassID() {
		return strategyClassID;
	}

	public void setStrategyClassID(Integer strategyClassID) {
		this.strategyClassID = strategyClassID;
	}

	public String getCategories() {
		return categories;
	}

	public void setCategories(String categories) {
		this.categories = categories;
	}

	public Integer getSortType() {
		return sortType;
	}

	public void setSortType(Integer sortType) {
		this.sortType = sortType;
	}

	public Integer getSortYear() {
		return sortYear;
	}

	public void setSortYear(Integer sortYear) {
		this.sortYear = sortYear;
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

	public Boolean getHasMore() {
		return hasMore;
	}

	public void setHasMore(Boolean hasMore) {
		this.hasMore = hasMore;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Boolean getHasSubscred() {
		return hasSubscred;
	}

	public void setHasSubscred(Boolean hasSubscred) {
		this.hasSubscred = hasSubscred;
	}

	public Boolean getExpired() {
		return expired;
	}

	public void setExpired(Boolean expired) {
		this.expired = expired;
	}

	
}
