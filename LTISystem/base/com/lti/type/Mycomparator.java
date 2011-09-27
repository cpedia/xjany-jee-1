package com.lti.type;

import java.util.Comparator;
import java.util.Date;

import com.lti.Exception.SecurityException;
import com.lti.service.bo.Security;

public class Mycomparator implements Comparator{

	@Override
	public int compare(Object arg0, Object arg1) {
		SecurityValue sv1 = (SecurityValue)arg0;
		SecurityValue sv2 = (SecurityValue)arg1;
		
		if(sv1.value < sv2.value)
			return 1;
		else
			return 0;
	}
	
	/*private TimePeriod p;
	private TimeUnit tu;
	private SortType st;
	
	public Mycomparator(Date startDate, Date endDate, SortType St, TimeUnit Tu)
	{
		p = new TimePeriod(startDate, endDate);
		p.setStartDate(startDate);
		p.setEndDate(endDate);
		this.tu = Tu;
		this.st = St;
	}
    public int compare(Object o1,Object o2) {
        Security p1=(Security)o1;
        Security p2=(Security)o2;  
       try {
    	   if(st == SortType.RETURN)
			{
				if(p1.getReturn(p)<p2.getReturn(p))
			       return 1;
			   else
			       return 0;
			}
    	   if(st == SortType.ALPHA)
			{
				if(p1.getAlpha(p.getStartDate(), p.getEndDate(), tu)<p2.getAlpha(p.getStartDate(), p.getEndDate(), tu))
			       return 1;
			   else
			       return 0;
			}
    	   if(st == SortType.BETA)
			{
				if(p1.getBeta(p.getStartDate(), p.getEndDate(), tu)<p2.getBeta(p.getStartDate(), p.getEndDate(), tu))
			       return 1;
			   else
			       return 0;
			}
    	   if(st == SortType.ANNULIZEDRETURN)
			{
				try {
					if(p1.getAnnualizedReturn(p.getStartDate(), p.getEndDate())<p2.getAnnualizedReturn(p.getStartDate(), p.getEndDate()))
					   return 1;
					else
					   return 0;
				} catch (Exception e) {
					// TODO Auto-generated catch block						
					e.printStackTrace();
					return 0;
				}
			}
    	   if(st == SortType.DRAWDOWN)
			{
				if(p1.getDrawDown(p.getStartDate(), p.getEndDate(), tu)<p2.getDrawDown(p.getStartDate(), p.getEndDate(), tu))
			       return 1;
			   else
			       return 0;
			}
    	   if(st == SortType.SHARPE)
			{
				if(p1.getSharpeRatio(p.getStartDate(), p.getEndDate(), tu)<p2.getSharpeRatio(p.getStartDate(), p.getEndDate(), tu))
			       return 1;
			   else
			       return 0;
			}
    	   if(st == SortType.RSQUARED)
			{
				if(p1.getRSquared(p.getStartDate(), p.getEndDate(), tu)<p2.getRSquared(p.getStartDate(), p.getEndDate(), tu))
			       return 1;
			   else
			       return 0;
			}
    	   if(st == SortType.STANDARDDIVIATION)
			{
				if(p1.getStandardDeviation(p.getStartDate(), p.getEndDate(), tu)<p2.getStandardDeviation(p.getStartDate(), p.getEndDate(), tu))
			       return 1;
			   else
			       return 0;
			}
    	   if(st == SortType.TREYNOR)
			{
				if(p1.getTreynorRatio(p.getStartDate(), p.getEndDate(), tu)<p2.getTreynorRatio(p.getStartDate(), p.getEndDate(), tu))
			       return 1;
			   else
			       return 0;
			}
    	   else return 0;
    	   
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
       }*/

}