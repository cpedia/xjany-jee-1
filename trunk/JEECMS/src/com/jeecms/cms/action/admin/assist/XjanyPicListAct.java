package com.jeecms.cms.action.admin.assist;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.cms.entity.assist.XjanyPicList;
import com.jeecms.cms.entity.main.CmsSite;
import com.jeecms.cms.manager.assist.XjanyPicListMng;
import com.jeecms.cms.manager.main.CmsLogMng;
import com.jeecms.cms.web.CmsUtils;
import com.jeecms.cms.web.WebErrors;


/*
 * author Blair
 * date 2011/09/17
 */
@Controller
public class XjanyPicListAct {
	private static final Logger log = LoggerFactory
			.getLogger(XjanyPicListAct.class);

	@RequestMapping("/xjanypic/v_list.do")
	public String list(Integer queryCtgId, HttpServletRequest request,
			ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		List<XjanyPicList> list = manager.getList();
		model.addAttribute("list", list);
		if (queryCtgId != null) {
			model.addAttribute("queryCtgId", queryCtgId);
		}
		return "xjanypic/list";
	}

	@RequestMapping("/xjanypic/v_add.do")
	public String add(ModelMap model, HttpServletRequest request) {
		return "xjanypic/add";
	}

	@RequestMapping("/xjanypic/v_edit.do")
	public String edit(Integer id, Integer queryCtgId,
			HttpServletRequest request, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		WebErrors errors = validateEdit(id, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		model.addAttribute("XjanyPicList", manager.findById(id));
		
	
		return "xjanypic/edit";
	}

	@RequestMapping("/xjanypic/o_save.do")
	public String save(XjanyPicList bean, Integer ctgId,
			HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateSave(bean, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		bean = manager.save(bean);
		log.info("save XjanyPicList id={}", bean.getId());
		cmsLogMng.operating(request, "XjanyPicList.log.save", "id="
				+ bean.getId() + ";name=" + bean.getName());
		return "redirect:v_list.do";
	}

	@RequestMapping("/xjanypic/o_update.do")
	public String update(XjanyPicList bean, Integer ctgId, Integer queryCtgId,
			HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateUpdate(bean.getId(), request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		bean = manager.update(bean);
		log.info("update XjanyPicList id={}.", bean.getId());
		cmsLogMng.operating(request, "XjanyPicList.log.update", "id="
				+ bean.getId() + ";name=" + bean.getName());
		return list(queryCtgId, request, model);
	}

	@RequestMapping("/xjanypic/o_delete.do")
	public String delete(Integer[] ids, Integer queryCtgId,
			HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateDelete(ids, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		XjanyPicList[] beans = manager.deleteByIds(ids);
		for (XjanyPicList bean : beans) {
			log.info("delete XjanyPicList id={}", bean.getId());
			cmsLogMng.operating(request, "XjanyPicList.log.delete", "id="
					+ bean.getId() + ";name=" + bean.getName());
		}
		return list(queryCtgId, request, model);
	}



	private WebErrors validateSave(XjanyPicList bean,
			HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		CmsSite site = CmsUtils.getSite(request);
		return errors;
	}

	private WebErrors validateEdit(Integer id, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		CmsSite site = CmsUtils.getSite(request);
		if (vldExist(id, site.getId(), errors)) {
			return errors;
		}
		return errors;
	}

	private WebErrors validateUpdate(Integer id, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		CmsSite site = CmsUtils.getSite(request);
		if (vldExist(id, site.getId(), errors)) {
			return errors;
		}
		return errors;
	}

	private WebErrors validatePriority(Integer[] ids, Integer[] priorities,
			HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		CmsSite site = CmsUtils.getSite(request);
		return errors;
	}

	private WebErrors validateDelete(Integer[] ids, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		CmsSite site = CmsUtils.getSite(request);
		if (errors.ifEmpty(ids, "ids")) {
			return errors;
		}
		for (Integer id : ids) {
			vldExist(id, site.getId(), errors);
		}
		return errors;
	}

	private boolean vldExist(Integer id, Integer siteId, WebErrors errors) {
		if (errors.ifNull(id, "id")) {
			return true;
		}
		XjanyPicList entity = manager.findById(id);
		if (errors.ifNotExist(entity, XjanyPicList.class, id)) {
			return true;
		}
		return false;
	}

	@Autowired
	private CmsLogMng cmsLogMng;
	@Autowired
	private XjanyPicListMng manager;
}