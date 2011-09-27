package com.lti.action.admin.stock;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.lti.action.Action;
import com.opensymphony.xwork2.ActionSupport;
import com.lti.type.PaginationSupport;

import com.lti.service.bo.IncomeStatement;
import com.lti.service.bo.Security;
import com.lti.service.FinancialStatementManager;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;

public class SearchAction extends ActionSupport implements Action{

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(SearchAction.class);
	
	private List<IncomeStatement> is;

	private Integer startIndex;
	
	private Integer pageSize;
	
	private FinancialStatementManager fsm;
	
	private String symbol;
	
	private IncomeStatement in;

	public List<IncomeStatement> getIs() {
		return is;
	}

	public void setIs(List<IncomeStatement> is) {
		this.is = is;
	}

	public Integer getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(Integer startIndex) {
		this.startIndex = startIndex;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public FinancialStatementManager getFsm() {
		return fsm;
	}

	public void setFsm(FinancialStatementManager fsm) {
		this.fsm = fsm;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public IncomeStatement getIn() {
		return in;
	}

	public void setIn(IncomeStatement in) {
		this.in = in;
	}

	public void validate(){
		
		if(this.startIndex==null)this.startIndex=0;
		
		if(this.pageSize==null||this.pageSize<=0)this.pageSize=Configuration.DefaultPageSize;
		
	}
	
	@Override
	public String execute() throws Exception {
		fsm = ContextHolder.getFinancialStatementManager();
		
		DetachedCriteria detachedCriteria=DetachedCriteria.forClass(IncomeStatement.class);
		
		detachedCriteria.add(Restrictions.eq("Symbol",symbol));
		
		is = fsm.getIncomeStatement(symbol);	
		
		if(is!=null){
			for(int i=0;i<is.size();i++){
				in = is.get(i);
			}
		}
		
		return Action.SUCCESS;

	}	
}
