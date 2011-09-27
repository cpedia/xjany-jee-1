package com.lti.rmi;

public interface ForumService {
	public int newForum(ForumBean forumBean);
	
	public boolean deleteForum(int forumId);
	
	public boolean updateForum(ForumBean forumBean);
	
	public long getForumID(int categoryID,String forumName);

}
