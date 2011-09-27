package com.lti.service;

import java.util.List;
import com.lti.service.bo.JforumPostText;
import com.lti.service.bo.JforumTopics;
import com.lti.service.bo.JforumUser;
import com.lti.service.bo.Symbol;
import com.lti.system.Configuration;
import com.lti.type.PaginationSupport;

public interface JforumManager {
	
	public JforumPostText getPostTextByPostID(int postId);
	
	public JforumTopics getTopicByTopicId(int postId);
	
	PaginationSupport getAllTopicsByForumIdDesc(int forumId,int pageSize,int startIndex,int topicNum);
	
	PaginationSupport getAllTopicsListByForumIdDesc(Integer[] forumId,int pageSize,int startIndex,int topicNum);
	
	PaginationSupport getAllTopicsBySymbolDesc(String symbol,int pageSize, int startIndex,int topicNum);
	
	public int getForumId(String forumName);
	
	public int countRepliesByTopic(int topicId);
	
	public PaginationSupport getTopicByForumIDDesc(int forumId1,int forumId2,int pageSize, int startIndex,int topicNum);
	
	public List<JforumTopics> getTopicsByForumIDDesc(int forumId);
	
	public List<JforumTopics> getTopicsByForumIDDesc(int forumId1,int forumId2);
	
	public void update(JforumPostText jp);
	
	public int getPostIdbyTopicId(Integer id);
	
	public void addNew(String symbol,Integer Id,String subject);

	JforumTopics getTopicByPlanId(int planid);

	/**
	 * 返回某topic下的所有post的内容
	 * @param topicid
	 * @return
	 */
	List<JforumPostText> getPostTextsByTopicID(int topicid);

	JforumTopics getTopic(int id);

	void updateTopic(JforumTopics jp);



	int saveTopic(int forumId, String subject, String text, long userid, long planid, String ip);

	int savePost(int forumId, String subject, String text, long userid, long topicid, long planid, String ip);


	void addComment(int forumId, Long planid, String userName, String subject, String text, String ip);

	JforumUser findByName(String str);
	
}
