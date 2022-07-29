package com.my.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.my.dto.Board;
import com.my.dto.PageBean;
import com.my.dto.ResultBean;
import com.my.exception.FindException;
import com.my.service.BoardService;

@Controller
public class BoardController {
	
	@Autowired
	private BoardService service;
	
	@GetMapping("boardlist")
	@ResponseBody
	public ResultBean<PageBean<Board>> list(@RequestParam(required = false, defaultValue = "0") int currentPage) { // default값이 문자열 0으로 설정하여 int로 자동형변환 되게끔한다.
		ResultBean<PageBean<Board>> rb = new ResultBean<>();
		try {
			PageBean<Board> pb = service.boardList(currentPage);
			rb.setStatus(1);
			rb.setT(pb);
		} catch (FindException e) {
			e.printStackTrace();
			rb.setStatus(0);
			rb.setMsg(e.getMessage());
		}
		return rb;
	}
	
	@GetMapping("search")
	@ResponseBody
	public ResultBean<PageBean<Board>> search(@RequestParam(required = false, defaultValue = "0") int currentPage, @RequestParam(required = false, defaultValue = "")String word) {
		
		ResultBean<PageBean<Board>> rb = new ResultBean<>();
		try {
			PageBean<Board> pb;
			if("".equals(word)) {
				pb = service.boardList(currentPage); // 문자열이 들어오지 않을경우 모든 값을 반환하게
			}else {
				pb = service.searchBoard(word, currentPage); // 검색어가 들어올경우 해당되는 값만 반환하게
			}
			rb.setStatus(1);
			rb.setT(pb);
		} catch (FindException e) {
			e.printStackTrace();
			rb.setStatus(0);
			rb.setMsg(e.getMessage());
		}
		return rb;
	}
	
	@GetMapping("viewboard")
	@ResponseBody
	public ResultBean<Board> viewBoard(int boardNo) {
		ResultBean<Board> rb = new ResultBean<>();
		try {
			Board b = service.viewBoard(boardNo);
			rb.setStatus(1);
			rb.setT(b);
		} catch (FindException e) {
			e.printStackTrace();
			rb.setStatus(0);
			rb.setMsg(e.getMessage());
		}
		return rb;
	}
}
