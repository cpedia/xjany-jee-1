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

public class ModelPortfolios extends CustomizePageAction{
	public ModelPortfolios(){super();}

	private static final long serialVersionUID = 2496394976004782444L;
public List<Portfolio> getPortfolios(String[] names){
	List<Portfolio> portfolios=new ArrayList<Portfolio>();
	if(names==null||names.length==0)return portfolios;
	for(int i=0;i<names.length;i++){
		Portfolio p=portfolioManager.get(names[i]);
		if(p==null)continue;
		portfolios.add(p);
	}
	return portfolios;
}
	
public List<Portfolio> getPortfolios(Long[] ids){
	List<Portfolio> portfolios=new ArrayList<Portfolio>();
	if(ids==null||ids.length==0)return portfolios;
	for(int i=0;i<ids.length;i++){
		Portfolio p=portfolioManager.get(ids[i]);
		if(p==null)continue;
		portfolios.add(p);
	}
	return portfolios;
}

public List<PortfolioItem> getPortfolioItems(List<Portfolio> portfolios) {
	List<PortfolioItem> pis=new ArrayList<PortfolioItem>();
		
	if(portfolios==null||portfolios.size()==0)return pis;
	
	for (int i = 0; i < portfolios.size(); i++) {
		Portfolio p=portfolios.get(i);
		if(p==null)continue;
			
		PortfolioItem pi=new PortfolioItem();
		pi.setID(p.getID());
		pi.setName(p.getName());
		pi.setState(p.getState()==Configuration.PORTFOLIO_STATE_ALIVE?"Live":"");
		pi.setShowName();
			
		PortfolioMPT mpt1 = portfolioManager.getPortfolioMPT(p.getID(), PortfolioMPT.LAST_ONE_YEAR);
		PortfolioMPT mpt2 = portfolioManager.getPortfolioMPT(p.getID(), PortfolioMPT.LAST_THREE_YEAR);
		PortfolioMPT mpt3 = portfolioManager.getPortfolioMPT(p.getID(), PortfolioMPT.LAST_FIVE_YEAR);
			
		if (mpt1 != null) {
			pi.setAR1(FormatUtil.formatPercentage(mpt1.getAR()));
			pi.setBeta1(FormatUtil.formatQuantity(mpt1.getBeta()));
			pi.setSharpeRatio1(FormatUtil.formatPercentage(mpt1.getSharpeRatio()));
		}
		if (mpt2 != null) {
			pi.setAR3(FormatUtil.formatPercentage(mpt2.getAR()));
			pi.setBeta3(FormatUtil.formatQuantity(mpt2.getBeta()));
			pi.setSharpeRatio3(FormatUtil.formatPercentage(mpt2.getSharpeRatio()));
		}
		if (mpt3 != null) {
			pi.setAR5(FormatUtil.formatPercentage(mpt3.getAR()));
			pi.setBeta5(FormatUtil.formatQuantity(mpt3.getBeta()));
			pi.setSharpeRatio5(FormatUtil.formatPercentage(mpt3.getSharpeRatio()));
		}
		
		pis.add(pi);
	}
		
	return pis;
}	@SuppressWarnings("unchecked")
	public String process() throws Exception {

Template t = BasePage.CustomizePageConf.getTemplate("ModelPortfolios.ftl");ByteArrayOutputStream buf = new ByteArrayOutputStream();Writer out = new OutputStreamWriter(buf, "utf8");		executeUserCode();
		t.process(root, out);		return (buf.toString());
	}
	@SuppressWarnings("unchecked")
	public void executeUserCode() throws Exception {

		Long[] ids1={3690L,3688L,3686L};
Long[] ids2={3694L,3696L,3692L};
Long[] ids3={3698L,3700L,3702L};
List<PortfolioItem> lazy_portfolios=getPortfolioItems(getPortfolios(ids1));
root.put("lazy_portfolios", lazy_portfolios);

List<PortfolioItem> simple_portfolios=getPortfolioItems(getPortfolios(ids2));
root.put("simple_portfolios", simple_portfolios);

List<PortfolioItem> flexible_portfolios=getPortfolioItems(getPortfolios(ids3));
root.put("flexible_portfolios", flexible_portfolios);
root.put("HomeURL", "CustomizePage.action?pageName=");

	}
	@SuppressWarnings("unchecked")
	public String execute() throws Exception {

		init();
		Long[] ids1={3690L,3688L,3686L};
Long[] ids2={3694L,3696L,3692L};
Long[] ids3={3698L,3700L,3702L};
List<PortfolioItem> lazy_portfolios=getPortfolioItems(getPortfolios(ids1));
root.put("lazy_portfolios", lazy_portfolios);

List<PortfolioItem> simple_portfolios=getPortfolioItems(getPortfolios(ids2));
root.put("simple_portfolios", simple_portfolios);

List<PortfolioItem> flexible_portfolios=getPortfolioItems(getPortfolios(ids3));
root.put("flexible_portfolios", flexible_portfolios);
root.put("HomeURL", "CustomizePage.action?pageName=");
		return Action.SUCCESS;
	}

}
