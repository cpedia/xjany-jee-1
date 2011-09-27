package com.lti.rmi;

import java.io.Serializable;
import java.util.Date;

public class TopicBean implements Serializable{
	
	private int forum_id;
	private String topic_title;
	private int user_id;
	private Date topic_time;
	private int topic_type;
	private int topic_first_post_id;
	private int topic_last_post_id;
	private boolean moderated;
	private String topic_content;

	public String getTopic_content() {
		return topic_content;
	}
	public void setTopic_content(String topic_content) {
		this.topic_content = topic_content;
	}
	public boolean isModerated() {
		return moderated;
	}
	public void setModerated(boolean moderated) {
		this.moderated = moderated;
	}
	public int getForum_id() {
		return forum_id;
	}
	public void setForum_id(int forum_id) {
		this.forum_id = forum_id;
	}
	public String getTopic_title() {
		return topic_title;
	}
	public void setTopic_title(String topic_title) {
		this.topic_title = topic_title;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public Date getTopic_time() {
		return topic_time;
	}
	public void setTopic_time(Date topic_time) {
		this.topic_time = topic_time;
	}
	
	public int getTopic_type() {
		return topic_type;
	}
	public void setTopic_type(int topic_type) {
		this.topic_type = topic_type;
	}
	public int getTopic_first_post_id() {
		return topic_first_post_id;
	}
	public void setTopic_first_post_id(int topic_first_post_id) {
		this.topic_first_post_id = topic_first_post_id;
	}
	public int getTopic_last_post_id() {
		return topic_last_post_id;
	}
	public void setTopic_last_post_id(int topic_last_post_id) {
		this.topic_last_post_id = topic_last_post_id;
	}
	
}
