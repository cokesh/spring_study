package com.my.repository;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.my.dto.Customer;
import com.my.exception.AddException;
import com.my.exception.FindException;


@Repository(value="customerOracleRepository")
public class CustomerOracleRepository implements CustomerRepository {
	
	@Autowired
	private SqlSessionFactory sessionFactory;


	@Override
	public void insert(Customer customer) throws AddException {
		SqlSession session = null;
		try {
			session = sessionFactory.openSession();
			session.insert("com.my.mapper.CustomerMapper.insert", customer);
			
		} catch(Exception e) {
			e.printStackTrace();
			throw new AddException(e.getMessage());
		} finally {
			if(session != null) {
				session.close();
			}
		}

	}

	@Override
	public Customer selectById(String id) throws FindException {
		SqlSession session = null;
		try {
			session = sessionFactory.openSession(); //Connection과 같은 역할
			Customer c = session.selectOne("com.my.mapper.CustomerMapper.selectById", id); // 2가지의 파라미터가 온다. 하나의 행만 찾는 것임 ,pool을 사용할 때는 connection 을 끊어주지 않아도 된다. 
			if (c == null) {
				throw new FindException("고객이 없습니다.");
			}
			return c;
		}catch(Exception e) {
			e.printStackTrace();
			throw new FindException(e.getMessage());
		}finally {
			if(session != null) {
				session.close();//굳이 안해줘도 된다. 이유는 풀에 반납한다는 의미인데 안해줘도 자동반납?
			}
		}
	}
}
