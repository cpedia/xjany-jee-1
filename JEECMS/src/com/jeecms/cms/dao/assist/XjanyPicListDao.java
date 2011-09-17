package com.jeecms.cms.dao.assist;

import java.util.List;


import com.jeecms.cms.entity.assist.XjanyPicList;
import com.jeecms.common.hibernate3.Updater;

public interface XjanyPicListDao {
	public List<XjanyPicList> getList();


	public XjanyPicList findById(Integer id);

	public XjanyPicList save(XjanyPicList bean);

	public XjanyPicList updateByUpdater(Updater<XjanyPicList> updater);

	public XjanyPicList deleteById(Integer id);
}