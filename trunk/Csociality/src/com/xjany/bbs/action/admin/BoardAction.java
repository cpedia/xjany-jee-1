package com.xjany.bbs.action.admin;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xjany.bbs.entity.BbsBoard;
import com.xjany.bbs.service.BoardService;
import com.xjany.common.page.Pagination;
import com.xjany.common.page.SimplePage;
import com.xjany.common.web.CookieUtils;

@Controller
public class BoardAction {

	@Autowired
	private BoardService boardService;

	@RequestMapping("/board/v_list.do")
	public String v_list(Integer pageNo, HttpServletRequest request, ModelMap model) {
		Pagination pagination = boardService.getPage(SimplePage.cpn(pageNo), CookieUtils.getPageSize(request));
		model.addAttribute("pagination", pagination);
		return "board/list";
	}

	@RequestMapping("/board/v_add.do")
	public String v_add(HttpServletRequest request, ModelMap model) {
		return "board/add";
	}

	@RequestMapping("/board/v_edit.do")
	public String v_edit(Integer id,HttpServletRequest request, ModelMap model) {
		BbsBoard bbsBoard = boardService.findById(id);
		model.addAttribute("bbsBoard",bbsBoard);
		return "board/edit";
	}
	
	@RequestMapping("/board/o_save.do")
	public String o_save(BbsBoard bbsBoard, HttpServletRequest request, ModelMap model) {
		boardService.save(bbsBoard);
		return "redirect:v_list.do";
	}

	@RequestMapping("/board/o_update.do")
	public String o_update(BbsBoard bbsBoard,HttpServletRequest request, ModelMap model) {
		boardService.update(bbsBoard);
		return "redirect:v_list.do";
	}
	
	@RequestMapping("/board/o_delete.do")
	public String o_delete(Integer id,HttpServletRequest request, ModelMap model) {
		boardService.deleteById(id);
		return "redirect:v_list.do";
	}
}
