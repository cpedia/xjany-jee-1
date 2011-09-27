package com.lti.service.impl;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import com.lti.service.JforumManager;
import com.lti.service.StrategyManager;
import com.lti.service.bo.JforumPost;
import com.lti.service.bo.JforumPostText;
import com.lti.service.bo.JforumTopics;
import com.lti.service.bo.JforumUser;
import com.lti.service.bo.Strategy;
import com.lti.service.bo.Symbol;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.type.PaginationSupport;

public class JforumManagerImpl extends DAOManagerImpl implements JforumManager, Serializable {

	private static final long serialVersionUID = 1L;

	
	
	@Override
	public JforumPostText getPostTextByPostID(int postId) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(JforumPostText.class);
		detachedCriteria.add(Restrictions.eq("ID", postId));
		List<JforumPostText> jpList = findByCriteria(detachedCriteria);
		if (jpList != null && jpList.size() >= 1) {
			return jpList.get(0);
		} else
			return null;
	}

	@Override
	public JforumTopics getTopicByPlanId(int planid) {
		DecimalFormat format = new DecimalFormat("###,###");
		String planPointID = format.format(planid);
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(JforumTopics.class);
		detachedCriteria.add(Restrictions.and(Restrictions.or(Restrictions.eq("PlanId", planid+""), Restrictions.eq("PlanId", planPointID)), Restrictions.eq("ForumId", Integer.valueOf(75))));
		List<JforumTopics> tList = findByCriteria(detachedCriteria);
		if (tList != null && tList.size() >= 1) {
			JforumPostText jpt = this.getPostTextByPostID(tList.get(0).getTopicFirstPostId());
			tList.get(0).setPostText(jpt.getPostText());
			return tList.get(0); 
		} else
			return null;
	}
	
	
	@Override
	public List<JforumPostText> getPostTextsByTopicID(int topicid) {
		DetachedCriteria dc = DetachedCriteria.forClass(JforumPost.class);
		dc.add(Restrictions.eq("TopicID", topicid));
		dc.setProjection(Projections.property("PostID"));
		List<Integer> ids=this.findByCriteria(dc);
		
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(JforumPostText.class);
		detachedCriteria.add(Restrictions.in("ID", ids));
		List<JforumPostText> jpList = findByCriteria(detachedCriteria);
		return jpList;
	}
	
	
	
	@Override
	public JforumTopics getTopicByTopicId(int topicId) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(JforumTopics.class);
		detachedCriteria.add(Restrictions.eq("ID", topicId));
		List<JforumTopics> tList = findByCriteria(detachedCriteria);
		if (tList != null && tList.size() >= 1) {
			JforumPostText jpt = this.getPostTextByPostID(tList.get(0).getTopicFirstPostId());
			tList.get(0).setPostText(jpt.getPostText());
			return tList.get(0);
		} else
			return null;
	}

	@Override
	public PaginationSupport getAllTopicsByForumIdDesc(int forumId, int pageSize, int startIndex, int topicNum) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(JforumTopics.class);
		detachedCriteria.add(Restrictions.eq("ForumId", forumId));
		detachedCriteria.add(Restrictions.eq("NeedModerate", false));
		detachedCriteria.addOrder(Order.desc("ID"));
		PaginationSupport paginationSupport = findPageByCriteria(detachedCriteria, pageSize, startIndex);
		int size = paginationSupport.getItems().size();
		if (topicNum > size) {
			topicNum = size;
		}
		for (int i = 0; i < topicNum; i++) {
			String text = this.getPostTextByPostID(((JforumTopics) paginationSupport.getItems().get(i)).getTopicFirstPostId()).getPostText();
			((JforumTopics) paginationSupport.getItems().get(i)).setPostText(text);
		}
		return paginationSupport;
	}

	@Override
	public PaginationSupport getAllTopicsListByForumIdDesc(Integer[] forumId, int pageSize, int startIndex, int topicNum) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(JforumTopics.class);
		detachedCriteria.add(Restrictions.in("ForumId", forumId));
		detachedCriteria.add(Restrictions.eq("NeedModerate", false));
		detachedCriteria.addOrder(Order.desc("ID"));
		PaginationSupport paginationSupport = findPageByCriteria(detachedCriteria, pageSize, startIndex);
		int size = paginationSupport.getItems().size();
		if (topicNum > size) {
			topicNum = size;
		}
		for (int i = 0; i < topicNum; i++) {
			String text = this.getPostTextByPostID(((JforumTopics) paginationSupport.getItems().get(i)).getTopicFirstPostId()).getPostText();
			((JforumTopics) paginationSupport.getItems().get(i)).setPostText(text);
		}
		return paginationSupport;
	}

	@Override
	public PaginationSupport getAllTopicsBySymbolDesc(String symbol, int pageSize, int startIndex, int topicNum) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Symbol.class);
		String[] symbols = symbol.split(",");
