package com.lti.action.admin.stock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.lti.action.Action;
import com.lti.action.admin.holiday.ExportHolidayAction;
import com.opensymphony.xwork2.ActionSupport;
import com.lti.type.PaginationSupport;

import com.lti.service.bo.IncomeStatement;
import com.lti.service.FinancialStatementManager;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;

public class FinancialInfoAction extends ActionSupport implements Action{
 
	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(FinancialInfoAction.class);
	
	private String action;
	
	private String symbol;
	
	private FinancialStatementManager fsm;
	
	private PaginationSupport statements;
	
	private Integer startIndex;
	
	private Integer pageSize;
	
	
	
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public FinancialStatementManager getFsm() {
		return fsm;
	}

	public void setFsm(FinancialStatementManager fsm) {
		this.fsm = fsm;
	}

	public PaginationSupport getStatements() {
		return statements;
	}

	public void setStatements(PaginationSupport statements) {
		this.statements = statements;
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

	public void validate(){

		if(this.startIndex==null)this.startIndex=0;
		
		if(this.pageSize==null||this.pageSize<=0)this.pageSize=Configuration.DefaultPageSize;
	}
	
	public String execute(){
		fsm = ContextHolder.getFinancialStatementManager();
		DetachedCriteria detachedCriteria=DetachedCriteria.forClass(IncomeStatement.class);
		statements = fsm.getIncomeStatement(this.pageSize, this.startIndex);
		return Action.SUCCESS;
	}

}
