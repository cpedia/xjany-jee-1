package com.lti.action.admin.group.emails;

import com.lti.action.Action;
import com.lti.service.GroupManager;
import com.opensymphony.xwork2.ActionSupport;

public class InsertPortfolioAction extends ActionSupport implements Action
{
	/**
	 * whj
	 */
	private static final long serialVersionUID = 1L;
	
	private String portfolioIDs;
	private GroupManager groupManager;
	private String message;
	
	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public GroupManager getGroupManager()
	{
		return groupManager;
	}

	public void setGroupManager(GroupManager groupManager)
	{
		this.groupManager = groupManager;
	}

	public String getPortfolioIDs()
	{
		return portfolioIDs;
	}

	public void setPortfolioIDs(String portfolioIDs)
	{
		this.portfolioIDs = portfolioIDs;
	}
	
	public String execute()
	{ 
		if(!portfolioIDs.trim().equals("") ||portfolioIDs.trim() == null)
		{
			try
			{
				message = groupManager.addPortfolioID(portfolioIDs.trim())+"id="+portfolioIDs;
				return Action.MESSAGE;
			} catch (Exception e)
			{
				e.printStackTrace();
				System.out.println("CATCH SUCCESS");
				message = "CATCH SUCCESS"+"id="+portfolioIDs;
				return Action.MESSAGE;
			}
		}else
		{
			System.out.println("ERROR");
			message = "null"+"id=error"+portfolioIDs;
			System.out.println(message);
			return Action.MESSAGE;
		}
	}
}
