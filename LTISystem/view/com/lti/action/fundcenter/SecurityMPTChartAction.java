package com.lti.action.fundcenter;

import java.util.ArrayList;
import java.util.Iterator;

import com.lti.action.Action;
import com.lti.action.TemplateAction;
import com.lti.service.PortfolioManager;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.Security;
import com.lti.system.ContextHolder;

public class SecurityMPTChartAction extends TemplateAction implements Action {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private java.lang.String symbols;
	
	private java.util.List<String> symbolList;
	private java.util.List<String> nameList;
	
	public java.util.List<String> getNameList() {
		return nameList;
	}


	public void setNameList(java.util.List<String> nameList) {
		this.nameList = nameList;
	}

	private java.lang.String symbol;
	
	private java.lang.String names;
	
	private String chartName;

	public String getChartName() {
		return chartName;
	}


	public void setChartName(String chartName) {
		this.chartName = chartName;
	}


	public java.lang.String getNames() {
		return names;
	}


	public void setNames(java.lang.String names) {
		this.names = names;
	}


	//写得乱七八糟
	@Override
	public void validate() {
		
		super.validate();
		PortfolioManager pm=ContextHolder.getPortfolioManager();
		com.lti.service.SecurityManager sm=ContextHolder.getSecurityManager();
		if(symbol == null || symbol.equals(""))
		{
			addActionError("No Security Symbol!");
			return;
		}
		if(symbols==null){
			if(symbolList == null || symbolList.size() == 0){
				symbols = symbol;
				symbolList=new ArrayList<String>();
				symbolList.add(symbol);
			}
			else{
				names = "";
				symbols = "";
				nameList=new ArrayList<String>();
				Iterator<String> iter=symbolList.iterator();
				while(iter.hasNext()){
					String tmp=iter.next();
					if(tmp.equals("")){
						iter.remove();
						continue;
					}
					
					if(tmp.startsWith("P_")){
						try {
							Portfolio p=pm.getBasicPortfolio(Long.parseLong(tmp.substring(2, tmp.length())));
							nameList.add(p.getName().replace(',', '_'));
							names += p.getName().replace(',', '_')+",";
							symbols += tmp+",";
						} catch (Exception e) {
							iter.remove();
							continue;
						}
					}else{
						Security sec=sm.get(tmp);
						if(sec!=null){
							symbols += tmp+",";
							names += sec.getName().replace(',', '_')+",";
							nameList.add(sec.getName().replace(',', '_'));
						}else{
							iter.remove();
							continue;
						}
					}
				}
				
				
			}//end symbolList==null ||
		}//end if symbols==null
		else{
			String[] arr=symbols.split(",");
			symbolList=new ArrayList<String>();
			for(String str:arr){
				symbolList.add(str);
			}
			
			String[] arr1=names.split(",");
			nameList=new ArrayList<String>();
			for(String str:arr1){
				nameList.add(str);
			}
		}
		
		if(names!=null&&names.endsWith(",")){
			names=names.substring(0, names.length()-1);
		}
		if(symbols!=null&&symbols.endsWith(",")){
			symbols=symbols.substring(0, symbols.length()-1);
		}
		
	}
	

	@Override
	public String execute() throws Exception {
		
		return Action.SUCCESS;
	}


	public java.lang.String getSymbols() {
		return symbols;
	}

	public void setSymbols(java.lang.String symbols) {
		this.symbols = symbols;
	}

	public java.util.List<String> getSymbolList() {
		return symbolList;
	}

	public void setSymbolList(java.util.List<String> symbolList) {
		this.symbolList = symbolList;
	}

	public java.lang.String getSymbol() {
		return symbol;
	}

	public void setSymbol(java.lang.String symbol) {
		this.symbol = symbol;
	}
	
	
	

}
