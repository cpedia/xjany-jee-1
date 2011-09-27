package com.lti.widgets.bean;

public class WidgetBean {
	/**
	 * Widget的名字
	 */
	private String name;
	/**
	 * 页面显示Widget的容器（DIV）的ID
	 */
	private String id;
	/**
	 * 构造Widget的相关参数，以name1=val1,name2=val2的形式
	 * 注意需要编码解码
	 */
	private String params;
	
	
	/**
	 * 是否自动加载widget的初始化函数
	 * 如果为false，可以由其他widget调用
	 */
	private boolean autoloading=true;
	
	
	public boolean isAutoloading() {
		return autoloading;
	}
	public void setAutoloading(boolean autoloading) {
		this.autoloading = autoloading;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getParams() {
		return params;
	}
	public void setParams(String params) {
		this.params = params;
	}
	
	
	
}
