package com.finalproject.dto.cartitem;

import com.finalproject.dto.ProductDTO;

public class OrderItemDTO {

	private String productName;
	private Integer quantity;

	public OrderItemDTO() {
		super();
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

}
