package com.lti.action.ajax;

import java.util.List;

import com.lti.action.Action;
import com.lti.service.AssetClassManager;
import com.lti.service.SecurityManager;
import com.lti.service.bo.AssetClass;
import com.lti.service.bo.Security;
import com.opensymphony.xwork2.ActionSupport;

public class GetAssetClassListSuggestTxtAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	AssetClassManager assetClassManager;

	public void setAssetClassManager(AssetClassManager assetClassManager) {
		this.assetClassManager = assetClassManager;
	}

	private String resultString;

	private String q;

	@Override
	public String execute() throws Exception {
		resultString = "";
		try {
			if (q != null && !q.equals("")) {
				List<AssetClass> acs = assetClassManager.getClasses();
				StringBuffer sb = new StringBuffer();
				int count = 0;
				for (int i = 0; i < acs.size(); i++) {
					if (!acs.get(i).getName().toLowerCase().contains(q.toLowerCase()) || acs.get(i).getName().equals("ROOT")) {
						continue;
					}
					count++;
					if (count > 5)
						break;
					sb.append(acs.get(i).getName());
					List<String> strs = assetClassManager.getAbsoluteClassName(acs.get(i).getID());
					sb.append('#');
					for (int j = 0; j < strs.size(); j++) {
						sb.append(strs.get(j));
						if (j != strs.size() - 1)
							sb.append("->");
					}
					sb.append('\n');
				}
				resultString = sb.toString();
				System.out.println(resultString);
			}
		} catch (Exception ex) {
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
