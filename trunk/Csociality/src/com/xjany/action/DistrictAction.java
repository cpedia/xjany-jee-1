package com.xjany.action;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import com.xjany.entity.District;
import com.xjany.service.DistrictService;

@Controller

public class DistrictAction {

	@Autowired
	private DistrictService districtService;
	@RequestMapping("district.do")
	public String getList(HttpServletRequest request, ModelMap model,
			District district) throws Exception {

		List<District> list = districtService.listDistrict();

		model.addAttribute("studentlist", list);// request保存这个对象
		return "student";
	}
	
	@RequestMapping("districtAdd.do")
	 public String add(HttpServletRequest request,ModelMap model,District district) throws Exception{

		 districtService.addDistrict(district);
	  List<District> list = districtService.listDistrict();
	  
	  model.addAttribute("studentlist", list);//request保存这个对象
	  return "student";
	 }

}
