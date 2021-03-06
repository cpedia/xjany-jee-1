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

public class FoliosAA extends CustomizePageAction{
	public FoliosAA(){super();}

	private static final long serialVersionUID = 2496394976004782444L;
	@SuppressWarnings("unchecked")
	public String process() throws Exception {

Template t = BasePage.CustomizePageConf.getTemplate("FoliosAA.ftl");ByteArrayOutputStream buf = new ByteArrayOutputStream();Writer out = new OutputStreamWriter(buf, "utf8");		executeUserCode();
		t.process(root, out);		return (buf.toString());
	}
	@SuppressWarnings("unchecked")
	public void executeUserCode() throws Exception {

		long[] ids=new long[]{19493,19495,19791,19497,19579};
StringBuffer sb=new StringBuffer();
sb.append("from CachePortfolioItem gpi where gpi.GroupID=0 and ");
if(ids.length==1){
  sb.append("gpi.PortfolioID=");
  sb.append(ids[0]);
}else{
  for (int i = 0; i < ids.length; i++) {
    Long gid = ids[i];
    if(i==0){
      sb.append("(");
      sb.append("gpi.PortfolioID=");
      sb.append(gid);
    }else {
      sb.append(" or ");
      sb.append("gpi.PortfolioID=");
      sb.append(gid);
   }
   if(i==ids.length-1){
      sb.append(")");
   }
  }//end for
}//end else
PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();
List<CachePortfolioItem> ps = portfolioManager.findByHQL(sb.toString());
root.put("portfolios",ps);

ids=new long[]{19487,19489,19795,19491,19797};
sb=new StringBuffer();
sb.append("from CachePortfolioItem gpi where gpi.GroupID=0 and ");
if(ids.length==1){
  sb.append("gpi.PortfolioID=");
  sb.append(ids[0]);
}else{
  for (int i = 0; i < ids.length; i++) {
    Long gid = ids[i];
    if(i==0){
      sb.append("(");
      sb.append("gpi.PortfolioID=");
      sb.append(gid);
    }else {
      sb.append(" or ");
      sb.append("gpi.PortfolioID=");
      sb.append(gid);
   }
   if(i==ids.length-1){
      sb.append(")");
   }
  }//end for
}//end else
portfolioManager = ContextHolder.getPortfolioManager();
List<CachePortfolioItem> ps1 = portfolioManager.findByHQL(sb.toString());
root.put("portfolios1",ps1);

	}
	@SuppressWarnings("unchecked")
	public String execute() throws Exception {

		init();
		long[] ids=new long[]{19493,19495,19791,19497,19579};
StringBuffer sb=new StringBuffer();
sb.append("from CachePortfolioItem gpi where gpi.GroupID=0 and ");
if(ids.length==1){
  sb.append("gpi.PortfolioID=");
  sb.append(ids[0]);
}else{
  for (int i = 0; i < ids.length; i++) {
    Long gid = ids[i];
    if(i==0){
      sb.append("(");
      sb.append("gpi.PortfolioID=");
      sb.append(gid);
    }else {
      sb.append(" or ");
      sb.append("gpi.PortfolioID=");
      sb.append(gid);
   }
   if(i==ids.length-1){
      sb.append(")");
   }
  }//end for
}//end else
PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();
List<CachePortfolioItem> ps = portfolioManager.findByHQL(sb.toString());
root.put("portfolios",ps);

ids=new long[]{19487,19489,19795,19491,19797};
sb=new StringBuffer();
sb.append("from CachePortfolioItem gpi where gpi.GroupID=0 and ");
if(ids.length==1){
  sb.append("gpi.PortfolioID=");
  sb.append(ids[0]);
}else{
  for (int i = 0; i < ids.length; i++) {
    Long gid = ids[i];
    if(i==0){
      sb.append("(");
      sb.append("gpi.PortfolioID=");
      sb.append(gid);
    }else {
      sb.append(" or ");
      sb.append("gpi.PortfolioID=");
      sb.append(gid);
   }
   if(i==ids.length-1){
      sb.append(")");
   }
  }//end for
}//end else
portfolioManager = ContextHolder.getPortfolioManager();
List<CachePortfolioItem> ps1 = portfolioManager.findByHQL(sb.toString());
root.put("portfolios1",ps1);
		return Action.SUCCESS;
	}

}
