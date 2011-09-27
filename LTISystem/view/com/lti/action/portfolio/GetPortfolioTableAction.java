package com.lti.action.portfolio;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.action.Action;
import com.lti.bean.PortfolioItem;
import com.lti.permission.MPIQChecker;
import com.lti.permission.ValidFiChecker;
import com.lti.service.GroupManager;
import com.lti.service.PortfolioManager;
import com.lti.service.UserManager;
import com.lti.service.bo.CachePortfolioItem;
import com.lti.service.bo.User;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.util.FormatUtil;

public class GetPortfolioTableAction implements Action {
	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(GetPortfolioTableAction.class);

	

	private PortfolioManager portfolioManager;
	private UserManager userManager;
	private GroupManager groupManager;

	private Long userID;
	private String groupIDs;
	private Integer size = 100;

	private Boolean owner = false;
	private Boolean admin = false;
	
	private String keyword =null ;

	private String title = null;
	private String urlPrefix=null;
	
	private Boolean hasMore = false;
	private List<PortfolioItem> portfolios;
	private Boolean paged = true;
	public Boolean getPaged() {
		return paged;
	}

	public void setPaged(Boolean paged) {
		this.paged = paged;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}


	private String getHql(){
		Long[] groupids=getGroups();
		StringBuffer sb=new StringBuffer();
		if (owner) {
			sb.append("from CachePortfolioItem gpi where gpi.UserID=");
			sb.append(userID);
			sb.append(" and gpi.GroupID=0");
			
		} else if (userID.equals(Configuration.SUPER_USER_ID)&&admin) {
			sb.append("from CachePortfolioItem gpi where gpi.GroupID=0");
			
		} else {
			sb.append("from CachePortfolioItem gpi where ");
			
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
				}//end for
			}//end else
			
					
		}
		if(keyword!=null&&!keyword.equals("")){
			sb.append(" and gpi.PortfolioName like '%");
			sb.append(keyword);
			sb.append("%'");
		}	
		
		sb.append(" order by EndDate desc");
		return sb.toString();
	}
	
	private boolean inGroups(Object[] groups,Object group){
		for(int i=0;i<groups.length;i++){
			if(groups[i].equals(group))return true;
		}
		return false;
	}
	private Long[] getGroups(){
		if (groupIDs == null || groupIDs.equals("")) {
			if (userID.equals(Configuration.USER_ANONYMOUS)) {
				groupIDs = Configuration.GROUP_ANONYMOUS_ID + "";
				return new Long[]{Configuration.GROUP_ANONYMOUS_ID};
			} else {
				groupIDs = Configuration.GROUP_ANONYMOUS_ID + ",";
				groupIDs += Configuration.GROUP_MEMBER_ID;
				return new Long[]{Configuration.GROUP_ANONYMOUS_ID, Configuration.GROUP_MEMBER_ID};
			}
		}
		Object[] groups = groupManager.getGroupIDs(userID);
		String[] ss = groupIDs.split(",");
		Long[] ids = new Long[ss.length];
		for (int i = 0; i < ids.length; i++) {
			Long g= Long.parseLong(ss[i]);
			if(inGroups(groups, g)||userID.equals(Configuration.SUPER_USER_ID)||g.equals(Configuration.GROUP_ANONYMOUS_ID)){
				ids[i]=g;
			}
			else{
				throw new RuntimeException("Invalid access.");
			}
		}
		return ids;
	}
	
	private int inPortfolios(Long id){
		for(int i=0;i<portfolios.size();i++){
			if(portfolios.get(i).getID().equals(id))return i;
		}
		return -1;
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
		
		long t1 = System.currentTimeMillis();
		portfolioManager = ContextHolder.getPortfolioManager();
		userManager = ContextHolder.getUserManager();
		groupManager = ContextHolder.getGroupManager();

		User user = userManager.getLoginUser();
		userID = user.getID();

		MPIQChecker fc=new MPIQChecker(userID);
		ValidFiChecker vc=new ValidFiChecker(userID);
		if(fc.hasSubscred()||vc.hasSubscred()){
			hasSubscred=true;
		}
		
		String hql = getHql();
		
		List<CachePortfolioItem> ps = portfolioManager.findByHQL(hql);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		portfolios = new ArrayList<PortfolioItem>();
		for (int i = 0; i < ps.size(); i++) {
			CachePortfolioItem cpi=ps.get(i);
			PortfolioItem pi = null;
			int pos=inPortfolios (cpi.getPortfolioID());
			if (pos!=-1) {
				if(!cpi.getRoleID().equals(Configuration.ROLE_PORTFOLIO_REALTIME_ID)){
					portfolios.remove(pos);
				}else{
					continue;
				}
			} 
			if(cpi.getPortfolioName()==null)continue;
			pi = new PortfolioItem();

			pi.setID(cpi.getPortfolioID());
			pi.setName(cpi.getPortfolioName());

			if (cpi.getState() == Configuration.PORTFOLIO_STATE_ALIVE) {
				pi.setState("Yes");
				pi.setLiveShowName();
			} else {
				pi.setState("No");
				pi.setShowName();
			}

			pi.setDelayed(cpi.getRoleID()==Configuration.ROLE_PORTFOLIO_DELAYED_ID?true:false);

			pi.setLastValidDate(cpi.getEndDate()==null?"":sdf.format(cpi.getEndDate()));
			pi.setLastTransactionDate(cpi.getLastTransactionDate()==null?"":sdf.format(cpi.getLastTransactionDate()));

			pi.setAR1(FormatUtil.formatPercentage(cpi.getAR1()));
			pi.setSharpeRatio1(FormatUtil.formatPercentage(cpi.getSharpeRatio1()));
			pi.setAR3(FormatUtil.formatPercentage(cpi.getAR3()));
			pi.setSharpeRatio3(FormatUtil.formatPercentage(cpi.getSharpeRatio3()));
			pi.setAR5(FormatUtil.formatPercentage(cpi.getAR5()));
			pi.setSharpeRatio5(FormatUtil.formatPercentage(cpi.getSharpeRatio5()));
			
			portfolios.add(pi);
			
			if (size != 0 && portfolios.size() >= size) {
				hasMore = true;
				break;
			}
		}
		long t2 = System.currentTimeMillis();
		System.out.println("Get Portfolio Table Times: " + (t2 - t1));
		return Action.SUCCESS;
	}

	public List<PortfolioItem> getPortfolios() {
		return portfolios;
	}

	public void setPortfolios(List<PortfolioItem> portfolios) {
		this.portfolios = portfolios;
	}

	public void setPortfolioManager(PortfolioManager portfolioManager) {
		this.portfolioManager = portfolioManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public String getGroupIDs() {
		return groupIDs;
	}

	public void setGroupIDs(String groupIDs) {
		this.groupIDs = groupIDs;
	}

	private Long[] parseIDs(String s) {
		String[] ss = s.split(",");
		Long[] ids = new Long[ss.length];
		for (int i = 0; i < ids.length; i++) {
			ids[i] = Long.parseLong(ss[i]);
		}
		return ids;

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

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}


	public Boolean getHasMore() {
		return hasMore;
	}

	public void setHasMore(Boolean hasMore) {
		this.hasMore = hasMore;
	}


	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getUrlPrefix() {
		return urlPrefix;
	}

	public void setUrlPrefix(String urlPrefix) {
		this.urlPrefix = urlPrefix;
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
}
