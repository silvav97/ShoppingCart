package com.finalproject.controller;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finalproject.dto.cartitem.ShoppingCartDTO;
import com.finalproject.entity.CartItem;
import com.finalproject.entity.Product;
import com.finalproject.entity.ShoppingCart;
import com.finalproject.entity.User;
import com.finalproject.service.ShoppingCartServiceImpl;

import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.CoreMatchers.is;

@WebMvcTest
public class ShoppingCartControllerTests2 {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private ShoppingCartServiceImpl shoppingCartService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private ShoppingCart shoppingCart;
	private Product product;
	private CartItem cartItem; 
	private User user;
	
	@Test
	public void testAddCartItemToShoppingCart() throws Exception {
		//given
		shoppingCart = new ShoppingCart();
		product = new Product();
		cartItem = new CartItem();
		cartItem.setId(1L);
		cartItem.setCreatedDate(new Date());
		cartItem.setProduct(product);
		cartItem.setQuantity(1);
		cartItem.setShoppingCart(shoppingCart);
		Set<CartItem> setCartItems = new HashSet<>();
		
		user = new User();
		user.setId(1L);user.setName("name");
		user.setUsername("username");
		user.setEmail("user@gmail.com");
		user.setPassword("userPassword");
		user.setCurrentAddress("userAddress");
		
		shoppingCart.setAddress(user.getCurrentAddress());
		shoppingCart.setCartItems(setCartItems);
		shoppingCart.setId(1L);
		shoppingCart.setUser(user);
		
		given(shoppingCartService.addToShoppingCart(any(ShoppingCartDTO.class), null))
			.willAnswer((invocation) -> invocation.getArgument(0));
		
		//when
		ResultActions response = mockMvc.perform(post("/api/shoppingcart")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(setCartItems)));
		
		//then
		response.andDo(print())
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.success", is(true)))
			.andExpect(jsonPath("$.message", is("Added to cart")));
		
		
		
	}
}
