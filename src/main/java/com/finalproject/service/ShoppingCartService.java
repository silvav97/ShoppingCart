package com.finalproject.service;

import javax.servlet.http.HttpServletRequest;

import com.finalproject.dto.CreateAddressDTO;
import com.finalproject.dto.UpdateAddressDTO;
import com.finalproject.dto.UpdatePaymentMethodDTO;
import com.finalproject.dto.cartitem.ShoppingCartDTO;
import com.finalproject.dto.cartitem.ShoppingCartListItemsDTO;
import com.finalproject.entity.ShoppingCart;
import com.finalproject.entity.User;

public interface ShoppingCartService {

	public ShoppingCart addToShoppingCart(ShoppingCartDTO shoppingCartDTO, HttpServletRequest request);

	public ShoppingCartListItemsDTO listCartItems(HttpServletRequest request);

	public void reduceOrDeleteCartItem(Long cartItemId, HttpServletRequest request);

	public void addAddress(CreateAddressDTO createAddressDTO, HttpServletRequest request);

	public User updateCurrentAddress(UpdateAddressDTO updateAddressDTO, HttpServletRequest request);

	public void updatePaymentMethod(UpdatePaymentMethodDTO updatePaymentMethodDTO, HttpServletRequest request);

}
