package com.my.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MyConnection {
	static { // 객체생성과 상관없이 클래스 로드 시 static zone 에 올라와서 자동으로 호출됨.		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");

		} catch(ClassNotFoundException e) {
			e.printStackTrace();
		}

	}
	// 넌 스태틱 메소드는 객체생성후 사용이 가능한 당연한 이야기
	// 스태틱 메소드로 만들어서 객체를 생성하지 않아도 메서드 사용 가능
	public static Connection getConnection() throws SQLException {
		Connection con = null;
		// m1 맥북 페러렐즈의 윈도우 호스트주소를 입력
		String url = "jdbc:oracle:thin:@10.211.55.3:1521:xe";
		String user = "hr";
		String password = "hr";

		con = DriverManager.getConnection(url, user, password);
		return con;

	}

	public static void close(ResultSet rs, Statement stmt, Connection con) {
		// db와의 연결해제
		if (rs!=null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(stmt!=null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(con!=null) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}
	
	public static void close(Statement stmt, Connection con) {
		close(null, stmt, con);
	}

}
