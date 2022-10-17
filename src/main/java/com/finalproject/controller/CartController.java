package com.finalproject.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finalproject.common.ApiResponse;
import com.finalproject.dto.cart.AddToCartDTO;
import com.finalproject.dto.cart.CartDTO;
import com.finalproject.service.CartServiceImpl;

@RestController
@RequestMapping("/api/cart")
public class CartController {

	@Autowired
	private CartServiceImpl cartService;

	
	// post cart
	@PostMapping("/add")
	public ResponseEntity<ApiResponse> addToCart(@RequestBody AddToCartDTO addToCartDTO, HttpServletRequest request) {
		cartService.addToCart(addToCartDTO, request);
		return new ResponseEntity<>(new ApiResponse(true, "Added to cart"), HttpStatus.CREATED);
	}
	
	// get all cart items for a user
	@GetMapping("/")
	public ResponseEntity<CartDTO> getCartItems(HttpServletRequest request) {
		CartDTO cartDTO = cartService.listCartItems(request);
		return new ResponseEntity<>(cartDTO, HttpStatus.OK);
	}
	
	
	// delete a cat item for a user
	@DeleteMapping("/{cartItemId}")
	public ResponseEntity<ApiResponse> deleteCartItem(@PathVariable("cartItemId") Long cartItemId, HttpServletRequest request) {
		cartService.reduceOrDeleteCartItem(cartItemId,request);
		return new ResponseEntity<>(new ApiResponse(true, "Item has been removed"), HttpStatus.OK);
	}

	
	
	
	
	
	
	
	
	
	
}
