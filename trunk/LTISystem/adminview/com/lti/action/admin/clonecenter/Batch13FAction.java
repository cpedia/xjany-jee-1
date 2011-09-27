package com.lti.action.admin.clonecenter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.supercsv.io.CsvListReader;
import org.supercsv.io.CsvListWriter;
import org.supercsv.prefs.CsvPreference;

import com.lti.service.CloningCenterManager;
import com.lti.system.Configuration;
import com.lti.util.StringUtil;

public class Batch13FAction {
	private File batchFile;
	private String batchFileFileName;
	private String actionMessage;
	private String outputFileName;
	public String getActionMessage() {
		return actionMessage;
	}
	public void setActionMessage(String actionMessage) {
		this.actionMessage = actionMessage;
	}
	public File getBatchFile() {
		return batchFile;
	}
	public void setBatchFile(File batchFile) {
		this.batchFile = batchFile;
	}
	public String getBatchFileFileName() {
		return batchFileFileName;
	}
	public void setBatchFileFileName(String batchFileFileName) {
		this.batchFileFileName = batchFileFileName;
	}
	@Deprecated
	public String execute(){
//		BufferedReader br=null;
//		CsvListReader cr=null;
//		
//		CsvListWriter cw=null;
//		
//		try {
//			br=new BufferedReader(new FileReader(batchFile));
//			cr=new CsvListReader(br,CsvPreference.STANDARD_PREFERENCE);
//			outputFileName="batchOutput"+(new Date()).getTime()+".csv";
//			File outputfile=new File(Configuration.getTempDir(),outputFileName);
//			cw=new CsvListWriter(new FileWriter(outputfile),CsvPreference.STANDARD_PREFERENCE);
//			List<String> hlist=new ArrayList<String>(5);
//			hlist.add("Portfolio Name");
//			hlist.add("13F Name");
//			hlist.add("Category");
//			hlist.add("Description");	
//			hlist.add("Memo");
//			cw.write(hlist);
//			List<String> list=cr.read();
//			while(list!=null){
//				if(list.size()<2||list.get(0).trim().equals("")||list.get(1).trim().equals("")||list.get(0).trim().toLowerCase().equalsIgnoreCase("portfolio name")){
//					list=cr.read();
//					continue;
//				}
//				List<String> olist=new ArrayList<String>(5);
//				try {
//					String pname=list.get(0);
//					String _13fname=list.get(1);
//					String category="";
//					if(list.size()>=3)category=list.get(2);
//					String description="";
//					if(list.size()>=4)description=list.get(3);
//					olist.add(pname);
//					olist.add(_13fname);
//					olist.add(category);
//					olist.add(description);
//					List<String[]> strList=CloningCenterManager.get13FName(_13fname);
//					if(strList!=null&&strList.size()!=0){
//						if(strList.size()==1){
//							CloningCenterManager.create13FPortfolio(pname,strList.get(0)[0], null,StringUtil.sortCategories(category),description);
//							olist.add("");
//							olist.set(1, strList.get(0)[0]);
//						}else{
//							StringBuffer sb=new StringBuffer();
//							sb.append("Failed.\n");
//							for(int i=0;i<strList.size();i++){
//								sb.append(i+1);
//								sb.append(". ");
//								sb.append(strList.get(i)[0]);
//								sb.append("\n");
//							}
//							olist.add(sb.toString());
//						}
//					}else{
//						olist.add("No found.");
//					}
//					
//				} catch (Exception e) {
//					olist.add(e.getMessage());
//				}
//				cw.write(olist);
//				list=cr.read();
//			}
//			actionMessage="See the ouput csv to get the batch result.";
//		} catch (Exception e) {
//			actionMessage=e.getMessage();
//		}finally{
//			if(cr!=null){
//				try {
//					cr.close();
//				} catch (IOException e) {
//				}
//			}
//			if(cw!=null){
//				try {
//					cw.close();
//				} catch (IOException e) {
//				}
//			}
//			batchFile.delete();
//		}
		return "success";
	}
	public String getOutputFileName() {
		return outputFileName;
	}
	public void setOutputFileName(String outputFileName) {
		this.outputFileName = outputFileName;
	}
}
