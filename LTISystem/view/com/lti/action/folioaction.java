package com.lti.action;

import java.util.ArrayList;
import java.util.List;

import com.lti.service.GroupManager;
import com.lti.service.PortfolioManager;
import com.lti.service.UserManager;
import com.lti.service.bo.CachePortfolioItem;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.User;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;

public class folioaction {

	private Long userID;

	private String getHql(long[] ids) {
		Long[] groupids = getGroups();
		StringBuffer sb = new StringBuffer();
		sb.append("from CachePortfolioItem gpi where ");
		if(userID.equals(Configuration.SUPER_USER_ID)){
			sb.append("gpi.GroupID=");
			sb.append(0);
		}else if (groupids.length == 1) {
			sb.append("gpi.GroupID=");
			sb.append(groupids[0]);
		} else {
			for (int i = 0; i < groupids.length; i++) {
				Long gid = groupids[i];
				if (i == 0) {
					sb.append("(");
					sb.append("gpi.GroupID=");
					sb.append(gid);
				} else {
					sb.append(" or ");
					sb.append("gpi.GroupID=");
					sb.append(gid);
				}
				if (i == groupids.length - 1) {
					sb.append(")");
				}
			}// end for
		}// end else
		
		if(ids!=null){
			sb.append(" and ");
			for (int i = 0; i < ids.length; i++) {
				Long id = ids[i];
				if (i == 0) {
					sb.append("(");
					sb.append("gpi.PortfolioID=");
					sb.append(id);
				} else {
					sb.append(" or ");
					sb.append("gpi.PortfolioID=");
					sb.append(id);
				}
				if (i == ids.length - 1) {
					sb.append(")");
				}
			}// end for
		}

		sb.append(" order by EndDate desc");
		System.out.println(sb.toString());
		return sb.toString();
	}

	private GroupManager groupManager;
	private UserManager userManager;
	private PortfolioManager portfolioManager;

	private Long[] getGroups() {
		Object[] groups = groupManager.getGroupIDs(userID);
		if(groups==null){
			return new Long[]{-10000l};
		}
		Long[] ids = new Long[groups.length];
		for (int i = 0; i < ids.length; i++) {
			ids[i]=(Long) groups[i];
		}
		return ids;
	}

	private String IDS1;
	private String IDS2;
	
	private String Name1;
	private String Name2;
	
	private long[] getIDS(String ids){
		String[] ss=ids.split(",");
		long[] nids=new long[ss.length];
		for(int i=0;i<nids.length;i++){
			nids[i]=Long.parseLong(ss[i]);
		}
		return nids;
	}
	
	private List<CachePortfolioItem> portfolios1;
	private List<CachePortfolioItem> portfolios2;
	
	
	private List<CachePortfolioItem> getPortfolios(long[] ids,String[] names,List<CachePortfolioItem> ps){
		List<CachePortfolioItem> portfolios=new ArrayList<CachePortfolioItem>();
		for(int i=0;i<ids.length;i++){
			CachePortfolioItem cpi=null;
			for(int j=0;j<ps.size();j++){
				if(ps.get(j).getPortfolioID().equals(ids[i])){
					if(cpi==null){
						cpi=ps.get(j);
						if(cpi.getRoleID()==Configuration.ROLE_PORTFOLIO_REALTIME_ID)break;
					}else{
						if(ps.get(j).getRoleID()==Configuration.ROLE_PORTFOLIO_REALTIME_ID){
							cpi=ps.get(j);
							break;
						}
					}
				}
			}
			if(cpi!=null){
				cpi.setPortfolioName(names[i]);
				portfolios.add(cpi);
			}
		}
		return portfolios;
	}
	
	public String foliosaa() {
		userManager = ContextHolder.getUserManager();
		groupManager = ContextHolder.getGroupManager();
		portfolioManager = ContextHolder.getPortfolioManager();
		User user = userManager.getLoginUser();
		userID = user.getID();
		
		long[] ids1=new long[] { 19493, 19495, 19791, 32617, 19579 };
		long[] ids2=new long[] { 19487, 19489, 19795, 32616, 19797 };

		if(IDS1!=null){
			ids1=getIDS(IDS1);
		}
		
		if(IDS2!=null){
			ids2=getIDS(IDS2);
		}
		
		String[] names1=new String[]{
				"MyPlanIQ SAA Growth",
				"MyPlanIQ SAA Moderate",
				"MyPlanIQ SAA Balanced",
				"MyPlanIQ SAA Conservative",
				"MyPlanIQ SAA VCons "
		};
		
		String[] names2=new String[]{
				"MyPlanIQ TAA Growth",
				"MyPlanIQ TAA Moderate",
				"MyPlanIQ TAA Balanced",
				"MyPlanIQ TAA Conservative",
				"MyPlanIQ TAA VCons "
		};
		
		if(Name1!=null){
			names1=Name1.split(",");
		}
		if(Name2!=null){
			names2=Name2.split(",");
		}
	
		List<CachePortfolioItem> ps1=portfolioManager.findByHQL(getHql(ids1));
		List<CachePortfolioItem> ps2=portfolioManager.findByHQL(getHql(ids2));
		
		portfolios1=getPortfolios(ids1,names1, ps1);
		portfolios2=getPortfolios(ids2,names2, ps2);
		
		return Action.SUCCESS;
	}

	public String getIDS1() {
		return IDS1;
	}

	public void setIDS1(String iDS1) {
		IDS1 = iDS1;
	}

	public String getIDS2() {
		return IDS2;
	}

	public void setIDS2(String iDS2) {
		IDS2 = iDS2;
	}

	public List<CachePortfolioItem> getPortfolios1() {
		return portfolios1;
	}

	public void setPortfolios1(List<CachePortfolioItem> portfolios1) {
		this.portfolios1 = portfolios1;
	}

	public List<CachePortfolioItem> getPortfolios2() {
		return portfolios2;
	}

	public void setPortfolios2(List<CachePortfolioItem> portfolios2) {
		this.portfolios2 = portfolios2;
	}
	
	

}
