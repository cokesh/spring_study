package com.my.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.my.dto.Customer;
import com.my.exception.AddException;
import com.my.exception.FindException;
import com.my.sql.MyConnection;
@Repository(value="customerOracleRepository")
public class CustomerOracleRepository implements CustomerRepository {
	@Autowired
	@Qualifier(value="dataSource")
	private DataSource ds;
//	@Override
//	public Customer selectByIdAndPwd(String id, String pwd) throws FindException {
//
//		//DB와 연결
//		Connection con = null;
//		//SQL송신
//		PreparedStatement pstmt = null;
//		//송신결과
//		ResultSet rs = null;
//
//		try {
//			con = MyConnection.getConnection();
//			String selectIdNPwdSQL = "SELECT * FROM customer WHERE id=? AND pwd=?";
//			pstmt = con.prepareStatement(selectIdNPwdSQL);
//			pstmt.setString(1,  id);
//			pstmt.setString(2,  pwd);
//			pstmt.executeQuery();
//			rs = pstmt.executeQuery();// executequery(); = 결과값을 나타내는 함수
//			if(rs.next()) { //행이 존재하면 로그인 성공된 것 
//				return new Customer(rs.getString("id"),
//						rs.getString("pwd"),
//						rs.getString("name"),
//						rs.getString("ADDRESS"),
//						rs.getInt("STATUS"),
//						rs.getString("BUILDINGNO")
//						);
//			}
//			throw new FindException("고객이 없습니다");
//		}catch (Exception e) {
//			throw new FindException(e.getMessage());
//		}finally {
//			MyConnection.close(rs, pstmt, con);
//		}
//	}

	@Override
	public void insert(Customer customer) throws AddException {
		//DB연결
		Connection con = null;     //가입성공 아닐 시 무조건 실패
		//SQL송신
		PreparedStatement pstmt = null; //executeUPdate() -> DML이나 DDL 사용하려면 메서드 사용
		int rs = 0;  //insert일 때에는 자료형 int사용 / select일 때에는 resultSet 사용

		String result = "{\"status\":0, \"msg\": \"가입실패\"}";
		try {
			con = ds.getConnection();
			String insertSQL = "INSERT INTO customer(id,pwd,name, status, buildingno, address) VALUES (?,?,?,1,?,?)";
			pstmt = con.prepareStatement(insertSQL);
			pstmt.setString(1, customer.getId());
			pstmt.setString(2, customer.getPwd());
			pstmt.setString(3, customer.getName());
			pstmt.setString(4, customer.getAddress());
			pstmt.setString(5, customer.getBuildingno());
			pstmt.executeUpdate(); 
			result = "{\"status\": 1,  \"msg\": \"가입성공\" }";
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AddException(e.getMessage());
		} finally {
			MyConnection.close(pstmt, con);
		}
	}

	@Override
	public Customer selectById(String id) throws FindException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String selectIdDupChkSQL = "SELECT * FROM customer WHERE id = ?";
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(selectIdDupChkSQL);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return new Customer(
						rs.getString("id"),
						rs.getString("pwd"),
						rs.getString("name"),
						rs.getString("address"),
						rs.getInt("status"),
						rs.getString("buildingno")
						);
			}
			throw new FindException ("고객이 없습니다.");
		}catch(SQLException e) {
			throw new FindException(e.getMessage());
		}finally {
			MyConnection.close(rs, pstmt, con);
		}
	}
}
