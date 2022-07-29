package com.my.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.my.dto.Product;
import com.my.exception.AddException;
import com.my.exception.FindException;
import com.my.sql.MyConnection;

@Repository(value="productRepository")
public class ProductOracleRepository implements ProductRepository { 
//퍼블릭 타입의 ProductRepository라는 인터페이스를 가진 ProductOracleRepository 클래스 
	@Autowired
	@Qualifier(value="dataSource")
	private DataSource ds;
	
//	public ProductOracleRepository() {}
//	
//	public ProductOracleRepository(DataSource ds) {
//		this.ds = ds;
//	}
	
	@Override 
	public void insert(Product product) throws AddException {  
		// TODO Auto-generated method stub

	}


	@Override
	// List를 참조하는 Product 타입의 반환값을 갖는 selectAll메서드
	public List<Product> selectAll() throws FindException { 
		//DB쪽 자료
		// List<Map<String,Object>> sample = new ArrayList<>();
		List<Product> products = new ArrayList<>();  
		//List참조하는 Product타입의 변수 product에 new 연산자로 ArrayList객체 생성
		//---Oracle DB랑 연결을 준비
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		//---
		//String타입의 변수 select~에 sql구문(SELECT * FROM product ORDER BY prod_no ASC대입
		String selectProductAllSQL = "SELECT * FROM product ORDER BY prod_no ASC";

		try {
			//con 변수에 Myconnection의 getConnection 메서드 호출
//			con = MyConnection.getConnection();
			con = ds.getConnection();
			//pstmt라는 변수에 con
			pstmt = con.prepareStatement(selectProductAllSQL);
			//pstmt.setString(1, ~~);
			rs = pstmt.executeQuery(); //데이터베이스에 저장되어있는 값이 rs
			
			while(rs.next()) {
				//String  타입의 변수 prod_no 에 result타입의 변수
				String prod_no = rs.getString("prod_no");
				String prod_name = rs.getString("prod_name");
				int prod_price = rs.getInt("prod_price");
				//    		  Map<String,Object> map1 = new HashMap<>();
				//MAP은 모든 class의 다목적 가방(value값 다양하게 설정해서 사용할 수 있다)
				//상품정보만 들어가게 하고 싶으면 class로 처리(class도 하나의 자료구조)
				//    		  map1.put("prod_no", prod_no);     
				//    		  map1.put("prod_name", prod_name);
				//    		  map1.put("prod_price",prod_price);
				Product p = new Product(prod_no, prod_name, prod_price);

				//    		  sample.add(map1);	
				products.add(p);
			}
			if(products.size() == 0) {
				throw new FindException("상품이 없습니다");
			}
			return products;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new FindException(e.getMessage());
		} finally {
			MyConnection.close(rs, pstmt, con);
		}
		//		return null;
	}

	@Override
	public Product selectByProdNo(String prodNo) throws FindException {
		Connection  con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String selectProdNoSQL = "SELECT * FROM product WHERE prod_no=?"; //최대 1개행 최소 0개행을 검색해옴
		try {
//			con = MyConnection.getConnection();
			con = ds.getConnection();
			pstmt = con.prepareStatement(selectProdNoSQL);
			pstmt.setString(1, prodNo);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				String prodName = rs.getString("prod_name");
				int prodPrice = rs.getInt("prod_price");
				String prodInfo = rs.getString("PROD_INFO"); //오라클에서는 컬럼명 대소문자 관계 X
				java.sql.Date prodMfd = rs.getDate("PROD_MFD");
				Product p = new Product(prodNo,
						prodName,
						prodPrice,
						prodInfo,
						prodMfd);
				return p;
			} else {
				throw new FindException("상품이 없습니다");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new FindException(e.getMessage());
		} finally {
			MyConnection.close(rs, pstmt, con);
		}
	}

	@Override
	public List<Product> selectByProdNoOrProdName(String word) throws FindException {
		// TODO Auto-generated method stub
		return null;
	}

}
