package com.lti.start;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.lti.executor.Compiler;
import com.lti.service.StrategyManager;
import com.lti.service.bo.Strategy;
import com.lti.service.bo.StrategyCode;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;

public class CompiledAllStrategies {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		StrategyManager sm = (StrategyManager) ContextHolder.getInstance().getApplicationContext().getBean("strategyManager");

		List<Strategy> strategies = sm.getStrategiesByType(Configuration.STRATEGY_TYPE_NORMAL);
		System.out.println(strategies.size());

		boolean parallel = false;

		for (int i = 0; i < strategies.size(); i++) {
			Strategy s = strategies.get(i);
			StrategyCode sc = sm.getLatestStrategyCode(s.getID());
			if (s.getName() == null || s.getName().length() == 0)
				continue;
			try {
				Long strclassid=s.getStrategyClassID();
				if(strclassid==null)strclassid=0l;
				if (Compiler.complie(sc.getCode(),strclassid).length() != 0) {
					try {
						// Compiler.delete(s);
					} catch (Exception e1) {

					}
					System.out.println("Number " + i + ": " + s.getName() + " ... Failed to Compile");
				}
				System.out.println("Number " + i + ": " + s.getName() + " ... OK");
			} catch (Exception e) {
				System.out.println("Number " + i + ": " + s.getName() + " ... ERROR");
				e.printStackTrace();
				try {
					Compiler.delete(sc.getCode());
				} catch (Exception e1) {

				}
			}
		}

	}

}
