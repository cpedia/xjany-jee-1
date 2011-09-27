package com.lti.util;

import java.util.List;

import com.lti.service.bo.AssetClass;

public class GenerateAssetClassTree {
	public static long getRoot(List<AssetClass> treeList){
	 	long min=10000;
	 	for(int i=0;i<treeList.size();i++)
	 		if(treeList.get(i).getID()<min)min=treeList.get(i).getID();
	 	return min;
	}

	public static long getTreeDeepth(List<AssetClass> treeList){
	 	long root=getRoot(treeList);
	 	return getTreeDeepth1(treeList,root);
	}

	public static long getTreeDeepth1(List<AssetClass> treeList,long parentID){
	 	long deepth=1;
	 	for(int i=0;i<treeList.size();i++){
	 		if(treeList.get(i).getParentID()!=parentID)continue;
	 		long tmp=1+getTreeDeepth1(treeList,treeList.get(i).getID());
	 		if(deepth<tmp)deepth=tmp;
	 	}
	 	return deepth;
	}
	public static String getTreeList(List<AssetClass> treeList){
		String resultString="";
		resultString+="<select id=\"classTree\" style=\"display:none\">";
		for(int i=0;i<treeList.size();i++){
			if(treeList.get(i).getID()==0)continue;
			resultString+="<option class=\""+treeList.get(i).getParentID()+"\" value=\""+
			treeList.get(i).getID()
			+"\">"+treeList.get(i).getName()
			+"</option>";
		}
		resultString+="<option class=\"treeDepth\" value=\""+(getTreeDeepth(treeList)-1)+"\">treeDepth</option>";
		resultString+="</select>";
		return resultString;
	}
}
