package com.finalproject.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;

import com.finalproject.dto.cartitem.CartItemDTO;
import com.finalproject.dto.cartitem.OrderItemDTO;
import com.finalproject.dto.cartitem.OrderResponseDTO;
import com.finalproject.entity.CartItem;
import com.finalproject.entity.Order;
import com.finalproject.entity.OrderItem;
import com.finalproject.entity.ShoppingCart;
import com.finalproject.entity.User;
import com.finalproject.exception.ResourceNotFoundException;
import com.finalproject.exception.ShoppingCartException;
import com.finalproject.exception.UserDoesNotHaveAShoppingCartException;
import com.finalproject.repository.OrderItemRepository;
import com.finalproject.repository.OrderRepository;
import com.finalproject.repository.ShoppingCartRepository;
import com.finalproject.repository.UserRepository;
import com.finalproject.security.JwtAuthenticationFilter;
import com.finalproject.security.JwtTokenProvider;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private ProductServiceImpl productService;
	
	@Autowired
	private ShoppingCartRepository shoppingCartRepository;
	
	@Autowired
	private OrderItemRepository orderItemRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Autowired
	private UserRepository userRepository;

	public String getOrderId() {
	    Random r = new Random( System.currentTimeMillis() );
	    return String.valueOf(10000 + r.nextInt(20000));
	}
	
	// Method to get the user
	public User getTheUserFromRequest(HttpServletRequest request) {
		User user = new User();
		String username;
		String token = jwtAuthenticationFilter.getJwtFromRequest(request);
		// Validate the token
		if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
			// Get the User
			username = jwtTokenProvider.getUsernameFromJWT(token);
			user = userRepository.findByUsernameOrEmail(username, username)
					.orElseThrow(() -> new ResourceNotFoundException("User", "username or Email", 1L));
			return user;
		}
		throw new ShoppingCartException(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid token");
	}
	
	@PostMapping
	public OrderResponseDTO createOrder(HttpServletRequest request) {
		User user = getTheUserFromRequest(request);
		
		// check if shopping cart already exist ()
		ShoppingCart shoppingCart = shoppingCartRepository.findByUser(user)
				.orElseThrow(() -> new UserDoesNotHaveAShoppingCartException("Shopping Cart must exists before creating an order"));
		
		OrderResponseDTO orderResponseDTO = new OrderResponseDTO();
		
		Order newOrder = new Order();
		orderRepository.save(newOrder);
		Set<CartItem> cartItems = shoppingCart.getCartItems();
		List<OrderItemDTO> cartItemsResponse = new ArrayList<>();

		for(CartItem cartItem : cartItems) {
			OrderItem orderItem = new OrderItem();
			OrderItemDTO orderItemDTO = new OrderItemDTO();
			
			orderItem.setCreatedDate(cartItem.getCreatedDate());
			
			
			orderItem.setProduct(cartItem.getProduct());
			orderItemDTO.setProductName(cartItem.getProduct().getName());
			
			orderItem.setQuantity(cartItem.getQuantity());
			orderItemDTO.setQuantity(cartItem.getQuantity());
			cartItemsResponse.add(orderItemDTO);
			
			orderItem.setOrder(newOrder);
		
			OrderItem newOrderItem = orderItemRepository.save(orderItem);
			newOrder.addItem(newOrderItem);
		
		}
		
		newOrder.setCreatedDate(new Date().toString());
		newOrder.setOrderId(getOrderId());
		newOrder.setShippingAddress(user.getCurrentAddress());
		newOrder.setUser(user);
		
		orderResponseDTO.setCartItemsResponse(cartItemsResponse);
		orderResponseDTO.setCreatedDate(new Date().toString());
		orderResponseDTO.setCustomer(user.getName());
		orderResponseDTO.setOrderId(newOrder.getOrderId());
		orderResponseDTO.setShippingAddress(user.getCurrentAddress());

		orderRepository.save(newOrder);
		shoppingCartRepository.delete(shoppingCart);
		return orderResponseDTO;
	}

}
