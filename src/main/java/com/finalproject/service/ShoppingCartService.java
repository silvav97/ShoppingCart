package com.finalproject.service;

import javax.servlet.http.HttpServletRequest;

import com.finalproject.dto.cartitem.ShoppingCartDTO;
import com.finalproject.dto.cartitem.ShoppingCartListItemsDTO;
import com.finalproject.entity.ShoppingCart;

public interface ShoppingCartService {

	public ShoppingCart addToShoppingCart(ShoppingCartDTO shoppingCartDTO, HttpServletRequest request);

	public ShoppingCartListItemsDTO listCartItems(HttpServletRequest request);

	public void reduceOrDeleteCartItem(Long cartItemId, HttpServletRequest request);

}
