package com.my.control;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ReturnTest {
	@GetMapping("a1")
	public ModelAndView a() {
		ModelAndView mnv = new ModelAndView();
		mnv.addObject("greeting", "hell yeah a");
		mnv.setViewName("/WEB-INF/jsp/a.jsp");
		return mnv;
	}
	
	@GetMapping("b1")
	public String b(Model model) {
		model.addAttribute("greeting", "안녕하세요");
		return "/WEB-INF/jsp/a.jsp";
//		return "/a1";
	}
	
	@GetMapping("c1")
	public void c() {
	}
	
	@GetMapping(value="d1", produces="text/plain;charset=UTF-8")
	@ResponseBody
//	produces="application/json;charset=UTF-8"
	public String d() {
		String responseData = "응답내용입니다.";
		return responseData;
	}
}

