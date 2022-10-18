package com.finalproject.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finalproject.common.ApiResponse;
import com.finalproject.dto.CreateAddressDTO;
import com.finalproject.dto.UpdateAddressDTO;
import com.finalproject.dto.UpdatePaymentMethodDTO;
import com.finalproject.dto.cartitem.ShoppingCartDTO;
import com.finalproject.dto.cartitem.ShoppingCartListItemsDTO;
import com.finalproject.entity.ShoppingCart;
import com.finalproject.service.ShoppingCartServiceImpl;

@RestController
@RequestMapping("/api/shoppingcart")
public class ShoppingCartController {
	
	@Autowired
	private ShoppingCartServiceImpl shoppingCartService;
	
	// post cart
	@PostMapping
	public ResponseEntity<ApiResponse> addToCart(@RequestBody ShoppingCartDTO shoppingCartDTO, HttpServletRequest request) {
		ShoppingCart cartItem = shoppingCartService.addToShoppingCart(shoppingCartDTO, request);
		return new ResponseEntity<>(new ApiResponse(true, "Added to cart"), HttpStatus.CREATED);
		//return new ResponseEntity<>(cartItem, HttpStatus.CREATED);
	}
	
	// get all cart items for a user
	@GetMapping
	public ResponseEntity<ShoppingCartListItemsDTO> getCartItems(HttpServletRequest request) {
		ShoppingCartListItemsDTO shoppingCartListItemsDTO = shoppingCartService.listCartItems(request);
		return new ResponseEntity<>(shoppingCartListItemsDTO, HttpStatus.OK);
	}
	
	// reduce or delete a cart item from the shoppingCart
	@DeleteMapping("/{cartItemId}")
	public ResponseEntity<ApiResponse> reduceOrDeleteCartItem(@PathVariable("cartItemId") Long cartItemId, HttpServletRequest request) {
		shoppingCartService.reduceOrDeleteCartItem(cartItemId,request);
		return new ResponseEntity<>(new ApiResponse(true, "Item has been removed"), HttpStatus.OK);
	}
	
	
	// Add more Addresses
	@PostMapping("/address")
	public ResponseEntity<ApiResponse> addAddress(@RequestBody CreateAddressDTO createAddressDTO, HttpServletRequest request) {
		shoppingCartService.addAddress(createAddressDTO, request);
		return new ResponseEntity<>(new ApiResponse(true, "Address Added"), HttpStatus.CREATED);

	}
	
	// Update Current Address
	@PutMapping("/address")
	public ResponseEntity<ApiResponse> updateCurrentAddress(@RequestBody UpdateAddressDTO updateAddressDTO, HttpServletRequest request) {
		shoppingCartService.updateCurrentAddress(updateAddressDTO, request);
		return new ResponseEntity<>(new ApiResponse(true, "Current Address Updated"), HttpStatus.OK);
	}
	
	// Update Payment Method
	@PutMapping("/paymentmethod")
	public ResponseEntity<ApiResponse> updatePaymentMethod(@RequestBody UpdatePaymentMethodDTO updatePaymentMethodDTO, HttpServletRequest request) {
		shoppingCartService.updatePaymentMethod(updatePaymentMethodDTO, request);
		return new ResponseEntity<>(new ApiResponse(true, "Current Address Updated"), HttpStatus.OK);
	}
	
	
	

}
