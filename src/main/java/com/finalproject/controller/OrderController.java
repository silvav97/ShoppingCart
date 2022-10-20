package com.finalproject.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finalproject.common.ApiResponse;
import com.finalproject.dto.cartitem.OrderResponseDTO;
//import com.finalproject.dto.cartitem.OrderDTO;
import com.finalproject.dto.cartitem.ShoppingCartDTO;
import com.finalproject.entity.Order;
import com.finalproject.entity.ShoppingCart;
import com.finalproject.service.OrderServiceImpl;
import com.finalproject.service.ShoppingCartServiceImpl;

@RestController
@RequestMapping("api/order")
public class OrderController {
	
	@Autowired
	private ShoppingCartServiceImpl cartService;
	
	@Autowired
	private OrderServiceImpl orderService;
	
	@PostMapping
	public ResponseEntity<OrderResponseDTO> createOrder(HttpServletRequest request) {
		OrderResponseDTO orderResponse = orderService.createOrder(request);
		//return new ResponseEntity<>(new ApiResponse(true, "Added to cart"), HttpStatus.CREATED);
		return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
	}

}
