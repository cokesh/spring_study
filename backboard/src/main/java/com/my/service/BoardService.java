package com.my.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.my.dto.Board;
import com.my.dto.PageBean;
import com.my.exception.AddException;
import com.my.exception.FindException;
import com.my.exception.ModifyException;
import com.my.exception.RemoveException;
import com.my.repository.BoardRepository;

@Service
public class BoardService {
	private static final int CNT_PER_PAGE = 3;
	@Autowired
	private BoardRepository repository;
	/**
	 * 페이지별 게시글 목록과 페이지그룹정보를 반환한다.
	 * @param currentPage 검색할 페이지
	 * @return
	 * @throws FindException
	 */
	public PageBean<Board> boardList(int currentPage) throws FindException{
		
		List<Board> list = repository.selectByPage(currentPage, CNT_PER_PAGE);
		int totalCnt = repository.selectCount(); // 총 행수
		int totalPage = (int) Math.ceil((double)totalCnt / CNT_PER_PAGE);// 실수 를 정수로 나누거나 반대로 하거나 해서 값이 실수로 반환하게 하여올림을 진행해야 함
		
		int cntPerPageGroup = 2;
		int startPage = 0;
		//currentPage가 cntPerPage+2 가 될때마다 startPage 는 currentPage가 되고 endPage는 +1 이 된다 만약 currentPage가 2의 배수라면 endPage는 currentPage가 된다.;
		int endPage = 0;
		
		if ( currentPage < totalPage ) { // 만약 currentPage가 totalPage 보다 작은경우
			if( currentPage == 1 || currentPage % 2 == 1 ) { // currentPage가 1일 경우이거나 2로 나눴을 때의 나머지가 1일 경우
				startPage = currentPage; // startPage가 currentPage그대로 감
				endPage = startPage + 1;	 // endPage는 startPage의 +1 이 된 값이 들어감.
			} else {
				startPage = currentPage-1;
				endPage = startPage + 1;
			}
		} else {
			startPage = totalPage;
			endPage = startPage;
		}
		System.out.println("현재페이지는 : " + currentPage);
		System.out.println("시작페이지는 : " + startPage);
		System.out.println("끝페이지는 : " + endPage);
		System.out.println("---------------------");
//		PageBean<Board> pb = new PageBean<>(list, currentPage, totalPage, startPage, endPage, cntPerPageGroup); // set을 대신하여 생성자를 만들어 객체를 생성함.
		PageBean<Board> pb = new PageBean<>();
		pb.setList(list);
		pb.setCurrentPage(currentPage);
		pb.setTotalPage(totalPage);
		pb.setStartPage(startPage);
		pb.setEndPage(endPage);
		pb.setCntPerPageGroup(cntPerPageGroup);
		return pb;
		
	}
	
	/**
	 * 검색어를 이용한 게시글 검색 목록과 페이지 그룹정보를 반환한다.
	 * @param word 검색어
	 * @param currentPage 현재 페이지
	 * @return
	 * @throws FindException
	 */
	public PageBean<Board> searchBoard(String word, int currentPage) throws FindException{
		List<Board> list = repository.selectByWord(word, currentPage, CNT_PER_PAGE);
		
		int totalCnt = repository.selectCount(word);
		int cntPerPageGroup = 2;
		PageBean<Board> pb1 = new PageBean<>(list, totalCnt, currentPage, cntPerPageGroup, CNT_PER_PAGE);
		return pb1;
		
		
	}
	
	/**
	 * 게시글번호의 조회수를 1증가한다.
	 * 게시글번호의 게시글을 반환한다.
	 * @param boardNo
	 * @return
	 * @throws FindException
	 */
	public Board viewBoard(int boardNo) throws FindException {
		try {
			// 조회수를 1증가한다.
			Board b = new Board();
			b.setBoardNo(boardNo);
			b.setBoardViewcount(-1);
			repository.update(b);
			// 게시글번호의 게시글을 조회한다.
			Board b1 = repository.selectByBoardNo(boardNo);
			return b1;
		} catch (ModifyException e) {
			e.printStackTrace();
			throw new FindException(e.getMessage());
		}
		
	}
	
	/**
	 * 글쓰기
	 * @param board
	 * @throws AddException
	 */
	public void writeBoard(Board board) throws AddException {
		board.setBoardParentNo(0);
		repository.insert(board);
	}
	
	/**
	 * 답글쓰기
	 * @param board
	 * @throws AddException
	 */
	public void replyBoard(Board board) throws AddException{
		if(board.getBoardParentNo() == 0) {
			throw new AddException("답글쓰기의 부모글번호가 없습니다");
		}
		repository.insert(board);
	}

	/**
	 * 게시글 지우기
	 * @param boardNo
	 * @throws FindException
	 */
	public void deleteBoard(int boardNo) throws FindException {
		try {
			repository.delete(boardNo);
		} catch (RemoveException e) {
			e.printStackTrace();
			throw new FindException();
		}
	}
	
	public void updateBoard(Board board) throws ModifyException {
		repository.update(board);
		
	}
}
