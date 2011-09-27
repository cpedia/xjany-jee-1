package com.lti.database;

import java.util.List;

import com.lti.service.PortfolioManager;
import com.lti.service.StrategyManager;
import com.lti.service.bo.Strategy;
import com.lti.system.ContextHolder;
import com.lti.util.StringUtil;

public class Fixed20100921 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		StrategyManager sm=ContextHolder.getStrategyManager();
		PortfolioManager pm=ContextHolder.getPortfolioManager();
		List<Strategy> strs=sm.findByHQL("from Strategy s where s.Name like '%\n%'");
		for(Strategy str:strs){
			str.setName(StringUtil.getValidName2(str.getName()));
			System.out.println(str.getName());
			sm.update(str);
		}
		long[] ids=new long[]{
		14212l,
		14214l,
		14216l,
		14218l,
		14220l,
		14222l,
		14272l,
		14274l,
		14276l,
		14278l,
		14280l,
		14282l,
		14574l,
		14576l,
		14578l,
		14580l,
		14582l,
		14584l,
		16518l,
		16520l,
		16522l,
		18129l,
		18131l,
		18133l,
		18135l,
		18137l,
		18139l,
		19201l,
		19203l,
		19205l,
		19207l,
		19209l,
		19211l
		};
		for(long id:ids){
			try {
				pm.remove(id);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
