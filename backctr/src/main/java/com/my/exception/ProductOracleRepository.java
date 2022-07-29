package com.my.exception;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.my.dto.Product;
import com.my.repository.ProductRepository;
import com.my.sql.MyConnection;

public class ProductOracleRepository implements ProductRepository {

	@Override
	public void insert(Product product) throws AddException {
		// TODO Auto-generated method stub

	}

	@Override
	// servlet이 하는 일을 가져와서 가볍게 만듬. + 재사용성 
	// 상품전체를 선택해오는 메소드임
	// product타입의 리스트자료구조를 갖는 selectAll()
	public List<Product> selectAll() throws FindException {
		// products 변수를 만들어 arryList 객체를 할당함.
		List<Product> products = new ArrayList<>();
		
		// ------- 연결 준비 ----
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null; // select 구문이기 때문에 resultSet을 씀
		// String 타입의 sql구문 문자열을 변수에 할당함 
		// prod_no 오름차순으로 정렬한 상품 테이블에 있는 모든것을 조회한다.
		String selectProductAllSQL = "SELECT * FROM product ORDER BY prod_no ASC";
		try {
			// MyConnection의 getConnection메소드를실행하여 나온 return 값을 con변수에 할당함.
			con = MyConnection.getConnection();
			// jdbc와 연결한 con 변수에 접근하여 prepareStatement 메소드를 실행함. 파라미터값으로 미리 준비한 sql 구문을 넣어줌.
			pstmt = con.prepareStatement(selectProductAllSQL);
			// rs변수에 pstmt 변수에 접근하여 executeQuery 메소드를 실행하여 반환값을 할당함.
			rs = pstmt.executeQuery(); // select 구문을 처리할 메서드 // ddl dml 구문은 executeUpdate()
			// 행을 읽을수 있을 때까지 반복을 돈다. 행을읽을 수 없다면 false를 반환함.
			while(rs.next()) { //커서를 이동시켜서 행을 이동함
				// 더이상 행을 읽을수 없을때 false값을 반환해야 함
				// ---- DB의 테이블로부터 읽은 값에 접근하여 칼럼명이 다음과 같은 것들을 가져와서 변수에 할당함.
				String prod_no = rs.getString("prod_no"); //java에서의 String oracle에서의 varchar2
				String prod_name = rs.getString("prod_name");
				int prod_price = rs.getInt("prod_price");
				// ----
				// product 타입의 p 변수에 product 객체의 생성자의 파라미터값으로 위에서 지정한 변수명을 넣음.
				Product p = new Product(prod_no, prod_name, prod_price);
				// arraylist객체를 담은 변수 products 에 add 메소드를 통해 p 객체를 추가함.
				products.add(p);	
			}
			// 만약 products의 사이즈가 0이라면 예외를 던짐
			if (products.size() == 0) {
				throw new FindException("상품이 없습니다.");
			} // 0이 아니라면 products를 리턴함.
			return products;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new FindException(e.getMessage());
		} finally {
			//연결을 열었으니 닫아준다!!
			MyConnection.close(rs, pstmt, con);
		}
		//---------------------------------------------
	}

	@Override
	// product타입의 반환값을 갖는 selectByProdNo 함수 매개변수로 String 타입의 prodno가 온다고 알려줌.
	public Product selectByProdNo(String prodNo) throws FindException {
		// -- 오라클과 연결준비
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		// -- 
		// String 타입의 selectProdNoSQL 변수에 sql 문의 문자열을 할당함. where문으로 prod_no 뒤에 파라미터값이 오기 때문에 ? 처리해줌
		String selectProdNoSQL = "SELECT * FROM product WHERE prod_no=?";
		try {
			// --- DB 와 연결준비 
			con = MyConnection.getConnection();
			pstmt = con.prepareStatement(selectProdNoSQL);
			// ---
			// ? 가 하나이기 때문에 setString의 첫번째 파라미터 값으로 1이옴(들어갈 자리를 의미) 2번째 파라미터값으로 selectProdNo 메소드의 매개변수로 들어오는 값을 넣어줌. 
			pstmt.setString(1, prodNo);
			rs = pstmt.executeQuery(); // select 구문을 처리할 메서드 // ddl dml 구문은 executeUpdate()
			if(rs.next()) { //커서를 이동시켜서 행을 이동함
				// 더이상 행을 읽을수 없을때 false값을 반환해야 함
				// sql 구문을 읽어서해당되는 컬럼명을 각 변수에 할당함.
				String prodName = rs.getString("prod_name");
				int prodPrice = rs.getInt("prod_price");
				String prodInfo = rs.getString("prod_info");
				// 날짜형식을 받는 date 타입의 변수사용
				java.sql.Date prodMfd = rs.getDate("PROD_MFD");
				// product 타입의 p 변수에 다음과 같은 매개변수를 갖는 product 객체를 생성함.
				Product p = new Product(prodNo, prodName, prodPrice, prodInfo, prodMfd);
				// p 객체를 반환함.
				return p;	
			} else {
				// 읽을 상품이 없다면 예외를 던짐.
				throw new FindException("상품이 없습니다.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new FindException(e.getMessage());
		} finally {
			MyConnection.close(rs, pstmt, con);
		}
		//---------------------------------------------
	}

	@Override
	public List<Product> selectByProdNoOrProdName(String word) throws FindException {
		
		return null;
	}

}
