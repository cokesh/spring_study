package com.my.control;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.my.dto.Product;

@Controller
public class ParameterTest {
	@GetMapping("a")
	public void a() {
		System.out.println("메서드 호출");
	}
	
	@GetMapping("b")
	public void b(HttpServletRequest request) {
		System.out.println("b메서드 호출");
		System.out.println(request.getParameter("no"));
	}
	
	@GetMapping("c")
	public void c(HttpServletResponse response) throws IOException {
		response.sendRedirect("http://www.naver.com");
	}
	
	@GetMapping("d")
	public void d(HttpSession session) {
		System.out.println("세션 새로 생성여부 : " + session.isNew()); 
	}
	
	@GetMapping("e")
	public void e(String prodNo, String prodName, int prodPrice) {
		System.out.println("prodNo = " + prodNo);
		System.out.println("prodName = " + prodName);
		System.out.println("prodPrice = " + prodPrice);
	}
	@GetMapping("f")
	public void f(@RequestParam(name="prod_no") String prodNo,
				@RequestParam(name="prod_name", required=false, defaultValue="상품이름") String prodName,
				@RequestParam(name="prod_price", required=false, defaultValue="0") int prodPrice) {
		System.out.println("prodNo = " + prodNo);
		System.out.println("prodName = " + prodName);
		System.out.println("prodPrice = " + prodPrice);
	}
	
	@GetMapping("g")
	public void g(Product p) {
		System.out.println("prodNo = " + p.getProdNo());
		System.out.println("prodName = " + p.getProdName());
		System.out.println("prodPrice = " + p.getProdPrice());
	}
	
	@GetMapping("h")
	public void h(String[] arr) {
		for(String str: arr) {
			System.out.println(str);
		}
	}
	
	@PostMapping("i")
	public void i(@RequestBody List<Product> list) {
		for(Product p: list) {
			System.out.println(p);
		}
	}
//	public void i(String[] prodNo, String[] prodName) {
//		for(String no : prodNo) {
//			System.out.println(no);
//		}
//		for(String no : prodName) {
//			System.out.println(no);
//		}
//	}
	
	
}