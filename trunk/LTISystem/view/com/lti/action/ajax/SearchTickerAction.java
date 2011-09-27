/**
 * 
 */
package com.lti.action.ajax;

import java.util.List;

import com.lti.action.Action;
import com.lti.service.bo.Security;
import com.lti.service.bo.VariableFor401k;
import com.lti.util.FundTableCachingUtil;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author CCD
 *
 */
public class SearchTickerAction extends ActionSupport implements Action {

	private String query;
	
	private String resultString;

	@Override
	public String execute() throws Exception {
		resultString="";
		resultString=new String(resultString.getBytes("ISO-8859-1"), "UTF-8");
    	try{
			if(query!=null&&!query.equals("")){
				query=new String(query.getBytes("ISO-8859-1"), "UTF-8");
				query=query.replace("_", "\\_");
				VariableFor401k variable = FundTableCachingUtil.find(query);
				if(variable != null){
					resultString += variable.getAssetClassName();
					resultString+="#";
					resultString += variable.getSymbol();
					resultString+="#";
					resultString += variable.getName();
				}
			}
		}catch(Exception ex){
		}
		return Action.SUCCESS;
	}
	
	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getResultString() {
		return resultString;
	}

	public void setResultString(String resultString) {
		this.resultString = resultString;
	}
}
