package com.finalproject.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "address")
public class Address {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String userAddres;

	public Address(Long id, String userAddres) {
		super();
		this.id = id;
		this.userAddres = userAddres;
	}

	public Address() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserAddres() {
		return userAddres;
	}

	public void setUserAddres(String userAddres) {
		this.userAddres = userAddres;
	}

}
