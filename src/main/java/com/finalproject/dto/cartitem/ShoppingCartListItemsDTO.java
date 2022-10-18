package com.finalproject.dto.cartitem;

import java.util.List;

public class ShoppingCartListItemsDTO {

	private List<CartItemDTO> cartItems;
	private String deliveryAddress;
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

}
