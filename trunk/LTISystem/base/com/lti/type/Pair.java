package com.lti.type;

import java.io.Serializable;

public class Pair implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String Pre;
	public String Post;
	public Pair(){
		this.Pre=new String();
		this.Post=new String();
	}
	public Pair(String condition,String action){
		this.Pre=condition;
		this.Post=action;
	}
	public boolean equals(Pair p){
		if(this.Pre.equals(p.Pre)&&this.Post.equals(p.Post))
			return true;
		else return false;
	}
	public String getPre() {
		return Pre;
	}
	public void setPre(String pre) {
		Pre = pre;
	}
	public String getPost() {
		return Post;
	}
	public void setPost(String post) {
		Post = post;
	}
}
