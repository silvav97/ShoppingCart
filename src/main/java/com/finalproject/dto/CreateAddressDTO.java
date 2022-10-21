package com.finalproject.dto;

public class CreateAddressDTO {
	
	private Long id;
	private String newAddress;

	public CreateAddressDTO() {
		super();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNewAddress() {
		return newAddress;
	}

	public void setNewAddress(String newAddress) {
		this.newAddress = newAddress;
	}

}
