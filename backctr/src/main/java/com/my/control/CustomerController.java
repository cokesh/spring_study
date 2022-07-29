package com.my.control;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.dto.Customer;
import com.my.exception.AddException;
import com.my.exception.FindException;
import com.my.service.CustomerService;

@Controller
public class CustomerController {
	@Autowired
	private CustomerService service;
	
	@PostMapping("/login")
	@ResponseBody
	public Map login(String id, String pwd, HttpSession session) throws IOException {
		Map<String, Object>map = new HashMap<>();
		map.put("status",0);
		session.removeAttribute("loginInfo");

		try {
			Customer c = service.login(id, pwd);
			map.put("status", 1);
			session.setAttribute("loginInfo",id);
			// 로그인 된 아이디의 값뿐만아니라 모든 정보를 관리하고 싶다면 setAttribute("loginInfo", c); 객체를 set해주면 된다.
		} catch (FindException e) {
			e.printStackTrace();
			map.put("msg", e.getMessage());
		}
		return map;
	}
	
	@PostMapping("signup")
	@ResponseBody
	public Map signup(Customer c) {
		Map<String, Object> map = new HashMap<>();
		map.put("status", 0);
		map.put("msg", "회원가입 실패");

		try {
			service.signup(c);
			map.put("status", 1);
			map.put("msg", "회원가입 성공");
		} catch (AddException e) {
			e.printStackTrace();
			map.put("msg", e.getMessage());
		}
		return map;
	}
	
	@PostMapping("iddupchk")
	@ResponseBody
	public Map iddupchk(String id) {
		Map<String, Object> map = new HashMap<>();
		map.put("msg", "중복된 아이디입니다.");
		map.put("status", 0);
		try {
			service.iddupchk(id);
		} catch (FindException e) {
			map.put("status", 1);
			map.put("msg", "사용할 수 있는 아이디입니다.");
			e.printStackTrace();
		}
		return map;
	}
	
	@PostMapping("loginstatus")
	@ResponseBody
	public Map loginstatus(HttpSession session) {
		Map<String, Object> map = new HashMap<>();
		if(session.getAttribute("loginInfo") == null) {
			map.put("status", 0);
		} else {
			map.put("status", 1);
		}
		return map;
	}
	
	@PostMapping("logout")
	@ResponseBody
	public Map logout(HttpSession session) {
		Map<String, Object> map = new HashMap<>();
		session.removeAttribute("loginInfo");
		map.put("status", 0);
		map.put("msg", "로그아웃되었습니다.");
		return map;
	}
}
