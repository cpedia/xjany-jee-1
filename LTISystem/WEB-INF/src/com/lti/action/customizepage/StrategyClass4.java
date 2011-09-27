package com.lti.action.customizepage;

import com.lti.service.bo.*;
import java.util.*;
import com.lti.type.*;
import com.lti.util.*;

import java.io.*;
import com.lti.bean.*;
import com.lti.system.*;
import com.lti.service.*;
import com.lti.action.customizepage.CustomizePageAction;
import com.lti.customizepage.BasePage;
import com.lti.action.Action;
import freemarker.template.Template;

public class StrategyClass4 extends CustomizePageAction{
	public StrategyClass4(){super();}

	private static final long serialVersionUID = 2496394976004782444L;
        public void setTopStrategies(List<StrategyItem> strategies, Long strategyClassID)
         {
		Long userID;
		User user = userManager.getLoginUser();
		if (user == null) {
			userID = -1l;
		} else
			userID = user.getID();
                   Object[] IDs = strategyClassManager.getClassIDs(strategyClassID);
                   
                 List<PortfolioMPT> mpts = strategyManager.getTopStrategyByMPT(IDs, com.lti.service.bo.PortfolioMPT.LAST_ONE_YEAR,com.lti.service.bo.PortfolioMPT.SORT_BY_SHARPERATIO, userID, 100);

                   if(mpts == null){
                            return;
                   }
                   for(int i=0; i<mpts.size();i++){                    

                            StrategyItem si = new StrategyItem();
                            // Strategy s=strategyManager.get(mpts.get(i).getStrategyID());
                            si.setID(mpts.get(i).getStrategyID());
                            si.setName(mpts.get(i).getStrategyName());
                            si.setUserName("PUBLIC");
                			//if(mpts.get(i).getStrategyUserID()!=null){
                				//User u=userManager.get(mpts.get(i).getStrategyUserID());
                				//if(u!=null){
                					//si.setUserName(u.getUserName());
                				//}
                			//}
                            
                            si.setShowName();

                            Portfolio p = portfolioManager.get(mpts.get(i).getPortfolioID());
                            if (p != null&&p.getState()!=Configuration.PORTFOLIO_STATE_TEMP) {
                                     si.setPortfolioID(p.getID());
                                     si.setPortfolioName(p.getName());
                                     
                                    si.setPortfolioUserName("PUBLIC");
                     				if(p.getUserID()!=null){
                     					User u=userManager.get(p.getUserID());
                     					if(u!=null){
                     						si.setPortfolioUserName(u.getUserName());
                     					}
                     				}
                                     
                                     si.setPortfolioShortName(p.getState());

                                     PortfolioMPT mpt1 = mpts.get(i);
                                     PortfolioMPT mpt2 = portfolioManager.getPortfolioMPT(p.getID(), PortfolioMPT.LAST_THREE_YEAR);
                                     PortfolioMPT mpt3 = portfolioManager.getPortfolioMPT(p.getID(), PortfolioMPT.LAST_FIVE_YEAR);

                                     if (mpt1 != null) {
                                               si.setAR1(FormatUtil.formatPercentage(mpt1.getAR()));
                                               si.setBeta1(FormatUtil.formatQuantity(mpt1.getBeta()));
                                               si.setSharpeRatio1(FormatUtil.formatPercentage(mpt1.getSharpeRatio()));
                                     }
                                     if (mpt2 != null) {
                                               si.setAR3(FormatUtil.formatPercentage(mpt2.getAR()));
                                               si.setBeta3(FormatUtil.formatQuantity(mpt2.getBeta()));
                                               si.setSharpeRatio3(FormatUtil.formatPercentage(mpt2.getSharpeRatio()));
                                     }
                                     if (mpt3 != null) {
                                               si.setAR5(FormatUtil.formatPercentage(mpt3.getAR()));
                                               si.setBeta5(FormatUtil.formatQuantity(mpt3.getBeta()));
                                               si.setSharpeRatio5(FormatUtil.formatPercentage(mpt3.getSharpeRatio()));
                                     }
                            }
                            strategies.add(si);
                   }
         }	@SuppressWarnings("unchecked")
	public String process() throws Exception {

Template t = BasePage.CustomizePageConf.getTemplate("StrategyClass4.ftl");ByteArrayOutputStream buf = new ByteArrayOutputStream();Writer out = new OutputStreamWriter(buf, "utf8");		executeUserCode();
		t.process(root, out);		return (buf.toString());
	}
	@SuppressWarnings("unchecked")
	public void executeUserCode() throws Exception {

		/*  List<StrategyClassBean> Strategies;
  Long userID;
  User user = userManager.getLoginUser();
  if(user == null){
    userID =  0l;
  }
  else
    userID = user.getID();
                   
  Strategies = new ArrayList<StrategyClassBean>();
  
  List<StrategyItem> items = new ArrayList<StrategyItem>();
// Asset Allocation Strategy Class ID is 3
  StrategyClass sc = strategyClassManager.get(3L);
  setTopStrategies(items, sc.getID());
  if(items.size()>0){
      StrategyClassBean scb = new StrategyClassBean();
      scb.setClassID(sc.getID());
      scb.setName(sc.getName());
      scb.setItems(items);
      Strategies.add(scb);
   }

root.put("strategies",Strategies);*/
root.put("HomeURL", "CustomizePage.action?pageName=");

	}
	@SuppressWarnings("unchecked")
	public String execute() throws Exception {

		init();
		/*  List<StrategyClassBean> Strategies;
  Long userID;
  User user = userManager.getLoginUser();
  if(user == null){
    userID =  0l;
  }
  else
    userID = user.getID();
                   
  Strategies = new ArrayList<StrategyClassBean>();
  
  List<StrategyItem> items = new ArrayList<StrategyItem>();
// Asset Allocation Strategy Class ID is 3
  StrategyClass sc = strategyClassManager.get(3L);
  setTopStrategies(items, sc.getID());
  if(items.size()>0){
      StrategyClassBean scb = new StrategyClassBean();
      scb.setClassID(sc.getID());
      scb.setName(sc.getName());
      scb.setItems(items);
      Strategies.add(scb);
   }

root.put("strategies",Strategies);*/
root.put("HomeURL", "CustomizePage.action?pageName=");
		return Action.SUCCESS;
	}

}
