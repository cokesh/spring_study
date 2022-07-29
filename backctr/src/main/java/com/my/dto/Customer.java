package com.my.dto;

public class Customer {
	// has a 관계 가질 필요도 없는 기본 DTO.. ?! 
	
	/*
	 * 
	 */
	private String pwd;
	private String name;
	private String address;
	private int status;
	private String Buildingno;
	
	public Customer() {
		super();
	}
	
	private String id;
	public Customer(String id, String pwd, String name, String address, int status, String buildingno) {
		super();
		this.id = id;
		this.pwd = pwd;
		this.name = name;
		this.address = address;
		this.status = status;
		Buildingno = buildingno;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getBuildingno() {
		return Buildingno;
	}

	public void setBuildingno(String buildingno) {
		Buildingno = buildingno;
	}

}