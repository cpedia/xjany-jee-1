package com.lti.rmi;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.lti.system.ContextHolder;

public class RmiUtil {
	public static Long newTopic(TopicBean t)
	{
		
		TopicService ts=(TopicService)ContextHolder.getInstance().getApplicationContext().getBean("topicProxy");
				
		Long topicID=new Long(ts.newTopic(t));
		
		System.out.print("topicId: "+topicID);
		
		return topicID;
	}
	
	public static List<PostBean> getPost(int forumID)
	{
		PostService ps=(PostService)ContextHolder.getInstance().getApplicationContext().getBean("postProxy");
		
		return ps.getPost(forumID);
	}
	
	public static Long newUser(UserBean u)
	{
		UserService us=(UserService)ContextHolder.getInstance().getApplicationContext().getBean("userProxy");
		Long userID=new Long(us.newUser(u));
		System.out.print("userID: "+userID);
		return userID;
	}
	
	public static Long newForum(ForumBean f)
	{
		ForumService fs=(ForumService)ContextHolder.getInstance().getApplicationContext().getBean("forumProxy");
		Long forumID=new Long(fs.newForum(f));
		System.out.print("forumID: "+forumID);
		return forumID;
	}
	
	public static int newCategory(CategoryBean c)
	{
		CategoryService cs=(CategoryService)ContextHolder.getInstance().getApplicationContext().getBean("categoryProxy");
		int categoryID=cs.newCategory(c);
		System.out.println("categoryID: "+categoryID);
		return categoryID;
	}
	
	public static void modifyTopicContent(int topicId,String content)
	{
		PostService ps=(PostService)ContextHolder.getInstance().getApplicationContext().getBean("postProxy");
		ps.modifyTopicContent(topicId, content);
		return;
	}
	
	public static int getCategoryID(String strategyClassName)
	{
		if(strategyClassName.equals("ASSET ALLOCATION STRATEGY")) return 1;//ASSET ALLOCATION STRATEGY
		else if(strategyClassName.equals("REBALANCING STRATEGY")) return 5;//REBALANCING STRATEGY
		else if(strategyClassName.equals("CASH FLOW STRATEGY")) return 6;//CASH FLOW STRATEGY
		else if(strategyClassName.equals("EQUITY")) return 7;//EQUITY
		else if(strategyClassName.equals("FIXED INCOME")) return 8;//FIXED INCOME
		else if(strategyClassName.equals("CASH")) return 9;//CASH
		else if(strategyClassName.equals("HYBRID ASSETS")) return 10;//HYBRID ASSETS
		else if(strategyClassName.equals("REAL ESTATE")) return 11;//REAL ESTATE
		else if(strategyClassName.equals("COMMODITIES")) return 12;//COMMODITIES
		else if(strategyClassName.equals("HEDGES")) return 13;//HEDGES
		else return 14;//Others
	}
	public static List<PostBean> getPostsByTopicID(int topicID){
		List<PostBean> postList = null;
		PostService ps=(PostService)ContextHolder.getInstance().getApplicationContext().getBean("postProxy");
		postList = ps.getPostsByTopicID(topicID);
		return postList;
	}
	public static int getTopicIDByName(int forumID,String topicTitle) {
		TopicService ps=(TopicService)ContextHolder.getInstance().getApplicationContext().getBean("topicProxy");
		int topicID = ps.getTopicID(forumID, topicTitle);
		return topicID;
	}
	public static long getForumIDByName(int categoryID,String forumName) {
		ForumService fs=(ForumService)ContextHolder.getInstance().getApplicationContext().getBean("forumProxy");
		long forumID = fs.getForumID(categoryID,forumName); 
		return forumID;
	}
	public static int getCategoryIDByName(String categoryName){
		CategoryService cs = (CategoryService)ContextHolder.getInstance().getApplicationContext().getBean("categoryProxy");
		int categoryID = cs.getCategoryID(categoryName);
		return categoryID;
	}
}
