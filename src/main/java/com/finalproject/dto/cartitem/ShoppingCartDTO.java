package com.finalproject.dto.cartitem;

import javax.validation.constraints.NotNull;

public class ShoppingCartDTO {

	private Long id;
	private @NotNull Long productId;
	//private @NotNull String deliveryAddress;
	
	public ShoppingCartDTO() {
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
	
	
	
	
	
}