//		detachedCriteria.add(Restrictions.eq("symbol", symbol));
		detachedCriteria.add(Restrictions.in("symbol",symbols));
		detachedCriteria.add(Restrictions.eq("needModerate", false));
		detachedCriteria.addOrder(Order.desc("topicId"));
		PaginationSupport paginationSupport = findPageByCriteria(detachedCriteria, pageSize, startIndex);
		int size = paginationSupport.getItems().size();
		if (topicNum > size) {
			topicNum = size;
		}
		for (int i = 0; i < topicNum; i++) {
			Symbol s = ((Symbol) (paginationSupport.getItems().get(i)));
			int topicId = s.getTopicId();
			int topicReplies = this.getTopicByTopicId(topicId).getTopicReplies();
			String text = this.getPostTextByPostID(this.getTopicByTopicId(topicId).getTopicFirstPostId()).getPostText();
			s.setPostText(text);
			s.setTopicReplies(topicReplies);
		}
		return paginationSupport;
	}

	@Override
	public int getForumId(String forumName) {
		String hql = "select j.ID from JforumForum j where j.ForumName='" + forumName + "'";
		List<Integer> idList = getHibernateTemplate().find(hql);
		if (idList != null && idList.size() >= 1) {
			return idList.get(0);
		} else
			return 0;
	}

	@Override
	public int countRepliesByTopic(int topicId) {
		String hql = "select j.TopicReplies from JforumTopics j where j.ID=" + topicId;
		List<Integer> s = getHibernateTemplate().find(hql);
		if (s != null && s.size() >= 1) {
			return s.get(0);
		} else
			return 0;
	}

	@Override
	public PaginationSupport getTopicByForumIDDesc(int forumId1, int forumId2, int pageSize, int startIndex, int topicNum) {
		// DetachedCriteria detachedCriteria =
		// DetachedCriteria.forClass(JforumTopics.class);
		// detachedCriteria.add(Restrictions.eq("ForumId", forumId1));
		// detachedCriteria.add(Restrictions.eq("ForumId", forumId2));
		//
		// detachedCriteria.add(Restrictions.eq("NeedModerate",false));
		//
		// detachedCriteria.addOrder(Order.desc("ID"));
		//
		// PaginationSupport paginationSupport =
		// findPageByCriteria(detachedCriteria, pageSize, startIndex);

		String hql = "from JforumTopics j where j.NeedModerate=0 And (j.ForumId=" + forumId1 + "or j.ForumId=" + forumId2 + ")order By j.ID Desc";
		PaginationSupport paginationSupport = this.findPageByHQL(hql, pageSize, startIndex);

		int size = paginationSupport.getItems().size();
		if (topicNum > size) {
			topicNum = size;
		}
		for (int i = 0; i < topicNum; i++) {
			String text = this.getPostTextByPostID(((JforumTopics) paginationSupport.getItems().get(i)).getTopicFirstPostId()).getPostText();
			((JforumTopics) paginationSupport.getItems().get(i)).setPostText(text);
		}
		return paginationSupport;
	}

	@Override
	public List<JforumTopics> getTopicsByForumIDDesc(int forumId) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(JforumTopics.class);
		detachedCriteria.add(Restrictions.eq("ForumId", forumId));
		detachedCriteria.add(Restrictions.eq("NeedModerate", false));
		detachedCriteria.addOrder(Order.desc("ID"));
		List<JforumTopics> tList = this.findByCriteria(detachedCriteria);
		if (tList != null && tList.size() >= 1) {
			return tList;
		} else
			return null;
	}

	@Override
	public List<JforumTopics> getTopicsByForumIDDesc(int forumId1, int forumId2) {
		String hql = "from JforumTopics j where j.NeedModerate=0 And (j.ForumId=" + forumId1 + "or j.ForumId=" + forumId2 + ")order By j.ID Desc";
		List<JforumTopics> topicList = this.findByHQL(hql);
		if (topicList != null && topicList.size() >= 1) {
			return topicList;
		} else {
			return null;
		}
	}

	@Override
	public void update(JforumPostText jp) {
		System.out.println(jp.getPostText());
		// String sql =
		// "update j.post_text set values("+jp.getPostText()+") from jforum_posts_text j where j.post_id = "+jp.getID();
		// String sql =
		// "update jforum_posts_text set post_text = \'"+jp.getPostText()+"\' where post_id = 3273";
		getHibernateTemplate().update(jp);

	}

	@SuppressWarnings({ "deprecation" })
	public void addNew(String symbol, Integer Id, String subject) {
		Connection conn = getHibernateTemplate().getSessionFactory().openSession().connection();
		PreparedStatement p = null;
		try {
			p = conn.prepareStatement("insert into ltisystem.jforum_symbols(symbol,topic_id,topic_title,time,need_moderate) values(?,?,?,?,?)");
			p.setString(1, symbol.trim());
			p.setInt(2, Id);
			p.setString(3, subject);
			p.setTimestamp(4, new Timestamp(this.getDate(Id).getTime()));
			p.setInt(5, 0);
			p.executeUpdate();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		try {
			if (p != null)
				p.close();
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			System.out.println(e.toString());
		}
	}

	public int getPostIdbyTopicId(Integer id) {
		List list = new ArrayList();
		try {
			list = this.findBySQL("select post_id from jforum_posts where topic_id=" + id + " order by post_id asc");
		} catch (Exception e) {
			e.printStackTrace();
		}
		// list =
		// getHibernateTemplate().find("select post_id from jforum_posts where topic_id="+id);
		return (Integer) list.get(0);
	}

	public Date getDate(int id) {
		List list = new ArrayList();
		Date date = new Date();
		try {
			list = this.findBySQL("select post_time from ltisystem.jforum_posts where post_id=" + id);
			date = (Date) list.get(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}
	@Override
	public int savePost(int forumId, String subject, String text, long userid, long topicid, long planid, String ip) {
		JforumPost post=new JforumPost();
		post.setTopicID((int)topicid);
		post.setForumID(forumId);
		post.setUserID((int)userid);
		post.setPostTime(new Date());
		post.setPosterIP(ip);
		post.setEnableBBCode(true);
		post.setEnableHtml(false);
		post.setEnableSmilies(true);
		post.setEnableSig(true);
		post.setPostEditTime(new Date());
		post.setPostEditCount(0);
		post.setStatus(true);
		post.setAttach(false);
		post.setNeedModerate(false);
		post.setPlanID(planid+"");
		getHibernateTemplate().save(post);
		
		JforumPostText jpt=new JforumPostText();
		jpt.setID(post.getPostID());
		jpt.setPostSubject(subject);
		jpt.setPostText(text);
		getHibernateTemplate().save(jpt);
		
		return post.getPostID();
	}
	
	@Override
	public JforumTopics getTopic(int id){
		return (JforumTopics) getHibernateTemplate().get(JforumTopics.class, id);
	}
	@Override
	public void updateTopic(JforumTopics jp) {
		 getHibernateTemplate().update(jp);
	}
	@Override
	public int saveTopic(int forumId, String subject, String text, long userid, long planid,String ip) {
		JforumTopics topic=new JforumTopics();
		topic.setForumId(forumId);
		topic.setTopicTitle(subject);
		topic.setUserId((int)userid);
		topic.setTopicTime(new Timestamp(System.currentTimeMillis()));
		topic.setTopicViews(0);
		topic.setTopicReplies(0);
		topic.setTopicStatus(0);
		topic.setTopicVoteId(0);
		topic.setTopicType(0);
		topic.setTopicFirstPostId(0);
		topic.setTopicLastPostId(0);
		topic.setTopicMovedId(0);
		topic.setModerated(0);
		topic.setPlanId(planid+"");
		topic.setNeedModerate(false);
		getHibernateTemplate().save(topic);
		
		return topic.getID();
	}
	@Override
	public void addComment(int forumId,Long planid,String userName,String subject,String text,String ip){
		JforumUser ju = new JforumUser();
		if(userName!=null){
			ju = this.findByName(userName);
		}else{
			ju.setId(0);
		}
		
		JforumTopics t=this.getTopicByPlanId(planid.intValue());
		if(t==null){
			int tid=this.saveTopic(forumId,subject, text, ju.getId(), planid,ip);
			int postid=this.savePost(forumId,subject, text, ju.getId(), tid,planid, ip);
			t=this.getTopic(tid);
			t.setTopicReplies(1);
			t.setTopicFirstPostId(postid);
			t.setTopicLastPostId(postid);
			this.updateTopic(t);
			//System.out.println("http://222.200.180.114/jforum/posts/list/"+tid+".page");
		}else{
			int postid=this.savePost(forumId,subject, text, ju.getId(), t.getID(),planid, ip);
			t.setTopicLastPostId(postid);
			t.setTopicReplies(t.getTopicReplies()+1);
			this.updateTopic(t);
			//System.out.println("http://222.200.180.114/jforum/posts/list/"+t.getID()+".page");
		}
	}
	@Override
	public JforumUser findByName(String str){
		JforumUser ju = new JforumUser();
		List<JforumUser> julist = this.findByHQL("from JforumUser where username = '"+str+"'");
		return julist.get(0);
	}
	
	@SuppressWarnings("unused")
	public static void main(String[] args){
		JforumManager jm=ContextHolder.getJforumManager();
		StrategyManager sm=ContextHolder.getStrategyManager();
		List<JforumPostText> texts=jm.getPostTextsByTopicID(369);
		List<Strategy> plans=sm.getStrategiesByType(Configuration.STRATEGY_TYPE_401K);
		for(int r=0;r<plans.size();r++){
			Strategy plan=plans.get(r);
		}
	}
	
	
	
	
	
	
	
}
