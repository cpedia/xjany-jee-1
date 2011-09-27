/**
 * 
 */
package com.lti.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.lti.service.StrategyManager;
import com.lti.service.bo.Strategy;
import com.lti.system.ContextHolder;
import com.lti.system.Configuration;
import com.lti.util.PersistentUtil;

/**
 * @author CCD
 *
 */
public class ChangeFileObjectToGlobalObjectUtil {

	public static void changeObjectToGlobal(){
		String TAA_DATA_PLAN = "TAA Data Plan ";
		String TAA_DATA_PLAN_KEY = "TAA_Data_Plan_";
		String SAA_DATA_PLAN = "SAA Data Plan ";
		String SAA_DATA_PLAN_KEY = "SAA_Data_Plan_";
		String DATA_PLAN = "HoldingInfoPlan_";
		StrategyManager sm = (StrategyManager) ContextHolder.getStrategyManager();
		List<Strategy> planList = sm.getStrategiesByType(Configuration.STRATEGY_TYPE_401K);
		for(Strategy plan: planList){
			try {
				HashMap<String, List<String>> _storeData = (HashMap<String, List<String>>)PersistentUtil.readObject(TAA_DATA_PLAN + plan.getID());
				if(_storeData != null)
					PersistentUtil.writeGlobalObject(_storeData, TAA_DATA_PLAN_KEY + plan.getID(), plan.getID(), 0L);
			} catch (Exception e) {
				System.out.println("Error TAA For Plan: " + plan.getID());
			}
			try {
				HashMap<String, List<String>> _storeData = (HashMap<String, List<String>>)PersistentUtil.readObject(SAA_DATA_PLAN + plan.getID());
				if(_storeData != null)
					PersistentUtil.writeGlobalObject(_storeData, SAA_DATA_PLAN_KEY + plan.getID(), plan.getID(), 0L);
			} catch (Exception e) {
				System.out.println("Error SAA For Plan: " + plan.getID());
			}
			try{
				Map<String,Map<String,Integer>> _storeData = (Map<String,Map<String,Integer>>)PersistentUtil.readObject(DATA_PLAN + plan.getID());
				if(_storeData != null)
					PersistentUtil.writeGlobalObject(_storeData, DATA_PLAN + plan.getID(), plan.getID(), 0L);
			}catch (Exception e) {
				System.out.println("Error For Plan: " + plan.getID());
			}
		}
	}
	
	public static void checkPersistent(){
		String TAA_DATA_PLAN = "TAA Data Plan ";
		String TAA_DATA_PLAN_KEY = "TAA_Data_Plan_";
		String SAA_DATA_PLAN = "SAA Data Plan ";
		String SAA_DATA_PLAN_KEY = "SAA_Data_Plan_";
		StrategyManager sm = (StrategyManager) ContextHolder.getStrategyManager();
		//List<Strategy> planList = sm.getStrategiesByType(Configuration.STRATEGY_TYPE_401K);
		Long planID = 1218L;
		//for(Strategy plan: planList){
			try {
				HashMap<String, List<String>> _storeData1 = (HashMap<String, List<String>>)PersistentUtil.readObject(TAA_DATA_PLAN + planID);
				HashMap<String, List<String>> _storeData2 = (HashMap<String, List<String>>)PersistentUtil.readGlobalObject(TAA_DATA_PLAN_KEY + planID);
				Collection<List<String>> list1 = _storeData1.values();
				Collection<List<String>> list2 = _storeData2.values();
				Iterator<List<String>> it1 = list1.iterator();
				while(it1.hasNext()){
					List<String> strList= it1.next();
					for(String str: strList){
						System.out.print(str + " ");
					}
					System.out.println();
				}
				System.out.println("----------------------------in file----------------------------");
				Iterator<List<String>> it2 = list2.iterator();
				while(it2.hasNext()){
					List<String> strList= it2.next();
					for(String str: strList){
						System.out.print(str + " ");
					}
					System.out.println();
				}
				System.out.println("----------------------------in data base----------------------------");
				System.out.println("-----------------------------");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	//}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ChangeFileObjectToGlobalObjectUtil.changeObjectToGlobal();
	}

}
