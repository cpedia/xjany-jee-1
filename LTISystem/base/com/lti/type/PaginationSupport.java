package com.lti.type;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PaginationSupport  implements Serializable{

	public final static int PAGESIZE = 30;

	/**
	 * Page Size
	 */
	private int pageSize = PAGESIZE;

	@SuppressWarnings("unchecked")
	private List items;

	private int totalCount;

	/**
	 * the start index of every page
	 */
	private int[] indexes = new int[0];
	
	private int[] indexes10=new int[0];
	
	private List<Integer> pages=new ArrayList<Integer>(15);

	private int startIndex = 0;
	
	/**
	 * Page counts
	 */
	private int totalPage;

	/**
	 * the additional parameters
	 */
	private java.util.HashMap parameters = new java.util.HashMap();
	
	public List<Integer> getPages() {
		return pages;
	}

	public void setPages(List<Integer> pages) {
		this.pages = pages;
	}

	@SuppressWarnings("unchecked")
	public PaginationSupport(List items, int totalCount) {
		setPageSize(PAGESIZE);
		setTotalCount(totalCount);
		setItems(items);
		setStartIndex(0);
		setTotalPage();
	}
	
	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage() {
		int count = totalCount / pageSize;
		if (totalCount % pageSize > 0)
			count++;
		this.totalPage = count;
		//for pages
		buildPages();
	}

	@SuppressWarnings("unchecked")
	public PaginationSupport(List items, int totalCount, int startIndex) {
		setPageSize(PAGESIZE);
		setTotalCount(totalCount);
		setItems(items);
		setStartIndex(startIndex);
		setTotalPage();
	}
	@SuppressWarnings("unchecked")
	public PaginationSupport(List items, int totalCount, int pageSize,
			int startIndex) {
		setPageSize(pageSize);
		setTotalCount(totalCount);
		setItems(items);
		setStartIndex(startIndex);
		setTotalPage();
	}
	@SuppressWarnings("unchecked")
	public List getItems() {
		return items;
	}
	@SuppressWarnings("unchecked")
	public void setItems(List items) {
		this.items = items;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalCount() {
		return totalCount;
	}
	private void buildPages(){
		int currentPage = this.startIndex / pageSize;
		int startPage = currentPage/15*15;
		for(int i=startPage-1;i<=startPage+15;i++){
			if(i>=0&&i<totalPage&&!pages.contains(i)){
				pages.add(i);
			}
		}
		
	}
	public void setTotalCount(int totalCount) {
		if (totalCount > 0) {
			this.totalCount = totalCount;
			int count = totalCount / pageSize;
			if (totalCount % pageSize > 0)
				count++;
			indexes = new int[count];
					
			for (int i = 0; i < count; i++) {
				indexes[i] = pageSize * i;
			}
			
			
			
			//for indexex 10
			if(count<=10){
				this.indexes10=new int[count];
				for(int i=0;i<count;i++){
					indexes10[i]=indexes[i];
				}
			}else {
				this.indexes10=new int[10];
				int currentPage = this.startIndex / pageSize;
				int startPage = currentPage - 4;
				if (startPage < 0)
					startPage = 0;
				int endPage = currentPage + 5;
				if (endPage >= count)
					endPage = count - 1;
				for (int i = 0; i < indexes10.length; i++) {
					indexes10[i] = indexes[startPage];
					startPage++;
					if (startPage > endPage)
						break;
				}
			}
			
		} else {
			this.totalCount = 0;
		}
	}

	public int[] getIndexes() {
		return indexes;
	}

	public void setIndexes(int[] indexes) {
		this.indexes = indexes;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		if (totalCount <= 0)
			this.startIndex = 0;
		else if (startIndex >= totalCount)
			this.startIndex = indexes[indexes.length - 1];
		else if (startIndex < 0)
			this.startIndex = 0;
		else {
			this.startIndex = indexes[startIndex / pageSize];
		}
	}

	public int getNextIndex() {
		int nextIndex = getStartIndex() + pageSize;
		if (nextIndex >= totalCount)
			return getStartIndex();
		else
			return nextIndex;
	}

	public int getPreviousIndex() {
		int previousIndex = getStartIndex() - pageSize;
		if (previousIndex < 0)
			return 0;
		else
			return previousIndex;
	}

	public java.util.HashMap[] getParameters() {
		java.util.HashMap[] maps=new java.util.HashMap[this.indexes.length];
		for (int i = 0; i < maps.length; i++) {
			java.util.HashMap map = new java.util.HashMap();
			Iterator iter = this.parameters.keySet().iterator();
			while (iter.hasNext()) {
				String key = (String) iter.next();
				map.put(key, this.parameters.get(key));
			}
			map.put("startIndex", indexes[i]);
			maps[i]=map;
		}
		return maps;
	}
	public java.util.HashMap getFirstParameter(){
		//first one
		java.util.HashMap map = new java.util.HashMap();
		Iterator iter = this.parameters.keySet().iterator();
		while (iter.hasNext()) {
			String key = (String) iter.next();
			map.put(key, this.parameters.get(key));
		}
		map.put("startIndex", indexes[0]);
		return map;
	}
	public java.util.HashMap getLastParameter(){
		java.util.HashMap map = new java.util.HashMap();
		Iterator iter = this.parameters.keySet().iterator();
		while (iter.hasNext()) {
			String key = (String) iter.next();
			map.put(key, this.parameters.get(key));
		}
		map.put("startIndex", indexes[indexes.length-1]);
		return map;
	}	
	@SuppressWarnings("unchecked")
	public java.util.HashMap[] getParameters10() {
		
		int length=this.indexes.length;
		if(length>10)length=10;
		
		int si=this.startIndex;
		
		si-=this.pageSize*5;
		
		if(si<0)si=0;
		
		java.util.HashMap[] maps=new java.util.HashMap[length];

		for (int i = 0; i < maps.length; i++) {
			java.util.HashMap map = new java.util.HashMap();
			Iterator iter = this.parameters.keySet().iterator();
			while (iter.hasNext()) {
				String key = (String) iter.next();
				map.put(key, this.parameters.get(key));
			}
			
			map.put("startIndex", si+i*this.pageSize);
			maps[i]=map;
		}
		return maps;
	}
	public void setParameters(java.util.HashMap parameters) {
		this.parameters = parameters;
	}

	public void addParameter(Object key,Object value){
		this.parameters.put(key, value);
	}

	public int[] getIndexes10() {
		return indexes10;
	}

	public void setIndexes10(int[] indexes10) {
		this.indexes10 = indexes10;
	}
}
