package com.lti.action.ajax;

import java.util.List;

import com.lti.action.Action;
import com.lti.service.StrategyManager;
import com.lti.service.bo.Strategy;
import com.lti.service.bo.StrategyCode;
import com.lti.type.Quaternion;
import com.opensymphony.xwork2.ActionSupport;



public class GetStrategyParameterAction  extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	StrategyManager strategyManager;

	public void setStrategyManager(StrategyManager strategyManager) {
		this.strategyManager = strategyManager;
	}
	
	private String q;
	
	private String name;
	
	private String resultString;

	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getResultString() {
		return resultString;
	}

	public void setResultString(String resultString) {
		this.resultString = resultString;
	}

	public String execute() throws Exception {
		StringBuffer sb=new StringBuffer();
		
    	try{
			if(q!=null&&!q.equals("")){
				q=new String(q.getBytes("ISO-8859-1"), "UTF-8");
				Strategy strategy=strategyManager.get(q);
				StrategyCode sc=strategyManager.getLatestStrategyCode(strategy.getID());
				if (strategy!=null) {
					
					List<Quaternion> pairList = sc.getCode().getParameter();
					if (pairList != null&&pairList.size()!=0){
						sb.append("<table width='100%'>");
						for (int j = 0; j < pairList.size(); j++) {
							sb.append("<tr>");
							sb.append("<td>");
							sb.append(pairList.get(j).getFirst());
							sb.append(" ");
							sb.append(pairList.get(j).getSecond());
							sb.append("</td>");
							
							if(pairList.get(j).getFirst().contains("[")){
								sb.append("<td colspan='2'>");
								sb.append(pairList.get(j).getFourth());
								sb.append("</td>");
								sb.append("</tr>");
								
								sb.append("<tr>");
								sb.append("<td>");
								sb.append("</td>");
								sb.append("<td colspan='2'>");
								sb.append("<textarea cols=70 rows=4 name=");
								sb.append(name);
								sb.append(".");
								sb.append(pairList.get(j).getSecond());
								sb.append(" id=");
								sb.append(name);
								sb.append(".");
								sb.append(pairList.get(j).getSecond());
								sb.append(">");
								sb.append(pairList.get(j).getThird());
								sb.append("</textarea>");
								
								sb.append("</td>");
								sb.append("</tr>");
							}else{
								sb.append("<td>");
								sb.append("<input type=text  name=");
								sb.append(name);
								sb.append(".");
								sb.append(pairList.get(j).getSecond());
								sb.append(" id=");
								sb.append(name);
								sb.append(".");
								sb.append(pairList.get(j).getSecond());
								sb.append(" value='");
								sb.append(pairList.get(j).getThird());
								sb.append("'>");
								sb.append("</td>");
								sb.append("<td>");
								sb.append(pairList.get(j).getFourth());
								sb.append("</td>");
								sb.append("</tr>");
							}

						}
						sb.append("</table>");
						resultString=sb.toString();
					}else resultString+="No Parameter !";
				}else resultString+="You have selected a non-existed strategy!";
			}else resultString+="Did you submit the corret information?";
		}catch(Exception ex){
		}
		return Action.SUCCESS;
	}
}
