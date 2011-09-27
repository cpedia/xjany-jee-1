package com.lti.util;

import com.lti.service.CustomizePageManager;
import com.lti.service.GroupManager;
import com.lti.service.bo.CustomizeRegion;
import com.lti.system.ContextHolder;

public class CustomizeUtil {
	public static final String HOME_PAGE = "Home Page";
	public static final String PORTFOLIO_MAIN = "Portfolio Main Page";
	public static final String PORTFOLIO_INDIVIDUAL = "Portfolio Individual Page";
	public static final String STRATEGY_MAIN = "Strategy Main Page";
	public static final String STRATEGY_INDIVIDUAL = "Strategy Individual Page";
	public static final String STRATEGY_SEARCHRESULT = "Strategy Search Result Page";
	public static final String SECURITY_MAIN = "Security Main Page";
	public static final String STRATEGY_DELETE = "Strategy Delete Page";
	public static final String SECURITY_INDIVIDUAL = "Security Individual Page";
	public static final String PORTFOLIO_DAILYDATA = "Portfolio Daily Data Page";
	public static final String PORTFOLIO_MPT = "Portfolio MPT Page";
	public static final String PORTFOLIO_TRASACTION = "Portfolio Transaction Page";
	public static final String PORTFOLIO_LOG = "Portfolio Log Page";
	public static final String PORTFOLIO_SEARCHRESULT = "Portfolio Search Result Page";
	public static final String COMMENTARY_MAIN = "Commentary Main Page";
	public static final String SECURITY_SCREENING_RESULT = "Security Screening Result Page";
	public static final String SECURITY_SCREENING = "Security Screening Page";
	public static final String JFORUM_MAIN = "Jforum Main Page";
	public static final String BLAPP_INFO = "B.L. Application Information Page";
	public static final String BLAPP_RESULT = "B.L. Application Result Page";
	public static final String RAA_MAIN = "RAA Main Page";
	public static final String RAA_CALCULATE = "RAA Calculate Page";
	public static final String RAA_RESULT = "RAA Result Page";
	
	private static Boolean isAuthorized(Long authorizeGroupID, Object[] groupIDs){
		if(groupIDs == null)
			return false;
		if(authorizeGroupID == null)
			return true; 
		if(authorizeGroupID == 1)
			return true;
		for(int i = 0; i < groupIDs.length; i++){
			Long groupID =(Long) groupIDs[i];
			if(authorizeGroupID.equals(groupID))
				return true;
		}
		return false;
	}
	
	private static void setRegionContents(CustomizeRegion cr, Object[] groupIDs){
		CustomizePageManager customizePageManager=(CustomizePageManager) ContextHolder.getInstance().getApplicationContext().getBean("customizePageManager");
		if(cr.getEast() != null && cr.getEast() != 0l && isAuthorized(cr.getEastGroupID(), groupIDs) == true){
			 cr.setEastRegionName(customizePageManager.get(cr.getEast()).getName());
		}
		else if(cr.getEast() != null && cr.getEast() == 0l){
			cr.setEastRegionName("Fixed");
		}
		else
			cr.setEastRegionName(null);
		if(cr.getWest() != null && cr.getWest() != 0l  && isAuthorized(cr.getWestGroupID(), groupIDs) == true){
			cr.setWestRegionName(customizePageManager.get(cr.getWest()).getName());
		}
		else if(cr.getWest() != null && cr.getWest() == 0l){
			cr.setWestRegionName("Fixed");
		}
		else
			cr.setWestRegionName(null);
		if(cr.getSouth() != null && cr.getSouth() != 0l && isAuthorized(cr.getSouthGroupID(), groupIDs) == true){
			cr.setSouthRegionName(customizePageManager.get(cr.getSouth()).getName());
		}
		else if(cr.getSouth() != null && cr.getSouth() == 0l){
			cr.setSouthRegionName("Fixed");
		}
		else
			cr.setSouthRegionName(null);
		if(cr.getCenter() != null && cr.getCenter() != 0){
			cr.setCenterRegionName(customizePageManager.get(cr.getCenter()).getName());
		}
		else
			cr.setCenterRegionName(null);
	}
	
