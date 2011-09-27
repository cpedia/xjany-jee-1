package com.lti.database;

import java.util.ArrayList;
import java.util.List;

import com.lti.service.AssetClassManager;
import com.lti.service.bo.AssetClass;
import com.lti.system.ContextHolder;

public class BuildAssetSelect {
	static AssetClassManager acm=ContextHolder.getAssetClassManager();
	static List<AssetClass> list=new ArrayList<AssetClass>();
	static int level=-1;
	static String getS(int l){
		String[] strs={"--","++","+","","","",""};
		return strs[l];
	}
	public static void build(AssetClass ac,String prefix){
		List<AssetClass> acs=acm.getChildClass(ac.getID());
		if(acs!=null&&acs.size()>0){
			
			for(int i=0;i<acs.size();i++){
				level++;
				list.add(acs.get(i));
				int size=list.size();
				build(acs.get(i), prefix+"&nbsp;&nbsp;&nbsp;&nbsp;"); 
				String name=null;
				if(size==list.size()&&level!=0){
					name=prefix+acs.get(i).getName();
				}else{
					name=prefix+getS(level)+acs.get(i).getName();
				}
				acs.get(i).setName(name);
				level--;
			}
		}
	}
	
	
	public static void main(String[] args){
		AssetClass ac=new AssetClass();
		ac.setID(0l);
		build(ac,"");
		StringBuffer sb=new StringBuffer();
		for(AssetClass str:list){
			List<String> ss=acm.getAbsoluteClassName(str.getID());
			String fn="";
			if(ss!=null){
				for(int i=0;i<ss.size();i++){
					String s=ss.get(i);
					fn+=s;
					if(i!=ss.size()-1)fn+=" -> ";
					
				}
			}
			
			sb.append("<option value='"+str.getID()+". "+fn+"'>");
			sb.append(str.getName());
			sb.append("</option>");
			//sb.append("\r\n");
		}
		System.out.println(sb.toString());
	}
}
