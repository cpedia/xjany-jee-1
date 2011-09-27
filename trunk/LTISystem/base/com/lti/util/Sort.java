package com.lti.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.lti.service.bo.AssetClass;
import com.lti.service.bo.VariableFor401k;
import com.lti.type.Menu;

public class Sort {
	public static List<List<String>> filesort(List<List<String>> lists){
		Collections.sort(lists, new Comparator< List<String> >(){
			public int compare(List<String> o1,List<String> o2){
				if(Double.parseDouble(o1.get(0))<Double.parseDouble(o2.get(0)))
					return 1;
				else 
					return 0;
			}
		});
		return lists;
	}
	
	/**
	 * sort assetClass list with the name of assetClass
	 * @param lists
	 * @return
	 */
	public static List<AssetClass> assetClassSort(List<AssetClass>lists){
		Collections.sort(lists,new Comparator<AssetClass>(){
			public int compare(AssetClass ac1,AssetClass ac2){
				if(ac1.getName().compareToIgnoreCase(ac2.getName())<0 )
				   return 0;
				else
					return 1;
			}
			
		});
		return lists;
	}
	
	/**
	 * sort menu list with the menu text
	 * @param lists
	 * @return
	 */
	public static List<Menu> menuSort(List<Menu> lists){
		Collections.sort(lists,new Comparator<Menu>(){
			public int compare(Menu m1,Menu m2){
				if(m1.getText().compareToIgnoreCase(m2.getText())<0)
					return 0;
				else
					return 1;				
			}
			
		});
		return lists;
	}
	
	/**
	 * sort VariableFor401k list with assetClassName 
	 * @param lists
	 * @return
	 */
	public static List<VariableFor401k>variableSort(List<VariableFor401k> lists){
		Collections.sort(lists, new Comparator<VariableFor401k>(){
			public int compare(VariableFor401k v1,VariableFor401k v2){
				if(v1.getAssetClassName().compareToIgnoreCase(v2.getAssetClassName())<0)
					return 0;
				else if(v1.getAssetClassName().compareToIgnoreCase(v2.getAssetClassName())==0){
					if(v1.getDescription()!=null && v2.getDescription()!=null){
						if(v1.getDescription().compareToIgnoreCase(v2.getDescription())<0 )
							return 0;
						else 
							return 1;
					}else
						 return 1;
					
				}else 
					return 1;
			}
			
		});
		return lists;
	}
}
