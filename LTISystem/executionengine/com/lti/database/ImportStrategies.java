package com.lti.database;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.lti.service.bo.Strategy;
import com.lti.service.bo.StrategyCode;
import com.lti.system.ContextHolder;
import com.lti.type.Pair;
import com.lti.type.executor.CodeInf;
import com.lti.type.finance.GStrategy;
public class ImportStrategies {

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main2(String[] args) throws FileNotFoundException {
		File[] files=new File("f:/strategies").listFiles();
		for(File file:files){
			if(file.getName().endsWith(".xml")){
				CodeInf ci=CodeInf.getInstance(new FileReader(file));
				if(ci==null){
					System.out.println(file.getAbsolutePath());
					continue;
				}
				StrategyCode sc=new StrategyCode();
				process(ci);
				sc.setCode(ci);
				sc.setStrategyID(ci.getID());
				sc.setDate(new Date());
				ContextHolder.getStrategyManager().saveStrategyCode(sc);
				System.out.println(sc.getStrategyID());
			}
		}
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		File[] files=new File("/home/sshadmin/p").listFiles();
		for(File file:files){
			if(file.getName().endsWith(".xml")){
				//System.out.println(file.getAbsolutePath());
				if(file.getName().startsWith("S_CODE")){
					CodeInf ci=CodeInf.getInstance(new FileReader(file));
					
					if(ci==null){
						System.out.println(file.getAbsolutePath());
						continue;
					}
					StrategyCode sc=new StrategyCode();
					process(ci);
					sc.setCode(ci);
					sc.setStrategyID(ci.getID());
					sc.setDate(new Date());
					ContextHolder.getStrategyManager().saveStrategyCode(sc);
					System.out.println("CODE:"+sc.getStrategyID());
				}else if(file.getName().startsWith("S_")){
					if(new Boolean(true))continue;
					GStrategy gs=GStrategy.getInstance(new FileReader(file));
					if(gs==null){
						System.out.println(file.getAbsolutePath());
						continue;
					}
					Strategy s=gs.toStrategy();
					Strategy ds=ContextHolder.getStrategyManager().get(s.getID());
					ds.setType(s.getType());
					ds.setAttributes(s.getAttributes());
					ContextHolder.getStrategyManager().update(ds);
				}
				
			}
		}
	}
	private static Map<String, String> replacemap=new HashMap<String, String>();
	static{
		replacemap.put("SecurityItem","HoldingItem");
		replacemap.put(".getClassID",".getAssetClassID");
		replacemap.put(".getSecurityList",".getHoldingItems");
		replacemap.put(".getSecurities",".getHoldingItems");
		replacemap.put(".getCurrentSecurityList",".getHoldingItems");
		replacemap.put("CurrSecurList.get(j).getTotalAmount()", "CurrSecurList.get(j).getShare()*CurrSecurList.get(j).getPrice()");
		//replacemap.put("CurrentPortfolio.getStartingDate()", "CurrSecurList.get(j).getAmount()");
		
	}
	
	public static String process(String code,long id){
		if(code==null)return null;
		Iterator<String> iter=replacemap.keySet().iterator();
		while(iter.hasNext()){
			String key = iter.next();
			code=code.replace(key,replacemap.get(key));
		}
		return code;
	}
	
	public static void process(CodeInf ci){
		ci.setInit(process(ci.getInit(),ci.getID()));
		ci.setDefaultAction(process(ci.getDefaultAction(),ci.getID()));
		ci.setCommentAction(process(ci.getCommentAction(),ci.getID()));
		ci.setInit(process(ci.getInit(),ci.getID()));
		if(ci.getConditionAction()!=null&&ci.getConditionAction().size()>0){
			for(Pair p:ci.getConditionAction()){
				if(p.getPost()!=null){
					p.setPost(process(p.getPost(),ci.getID()));
				}
			}
		}
		ci.setFunction(process(ci.getFunction(),ci.getID()));
		
		
	}

}
