package com.lti.rmi;

public interface TopicService {
	public int newTopic(TopicBean topicBean);
	public int getTopicID(int forumID,String topicTitle);
}
