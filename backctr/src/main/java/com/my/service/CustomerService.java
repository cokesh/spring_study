package com.my.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.my.dto.Customer;
import com.my.exception.AddException;
import com.my.exception.FindException;
import com.my.repository.CustomerOracleRepository;
import com.my.repository.CustomerRepository;

@Service(value="customerService")
public class CustomerService {
	@Autowired
	private CustomerRepository repository;
	//public CustomerService() {
		//this.repository = new CustomerOracleRepository();
	//} -> 컨테이너로 관리할것이다.
		
	public Customer login(String id, String pwd) throws FindException {
		Customer c = repository.selectById(id);
		//비밀번호가 일치하지 않아 로그인이 실패하는 경우
		if(!c.getPwd().equals(pwd)) {
			throw new FindException();
		}
		return c;
	}
	
	public Customer iddupchk(String id) throws FindException {
		Customer c = repository.selectById(id);
		return c;
	}
	
	public void signup(Customer customer) throws AddException {
		repository.insert(customer);
	}
}
