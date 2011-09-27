package com.lti.action.jforum;

import java.util.ArrayList;
import java.util.List;

import com.lti.rmi.PostBean;
import com.lti.rmi.PostService;
import com.lti.rmi.RmiUtil;
import com.lti.service.StrategyManager;
import com.lti.service.bo.Strategy;
import com.lti.system.ContextHolder;




public class PostAction {
	private Long ID;
	private String message;
	private StrategyManager strategyManager;
	private List<PostBean> posts;
	private List<SouthRegionPost> displayPosts;
		
	public List<SouthRegionPost> getDisplayPosts() {
		return displayPosts;
	}

	public void setDisplayPosts(List<SouthRegionPost> displayPosts) {
		this.displayPosts = displayPosts;
	}

	public String execute()
	{
		displayPosts=new ArrayList();
		Long forumId;
		if (ID != null) 
		{			
			Strategy strategy;
			strategy=strategyManager.get(ID);
			forumId=strategy.getForumID();
			if(forumId!=null)
			{
				posts = RmiUtil.getPost(Integer.parseInt(forumId.toString()));
				for(int i=0;i<posts.size();i++)
				{	
					
					SouthRegionPost p= new SouthRegionPost();
					p.setText(posts.get(i).getText());
					p.setUser(posts.get(i).getUserName());
					p.setTime(posts.get(i).getTime().toLocaleString());
					p.setTopic(posts.get(i).getTopicTitle());
					displayPosts.add(p);
				}
			}
			else
			{
				return "false";
			}
			
		}
		else 
		{
			return "false";
		}
		
		return "success";
	}

	public Long getID() {
		return ID;
	}

	public void setID(Long id) {
		ID = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public StrategyManager getStrategyManager() {
		return strategyManager;
	}

	public void setStrategyManager(StrategyManager strategyManager) {
		this.strategyManager = strategyManager;
	}

	public List<PostBean> getPosts() {
		return posts;
	}

	public void setPosts(List<PostBean> posts) {
		this.posts = posts;
	}

	

	

	
}
