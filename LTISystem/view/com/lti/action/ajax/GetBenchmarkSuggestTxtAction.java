package com.lti.action.ajax;

import java.util.List;

import com.lti.action.Action;
import com.lti.service.AssetClassManager;
import com.lti.service.SecurityManager;
import com.lti.service.bo.AssetClass;
import com.lti.service.bo.Security;
import com.opensymphony.xwork2.ActionSupport;

public class GetBenchmarkSuggestTxtAction  extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	SecurityManager securityManager;
	
	AssetClassManager assetClassManager;

	public void setAssetClassManager(AssetClassManager assetClassManager) {
		this.assetClassManager = assetClassManager;
	}
	public void setSecurityManager(SecurityManager securityManager) {
		this.securityManager = securityManager;
	}
	private String resultString;
	
	private String q;
	@Override
	public String execute() throws Exception {
		resultString="";
    	try{
			if(q!=null&&!q.equals("")){
				q=new String(q.getBytes("ISO-8859-1"), "UTF-8");
				q=q.replace("_", "\\_");
				AssetClass ac=assetClassManager.get(q);
				
				if(ac!=null){
					Security s=null;
					try {
						s=securityManager.get(ac.getBenchmarkID());
					} catch (Exception e) {
						e.printStackTrace();
					}
					if(s!=null){
						resultString=s.getSymbol();
					}
				}
			}
		}catch(Exception ex){
		}
		return Action.SUCCESS;

	}
	public String getResultString() {
		return resultString;
	}
	public void setResultString(String resultString) {
		this.resultString = resultString;
	}
	public String getQ() {
		return q;
	}
	public void setQ(String q) {
		this.q = q;
	}

}
