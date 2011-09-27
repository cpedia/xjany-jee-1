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

public class GuruAllocation extends CustomizePageAction{
	public GuruAllocation(){super();}

	private static final long serialVersionUID = 2496394976004782444L;
public String getLink(String symbol, int interval,Date startDate,Date endDate, boolean allowNegative, String[] indexArray){
       String[] lowerArray=new String[indexArray.length];
       String[] upperArray=new String[indexArray.length];
       for(int i=0;i<lowerArray.length;i++){
              lowerArray[i]="0";
              upperArray[i]="1";
       }
       return this.getLink(symbol, interval,startDate,endDate, allowNegative,  indexArray,lowerArray,upperArray);
}
public String getLink(String symbol, int interval,Date startDate,Date endDate, boolean allowNegative, String[] indexArray, String[] lowerArray,String upperArray[]){
		StringBuffer sb = new StringBuffer();
		sb.append("http://www.validfi.com/LTISystem/jsp/mutualfund/Result.action?symbol=");
		sb.append(symbol);
		sb.append("&interval=");
		sb.append(interval);

		Date date = new Date();
		java.text.DateFormat df = new java.text.SimpleDateFormat("MM/dd/yyyy");
		sb.append("&endDate=");
		sb.append(df.format(endDate));

		Calendar ca = Calendar.getInstance();
		ca.add(Calendar.MONTH, -6);
		sb.append("&startDate=");
		sb.append(df.format(startDate));

		sb.append("&allowNegative=");
		sb.append("false");

		sb.append("&indexArray=");
		for (int i = 0; i < indexArray.length; i++) {
			sb.append(indexArray[i]);
			if (i != indexArray.length - 1)
				sb.append(",%20");
		}

                sb.append("&lowerArray=");
		for (int i = 0; i < lowerArray.length; i++) {
			sb.append(lowerArray[i]);
			if (i != lowerArray.length - 1)
				sb.append(",");
		}

                sb.append("&upperArray=");
		for (int i = 0; i < upperArray.length; i++) {
			sb.append(upperArray[i]);
			if (i != upperArray.length - 1)
				sb.append(",");
		}
		return sb.toString();

}
	@SuppressWarnings("unchecked")
	public String process() throws Exception {

Template t = BasePage.CustomizePageConf.getTemplate("GuruAllocation.ftl");ByteArrayOutputStream buf = new ByteArrayOutputStream();Writer out = new OutputStreamWriter(buf, "utf8");		executeUserCode();
		t.process(root, out);		return (buf.toString());
	}
	@SuppressWarnings("unchecked")
	public void executeUserCode() throws Exception {

		Date CurrentDate = new Date();
CurrentDate=LTIDate.add(CurrentDate, -7);
String HSGFXRAAanalysis=getLink("HSGFX",12,LTIDate.getDate(2008,1,1),CurrentDate,false,new String[]{"VFINX","CASH"});
String GLRBXRAAanalysis=getLink("GLRBX",12,LTIDate.getDate(2008,1,1),CurrentDate,false,new String[]{"VFINX","CASH"});
String CGMFXRAAanalysis=getLink("CGMFX",12,LTIDate.getDate(2008,1,1),CurrentDate,false,new String[]{"XLE","XLB","XLY","XLP","XLV","XLF","XLK","XLU"});
String GGHEXRAAanalysis=getLink("GGHEX",12,LTIDate.getDate(2008,1,1),CurrentDate,false,new String[]{"VFINX","VGTSX","VGSIX","VBMFX"});

String GBMFXRAAanalysis=getLink("GBMFX",12,LTIDate.getDate(2008,1,1),CurrentDate,false,new String[]{"VFINX","VGTSX","BEGBX","VBMFX"});

String LCORXRAAanalysis=getLink("LCORX",12,LTIDate.getDate(2008,1,1),CurrentDate,false,new String[]{"VFINX","VGSIX","VBMFX","CASH"});

String WASAXRAAanalysis=getLink("WASAX",12,LTIDate.getDate(2008,1,1),CurrentDate,false,new String[]{"VFINX","VGTSX","QRAAX","BEGBX","VBMFX"});

String PASDXRAAanalysis=getLink("PASDX",12,LTIDate.getDate(2008,1,1),CurrentDate,false,new String[]{"VFINX","VGTSX","QRAAX","BEGBX","VBMFX"});

root.put("HSGFXRAAanalysis",HSGFXRAAanalysis);
root.put("GLRBXRAAanalysis",GLRBXRAAanalysis);
root.put("CGMFXRAAanalysis",CGMFXRAAanalysis);
root.put("GGHEXRAAanalysis",GGHEXRAAanalysis);
root.put("GBMFXRAAanalysis",GBMFXRAAanalysis);
root.put("LCORXRAAanalysis",LCORXRAAanalysis);
root.put("WASAXRAAanalysis",WASAXRAAanalysis);
root.put("PASDXRAAanalysis",PASDXRAAanalysis);


	}
	@SuppressWarnings("unchecked")
	public String execute() throws Exception {

		init();
		Date CurrentDate = new Date();
CurrentDate=LTIDate.add(CurrentDate, -7);
String HSGFXRAAanalysis=getLink("HSGFX",12,LTIDate.getDate(2008,1,1),CurrentDate,false,new String[]{"VFINX","CASH"});
String GLRBXRAAanalysis=getLink("GLRBX",12,LTIDate.getDate(2008,1,1),CurrentDate,false,new String[]{"VFINX","CASH"});
String CGMFXRAAanalysis=getLink("CGMFX",12,LTIDate.getDate(2008,1,1),CurrentDate,false,new String[]{"XLE","XLB","XLY","XLP","XLV","XLF","XLK","XLU"});
String GGHEXRAAanalysis=getLink("GGHEX",12,LTIDate.getDate(2008,1,1),CurrentDate,false,new String[]{"VFINX","VGTSX","VGSIX","VBMFX"});

String GBMFXRAAanalysis=getLink("GBMFX",12,LTIDate.getDate(2008,1,1),CurrentDate,false,new String[]{"VFINX","VGTSX","BEGBX","VBMFX"});

String LCORXRAAanalysis=getLink("LCORX",12,LTIDate.getDate(2008,1,1),CurrentDate,false,new String[]{"VFINX","VGSIX","VBMFX","CASH"});

String WASAXRAAanalysis=getLink("WASAX",12,LTIDate.getDate(2008,1,1),CurrentDate,false,new String[]{"VFINX","VGTSX","QRAAX","BEGBX","VBMFX"});

String PASDXRAAanalysis=getLink("PASDX",12,LTIDate.getDate(2008,1,1),CurrentDate,false,new String[]{"VFINX","VGTSX","QRAAX","BEGBX","VBMFX"});

root.put("HSGFXRAAanalysis",HSGFXRAAanalysis);
root.put("GLRBXRAAanalysis",GLRBXRAAanalysis);
root.put("CGMFXRAAanalysis",CGMFXRAAanalysis);
root.put("GGHEXRAAanalysis",GGHEXRAAanalysis);
root.put("GBMFXRAAanalysis",GBMFXRAAanalysis);
root.put("LCORXRAAanalysis",LCORXRAAanalysis);
root.put("WASAXRAAanalysis",WASAXRAAanalysis);
root.put("PASDXRAAanalysis",PASDXRAAanalysis);

		return Action.SUCCESS;
	}

}
