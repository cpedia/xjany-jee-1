package com.jeecms.cms.manager.assist;

import java.util.List;

import com.jeecms.cms.entity.assist.XjanyPicList;

public interface XjanyPicListMng {
	public List<XjanyPicList> getList();


	public XjanyPicList findById(Integer id);

	public int updateViews(Integer id);

	public XjanyPicList save(XjanyPicList bean);

	public XjanyPicList update(XjanyPicList bean);

	public XjanyPicList deleteById(Integer id);

	public XjanyPicList[] deleteByIds(Integer[] ids);
}