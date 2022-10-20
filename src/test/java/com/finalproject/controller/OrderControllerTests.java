package com.finalproject.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.finalproject.dto.CreateAddressDTO;
import com.finalproject.dto.cartitem.OrderItemDTO;
import com.finalproject.dto.cartitem.OrderResponseDTO;
import com.finalproject.entity.CartItem;
import com.finalproject.entity.Category;
import com.finalproject.entity.Product;
import com.finalproject.entity.ShoppingCart;
import com.finalproject.entity.User;
import com.finalproject.repository.ShoppingCartRepository;
import com.finalproject.security.CustomUserDetailsService;
import com.finalproject.security.JwtAuthenticationEntryPoint;
import com.finalproject.security.JwtTokenProvider;
import com.finalproject.service.OrderServiceImpl;
import com.finalproject.service.ShoppingCartServiceImpl;

@WebMvcTest(OrderController.class)
public class OrderControllerTests {
	
	@MockBean
	private CustomUserDetailsService customUserDetailsService;
	
	@MockBean
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	
	@MockBean
	private JwtTokenProvider jwtTokenProvider;
	
	@MockBean
	private ShoppingCartServiceImpl shoppingCartService;
	
	@MockBean
	private OrderServiceImpl orderService;

	@Autowired
	private MockMvc mockMvc;


	private User user;
	
	@BeforeEach
	void setup() {	
		user = new User();
		user.setCurrentAddress("sebastianAddress");
		user.setEmail("sebastian@gmail.com");
		user.setId(1L);
		user.setName("sebastian");
		user.setPassword("sebastianPassword");	
	}
	
	@Test
	void shouldCreateMockMvc() {
		assertNotNull(mockMvc);
	}
	
	@Test
	@WithMockUser(username = "sebastian", roles = {"USER"}, password = "sebastianPassword")
	void shouldCreateOrder() throws Exception {
		// given
		List<OrderItemDTO> orderItems = new ArrayList<>();
		OrderItemDTO orderItemDTO = new OrderItemDTO();
		orderItemDTO.setProductName("productName");
		orderItemDTO.setQuantity(87);
		orderItems.add(orderItemDTO);
		
		OrderResponseDTO orderResponseDTO = new OrderResponseDTO();
		orderResponseDTO.setCartItemsResponse(orderItems);
		orderResponseDTO.setCreatedDate("Today");
		orderResponseDTO.setCustomer(user.getName());
		orderResponseDTO.setOrderId("123456789");
		orderResponseDTO.setShippingAddress(user.getCurrentAddress());
		
		// when
		when(orderService.createOrder(any(HttpServletRequest.class))).thenReturn(orderResponseDTO);
		
		// then
		this.mockMvc.perform(post("http://localhost:8087/api/order")
			.content("{\"newAddres\": \"sebs\"}")
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.orderId").value("123456789"))
			.andExpect(jsonPath("$.createdDate").value("Today"))
			.andExpect(jsonPath("$.customer").value("sebastian"))
			.andExpect(jsonPath("$.shippingAddress").value("sebastianAddress"))
			.andExpect(jsonPath("$.cartItemsResponse[0].productName").value("productName"))
			.andExpect(jsonPath("$.cartItemsResponse[0].quantity").value(87));

	}

}
