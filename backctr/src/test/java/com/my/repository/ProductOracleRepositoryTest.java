package com.my.repository;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.my.dto.Product;
import com.my.exception.FindException;

//스프링컨테이너(ApplicationContext)구동
@RunWith(SpringRunner.class)
//Spring 컨테이너용 XML파일 설정
@ContextConfiguration(locations={
		"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml"})

public class ProductOracleRepositoryTest {

	@Autowired
	private ProductRepository repository;
	
	@Test
	public void testSelectByProdNo() throws FindException {
		String prodNo = "C0001";
		String expectedProdName = "제주비자림";
		int expectedProdPrice = 1100;
		Product p = repository.selectByProdNo(prodNo);
		assertNotNull(p);
		assertEquals(expectedProdName, p.getProdName()); // 내가 예상한 값과 같다고 단정을 지음
		assertEquals(expectedProdPrice, p.getProdPrice());
	}
	
	@Test
	public void testSelectAll() throws FindException{
		int expectedSize = 4;
		List<Product> repo = repository.selectAll();
		assertTrue(expectedSize == repo.size());
	}

}
