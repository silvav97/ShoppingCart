package com.finalproject.dto.cart;

import javax.validation.constraints.NotNull;

public class AddToCartDTO {

	private Long id;
	private @NotNull Long productId;
	private @NotNull String deliveryAddress;
	
	public AddToCartDTO() {
		super();
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	
	public String getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}
	
	
	
}
