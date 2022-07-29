package com.my.control;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.JsonPathResultMatchers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.my.exception.FindException;

@RunWith(SpringRunner.class)
@ContextConfiguration(locations= {"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml"})

@WebAppConfiguration
public class BoardControllerTest {
	
	@Autowired
	private WebApplicationContext context;
	
	private MockMvc mockMvc; // 컨트롤러 테스트용 모의객체
	
	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}
	
	@Test
	public void testList() throws Exception {
		//게시글 페이지별 목록 테스트
		String uri = "/boardlist";
		MockHttpServletRequestBuilder mockRequestBuilder = MockMvcRequestBuilders.get(uri).accept(org.springframework.http.MediaType.APPLICATION_JSON);// 응답형태는 스트링타입이 올수가 있음 application/json으로 쓸수도 있지만 오타가 날 경우 에러가 날것이기 때문에 우리는 오류를 범하는 확률을 줄이기 위해 상수를 쓴다.
		mockRequestBuilder.param("currentPage", "1"); // 요청전달데이터는 무조건 String 타입으로 보내준다.
		//응답정보: ResultAction
		ResultActions resultActions = mockMvc.perform(mockRequestBuilder);//요청
		resultActions.andExpect(MockMvcResultMatchers.status().isOk()); //응답상태코드200번 성공 예상
		resultActions.andExpect(MockMvcResultMatchers.jsonPath("status", is(1)));
		
		org.hamcrest.Matcher<Integer> matcher;
		JsonPathResultMatchers jsonPathMatcher; 
		ResultMatcher resultMatcher;
		jsonPathMatcher = jsonPath("status");
		resultMatcher = jsonPathMatcher.exists(); //status값을 확인함으로써 status값의 존재의 유무를 확인함
		resultActions.andExpect(resultMatcher);
		
		int expectedStatus = 1;
		matcher = org.hamcrest.CoreMatchers.is(expectedStatus);		
		resultMatcher = jsonPath("status", matcher); // status의 값이 1임을 예측하는 테스트이다.
		resultActions.andExpect(resultMatcher);
		
		//json객체의 "t"프로퍼티의 자료형은PageBean이고 PageBean의 "list"프로퍼티 자료형은 List이다
		jsonPathMatcher = jsonPath("t.list");
		resultMatcher = jsonPathMatcher.exists();
		resultActions.andExpect(resultMatcher);
		
		//게시글목록의 크기 t.list.length()
		int expectedListSize = 3;
		matcher = org.hamcrest.CoreMatchers.is(expectedListSize);		
		resultMatcher = jsonPath("t.list.length()", matcher);		
		resultActions.andExpect(resultMatcher);
		
		//게시글목록의 첫번째요소 t.list[0].boardNo
		int expectedBoardNo = 3;
		matcher = org.hamcrest.CoreMatchers.is(expectedBoardNo);		
		resultMatcher = jsonPath("t.list[0].boardNo", matcher);
		resultActions.andExpect(resultMatcher);
		
		//게시글페이지그룹의 시작페이지값 t.startPage
		int expectedStartPage = 1;
		matcher = org.hamcrest.CoreMatchers.is(expectedStartPage);
		resultMatcher = jsonPath("t.startPage", matcher);
		resultActions.andExpect(resultMatcher);
	}
	
	@Test
	public void testSearch() throws Exception {
		String uri = "/search";
		MockHttpServletRequestBuilder mockRequestBuilder = MockMvcRequestBuilders.get(uri).accept(org.springframework.http.MediaType.APPLICATION_JSON);
		mockRequestBuilder.param("currentPage", "1"); // 검색어에 해당하는 글이 몇페이지에 존재하는지 테스트
		mockRequestBuilder.param("word", "1번글"); // 검색어에 해당하는 글이 몇페이지에 존재하는지 테스트
		ResultActions resultActions = mockMvc.perform(mockRequestBuilder);//요청
		resultActions.andExpect(MockMvcResultMatchers.status().isOk()); //응답상태코드200번 성공 예상
		resultActions.andExpect(MockMvcResultMatchers.jsonPath("status", is(1)));
	}
}
