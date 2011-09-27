package com.lti.action.admin.clonecenter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.lti.service.CloningCenterManager;
import com.lti.service.PortfolioManager;
import com.lti.service.bo.Portfolio;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.util.LTIDate;
import com.lti.util.StringUtil;

public class Clone13FCenterAction {
	private String operation;
	private String category;
	private Long ID;
	private String fundName;
	private String _13fName;
	private String startingDate;
	private String actionMessage;
	private List<Portfolio> portfolios;
	private String ids;
	@Deprecated
	public String execute(){
//		if(operation==null){
//			operation="view";
//		}
//		operation=operation.toLowerCase();
//		if(operation.equals("delete")||operation.equals("del")){
//			PortfolioManager pm=ContextHolder.getPortfolioManager();
//			try {
//				pm.delete(ID);
//				actionMessage="Delete ["+ID+"] sucessfully.";
//			} catch (Exception e) {
//				actionMessage="Failed to delete.\n"+e.getMessage();
//			}
//			return "actionMessage";
//		}else if(operation.toLowerCase().equals("deleteall")){
//			PortfolioManager pm=ContextHolder.getPortfolioManager();
//			if(ids==null){
//				actionMessage="Nothing to delete.";
//				return "actionMessage";
//			}
//			String[] pids=ids.split(",");
//			StringBuffer sb=new StringBuffer();
//			sb.append("Try to delete "+ids.length()+" portfolios.");
//			for(int i=0;i<pids.length;i++){
//				try {
//					pm.delete(Long.parseLong(pids[i].trim()));
//					sb.append("Delete ["+pids[i]+"] sucessfully.\n");
//				} catch (Exception e) {
//					sb.append("Failed to delete["+pids[i]+"].\n"+e.getMessage()+"\n");
//				}
//			}
//			actionMessage=sb.toString();
//			return "actionMessage";
//		}else if(operation.equals("create")||operation.equals("save")||operation.equals("add")){
//			try {
//				Date date=LTIDate.clearHMSM(new Date());
//				SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd");
//				SimpleDateFormat sdf2=new SimpleDateFormat("MM/dd/yyyy");
//				try{
//					date=sdf1.parse(startingDate);
//				}catch(Exception e){
//					try {
//						date=sdf2.parse(startingDate);
//					} catch (Exception e1) {}
//				}
//				
//				CloningCenterManager.create13FPortfolio(fundName, _13fName, date,StringUtil.sortCategories(category),null);
//				actionMessage="Create "+fundName+" sucessfully.";
//			} catch (Exception e) {
//				actionMessage="Failed to create.\n"+e.getMessage();
//			}
//			return "actionMessage";
//			
//		}else if(operation.equals("category")||operation.equals("categories")){
//			PortfolioManager pm=ContextHolder.getPortfolioManager();
//			try {
//				Portfolio p=pm.get(ID);
//				p.setCategories(StringUtil.sortCategories(category));
//				pm.update(p);
//				Portfolio o=pm.getOriginalPortfolio(p.getID());
//				o.setCategories(StringUtil.sortCategories(category));
//				pm.update(o);
//				actionMessage="Update category successfully.\n";
//			} catch (RuntimeException e) {
//				actionMessage="Failed to update category.\n"+e.getMessage();
//			}
//			return "actionMessage";
//		}else if(operation.equals("search")){
//			PortfolioManager pm=ContextHolder.getPortfolioManager();
//			actionMessage="No result found.";
//			try {
//				List<String> list=pm.findBySQL("SELECT distinct companyname FROM "+Configuration.TABLE_COMPANY_INDEX+" l where companyname like '"+fundName+"%' and formtype='13f-hr';");
//				
//				if(list!=null){
//					StringBuffer sb=new StringBuffer();
//					for(int i=0;i<list.size();i++){
//						String name=list.get(i);
//						sb.append(name);
//						List<Object> dlist=pm.findBySQL("SELECT datefiled FROM "+Configuration.TABLE_COMPANY_INDEX+" l where companyname ='"+name+"' and formtype='13f-hr' order by datefiled asc limit 0,1;");
//						if(dlist!=null&&dlist.size()==1){
//							sb.append(",");
//							sb.append(dlist.get(0).toString());
//						}
//						sb.append('\n');
//					}
//					actionMessage=sb.toString();
//				}
//				
//			} catch (Exception e) {
//			}
//			return "actionMessage";
//			
//		}
//		else{
//			
//		}
//		portfolios=CloningCenterManager.get13FPortfolios();
		return "success";
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public Long getID() {
		return ID;
	}

	public void setID(Long id) {
		ID = id;
	}

	public String getFundName() {
		return fundName;
	}

	public void setFundName(String fundName) {
		this.fundName = fundName;
	}

	public String get_13fName() {
		return _13fName;
	}

	public void set_13fName(String name) {
		_13fName = name;
	}


	public String getStartingDate() {
		return startingDate;
	}

	public void setStartingDate(String startingDate) {
		this.startingDate = startingDate;
	}

	public List<Portfolio> getPortfolios() {
		return portfolios;
	}

	public void setPortfolios(List<Portfolio> portfolios) {
		this.portfolios = portfolios;
	}

	public String getActionMessage() {
		return actionMessage;
	}

	public void setActionMessage(String actionMessage) {
		this.actionMessage = actionMessage;
	}


	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

}
