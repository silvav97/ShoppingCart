package com.finalproject.dto.cartitem;

import java.util.List;

public class OrderResponseDTO {

	private String orderId;
	private String createdDate;
	private String customer;
	private String shippingAddress;
	private List<OrderItemDTO> cartItemsResponse;
	private double totalPrice;

	public OrderResponseDTO() {
		super();
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(String shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public List<OrderItemDTO> getCartItemsResponse() {
		return cartItemsResponse;
	}

	public void setCartItemsResponse(List<OrderItemDTO> cartItemsResponse) {
		this.cartItemsResponse = cartItemsResponse;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	
	

}
