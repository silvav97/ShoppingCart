package com.finalproject.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.finalproject.dto.ProductDTO;
import com.finalproject.dto.cart.AddToCartDTO;
import com.finalproject.dto.cart.CartDTO;
import com.finalproject.dto.cart.CartItemDTO;
import com.finalproject.entity.CartItem;
import com.finalproject.entity.Product;
import com.finalproject.entity.User;
import com.finalproject.exception.CartItemDoesNotBelongToUserException;
import com.finalproject.exception.ResourceNotFoundException;
import com.finalproject.exception.ShoppingCartException;
import com.finalproject.repository.CartRepository;
import com.finalproject.repository.UserRepository;
import com.finalproject.security.JwtAuthenticationFilter;
import com.finalproject.security.JwtTokenProvider;

@Service
public class CartServiceImpl implements CartService {
	
	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ProductServiceImpl productService;
	
	@Autowired
	private CartRepository cartRepository;

	// Method to get the user
	private User getTheUserFromRequest(HttpServletRequest request) {
		User user = new User();
		String username;
		String token = jwtAuthenticationFilter.getJwtFromRequest(request);		
		// Validate the token
		if(StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
			// Get the User
			username = jwtTokenProvider.getUsernameFromJWT(token);
			user = userRepository.findByUsernameOrEmail(username, username)
					.orElseThrow(() -> new ResourceNotFoundException("User", "username or Email", 1L));
			return user;
		}
		throw new ShoppingCartException(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid token")  ;
	}
	
	@Override
	public CartItem addToCart(AddToCartDTO addToCartDTO, HttpServletRequest request) {
		User user = getTheUserFromRequest(request);
		// validate if the product id is valid
		Product product = productService.getById(addToCartDTO.getProductId());
		
		List<CartItem> cartList = cartRepository.findAllByUserOrderByCreatedDateDesc(user);
		for(CartItem cartItem : cartList) {
			if(cartItem.getProduct().getId() == product.getId()) {
				// ya existia el product
				cartItem.setQuantity(cartItem.getQuantity() + 1);
				return cartRepository.save(cartItem);
			}
		}
		
		/*
		if(cartRepository.findById(product.getId()).isPresent()){
			Cart cart = cartRepository.findById(product.getId()).get();
			cart.setQuantity(cart.getQuantity() + addToCartDTO.getQuantity());
			return cartRepository.save(cart);
		}*/
		
		CartItem cart = new CartItem();
		cart.setProduct(product);
		cart.setUser(user);
		cart.setQuantity(1);
		cart.setCreatedDate(new Date());
		cart.setAddress(addToCartDTO.getDeliveryAddress());
		return cartRepository.save(cart);
	}
	
	@Override
	public CartDTO listCartItems(HttpServletRequest request) {
		User user = getTheUserFromRequest(request);
		
		List<CartItem> cartList = cartRepository.findAllByUserOrderByCreatedDateDesc(user);
		List<CartItemDTO> cartItems = new ArrayList<>();
		double totalCost = 0;
		for(CartItem cart: cartList) {
			CartItemDTO cartItemDTO = new CartItemDTO(cart);
			totalCost += cartItemDTO.getQuantity() * cart.getProduct().getPrice();
			cartItems.add(cartItemDTO);
		}
		
		CartDTO cartDTO = new CartDTO();
		cartDTO.setTotalCost(totalCost);
		cartDTO.setCartItems(cartItems);
		
		return cartDTO;
	}

	@Override
	public void reduceOrDeleteCartItem(Long cartItemId, HttpServletRequest request) {
		User user = getTheUserFromRequest(request);
		// check if the item belongs to user
		CartItem cart = cartRepository.findById(cartItemId)
				.orElseThrow(() -> new ResourceNotFoundException("Cart item", "cartItemId", cartItemId));
		
		if(!cart.getUser().equals(user)) {
			throw new CartItemDoesNotBelongToUserException("Item does not belong to this user");
		}
		
		if(cart.getQuantity() == 1) {
			cartRepository.delete(cart);
		} else {
			cart.setQuantity(cart.getQuantity()-1);
			cartRepository.save(cart);
		}
	}

}
