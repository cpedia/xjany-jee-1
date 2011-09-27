package com.lti.util;

import java.io.*;
import java.util.*;

import com.lti.service.AssetClassManager;
import com.lti.service.SecurityManager;
import com.lti.service.bo.AssetClass;
import com.lti.service.bo.Security;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;

public final class LTIFactorManager {
	
	public class Factor{
		private String name; 
		private String type;
		
		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		Factor(){};
	};

	private String styleFactorConfigureFile;
	private String RAAFactorConfigureFile;
	
	
	
	private Map<String,List<String>> securityAndFactorMap;
	private Map<Long,List<String>> assetAndFactorMap;
	private Map<String,Factor> factorMap;
	
	private static LTIFactorManager lfm = null;
	
	private LTIFactorManager(){
		String systemPath;
		String sysPath = System.getenv("windir");
		if(!Configuration.isLinux())systemPath=sysPath+"\\temp\\us\\";
		else systemPath="/var/tmp/us/";
		this.setStyleFactorConfigureFile(systemPath+"style-factors.csv");
		this.setRAAFactorConfigureFile(systemPath+"RAA-factors.csv");

		this.securityAndFactorMap = new HashMap<String,List<String>>();
		this.assetAndFactorMap = new HashMap<Long,List<String>>();
		this.factorMap = new HashMap<String,Factor>();
	}

	public static synchronized LTIFactorManager getInstance(String type)
	{
		if(lfm==null)
			lfm = new LTIFactorManager();
		
		if(type.equalsIgnoreCase("RAA"))lfm.getFactorsAndMaps("RAA");
		else if(type.equalsIgnoreCase("Style")) lfm.getFactorsAndMaps("Style");
		
		return lfm;
	}
	
	public void getFactorsAndMaps(String type){
		
		AssetClassManager assetClassManager = (AssetClassManager) ContextHolder.getInstance().getApplicationContext().getBean("assetClassManager");
		
		this.factorMap.clear();
		this.assetAndFactorMap.clear();
		this.securityAndFactorMap.clear();
		
		String fileName;
		if(type.equalsIgnoreCase("Style"))fileName=this.styleFactorConfigureFile;
		else fileName = this.RAAFactorConfigureFile;
		
		try {
			FileReader fr = new FileReader(fileName);
			BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();
			
			int step=0;
			
			while(line!=null){
				if(line.length()==0 || line.charAt(0)=='*' ||line.charAt(0)==','){
				    String preLine = line;
					line = br.readLine();
				    
					if((preLine.length()==0 || preLine.charAt(0)==',')&& line.charAt(0)=='*')
				    	step++;
					
					continue;
				}
				
				if(step==0){
					String[] tmpS = line.trim().split(",");
					Factor fa = new Factor();
					fa.setName(tmpS[0].trim());
					fa.setType(tmpS[1].trim());
					this.factorMap.put(tmpS[0].trim(), fa);
				}
				else if(step==1){
					String[] tmpS = line.trim().split(",");
					List<String> faList = new ArrayList<String>();
					for(int i=1;i<tmpS.length;i++)
						faList.add(tmpS[i].trim());
					this.securityAndFactorMap.put(tmpS[0].trim(), faList);
				}
				else if(step==2){
					String[] tmpS = line.trim().split(",");
					List<String> faList = new ArrayList<String>();
					long classid=0;
					AssetClass ac = assetClassManager.get(tmpS[0].trim());
					classid = (ac==null)?classid:ac.getID();
					
					if(tmpS[1]!=null && tmpS[1].length()>0){
						ac = assetClassManager.getChildClass(tmpS[1].trim(), classid);
						classid = (ac==null)?classid:ac.getID();
					}
					if(tmpS[2]!=null && tmpS[2].length()>0){
						ac = assetClassManager.getChildClass(tmpS[2].trim(), classid);
						classid = (ac==null)?classid:ac.getID();
					}
					if(tmpS[3]!=null && tmpS[3].length()>0){
						ac = assetClassManager.getChildClass(tmpS[3].trim(), classid);
						classid = (ac==null)?classid:ac.getID();
					}
					
					for(int i=4;i<tmpS.length;i++)
						faList.add(tmpS[i].trim());
					this.assetAndFactorMap.put(classid, faList);
				}
				
				line = br.readLine();
				if(line == null){
					br.close();
					fr.close();
					break;
				}
				if(line.length()!=0 && line.charAt(0)=='*')
					step++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void processNewStyleConfigureFile(String newFileName){
		String oldName = this.getStyleFactorConfigureFile();
		File oldFile = new File(oldName);
		File newFile = new File(newFileName);
		if(oldFile != null)
			oldFile.delete();
		newFile.renameTo(oldFile);
		
		//this.getFactorsAndMaps("Style");
	}
	
	public void processNewRAAConfigureFile(String newFileName){
		String oldName = this.getRAAFactorConfigureFile();
		File oldFile = new File(oldName);
		File newFile = new File(newFileName);
		if(oldFile != null)
			oldFile.delete();
		newFile.renameTo(oldFile);
		
		//this.getFactorsAndMaps("RAA");
	}
	
	public List<Factor> getFactorsWithName(List<String> faNameList){
		List<Factor> list = null;
		if(faNameList!=null && faNameList.size()>0){
			list = new ArrayList<Factor>();
			for(int i=0;i<faNameList.size();i++){
				Factor fa = this.factorMap.get(faNameList.get(i));
				if(fa!=null)
					list.add(fa);
			}
		}
		return list;
	}
	
	public List<Factor> getFactorsForSecurity(long securityID){
		SecurityManager securityManager=(SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
		Security se = securityManager.get(securityID);
		return getFactorsForSecurity(se.getSymbol());
	}
	
	public List<Factor> getFactorsForAsset(long assetClassID){
		List<String> list =null;
		if(this.assetAndFactorMap.containsKey(assetClassID))
		{
			list= this.assetAndFactorMap.get(assetClassID);
			if(list!=null && list.size()>0)
				return this.getFactorsWithName(list);
		}
		return null;
	}
	
	public List<Factor> getFactorsForSecurity(String symbol){
		SecurityManager securityManager=(SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
		Security se = securityManager.getBySymbol(symbol);
		List<String> list = this.securityAndFactorMap.get(symbol);
		if(list==null || list.size()==0){
			list = this.assetAndFactorMap.get(se.getAssetClass().getID());
		}
		return this.getFactorsWithName(list);
	}
	
	public List<Factor> getFactorsForAsset(String assetClassName){
		AssetClassManager assetClassManager=(AssetClassManager) ContextHolder.getInstance().getApplicationContext().getBean("assetClassManager");
		AssetClass as = assetClassManager.get(assetClassName);
		List<String> list = this.assetAndFactorMap.get(as.getID());
		return this.getFactorsWithName(list);
	}

	public String getStyleFactorConfigureFile() {
		return styleFactorConfigureFile;
	}

	public void setStyleFactorConfigureFile(String styleFactorConfigureFile) {
		this.styleFactorConfigureFile = styleFactorConfigureFile;
	}

	public String getRAAFactorConfigureFile() {
		return RAAFactorConfigureFile;
	}

	public void setRAAFactorConfigureFile(String factorConfigureFile) {
		RAAFactorConfigureFile = factorConfigureFile;
	}
	public static void main(String[] args)
	{
		LTIFactorManager lfm=LTIFactorManager.getInstance("RAA");
		List<Factor> factorList = lfm.getFactorsForSecurity("SVBIX");
		if(factorList!=null && factorList.size()>0)
		{
			for(int i=0;i<factorList.size();++i)
				System.out.println(factorList.get(i).getName());
		}
	}
}
