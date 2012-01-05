package com.xjany.common.springmvc;

import java.util.List;

public class PageModel
{

	/**
	 * 分页组件
	 * 
	 * @author Administrator
	 * 
	 */
	// 总记录数
	private int totalRecords;

	// 结果集
	private List list;

	// 当前页
	private int pageNo;

	// 每页显示多少条
	private int pageSize;

	public int getTotalRecords()
	{
		return totalRecords;
	}

	/**
	 * 取得总页数
	 * 
	 * @return
	 */
	public int getTotalPages()
	{
		return (totalRecords + pageSize - 1) / pageSize;
	}

	public void setTotalRecords(int totalRecords)
	{
		this.totalRecords = totalRecords;
	}

	public List getList()
	{
		return list;
	}

	public void setList(List list)
	{
		this.list = list;
	}

	public int getPageSize()
	{
		return pageSize;
	}

	public void setPageSize(int pageSize)
	{
		this.pageSize = pageSize;
	}

	public int getPageNo()
	{
		return pageNo;
	}

	public void setPageNo(int pageNo)
	{
		this.pageNo = pageNo;
	}

	/**
	 * 取得第一页
	 * 
	 * @return
	 */
	public int getTopPageNo()
	{
		return 1;
	}

	/**
	 * 取得上一页
	 * 
	 * @return
	 */
	public int getPreviousPageNo()
	{
		if (pageNo <= 1)
		{
			return 1;
		}
		return pageNo - 1;
	}

	/**
	 * 取得下一页
	 * 
	 * @return
	 */
	public int getNextPageNo()
	{
		if (pageNo >= getTotalPages())
		{
			return getTotalPages() == 0 ? 1 : getTotalPages();
		}
		return pageNo + 1;
	}

	/**
	 * 取得最后一页
	 * 
	 * @return
	 */
	public int getBottomPageNo()
	{
		return getTotalPages() == 0 ? 1 : getTotalPages();
	}

}
