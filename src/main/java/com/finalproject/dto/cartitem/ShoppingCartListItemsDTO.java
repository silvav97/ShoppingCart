package com.finalproject.dto.cartitem;

import java.util.List;

public class ShoppingCartListItemsDTO {
	
	private String userName;
	private String userEmail;
	private String deliveryAddress;
	private List<CartItemDTO> cartItems;
	private double totalCost;

	public ShoppingCartListItemsDTO() {
	}

	public List<CartItemDTO> getCartItems() {
		return cartItems;
	}

	public void setCartItems(List<CartItemDTO> cartItems) {
		this.cartItems = cartItems;
	}

	public double getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(double totalCost) {
		this.totalCost = totalCost;
	}

	public String getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	
	

}
