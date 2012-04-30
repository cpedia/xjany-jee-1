package com.xjany.xijie.action;


import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class WizardAction {

	@RequestMapping("/wizard.do")
	public String showIndex(HttpServletRequest request, ModelMap model)
			throws Exception {
		return "createweb/wizard";
	}
	@RequestMapping("/wizardtwo.do")
	public String wizardtwo(HttpServletRequest request, ModelMap model)
			throws Exception {
		return "createweb/wizardtwo";
	}
}
