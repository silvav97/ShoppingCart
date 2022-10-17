package com.finalproject.service;

import javax.servlet.http.HttpServletRequest;

import com.finalproject.dto.cart.AddToCartDTO;
import com.finalproject.dto.cart.CartDTO;
import com.finalproject.entity.CartItem;

public interface CartService {

	CartItem addToCart(AddToCartDTO addToCartDTO, HttpServletRequest request);

	CartDTO listCartItems(HttpServletRequest request);

	void reduceOrDeleteCartItem(Long cartItemId, HttpServletRequest request);

}
