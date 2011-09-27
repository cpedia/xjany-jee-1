/**
 * 
 */
package com.lti.util;

import java.util.Comparator;

import com.lti.service.bo.PortfolioMPT;
import com.lti.type.SortType;

/**
 * @author CCD
 *
 */
public class PortfolioMPTComparator implements Comparator{

	public SortType st;
	public SortType getSt() {
		return st;
	}
	public void setSt(SortType st) {
		this.st = st;
	}
	public PortfolioMPTComparator(SortType st){
		this.st = st;
	}
	@Override
	public int compare(Object o1, Object o2) {
		// TODO Auto-generated method stub
		PortfolioMPT p1 = (PortfolioMPT) o1;
		PortfolioMPT p2 = (PortfolioMPT) o2;
		
		if(st == SortType.ANNULIZEDRETURN){
			if( p1.getAR() < p2.getAR())
				return 1;
			return 0;
		}
		if(st == SortType.SORTINO){
			if( p1.getSortinoRatio() < p2.getSortinoRatio())
				return 1;
			return 0;
		}
		if(st == SortType.SHARPE){
			if( p1.getSharpeRatio() < p2.getSharpeRatio())
				return 1;
			return 0;
		}
		if(st == SortType.TREYNOR){
			if( p1.getTreynorRatio() < p2.getTreynorRatio())
				return 1;
			return 0;
		}
		if(st == SortType.DRAWDOWN){
			if( p1.getDrawDown() > p2.getDrawDown())
				return 1;
			return 0;
		}
		return 0;
	}
}
