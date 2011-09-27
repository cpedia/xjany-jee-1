package com.lti.action.admin.filebrowser;

import java.util.List;

public class FileList {

	private String text;

	private boolean leaf;

	private String cls;

	private String href;

	private List<FileList> children;

	private boolean expanded;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isLeaf() {
		return leaf;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	public String getCls() {
		return cls;
	}

	public void setCls(String cls) {
		this.cls = cls;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public List<FileList> getChildren() {
		return children;
	}

	public void setChildren(List<FileList> children) {
		this.children = children;
	}

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}
	
}
