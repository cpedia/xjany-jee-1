package com.lti.rmi;

import java.util.ArrayList;
import java.util.List;

import com.lti.rmi.PostBean;

public interface PostService {
	public List<PostBean> getPost(int forumId);
	public void modifyTopicContent(int topicId,String content);
	public List<PostBean> getPostsByTopicID(int topicId);
}