	private static void setRegionTitles(CustomizeRegion cr){
		if(cr.getEast()!=null && cr.getEastTitle() == null)
			cr.setEastTitle("east");
		if(cr.getSouth()!=null && cr.getSouthTitle() == null)
			cr.setSouthTitle("south");
		if(cr.getWest() != null && cr.getWestTitle() == null)
			cr.setWestTitle("west");
	}
	
	public static void setRegion(CustomizeRegion cr, Long UserID)
	{
		GroupManager groupManager=(GroupManager) ContextHolder.getInstance().getApplicationContext().getBean("groupManager");
		Object[] GroupIDs;
		GroupIDs = groupManager.getGroupIDs(UserID);
		if(cr != null){
			setRegionContents(cr, GroupIDs);
			setRegionTitles(cr);
			setRegionWidth(cr);
		}
	}
	
	public static void setRegionContentName(CustomizeRegion cr){
		CustomizePageManager customizePageManager=(CustomizePageManager) ContextHolder.getInstance().getApplicationContext().getBean("customizePageManager");
		if(cr.getEast() == null){
			cr.setEastRegionName("No Content");
		}
		else if(cr.getEast() == 0l){
			cr.setEastRegionName("Fixed");
		}
		else
		{
			cr.setEastRegionName(customizePageManager.get(cr.getEast()).getName());
		}
		if(cr.getWest() == null){
			cr.setWestRegionName("No Content");
		}
		else if(cr.getWest() == 0l){
			cr.setWestRegionName("Fixed");
		}
		else
		{
			cr.setWestRegionName(customizePageManager.get(cr.getWest()).getName());
		}
		if(cr.getSouth() == null){
			cr.setSouthRegionName("No Content");
		}
		else if(cr.getSouth() == 0l){
			cr.setSouthRegionName("Fixed");
		}
		else
		{
			cr.setSouthRegionName(customizePageManager.get(cr.getSouth()).getName());
		}
		if(cr.getCenter() == null || cr.getCenter() == 0l){
			cr.setCenterRegionName("Fixed");
		}
		else
		{
			cr.setCenterRegionName(customizePageManager.get(cr.getCenter()).getName());
		}
	}
	
	public static void setRegionWidth(CustomizeRegion cr){
		if(cr.getEast() != null && cr.getEastWidth() != null && cr.getEastWidth() != 0){
			cr.setEastWidthStr(cr.getEastWidth() * 100 + "%");
		}
		//set east side default width
		else if(cr.getEast() != null && (cr.getEastWidth() == null || cr.getEastWidth() == 0)){
			cr.setEastWidthStr("20%");
		}
		else
			cr.setEastWidthStr("0");
		
		if(cr.getSouth() != null && cr.getSouthHeight() != null && cr.getSouthHeight() != 0){
			cr.setSouthHeightStr(cr.getSouthHeight() * 100 + "%");
		}
		//set south side default width
		else if(cr.getSouthHeight() != null && (cr.getSouthHeight() == null || cr.getSouthHeight() == 0)){
			cr.setSouthHeightStr("10%");
		}
		else
			cr.setSouthHeightStr("0");
		
		if(cr.getWest() != null && cr.getWestWidth() != null && cr.getWestWidth() != 0){
			cr.setWestWidthStr(cr.getWestWidth() * 100 + "%");
		}
		//set west side default width
		else if(cr.getWest() != null && (cr.getWestWidth() == null || cr.getWestWidth() == 0)){
			cr.setWestWidthStr("20%");
		}
		else
			cr.setWestWidthStr("0");
	}
	
	//copy the c1 to c2 except for the ID
	public static void copyCustomizeRegionWithoutID(CustomizeRegion c1, CustomizeRegion c2){
		if(c1 == null || c2 == null)
			return;
		c2.setCenter(c1.getCenter());
		c2.setCenterTitle(c1.getCenterTitle());
		c2.setEast(c1.getEast());
		c2.setEastGroupID(c1.getEastGroupID());
		c2.setEastTitle(c1.getEastTitle());
		c2.setWest(c1.getWest());
		c2.setWestGroupID(c1.getWestGroupID());
		c2.setWestTitle(c1.getWestTitle());
		c2.setSouth(c1.getSouth());
		c2.setSouthGroupID(c1.getSouthGroupID());
		c2.setSouthTitle(c1.getSouthTitle());
		c2.setPageName(c1.getPageName());
	}
}
