package com.finalproject.service;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;

import com.finalproject.dto.cartitem.OrderResponseDTO;
import com.finalproject.entity.Address;
import com.finalproject.entity.CartItem;
import com.finalproject.entity.Category;
import com.finalproject.entity.Order;
import com.finalproject.entity.OrderItem;
import com.finalproject.entity.Product;
import com.finalproject.entity.ShoppingCart;
import com.finalproject.entity.User;
import com.finalproject.exception.ResourceNotFoundException;
import com.finalproject.exception.UserDoesNotHaveAShoppingCartException;
import com.finalproject.repository.AddressRepository;
import com.finalproject.repository.OrderItemRepository;
import com.finalproject.repository.OrderRepository;
import com.finalproject.repository.ShoppingCartRepository;
import com.finalproject.repository.UserRepository;
import com.finalproject.security.JwtAuthenticationEntryPoint;
import com.finalproject.security.JwtAuthenticationFilter;
import com.finalproject.security.JwtTokenProvider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTests {

	@Mock
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	
	@Mock
	private JwtTokenProvider jwtTokenProvider;
	
	@Mock
	private JwtAuthenticationFilter jwtAuthenticationFilter;
	
	@Mock
	private UserRepository userRepository;
	
	@Mock
	private OrderRepository orderRepository;
	
	@Mock
	private ShoppingCartRepository shoppingCartRepository;
	
	@Mock
	private OrderItemRepository orderItemRepository;
	
	@InjectMocks
	private OrderServiceImpl OrderService;
	
	private User user;
	private Category category;
	private ShoppingCart shoppingCart; 
	
	@BeforeEach
	public void setup() {
		user = new User();
		shoppingCart = new ShoppingCart();
	}
	
	@Test
	void shouldWork() {
		assertEquals(0, 0);
	}
	
	@Test
	public void GIVEN_THEN_Create_Order() {
		// given
		MockHttpServletRequest request = new MockHttpServletRequest();
		Product product = new Product();
		product.setCategory(category);
		product.setDescription("description");
		product.setId(1L);
		product.setName("product name");
		product.setPrice(10);
		Order newOrder = new Order();
		CartItem cartItem =new CartItem();
		cartItem.setCreatedDate(new Date());
		cartItem.setId(1L);
		cartItem.setProduct(product);
		cartItem.setQuantity(20);
		
		OrderItem orderItem = new OrderItem();
		
		shoppingCart.addItem(cartItem);
		
		// when
		when(jwtAuthenticationFilter.getJwtFromRequest(request)).thenReturn("string");
		when(jwtTokenProvider.validateToken("string")).thenReturn(true);
		when(userRepository.findByUsernameOrEmail(anyString(), anyString())).thenReturn(Optional.of(user));
		when(jwtTokenProvider.getUsernameFromJWT(anyString())).thenReturn("something");
		when(shoppingCartRepository.findByUser(any(User.class))).thenReturn(Optional.of(shoppingCart));
		when(orderRepository.save(any(Order.class))).thenReturn(newOrder);
		when(orderItemRepository.save(any(OrderItem.class))).thenReturn(orderItem);
		
		// then
		OrderResponseDTO orderResponseDTO = OrderService.createOrder(request);
		assertThat(orderResponseDTO).isNotNull();
		
	}
	
	
	@Test
	public void GIVEN_User_Does_Not_Have_A_Shopping_Cart_THEN_Exception() {
		// given
		MockHttpServletRequest request = new MockHttpServletRequest();
		
		
		// when
		when(jwtAuthenticationFilter.getJwtFromRequest(request)).thenReturn("string");
		when(jwtTokenProvider.validateToken("string")).thenReturn(true);
		when(userRepository.findByUsernameOrEmail(anyString(), anyString())).thenReturn(Optional.of(user));
		when(jwtTokenProvider.getUsernameFromJWT(anyString())).thenReturn("something");
		when(shoppingCartRepository.findByUser(any(User.class))).thenReturn(Optional.ofNullable(null));
		
		// then
		try {
			 OrderService.createOrder(request);
		} catch (Exception e) {
			assertTrue(e instanceof UserDoesNotHaveAShoppingCartException);
		}
		
	}

	
}
