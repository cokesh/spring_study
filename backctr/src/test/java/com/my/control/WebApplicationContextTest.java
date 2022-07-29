package com.my.control;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
//스프링컨테이너(ApplicationContext)구동
@RunWith(SpringRunner.class)
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml"})

//@WebAppConfiguration is a class-level annotation that is used to declare that the ApplicationContext
@WebAppConfiguration  // 컨트롤러들을 테스트하려면 이게 필요하다.
public class WebApplicationContextTest {
	@Autowired
	private WebApplicationContext context; // 컨트롤러들을 테스트하려면 이게 필요하다.
	
	private  MockMvc mockMvc;// 모의 객체 : "흉내"내는 "가짜" 모듈 not webMVC
	
	@Before 
	public void setup(){
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}
	
	@Test
	public void viewProductTest() throws Exception {
		String uri="/viewproduct";
		MockHttpServletRequestBuilder  mockRequestBuilder =
			MockMvcRequestBuilders.get(uri).accept(org.springframework.http.MediaType.APPLICATION_JSON);
		mockRequestBuilder.param("prod_no", "C0001");
		ResultActions resultActions = mockMvc.perform(mockRequestBuilder); // 요청
		resultActions.andExpect(MockMvcResultMatchers.status().isOk());// 응답상태코드 200번 성공예상
//		resultActions.andExpect(MockMvcResultMatchers.content().string("welcome")); //응답내용 welcome 예상
		resultActions.andExpect(jsonPath("status",is(1))); //json객체의 status프로퍼티값이 1임일 예상하는 코드
	}
	
	@Test
	public void loginTest() throws Exception {
		String uri="/login";
		MockHttpServletRequestBuilder mockReqBuilder = MockMvcRequestBuilders.get(uri);
		mockMvc.perform(mockReqBuilder);
	}
//	@Test
	public void boardListTest() {
		String uri="/board/list/1";
		MockHttpServletRequestBuilder mockReqBuilder = 
				MockMvcRequestBuilders.get(uri);
		try {
			ResultActions resultAction1 = 
					mockMvc.perform(mockReqBuilder);
			resultAction1.andDo(MockMvcResultHandlers.print());
			resultAction1.andExpect(status().isOk());
		} catch (Exception e) {
			fail(e.getMessage());
		}		
	}
//	@Test
//	public void boardDetailTest() {
//		String uri = "/board/detail/32";
//		MockHttpServletRequestBuilder mockReqBuilder = 
//				MockMvcRequestBuilders.get(uri);
//		try {
//			ResultActions resultAction1 = 
//					mockMvc.perform(mockReqBuilder);
//			resultAction1.andDo(MockMvcResultHandlers.print());
//		} catch (Exception e) {
//			fail(e.getMessage());
//		}	
//	}
}
